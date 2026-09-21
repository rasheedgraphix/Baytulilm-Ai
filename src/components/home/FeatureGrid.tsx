import React, { useState } from 'react';
import { FEATURES_DATA } from '../../config/featuresData';
import { FeatureCard } from '../common/FeatureCard';
import { SectionHeader } from '../common/SectionHeader';
import { useLanguage } from '../../context/LanguageContext';

export const FeatureGrid: React.FC = () => {
  const [activeCategory, setActiveCategory] = useState<'all' | 'academic' | 'study' | 'ai' | 'core'>('all');
  const { language, isRtl, t } = useLanguage();

  const categories = [
    {
      id: 'all',
      label: language === 'ps' ? 'ټولې ځانګړتیاوې' : language === 'ur' ? 'تمام خصوصیات' : 'All Features'
    },
    {
      id: 'academic',
      label: language === 'ps' ? 'درسِ نظامي او کتب' : language === 'ur' ? 'درسِ نظامی اور کتب' : 'Dars-e-Nizami & Books'
    },
    {
      id: 'study',
      label: language === 'ps' ? 'دیني کوئز او ازموینې' : language === 'ur' ? 'دینی کوئز اور امتحانات' : 'Quizzes & Progress'
    },
    {
      id: 'ai',
      label: language === 'ps' ? 'اسلامي AI مرستیال' : language === 'ur' ? 'اسلامی AI معاون' : 'AI Assistant'
    },
    {
      id: 'core',
      label: language === 'ps' ? 'د اپلیکیشن اوزار' : language === 'ur' ? 'ایپ کے اوزار' : 'App Features'
    },
  ];

  const filteredFeatures = FEATURES_DATA.filter((item) => {
    if (activeCategory === 'all') return true;
    return item.category === activeCategory;
  });

  const getBadge = () => {
    if (language === 'ps') return 'د انډرایډ اپلیکیشن ځانګړتیاوې';
    if (language === 'ur') return 'اینڈرائیڈ ایپلیکیشن کی خصوصیات';
    return 'Android Application Features';
  };

  const getTitle = () => {
    if (language === 'ps') return 'په بیت العلم AI اپلیکیشن کې شاملې اسانتیاوې';
    if (language === 'ur') return 'بیت العلم AI میں شامل اہم تعلیمی و دینی سہولیات';
    return 'Built Into Baytul Ilm AI for Android';
  };

  const getSubtitle = () => {
    if (language === 'ps') return 'د زده کوونکو او استاذانو لپاره د درسِ نظامي، قرآن کریم، د کوئزونو او اسلامي علومو منظم ډیجیټل نصاب.';
    if (language === 'ur') return 'طلبہ، اساتذہ اور شائقینِ علم کے لیے قرآن پاک، درسِ نظامی، موضوعاتی کوئز اور AI علمی معاون کی جامع فراہمی۔';
    return 'Key learning tools, curriculum index, quizzes, and AI tutor built directly into the official Android application.';
  };

  return (
    <section className="py-20 bg-slate-50 dark:bg-slate-950/80">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-12">
        <SectionHeader
          badge={getBadge()}
          title={getTitle()}
          arabicTitle="مُمَيَّزَاتُ تَطْبِيقِ الأَنْدُرُويْد"
          subtitle={getSubtitle()}
        />

        {/* Category Filters */}
        <div className="flex flex-wrap items-center justify-center gap-2" dir={isRtl ? 'rtl' : 'ltr'}>
          {categories.map((cat) => (
            <button
              key={cat.id}
              onClick={() => setActiveCategory(cat.id as any)}
              className={`px-4 py-2 rounded-xl text-xs sm:text-sm font-semibold transition-all cursor-pointer ${
                activeCategory === cat.id
                  ? 'bg-emerald-800 text-white shadow-md shadow-emerald-900/10 scale-102 ring-2 ring-emerald-500/20'
                  : 'bg-white dark:bg-slate-900 text-slate-700 dark:text-slate-300 border border-slate-200 dark:border-slate-800 hover:bg-slate-100 dark:hover:bg-slate-800'
              }`}
            >
              {cat.label}
            </button>
          ))}
        </div>

        {/* Features Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6" dir={isRtl ? 'rtl' : 'ltr'}>
          {filteredFeatures.map((feature) => (
            <FeatureCard key={feature.id} feature={feature} />
          ))}
        </div>
      </div>
    </section>
  );
};
