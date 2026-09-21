import React, { useState } from 'react';
import { Download, ShieldCheck, AlertCircle, Sparkles } from 'lucide-react';
import { APK_DOWNLOAD_URL, APP_CONFIG } from '../../config/appConfig';
import { useLanguage } from '../../context/LanguageContext';

interface DownloadButtonProps {
  variant?: 'primary' | 'secondary' | 'outline' | 'hero';
  size?: 'sm' | 'md' | 'lg';
  showDetails?: boolean;
  className?: string;
  customLabel?: string;
  compactOnMobile?: boolean;
  onClickCustom?: () => void;
}

export const DownloadButton: React.FC<DownloadButtonProps> = ({
  variant = 'primary',
  size = 'md',
  showDetails = false,
  className = '',
  customLabel,
  compactOnMobile = false,
  onClickCustom
}) => {
  const [downloadNotice, setDownloadNotice] = useState<string | null>(null);
  const { language, t } = useLanguage();

  const isUrlConfigured = Boolean(
    APK_DOWNLOAD_URL &&
    APK_DOWNLOAD_URL.trim().length > 0 &&
    APK_DOWNLOAD_URL.startsWith('http')
  );

  const handleDownload = (e: React.MouseEvent) => {
    if (onClickCustom) {
      onClickCustom();
      return;
    }

    if (!isUrlConfigured) {
      e.preventDefault();
      setDownloadNotice(
        "APK download link will be available soon."
      );
      setTimeout(() => setDownloadNotice(null), 6000);
      return;
    }
  };

  const isCompactMode = compactOnMobile || size === 'sm';

  const sizeClasses = {
    sm: isCompactMode
      ? 'px-2 py-1 sm:px-3.5 sm:py-1.5 text-[11px] sm:text-xs font-semibold rounded-lg sm:rounded-xl gap-1 sm:gap-1.5'
      : 'px-3 sm:px-4 py-2 text-xs font-semibold rounded-lg gap-1.5',
    md: 'px-4 sm:px-6 py-2.5 sm:py-3 text-xs sm:text-sm font-semibold rounded-xl gap-2 sm:gap-2.5',
    lg: 'px-5 sm:px-8 py-3 sm:py-4 text-sm sm:text-base font-bold rounded-2xl gap-2 sm:gap-3 max-w-full text-center'
  };

  const variantClasses = {
    primary:
      'bg-emerald-700 hover:bg-emerald-800 text-white shadow-md shadow-emerald-900/10 hover:shadow-lg transition-all transform active:scale-98 border border-emerald-600/30',
    hero:
      'bg-gradient-to-r from-emerald-700 via-emerald-800 to-teal-900 hover:from-emerald-800 hover:to-teal-950 text-white shadow-xl shadow-emerald-900/25 hover:shadow-2xl transition-all transform active:scale-98 border border-emerald-400/40 sm:ring-4 sm:ring-emerald-500/20',
    secondary:
      'bg-amber-600 hover:bg-amber-700 text-white shadow-md hover:shadow-lg transition-all active:scale-98 border border-amber-500/30',
    outline:
      'bg-emerald-50/80 dark:bg-emerald-950/40 hover:bg-emerald-100 dark:hover:bg-emerald-900/40 text-emerald-900 dark:text-emerald-100 border border-emerald-300 dark:border-emerald-700 transition-all active:scale-98'
  };

  const getButtonText = () => {
    if (customLabel) return customLabel;
    if (size === 'sm') {
      if (language === 'ps') return 'APK ډاونلوډ';
      if (language === 'ur') return 'APK ڈاؤنلوڈ';
      return 'Download APK';
    }
    if (language === 'ps') return 'تازه ترین APK ډاونلوډ کړئ';
    if (language === 'ur') return 'تازہ ترین APK ڈاؤن لوڈ کریں';
    return 'Download Latest APK';
  };

  const getMobileCompactText = () => {
    if (customLabel) return customLabel;
    return 'APK';
  };

  return (
    <div className="inline-flex flex-col items-center gap-2">
      <a
        href={isUrlConfigured ? APK_DOWNLOAD_URL : "#"}
        onClick={handleDownload}
        download={isUrlConfigured ? "Baytul.Ilm.AI.2.apk" : undefined}
        target={isUrlConfigured ? "_blank" : undefined}
        rel={isUrlConfigured ? "noopener noreferrer" : undefined}
        className={`inline-flex items-center justify-center cursor-pointer select-none whitespace-nowrap ${sizeClasses[size]} ${variantClasses[variant]} ${className}`}
        id="btn-download-apk"
        aria-label={`Download Baytul Ilm AI APK v${APP_CONFIG.version}`}
      >
        <Download className={`${size === 'lg' ? 'w-6 h-6' : size === 'sm' ? 'w-3.5 h-3.5 sm:w-4 sm:h-4' : 'w-5 h-5'} animate-bounce-subtle shrink-0`} />
        {isCompactMode ? (
          <>
            <span className="sm:hidden font-bold tracking-tight text-xs font-urdu">{getMobileCompactText()}</span>
            <span className="hidden sm:inline font-bold tracking-tight">{getButtonText()}</span>
            <span className="hidden sm:inline-block ml-1 text-xs px-2 py-0.5 rounded-full bg-white/20 font-mono font-medium whitespace-nowrap">
              v{APP_CONFIG.version}
            </span>
          </>
        ) : (
          <>
            <span className="font-bold tracking-tight">{getButtonText()}</span>
            <span className="ml-1 text-xs px-2 py-0.5 rounded-full bg-white/20 font-mono font-medium whitespace-nowrap">
              v{APP_CONFIG.version}
            </span>
          </>
        )}
      </a>

      {showDetails && (
        <div className="flex flex-wrap items-center justify-center gap-2 sm:gap-3 text-xs text-slate-600 dark:text-slate-400 font-medium">
          <span className="flex items-center gap-1 text-amber-700 dark:text-amber-400 font-semibold">
            <ShieldCheck className="w-3.5 h-3.5" />
            {APP_CONFIG.virusTotalStatus}
          </span>
          <span>•</span>
          <span>{APP_CONFIG.fileSize}</span>
          <span>•</span>
          <span>Android 7.0+</span>
        </div>
      )}

      {downloadNotice && (
        <div className="mt-2 p-3 text-xs bg-amber-50 dark:bg-amber-950/60 border border-amber-300 dark:border-amber-800 text-amber-900 dark:text-amber-200 rounded-lg max-w-md text-center shadow-md animate-fade-in flex items-start gap-2">
          <AlertCircle className="w-4 h-4 text-amber-600 shrink-0 mt-0.5" />
          <span>{downloadNotice}</span>
        </div>
      )}
    </div>
  );
};

