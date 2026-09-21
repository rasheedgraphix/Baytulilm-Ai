import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { HeartHandshake, Moon, BookOpen, ArrowRight, ArrowLeft } from 'lucide-react';
import { AsmaUnNabiInteractive } from '../components/islamic/AsmaUnNabiInteractive';
import { IslamicPatternBg } from '../components/layout/IslamicPatternBg';
import { useLanguage } from '../context/LanguageContext';

export const AsmaUnNabiPage: React.FC = () => {
  const { isRtl } = useLanguage();

  useEffect(() => {
    document.title = 'اسمائے نبی ﷺ (حضرت محمد مصطفیٰ ﷺ کے ۹۹ مبارک نام) مع فضائل و درود شریف | بیت العلم AI';
    window.scrollTo(0, 0);
  }, []);

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 pt-20 pb-20 space-y-8" dir={isRtl ? 'rtl' : 'ltr'}>
      {/* Header Banner */}
      <section className="relative bg-gradient-to-b from-[#091e13] via-[#0f2d1e] to-[#091e13] text-white py-12 sm:py-16 overflow-hidden border-b border-emerald-500/30 shadow-lg">
        <IslamicPatternBg variant="subtle" />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-4">
          {/* Breadcrumbs */}
          <div className="flex items-center justify-between border-b border-white/10 pb-4">
            <div className="flex items-center gap-2 text-xs font-bold text-emerald-300 font-urdu">
              <Link to="/" className="hover:text-white transition-colors">
                ہوم
              </Link>
              <span>/</span>
              <span className="text-white">اسمائے نبی ﷺ (حضرت محمد مصطفیٰ ﷺ کے ۹۹ نام)</span>
            </div>

            <Link
              to="/asma-ul-husna"
              className="inline-flex items-center gap-1.5 text-xs font-bold text-emerald-300 hover:text-white font-urdu transition-colors"
            >
              <Moon className="w-3.5 h-3.5" />
              <span>اسماء الحسنیٰ کا صفحہ دیکھیں</span>
            </Link>
          </div>

          <div className="max-w-3xl space-y-2">
            <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-emerald-500/20 border border-emerald-400/40 text-emerald-300 text-xs font-bold font-urdu">
              <HeartHandshake className="w-3.5 h-3.5 text-emerald-400" />
              <span>وَمَا أَرْسَلْنَاكَ إِلَّا رَحْمَةً لِّلْعَالَمِينَ ﷺ</span>
            </div>
            <h1 className="text-2xl sm:text-3xl lg:text-4xl font-black text-white font-urdu tracking-tight">
              اسمائے طیبہ نبی کریم ﷺ: ۹۹ مبارک صفاتی نام مع درود شریف کاؤنٹر
            </h1>
            <p className="text-xs sm:text-sm text-emerald-100/80 font-urdu leading-relaxed">
              سرورِ کائنات، فخرِ موجودات، سیدنا و مولانا حضرت محمد مصطفیٰ ﷺ کے ۹۹ مبارک و نورانی اسماء، معانی، ارشاداتِ نبوی ﷺ اور ڈیجیٹل درود شریف تسبیح کاؤنٹر۔
            </p>
          </div>
        </div>
      </section>

      {/* Main Interactive Component */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <AsmaUnNabiInteractive />
      </main>
    </div>
  );
};
