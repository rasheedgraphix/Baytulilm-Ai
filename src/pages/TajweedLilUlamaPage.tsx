import React, { useState, useMemo, useEffect } from 'react';
import { Link } from 'react-router-dom';
import {
  GraduationCap,
  Search,
  Download,
  CheckCircle2,
  ArrowRight,
  ArrowLeft,
  X,
  BookOpen,
  Layers,
  Sparkles,
  ShieldCheck,
  Languages,
  FileCheck
} from 'lucide-react';
import { TAJWEED_LIL_ULAMA_BOOKS } from '../data/tajweedLilUlamaBooks';
import { DarsNizamiBookItem } from '../types';
import { BookCard } from '../components/library/BookCard';
import { PdfModal } from '../components/library/PdfModal';
import { IslamicPatternBg } from '../components/layout/IslamicPatternBg';
import { useLanguage } from '../context/LanguageContext';
import { updatePageSEO } from '../utils/seo';

export const TajweedLilUlamaPage: React.FC = () => {
  const { isRtl } = useLanguage();
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedSubCategory, setSelectedSubCategory] = useState<string>('all');
  const [previewBook, setPreviewBook] = useState<DarsNizamiBookItem | null>(null);

  useEffect(() => {
    updatePageSEO({
      title: 'تجوید للعلماء (المقدمة الجزرية، شروحات، معلم التجوید، علوم القرآن، پرچہ جات)',
      description:
        'شعبہ تجوید للعلماء و طالبات - مقدمۃ الجزریہ مع معتمد شروحات، تفہیم الوقوف، معلم التجوید، تحفہ رحیمیہ، علوم القرآن (مفتی تقی عثمانی)، صفحات فی علوم القراءات، دفاع قراءات اور وفاق کے پرچہ جات۔',
      keywords: [
        'تجوید للعلماء',
        'المقدمۃ الجزریۃ پی ڈی ایف',
        'الجواہر النقیۃ شرح الجزریۃ',
        'شرح الجزری اردو',
        'معلم التجوید',
        'تفہیم الوقوف',
        'علوم القرآن مفتی تقی عثمانی',
        'صفحات فی علوم القراءات',
        'دفاع قراءات',
        'پرچہ جات تجوید علماء عالمات وفاق المدارس',
        'Tajweed Lil Ulama PDF',
        'Muqaddamah Jazariyyah PDF',
        'Uloom ul Quran Taqi Usmani PDF'
      ],
      canonicalUrl: 'https://bait-ul-ilm-web.ai.studio/tajweed-lil-ulama'
    });
    window.scrollTo(0, 0);
  }, []);

  // Distinct sub-categories
  const subCategories = useMemo(() => {
    const list = [
      { id: 'all', label: 'تمام کتبِ تجوید للعلماء', count: TAJWEED_LIL_ULAMA_BOOKS.length },
      { id: 'جزری', label: 'متن الجزریہ و شروحات', count: TAJWEED_LIL_ULAMA_BOOKS.filter(b => b.name.toLowerCase().includes('jazr') || (b.nameUrdu || '').includes('جزر')).length },
      { id: 'قواعد', label: 'قواعد التجوید و رسائل', count: TAJWEED_LIL_ULAMA_BOOKS.filter(b => b.category.includes('قواعد') || b.category.includes('رسائل')).length },
      { id: 'علوم', label: 'علوم القرآن و القراءات', count: TAJWEED_LIL_ULAMA_BOOKS.filter(b => b.category.includes('علوم') || b.category.includes('دفاع')).length },
      { id: 'پرچہ', label: 'امتحانی پرچہ جات وفاق', count: TAJWEED_LIL_ULAMA_BOOKS.filter(b => b.category.includes('امتحان') || b.name.toLowerCase().includes('parcha')).length },
    ];
    return list;
  }, []);

  // Filtered books
  const filteredBooks = useMemo(() => {
    return TAJWEED_LIL_ULAMA_BOOKS.filter((book) => {
      // Sub-category filter
      if (selectedSubCategory !== 'all') {
        if (selectedSubCategory === 'جزری') {
          const isJazri = book.name.toLowerCase().includes('jazr') || (book.nameUrdu || '').includes('جزر');
          if (!isJazri) return false;
        } else if (selectedSubCategory === 'پرچہ') {
          const isParcha = book.category.includes('امتحان') || book.name.toLowerCase().includes('parcha');
          if (!isParcha) return false;
        } else if (selectedSubCategory === 'علوم') {
          const isUloom = book.category.includes('علوم') || book.category.includes('دفاع');
          if (!isUloom) return false;
        } else if (selectedSubCategory === 'قواعد') {
          const isQawaid = book.category.includes('قواعد') || book.category.includes('رسائل');
          if (!isQawaid) return false;
        } else if (!book.category.includes(selectedSubCategory)) {
          return false;
        }
      }

      // Search filter
      if (searchQuery.trim()) {
        const q = searchQuery.toLowerCase().trim();
        const matchName = book.name.toLowerCase().includes(q);
        const matchNameUrdu = (book.nameUrdu || '').toLowerCase().includes(q);
        const matchAuthor = (book.author || '').toLowerCase().includes(q);
        const matchCategory = book.category.toLowerCase().includes(q);
        if (!matchName && !matchNameUrdu && !matchAuthor && !matchCategory) {
          return false;
        }
      }

      return true;
    });
  }, [selectedSubCategory, searchQuery]);

  return (
    <div className="pt-24 pb-20 space-y-10 min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100" dir="rtl">
      {/* Header Banner */}
      <section className="relative bg-gradient-to-b from-sky-950 via-teal-950 to-slate-950 text-white py-14 sm:py-18 overflow-hidden shadow-xl border-b border-sky-500/20">
        <IslamicPatternBg variant="hero" />
        
        {/* Ambient Glow */}
        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-96 h-96 bg-sky-500/15 blur-[120px] rounded-full pointer-events-none" />
        
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-5 text-center">
          {/* Breadcrumb Navigation */}
          <div className="flex items-center justify-center gap-2 text-xs text-sky-300/80 font-urdu">
            <Link to="/" className="hover:text-white transition-colors">بیت العلم پورٹل</Link>
            <span>/</span>
            <span className="text-sky-200 font-bold">تجوید للعلماء (Tajweed Lil Ulama)</span>
          </div>

          <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-sky-500/15 border border-sky-400/40 text-sky-300 text-xs sm:text-sm font-urdu font-bold shadow-inner">
            <GraduationCap className="w-4 h-4 text-sky-400" />
            <span>نصابِ تجوید، متون، شروحات و علوم القرآن برائے درجاتِ علماء و عالمات</span>
          </div>

          <h1 className="text-3xl sm:text-4xl md:text-5xl font-black tracking-tight font-arabic text-transparent bg-clip-text bg-gradient-to-r from-sky-100 via-teal-100 to-amber-200">
            شعبہ تجوید للعلماء
          </h1>

          <p className="max-w-3xl mx-auto text-sm sm:text-base text-slate-300 font-urdu leading-relaxed">
            علماء و طالبات کے اعلیٰ درسی نصاب کے لیے مستند متون و شروحات کا جامع ذخیرہ — <strong>متن المقدمة الجزرية</strong> مع ۳ اہم شروحات (الجواہر النقیۃ، شرح الجزری، ہندی اردو شرح)، <strong>معلم التجوید</strong>، <strong>تفہیم الوقوف</strong>، <strong>تحفۂ رحیمیہ</strong>، شیخ الاسلام مفتی تقی عثمانی کی معرکہ آراء تصنیف <strong>علوم القرآن</strong>، <strong>صفحات فی علوم القراءات</strong>، <strong>دفاعِ قراءات</strong> اور وفاق المدارس کے <strong>سابقہ امتحانی پرچہ جات</strong>۔
          </p>

          <div className="pt-2 flex items-center justify-center gap-3 flex-wrap text-xs text-sky-200 font-urdu">
            <div className="flex items-center gap-1.5 bg-sky-900/60 px-3.5 py-1.5 rounded-full border border-sky-500/30">
              <BookOpen className="w-4 h-4 text-sky-300" />
              <span>{TAJWEED_LIL_ULAMA_BOOKS.length} تخصصی کتب و امتحانی شروحات</span>
            </div>
            <div className="flex items-center gap-1.5 bg-sky-900/60 px-3.5 py-1.5 rounded-full border border-sky-500/30">
              <Download className="w-4 h-4 text-amber-300" />
              <span>مفت تیز رفتار پی ڈی ایف مطالعہ و ڈاؤن لوڈ</span>
            </div>
          </div>
        </div>
      </section>

      {/* Main Content Area */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        
        {/* Search & Filter Bar */}
        <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-3xl p-5 sm:p-6 shadow-sm space-y-5">
          {/* Search Box */}
          <div className="relative">
            <Search className="w-5 h-5 absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none" />
            <input
              id="tajweed-ulama-search-input"
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="کتاب کا نام، شارح، متن یا موضوع تلاش کریں (مثلاً: جزریہ، معلم التجوید، علوم القرآن، تفہیم الوقوف، پرچہ جات)..."
              className="w-full pl-10 pr-12 py-3.5 rounded-2xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 text-sm font-urdu focus:outline-none focus:ring-2 focus:ring-sky-500 transition-all text-right"
              dir="rtl"
            />
            {searchQuery && (
              <button
                onClick={() => setSearchQuery('')}
                className="absolute left-3 top-1/2 -translate-y-1/2 p-1.5 text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 rounded-lg"
              >
                <X className="w-4 h-4" />
              </button>
            )}
          </div>

          {/* Sub-Category Filter Chips */}
          <div className="space-y-2">
            <div className="flex items-center justify-between gap-2">
              <span className="text-xs font-extrabold uppercase tracking-wider text-slate-500 dark:text-slate-400 font-urdu">
                موضوع و متن کے لحاظ سے منتخب کریں:
              </span>
              <span className="text-xs text-sky-700 dark:text-sky-400 font-bold font-urdu">
                موجود کتب: {filteredBooks.length}
              </span>
            </div>

            <div className="flex items-center gap-2 overflow-x-auto pb-2 scrollbar-none flex-nowrap sm:flex-wrap">
              {subCategories.map((cat) => {
                const isActive = selectedSubCategory === cat.id;
                return (
                  <button
                    key={cat.id}
                    onClick={() => setSelectedSubCategory(cat.id)}
                    className={`px-3.5 py-2 rounded-xl text-xs font-bold whitespace-nowrap transition-all flex items-center gap-2 font-urdu ${
                      isActive
                        ? 'bg-gradient-to-r from-sky-700 to-teal-800 text-white shadow-md shadow-sky-900/30 ring-2 ring-sky-500/40'
                        : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700'
                    }`}
                  >
                    <span>{cat.label}</span>
                    <span className={`text-[10px] px-1.5 py-0.5 rounded-full font-mono ${
                      isActive
                        ? 'bg-sky-950/70 text-sky-100'
                        : 'bg-slate-200 dark:bg-slate-700 text-slate-600 dark:text-slate-300'
                    }`}>
                      {cat.count}
                    </span>
                  </button>
                );
              })}
            </div>
          </div>
        </div>

        {/* Results Header */}
        <div className="flex items-center justify-between border-b border-slate-200 dark:border-slate-800 pb-3">
          <div className="flex items-center gap-2">
            <h2 className="text-lg sm:text-xl font-black text-slate-900 dark:text-slate-100 font-urdu">
              کتبِ تجوید و متون برائے علماء و طالبات
            </h2>
            <span className="text-xs px-2.5 py-0.5 rounded-full bg-sky-100 dark:bg-sky-950/70 text-sky-800 dark:text-sky-300 font-bold border border-sky-200 dark:border-sky-800">
              {filteredBooks.length} کتب دستیاب
            </span>
          </div>

          <div className="text-xs text-slate-500 dark:text-slate-400 font-urdu">
            براہِ راست آن لائن مطالعہ کریں یا ڈاؤن لوڈ کریں
          </div>
        </div>

        {/* Books Grid */}
        {filteredBooks.length > 0 ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {filteredBooks.map((book) => (
              <BookCard
                key={book.id}
                book={book}
                onPreview={(b) => setPreviewBook(b)}
              />
            ))}
          </div>
        ) : (
          <div className="text-center py-16 bg-white dark:bg-slate-900 rounded-3xl border border-dashed border-slate-300 dark:border-slate-800 p-8 space-y-4">
            <div className="w-14 h-14 rounded-full bg-sky-50 dark:bg-sky-950/60 text-sky-600 dark:text-sky-400 flex items-center justify-center mx-auto">
              <Search className="w-7 h-7" />
            </div>
            <h3 className="text-lg font-bold text-slate-800 dark:text-slate-200 font-urdu">
              کوئی کتاب نہیں ملی
            </h3>
            <p className="text-sm text-slate-500 dark:text-slate-400 font-urdu max-w-md mx-auto">
              آپ کی تلاش کردہ اصطلاح "{searchQuery}" کے مطابق کوئی کتاب دستیاب نہیں ہے۔ برائے مہربانی املا چیک کریں یا فلٹر تبدیل کریں۔
            </p>
            <button
              onClick={() => {
                setSearchQuery('');
                setSelectedSubCategory('all');
              }}
              className="px-4 py-2 rounded-xl bg-sky-700 text-white text-xs font-bold font-urdu hover:bg-sky-600 transition-colors"
            >
              تمام کتب دکھائیں
            </button>
          </div>
        )}
      </div>

      {/* PDF Reading Modal */}
      {previewBook && (
        <PdfModal
          book={previewBook}
          onClose={() => setPreviewBook(null)}
        />
      )}
    </div>
  );
};
