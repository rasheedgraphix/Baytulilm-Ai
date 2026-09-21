import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import {
  GraduationCap,
  BookOpen,
  ArrowRight,
  ArrowLeft,
  Search,
  Sparkles,
  Layers,
  ChevronLeft,
  ChevronRight,
  CheckCircle2,
  BookMarked,
  Download
} from 'lucide-react';
import { DARS_YEARS_META } from '../data/darsNizamiBooks';
import { ALL_DARS_NIZAMI_BOOKS } from '../data/darsNizamiBooks';
import { useLanguage } from '../context/LanguageContext';
import { IslamicPatternBg } from '../components/layout/IslamicPatternBg';

export const DarsNizamiClassesPage: React.FC = () => {
  const { isRtl, language } = useLanguage();
  const [searchFilter, setSearchFilter] = useState('');

  useEffect(() => {
    document.title = 'درسِ نظامی درجات و کلاسز (سال اول تا دورۂ حدیث شریف) | بیت العلم AI';
    window.scrollTo(0, 0);
  }, []);

  // Filter out non-dars classes (like 'quran') for this specific classes view
  const classesList = DARS_YEARS_META.filter((c) => c.classLevel !== 'quran');

  const filteredClasses = classesList.filter((c) => {
    if (!searchFilter.trim()) return true;
    const q = searchFilter.toLowerCase();
    return (
      c.nameUrdu.toLowerCase().includes(q) ||
      c.nameEnglish.toLowerCase().includes(q) ||
      c.descriptionUrdu.toLowerCase().includes(q) ||
      c.badge.toLowerCase().includes(q)
    );
  });

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 pt-20 pb-20 space-y-10" dir={isRtl ? 'rtl' : 'ltr'}>
      {/* Compact Elegant Header */}
      <section className="relative bg-gradient-to-b from-[#091510] via-[#0f241c] to-[#091510] text-white py-12 sm:py-16 overflow-hidden border-b border-emerald-800/40 shadow-lg">
        <IslamicPatternBg variant="subtle" />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-5">
          {/* Breadcrumb Navigation */}
          <div className="flex items-center gap-2 text-xs font-bold text-emerald-300/80 font-urdu">
            <Link to="/" className="hover:text-amber-300 transition-colors">
              مرکزی صفحہ (ہوم)
            </Link>
            <span>/</span>
            <span className="text-amber-300">درسِ نظامی تمام درجات</span>
          </div>

          <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
            <div className="space-y-2 max-w-3xl">
              <div className="inline-flex items-center gap-2 px-3.5 py-1 rounded-full bg-emerald-900/80 border border-amber-400/40 text-amber-300 text-xs font-bold font-urdu">
                <GraduationCap className="w-4 h-4 text-amber-400" />
                <span>مکمل ۸ سالہ نصابِ عالمیت و فاضلیت</span>
              </div>
              <h1 className="text-2xl sm:text-3xl lg:text-4xl font-black text-white font-urdu tracking-tight leading-tight">
                نصابِ درسِ نظامی: تمام درجات و کلاسز
              </h1>
              <p className="text-xs sm:text-sm text-emerald-100/90 font-urdu leading-relaxed">
                جس درجہ کی کتب پڑھنا یا ڈاؤن لوڈ کرنا چاہتے ہیں، اس درجہ پر کلک کریں۔ ہر درجہ کے اندر اس کی تمام اصل درسی کتب، مستند شروحات، حواشی اور تراجم دستیاب ہیں۔
              </p>
            </div>

            {/* Quick Metrics */}
            <div className="flex items-center gap-3 shrink-0 flex-wrap">
              <div className="px-4 py-2.5 rounded-2xl bg-black/40 border border-white/15 backdrop-blur-md text-center">
                <span className="block text-xl font-black text-amber-400 font-mono">8</span>
                <span className="text-[11px] text-slate-300 font-urdu">مکمل سالانہ درجات</span>
              </div>
              <div className="px-4 py-2.5 rounded-2xl bg-black/40 border border-white/15 backdrop-blur-md text-center">
                <span className="block text-xl font-black text-emerald-400 font-mono">
                  {ALL_DARS_NIZAMI_BOOKS.length - 11}
                </span>
                <span className="text-[11px] text-slate-300 font-urdu">کل کتب و شروحات</span>
              </div>
            </div>
          </div>

          {/* Quick Class Search */}
          <div className="max-w-xl pt-2">
            <div className="relative flex items-center bg-white/10 dark:bg-black/40 rounded-2xl border border-white/20 p-1.5 backdrop-blur-md focus-within:border-amber-400 focus-within:ring-2 focus-within:ring-amber-400/20 transition-all">
              <Search className="w-5 h-5 text-amber-400 mr-2.5 ml-2 shrink-0" />
              <input
                id="classes-overview-filter"
                name="classFilter"
                type="text"
                value={searchFilter}
                onChange={(e) => setSearchFilter(e.target.value)}
                placeholder="درجہ یا سال تلاش کریں (مثلاً: سال اول، دورۂ حدیث، ثالثہ)..."
                className="w-full bg-transparent text-sm text-white placeholder-slate-300 focus:outline-none px-2 font-urdu"
                dir="rtl"
              />
            </div>
          </div>
        </div>
      </section>

      {/* Main Grid: All 8 Classes */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-6">
        <div className="flex items-center justify-between border-b border-slate-200 dark:border-slate-800 pb-3">
          <div className="flex items-center gap-2">
            <div className="w-2.5 h-6 rounded-full bg-emerald-600 dark:bg-emerald-500" />
            <h2 className="text-lg sm:text-xl font-black text-slate-900 dark:text-slate-100 font-urdu">
              درجہ وار درسی کتب خانہ (Select a Class)
            </h2>
          </div>
          <span className="text-xs text-slate-500 dark:text-slate-400 font-urdu">
            کل درجات: {filteredClasses.length}
          </span>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-5 sm:gap-6">
          {filteredClasses.map((darja, index) => {
            return (
              <div
                key={darja.id}
                className="group relative bg-white dark:bg-slate-900 rounded-3xl border border-slate-200 dark:border-slate-800 hover:border-emerald-500 dark:hover:border-emerald-500 shadow-sm hover:shadow-xl transition-all duration-300 flex flex-col justify-between overflow-hidden"
              >
                {/* Top Accent Strip */}
                <div className={`h-2.5 w-full bg-gradient-to-r ${darja.colorTheme}`} />

                <div className="p-5 sm:p-6 space-y-4 flex-1 flex flex-col justify-between">
                  <div className="space-y-2.5">
                    {/* Badge & Year Number */}
                    <div className="flex items-center justify-between">
                      <span className="px-3 py-1 rounded-full text-xs font-black bg-emerald-100 dark:bg-emerald-950/80 text-emerald-800 dark:text-emerald-300 border border-emerald-300 dark:border-emerald-800 font-urdu">
                        {darja.badge}
                      </span>
                      <span className="text-xs font-mono font-bold text-slate-400">
                        {darja.totalBooks} کتب
                      </span>
                    </div>

                    {/* Class Title */}
                    <div>
                      <h3 className="text-lg sm:text-xl font-black text-slate-900 dark:text-slate-100 font-urdu group-hover:text-emerald-700 dark:group-hover:text-emerald-400 transition-colors">
                        {darja.nameUrdu}
                      </h3>
                      <p className="text-xs text-slate-500 dark:text-slate-400 font-medium mt-0.5">
                        {darja.nameEnglish}
                      </p>
                    </div>

                    {/* Subjects Description */}
                    <p className="text-xs text-slate-600 dark:text-slate-300 font-urdu leading-relaxed line-clamp-3">
                      {darja.descriptionUrdu}
                    </p>
                  </div>

                  {/* Direct Action Link to Class Detail Page */}
                  <div className="pt-4 border-t border-slate-100 dark:border-slate-800">
                    <Link
                      to={`/dars-e-nizami/${darja.classLevel}`}
                      className="w-full inline-flex items-center justify-center gap-2 py-3 px-4 rounded-2xl bg-gradient-to-r from-emerald-700 to-teal-800 hover:from-emerald-600 hover:to-teal-700 text-white font-urdu font-bold text-xs sm:text-sm shadow-md transition-all group-hover:shadow-lg group-hover:scale-[1.02]"
                    >
                      <span>اس درجہ کی کتب ملاحظہ کریں</span>
                      {isRtl ? <ArrowLeft className="w-4 h-4" /> : <ArrowRight className="w-4 h-4" />}
                    </Link>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </main>
    </div>
  );
};
