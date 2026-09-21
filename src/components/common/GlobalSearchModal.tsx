import React, { useState, useEffect, useMemo, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  Search, X, BookOpen, Video, Download, Clock, Heart, 
  Sparkles, ExternalLink, ArrowRight, BookMarked, Radio
} from 'lucide-react';
import { ALL_DARS_NIZAMI_BOOKS } from '../../data/darsNizamiBooks';
import { APP_CONFIG } from '../../config/appConfig';
import { useLanguage } from '../../context/LanguageContext';

interface GlobalSearchModalProps {
  isOpen: boolean;
  onClose: () => void;
}

interface SearchResultItem {
  id: string;
  title: string;
  subtitle: string;
  category: string;
  type: 'app' | 'live' | 'book' | 'feature' | 'quran';
  path: string;
  isLive?: boolean;
  badge?: string;
  keywords: string[];
}

export const GlobalSearchModal: React.FC<GlobalSearchModalProps> = ({ isOpen, onClose }) => {
  const [query, setQuery] = useState('');
  const inputRef = useRef<HTMLInputElement>(null);
  const navigate = useNavigate();
  const { language, isRtl } = useLanguage();

  // Focus input when opened
  useEffect(() => {
    if (isOpen) {
      setTimeout(() => inputRef.current?.focus(), 50);
    } else {
      setQuery('');
    }
  }, [isOpen]);

  // Handle Escape key
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape' && isOpen) {
        onClose();
      }
      // Ctrl+K or Cmd+K
      if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'k') {
        e.preventDefault();
        if (isOpen) onClose();
      }
    };
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [isOpen, onClose]);

  // Static key features list with high search intent
  const keyFeatures: SearchResultItem[] = useMemo(() => [
    {
      id: 'feature-live',
      title: 'Haramain Live 24/7 (حرمین شریفین لائیو نشریات)',
      subtitle: 'Makkah & Madinah Live Stream 24/7 with Live Azan, Salah & Tawaf',
      category: 'Live Broadcast',
      type: 'live',
      path: '/haramain-live',
      isLive: true,
      badge: '24/7 Live Stream',
      keywords: ['haramain', 'live', 'makkah', 'madinah', 'makka', 'madina', 'kaaba', 'haram', 'حرمین', 'لائیو', 'مکہ', 'مدینہ', 'کعبہ', 'نشریات', 'براہ راست']
    },
    {
      id: 'feature-app',
      title: 'Baitul Ilm Official Mobile App (بیت العلم اینڈرائیڈ ایپ)',
      subtitle: `Download v${APP_CONFIG.version} APK - Complete Offline Islamic Library & Tools`,
      category: 'Official App',
      type: 'app',
      path: '/download',
      badge: `v${APP_CONFIG.version} APK`,
      keywords: ['app', 'apk', 'download', 'mobile', 'android', 'install', 'baitul ilm', 'baytul ilm', 'ایپ', 'ڈاؤن لوڈ', 'انسٹال', 'اینڈرائیڈ', 'موبائل']
    },
    {
      id: 'feature-prayer',
      title: 'Prayer Times & Qibla (اوقاتِ نماز و قبلہ رخ)',
      subtitle: 'Accurate Salah timings with countdown to next prayer & live compass',
      category: 'Islamic Tool',
      type: 'feature',
      path: '/prayer-times',
      badge: 'Daily Salah',
      keywords: ['namaz', 'salah', 'prayer', 'fajr', 'zuhr', 'asr', 'maghrib', 'isha', 'qibla', 'نماز', 'اوقات', 'قبلہ', 'فجر', 'عصر', 'مغرب', 'عشاء']
    },
    {
      id: 'feature-duas',
      title: 'Masnoon Duas & Azkar (مسنون دعائیں و اذکار)',
      subtitle: 'Daily prophetic supplications with authentic Arabic, Urdu & English',
      category: 'Islamic Tool',
      type: 'feature',
      path: '/masnoon-duas',
      badge: 'Duas & Azkar',
      keywords: ['dua', 'duas', 'azkar', 'zikr', 'masnoon', 'دعائیں', 'اذکار', 'دعا', 'مسنون']
    },
    {
      id: 'feature-asma-husna',
      title: 'Asma-ul-Husna (اللہ تعالیٰ کے ۹۹ مبارک نام)',
      subtitle: '99 Beautiful Names of Allah with profound meanings & benefits',
      category: 'Islamic Tool',
      type: 'feature',
      path: '/asma-ul-husna',
      badge: '99 Names',
      keywords: ['asma', 'husna', 'allah', 'names', '99 names', 'اسماء', 'الحسنیٰ', 'اللہ', 'نام']
    },
    {
      id: 'feature-quran',
      title: 'Holy Quran Library (قرآن مجید کے نسخہ جات)',
      subtitle: '16-line, 15-line, 13-line Tajweedi and Hifzi Quran editions in HD PDF',
      category: 'Holy Quran',
      type: 'quran',
      path: '/quran',
      badge: 'HD Editions',
      keywords: ['quran', 'tajweed', 'hifz', 'parah', 'surah', 'قرآن', 'مصحف', 'تجوید', 'حفاظی']
    },
    {
      id: 'feature-tafaseer',
      title: 'Quran Tafaseer Collection (تفاسیرِ قرآن)',
      subtitle: 'Tafseer Ibn Kathir, Tafseer Jalalain, Tafseer Baidawi & classical commentaries',
      category: 'Tafseer',
      type: 'feature',
      path: '/tafaseer',
      badge: 'Tafseer',
      keywords: ['tafseer', 'tafsir', 'jalalain', 'ibn kathir', 'تفسیر', 'جلالین', 'ابن کثیر']
    },
    {
      id: 'feature-fatawa',
      title: 'Fatawa Collection (فتاویٰ کے مجموعات)',
      subtitle: 'Fatawa Razawiyya, Fatawa Alamgiri, Fatawa Shami & authentic jurisprudence',
      category: 'Fatawa',
      type: 'feature',
      path: '/fatawa',
      badge: 'Fatawa',
      keywords: ['fatawa', 'fatwa', 'alamgiri', 'razawiyya', 'shami', 'فتاویٰ', 'فتوی', 'عالمگیری', 'رضویہ']
    },
    {
      id: 'feature-lughat',
      title: 'Arabic & Islamic Dictionaries (عربی اسلامی لغات)',
      subtitle: 'Al-Qamoos ul Waheed, Al-Munjid, Misbah ul Lughat & Lexicons',
      category: 'Lughat',
      type: 'feature',
      path: '/lughat',
      badge: 'Dictionaries',
      keywords: ['lughat', 'dictionary', 'qamoos', 'munjid', 'arabic', 'لغات', 'قاموس', 'منجد', 'لغت']
    }
  ], []);

  // Search through features and all books
  const searchResults = useMemo(() => {
    const cleanQuery = query.trim().toLowerCase();
    if (!cleanQuery) {
      // Return top recommended shortcuts
      return keyFeatures.slice(0, 4);
    }

    const matches: { item: SearchResultItem; score: number }[] = [];

    // 1. Search in key features
    keyFeatures.forEach(feat => {
      let score = 0;
      const titleLower = feat.title.toLowerCase();
      const subLower = feat.subtitle.toLowerCase();

      if (titleLower === cleanQuery) score += 100;
      else if (titleLower.includes(cleanQuery)) score += 50;
      else if (subLower.includes(cleanQuery)) score += 20;

      if (feat.keywords.some(k => k.includes(cleanQuery) || cleanQuery.includes(k))) {
        score += 40;
      }

      if (score > 0) {
        matches.push({ item: feat, score });
      }
    });

    // 2. Search in All Books
    ALL_DARS_NIZAMI_BOOKS.forEach(book => {
      let score = 0;
      const titleLower = (book.title || '').toLowerCase();
      const authorLower = (book.author || '').toLowerCase();
      const descLower = (book.description || '').toLowerCase();
      const catLower = (book.category || '').toLowerCase();

      if (titleLower === cleanQuery) score += 90;
      else if (titleLower.startsWith(cleanQuery)) score += 45;
      else if (titleLower.includes(cleanQuery)) score += 35;
      else if (authorLower.includes(cleanQuery)) score += 20;
      else if (descLower.includes(cleanQuery)) score += 10;
      else if (catLower.includes(cleanQuery)) score += 15;

      if (score > 0) {
        matches.push({
          item: {
            id: `book-${book.id}`,
            title: book.title,
            subtitle: `${book.author ? `${book.author} • ` : ''}${book.category || 'کتاب'} (${book.pages ? `${book.pages} صفحات` : 'PDF'})`,
            category: book.category || 'Dars-e-Nizami Book',
            type: 'book',
            path: `/reader?url=${encodeURIComponent(book.pdfUrl)}&title=${encodeURIComponent(book.title)}&bookId=${encodeURIComponent(book.id)}`,
            badge: book.classLevel ? `درجہ: ${book.classLevel}` : 'PDF Book',
            keywords: [book.title, book.author || '', book.category || '']
          },
          score
        });
      }
    });

    // Sort by highest matching score (Top 1, 2, 3...)
    matches.sort((a, b) => b.score - a.score);

    return matches.slice(0, 15).map(m => m.item);
  }, [query, keyFeatures]);

  if (!isOpen) return null;

  const handleSelect = (path: string) => {
    onClose();
    navigate(path);
  };

  const suggestionPills = [
    { label: 'حرمین لائیو 24/7', q: 'haramain live' },
    { label: 'ایپ ڈاؤن لوڈ', q: 'app download' },
    { label: 'ہدایہ', q: 'ہدایہ' },
    { label: 'صحیح بخاری', q: 'بخاری' },
    { label: 'تفسیر جلالین', q: 'جلالین' },
    { label: 'اوقاتِ نماز', q: 'prayer' },
  ];

  return (
    <div 
      className="fixed inset-0 z-[100] flex items-start justify-center pt-12 sm:pt-20 px-3 sm:px-4 bg-slate-950/70 backdrop-blur-md transition-opacity animate-in fade-in duration-200"
      onClick={onClose}
    >
      <div 
        className="w-full max-w-2xl bg-white dark:bg-slate-900 rounded-2xl shadow-2xl border border-slate-200/80 dark:border-slate-800 overflow-hidden flex flex-col max-h-[82vh]"
        onClick={(e) => e.stopPropagation()}
        dir={isRtl ? 'rtl' : 'ltr'}
      >
        {/* Header Search Input */}
        <div className="relative flex items-center px-4 py-3.5 border-b border-slate-200 dark:border-slate-800 bg-slate-50/50 dark:bg-slate-950/50">
          <Search className="w-5 h-5 text-amber-500 shrink-0 mx-2" />
          <input
            ref={inputRef}
            type="text"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder={
              language === 'en' 
                ? 'Search book, App, Haramain Live, Quran, Fatawa...' 
                : 'کتاب، موبائل ایپ، حرمین لائیو، قرآن، تفاسیر تلاش کریں...'
            }
            className="w-full bg-transparent text-sm sm:text-base text-slate-900 dark:text-slate-100 placeholder-slate-400 dark:placeholder-slate-500 focus:outline-none font-urdu"
          />
          {query ? (
            <button 
              onClick={() => setQuery('')}
              className="p-1 rounded-full hover:bg-slate-200 dark:hover:bg-slate-800 text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 transition-colors"
            >
              <X className="w-4 h-4" />
            </button>
          ) : (
            <span className="text-[11px] font-mono font-semibold px-2 py-0.5 rounded bg-slate-200/80 dark:bg-slate-800 text-slate-500 dark:text-slate-400 shrink-0">
              ESC
            </span>
          )}
        </div>

        {/* Quick Suggestion Pills */}
        <div className="px-4 py-2 border-b border-slate-100 dark:border-slate-800/60 bg-slate-50/30 dark:bg-slate-900/30 flex items-center gap-1.5 overflow-x-auto no-scrollbar text-xs">
          <span className="text-slate-400 font-urdu shrink-0 text-[11px]">
            {language === 'en' ? 'Trending:' : 'فوری تلاش:'}
          </span>
          {suggestionPills.map((pill) => (
            <button
              key={pill.q}
              onClick={() => setQuery(pill.q)}
              className="px-2.5 py-1 rounded-full bg-slate-100 hover:bg-emerald-50 dark:bg-slate-800 dark:hover:bg-emerald-950/50 text-slate-700 dark:text-slate-300 hover:text-emerald-700 dark:hover:text-emerald-300 border border-slate-200 dark:border-slate-700 transition-all font-urdu text-[11px] shrink-0"
            >
              {pill.label}
            </button>
          ))}
        </div>

        {/* Search Results List */}
        <div className="overflow-y-auto p-2 sm:p-3 space-y-1.5 flex-1 divide-y divide-slate-100 dark:divide-slate-800/40">
          {searchResults.length === 0 ? (
            <div className="py-12 text-center text-slate-400 dark:text-slate-500 font-urdu">
              <BookOpen className="w-10 h-10 mx-auto mb-3 opacity-30 text-amber-500" />
              <p className="text-sm font-semibold">کوئی نتیجہ نہیں ملا</p>
              <p className="text-xs mt-1">برائے مہربانی کتاب کا نام، مصنف یا موضوع تبدیل کر کے تلاش کریں۔</p>
            </div>
          ) : (
            searchResults.map((item, index) => {
              const rankNumber = index + 1;
              const isTop3 = rankNumber <= 3;

              return (
                <div
                  key={item.id}
                  onClick={() => handleSelect(item.path)}
                  className={`group w-full p-2.5 sm:p-3 rounded-xl flex items-center justify-between gap-3 cursor-pointer transition-all ${
                    isTop3 
                      ? 'bg-gradient-to-r from-emerald-50/60 via-transparent to-amber-50/30 dark:from-emerald-950/30 dark:via-transparent dark:to-amber-950/20 hover:bg-emerald-100/60 dark:hover:bg-emerald-900/40 border border-emerald-200/60 dark:border-emerald-800/50' 
                      : 'hover:bg-slate-100/80 dark:hover:bg-slate-800/50'
                  }`}
                >
                  <div className="flex items-center gap-3 min-w-0">
                    {/* Rank Badge #1, #2, #3 */}
                    <div className={`w-7 h-7 sm:w-8 sm:h-8 rounded-lg flex items-center justify-center font-bold text-xs sm:text-sm shrink-0 ${
                      rankNumber === 1 
                        ? 'bg-amber-500 text-white shadow-md shadow-amber-500/20'
                        : rankNumber === 2
                        ? 'bg-slate-400 text-white dark:bg-slate-700'
                        : rankNumber === 3
                        ? 'bg-amber-700 text-white'
                        : 'bg-slate-100 dark:bg-slate-800 text-slate-500 dark:text-slate-400'
                    }`}>
                      {rankNumber}
                    </div>

                    {/* Icon by Type */}
                    <div className="p-2 rounded-lg bg-white dark:bg-slate-800 border border-slate-200/80 dark:border-slate-700 shrink-0 text-emerald-600 dark:text-emerald-400 shadow-sm">
                      {item.type === 'live' ? (
                        <Radio className="w-4 h-4 text-red-500 animate-pulse" />
                      ) : item.type === 'app' ? (
                        <Download className="w-4 h-4 text-emerald-600 dark:text-emerald-400" />
                      ) : item.type === 'quran' ? (
                        <BookMarked className="w-4 h-4 text-emerald-600 dark:text-emerald-400" />
                      ) : item.type === 'feature' ? (
                        <Sparkles className="w-4 h-4 text-amber-500" />
                      ) : (
                        <BookOpen className="w-4 h-4 text-slate-600 dark:text-slate-300" />
                      )}
                    </div>

                    {/* Title & Subtitle */}
                    <div className="min-w-0 flex flex-col">
                      <div className="flex items-center gap-2 flex-wrap">
                        <span className="font-bold text-xs sm:text-sm text-slate-900 dark:text-slate-100 font-urdu group-hover:text-emerald-700 dark:group-hover:text-emerald-300 truncate">
                          {item.title}
                        </span>
                        {item.isLive && (
                          <span className="px-1.5 py-0.5 rounded text-[10px] font-bold bg-red-500/10 text-red-600 dark:text-red-400 border border-red-500/30 flex items-center gap-1">
                            <span className="w-1.5 h-1.5 rounded-full bg-red-500 animate-ping" />
                            LIVE
                          </span>
                        )}
                        {rankNumber === 1 && (
                          <span className="px-1.5 py-0.2 rounded text-[10px] font-bold bg-amber-500/10 text-amber-600 dark:text-amber-400 border border-amber-500/30">
                            #1 Top Match
                          </span>
                        )}
                      </div>
                      <p className="text-[11px] sm:text-xs text-slate-500 dark:text-slate-400 truncate mt-0.5 font-urdu">
                        {item.subtitle}
                      </p>
                    </div>
                  </div>

                  {/* Right Action Trigger */}
                  <div className="flex items-center gap-1.5 shrink-0">
                    <span className="hidden sm:inline-block text-[11px] font-medium px-2 py-0.5 rounded-md bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300 border border-slate-200/60 dark:border-slate-700">
                      {item.badge || item.category}
                    </span>
                    <ArrowRight className="w-4 h-4 text-slate-400 group-hover:text-emerald-600 group-hover:translate-x-0.5 transition-all rtl:rotate-180" />
                  </div>
                </div>
              );
            })
          )}
        </div>

        {/* Footer info */}
        <div className="px-4 py-2.5 border-t border-slate-200 dark:border-slate-800 bg-slate-50/50 dark:bg-slate-950/50 flex items-center justify-between text-xs text-slate-500 dark:text-slate-400 font-urdu">
          <span>
            {language === 'en' ? 'Baytul Ilm Global Search Engine' : 'بیت العلم گلوبل سرچ انجن'}
          </span>
          <span className="text-[11px] font-mono">
            {searchResults.length} {language === 'en' ? 'results ranked' : 'نتائج دستیاب'}
          </span>
        </div>
      </div>
    </div>
  );
};
