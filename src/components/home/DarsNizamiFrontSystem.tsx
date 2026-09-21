import React, { useState, useMemo } from 'react';
import {
  BookOpen,
  Search,
  GraduationCap,
  Sparkles,
  Layers,
  X,
  Eye,
  CheckCircle2,
  BookMarked,
  Languages,
  Download,
  Share2,
  ScrollText
} from 'lucide-react';
import { ALL_DARS_NIZAMI_BOOKS, DARS_YEARS_META, DarsYearMeta } from '../../data/darsNizamiBooks';
import { DarsNizamiBookItem } from '../../types';
import { BookCard } from '../library/BookCard';
import { PdfModal } from '../library/PdfModal';
import { IslamicPatternBg } from '../layout/IslamicPatternBg';
import { useLanguage } from '../../context/LanguageContext';
import { CategoryPortalCards, MainPortalSection } from './CategoryPortalCards';
import { AsmaUlHusnaInteractive } from '../islamic/AsmaUlHusnaInteractive';
import { AsmaUnNabiInteractive } from '../islamic/AsmaUnNabiInteractive';
import { PrayerTimesWidget } from '../islamic/PrayerTimesWidget';

export const DarsNizamiFrontSystem: React.FC = () => {
  const { language, isRtl } = useLanguage();

  // Active Main Department (Dars-e-Nizami, Quran Majeed, Fatawa, Tafaseer, Asma-ul-Husna, Asma-un-Nabi)
  const [mainSection, setMainSection] = useState<MainPortalSection>('dars-e-nizami');

  // Active Darja Tab (Default to '1st' year for Dars-e-Nizami)
  const [activeTab, setActiveTab] = useState<string>('1st');
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [selectedType, setSelectedType] = useState<string>('all');
  const [previewBook, setPreviewBook] = useState<DarsNizamiBookItem | null>(null);

  // Switch Main Section
  const handleSelectSection = (section: MainPortalSection) => {
    setMainSection(section);
    if (section === 'quran') {
      setActiveTab('quran');
    } else if (section === 'tafaseer') {
      setActiveTab('tafaseer');
    } else if (section === 'lughat') {
      setActiveTab('lughat');
    } else if (section === 'fatawa') {
      setActiveTab('fatawa');
    } else if (section === 'asmaulhusna') {
      setActiveTab('asmaulhusna');
    } else if (section === 'asmaunnabi') {
      setActiveTab('asmaunnabi');
    } else if (section === 'prayertimes') {
      setActiveTab('prayertimes');
    } else if (section === 'dars-e-nizami') {
      if (
        activeTab === 'quran' ||
        activeTab === 'tafaseer' ||
        activeTab === 'lughat' ||
        activeTab === 'fatawa' ||
        activeTab === 'asmaulhusna' ||
        activeTab === 'asmaunnabi' ||
        activeTab === 'prayertimes'
      ) {
        setActiveTab('1st');
      }
    }
    setSelectedType('all');
    setSearchQuery('');
  };

  // Active Darja metadata
  const activeDarjaMeta = useMemo(() => {
    if (activeTab === 'all' || activeTab === 'tafaseer' || activeTab === 'lughat' || activeTab === 'fatawa') return null;
    return DARS_YEARS_META.find((y) => y.classLevel === activeTab) || null;
  }, [activeTab]);

  // Books filtered by active tab, type, and search query
  const displayedBooks = useMemo(() => {
    return ALL_DARS_NIZAMI_BOOKS.filter((book) => {
      // If we are on 'quran' main section, show only quran editions
      if (mainSection === 'quran') {
        return book.classLevel === 'quran';
      }

      // If we are on 'tafaseer' main section, show only tafaseer books
      if (mainSection === 'tafaseer') {
        if (book.classLevel !== 'tafaseer') {
          return false;
        }
      }

      // If we are on 'lughat' main section, show only lughat books
      if (mainSection === 'lughat') {
        if (book.classLevel !== 'lughat') {
          return false;
        }
      }

      // If we are on 'fatawa' main section, show only fatawa books
      if (mainSection === 'fatawa') {
        if (book.classLevel !== 'fatawa') {
          return false;
        }
      }

      // If we are on 'dars-e-nizami' section and activeTab is NOT all, filter by classLevel
      if (mainSection === 'dars-e-nizami') {
        // Must exclude quran, tafaseer, lughat, fatawa, tareekh, tajweed-lil-huffaz, tajweed-lil-ulama from general dars-e-nizami
        if (
          book.classLevel === 'quran' ||
          book.classLevel === 'tafaseer' ||
          book.classLevel === 'lughat' ||
          book.classLevel === 'fatawa' ||
          book.classLevel === 'tareekh' ||
          book.classLevel === 'tajweed-lil-huffaz' ||
          book.classLevel === 'tajweed-lil-ulama'
        ) {
          return false;
        }
        if (activeTab !== 'all' && book.classLevel !== activeTab) {
          return false;
        }
      }

      // 2. Type filter
      if (selectedType !== 'all') {
        if (selectedType === 'main' && !book.type.includes('Main') && !book.typeUrdu?.includes('اصل') && !book.type.includes('Quran') && !book.typeUrdu?.includes('قرآن')) {
          return false;
        }
        if (selectedType === 'sharh' && !book.type.includes('Sharh') && !book.typeUrdu?.includes('شرح')) {
          return false;
        }
        if (selectedType === 'translation' && !book.type.includes('Translation') && !book.typeUrdu?.includes('ترجمہ')) {
          return false;
        }
        if (selectedType === 'notes' && !book.type.includes('Darsi') && !book.typeUrdu?.includes('تقریر')) {
          return false;
        }
      }

      // 3. Search query
      if (searchQuery.trim()) {
        const q = searchQuery.toLowerCase().trim();
        const matchName = book.name.toLowerCase().includes(q);
        const matchNameUrdu = (book.nameUrdu || '').toLowerCase().includes(q);
        const matchCategory = book.category.toLowerCase().includes(q);
        const matchClass = (book.classNameUrdu || '').toLowerCase().includes(q);
        if (!matchName && !matchNameUrdu && !matchCategory && !matchClass) {
          return false;
        }
      }

      return true;
    });
  }, [mainSection, activeTab, selectedType, searchQuery]);

  // When changing tab, reset sub-filters
  const handleTabChange = (tabId: string) => {
    setActiveTab(tabId);
    setSelectedType('all');
  };

  return (
    <section id="dars-e-nizami-portal" className="relative pt-24 sm:pt-28 pb-16 bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 overflow-hidden" dir="rtl">
      {/* Radiant Islamic Top Ambient Background */}
      <div className="absolute top-0 left-0 right-0 h-96 bg-gradient-to-b from-emerald-950 via-teal-950 to-slate-950 text-white overflow-hidden pointer-events-none">
        <IslamicPatternBg variant="hero" />
        <div className="absolute top-0 left-1/2 -translate-x-1/2 w-full max-w-[700px] h-[350px] bg-emerald-500/15 blur-[120px] rounded-full pointer-events-none" />
        <div className="absolute top-10 right-0 sm:right-10 w-full max-w-[350px] h-[300px] bg-amber-500/10 blur-[100px] rounded-full pointer-events-none" />
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-8">
        {/* =========================================================================
            1. PREMIER CALLIGRAPHIC HEADER (Right at the Front)
            ========================================================================= */}
        <div className="text-center space-y-4 pt-4 sm:pt-6">
          {/* Bismillah */}
          <div className="flex items-center justify-center">
            <span className="font-arabic text-xl sm:text-2xl md:text-3xl text-amber-300 font-serif tracking-widest drop-shadow">
              بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ
            </span>
          </div>

          {/* Badge */}
          <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-emerald-900/90 text-amber-300 border border-amber-400/40 shadow-lg text-xs font-bold uppercase tracking-wider backdrop-blur-md">
            <GraduationCap className="w-4 h-4 text-amber-400" />
            <span className="font-urdu">درسِ نظامی ڈیجیٹل کتب خانہ • سال اول تا دورۂ حدیث شریف</span>
          </div>

          {/* Main Title */}
          <h1 className="text-3xl sm:text-4xl md:text-5xl font-black text-white font-urdu tracking-tight leading-tight drop-shadow-md">
            جامع نظامِ نصاب درسِ نظامی و شروحات
          </h1>

          {/* Subtitle */}
          <p className="max-w-3xl mx-auto text-sm sm:text-base text-emerald-100/90 font-urdu leading-relaxed px-2">
            کسی بھی درجہ پر کلک کریں اور اس درجہ کی تمام اصل درسی کتب، مستند عربی و اردو شروحات، حواشی اور تراجم فوری طور پر آن لائن پڑھیں یا براہ راست PDF ڈاؤن لوڈ کریں۔
          </p>

          {/* Quick Real-Time Search Bar */}
          <div className="max-w-2xl mx-auto pt-2">
            <div className="relative flex items-center bg-white dark:bg-slate-900 rounded-2xl shadow-xl border border-slate-200/90 dark:border-slate-800 p-1.5 focus-within:border-emerald-500 focus-within:ring-2 focus-within:ring-emerald-500/20 transition-all">
              <Search className="w-5 h-5 text-slate-400 mr-3 shrink-0" />
              <input
                id="dars-nizami-books-search"
                name="darsNizamiSearch"
                type="text"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder="تمام کتب میں سے تلاش کریں... (مثلاً: قدوری، نحو میر، ہدایہ، کافیہ، مشکوٰۃ، جلالین)"
                className="w-full min-w-0 flex-1 bg-transparent text-sm sm:text-base text-slate-900 dark:text-slate-100 placeholder-slate-400 focus:outline-none px-2 font-urdu"
                dir="rtl"
              />
              {searchQuery && (
                <button
                  onClick={() => setSearchQuery('')}
                  className="p-1.5 rounded-lg text-slate-400 hover:text-slate-600 dark:hover:text-slate-200"
                  title="تلاش ختم کریں"
                >
                  <X className="w-4 h-4" />
                </button>
              )}
            </div>
          </div>
        </div>

        {/* =========================================================================
            2. TOP MAIN PORTAL CARDS (Dars-e-Nizami, Quran Majeed, Tafaseer, Lughat, Fatawa)
            ========================================================================= */}
        <CategoryPortalCards
          activeSection={mainSection}
          onSelectSection={handleSelectSection}
          quranCount={11}
          darsNizamiCount={ALL_DARS_NIZAMI_BOOKS.length - 11 - 42 - 10}
          fatawaCount={0}
          tafaseerCount={42}
          lughatCount={10}
        />

        {/* =========================================================================
            3. SECTION-SPECIFIC CONTROLS & VIEWS
            ========================================================================= */}
        {mainSection === 'asmaulhusna' && (
          <div className="space-y-4 animate-fade-in">
            <AsmaUlHusnaInteractive />
          </div>
        )}

        {mainSection === 'asmaunnabi' && (
          <div className="space-y-4 animate-fade-in">
            <AsmaUnNabiInteractive />
          </div>
        )}

        {mainSection === 'prayertimes' && (
          <div className="space-y-4 animate-fade-in">
            <PrayerTimesWidget />
          </div>
        )}

        {mainSection === 'dars-e-nizami' && (
          <div className="space-y-3">
            <div className="flex items-center justify-between px-1">
              <div className="flex items-center gap-2">
                <Layers className="w-5 h-5 text-emerald-700 dark:text-emerald-400" />
                <h2 className="text-base sm:text-lg font-black text-slate-900 dark:text-slate-100 font-urdu">
                  درجاتِ درسِ نظامی (سال اول تا دورۂ حدیث شریف)
                </h2>
              </div>
              <span className="text-xs font-semibold text-emerald-800 dark:text-emerald-400 bg-emerald-100 dark:bg-emerald-950/80 px-2.5 py-1 rounded-full border border-emerald-300 dark:border-emerald-800">
                کل نصابی کتب: {ALL_DARS_NIZAMI_BOOKS.length - 11}
              </span>
            </div>

            {/* Horizontal Scrollable Tabs Strip */}
            <div className="relative">
              <div className="flex items-center gap-2 sm:gap-2.5 overflow-x-auto pb-2 scrollbar-thin scrollbar-thumb-emerald-600/30">
                {/* Tab: All Books */}
                <button
                  onClick={() => handleTabChange('all')}
                  className={`shrink-0 px-4 py-3 rounded-2xl font-urdu font-bold text-xs sm:text-sm transition-all duration-200 flex flex-col items-center justify-center gap-0.5 min-w-[100px] border shadow-sm ${
                    activeTab === 'all'
                      ? 'bg-gradient-to-r from-emerald-700 to-teal-800 text-white border-emerald-500 shadow-md scale-[1.02] ring-2 ring-amber-400/50'
                      : 'bg-white dark:bg-slate-900 text-slate-700 dark:text-slate-300 border-slate-200 dark:border-slate-800 hover:bg-emerald-50/60 dark:hover:bg-slate-800/80'
                  }`}
                >
                  <span>تمام درجات</span>
                  <span className={`text-[10px] font-normal ${activeTab === 'all' ? 'text-amber-300' : 'text-slate-400'}`}>
                    {ALL_DARS_NIZAMI_BOOKS.length - 11} کتب
                  </span>
                </button>

                {/* Individual Tabs for Each Darja (1st through 8th Year) */}
                {DARS_YEARS_META.filter((darja) => darja.classLevel !== 'quran').map((darja) => {
                  const isActive = activeTab === darja.classLevel;
                  return (
                    <button
                      key={darja.id}
                      onClick={() => handleTabChange(darja.classLevel)}
                      className={`shrink-0 px-4 py-3 rounded-2xl font-urdu font-bold text-xs sm:text-sm transition-all duration-200 flex flex-col items-center justify-center gap-0.5 min-w-[125px] sm:min-w-[140px] border shadow-sm ${
                        isActive
                          ? 'bg-gradient-to-br from-emerald-800 via-teal-900 to-slate-900 text-white border-amber-400 shadow-lg scale-[1.02] ring-2 ring-amber-400/60'
                          : 'bg-white dark:bg-slate-900 text-slate-800 dark:text-slate-200 border-slate-200 dark:border-slate-800 hover:border-emerald-400/60 hover:bg-emerald-50/50 dark:hover:bg-slate-800'
                      }`}
                    >
                      <span className="text-sm sm:text-base font-black truncate max-w-[150px]">
                        {darja.badge}
                      </span>
                      <span
                        className={`text-[11px] font-medium truncate max-w-[150px] ${
                          isActive ? 'text-amber-300' : 'text-slate-500 dark:text-slate-400'
                        }`}
                      >
                        {darja.nameUrdu.split('(')[0].trim()}
                      </span>
                      <span
                        className={`text-[10px] px-2 py-0.2 rounded-full mt-0.5 ${
                          isActive
                            ? 'bg-amber-400 text-slate-950 font-extrabold'
                            : 'bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400'
                        }`}
                      >
                        {darja.totalBooks} کتب
                      </span>
                    </button>
                  );
                })}
              </div>
            </div>
          </div>
        )}

        {mainSection === 'quran' && (
          <div className="p-4 sm:p-5 rounded-2xl bg-gradient-to-r from-amber-500/10 via-emerald-500/10 to-teal-500/10 border border-amber-400/30 flex flex-col sm:flex-row sm:items-center justify-between gap-3">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-amber-400 text-slate-950 flex items-center justify-center font-bold shrink-0 shadow-md">
                <BookOpen className="w-5 h-5" />
              </div>
              <div>
                <h3 className="text-base sm:text-lg font-black text-slate-900 dark:text-slate-100 font-urdu">
                  قرآن مجید کے معتبر و مستند مطبوعہ نسخہ جات
                </h3>
                <p className="text-xs text-slate-600 dark:text-slate-400 font-urdu">
                  حفاظ، طلبہ اور عام قراء کے لیے ۱۶، ۱۵، ۱۳، ۱۰، ۱۱، ۱۴، ۱۷، ۱۸ اور ۲۱ سطری مکمل رنگین تجویدی و حفاظی مصاحف
                </p>
              </div>
            </div>
            <span className="text-xs font-bold px-3 py-1 rounded-full bg-amber-400 text-slate-950 font-urdu self-start sm:self-auto shrink-0 shadow-sm">
              کل نسخے: ۱۱
            </span>
          </div>
        )}

        {mainSection === 'tafaseer' && (
          <div className="p-4 sm:p-5 rounded-2xl bg-gradient-to-r from-blue-500/10 via-indigo-500/10 to-teal-500/10 border border-blue-400/30 flex flex-col sm:flex-row sm:items-center justify-between gap-3">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-blue-600 text-white flex items-center justify-center font-bold shrink-0 shadow-md">
                <BookOpen className="w-5 h-5" />
              </div>
              <div>
                <h3 className="text-base sm:text-lg font-black text-slate-900 dark:text-slate-100 font-urdu">
                  فہرستِ معتبر تفاسیر اور براہِ راست ڈاؤن لوڈ لنکس
                </h3>
                <p className="text-xs text-slate-600 dark:text-slate-400 font-urdu">
                  تفسیر ابن کثیر، معارف القرآن، قرطبی، جلالین، بیضاوی، رشد القرآن (پشتو)، تسہیل البیان، ہدایت القرآن، اور صفوۃ التفاسیر مع مکمل مجلدات
                </p>
              </div>
            </div>
            <span className="text-xs font-bold px-3 py-1 rounded-full bg-blue-600 text-white font-urdu self-start sm:self-auto shrink-0 shadow-sm">
              کل تفاسیر: ۱۹ (۴۲ مجلدات)
            </span>
          </div>
        )}

        {mainSection === 'lughat' && (
          <div className="p-4 sm:p-5 rounded-2xl bg-gradient-to-r from-rose-500/10 via-pink-500/10 to-amber-500/10 border border-rose-400/30 flex flex-col sm:flex-row sm:items-center justify-between gap-3">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-rose-600 text-white flex items-center justify-center font-bold shrink-0 shadow-md">
                <Languages className="w-5 h-5" />
              </div>
              <div>
                <h3 className="text-base sm:text-lg font-black text-slate-900 dark:text-slate-100 font-urdu">
                  فہرستِ معتبر لغات، معاجم اور براہِ راست ڈاؤن لوڈ لنکس
                </h3>
                <p className="text-xs text-slate-600 dark:text-slate-400 font-urdu">
                  القاموس المحیط، مصباح اللغات، غیاث اللغات، فیروز اللغات، انوار البیان (۴ جلدیں) اور القاموس الجدید
                </p>
              </div>
            </div>
            <span className="text-xs font-bold px-3 py-1 rounded-full bg-rose-600 text-white font-urdu self-start sm:self-auto shrink-0 shadow-sm">
              کل لغات: ۱۰ مستند کتب
            </span>
          </div>
        )}

        {/* =========================================================================
            4. ACTIVE DARJA/CATEGORY INFORMATION BANNER & SUBJECT PILLS (when in book categories)
            ========================================================================= */}
        {mainSection !== 'asmaulhusna' && mainSection !== 'asmaunnabi' && (
          <>
            <div className="p-4 sm:p-5 rounded-2xl bg-white dark:bg-slate-900/95 border border-slate-200/90 dark:border-slate-800 shadow-sm space-y-4">
              <div className="flex flex-col md:flex-row md:items-center justify-between gap-3">
                <div>
                  <div className="flex items-center gap-2">
                    <span className="px-2.5 py-0.5 rounded-md text-xs font-extrabold bg-amber-400 text-slate-950 font-urdu">
                      {mainSection === 'quran'
                        ? 'قرآن مجید'
                        : mainSection === 'tafaseer'
                        ? 'تفاسیرِ قرآن'
                        : mainSection === 'lughat'
                        ? 'معاجم و لغات'
                        : mainSection === 'fatawa'
                        ? 'فتاویٰ و فقہ'
                        : activeDarjaMeta
                        ? activeDarjaMeta.badge
                        : 'تمام درجات'}
                    </span>
                    <h3 className="text-lg sm:text-xl font-black text-slate-900 dark:text-slate-100 font-urdu">
                      {mainSection === 'quran'
                        ? 'مستند مطبوعہ نسخہ جاتِ قرآن مجید'
                        : mainSection === 'tafaseer'
                        ? 'معتبر تفاسیر، شروحات و تراجم (اردو، پشتو، عربی)'
                        : mainSection === 'lughat'
                        ? 'معتبر معاجم و لغات (عربی، فارسی، اردو و قرآنی)'
                        : mainSection === 'fatawa'
                        ? 'فتاویٰ و فقہی ذخیرہ'
                        : activeDarjaMeta
                        ? activeDarjaMeta.nameUrdu
                        : 'مکمل نصاب درسِ نظامی (تمام کتب)'}
                    </h3>
                  </div>
                  <p className="text-xs sm:text-sm text-slate-600 dark:text-slate-400 font-urdu mt-1">
                    {mainSection === 'quran'
                      ? '۱۶، ۱۵، ۱۳، ۱۰، ۱۱، ۱۴، ۱۷، ۱۸ اور ۲۱ سطری مکمل رنگین تجویدی و حفاظی مصاحف'
                      : mainSection === 'tafaseer'
                      ? 'ابن کثیر، معارف القرآن، قرطبی، رشد القرآن، ہدایت القرآن، تسہیل البیان، مظہری و صفوۃ التفاسیر کے براہِ راست پی ڈی ایف لنکس'
                      : mainSection === 'lughat'
                      ? 'غیاث اللغات، القاموس المحیط، فیروز اللغات، مصباح اللغات، انوار البیان اور القاموس الجدید کے براہِ راست پی ڈی ایف لنکس'
                      : mainSection === 'fatawa'
                      ? 'فتاویٰ شامی، فتاویٰ عالمگیری، فتاویٰ دارالعلوم دیوبند (آپ کے ارسال کردہ لنکس شامل کیے جا رہے ہیں)'
                      : activeDarjaMeta
                      ? `مضامین و فنون: ${activeDarjaMeta.descriptionUrdu}`
                      : 'سال اول (اولیٰ) سے سال ہشتم (دورۂ حدیث شریف) تک تمام نصابی کتب و شروحات'}
                  </p>
                </div>

                <div className="flex items-center gap-3 shrink-0 flex-wrap">
                  {/* Type Switcher */}
                  <div className="flex items-center bg-slate-100 dark:bg-slate-800 p-0.5 rounded-xl text-xs font-urdu font-bold">
                    <button
                      onClick={() => setSelectedType('all')}
                      className={`px-2.5 py-1 rounded-lg transition-colors ${
                        selectedType === 'all'
                          ? 'bg-amber-400 text-slate-950 font-black shadow-sm'
                          : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-slate-200'
                      }`}
                    >
                      تمام کتب
                    </button>
                    <button
                      onClick={() => setSelectedType('main')}
                      className={`px-2.5 py-1 rounded-lg transition-colors ${
                        selectedType === 'main'
                          ? 'bg-amber-400 text-slate-950 font-black shadow-sm'
                          : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-slate-200'
                      }`}
                    >
                      اصل کتب
                    </button>
                    <button
                      onClick={() => setSelectedType('sharh')}
                      className={`px-2.5 py-1 rounded-lg transition-colors ${
                        selectedType === 'sharh'
                          ? 'bg-amber-400 text-slate-950 font-black shadow-sm'
                          : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-slate-200'
                      }`}
                    >
                      شروحات و حواشی
                    </button>
                  </div>

                  <span className="text-xs font-bold text-slate-600 dark:text-slate-400 font-urdu">
                    نمایاں: {displayedBooks.length}
                  </span>

                  {(selectedType !== 'all' || searchQuery) && (
                    <button
                      onClick={() => {
                        setSelectedType('all');
                        setSearchQuery('');
                      }}
                      className="text-xs text-rose-600 dark:text-rose-400 font-bold hover:underline font-urdu"
                    >
                      فلٹرز ختم کریں
                    </button>
                  )}
                </div>
              </div>
            </div>

            {/* =========================================================================
                5. THE BOOKS GRID / AWAITING SECTION
                ========================================================================= */}
            {displayedBooks.length > 0 ? (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 sm:gap-6">
                {displayedBooks.map((book, idx) => (
                  <BookCard
                    key={`${book.id}-${idx}`}
                    book={book}
                    onSelectForView={(b) => setPreviewBook(b)}
                  />
                ))}
              </div>
            ) : mainSection === 'fatawa' ? (
              <div className="py-16 text-center bg-white dark:bg-slate-900 rounded-3xl border-2 border-dashed border-indigo-400/40 dark:border-indigo-400/20 p-8 sm:p-12 space-y-4">
                <div className="w-16 h-16 rounded-2xl bg-indigo-500/10 text-indigo-500 mx-auto flex items-center justify-center">
                  <ScrollText className="w-8 h-8" />
                </div>
                <h4 className="text-xl sm:text-2xl font-black text-slate-900 dark:text-slate-100 font-urdu">
                  فتاویٰ کا خصوصی سیکشن تیار ہے
                </h4>
                <p className="text-sm sm:text-base text-slate-600 dark:text-slate-400 font-urdu max-w-xl mx-auto leading-relaxed">
                  آپ جیسے ہی فتاویٰ کے نام اور ڈاؤن لوڈ لنکس بھیجیں گے، وہ تمام فتاویٰ کی کتب و مجلدات اپنے اصل کور اور پی ڈی ایف کے ساتھ فوراً یہاں شامل کر دیے جائیں گے۔
                </p>
                <div className="inline-flex items-center gap-2 px-4 py-2 rounded-xl bg-indigo-500 text-white font-urdu font-bold text-sm shadow-md">
                  <Sparkles className="w-4 h-4" />
                  <span>فتاویٰ کے لنکس موصول ہوتے ہی یہاں کتب ظاہر ہو جائیں گی</span>
                </div>
              </div>
            ) : (
              <div className="py-16 text-center bg-white dark:bg-slate-900 rounded-3xl border border-slate-200 dark:border-slate-800 p-8 space-y-4">
                <BookMarked className="w-12 h-12 text-slate-400 mx-auto" />
                <h4 className="text-lg font-bold text-slate-800 dark:text-slate-200 font-urdu">
                  کوئی کتاب نہیں ملی
                </h4>
                <p className="text-sm text-slate-500 dark:text-slate-400 font-urdu max-w-md mx-auto">
                  آپ کے منتخب کردہ فلٹر یا تلاش کے الفاظ کے مطابق کوئی کتاب دستیاب نہیں ہے۔ برائے مہربانی تلاش کا لفظ تبدیل کریں یا تمام فلٹرز ختم کریں۔
                </p>
                <button
                  onClick={() => {
                    setSelectedType('all');
                    setSearchQuery('');
                  }}
                  className="inline-flex items-center gap-2 px-5 py-2.5 rounded-xl text-sm font-bold bg-emerald-700 hover:bg-emerald-800 text-white font-urdu"
                >
                  تمام فلٹرز ختم کریں
                </button>
              </div>
            )}
          </>
        )}
      </div>

      {/* Embedded High-Fidelity Online PDF Reader Modal */}
      {previewBook && (
        <PdfModal book={previewBook} onClose={() => setPreviewBook(null)} />
      )}
    </section>
  );
};
