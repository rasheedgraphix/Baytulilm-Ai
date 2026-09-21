import React, { createContext, useContext, useState, useEffect } from 'react';
import { Language, TRANSLATIONS } from '../translations/translations';

interface LanguageContextType {
  language: Language;
  setLanguage: (lang: Language) => void;
  dir: 'ltr' | 'rtl';
  isRtl: boolean;
  t: (key: string, fallback?: string) => string;
}

const LanguageContext = createContext<LanguageContextType | undefined>(undefined);

const STORAGE_KEY = 'baytul_ilm_preferred_language';

export const LanguageProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [language, setLanguageState] = useState<Language>(() => {
    try {
      const saved = localStorage.getItem(STORAGE_KEY);
      if (saved === 'en' || saved === 'ur' || saved === 'ps') {
        return saved;
      }
    } catch {
      // Ignore localStorage errors in restricted environments
    }
    return 'ur'; // Default to Urdu for authentic Islamic context or fallback to English
  });

  const dir: 'ltr' | 'rtl' = language === 'en' ? 'ltr' : 'rtl';
  const isRtl = dir === 'rtl';

  useEffect(() => {
    try {
      localStorage.setItem(STORAGE_KEY, language);
    } catch {
      // Ignore
    }

    // Apply document attributes for accessibility and layout
    document.documentElement.dir = dir;
    document.documentElement.lang = language;
    
    if (dir === 'rtl') {
      document.body.classList.add('rtl-layout');
    } else {
      document.body.classList.remove('rtl-layout');
    }
  }, [language, dir]);

  const setLanguage = (lang: Language) => {
    setLanguageState(lang);
  };

  const t = (key: string, fallback?: string): string => {
    const entry = TRANSLATIONS[key];
    if (entry && entry[language]) {
      return entry[language];
    }
    if (entry && entry.en) {
      return entry.en;
    }
    return fallback || key;
  };

  return (
    <LanguageContext.Provider value={{ language, setLanguage, dir, isRtl, t }}>
      {children}
    </LanguageContext.Provider>
  );
};

const defaultLanguageContext: LanguageContextType = {
  language: 'ur',
  setLanguage: () => {},
  dir: 'rtl',
  isRtl: true,
  t: (key: string, fallback?: string) => {
    const entry = TRANSLATIONS[key];
    if (entry && entry.ur) return entry.ur;
    if (entry && entry.en) return entry.en;
    return fallback || key;
  },
};

export const useLanguage = (): LanguageContextType => {
  const context = useContext(LanguageContext);
  if (!context) {
    return defaultLanguageContext;
  }
  return context;
};
