import React, { useState, useMemo, useRef, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { 
  Search, 
  Sparkles, 
  GraduationCap, 
  BookOpen, 
  BookMarked, 
  Languages, 
  ScrollText, 
  Radio, 
  ArrowLeft, 
  ArrowRight,
  ChevronRight,
  X,
  FileText,
  Download
} from 'lucide-react';
import { ALL_DARS_NIZAMI_BOOKS } from '../../data/darsNizamiBooks';
import { QURAN_EDITIONS } from '../../data/quranEditions';
import { TAFASEER_BOOKS } from '../../data/tafaseerBooks';
import { LUGHAT_BOOKS } from '../../data/lughatBooks';
import { FATAWA_BOOKS } from '../../data/fatawaBooks';
import { IslamicPatternBg } from '../layout/IslamicPatternBg';
import { useLanguage } from '../../context/LanguageContext';

export const PortalAcademicHero: React.FC = () => {
  const { isRtl, language } = useLanguage();
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState('');
  const [isSearchFocused, setIsSearchFocused] = useState(false);
  const searchContainerRef = useRef<HTMLDivElement>(null);

  // Close search dropdown on click outside
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (searchContainerRef.current && !searchContainerRef.current.contains(event.target as Node)) {
        setIsSearchFocused(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  // Instant Live Search across all portal books
  const searchResults = useMemo(() => {
    const q = searchQuery.trim().toLowerCase();
    if (!q || q.length < 2) return [];

    const results: Array<{
      id: string;
      title: string;
      category: string;
      author?: string;
      link: string;
      type: string;
    }> = [];

    // Search Dars-e-Nizami books
    for (const b of ALL_DARS_NIZAMI_BOOKS) {
      if (
        b.name.toLowerCase().includes(q) ||
        b.nameUrdu.toLowerCase().includes(q) ||
        (b.author || '').toLowerCase().includes(q)
      ) {
        results.push({
          id: b.id,
          title: b.nameUrdu || b.name,
          category: `درسِ نظامی (${b.classNameUrdu || 'عام'})`,
          author: b.author,
          link: `/dars-e-nizami/${b.classLevel || '1st'}`,
          type: b.typeUrdu || 'کتاب'
        });
        if (results.length >= 6) break;
      }
    }

    // Search Quran Editions
    if (results.length < 8) {
      for (const qb of QURAN_EDITIONS) {
        if ((qb.nameUrdu || '').toLowerCase().includes(q) || (qb.edition || '').toLowerCase().includes(q)) {
          results.push({
            id: qb.id,
            title: qb.nameUrdu || qb.name,
            category: 'قرآن مجید',
            author: qb.edition,
            link: '/quran',
            type: 'مصحف'
          });
        }
      }
    }

    // Search Tafaseer
    if (results.length < 8) {
      for (const tb of TAFASEER_BOOKS) {
        if ((tb.nameUrdu || '').toLowerCase().includes(q) || (tb.author || '').toLowerCase().includes(q)) {
          results.push({
            id: tb.id,
            title: tb.nameUrdu || tb.name,
            category: 'تفاسیرِ قرآن',
            author: tb.author,
            link: '/tafaseer',
            type: 'تفسیر'
          });
        }
      }
    }

    // Search Dictionaries
    if (results.length < 8) {
      for (const lb of LUGHAT_BOOKS) {
        if ((lb.nameUrdu || '').toLowerCase().includes(q) || (lb.author || '').toLowerCase().includes(q)) {
          results.push({
            id: lb.id,
            title: lb.nameUrdu || lb.name,
            category: 'معاجم و لغات',
            author: lb.author,
            link: '/lughat',
            type: 'لغت'
          });
        }
      }
    }

    return results.slice(0, 8);
  }, [searchQuery]);

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!searchQuery.trim()) return;
    navigate(`/library?q=${encodeURIComponent(searchQuery.trim())}`);
  };

  return (
    <section className="relative pt-28 pb-16 md:pt-36 md:pb-20 overflow-hidden bg-gradient-to-b from-[#051f18] via-[#072a21] to-[#041a14] text-white">
      {/* Islamic Background Lattice */}
      <IslamicPatternBg variant="subtle" />

      {/* Radiant Emerald & Gold Ambient Lights */}
      <div className="absolute top-10 left-1/2 -translate-x-1/2 w-full max-w-[600px] h-[350px] bg-emerald-500/15 blur-[120px] rounded-full pointer-events-none" />
      <div className="absolute top-1/4 right-0 sm:right-10 w-full max-w-[300px] h-[300px] bg-amber-500/10 blur-[100px] rounded-full pointer-events-none" />

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 text-center space-y-6">
        {/* Bismillah Calligraphy */}
        <div className="flex items-center justify-center gap-3">
          <span className="font-arabic font-serif text-xl sm:text-2xl md:text-3xl text-amber-300/90 tracking-widest drop-shadow">
            بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ
          </span>
        </div>

        {/* Official Islamic Portal Badge */}
        <div className="inline-flex items-center justify-center gap-2 px-4 py-1.5 rounded-full text-xs font-semibold bg-emerald-950/80 text-emerald-200 border border-emerald-500/30 shadow-lg backdrop-blur-md">
          <Sparkles className="w-3.5 h-3.5 text-amber-400" />
          <span className="font-urdu font-bold">بیت العلم AI — جامع اسلامی، قرآنی و درسی کتب خانہ</span>
        </div>

        {/* Main Dignified Academic Headline */}
        <div className="space-y-3 max-w-4xl mx-auto">
          <h1 className="text-3xl sm:text-4xl md:text-5xl lg:text-6xl font-black text-white font-urdu tracking-tight leading-tight drop-shadow-md">
            مرکزی دارالعلوم و کتب خانہ علومِ اسلامیہ
          </h1>
          <p className="text-sm sm:text-base md:text-lg text-emerald-100/90 font-urdu leading-relaxed max-w-3xl mx-auto px-2">
            درسِ نظامی کا مکمل ۸ سالہ نصاب (درجہ اولیٰ تا دورۂ حدیث شریف)، قرآن مجید کے تمام مطبوعہ مصاحف، معتبر تفاسیر، عربی و اردو لغات، فتاویٰ اور حرمین شریفین ۲۴/۷ لائیو نشریات۔
          </p>
        </div>

        {/* Universal Fast Search Bar */}
        <div ref={searchContainerRef} className="max-w-3xl mx-auto pt-2 relative">
          <form id="hero-academic-search-form" name="academicSearchForm" onSubmit={handleSearchSubmit} className="relative" role="search">
            <div className="relative flex items-center bg-white dark:bg-slate-900 rounded-2xl shadow-2xl border-2 border-emerald-500/60 focus-within:border-amber-400 p-2 transition-all">
              <label htmlFor="universal-academic-search" className="sr-only">پورٹل بھر میں تلاش کریں</label>
              <Search className="w-5 h-5 text-emerald-600 dark:text-emerald-400 ml-3 mr-1 shrink-0" aria-hidden="true" />
              <input
                id="universal-academic-search"
                name="searchQuery"
                type="text"
                value={searchQuery}
                onFocus={() => setIsSearchFocused(true)}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder="پورٹل بھر میں کوئی بھی کتاب، شرح یا مصنف تلاش کریں... (مثلاً: قدوری، ابن کثیر، نحو میر، ہدایہ، لغت)"
                className="w-full min-w-0 flex-1 bg-transparent text-sm sm:text-base text-slate-900 dark:text-slate-100 placeholder-slate-400 focus:outline-none px-2 font-urdu font-medium"
                dir="rtl"
                aria-label="پورٹل بھر میں تلاش کریں"
                autoComplete="off"
              />
              {searchQuery && (
                <button
                  type="button"
                  onClick={() => setSearchQuery('')}
                  className="p-1 rounded-lg text-slate-400 hover:text-slate-600 dark:hover:text-slate-200"
                >
                  <X className="w-4 h-4" />
                </button>
              )}
              <button
                type="submit"
                className="mr-2 px-5 py-2.5 rounded-xl bg-gradient-to-r from-emerald-600 to-teal-700 hover:from-emerald-500 hover:to-teal-600 text-white font-urdu font-bold text-xs sm:text-sm shadow-md transition-all shrink-0"
              >
                تلاش کریں
              </button>
            </div>
          </form>

          {/* Instant Search Results Dropdown */}
          {isSearchFocused && searchResults.length > 0 && (
            <div className="absolute left-0 right-0 top-full mt-2 bg-white dark:bg-slate-900 rounded-2xl shadow-2xl border border-slate-200 dark:border-slate-800 overflow-hidden z-50 text-right animate-fade-in divide-y divide-slate-100 dark:divide-slate-800">
              <div className="px-4 py-2 bg-slate-50 dark:bg-slate-950 flex items-center justify-between text-xs text-slate-500 dark:text-slate-400 font-urdu">
                <span>فوری نتائج ({searchResults.length})</span>
                <span>براہِ راست کتاب پر جائیں</span>
              </div>
              {searchResults.map((item) => (
                <Link
                  key={item.id}
                  to={item.link}
                  onClick={() => setIsSearchFocused(false)}
                  className="px-4 py-3 hover:bg-emerald-50 dark:hover:bg-slate-800 flex items-center justify-between gap-3 text-right group transition-colors"
                >
                  <div className="flex items-center gap-2">
                    <span className="text-[10px] px-2 py-0.5 rounded-full font-bold bg-emerald-100 dark:bg-emerald-950 text-emerald-800 dark:text-emerald-300 border border-emerald-200 dark:border-emerald-800 font-urdu">
                      {item.category}
                    </span>
                    <span className="text-[10px] text-slate-400 font-urdu">
                      {item.author}
                    </span>
                  </div>
                  <div className="flex items-center gap-2">
                    <span className="text-sm font-bold text-slate-900 dark:text-slate-100 group-hover:text-emerald-700 dark:group-hover:text-amber-400 font-urdu">
                      {item.title}
                    </span>
                    <FileText className="w-4 h-4 text-slate-400 group-hover:text-emerald-600" />
                  </div>
                </Link>
              ))}
              <div className="p-2.5 bg-slate-50 dark:bg-slate-950 text-center">
                <button
                  type="button"
                  onClick={handleSearchSubmit}
                  className="text-xs font-bold text-emerald-700 dark:text-emerald-400 hover:underline font-urdu"
                >
                  تمام نتائج لائبریری میں دیکھیں ({searchQuery}) ←
                </button>
              </div>
            </div>
          )}
        </div>

        {/* Popular Quick-Jump Tags */}
        <div className="flex items-center justify-center gap-2 flex-wrap text-xs pt-1 font-urdu">
          <span className="text-emerald-200/75 font-semibold">فوری تلاش:</span>
          {[
            { label: 'درسِ نظامی درجات', path: '/dars-e-nizami' },
            { label: 'قرآن مجید ۱۶ سطری', path: '/quran' },
            { label: 'دورۂ حدیث شریف', path: '/dars-e-nizami/daura' },
            { label: 'تفسیر ابن کثیر', path: '/tafaseer' },
            { label: 'فتاویٰ شامی', path: '/fatawa' },
            { label: 'القاموس المحیط', path: '/lughat' },
            { label: 'حرمین شریفین لائیو', path: '/haramain-live', isLive: true }
          ].map((tag) => (
            <Link
              key={tag.label}
              to={tag.path}
              className={`px-3 py-1 rounded-xl transition-all border flex items-center gap-1.5 ${
                tag.isLive
                  ? 'bg-red-500/20 text-red-300 border-red-500/40 hover:bg-red-500/30'
                  : 'bg-white/10 hover:bg-white/20 text-white border-white/15'
              }`}
            >
              {tag.isLive && <span className="w-2 h-2 rounded-full bg-red-500 animate-pulse" />}
              <span>{tag.label}</span>
            </Link>
          ))}
        </div>

        {/* Key Statistics Cards Strip */}
        <div className="pt-6 max-w-4xl mx-auto grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-3 sm:gap-4">
          <div className="p-3.5 rounded-2xl bg-white/5 border border-white/10 backdrop-blur-md text-center">
            <span className="block text-2xl font-black text-amber-400 font-mono">1700+</span>
            <span className="text-xs text-slate-300 font-urdu">کتب و شروحات</span>
          </div>
          <div className="p-3.5 rounded-2xl bg-white/5 border border-white/10 backdrop-blur-md text-center">
            <span className="block text-2xl font-black text-emerald-400 font-mono">8</span>
            <span className="text-xs text-slate-300 font-urdu">سالانہ درجات</span>
          </div>
          <div className="p-3.5 rounded-2xl bg-white/5 border border-white/10 backdrop-blur-md text-center">
            <span className="block text-2xl font-black text-cyan-400 font-mono">19</span>
            <span className="text-xs text-slate-300 font-urdu">معتبر تفاسیر</span>
          </div>
          <div className="p-3.5 rounded-2xl bg-white/5 border border-white/10 backdrop-blur-md text-center">
            <span className="block text-2xl font-black text-rose-400 font-mono">10</span>
            <span className="text-xs text-slate-300 font-urdu">جامع لغات و معاجم</span>
          </div>
          <div className="p-3.5 rounded-2xl bg-white/5 border border-white/10 backdrop-blur-md text-center col-span-2 sm:col-span-1">
            <span className="block text-2xl font-black text-red-400 font-mono">24/7</span>
            <span className="text-xs text-slate-300 font-urdu">حرمین لائیو نشریات</span>
          </div>
        </div>
      </div>
    </section>
  );
};
