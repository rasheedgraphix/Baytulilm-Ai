import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { BookOpen, Sparkles, HeartHandshake, Bookmark, ArrowRight } from 'lucide-react';
import { MasnoonDuasInteractive } from '../components/islamic/MasnoonDuasInteractive';
import { IslamicPatternBg } from '../components/layout/IslamicPatternBg';
import { useLanguage } from '../context/LanguageContext';
import { updatePageSEO } from '../utils/seo';

export const MasnoonDuasPage: React.FC = () => {
  const { isRtl } = useLanguage();

  useEffect(() => {
    updatePageSEO({
      title: '۱۰۰ مشہور مکمل مسنون و قرآنی دعائیں (بیت العلم پورٹل)',
      description:
        '۱۰۰ مستند مسنون و قرآنی دعائیں مع مکمل عربی اعراب شدہ متن، اردو ترجمہ، انگلش تلفظ اور مستند کتبِ احادیث (بخاری، مسلم، ترمذی، ابوداؤد) کے حوالہ جات۔',
      keywords: [
        'مسنون دعائیں',
        '100 مسنون دعائیں',
        'قرآنی دعائیں',
        'صبح شام کی دعائیں',
        'سونے جاگنے کی دعا',
        'کھانے پینے کی دعائیں',
        'حفاظت کی دعائیں',
        'سید الاستغفار',
        'Masnoon Duas 100',
        'Quranic Duas'
      ],
      canonicalUrl: 'https://bait-ul-ilm-web.ai.studio/duas'
    });
    window.scrollTo(0, 0);
  }, []);

  return (
    <div className="pt-24 pb-20 space-y-10 min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 font-urdu" dir="rtl">
      {/* Header Banner */}
      <section className="relative bg-gradient-to-b from-slate-900 via-emerald-950 to-slate-950 text-white py-12 sm:py-16 overflow-hidden shadow-xl border-b border-emerald-500/20">
        <IslamicPatternBg variant="hero" />
        
        {/* Glow */}
        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-96 h-96 bg-emerald-500/15 blur-[120px] rounded-full pointer-events-none" />

        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-4 text-center">
          {/* Breadcrumbs */}
          <div className="flex items-center justify-center gap-2 text-xs text-emerald-300/80 font-urdu">
            <Link to="/" className="hover:text-white transition-colors">بیت العلم پورٹل</Link>
            <span>/</span>
            <span className="text-emerald-200 font-bold">۱۰۰ مسنون و قرآنی دعائیں</span>
          </div>

          <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-emerald-500/15 border border-emerald-400/40 text-emerald-300 text-xs sm:text-sm font-urdu font-bold shadow-inner">
            <Sparkles className="w-4 h-4 text-emerald-400" />
            <span>مستند کتبِ احادیث و قرآن مجید سے مکمل عربی متون و اردو ترجمہ</span>
          </div>

          <h1 className="text-3xl sm:text-4xl md:text-5xl font-black tracking-tight font-arabic text-transparent bg-clip-text bg-gradient-to-r from-emerald-100 via-amber-100 to-amber-300">
            ۱۰۰ مشہور مکمل مسنون و قرآنی دعائیں
          </h1>

          <p className="max-w-3xl mx-auto text-sm sm:text-base text-slate-300 font-urdu leading-relaxed">
            صبح و شام، نیند، وضو، نماز، کھانا پینا، سفر، حفاظت و شفاء، استغفار، رحمت اور قرآنی دعاؤں کا مستند مجموعہ بغیر کسی اختصار یا کمی کے۔
          </p>
        </div>
      </section>

      {/* Main Interactive Widget */}
      <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8">
        <MasnoonDuasInteractive />
      </div>
    </div>
  );
};
