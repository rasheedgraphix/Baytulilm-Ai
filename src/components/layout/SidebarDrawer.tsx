import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { 
  X, 
  Search, 
  BookOpen, 
  GraduationCap, 
  BookMarked, 
  Languages, 
  ScrollText, 
  Radio, 
  Moon, 
  HeartHandshake, 
  Download, 
  Sparkles, 
  Phone, 
  ShieldCheck, 
  Image as ImageIcon, 
  Home,
  Landmark,
  Clock
} from 'lucide-react';
import { BrandLogo } from '../common/BrandLogo';
import { LanguageSwitcher } from '../common/LanguageSwitcher';
import { DownloadButton } from '../common/DownloadButton';
import { APP_CONFIG } from '../../config/appConfig';
import { useLanguage } from '../../context/LanguageContext';

interface SidebarDrawerProps {
  isOpen: boolean;
  onClose: () => void;
}

export const SidebarDrawer: React.FC<SidebarDrawerProps> = ({ isOpen, onClose }) => {
  const location = useLocation();
  const { isRtl, language, t } = useLanguage();
  const [filterText, setFilterText] = useState('');

  if (!isOpen) return null;

  const isEn = language === 'en';
  const isPs = language === 'ps';

  const mainDepartments = [
    { 
      name: isEn ? 'Home Portal' : isPs ? 'اصلي پاڼه' : 'ہوم پورٹل', 
      path: '/', 
      icon: Home, 
      badge: 'Main' 
    },
    { 
      name: isEn ? 'Dars-e-Nizami (8-Year Syllabus)' : isPs ? 'درسِ نظامي (۸ کلن نصاب)' : 'درسِ نظامی (۸ سالہ نصاب)', 
      path: '/dars-e-nizami', 
      icon: GraduationCap, 
      badge: '1700+ Books' 
    },
    { 
      name: isEn ? 'The Holy Quran (Printed Mushafs)' : isPs ? 'قرآن مجید (چاپي مصاحف)' : 'قرآن مجید (مطبوعہ مصاحف)', 
      path: '/quran', 
      icon: BookOpen, 
      badge: isEn ? '11 Editions' : '۱۱ نسخے' 
    },
    { 
      name: isEn ? 'Quranic Exegesis (Tafaseer)' : isPs ? 'د قرآن تفاسیر' : 'تفاسیرِ قرآن (معتبر شروحات)', 
      path: '/tafaseer', 
      icon: BookMarked, 
      badge: isEn ? '19 Books' : '۱۹ تفاسیر' 
    },
    { 
      name: isEn ? 'Arabic Dictionaries (Lughat)' : isPs ? 'عربي لغتونه او قاموسونه' : 'معاجم و لغات (ڈکشنریز)', 
      path: '/lughat', 
      icon: Languages, 
      badge: isEn ? '10 Books' : '۱۰ کتب' 
    },
    { 
      name: isEn ? 'Fatawa & Islamic Jurisprudence' : isPs ? 'فتاویٰ او فقهي ټولګې' : 'فتاویٰ و فقہی ذخیرہ', 
      path: '/fatawa', 
      icon: ScrollText, 
      badge: isEn ? '6 Sets' : '۶ مجموعے' 
    },
    { 
      name: isEn ? 'Tajweed for Huffaz (Rules & Books)' : isPs ? 'تجوید د حفاظو لپاره' : 'تجوید للحفاظ (نصاب و قواعد)', 
      path: '/tajweed-lil-huffaz', 
      icon: Sparkles, 
      badge: isEn ? '11 Books' : '۱۱ کتب' 
    },
    { 
      name: isEn ? 'Tajweed for Ulama (Al-Jazariyyah)' : isPs ? 'تجوید د علماء لپاره' : 'تجوید للعلماء (متن الجزریہ و شروحات)', 
      path: '/tajweed-lil-ulama', 
      icon: GraduationCap, 
      badge: isEn ? '11 Books' : '۱۱ کتب' 
    },
    { 
      name: isEn ? 'Islamic History & Seerah' : isPs ? 'اسلامي تاریخ او سیرت' : 'تاریخ و سیرت کتب خانہ', 
      path: '/tareekh', 
      icon: Landmark, 
      badge: isEn ? '71 Books' : '۷۱ کتب' 
    },
    { 
      name: isEn ? 'Asma-ul-Husna (99 Divine Names)' : isPs ? 'اسماء الحسنیٰ (۹۹ مبارک نومونه)' : 'اسماء الحسنیٰ (۹۹ نام)', 
      path: '/asma-ul-husna', 
      icon: Moon, 
      badge: isEn ? 'Interactive' : 'انٹرایکٹو' 
    },
    { 
      name: isEn ? 'Asma-un-Nabi ﷺ (99 Prophet Names)' : isPs ? 'اسمائے نبی ﷺ (۹۹ نومونه)' : 'اسمائے نبی ﷺ (۹۹ مبارک اسماء)', 
      path: '/asma-un-nabi', 
      icon: HeartHandshake, 
      badge: isEn ? 'Durood Counter' : 'درود کاؤنٹر' 
    },
    { 
      name: isEn ? 'Prayer Times (Accurate Calculation)' : isPs ? 'د لمانځه وختونه' : 'اوقاتِ نماز (جامعہ علوم اسلامیہ)', 
      path: '/prayer-times', 
      icon: Clock, 
      badge: isEn ? 'Live Daily' : 'جامعہ علوم اسلامیہ' 
    },
    { 
      name: isEn ? '100 Authentic Masnoon & Quranic Duas' : isPs ? '۱۰۰ مسنونې او قرآني دعاګانې' : '۱۰۰ مسنون و قرآنی دعائیں', 
      path: '/duas', 
      icon: HeartHandshake, 
      badge: isEn ? '100 Duas' : '۱۰۰ مستند دعائیں' 
    },
    { 
      name: isEn ? 'Haramain 24/7 Live Stream HD' : isPs ? 'حرمین شریفین ۲۴/۷ ژوندۍ بڼه' : 'حرمین شریفین ۲۴/۷ لائیو (HD)', 
      path: '/haramain-live', 
      icon: Radio, 
      isLive: true, 
      badge: '1080p' 
    },
    { 
      name: isEn ? 'Comprehensive Islamic Library' : isPs ? 'جامع اسلامي کتابتون' : 'جامع اسلامی لائبریری', 
      path: '/library', 
      icon: Sparkles, 
      badge: isEn ? 'Full Search' : 'مکمل سرچ' 
    },
  ];

  const darsNizamiClasses = [
    { 
      name: isEn ? '1st Year (Darja Awwal)' : isPs ? 'لومړۍ درجه (اول کال)' : 'درجہ اولیٰ (سال اول)', 
      path: '/dars-e-nizami/1st', 
      level: '1st' 
    },
    { 
      name: isEn ? '2nd Year (Darja Saniyah)' : isPs ? 'دویمه درجه (دویم کال)' : 'درجہ ثانیہ (سال دوم)', 
      path: '/dars-e-nizami/2nd', 
      level: '2nd' 
    },
    { 
      name: isEn ? '3rd Year (Darja Salisah)' : isPs ? 'دریمه درجه (دریم کال)' : 'درجہ ثالثہ (سال سوم)', 
      path: '/dars-e-nizami/3rd', 
      level: '3rd' 
    },
    { 
      name: isEn ? '4th Year (Darja Rabiah)' : isPs ? 'څلورمه درجه (څلورم کال)' : 'درجہ رابعہ (سال چہارم)', 
      path: '/dars-e-nizami/4th', 
      level: '4th' 
    },
    { 
      name: isEn ? '5th Year (Darja Khamisah)' : isPs ? 'پنځمه درجه (پنځم کال)' : 'درجہ خامسہ (سال پنجم)', 
      path: '/dars-e-nizami/5th', 
      level: '5th' 
    },
    { 
      name: isEn ? '6th Year (Darja Sadisah)' : isPs ? 'شپږمه درجه (شپږم کال)' : 'درجہ سادسہ (سال ششم)', 
      path: '/dars-e-nizami/6th', 
      level: '6th' 
    },
    { 
      name: isEn ? '7th Year (Darja Sabiah)' : isPs ? 'اوومه درجه (موقوف علیه)' : 'درجہ سابعہ (موقوف علیہ)', 
      path: '/dars-e-nizami/7th', 
      level: '7th' 
    },
    { 
      name: isEn ? '8th Year: Daura-e-Hadith (Al-Alamiyyah)' : isPs ? 'دوره حدیث شریف (العالمیه)' : 'دورۂ حدیث شریف (العالمیہ)', 
      path: '/dars-e-nizami/daura', 
      level: 'daura', 
      isSpecial: true 
    },
  ];

  const portalTools = [
    { 
      name: isEn ? 'Download Android App (APK)' : isPs ? 'انډرایډ اپلیکیشن ډاونلوډ (APK)' : 'ڈاؤن لوڈ موبائل ایپ (APK)', 
      path: '/download', 
      icon: Download, 
      isHot: true 
    },
    { 
      name: isEn ? 'All App Features' : isPs ? 'د اپلیکیشن ځانګړتیاوې' : 'ایپ کی تمام خصوصیات', 
      path: '/features', 
      icon: Sparkles 
    },
    { 
      name: isEn ? 'App Screenshots' : isPs ? 'د اپلیکیشن عکسونه' : 'ایپ اسکرین شاٹس', 
      path: '/screenshots', 
      icon: ImageIcon 
    },
    { 
      name: isEn ? 'Contact & Feedback' : isPs ? 'موږ سره اړیکه او ملاتړ' : 'ہم سے رابطہ و فیڈ بیک', 
      path: '/contact', 
      icon: Phone 
    },
    { 
      name: isEn ? 'Privacy Policy' : isPs ? 'د محرمیت تګلاره' : 'پرائیویسی پالیسی', 
      path: '/privacy-policy', 
      icon: ShieldCheck 
    },
  ];

  const filteredDepts = mainDepartments.filter(d => 
    d.name.toLowerCase().includes(filterText.toLowerCase())
  );

  return (
    <div className="fixed inset-0 z-[100] overflow-hidden" dir={isRtl ? 'rtl' : 'ltr'}>
      {/* Semi-transparent dark backdrop */}
      <div 
        onClick={onClose}
        className="fixed inset-0 bg-black/75 backdrop-blur-sm transition-opacity animate-fade-in cursor-pointer"
        aria-hidden="true"
      />

      {/* Slide-out Drawer Panel: Anchored on the Right in RTL, Left in LTR */}
      <div 
        className={`fixed top-0 bottom-0 ${
          isRtl ? 'right-0 animate-slide-in-right border-l' : 'left-0 animate-slide-in-left border-r'
        } w-full max-w-sm sm:max-w-md bg-[#051611] text-slate-100 h-full flex flex-col shadow-2xl border-[#d4af37]/30 z-10 overflow-hidden`}
      >
        {/* Decorative ambient gold glow in drawer */}
        <div className={`absolute top-0 ${isRtl ? 'right-0' : 'left-0'} w-64 h-64 bg-[#d4af37]/10 blur-3xl pointer-events-none`} />
        <div className={`absolute bottom-0 ${isRtl ? 'left-0' : 'right-0'} w-64 h-64 bg-emerald-500/10 blur-3xl pointer-events-none`} />

        {/* 1. DRAWER HEADER */}
        <div className="p-4 sm:p-5 border-b border-[#d4af37]/20 bg-[#071f18]/90 flex items-center justify-between gap-3 relative z-10 shrink-0">
          <div className="flex items-center gap-3">
            <BrandLogo size="md" />
            <div>
              <h3 className="text-base sm:text-lg font-black text-white tracking-tight font-arabic">
                {APP_CONFIG.appName}
              </h3>
              <p className="text-[11px] text-emerald-300 font-urdu">
                {isEn ? 'Islamic Portal & Dars-e-Nizami Navigation' : isPs ? 'مرکزي اسلامي کتابتون او د مدرسې مینو' : 'مرکزی اسلامی دارالعلوم و کتب خانہ مینو'}
              </p>
            </div>
          </div>

          <button
            onClick={onClose}
            className="p-2 rounded-xl bg-slate-900/80 hover:bg-[#d4af37] text-slate-300 hover:text-slate-950 transition-colors border border-white/10 cursor-pointer"
            aria-label="Close menu"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* 2. DRAWER SEARCH FILTER & LANGUAGE */}
        <div className="p-4 border-b border-white/10 space-y-3 bg-[#04110d] relative z-10 shrink-0">
          <div className="relative flex items-center bg-slate-900/90 rounded-xl border border-[#d4af37]/30 px-3 py-2 focus-within:border-[#d4af37]">
            <Search className={`w-4 h-4 text-[#d4af37] shrink-0 ${isRtl ? 'ml-2' : 'mr-2'}`} />
            <input
              id="sidebar-menu-filter"
              name="menuFilter"
              type="text"
              value={filterText}
              onChange={(e) => setFilterText(e.target.value)}
              placeholder={isEn ? 'Search menu items quickly...' : isPs ? 'په مینو کې چټک لټون...' : 'مینو میں فوری تلاش کریں...'}
              className="w-full bg-transparent text-xs sm:text-sm text-white placeholder-slate-400 focus:outline-none font-urdu"
            />
            {filterText && (
              <button onClick={() => setFilterText('')} className="text-slate-400 hover:text-white p-1">
                <X className="w-3.5 h-3.5" />
              </button>
            )}
          </div>

          <div className="flex items-center justify-between gap-2 pt-1">
            <span className="text-[11px] text-slate-400 font-urdu">
              {isEn ? 'Change Language:' : isPs ? 'ژبه بدله کړئ:' : 'زبان تبدیل کریں:'}
            </span>
            <LanguageSwitcher variant="expanded" />
          </div>
        </div>

        {/* 3. SCROLLABLE MENU BODY */}
        <div className="flex-1 overflow-y-auto p-4 space-y-6 relative z-10 divide-y divide-white/10 scrollbar-thin">
          {/* SECTION A: MAIN ISLAMIC DEPARTMENTS */}
          <div className="space-y-2">
            <div className="flex items-center justify-between text-xs font-bold text-[#fae19c] font-urdu px-1">
              <span>📚 {isEn ? 'Main Departments & Library' : isPs ? 'مرکزي څانګې او کتابتون' : 'مرکزی شعبہ جات و کتب خانہ'}</span>
              <span className="text-[10px] text-emerald-400">
                {filteredDepts.length} {isEn ? 'Items' : 'ابواب'}
              </span>
            </div>

            <div className="space-y-1">
              {filteredDepts.map((item) => {
                const Icon = item.icon;
                const isActive = location.pathname === item.path;
                return (
                  <Link
                    key={item.path}
                    to={item.path}
                    onClick={onClose}
                    className={`p-2.5 rounded-xl transition-all flex items-center justify-between gap-3 text-xs sm:text-sm font-urdu ${
                      isActive
                        ? 'bg-gradient-to-r from-emerald-800 to-teal-800 text-white font-bold border border-emerald-400/50 shadow-md'
                        : 'hover:bg-slate-900/90 text-slate-200 hover:text-white border border-transparent'
                    }`}
                  >
                    <div className="flex items-center gap-2.5 min-w-0">
                      <div className={`w-7 h-7 rounded-lg flex items-center justify-center shrink-0 ${
                        isActive ? 'bg-white/20 text-white' : 'bg-slate-800/80 text-[#fae19c]'
                      }`}>
                        <Icon className="w-4 h-4" />
                      </div>
                      <span className="truncate">{item.name}</span>
                    </div>

                    <div className="flex items-center gap-1.5 shrink-0">
                      {item.isLive ? (
                        <span className="inline-flex items-center gap-1 text-[10px] px-2 py-0.5 rounded-full bg-red-600/30 text-red-400 font-bold border border-red-500/40 animate-pulse">
                          <span className="w-1.5 h-1.5 rounded-full bg-red-500" />
                          {isEn ? 'Live 24/7' : 'لائیو ۲۴/۷'}
                        </span>
                      ) : (
                        <span className="text-[10px] px-2 py-0.5 rounded-full bg-slate-800/80 text-slate-300 border border-white/10">
                          {item.badge}
                        </span>
                      )}
                    </div>
                  </Link>
                );
              })}
            </div>
          </div>

          {/* SECTION B: DARS-E-NIZAMI 8 CLASSES DIRECT LINKS */}
          <div className="pt-5 space-y-2">
            <div className="flex items-center justify-between text-xs font-bold text-[#fae19c] font-urdu px-1">
              <span>🎓 {isEn ? 'Dars-e-Nizami 8 Year Classes' : isPs ? 'د درسِ نظامي ۸ درجات' : 'درسِ نظامی کے ۸ سالانہ درجات'}</span>
              <Link to="/dars-e-nizami" onClick={onClose} className="text-[10px] text-amber-400 hover:underline">
                {isEn ? 'View All →' : 'تمام دیکھیں ←'}
              </Link>
            </div>

            <div className="grid grid-cols-2 gap-1.5 pt-1">
              {darsNizamiClasses.map((cls) => {
                const isActive = location.pathname === cls.path;
                return (
                  <Link
                    key={cls.path}
                    to={cls.path}
                    onClick={onClose}
                    className={`p-2 rounded-xl text-xs font-urdu transition-all border ${
                      isRtl ? 'text-right' : 'text-left'
                    } ${
                      isActive
                        ? 'bg-amber-500/20 text-amber-200 border-amber-400/50 font-bold'
                        : cls.isSpecial
                        ? 'bg-emerald-950/60 text-emerald-200 border-emerald-500/30 hover:bg-emerald-900/60 font-bold col-span-2 text-center'
                        : 'bg-slate-900/60 text-slate-300 border-white/5 hover:bg-slate-800 hover:text-white'
                    }`}
                  >
                    <span>{cls.name}</span>
                  </Link>
                );
              })}
            </div>
          </div>

          {/* SECTION C: APP DOWNLOAD & UTILITIES */}
          <div className="pt-5 space-y-2">
            <div className="text-xs font-bold text-[#fae19c] font-urdu px-1">
              📱 {isEn ? 'App & Portal Quick Links' : isPs ? 'د اپلیکیشن او پورټل اړیکې' : 'ایپ و پورٹل کے اہم روابط'}
            </div>

            <div className="space-y-1">
              {portalTools.map((tool) => {
                const Icon = tool.icon;
                const isActive = location.pathname === tool.path;
                return (
                  <Link
                    key={tool.path}
                    to={tool.path}
                    onClick={onClose}
                    className={`p-2.5 rounded-xl transition-all flex items-center justify-between gap-3 font-urdu text-xs ${
                      isActive
                        ? 'bg-emerald-800/80 text-white font-bold border border-emerald-400/40'
                        : tool.isHot
                        ? 'bg-gradient-to-r from-emerald-950 to-teal-950 text-emerald-200 border border-emerald-500/30 font-bold hover:bg-emerald-900/80'
                        : 'hover:bg-slate-900 text-slate-300 hover:text-white'
                    }`}
                  >
                    <div className="flex items-center gap-2">
                      <Icon className="w-4 h-4 text-[#d4af37]" />
                      <span>{tool.name}</span>
                    </div>
                    {tool.isHot && (
                      <span className="text-[10px] px-2 py-0.5 rounded-full bg-amber-500/20 text-amber-300 border border-amber-500/40 font-bold">
                        v{APP_CONFIG.version}
                      </span>
                    )}
                  </Link>
                );
              })}
            </div>
          </div>
        </div>

        {/* 4. DRAWER FOOTER CTA */}
        <div className="p-4 border-t border-[#d4af37]/20 bg-[#04110d] relative z-10 flex flex-col items-center gap-2 shrink-0">
          <DownloadButton variant="hero" size="md" showDetails />
          <p className="text-[10px] text-slate-400 font-urdu text-center">
            {isEn
              ? `Baytul Ilm AI — All Rights Reserved © ${new Date().getFullYear()}`
              : `بیت العلم AI — تمام حقوق جملہ اشاعت محفوظ ہیں © ${new Date().getFullYear()}`}
          </p>
        </div>
      </div>
    </div>
  );
};

export default SidebarDrawer;
