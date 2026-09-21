import React from 'react';

interface SectionHeaderProps {
  badge?: string;
  title: string;
  arabicTitle?: string;
  subtitle?: string;
  centered?: boolean;
  className?: string;
  showBismillah?: boolean;
}

export const SectionHeader: React.FC<SectionHeaderProps> = ({
  badge,
  title,
  arabicTitle,
  subtitle,
  centered = true,
  className = '',
  showBismillah = false
}) => {
  return (
    <div className={`space-y-3.5 ${centered ? 'text-center mx-auto max-w-3xl' : ''} ${className}`}>
      {showBismillah && (
        <div className="text-center pb-1">
          <span className="font-arabic font-serif text-lg sm:text-xl text-amber-500/90 dark:text-amber-400/90 tracking-widest select-none">
            ﷽
          </span>
        </div>
      )}

      {badge && (
        <div className="inline-flex items-center gap-2 px-3.5 py-1 rounded-full text-xs font-semibold uppercase tracking-wider bg-emerald-900/60 dark:bg-emerald-950/80 text-emerald-200 dark:text-emerald-300 border border-emerald-500/40 shadow-sm">
          <span className="w-1.5 h-1.5 rounded-full bg-amber-400 animate-pulse"></span>
          {badge}
        </div>
      )}

      <div className="space-y-1.5">
        <h2 className="text-2xl sm:text-3xl md:text-4xl font-extrabold text-slate-900 dark:text-slate-100 tracking-tight font-heading-en">
          {title}
        </h2>
        {arabicTitle && (
          <p className="text-xl sm:text-2xl font-serif font-arabic text-amber-600 dark:text-amber-400 tracking-wide font-normal pt-0.5">
            {arabicTitle}
          </p>
        )}
      </div>

      {/* Ornate Islamic Gold-Emerald Ornament Divider */}
      <div className={`flex items-center gap-3 py-1 ${centered ? 'justify-center' : ''}`} aria-hidden="true">
        <div className="h-px w-16 bg-gradient-to-r from-transparent via-emerald-500 to-amber-400"></div>
        <div className="flex items-center gap-1">
          <div className="w-1.5 h-1.5 rotate-45 bg-emerald-500"></div>
          <div className="w-3 h-3 rotate-45 border border-amber-400 bg-amber-500/30 flex items-center justify-center">
            <div className="w-1 h-1 bg-amber-300 rounded-full"></div>
          </div>
          <div className="w-1.5 h-1.5 rotate-45 bg-emerald-500"></div>
        </div>
        <div className="h-px w-16 bg-gradient-to-l from-transparent via-emerald-500 to-amber-400"></div>
      </div>

      {subtitle && (
        <p className="text-base sm:text-lg text-slate-600 dark:text-slate-300 leading-relaxed font-normal">
          {subtitle}
        </p>
      )}
    </div>
  );
};
