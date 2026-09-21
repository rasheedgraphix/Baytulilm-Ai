import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { 
  BookOpen, 
  GraduationCap, 
  ScrollText, 
  BookMarked,
  Languages,
  Sparkles,
  Moon,
  HeartHandshake,
  Radio,
  Landmark,
  Clock,
  ArrowLeft,
  ArrowRight,
  ExternalLink
} from 'lucide-react';
import { useLanguage } from '../../context/LanguageContext';

export type MainPortalSection = 
  | 'dars-e-nizami' 
  | 'quran' 
  | 'tafaseer' 
  | 'tajweed'
  | 'tajweed-ulama'
  | 'prayertimes'
  | 'duas'
  | 'tareekh'
  | 'lughat' 
  | 'fatawa' 
  | 'asmaulhusna' 
  | 'asmaunnabi'
  | 'haramainlive';

interface CategoryPortalCardsProps {
  activeSection?: MainPortalSection;
  onSelectSection?: (section: MainPortalSection) => void;
  quranCount?: number;
  darsNizamiCount?: number;
  tafaseerCount?: number;
  lughatCount?: number;
  fatawaCount?: number;
  tareekhCount?: number;
  tajweedCount?: number;
  tajweedUlamaCount?: number;
}

export const CategoryPortalCards: React.FC<CategoryPortalCardsProps> = ({
  activeSection = 'dars-e-nizami',
  onSelectSection,
  quranCount = 11,
  darsNizamiCount = 1700,
  tafaseerCount = 19,
  lughatCount = 10,
  fatawaCount = 6,
  tareekhCount = 71,
  tajweedCount = 11,
  tajweedUlamaCount = 11
}) => {
  const { isRtl } = useLanguage();
  const navigate = useNavigate();

  const categories = [
    {
      id: 'dars-e-nizami' as MainPortalSection,
      path: '/dars-e-nizami',
      titleUrdu: 'درسِ نظامی',
      titleEnglish: 'Dars-e-Nizami (8 Classes)',
      subtitleUrdu: 'مکمل ۸ سالہ نصابِ عالمیت (سال اول تا دورۂ حدیث شریف)',
      descriptionUrdu: 'صرف، نحو، فقہ، اصول، منطق، بلاغت اور صحاح ستہ کی تمام اصل کتب، شروحات و حواشی',
      badgeUrdu: '۸ سالہ درجات',
      countLabel: `${darsNizamiCount}+ کتب و شروحات`,
      icon: GraduationCap,
      accentColor: 'emerald',
      gradient: 'from-emerald-950 via-teal-950 to-slate-900',
      activeBorder: 'border-emerald-500 ring-4 ring-emerald-500/20',
      badgeBg: 'bg-emerald-500/20 text-emerald-300 border-emerald-500/40',
      iconColor: 'text-emerald-400',
      actionText: 'تمام ۸ درجات دیکھیں'
    },
    {
      id: 'quran' as MainPortalSection,
      path: '/quran',
      titleUrdu: 'قرآن مجید',
      titleEnglish: 'Holy Quran (All Scripts)',
      subtitleUrdu: 'مستند مطبوعہ مصاحف و تجویدی نسخہ جات',
      descriptionUrdu: '۱۶، ۱۵، ۱۳، ۱۰، ۱۱، ۱۴، ۱۷، ۱۸ اور ۲۱ سطری رنگین تجویدی و حفاظی نسخے',
      badgeUrdu: 'مصحفِ مبارک',
      countLabel: `${quranCount} مستند نسخے`,
      icon: BookOpen,
      accentColor: 'amber',
      gradient: 'from-amber-950/80 via-slate-900 to-slate-950',
      activeBorder: 'border-amber-400 ring-4 ring-amber-400/20',
      badgeBg: 'bg-amber-500/20 text-amber-300 border-amber-500/40',
      iconColor: 'text-amber-400',
      actionText: 'تمام نسخے ملاحظہ کریں'
    },
    {
      id: 'tafaseer' as MainPortalSection,
      path: '/tafaseer',
      titleUrdu: 'تفاسیرِ قرآن',
      titleEnglish: 'Quran Exegesis (Tafseer)',
      subtitleUrdu: 'معتبر تفاسیر، شروحات و تراجم (اردو، پشتو، عربی)',
      descriptionUrdu: 'تفسیر ابن کثیر، معارف القرآن، قرطبی، جلالین، بیضاوی، رشد القرآن فی تفسیر آیات القرآن (پشتو)',
      badgeUrdu: '۱۹ تفاسیر فعال',
      countLabel: `${tafaseerCount} مجلدات و تفاسیر`,
      icon: BookMarked,
      accentColor: 'cyan',
      gradient: 'from-cyan-950/70 via-slate-900 to-slate-950',
      activeBorder: 'border-cyan-400 ring-4 ring-cyan-400/20',
      badgeBg: 'bg-cyan-500/20 text-cyan-300 border-cyan-500/40',
      iconColor: 'text-cyan-400',
      actionText: 'تفاسیر کا صفحہ کھولیں'
    },
    {
      id: 'tajweed' as MainPortalSection,
      path: '/tajweed-lil-huffaz',
      titleUrdu: 'تجوید للحفاظ',
      titleEnglish: 'Tajweed Lil Huffaz',
      subtitleUrdu: 'قواعد التجوید، اوقاف، صرف و بنیادی اسلامیات',
      descriptionUrdu: 'خلاصة التجوید، جمال القرآن، فوائد مکیہ، جامع الوقف، علم الصرف اولین اور تعلیم الاسلام (مکمل چار حصے)',
      badgeUrdu: 'مخصوص نصابِ حفاظ',
      countLabel: `${tajweedCount} کتب و شروحات`,
      icon: Sparkles,
      accentColor: 'teal',
      gradient: 'from-teal-950 via-emerald-950 to-slate-950',
      activeBorder: 'border-teal-400 ring-4 ring-teal-400/20',
      badgeBg: 'bg-teal-500/20 text-teal-300 border-teal-500/40',
      iconColor: 'text-teal-400',
      actionText: 'تجوید للحفاظ صفحہ کھولیں'
    },
    {
      id: 'tajweed-ulama' as MainPortalSection,
      path: '/tajweed-lil-ulama',
      titleUrdu: 'تجوید للعلماء',
      titleEnglish: 'Tajweed Lil Ulama',
      subtitleUrdu: 'مقدمۃ الجزریہ، شروحات، تفہیم الوقوف، علوم القراءات و پرچہ جات',
      descriptionUrdu: 'متن الجزریہ مع ۳ مستند شروحات، معلم التجوید، علوم القرآن (مفتی تقی عثمانی)، دفاع قراءات اور وفاق کے سابقہ پرچہ جات',
      badgeUrdu: 'تخصصی نصابِ علماء',
      countLabel: `${tajweedUlamaCount} کتب و شروحات`,
      icon: GraduationCap,
      accentColor: 'cyan',
      gradient: 'from-cyan-950 via-sky-950 to-slate-950',
      activeBorder: 'border-sky-400 ring-4 ring-sky-400/20',
      badgeBg: 'bg-sky-500/20 text-sky-300 border-sky-500/40',
      iconColor: 'text-sky-400',
      actionText: 'تجوید للعلماء صفحہ کھولیں'
    },
    {
      id: 'prayertimes' as MainPortalSection,
      path: '/prayer-times',
      titleUrdu: 'اوقاتِ نماز',
      titleEnglish: 'Prayer Times',
      subtitleUrdu: 'جامعہ علوم اسلامیہ بنوری ٹاؤن کراچی کے مطابق مستند اوقات',
      descriptionUrdu: 'فجر، طلوع، اشراق، چاشت، ظہر، عصر (حنفی و شافعی)، مغرب، عشاء اور تہجد کے حسابی اوقات مع لائیو لوکیشن (GPS) و تلاش',
      badgeUrdu: 'جامعہ علوم اسلامیہ',
      countLabel: 'لائیو جی پی ایس و تلاش',
      icon: Clock,
      accentColor: 'emerald',
      gradient: 'from-emerald-950 via-teal-950 to-slate-950',
      activeBorder: 'border-emerald-400 ring-4 ring-emerald-400/30',
      badgeBg: 'bg-emerald-500/20 text-emerald-300 border-emerald-500/40',
      iconColor: 'text-emerald-400',
      actionText: 'اوقاتِ نماز دیکھیں'
    },
    {
      id: 'duas' as MainPortalSection,
      path: '/duas',
      titleUrdu: '۱۰۰ مسنون دعائیں',
      titleEnglish: '100 Authentic Masnoon & Quranic Duas',
      subtitleUrdu: 'مکمل اعراب شدہ عربی متن، اردو ترجمہ و حوالہ جات',
      descriptionUrdu: 'صبح و شام، نیند، وضو، نماز، کھانا، سفر، حفاظت، شفاء، استغفار اور قرآنی دعاؤں کا مستند مجموعہ',
      badgeUrdu: '۱۰۰ مستند دعائیں',
      countLabel: '۱۰۰ مسنون دعائیں',
      icon: HeartHandshake,
      accentColor: 'teal',
      gradient: 'from-teal-950 via-emerald-950 to-slate-950',
      activeBorder: 'border-teal-400 ring-4 ring-teal-400/30',
      badgeBg: 'bg-teal-500/20 text-teal-300 border-teal-500/40',
      iconColor: 'text-teal-400',
      actionText: 'تمام ۱۰۰ دعائیں کھولیں'
    },
    {
      id: 'tareekh' as MainPortalSection,
      path: '/tareekh',
      titleUrdu: 'تاریخ و سیرت',
      titleEnglish: 'Islamic History & Heritage',
      subtitleUrdu: 'تاریخ طبری، ابن خلدون، سیرت صحابہ و تاریخ اسلام',
      descriptionUrdu: 'امہات التاریخ، قصص القرآن، تاریخ مکہ و مدینہ، خلفائے راشدین اور تحریکاتِ آزادی کی نادر کتب',
      badgeUrdu: '۷۰+ تاریخی کتب',
      countLabel: `${tareekhCount} کتب و مجلدات`,
      icon: Landmark,
      accentColor: 'amber',
      gradient: 'from-amber-950 via-amber-900/60 to-slate-950',
      activeBorder: 'border-amber-500 ring-4 ring-amber-500/20',
      badgeBg: 'bg-amber-500/20 text-amber-300 border-amber-500/40',
      iconColor: 'text-amber-400',
      actionText: 'تاریخ کا صفحہ کھولیں'
    },
    {
      id: 'lughat' as MainPortalSection,
      path: '/lughat',
      titleUrdu: 'معاجم و لغات',
      titleEnglish: 'Arabic & Islamic Dictionaries',
      subtitleUrdu: 'عربی، فارسی، اردو اور قرآنی ڈکشنریز',
      descriptionUrdu: 'القاموس المحیط، مصباح اللغات، غیاث اللغات، فیروز اللغات، انوار البیان (۴ جلدیں) اور القاموس الجدید',
      badgeUrdu: '۱۰ معتبر لغات',
      countLabel: `${lughatCount} کتب و معاجم`,
      icon: Languages,
      accentColor: 'rose',
      gradient: 'from-rose-950/70 via-slate-900 to-slate-950',
      activeBorder: 'border-rose-400 ring-4 ring-rose-400/20',
      badgeBg: 'bg-rose-500/20 text-rose-300 border-rose-500/40',
      iconColor: 'text-rose-400',
      actionText: 'لغات کا صفحہ کھولیں'
    },
    {
      id: 'fatawa' as MainPortalSection,
      path: '/fatawa',
      titleUrdu: 'فتاویٰ و فقہی ذخیرہ',
      titleEnglish: 'Islamic Fatawa & Jurisprudence',
      subtitleUrdu: 'فقہ حنفی کے مستند و معتمد فتاویٰ کتب',
      descriptionUrdu: 'فتاویٰ شامی (رد المحتار علی الدر المختار)، فتاویٰ عالمگیری (ہندیہ)، فتاویٰ دارالعلوم دیوبند و رحیمیہ',
      badgeUrdu: 'مستند فقہ و فتاویٰ',
      countLabel: `${fatawaCount} مجلدات و کتب`,
      icon: ScrollText,
      accentColor: 'indigo',
      gradient: 'from-indigo-950/70 via-slate-900 to-slate-950',
      activeBorder: 'border-indigo-400 ring-4 ring-indigo-400/20',
      badgeBg: 'bg-indigo-500/20 text-indigo-300 border-indigo-500/40',
      iconColor: 'text-indigo-400',
      actionText: 'فتاویٰ کا صفحہ کھولیں'
    },
    {
      id: 'asmaulhusna' as MainPortalSection,
      path: '/asma-ul-husna',
      titleUrdu: 'اسماء الحسنیٰ',
      titleEnglish: '99 Names of Allah (Interactive)',
      subtitleUrdu: 'اللہ تعالیٰ کے ۹۹ مبارک نام مع اردو معانی',
      descriptionUrdu: 'الرحمن، الرحیم، القدوس سے الصبور تک — عربی و اردو معانی، فضائل و برکات اور ڈیجیٹل تسبیح کاؤنٹر',
      badgeUrdu: '۹۹ اسماء الحسنیٰ',
      countLabel: '۹۹ اسماء و صفات',
      icon: Moon,
      accentColor: 'amber',
      gradient: 'from-amber-950 via-yellow-950 to-slate-950',
      activeBorder: 'border-amber-400 ring-4 ring-amber-400/30',
      badgeBg: 'bg-amber-500/20 text-amber-300 border-amber-500/40',
      iconColor: 'text-amber-400',
      actionText: 'اسماء الحسنیٰ صفحہ کھولیں'
    },
    {
      id: 'asmaunnabi' as MainPortalSection,
      path: '/asma-un-nabi',
      titleUrdu: 'اسمائے نبی ﷺ',
      titleEnglish: '99 Names of Prophet Muhammad ﷺ',
      subtitleUrdu: 'حضرت محمد ﷺ کے ۹۹ مبارک نام و صفات',
      descriptionUrdu: 'محمد، احمد، حامد، محمود سے رحمۃ للعالمین تک — معانی، ارشاداتِ نبوی ﷺ اور درود شریف کاؤنٹر',
      badgeUrdu: '۹۹ اسمائے نبی ﷺ',
      countLabel: '۹۹ مبارک اسماء',
      icon: HeartHandshake,
      accentColor: 'emerald',
      gradient: 'from-emerald-950 via-green-950 to-slate-950',
      activeBorder: 'border-emerald-400 ring-4 ring-emerald-400/30',
      badgeBg: 'bg-emerald-500/20 text-emerald-300 border-emerald-500/40',
      iconColor: 'text-emerald-400',
      actionText: 'اسمائے نبی ﷺ صفحہ کھولیں'
    },
    {
      id: 'haramainlive' as MainPortalSection,
      path: '/haramain-live',
      titleUrdu: 'حرمین لائیو ۲۴/۷',
      titleEnglish: 'Haramain 24/7 Live Stream',
      subtitleUrdu: 'مکہ مکرمہ و مدینہ منورہ سے براہِ راست نشریات',
      descriptionUrdu: 'مسجد الحرام (کعبۃ اللہ) اور مسجد النبوی الشریف سے ۲۴ گھنٹے براہِ راست ایچ ڈی نشریات مع اذان و نماز',
      badgeUrdu: 'لائیو ۲۴/۷ HD',
      countLabel: 'مکہ و مدینہ لائیو',
      icon: Radio,
      accentColor: 'red',
      gradient: 'from-red-950 via-rose-950 to-slate-950',
      activeBorder: 'border-red-500 ring-4 ring-red-500/30',
      badgeBg: 'bg-red-500/20 text-red-300 border-red-500/40',
      iconColor: 'text-red-400',
      isLive: true,
      actionText: 'حرمین شریفین لائیو دیکھیں'
    }
  ];

  return (
    <div className="space-y-6" dir={isRtl ? 'rtl' : 'ltr'}>
      {/* Category Header Bar */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 px-1">
        <div className="space-y-1">
          <div className="flex items-center gap-2.5">
            <span className="w-2.5 h-6 rounded-full bg-gradient-to-b from-amber-400 to-amber-600 shadow-sm" />
            <h2 className="text-xl sm:text-2xl font-black text-slate-900 dark:text-slate-100 font-urdu tracking-tight">
              مرکزی شعبہ جات و درسی ابواب (Select Department)
            </h2>
          </div>
          <p className="text-xs sm:text-sm font-urdu text-slate-600 dark:text-slate-300">
            جس شعبے یا کتاب کا مطالعہ کرنا چاہتے ہیں، نیچے دیے گئے کارڈ پر کلک کریں۔ ہر شعبے کا تفصیلی الگ صفحہ کھل جائے گا۔
          </p>
        </div>

        <span className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-xl text-xs font-bold font-urdu bg-emerald-50 dark:bg-emerald-950/80 text-emerald-800 dark:text-emerald-300 border border-emerald-300 dark:border-emerald-800 shrink-0 shadow-sm">
          <Sparkles className="w-3.5 h-3.5 text-amber-500" />
          <span>تمام صفحات آزاد و فعال</span>
        </span>
      </div>

      {/* Grid of 8 Main Department Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 sm:gap-5">
        {categories.map((cat) => {
          const Icon = cat.icon;

          return (
            <Link
              key={cat.id}
              to={cat.path}
              onClick={() => {
                if (onSelectSection) onSelectSection(cat.id);
              }}
              className="group relative text-right p-5 sm:p-6 rounded-3xl transition-all duration-300 flex flex-col justify-between border overflow-hidden bg-white dark:bg-slate-900/90 text-slate-900 dark:text-white border-slate-200/90 dark:border-slate-800 shadow-md hover:shadow-2xl hover:border-emerald-500 dark:hover:border-amber-400 hover:-translate-y-1"
            >
              {/* Top Accent Gradient Bar */}
              <div 
                className={`absolute top-0 left-0 right-0 h-1.5 transition-all duration-300 ${
                  cat.accentColor === 'amber' ? 'bg-amber-400 group-hover:h-2' :
                  cat.accentColor === 'cyan' ? 'bg-cyan-500 group-hover:h-2' :
                  cat.accentColor === 'rose' ? 'bg-rose-500 group-hover:h-2' :
                  cat.accentColor === 'indigo' ? 'bg-indigo-500 group-hover:h-2' :
                  cat.accentColor === 'red' ? 'bg-red-500 group-hover:h-2' : 'bg-emerald-500 group-hover:h-2'
                }`}
              />

              {/* Top Row: Icon + Badge */}
              <div className="relative z-10 flex items-start justify-between gap-2 w-full mb-3 pt-1">
                <div className={`w-12 h-12 rounded-2xl flex items-center justify-center border shadow-sm transition-transform group-hover:scale-105 ${
                  cat.accentColor === 'red' ? 'bg-red-50 dark:bg-red-950/50 border-red-200 dark:border-red-900/60 text-red-600 dark:text-red-400' :
                  cat.accentColor === 'amber' ? 'bg-amber-50 dark:bg-amber-950/50 border-amber-200 dark:border-amber-900/60 text-amber-700 dark:text-amber-400' :
                  cat.accentColor === 'cyan' ? 'bg-cyan-50 dark:bg-cyan-950/50 border-cyan-200 dark:border-cyan-900/60 text-cyan-700 dark:text-cyan-400' :
                  cat.accentColor === 'rose' ? 'bg-rose-50 dark:bg-rose-950/50 border-rose-200 dark:border-rose-900/60 text-rose-700 dark:text-rose-400' :
                  cat.accentColor === 'indigo' ? 'bg-indigo-50 dark:bg-indigo-950/50 border-indigo-200 dark:border-indigo-900/60 text-indigo-700 dark:text-indigo-400' :
                  'bg-emerald-50 dark:bg-emerald-950/50 border-emerald-200 dark:border-emerald-900/60 text-emerald-700 dark:text-emerald-400'
                }`}>
                  <Icon className="w-6 h-6" />
                </div>

                <div className="flex flex-col items-end gap-1">
                  <span className={`px-2.5 py-0.5 rounded-full text-[10px] font-bold border font-urdu flex items-center gap-1 ${
                    cat.isLive 
                      ? 'bg-red-100 dark:bg-red-950 text-red-700 dark:text-red-300 border-red-300 dark:border-red-800'
                      : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 border-slate-200 dark:border-slate-700'
                  }`}>
                    {cat.isLive && (
                      <span className="w-2 h-2 rounded-full bg-red-500 animate-pulse" />
                    )}
                    <span>{cat.badgeUrdu}</span>
                  </span>
                  <span className="text-[11px] font-bold text-emerald-700 dark:text-emerald-400 font-urdu">
                    {cat.countLabel}
                  </span>
                </div>
              </div>

              {/* Title & Subtitle */}
              <div className="relative z-10 space-y-1.5 mb-4 text-right">
                <h3 className="text-xl sm:text-2xl font-black text-slate-900 dark:text-white font-urdu tracking-tight group-hover:text-emerald-700 dark:group-hover:text-amber-300 transition-colors">
                  {cat.titleUrdu}
                </h3>
                <p className="text-xs font-semibold text-emerald-800 dark:text-amber-200/90 font-urdu leading-snug">
                  {cat.subtitleUrdu}
                </p>
                <p className="text-[11px] text-slate-600 dark:text-slate-400 font-urdu line-clamp-2 leading-relaxed pt-1">
                  {cat.descriptionUrdu}
                </p>
              </div>

              {/* Bottom Direct Link Action */}
              <div className="relative z-10 pt-3 border-t border-slate-100 dark:border-slate-800 flex items-center justify-between text-xs font-urdu">
                <span className="font-bold text-slate-700 dark:text-amber-300 group-hover:text-emerald-700 dark:group-hover:text-white flex items-center gap-1.5 transition-colors">
                  <span>{cat.actionText}</span>
                </span>
                <div className="w-8 h-8 rounded-xl bg-slate-100 dark:bg-slate-800 group-hover:bg-emerald-600 dark:group-hover:bg-amber-400 text-slate-700 dark:text-slate-300 group-hover:text-white dark:group-hover:text-slate-950 flex items-center justify-center transition-all shadow-sm">
                  {isRtl ? (
                    <ArrowLeft className="w-4 h-4 transition-transform group-hover:-translate-x-1" />
                  ) : (
                    <ArrowRight className="w-4 h-4 transition-transform group-hover:translate-x-1" />
                  )}
                </div>
              </div>
            </Link>
          );
        })}
      </div>
    </div>
  );
};
