import React, { useState, useEffect, useRef } from 'react';
import { Sparkles, Heart, Search } from 'lucide-react';
import { useLanguage } from '../../context/LanguageContext';

export interface ProphetName {
  no: number;
  arabic: string;
  translit: string;
  urdu: string;
}

export const ASMA_UN_NABI_DATA: ProphetName[] = [
  { no: 1, arabic: 'مُحَمَّدٌ', translit: 'Muhammad', urdu: 'بہت تعریف کیا گیا' },
  { no: 2, arabic: 'أَحْمَدُ', translit: 'Ahmad', urdu: 'بہت حمد کرنے والا' },
  { no: 3, arabic: 'حَامِدٌ', translit: 'Hamid', urdu: 'تعریف کرنے والا' },
  { no: 4, arabic: 'مَحْمُودٌ', translit: 'Mahmood', urdu: 'تعریف کیا گیا' },
  { no: 5, arabic: 'قَاسِمٌ', translit: 'Qasim', urdu: 'تقسیم کرنے والا' },
  { no: 6, arabic: 'عَاقِبٌ', translit: 'Aqib', urdu: 'آخری آنے والا' },
  { no: 7, arabic: 'فَاتِحٌ', translit: 'Fatih', urdu: 'کھولنے والا' },
  { no: 8, arabic: 'خَاتِمٌ', translit: 'Khatim', urdu: 'ختم کرنے والا' },
  { no: 9, arabic: 'حَاشِرٌ', translit: 'Hashir', urdu: 'جمع کرنے والا' },
  { no: 10, arabic: 'مَاحِي', translit: 'Mahi', urdu: 'مٹانے والا کفر کو' },
  { no: 11, arabic: 'دَاعِي', translit: "Da'i", urdu: 'ہدایت کی دعوت دینے والا' },
  { no: 12, arabic: 'سِرَاجٌ', translit: 'Siraj', urdu: 'چراغ' },
  { no: 13, arabic: 'مُنِيرٌ', translit: 'Munir', urdu: 'روشن کرنے والا' },
  { no: 14, arabic: 'رَشِيدٌ', translit: 'Rasheed', urdu: 'ہدایت یافتہ' },
  { no: 15, arabic: 'رَسُولٌ', translit: 'Rasool', urdu: 'بھیجا گیا پیامبر' },
  { no: 16, arabic: 'نَبِيٌّ', translit: 'Nabi', urdu: 'غیب کی خبر دینے والا' },
  { no: 17, arabic: 'هَادِي', translit: 'Hadi', urdu: 'ہدایت دینے والا' },
  { no: 18, arabic: 'مَهْدِيٌّ', translit: 'Mahdi', urdu: 'ہدایت یافتہ' },
  { no: 19, arabic: 'بَشِيرٌ', translit: 'Bashir', urdu: 'خوشخبری دینے والا' },
  { no: 20, arabic: 'نَذِيرٌ', translit: 'Nazeer', urdu: 'ڈرانے والا' },
  { no: 21, arabic: 'رَؤُوفٌ', translit: "Ra'uf", urdu: 'نہایت نرم دل' },
  { no: 22, arabic: 'رَحِيمٌ', translit: 'Raheem', urdu: 'نہایت مہربان' },
  { no: 23, arabic: 'مُجْتَبَى', translit: 'Mujtaba', urdu: 'چنا ہوا' },
  { no: 24, arabic: 'مُصْطَفَى', translit: 'Mustafa', urdu: 'منتخب و برگزیدہ' },
  { no: 25, arabic: 'مُرْتَضَى', translit: 'Murtada', urdu: 'پسندیدہ' },
  { no: 26, arabic: 'الصَّادِقُ', translit: 'As-Sadiq', urdu: 'سچا' },
  { no: 27, arabic: 'الأَمِينُ', translit: 'Al-Amin', urdu: 'امانتدار' },
  { no: 28, arabic: 'الْمُصَدَّقُ', translit: 'Al-Musaddaq', urdu: 'تصدیق کیا گیا' },
  { no: 29, arabic: 'حَبِيبُ اللهِ', translit: 'Habibullah', urdu: 'اللہ کا محبوب' },
  { no: 30, arabic: 'صَفِيُّ اللهِ', translit: 'Safiullah', urdu: 'اللہ کا چنا ہوا' },
  { no: 31, arabic: 'نَجِيُّ اللهِ', translit: 'Najiullah', urdu: 'اللہ کا رازدار' },
  { no: 32, arabic: 'كَلِيمُ اللهِ', translit: 'Kalimullah', urdu: 'اللہ کا مقرب و کلام کرنے والا' },
  { no: 33, arabic: 'خَاتَمُ النَّبِيِّينَ', translit: 'Khatam-un-Nabiyyin', urdu: 'نبیوں کے خاتم ﷺ' },
  { no: 34, arabic: 'سَيِّدُ الْمُرْسَلِينَ', translit: 'Sayyid-ul-Mursalin', urdu: 'رسولوں کے سردار' },
  { no: 35, arabic: 'إِمَامُ الْمُتَّقِينَ', translit: 'Imam-ul-Muttaqin', urdu: 'متقیوں کے امام' },
  { no: 36, arabic: 'قَائِدُ الْغُرِّ الْمُحَجَّلِينَ', translit: 'Qaid-ul-Ghurril Muhajjalin', urdu: 'نورانی اعضاء والوں کے قائد' },
  { no: 37, arabic: 'شَفِيعٌ', translit: 'Shafi', urdu: 'شفاعت فرمانے والا' },
  { no: 38, arabic: 'مُشَفَّعٌ', translit: 'Mushaffa', urdu: 'جس کی شفاعت مقبول ہو' },
  { no: 39, arabic: 'مُصْلِحٌ', translit: 'Muslih', urdu: 'اصلاح کرنے والا' },
  { no: 40, arabic: 'مُهَيْمِنٌ', translit: 'Muhaymin', urdu: 'نگہبان' },
  { no: 41, arabic: 'صَادِقٌ', translit: 'Sadiq', urdu: 'راست باز' },
  { no: 42, arabic: 'مَصْدُوقٌ', translit: 'Masduq', urdu: 'تصدیق شدہ' },
  { no: 43, arabic: 'بُرْهَانٌ', translit: 'Burhan', urdu: 'کھلی دلیل' },
  { no: 44, arabic: 'حُجَّةُ اللهِ', translit: 'Hujjatullah', urdu: 'اللہ کی حجت' },
  { no: 45, arabic: 'صَاحِبُ الْحُجَّةِ', translit: 'Sahib-ul-Hujja', urdu: 'حجت و دلیل والے' },
  { no: 46, arabic: 'مُطِيعٌ', translit: 'Muti', urdu: 'فرمانبردار' },
  { no: 47, arabic: 'مُطَاعٌ', translit: 'Muta', urdu: 'جس کی اطاعت فرض ہے' },
  { no: 48, arabic: 'مَحْمُودُ الْخِصَالِ', translit: 'Mahmood-ul-Khisal', urdu: 'پسندیدہ صفات والے' },
  { no: 49, arabic: 'حَامِدُ الرَّحْمَنِ', translit: 'Hamid-ur-Rahman', urdu: 'رحمن کی حمد کرنے والے' },
  { no: 50, arabic: 'مَحْمُودٌ عِنْدَ اللهِ', translit: 'Mahmud Indallah', urdu: 'اللہ کے ہاں پسندیدہ' },
  { no: 51, arabic: 'نُورٌ', translit: 'Noor', urdu: 'سراپا نور' },
  { no: 52, arabic: 'مِصْبَاحٌ', translit: 'Misbah', urdu: 'چراغِ ہدایت' },
  { no: 53, arabic: 'دُرٌّ', translit: 'Dur', urdu: 'بے بہا موتی' },
  { no: 54, arabic: 'مُدَّثِّرٌ', translit: 'Muddathir', urdu: 'چادر اوڑھنے والے' },
  { no: 55, arabic: 'مُزَّمِّلٌ', translit: 'Muzzammil', urdu: 'کملی والے' },
  { no: 56, arabic: 'طَاهِرٌ', translit: 'Tahir', urdu: 'پاک و صاف' },
  { no: 57, arabic: 'مُطَهَّرٌ', translit: 'Mutahhar', urdu: 'پاک کیا گیا' },
  { no: 58, arabic: 'طَيِّبٌ', translit: 'Tayyib', urdu: 'پاکیزہ' },
  { no: 59, arabic: 'مُطَيَّبٌ', translit: 'Mutayyab', urdu: 'معطر و خوشبودار' },
  { no: 60, arabic: 'سَيِّدٌ', translit: 'Sayyid', urdu: 'سردارِ دو عالم' },
  { no: 61, arabic: 'إِمَامٌ', translit: 'Imam', urdu: 'پیشوا' },
  { no: 62, arabic: 'عَبْدُ اللهِ', translit: 'Abdullah', urdu: 'اللہ کا خاص بندہ' },
  { no: 63, arabic: 'حَبِيبٌ', translit: 'Habib', urdu: 'محبوب' },
  { no: 64, arabic: 'خَلِيلٌ', translit: 'Khalil', urdu: 'خاص دوست' },
  { no: 65, arabic: 'بَرٌّ', translit: 'Bar', urdu: 'نیک و خیر خواہ' },
  { no: 66, arabic: 'بَارٌّ', translit: 'Barr', urdu: 'احسان کرنے والا' },
  { no: 67, arabic: 'وَجِيهٌ', translit: 'Wajih', urdu: 'باعزت و وجاہت والے' },
  { no: 68, arabic: 'نَاصِحٌ', translit: 'Nasih', urdu: 'نصیحت فرمانے والے' },
  { no: 69, arabic: 'نَصِيحٌ', translit: 'Nasih (Khair Khwah)', urdu: 'بے پناہ خیر خواہ' },
  { no: 70, arabic: 'وَكِيلٌ', translit: 'Wakil', urdu: 'کارساز' },
  { no: 71, arabic: 'مُتَوَكِّلٌ', translit: 'Mutawakkil', urdu: 'اللہ پر توکل کرنے والے' },
  { no: 72, arabic: 'كَفِيلٌ', translit: 'Kafil', urdu: 'ضامن' },
  { no: 73, arabic: 'شَافِي', translit: 'Shafi (Shifa)', urdu: 'شفا کا وسیلہ (باذن اللہ)' },
  { no: 74, arabic: 'صَالِحٌ', translit: 'Salih', urdu: 'نیک و پارسا' },
  { no: 75, arabic: 'مُصْلِحُ الْقُلُوبِ', translit: 'Muslih-ul-Qulub', urdu: 'دلوں کی اصلاح فرمانے والے' },
  { no: 76, arabic: 'مُهَيْمِنُ الْأُمَّةِ', translit: 'Muhaymin-ul-Ummah', urdu: 'امت کے نگہبان' },
  { no: 77, arabic: 'قَيِّمٌ', translit: 'Qayyim', urdu: 'دین کو قائم رکھنے والے' },
  { no: 78, arabic: 'حَافِظٌ', translit: 'Hafiz', urdu: 'حفاظت فرمانے والے' },
  { no: 79, arabic: 'مَحْفُوظٌ', translit: 'Mahfuz', urdu: 'محفوظ' },
  { no: 80, arabic: 'صَادِقُ الْوَعْدِ', translit: 'Sadiq-ul-Wa\'d', urdu: 'وعدے کے سچے' },
  { no: 81, arabic: 'أَمِينُ الْوَحْيِ', translit: 'Amin-ul-Wahy', urdu: 'وحی کے امانتدار' },
  { no: 82, arabic: 'صَادِقُ الْقَوْلِ', translit: 'Sadiq-ul-Qawl', urdu: 'بات کے سچے' },
  { no: 83, arabic: 'مَصْدُوقُ اللِّسَانِ', translit: 'Masduq-ul-Lisan', urdu: 'حق گو زبان والے' },
  { no: 84, arabic: 'أَوَّلُ شَافِعٍ', translit: 'Awwal Shafi', urdu: 'پہلے شفاعت فرمانے والے' },
  { no: 85, arabic: 'آخِرُ الْأَنْبِيَاءِ', translit: 'Aakhir-ul-Anbiya', urdu: 'آخری نبی ﷺ' },
  { no: 86, arabic: 'ظَاهِرُ الْحَقِّ', translit: 'Zahir-ul-Haqq', urdu: 'حق کو غالب کرنے والے' },
  { no: 87, arabic: 'بَاطِنُ الْعِلْمِ', translit: 'Batin-ul-Ilm', urdu: 'اسرارِ الٰہی کے حامل' },
  { no: 88, arabic: 'حَفِيٌّ', translit: 'Hafi', urdu: 'نہایت شفیق و مہربان' },
  { no: 89, arabic: 'عَفُوٌّ', translit: 'Afu', urdu: 'درگزر فرمانے والے' },
  { no: 90, arabic: 'قَرِيبٌ', translit: 'Qarib', urdu: 'مومنوں کے قریب' },
  { no: 91, arabic: 'وَاصِلٌ', translit: 'Wasil', urdu: 'اللہ سے ملانے والے' },
  { no: 92, arabic: 'حَبِيبُ الْقُلُوبِ', translit: 'Habib-ul-Qulub', urdu: 'دلوں کے محبوب' },
  { no: 93, arabic: 'مُنِيبٌ', translit: 'Munib', urdu: 'اللہ کی طرف رجوع کرنے والے' },
  { no: 94, arabic: 'مُبَشِّرٌ', translit: 'Mubashir', urdu: 'جنت کی بشارت دینے والے' },
  { no: 95, arabic: 'مُبَلِّغٌ', translit: 'Muballigh', urdu: 'پیغام پہنچانے والے' },
  { no: 96, arabic: 'مُتَذَكِّرٌ', translit: 'Mutazakkir', urdu: 'نصیحت قبول فرمانے والے' },
  { no: 97, arabic: 'مُذَكِّرٌ', translit: 'Muzakkir', urdu: 'یاد دہانی کرانے والے' },
  { no: 98, arabic: 'مُكَرَّمٌ', translit: 'Mukarram', urdu: 'باعزت و مکرم' },
  { no: 99, arabic: 'رَحْمَةٌ لِلْعَالَمِينَ', translit: 'Rahmat-ul-lil-Alameen', urdu: 'تمام جہانوں کے لیے رحمت' },
];

const EMERALD_PALETTE = [
  '#22c55e', '#d4af37', '#10b981', '#facc15',
  '#16a34a', '#fde68a', '#059669', '#fbbf24',
  '#34d399', '#eab308', '#4ade80', '#fef08a'
];

const PROPHET_GRADIENTS = [
  'linear-gradient(90deg, #16a34a 0%, #22c55e 18%, #d4af37 52%, #fde68a 88%)',
  'linear-gradient(90deg, #059669 0%, #10b981 28%, #facc15 58%, #d4af37 100%)',
  'linear-gradient(90deg, #22c55e 0%, #4ade80 22%, #fef08a 48%, #eab308 82%)',
  'linear-gradient(90deg, #047857 0%, #34d399 32%, #fde68a 66%, #fbbf24 100%)',
  'linear-gradient(90deg, #d4af37 0%, #facc15 18%, #10b981 48%, #065f46 100%)',
  'linear-gradient(90deg, #065f46 0%, #059669 25%, #facc15 60%, #fef08a 100%)',
  'linear-gradient(90deg, #16a34a 0%, #a3e635 22%, #fde68a 55%, #d4af37 100%)',
  'linear-gradient(90deg, #fbbf24 0%, #d4af37 20%, #16a34a 55%, #0f766e 100%)',
  'linear-gradient(90deg, #10b981 0%, #6ee7b7 18%, #fef9c3 42%, #eab308 78%)',
  'linear-gradient(90deg, #14532d 0%, #22c55e 35%, #fde68a 68%, #facc15 100%)',
  'linear-gradient(90deg, #f59e0b 0%, #fde68a 25%, #22c55e 60%, #047857 100%)',
  'linear-gradient(90deg, #bbf7d0 0%, #4ade80 20%, #d4af37 50%, #92400e 100%)'
];

interface GradientArabicProps {
  text: string;
  offset?: number;
  active?: boolean;
}

const GradientProphetText: React.FC<GradientArabicProps> = ({ text, offset = 0, active = false }) => {
  const grad = PROPHET_GRADIENTS[offset % PROPHET_GRADIENTS.length];
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
          ? 'brightness(1.22) drop-shadow(0 0 14px rgba(212,175,55,0.45)) drop-shadow(0 0 28px rgba(34,197,94,0.22))'
          : 'brightness(1.06) drop-shadow(0 1px 10px rgba(0,0,0,0.45))',
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

export const AsmaUnNabiInteractive: React.FC = () => {
  const { language } = useLanguage();
  const [activeNameNo, setActiveNameNo] = useState<number | null>(null);
  const [duroodCount, setDuroodCount] = useState<number>(0);
  const [isAutoDurood, setIsAutoDurood] = useState<boolean>(false);
  const [currentIndex, setCurrentIndex] = useState<number>(0);
  const [searchQuery, setSearchQuery] = useState<string>('');
  const intervalRef = useRef<number | null>(null);

  const filteredNames = ASMA_UN_NABI_DATA.filter((item) => {
    const q = searchQuery.trim().toLowerCase();
    if (!q) return true;
    return (
      item.urdu.includes(q) ||
      item.translit.toLowerCase().includes(q) ||
      item.arabic.includes(q)
    );
  });

  useEffect(() => {
    if (isAutoDurood) {
      intervalRef.current = window.setInterval(() => {
        setCurrentIndex((prev) => (prev + 1) % 99);
        setDuroodCount((prev) => prev + 1);
      }, 1450);
    } else if (intervalRef.current) {
      clearInterval(intervalRef.current);
    }
    return () => {
      if (intervalRef.current) clearInterval(intervalRef.current);
    };
  }, [isAutoDurood]);

  return (
    <div className="relative rounded-3xl overflow-hidden bg-[#050a06] text-white border border-[#22c55e]/30 shadow-2xl p-4 sm:p-7 space-y-7 selection:bg-[#22c55e]/30">
      {/* Background ambient lighting */}
      <div className="pointer-events-none absolute inset-0 overflow-hidden">
        <div className="absolute top-0 left-1/2 -translate-x-1/2 w-full max-w-[1000px] h-[600px] bg-[radial-gradient(ellipse_at_center,_rgba(212,175,55,0.18),_rgba(34,197,94,0.14)_35%,_transparent_70%)] blur-[0.5px]" />
        <div className="absolute bottom-0 right-0 w-full max-w-[500px] h-[500px] bg-[radial-gradient(ellipse_at_center,_rgba(34,197,94,0.18),_transparent_65%)]" />
        <div className="absolute top-1/3 left-0 w-full max-w-[400px] h-[400px] bg-[radial-gradient(ellipse_at_center,_rgba(212,175,55,0.12),_transparent_65%)]" />
      </div>

      {/* Header Banner */}
      <div className="relative z-10 text-center space-y-4">
        <div className="flex flex-col items-center gap-2">
          <div className="relative px-8 py-2.5 rounded-full bg-gradient-to-r from-white/[0.07] to-white/[0.02] border border-[#22c55e]/25 backdrop-blur-xl shadow-[0_0_30px_rgba(34,197,94,0.15)]">
            <p className="font-serif text-[18px] sm:text-[22px] tracking-wide text-[#eafff0]" dir="rtl">
              بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
            </p>
          </div>
          <div className="relative px-7 py-2 rounded-full bg-gradient-to-r from-[#22c55e]/20 to-[#d4af37]/20 border border-[#d4af37]/30 backdrop-blur-xl">
            <p className="font-serif text-[16px] sm:text-[20px] tracking-wide text-[#fde68a]" dir="rtl">
              اَللّٰهُمَّ صَلِّ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ ﷺ
            </p>
          </div>
        </div>

        <div className="space-y-2">
          <h2 className="text-3xl sm:text-5xl font-bold tracking-tight">
            <span className="block bg-gradient-to-r from-[#4ade80] via-[#fde68a] to-[#d4af37] bg-clip-text text-transparent drop-shadow-[0_2px_24px_rgba(212,175,55,0.35)]">
              اسماء النبی ﷺ
            </span>
            <span className="block mt-1 text-sm sm:text-base font-normal tracking-[0.24em] text-white/60 uppercase">
              Asma-un-Nabi ﷺ • 99 Blessed Names of Prophet Muhammad ﷺ
            </span>
            <span className="block mt-2 text-xl sm:text-2xl font-bold bg-gradient-to-r from-[#22c55e] via-[#fde68a] to-[#16a34a] bg-clip-text text-transparent font-urdu">
              حضور سرورِ کائنات ﷺ کے ۹۹ مبارک نام مع صفات
            </span>
          </h2>
          <p className="text-sm sm:text-base text-white/75 font-urdu max-w-2xl mx-auto leading-relaxed" dir="rtl">
            سبز و سنہری جڑے ہوئے عربی خط میں، اردو صفت، درود شریف کاؤنٹر اور خودکار ورد کی برکت کے ساتھ۔
          </p>
        </div>

        {/* Controls: Durood Counter, Auto Durood, Search */}
        <div className="flex flex-wrap justify-center items-center gap-3 pt-2">
          {/* Durood Counter */}
          <div className="flex items-center gap-3 px-4 py-2.5 rounded-full bg-white/[0.06] border border-[#22c55e]/20 backdrop-blur-xl shadow-md">
            <div className="w-9 h-9 rounded-full bg-gradient-to-br from-[#22c55e] to-[#14532d] flex items-center justify-center shadow-[0_0_18px_rgba(34,197,94,0.55)] border border-[#d4af37]/30">
              <span className="text-sm font-bold text-white font-serif">ﷺ</span>
            </div>
            <div className="text-left">
              <div className="text-[10px] tracking-[0.2em] text-white/50 uppercase">Durood Count</div>
              <div className="font-bold text-base sm:text-lg leading-none tabular-nums text-[#fde68a]">
                {duroodCount.toString().padStart(3, '0')}
              </div>
            </div>
            <button
              onClick={() => setDuroodCount(0)}
              className="ml-1 text-[10px] px-2.5 py-1 rounded-full bg-white/10 hover:bg-white/20 transition font-urdu"
              title="Reset Durood counter"
            >
              صفر
            </button>
          </div>

          {/* Auto Durood Toggle */}
          <button
            onClick={() => setIsAutoDurood(!isAutoDurood)}
            className={`group flex items-center gap-2.5 px-5 py-2.5 rounded-full border backdrop-blur-xl transition-all cursor-pointer ${
              isAutoDurood
                ? 'bg-gradient-to-r from-[#22c55e] to-[#d4af37] border-[#d4af37] text-black shadow-[0_0_24px_rgba(34,197,94,0.5)] font-bold'
                : 'bg-white/[0.06] border-[#22c55e]/20 hover:border-[#d4af37]/40 hover:bg-white/[0.08] text-white/90'
            }`}
          >
            <span
              className={`w-2 h-2 rounded-full ${
                isAutoDurood ? 'bg-black animate-pulse' : 'bg-[#22c55e] shadow-[0_0_10px_#22c55e]'
              }`}
            />
            <span className="text-xs sm:text-sm tracking-wide font-urdu">
              {isAutoDurood ? 'روک دیں — Pause Durood' : 'درود شروع کریں — Auto Durood'}
            </span>
            <span className={`text-xs transition-transform ${isAutoDurood ? '' : 'group-hover:translate-x-0.5'}`}>
              ✦
            </span>
          </button>

          {/* Search Box */}
          <div className="flex items-center gap-2 px-3 py-2 rounded-full bg-white/[0.05] border border-[#22c55e]/20">
            <input
              id="asma-un-nabi-durood-filter"
              name="asmaUnNabiFilter"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="تلاش — نام یا صفت..."
              className="bg-transparent outline-none text-xs sm:text-sm font-urdu placeholder:text-white/40 w-36 sm:w-48 text-right"
              dir="rtl"
            />
            <Search className="w-4 h-4 text-[#4ade80]" />
          </div>
        </div>

        {/* Audio / Rhythm Visualizer Bars */}
        <div className="flex justify-center gap-[3px] h-7 items-end pt-1">
          {Array.from({ length: 32 }).map((_, idx) => (
            <div
              key={idx}
              className="w-[3px] rounded-full transition-all duration-300"
              style={{
                height: isAutoDurood
                  ? `${10 + Math.sin((currentIndex * 0.9 + idx) * 1.2) * 8 + Math.random() * 12}px`
                  : '4px',
                background: EMERALD_PALETTE[idx % EMERALD_PALETTE.length],
                opacity: isAutoDurood ? 0.95 : 0.28,
                boxShadow: isAutoDurood ? `0 0 10px ${EMERALD_PALETTE[idx % EMERALD_PALETTE.length]}` : 'none'
              }}
            />
          ))}
        </div>
      </div>

      {/* Grid of 99 Names */}
      <div className="relative z-10 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4 auto-rows-fr">
        {filteredNames.map((item, idx) => {
          const isZikrActive = isAutoDurood && idx === currentIndex;
          const isExpanded = activeNameNo === item.no;

          return (
            <div
              key={item.no}
              onClick={() => {
                setActiveNameNo(activeNameNo === item.no ? null : item.no);
                setDuroodCount((c) => c + 1);
                setCurrentIndex(idx);
              }}
              className={`group relative rounded-[22px] p-[1px] cursor-pointer transition-all duration-500 ${
                isZikrActive ? 'scale-[1.02] z-10' : ''
              } ${isExpanded ? 'z-20' : ''}`}
              style={{
                background: isZikrActive
                  ? `linear-gradient(135deg, ${EMERALD_PALETTE[idx % EMERALD_PALETTE.length]}, #fde68a, ${
                      EMERALD_PALETTE[(idx + 3) % EMERALD_PALETTE.length]
                    })`
                  : isExpanded
                  ? 'linear-gradient(135deg, #22c55e, #fde68a, #d4af37)'
                  : 'linear-gradient(135deg, rgba(34,197,94,0.18), rgba(255,255,255,0.06))'
              }}
            >
              <div
                className={`relative h-full rounded-[21px] overflow-hidden transition-all duration-500 bg-gradient-to-b from-[#101a11] to-[#080e08] backdrop-blur-xl p-5 flex flex-col items-center justify-between text-center min-h-[218px] ${
                  isZikrActive
                    ? 'shadow-[0_0_40px_rgba(34,197,94,0.35),0_0_80px_rgba(212,175,55,0.18)]'
                    : 'shadow-[0_8px_32px_rgba(0,0,0,0.55)] group-hover:shadow-[0_12px_48px_rgba(0,0,0,0.65),0_0_32px_rgba(34,197,94,0.22)]'
                } group-hover:-translate-y-[2px]`}
              >
                {/* Top Border Accent */}
                <div className="absolute top-0 inset-x-0 h-[1px] bg-gradient-to-r from-transparent via-[#4ade80]/30 to-transparent" />

                {/* Number Badge & Indicators */}
                <div className="w-full flex items-center justify-between">
                  <div className="w-8 h-8 rounded-full bg-black/50 border border-[#22c55e]/20 grid place-items-center backdrop-blur">
                    <span className="text-[11px] font-bold tracking-widest text-[#fde68a]">
                      {String(item.no).padStart(2, '0')}
                    </span>
                  </div>
                  {isZikrActive && <div className="w-2 h-2 rounded-full bg-[#4ade80] animate-ping" />}
                  <span className="text-xs text-[#4ade80] font-serif">ﷺ</span>
                </div>

                {/* Arabic Calligraphy in Color Gradient */}
                <div className="py-2 text-[34px] sm:text-[40px] leading-[1.25] flex items-center justify-center min-h-[64px]">
                  <GradientProphetText text={item.arabic} offset={idx * 2 + item.no} active={isZikrActive || isExpanded} />
                </div>

                {/* Divider */}
                <div className="w-full h-[1px] bg-gradient-to-r from-transparent via-[#22c55e]/20 to-transparent my-1" />

                {/* Transliteration and Urdu Meaning */}
                <div className="space-y-1 w-full">
                  <div className="text-xs tracking-[0.18em] uppercase font-semibold text-[#4ade80]/80">
                    {item.translit}
                  </div>
                  <div className="font-urdu text-base text-[#fde68a] drop-shadow-[0_1px_10px_rgba(212,175,55,0.25)]">
                    {item.urdu}
                  </div>
                </div>

                {/* Expandable Detail */}
                {isExpanded && (
                  <div className="w-full mt-3 pt-2.5 px-3 py-2 rounded-xl bg-[#22c55e]/[0.07] border border-[#22c55e]/20 text-xs leading-5 text-white/80 font-urdu text-right" dir="rtl">
                    <span className="text-[#4ade80] font-bold">✦ </span>
                    یہ مبارک نام حضور خاتم النبیین ﷺ کی صفت <b className="text-[#fde68a]">"{item.urdu}"</b> کو بیان فرماتا ہے۔ اس پر درود بھیجنا باعثِ نجات و برکت ہے۔
                    <div className="mt-1.5 text-[11px] text-[#4ade80]/90 font-serif">
                      صَلَّى اللهُ عَلَيْهِ وَسَلَّمَ
                    </div>
                  </div>
                )}

                {/* Bottom Color Accent */}
                <div className="absolute bottom-0 inset-x-0 h-[3px] flex">
                  {Array.from({ length: 6 }).map((_, f) => (
                    <div
                      key={f}
                      className="flex-1"
                      style={{
                        background: EMERALD_PALETTE[(idx + f) % EMERALD_PALETTE.length],
                        opacity: isZikrActive || isExpanded ? 1 : 0.55
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
      <div className="relative z-10 pt-4 border-t border-[#22c55e]/20 flex flex-col sm:flex-row items-center justify-between gap-3 text-xs text-white/60 font-urdu" dir="rtl">
        <p className="text-center sm:text-right leading-relaxed max-w-3xl">
          وَمَا أَرْسَلْنَاكَ إِلَّا رَحْمَةً لِّلْعَالَمِينَ — اور ہم نے آپ ﷺ کو تمام جہانوں کے لیے سراپا رحمت بنا کر بھیجا۔ <span className="text-amber-300/80">(سورۃ الانبیاء: ۱۰۷)</span>
        </p>
        <span className="text-[11px] text-white/40 tracking-wider font-sans uppercase">
          Baytul Ilm AI • 99 Blessed Names of Rasulullah ﷺ
        </span>
      </div>
    </div>
  );
};
