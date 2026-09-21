import React from 'react';
import { SectionHeader } from '../components/common/SectionHeader';
import { DownloadButton } from '../components/common/DownloadButton';
import { InstallationGuide } from '../components/common/InstallationGuide';
import { BrandLogo } from '../components/common/BrandLogo';
import { APP_CONFIG, APK_DOWNLOAD_URL } from '../config/appConfig';
import { Smartphone, AlertCircle, Copy, Check, Sparkles, ShieldCheck, BookOpen, HeartHandshake, Moon, Repeat, Video, Compass, Library, ExternalLink } from 'lucide-react';
import { IslamicPatternBg } from '../components/layout/IslamicPatternBg';
import { useLanguage } from '../context/LanguageContext';

export const DownloadPage: React.FC = () => {
  const [copiedHash, setCopiedHash] = React.useState(false);
  const { language, isRtl, t } = useLanguage();

  const isUrlConfigured = Boolean(
    APK_DOWNLOAD_URL &&
    APK_DOWNLOAD_URL.trim().length > 0 &&
    APK_DOWNLOAD_URL.startsWith('http')
  );

  const handleCopyHash = () => {
    navigator.clipboard.writeText(APP_CONFIG.sha256Checksum);
    setCopiedHash(true);
    setTimeout(() => setCopiedHash(false), 3000);
  };

  const featurePills = [
    { icon: BookOpen, label: language === 'ur' ? '📖 قرآن مجید' : language === 'ps' ? '📖 قرآن کریم' : '📖 Holy Quran' },
    { icon: HeartHandshake, label: language === 'ur' ? '🤲 مسنون دعائیں' : language === 'ps' ? '🤲 مسنونې دعاګانې' : '🤲 Masnoon Duas' },
    { icon: Moon, label: language === 'ur' ? '🌙 99 اسمائے حسنیٰ' : language === 'ps' ? '🌙 د الله ۹۹ مبارک نومونه' : '🌙 99 Names of Allah' },
    { icon: Sparkles, label: language === 'ur' ? 'ﷺ اسمائے محمد ﷺ' : language === 'ps' ? 'ﷺ د محمد ﷺ نومونه' : 'ﷺ Prophet ﷺ Names' },
    { icon: Video, label: language === 'ur' ? '🕋 حرمین شریفین لائیو' : language === 'ps' ? '🕋 د حرمین شریفین ژوندۍ خپرونې' : '🕋 Haramain Live' },
    { icon: Compass, label: language === 'ur' ? '🧭 قبلہ کمپاس' : language === 'ps' ? '🧭 د قبلې قطب نما' : '🧭 Qibla Compass' },
    { icon: Repeat, label: language === 'ur' ? '📿 تسبیح' : language === 'ps' ? '📿 ډیجیټل تسبېح' : '📿 Digital Tasbeeh' },
    { icon: Library, label: language === 'ur' ? '📚 ڈیجیٹل کتب خانہ' : language === 'ps' ? '📚 ډیجیټل کتابتون' : '📚 Islamic Library' },
  ];

  return (
    <div className="pt-28 pb-20 space-y-16">
      {/* Header Banner */}
      <section className="relative bg-emerald-950 text-white py-16 overflow-hidden">
        <IslamicPatternBg variant="hero" />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-4 text-center">
          <SectionHeader
            badge={
              language === 'ps'
                ? `د رسمي APK ډاونلوډ پاڼه • نسخه ${APP_CONFIG.version}`
                : language === 'ur'
                ? `آفیشل APK پورٹل • ورژن ${APP_CONFIG.version}`
                : `Official APK Portal • v${APP_CONFIG.version}`
            }
            title={t('downloadPortalTitle')}
            arabicTitle="تَحْمِيلُ أحدث APK - بَيْتُ الْعِلْمِ AI"
            subtitle={t('downloadPortalSubtitle')}
          />
        </div>
      </section>

      {/* Main Download Card */}
      <section className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 space-y-12">
        <div className="bg-white dark:bg-slate-900 rounded-3xl p-8 border border-slate-200/80 dark:border-slate-800 shadow-xl space-y-8">
          {/* App Title & Version Header */}
          <div className="flex flex-col sm:flex-row items-center justify-between gap-6 pb-6 border-b border-slate-200 dark:border-slate-800 text-center sm:text-left">
            <div className="flex items-center gap-4">
              <BrandLogo size="lg" withGlow />
              <div>
                <h2 className="text-2xl sm:text-3xl font-extrabold text-slate-900 dark:text-slate-100 font-urdu">
                  {APP_CONFIG.appName} — {language === 'ps' ? `نسخه ${APP_CONFIG.version}` : language === 'ur' ? `ورژن ${APP_CONFIG.version}` : `Version ${APP_CONFIG.version}`}
                </h2>
                <div className="flex flex-wrap items-center gap-2 pt-1">
                  <span className="text-xs font-mono text-emerald-800 dark:text-emerald-400 font-semibold">
                    Package: {APP_CONFIG.packageName}
                  </span>
                  <span className="text-slate-400">•</span>
                  <span className="text-xs font-bold text-amber-700 dark:text-amber-400">
                    {t('approxSize')}
                  </span>
                </div>
              </div>
            </div>

            <div className="flex flex-col items-center sm:items-end gap-1.5">
              <span className="px-4 py-1.5 rounded-full bg-emerald-700 text-white text-xs sm:text-sm font-extrabold shadow-sm flex items-center gap-1.5">
                <Sparkles className="w-3.5 h-3.5 text-amber-300" />
                {language === 'ps' ? `نسخه ${APP_CONFIG.version} (رسمي خپره شوې)` : language === 'ur' ? `ورژن ${APP_CONFIG.version} (آفیشل ریلیز)` : `Version ${APP_CONFIG.version} (Official Release)`}
              </span>
              <span className="text-xs font-bold text-emerald-900 dark:text-emerald-300">{t('approxSize')}</span>
            </div>
          </div>

          {/* Download Action Section */}
          <div className="text-center space-y-5 py-6 bg-gradient-to-b from-emerald-50/50 to-teal-50/20 dark:from-slate-950 dark:to-emerald-950/20 rounded-2xl p-6 sm:p-8 border border-emerald-300/80 dark:border-emerald-800/80 shadow-inner">
            <div className="space-y-1">
              <span className="text-xs font-bold uppercase tracking-wider text-emerald-800 dark:text-emerald-400 font-mono">
                {language === 'ps' ? 'د اصلي APK مستقیم فایل' : language === 'ur' ? 'ڈائریکٹ آفیشل APK فائل' : 'DIRECT APK INSTALLATION FILE'}
              </span>
              <h3 className="text-xl sm:text-2xl font-bold text-slate-900 dark:text-white">
                {t('downloadLatestApk')}
              </h3>
            </div>

            <div className="flex flex-col sm:flex-row items-center justify-center gap-3 pt-2">
              <DownloadButton variant="hero" size="lg" showDetails />
              <a
                href={APP_CONFIG.apkPureUrl}
                target="_blank"
                rel="noopener noreferrer"
                className="inline-flex items-center justify-center px-6 py-3.5 text-sm sm:text-base font-bold rounded-2xl bg-emerald-900/90 hover:bg-emerald-950 text-white shadow-lg border border-emerald-500/40 transition-all transform active:scale-98 gap-2.5"
                id="btn-apkpure-link"
                aria-label="Download from APKPure"
              >
                <ExternalLink className="w-5 h-5 text-amber-300 shrink-0" />
                <div className="flex flex-col text-left">
                  <span className="text-[10px] uppercase font-semibold text-emerald-300 leading-none">
                    {language === 'ur' ? 'آفیشل اسٹور' : language === 'ps' ? 'رسمي پلورنځی' : 'Official Store'}
                  </span>
                  <span className="text-sm font-bold text-white leading-tight">
                    APKPure ({APP_CONFIG.fileSize})
                  </span>
                </div>
              </a>
            </div>

            <p className="text-xs text-slate-600 dark:text-slate-400 font-urdu leading-relaxed max-w-lg mx-auto" dir={isRtl ? 'rtl' : 'ltr'}>
              {t('directDownloadNote')}
            </p>
          </div>

          {/* Included Features Grid in Version */}
          <div className="space-y-3 pt-2" dir={isRtl ? 'rtl' : 'ltr'}>
            <h4 className="text-sm font-bold text-slate-900 dark:text-slate-100 uppercase tracking-wider text-center sm:text-left">
              {language === 'ps'
                ? `په بیت العلم AI نسخه ${APP_CONFIG.version} کې شامل مهم فیچرز`
                : language === 'ur'
                ? `بیت العلم AI ورژن ${APP_CONFIG.version} میں شامل اہم خصوصیات`
                : `Key Features Included in Baytul Ilm AI v${APP_CONFIG.version}`}
            </h4>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-2.5">
              {featurePills.map((f, idx) => {
                const Icon = f.icon;
                return (
                  <div
                    key={idx}
                    className="p-3 rounded-xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 flex items-center gap-2.5 text-xs font-semibold text-slate-800 dark:text-slate-200"
                  >
                    <Icon className="w-4 h-4 text-emerald-600 dark:text-emerald-400 shrink-0" />
                    <span>{f.label}</span>
                  </div>
                );
              })}
            </div>
          </div>

          {/* Verification & Security Spec Grid */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 text-xs pt-4 border-t border-slate-200 dark:border-slate-800" dir={isRtl ? 'rtl' : 'ltr'}>
            <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-950/40 border border-slate-200/80 dark:border-slate-800 space-y-1">
              <span className="font-bold text-slate-500 uppercase block">
                {language === 'ps' ? 'لږ تر لږه انډرایډ اړتیا:' : language === 'ur' ? 'کم از کم اینڈرائیڈ کی ضرورت:' : 'Minimum Requirement:'}
              </span>
              <span className="text-sm font-semibold text-slate-900 dark:text-slate-100">
                {APP_CONFIG.minAndroidVersion}
              </span>
            </div>

            <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-950/40 border border-slate-200/80 dark:border-slate-800 space-y-1">
              <span className="font-bold text-slate-500 uppercase block">
                {language === 'ps' ? 'د امنیت او سکین حالت:' : language === 'ur' ? 'سیکیورٹی اسکین کی کیفیت:' : 'Security Scan Status:'}
              </span>
              <span className="text-sm font-semibold text-amber-700 dark:text-amber-400 flex items-center gap-1">
                <ShieldCheck className="w-4 h-4" />
                {APP_CONFIG.virusTotalStatus}
              </span>
            </div>

            <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-950/40 border border-slate-200/80 dark:border-slate-800 space-y-1 sm:col-span-2">
              <div className="flex items-center justify-between">
                <span className="font-bold text-slate-500 uppercase">
                  {language === 'ps' ? 'رسمي SHA-256 کوډ:' : language === 'ur' ? 'آفیشل ریلیز SHA-256 چیک سم:' : 'Official Release SHA-256:'}
                </span>
                <button
                  onClick={handleCopyHash}
                  className="text-emerald-700 dark:text-emerald-400 hover:underline flex items-center gap-1 cursor-pointer font-medium"
                >
                  {copiedHash ? <Check className="w-3.5 h-3.5" /> : <Copy className="w-3.5 h-3.5" />}
                  {copiedHash
                    ? language === 'ps'
                      ? 'کاپي شو!'
                      : language === 'ur'
                      ? 'کاپی ہو گیا!'
                      : 'Copied'
                    : language === 'ps'
                    ? 'کوډ کاپي کړئ'
                    : language === 'ur'
                    ? 'چیک سم کاپی کریں'
                    : 'Copy Checksum'}
                </button>
              </div>
              <span className="font-mono text-[11px] text-slate-700 dark:text-slate-300 break-all block" dir="ltr">
                {APP_CONFIG.sha256Checksum}
              </span>
            </div>
          </div>
        </div>
      </section>

      {/* Installation Guide Section */}
      <InstallationGuide />
    </div>
  );
};

