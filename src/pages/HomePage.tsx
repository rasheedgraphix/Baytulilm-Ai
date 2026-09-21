import React, { useEffect } from 'react';
import { PortalAcademicHero } from '../components/home/PortalAcademicHero';
import { CategoryPortalCards } from '../components/home/CategoryPortalCards';
import { InstallationGuide } from '../components/common/InstallationGuide';
import { DownloadButton } from '../components/common/DownloadButton';
import { APP_CONFIG } from '../config/appConfig';
import { HelpCircle, Sparkles } from 'lucide-react';
import { FAQ_DATA } from '../config/faqData';
import { useLanguage } from '../context/LanguageContext';
import { updatePageSEO } from '../utils/seo';
import { AdsterraBanner } from '../components/ads/AdsterraBanner';
import { AdsterraNative } from '../components/ads/AdsterraNative';

export const HomePage: React.FC = () => {
  const { language, isRtl, t } = useLanguage();

  useEffect(() => {
    updatePageSEO({
      title: 'Baytul Ilm AI (بیت العلم) – Islamic Education, Dars-e-Nizami & Complete Library',
      description:
        'Baytul Ilm AI (Baitul Ilm / بیت العلم) is the complete Islamic educational portal with 8-year Dars-e-Nizami curriculum, Quran Tafseer, Fatawa, Dictionaries, and free PDF books download.',
      keywords: [
        'Baytul Ilm AI',
        'Baitul Ilm AI',
        'Bayt ul Ilm AI',
        'Bait ul Ilm AI',
        'Baytul Ilm',
        'Baitul Ilm',
        'Bayt ul Ilm',
        'Bait ul Ilm',
        'Baitulilm',
        'Baytulilm',
        'بیت العلم AI',
        'بیت العلم',
        'بیت العلوم',
        'Dars e Nizami books pdf',
        'Tafseer Ibn Katheer',
        'Fatawa Razawiyya',
        'Kanzud Daqaiq',
        'Hidayah',
        'Mishkat Sharif',
        'Islamic Books Online'
      ],
      canonicalUrl: 'https://bait-ul-ilm-web.ai.studio/'
    });
  }, []);

  return (
    <div className="space-y-0 bg-slate-50 dark:bg-slate-950">
      {/* 1. Dignified Academic Islamic Gateway Hero with Universal Instant Live Search */}
      <PortalAcademicHero />

      {/* 2. Central Showcase: 8 Main Department Portal Cards (Each links directly to its dedicated page) */}
      <section className="py-12 sm:py-16 bg-white dark:bg-slate-900/50 border-b border-slate-200/80 dark:border-slate-800">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <CategoryPortalCards />
        </div>
      </section>

      {/* Sponsored Ad Unit */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
        <AdsterraBanner />
      </div>

      {/* 4. Installation Guide & App Setup */}
      <InstallationGuide />

      {/* Sponsored Native Recommendations */}
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-2">
        <AdsterraNative />
      </div>

      {/* 5. Frequently Asked Questions */}
      <section className="py-16 sm:py-20 bg-slate-50 dark:bg-slate-950 border-t border-slate-200/80 dark:border-slate-800" dir={isRtl ? 'rtl' : 'ltr'}>
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 space-y-10">
          <div className="text-center space-y-2">
            <h2 className="text-2xl sm:text-3xl font-black text-slate-900 dark:text-slate-100 font-urdu">
              {language === 'ur' ? 'اکثر پوچھے جانے والے سوالات' : language === 'ps' ? 'ډېرې پوښتل کېدونکې پوښتنې (FAQs)' : 'Frequently Asked Questions'}
            </h2>
            <p className="text-slate-600 dark:text-slate-400 text-xs sm:text-sm font-urdu">
              {language === 'ur' 
                ? 'ایپ اور ویب پورٹل کے استعمال، سیکیورٹی اور کتب سے متعلق اہم سوالات کے جوابات'
                : language === 'ps'
                ? 'د اپلیکیشن او ویب پاڼې د کارولو، امنیت او کتابونو په اړه د پوښتنو ځوابونه'
                : 'Common questions regarding library usage, security, and app features.'}
            </p>
          </div>

          <div className="space-y-3.5">
            {FAQ_DATA.slice(0, 4).map((faq) => {
              const displayQuestion =
                language === 'ps' && faq.questionPashto
                  ? faq.questionPashto
                  : language === 'ur' && faq.questionUrdu
                  ? faq.questionUrdu
                  : faq.question;

              const displayAnswer =
                language === 'ps' && faq.answerPashto
                  ? faq.answerPashto
                  : language === 'ur' && faq.answerUrdu
                  ? faq.answerUrdu
                  : faq.answer;

              return (
                <div
                  key={faq.id}
                  className="p-5 sm:p-6 bg-white dark:bg-slate-900 rounded-2xl border border-slate-200/90 dark:border-slate-800 space-y-2 shadow-sm"
                >
                  <h3 className="text-sm sm:text-base font-bold text-slate-900 dark:text-slate-100 flex items-center gap-2.5 font-urdu">
                    <HelpCircle className="w-4 h-4 text-emerald-600 dark:text-emerald-400 shrink-0" />
                    <span>{displayQuestion}</span>
                  </h3>
                  <p className={`text-xs sm:text-sm text-slate-600 dark:text-slate-300 leading-relaxed font-urdu ${isRtl ? 'pr-6' : 'pl-6'}`}>
                    {displayAnswer}
                  </p>
                </div>
              );
            })}
          </div>
        </div>
      </section>

      {/* 6. Final Mobile APK & Offline Download Banner */}
      <section className="py-16 sm:py-20 bg-gradient-to-r from-emerald-950 via-[#032219] to-teal-950 text-white border-t border-emerald-900/60 relative overflow-hidden">
        <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 text-center space-y-6 relative z-10">
          <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-emerald-900/90 text-emerald-200 border border-emerald-500/40 text-xs font-bold font-urdu">
            <Sparkles className="w-3.5 h-3.5 text-amber-400" />
            <span>بیت العلم AI موبائل ایپ — ورژن {APP_CONFIG.version}</span>
          </div>

          <h2 className="text-2xl sm:text-3xl md:text-4xl font-black text-white font-urdu tracking-tight">
            {t('downloadLatestApk')}
          </h2>

          <p className="text-xs sm:text-sm md:text-base text-emerald-100/90 max-w-2xl mx-auto leading-relaxed font-urdu" dir={isRtl ? 'rtl' : 'ltr'}>
            {language === 'ur'
              ? 'تمام درسی کتب، شروحات، تفاسیر، لغات اور اسلامی ٹولز بغیر انٹرنیٹ (آف لائن) اپنے اینڈرائیڈ موبائل میں چلانے کے لیے آفیشل APK مفت ڈاؤن لوڈ کریں۔'
              : language === 'ps'
              ? 'ټول درسي کتابونه، تفاسیر، لغتونه او اسلامي اوزار بې له انټرنېټه په خپل مبایل کښې وکاروئ.'
              : 'Access all books, commentaries, dictionaries, and Islamic tools offline on your Android device.'}
          </p>

          <div className="pt-2 flex justify-center">
            <DownloadButton variant="hero" size="lg" showDetails />
          </div>
        </div>
      </section>
    </div>
  );
};

