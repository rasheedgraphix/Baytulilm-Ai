import React, { useState, useEffect, useRef } from 'react';
import Hls from 'hls.js';

interface StreamChannel {
  id: string;
  nameUrdu: string;
  locationUrdu: string;
  descUrdu: string;
  icon: string;
  youtubeUrl: string;
  sources: string[];
}

const CHANNELS: StreamChannel[] = [
  {
    id: 'makkah',
    nameUrdu: 'مکہ مکرمہ لائیو',
    locationUrdu: 'المسجد الحرام، مکہ مکرمہ',
    descUrdu: 'سعودی قرآن ٹی وی - بیت اللہ شریف سے 24 گھنٹے براہِ راست نشریات و تلاوتِ کلامِ پاک',
    icon: '🕋',
    youtubeUrl: 'https://www.youtube.com/@SaudiQuranTv/live',
    sources: [
      'https://cdn-globecast.akamaized.net/live/eds/saudi_quran/hls_roku/index.m3u8',
      'https://m.live.net.sa:1935/live/quran/playlist.m3u8',
      'https://m.live.net.sa:1935/live/quran/gmswf.m3u8'
    ]
  },
  {
    id: 'madinah',
    nameUrdu: 'مدینہ منورہ لائیو',
    locationUrdu: 'المسجد النبوی، مدینہ منورہ',
    descUrdu: 'سعودی سنت ٹی وی - روضۂ رسول ﷺ اور مسجدِ نبوی سے 24 گھنٹے براہِ راست نشریات و احادیثِ مبارکہ',
    icon: '🕌',
    youtubeUrl: 'https://www.youtube.com/@SaudiSunnahTv/live',
    sources: [
      'https://cdn-globecast.akamaized.net/live/eds/saudi_sunnah/hls_roku/index.m3u8',
      'https://m.live.net.sa:1935/live/sunnah/playlist.m3u8',
      'https://m.live.net.sa:1935/live/sunnah/gmswf.m3u8'
    ]
  }
];

export const HaramainLive: React.FC = () => {
  const [activeChannelIndex, setActiveChannelIndex] = useState(0);
  const [sourceIndex, setSourceIndex] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [hasError, setHasError] = useState(false);
  const [isPlaying, setIsPlaying] = useState(true);
  const [isMuted, setIsMuted] = useState(true);
  
  const videoRef = useRef<HTMLVideoElement>(null);
  const hlsRef = useRef<Hls | null>(null);
  const containerRef = useRef<HTMLDivElement>(null);

  const currentChannel = CHANNELS[activeChannelIndex];

  useEffect(() => {
    const video = videoRef.current;
    if (!video) return;

    setIsLoading(true);
    setHasError(false);

    if (hlsRef.current) {
      hlsRef.current.destroy();
      hlsRef.current = null;
    }

    const streamUrl = currentChannel.sources[sourceIndex] || currentChannel.sources[0];

    if (Hls.isSupported()) {
      const hls = new Hls({
        enableWorker: true,
        lowLatencyMode: true,
        backBufferLength: 60
      });
      hlsRef.current = hls;

      hls.loadSource(streamUrl);
      hls.attachMedia(video);

      hls.on(Hls.Events.MANIFEST_PARSED, () => {
        setIsLoading(false);
        video.play().catch(() => {
          setIsPlaying(false);
        });
      });

      hls.on(Hls.Events.ERROR, (_, data) => {
        if (data.fatal) {
          if (data.type === Hls.ErrorTypes.NETWORK_ERROR) {
            // Auto switch to next backup server if available
            if (sourceIndex < currentChannel.sources.length - 1) {
              setSourceIndex(prev => prev + 1);
            } else {
              setIsLoading(false);
              setHasError(true);
            }
          } else {
            setIsLoading(false);
            setHasError(true);
          }
        }
      });
    } else if (video.canPlayType('application/vnd.apple.mpegurl')) {
      // Native Safari / Apple HLS
      video.src = streamUrl;
      video.addEventListener('loadedmetadata', () => {
        setIsLoading(false);
        video.play().catch(() => setIsPlaying(false));
      });
      video.addEventListener('error', () => {
        setIsLoading(false);
        setHasError(true);
      });
    } else {
      setIsLoading(false);
      setHasError(true);
    }

    return () => {
      if (hlsRef.current) {
        hlsRef.current.destroy();
        hlsRef.current = null;
      }
    };
  }, [activeChannelIndex, sourceIndex]);

  const togglePlay = () => {
    if (!videoRef.current) return;
    if (videoRef.current.paused) {
      videoRef.current.play();
      setIsPlaying(true);
    } else {
      videoRef.current.pause();
      setIsPlaying(false);
    }
  };

  const toggleMute = () => {
    if (!videoRef.current) return;
    videoRef.current.muted = !videoRef.current.muted;
    setIsMuted(videoRef.current.muted);
  };

  const toggleFullscreen = () => {
    if (!containerRef.current) return;
    if (!document.fullscreenElement) {
      containerRef.current.requestFullscreen().catch(console.error);
    } else {
      document.exitFullscreen();
    }
  };

  return (
    <div className="w-full max-w-5xl mx-auto p-4 font-sans text-slate-100" dir="rtl">
      {/* Channel Switcher Tabs */}
      <div className="grid grid-cols-2 gap-3 mb-4">
        {CHANNELS.map((channel, idx) => {
          const isSelected = idx === activeChannelIndex;
          return (
            <button
              key={channel.id}
              onClick={() => {
                if (idx !== activeChannelIndex) {
                  setActiveChannelIndex(idx);
                  setSourceIndex(0);
                }
              }}
              className={`flex items-center gap-3 p-4 rounded-xl border-2 transition-all duration-200 text-right ${
                isSelected
                  ? 'bg-slate-800/90 border-amber-500 shadow-lg shadow-amber-500/10'
                  : 'bg-slate-900 border-slate-800 text-slate-400 hover:border-slate-700'
              }`}
            >
              <span className="text-3xl">{channel.icon}</span>
              <div>
                <div className="font-bold text-base text-white">{channel.nameUrdu}</div>
                <div className="text-xs text-amber-400/90">{channel.locationUrdu}</div>
              </div>
            </button>
          );
        })}
      </div>

      {/* Video Player Box */}
      <div
        ref={containerRef}
        className="relative w-full aspect-video bg-black rounded-2xl overflow-hidden shadow-2xl border border-slate-800 group"
      >
        <video
          ref={videoRef}
          className="w-full h-full object-contain"
          playsInline
          autoPlay
          muted={isMuted}
        />

        {/* Loading Spinner */}
        {isLoading && (
          <div className="absolute inset-0 flex flex-col items-center justify-center bg-black/60 backdrop-blur-sm z-10">
            <div className="w-12 h-12 border-4 border-amber-500 border-t-transparent rounded-full animate-spin mb-3"></div>
            <span className="text-sm font-medium text-amber-300">سٹریم لوڈ ہو رہی ہے...</span>
          </div>
        )}

        {/* Error Fallback */}
        {hasError && (
          <div className="absolute inset-0 flex flex-col items-center justify-center bg-black/90 p-6 text-center z-20">
            <div className="text-amber-400 text-4xl mb-3">⚠️</div>
            <h3 className="text-lg font-bold text-white mb-1">سٹریم پلے بیک میں رکاوٹ</h3>
            <p className="text-xs text-slate-400 mb-4 max-w-sm">
              سرور پر لوڈ زیادہ ہونے کی صورت میں متبادل سرور یا یوٹیوب استعمال کریں۔
            </p>
            <div className="flex gap-3">
              <button
                onClick={() => setSourceIndex((prev) => (prev + 1) % currentChannel.sources.length)}
                className="bg-amber-600 hover:bg-amber-500 text-slate-950 font-bold px-4 py-2 rounded-lg text-xs transition"
              >
                دوسرا سرور آزمائیں
              </button>
              <a
                href={currentChannel.youtubeUrl}
                target="_blank"
                rel="noopener noreferrer"
                className="bg-red-600 hover:bg-red-500 text-white font-bold px-4 py-2 rounded-lg text-xs transition inline-flex items-center gap-1"
              >
                یوٹیوب پر لائیو دیکھیں ↗
              </a>
            </div>
          </div>
        )}

        {/* Floating Custom Controls */}
        <div className="absolute bottom-0 inset-x-0 bg-gradient-to-t from-black/90 via-black/40 to-transparent p-4 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-between z-10">
          <div className="flex items-center gap-3">
            <button
              onClick={togglePlay}
              className="w-9 h-9 rounded-full bg-white/20 hover:bg-white/30 flex items-center justify-center transition"
            >
              {isPlaying ? '⏸' : '▶'}
            </button>
            <button
              onClick={toggleMute}
              className="w-9 h-9 rounded-full bg-white/20 hover:bg-white/30 flex items-center justify-center transition"
            >
              {isMuted ? '🔇' : '🔊'}
            </button>
            <span className="text-xs font-bold px-2 py-0.5 rounded bg-red-600 text-white">LIVE</span>
            <span className="text-xs text-slate-300">{currentChannel.nameUrdu}</span>
          </div>

          <div className="flex items-center gap-2">
            <button
              onClick={toggleFullscreen}
              className="w-9 h-9 rounded-full bg-white/20 hover:bg-white/30 flex items-center justify-center transition"
              title="فل اسکرین"
            >
              ⛶
            </button>
          </div>
        </div>
      </div>

      {/* Info & Server Selection */}
      <div className="mt-4 bg-slate-900/60 border border-slate-800 rounded-xl p-4 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-base font-bold text-white mb-0.5">{currentChannel.locationUrdu}</h2>
          <p className="text-xs text-slate-400">{currentChannel.descUrdu}</p>
        </div>

        <div className="flex items-center gap-2 text-xs">
          <span className="text-slate-500">سرور:</span>
          {currentChannel.sources.map((_, sIdx) => (
            <button
              key={sIdx}
              onClick={() => setSourceIndex(sIdx)}
              className={`px-3 py-1 rounded-lg border transition ${
                sIdx === sourceIndex
                  ? 'bg-amber-500/20 text-amber-300 border-amber-500/50 font-medium'
                  : 'bg-slate-800 text-slate-400 border-slate-700 hover:bg-slate-700'
              }`}
            >
              سرور {sIdx + 1}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};
export default HaramainLive;
