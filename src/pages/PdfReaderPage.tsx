import React, { useState, useEffect } from 'react';
import { useSearchParams, Link, useNavigate } from 'react-router-dom';
import {
  ArrowRight,
  ArrowLeft,
  Download,
  ExternalLink,
  BookOpen,
  Share2,
  Copy,
  Check,
  Maximize2,
  Minimize2,
  AlertCircle,
  Sparkles,
  ShieldCheck,
  RotateCw
} from 'lucide-react';
import { useLanguage } from '../context/LanguageContext';
import { downloadDirectPdf } from '../utils/pdfDownloader';
import { BrandLogo } from '../components/common/BrandLogo';
import { updatePageSEO } from '../utils/seo';
import { AdsterraBanner } from '../components/ads/AdsterraBanner';

export const PdfReaderPage: React.FC = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { isRtl } = useLanguage();

  const pdfUrl = searchParams.get('url') || '';
  const title = searchParams.get('title') || 'اسلامی درسی کتاب';
  const romanName = searchParams.get('name') || '';
  const category = searchParams.get('category') || 'اسلامی کتب';
  const classNameUrdu = searchParams.get('class') || '';
  const author = searchParams.get('author') || '';
  const edition = searchParams.get('edition') || '';

  const [isFullscreen, setIsFullscreen] = useState(false);
  const [copied, setCopied] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [loadError, setLoadError] = useState(false);

  useEffect(() => {
    updatePageSEO({
      title: `${title} (${romanName || classNameUrdu || 'پی ڈی ایف'}) – آن لائن مطالعہ و ڈاؤن لوڈ`,
      description: `${title} ${author ? `از ${author}` : ''} ${classNameUrdu ? `(درجہ: ${classNameUrdu})` : ''} کا آن لائن مطالعہ کریں اور پی ڈی ایف مفت ڈاؤن لوڈ کریں۔`,
      keywords: [
        title,
        romanName,
        `${title} pdf`,
        `${title} آن لائن مطالعہ`,
        `${title} ڈاؤن لوڈ`,
        category,
        classNameUrdu,
        author
      ].filter(Boolean) as string[],
      bookData: {
        name: title,
        author: author || undefined,
        category: category || classNameUrdu || 'Islamic Education',
        language: 'Urdu / Arabic',
        description: `${title} - Full book PDF read and download on Baytul Ilm AI.`,
        fileUrl: pdfUrl
      }
    });
    window.scrollTo(0, 0);

    const timer = setTimeout(() => {
      setIsLoading(false);
    }, 1500);

    return () => clearTimeout(timer);
  }, [title, romanName, category, classNameUrdu, author, pdfUrl]);

  const handleCopyLink = async () => {
    try {
      await navigator.clipboard.writeText(window.location.href);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    } catch {
      // fallback
    }
  };

  const handleShare = async () => {
    if (navigator.share) {
      try {
        await navigator.share({
          title: `${title} | بیت العلم AI`,
          text: `آن لائن مطالعہ و ڈاؤن لوڈ: ${title} (${category})`,
          url: window.location.href,
        });
      } catch {
        // User cancelled
      }
    } else {
      handleCopyLink();
    }
  };

  const toggleFullscreen = () => {
    if (!document.fullscreenElement) {
      document.documentElement.requestFullscreen?.().catch(console.warn);
      setIsFullscreen(true);
    } else {
      document.exitFullscreen?.().catch(console.warn);
      setIsFullscreen(false);
    }
  };

  if (!pdfUrl) {
    return (
      <div className="min-h-screen bg-slate-950 text-white flex flex-col items-center justify-center p-6 text-center space-y-6 pt-24" dir={isRtl ? 'rtl' : 'ltr'}>
        <div className="w-16 h-16 rounded-3xl bg-rose-500/20 border border-rose-500/40 flex items-center justify-center text-rose-400">
          <AlertCircle className="w-8 h-8" />
        </div>
        <div className="space-y-2 max-w-md">
          <h2 className="text-2xl font-black font-urdu">پی ڈی ایف لنک دستیاب نہیں ہے</h2>
          <p className="text-sm text-slate-400 font-urdu">
            برائے مہربانی کتب خانے یا درسِ نظامی کے شعبے میں جا کر کسی کتاب کا انتخاب کریں۔
          </p>
        </div>
        <Link
          to="/library"
          className="inline-flex items-center gap-2 px-6 py-3 rounded-2xl bg-emerald-600 hover:bg-emerald-500 text-white font-bold font-urdu transition-all shadow-lg cursor-pointer"
        >
          {isRtl ? <ArrowRight className="w-4 h-4" /> : <ArrowLeft className="w-4 h-4" />}
          <span>مرکزی کتب خانے پر جائیں</span>
        </Link>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-[#020b08] text-slate-100 flex flex-col select-none" dir={isRtl ? 'rtl' : 'ltr'}>
      {/* 1. TOP CONTROL & NAVIGATION BAR */}
      <header className="sticky top-0 z-50 bg-[#061510]/95 backdrop-blur-md border-b border-[#d4af37]/30 px-3 sm:px-6 py-3 shadow-xl">
        <div className="max-w-7xl mx-auto flex flex-col md:flex-row items-stretch md:items-center justify-between gap-3">
          {/* Left / Start: Back & Book Details */}
          <div className="flex items-center gap-3 min-w-0">
            <button
              onClick={() => navigate(-1)}
              className="p-2 rounded-xl bg-slate-900/90 hover:bg-[#d4af37] text-slate-300 hover:text-slate-950 border border-white/10 hover:border-[#d4af37] transition-colors shrink-0 cursor-pointer"
              title="واپس جائیں"
            >
              {isRtl ? <ArrowRight className="w-5 h-5" /> : <ArrowLeft className="w-5 h-5" />}
            </button>

            <BrandLogo size="sm" className="hidden sm:inline-flex" />

            <div className="min-w-0 flex-1">
              <div className="flex items-center gap-1.5 flex-wrap">
                {classNameUrdu && (
                  <span className="px-2 py-0.5 rounded-md text-[10px] font-bold bg-emerald-500/20 text-emerald-300 border border-emerald-500/40">
                    {classNameUrdu}
                  </span>
                )}
                <span className="px-2 py-0.5 rounded-md text-[10px] font-bold bg-[#d4af37]/20 text-[#fae19c] border border-[#d4af37]/40">
                  {category}
                </span>
                {author && (
                  <span className="hidden lg:inline text-[11px] text-slate-400 font-urdu truncate">
                    مصنف: {author}
                  </span>
                )}
              </div>

              <h1 className="text-sm sm:text-base md:text-lg font-black text-white font-urdu truncate mt-0.5">
                {title}
              </h1>
            </div>
          </div>

          {/* Right / End: Direct Action Buttons */}
          <div className="flex items-center gap-2 shrink-0 self-end md:self-auto flex-wrap">
            {/* Direct Instant Download */}
            <button
              onClick={() => downloadDirectPdf(pdfUrl, title)}
              className="inline-flex items-center gap-1.5 px-3.5 sm:px-4 py-2 rounded-xl bg-gradient-to-r from-amber-500 to-amber-600 hover:from-amber-400 hover:to-amber-500 text-slate-950 font-bold text-xs sm:text-sm font-urdu transition-all shadow-md shadow-amber-500/20 cursor-pointer"
              title="فائل فوری طور پر اپنے آلے میں ڈاؤن لوڈ کریں"
            >
              <Download className="w-4 h-4 text-slate-950" />
              <span>ڈاؤن لوڈ کریں</span>
            </button>

            {/* Direct Raw PDF in Full Browser Window */}
            <a
              href={pdfUrl}
              target="_blank"
              rel="noopener noreferrer"
              className="inline-flex items-center gap-1.5 px-3 py-2 rounded-xl bg-slate-900/90 hover:bg-slate-800 border border-[#d4af37]/40 text-[#fae19c] font-urdu text-xs font-bold transition-all cursor-pointer"
              title="پی ڈی ایف فائل کو بغیر فریم کے نئی ونڈو میں براہِ راست کھولیں"
            >
              <ExternalLink className="w-3.5 h-3.5 text-[#d4af37]" />
              <span className="hidden sm:inline">نئی ونڈو میں پی ڈی ایف</span>
              <span className="sm:hidden">نئی ونڈو</span>
            </a>

            {/* Fullscreen button */}
            <button
              onClick={toggleFullscreen}
              className="p-2 rounded-xl bg-slate-900/90 hover:bg-slate-800 border border-white/10 text-slate-300 hover:text-white transition-colors cursor-pointer"
              title={isFullscreen ? 'عام موڈ' : 'فل اسکرین موڈ'}
            >
              {isFullscreen ? <Minimize2 className="w-4 h-4" /> : <Maximize2 className="w-4 h-4" />}
            </button>

            {/* Share / Copy */}
            <button
              onClick={handleShare}
              className="p-2 rounded-xl bg-slate-900/90 hover:bg-slate-800 border border-white/10 text-slate-300 hover:text-white transition-colors cursor-pointer"
              title="لنک شیئر کریں"
            >
              {copied ? <Check className="w-4 h-4 text-emerald-400" /> : <Share2 className="w-4 h-4" />}
            </button>
          </div>
        </div>
      </header>

      {/* 2. ASSISTANCE BANNER (Shows fast direct option) */}
      <div className="bg-[#0c241b] border-b border-[#d4af37]/20 px-4 py-2 flex items-center justify-between gap-3 text-xs font-urdu text-emerald-200">
        <div className="flex items-center gap-2 overflow-hidden">
          <ShieldCheck className="w-4 h-4 text-emerald-400 shrink-0" />
          <span className="truncate">
            آن لائن ریڈر لوڈ ہو رہا ہے۔ اگر آپ کے براؤزر میں پی ڈی ایف فریم نظر نہ آئے تو براہِ راست <strong>"ڈاؤن لوڈ کریں"</strong> یا <strong>"نئی ونڈو میں پی ڈی ایف"</strong> پر کلک کریں۔
          </span>
        </div>

        <a
          href={pdfUrl}
          target="_blank"
          rel="noopener noreferrer"
          className="shrink-0 px-2.5 py-1 rounded-lg bg-[#d4af37]/20 hover:bg-[#d4af37]/30 text-[#fae19c] font-bold border border-[#d4af37]/40 transition-all flex items-center gap-1"
        >
          <span>براہ راست فل اسکرین کھولیں ↗</span>
        </a>
      </div>

      {/* 3. DEDICATED FULL-SCREEN RESPONSIVE PDF VIEWER STAGE */}
      <main className="flex-1 w-full relative bg-[#030907] flex flex-col min-h-[85vh]">
        {isLoading && (
          <div className="absolute inset-0 bg-[#030907]/90 flex flex-col items-center justify-center gap-3 z-10 p-6 text-center backdrop-blur-sm">
            <div className="w-14 h-14 rounded-full border-4 border-[#d4af37]/20 border-t-[#d4af37] animate-spin" />
            <div className="space-y-1">
              <p className="text-base font-bold text-white font-arabic">جاري فتح الكتاب...</p>
              <p className="text-xs text-[#fae19c] font-urdu">کتاب کا پی ڈی ایف لوڈ کیا جا رہا ہے، برائے مہربانی چند لمحے انتظار فرمائیں...</p>
            </div>
          </div>
        )}

        {/* The Native PDF Viewport Frame */}
        <iframe
          src={`${pdfUrl}#toolbar=1&navpanes=1&statusbar=1&view=FitH`}
          title={title}
          className="w-full flex-1 border-0 min-h-[88vh] bg-slate-900"
          onLoad={() => setIsLoading(false)}
          onError={() => {
            setIsLoading(false);
            setLoadError(true);
          }}
        />
      </main>

      {/* Sponsored Ad Banner for Book Reader */}
      <div className="bg-[#05110d] px-4 py-2 border-t border-[#d4af37]/20 flex justify-center">
        <AdsterraBanner className="my-1" />
      </div>

      {/* 4. FOOTER STATUS BAR */}
      <footer className="bg-[#05110d] border-t border-[#d4af37]/20 px-4 py-2.5 flex items-center justify-between text-xs text-slate-400 font-urdu">
        <div className="flex items-center gap-2">
          <BookOpen className="w-4 h-4 text-[#d4af37]" />
          <span>بیت العلم AI – ڈیجیٹل اسلامی دارالعلوم و کتب خانہ</span>
        </div>

        <div className="flex items-center gap-3">
          <button
            onClick={() => downloadDirectPdf(pdfUrl, title)}
            className="text-[#fae19c] hover:underline font-bold"
          >
            براہِ راست ڈاؤن لوڈ کریں
          </button>
          <span>•</span>
          <Link to="/library" className="hover:text-white transition-colors">
            دیگر کتب دیکھیں
          </Link>
        </div>
      </footer>
    </div>
  );
};
