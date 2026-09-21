import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { 
  BookOpen, 
  HeartHandshake, 
  Moon, 
  Sparkles, 
  Video, 
  Compass, 
  Repeat, 
  Library, 
  Volume2, 
  Copy, 
  Check, 
  RefreshCw, 
  ExternalLink,
  ChevronRight,
  MapPin,
  Flame,
  Download,
  Eye,
  FileText
} from 'lucide-react';
import { useLanguage } from '../../context/LanguageContext';
import { APP_CONFIG } from '../../config/appConfig';
import { 
  QURAN_SAMPLES, 
  MASNOON_DUAS, 
  ASMA_UL_HUSNA, 
  ASMA_UN_NABI, 
  HARAMAIN_STREAMS, 
  QIBLA_CITIES, 
  TASBEEH_PRESETS, 
  LIBRARY_CLASSICS,
  SurahSample,
  MasnoonDuaItem,
  TasbeehZikr
} from '../../config/islamicFeaturesData';
import { QURAN_EDITIONS, QuranEditionItem } from '../../data/quranEditions';
import { DarsNizamiBookItem } from '../../types';
import { IslamicBookCover } from '../library/IslamicBookCover';
import { PdfModal } from '../library/PdfModal';
import { DownloadButton } from '../common/DownloadButton';
import { HaramainLivePlayer } from '../live/HaramainLivePlayer';
import { MasnoonDuasInteractive } from './MasnoonDuasInteractive';

type IslamicTab = 'quran' | 'duas' | 'asmaulhusna' | 'asmaunnabi' | 'haramain' | 'qibla' | 'tasbeeh' | 'library';

export const IslamicToolsHub: React.FC = () => {
  const { language, isRtl, t } = useLanguage();
  const [activeTab, setActiveTab] = useState<IslamicTab>('quran');

  // Quran state
  const [selectedSurah, setSelectedSurah] = useState<SurahSample>(QURAN_SAMPLES[0]);
  const [isPlayingAudio, setIsPlayingAudio] = useState(false);
  const [previewQuranBook, setPreviewQuranBook] = useState<DarsNizamiBookItem | null>(null);

  // Duas state
  const [duaCategory, setDuaCategory] = useState<string>('all');
  const [copiedDuaId, setCopiedDuaId] = useState<string | null>(null);

  // Tasbeeh state
  const [selectedZikr, setSelectedZikr] = useState<TasbeehZikr>(TASBEEH_PRESETS[0]);
  const [tasbeehCount, setTasbeehCount] = useState<number>(0);
  const [targetCount, setTargetCount] = useState<number>(33);
  const [lapCount, setLapCount] = useState<number>(0);

  // Qibla state
  const [selectedCity, setSelectedCity] = useState(QIBLA_CITIES[0]);

  // Asma ul Husna search
  const [nameSearch, setNameSearch] = useState('');

  // Handle Tasbeeh Count
  const handleTasbeehClick = () => {
    const next = tasbeehCount + 1;
    if (next >= targetCount) {
      setTasbeehCount(0);
      setLapCount((prev) => prev + 1);
    } else {
      setTasbeehCount(next);
    }
  };

  const handleResetTasbeeh = () => {
    setTasbeehCount(0);
    setLapCount(0);
  };

  const handleCopyText = (text: string, id: string) => {
    navigator.clipboard.writeText(text);
    setCopiedDuaId(id);
    setTimeout(() => setCopiedDuaId(null), 2000);
  };

  const tabs = [
    { id: 'quran', label: '📖 قرآن مجید', psLabel: '📖 قرآن کریم', enLabel: 'Holy Quran', icon: BookOpen },
    { id: 'duas', label: '🤲 مسنون دعائیں', psLabel: '🤲 مسنونې دعاګانې', enLabel: 'Masnoon Duas', icon: HeartHandshake },
    { id: 'asmaulhusna', label: '🌙 99 اسمائے حسنیٰ', psLabel: '🌙 د الله ۹۹ مبارک نومونه', enLabel: '99 Names of Allah', icon: Moon },
    { id: 'asmaunnabi', label: 'ﷺ اسمائے محمد ﷺ', psLabel: 'ﷺ د رسول الله ﷺ مبارک نومونه', enLabel: 'Prophet ﷺ Names', icon: Sparkles },
    { id: 'haramain', label: '🕋 حرمین شریفین لائیو', psLabel: '🕋 د حرمین شریفین ژوندۍ خپرونې', enLabel: 'Haramain Live', icon: Video },
    { id: 'qibla', label: '🧭 قبلہ کمپاس', psLabel: '🧭 د قبلې قطب نما', enLabel: 'Qibla Compass', icon: Compass },
    { id: 'tasbeeh', label: '📿 تسبیح', psLabel: '📿 ډیجیټل تسبېح', enLabel: 'Digital Tasbeeh', icon: Repeat },
    { id: 'library', label: '📚 ڈیجیٹل کتب خانہ', psLabel: '📚 د درسِ نظامي کتابتون', enLabel: 'Islamic Library', icon: Library },
  ];

  const filteredDuas = MASNOON_DUAS.filter((dua) => {
    if (duaCategory === 'all') return true;
    return dua.category === duaCategory;
  });

  const filteredNames = ASMA_UL_HUSNA.filter((n) => {
    if (!nameSearch.trim()) return true;
    const term = nameSearch.toLowerCase();
    return (
      n.arabic.includes(term) ||
      n.transliteration.toLowerCase().includes(term) ||
      n.meaningUrdu.includes(term) ||
      n.meaningPashto.includes(term) ||
      n.meaningEnglish.toLowerCase().includes(term)
    );
  });

  return (
    <section id="islamic-tools" className="py-16 md:py-24 bg-white dark:bg-slate-900/90 border-b border-slate-200 dark:border-slate-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-12">
        {/* Section Header */}
        <div className="text-center max-w-3xl mx-auto space-y-4">
          <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full bg-emerald-50 dark:bg-emerald-950/80 border border-emerald-300 dark:border-emerald-800 text-emerald-800 dark:text-emerald-300 text-xs font-semibold uppercase tracking-wider">
            <Sparkles className="w-3.5 h-3.5 text-amber-500" />
            <span>{t('interactiveHubBadge')}</span>
          </div>

          <h2 className="text-2xl sm:text-3xl lg:text-4xl font-extrabold text-slate-900 dark:text-white tracking-tight">
            {t('interactiveHubTitle')}
          </h2>

          <p className="text-sm sm:text-base text-slate-600 dark:text-slate-300 leading-relaxed">
            {t('interactiveHubSubtitle')}
          </p>
        </div>

        {/* 8 Feature Tabs Selector */}
        <div className="flex flex-wrap items-center justify-center gap-2 sm:gap-3 p-2 bg-slate-100 dark:bg-slate-950 rounded-2xl border border-slate-200 dark:border-slate-800">
          {tabs.map((tab) => {
            const Icon = tab.icon;
            const isActive = activeTab === tab.id;
            return (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id as IslamicTab)}
                className={`inline-flex items-center gap-2 px-3.5 py-2.5 rounded-xl text-xs sm:text-sm font-bold transition-all cursor-pointer ${
                  isActive
                    ? 'bg-emerald-700 text-white shadow-md shadow-emerald-900/20 scale-102 ring-2 ring-emerald-500/30'
                    : 'bg-white dark:bg-slate-900 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-800 border border-slate-200 dark:border-slate-800'
                }`}
                id={`tab-${tab.id}`}
              >
                <Icon className="w-4 h-4 text-amber-400 shrink-0" />
                <span>{language === 'en' ? tab.enLabel : language === 'ps' ? tab.psLabel : tab.label}</span>
              </button>
            );
          })}
        </div>

        {/* Dynamic Interactive Tab Content */}
        <div className="bg-slate-50 dark:bg-slate-950/60 rounded-3xl p-6 sm:p-8 lg:p-10 border border-slate-200/90 dark:border-slate-800 shadow-xl relative overflow-hidden">
          
          {/* TAB 1: HOLY QURAN */}
          {activeTab === 'quran' && (
            <div className="space-y-8 animate-fade-in">
              <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-200 dark:border-slate-800 pb-6">
                <div>
                  <h3 className="text-xl sm:text-2xl font-bold text-slate-900 dark:text-white flex items-center gap-2">
                    <BookOpen className="w-6 h-6 text-emerald-600 dark:text-emerald-400" />
                    <span>{t('featureQuranTitle')}</span>
                  </h3>
                  <p className="text-xs sm:text-sm text-slate-600 dark:text-slate-300 mt-1">
                    {t('featureQuranDesc')}
                  </p>
                </div>

                {/* Surah Pills */}
                <div className="flex flex-wrap items-center gap-2">
                  {QURAN_SAMPLES.map((surah) => (
                    <button
                      key={surah.id}
                      onClick={() => setSelectedSurah(surah)}
                      className={`px-3 py-1.5 rounded-lg text-xs font-semibold transition-all cursor-pointer ${
                        selectedSurah.id === surah.id
                          ? 'bg-emerald-700 text-white shadow'
                          : 'bg-white dark:bg-slate-900 text-slate-700 dark:text-slate-300 border border-slate-200 dark:border-slate-800 hover:bg-slate-100 dark:hover:bg-slate-800'
                      }`}
                    >
                      {language === 'ps' ? surah.namePashto : language === 'ur' ? surah.nameUrdu : surah.nameEnglish}
                    </button>
                  ))}
                </div>
              </div>

              {/* Surah Display Card */}
              <div className="bg-white dark:bg-slate-900 rounded-2xl p-6 sm:p-8 border border-emerald-500/30 shadow-md space-y-6">
                <div className="flex items-center justify-between border-b border-slate-100 dark:border-slate-800 pb-4">
                  <div className="flex items-center gap-3">
                    <span className="w-10 h-10 rounded-full bg-emerald-100 dark:bg-emerald-950 text-emerald-800 dark:text-emerald-300 font-bold flex items-center justify-center text-sm border border-emerald-300 dark:border-emerald-800">
                      {selectedSurah.id}
                    </span>
                    <div>
                      <h4 className="text-lg font-bold text-slate-900 dark:text-white">
                        {selectedSurah.nameArabic} ({language === 'ps' ? selectedSurah.namePashto : language === 'ur' ? selectedSurah.nameUrdu : selectedSurah.nameEnglish})
                      </h4>
                      <span className="text-xs text-slate-500 dark:text-slate-400">
                        {selectedSurah.revelationType} • {selectedSurah.versesCount} Verses
                      </span>
                    </div>
                  </div>

                  <button
                    onClick={() => setIsPlayingAudio(!isPlayingAudio)}
                    className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-emerald-50 dark:bg-emerald-950/80 text-emerald-800 dark:text-emerald-300 text-xs font-semibold border border-emerald-200 dark:border-emerald-800 hover:bg-emerald-100 cursor-pointer transition-colors"
                  >
                    <Volume2 className={`w-4 h-4 ${isPlayingAudio ? 'animate-pulse text-emerald-500' : ''}`} />
                    <span>{isPlayingAudio ? 'Audio Sample Playing...' : 'Sample Recitation'}</span>
                  </button>
                </div>

                {/* Arabic Text */}
                <div className="py-6 px-4 bg-emerald-50/40 dark:bg-slate-950/80 rounded-xl text-center border border-emerald-100 dark:border-slate-800">
                  <p className="text-2xl sm:text-3xl lg:text-4xl text-slate-900 dark:text-amber-100 leading-loose font-serif font-arabic" dir="rtl">
                    {selectedSurah.sampleAyahArabic}
                  </p>
                </div>

                {/* Multi-language Translations */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-xs sm:text-sm">
                  <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 space-y-1">
                    <span className="text-xs font-bold text-emerald-700 dark:text-emerald-400 block font-urdu">
                      اردو ترجمہ (Urdu Translation)
                    </span>
                    <p className="text-slate-700 dark:text-slate-300 font-urdu leading-relaxed text-sm sm:text-base" dir="rtl">
                      {selectedSurah.sampleAyahUrdu}
                    </p>
                  </div>

                  <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 space-y-1">
                    <span className="text-xs font-bold text-emerald-700 dark:text-emerald-400 block font-pashto">
                      پښتو ژباړه (Pashto Translation)
                    </span>
                    <p className="text-slate-700 dark:text-slate-300 font-pashto leading-relaxed text-sm sm:text-base" dir="rtl">
                      {selectedSurah.sampleAyahPashto}
                    </p>
                  </div>
                </div>

                <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800">
                  <span className="text-xs font-bold text-slate-500 dark:text-slate-400 block mb-1 uppercase tracking-wider">
                    English Translation & Linguistic Analysis
                  </span>
                  <p className="text-slate-700 dark:text-slate-300 leading-relaxed">
                    {selectedSurah.sampleAyahEnglish}
                  </p>
                </div>
              </div>

              {/* SECTION: QURAN MAJEED PRINT EDITIONS & SCRIPTS (16, 15, 13, 10, 11, 14, 17, 18, 21 Lines) */}
              <div className="space-y-4 pt-4 border-t border-slate-200 dark:border-slate-800">
                <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-2">
                  <div>
                    <div className="flex items-center gap-2">
                      <span className="px-2.5 py-0.5 rounded-full text-xs font-bold bg-amber-400 text-slate-950 font-urdu">
                        مصحفِ مبارک
                      </span>
                      <h4 className="text-lg sm:text-xl font-bold text-slate-900 dark:text-slate-100 font-urdu">
                        قرآن مجید کے معتبر و مستند مطبوعہ نسخہ جات (PDF ڈاؤن لوڈ و مطالعہ)
                      </h4>
                    </div>
                    <p className="text-xs sm:text-sm text-slate-600 dark:text-slate-400 font-urdu mt-1">
                      ۱۶، ۱۵، ۱۳، ۱۰، ۱۱، ۱۴، ۱۷، ۱۸ اور ۲۱ سطری رنگین تجویدی و حفاظی مصاحف مع اصل صفحۂ اول (Cover) اور براہ راست ڈاؤن لوڈنگ لنک
                    </p>
                  </div>
                  <span className="text-xs font-bold px-3 py-1 rounded-full bg-emerald-100 dark:bg-emerald-950 text-emerald-800 dark:text-emerald-300 border border-emerald-300 dark:border-emerald-800 shrink-0">
                    {QURAN_EDITIONS.length} مستند نسخے
                  </span>
                </div>

                {/* Grid of Quran Editions */}
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
                  {QURAN_EDITIONS.map((quran) => (
                    <div
                      key={quran.id}
                      className="group bg-white dark:bg-slate-900 border border-slate-200/90 dark:border-slate-800/90 rounded-2xl p-4 transition-all duration-200 hover:shadow-xl hover:border-emerald-500/50 flex flex-col justify-between"
                    >
                      <div className="flex items-start gap-3.5">
                        <IslamicBookCover
                          book={quran}
                          bookId={quran.id}
                          title={quran.name}
                          titleUrdu={quran.nameUrdu}
                          pdfUrl={quran.pdfUrl}
                          author={quran.publisher}
                          category="القرآن الکریم"
                          classNameUrdu={`${quran.lines} سطری`}
                          typeUrdu={quran.typeUrdu}
                          size="md"
                        />
                        <div className="flex-1 min-w-0 space-y-1.5">
                          <div className="flex items-center gap-1.5 flex-wrap">
                            <span className="px-2 py-0.5 rounded-md text-[10px] font-bold bg-amber-400 text-slate-950 font-urdu">
                              {quran.lines} سطری
                            </span>
                            <span className="px-2 py-0.5 rounded-md text-[10px] font-bold bg-emerald-100 dark:bg-emerald-950 text-emerald-800 dark:text-emerald-300 border border-emerald-300 dark:border-emerald-800 font-urdu">
                              {quran.typeUrdu}
                            </span>
                          </div>

                          <h5
                            className="text-sm font-bold text-slate-900 dark:text-slate-100 line-clamp-2 font-urdu leading-snug group-hover:text-emerald-600 dark:group-hover:text-emerald-400 transition-colors"
                            dir="rtl"
                          >
                            {quran.nameUrdu}
                          </h5>

                          <p className="text-[11px] text-slate-500 dark:text-slate-400 font-urdu line-clamp-2 leading-relaxed" dir="rtl">
                            {quran.featuresUrdu}
                          </p>

                          <div className="text-[10px] text-slate-400 dark:text-slate-500 font-medium truncate pt-0.5">
                            {quran.publisher}
                          </div>
                        </div>
                      </div>

                      {/* Action Buttons */}
                      <div className="mt-3 pt-3 border-t border-slate-100 dark:border-slate-800 flex items-center justify-between gap-2">
                        <button
                          onClick={() => setPreviewQuranBook(quran)}
                          className="flex-1 inline-flex items-center justify-center gap-1.5 px-3 py-1.5 rounded-xl bg-emerald-50 dark:bg-emerald-950/80 hover:bg-emerald-100 dark:hover:bg-emerald-900/80 text-emerald-800 dark:text-emerald-300 text-xs font-urdu font-bold transition-all border border-emerald-300 dark:border-emerald-800 cursor-pointer"
                        >
                          <Eye className="w-3.5 h-3.5" />
                          <span>آن لائن مطالعہ</span>
                        </button>
                        <a
                          href={quran.pdfUrl}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="inline-flex items-center justify-center gap-1.5 px-3 py-1.5 rounded-xl bg-emerald-700 hover:bg-emerald-800 text-white text-xs font-urdu font-bold transition-all shadow-sm shrink-0"
                          title="ڈاؤن لوڈ کریں"
                        >
                          <Download className="w-3.5 h-3.5" />
                          <span className="hidden sm:inline">ڈاؤن لوڈ PDF</span>
                        </a>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}

          {/* TAB 2: MASNOON DUAS */}
          {activeTab === 'duas' && (
            <div className="space-y-6 animate-fade-in">
              <MasnoonDuasInteractive />
            </div>
          )}

          {/* TAB 3: ASMA UL HUSNA (99 NAMES OF ALLAH) */}
          {activeTab === 'asmaulhusna' && (
            <div className="space-y-8 animate-fade-in">
              <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-200 dark:border-slate-800 pb-6">
                <div>
                  <h3 className="text-xl sm:text-2xl font-bold text-slate-900 dark:text-white flex items-center gap-2">
                    <Moon className="w-6 h-6 text-emerald-600 dark:text-emerald-400" />
                    <span>{t('featureAsmaulHusnaTitle')}</span>
                  </h3>
                  <p className="text-xs sm:text-sm text-slate-600 dark:text-slate-300 mt-1">
                    {t('featureAsmaulHusnaDesc')}
                  </p>
                </div>

                {/* Search box */}
                <input
                  id="asma-ul-husna-search-input"
                  name="asmaUlHusnaSearch"
                  type="text"
                  placeholder={
                    language === 'ps'
                      ? 'د مبارک نوم یا مانا لټون وکړئ...'
                      : language === 'ur'
                      ? 'اسمِ مبارک یا معنی تلاش کریں...'
                      : 'Search Name or Meaning...'
                  }
                  value={nameSearch}
                  onChange={(e) => setNameSearch(e.target.value)}
                  className="px-4 py-2 rounded-xl text-xs sm:text-sm bg-white dark:bg-slate-900 border border-slate-300 dark:border-slate-700 text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-emerald-500 w-full sm:w-64"
                />
              </div>

              {/* Names Grid */}
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                {filteredNames.map((name) => (
                  <div
                    key={name.number}
                    className="p-5 bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm hover:border-emerald-500/50 hover:shadow-md transition-all text-center space-y-3"
                  >
                    <span className="w-7 h-7 rounded-full bg-emerald-100 dark:bg-emerald-950/80 text-emerald-800 dark:text-emerald-300 font-bold text-xs inline-flex items-center justify-center border border-emerald-300 dark:border-emerald-800">
                      {name.number}
                    </span>

                    <h4 className="text-3xl font-bold text-emerald-900 dark:text-amber-200 font-arabic font-serif py-1" dir="rtl">
                      {name.arabic}
                    </h4>

                    <div className="text-xs font-semibold text-slate-700 dark:text-slate-300">
                      {name.transliteration}
                    </div>

                    <div className="space-y-1 text-xs border-t border-slate-100 dark:border-slate-800 pt-2 text-slate-600 dark:text-slate-400">
                      <p className="font-urdu text-emerald-800 dark:text-emerald-300 font-medium" dir="rtl">
                        {name.meaningUrdu}
                      </p>
                      <p className="font-pashto text-slate-500 dark:text-slate-400" dir="rtl">
                        {name.meaningPashto}
                      </p>
                    </div>

                    <div className="text-[11px] text-amber-800 dark:text-amber-400 bg-amber-50/80 dark:bg-amber-950/40 p-2 rounded-lg leading-tight font-urdu" dir="rtl">
                      {name.benefitUrdu}
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* TAB 4: ASMA UN NABI ﷺ */}
          {activeTab === 'asmaunnabi' && (
            <div className="space-y-8 animate-fade-in">
              <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-200 dark:border-slate-800 pb-6">
                <div>
                  <h3 className="text-xl sm:text-2xl font-bold text-slate-900 dark:text-white flex items-center gap-2">
                    <Sparkles className="w-6 h-6 text-amber-500" />
                    <span>{t('featureAsmaunNabiTitle')}</span>
                  </h3>
                  <p className="text-xs sm:text-sm text-slate-600 dark:text-slate-300 mt-1">
                    {t('featureAsmaunNabiDesc')}
                  </p>
                </div>
              </div>

              {/* Prophet ﷺ Names Grid */}
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                {ASMA_UN_NABI.map((nabi) => (
                  <div
                    key={nabi.number}
                    className="p-6 bg-white dark:bg-slate-900 rounded-2xl border border-amber-500/20 dark:border-amber-500/20 shadow-sm hover:border-amber-500/60 hover:shadow-lg transition-all text-center space-y-4 relative overflow-hidden"
                  >
                    <div className="text-2xl sm:text-3xl font-extrabold text-amber-900 dark:text-amber-200 font-arabic font-serif" dir="rtl">
                      {nabi.arabic}
                    </div>

                    <div className="text-xs font-bold text-slate-700 dark:text-slate-300">
                      {nabi.transliteration}
                    </div>

                    <span className="inline-block px-2.5 py-1 rounded-full bg-emerald-50 dark:bg-emerald-950 text-emerald-800 dark:text-emerald-300 text-[11px] font-bold border border-emerald-200 dark:border-emerald-800">
                      {nabi.honorific}
                    </span>

                    <div className="p-3 bg-slate-50 dark:bg-slate-950 rounded-xl space-y-1 text-xs">
                      <p className="font-urdu font-medium text-slate-800 dark:text-slate-200" dir="rtl">
                        {nabi.meaningUrdu}
                      </p>
                      <p className="font-pashto text-slate-500 dark:text-slate-400" dir="rtl">
                        {nabi.meaningPashto}
                      </p>
                    </div>

                    <p className="text-[11px] text-amber-700 dark:text-amber-400 font-urdu leading-tight" dir="rtl">
                      {nabi.virtueUrdu}
                    </p>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* TAB 5: HARAMAIN LIVE */}
          {activeTab === 'haramain' && (
            <div className="space-y-6 animate-fade-in">
              <HaramainLivePlayer />
            </div>
          )}

          {/* TAB 6: QIBLA COMPASS */}
          {activeTab === 'qibla' && (
            <div className="space-y-8 animate-fade-in">
              <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-200 dark:border-slate-800 pb-6">
                <div>
                  <h3 className="text-xl sm:text-2xl font-bold text-slate-900 dark:text-white flex items-center gap-2">
                    <Compass className="w-6 h-6 text-emerald-600 dark:text-emerald-400" />
                    <span>{t('featureQiblaTitle')}</span>
                  </h3>
                  <p className="text-xs sm:text-sm text-slate-600 dark:text-slate-300 mt-1">
                    {t('featureQiblaDesc')}
                  </p>
                </div>

                {/* City Select */}
                <div className="flex items-center gap-2">
                  <MapPin className="w-4 h-4 text-emerald-600 shrink-0" />
                  <select
                    id="qibla-city-select"
                    name="qiblaCity"
                    value={selectedCity.cityName}
                    onChange={(e) => {
                      const found = QIBLA_CITIES.find((c) => c.cityName === e.target.value);
                      if (found) setSelectedCity(found);
                    }}
                    className="px-3 py-2 rounded-xl text-xs sm:text-sm bg-white dark:bg-slate-900 border border-slate-300 dark:border-slate-700 text-slate-900 dark:text-white focus:ring-2 focus:ring-emerald-500"
                  >
                    {QIBLA_CITIES.map((c) => (
                      <option key={c.cityName} value={c.cityName}>
                        {c.cityName} ({c.cityNameUrdu}) - {c.country}
                      </option>
                    ))}
                  </select>
                </div>
              </div>

              {/* Compass Card */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-8 items-center">
                {/* Visual Compass dial */}
                <div className="flex flex-col items-center justify-center p-8 bg-white dark:bg-slate-900 rounded-3xl border border-slate-200 dark:border-slate-800 shadow-inner">
                  <div className="relative w-64 h-64 rounded-full border-4 border-emerald-700/40 dark:border-emerald-600/40 flex items-center justify-center bg-radial from-slate-50 to-emerald-50/20 dark:from-slate-950 dark:to-emerald-950/20 shadow-2xl">
                    {/* Cardinal markers */}
                    <span className="absolute top-2 font-bold text-xs text-red-500">N (0°)</span>
                    <span className="absolute bottom-2 font-bold text-xs text-slate-400">S (180°)</span>
                    <span className="absolute left-2 font-bold text-xs text-slate-400">W (270°)</span>
                    <span className="absolute right-2 font-bold text-xs text-slate-400">E (90°)</span>

                    {/* Rotating Needle pointing towards Kaaba angle */}
                    <div
                      className="absolute w-full h-full flex items-center justify-center transition-transform duration-700 ease-out pointer-events-none"
                      style={{ transform: `rotate(${selectedCity.qiblaAngle}deg)` }}
                    >
                      <div className="w-1.5 h-28 bg-gradient-to-t from-emerald-600 via-amber-400 to-amber-500 rounded-full shadow-lg relative -top-14 flex items-start justify-center">
                        <div className="w-5 h-5 rounded-full bg-amber-400 text-slate-950 font-bold text-[9px] flex items-center justify-center -top-2 shadow">
                          🕋
                        </div>
                      </div>
                    </div>

                    <div className="w-12 h-12 rounded-full bg-emerald-800 text-white font-bold text-xs flex items-center justify-center shadow-lg ring-4 ring-emerald-500/20 z-10">
                      {selectedCity.qiblaAngle}°
                    </div>
                  </div>

                  <p className="text-xs text-slate-500 dark:text-slate-400 mt-4 text-center">
                    Bearing to Kaaba: <strong className="text-emerald-700 dark:text-emerald-400">{selectedCity.qiblaAngle}° WSW</strong> from {selectedCity.cityName}
                  </p>
                </div>

                {/* Details */}
                <div className="space-y-6">
                  <div className="p-6 rounded-2xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 space-y-4">
                    <h4 className="text-lg font-bold text-slate-900 dark:text-white font-urdu">
                      {selectedCity.cityNameUrdu} سے قبلہ رخ کی تفصیل
                    </h4>

                    <div className="grid grid-cols-2 gap-4 text-xs sm:text-sm">
                      <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 space-y-1">
                        <span className="text-slate-500">
                          {language === 'ps' ? 'د قبلې زاویه:' : language === 'ur' ? 'قبلہ کا زاویہ:' : 'Qibla Direction:'}
                        </span>
                        <div className="text-xl font-extrabold text-emerald-700 dark:text-emerald-400 font-mono">
                          {selectedCity.qiblaAngle}°
                        </div>
                      </div>

                      <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 space-y-1">
                        <span className="text-slate-500">
                          {language === 'ps' ? 'تر مکې مکرمې واټن:' : language === 'ur' ? 'مکہ مکرمہ کا فاصلہ:' : 'Distance to Kaaba:'}
                        </span>
                        <div className="text-xl font-extrabold text-emerald-700 dark:text-emerald-400 font-mono">
                          {selectedCity.distanceKm} KM
                        </div>
                      </div>
                    </div>

                    <p className="text-xs text-slate-600 dark:text-slate-400 font-urdu leading-relaxed" dir={isRtl ? 'rtl' : 'ltr'}>
                      {language === 'ps'
                        ? 'په انډرایډ اپلیکیشن کې د GPS او مقناطیسي قطب نما له لارې په پوره دقت سره د قبلې رخ معلومېږي.'
                        : language === 'ur'
                        ? 'اینڈرائیڈ ایپ میں جی پی ایس سینسر اور مقناطیسی کمپاس کے ذریعے خودکار طریقے سے بالکل درست قبلہ رخ دکھایا جاتا ہے۔'
                        : 'Accurate Qibla direction is automatically calculated in the Android app using GPS sensors and magnetic compass.'}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* TAB 7: DIGITAL TASBEEH COUNTER */}
          {activeTab === 'tasbeeh' && (
            <div className="space-y-8 animate-fade-in">
              <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-200 dark:border-slate-800 pb-6">
                <div>
                  <h3 className="text-xl sm:text-2xl font-bold text-slate-900 dark:text-white flex items-center gap-2">
                    <Repeat className="w-6 h-6 text-emerald-600 dark:text-emerald-400" />
                    <span>{t('featureTasbeehTitle')}</span>
                  </h3>
                  <p className="text-xs sm:text-sm text-slate-600 dark:text-slate-300 mt-1">
                    {t('featureTasbeehDesc')}
                  </p>
                </div>

                {/* Target selector */}
                <div className="flex items-center gap-2">
                  <span className="text-xs text-slate-500 font-medium">
                    {language === 'ps' ? 'هدف:' : language === 'ur' ? 'ہدف:' : 'Target:'}
                  </span>
                  {[33, 99, 100, 1000].map((tVal) => (
                    <button
                      key={tVal}
                      onClick={() => setTargetCount(tVal)}
                      className={`px-3 py-1 rounded-lg text-xs font-bold transition-all cursor-pointer ${
                        targetCount === tVal
                          ? 'bg-emerald-700 text-white'
                          : 'bg-white dark:bg-slate-900 text-slate-700 dark:text-slate-300 border border-slate-200 dark:border-slate-800'
                      }`}
                    >
                      {tVal}
                    </button>
                  ))}
                </div>
              </div>

              {/* Zikr Presets Carousel */}
              <div className="flex flex-wrap gap-2 justify-center">
                {TASBEEH_PRESETS.map((zikr) => (
                  <button
                    key={zikr.id}
                    onClick={() => {
                      setSelectedZikr(zikr);
                      setTargetCount(zikr.defaultTarget);
                      setTasbeehCount(0);
                    }}
                    className={`px-4 py-2 rounded-xl text-xs font-bold transition-all cursor-pointer ${
                      selectedZikr.id === zikr.id
                        ? 'bg-emerald-800 text-white shadow ring-2 ring-emerald-500/40'
                        : 'bg-white dark:bg-slate-900 text-slate-700 dark:text-slate-300 border border-slate-200 dark:border-slate-800 hover:bg-slate-100'
                    }`}
                  >
                    {zikr.transliteration}
                  </button>
                ))}
              </div>

              {/* Tasbeeh Counter Circle */}
              <div className="max-w-md mx-auto bg-white dark:bg-slate-900 rounded-3xl p-8 border border-emerald-500/30 shadow-xl text-center space-y-6">
                <div className="space-y-2">
                  <h4 className="text-2xl sm:text-3xl font-extrabold text-emerald-950 dark:text-amber-200 font-arabic" dir="rtl">
                    {selectedZikr.arabic}
                  </h4>
                  <p className="text-xs text-slate-600 dark:text-slate-400 font-urdu" dir="rtl">
                    {selectedZikr.meaningUrdu}
                  </p>
                </div>

                {/* Big Interactive Tap Circle */}
                <button
                  onClick={handleTasbeehClick}
                  className="w-44 h-44 sm:w-52 sm:h-52 mx-auto rounded-full bg-gradient-to-br from-emerald-600 to-teal-800 text-white shadow-2xl hover:scale-105 active:scale-95 transition-all flex flex-col items-center justify-center border-4 border-emerald-400/40 cursor-pointer select-none ring-8 ring-emerald-500/10 group"
                  id="btn-tasbeeh-tap"
                  aria-label="Tap to count tasbeeh"
                >
                  <span className="text-xs uppercase tracking-wider text-emerald-200 font-semibold mb-1">
                    {language === 'ps' ? 'دلته کلیک وکړئ' : language === 'ur' ? 'گننے کے لیے دبائیں' : 'TAP TO COUNT'}
                  </span>
                  <span className="text-5xl sm:text-6xl font-black font-mono tracking-tight group-hover:text-amber-300 transition-colors">
                    {tasbeehCount}
                  </span>
                  <span className="text-xs text-emerald-200 font-mono mt-1">
                    {language === 'ps' ? 'ټاکل شوی هدف:' : language === 'ur' ? 'ہدف:' : 'Target:'} {targetCount}
                  </span>
                </button>

                <div className="flex items-center justify-between text-xs text-slate-500 border-t border-slate-100 dark:border-slate-800 pt-4">
                  <span>
                    {language === 'ps' ? 'بشپړ شوي پړاوونه (Laps):' : language === 'ur' ? 'مکمل تسبیحات (Laps):' : 'Completed Sets (Laps):'}{' '}
                    <strong className="text-emerald-600 font-bold">{lapCount}</strong>
                  </span>
                  <button
                    onClick={handleResetTasbeeh}
                    className="inline-flex items-center gap-1 text-slate-500 hover:text-red-500 transition-colors cursor-pointer"
                  >
                    <RefreshCw className="w-3.5 h-3.5" />
                    <span>{language === 'ps' ? 'بیا پیل (Reset)' : language === 'ur' ? 'ری سیٹ کریں' : 'Reset Counter'}</span>
                  </button>
                </div>
              </div>
            </div>
          )}

          {/* TAB 8: ISLAMIC LIBRARY & DARS-E-NIZAMI */}
          {activeTab === 'library' && (
            <div className="space-y-8 animate-fade-in">
              <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-200 dark:border-slate-800 pb-6">
                <div>
                  <h3 className="text-xl sm:text-2xl font-bold text-slate-900 dark:text-white flex items-center gap-2">
                    <Library className="w-6 h-6 text-emerald-600 dark:text-emerald-400" />
                    <span>{t('featureLibraryTitle')}</span>
                  </h3>
                  <p className="text-xs sm:text-sm text-slate-600 dark:text-slate-300 mt-1">
                    {t('featureLibraryDesc')}
                  </p>
                </div>

                <Link
                  to="/library"
                  className="inline-flex items-center gap-2 px-4 py-2.5 rounded-xl text-xs sm:text-sm font-bold bg-emerald-700 hover:bg-emerald-800 text-white transition-all shadow-md shrink-0 self-start md:self-auto"
                >
                  <BookOpen className="w-4 h-4 text-amber-300" />
                  <span>مکمل 8 سالہ کتب خانہ کھولیں (250+ کتب)</span>
                </Link>
              </div>

              {/* Library Categories Grid */}
              <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                {LIBRARY_CLASSICS.map((cat, idx) => (
                  <div
                    key={idx}
                    className="bg-white dark:bg-slate-900 rounded-2xl p-6 border border-slate-200 dark:border-slate-800 shadow-sm space-y-4"
                  >
                    <div className="border-b border-slate-100 dark:border-slate-800 pb-3">
                      <h4 className="text-base font-bold text-emerald-900 dark:text-emerald-300 font-urdu" dir="rtl">
                        {cat.categoryUrdu}
                      </h4>
                      <span className="text-xs text-slate-500 dark:text-slate-400">
                        {cat.categoryEnglish}
                      </span>
                    </div>

                    <ul className="space-y-3">
                      {cat.books.map((b, bIdx) => (
                        <li
                          key={bIdx}
                          className="p-3 rounded-xl bg-slate-50 dark:bg-slate-950 border border-slate-100 dark:border-slate-800 space-y-1"
                        >
                          <div className="flex items-center justify-between">
                            <span className="text-xs font-bold text-slate-900 dark:text-white font-arabic" dir="rtl">
                              {b.name}
                            </span>
                            <span className="text-[10px] px-2 py-0.5 rounded bg-emerald-100 dark:bg-emerald-950 text-emerald-800 dark:text-emerald-300 font-medium">
                              {b.level}
                            </span>
                          </div>
                          <p className="text-[11px] text-slate-500 font-urdu" dir="rtl">
                            مصنف: {b.author}
                          </p>
                        </li>
                      ))}
                    </ul>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Bottom Call to Action within Hub */}
          <div className="mt-10 pt-8 border-t border-slate-200 dark:border-slate-800 flex flex-col sm:flex-row items-center justify-between gap-4 bg-emerald-50/60 dark:bg-emerald-950/40 p-6 rounded-2xl border border-emerald-200/80 dark:border-emerald-800/80">
            <div className="space-y-1 text-center sm:text-left">
              <h4 className="text-base font-bold text-slate-900 dark:text-white font-urdu">
                {language === 'ur' 
                  ? 'اینڈرائیڈ پر مکمل آف لائن سہولیات حاصل کریں' 
                  : language === 'ps'
                  ? 'په انډرایډ کښې د آفلاین اسانتیاوو څخه ګټه پورته کړئ'
                  : 'Get the Full Offline Experience on Android'}
              </h4>
              <p className="text-xs text-slate-600 dark:text-slate-300 font-urdu">
                {language === 'ur'
                  ? `بیت العلم AI ورژن ${APP_CONFIG.version} ڈاؤن لوڈ کریں اور تمام اسلامی خصوصیات، مکمل قرآن مجید، درسِ نظامی کی کتب اور دینی کوئز انٹرنیٹ کے بغیر استعمال کریں۔`
                  : language === 'ps'
                  ? `د بیت العلم AI نسخه ${APP_CONFIG.version} ډاونلوډ کړئ او ټول اسلامي اوزار، بشپړ قرآن کریم، د درسِ نظامي کتابونه او دیني کوئز بې له انټرنیټه وکاروئ.`
                  : `Download Baytul Ilm AI v${APP_CONFIG.version} to access all Islamic tools, full Quran recitations, Dars-e-Nizami books, and chapter quizzes completely offline.`}
              </p>
            </div>

            <DownloadButton variant="hero" size="md" showDetails={false} />
          </div>

        </div>
      </div>

      {/* PDF Modal Viewer for Quran Majeed Editions */}
      <PdfModal book={previewQuranBook} onClose={() => setPreviewQuranBook(null)} />
    </section>
  );
};
