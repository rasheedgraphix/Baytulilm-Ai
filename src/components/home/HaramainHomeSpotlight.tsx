import React from 'react';
import { Link } from 'react-router-dom';
import { Radio, Video, Play, Volume2, ShieldCheck, ArrowLeft, ArrowRight, Sparkles } from 'lucide-react';
import { useLanguage } from '../../context/LanguageContext';

export const HaramainHomeSpotlight: React.FC = () => {
  const { isRtl } = useLanguage();

  return (
    <section className="py-12 sm:py-16 bg-gradient-to-b from-slate-900 via-[#0a1b14] to-slate-950 text-white border-b border-emerald-900/40 relative overflow-hidden" dir={isRtl ? 'rtl' : 'ltr'}>
      {/* Background ambient lighting */}
      <div className="absolute top-0 right-1/4 w-80 h-80 bg-red-600/10 rounded-full blur-3xl pointer-events-none" />
      <div className="absolute bottom-0 left-1/4 w-80 h-80 bg-emerald-600/10 rounded-full blur-3xl pointer-events-none" />

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-8">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-800 pb-5">
          <div className="space-y-1.5">
            <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-red-500/15 border border-red-500/30 text-red-300 text-xs font-bold font-urdu">
              <span className="w-2 h-2 rounded-full bg-red-500 animate-pulse" />
              <span>براہِ راست نشریات ۲۴/۷ (Live Broadcast)</span>
            </div>
            <h2 className="text-2xl sm:text-3xl font-black text-white font-urdu tracking-tight">
              حرمین شریفین ۲۴ گھنٹے براہِ راست لائیو نشریات
            </h2>
            <p className="text-xs sm:text-sm text-slate-300 font-urdu max-w-2xl">
              مسجد الحرام (مکہ مکرمہ) اور مسجد نبوی الشریف (مدینہ منورہ) سے تمام نمازیں، طواف اور تلاوتِ قرآن براہِ راست ایچ ڈی کوالٹی میں دیکھیں۔
            </p>
          </div>

          <Link
            to="/haramain-live"
            className="inline-flex items-center gap-2 px-6 py-3 rounded-2xl bg-gradient-to-r from-red-600 to-rose-700 hover:from-red-500 hover:to-rose-600 text-white font-urdu font-bold text-sm shadow-xl hover:shadow-red-900/40 transition-all shrink-0"
          >
            <Play className="w-4 h-4 fill-white" />
            <span>مکمل لائیو نشریات کھولیں</span>
            {isRtl ? <ArrowLeft className="w-4 h-4" /> : <ArrowRight className="w-4 h-4" />}
          </Link>
        </div>

        {/* 2 Stream Preview Cards (Makkah & Madinah) */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {/* Makkah Live Preview Card */}
          <Link
            to="/haramain-live?stream=makkah"
            className="group relative bg-slate-900/80 rounded-3xl border border-slate-800 hover:border-amber-400 p-6 shadow-xl transition-all duration-300 flex flex-col justify-between overflow-hidden"
          >
            <div className="flex items-center justify-between gap-3 mb-4">
              <div className="flex items-center gap-2">
                <span className="w-3 h-3 rounded-full bg-red-500 animate-ping" />
                <span className="text-xs font-black text-red-400 font-urdu uppercase tracking-wider">
                  مکہ لائیو • کعبۃ اللہ
                </span>
              </div>
              <span className="px-2.5 py-0.5 rounded-full text-[10px] font-bold bg-white/10 text-slate-300 font-mono">
                1080p HD
              </span>
            </div>

            <div className="space-y-2 mb-6">
              <h3 className="text-xl font-black text-white font-urdu group-hover:text-amber-300 transition-colors">
                مسجد الحرام، مکہ مکرمہ (Makkah Live)
              </h3>
              <p className="text-xs text-slate-300 font-urdu leading-relaxed">
                طوافِ کعبہ، نمازِ پنجگانہ، تہجد، اور خطباتِ حرمین شریفین کی براہِ راست نشریات مع اذان۔
              </p>
            </div>

            <div className="pt-4 border-t border-slate-800 flex items-center justify-between text-xs font-urdu text-amber-300 font-bold">
              <span>براہِ راست مکہ کیمرہ دیکھیں</span>
              <div className="w-8 h-8 rounded-xl bg-red-600/20 text-red-400 group-hover:bg-red-600 group-hover:text-white flex items-center justify-center transition-colors">
                <Play className="w-3.5 h-3.5 fill-current" />
              </div>
            </div>
          </Link>

          {/* Madinah Live Preview Card */}
          <Link
            to="/haramain-live?stream=madinah"
            className="group relative bg-slate-900/80 rounded-3xl border border-slate-800 hover:border-emerald-400 p-6 shadow-xl transition-all duration-300 flex flex-col justify-between overflow-hidden"
          >
            <div className="flex items-center justify-between gap-3 mb-4">
              <div className="flex items-center gap-2">
                <span className="w-3 h-3 rounded-full bg-emerald-500 animate-pulse" />
                <span className="text-xs font-black text-emerald-400 font-urdu uppercase tracking-wider">
                  مدینہ لائیو • روضۂ رسول ﷺ
                </span>
              </div>
              <span className="px-2.5 py-0.5 rounded-full text-[10px] font-bold bg-white/10 text-slate-300 font-mono">
                1080p HD
              </span>
            </div>

            <div className="space-y-2 mb-6">
              <h3 className="text-xl font-black text-white font-urdu group-hover:text-emerald-300 transition-colors">
                مسجد نبوی الشریف، مدینہ منورہ (Madinah Live)
              </h3>
              <p className="text-xs text-slate-300 font-urdu leading-relaxed">
                گنبدِ خضراء، روضۂ اقدس ﷺ اور ریاض الجنۃ سے براہِ راست روح پرور مناظر اور نمازیں۔
              </p>
            </div>

            <div className="pt-4 border-t border-slate-800 flex items-center justify-between text-xs font-urdu text-emerald-400 font-bold">
              <span>براہِ راست مدینہ کیمرہ دیکھیں</span>
              <div className="w-8 h-8 rounded-xl bg-emerald-600/20 text-emerald-400 group-hover:bg-emerald-600 group-hover:text-white flex items-center justify-center transition-colors">
                <Play className="w-3.5 h-3.5 fill-current" />
              </div>
            </div>
          </Link>
        </div>
      </div>
    </section>
  );
};
