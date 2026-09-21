import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Moon, HeartHandshake, BookOpen, ArrowRight, ArrowLeft } from 'lucide-react';
import { AsmaUlHusnaInteractive } from '../components/islamic/AsmaUlHusnaInteractive';
import { IslamicPatternBg } from '../components/layout/IslamicPatternBg';
import { useLanguage } from '../context/LanguageContext';

export const AsmaUlHusnaPage: React.FC = () => {
  const { isRtl } = useLanguage();

  useEffect(() => {
    document.title = 'اسماء الحسنیٰ (اللہ تعالیٰ کے ۹۹ مبارک نام) مع معانی، فضائل و تسبیح | بیت العلم AI';
    window.scrollTo(0, 0);
  }, []);

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 pt-20 pb-20 space-y-8" dir={isRtl ? 'rtl' : 'ltr'}>
      {/* Header Banner */}
      <section className="relative bg-gradient-to-b from-[#1b1505] via-[#2d2209] to-[#1b1505] text-white py-12 sm:py-16 overflow-hidden border-b border-amber-500/30 shadow-lg">
        <IslamicPatternBg variant="subtle" />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-4">
          {/* Breadcrumbs */}
          <div className="flex items-center justify-between border-b border-white/10 pb-4">
            <div className="flex items-center gap-2 text-xs font-bold text-amber-300 font-urdu">
              <Link to="/" className="hover:text-white transition-colors">
                ہوم
              </Link>
              <span>/</span>
              <span className="text-white">اسماء الحسنیٰ (اللہ تعالیٰ کے ۹۹ مبارک نام)</span>
            </div>

            <Link
              to="/asma-un-nabi"
              className="inline-flex items-center gap-1.5 text-xs font-bold text-amber-300 hover:text-white font-urdu transition-colors"
            >
              <HeartHandshake className="w-3.5 h-3.5" />
              <span>اسمائے نبی ﷺ کا صفحہ دیکھیں</span>
            </Link>
          </div>

          <div className="max-w-3xl space-y-2">
            <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-amber-400/20 border border-amber-400/40 text-amber-300 text-xs font-bold font-urdu">
              <Moon className="w-3.5 h-3.5 text-amber-400" />
              <span>وَلِلَّهِ الْأَسْمَاءُ الْحُسْنَىٰ فَادْعُوهُ بِهَا</span>
            </div>
            <h1 className="text-2xl sm:text-3xl lg:text-4xl font-black text-white font-urdu tracking-tight">
              اسماء الحسنیٰ: ۹۹ مبارک اسمائے الٰہی مع فضائل و تسبیح
            </h1>
            <p className="text-xs sm:text-sm text-amber-100/80 font-urdu leading-relaxed">
              اللہ رب العزت کے ننانوے مبارک و مقدس نام، ان کے عربی و اردو معانی، روزمرہ ورد کے فضائل و برکات اور انٹرایکٹو ڈیجیٹل تسبیح کاؤنٹر۔
            </p>
          </div>
        </div>
      </section>

      {/* Main Interactive Component */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <AsmaUlHusnaInteractive />
      </main>
    </div>
  );
};
