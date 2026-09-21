import React, { useEffect } from 'react';
import { HaramainLivePlayer } from '../components/live/HaramainLivePlayer';
import { useLanguage } from '../context/LanguageContext';
import { Radio, Sparkles, Heart, ShieldCheck, Video, Clock } from 'lucide-react';
import { Link } from 'react-router-dom';

export const HaramainLivePage: React.FC = () => {
  const { isRtl, language } = useLanguage();

  useEffect(() => {
    document.title = 'حرمین شریفین ۲۴/۷ لائیو نشریات (مکہ مکرمہ و مدینہ منورہ) | بیت العلم AI';
    window.scrollTo(0, 0);
  }, []);

  return (
    <div className="min-h-screen bg-[#040806] text-slate-100 py-8 sm:py-12 px-4 sm:px-6 lg:px-8 space-y-8" dir={isRtl ? 'rtl' : 'ltr'}>
      {/* Breadcrumb Header */}
      <div className="max-w-6xl mx-auto flex items-center justify-between border-b border-[#d4af37]/20 pb-4">
        <div className="flex items-center gap-2 text-xs font-semibold text-slate-400">
          <Link to="/" className="hover:text-[#fae19c] transition-colors font-urdu">
            ہوم
          </Link>
          <span>/</span>
          <span className="text-[#fae19c] font-urdu">حرمین شریفین ۲۴/۷ لائیو نشریات</span>
        </div>

        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-[#d4af37]/15 border border-[#d4af37]/30 text-[#fae19c] text-xs font-bold font-urdu">
          <Radio className="w-3.5 h-3.5 text-red-400 animate-pulse" />
          <span>براہِ راست سیٹلائٹ فیڈ</span>
        </div>
      </div>

      {/* Main Haramain Live Player */}
      <div className="max-w-6xl mx-auto">
        <HaramainLivePlayer standalone={true} />
      </div>

      {/* Spiritual Guide & Features Cards */}
      <div className="max-w-6xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-5 pt-6">
        <div className="bg-gradient-to-br from-[#0c1410] to-[#060c09] p-5 rounded-2xl border border-[#d4af37]/20 shadow-md space-y-2 text-right">
          <div className="w-9 h-9 rounded-xl bg-[#d4af37]/15 text-[#fae19c] flex items-center justify-center font-bold">
            <Clock className="w-5 h-5" />
          </div>
          <h4 className="text-base font-bold text-[#fae19c] font-urdu">چوبیس گھنٹے براہِ راست نشریات</h4>
          <p className="text-xs text-slate-400 font-urdu leading-relaxed">
            مکہ مکرمہ اور مدینہ منورہ کے مقدس مقامات سے سال کے بارہ مہینے اور چوبیس گھنٹے بلا تعطل براہِ راست لائیو آڈیو اور ویڈیو سلسلہ۔
          </p>
        </div>

        <div className="bg-gradient-to-br from-[#0c1410] to-[#060c09] p-5 rounded-2xl border border-[#d4af37]/20 shadow-md space-y-2 text-right">
          <div className="w-9 h-9 rounded-xl bg-emerald-500/15 text-emerald-400 flex items-center justify-center font-bold">
            <ShieldCheck className="w-5 h-5" />
          </div>
          <h4 className="text-base font-bold text-[#fae19c] font-urdu">سرکاری و معتبر ذرائع</h4>
          <p className="text-xs text-slate-400 font-urdu leading-relaxed">
            سعودی عرب کی وزارتِ اطلاعات و نشریات کے آفیشل سیٹلائٹ چینلز (سعودی قرآن ٹی وی اور سعودی سنہ ٹی وی) کے تصدیق شدہ براڈکاسٹ سرورز۔
          </p>
        </div>

        <div className="bg-gradient-to-br from-[#0c1410] to-[#060c09] p-5 rounded-2xl border border-[#d4af37]/20 shadow-md space-y-2 text-right">
          <div className="w-9 h-9 rounded-xl bg-rose-500/15 text-rose-400 flex items-center justify-center font-bold">
            <Heart className="w-5 h-5" />
          </div>
          <h4 className="text-base font-bold text-[#fae19c] font-urdu">اذان و پنجوقتہ نمازیں</h4>
          <p className="text-xs text-slate-400 font-urdu leading-relaxed">
            مسجد حرام اور مسجد نبوی کی پرکیف روح پرور اذانیں، ائمہ کرام کی تلاوت میں باجماعت نمازیں، جمعہ کا خطبہ اور تراویح و تہجد کا لائیو مشاہدہ۔
          </p>
        </div>
      </div>
    </div>
  );
};
