import * as pdfjsLib from 'pdfjs-dist';

// Configure pdfjs worker if in browser
if (typeof window !== 'undefined') {
  try {
    // Use worker from unpkg/cdnjs or Vite worker bundled
    pdfjsLib.GlobalWorkerOptions.workerSrc = `https://cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjsLib.version}/pdf.worker.min.mjs`;
  } catch (e) {
    console.warn('PDF.js worker initialization notice:', e);
  }
}

// In-memory runtime cache for instantaneous lookups
const memoryThumbnailCache = new Map<string, string>();

// IndexedDB configuration
const DB_NAME = 'baitul_ilm_pdf_cache_v2';
const STORE_NAME = 'pdf_thumbnails';
const DB_VERSION = 1;

let dbPromise: Promise<IDBDatabase> | null = null;

function getIDB(): Promise<IDBDatabase> {
  if (typeof window === 'undefined' || !window.indexedDB) {
    return Promise.reject(new Error('IndexedDB not supported'));
  }
  if (!dbPromise) {
    dbPromise = new Promise((resolve, reject) => {
      try {
        const req = window.indexedDB.open(DB_NAME, DB_VERSION);
        req.onupgradeneeded = () => {
          const db = req.result;
          if (!db.objectStoreNames.contains(STORE_NAME)) {
            db.createObjectStore(STORE_NAME, { keyPath: 'key' });
          }
        };
        req.onsuccess = () => resolve(req.result);
        req.onerror = () => reject(req.error);
      } catch (err) {
        reject(err);
      }
    });
  }
  return dbPromise;
}

async function getCachedFromIDB(key: string): Promise<string | null> {
  try {
    const db = await getIDB();
    return new Promise((resolve) => {
      const tx = db.transaction(STORE_NAME, 'readonly');
      const store = tx.objectStore(STORE_NAME);
      const req = store.get(key);
      req.onsuccess = () => {
        if (req.result && req.result.dataUrl) {
          resolve(req.result.dataUrl);
        } else {
          resolve(null);
        }
      };
      req.onerror = () => resolve(null);
    });
  } catch {
    return null;
  }
}

async function saveToIDB(key: string, dataUrl: string): Promise<void> {
  try {
    const db = await getIDB();
    const tx = db.transaction(STORE_NAME, 'readwrite');
    const store = tx.objectStore(STORE_NAME);
    store.put({ key, dataUrl, timestamp: Date.now() });
  } catch {
    // Ignore storage quota or IDB write errors gracefully
  }
}

// Track in-flight rendering promises to prevent duplicate fetches
const inFlightRequests = new Map<string, Promise<string | null>>();

export const PdfThumbnailHelper = {
  /**
   * Asynchronously generates or retrieves a cached high-quality thumbnail of the first page of a PDF.
   * Keyed by bookId or pdfUrl.
   */
  async getThumbnail(pdfUrl: string, bookId: string | number): Promise<string | null> {
    if (!pdfUrl) return null;

    const cacheKey = `thumb_${bookId || ''}_${pdfUrl}`;

    // 1. Check in-memory fast cache
    if (memoryThumbnailCache.has(cacheKey)) {
      return memoryThumbnailCache.get(cacheKey)!;
    }

    // 2. Check IndexedDB persistent cache
    const idbCached = await getCachedFromIDB(cacheKey);
    if (idbCached) {
      memoryThumbnailCache.set(cacheKey, idbCached);
      return idbCached;
    }

    // 3. Prevent duplicate concurrent downloads for the same book
    if (inFlightRequests.has(cacheKey)) {
      return inFlightRequests.get(cacheKey)!;
    }

    // 4. Render page 1 using PDF.js with a strict timeout to avoid stalling
    const renderPromise = (async () => {
      try {
        // Setup timeout promise (6 seconds timeout)
        const timeoutPromise = new Promise<null>((resolve) => {
          setTimeout(() => resolve(null), 6000);
        });

        const loadDocPromise = (async () => {
          const loadingTask = pdfjsLib.getDocument({
            url: pdfUrl,
            // Range request config to only download initial header/page 1 bytes
            rangeChunkSize: 65536,
            disableAutoFetch: true,
            disableStream: false,
          });

          const pdf = await loadingTask.promise;
          const page = await pdf.getPage(1);

          // Render at crisp resolution
          const defaultViewport = page.getViewport({ scale: 1.0 });
          const targetWidth = 320; // optimal crisp thumbnail width
          const scale = targetWidth / defaultViewport.width;
          const viewport = page.getViewport({ scale });

          const canvas = document.createElement('canvas');
          canvas.width = viewport.width;
          canvas.height = viewport.height;
          const ctx = canvas.getContext('2d', { alpha: false });

          if (!ctx) return null;

          await page.render({
            canvasContext: ctx,
            viewport,
            canvas,
          } as any).promise;

          const dataUrl = canvas.toDataURL('image/jpeg', 0.82);

          // Store in caches
          memoryThumbnailCache.set(cacheKey, dataUrl);
          await saveToIDB(cacheKey, dataUrl);

          return dataUrl;
        })();

        const result = await Promise.race([loadDocPromise, timeoutPromise]);
        return result;
      } catch (err) {
        // PDF load failed (CORS, 404, or network restrictions) -> fallback will be used smoothly
        return null;
      } finally {
        inFlightRequests.delete(cacheKey);
      }
    })();

    inFlightRequests.set(cacheKey, renderPromise);
    return renderPromise;
  },

  /**
   * Check if thumbnail is already cached synchronously in memory
   */
  getThumbnailSync(pdfUrl: string, bookId: string | number): string | null {
    const cacheKey = `thumb_${bookId || ''}_${pdfUrl}`;
    return memoryThumbnailCache.get(cacheKey) || null;
  },

  /**
   * Helper to clear memory cache if needed without destroying persistent DB
   */
  clearMemoryCache(): void {
    memoryThumbnailCache.clear();
  }
};
