import React, { useState, useMemo, useEffect } from 'react';
import { Link } from 'react-router-dom';
import {
  BookOpen,
  Search,
  Filter,
  Download,
  CheckCircle2,
  Sparkles,
  ArrowRight,
  ArrowLeft,
  BookMarked,
  X,
  Layers,
  GraduationCap
} from 'lucide-react';
import { QURAN_EDITIONS, QuranEditionItem } from '../data/quranEditions';
import { BookCard } from '../components/library/BookCard';
import { PdfModal } from '../components/library/PdfModal';
import { SectionHeader } from '../components/common/SectionHeader';
import { IslamicPatternBg } from '../components/layout/IslamicPatternBg';
import { useLanguage } from '../context/LanguageContext';
import { updatePageSEO } from '../utils/seo';

export const QuranPage: React.FC = () => {
  const { isRtl } = useLanguage();
  const [selectedLines, setSelectedLines] = useState<string>('all');
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [previewBook, setPreviewBook] = useState<QuranEditionItem | null>(null);

  useEffect(() => {
    updatePageSEO({
      title: 'مستند مطبوعہ نسخہ جاتِ قرآن مجید (۱۶، ۱۵، ۱۳، ۱۰، ۱۱ سطری)',
      description:
        'قرآن مجید کے معتبر و خوبصورت مطبوعہ نسخے (۱۶ سطری تاج کمپنی، ۱۵ سطری سعودی/حفاظی، ۱۳ سطری، لفظی ترجمہ و رنگین تجویدی نسخے)۔ ہائی ریزولیوشن پی ڈی ایف مفت ڈاؤن لوڈ کریں۔',
      keywords: [
        'قرآن مجید پی ڈی ایف',
        '۱۶ سطری قرآن پاک',
        '۱۵ سطری حفاظی قرآن',
        '۱۳ سطری قرآن مجید',
        'تجویدی قرآن پاک',
        'تاج کمپنی قرآن پاک',
        'Quran PDF 16 line',
        'Quran PDF 15 line',
        'Quran Majeed PDF Download',
        'بیت العلم قرآن'
      ],
      canonicalUrl: 'https://bait-ul-ilm-web.ai.studio/quran'
    });
    window.scrollTo(0, 0);
  }, []);

  // Line filters available
  const lineOptions = [
    { id: 'all', label: 'تمام نسخے', count: QURAN_EDITIONS.length },
    { id: '16', label: '۱۶ سطری', count: QURAN_EDITIONS.filter((q) => q.lines === 16).length },
    { id: '15', label: '۱۵ سطری حفاظی', count: QURAN_EDITIONS.filter((q) => q.lines === 15).length },
    { id: '13', label: '۱۳ سطری', count: QURAN_EDITIONS.filter((q) => q.lines === 13).length },
    { id: '10', label: '۱۰ سطری', count: QURAN_EDITIONS.filter((q) => q.lines === 10).length },
    { id: '11', label: '۱۱ سطری', count: QURAN_EDITIONS.filter((q) => q.lines === 11).length },
    { id: '14', label: '۱۴ سطری', count: QURAN_EDITIONS.filter((q) => q.lines === 14).length },
    { id: '17', label: '۱۷ سطری', count: QURAN_EDITIONS.filter((q) => q.lines === 17).length },
    { id: '18', label: '۱۸ سطری', count: QURAN_EDITIONS.filter((q) => q.lines === 18).length },
    { id: '21', label: '۲۱ سطری', count: QURAN_EDITIONS.filter((q) => q.lines === 21).length }
  ];

  // Filtered Quran list
  const filteredEditions = useMemo(() => {
    return QURAN_EDITIONS.filter((item) => {
      // Lines filter
      if (selectedLines !== 'all' && item.lines.toString() !== selectedLines) {
        return false;
      }

      // Search query
      if (searchQuery.trim()) {
        const q = searchQuery.toLowerCase().trim();
        const matchName = item.name.toLowerCase().includes(q);
        const matchNameUrdu = item.nameUrdu.toLowerCase().includes(q);
        const matchPublisher = item.publisher.toLowerCase().includes(q);
        const matchFeatures = (item.featuresUrdu || '').toLowerCase().includes(q);
        if (!matchName && !matchNameUrdu && !matchPublisher && !matchFeatures) {
          return false;
        }
      }

      return true;
    });
  }, [selectedLines, searchQuery]);

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 pt-20 pb-20 space-y-8" dir={isRtl ? 'rtl' : 'ltr'}>
      {/* Quranic Hero Header */}
      <section className="relative bg-gradient-to-b from-[#091510] via-[#0f241c] to-[#091510] text-white py-14 sm:py-18 overflow-hidden border-b border-amber-500/30 shadow-lg">
        <IslamicPatternBg variant="hero" />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-6">
          {/* Breadcrumb Navigation */}
          <div className="flex items-center justify-between border-b border-white/10 pb-4">
            <div className="flex items-center gap-2 text-xs font-bold text-emerald-300 font-urdu">
              <Link to="/" className="hover:text-amber-300 transition-colors">
                ہوم
              </Link>
              <span>/</span>
              <span className="text-amber-300">قرآن مجید نسخہ جات</span>
            </div>

            <Link
              to="/dars-e-nizami"
              className="inline-flex items-center gap-1.5 text-xs font-bold text-amber-300 hover:text-white font-urdu transition-colors"
            >
              <GraduationCap className="w-3.5 h-3.5" />
              <span>درسِ نظامی نصاب پر جائیں</span>
            </Link>
          </div>

          <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
            <div className="space-y-3 max-w-3xl">
              <div className="inline-flex items-center gap-2 px-3.5 py-1 rounded-full bg-amber-400/20 border border-amber-400/40 text-amber-300 text-xs font-bold font-urdu">
                <BookOpen className="w-4 h-4 text-amber-400" />
                <span>مستند مطبوعہ مصاحف و تجویدی نسخہ جات</span>
              </div>
              <h1 className="text-2xl sm:text-3xl lg:text-4xl font-black text-white font-urdu tracking-tight leading-tight">
                قرآن مجید: ۱۶، ۱۵، ۱۳، ۱۰ اور دیگر سطوری نسخے
              </h1>
              <p className="text-xs sm:text-sm text-emerald-100/90 font-urdu leading-relaxed">
                قرآن پاک کے تمام مستند طباعتی و تجویدی نسخے، جن میں حفاظ کرام کے لیے ۱۵ اور ۱۶ سطری، اور عام تلاوت کے لیے ۱۰، ۱۱، ۱۳، ۱۴، ۱۷، ۱۸ اور ۲۱ سطری مکمل رنگین مصاحف شامل ہیں — براہِ راست پی ڈی ایف مطالعہ اور ڈاؤن لوڈ لنکس کے ساتھ۔
              </p>
            </div>

            {/* Quick Metrics */}
            <div className="flex items-center gap-3 shrink-0 flex-wrap">
              <div className="px-5 py-3 rounded-2xl bg-black/40 border border-white/15 backdrop-blur-md text-center">
                <span className="block text-2xl font-black text-amber-400 font-mono">
                  {QURAN_EDITIONS.length}
                </span>
                <span className="text-xs text-slate-300 font-urdu">مستند طباعتی نسخے</span>
              </div>
              <div className="px-5 py-3 rounded-2xl bg-black/40 border border-white/15 backdrop-blur-md text-center">
                <span className="block text-2xl font-black text-emerald-400 font-mono">
                  100%
                </span>
                <span className="text-xs text-slate-300 font-urdu">مفت و تیز رفتار PDF</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Main Content Area */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-6">
        {/* Search & Lines Selector Box */}
        <div className="p-5 sm:p-6 rounded-3xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 shadow-sm space-y-5">
          {/* Search Box */}
          <div className="relative">
            <Search className="w-5 h-5 absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none" />
            <input
              id="quran-mushaf-search"
              name="quranSearch"
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="قرآن مجید کا مطلوبہ نسخہ تلاش کریں (مثلاً: ۱۶ سطری، ۱۵ سطری، تجویدی، اقراء، حفاظ)..."
              className="w-full pl-10 pr-12 py-3.5 rounded-2xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 text-xs sm:text-sm font-urdu focus:outline-none focus:ring-2 focus:ring-amber-400 transition-all text-right"
              dir="rtl"
            />
            {searchQuery && (
              <button
                onClick={() => setSearchQuery('')}
                className="absolute left-3.5 top-1/2 -translate-y-1/2 p-1 text-slate-400 hover:text-slate-600 dark:hover:text-slate-200"
              >
                <X className="w-4 h-4" />
              </button>
            )}
          </div>

          {/* Lines Filter Pills */}
          <div className="space-y-2">
            <div className="flex items-center justify-between text-xs font-bold text-slate-500 dark:text-slate-400 font-urdu">
              <span>سطور کی تعداد منتخب کریں (Select Lines):</span>
              <span className="text-amber-600 dark:text-amber-400 font-bold">
                دستیاب نسخے: {filteredEditions.length}
              </span>
            </div>

            <div className="flex items-center gap-2 overflow-x-auto pb-2 scrollbar-none flex-nowrap sm:flex-wrap">
              {lineOptions.map((opt) => {
                const isActive = selectedLines === opt.id;
                return (
                  <button
                    key={opt.id}
                    onClick={() => setSelectedLines(opt.id)}
                    className={`px-3.5 py-2 rounded-xl text-xs font-bold font-urdu whitespace-nowrap transition-all flex items-center gap-1.5 ${
                      isActive
                        ? 'bg-amber-400 text-slate-950 font-black shadow-md'
                        : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700'
                    }`}
                  >
                    <span>{opt.label}</span>
                    <span className={`text-[10px] px-1.5 py-0.2 rounded-full ${
                      isActive ? 'bg-slate-900/40 text-white' : 'bg-slate-200 dark:bg-slate-700 text-slate-600 dark:text-slate-400'
                    }`}>
                      {opt.count}
                    </span>
                  </button>
                );
              })}
            </div>
          </div>
        </div>

        {/* Editions Grid */}
        {filteredEditions.length > 0 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5 sm:gap-6">
            {filteredEditions.map((edition, idx) => (
              <BookCard
                key={`${edition.id}-${idx}`}
                book={edition}
                onSelectForView={(b) => setPreviewBook(edition)}
              />
            ))}
          </div>
        ) : (
          <div className="py-16 text-center bg-white dark:bg-slate-900 rounded-3xl border border-slate-200 dark:border-slate-800 p-8 space-y-4">
            <BookOpen className="w-12 h-12 text-slate-400 mx-auto" />
            <h4 className="text-lg font-bold text-slate-800 dark:text-slate-200 font-urdu">
              کوئی نسخہ نہیں ملا
            </h4>
            <p className="text-sm text-slate-500 dark:text-slate-400 font-urdu max-w-md mx-auto">
              آپ کے فلٹر کے مطابق کوئی نسخہ دستیاب نہیں ہے۔ برائے مہربانی تلاش ختم کریں یا تمام نسخے دیکھیں۔
            </p>
            <button
              onClick={() => {
                setSelectedLines('all');
                setSearchQuery('');
              }}
              className="inline-flex items-center gap-2 px-5 py-2.5 rounded-xl text-sm font-bold bg-amber-500 hover:bg-amber-600 text-slate-950 font-urdu"
            >
              تمام نسخے دکھائیں
            </button>
          </div>
        )}
      </main>

      {/* PDF Modal Viewer */}
      {previewBook && (
        <PdfModal
          book={previewBook}
          onClose={() => setPreviewBook(null)}
        />
      )}
    </div>
  );
};
