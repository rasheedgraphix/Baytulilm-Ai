import React from 'react';
import { SectionHeader } from '../components/common/SectionHeader';
import { FeatureGrid } from '../components/home/FeatureGrid';
import { IslamicToolsHub } from '../components/islamic/IslamicToolsHub';
import { DownloadButton } from '../components/common/DownloadButton';
import { IslamicPatternBg } from '../components/layout/IslamicPatternBg';
import { APP_CONFIG } from '../config/appConfig';
import { useLanguage } from '../context/LanguageContext';

export const FeaturesPage: React.FC = () => {
  const { language, isRtl } = useLanguage();

  const getBadge = () => {
    if (language === 'ps') return `د اپلیکیشن ځانګړتیاوې • نسخه ${APP_CONFIG.version}`;
    if (language === 'ur') return `ایپلیکیشن کی تمام خصوصیات • ورژن ${APP_CONFIG.version}`;
    return `Application Features • v${APP_CONFIG.version}`;
  };

  const getTitle = () => {
    if (language === 'ps') return 'د بیت العلم AI ټولې علمي اسانتیاوې';
    if (language === 'ur') return 'بیت العلم AI کی تمام تعلیمی و دینی خصوصیات';
    return 'Explore All Baytul Ilm AI Features';
  };

  const getSubtitle = () => {
    if (language === 'ps') return 'د درسِ نظامي طالبانو، مدرسینو او د اسلامي علومو مینه‌والو لپاره د ډیجیټل نصاب بشپړه ټولګه.';
    if (language === 'ur') return 'درسِ نظامی کے طلبہ و اساتذہ اور محققین کے لیے تیار کردہ جامع ڈیجیٹل اسلامی نصاب و اوزار۔';
    return 'Discover the comprehensive feature set designed for students, teachers, and seekers of Islamic knowledge.';
  };

  return (
    <div className="pt-28 pb-20 space-y-12">
      {/* Header Banner */}
      <section className="relative bg-emerald-950 text-white py-16 overflow-hidden">
        <IslamicPatternBg variant="hero" />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-4 text-center">
          <SectionHeader
            badge={getBadge()}
            title={getTitle()}
            arabicTitle="جَمِيعُ مُمَيَّزَاتِ التَّطْبِيقِ"
            subtitle={getSubtitle()}
          />
        </div>
      </section>

      {/* Islamic Tools Interactive Showcase */}
      <IslamicToolsHub />

      {/* Feature Grid Component */}
      <FeatureGrid />

      {/* Download Callout */}
      <section className="max-w-4xl mx-auto px-4 text-center py-12" dir={isRtl ? 'rtl' : 'ltr'}>
        <div className="p-8 rounded-3xl bg-slate-900 text-white border border-slate-800 space-y-4">
          <h2 className="text-2xl font-bold">
            {language === 'ps'
              ? `په خپل انډرایډ موبایل کې ټولې اسانتیاوې تجربه کړئ (نسخه ${APP_CONFIG.version})`
              : language === 'ur'
              ? `تمام خصوصیات اپنے اینڈرائیڈ فون پر استعمال کریں (ورژن ${APP_CONFIG.version})`
              : `Experience All Features on Android (v${APP_CONFIG.version})`}
          </h2>
          <p className="text-sm text-slate-300 max-w-xl mx-auto">
            {language === 'ps'
              ? 'د بیت العلم AI رسمي APK ډاونلوډ کړئ او له آفلاین درسي اسانتیاوو، کوئزونو، بک مارکونو او ډارک موډ څخه خوند واخلئ.'
              : language === 'ur'
              ? 'بیت العلم AI کی آفیشل APK ڈاؤن لوڈ کریں اور آف لائن درسی کتب، کوئزز، نوٹس اور نائٹ موڈ سے مستفید ہوں۔'
              : 'Download the official Baytul Ilm AI APK to enjoy offline study tools, bookmarks, dark mode, and interactive quizzes.'}
          </p>
          <div className="pt-2 flex justify-center">
            <DownloadButton variant="hero" size="md" showDetails />
          </div>
        </div>
      </section>
    </div>
  );
};

