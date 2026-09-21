import { DarsNizamiBookItem } from '../../types';
import { QURAN_EDITIONS } from '../quranEditions';
import { TAFASEER_BOOKS } from '../tafaseerBooks';
import { LUGHAT_BOOKS } from '../lughatBooks';
import { FATAWA_BOOKS } from '../fatawaBooks';
import { TAREEKH_BOOKS } from '../tareekhBooks';
import { TAJWEED_LIL_HUFFAZ_BOOKS } from '../tajweedLilHuffazBooks';
import { TAJWEED_LIL_ULAMA_BOOKS } from '../tajweedLilUlamaBooks';
import { DARJA_AWWAL_BOOKS } from './darjaAwwal';
import { DARJA_SANIA_BOOKS } from './darjaSania';
import { DARJA_SALISA_BOOKS } from './darjaSalisa';
import { DARJA_RABIA_BOOKS } from './darjaRabia';
import { DARJA_KHAMISA_BOOKS } from './darjaKhamisa';
import { DARJA_SADISA_BOOKS } from './darjaSadisa';
import { DARJA_SABEA_BOOKS } from './darjaSabea';
import { DORAE_HADITH_BOOKS } from './doraeHadith';
import { NEW_ADDED_DARS_BOOKS } from './newAddedBooks';

export const ALL_DARS_NIZAMI_BOOKS: DarsNizamiBookItem[] = [
  ...QURAN_EDITIONS,
  ...TAFASEER_BOOKS,
  ...LUGHAT_BOOKS,
  ...FATAWA_BOOKS,
  ...TAREEKH_BOOKS,
  ...TAJWEED_LIL_HUFFAZ_BOOKS,
  ...TAJWEED_LIL_ULAMA_BOOKS,
  ...DARJA_AWWAL_BOOKS,
  ...DARJA_SANIA_BOOKS,
  ...DARJA_SALISA_BOOKS,
  ...DARJA_RABIA_BOOKS,
  ...DARJA_KHAMISA_BOOKS,
  ...DARJA_SADISA_BOOKS,
  ...DARJA_SABEA_BOOKS,
  ...DORAE_HADITH_BOOKS,
  ...NEW_ADDED_DARS_BOOKS,
];

export interface DarsYearMeta {
  id: string;
  classLevel: string;
  nameUrdu: string;
  nameEnglish: string;
  descriptionUrdu: string;
  descriptionEnglish: string;
  badge: string;
  colorTheme: string;
  totalBooks: number;
}

export const DARS_YEARS_META: DarsYearMeta[] = [
  {
    id: 'quran',
    classLevel: 'quran',
    nameUrdu: 'قرآن مجید (مختلف طباعات و سطور)',
    nameEnglish: 'Holy Quran (Editions & Scripts)',
    descriptionUrdu: '۱۶، ۱۵، ۱۳، ۱۰، ۱۱، ۱۴، ۱۷، ۱۸ اور ۲۱ سطری رنگین تجویدی و حفاظی نسخہ جات مع اعلیٰ طباعت',
    descriptionEnglish: '16, 15, 13, 10, 11, 14, 17, 18 and 21-line Tajweedi & Hifzi Quran editions',
    badge: 'قرآن مجید',
    colorTheme: 'from-emerald-600 to-teal-800',
    totalBooks: ALL_DARS_NIZAMI_BOOKS.filter(b => b.classLevel === 'quran').length,
  },
  {
    id: '1st',
    classLevel: '1st',
    nameUrdu: 'درجہ اولیٰ (سال اول)',
    nameEnglish: '1st Year (Darja Ula / Awwal)',
    descriptionUrdu: 'صرف و نحو، ابتدائی فقہ (نور الایضاح، قدوری)، سیرت و عربی تکلم',
    descriptionEnglish: 'Sarf, Nahw, Introductory Fiqh (Qudoori, Noor-ul-Idah), Seerah & Arabic Basics',
    badge: 'سال اول',
    colorTheme: 'from-emerald-500 to-teal-600',
    totalBooks: ALL_DARS_NIZAMI_BOOKS.filter(b => b.classLevel === '1st').length,
  },
  {
    id: '2nd',
    classLevel: '2nd',
    nameUrdu: 'درجہ ثانیہ (سال دوم)',
    nameEnglish: '2nd Year (Darja Sania)',
    descriptionUrdu: 'نحو (ہدایۃ النحو)، فقہ (کنز الدقائق)، منطق (مرقاۃ، تیسیر المنطق) و علم الصیغہ',
    descriptionEnglish: 'Advanced Nahw (Hidayat-un-Nahw), Fiqh (Kanz-ud-Daqaiq), Mantiq (Mirqat) & Ilm-us-Seegha',
    badge: 'سال دوم',
    colorTheme: 'from-cyan-500 to-blue-600',
    totalBooks: ALL_DARS_NIZAMI_BOOKS.filter(b => b.classLevel === '2nd').length,
  },
  {
    id: '3rd',
    classLevel: '3rd',
    nameUrdu: 'درجہ ثالثہ (سال سوم)',
    nameEnglish: '3rd Year (Darja Salisa)',
    descriptionUrdu: 'حدیث (ریاض الصالحین)، نحو (کافیہ)، فقہ (کنز 2)، منطق (شرح تہذیب) و عقیدہ طحاویہ',
    descriptionEnglish: 'Hadith (Riyad-us-Saliheen), Nahw (Kafiyah), Fiqh (Kanz 2), Mantiq (Sharh Tahzeeb) & Tahawiyyah',
    badge: 'سال سوم',
    colorTheme: 'from-indigo-500 to-violet-600',
    totalBooks: ALL_DARS_NIZAMI_BOOKS.filter(b => b.classLevel === '3rd').length,
  },
  {
    id: '4th',
    classLevel: '4th',
    nameUrdu: 'درجہ رابعہ (سال چہارم)',
    nameEnglish: '4th Year (Darja Rabia)',
    descriptionUrdu: 'نحو (شرح جامی)، بلاغت (دروس البلاغہ)، ادب عربی (مقامات حریری) و منطق (قطبی)',
    descriptionEnglish: 'Nahw (Sharh Jami), Balaghat (Duroos-ul-Balagha), Arabic Literature (Maqamat) & Qutbi',
    badge: 'سال چہارم',
    colorTheme: 'from-purple-500 to-pink-600',
    totalBooks: ALL_DARS_NIZAMI_BOOKS.filter(b => b.classLevel === '4th').length,
  },
  {
    id: '5th',
    classLevel: '5th',
    nameUrdu: 'درجہ خامسہ (سال پنجم)',
    nameEnglish: '5th Year (Darja Khamisa)',
    descriptionUrdu: 'فقہ (ہدایہ 1)، اصول فقہ (حسامی)، بلاغت (مختصر المعانی)، حدیث (آثار السنن) و سبعہ معلقات',
    descriptionEnglish: 'Fiqh (Hidayah 1), Usul al-Fiqh (Husami), Balaghat (Mukhtasar), Hadith (Aasar-us-Sunan)',
    badge: 'سال پنجم',
    colorTheme: 'from-amber-500 to-orange-600',
    totalBooks: ALL_DARS_NIZAMI_BOOKS.filter(b => b.classLevel === '5th').length,
  },
  {
    id: '6th',
    classLevel: '6th',
    nameUrdu: 'درجہ سادسہ (سال ششم)',
    nameEnglish: '6th Year (Darja Sadisa)',
    descriptionUrdu: 'تفسیر (جلالین شریف)، اصول فقہ (توضیح و تلویح)، عقائد (شرح العقائد) و میراث (سراجی)',
    descriptionEnglish: 'Tafseer (Jalalain), Usul al-Fiqh (Tawzeeh & Talweeh), Aqaid (Sharh al-Aqaid) & Miras (Siraji)',
    badge: 'سال ششم',
    colorTheme: 'from-rose-500 to-red-600',
    totalBooks: ALL_DARS_NIZAMI_BOOKS.filter(b => b.classLevel === '6th').length,
  },
  {
    id: '7th',
    classLevel: '7th',
    nameUrdu: 'درجہ سابعہ (موقوف علیہ)',
    nameEnglish: '7th Year (Darja Sabea / Mauqoof Alaih)',
    descriptionUrdu: 'حدیث (مشکوٰۃ شریف)، اصول حدیث (نخبۃ الفکر، نزہۃ النظر)، فقہ (ہدایہ اخیرین) و تفسیر بیضاوی',
    descriptionEnglish: 'Hadith (Mishkat-ul-Masabeeh), Usul al-Hadith (Nukhbah & Nuzhah), Fiqh (Hidayah) & Baizawi',
    badge: 'موقوف علیہ',
    colorTheme: 'from-teal-500 to-emerald-700',
    totalBooks: ALL_DARS_NIZAMI_BOOKS.filter(b => b.classLevel === '7th').length,
  },
  {
    id: '8th',
    classLevel: '8th',
    nameUrdu: 'دورۂ حدیث شریف (سال ہشتم)',
    nameEnglish: '8th Year (Dora-e-Hadith Sharif)',
    descriptionUrdu: 'صحاح ستہ و کتب حدیث (بخاری، مسلم، ترمذی، ابوداؤد، نسائی، ابن ماجہ، طحاوی و موطا امام مالک)',
    descriptionEnglish: 'Sihah Sitta (Bukhari, Muslim, Tirmidhi, Abu Dawood, Nasaee, Ibn Majah, Tahawi & Muwatta)',
    badge: 'عالمیہ / دورہ حدیث',
    colorTheme: 'from-amber-600 to-yellow-600',
    totalBooks: ALL_DARS_NIZAMI_BOOKS.filter(b => b.classLevel === '8th').length,
  }
];

export {
  QURAN_EDITIONS,
  TAFASEER_BOOKS,
  LUGHAT_BOOKS,
  FATAWA_BOOKS,
  TAREEKH_BOOKS,
  TAJWEED_LIL_HUFFAZ_BOOKS,
  TAJWEED_LIL_ULAMA_BOOKS,
  DARJA_AWWAL_BOOKS,
  DARJA_SANIA_BOOKS,
  DARJA_SALISA_BOOKS,
  DARJA_RABIA_BOOKS,
  DARJA_KHAMISA_BOOKS,
  DARJA_SADISA_BOOKS,
  DARJA_SABEA_BOOKS,
  DORAE_HADITH_BOOKS,
  NEW_ADDED_DARS_BOOKS,
};
