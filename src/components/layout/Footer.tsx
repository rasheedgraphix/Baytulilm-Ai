import React from 'react';
import { Link } from 'react-router-dom';
import { BookOpen, ShieldCheck, Github, ExternalLink } from 'lucide-react';
import { APP_CONFIG } from '../../config/appConfig';
import { DownloadButton } from '../common/DownloadButton';
import { BrandLogo } from '../common/BrandLogo';
import { useLanguage } from '../../context/LanguageContext';

export const Footer: React.FC = () => {
  const { language, isRtl, t } = useLanguage();

  const isGithubConfigured = Boolean(
    APP_CONFIG.githubRepoUrl &&
    APP_CONFIG.githubRepoUrl.trim().length > 0 &&
    APP_CONFIG.githubRepoUrl.startsWith('http')
  );

  return (
    <footer className="bg-slate-950 text-slate-300 border-t border-slate-800/80 pt-16 pb-12 relative overflow-hidden" dir={isRtl ? 'rtl' : 'ltr'}>
      {/* Background Islamic Pattern Accent */}
      <div className="absolute inset-0 opacity-5 bg-[radial-gradient(#10b981_1px,transparent_1px)] [background-size:24px_24px] pointer-events-none"></div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-12">
        {/* Top Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-8 lg:gap-12">
          {/* Brand Info */}
          <div className="lg:col-span-2 space-y-4">
            <div className="flex items-center gap-3">
              <BrandLogo size="md" />
              <div className="flex flex-col">
                <span className="text-xl font-bold text-white tracking-tight">
                  {APP_CONFIG.appName}
                </span>
                <span className="text-xs text-emerald-400 font-mono">
                  Package: {APP_CONFIG.packageName}
                </span>
              </div>
            </div>

            <p className="text-sm text-slate-400 leading-relaxed max-w-sm font-urdu">
              {language === 'ur'
                ? 'بیت العلم AI ایک جامع اینڈرائیڈ تعلیمی ایپ ہے جو طلبہ اور اساتذہ کو قرآن مجید، مسنون دعائیں، درسِ نظامی نصاب، دینی کوئز اور AI اسلامی معاون فراہم کرتی ہے۔'
                : language === 'ps'
                ? 'بیت العلم AI یو رسمي انډرایډ تعلیمي اپلیکیشن دی چې پکې قرآن کریم، مسنونې دعاګانې، د درسِ نظامي کتابونه او AI مرستیال شامل دي.'
                : 'An official Android Islamic educational application providing students with Dars-e-Nizami resources, Quranic tools, chapter quizzes, and an AI learning assistant.'}
            </p>

            <div className="pt-2">
              <DownloadButton variant="hero" size="sm" showDetails />
            </div>
          </div>

          {/* Quick Links */}
          <div className="space-y-3">
            <h4 className="text-sm font-bold text-white uppercase tracking-wider text-emerald-400 font-urdu">
              {language === 'ur' ? 'شعبہ جات و کتب' : language === 'ps' ? 'څانګې او کتابونه' : 'Academic Sections'}
            </h4>
            <ul className="space-y-2 text-xs font-medium text-slate-400 font-urdu">
              <li>
                <Link to="/dars-e-nizami" className="hover:text-amber-300 transition-colors">درسِ نظامی (۸ سالہ نصاب)</Link>
              </li>
              <li>
                <Link to="/quran" className="hover:text-amber-300 transition-colors">قرآن مجید (۱۶ و ۱۵ سطری)</Link>
              </li>
              <li>
                <Link to="/tafaseer" className="hover:text-amber-300 transition-colors">تفاسیرِ قرآن (ابن کثیر، معارف)</Link>
              </li>
              <li>
                <Link to="/lughat" className="hover:text-amber-300 transition-colors">معاجم و لغات (عربی و اردو)</Link>
              </li>
              <li>
                <Link to="/fatawa" className="hover:text-amber-300 transition-colors">فتاویٰ و فقہی ذخیرہ</Link>
              </li>
              <li>
                <Link to="/tareekh" className="hover:text-amber-300 transition-colors text-amber-300 font-bold">تاریخ و سیرت (طبری، ابن خلدون)</Link>
              </li>
              <li>
                <Link to="/tajweed-lil-huffaz" className="hover:text-teal-300 transition-colors text-teal-300 font-bold">تجوید للحفاظ (جمال القرآن، فوائد مکیہ)</Link>
              </li>
              <li>
                <Link to="/tajweed-lil-ulama" className="hover:text-sky-300 transition-colors text-sky-300 font-bold">تجوید للعلماء (المقدمۃ الجزریۃ، علوم القرآن)</Link>
              </li>
              <li>
                <Link to="/prayer-times" className="hover:text-emerald-300 transition-colors text-emerald-300 font-bold">اوقاتِ نماز (جامعہ علوم اسلامیہ کراچی)</Link>
              </li>
              <li>
                <Link to="/duas" className="hover:text-amber-300 transition-colors text-amber-300 font-bold">۱۰۰ مسنون و قرآنی دعائیں (مکمل متن)</Link>
              </li>
              <li>
                <Link to="/haramain-live" className="hover:text-red-400 text-red-300 transition-colors flex items-center gap-1.5">
                  <span className="w-2 h-2 rounded-full bg-red-500 animate-pulse" />
                  <span>حرمین شریفین ۲۴/۷ لائیو</span>
                </Link>
              </li>
            </ul>
          </div>

          {/* Navigation Links */}
          <div className="space-y-3">
            <h4 className="text-sm font-bold text-white uppercase tracking-wider text-emerald-400 font-urdu">
              {t('footerNavigation')}
            </h4>
            <ul className="space-y-2 text-xs font-medium text-slate-400 font-urdu">
              <li>
                <Link to="/" className="hover:text-amber-300 transition-colors">{t('navHome')}</Link>
              </li>
              <li>
                <Link to="/library" className="hover:text-amber-300 transition-colors text-emerald-400 font-bold">{t('navLibrary')}</Link>
              </li>
              <li>
                <Link to="/download" className="hover:text-amber-300 transition-colors">{t('navDownload')}</Link>
              </li>
              <li>
                <Link to="/features" className="hover:text-amber-300 transition-colors">{t('navFeatures')}</Link>
              </li>
              <li>
                <Link to="/screenshots" className="hover:text-amber-300 transition-colors">{t('navScreenshots')}</Link>
              </li>
            </ul>
          </div>

          {/* Support & Legal */}
          <div className="space-y-3">
            <h4 className="text-sm font-bold text-white uppercase tracking-wider text-emerald-400 font-urdu">
              {t('footerLegal')}
            </h4>
            <ul className="space-y-2 text-xs font-medium text-slate-400 font-urdu">
              <li>
                <Link to="/privacy-policy" className="hover:text-amber-300 transition-colors">{t('navPrivacy')}</Link>
              </li>
              <li>
                <Link to="/contact" className="hover:text-amber-300 transition-colors">{t('navContact')}</Link>
              </li>
              <li>
                <a
                  href="/#installation-guide"
                  onClick={(e) => {
                    const target = document.getElementById('installation-guide');
                    if (target) {
                      e.preventDefault();
                      target.scrollIntoView({ behavior: 'smooth' });
                    }
                  }}
                  className="hover:text-amber-300 transition-colors cursor-pointer"
                >
                  {language === 'ur' ? 'انسٹالیشن گائیڈ' : language === 'ps' ? 'د انسټالولو لارښود' : 'Installation Guide'}
                </a>
              </li>
            </ul>
          </div>

          {/* Security & Builds */}
          <div className="space-y-3">
            <h4 className="text-sm font-bold text-white uppercase tracking-wider text-emerald-400 font-urdu">
              {t('footerSecurity')}
            </h4>
            <div className="p-3 rounded-xl bg-slate-900 border border-slate-800 space-y-2 text-xs">
              <div className="flex items-center gap-1.5 text-amber-400 font-semibold">
                <ShieldCheck className="w-4 h-4" />
                {APP_CONFIG.virusTotalStatus}
              </div>
              <p className="text-[11px] text-slate-400 leading-tight font-urdu">
                {language === 'ur'
                  ? 'بیت العلم AI آفیشل ریلیز پیکج ہے۔ ڈاؤن لوڈ کے بعد براہ کرم SHA-256 چیک سم کی تصدیق کر سکتے ہیں۔'
                  : language === 'ps'
                  ? 'بیت العلم AI یو رسمي باوري اپلیکیشن دی. له ډاونلوډ وروسته کولای شئ د SHA-256 کوډ پخلی کړئ.'
                  : 'APK is provided as an official release build. Users should verify the SHA-256 checksum after downloading.'}
              </p>
              {isGithubConfigured ? (
                <a
                  href={APP_CONFIG.githubRepoUrl}
                  target="_blank"
                  rel="noreferrer"
                  className="inline-flex items-center gap-1 text-[11px] text-amber-400 hover:underline pt-1"
                >
                  <Github className="w-3.5 h-3.5" /> {language === 'ps' ? 'د ګیټ هب سرچینه' : language === 'ur' ? 'گٹ ہب ریپازٹری' : 'GitHub Repository'} <ExternalLink className="w-2.5 h-2.5" />
                </a>
              ) : (
                <span className="inline-flex items-center gap-1 text-[11px] text-slate-500 pt-1">
                  <Github className="w-3.5 h-3.5" /> {language === 'ps' ? 'ګیټ هب لینک' : language === 'ur' ? 'گٹ ہب لنک' : 'Repository Link'}
                </span>
              )}
            </div>
          </div>
        </div>

        {/* Disclaimer regarding copyrighted PDFs */}
        <div className="p-4 rounded-xl bg-slate-900/80 border border-slate-800 text-xs text-slate-400 leading-relaxed flex items-start gap-3">
          <BookOpen className="w-5 h-5 text-amber-400 shrink-0 mt-0.5" />
          <div className="font-urdu">
            <span className="font-bold text-slate-200 block mb-0.5">
              {language === 'ur'
                ? 'دینی و تعلیمی وضاحتی بیان:'
                : language === 'ps'
                ? 'دیني او علمي وضاحت:'
                : 'Educational & Copyright Notice:'}
            </span>
            {t('footerDisclaimer')}
          </div>
        </div>

        {/* Bottom Bar */}
        <div className="pt-8 border-t border-slate-800/80 flex flex-col sm:flex-row items-center justify-between gap-4 text-xs text-slate-500">
          <div className="font-urdu">
            © {new Date().getFullYear()} {APP_CONFIG.appName} ({APP_CONFIG.packageName}).{' '}
            {language === 'ur'
              ? 'جملہ حقوق محفوظ ہیں۔'
              : language === 'ps'
              ? 'ټول حقوق خوندي دي.'
              : 'All rights reserved.'}
          </div>
          <div className="flex items-center gap-4 text-slate-400">
            <Link to="/privacy-policy" className="hover:text-amber-300 transition-colors">{t('navPrivacy')}</Link>
            <span>•</span>
            <Link to="/contact" className="hover:text-amber-300 transition-colors">{t('navContact')}</Link>
            <span>•</span>
            {isGithubConfigured ? (
              <a href={APP_CONFIG.githubRepoUrl} target="_blank" rel="noreferrer" className="hover:text-amber-300 transition-colors">GitHub</a>
            ) : (
              <span className="text-slate-500">GitHub</span>
            )}
          </div>
        </div>
      </div>
    </footer>
  );
};

