import React, { useState } from 'react';
import { APP_SCREENSHOTS } from '../../config/screenshotsData';
import { SectionHeader } from '../common/SectionHeader';
import { Maximize2 } from 'lucide-react';
import { useLanguage } from '../../context/LanguageContext';

export const ScreenshotsGallery: React.FC = () => {
  const [selectedCategory, setSelectedCategory] = useState<string>('All');
  const [activeImage, setActiveImage] = useState<string | null>(null);
  const { language, isRtl } = useLanguage();

  const categoryOptions = [
    { id: 'All', label: language === 'ps' ? 'ټول انځورونه' : language === 'ur' ? 'تمام اسکرینز' : 'All' },
    { id: 'Home', label: language === 'ps' ? 'اصلي سکرین' : language === 'ur' ? 'ہوم اسکرین' : 'Home' },
    { id: 'Dars-e-Nizami', label: language === 'ps' ? 'درسِ نظامي' : language === 'ur' ? 'درسِ نظامی' : 'Dars-e-Nizami' },
    { id: 'Quiz', label: language === 'ps' ? 'دیني کوئز' : language === 'ur' ? 'دینی کوئز' : 'Quiz' },
    { id: 'AI Scholar', label: language === 'ps' ? 'AI اسکالر' : language === 'ur' ? 'AI اسکالر' : 'AI Scholar' },
    { id: 'Library', label: language === 'ps' ? 'کتابتون' : language === 'ur' ? 'کتب خانہ' : 'Library' },
    { id: 'Profile', label: language === 'ps' ? 'پروفایل' : language === 'ur' ? 'پروفائل' : 'Profile' },
  ];

  const filteredScreenshots = APP_SCREENSHOTS.filter((s) => {
    if (selectedCategory === 'All') return true;
    return s.category === selectedCategory;
  });

  const getBadge = () => {
    if (language === 'ps') return 'د انډرایډ اپلیکیشن ژوندی نندارتون';
    if (language === 'ur') return 'اینڈرائیڈ ایپ اسکرین شاٹس گیلری';
    return 'Android App UI Gallery';
  };

  const getTitle = () => {
    if (language === 'ps') return 'د بیت العلم AI انډرایډ انټرفیس';
    if (language === 'ur') return 'بیت العلم AI آفیشل موبائل انٹرفیس کا تصویری جائزہ';
    return 'Official Mobile App Interface Preview';
  };

  const getSubtitle = () => {
    if (language === 'ps') return 'د اسلامي علومو او درسِ نظامي د مطالعې لپاره د ځانګړي زمردي ډارک او لایټ انټرفیس ننداره.';
    if (language === 'ur') return 'مدرسہ کے طلبہ و اساتذہ کے لیے تیار کردہ آرام دہ شاہی زمردی نائٹ موڈ اور جدید انٹرفیس۔';
    return 'Explore the clean, modern dark emerald Android user interface designed for distraction-free Islamic studies.';
  };

  return (
    <section className="py-20 bg-slate-50 dark:bg-slate-950/80">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-12">
        <SectionHeader
          badge={getBadge()}
          title={getTitle()}
          arabicTitle="مَعْرِضُ شَاشَاتِ التَّطْبِيقِ الأَكاديمِي"
          subtitle={getSubtitle()}
        />

        {/* Category Filters */}
        <div className="flex flex-wrap items-center justify-center gap-2" dir={isRtl ? 'rtl' : 'ltr'}>
          {categoryOptions.map((cat) => (
            <button
              key={cat.id}
              onClick={() => setSelectedCategory(cat.id)}
              className={`px-4 py-2 rounded-xl text-xs sm:text-sm font-semibold transition-all cursor-pointer ${
                selectedCategory === cat.id
                  ? 'bg-emerald-800 text-white shadow-md shadow-emerald-900/10 scale-102 ring-2 ring-emerald-500/20'
                  : 'bg-white dark:bg-slate-900 text-slate-700 dark:text-slate-300 border border-slate-200 dark:border-slate-800 hover:bg-slate-100 dark:hover:bg-slate-800'
              }`}
            >
              {cat.label}
            </button>
          ))}
        </div>

        {/* Screenshots Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8" dir={isRtl ? 'rtl' : 'ltr'}>
          {filteredScreenshots.map((screen) => {
            const displayTitle =
              language === 'ps' && screen.titlePashto
                ? screen.titlePashto
                : language === 'ur' && screen.titleUrdu
                ? screen.titleUrdu
                : screen.title;

            const displayCaption =
              language === 'ps' && screen.captionPashto
                ? screen.captionPashto
                : language === 'ur' && screen.captionUrdu
                ? screen.captionUrdu
                : screen.caption;

            return (
              <div
                key={screen.id}
                className="bg-white dark:bg-slate-900 rounded-3xl p-5 border border-slate-200/80 dark:border-slate-800 shadow-md hover:shadow-xl transition-all space-y-4 group"
              >
                <div className="relative rounded-2xl overflow-hidden bg-slate-950 aspect-[9/16] border border-slate-800 flex items-center justify-center">
                  <img
                    src={screen.imageUrl}
                    alt={displayTitle}
                    className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
                  />

                  {/* Overlay Zoom Icon */}
                  <button
                    onClick={() => setActiveImage(screen.imageUrl)}
                    className="absolute inset-0 bg-slate-950/40 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center text-white cursor-pointer"
                    aria-label={`Enlarge ${displayTitle}`}
                  >
                    <div className="p-3 rounded-full bg-emerald-700/90 shadow-lg flex items-center gap-2 text-xs font-bold">
                      <Maximize2 className="w-4 h-4" />
                      <span>{language === 'ps' ? 'غټ نندارتون' : language === 'ur' ? 'بڑا منظر' : 'Expand Preview'}</span>
                    </div>
                  </button>
                </div>

                <div className="space-y-1">
                  <div className="flex items-center justify-between">
                    <h3 className="font-bold text-slate-900 dark:text-slate-100 text-base">
                      {displayTitle}
                    </h3>
                    <span className="text-[10px] font-bold uppercase tracking-wider px-2 py-0.5 rounded bg-emerald-100 dark:bg-emerald-950 text-emerald-800 dark:text-emerald-300">
                      {screen.category}
                    </span>
                  </div>
                  <p className="text-xs text-slate-600 dark:text-slate-400 leading-relaxed">
                    {displayCaption}
                  </p>
                </div>
              </div>
            );
          })}
        </div>

        {/* Modal Lightbox */}
        {activeImage && (
          <div
            className="fixed inset-0 z-50 bg-slate-950/90 backdrop-blur-md flex items-center justify-center p-4"
            onClick={() => setActiveImage(null)}
          >
            <div
              className="relative max-w-sm w-full bg-slate-900 rounded-3xl p-3 border border-slate-800 shadow-2xl space-y-3"
              onClick={(e) => e.stopPropagation()}
            >
              <div className="rounded-2xl overflow-hidden aspect-[9/18]">
                <img src={activeImage} alt="Expanded Screenshot" className="w-full h-full object-cover" />
              </div>
              <button
                onClick={() => setActiveImage(null)}
                className="w-full py-2.5 rounded-xl bg-slate-800 text-white font-bold text-xs hover:bg-slate-700 transition-colors cursor-pointer"
              >
                {language === 'ps' ? 'بندول' : language === 'ur' ? 'بند کریں' : 'Close Full Screen'}
              </button>
            </div>
          </div>
        )}
      </div>
    </section>
  );
};
