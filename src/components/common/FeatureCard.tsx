import React from 'react';
import * as LucideIcons from 'lucide-react';
import { FeatureItem } from '../../types';
import { useLanguage } from '../../context/LanguageContext';

interface FeatureCardProps {
  feature: FeatureItem;
  className?: string;
}

export const FeatureCard: React.FC<FeatureCardProps> = ({ feature, className = '' }) => {
  const { language } = useLanguage();
  // Dynamically retrieve the Lucide icon or fallback to BookOpen
  const IconComponent = (LucideIcons as Record<string, any>)[feature.iconName] || LucideIcons.BookOpen;

  const displayTitle =
    language === 'ps' && feature.titlePashto
      ? feature.titlePashto
      : language === 'ur' && feature.titleUrdu
      ? feature.titleUrdu
      : feature.title;

  const displayDesc =
    language === 'ps' && feature.descriptionPashto
      ? feature.descriptionPashto
      : language === 'ur' && feature.descriptionUrdu
      ? feature.descriptionUrdu
      : feature.description;

  const displayHighlights =
    language === 'ps' && feature.highlightsPashto && feature.highlightsPashto.length > 0
      ? feature.highlightsPashto
      : language === 'ur' && feature.highlightsUrdu && feature.highlightsUrdu.length > 0
      ? feature.highlightsUrdu
      : feature.highlights;

  return (
    <div
      className={`group relative bg-white dark:bg-[#03241d]/90 rounded-2xl p-6 sm:p-7 border border-emerald-900/20 dark:border-emerald-500/20 shadow-sm hover:shadow-xl hover:border-amber-400/50 dark:hover:border-amber-400/50 transition-all duration-300 flex flex-col justify-between overflow-hidden ${className}`}
    >
      {/* Top accent glow & gold bar on hover */}
      <div className="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-emerald-500 via-amber-400 to-emerald-500 opacity-0 group-hover:opacity-100 transition-opacity"></div>

      <div className="space-y-4 relative z-10">
        <div className="w-13 h-13 rounded-2xl bg-emerald-50 dark:bg-emerald-950/80 border border-emerald-200/80 dark:border-emerald-700/60 text-emerald-800 dark:text-amber-300 flex items-center justify-center group-hover:scale-105 group-hover:bg-gradient-to-br group-hover:from-emerald-700 group-hover:to-teal-800 group-hover:text-amber-200 transition-all shadow-sm">
          <IconComponent className="w-6 h-6" />
        </div>

        <div className="space-y-2">
          <h3 className="text-lg sm:text-xl font-bold text-slate-900 dark:text-slate-100 group-hover:text-emerald-800 dark:group-hover:text-amber-300 transition-colors">
            {displayTitle}
          </h3>
          <p className="text-sm text-slate-600 dark:text-slate-300 leading-relaxed font-normal">
            {displayDesc}
          </p>
        </div>
      </div>

      {displayHighlights && displayHighlights.length > 0 && (
        <div className="mt-6 pt-4 border-t border-slate-100 dark:border-emerald-900/40 space-y-2 relative z-10">
          {displayHighlights.map((highlight, idx) => (
            <div key={idx} className="flex items-center gap-2 text-xs text-slate-600 dark:text-slate-300">
              <span className="w-1.5 h-1.5 rounded-full bg-amber-400 shrink-0"></span>
              <span>{highlight}</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
