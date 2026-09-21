import React, { useState, useMemo, useEffect } from 'react';
import { Link } from 'react-router-dom';
import {
  ScrollText,
  Search,
  Filter,
  Download,
  CheckCircle2,
  Sparkles,
  ArrowRight,
  ArrowLeft,
  X,
  BookOpen,
  Send
} from 'lucide-react';
import { FATAWA_BOOKS } from '../data/fatawaBooks';
import { DarsNizamiBookItem } from '../types';
import { BookCard } from '../components/library/BookCard';
import { PdfModal } from '../components/library/PdfModal';
import { IslamicPatternBg } from '../components/layout/IslamicPatternBg';
import { useLanguage } from '../context/LanguageContext';
import { updatePageSEO } from '../utils/seo';

export const FatawaPage: React.FC = () => {
  const { isRtl } = useLanguage();
  const [searchQuery, setSearchQuery] = useState('');
  const [previewBook, setPreviewBook] = useState<DarsNizamiBookItem | null>(null);
  const [requestBookName, setRequestBookName] = useState('');
  const [requestLink, setRequestLink] = useState('');
  const [submitSuccess, setSubmitSuccess] = useState(false);

  useEffect(() => {
    updatePageSEO({
      title: 'فتاویٰ و فقہی ذخیرہ (فتاویٰ شامی، عالمگیری، رضویہ، دارالعلوم دیوبند)',
      description:
        'مستند کتب فتاویٰ و فقہ اسلامی کا مکمل ذخیرہ: فتاویٰ شامی (رد المحتار)، فتاویٰ عالمگیری (ہندیہ)، فتاویٰ رضویہ، فتاویٰ دارالعلوم دیوبند، بہار شریعت۔ مفت مطالعہ و ڈاؤن لوڈ کریں۔',
      keywords: [
        'فتاویٰ پی ڈی ایف',
        'فتاویٰ شامی پی ڈی ایف',
        'فتاویٰ عالمگیری',
        'فتاویٰ رضویہ',
        'فتاویٰ دارالعلوم دیوبند',
        'بہار شریعت',
        'فقہ حنفی کتب',
        'Fatawa Shami PDF',
        'Fatawa Alamgiri Urdu',
        'Fatawa Razawiyya'
      ],
      canonicalUrl: 'https://bait-ul-ilm-web.ai.studio/fatawa'
    });
    window.scrollTo(0, 0);
  }, []);

  const displayedBooks = useMemo(() => {
    return FATAWA_BOOKS.filter((book) => {
      if (searchQuery.trim()) {
        const q = searchQuery.toLowerCase().trim();
        const matchName = book.name.toLowerCase().includes(q);
        const matchNameUrdu = book.nameUrdu.toLowerCase().includes(q);
        const matchAuthor = (book.author || '').toLowerCase().includes(q);
        const matchEdition = (book.edition || '').toLowerCase().includes(q);
        if (!matchName && !matchNameUrdu && !matchAuthor && !matchEdition) {
          return false;
        }
      }
      return true;
    });
  }, [searchQuery]);

  const handleRequestSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!requestBookName.trim()) return;
    setSubmitSuccess(true);
    setTimeout(() => {
      setRequestBookName('');
      setRequestLink('');
      setSubmitSuccess(false);
    }, 4000);
  };

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 pt-20 pb-20 space-y-8" dir={isRtl ? 'rtl' : 'ltr'}>
      {/* Header Banner */}
      <section className="relative bg-gradient-to-b from-[#100c28] via-[#171239] to-[#100c28] text-white py-14 sm:py-18 overflow-hidden border-b border-indigo-500/30 shadow-lg">
        <IslamicPatternBg variant="subtle" />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-6">
          {/* Breadcrumb Navigation */}
          <div className="flex items-center justify-between border-b border-white/10 pb-4">
            <div className="flex items-center gap-2 text-xs font-bold text-indigo-300 font-urdu">
              <Link to="/" className="hover:text-amber-300 transition-colors">
                ہوم
              </Link>
              <span>/</span>
              <span className="text-amber-300">فتاویٰ و فقہی ذخیرہ</span>
            </div>

            <Link
              to="/dars-e-nizami"
              className="inline-flex items-center gap-1.5 text-xs font-bold text-indigo-300 hover:text-white font-urdu transition-colors"
            >
              <BookOpen className="w-3.5 h-3.5" />
              <span>درسِ نظامی نصاب دیکھیں</span>
            </Link>
          </div>

          <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
            <div className="space-y-3 max-w-3xl">
              <div className="inline-flex items-center gap-2 px-3.5 py-1 rounded-full bg-indigo-500/20 border border-indigo-400/40 text-indigo-300 text-xs font-bold font-urdu">
                <ScrollText className="w-4 h-4 text-indigo-400" />
                <span>مستند فتاویٰ و فقہ حنفی کے امہات الکتب</span>
              </div>
              <h1 className="text-2xl sm:text-3xl lg:text-4xl font-black text-white font-urdu tracking-tight leading-tight">
                فتاویٰ و فقہی ذخیرہ: مکمل مجلدات و فتاویٰ
              </h1>
              <p className="text-xs sm:text-sm text-indigo-100/90 font-urdu leading-relaxed">
                فقہ اسلامی اور فتویٰ نویسی کے معتبر و معتمد ماخذ: فتاویٰ شامی (رد المحتار علی الدر المختار)، فتاویٰ عالمگیری (الفتاویٰ الہندیۃ)، فتاویٰ دارالعلوم دیوبند، فتاویٰ محمودیہ و فتاویٰ رحیمیہ۔
              </p>
            </div>

            {/* Metrics */}
            <div className="flex items-center gap-3 shrink-0 flex-wrap">
              <div className="px-5 py-3 rounded-2xl bg-black/40 border border-white/15 backdrop-blur-md text-center">
                <span className="block text-2xl font-black text-indigo-400 font-mono">
                  {FATAWA_BOOKS.length}
                </span>
                <span className="text-xs text-slate-300 font-urdu">دستیاب مجلدات</span>
              </div>
              <div className="px-5 py-3 rounded-2xl bg-black/40 border border-white/15 backdrop-blur-md text-center">
                <span className="block text-2xl font-black text-amber-400 font-mono">
                  100%
                </span>
                <span className="text-xs text-slate-300 font-urdu">مفت و تصدیق شدہ</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Main Content Area */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        {/* Search & Counter Bar */}
        <div className="p-4 sm:p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 shadow-sm flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div className="relative flex-1">
            <Search className="w-5 h-5 absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none" />
            <input
              id="fatawa-search-input"
              name="fatawaSearch"
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="فتاویٰ یا جلد کا نام تلاش کریں (مثلاً: فتاویٰ شامی، رد المحتار، جلد اول، جلد دوم)..."
              className="w-full pl-10 pr-12 py-3 rounded-xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 text-xs sm:text-sm font-urdu focus:outline-none focus:ring-2 focus:ring-indigo-500 transition-all text-right"
              dir="rtl"
            />
            {searchQuery && (
              <button
                onClick={() => setSearchQuery('')}
                className="absolute left-3 top-1/2 -translate-y-1/2 p-1 text-slate-400 hover:text-slate-600 dark:hover:text-slate-200"
              >
                <X className="w-4 h-4" />
              </button>
            )}
          </div>

          <span className="text-xs font-bold text-slate-500 dark:text-slate-400 font-urdu">
            دستیاب مجلدات: {displayedBooks.length}
          </span>
        </div>

        {/* Fatawa Books Grid */}
        {displayedBooks.length > 0 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 sm:gap-6">
            {displayedBooks.map((book, idx) => (
              <BookCard
                key={`${book.id}-${idx}`}
                book={book}
                onSelectForView={(b) => setPreviewBook(b)}
              />
            ))}
          </div>
        ) : (
          <div className="py-12 text-center bg-white dark:bg-slate-900 rounded-3xl border border-slate-200 dark:border-slate-800 p-8 space-y-4">
            <ScrollText className="w-12 h-12 text-slate-400 mx-auto" />
            <h4 className="text-lg font-bold text-slate-800 dark:text-slate-200 font-urdu">
              کوئی فتاویٰ نہیں ملا
            </h4>
            <p className="text-sm text-slate-500 dark:text-slate-400 font-urdu max-w-md mx-auto">
              آپ کے فلٹر کے مطابق کوئی کتاب نہیں ہے۔
            </p>
          </div>
        )}

        {/* User Submission Box for Additional Fatawa */}
        <div className="p-6 sm:p-8 rounded-3xl bg-gradient-to-br from-indigo-950/40 via-slate-900 to-slate-900 border border-indigo-500/30 space-y-4">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-indigo-500/20 text-indigo-400 flex items-center justify-center font-bold">
              <Sparkles className="w-5 h-5" />
            </div>
            <div>
              <h3 className="text-lg font-bold text-white font-urdu">
                مزید فتاویٰ کتب شامل کروانے کی درخواست
              </h3>
              <p className="text-xs text-slate-300 font-urdu">
                اگر آپ کے پاس کسی مستند فتاویٰ کا پی ڈی ایف یا لنک ہے تو یہاں ارسال کریں، ہم فوری پورٹل میں شامل کر دیں گے۔
              </p>
            </div>
          </div>

          <form id="fatawa-request-form" name="fatawaRequestForm" onSubmit={handleRequestSubmit} className="grid grid-cols-1 md:grid-cols-3 gap-3 pt-2">
            <input
              id="fatawa-request-book-name"
              name="requestBookName"
              type="text"
              required
              aria-label="فتاویٰ کا نام"
              value={requestBookName}
              onChange={(e) => setRequestBookName(e.target.value)}
              placeholder="فتاویٰ کا نام (مثلاً: فتاویٰ عالمگیری جلد ۳)..."
              className="bg-slate-800/90 border border-slate-700 rounded-xl px-4 py-2.5 text-xs sm:text-sm text-white focus:outline-none focus:border-indigo-400 font-urdu text-right"
              dir="rtl"
            />
            <input
              id="fatawa-request-link"
              name="requestLink"
              type="url"
              value={requestLink}
              onChange={(e) => setRequestLink(e.target.value)}
              placeholder="پی ڈی ایف یا آرکائیو لنک (اختیاری)..."
              className="bg-slate-800/90 border border-slate-700 rounded-xl px-4 py-2.5 text-xs sm:text-sm text-white focus:outline-none focus:border-indigo-400 font-urdu text-right"
              dir="rtl"
            />
            <button
              type="submit"
              className="inline-flex items-center justify-center gap-2 px-5 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-700 text-white font-urdu font-bold text-xs sm:text-sm shadow-md transition-all"
            >
              <Send className="w-4 h-4" />
              <span>ارسال کریں</span>
            </button>
          </form>

          {submitSuccess && (
            <div className="p-3 rounded-xl bg-emerald-500/20 border border-emerald-500/40 text-emerald-300 text-xs font-urdu font-bold flex items-center gap-2">
              <CheckCircle2 className="w-4 h-4" />
              <span>شکریہ! آپ کی ارسال کردہ کتاب کا لنک موصول ہو گیا ہے اور پورٹل میں جلد شامل کر دیا جائے گا۔</span>
            </div>
          )}
        </div>
      </main>

      {/* PDF Modal */}
      {previewBook && (
        <PdfModal
          book={previewBook}
          onClose={() => setPreviewBook(null)}
        />
      )}
    </div>
  );
};
