import React, { useState, useMemo, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import {
  GraduationCap,
  BookOpen,
  ArrowRight,
  ArrowLeft,
  Search,
  Filter,
  Layers,
  ChevronRight,
  ChevronLeft,
  BookMarked,
  Download,
  Sparkles,
  X
} from 'lucide-react';
import { ALL_DARS_NIZAMI_BOOKS, DARS_YEARS_META } from '../data/darsNizamiBooks';
import { DarsNizamiBookItem } from '../types';
import { BookCard } from '../components/library/BookCard';
import { PdfModal } from '../components/library/PdfModal';
import { IslamicPatternBg } from '../components/layout/IslamicPatternBg';
import { useLanguage } from '../context/LanguageContext';
import { updatePageSEO } from '../utils/seo';

export const DarsNizamiClassDetailPage: React.FC = () => {
  const { classLevel } = useParams<{ classLevel: string }>();
  const { isRtl } = useLanguage();
  const navigate = useNavigate();

  const [searchQuery, setSearchQuery] = useState('');
  const [selectedType, setSelectedType] = useState<'all' | 'main' | 'sharh'>('all');
  const [previewBook, setPreviewBook] = useState<DarsNizamiBookItem | null>(null);

  // Find metadata for the current class
  const classMeta = useMemo(() => {
    return DARS_YEARS_META.find((c) => c.classLevel === classLevel) || null;
  }, [classLevel]);

  // Filter books strictly belonging to this class
  const classBooks = useMemo(() => {
    if (!classLevel) return [];
    return ALL_DARS_NIZAMI_BOOKS.filter((b) => b.classLevel === classLevel);
  }, [classLevel]);

  // Update dynamic SEO for class and books
  useEffect(() => {
    if (classMeta) {
      const topBookNames = classBooks.slice(0, 10).map((b) => b.titleUrdu || b.title);
      updatePageSEO({
        title: `${classMeta.nameUrdu} کتب و شروحات (${classMeta.nameEnglish})`,
        description: `درسِ نظامی ${classMeta.nameUrdu} کی تمام کتب اور شروحات پی ڈی ایف مفت ڈاؤن لوڈ اور آن لائن مطالعہ کریں۔ کتب: ${topBookNames.join('، ')}۔`,
        keywords: [
          classMeta.nameUrdu,
          classMeta.nameEnglish,
          `Dars e Nizami ${classMeta.nameEnglish} books`,
          `${classMeta.nameUrdu} کتب پی ڈی ایف`,
          ...topBookNames
        ],
        canonicalUrl: `https://bait-ul-ilm-web.ai.studio/dars-e-nizami/${classLevel}`
      });
    } else {
      updatePageSEO({
        title: 'درسِ نظامی درجات و نصاب کتب',
        description: 'درسِ نظامی کے تمام 8 سالہ درجات (اولٰی تا دورہ حدیث) کی اصل کتب و شروحات کا ذخیرہ۔',
        canonicalUrl: 'https://bait-ul-ilm-web.ai.studio/dars-e-nizami'
      });
    }
    window.scrollTo(0, 0);
  }, [classMeta, classBooks, classLevel]);

  // Apply search and type filters
  const displayedBooks = useMemo(() => {
    return classBooks.filter((book) => {
      // Type filter
      if (selectedType === 'main') {
        const isSharh = book.type.toLowerCase().includes('sharh') || book.typeUrdu.includes('شرح') || book.typeUrdu.includes('حاشیہ');
        if (isSharh) return false;
      }
      if (selectedType === 'sharh') {
        const isSharh = book.type.toLowerCase().includes('sharh') || book.typeUrdu.includes('شرح') || book.typeUrdu.includes('حاشیہ');
        if (!isSharh) return false;
      }

      // Search query
      if (searchQuery.trim()) {
        const q = searchQuery.toLowerCase().trim();
        const matchName = book.name.toLowerCase().includes(q);
        const matchNameUrdu = book.nameUrdu.toLowerCase().includes(q);
        const matchAuthor = (book.author || '').toLowerCase().includes(q);
        const matchCategory = (book.category || '').toLowerCase().includes(q);
        if (!matchName && !matchNameUrdu && !matchAuthor && !matchCategory) {
          return false;
        }
      }

      return true;
    });
  }, [classBooks, selectedType, searchQuery]);

  // Other available classes for switching
  const otherClasses = useMemo(() => {
    return DARS_YEARS_META.filter((c) => c.classLevel !== 'quran');
  }, []);

  if (!classMeta) {
    return (
      <div className="min-h-screen pt-28 pb-20 px-4 text-center space-y-6" dir={isRtl ? 'rtl' : 'ltr'}>
        <div className="max-w-md mx-auto p-8 rounded-3xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 space-y-4">
          <BookMarked className="w-12 h-12 text-rose-500 mx-auto" />
          <h2 className="text-xl font-bold font-urdu">مطلوبہ درجہ دستیاب نہیں ہے</h2>
          <p className="text-xs text-slate-500 font-urdu">برائے مہربانی درسِ نظامی کے تمام درجات کی فہرست سے انتخاب کریں۔</p>
          <Link
            to="/dars-e-nizami"
            className="inline-flex items-center gap-2 px-5 py-2.5 rounded-2xl bg-emerald-700 text-white font-urdu font-bold text-sm"
          >
            {isRtl ? <ArrowRight className="w-4 h-4" /> : <ArrowLeft className="w-4 h-4" />}
            <span>درسِ نظامی کے تمام درجات دیکھیں</span>
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 pt-20 pb-20 space-y-8" dir={isRtl ? 'rtl' : 'ltr'}>
      {/* Top Hero Banner */}
      <section className="relative bg-gradient-to-b from-[#091510] via-[#0f241c] to-[#091510] text-white py-12 sm:py-16 overflow-hidden border-b border-emerald-800/40 shadow-lg">
        <IslamicPatternBg variant="subtle" />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-6">
          {/* Breadcrumbs & Back Button */}
          <div className="flex flex-wrap items-center justify-between gap-3 border-b border-white/10 pb-4">
            <div className="flex items-center gap-2 text-xs font-bold text-emerald-300 font-urdu">
              <Link to="/" className="hover:text-amber-300 transition-colors">
                ہوم
              </Link>
              <span>/</span>
              <Link to="/dars-e-nizami" className="hover:text-amber-300 transition-colors">
                درسِ نظامی درجات
              </Link>
              <span>/</span>
              <span className="text-amber-300">{classMeta.nameUrdu}</span>
            </div>

            <Link
              to="/dars-e-nizami"
              className="inline-flex items-center gap-2 px-4 py-1.5 rounded-xl bg-white/10 hover:bg-white/20 border border-white/20 text-xs font-bold font-urdu text-amber-300 transition-colors"
            >
              {isRtl ? <ArrowRight className="w-4 h-4" /> : <ArrowLeft className="w-4 h-4" />}
              <span>← واپس تمام درجات کی فہرست پر جائیں</span>
            </Link>
          </div>

          {/* Main Title & Class Meta */}
          <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
            <div className="space-y-3 max-w-3xl">
              <div className="inline-flex items-center gap-2 px-3.5 py-1 rounded-full bg-emerald-900/80 border border-amber-400/40 text-amber-300 text-xs font-bold font-urdu">
                <GraduationCap className="w-4 h-4 text-amber-400" />
                <span>{classMeta.badge}</span>
              </div>
              <h1 className="text-2xl sm:text-3xl lg:text-4xl font-black text-white font-urdu tracking-tight leading-tight">
                {classMeta.nameUrdu} — کتب و شروحات
              </h1>
              <p className="text-xs sm:text-sm text-emerald-100/90 font-urdu leading-relaxed">
                مضامین و فنون: {classMeta.descriptionUrdu}
              </p>
            </div>

            {/* Quick Metrics */}
            <div className="flex items-center gap-3 shrink-0 flex-wrap">
              <div className="px-5 py-3 rounded-2xl bg-black/40 border border-white/15 backdrop-blur-md text-center">
                <span className="block text-2xl font-black text-amber-400 font-mono">
                  {classBooks.length}
                </span>
                <span className="text-xs text-slate-300 font-urdu">اس درجہ کی کل کتب</span>
              </div>
              <div className="px-5 py-3 rounded-2xl bg-black/40 border border-white/15 backdrop-blur-md text-center">
                <span className="block text-2xl font-black text-emerald-400 font-mono">
                  {classBooks.filter((b) => b.type.toLowerCase().includes('sharh')).length}
                </span>
                <span className="text-xs text-slate-300 font-urdu">معتبر شروحات</span>
              </div>
            </div>
          </div>

          {/* Direct Class Switcher Pills */}
          <div className="pt-2">
            <div className="flex items-center gap-2 text-xs font-bold text-slate-300 font-urdu mb-2">
              <Layers className="w-3.5 h-3.5 text-amber-400" />
              <span>دیگر درجات میں جائیں (Switch Class):</span>
            </div>
            <div className="flex items-center gap-2 overflow-x-auto pb-2 scrollbar-none flex-nowrap sm:flex-wrap">
              {otherClasses.map((c) => {
                const isActive = c.classLevel === classLevel;
                return (
                  <Link
                    key={c.id}
                    to={`/dars-e-nizami/${c.classLevel}`}
                    className={`px-3 py-1.5 rounded-xl text-xs font-bold font-urdu whitespace-nowrap transition-all flex items-center gap-1.5 ${
                      isActive
                        ? 'bg-amber-400 text-slate-950 font-black shadow-md'
                        : 'bg-white/10 hover:bg-white/20 text-slate-200 border border-white/15'
                    }`}
                  >
                    <span>{c.badge}</span>
                    <span className="text-[10px] opacity-75 font-mono">({c.totalBooks})</span>
                  </Link>
                );
              })}
            </div>
          </div>
        </div>
      </section>

      {/* Main Content Area */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-6">
        {/* Search & Filter Bar */}
        <div className="p-4 sm:p-5 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 shadow-sm flex flex-col md:flex-row md:items-center justify-between gap-4">
          {/* Real-time Search Input */}
          <div className="relative flex-1">
            <Search className="w-5 h-5 absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none" />
            <input
              id="class-detail-search-input"
              name="classDetailSearch"
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder={`اس درجہ (${classMeta.badge}) میں کوئی کتاب، شرح، یا مصنف تلاش کریں...`}
              className="w-full pl-10 pr-12 py-3 rounded-xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 text-xs sm:text-sm font-urdu focus:outline-none focus:ring-2 focus:ring-emerald-500 transition-all text-right"
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

          {/* Type Switcher */}
          <div className="flex items-center gap-2 shrink-0 flex-wrap">
            <div className="flex items-center bg-slate-100 dark:bg-slate-800 p-1 rounded-xl text-xs font-urdu font-bold">
              <button
                onClick={() => setSelectedType('all')}
                className={`px-3 py-1.5 rounded-lg transition-colors ${
                  selectedType === 'all'
                    ? 'bg-amber-400 text-slate-950 font-black shadow-sm'
                    : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-slate-200'
                }`}
              >
                تمام کتب ({classBooks.length})
              </button>
              <button
                onClick={() => setSelectedType('main')}
                className={`px-3 py-1.5 rounded-lg transition-colors ${
                  selectedType === 'main'
                    ? 'bg-amber-400 text-slate-950 font-black shadow-sm'
                    : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-slate-200'
                }`}
              >
                اصل کتب
              </button>
              <button
                onClick={() => setSelectedType('sharh')}
                className={`px-3 py-1.5 rounded-lg transition-colors ${
                  selectedType === 'sharh'
                    ? 'bg-amber-400 text-slate-950 font-black shadow-sm'
                    : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-slate-200'
                }`}
              >
                شروحات و حواشی
              </button>
            </div>

            <span className="text-xs font-bold text-slate-500 dark:text-slate-400 font-urdu">
              ظاہر کردہ کتب: {displayedBooks.length}
            </span>
          </div>
        </div>

        {/* Books Grid */}
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
            <BookMarked className="w-12 h-12 text-slate-400 mx-auto" />
            <h4 className="text-lg font-bold text-slate-800 dark:text-slate-200 font-urdu">
              اس فلٹر یا تلاش کے مطابق کوئی کتاب نہیں ملی
            </h4>
            <p className="text-sm text-slate-500 dark:text-slate-400 font-urdu max-w-md mx-auto">
              برائے مہربانی تلاش کا لفظ تبدیل کریں یا تمام کتب کا فلٹر منتخب کریں۔
            </p>
            <button
              onClick={() => {
                setSelectedType('all');
                setSearchQuery('');
              }}
              className="inline-flex items-center gap-2 px-5 py-2.5 rounded-xl text-sm font-bold bg-emerald-700 hover:bg-emerald-800 text-white font-urdu"
            >
              تمام فلٹرز ختم کریں
            </button>
          </div>
        )}
      </main>

      {/* PDF Modal Reader */}
      {previewBook && (
        <PdfModal
          book={previewBook}
          onClose={() => setPreviewBook(null)}
        />
      )}
    </div>
  );
};
