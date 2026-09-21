import { DarsNizamiBookItem } from '../types';

/**
 * Universally triggers immediate, direct file download of the PDF
 * without navigating away or redirecting to an intermediate page.
 */
export const downloadDirectPdf = (pdfUrl: string, bookTitle?: string) => {
  if (!pdfUrl) return;

  try {
    const cleanName = (bookTitle || 'Islamic_Book')
      .replace(/[\\/:*?"<>|#]/g, '_')
      .trim();
    const fileName = cleanName.toLowerCase().endsWith('.pdf') ? cleanName : `${cleanName}.pdf`;

    const link = document.createElement('a');
    link.href = pdfUrl;
    link.setAttribute('download', fileName);
    link.setAttribute('target', '_blank');
    link.setAttribute('rel', 'noopener noreferrer');
    document.body.appendChild(link);
    link.click();
    setTimeout(() => {
      if (document.body.contains(link)) {
        document.body.removeChild(link);
      }
    }, 200);
  } catch (err) {
    console.warn('Direct download error, opening link:', err);
    window.open(pdfUrl, '_blank', 'noopener,noreferrer');
  }
};

/**
 * Returns the URL for opening the dedicated PDF Reader page
 */
export const getReaderUrl = (book: DarsNizamiBookItem | { pdfUrl: string; nameUrdu?: string; name?: string; author?: string; category?: string; classLevel?: string; classNameUrdu?: string; edition?: string }) => {
  const params = new URLSearchParams();
  params.set('url', book.pdfUrl);
  if (book.nameUrdu || book.name) {
    params.set('title', book.nameUrdu || book.name);
  }
  if (book.name) {
    params.set('name', book.name);
  }
  if (book.author) {
    params.set('author', book.author);
  }
  if (book.category) {
    params.set('category', book.category);
  }
  if (book.classNameUrdu || book.classLevel) {
    params.set('class', book.classNameUrdu || book.classLevel || '');
  }
  if (book.edition) {
    params.set('edition', book.edition);
  }

  return `/reader?${params.toString()}`;
};

/**
 * Opens the dedicated PDF Reader page in a clean new tab
 */
export const openPdfReader = (book: DarsNizamiBookItem | { pdfUrl: string; nameUrdu?: string; name?: string; author?: string; category?: string; classLevel?: string; classNameUrdu?: string; edition?: string }) => {
  const url = getReaderUrl(book);
  window.open(url, '_blank', 'noopener,noreferrer');
};
