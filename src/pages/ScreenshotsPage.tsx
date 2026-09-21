import React from 'react';
import { SectionHeader } from '../components/common/SectionHeader';
import { ScreenshotsGallery } from '../components/gallery/ScreenshotsGallery';
import { IslamicPatternBg } from '../components/layout/IslamicPatternBg';
import { useLanguage } from '../context/LanguageContext';

export const ScreenshotsPage: React.FC = () => {
  const { language } = useLanguage();

  const getBadge = () => {
    if (language === 'ps') return 'د اپلیکیشن انځورونه';
    if (language === 'ur') return 'موبائل ایپ اسکرین شاٹس';
    return 'Mobile Visuals';
  };

  const getTitle = () => {
    if (language === 'ps') return 'د بیت العلم AI انډرایډ نندارتون';
    if (language === 'ur') return 'اینڈرائیڈ ایپ کا تصویری جائزہ';
    return 'Android App UI Screenshots';
  };

  const getSubtitle = () => {
    if (language === 'ps') return 'د اسلامي علومو د زده کړې لپاره د ځانګړي زمردي ډارک او لایټ انټرفیس ننداره.';
    if (language === 'ur') return 'مدرسہ کے طلبہ و اساتذہ کے لیے تیار کردہ آرام دہ شاہی زمردی نائٹ موڈ اور جدید انٹرفیس کا مکمل جائزہ۔';
    return 'Preview the distraction-free dark emerald user experience crafted for Android devices.';
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
            arabicTitle="مَعْرِضُ شَاشَاتِ التَّطْبِيقِ الأَكاديمِي"
            subtitle={getSubtitle()}
          />
        </div>
      </section>

      {/* Screenshots Gallery Component */}
      <ScreenshotsGallery />
    </div>
  );
};
