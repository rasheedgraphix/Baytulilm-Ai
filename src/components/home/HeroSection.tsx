import React from 'react';
import { Link } from 'react-router-dom';
import { Download, Sparkles, ShieldCheck, Smartphone, BookOpen, CheckCircle, Moon, HeartHandshake, Compass, Repeat, Video } from 'lucide-react';
import { APP_CONFIG } from '../../config/appConfig';
import { DownloadButton } from '../common/DownloadButton';
import { PhoneMockupFrame } from '../common/PhoneMockupFrame';
import { IslamicPatternBg } from '../layout/IslamicPatternBg';
import { useLanguage } from '../../context/LanguageContext';

export const HeroSection: React.FC = () => {
  const { language, isRtl, t } = useLanguage();

  return (
    <section className="relative pt-28 pb-16 md:pt-36 md:pb-24 overflow-hidden bg-gradient-to-b from-[#021d18] via-[#04261f] to-[#011410] text-white">
      {/* Islamic Geometric Lattice Pattern */}
      <IslamicPatternBg variant="hero" />

      {/* Radiant Islamic Dome / Arch Glow Spotlights */}
      <div className="absolute top-10 left-1/2 -translate-x-1/2 w-full max-w-[700px] h-[450px] bg-emerald-500/10 blur-[130px] rounded-full pointer-events-none"></div>
      <div className="absolute top-1/3 right-0 sm:right-10 w-full max-w-[350px] h-[350px] bg-amber-500/10 blur-[100px] rounded-full pointer-events-none"></div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-12 lg:gap-8 items-center">
          {/* Left Text Content */}
          <div className="lg:col-span-7 space-y-6 text-center lg:text-left">
            
            {/* Calligraphic Bismillah Header */}
            <div className="flex items-center justify-center lg:justify-start gap-2">
              <span className="font-arabic font-serif text-lg sm:text-2xl text-amber-300/90 tracking-widest drop-shadow">
                بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ
              </span>
            </div>

            {/* Version 1.4.3 Prominent Top Banner Badge */}
            <div className="inline-flex flex-wrap items-center justify-center lg:justify-start gap-2 p-1.5 pr-4 rounded-full text-xs font-semibold bg-emerald-950/90 text-emerald-100 border border-emerald-500/40 shadow-lg shadow-emerald-950/50 backdrop-blur-md">
              <span className="px-3 py-1 rounded-full bg-gradient-to-r from-amber-400 to-amber-500 text-slate-950 font-extrabold text-[11px] uppercase tracking-wider flex items-center gap-1.5 shadow-sm">
                <Sparkles className="w-3.5 h-3.5 text-slate-950 fill-slate-950" />
                {t('latestVersionBadge')}
              </span>
              <span className="text-amber-300 font-serif font-arabic text-sm px-1">بَيْتُ الْعِلْمِ AI</span>
              <span className="hidden sm:inline text-emerald-400 font-mono text-[11px]">com.baytulilmai.app</span>
            </div>

            {/* Main Headline */}
            <h1 className="text-3xl sm:text-4xl md:text-5xl xl:text-6xl font-extrabold tracking-tight leading-tight text-white">
              {language === 'ur' ? (
                <>
                  <span className="font-urdu block text-3xl sm:text-5xl lg:text-6xl text-white drop-shadow-md">
                    {t('appName')}
                  </span>
                  <span className="block font-urdu text-2xl sm:text-4xl lg:text-5xl text-transparent bg-clip-text bg-gradient-to-r from-amber-200 via-emerald-200 to-teal-200 pt-2">
                    جامع اسلامی تعلیمی و دینی پلیٹ فارم
                  </span>
                </>
              ) : language === 'ps' ? (
                <>
                  <span className="font-pashto block text-3xl sm:text-5xl lg:text-6xl text-white">
                    {t('appName')}
                  </span>
                  <span className="block font-pashto text-2xl sm:text-4xl lg:text-5xl text-transparent bg-clip-text bg-gradient-to-r from-amber-200 via-emerald-200 to-teal-200 pt-2">
                    جامع اسلامي او تعلیمي پلیټفارم
                  </span>
                </>
              ) : (
                <>
                  <span className="font-heading-en block text-3xl sm:text-5xl lg:text-6xl text-white">
                    {t('appName')}
                  </span>
                  <span className="block font-heading-en text-xl sm:text-3xl lg:text-4xl text-transparent bg-clip-text bg-gradient-to-r from-amber-200 via-emerald-200 to-teal-200 pt-2 font-normal">
                    Islamic Education & Dars-e-Nizami Platform
                  </span>
                </>
              )}
            </h1>

            {/* Subtitle */}
            <p className="text-base sm:text-lg text-slate-200/90 leading-relaxed font-normal max-w-2xl mx-auto lg:mx-0 font-urdu" dir={isRtl ? 'rtl' : 'ltr'}>
              {t('heroSubtitle')}
            </p>

            {/* Islamic 8 Features Quick Highlights */}
            <div className="grid grid-cols-2 sm:grid-cols-4 gap-2.5 pt-2">
              <div className="p-3 rounded-2xl bg-emerald-950/60 border border-emerald-500/30 hover:border-amber-400/50 transition-all text-center space-y-1.5 shadow-sm">
                <BookOpen className="w-5 h-5 text-amber-400 mx-auto" />
                <span className="text-xs font-bold text-slate-100 block font-urdu">{t('featureQuranTitle')}</span>
              </div>
              <div className="p-3 rounded-2xl bg-emerald-950/60 border border-emerald-500/30 hover:border-amber-400/50 transition-all text-center space-y-1.5 shadow-sm">
                <HeartHandshake className="w-5 h-5 text-emerald-400 mx-auto" />
                <span className="text-xs font-bold text-slate-100 block font-urdu">{t('featureDuasTitle')}</span>
              </div>
              <div className="p-3 rounded-2xl bg-emerald-950/60 border border-emerald-500/30 hover:border-amber-400/50 transition-all text-center space-y-1.5 shadow-sm">
                <Moon className="w-5 h-5 text-amber-400 mx-auto" />
                <span className="text-xs font-bold text-slate-100 block font-urdu">{t('featureAsmaulHusnaTitle')}</span>
              </div>
              <div className="p-3 rounded-2xl bg-emerald-950/60 border border-emerald-500/30 hover:border-amber-400/50 transition-all text-center space-y-1.5 shadow-sm">
                <Repeat className="w-5 h-5 text-emerald-400 mx-auto" />
                <span className="text-xs font-bold text-slate-100 block font-urdu">{t('featureTasbeehTitle')}</span>
              </div>
            </div>

            {/* Action Buttons: Prominent [ Download Latest APK ] */}
            <div className="pt-4 flex flex-col sm:flex-row items-center justify-center lg:justify-start gap-4">
              <DownloadButton variant="hero" size="lg" showDetails />

              <a
                href="#islamic-tools"
                onClick={(e) => {
                  e.preventDefault();
                  const target = document.getElementById('islamic-tools');
                  if (target) {
                    target.scrollIntoView({ behavior: 'smooth' });
                  }
                }}
                className="px-6 py-4 text-base font-semibold rounded-2xl bg-emerald-950/80 hover:bg-emerald-900/80 text-amber-300 border border-amber-500/40 hover:border-amber-400 transition-all flex items-center justify-center gap-2 w-full sm:w-auto cursor-pointer shadow-lg shadow-black/40"
              >
                <Sparkles className="w-5 h-5 text-amber-400" />
                <span className="font-medium">{t('tryIslamicTools')}</span>
              </a>
            </div>

            {/* Metadata Footer Badge */}
            <div className="pt-2 text-xs text-slate-400 flex items-center justify-center lg:justify-start gap-4 font-mono">
              <span className="flex items-center gap-1.5 text-emerald-400 font-semibold">
                <ShieldCheck className="w-4 h-4 text-emerald-400" /> {t('latestVersionBadge')}
              </span>
              <span>•</span>
              <span className="text-slate-300">Android 7.0+ (Nougat)</span>
              <span>•</span>
              <span className="text-amber-400 font-semibold">{t('approxSize')}</span>
            </div>
          </div>

          {/* Right Smartphone Preview */}
          <div className="lg:col-span-5 flex justify-center">
            <div className="relative">
              {/* Decorative Islamic Arch background glow */}
              <div className="absolute inset-0 bg-gradient-to-t from-emerald-500/25 to-amber-500/20 blur-3xl rounded-full"></div>
              
              <PhoneMockupFrame initialTab="home" />
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

