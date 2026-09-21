import React, { useState, useMemo, useEffect } from 'react';
import { Link } from 'react-router-dom';
import {
  Landmark,
  Search,
  Filter,
  Download,
  CheckCircle2,
  Sparkles,
  ArrowRight,
  ArrowLeft,
  X,
  BookOpen,
  Calendar,
  Layers,
  History
} from 'lucide-react';
import { TAREEKH_BOOKS } from '../data/tareekhBooks';
import { DarsNizamiBookItem } from '../types';
import { BookCard } from '../components/library/BookCard';
import { PdfModal } from '../components/library/PdfModal';
import { IslamicPatternBg } from '../components/layout/IslamicPatternBg';
import { useLanguage } from '../context/LanguageContext';
import { updatePageSEO } from '../utils/seo';

export const TareekhPage: React.FC = () => {
  const { isRtl } = useLanguage();
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedSubCategory, setSelectedSubCategory] = useState<string>('all');
  const [previewBook, setPreviewBook] = useState<DarsNizamiBookItem | null>(null);

  useEffect(() => {
    updatePageSEO({
      title: 'تاریخ و سیرت کتب خانہ (تاریخ طبری، ابن خلدون، تاریخ اسلام، قصص القرآن)',
      description:
        'تاریخِ اسلام، سیرتِ طیبہ، قصص الانبیاء، امہات التاریخ (تاریخ طبری، تاریخ ابن خلدون)، تاریخ حرمین شریفین، اور اکابرین کی مستند تاریخی کتب کا مکمل ذخیرہ۔ آن لائن مطالعہ و ڈاؤن لوڈ کریں۔',
      keywords: [
        'تاریخ اسلام پی ڈی ایف',
        'تاریخ طبری اردو',
        'تاریخ ابن خلدون اردو',
        'قصص القرآن حفظ الرحمن سیوہاروی',
        'تاریخ مکہ مکرمہ',
        'تاریخ مدینہ منورہ',
        'تحریک پاکستان اور علمائے دیوبند',
        'خلافت راشدہ کے ۳۰ سال',
        'Islamic History Books PDF',
        'Tareekh e Tabari Urdu PDF',
        'Tareekh Ibn e Khaldoon Urdu PDF'
      ],
      canonicalUrl: 'https://bait-ul-ilm-web.ai.studio/tareekh'
    });
    window.scrollTo(0, 0);
  }, []);

  // Distinct sub-categories
  const subCategories = useMemo(() => {
    const list = [
      { id: 'all', label: 'تمام تاریخی کتب', count: TAREEKH_BOOKS.length },
      { id: 'امہات التاریخ', label: 'امہات التاریخ (طبری و ابن خلدون)', count: TAREEKH_BOOKS.filter(b => b.category.includes('امہات')).length },
      { id: 'تاریخ حرمین', label: 'تاریخِ حرمین و مقامات مقدسہ', count: TAREEKH_BOOKS.filter(b => b.category.includes('حرمین')).length },
      { id: 'قصص الانبیاء', label: 'قصص الانبیاء و تاریخِ قرآن', count: TAREEKH_BOOKS.filter(b => b.category.includes('قصص') || b.category.includes('قرآن')).length },
      { id: 'تاریخِ صحابہ', label: 'تاریخِ صحابہ و خلافت راشدہ', count: TAREEKH_BOOKS.filter(b => b.category.includes('صحابہ') || b.category.includes('خلافت')).length },
      { id: 'شخصیات و سیرت', label: 'شخصیات، سوانح و سیرت', count: TAREEKH_BOOKS.filter(b => b.category.includes('شخصیات') || b.category.includes('سیرت')).length },
      { id: 'تاریخ ہند', label: 'تاریخ ہند و تحریکاتِ آزادی', count: TAREEKH_BOOKS.filter(b => b.category.includes('ہند') || b.category.includes('تحریک')).length },
      { id: 'تاریخِ دارالعلوم', label: 'تاریخِ دارالعلوم دیوبند و مدارس', count: TAREEKH_BOOKS.filter(b => b.category.includes('دارالعلوم') || b.category.includes('مدارس')).length }
    ];
    return list;
  }, []);

  const displayedBooks = useMemo(() => {
    return TAREEKH_BOOKS.filter((book) => {
      // Sub-category filter
      if (selectedSubCategory !== 'all') {
        if (!book.category.includes(selectedSubCategory)) {
          return false;
        }
      }

      // Search query filter
      if (searchQuery.trim()) {
        const q = searchQuery.toLowerCase().trim();
        const matchName = book.name.toLowerCase().includes(q);
        const matchNameUrdu = (book.nameUrdu || '').toLowerCase().includes(q);
        const matchAuthor = (book.author || '').toLowerCase().includes(q);
        const matchEdition = (book.edition || '').toLowerCase().includes(q);
        const matchCat = (book.category || '').toLowerCase().includes(q);
        if (!matchName && !matchNameUrdu && !matchAuthor && !matchEdition && !matchCat) {
          return false;
        }
      }
      return true;
    });
  }, [searchQuery, selectedSubCategory]);

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 pt-20 pb-20 space-y-8" dir={isRtl ? 'rtl' : 'ltr'}>
      {/* Header Banner */}
      <section className="relative bg-gradient-to-b from-[#240b07] via-[#36130c] to-[#1c0805] text-white py-14 sm:py-18 overflow-hidden border-b border-amber-600/30 shadow-lg">
        <IslamicPatternBg variant="subtle" />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-6">
          {/* Breadcrumb Navigation */}
          <div className="flex items-center justify-between border-b border-white/10 pb-4">
            <div className="flex items-center gap-2 text-xs font-bold text-amber-300 font-urdu">
              <Link to="/" className="hover:text-amber-200 transition-colors">
                ہوم
              </Link>
              <span>/</span>
              <span className="text-amber-400">تاریخ و سیرت</span>
            </div>

            <Link
              to="/library"
              className="inline-flex items-center gap-1.5 text-xs font-bold text-amber-300 hover:text-white font-urdu transition-colors"
            >
              <span>تمام کتب خانہ</span>
              {isRtl ? <ArrowLeft className="w-3.5 h-3.5" /> : <ArrowRight className="w-3.5 h-3.5" />}
            </Link>
          </div>

          <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
            <div className="space-y-3 max-w-3xl">
              <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-amber-500/20 text-amber-300 border border-amber-500/40 text-xs font-bold font-urdu">
                <Landmark className="w-3.5 h-3.5 text-amber-400" />
                <span>شعبہ تاریخِ اسلام، سیرت و سوانح (Islamic History & Heritage)</span>
              </div>

              <h1 className="text-2xl sm:text-4xl lg:text-5xl font-black text-white font-urdu tracking-tight leading-tight">
                تاریخ و سیرت کتب خانہ
              </h1>

              <p className="text-xs sm:text-sm md:text-base text-amber-100/90 font-urdu leading-relaxed">
                امہات التاریخ (تاریخ طبری، تاریخ ابن خلدون)، تاریخ مکہ و مدینہ، قصص القرآن، سیرتِ صحابہ و خلفائے راشدین، تاریخ دارالعلوم دیوبند، اور تحریکاتِ آزادی کی {TAREEKH_BOOKS.length} سے زائد نایاب کتب کا مستند آن لائن ذخیرہ۔
              </p>
            </div>

            {/* Quick Stats Block */}
            <div className="bg-amber-950/60 border border-amber-500/30 p-4 rounded-2xl flex flex-col items-center justify-center text-center shrink-0 min-w-[170px] backdrop-blur-sm">
              <History className="w-8 h-8 text-amber-400 mb-1" />
              <div className="text-2xl font-black text-white font-mono">{TAREEKH_BOOKS.length}</div>
              <div className="text-xs font-bold text-amber-200 font-urdu">مستند تاریخی کتب و مجلدات</div>
            </div>
          </div>
        </div>
      </section>

      {/* Main Content Area */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-6">
        {/* Search Bar and Sub-category Filters */}
        <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl p-4 sm:p-5 shadow-sm space-y-4">
          <div className="relative">
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="تاریخ کی کتاب، مؤلف، یا موضوع تلاش کریں (مثلاً: تاریخ طبری، ابن خلدون، مکہ، دیوبند، قصص القرآن)..."
              className="w-full bg-slate-50 dark:bg-slate-950 border border-slate-300 dark:border-slate-700 rounded-xl px-4 py-3 pl-11 text-sm font-urdu focus:outline-none focus:ring-2 focus:ring-amber-500 text-slate-900 dark:text-slate-100"
            />
            <Search className={`w-5 h-5 text-slate-400 absolute top-3.5 ${isRtl ? 'left-3' : 'right-3'}`} />
            {searchQuery && (
              <button
                onClick={() => setSearchQuery('')}
                className={`absolute top-3.5 ${isRtl ? 'right-3' : 'left-3'} text-slate-400 hover:text-slate-600 dark:hover:text-slate-200`}
              >
                <X className="w-5 h-5" />
              </button>
            )}
          </div>

          {/* Sub-Category Chips */}
          <div className="flex items-center gap-2 overflow-x-auto pb-1 scrollbar-thin">
            {subCategories.map((cat) => (
              <button
                key={cat.id}
                onClick={() => setSelectedSubCategory(cat.id)}
                className={`px-3 py-1.5 rounded-xl text-xs font-bold font-urdu whitespace-nowrap transition-all border shrink-0 ${
                  selectedSubCategory === cat.id
                    ? 'bg-amber-600 text-white border-amber-600 shadow-sm'
                    : 'bg-slate-100 dark:bg-slate-800/80 text-slate-700 dark:text-slate-300 border-slate-200 dark:border-slate-700 hover:bg-amber-50 dark:hover:bg-amber-950/30'
                }`}
              >
                {cat.label} ({cat.count})
              </button>
            ))}
          </div>
        </div>

        {/* Active Results Summary */}
        <div className="flex items-center justify-between px-1">
          <div className="text-xs sm:text-sm font-bold text-slate-600 dark:text-slate-400 font-urdu">
            دستیاب کتب: <span className="text-amber-700 dark:text-amber-400 font-mono font-bold text-base">{displayedBooks.length}</span>
          </div>
          {searchQuery && (
            <button
              onClick={() => setSearchQuery('')}
              className="text-xs font-bold text-amber-600 dark:text-amber-400 hover:underline font-urdu"
            >
              تلاش کا فلٹر ختم کریں
            </button>
          )}
        </div>

        {/* Books Grid */}
        {displayedBooks.length > 0 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 sm:gap-6">
            {displayedBooks.map((book) => (
              <BookCard key={book.id} book={book} onSelectForView={(b) => setPreviewBook(b)} />
            ))}
          </div>
        ) : (
          <div className="text-center py-16 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl p-8 space-y-3">
            <BookOpen className="w-12 h-12 text-slate-400 mx-auto" />
            <h3 className="text-lg font-bold font-urdu text-slate-800 dark:text-slate-200">
              کوئی کتاب نہیں ملی
            </h3>
            <p className="text-xs sm:text-sm font-urdu text-slate-500 dark:text-slate-400">
              براہِ کرم کسی دوسرے نام یا لفظ سے تلاش کریں۔
            </p>
          </div>
        )}
      </div>

      {/* Embedded PDF Modal */}
      {previewBook && (
        <PdfModal book={previewBook} onClose={() => setPreviewBook(null)} />
      )}
    </div>
  );
};
