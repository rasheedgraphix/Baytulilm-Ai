import React, { useState, useMemo, useEffect } from 'react';
import {
  BookOpen,
  Search,
  Filter,
  Download,
  CheckCircle2,
  GraduationCap,
  Sparkles,
  Layers,
  ArrowRight,
  BookMarked,
  X,
  Landmark
} from 'lucide-react';
import { ALL_DARS_NIZAMI_BOOKS, DARS_YEARS_META } from '../data/darsNizamiBooks';
import { DarsNizamiBookItem } from '../types';
import { BookCard } from '../components/library/BookCard';
import { PdfModal } from '../components/library/PdfModal';
import { SectionHeader } from '../components/common/SectionHeader';
import { DownloadButton } from '../components/common/DownloadButton';
import { IslamicPatternBg } from '../components/layout/IslamicPatternBg';
import { APP_CONFIG } from '../config/appConfig';
import { useLanguage } from '../context/LanguageContext';
import { updatePageSEO } from '../utils/seo';
import { AdsterraBanner } from '../components/ads/AdsterraBanner';

export const LibraryPage: React.FC = () => {
  const { language, isRtl } = useLanguage();
  const [selectedYear, setSelectedYear] = useState<string>('all');
  const [selectedCategory, setSelectedCategory] = useState<string>('all');
  const [selectedType, setSelectedType] = useState<string>('all');
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [previewBook, setPreviewBook] = useState<DarsNizamiBookItem | null>(null);

  useEffect(() => {
    updatePageSEO({
      title: 'Islamic Books Library & Dars-e-Nizami PDF Collection (کتب خانہ)',
      description:
        'Download and read hundreds of Islamic books online, complete 8-year Dars-e-Nizami textbooks, Shurooh, Tafaseer, Fatawa, and Arabic dictionaries on Baytul Ilm AI (بیت العلم).',
      keywords: [
        'Islamic Books Library',
        'Dars-e-Nizami books pdf',
        'Dars e Nizami shurooh',
        'Kanzud Daqaiq pdf',
        'Al Hidayah pdf',
        'Mukhtasar al Quduri pdf',
        'Mishkat al Masabih',
        'Sharh Jami',
        'Kafia',
        'Hidayat un Nahw',
        'Nur al Anwar',
        'Usul ash Shashi',
        'Download Islamic Books PDF',
        'کتب درس نظامی',
        'شروحات درس نظامی',
        'بیت العلم لائبریری'
      ],
      canonicalUrl: 'https://bait-ul-ilm-web.ai.studio/library'
    });
  }, []);

  // Extract all distinct categories
  const categories = useMemo(() => {
    const set = new Set<string>();
    ALL_DARS_NIZAMI_BOOKS.forEach((b) => {
      if (b.category) {
        // Clean up or group
        const cat = b.category.split('(')[0].trim();
        set.add(cat);
      }
    });
    return Array.from(set).sort();
  }, []);

  // Filtered books
  const filteredBooks = useMemo(() => {
    return ALL_DARS_NIZAMI_BOOKS.filter((book) => {
      // Filter by year
      if (selectedYear !== 'all' && book.classLevel !== selectedYear) {
        return false;
      }

      // Filter by category
      if (selectedCategory !== 'all') {
        const catClean = book.category.split('(')[0].trim();
        if (catClean !== selectedCategory && !book.category.includes(selectedCategory)) {
          return false;
        }
      }

      // Filter by type
      if (selectedType !== 'all') {
        if (selectedType === 'main' && !book.type.includes('Main') && !book.typeUrdu.includes('اصل')) {
          return false;
        }
        if (selectedType === 'sharh' && !book.type.includes('Sharh') && !book.typeUrdu.includes('شرح')) {
          return false;
        }
        if (selectedType === 'translation' && !book.type.includes('Translation') && !book.typeUrdu.includes('ترجمہ')) {
          return false;
        }
        if (selectedType === 'notes' && !book.type.includes('Darsi') && !book.typeUrdu.includes('تقریر')) {
          return false;
        }
      }

      // Filter by search query
      if (searchQuery.trim()) {
        const query = searchQuery.toLowerCase().trim();
        const matchName = book.name.toLowerCase().includes(query);
        const matchNameUrdu = book.nameUrdu.toLowerCase().includes(query);
        const matchCategory = book.category.toLowerCase().includes(query);
        const matchClass = book.classNameUrdu.toLowerCase().includes(query) || book.classNameEnglish.toLowerCase().includes(query);
        if (!matchName && !matchNameUrdu && !matchCategory && !matchClass) {
          return false;
        }
      }

      return true;
    });
  }, [selectedYear, selectedCategory, selectedType, searchQuery]);

  // Selected year metadata
  const currentYearMeta = useMemo(() => {
    if (selectedYear === 'all') return null;
    return DARS_YEARS_META.find((y) => y.classLevel === selectedYear) || null;
  }, [selectedYear]);

  const clearAllFilters = () => {
    setSelectedYear('all');
    setSelectedCategory('all');
    setSelectedType('all');
    setSearchQuery('');
  };

  return (
    <div className="pt-24 pb-20 space-y-10 min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100">
      {/* Header Banner */}
      <section className="relative bg-emerald-950 text-white py-14 sm:py-18 overflow-hidden shadow-md">
        <IslamicPatternBg variant="hero" />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-5 text-center">
          <SectionHeader
            badge="درسِ نظامی ڈیجیٹل کتب خانہ • Digital Library"
            title="جامع کتب خانہ درسِ نظامی و شروحات"
            arabicTitle="المَكْتَبَةُ الإِسْلامِيَّةُ الشَّامِلَةُ لِدِرَاسَةِ العُلُومِ الشَّرْعِيَّة"
            subtitle="سال اول (اولیٰ) تا سال ہشتم (دورۂ حدیث شریف) کی تمام نصابی درسی کتب، معتبر عربی و اردو شروحات، تراجم، امتحانی حل شدہ پرچہ جات اور براہِ راست ڈاؤن لوڈ کے روابط ایک ہی جگہ۔"
            showBismillah
          />

          {/* Quick Metrics */}
          <div className="pt-3 flex items-center justify-center gap-3 sm:gap-6 flex-wrap text-xs sm:text-sm font-medium text-emerald-200">
            <div className="flex items-center gap-1.5 bg-emerald-900/60 px-3 py-1 rounded-full border border-emerald-500/30">
              <GraduationCap className="w-4 h-4 text-amber-400" />
              <span>8 سالہ مکمل درجات</span>
            </div>
            <div className="flex items-center gap-1.5 bg-emerald-900/60 px-3 py-1 rounded-full border border-emerald-500/30">
              <BookOpen className="w-4 h-4 text-amber-400" />
              <span>250+ مستند کتب و شروحات</span>
            </div>
            <div className="flex items-center gap-1.5 bg-emerald-900/60 px-3 py-1 rounded-full border border-emerald-500/30">
              <Download className="w-4 h-4 text-amber-400" />
              <span>مفت اور تیز رفتار PDF لنکس</span>
            </div>
          </div>
        </div>
      </section>

      {/* Main Content Area */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        {/* Search and Year Navigation Box */}
        <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-3xl p-5 sm:p-6 shadow-sm space-y-5">
          {/* Real-time Search Input */}
          <div className="relative">
            <Search className="w-5 h-5 absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none" />
            <input
              id="library-books-search"
              name="librarySearch"
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="کتاب کا نام، مصنف، موضوع، یا درجہ تلاش کریں (مثلاً: ہدایہ، نور الایضاح، کافیہ، مشکوٰۃ، بخاری، منطق)..."
              className="w-full pl-10 pr-12 py-3.5 rounded-2xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 text-sm font-urdu focus:outline-none focus:ring-2 focus:ring-emerald-500 transition-all text-right"
              dir="rtl"
            />
            {searchQuery && (
              <button
                onClick={() => setSearchQuery('')}
                className="absolute left-3.5 top-1/2 -translate-y-1/2 p-1 text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 rounded-full"
                aria-label="Clear search"
              >
                <X className="w-4 h-4" />
              </button>
            )}
          </div>

          {/* Dars-e-Nizami Year Selector Pills */}
          <div className="space-y-2">
            <div className="flex items-center justify-between gap-2">
              <span className="text-xs font-extrabold uppercase tracking-wider text-slate-500 dark:text-slate-400">
                درجہ بندی برائے سال / کلاس (Select Class)
              </span>
              <span className="text-xs text-emerald-700 dark:text-emerald-400 font-bold">
                {selectedYear === 'all'
                  ? `تمام سال (${ALL_DARS_NIZAMI_BOOKS.length} کتب)`
                  : `${currentYearMeta?.nameUrdu} (${filteredBooks.length} کتب)`}
              </span>
            </div>

            <div className="flex items-center gap-2 overflow-x-auto pb-2 scrollbar-none flex-nowrap sm:flex-wrap">
              <button
                onClick={() => setSelectedYear('all')}
                className={`px-3.5 py-2 rounded-xl text-xs font-bold whitespace-nowrap transition-all flex items-center gap-1.5 ${
                  selectedYear === 'all'
                    ? 'bg-emerald-700 text-white shadow-md shadow-emerald-700/20'
                    : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700'
                }`}
              >
                <Layers className="w-3.5 h-3.5" />
                <span>تمام درجات (All)</span>
              </button>

              {DARS_YEARS_META.map((year) => {
                const isActive = selectedYear === year.classLevel;
                return (
                  <button
                    key={year.id}
                    onClick={() => setSelectedYear(year.classLevel)}
                    className={`px-3.5 py-2 rounded-xl text-xs font-bold whitespace-nowrap transition-all flex items-center gap-1.5 ${
                      isActive
                        ? 'bg-emerald-700 text-white shadow-md shadow-emerald-700/20'
                        : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700'
                    }`}
                  >
                    <span>{year.badge}</span>
                    <span className={`text-[10px] px-1.5 py-0.2 rounded-full ${
                      isActive ? 'bg-emerald-900/60 text-emerald-100' : 'bg-slate-200 dark:bg-slate-700 text-slate-600 dark:text-slate-300'
                    }`}>
                      {year.totalBooks}
                    </span>
                  </button>
                );
              })}

              {/* Dedicated Department Short-cut Pills */}
              <button
                onClick={() => setSelectedYear('tareekh')}
                className={`px-3.5 py-2 rounded-xl text-xs font-bold whitespace-nowrap transition-all flex items-center gap-1.5 ${
                  selectedYear === 'tareekh'
                    ? 'bg-amber-700 text-white shadow-md shadow-amber-700/20'
                    : 'bg-amber-50 dark:bg-amber-950/40 text-amber-800 dark:text-amber-300 border border-amber-300/60 dark:border-amber-800 hover:bg-amber-100'
                }`}
              >
                <Landmark className="w-3.5 h-3.5" />
                <span>تاریخ و سیرت</span>
                <span className={`text-[10px] px-1.5 py-0.2 rounded-full ${
                  selectedYear === 'tareekh' ? 'bg-amber-900/60 text-amber-100' : 'bg-amber-200/80 dark:bg-amber-900 text-amber-900 dark:text-amber-200'
                }`}>
                  {ALL_DARS_NIZAMI_BOOKS.filter(b => b.classLevel === 'tareekh').length}
                </span>
              </button>

              <button
                onClick={() => setSelectedYear('tajweed-lil-huffaz')}
                className={`px-3.5 py-2 rounded-xl text-xs font-bold whitespace-nowrap transition-all flex items-center gap-1.5 ${
                  selectedYear === 'tajweed-lil-huffaz'
                    ? 'bg-teal-700 text-white shadow-md shadow-teal-700/20'
                    : 'bg-teal-50 dark:bg-teal-950/40 text-teal-800 dark:text-teal-300 border border-teal-300/60 dark:border-teal-800 hover:bg-teal-100'
                }`}
              >
                <Sparkles className="w-3.5 h-3.5" />
                <span>تجوید للحفاظ</span>
                <span className={`text-[10px] px-1.5 py-0.2 rounded-full ${
                  selectedYear === 'tajweed-lil-huffaz' ? 'bg-teal-900/60 text-teal-100' : 'bg-teal-200/80 dark:bg-teal-900 text-teal-900 dark:text-teal-200'
                }`}>
                  {ALL_DARS_NIZAMI_BOOKS.filter(b => b.classLevel === 'tajweed-lil-huffaz').length}
                </span>
              </button>

              <button
                onClick={() => setSelectedYear('tajweed-lil-ulama')}
                className={`px-3.5 py-2 rounded-xl text-xs font-bold whitespace-nowrap transition-all flex items-center gap-1.5 ${
                  selectedYear === 'tajweed-lil-ulama'
                    ? 'bg-sky-700 text-white shadow-md shadow-sky-700/20'
                    : 'bg-sky-50 dark:bg-sky-950/40 text-sky-800 dark:text-sky-300 border border-sky-300/60 dark:border-sky-800 hover:bg-sky-100'
                }`}
              >
                <GraduationCap className="w-3.5 h-3.5" />
                <span>تجوید للعلماء</span>
                <span className={`text-[10px] px-1.5 py-0.2 rounded-full ${
                  selectedYear === 'tajweed-lil-ulama' ? 'bg-sky-900/60 text-sky-100' : 'bg-sky-200/80 dark:bg-sky-900 text-sky-900 dark:text-sky-200'
                }`}>
                  {ALL_DARS_NIZAMI_BOOKS.filter(b => b.classLevel === 'tajweed-lil-ulama').length}
                </span>
              </button>

              <button
                onClick={() => setSelectedYear('fatawa')}
                className={`px-3.5 py-2 rounded-xl text-xs font-bold whitespace-nowrap transition-all flex items-center gap-1.5 ${
                  selectedYear === 'fatawa'
                    ? 'bg-indigo-700 text-white shadow-md shadow-indigo-700/20'
                    : 'bg-indigo-50 dark:bg-indigo-950/40 text-indigo-800 dark:text-indigo-300 border border-indigo-300/60 dark:border-indigo-800 hover:bg-indigo-100'
                }`}
              >
                <span>فتاویٰ و فقہ</span>
                <span className={`text-[10px] px-1.5 py-0.2 rounded-full ${
                  selectedYear === 'fatawa' ? 'bg-indigo-900/60 text-indigo-100' : 'bg-indigo-200/80 dark:bg-indigo-900 text-indigo-900 dark:text-indigo-200'
                }`}>
                  {ALL_DARS_NIZAMI_BOOKS.filter(b => b.classLevel === 'fatawa').length}
                </span>
              </button>
            </div>
          </div>

          {/* Secondary Filters: Subject Category & Book Type */}
          <div className="pt-2 border-t border-slate-100 dark:border-slate-800 flex flex-col md:flex-row items-stretch md:items-center justify-between gap-4">
            {/* Categories */}
            <div className="flex items-center gap-2 flex-wrap">
              <span className="text-xs font-semibold text-slate-500 dark:text-slate-400 shrink-0">
                موضوع (Subject):
              </span>
              <button
                onClick={() => setSelectedCategory('all')}
                className={`px-2.5 py-1 rounded-lg text-xs font-medium transition-colors ${
                  selectedCategory === 'all'
                    ? 'bg-amber-600 text-white'
                    : 'bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300 hover:bg-slate-200'
                }`}
              >
                سب مضامین
              </button>
              {categories.map((cat) => (
                <button
                  key={cat}
                  onClick={() => setSelectedCategory(cat)}
                  className={`px-2.5 py-1 rounded-lg text-xs font-medium transition-colors ${
                    selectedCategory === cat
                      ? 'bg-amber-600 text-white'
                      : 'bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300 hover:bg-slate-200'
                  }`}
                >
                  {cat}
                </button>
              ))}
            </div>

            {/* Type Filter */}
            <div className="flex items-center gap-1.5 shrink-0 flex-wrap">
              <span className="text-xs font-semibold text-slate-500 dark:text-slate-400 shrink-0">
                نوعیت:
              </span>
              {[
                { id: 'all', label: 'تمام' },
                { id: 'main', label: 'اصل کتب' },
                { id: 'sharh', label: 'شروحات' },
                { id: 'translation', label: 'تراجم' },
                { id: 'notes', label: 'درسی تقاریر' },
              ].map((t) => (
                <button
                  key={t.id}
                  onClick={() => setSelectedType(t.id)}
                  className={`px-2.5 py-1 rounded-lg text-xs font-semibold transition-colors ${
                    selectedType === t.id
                      ? 'bg-emerald-800 text-white'
                      : 'bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400 hover:bg-slate-200'
                  }`}
                >
                  {t.label}
                </button>
              ))}
            </div>
          </div>
        </div>

        {/* Selected Year Meta Banner (if specific year selected) */}
        {currentYearMeta && (
          <div className="bg-gradient-to-r from-emerald-900 to-teal-950 text-white rounded-3xl p-5 sm:p-6 shadow-md border border-emerald-700/50 flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
            <div className="space-y-1">
              <div className="flex items-center gap-2">
                <span className="px-2.5 py-0.5 rounded-full text-xs font-bold bg-amber-400 text-emerald-950">
                  {currentYearMeta.badge}
                </span>
                <h3 className="text-xl font-bold font-urdu" dir="rtl">
                  {currentYearMeta.nameUrdu}
                </h3>
              </div>
              <p className="text-xs sm:text-sm text-emerald-100/90 font-urdu pt-1" dir="rtl">
                {currentYearMeta.descriptionUrdu}
              </p>
              <p className="text-[11px] text-emerald-300">
                {currentYearMeta.descriptionEnglish}
              </p>
            </div>

            <div className="shrink-0 flex items-center gap-2 bg-emerald-800/60 px-4 py-2 rounded-2xl border border-emerald-600/40">
              <BookMarked className="w-5 h-5 text-amber-300" />
              <div className="text-center">
                <div className="text-lg font-extrabold text-amber-300 leading-none">
                  {currentYearMeta.totalBooks}
                </div>
                <div className="text-[10px] text-emerald-200 uppercase font-semibold">
                  مجموعی کتب
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Results Bar */}
        <div className="flex items-center justify-between gap-3 text-xs sm:text-sm font-semibold text-slate-600 dark:text-slate-300 px-1">
          <div className="flex items-center gap-2">
            <span>دستیاب کتب و شروحات:</span>
            <span className="px-2.5 py-0.5 rounded-full bg-emerald-100 dark:bg-emerald-950 text-emerald-800 dark:text-emerald-300 font-bold border border-emerald-300 dark:border-emerald-800">
              {filteredBooks.length} کتب
            </span>
          </div>

          {(selectedYear !== 'all' || selectedCategory !== 'all' || selectedType !== 'all' || searchQuery) && (
            <button
              onClick={clearAllFilters}
              className="text-xs text-rose-600 dark:text-rose-400 hover:underline flex items-center gap-1 font-bold"
            >
              <X className="w-3.5 h-3.5" />
              <span>تمام فلٹرز ختم کریں</span>
            </button>
          )}
        </div>

        {/* Books Grid */}
        {filteredBooks.length > 0 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4 sm:gap-5">
            {filteredBooks.map((book, idx) => (
              <BookCard
                key={`${book.id}-${idx}`}
                book={book}
                onSelectForView={(b) => setPreviewBook(b)}
              />
            ))}
          </div>
        ) : (
          <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-3xl p-12 text-center space-y-4">
            <BookOpen className="w-12 h-12 mx-auto text-slate-300 dark:text-slate-600" />
            <h3 className="text-lg font-bold text-slate-800 dark:text-slate-200 font-urdu">
              کوئی کتاب یا شرح نہیں ملی
            </h3>
            <p className="text-xs text-slate-500 dark:text-slate-400 max-w-md mx-auto">
              آپ کے مطلوبہ الفاظ یا فلٹرز کے مطابق کوئی درسی کتاب تلاش نہیں ہو سکی۔ براہِ کرم فلٹرز تبدیل کریں یا تمام درجات کا انتخاب کریں۔
            </p>
            <button
              onClick={clearAllFilters}
              className="px-4 py-2 rounded-xl text-xs font-bold bg-emerald-700 hover:bg-emerald-800 text-white transition-colors"
            >
              تمام فلٹرز ری سیٹ کریں
            </button>
          </div>
        )}

        {/* Sponsored Banner Unit */}
        <div className="pt-6">
          <AdsterraBanner />
        </div>

        {/* Mobile App Callout Banner */}
        <section className="mt-14 bg-gradient-to-br from-slate-900 via-emerald-950 to-slate-900 text-white rounded-3xl p-6 sm:p-10 border border-emerald-800/40 shadow-xl space-y-6">
          <div className="flex flex-col md:flex-row items-center justify-between gap-6">
            <div className="space-y-2 text-center md:text-right" dir={isRtl ? 'rtl' : 'ltr'}>
              <span className="inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-bold bg-amber-400 text-slate-950">
                <Sparkles className="w-3.5 h-3.5" />
                <span>اینڈرائیڈ موبائل ایپ میں شامل</span>
              </span>
              <h2 className="text-2xl sm:text-3xl font-extrabold tracking-tight font-urdu">
                یہ تمام کتب و شروحات اپنے اینڈرائیڈ فون پر حاصل کریں
              </h2>
              <p className="text-xs sm:text-sm text-slate-300 max-w-2xl">
                بیت العلم AI موبائل ایپلیکیشن (ورژن {APP_CONFIG.version}) میں مکمل 8 سالہ درسِ نظامی کا نصاب، آف لائن مطالعہ، بک مارکس، فقہی اور لغوی کوئزز اور اسمارٹ تعلیمی اوزار ایک ہی جگہ میسر ہیں۔
              </p>
            </div>

            <div className="shrink-0 flex flex-col items-center gap-3">
              <DownloadButton variant="hero" size="md" showDetails />
              <span className="text-[11px] text-emerald-300/80">
                حجم: {APP_CONFIG.fileSize} • مفت انسٹالیشن
              </span>
            </div>
          </div>
        </section>
      </div>

      {/* PDF Modal Viewer */}
      <PdfModal
        book={previewBook}
        onClose={() => setPreviewBook(null)}
      />
    </div>
  );
};
