import React, { useState, useRef, useEffect } from 'react';
import { Globe, ChevronDown } from 'lucide-react';
import { useLanguage } from '../../context/LanguageContext';
import { Language } from '../../translations/translations';

interface LanguageSwitcherProps {
  className?: string;
  variant?: 'auto' | 'compact' | 'expanded';
}

export const LanguageSwitcher: React.FC<LanguageSwitcherProps> = ({
  className = '',
  variant = 'auto',
}) => {
  const { language, setLanguage } = useLanguage();
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);

  const languages: { code: Language; label: string; shortLabel: string; flag: string }[] = [
    { code: 'ur', label: 'اردو', shortLabel: 'اردو', flag: '🇵🇰' },
    { code: 'ps', label: 'پښتو', shortLabel: 'پښتو', flag: '🇦🇫' },
    { code: 'en', label: 'English', shortLabel: 'EN', flag: '🌐' },
  ];

  const currentLang = languages.find((l) => l.code === language) || languages[0];

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent | TouchEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };
    if (isOpen) {
      document.addEventListener('mousedown', handleClickOutside);
      document.addEventListener('touchstart', handleClickOutside);
    }
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
      document.removeEventListener('touchstart', handleClickOutside);
    };
  }, [isOpen]);

  // Standard inline expanded view
  const renderExpanded = () => (
    <div className={`inline-flex items-center p-1 rounded-xl bg-slate-900/60 dark:bg-slate-900/80 border border-emerald-500/20 dark:border-slate-700/80 backdrop-blur-md ${className}`}>
      <Globe className="w-3.5 h-3.5 text-emerald-400 mx-1.5 shrink-0" />
      <div className="flex items-center gap-0.5">
        {languages.map((lang) => (
          <button
            key={lang.code}
            onClick={() => setLanguage(lang.code)}
            className={`px-2 py-1 rounded-lg text-xs font-bold transition-all cursor-pointer select-none ${
              language === lang.code
                ? 'bg-emerald-700 text-white shadow-sm'
                : 'text-slate-300 hover:text-white hover:bg-white/10'
            }`}
            aria-label={`Switch language to ${lang.label}`}
          >
            {lang.label}
          </button>
        ))}
      </div>
    </div>
  );

  // Compact mobile dropdown trigger
  const renderCompact = () => (
    <div className={`relative inline-block text-left ${className}`} ref={dropdownRef}>
      <button
        onClick={(e) => {
          e.stopPropagation();
          setIsOpen(!isOpen);
        }}
        className="inline-flex items-center gap-1 px-1.5 py-1 rounded-lg bg-slate-900/70 hover:bg-slate-900 text-white border border-emerald-500/30 hover:border-emerald-400/60 shadow-sm backdrop-blur-md cursor-pointer transition-all active:scale-95"
        aria-expanded={isOpen}
        aria-label="تبدیل زبان / Switch Language"
      >
        <Globe className="w-3 h-3 text-amber-400 shrink-0" />
        <span className="text-[11px] font-bold font-urdu leading-none">{currentLang.label}</span>
        <ChevronDown className={`w-2.5 h-2.5 text-slate-400 transition-transform duration-200 ${isOpen ? 'rotate-180' : ''}`} />
      </button>

      {isOpen && (
        <div
          onClick={(e) => e.stopPropagation()}
          className="absolute top-full mt-2 left-0 sm:left-auto sm:right-0 w-36 rounded-xl bg-slate-950 text-white shadow-2xl border border-emerald-500/50 p-1.5 z-[100] animate-fade-in backdrop-blur-2xl ring-1 ring-black/60"
        >
          {languages.map((lang) => (
            <button
              key={lang.code}
              onClick={() => {
                setLanguage(lang.code);
                setIsOpen(false);
              }}
              className={`w-full flex items-center justify-between px-2.5 py-2 rounded-lg text-xs font-bold font-urdu transition-colors cursor-pointer ${
                language === lang.code
                  ? 'bg-emerald-700 text-white shadow-sm'
                  : 'text-slate-300 hover:text-white hover:bg-emerald-950/60'
              }`}
            >
              <span className="flex items-center gap-2">
                <span className="text-sm">{lang.flag}</span>
                <span>{lang.label}</span>
              </span>
              {language === lang.code && <span className="text-xs text-amber-300 font-bold">✓</span>}
            </button>
          ))}
        </div>
      )}
    </div>
  );

  if (variant === 'expanded') {
    return renderExpanded();
  }

  if (variant === 'compact') {
    return renderCompact();
  }

  // variant === 'auto': Compact dropdown on mobile (<sm), expanded 3-button pill on tablet/desktop (sm+)
  return (
    <>
      <div className="sm:hidden">
        {renderCompact()}
      </div>
      <div className="hidden sm:inline-flex">
        {renderExpanded()}
      </div>
    </>
  );
};
