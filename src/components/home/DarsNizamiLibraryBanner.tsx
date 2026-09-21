import React from 'react';
import { Link } from 'react-router-dom';
import { BookOpen, GraduationCap, ArrowRight, ArrowLeft, Download, CheckCircle, Sparkles } from 'lucide-react';
import { useLanguage } from '../../context/LanguageContext';
import { DARS_YEARS_META, ALL_DARS_NIZAMI_BOOKS } from '../../data/darsNizamiBooks';
import { IslamicBookCover } from '../library/IslamicBookCover';

export const DarsNizamiLibraryBanner: React.FC = () => {
  const { language, isRtl } = useLanguage();

  // Sample representative books for visual showcase
  const sampleBooks = React.useMemo(() => {
    // Pick 4 iconic books from different categories/years
    return [
      ALL_DARS_NIZAMI_BOOKS[0], // Noor-ul-Izah (Fiqh)
      ALL_DARS_NIZAMI_BOOKS.find(b => b.category.includes('Nahw') || b.name.includes('Kafia')) || ALL_DARS_NIZAMI_BOOKS[15],
      ALL_DARS_NIZAMI_BOOKS.find(b => b.category.includes('Tafseer') || b.name.includes('Jalalain')) || ALL_DARS_NIZAMI_BOOKS[40],
      ALL_DARS_NIZAMI_BOOKS.find(b => b.classLevel === '8th' || b.category.includes('Hadith')) || ALL_DARS_NIZAMI_BOOKS[ALL_DARS_NIZAMI_BOOKS.length - 1],
    ].filter(Boolean);
  }, []);

  return (
    <section className="py-14 bg-gradient-to-b from-slate-50 to-emerald-50/40 dark:from-slate-950 dark:to-emerald-950/20 border-y border-slate-200/80 dark:border-slate-800" dir={isRtl ? 'rtl' : 'ltr'}>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        {/* Banner Card */}
        <div className="relative rounded-3xl overflow-hidden bg-gradient-to-br from-emerald-900 via-teal-950 to-slate-950 text-white p-6 sm:p-10 md:p-12 shadow-xl border border-emerald-700/40">
          {/* Decorative Islamic geometry */}
          <div className="absolute top-0 right-0 w-96 h-96 bg-emerald-500/10 rounded-full blur-3xl pointer-events-none -mr-20 -mt-20"></div>
          <div className="absolute bottom-0 left-0 w-80 h-80 bg-amber-500/10 rounded-full blur-3xl pointer-events-none -ml-20 -mb-20"></div>

          <div className="relative z-10 flex flex-col lg:flex-row items-center justify-between gap-8">
            <div className="space-y-4 max-w-2xl text-center lg:text-right">
              <div className="inline-flex items-center gap-2 px-3.5 py-1 rounded-full text-xs font-semibold bg-amber-400 text-emerald-950 shadow-sm">
                <GraduationCap className="w-4 h-4" />
                <span>درسِ نظامی ڈیجیٹل کتب خانہ • Digital Books Library</span>
              </div>

              <h2 className="text-2xl sm:text-3xl md:text-4xl font-extrabold tracking-tight font-urdu leading-snug">
                {language === 'ur'
                  ? 'درسِ نظامی کا مکمل 8 سالہ نصاب مع درسی شروحات و تراجم'
                  : language === 'ps'
                  ? 'د درسِ نظامي د اتو کلونو بشپړ نصاب، شروحات او ترجمې'
                  : 'Complete 8-Year Dars-e-Nizami Curriculum, Commentaries & Translations'}
              </h2>

              <p className="text-sm sm:text-base text-emerald-100/90 leading-relaxed font-urdu">
                {language === 'ur'
                  ? 'سال اول (اولیٰ) سے دورۂ حدیث شریف تک تمام کتب، مستند عربی و اردو شروحات (ہدایہ، کافیہ، کنز، جامی، مشکوٰۃ، بخاری شریف وغیرہ)، حواشی، اور امتحانی پرچے آن لائن مطالعہ اور براہ راست PDF ڈاؤن لوڈ کے لیے دستیاب ہیں۔'
                  : language === 'ps'
                  ? 'له لومړي کال (اولیٰ) څخه تر دورۂ حدیث پورې ټول درسي کتابونه، معتبرې عربي او پښتو/اردو شروحات، او حل شوي پرچې په وړیا توګه آنلاین او PDF ترلاسه کړئ.'
                  : 'Explore 1,600+ textbooks, classical Arabic & Urdu commentaries, translations, and examination papers with direct high-speed download links.'}
              </p>

              {/* Badges of classes */}
              <div className="flex items-center gap-1.5 flex-wrap justify-center lg:justify-start pt-1">
                {DARS_YEARS_META.map((y) => (
                  <span
                    key={y.id}
                    className="px-2.5 py-1 rounded-lg text-xs font-semibold bg-emerald-800/80 text-emerald-100 border border-emerald-600/40"
                  >
                    {y.badge}
                  </span>
                ))}
              </div>
            </div>

            {/* Showcase book covers stack + CTA button */}
            <div className="flex flex-col items-center gap-5 shrink-0">
              {/* Visual Book Covers Row */}
              <div className="flex items-center -space-x-3 sm:-space-x-4 hover:space-x-1 transition-all duration-300 py-2">
                {sampleBooks.map((b, idx) => (
                  <div
                    key={b.id}
                    className="transform transition-transform hover:-translate-y-2 hover:scale-105 hover:z-20 shadow-xl"
                    style={{ zIndex: 10 + idx }}
                  >
                    <IslamicBookCover
                      book={b}
                      bookId={b.id}
                      title={b.name}
                      titleUrdu={b.nameUrdu}
                      pdfUrl={b.pdfUrl}
                      author={b.author}
                      category={b.category}
                      classNameUrdu={b.classNameUrdu}
                      typeUrdu={b.typeUrdu || b.type}
                      size="sm"
                    />
                  </div>
                ))}
              </div>

              <div className="flex flex-col sm:flex-row lg:flex-col items-center gap-2.5">
                <Link
                  to="/library"
                  className="inline-flex items-center gap-2 px-6 py-3.5 rounded-2xl text-sm font-bold bg-amber-400 hover:bg-amber-300 text-emerald-950 transition-all shadow-lg hover:shadow-xl hover:scale-105"
                >
                  <BookOpen className="w-5 h-5" />
                  <span>کتب خانہ دیکھیں (Explore Library)</span>
                  {isRtl ? <ArrowLeft className="w-4 h-4" /> : <ArrowRight className="w-4 h-4" />}
                </Link>
                <span className="text-xs text-emerald-200/80 font-medium">
                  250+ کتب مع سرورق و شروحات • مفت ڈاؤن لوڈ
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};
