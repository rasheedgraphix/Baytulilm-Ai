import React from 'react';
import { SectionHeader } from './SectionHeader';
import { Download, Shield, Settings, CheckCircle2 } from 'lucide-react';
import { APP_CONFIG } from '../../config/appConfig';
import { useLanguage } from '../../context/LanguageContext';

export const InstallationGuide: React.FC = () => {
  const { language, isRtl, t } = useLanguage();

  const steps = [
    {
      number: '01',
      title: t('installStep1Title'),
      description: t('installStep1Desc'),
      icon: Download,
    },
    {
      number: '02',
      title: t('installStep2Title'),
      description: t('installStep2Desc'),
      icon: Settings,
    },
    {
      number: '03',
      title: t('installStep3Title'),
      description: t('installStep3Desc'),
      icon: Shield,
    },
    {
      number: '04',
      title: t('installStep4Title'),
      description: t('installStep4Desc'),
      icon: CheckCircle2,
    },
  ];

  return (
    <section className="py-16 bg-white dark:bg-slate-900 border-t border-slate-200/80 dark:border-slate-800" id="installation-guide">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-12">
        <SectionHeader
          badge={language === 'ur' ? 'آسان انسٹالیشن' : language === 'ps' ? 'آسانه انسټالول' : 'Simple Setup'}
          title={t('installGuideTitle')}
          arabicTitle="طَرِيقَةُ تَثْبِيتِ التَّطْبِيق"
          subtitle={t('installGuideSubtitle')}
        />

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6" dir={isRtl ? 'rtl' : 'ltr'}>
          {steps.map((step) => {
            const Icon = step.icon;
            return (
              <div
                key={step.number}
                className="bg-slate-50 dark:bg-slate-950/60 rounded-2xl p-6 border border-slate-200/80 dark:border-slate-800 space-y-4 hover:border-emerald-500/40 transition-all flex flex-col justify-between"
              >
                <div className="space-y-3">
                  <div className="flex items-center justify-between">
                    <span className="text-2xl font-black text-emerald-800 dark:text-emerald-400 font-mono">
                      {step.number}
                    </span>
                    <div className="p-2.5 rounded-xl bg-emerald-100 dark:bg-emerald-950/80 text-emerald-800 dark:text-emerald-300">
                      <Icon className="w-5 h-5" />
                    </div>
                  </div>
                  <h3 className="text-base font-bold text-slate-900 dark:text-slate-100 font-urdu">
                    {step.title}
                  </h3>
                  <p className="text-xs sm:text-sm text-slate-600 dark:text-slate-400 leading-relaxed font-urdu">
                    {step.description}
                  </p>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </section>
  );
};

