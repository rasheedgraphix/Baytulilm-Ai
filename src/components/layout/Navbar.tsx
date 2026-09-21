import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Menu, X, BookOpen, Download, Sparkles, Moon, Layers, Search } from 'lucide-react';
import { APP_CONFIG } from '../../config/appConfig';
import { DownloadButton } from '../common/DownloadButton';
import { BrandLogo } from '../common/BrandLogo';
import { LanguageSwitcher } from '../common/LanguageSwitcher';
import { SidebarDrawer } from './SidebarDrawer';
import { GlobalSearchModal } from '../common/GlobalSearchModal';
import { useLanguage } from '../../context/LanguageContext';

export const Navbar: React.FC = () => {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isSearchOpen, setIsSearchOpen] = useState(false);
  const [isScrolled, setIsScrolled] = useState(false);
  const location = useLocation();
  const { language, t } = useLanguage();

  // Listen for Ctrl+K or Cmd+K
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'k') {
        e.preventDefault();
        setIsSearchOpen(prev => !prev);
      }
    };
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, []);

  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 20);
    };
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  // Close drawer when route changes
  useEffect(() => {
    setIsDrawerOpen(false);
  }, [location.pathname]);

  const navLinks = [
    { name: t('navHome'), path: '/' },
    { name: 'درسِ نظامی', path: '/dars-e-nizami' },
    { name: 'قرآن مجید', path: '/quran' },
    { name: 'تفاسیر', path: '/tafaseer' },
    { name: 'لغات', path: '/lughat', hideOnLg: true },
    { name: 'فتاویٰ', path: '/fatawa', hideOnLg: true },
    { name: language === 'en' ? 'Prayer Times' : 'اوقاتِ نماز', path: '/prayer-times' },
    { name: language === 'en' ? 'Haramain Live' : 'حرمین لائیو', path: '/haramain-live', isLive: true },
    { name: language === 'en' ? 'Library' : language === 'ps' ? 'کتابتون' : 'کتب خانہ', path: '/library' },
  ];

  const isHomeTop = !isScrolled && location.pathname === '/';

  return (
    <>
      <header
        className={`fixed top-0 left-0 right-0 z-50 w-full max-w-full transition-all duration-300 ${
          isScrolled
            ? 'bg-white/95 dark:bg-slate-950/95 backdrop-blur-md shadow-md border-b border-slate-200/80 dark:border-slate-800/80 py-2 sm:py-2.5'
            : isHomeTop
            ? 'bg-gradient-to-b from-[#011410]/95 via-[#011410]/80 to-transparent py-2.5 sm:py-4'
            : 'bg-white/90 dark:bg-slate-950/90 backdrop-blur-md border-b border-slate-200/80 dark:border-slate-800/80 py-2 sm:py-4'
        }`}
      >
        <div className="max-w-7xl mx-auto px-2 sm:px-6 lg:px-8 flex items-center justify-between gap-1 sm:gap-3 w-full">
          {/* Brand Logo & Title + 3-Line Menu Trigger */}
          <div className="flex items-center gap-1.5 sm:gap-3 shrink-0">
            {/* 3-Line Menu (Hamburger) Button for ALL Screens */}
            <button
              onClick={() => setIsDrawerOpen(true)}
              className={`p-1.5 sm:px-3 sm:py-2 rounded-lg sm:rounded-xl transition-all flex items-center gap-1.5 sm:gap-2 border cursor-pointer shrink-0 ${
                isHomeTop
                  ? 'bg-white/10 hover:bg-white/20 text-white border-white/20 hover:border-amber-400'
                  : 'bg-slate-100 hover:bg-slate-200 dark:bg-slate-900 dark:hover:bg-slate-800 text-slate-800 dark:text-slate-100 border-slate-200 dark:border-slate-800'
              }`}
              title={language === 'en' ? 'Open All Menus & Departments' : language === 'ps' ? 'ټول مینو او برخې پرانیزئ' : 'تمام مینو اور شعبہ جات کھولیں'}
              aria-label={language === 'en' ? 'Open Sidebar Menu' : language === 'ps' ? 'د سائیډ مینو پرانیستل' : 'سائیڈ مینو کھولیں'}
            >
              <Menu className="w-4 h-4 sm:w-5 sm:h-5 text-amber-400 shrink-0" />
              <span className="hidden sm:inline text-xs font-bold font-urdu">
                {language === 'en' ? 'Menu' : language === 'ps' ? 'مینو' : 'مینو'}
              </span>
            </button>

            <Link to="/" className="flex items-center gap-1.5 sm:gap-2 group shrink-0 min-w-0">
              <BrandLogo size="nav" className="shrink-0 group-hover:scale-105 transition-transform" />
              <div className="flex flex-col shrink-0">
                <span className={`text-xs sm:text-base lg:text-lg font-black tracking-tight leading-none whitespace-nowrap transition-colors ${
                  isHomeTop
                    ? 'text-white group-hover:text-emerald-300'
                    : 'text-slate-900 dark:text-slate-100 group-hover:text-emerald-700 dark:group-hover:text-emerald-400'
                }`}>
                  {APP_CONFIG.appName}
                </span>
                <span className={`hidden sm:block text-[10px] font-semibold tracking-wider mt-0.5 font-urdu whitespace-nowrap ${
                  isHomeTop ? 'text-emerald-300' : 'text-emerald-700 dark:text-emerald-400'
                }`}>
                  {language === 'ps'
                    ? `نسخه ${APP_CONFIG.version} رسمي خپرونه`
                    : language === 'ur'
                    ? `ورژن ${APP_CONFIG.version} آفیشل ایپ`
                    : `v${APP_CONFIG.version} Official App`}
                </span>
              </div>
            </Link>
          </div>

          {/* Desktop Navigation Links */}
          <nav className="hidden lg:flex items-center gap-1 xl:gap-1.5 shrink min-w-0">
            {navLinks.map((link) => {
              const isActive = location.pathname === link.path;
              return (
                <Link
                  key={link.path}
                  to={link.path}
                  className={`px-2 xl:px-3 py-1.5 text-xs font-bold rounded-lg transition-all flex items-center gap-1.5 whitespace-nowrap shrink-0 ${
                    link.hideOnLg ? 'hidden xl:flex' : ''
                  } ${
                    isActive
                      ? isHomeTop
                        ? 'bg-emerald-500/20 text-emerald-200 border border-emerald-400/40'
                        : 'bg-emerald-100 dark:bg-emerald-950 text-emerald-900 dark:text-emerald-200 border border-emerald-300 dark:border-emerald-800'
                      : isHomeTop
                      ? 'text-slate-200 hover:text-white hover:bg-white/10'
                      : 'text-slate-700 dark:text-slate-300 hover:text-emerald-800 dark:hover:text-emerald-400 hover:bg-slate-100/80 dark:hover:bg-slate-900/60'
                  }`}
                >
                  <span className="whitespace-nowrap">{link.name}</span>
                  {link.isLive && (
                    <span className="w-2 h-2 rounded-full bg-red-500 animate-pulse shadow-[0_0_6px_#ef4444] shrink-0" />
                  )}
                </Link>
              );
            })}
          </nav>

          {/* Right Controls: Global Search + Language Switcher + Download APK Button */}
          <div className="flex items-center gap-1 sm:gap-2 shrink-0">
            {/* Quick Search Button */}
            <button
              onClick={() => setIsSearchOpen(true)}
              className={`p-1.5 sm:px-2.5 sm:py-1.5 rounded-lg sm:rounded-xl transition-all flex items-center gap-1.5 border cursor-pointer ${
                isHomeTop
                  ? 'bg-white/10 hover:bg-white/20 text-white border-white/20 hover:border-amber-400'
                  : 'bg-slate-100 hover:bg-slate-200 dark:bg-slate-900 dark:hover:bg-slate-800 text-slate-800 dark:text-slate-100 border-slate-200 dark:border-slate-800'
              }`}
              title={language === 'en' ? 'Search books, app, live (Ctrl+K)' : 'کتاب، ایپ یا حرمین لائیو تلاش کریں (Ctrl+K)'}
              aria-label="Search"
            >
              <Search className="w-4 h-4 text-amber-400 shrink-0" />
              <span className="hidden md:inline text-xs font-semibold font-urdu">
                {language === 'en' ? 'Search' : 'تلاش'}
              </span>
              <span className="hidden xl:inline-block text-[9px] font-mono px-1 py-0.2 rounded bg-black/20 text-slate-300">
                ⌘K
              </span>
            </button>

            <LanguageSwitcher />
            <DownloadButton variant={isHomeTop ? "hero" : "primary"} size="sm" compactOnMobile />
          </div>
        </div>
      </header>

      {/* Global Quick Search Modal (Instant Ranking #1, #2, #3) */}
      <GlobalSearchModal
        isOpen={isSearchOpen}
        onClose={() => setIsSearchOpen(false)}
      />

      {/* Slide-out Sidebar Drawer with 3-line menu */}
      <SidebarDrawer
        isOpen={isDrawerOpen}
        onClose={() => setIsDrawerOpen(false)}
      />
    </>
  );
};

