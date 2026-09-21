import React, { useState, useMemo, useEffect } from 'react';
import { Link } from 'react-router-dom';
import {
  Languages,
  Search,
  Filter,
  Download,
  CheckCircle2,
  Sparkles,
  ArrowRight,
  ArrowLeft,
  X,
  BookOpen,
  BookMarked
} from 'lucide-react';
import { LUGHAT_BOOKS } from '../data/lughatBooks';
import { DarsNizamiBookItem } from '../types';
import { BookCard } from '../components/library/BookCard';
import { PdfModal } from '../components/library/PdfModal';
import { IslamicPatternBg } from '../components/layout/IslamicPatternBg';
import { useLanguage } from '../context/LanguageContext';
import { updatePageSEO } from '../utils/seo';

export const LughatPage: React.FC = () => {
  const { isRtl } = useLanguage();
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedLanguage, setSelectedLanguage] = useState<'all' | 'arabic' | 'farsi' | 'urdu' | 'quranic'>('all');
  const [previewBook, setPreviewBook] = useState<DarsNizamiBookItem | null>(null);

  useEffect(() => {
    updatePageSEO({
      title: 'معتبر معاجم و لغات (القاموس الوحید، المنجد، مصباح اللغات)',
      description:
        'عربی، فارسی، اردو اور قرآنی لغات کا شاہکار ذخیرہ: القاموس الوحید، المنجد عربی اردو، مصباح اللغات، مفردات القرآن للراغب اصفہانی۔ مفت آن لائن مطالعہ و ڈاؤن لوڈ کریں۔',
      keywords: [
        'القاموس الوحید پی ڈی ایف',
        'المنجد عربی اردو',
        'مصباح اللغات پی ڈی ایف',
        'مفردات القرآن راغب اصفہانی',
        'Arabic Urdu Dictionary PDF',
        'Al Qamoos ul Waheed PDF',
        'Al Munjid Urdu PDF',
        'قرآنی لغت'
      ],
      canonicalUrl: 'https://bait-ul-ilm-web.ai.studio/lughat'
    });
    window.scrollTo(0, 0);
  }, []);

  const displayedBooks = useMemo(() => {
    return LUGHAT_BOOKS.filter((book) => {
      // Language filter
      if (selectedLanguage === 'arabic') {
        const isArabic = book.nameUrdu.includes('عربی') || book.classNameUrdu.includes('عربی') || book.typeUrdu.includes('عربی');
        if (!isArabic) return false;
      }
      if (selectedLanguage === 'farsi') {
        const isFarsi = book.nameUrdu.includes('فارسی') || book.classNameUrdu.includes('فارسی') || book.typeUrdu.includes('فارسی');
        if (!isFarsi) return false;
      }
      if (selectedLanguage === 'quranic') {
        const isQuranic = book.nameUrdu.includes('قرآن') || book.classNameUrdu.includes('قرآنی') || book.nameUrdu.includes('انوار البیان');
        if (!isQuranic) return false;
      }

      // Search query
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
  }, [selectedLanguage, searchQuery]);

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 pt-20 pb-20 space-y-8" dir={isRtl ? 'rtl' : 'ltr'}>
      {/* Header Banner */}
      <section className="relative bg-gradient-to-b from-[#200812] via-[#2d0e1c] to-[#200812] text-white py-14 sm:py-18 overflow-hidden border-b border-rose-500/30 shadow-lg">
        <IslamicPatternBg variant="subtle" />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-6">
          {/* Breadcrumb Navigation */}
          <div className="flex items-center justify-between border-b border-white/10 pb-4">
            <div className="flex items-center gap-2 text-xs font-bold text-rose-300 font-urdu">
              <Link to="/" className="hover:text-amber-300 transition-colors">
                ہوم
              </Link>
              <span>/</span>
              <span className="text-amber-300">معاجم و لغات</span>
            </div>

            <Link
              to="/dars-e-nizami"
              className="inline-flex items-center gap-1.5 text-xs font-bold text-rose-300 hover:text-white font-urdu transition-colors"
            >
              <BookOpen className="w-3.5 h-3.5" />
              <span>درسِ نظامی نصاب دیکھیں</span>
            </Link>
          </div>

          <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
            <div className="space-y-3 max-w-3xl">
              <div className="inline-flex items-center gap-2 px-3.5 py-1 rounded-full bg-rose-500/20 border border-rose-400/40 text-rose-300 text-xs font-bold font-urdu">
                <Languages className="w-4 h-4 text-rose-400" />
                <span>معتبر معاجم و لغات (عربی، فارسی، اردو اور قرآنی)</span>
              </div>
              <h1 className="text-2xl sm:text-3xl lg:text-4xl font-black text-white font-urdu tracking-tight leading-tight">
                معاجم و لغات: عربی، فارسی اور قرآنی لغات
              </h1>
              <p className="text-xs sm:text-sm text-rose-100/90 font-urdu leading-relaxed">
                القاموس المحیط، مصباح اللغات، غیاث اللغات، فیروز اللغات، انوار البیان فی حل لغات القرآن (۴ جلدیں مکمل)، اور القاموس الجدید کے براہِ راست پی ڈی ایف مطالعہ اور ڈاؤن لوڈ روابط۔
              </p>
            </div>

            {/* Metrics */}
            <div className="flex items-center gap-3 shrink-0 flex-wrap">
              <div className="px-5 py-3 rounded-2xl bg-black/40 border border-white/15 backdrop-blur-md text-center">
                <span className="block text-2xl font-black text-rose-400 font-mono">
                  {LUGHAT_BOOKS.length}
                </span>
                <span className="text-xs text-slate-300 font-urdu">معتبر لغات و معاجم</span>
              </div>
              <div className="px-5 py-3 rounded-2xl bg-black/40 border border-white/15 backdrop-blur-md text-center">
                <span className="block text-2xl font-black text-amber-400 font-mono">
                  100%
                </span>
                <span className="text-xs text-slate-300 font-urdu">مفت و تیز رفتار</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Main Content Area */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-6">
        {/* Search & Filter Bar */}
        <div className="p-4 sm:p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 shadow-sm flex flex-col md:flex-row md:items-center justify-between gap-4">
          {/* Search Box */}
          <div className="relative flex-1">
            <Search className="w-5 h-5 absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none" />
            <input
              id="lughat-search-input"
              name="lughatSearch"
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="لغت کا نام، مصنف، یا موضوع تلاش کریں (مثلاً: القاموس، مصباح اللغات، غیاث اللغات، انوار البیان)..."
              className="w-full pl-10 pr-12 py-3 rounded-xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 text-xs sm:text-sm font-urdu focus:outline-none focus:ring-2 focus:ring-rose-500 transition-all text-right"
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

          {/* Language Switcher */}
          <div className="flex items-center gap-2 shrink-0 flex-wrap">
            <div className="flex items-center bg-slate-100 dark:bg-slate-800 p-1 rounded-xl text-xs font-urdu font-bold">
              <button
                onClick={() => setSelectedLanguage('all')}
                className={`px-3 py-1.5 rounded-lg transition-colors ${
                  selectedLanguage === 'all'
                    ? 'bg-rose-500 text-white font-black shadow-sm'
                    : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-slate-200'
                }`}
              >
                تمام لغات ({LUGHAT_BOOKS.length})
              </button>
              <button
                onClick={() => setSelectedLanguage('arabic')}
                className={`px-3 py-1.5 rounded-lg transition-colors ${
                  selectedLanguage === 'arabic'
                    ? 'bg-rose-500 text-white font-black shadow-sm'
                    : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-slate-200'
                }`}
              >
                عربی لغات
              </button>
              <button
                onClick={() => setSelectedLanguage('farsi')}
                className={`px-3 py-1.5 rounded-lg transition-colors ${
                  selectedLanguage === 'farsi'
                    ? 'bg-rose-500 text-white font-black shadow-sm'
                    : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-slate-200'
                }`}
              >
                فارسی لغات
              </button>
              <button
                onClick={() => setSelectedLanguage('quranic')}
                className={`px-3 py-1.5 rounded-lg transition-colors ${
                  selectedLanguage === 'quranic'
                    ? 'bg-rose-500 text-white font-black shadow-sm'
                    : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-slate-200'
                }`}
              >
                قرآنی لغات
              </button>
            </div>

            <span className="text-xs font-bold text-slate-500 dark:text-slate-400 font-urdu">
              دستیاب: {displayedBooks.length}
            </span>
          </div>
        </div>

        {/* Lughat Grid */}
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
          <div className="py-16 text-center bg-white dark:bg-slate-900 rounded-3xl border border-slate-200 dark:border-slate-800 p-8 space-y-4">
            <Languages className="w-12 h-12 text-slate-400 mx-auto" />
            <h4 className="text-lg font-bold text-slate-800 dark:text-slate-200 font-urdu">
              کوئی لغت نہیں ملی
            </h4>
            <p className="text-sm text-slate-500 dark:text-slate-400 font-urdu max-w-md mx-auto">
              آپ کے فلٹر کے مطابق کوئی لغت دستیاب نہیں ہے۔ برائے مہربانی تلاش کا لفظ تبدیل کریں یا تمام لغات دیکھیں۔
            </p>
            <button
              onClick={() => {
                setSelectedLanguage('all');
                setSearchQuery('');
              }}
              className="inline-flex items-center gap-2 px-5 py-2.5 rounded-xl text-sm font-bold bg-rose-600 hover:bg-rose-700 text-white font-urdu"
            >
              تمام لغات دکھائیں
            </button>
          </div>
        )}
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
