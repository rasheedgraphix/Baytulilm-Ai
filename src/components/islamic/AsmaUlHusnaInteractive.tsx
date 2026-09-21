import React, { useState, useEffect, useRef } from 'react';
import { Sparkles, Moon, RefreshCw, Search } from 'lucide-react';
import { useLanguage } from '../../context/LanguageContext';

export interface DivineName {
  no: number;
  arabic: string;
  translit: string;
  urdu: string;
}

export const ASMA_UL_HUSNA_DATA: DivineName[] = [
  { no: 1, arabic: 'الرَّحْمَنُ', translit: 'Ar-Rahman', urdu: 'بہت رحم کرنے والا' },
  { no: 2, arabic: 'الرَّحِيمُ', translit: 'Ar-Rahim', urdu: 'نہایت مہربان' },
  { no: 3, arabic: 'الْمَلِكُ', translit: 'Al-Malik', urdu: 'بادشاہ' },
  { no: 4, arabic: 'الْقُدُّوسُ', translit: 'Al-Quddus', urdu: 'نہایت پاک' },
  { no: 5, arabic: 'السَّلَامُ', translit: 'As-Salam', urdu: 'سلامتی دینے والا' },
  { no: 6, arabic: 'الْمُؤْمِنُ', translit: "Al-Mu'min", urdu: 'امن دینے والا' },
  { no: 7, arabic: 'الْمُهَيْمِنُ', translit: 'Al-Muhaymin', urdu: 'نگہبان' },
  { no: 8, arabic: 'الْعَزِيزُ', translit: 'Al-Aziz', urdu: 'غالب' },
  { no: 9, arabic: 'الْجَبَّارُ', translit: 'Al-Jabbar', urdu: 'زبردست' },
  { no: 10, arabic: 'الْمُتَكَبِّرُ', translit: 'Al-Mutakabbir', urdu: 'بڑائی والا' },
  { no: 11, arabic: 'الْخَالِقُ', translit: 'Al-Khaliq', urdu: 'پیدا کرنے والا' },
  { no: 12, arabic: 'الْبَارِئُ', translit: 'Al-Bari', urdu: 'بنانے والا' },
  { no: 13, arabic: 'الْمُصَوِّرُ', translit: 'Al-Musawwir', urdu: 'صورت بنانے والا' },
  { no: 14, arabic: 'الْغَفَّارُ', translit: 'Al-Ghaffar', urdu: 'بہت بخشنے والا' },
  { no: 15, arabic: 'الْقَهَّارُ', translit: 'Al-Qahhar', urdu: 'قہار' },
  { no: 16, arabic: 'الْوَهَّابُ', translit: 'Al-Wahhab', urdu: 'بہت عطا کرنے والا' },
  { no: 17, arabic: 'الرَّزَّاقُ', translit: 'Ar-Razzaq', urdu: 'رزق دینے والا' },
  { no: 18, arabic: 'الْفَتَّاحُ', translit: 'Al-Fattah', urdu: 'کھولنے والا' },
  { no: 19, arabic: 'الْعَلِيمُ', translit: 'Al-Alim', urdu: 'جاننے والا' },
  { no: 20, arabic: 'الْقَابِضُ', translit: 'Al-Qabid', urdu: 'تنگی کرنے والا' },
  { no: 21, arabic: 'الْبَاسِطُ', translit: 'Al-Basit', urdu: 'وسعت دینے والا' },
  { no: 22, arabic: 'الْخَافِضُ', translit: 'Al-Khafid', urdu: 'پست کرنے والا' },
  { no: 23, arabic: 'الرَّافِعُ', translit: 'Ar-Rafi', urdu: 'بلند کرنے والا' },
  { no: 24, arabic: 'الْمُعِزُّ', translit: "Al-Mu'izz", urdu: 'عزت دینے والا' },
  { no: 25, arabic: 'الْمُذِلُّ', translit: 'Al-Mudhill', urdu: 'ذلت دینے والا' },
  { no: 26, arabic: 'السَّمِيعُ', translit: 'As-Sami', urdu: 'سننے والا' },
  { no: 27, arabic: 'الْبَصِيرُ', translit: 'Al-Basir', urdu: 'دیکھنے والا' },
  { no: 28, arabic: 'الْحَكَمُ', translit: 'Al-Hakam', urdu: 'فیصلہ کرنے والا' },
  { no: 29, arabic: 'الْعَدْلُ', translit: 'Al-Adl', urdu: 'انصاف کرنے والا' },
  { no: 30, arabic: 'اللَّطِيفُ', translit: 'Al-Latif', urdu: 'مہربان' },
  { no: 31, arabic: 'الْخَبِيرُ', translit: 'Al-Khabir', urdu: 'خبردار' },
  { no: 32, arabic: 'الْحَلِيمُ', translit: 'Al-Halim', urdu: 'بردبار' },
  { no: 33, arabic: 'الْعَظِيمُ', translit: 'Al-Azim', urdu: 'بہت بڑا' },
  { no: 34, arabic: 'الْغَفُورُ', translit: 'Al-Ghafur', urdu: 'بخشنے والا' },
  { no: 35, arabic: 'الشَّكُورُ', translit: 'Ash-Shakur', urdu: 'قدردان' },
  { no: 36, arabic: 'الْعَلِيُّ', translit: 'Al-Ali', urdu: 'بہت بلند' },
  { no: 37, arabic: 'الْكَبِيرُ', translit: 'Al-Kabir', urdu: 'بہت بڑا' },
  { no: 38, arabic: 'الْحَفِيظُ', translit: 'Al-Hafiz', urdu: 'حفاظت کرنے والا' },
  { no: 39, arabic: 'الْمُقِيتُ', translit: 'Al-Muqit', urdu: 'روزی دینے والا' },
  { no: 40, arabic: 'الْحَسِيبُ', translit: 'Al-Hasib', urdu: 'حساب لینے والا' },
  { no: 41, arabic: 'الْجَلِيلُ', translit: 'Al-Jalil', urdu: 'بزرگی والا' },
  { no: 42, arabic: 'الْكَرِيمُ', translit: 'Al-Karim', urdu: 'کرم کرنے والا' },
  { no: 43, arabic: 'الرَّقِيبُ', translit: 'Ar-Raqib', urdu: 'نگہبان' },
  { no: 44, arabic: 'الْمُجِيبُ', translit: 'Al-Mujib', urdu: 'دعا قبول کرنے والا' },
  { no: 45, arabic: 'الْوَاسِعُ', translit: 'Al-Wasi', urdu: 'وسعت والا' },
  { no: 46, arabic: 'الْحَكِيمُ', translit: 'Al-Hakim', urdu: 'حکمت والا' },
  { no: 47, arabic: 'الْوَدُودُ', translit: 'Al-Wadud', urdu: 'محبت کرنے والا' },
  { no: 48, arabic: 'الْمَجِيدُ', translit: 'Al-Majid', urdu: 'بزرگی والا' },
  { no: 49, arabic: 'الْبَاعِثُ', translit: 'Al-Baith', urdu: 'اٹھانے والا' },
  { no: 50, arabic: 'الشَّهِيدُ', translit: 'Ash-Shahid', urdu: 'گواہ' },
  { no: 51, arabic: 'الْحَقُّ', translit: 'Al-Haqq', urdu: 'سچا' },
  { no: 52, arabic: 'الْوَكِيلُ', translit: 'Al-Wakil', urdu: 'کارساز' },
  { no: 53, arabic: 'الْقَوِيُّ', translit: 'Al-Qawiyy', urdu: 'طاقتور' },
  { no: 54, arabic: 'الْمَتِينُ', translit: 'Al-Matin', urdu: 'مضبوط' },
  { no: 55, arabic: 'الْوَلِيُّ', translit: 'Al-Waliyy', urdu: 'دوست' },
  { no: 56, arabic: 'الْحَمِيدُ', translit: 'Al-Hamid', urdu: 'تعریف کے لائق' },
  { no: 57, arabic: 'الْمُحْصِي', translit: 'Al-Muhsi', urdu: 'گننے والا' },
  { no: 58, arabic: 'الْمُبْدِئُ', translit: 'Al-Mubdi', urdu: 'پہلی بار پیدا کرنے والا' },
  { no: 59, arabic: 'الْمُعِيدُ', translit: "Al-Mu'id", urdu: 'دوبارہ پیدا کرنے والا' },
  { no: 60, arabic: 'الْمُحْيِي', translit: 'Al-Muhyi', urdu: 'زندہ کرنے والا' },
  { no: 61, arabic: 'الْمُمِيتُ', translit: 'Al-Mumit', urdu: 'موت دینے والا' },
  { no: 62, arabic: 'الْحَيُّ', translit: 'Al-Hayy', urdu: 'زندہ' },
  { no: 63, arabic: 'الْقَيُّومُ', translit: 'Al-Qayyum', urdu: 'قائم رہنے والا' },
  { no: 64, arabic: 'الْوَاجِدُ', translit: 'Al-Wajid', urdu: 'پانے والا' },
  { no: 65, arabic: 'الْمَاجِدُ', translit: 'Al-Majid', urdu: 'بزرگ' },
  { no: 66, arabic: 'الْوَاحِدُ', translit: 'Al-Wahid', urdu: 'ایک' },
  { no: 67, arabic: 'الْأَحَدُ', translit: 'Al-Ahad', urdu: 'یکتا' },
  { no: 68, arabic: 'الصَّمَدُ', translit: 'As-Samad', urdu: 'بے نیاز' },
  { no: 69, arabic: 'الْقَادِرُ', translit: 'Al-Qadir', urdu: 'قدرت والا' },
  { no: 70, arabic: 'الْمُقْتَدِرُ', translit: 'Al-Muqtadir', urdu: 'اقتدار والا' },
  { no: 71, arabic: 'الْمُقَدِّمُ', translit: 'Al-Muqaddim', urdu: 'آگے کرنے والا' },
  { no: 72, arabic: 'الْمُؤَخِّرُ', translit: "Al-Mu'akhkhir", urdu: 'پیچھے کرنے والا' },
  { no: 73, arabic: 'الْأَوَّلُ', translit: 'Al-Awwal', urdu: 'پہلا' },
  { no: 74, arabic: 'الْآخِرُ', translit: 'Al-Akhir', urdu: 'آخری' },
  { no: 75, arabic: 'الظَّاهِرُ', translit: 'Az-Zahir', urdu: 'ظاہر' },
  { no: 76, arabic: 'الْبَاطِنُ', translit: 'Al-Batin', urdu: 'پوشیدہ' },
  { no: 77, arabic: 'الْوَالِي', translit: 'Al-Wali', urdu: 'والی' },
  { no: 78, arabic: 'الْمُتَعَالِي', translit: "Al-Muta'ali", urdu: 'بلند' },
  { no: 79, arabic: 'الْبَرُّ', translit: 'Al-Barr', urdu: 'نیکی کرنے والا' },
  { no: 80, arabic: 'التَّوَّابُ', translit: 'At-Tawwab', urdu: 'توبہ قبول کرنے والا' },
  { no: 81, arabic: 'الْمُنْتَقِمُ', translit: 'Al-Muntaqim', urdu: 'انتقام لینے والا' },
  { no: 82, arabic: 'الْعَفُوُّ', translit: 'Al-Afuww', urdu: 'معاف کرنے والا' },
  { no: 83, arabic: 'الرَّؤُوفُ', translit: "Ar-Ra'uf", urdu: 'شفقت کرنے والا' },
  { no: 84, arabic: 'مَالِكُ الْمُلْكِ', translit: 'Malik-ul-Mulk', urdu: 'بادشاہوں کا بادشاہ' },
  { no: 85, arabic: 'ذُو الْجَلَالِ وَالْإِكْرَامِ', translit: 'Dhu-al-Jalal wal-Ikram', urdu: 'جلال و اکرام والا' },
  { no: 86, arabic: 'الْمُقْسِطُ', translit: 'Al-Muqsit', urdu: 'انصاف کرنے والا' },
  { no: 87, arabic: 'الْجَامِعُ', translit: 'Al-Jami', urdu: 'جمع کرنے والا' },
  { no: 88, arabic: 'الْغَنِيُّ', translit: 'Al-Ghaniyy', urdu: 'بے نیاز' },
  { no: 89, arabic: 'الْمُغْنِي', translit: 'Al-Mughni', urdu: 'بے نیاز کرنے والا' },
  { no: 90, arabic: 'الْمَانِعُ', translit: 'Al-Mani', urdu: 'روکنے والا' },
  { no: 91, arabic: 'الضَّارُّ', translit: 'Ad-Darr', urdu: 'نقصان پہنچانے والا (حکمت سے)' },
  { no: 92, arabic: 'النَّافِعُ', translit: 'An-Nafi', urdu: 'نفع دینے والا' },
  { no: 93, arabic: 'النُّورُ', translit: 'An-Nur', urdu: 'نور دینے والا' },
  { no: 94, arabic: 'الْهَادِي', translit: 'Al-Hadi', urdu: 'ہدایت دینے والا' },
  { no: 95, arabic: 'الْبَدِيعُ', translit: 'Al-Badi', urdu: 'ایجاد کرنے والا' },
  { no: 96, arabic: 'الْبَاقِي', translit: 'Al-Baqi', urdu: 'باقی رہنے والا' },
  { no: 97, arabic: 'الْوَارِثُ', translit: 'Al-Warith', urdu: 'وارث' },
  { no: 98, arabic: 'الرَّشِيدُ', translit: 'Ar-Rashid', urdu: 'ہدایت دینے والا' },
  { no: 99, arabic: 'الصَّبُورُ', translit: 'As-Sabur', urdu: 'صبر کرنے والا' },
];

const PALETTE = [
  '#FF6B6B', '#4ECDC4', '#45B7D1', '#FFA600',
  '#96CEB4', '#FFEAA7', '#DDA0DD', '#98D8C8',
  '#F7DC6F', '#BB8FCE', '#FF9A9E', '#A8E6CF'
];

const GRADIENTS = [
  'linear-gradient(90deg, #FF6B6B 0%, #FFA600 28%, #4ECDC4 62%, #45B7D1 85%, #BB8FCE 100%)',
  'linear-gradient(90deg, #FFD86F 0%, #FF9A9E 30%, #FF6B6B 60%, #DDA0DD 100%)',
  'linear-gradient(90deg, #4ECDC4 0%, #2ECC9A 25%, #FFEAA7 55%, #FFA600 85%)',
  'linear-gradient(90deg, #45B7D1 0%, #6C5CE7 35%, #FD79A8 70%, #FFA600 100%)',
  'linear-gradient(90deg, #00B894 0%, #55EFC4 30%, #FFEAA7 60%, #FDCB6E 100%)',
  'linear-gradient(90deg, #E17055 0%, #FFA600 25%, #FFEAA7 55%, #74B9FF 100%)',
  'linear-gradient(90deg, #A29BFE 0%, #81ECEC 30%, #FFEAA7 65%, #FF7675 100%)',
  'linear-gradient(90deg, #FF9A9E 0%, #FAD390 30%, #6AB8FF 65%, #A8E6CF 100%)',
  'linear-gradient(90deg, #F7DC6F 0%, #F39C12 25%, #FF6B6B 60%, #BB8FCE 100%)',
  'linear-gradient(90deg, #0984E3 0%, #4ECDC4 35%, #FFEAA7 68%, #FF6B6B 100%)',
  'linear-gradient(90deg, #DDA0DD 0%, #FF6B6B 30%, #FFA600 60%, #55EFC4 100%)',
  'linear-gradient(90deg, #FFEAA7 0%, #F7DC6F 15%, #4ECDC4 45%, #45B7D1 75%, #9B59B6 100%)'
];

interface GradientArabicProps {
  text: string;
  offset?: number;
  active?: boolean;
}

const GradientArabicText: React.FC<GradientArabicProps> = ({ text, offset = 0, active = false }) => {
  const grad = GRADIENTS[offset % GRADIENTS.length];
  return (
    <span
      dir="rtl"
      className="inline-block font-bold leading-[1.25] tracking-normal font-serif"
      style={{
        background: grad,
        WebkitBackgroundClip: 'text',
        backgroundClip: 'text',
        WebkitTextFillColor: 'transparent',
        color: 'transparent',
        filter: active
          ? 'brightness(1.22) drop-shadow(0 0 14px rgba(255,220,120,0.45)) drop-shadow(0 0 28px rgba(255,220,120,0.22))'
          : 'brightness(1.08) drop-shadow(0 1px 8px rgba(0,0,0,0.45))',
        transform: active ? 'scale(1.06)' : undefined,
        transition: 'all 0.45s cubic-bezier(.2,.8,.2,1)',
        fontFeatureSettings: '"liga" 1, "calt" 1',
        textRendering: 'optimizeLegibility'
      }}
    >
      {text}
    </span>
  );
};

export const AsmaUlHusnaInteractive: React.FC = () => {
  const { language } = useLanguage();
  const [activeNameNo, setActiveNameNo] = useState<number | null>(null);
  const [tasbeehCount, setTasbeehCount] = useState<number>(0);
  const [isAutoZikr, setIsAutoZikr] = useState<boolean>(false);
  const [currentZikrIndex, setCurrentZikrIndex] = useState<number>(0);
  const [searchQuery, setSearchQuery] = useState<string>('');
  const intervalRef = useRef<number | null>(null);

  const filteredNames = ASMA_UL_HUSNA_DATA.filter((item) => {
    const q = searchQuery.trim().toLowerCase();
    if (!q) return true;
    return (
      item.urdu.includes(q) ||
      item.translit.toLowerCase().includes(q) ||
      item.arabic.includes(q)
    );
  });

  useEffect(() => {
    if (isAutoZikr) {
      intervalRef.current = window.setInterval(() => {
        setCurrentZikrIndex((prev) => (prev + 1) % 99);
        setTasbeehCount((prev) => prev + 1);
      }, 1400);
    } else if (intervalRef.current) {
      clearInterval(intervalRef.current);
    }
    return () => {
      if (intervalRef.current) clearInterval(intervalRef.current);
    };
  }, [isAutoZikr]);

  return (
    <div className="relative rounded-3xl overflow-hidden bg-[#08070b] text-white border border-[#d4af37]/30 shadow-2xl p-4 sm:p-7 space-y-7 selection:bg-[#d4af37]/30">
      {/* Background ambient lighting */}
      <div className="pointer-events-none absolute inset-0 overflow-hidden">
        <div className="absolute top-0 left-1/2 -translate-x-1/2 w-full max-w-[1000px] h-[600px] bg-[radial-gradient(ellipse_at_center,_rgba(212,175,55,0.16),_rgba(120,80,20,0.08)_35%,_transparent_70%)] blur-[0.5px]" />
        <div className="absolute bottom-0 right-0 w-full max-w-[500px] h-[500px] bg-[radial-gradient(ellipse_at_center,_rgba(78,205,196,0.12),_transparent_65%)]" />
        <div className="absolute top-1/3 left-0 w-full max-w-[400px] h-[400px] bg-[radial-gradient(ellipse_at_center,_rgba(255,107,107,0.08),_transparent_65%)]" />
      </div>

      {/* Header Banner */}
      <div className="relative z-10 text-center space-y-4">
        <div className="flex justify-center">
          <div className="relative px-8 py-2.5 rounded-full bg-gradient-to-r from-white/[0.08] to-white/[0.02] border border-[#d4af37]/30 backdrop-blur-xl shadow-[0_0_24px_rgba(212,175,55,0.2)]">
            <p className="font-serif text-[20px] sm:text-[24px] tracking-wide text-[#f5e6b8]" dir="rtl">
              بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
            </p>
          </div>
        </div>

        <div className="space-y-2">
          <h2 className="text-3xl sm:text-5xl font-bold tracking-tight">
            <span className="block bg-gradient-to-r from-[#FFD86F] via-[#FFEAA7] to-[#D4AF37] bg-clip-text text-transparent drop-shadow-[0_2px_18px_rgba(212,175,55,0.4)]">
              اسماء الحسنیٰ
            </span>
            <span className="block mt-1 text-sm sm:text-base font-normal tracking-[0.24em] text-white/60 uppercase">
              Asma ul Husna • The 99 Beautiful Names of Allah
            </span>
            <span className="block mt-2 text-xl sm:text-2xl font-bold bg-gradient-to-r from-[#4ECDC4] via-[#FFEAA7] to-[#FF6B6B] bg-clip-text text-transparent font-urdu">
              اللہ تعالیٰ کے ۹۹ مبارک نام مع اردو معانی
            </span>
          </h2>
          <p className="text-sm sm:text-base text-white/70 font-urdu max-w-2xl mx-auto leading-relaxed" dir="rtl">
            ہر اسمِ مبارک مکمل جڑا ہوا، خوبصورت رنگین گریڈینٹ میں، تسبیح کاؤنٹر اور خودکار ذکر کے ساتھ۔
          </p>
        </div>

        {/* Controls: Tasbeeh Counter, Auto Zikr, Search */}
        <div className="flex flex-wrap justify-center items-center gap-3 pt-2">
          {/* Tasbeeh Counter */}
          <div className="flex items-center gap-3 px-4 py-2.5 rounded-full bg-white/[0.06] border border-white/10 backdrop-blur-xl shadow-md">
            <div className="w-9 h-9 rounded-full bg-gradient-to-br from-[#d4af37] to-[#8a6a18] flex items-center justify-center shadow-[0_0_18px_rgba(212,175,55,0.45)]">
              <span className="text-xs font-bold text-black font-serif">ﷻ</span>
            </div>
            <div className="text-left">
              <div className="text-[10px] tracking-[0.2em] text-white/50 uppercase">Tasbeeh</div>
              <div className="font-bold text-base sm:text-lg leading-none tabular-nums text-amber-300">
                {tasbeehCount.toString().padStart(3, '0')}
              </div>
            </div>
            <button
              onClick={() => setTasbeehCount(0)}
              className="ml-1 text-[10px] px-2.5 py-1 rounded-full bg-white/10 hover:bg-white/20 transition font-urdu"
              title="Reset counter"
            >
              صفر
            </button>
          </div>

          {/* Auto Zikr Toggle */}
          <button
            onClick={() => setIsAutoZikr(!isAutoZikr)}
            className={`group flex items-center gap-2.5 px-5 py-2.5 rounded-full border backdrop-blur-xl transition-all cursor-pointer ${
              isAutoZikr
                ? 'bg-[#d4af37] border-[#d4af37] text-black shadow-[0_0_24px_rgba(212,175,55,0.5)] font-bold'
                : 'bg-white/[0.06] border-white/10 hover:border-[#d4af37]/40 hover:bg-white/[0.08] text-white/90'
            }`}
          >
            <span
              className={`w-2 h-2 rounded-full ${
                isAutoZikr ? 'bg-black animate-pulse' : 'bg-[#4ECDC4] shadow-[0_0_10px_#4ECDC4]'
              }`}
            />
            <span className="text-xs sm:text-sm tracking-wide font-urdu">
              {isAutoZikr ? 'روک دیں — Pause Zikr' : 'ذکر شروع کریں — Auto Zikr'}
            </span>
            <span className={`text-xs transition-transform ${isAutoZikr ? '' : 'group-hover:translate-x-0.5'}`}>
              ✦
            </span>
          </button>

          {/* Search Box */}
          <div className="flex items-center gap-2 px-3 py-2 rounded-full bg-white/[0.04] border border-white/10">
            <input
              id="asma-ul-husna-zikr-filter"
              name="asmaUlHusnaFilter"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="تلاش — نام یا معنی..."
              className="bg-transparent outline-none text-xs sm:text-sm font-urdu placeholder:text-white/40 w-36 sm:w-48 text-right"
              dir="rtl"
            />
            <Search className="w-4 h-4 text-white/50" />
          </div>
        </div>

        {/* Audio / Rhythm Visualizer Bars */}
        <div className="flex justify-center gap-[3px] h-7 items-end pt-1">
          {Array.from({ length: 30 }).map((_, idx) => (
            <div
              key={idx}
              className="w-[3px] rounded-full transition-all duration-300"
              style={{
                height: isAutoZikr
                  ? `${10 + Math.sin((currentZikrIndex * 0.9 + idx) * 1.2) * 8 + Math.random() * 12}px`
                  : '4px',
                background: PALETTE[idx % PALETTE.length],
                opacity: isAutoZikr ? 0.9 : 0.25,
                boxShadow: isAutoZikr ? `0 0 10px ${PALETTE[idx % PALETTE.length]}` : 'none'
              }}
            />
          ))}
        </div>
      </div>

      {/* Grid of 99 Names */}
      <div className="relative z-10 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4 auto-rows-fr">
        {filteredNames.map((item, idx) => {
          const isZikrActive = isAutoZikr && idx === currentZikrIndex;
          const isExpanded = activeNameNo === item.no;

          return (
            <div
              key={item.no}
              onClick={() => {
                setActiveNameNo(activeNameNo === item.no ? null : item.no);
                setTasbeehCount((c) => c + 1);
                setCurrentZikrIndex(idx);
              }}
              className={`group relative rounded-[22px] p-[1px] cursor-pointer transition-all duration-500 ${
                isZikrActive ? 'scale-[1.02] z-10' : ''
              } ${isExpanded ? 'z-20' : ''}`}
              style={{
                background: isZikrActive
                  ? `linear-gradient(135deg, ${PALETTE[idx % PALETTE.length]}, #FFD86F, ${
                      PALETTE[(idx + 3) % PALETTE.length]
                    })`
                  : isExpanded
                  ? 'linear-gradient(135deg, #d4af37, #FFEAA7, #d4af37)'
                  : 'linear-gradient(135deg, rgba(255,255,255,0.12), rgba(255,255,255,0.04))'
              }}
            >
              <div
                className={`relative h-full rounded-[21px] overflow-hidden transition-all duration-500 bg-gradient-to-b from-[#15131c] to-[#0e0d13] backdrop-blur-xl p-5 flex flex-col items-center justify-between text-center min-h-[210px] ${
                  isZikrActive
                    ? 'shadow-[0_0_40px_rgba(212,175,55,0.35),0_0_80px_rgba(78,205,196,0.15)]'
                    : 'shadow-[0_8px_32px_rgba(0,0,0,0.45)] group-hover:shadow-[0_12px_48px_rgba(0,0,0,0.6),0_0_32px_rgba(212,175,55,0.15)]'
                } group-hover:-translate-y-[2px]`}
              >
                {/* Top Subtle Border */}
                <div className="absolute top-0 inset-x-0 h-[1px] bg-gradient-to-r from-transparent via-white/20 to-transparent" />

                {/* Number Badge & Indicators */}
                <div className="w-full flex items-center justify-between">
                  <div className="w-8 h-8 rounded-full bg-black/40 border border-white/10 grid place-items-center backdrop-blur">
                    <span className="text-[11px] font-bold tracking-widest text-[#FFEAA7]">
                      {String(item.no).padStart(2, '0')}
                    </span>
                  </div>
                  {isZikrActive && <div className="w-2 h-2 rounded-full bg-[#4ECDC4] animate-ping" />}
                  <span className="text-[11px] text-amber-400/70 font-serif">ﷻ</span>
                </div>

                {/* Arabic Calligraphy in Color Gradient */}
                <div className="py-2 text-[36px] sm:text-[42px] leading-[1.25] flex items-center justify-center min-h-[64px]">
                  <GradientArabicText text={item.arabic} offset={idx * 2 + item.no} active={isZikrActive || isExpanded} />
                </div>

                {/* Divider */}
                <div className="w-full h-[1px] bg-gradient-to-r from-transparent via-white/10 to-transparent my-1" />

                {/* Transliteration and Urdu Meaning */}
                <div className="space-y-1 w-full">
                  <div className="text-xs tracking-[0.18em] uppercase font-semibold text-white/70">
                    {item.translit}
                  </div>
                  <div className="font-urdu text-base text-[#f7e7b5] drop-shadow-[0_1px_10px_rgba(212,175,55,0.25)]">
                    {item.urdu}
                  </div>
                </div>

                {/* Expandable Detail */}
                {isExpanded && (
                  <div className="w-full mt-3 pt-2.5 px-3 py-2 rounded-xl bg-white/[0.04] border border-white/10 text-xs leading-5 text-white/75 font-urdu text-right" dir="rtl">
                    <span className="text-[#d4af37] font-bold">✦ </span>
                    یہ مبارک نام اللہ تعالیٰ کی صفت <b className="text-amber-200">"{item.urdu}"</b> کو ظاہر کرتا ہے۔ اس کا ورد دل کو اطمینان اور برکت عطا کرتا ہے۔
                  </div>
                )}

                {/* Bottom Color Accent */}
                <div className="absolute bottom-0 inset-x-0 h-[3px] flex">
                  {Array.from({ length: 6 }).map((_, f) => (
                    <div
                      key={f}
                      className="flex-1"
                      style={{
                        background: PALETTE[(idx + f) % PALETTE.length],
                        opacity: isZikrActive || isExpanded ? 1 : 0.45
                      }}
                    />
                  ))}
                </div>
              </div>
            </div>
          );
        })}
      </div>

      {filteredNames.length === 0 && (
        <div className="text-center py-16 font-urdu text-white/50 text-base">
          کوئی نام نہیں ملا — براہِ کرم تلاش بدل کر دیکھیں۔
        </div>
      )}

      {/* Footer Verse */}
      <div className="relative z-10 pt-4 border-t border-white/10 flex flex-col sm:flex-row items-center justify-between gap-3 text-xs text-white/60 font-urdu" dir="rtl">
        <p className="text-center sm:text-right leading-relaxed">
          وَلِلَّهِ الْأَسْمَاءُ الْحُسْنَىٰ فَادْعُوهُ بِهَا — اور اللہ ہی کے لیے بہترین نام ہیں، سو تم اسے انہی ناموں سے پکارو۔ <span className="text-amber-300/80">(سورۃ الاعراف: ۱۸۰)</span>
        </p>
        <span className="text-[11px] text-white/40 tracking-wider font-sans uppercase">
          Baytul Ilm AI • 99 Holy Names
        </span>
      </div>
    </div>
  );
};
