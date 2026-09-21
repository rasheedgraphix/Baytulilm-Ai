import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import {
  Clock,
  Compass,
  MapPin,
  Calendar,
  Sparkles,
  BookOpen,
  ArrowRight,
  ShieldCheck,
  CheckCircle2,
  Building2,
  Sliders
} from 'lucide-react';
import { PrayerTimesWidget } from '../components/islamic/PrayerTimesWidget';
import { IslamicPatternBg } from '../components/layout/IslamicPatternBg';
import { useLanguage } from '../context/LanguageContext';
import { updatePageSEO } from '../utils/seo';

export const PrayerTimesPage: React.FC = () => {
  const { isRtl } = useLanguage();

  useEffect(() => {
    updatePageSEO({
      title: 'اوقاتِ نماز (جامعہ علوم اسلامیہ بنوری ٹاؤن کراچی - لائیو لوکیشن و تلاش برائے تمام شہر)',
      description:
        'مستند اوقاتِ نماز برائے کراچی، پاکستان اور دنیا بھر کے تمام شہر۔ جامعہ علوم اسلامیہ علامہ بنوری ٹاؤن کراچی کے مستند حسابی اصول، لائیو GPS لوکیشن، مسلک حنفی و شافعی، سحری، افطار، اشراق، چاشت اور تہجد کے درست اوقات۔',
      keywords: [
        'اوقات نماز کراچی',
        'جامعہ علوم اسلامیہ بنوری ٹاؤن کراچی نماز ٹائم',
        'نماز کے اوقات پاکستان',
        'University of Islamic Sciences Karachi prayer times',
        'Prayer times live location GPS',
        'Sehri and Iftar timing Karachi',
        'Fajr Dhuhr Asr Maghrib Isha time',
        'سحری و افطار اوقات',
        'حنفی نماز اوقات'
      ],
      canonicalUrl: 'https://bait-ul-ilm-web.ai.studio/prayer-times'
    });
    window.scrollTo(0, 0);
  }, []);

  return (
    <div className="pt-24 pb-20 space-y-10 min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100" dir="rtl">
      {/* Header Banner */}
      <section className="relative bg-gradient-to-b from-emerald-950 via-teal-950 to-slate-950 text-white py-12 sm:py-16 overflow-hidden shadow-xl border-b border-emerald-500/20">
        <IslamicPatternBg variant="hero" />
        
        {/* Glow */}
        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-96 h-96 bg-emerald-500/15 blur-[120px] rounded-full pointer-events-none" />

        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-4 text-center">
          {/* Breadcrumb Navigation */}
          <div className="flex items-center justify-center gap-2 text-xs text-emerald-300/80 font-urdu">
            <Link to="/" className="hover:text-white transition-colors">بیت العلم پورٹل</Link>
            <span>/</span>
            <span className="text-emerald-200 font-bold">اوقاتِ نماز (Prayer Times)</span>
          </div>

          <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-emerald-500/15 border border-emerald-400/40 text-emerald-300 text-xs sm:text-sm font-urdu font-bold shadow-inner">
            <Building2 className="w-4 h-4 text-emerald-400" />
            <span>معیارِ جامعہ علوم اسلامیہ علامہ بنوری ٹاؤن کراچی (University of Islamic Sciences, Karachi)</span>
          </div>

          <h1 className="text-3xl sm:text-4xl md:text-5xl font-black tracking-tight font-arabic text-transparent bg-clip-text bg-gradient-to-r from-emerald-100 via-amber-100 to-amber-300">
            اوقاتِ نماز و مسنون عبادات
          </h1>

          <p className="max-w-3xl mx-auto text-sm sm:text-base text-slate-300 font-urdu leading-relaxed">
            فجر، طلوعِ آفتاب، اشراق، چاشت، ظہر، عصر (حنفی و شافعی)، غروبِ آفتاب (افطار)، مغرب، عشاء اور تہجد کے مستند ترین اوقات۔ اپنے موجودہ مقام (GPS) یا کسی بھی شہر کے نام سے باآسانی معلوم کریں۔
          </p>
        </div>
      </section>

      {/* Main Interactive Widget Container */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-10">
        <PrayerTimesWidget />

        {/* Informational Cards & Jurisprudence Notes */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 font-urdu">
          
          <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-3xl p-6 shadow-sm space-y-3">
            <div className="flex items-center gap-2.5 text-emerald-600 dark:text-emerald-400 font-bold">
              <Building2 className="w-5 h-5" />
              <h3 className="text-base font-black">جامعہ علوم اسلامیہ کراچی کا ضابطہ</h3>
            </div>
            <p className="text-xs sm:text-sm text-slate-600 dark:text-slate-300 leading-relaxed">
              پاکستان اور برصغیر میں فجر اور عشاء کے وقت کے لیے افق پر سورج کا زاویہ <strong>۱۸ درجے (18.0°)</strong> زیرِ افق ہونا معتمد ہے۔ جامعہ علوم اسلامیہ بنوری ٹاؤن کراچی کا یہ معیار دار العلوم دیوبند اور کبار علمائے فلکیات کا متفقہ فیصلہ ہے۔
            </p>
          </div>

          <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-3xl p-6 shadow-sm space-y-3">
            <div className="flex items-center gap-2.5 text-amber-600 dark:text-amber-400 font-bold">
              <Sparkles className="w-5 h-5" />
              <h3 className="text-base font-black">عصر کا وقت (حنفی و جمہور)</h3>
            </div>
            <p className="text-xs sm:text-sm text-slate-600 dark:text-slate-300 leading-relaxed">
              فقہ حنفی میں عصر کا وقت سایہ اصلی کے علاوہ ہر چیز کا سایہ <strong>دو گنا (مثلین)</strong> ہونے پر شروع ہوتا ہے، جبکہ ائمہ ثلاثہ (شافعی، مالکی، حنبلی) کے ہاں سایہ <strong>ایک گنا (مثلِ اول)</strong> ہونے پر شروع ہوتا ہے۔ آپ اوپر دیے گئے بٹن سے دونوں کے مطابق وقت دیکھ سکتے ہیں۔
            </p>
          </div>

          <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-3xl p-6 shadow-sm space-y-3">
            <div className="flex items-center gap-2.5 text-sky-600 dark:text-sky-400 font-bold">
              <MapPin className="w-5 h-5" />
              <h3 className="text-base font-black">لائیو جی پی ایس و تلاش</h3>
            </div>
            <p className="text-xs sm:text-sm text-slate-600 dark:text-slate-300 leading-relaxed">
              اگر آپ سفر میں ہیں یا کسی مخصوص مقام پر ہیں تو <strong>"لائیو لوکیشن (GPS)"</strong> کے بٹن پر کلک کر کے سیکنڈوں میں اپنے خطِ عرض و طول کے عین مطابق سحری، افطار اور نماز کے اوقات دیکھ سکتے ہیں۔
            </p>
          </div>

        </div>
      </div>
    </div>
  );
};
