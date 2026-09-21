import React, { useState, useEffect, useRef, useCallback } from 'react';
import Hls from 'hls.js';
import {
  Play,
  Pause,
  Volume2,
  VolumeX,
  Volume1,
  Maximize2,
  Minimize2,
  RotateCw,
  Radio,
  Sparkles,
  CheckCircle2,
  Share2,
  ExternalLink,
  Tv,
  Layers,
  AlertTriangle,
  Flame,
  ShieldCheck
} from 'lucide-react';
import { useLanguage } from '../../context/LanguageContext';

export type HaramainChannelId = 'makkah' | 'madinah';
export type PlayerEngine = 'hls' | 'backup';

export interface HlsStreamSource {
  id: number;
  nameUrdu: string;
  nameArabic: string;
  url: string;
  quality: string;
}

export interface ChannelConfig {
  id: HaramainChannelId;
  titleUrdu: string;
  titleArabic: string;
  titleEnglish: string;
  subtitleUrdu: string;
  subtitleArabic: string;
  subtitleEnglish: string;
  quranAyatArabic: string;
  quranAyatUrdu: string;
  reference: string;
  youtubeLiveUrl: string;
  youtubeEmbedUrl: string;
  hlsStreams: HlsStreamSource[];
}

export const HARAMAIN_CHANNELS: Record<HaramainChannelId, ChannelConfig> = {
  makkah: {
    id: 'makkah',
    titleUrdu: 'مکہ مکرمہ براہِ راست',
    titleArabic: 'مكة المكرمة مباشرة',
    titleEnglish: 'Makkah Live 24/7',
    subtitleUrdu: 'المسجد الحرام، کعبۃ اللہ شریف — سعودی قرآن ٹی وی',
    subtitleArabic: 'المسجد الحرام - قناة القرآن الكريم السعودية',
    subtitleEnglish: 'Masjid al-Haram, Kaaba Sharif - Saudi Quran TV',
    quranAyatArabic: 'إِنَّ أَوَّلَ بَيْتٍ وُضِعَ لِلنَّاسِ لَلَّذِي بِبَكَّةَ مُبَارَكًا وَهُدًى لِّلْعَالَمِينَ',
    quranAyatUrdu: 'بیشک پہلا گھر جو لوگوں (کی عبادت) کے لیے مقرر کیا گیا وہی ہے جو مکہ میں ہے، جو بابرکت ہے اور جہاں تمام جہانوں کے لیے ہدایت ہے۔',
    reference: 'سورة آل عمران: ۹۶',
    youtubeLiveUrl: 'https://www.youtube.com/@SaudiQuranTv/live',
    youtubeEmbedUrl: 'https://www.youtube-nocookie.com/embed/live_stream?channel=UCv0tL9gS4y7w-aYQjKeqwsg&autoplay=1&playsinline=1&rel=0',
    hlsStreams: [
      {
        id: 1,
        nameUrdu: 'سٹریم ۱: Akamai CDN (1080p HD)',
        nameArabic: 'البث ١: أكاماي فائق السرعة',
        url: 'https://cdn-globecast.akamaized.net/live/eds/saudi_quran/hls_roku/index.m3u8',
        quality: '1080p / 720p HD'
      },
      {
        id: 2,
        nameUrdu: 'سٹریم ۲: Saudi Live Network',
        nameArabic: 'البث ٢: شبكة البث المباشر',
        url: 'http://m.live.net.sa:1935/live/quran/playlist.m3u8',
        quality: '720p HD'
      },
      {
        id: 3,
        nameUrdu: 'سٹریم ۳: Holol HD Master Feed',
        nameArabic: 'البث ٣: خادم هولول المباشر',
        url: 'https://win.holol.com/live/quran/index.m3u8',
        quality: 'HD Stream'
      }
    ]
  },
  madinah: {
    id: 'madinah',
    titleUrdu: 'مدینہ منورہ براہِ راست',
    titleArabic: 'المدينة المنورة مباشرة',
    titleEnglish: 'Madinah Live 24/7',
    subtitleUrdu: 'المسجد النبوی الشریف، گنبدِ خضراء — سعودی سنہ ٹی وی',
    subtitleArabic: 'المسجد النبوي الشريف - قناة السنة النبوية السعودية',
    subtitleEnglish: 'Al-Masjid an-Nabawi, Rawdah Sharif - Saudi Sunnah TV',
    quranAyatArabic: 'مَا بَيْنَ بَيْتِي وَمِنْبَرِي رَوْضَةٌ مِنْ رِيَاضِ الْجَنَّةِ',
    quranAyatUrdu: 'میرے گھر اور میرے منبر کے درمیانی جگہ جنت کے باغوں میں سے ایک باغ ہے۔',
    reference: 'صحیح البخاری: ۱۱۹۵',
    youtubeLiveUrl: 'https://www.youtube.com/@SaudiSunnahTv/live',
    youtubeEmbedUrl: 'https://www.youtube-nocookie.com/embed/live_stream?channel=UCXw_T67V9Xm8E8t6XmB45yA&autoplay=1&playsinline=1&rel=0',
    hlsStreams: [
      {
        id: 1,
        nameUrdu: 'سٹریم ۱: Akamai CDN (1080p HD)',
        nameArabic: 'البث ١: أكاماي فائق السرعة',
        url: 'https://cdn-globecast.akamaized.net/live/eds/saudi_sunnah/hls_roku/index.m3u8',
        quality: '1080p / 720p HD'
      },
      {
        id: 2,
        nameUrdu: 'سٹریم ۲: Saudi Live Network',
        nameArabic: 'البث ٢: شبكة البث المباشر',
        url: 'http://m.live.net.sa:1935/live/sunnah/playlist.m3u8',
        quality: '720p HD'
      },
      {
        id: 3,
        nameUrdu: 'سٹریم ۳: Holol HD Master Feed',
        nameArabic: 'البث ٣: خادم هولول المباشر',
        url: 'https://win.holol.com/live/sunnah/index.m3u8',
        quality: 'HD Stream'
      }
    ]
  }
};

interface HaramainLivePlayerProps {
  initialChannel?: HaramainChannelId;
  standalone?: boolean;
}

export const HaramainLivePlayer: React.FC<HaramainLivePlayerProps> = ({
  initialChannel = 'makkah',
  standalone = false
}) => {
  const { isRtl } = useLanguage();
  const [currentChannelId, setCurrentChannelId] = useState<HaramainChannelId>(initialChannel);
  const [engine, setEngine] = useState<PlayerEngine>('hls');
  const [activeHlsIndex, setActiveHlsIndex] = useState<number>(0);

  // Player state
  const [isPlaying, setIsPlaying] = useState<boolean>(false);
  const [isMuted, setIsMuted] = useState<boolean>(false);
  const [volume, setVolume] = useState<number>(1);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [isFullscreen, setIsFullscreen] = useState<boolean>(false);
  const [copiedLink, setCopiedLink] = useState<boolean>(false);
  const [autoFallbackTriggered, setAutoFallbackTriggered] = useState<boolean>(false);

  const videoRef = useRef<HTMLVideoElement | null>(null);
  const hlsRef = useRef<Hls | null>(null);
  const containerRef = useRef<HTMLDivElement | null>(null);
  const retryTimeoutRef = useRef<NodeJS.Timeout | null>(null);

  const currentChannel = HARAMAIN_CHANNELS[currentChannelId];
  const activeHlsSource = currentChannel.hlsStreams[activeHlsIndex] || currentChannel.hlsStreams[0];

  // Clean up Hls instance
  const cleanupHls = useCallback(() => {
    if (retryTimeoutRef.current) {
      clearTimeout(retryTimeoutRef.current);
      retryTimeoutRef.current = null;
    }
    if (hlsRef.current) {
      try {
        hlsRef.current.stopLoad();
        hlsRef.current.destroy();
      } catch (err) {
        console.warn('HLS cleanup error:', err);
      }
      hlsRef.current = null;
    }
  }, []);

  // Initialize and attach HLS stream
  const initHlsStream = useCallback(
    (streamUrl: string) => {
      cleanupHls();
      setIsLoading(true);
      setErrorMessage(null);

      const video = videoRef.current;
      if (!video) return;

      video.volume = volume;
      video.muted = isMuted;

      // 1. If native HLS is supported (Safari / iOS)
      if (video.canPlayType('application/vnd.apple.mpegurl')) {
        video.src = streamUrl;
        video
          .play()
          .then(() => {
            setIsPlaying(true);
            setIsLoading(false);
          })
          .catch((err) => {
            console.warn('Native video autoplay hindered:', err);
            setIsLoading(false);
          });
        return;
      }

      // 2. If Hls.js is supported (Chrome, Firefox, Edge, Android)
      if (Hls.isSupported()) {
        const hls = new Hls({
          enableWorker: true,
          lowLatencyMode: true,
          backBufferLength: 60,
          manifestLoadingTimeOut: 8000,
          manifestLoadingMaxRetry: 3,
          levelLoadingTimeOut: 8000,
          fragLoadingTimeOut: 12000
        });

        hlsRef.current = hls;
        hls.loadSource(streamUrl);
        hls.attachMedia(video);

        hls.on(Hls.Events.MANIFEST_PARSED, () => {
          setIsLoading(false);
          video
            .play()
            .then(() => {
              setIsPlaying(true);
            })
            .catch(() => {
              // Browser may require user interaction for unmuted audio
              video.muted = true;
              setIsMuted(true);
              video.play().catch(() => {});
            });
        });

        hls.on(Hls.Events.ERROR, (_event, data) => {
          console.warn('HLS Event Error:', data.type, data.details, data.fatal);
          if (data.fatal) {
            switch (data.type) {
              case Hls.ErrorTypes.NETWORK_ERROR:
                console.warn('Fatal network error in HLS stream. Attempting recovery / fallback.');
                // Try switching to next stream or fallback engine
                if (activeHlsIndex < currentChannel.hlsStreams.length - 1) {
                  setActiveHlsIndex((prev) => prev + 1);
                } else {
                  // Switch to Backup YouTube Engine automatically
                  setAutoFallbackTriggered(true);
                  setEngine('backup');
                  setIsLoading(false);
                }
                break;
              case Hls.ErrorTypes.MEDIA_ERROR:
                hls.recoverMediaError();
                break;
              default:
                cleanupHls();
                setAutoFallbackTriggered(true);
                setEngine('backup');
                setIsLoading(false);
                break;
            }
          }
        });
      } else {
        // Fallback for browsers without HLS support
        setAutoFallbackTriggered(true);
        setEngine('backup');
        setIsLoading(false);
      }
    },
    [activeHlsIndex, cleanupHls, currentChannel.hlsStreams.length, isMuted, volume]
  );

  // Effect to load stream when channel, HLS index, or engine changes
  useEffect(() => {
    if (engine === 'hls') {
      initHlsStream(activeHlsSource.url);
    } else {
      cleanupHls();
      setIsLoading(true);
      const timer = setTimeout(() => {
        setIsLoading(false);
      }, 1200);
      return () => clearTimeout(timer);
    }

    return () => {
      cleanupHls();
    };
  }, [engine, currentChannelId, activeHlsIndex, initHlsStream, activeHlsSource.url, cleanupHls]);

  // Fullscreen change listener
  useEffect(() => {
    const handleFs = () => {
      setIsFullscreen(!!document.fullscreenElement);
    };
    document.addEventListener('fullscreenchange', handleFs);
    return () => document.removeEventListener('fullscreenchange', handleFs);
  }, []);

  // Channel switch handler
  const handleSelectChannel = (channelId: HaramainChannelId) => {
    if (channelId === currentChannelId) return;
    setCurrentChannelId(channelId);
    setActiveHlsIndex(0);
    setAutoFallbackTriggered(false);
    setErrorMessage(null);
  };

  // Play / Pause toggle for HLS engine
  const handleTogglePlay = () => {
    const video = videoRef.current;
    if (!video) return;

    if (video.paused) {
      video.play().then(() => setIsPlaying(true)).catch(console.warn);
    } else {
      video.pause();
      setIsPlaying(false);
    }
  };

  // Volume toggle
  const handleToggleMute = () => {
    const video = videoRef.current;
    if (!video) return;

    const newMuted = !isMuted;
    video.muted = newMuted;
    setIsMuted(newMuted);
    if (!newMuted && volume === 0) {
      setVolume(0.8);
      video.volume = 0.8;
    }
  };

  // Volume slider change
  const handleVolumeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newVol = parseFloat(e.target.value);
    setVolume(newVol);
    const video = videoRef.current;
    if (video) {
      video.volume = newVol;
      video.muted = newVol === 0;
      setIsMuted(newVol === 0);
    }
  };

  // Manual Reload
  const handleReload = () => {
    setIsLoading(true);
    setErrorMessage(null);
    if (engine === 'hls') {
      initHlsStream(activeHlsSource.url);
    } else {
      setEngine('hls');
      setActiveHlsIndex(0);
    }
  };

  // Fullscreen toggle
  const toggleFullscreen = () => {
    const el = containerRef.current;
    if (!el) return;

    if (!document.fullscreenElement) {
      el.requestFullscreen?.().catch((err) => {
        console.warn('Fullscreen request failed:', err);
      });
    } else {
      document.exitFullscreen?.().catch(() => {});
    }
  };

  // Share URL
  const handleShare = () => {
    const url = window.location.origin + '/haramain-live';
    if (navigator.clipboard) {
      navigator.clipboard.writeText(url);
      setCopiedLink(true);
      setTimeout(() => setCopiedLink(false), 2500);
    }
  };

  return (
    <div
      className={`w-full ${standalone ? 'max-w-6xl mx-auto' : 'max-w-5xl mx-auto'} space-y-4 select-none`}
      dir={isRtl ? 'rtl' : 'ltr'}
    >
      {/* 1. CHANNEL SELECTION & DUAL ENGINE CONTROLS */}
      <div className="bg-gradient-to-b from-[#091510] via-[#05110d] to-[#030907] border border-[#d4af37]/30 rounded-3xl p-4 sm:p-6 shadow-2xl relative overflow-hidden">
        <div className="absolute top-0 right-0 w-80 h-80 bg-[#d4af37]/10 rounded-full blur-3xl pointer-events-none" />
        <div className="absolute bottom-0 left-0 w-80 h-80 bg-emerald-500/10 rounded-full blur-3xl pointer-events-none" />

        <div className="relative z-10 space-y-4">
          {/* Top Bar: Badges & Live Status */}
          <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 border-b border-white/10 pb-4">
            <div className="space-y-1">
              <div className="flex items-center gap-2 flex-wrap">
                <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-red-600 text-white text-xs font-bold tracking-wider animate-pulse shadow-[0_0_12px_#ef4444]">
                  <span className="w-2 h-2 rounded-full bg-white" />
                  <span>24/7 براہِ راست نشریات</span>
                </div>
                <div className="inline-flex items-center gap-1 px-2.5 py-1 rounded-full bg-[#d4af37]/20 border border-[#d4af37]/40 text-[#f3e5ab] text-xs font-bold">
                  <Sparkles className="w-3.5 h-3.5 text-[#d4af37]" />
                  <span>Akamai CDN Direct HLS Stream</span>
                </div>
                <div className="inline-flex items-center gap-1 px-2.5 py-1 rounded-full bg-emerald-500/20 border border-emerald-500/40 text-emerald-300 text-xs font-bold">
                  <ShieldCheck className="w-3.5 h-3.5 text-emerald-400" />
                  <span className="font-urdu">سرکاری سیٹلائٹ فیڈ</span>
                </div>
              </div>

              <h2 className="text-xl sm:text-2xl lg:text-3xl font-black text-transparent bg-clip-text bg-gradient-to-r from-[#fae19c] via-[#f3e5ab] to-[#d4af37] font-arabic pt-1">
                حرمین شریفین براہِ راست نشریات (مکہ مکرمہ و مدینہ منورہ)
              </h2>
            </div>

            {/* Quick Actions */}
            <div className="flex items-center gap-2 shrink-0">
              <a
                href={currentChannel.youtubeLiveUrl}
                target="_blank"
                rel="noopener noreferrer"
                className="inline-flex items-center gap-1.5 px-3.5 py-2 rounded-xl bg-red-950/80 hover:bg-red-900 border border-red-500/50 text-xs font-bold text-red-200 transition-all shadow-md cursor-pointer"
                title="آفیشل یوٹیوب چینل پر دیکھیں"
              >
                <ExternalLink className="w-4 h-4 text-red-400" />
                <span className="font-urdu">آفیشل لائیو چینل ↗</span>
              </a>

              <button
                onClick={handleShare}
                className="inline-flex items-center gap-1.5 px-3.5 py-2 rounded-xl bg-slate-900/90 border border-[#d4af37]/30 hover:border-[#d4af37] text-xs font-bold text-[#fae19c] transition-all hover:bg-slate-800 cursor-pointer"
                title="لنک کاپی کریں"
              >
                {copiedLink ? <CheckCircle2 className="w-4 h-4 text-emerald-400" /> : <Share2 className="w-4 h-4" />}
                <span className="font-urdu">{copiedLink ? 'کاپی ہو گیا' : 'شیئر'}</span>
              </button>

              <button
                onClick={handleReload}
                className="inline-flex items-center gap-1.5 px-3.5 py-2 rounded-xl bg-emerald-950/80 border border-emerald-500/50 hover:bg-emerald-900 text-xs font-bold text-emerald-200 transition-all cursor-pointer"
                title="سٹریم ریفریش کریں"
              >
                <RotateCw className={`w-4 h-4 ${isLoading ? 'animate-spin' : ''}`} />
                <span className="font-urdu">ریفریش</span>
              </button>
            </div>
          </div>

          {/* TWO MAIN LIVE CHANNELS */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4">
            {/* Makkah Channel Button */}
            <button
              onClick={() => handleSelectChannel('makkah')}
              className={`p-4 rounded-2xl transition-all duration-200 text-right flex items-center justify-between gap-3 border relative overflow-hidden cursor-pointer ${
                currentChannelId === 'makkah'
                  ? 'bg-gradient-to-r from-[#1b2b20] to-[#0f1f16] border-[#d4af37] ring-2 ring-[#d4af37]/50 shadow-xl shadow-[#d4af37]/15'
                  : 'bg-[#06100c] border-white/10 hover:border-[#d4af37]/40 hover:bg-[#0a1813] text-slate-300'
              }`}
            >
              <div className="flex items-center gap-3">
                <div
                  className={`w-12 h-12 rounded-2xl flex items-center justify-center font-bold text-2xl shrink-0 transition-transform ${
                    currentChannelId === 'makkah'
                      ? 'bg-gradient-to-br from-[#d4af37] to-[#997316] text-black shadow-lg scale-105'
                      : 'bg-slate-800 text-amber-300'
                  }`}
                >
                  🕋
                </div>
                <div>
                  <div className="flex items-center gap-2">
                    <span className="text-base sm:text-lg font-black text-white font-arabic">
                      مکہ مکرمہ لائیو
                    </span>
                    <span className="text-[10px] px-2 py-0.5 rounded-full font-bold bg-[#d4af37]/20 text-[#fae19c] border border-[#d4af37]/40">
                      سعودی قرآن ٹی وی
                    </span>
                  </div>
                  <p className="text-xs text-slate-300 font-urdu mt-0.5">
                    المسجد الحرام، کعبۃ اللہ شریف، مطاف و صفا مروہ
                  </p>
                </div>
              </div>

              {currentChannelId === 'makkah' && (
                <span className="inline-flex items-center gap-1 text-xs font-bold text-[#fae19c] font-urdu shrink-0 bg-[#d4af37]/20 px-2.5 py-1 rounded-lg border border-[#d4af37]/40">
                  <span className="w-2 h-2 rounded-full bg-emerald-400 animate-ping" />
                  چل رہا ہے
                </span>
              )}
            </button>

            {/* Madinah Channel Button */}
            <button
              onClick={() => handleSelectChannel('madinah')}
              className={`p-4 rounded-2xl transition-all duration-200 text-right flex items-center justify-between gap-3 border relative overflow-hidden cursor-pointer ${
                currentChannelId === 'madinah'
                  ? 'bg-gradient-to-r from-[#1b2b20] to-[#0f1f16] border-[#d4af37] ring-2 ring-[#d4af37]/50 shadow-xl shadow-[#d4af37]/15'
                  : 'bg-[#06100c] border-white/10 hover:border-[#d4af37]/40 hover:bg-[#0a1813] text-slate-300'
              }`}
            >
              <div className="flex items-center gap-3">
                <div
                  className={`w-12 h-12 rounded-2xl flex items-center justify-center font-bold text-2xl shrink-0 transition-transform ${
                    currentChannelId === 'madinah'
                      ? 'bg-gradient-to-br from-[#d4af37] to-[#997316] text-black shadow-lg scale-105'
                      : 'bg-slate-800 text-emerald-300'
                  }`}
                >
                  🕌
                </div>
                <div>
                  <div className="flex items-center gap-2">
                    <span className="text-base sm:text-lg font-black text-white font-arabic">
                      مدینہ منورہ لائیو
                    </span>
                    <span className="text-[10px] px-2 py-0.5 rounded-full font-bold bg-emerald-500/20 text-emerald-300 border border-emerald-500/40">
                      سعودی سنہ ٹی وی
                    </span>
                  </div>
                  <p className="text-xs text-slate-300 font-urdu mt-0.5">
                    المسجد النبوی الشریف، روضۂ اطہر، گنبدِ خضراء
                  </p>
                </div>
              </div>

              {currentChannelId === 'madinah' && (
                <span className="inline-flex items-center gap-1 text-xs font-bold text-[#fae19c] font-urdu shrink-0 bg-emerald-500/20 px-2.5 py-1 rounded-lg border border-emerald-500/40">
                  <span className="w-2 h-2 rounded-full bg-emerald-400 animate-ping" />
                  چل رہا ہے
                </span>
              )}
            </button>
          </div>

          {/* DUAL-ENGINE SELECTOR & HLS SOURCE SWITCHER */}
          <div className="flex flex-wrap items-center justify-between gap-3 pt-3 border-t border-white/10">
            {/* Engine Tabs */}
            <div className="flex items-center gap-1.5 bg-black/60 p-1 rounded-xl border border-white/10">
              <button
                onClick={() => {
                  setEngine('hls');
                  setAutoFallbackTriggered(false);
                }}
                className={`px-3 py-1.5 rounded-lg text-xs font-urdu font-bold transition-all cursor-pointer flex items-center gap-1.5 ${
                  engine === 'hls'
                    ? 'bg-gradient-to-r from-emerald-600 to-teal-700 text-white shadow-md'
                    : 'text-slate-400 hover:text-white'
                }`}
              >
                <Flame className="w-3.5 h-3.5 text-amber-300" />
                <span>ڈائریکٹ HLS سٹریمنگ (اشتہارات کے بغیر)</span>
              </button>

              <button
                onClick={() => setEngine('backup')}
                className={`px-3 py-1.5 rounded-lg text-xs font-urdu font-bold transition-all cursor-pointer flex items-center gap-1.5 ${
                  engine === 'backup'
                    ? 'bg-gradient-to-r from-red-600 to-rose-700 text-white shadow-md'
                    : 'text-slate-400 hover:text-white'
                }`}
              >
                <Tv className="w-3.5 h-3.5 text-white" />
                <span>بیک اپ لائیو فیڈ</span>
              </button>
            </div>

            {/* HLS Server Sources when in HLS mode */}
            {engine === 'hls' && (
              <div className="flex items-center gap-1.5 flex-wrap">
                <span className="text-xs text-slate-400 font-urdu flex items-center gap-1">
                  <Layers className="w-3 h-3 text-[#d4af37]" />
                  سرور:
                </span>
                {currentChannel.hlsStreams.map((src, idx) => (
                  <button
                    key={src.id}
                    onClick={() => {
                      setActiveHlsIndex(idx);
                      setIsLoading(true);
                    }}
                    className={`px-2.5 py-1 rounded-lg text-xs font-urdu transition-all cursor-pointer border ${
                      activeHlsIndex === idx
                        ? 'bg-[#d4af37] text-slate-950 font-bold border-[#d4af37]'
                        : 'bg-black/40 hover:bg-slate-800 text-slate-300 border-white/10'
                    }`}
                  >
                    {src.nameUrdu}
                  </button>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>

      {/* 2. DUAL-ENGINE VIDEO PLAYER STAGE */}
      <div
        ref={containerRef}
        className="relative bg-black rounded-3xl overflow-hidden shadow-2xl border-2 border-[#d4af37]/40 aspect-video group"
      >
        {/* ENGINE 1: DIRECT HLS NATIVE VIDEO */}
        {engine === 'hls' ? (
          <video
            ref={videoRef}
            className="w-full h-full object-contain bg-black relative z-10"
            playsInline
            autoPlay
            onWaiting={() => setIsLoading(true)}
            onPlaying={() => {
              setIsLoading(false);
              setIsPlaying(true);
            }}
            onPause={() => setIsPlaying(false)}
            onError={() => {
              console.warn('Native video error. Auto-fallback to backup stream.');
              setAutoFallbackTriggered(true);
              setEngine('backup');
            }}
          />
        ) : (
          /* ENGINE 2: BACKUP EMBEDDED PLAYER */
          <iframe
            key={`${currentChannelId}-backup`}
            src={currentChannel.youtubeEmbedUrl}
            title={`${currentChannel.titleUrdu} بیک اپ لائیو`}
            className="w-full h-full border-0 bg-black relative z-10"
            referrerPolicy="no-referrer-when-downgrade"
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
            allowFullScreen
            onLoad={() => setIsLoading(false)}
          />
        )}

        {/* LOADING SPINNER OVERLAY */}
        {isLoading && (
          <div className="absolute inset-0 bg-[#020b08]/90 flex flex-col items-center justify-center gap-3 z-20 text-center p-4 backdrop-blur-sm">
            <div className="relative w-16 h-16">
              <div className="w-16 h-16 rounded-full border-4 border-[#d4af37]/20 border-t-[#d4af37] animate-spin" />
              <div className="absolute inset-0 flex items-center justify-center text-xl">
                {currentChannelId === 'makkah' ? '🕋' : '🕌'}
              </div>
            </div>
            <div className="space-y-1">
              <p className="text-base font-bold text-white font-arabic">
                {engine === 'hls' ? 'جاري تشغيل البث المباشر (HLS Akamai)...' : 'جاري تشغيل البث الاحتياطي...'}
              </p>
              <p className="text-xs text-[#fae19c] font-urdu">
                براہِ راست نشریات منسلک ہو رہی ہیں، چند لمحے انتظار فرمائیں...
              </p>
            </div>
          </div>
        )}

        {/* TOP STATUS BAR (Overlay) */}
        <div className="absolute top-0 left-0 right-0 p-3 sm:p-4 bg-gradient-to-b from-black/90 via-black/40 to-transparent z-30 flex items-center justify-between pointer-events-none transition-opacity duration-300">
          <div className="flex items-center gap-2 pointer-events-auto">
            <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-red-600 text-white text-xs font-extrabold shadow-md animate-pulse">
              <Radio className="w-3.5 h-3.5" />
              <span>مباشر LIVE</span>
            </div>
            <span className="text-white font-arabic font-bold text-xs sm:text-sm drop-shadow-md bg-black/60 px-2.5 py-1 rounded-lg border border-white/20">
              {currentChannel.titleArabic}
            </span>
          </div>

          <div className="flex items-center gap-2 pointer-events-auto">
            {autoFallbackTriggered && (
              <span className="text-[11px] font-urdu bg-amber-500/20 text-amber-300 border border-amber-500/40 px-2.5 py-1 rounded-lg backdrop-blur-md">
                خودکار بیک اپ فعال ہے
              </span>
            )}
            <button
              onClick={toggleFullscreen}
              className="p-2 rounded-xl bg-black/80 hover:bg-[#d4af37] text-white hover:text-slate-950 transition-colors border border-white/20 backdrop-blur-md cursor-pointer shadow-lg"
              title="فل اسکرین کریں"
              aria-label={isFullscreen ? 'Exit Fullscreen' : 'Fullscreen'}
            >
              {isFullscreen ? <Minimize2 className="w-4 h-4" /> : <Maximize2 className="w-4 h-4" />}
            </button>
          </div>
        </div>

        {/* BOTTOM CONTROLS BAR (Overlay for HLS engine) */}
        {engine === 'hls' && (
          <div className="absolute bottom-0 left-0 right-0 p-3 bg-gradient-to-t from-black/90 via-black/60 to-transparent z-30 flex items-center justify-between gap-4 pointer-events-auto opacity-95 group-hover:opacity-100 transition-opacity">
            <div className="flex items-center gap-3">
              <button
                onClick={handleTogglePlay}
                className="w-10 h-10 rounded-xl bg-[#d4af37] text-slate-950 flex items-center justify-center hover:bg-[#fae19c] transition-colors shadow-lg cursor-pointer"
                title={isPlaying ? 'روکیں' : 'چلائیں'}
              >
                {isPlaying ? <Pause className="w-5 h-5 fill-current" /> : <Play className="w-5 h-5 fill-current ml-0.5" />}
              </button>

              <div className="flex items-center gap-2">
                <button
                  onClick={handleToggleMute}
                  className="p-2 rounded-lg bg-black/60 text-white hover:bg-slate-800 transition-colors cursor-pointer"
                  title={isMuted ? 'آواز کھولیں' : 'آواز بند کریں'}
                >
                  {isMuted || volume === 0 ? (
                    <VolumeX className="w-4 h-4 text-red-400" />
                  ) : volume < 0.5 ? (
                    <Volume1 className="w-4 h-4 text-[#fae19c]" />
                  ) : (
                    <Volume2 className="w-4 h-4 text-[#fae19c]" />
                  )}
                </button>
                <input
                  id="haramain-volume-slider"
                  name="volumeControl"
                  type="range"
                  min="0"
                  max="1"
                  step="0.05"
                  value={isMuted ? 0 : volume}
                  onChange={handleVolumeChange}
                  className="w-16 sm:w-24 accent-[#d4af37] cursor-pointer"
                  title="والیوم"
                  aria-label="Volume Slider"
                />
              </div>
            </div>

            <div className="flex items-center gap-2">
              <span className="text-xs text-emerald-400 font-mono flex items-center gap-1.5 bg-black/60 px-2.5 py-1 rounded-lg border border-emerald-500/30">
                <span className="w-2 h-2 rounded-full bg-emerald-400 animate-ping" />
                <span>Akamai Live HD</span>
              </span>
            </div>
          </div>
        )}
      </div>

      {/* 3. RESILIENT FALLBACK / ASSISTANCE BAR */}
      <div className="bg-slate-900/80 border border-[#d4af37]/25 rounded-2xl p-3.5 flex flex-col sm:flex-row sm:items-center justify-between gap-3 text-xs font-urdu text-slate-300 shadow-md">
        <div className="flex items-center gap-2">
          <AlertTriangle className="w-4 h-4 text-amber-400 shrink-0" />
          <span>
            اگر کسی براؤزر یا نیٹ ورک پر ویڈیو لوڈ نہ ہو، تو فوراً <strong>بیک اپ لائیو فیڈ</strong> منتخب کریں یا آفیشل چینل پر کلک کریں۔
          </span>
        </div>

        <div className="flex items-center gap-2 shrink-0 self-end sm:self-auto">
          <button
            onClick={() => setEngine(engine === 'hls' ? 'backup' : 'hls')}
            className="px-3 py-1.5 rounded-xl bg-[#d4af37]/20 hover:bg-[#d4af37]/30 text-[#fae19c] font-bold border border-[#d4af37]/40 transition-all cursor-pointer"
          >
            {engine === 'hls' ? 'بیک اپ پلیئر پر جائیں' : 'HLS پلیئر پر واپس جائیں'}
          </button>

          <a
            href={currentChannel.youtubeLiveUrl}
            target="_blank"
            rel="noopener noreferrer"
            className="px-3 py-1.5 rounded-xl bg-red-900/60 hover:bg-red-800/80 text-red-200 font-bold border border-red-500/40 transition-all flex items-center gap-1.5 cursor-pointer"
          >
            <ExternalLink className="w-3.5 h-3.5" />
            <span>آفیشل لائیو سٹریم ↗</span>
          </a>
        </div>
      </div>

      {/* 4. SPIRITUAL FOOTER DETAILS */}
      <div className="bg-gradient-to-r from-[#0d1712] via-[#09110d] to-[#0d1712] p-5 sm:p-6 rounded-2xl border border-[#d4af37]/25 shadow-lg space-y-3">
        <div className="flex items-center justify-between gap-3 flex-wrap border-b border-white/10 pb-3">
          <div className="flex items-center gap-2">
            <span className="w-2.5 h-2.5 rounded-full bg-emerald-400 shadow-[0_0_8px_#34d399]" />
            <h3 className="text-base sm:text-lg font-bold text-[#fae19c] font-arabic">
              {currentChannel.titleUrdu} — {currentChannel.subtitleUrdu}
            </h3>
          </div>
          <span className="text-xs text-[#fae19c] font-urdu bg-[#d4af37]/10 px-3 py-1 rounded-full border border-[#d4af37]/20">
            {currentChannel.reference}
          </span>
        </div>

        <div className="space-y-2 text-right">
          <p className="text-lg sm:text-xl font-bold text-white font-arabic leading-relaxed">
            "{currentChannel.quranAyatArabic}"
          </p>
          <p className="text-xs sm:text-sm text-slate-300 font-urdu leading-relaxed">
            {currentChannel.quranAyatUrdu}
          </p>
        </div>
      </div>
    </div>
  );
};
