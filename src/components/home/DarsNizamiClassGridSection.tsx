import React from 'react';
import { Link } from 'react-router-dom';
import { GraduationCap, ArrowLeft, ArrowRight, BookOpen, Layers } from 'lucide-react';
import { DARS_YEARS_META } from '../../data/darsNizamiBooks';
import { useLanguage } from '../../context/LanguageContext';

export const DarsNizamiClassGridSection: React.FC = () => {
  const { isRtl } = useLanguage();
  const darsClasses = DARS_YEARS_META.filter((c) => c.classLevel !== 'quran');

  return (
    <section className="py-12 sm:py-16 bg-slate-50/80 dark:bg-slate-950 border-b border-slate-200/80 dark:border-slate-800/80" dir={isRtl ? 'rtl' : 'ltr'}>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        {/* Section Header */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-slate-200 dark:border-slate-800 pb-4">
          <div className="space-y-1">
            <div className="flex items-center gap-2">
              <span className="w-2.5 h-6 rounded-full bg-emerald-600 dark:bg-emerald-500 shadow-sm" />
              <h2 className="text-xl sm:text-2xl font-black text-slate-900 dark:text-slate-100 font-urdu tracking-tight">
                ۸ سالہ درجاتِ درسِ نظامی (سال اول تا دورۂ حدیث شریف)
              </h2>
            </div>
            <p className="text-xs sm:text-sm text-slate-600 dark:text-slate-400 font-urdu">
              کسی بھی درجہ پر کلک کریں اور اس درجہ کا مخصوص صفحہ کھولیں جہاں تمام اصل درسی کتب اور شروحات دستیاب ہیں۔
            </p>
          </div>

          <Link
            to="/dars-e-nizami"
            className="inline-flex items-center gap-2 px-4 py-2 rounded-xl bg-emerald-100 dark:bg-emerald-950 text-emerald-900 dark:text-emerald-300 hover:bg-emerald-200 dark:hover:bg-emerald-900 border border-emerald-300 dark:border-emerald-800 text-xs font-bold font-urdu transition-colors shrink-0"
          >
            <span>تمام درجات کی مکمل تفصیل دیکھیں</span>
            {isRtl ? <ArrowLeft className="w-3.5 h-3.5" /> : <ArrowRight className="w-3.5 h-3.5" />}
          </Link>
        </div>

        {/* 8 Classes Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 sm:gap-5">
          {darsClasses.map((darja, index) => {
            return (
              <Link
                key={darja.id}
                to={`/dars-e-nizami/${darja.classLevel}`}
                className="group relative bg-white dark:bg-slate-900/90 rounded-2xl border border-slate-200/90 dark:border-slate-800 p-5 shadow-sm hover:shadow-xl hover:border-emerald-500 dark:hover:border-emerald-500 transition-all duration-300 flex flex-col justify-between overflow-hidden text-right hover:-translate-y-1"
              >
                {/* Top Subtle Stripe */}
                <div className={`absolute top-0 left-0 right-0 h-1 bg-gradient-to-r ${darja.colorTheme}`} />

                <div className="space-y-3">
                  {/* Badge & Year Counter */}
                  <div className="flex items-center justify-between">
                    <span className="px-2.5 py-0.5 rounded-full text-[11px] font-bold bg-emerald-100 dark:bg-emerald-950 text-emerald-800 dark:text-emerald-300 border border-emerald-200 dark:border-emerald-800 font-urdu">
                      {darja.badge}
                    </span>
                    <span className="text-xs font-mono font-bold text-slate-400">
                      {darja.totalBooks} کتب
                    </span>
                  </div>

                  {/* Title & Subjects */}
                  <div className="space-y-1">
                    <h3 className="text-lg font-black text-slate-900 dark:text-white font-urdu group-hover:text-emerald-700 dark:group-hover:text-emerald-400 transition-colors">
                      {darja.nameUrdu}
                    </h3>
                    <p className="text-[11px] font-medium text-slate-500 dark:text-slate-400">
                      {darja.nameEnglish}
                    </p>
                    <p className="text-[11px] text-slate-600 dark:text-slate-300 font-urdu line-clamp-2 leading-relaxed pt-1">
                      {darja.descriptionUrdu}
                    </p>
                  </div>
                </div>

                {/* Bottom Action */}
                <div className="pt-4 mt-3 border-t border-slate-100 dark:border-slate-800/80 flex items-center justify-between text-xs font-urdu text-emerald-700 dark:text-emerald-400 font-bold group-hover:text-emerald-800">
                  <span>کتب و شروحات دیکھیں</span>
                  <div className="w-6 h-6 rounded-lg bg-emerald-50 dark:bg-emerald-950 flex items-center justify-center group-hover:bg-emerald-600 group-hover:text-white transition-colors">
                    {isRtl ? <ArrowLeft className="w-3.5 h-3.5" /> : <ArrowRight className="w-3.5 h-3.5" />}
                  </div>
                </div>
              </Link>
            );
          })}
        </div>
      </div>
    </section>
  );
};
