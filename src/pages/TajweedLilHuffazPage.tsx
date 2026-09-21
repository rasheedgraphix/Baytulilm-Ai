import React, { useState, useMemo, useEffect } from 'react';
import { Link } from 'react-router-dom';
import {
  Sparkles,
  Search,
  Download,
  CheckCircle2,
  ArrowRight,
  ArrowLeft,
  X,
  BookOpen,
  Layers,
  GraduationCap,
  ShieldCheck,
  Languages
} from 'lucide-react';
import { TAJWEED_LIL_HUFFAZ_BOOKS } from '../data/tajweedLilHuffazBooks';
import { DarsNizamiBookItem } from '../types';
import { BookCard } from '../components/library/BookCard';
import { PdfModal } from '../components/library/PdfModal';
import { IslamicPatternBg } from '../components/layout/IslamicPatternBg';
import { useLanguage } from '../context/LanguageContext';
import { updatePageSEO } from '../utils/seo';

export const TajweedLilHuffazPage: React.FC = () => {
  const { isRtl } = useLanguage();
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedSubCategory, setSelectedSubCategory] = useState<string>('all');
  const [previewBook, setPreviewBook] = useState<DarsNizamiBookItem | null>(null);

  useEffect(() => {
    updatePageSEO({
      title: 'تجوید للحفاظ (خلاصة التجوید، جمال القرآن، فوائد مکیہ، تعلیم الاسلام)',
      description:
        'حفاظِ کرام کے لیے تجوید و قراءت، اوقاف، علم الصرف اور بنیادی اسلامیات (تعلیم الاسلام) کا مکمل نصاب و شروحات۔ آن لائن مطالعہ و پی ڈی ایف ڈاؤن لوڈ کریں۔',
      keywords: [
        'تجوید للحفاظ',
        'خلاصة التجوید پی ڈی ایف',
        'جمال القرآن اشرف علی تھانوی',
        'ہدیۃ العثمانی شرح جمال القرآن',
        'علم الصرف اولین البشریٰ',
        'تعلیم الاسلام مفتی کفایت اللہ',
        'فوائد مکیہ البشریٰ',
        'جامع الوقف فی علم التجوید',
        'توضیح الوقف شرح جامع الوقف',
        'Tajweed Lil Huffaz PDF',
        'Khulasa tut Tajweed PDF',
        'Taleem ul Islam PDF'
      ],
      canonicalUrl: 'https://bait-ul-ilm-web.ai.studio/tajweed-lil-huffaz'
    });
    window.scrollTo(0, 0);
  }, []);

  // Distinct sub-categories
  const subCategories = useMemo(() => {
    const list = [
      { id: 'all', label: 'تمام کتبِ تجوید و نصاب', count: TAJWEED_LIL_HUFFAZ_BOOKS.length },
      { id: 'تجوید', label: 'قواعد التجوید و قراءت', count: TAJWEED_LIL_HUFFAZ_BOOKS.filter(b => b.category.includes('تجوید')).length },
      { id: 'وقف', label: 'قواعد الوقف والابتداء', count: TAJWEED_LIL_HUFFAZ_BOOKS.filter(b => b.category.includes('وقف')).length },
      { id: 'صرف', label: 'علم الصرف و گردانیں', count: TAJWEED_LIL_HUFFAZ_BOOKS.filter(b => b.category.includes('صرف')).length },
      { id: 'تعلیم الاسلام', label: 'تعلیم الاسلام (عقائد و فقہ)', count: TAJWEED_LIL_HUFFAZ_BOOKS.filter(b => b.name.toLowerCase().includes('taleem') || (b.nameUrdu || '').includes('تعلیم')).length },
    ];
    return list;
  }, []);

  // Filtered books
  const filteredBooks = useMemo(() => {
    return TAJWEED_LIL_HUFFAZ_BOOKS.filter((book) => {
      // Sub-category filter
      if (selectedSubCategory !== 'all') {
        if (selectedSubCategory === 'تعلیم الاسلام') {
          const isTaleem = book.name.toLowerCase().includes('taleem') || (book.nameUrdu || '').includes('تعلیم');
          if (!isTaleem) return false;
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
      <section className="relative bg-gradient-to-b from-teal-950 via-emerald-950 to-slate-950 text-white py-14 sm:py-18 overflow-hidden shadow-xl border-b border-teal-500/20">
        <IslamicPatternBg variant="hero" />
        
        {/* Ambient Glow */}
        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-96 h-96 bg-teal-500/15 blur-[120px] rounded-full pointer-events-none" />
        
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-5 text-center">
          {/* Breadcrumb Navigation */}
          <div className="flex items-center justify-center gap-2 text-xs text-teal-300/80 font-urdu">
            <Link to="/" className="hover:text-white transition-colors">بیت العلم پورٹل</Link>
            <span>/</span>
            <span className="text-teal-200 font-bold">تجوید للحفاظ (Tajweed Lil Huffaz)</span>
          </div>

          <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-teal-500/15 border border-teal-400/40 text-teal-300 text-xs sm:text-sm font-urdu font-bold shadow-inner">
            <Sparkles className="w-4 h-4 text-teal-400" />
            <span>نصابِ تجوید، اوقاف، صرف و بنیادی اسلامی تعلیمات برائے حفاظ</span>
          </div>

          <h1 className="text-3xl sm:text-4xl md:text-5xl font-black tracking-tight font-arabic text-transparent bg-clip-text bg-gradient-to-r from-teal-100 via-emerald-100 to-amber-200">
            شعبہ تجوید للحفاظ
          </h1>

          <p className="max-w-3xl mx-auto text-sm sm:text-base text-slate-300 font-urdu leading-relaxed">
            حفاظِ کرام کے لیے ضروری کتب کا مستند ذخیرہ — مخارج و صفات کے لیے <strong>خلاصة التجوید</strong>، حکیم الامت مولانا اشرف علی تھانویؒ کی <strong>جمال القرآن</strong> مع مستند شرح، قاری عبد الرحمن الہ آبادیؒ کی <strong>فوائد مکیہ</strong>، قواعدِ وقف کے لیے <strong>جامع الوقف</strong>، <strong>علم الصرف اولین</strong> مع شرح اور مفتی اعظم مفتی کفایت اللہ دہلویؒ کی <strong>تعلیم الاسلام</strong> کے مکمل چاروں حصے۔
          </p>

          <div className="pt-2 flex items-center justify-center gap-3 flex-wrap text-xs text-teal-200 font-urdu">
            <div className="flex items-center gap-1.5 bg-teal-900/60 px-3.5 py-1.5 rounded-full border border-teal-500/30">
              <BookOpen className="w-4 h-4 text-teal-300" />
              <span>{TAJWEED_LIL_HUFFAZ_BOOKS.length} کتب و مستند شروحات</span>
            </div>
            <div className="flex items-center gap-1.5 bg-teal-900/60 px-3.5 py-1.5 rounded-full border border-teal-500/30">
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
              id="tajweed-search-input"
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="تجوید کی کتاب، مصنف یا موضوع تلاش کریں (مثلاً: خلاصۃ التجوید، جمال القرآن، فوائد مکیہ، جامع الوقف، تعلیم الاسلام، صرف)..."
              className="w-full pl-10 pr-12 py-3.5 rounded-2xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 text-sm font-urdu focus:outline-none focus:ring-2 focus:ring-teal-500 transition-all text-right"
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
                موضوع و فن کے لحاظ سے منتخب کریں:
              </span>
              <span className="text-xs text-teal-700 dark:text-teal-400 font-bold font-urdu">
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
                        ? 'bg-gradient-to-r from-teal-700 to-emerald-800 text-white shadow-md shadow-teal-900/30 ring-2 ring-teal-500/40'
                        : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700'
                    }`}
                  >
                    <span>{cat.label}</span>
                    <span className={`text-[10px] px-1.5 py-0.5 rounded-full font-mono ${
                      isActive
                        ? 'bg-teal-950/70 text-teal-100'
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
              کتبِ تجوید و نصابِ حفاظ
            </h2>
            <span className="text-xs px-2.5 py-0.5 rounded-full bg-teal-100 dark:bg-teal-950/70 text-teal-800 dark:text-teal-300 font-bold border border-teal-200 dark:border-teal-800">
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
            <div className="w-14 h-14 rounded-full bg-teal-50 dark:bg-teal-950/60 text-teal-600 dark:text-teal-400 flex items-center justify-center mx-auto">
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
              className="px-4 py-2 rounded-xl bg-teal-700 text-white text-xs font-bold font-urdu hover:bg-teal-600 transition-colors"
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
