import React, { useState, useEffect, useMemo } from 'react';
import { DarsNizamiBookItem } from '../../types';
import { getAuthenticBookCover } from '../../data/bookCoverRegistry';
import { PdfThumbnailHelper } from '../../utils/pdfThumbnailHelper';

export interface IslamicBookCoverProps {
  bookId?: string | number;
  title?: string;
  titleUrdu?: string;
  author?: string;
  category?: string;
  year?: string;
  classNameUrdu?: string;
  typeUrdu?: string;
  pdfUrl?: string;
  coverUrl?: string;
  book?: DarsNizamiBookItem;
  size?: 'sm' | 'md' | 'lg' | 'xl';
  className?: string;
  onClick?: () => void;
}

// 16 Distinct Authentic Islamic Leather Binding Palettes
interface Palette {
  name: string;
  bg: string;
  accent: string;
  border: string;
  innerBorder: string;
  titleGrad: string;
  ribbon: string;
  patternColor: string;
  categoryUrduDefault: string;
  publisherDefault: string;
}

const ISLAMIC_PALETTES: Palette[] = [
  // 0. Sacred Emerald Green (زمردی سبز)
  {
    name: 'Emerald Green',
    bg: 'from-[#03361f] via-[#022314] to-[#01120a]',
    accent: '#fbbf24',
    border: 'border-amber-400/90',
    innerBorder: 'border-amber-400/50',
    titleGrad: 'from-amber-100 via-amber-200 to-amber-400',
    ribbon: 'bg-emerald-500',
    patternColor: 'text-amber-400',
    categoryUrduDefault: 'فقہ و اصول',
    publisherDefault: 'مکتبۃ البشریٰ کراچی',
  },
  // 1. Royal Sapphire Blue (شاہی نیلمی نیلا)
  {
    name: 'Sapphire Blue',
    bg: 'from-[#092b4c] via-[#051a30] to-[#020d18]',
    accent: '#38bdf8',
    border: 'border-sky-300/90',
    innerBorder: 'border-sky-400/40',
    titleGrad: 'from-sky-100 via-amber-100 to-amber-300',
    ribbon: 'bg-sky-500',
    patternColor: 'text-sky-300',
    categoryUrduDefault: 'علمِ نحو و صرف',
    publisherDefault: 'مکتبۃ البشریٰ کراچی',
  },
  // 2. Royal Burgundy / Maroon (شاہی یاقوتی سرخ)
  {
    name: 'Royal Maroon',
    bg: 'from-[#420914] via-[#2d050c] to-[#160205]',
    accent: '#f59e0b',
    border: 'border-amber-400/90',
    innerBorder: 'border-amber-400/40',
    titleGrad: 'from-amber-100 via-amber-200 to-amber-400',
    ribbon: 'bg-rose-600',
    patternColor: 'text-amber-400',
    categoryUrduDefault: 'علمِ حدیث شریف',
    publisherDefault: 'دار الکتب العلمیہ',
  },
  // 3. Persian Amber Gold (فارسی سنہری عنبری)
  {
    name: 'Amber Bronze',
    bg: 'from-[#3a2206] via-[#261603] to-[#120a01]',
    accent: '#fde047',
    border: 'border-amber-300/90',
    innerBorder: 'border-amber-400/50',
    titleGrad: 'from-yellow-100 via-amber-200 to-amber-400',
    ribbon: 'bg-amber-500',
    patternColor: 'text-amber-300',
    categoryUrduDefault: 'عربی ادب و بلاغت',
    publisherDefault: 'قدیمی کتب خانہ کراچی',
  },
  // 4. Imperial Charcoal Black (شاہی آبنوسی سیاہ)
  {
    name: 'Imperial Charcoal',
    bg: 'from-[#1c1917] via-[#12100e] to-[#080706]',
    accent: '#fbbf24',
    border: 'border-amber-400/90',
    innerBorder: 'border-amber-400/45',
    titleGrad: 'from-amber-50 via-amber-200 to-amber-400',
    ribbon: 'bg-emerald-600',
    patternColor: 'text-amber-400',
    categoryUrduDefault: 'علمِ تفسیر و علوم القرآن',
    publisherDefault: 'مکتبۃ البشریٰ کراچی',
  },
  // 5. Deep Peacock Teal (فیروزی طاؤسی)
  {
    name: 'Peacock Teal',
    bg: 'from-[#063b39] via-[#032423] to-[#01100f]',
    accent: '#2dd4bf',
    border: 'border-teal-300/90',
    innerBorder: 'border-teal-400/40',
    titleGrad: 'from-teal-100 via-amber-100 to-amber-300',
    ribbon: 'bg-teal-500',
    patternColor: 'text-teal-300',
    categoryUrduDefault: 'اصولِ فقہ و قواعد',
    publisherDefault: 'مکتبہ امدادیہ ملتان',
  },
  // 6. Deep Amethyst Purple (شاہی ارغوانی جامنی)
  {
    name: 'Amethyst Purple',
    bg: 'from-[#2e0854] via-[#1c0434] to-[#0c0117]',
    accent: '#c084fc',
    border: 'border-purple-300/90',
    innerBorder: 'border-purple-400/40',
    titleGrad: 'from-purple-100 via-amber-100 to-amber-300',
    ribbon: 'bg-purple-600',
    patternColor: 'text-purple-300',
    categoryUrduDefault: 'علمِ منطق و فلسفہ',
    publisherDefault: 'مکتبہ رحمانیہ لاہور',
  },
  // 7. Antique Terracotta Rust (قدیمی عقیقی)
  {
    name: 'Terracotta Rust',
    bg: 'from-[#421706] via-[#2a0e03] to-[#140601]',
    accent: '#fb923c',
    border: 'border-amber-400/80',
    innerBorder: 'border-orange-400/40',
    titleGrad: 'from-orange-100 via-amber-200 to-amber-400',
    ribbon: 'bg-orange-600',
    patternColor: 'text-orange-300',
    categoryUrduDefault: 'تاریخ و سیرت',
    publisherDefault: 'دار الاشاعت کراچی',
  },
  // 8. Deep Lapis Lazuli (لاجوردی نیلگوں)
  {
    name: 'Lapis Lazuli',
    bg: 'from-[#0c1f4a] via-[#071330] to-[#020817]',
    accent: '#60a5fa',
    border: 'border-blue-300/90',
    innerBorder: 'border-blue-400/40',
    titleGrad: 'from-blue-100 via-amber-100 to-amber-300',
    ribbon: 'bg-blue-600',
    patternColor: 'text-blue-300',
    categoryUrduDefault: 'شروحاتِ حدیث',
    publisherDefault: 'دار الکتب العلمیہ',
  },
  // 9. Crimson Damask (دمشقی گلابی سرخ)
  {
    name: 'Crimson Damask',
    bg: 'from-[#4c0519] via-[#31020f] to-[#190107]',
    accent: '#fb7185',
    border: 'border-rose-300/90',
    innerBorder: 'border-rose-400/40',
    titleGrad: 'from-rose-100 via-amber-100 to-amber-300',
    ribbon: 'bg-rose-500',
    patternColor: 'text-rose-300',
    categoryUrduDefault: 'سیرت النبی ﷺ',
    publisherDefault: 'مکتبہ دار العلوم کراچی',
  },
  // 10. Forest Olive Pine (زیتونی جنگلی)
  {
    name: 'Forest Olive',
    bg: 'from-[#14301a] via-[#0c1e10] to-[#050e07]',
    accent: '#a3e635',
    border: 'border-lime-400/80',
    innerBorder: 'border-lime-400/40',
    titleGrad: 'from-lime-100 via-amber-100 to-amber-300',
    ribbon: 'bg-lime-600',
    patternColor: 'text-lime-300',
    categoryUrduDefault: 'فتاویٰ و فقہی مسائل',
    publisherDefault: 'دار الافتاء دار العلوم کراچی',
  },
  // 11. Deep Royal Indigo (شاہی انڈیکو)
  {
    name: 'Royal Indigo',
    bg: 'from-[#1e1b4b] via-[#121033] to-[#08061a]',
    accent: '#a78bfa',
    border: 'border-indigo-300/90',
    innerBorder: 'border-indigo-400/40',
    titleGrad: 'from-indigo-100 via-amber-100 to-amber-300',
    ribbon: 'bg-indigo-600',
    patternColor: 'text-indigo-300',
    categoryUrduDefault: 'علمِ کلام و عقائد',
    publisherDefault: 'مکتبۃ البشریٰ کراچی',
  },
  // 12. Antique Umber Coffee (قدیمی قہوہ ای)
  {
    name: 'Antique Umber',
    bg: 'from-[#2e1d11] via-[#1b1008] to-[#0b0603]',
    accent: '#fcd34d',
    border: 'border-amber-400/90',
    innerBorder: 'border-amber-500/40',
    titleGrad: 'from-amber-100 via-amber-200 to-amber-400',
    ribbon: 'bg-amber-600',
    patternColor: 'text-amber-400',
    categoryUrduDefault: 'لغات و معاجم',
    publisherDefault: 'قدیمی کتب خانہ کراچی',
  },
  // 13. Midnight Navy Cobalt (نیم شبی کوبالٹ)
  {
    name: 'Midnight Cobalt',
    bg: 'from-[#0f1d38] via-[#091124] to-[#030712]',
    accent: '#93c5fd',
    border: 'border-sky-300/90',
    innerBorder: 'border-sky-400/40',
    titleGrad: 'from-sky-100 via-amber-100 to-amber-300',
    ribbon: 'bg-sky-600',
    patternColor: 'text-sky-300',
    categoryUrduDefault: 'تجوید للعلماء',
    publisherDefault: 'مکتبۃ البشریٰ کراچی',
  },
  // 14. Ruby Velvet Wine (یاقوتی مخملی)
  {
    name: 'Ruby Wine',
    bg: 'from-[#3b0824] via-[#240416] to-[#12010a]',
    accent: '#f472b6',
    border: 'border-pink-300/90',
    innerBorder: 'border-pink-400/40',
    titleGrad: 'from-pink-100 via-amber-100 to-amber-300',
    ribbon: 'bg-pink-600',
    patternColor: 'text-pink-300',
    categoryUrduDefault: 'تجوید للحفاظ',
    publisherDefault: 'مکتبہ امدادیہ ملتان',
  },
  // 15. Emerald Jade Gilded (یشم زمرد)
  {
    name: 'Emerald Jade',
    bg: 'from-[#064e3b] via-[#032e23] to-[#01140e]',
    accent: '#fde68a',
    border: 'border-emerald-300/90',
    innerBorder: 'border-amber-400/40',
    titleGrad: 'from-amber-100 via-amber-200 to-amber-400',
    ribbon: 'bg-emerald-600',
    patternColor: 'text-amber-300',
    categoryUrduDefault: 'حواشی و شروحات',
    publisherDefault: 'مکتبۃ البشریٰ کراچی',
  },
];

// Helper to compute deterministic hash code from any string/ID
function getBookHash(id: string | number, name: string): number {
  const str = `${id || ''}_${name || ''}`;
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = (hash << 5) - hash + str.charCodeAt(i);
    hash |= 0;
  }
  return Math.abs(hash);
}

// 6 Vector Geometric Islamic Accent Motifs
const IslamicMotif: React.FC<{ patternIndex: number; className?: string }> = ({ patternIndex, className = 'w-3.5 h-3.5' }) => {
  const mod = patternIndex % 6;
  if (mod === 0) {
    // 8-Pointed Islamic Star (Khatim)
    return (
      <svg viewBox="0 0 24 24" fill="currentColor" className={className}>
        <path d="M12 2l2.4 4.8 5.3.8-3.8 3.7.9 5.3-4.8-2.5-4.8 2.5.9-5.3-3.8-3.7 5.3-.8L12 2z" />
      </svg>
    );
  }
  if (mod === 1) {
    // Islamic Mihrab Arch
    return (
      <svg viewBox="0 0 24 24" fill="currentColor" className={className}>
        <path d="M12 2C8 4 6 7 6 11v11h12V11c0-4-2-7-6-9zm0 3c2.5 1.5 4 4 4 6v8H8v-8c0-2 1.5-4.5 4-6z" />
      </svg>
    );
  }
  if (mod === 2) {
    // Arabesque Rosette
    return (
      <svg viewBox="0 0 24 24" fill="currentColor" className={className}>
        <circle cx="12" cy="12" r="3" />
        <path d="M12 2a4 4 0 0 0-4 4c0 3 4 6 4 6s4-3 4-6a4 4 0 0 0-4-4zm0 20a4 4 0 0 0 4-4c0-3-4-6-4-6s-4 3-4 6a4 4 0 0 0 4 4zm-10-10a4 4 0 0 0 4 4c3 0 6-4 6-4s-3-4-6-4a4 4 0 0 0-4 4zm20 0a4 4 0 0 0-4-4c-3 0-6 4-6 4s3 4 6 4a4 4 0 0 0 4-4z" />
      </svg>
    );
  }
  if (mod === 3) {
    // Calligraphic Seal
    return (
      <svg viewBox="0 0 24 24" fill="currentColor" className={className}>
        <path d="M12 3l3 3-3 3-3-3 3-3zm0 12l3 3-3 3-3-3 3-3zm9-6l-3 3-3-3 3-3 3 3zM6 9L3 12l3 3 3-3-3-3z" />
      </svg>
    );
  }
  if (mod === 4) {
    // Interlocking Islamic Geometric Knot
    return (
      <svg viewBox="0 0 24 24" fill="currentColor" className={className}>
        <path d="M12 2L2 12l10 10 10-10L12 2zm0 3.8L18.2 12 12 18.2 5.8 12 12 5.8z" />
      </svg>
    );
  }
  // Gilded Dome & Crescent
  return (
    <svg viewBox="0 0 24 24" fill="currentColor" className={className}>
      <path d="M12 2a1 1 0 0 1 1 1v1.1a7 7 0 0 1 6 6.9v10H5V11a7 7 0 0 1 6-6.9V3a1 1 0 0 1 1-1zm0 4.2A5 5 0 0 0 7 11v8h10v-8a5 5 0 0 0-5-4.8z" />
    </svg>
  );
};

export const IslamicBookCover: React.FC<IslamicBookCoverProps> = ({
  bookId,
  title,
  titleUrdu,
  author,
  category,
  year,
  classNameUrdu,
  typeUrdu,
  pdfUrl,
  coverUrl,
  book,
  size = 'md',
  className = '',
  onClick,
}) => {
  // Resolve parameters from either props or book object
  const resolvedId = bookId ?? book?.id ?? '';
  const resolvedTitle = title ?? book?.name ?? '';
  const resolvedTitleUrdu = titleUrdu ?? book?.nameUrdu ?? resolvedTitle;
  const resolvedAuthor = author ?? book?.author ?? '';
  const resolvedCategory = category ?? book?.category ?? '';
  const resolvedClassUrdu = classNameUrdu ?? book?.classNameUrdu ?? year ?? '';
  const resolvedTypeUrdu = typeUrdu ?? book?.typeUrdu ?? book?.type ?? 'درسی کتاب';
  const resolvedPdfUrl = pdfUrl ?? book?.pdfUrl ?? '';
  const resolvedCustomCover = coverUrl ?? book?.coverUrl;

  const [pdfThumbnailUrl, setPdfThumbnailUrl] = useState<string | null>(null);
  const [imageError, setImageError] = useState(false);
  const [imageLoaded, setImageLoaded] = useState(false);

  // 1. Resolve authentic registry cover URL if available
  const authenticCoverUrl = useMemo(() => {
    return getAuthenticBookCover(
      String(resolvedId),
      resolvedTitle,
      resolvedTitleUrdu,
      resolvedPdfUrl,
      resolvedCustomCover
    );
  }, [resolvedId, resolvedTitle, resolvedTitleUrdu, resolvedPdfUrl, resolvedCustomCover]);

  // 2. Asynchronously load PDF Page 1 Thumbnail using PdfThumbnailHelper
  useEffect(() => {
    let isMounted = true;

    // Reset error state on URL change
    setImageError(false);
    setImageLoaded(false);

    if (authenticCoverUrl) {
      setPdfThumbnailUrl(authenticCoverUrl);
      return;
    }

    if (resolvedPdfUrl) {
      // Check sync cache first
      const cached = PdfThumbnailHelper.getThumbnailSync(resolvedPdfUrl, resolvedId);
      if (cached) {
        setPdfThumbnailUrl(cached);
        return;
      }

      // Asynchronously fetch and render
      PdfThumbnailHelper.getThumbnail(resolvedPdfUrl, resolvedId)
        .then((thumb) => {
          if (isMounted && thumb) {
            setPdfThumbnailUrl(thumb);
          }
        })
        .catch(() => {
          // Graceful fallback to deterministic Islamic cover
        });
    }

    return () => {
      isMounted = false;
    };
  }, [resolvedPdfUrl, resolvedId, authenticCoverUrl]);

  // 3. Deterministic Theme Palette Calculation based on bookId / seed hash
  const hash = useMemo(() => {
    return getBookHash(resolvedId, resolvedTitleUrdu || resolvedTitle);
  }, [resolvedId, resolvedTitleUrdu, resolvedTitle]);

  const palette = useMemo(() => {
    // Also check explicit category overrides if desired
    const cat = resolvedCategory.toLowerCase();
    const nameU = resolvedTitleUrdu.toLowerCase();

    if (cat.includes('quran') || cat.includes('قرآن') || nameU.includes('قرآن')) {
      return ISLAMIC_PALETTES[0]; // Emerald
    }
    if (cat.includes('hadith') || cat.includes('حدیث')) {
      return ISLAMIC_PALETTES[2]; // Royal Maroon
    }
    if (cat.includes('tafseer') || cat.includes('تفسیر')) {
      return ISLAMIC_PALETTES[4]; // Charcoal Black
    }
    if (cat.includes('nahw') || cat.includes('sarf') || cat.includes('نحو') || cat.includes('صرف')) {
      return ISLAMIC_PALETTES[1]; // Sapphire Blue
    }
    if (cat.includes('mantiq') || cat.includes('منطق')) {
      return ISLAMIC_PALETTES[6]; // Amethyst Purple
    }
    if (cat.includes('adab') || cat.includes('balaghat') || cat.includes('بلاغت') || cat.includes('ادب')) {
      return ISLAMIC_PALETTES[3]; // Amber Gold
    }
    if (cat.includes('tareekh') || cat.includes('تاریخ') || cat.includes('سیرت')) {
      return ISLAMIC_PALETTES[7]; // Terracotta Rust
    }
    if (cat.includes('lughat') || cat.includes('لغات')) {
      return ISLAMIC_PALETTES[12]; // Antique Umber
    }
    if (cat.includes('fatawa') || cat.includes('فتاویٰ')) {
      return ISLAMIC_PALETTES[10]; // Forest Olive
    }

    // Default to unique deterministic palette using bookId hash
    return ISLAMIC_PALETTES[hash % ISLAMIC_PALETTES.length];
  }, [resolvedCategory, resolvedTitleUrdu, hash]);

  // Size dimensions
  const sizeClasses = {
    sm: 'w-20 h-28 text-[8px]',
    md: 'w-28 sm:w-32 h-40 sm:h-44 text-[10px]',
    lg: 'w-48 sm:w-56 h-68 sm:h-76 text-xs',
    xl: 'w-64 sm:w-72 h-88 sm:h-96 text-sm',
  }[size];

  const activePhotoUrl = !imageError ? (pdfThumbnailUrl || authenticCoverUrl) : null;

  return (
    <div
      onClick={onClick}
      className={`relative select-none shrink-0 group/cover rounded-r-lg rounded-l-[4px] shadow-xl transition-all duration-300 transform group-hover:scale-[1.02] group-hover:-translate-y-1 ${sizeClasses} ${className}`}
      style={{ perspective: '900px' }}
    >
      {/* 3D Book Spine Thickness & Left Depth Crease */}
      <div className="absolute top-0 left-0 bottom-0 w-2.5 sm:w-3 bg-gradient-to-r from-black/85 via-black/50 to-transparent z-30 rounded-l-[3px] pointer-events-none" />

      {/* 3D Book Page Edge (Right Edge Book Block Thickness) */}
      <div className="absolute top-1 -right-1 bottom-1 w-1.5 sm:w-2 bg-gradient-to-r from-amber-100/40 via-amber-50/70 to-slate-300/90 rounded-r-[2px] shadow-sm z-0 pointer-events-none border-y border-r border-slate-300/40" />

      {/* Main Cover Body */}
      <div
        className={`relative w-full h-full rounded-r-lg rounded-l-[3px] overflow-hidden ${
          activePhotoUrl ? 'bg-slate-950' : `bg-gradient-to-br ${palette.bg}`
        } border-t border-b border-r border-amber-500/50 border-l-2 border-l-black/80 shadow-inner flex flex-col justify-between p-2 text-center`}
      >
        {/* CASE 1: REAL PDF THUMBNAIL / SCAN COVER */}
        {activePhotoUrl && (
          <div className="absolute inset-0 z-10 bg-slate-950">
            <img
              src={activePhotoUrl}
              alt={resolvedTitleUrdu || resolvedTitle}
              className={`w-full h-full object-cover object-center transition-opacity duration-300 ${
                imageLoaded ? 'opacity-100' : 'opacity-0'
              }`}
              loading="lazy"
              onLoad={() => setImageLoaded(true)}
              onError={() => setImageError(true)}
            />
            {/* Spine Depth Shadow on Image */}
            <div className="absolute top-0 left-0 bottom-0 w-3 bg-gradient-to-r from-black/80 to-transparent pointer-events-none z-20" />
            {/* Subtle Realistic Book Sheen */}
            <div className="absolute inset-0 bg-gradient-to-tr from-transparent via-white/5 to-transparent pointer-events-none z-20" />
          </div>
        )}

        {/* CASE 2: UNIQUE DECORATIVE ISLAMIC COVER (Shown when no image or loading fallback) */}
        {(!activePhotoUrl || !imageLoaded) && (
          <>
            {/* Fine Moroccan Leather Texture Pattern */}
            <div
              className="absolute inset-0 opacity-25 pointer-events-none mix-blend-overlay"
              style={{
                backgroundImage: `radial-gradient(circle at 50% 50%, #ffffff 12%, transparent 20%), radial-gradient(circle at 20% 80%, #000000 18%, transparent 20%)`,
                backgroundSize: '6px 6px',
              }}
            />

            {/* Outer Gold Foil Border with Corner Arabesques */}
            <div
              className={`absolute inset-1 sm:inset-1.5 border ${palette.border} rounded-r-md pointer-events-none flex flex-col justify-between p-0.5`}
            >
              <div className="flex justify-between items-start pointer-events-none">
                <svg className="w-2.5 h-2.5 sm:w-3 sm:h-3 text-amber-400 opacity-90" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M2 2h7v2H4v5H2V2zm2 2v3h2V6h3V4H4z" />
                </svg>
                <svg className="w-2.5 h-2.5 sm:w-3 sm:h-3 text-amber-400 opacity-90" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M22 2h-7v2h5v5h2V2zm-2 2v3h-2V6h-3V4h5z" />
                </svg>
              </div>
              <div className="flex justify-between items-end pointer-events-none">
                <svg className="w-2.5 h-2.5 sm:w-3 sm:h-3 text-amber-400 opacity-90" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M2 22h7v-2H4v-5H2v7zm2-2v-3h2v1h3v2H4z" />
                </svg>
                <svg className="w-2.5 h-2.5 sm:w-3 sm:h-3 text-amber-400 opacity-90" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M22 22h-7v-2h5v-5h2v7zm-2-2v-3h-2v1h-3v2h5z" />
                </svg>
              </div>
            </div>

            {/* Inner Filigree Gold Frame */}
            <div className={`absolute inset-2 sm:inset-2.5 border ${palette.innerBorder} rounded-[2px] pointer-events-none`} />

            {/* Spine Gilded Ribs */}
            <div className="absolute top-0 left-2 bottom-0 w-[1px] bg-amber-400/30 pointer-events-none" />
            <div className="absolute top-1/4 left-0 w-2 h-[1px] bg-amber-400/40 pointer-events-none" />
            <div className="absolute top-2/4 left-0 w-2 h-[1px] bg-amber-400/40 pointer-events-none" />
            <div className="absolute top-3/4 left-0 w-2 h-[1px] bg-amber-400/40 pointer-events-none" />

            {/* Top Header: Bismillah Calligraphy & Year/Class Badge */}
            <div className="relative z-20 pt-1 px-1">
              <div className="text-[7px] sm:text-[8px] text-amber-300/90 font-arabic tracking-wide leading-tight truncate drop-shadow-sm" dir="rtl">
                بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ
              </div>
              {resolvedClassUrdu && (
                <div className="mt-0.5 inline-block px-1.5 py-0.5 rounded text-[7px] sm:text-[8px] font-bold text-amber-300 font-urdu border border-amber-400/40 bg-black/45 shadow-sm max-w-[95%] truncate">
                  {resolvedClassUrdu}
                </div>
              )}
            </div>

            {/* Center: Medallion with Islamic Geometric Vector Motif & Urdu Title */}
            <div className="relative z-20 my-auto py-1 px-1 flex flex-col items-center justify-center">
              <div className="w-full relative py-2 px-1 border border-amber-400/35 rounded-lg bg-black/40 backdrop-blur-[0.5px] shadow-inner flex flex-col items-center">
                <div className={`${palette.patternColor} mb-1 flex items-center justify-center opacity-90`}>
                  <IslamicMotif patternIndex={hash} className="w-3.5 h-3.5 sm:w-4 sm:h-4 drop-shadow" />
                </div>

                <h4
                  className={`font-black leading-tight text-transparent bg-clip-text bg-gradient-to-b ${palette.titleGrad} drop-shadow-[0_1px_3px_rgba(0,0,0,0.95)] font-urdu tracking-tight px-1 line-clamp-3 ${
                    size === 'sm'
                      ? 'text-[9px]'
                      : size === 'lg'
                      ? 'text-base sm:text-lg'
                      : size === 'xl'
                      ? 'text-lg sm:text-xl'
                      : 'text-xs sm:text-[13px]'
                  }`}
                  dir="rtl"
                  title={resolvedTitleUrdu || resolvedTitle}
                >
                  {resolvedTitleUrdu || resolvedTitle}
                </h4>

                <div className="mt-1.5 inline-flex items-center gap-1">
                  <span className="text-[7px] sm:text-[8px] font-bold text-amber-300 font-urdu px-1.5 py-0.5 rounded bg-amber-950/80 border border-amber-400/40 shadow-sm">
                    {resolvedTypeUrdu}
                  </span>
                </div>
              </div>
            </div>

            {/* Bottom Footer: Category & Author / Publisher */}
            <div className="relative z-20 pb-1 px-1">
              <div className="flex items-center justify-center gap-1 text-[7px] sm:text-[8px] text-amber-300/90 font-medium truncate">
                <span className="font-urdu font-semibold">{resolvedCategory || palette.categoryUrduDefault}</span>
                <span>•</span>
                <span className="font-urdu truncate max-w-[65px] sm:max-w-[85px]">{resolvedAuthor || palette.publisherDefault}</span>
              </div>
            </div>

            {/* Silk Bookmark Ribbon */}
            <div
              className={`absolute -bottom-2 right-3 w-2.5 h-4 ${palette.ribbon} shadow-md z-30 transform -rotate-3`}
              style={{ clipPath: 'polygon(0 0, 100% 0, 100% 100%, 50% 75%, 0 100%)' }}
            />
          </>
        )}
      </div>
    </div>
  );
};

export default IslamicBookCover;
