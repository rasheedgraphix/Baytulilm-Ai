import { DarsNizamiLevel } from '../types';

export const DARS_NIZAMI_LEVELS: DarsNizamiLevel[] = [
  {
    id: 'year-1',
    yearName: 'Year 1 (Darja Ula / Initial)',
    arabicName: 'السَّنَةُ الأُولَى - الدَّرَجَةُ الأُولَى (الإِعْدَادِيَّة)',
    englishTitle: 'Foundations of Arabic Language & Basic Fiqh',
    description: 'Focuses on foundational Arabic morphology (Sarf), syntax (Nahw), introductory jurisprudence, and Arabic composition.',
    subjects: [
      {
        name: 'Arabic Morphology (Sarf)',
        arabicName: 'علم الصرف',
        coreBooks: ['Mizan-us-Sarf', 'Munsha\'ib', 'Ilm-us-Sarf', 'Panjgina'],
        description: 'Mastery of verb conjugations (Sighah), root derivations, and basic grammatical patterns.'
      },
      {
        name: 'Arabic Syntax (Nahw)',
        arabicName: 'علم النحو',
        coreBooks: ['Nahw Mir', 'Tashil-un-Nahw', 'Awamil-un-Nahw'],
        description: 'Rules governing word endings, sentence construction, and grammatical analysis (I\'rab).'
      },
      {
        name: 'Elementary Fiqh',
        arabicName: 'الفقه الميسر',
        coreBooks: ['Nur-ul-Izah', 'Taleem-ul-Islam', 'Nisaab-ul-Fiqh'],
        description: 'Practical rulings on Taharah (purification), Salah (prayer), Sawm (fasting), and daily worship.'
      },
      {
        name: 'Adab & Composition',
        arabicName: 'الأدب العربي',
        coreBooks: ['Al-Qira\'at-ur-Rashidah', 'Minhaj-ul-Arabiyyah'],
        description: 'Building vocabulary, sentence structure, and conversational Arabic fluency.'
      }
    ]
  },
  {
    id: 'year-2',
    yearName: 'Year 2 (Darja Sania / Intermediate 1)',
    arabicName: 'السَّنَةُ الثَّانِيَةُ - الدَّرَجَةُ الثَّانِيَة',
    englishTitle: 'Intermediate Grammar, Logic & Fiqh Manuals',
    description: 'Deepens grammatical analysis, introduces classical logic (Mantiq) and intermediate Fiqh manuals.',
    subjects: [
      {
        name: 'Intermediate Nahw',
        arabicName: 'النحو المتوسط',
        coreBooks: ['Hidayat-un-Nahw', 'Kafiyah (Ibn-ul-Hajib)'],
        description: 'Comprehensive analysis of complex grammatical structures and governing agents.'
      },
      {
        name: 'Intermediate Sarf',
        arabicName: 'الصرف المتوسط',
        coreBooks: ['Ilm-us-Sighah', 'Dustur-ul-Mubtadi'],
        description: 'Irregular verbs (Haft Aqsam), weak letters, and morphological transformations.'
      },
      {
        name: 'Classical Fiqh',
        arabicName: 'الفقه الخدامي',
        coreBooks: ['Mukhtasar-ul-Quduri'],
        description: 'Foundational Hanafi legal manual covering worship, commercial transactions, and family law.'
      },
      {
        name: 'Classical Logic (Mantiq)',
        arabicName: 'علم المنطق',
        coreBooks: ['Isaghoji', 'Mirqat', 'Al-Muqaddamat-ul-Mantiqiyyah'],
        description: 'Principles of logical definitions, syllogisms, and formal reasoning.'
      }
    ]
  },
  {
    id: 'year-3',
    yearName: 'Year 3 (Darja Salisa / Intermediate 2)',
    arabicName: 'السَّنَةُ الثَّالِثَةُ - الدَّرَجَةُ الثَّالِثَة',
    englishTitle: 'Principles of Islamic Law & Rhetoric',
    description: 'Introduces Usul-ul-Fiqh (legal methodology), Balaghat (Arabic rhetoric), and Seerah studies.',
    subjects: [
      {
        name: 'Usul-ul-Fiqh (Legal Principles)',
        arabicName: 'أصول الفقه',
        coreBooks: ['Usul-ush-Shashi', 'Al-Husamiyyah'],
        description: 'Methodology for extracting legal rulings from Quran and Sunnah.'
      },
      {
        name: 'Balaghat (Rhetoric & Eloquence)',
        arabicName: 'علم البلاغة',
        coreBooks: ['Durus-ul-Balaghat', 'Mukhtasar-ul-Ma\'ani'],
        description: 'Study of Ma\'ani (semantics), Badi\' (figures of speech), and Bayan (metaphors).'
      },
      {
        name: 'Advanced Fiqh',
        arabicName: 'الفقه الموسع',
        coreBooks: ['Kanz-ud-Daqaiq', 'Sharh-ul-Wiqayah'],
        description: 'Detailed analysis of Hanafi jurisprudence with legal reasoning.'
      },
      {
        name: 'Seerah & Islamic History',
        arabicName: 'السيرة النبوية',
        coreBooks: ['Nur-ul-Yaqin', 'Seerat-ul-Mustafa'],
        description: 'Chronological biography of Prophet Muhammad ﷺ.'
      }
    ]
  },
  {
    id: 'year-4',
    yearName: 'Year 4 (Darja Rabia / Higher Secondary 1)',
    arabicName: 'السَّنَةُ الرَّابِعَةُ - الدَّرَجَةُ الرَّابِعَة',
    englishTitle: 'Advanced Legal Methodology & Tafsir Primer',
    description: 'Covers higher principles of jurisprudence, theology (Aqeedah), and introductory Tafsir.',
    subjects: [
      {
        name: 'Usul-ul-Fiqh Advanced',
        arabicName: 'أصول الفقه المتقدم',
        coreBooks: ['Nur-ul-Anwar', 'Al-Manar'],
        description: 'Deep dive into textual implications, generality/specificity, and Qiyas.'
      },
      {
        name: 'Tafsir Primers',
        arabicName: 'أصول التفسير والقرآن',
        coreBooks: ['Tafsir Al-Jalalayn (Juz 1-15)', 'Al-Fawz-ul-Kabir'],
        description: 'Exegesis of Quranic surahs and principles of exegesis.'
      },
      {
        name: 'Aqeedah & Kalam (Theology)',
        arabicName: 'العقيدة وعلم الكلام',
        coreBooks: ['Al-Aqeedah-ut-Tahawiyyah', 'Sharh-ul-Aqa\'id-un-Nasafiyyah'],
        description: 'Exposition of orthodox Islamic creed and defense of theology.'
      }
    ]
  },
  {
    id: 'year-5',
    yearName: 'Year 5 (Darja Khamisa / Higher Secondary 2 - Al-Aliyah)',
    arabicName: 'السَّنَةُ الخَامِسَةُ - الدَّرَجَةُ الخَامِسَة (العَالِيَة)',
    englishTitle: 'Higher Tafsir & Jurisprudential Extraction',
    description: 'Focuses on the second half of Tafsir Al-Jalalayn, advanced Hanafi comparative jurisprudence, and philosophy.',
    subjects: [
      {
        name: 'Intermediate Tafsir',
        arabicName: 'التفسير المتوسط',
        coreBooks: ['Tafsir Al-Jalalayn (Juz 16-30)', 'Tafsir Al-Baidawi (Surah Al-Baqarah)'],
        description: 'Detailed grammatical, linguistic, and thematic exegesis of the Holy Quran.'
      },
      {
        name: 'Higher Hanafi Jurisprudence',
        arabicName: 'الفقه المقارن المتقدم',
        coreBooks: ['Al-Hidayah (Volume 1 & 2)'],
        description: 'Comparative juristic evidence, legal deductions, and classical Hanafi debate.'
      },
      {
        name: 'Usul-ul-Fiqh Mastery',
        arabicName: 'أصول الفقه العالي',
        coreBooks: ['Al-Husami', 'Usul-us-Sarkhasi (Excerpts)'],
        description: 'Complex legal methodology and textual inference rules.'
      }
    ]
  },
  {
    id: 'year-6',
    yearName: 'Year 6 (Darja Sadisa / Advanced 1)',
    arabicName: 'السَّنَةُ السَّادِسَةُ - الدَّرَجَةُ السَّادِسَة',
    englishTitle: 'Comparative Jurisprudence & Hadith Methodology',
    description: 'Advanced study of civil, commercial, and criminal rulings alongside Hadith classification principles.',
    subjects: [
      {
        name: 'Comparative Fiqh (Volumes 3 & 4)',
        arabicName: 'الهداية - الجزء الثالث والرابع',
        coreBooks: ['Al-Hidayah (Volume 3 & 4)'],
        description: 'Commercial contracts, judicial procedures, inheritance, and criminal law.'
      },
      {
        name: 'Usul-ul-Hadith (Hadith Science)',
        arabicName: 'أصول الحديث وتخريجه',
        coreBooks: ['Nukhbat-ul-Fikar', 'Muqaddimah Ibn-us-Salah'],
        description: 'Narrator criticism (Jarh wa Ta\'dil), chain authentication, and Hadith taxonomy.'
      },
      {
        name: 'Higher Theology & Rhetoric',
        arabicName: 'البلاغة والعقيدة العالية',
        coreBooks: ['Sharh-ul-Aqa\'id', 'Al-Mutawwal (Excerpts)'],
        description: 'Philosophical theology and advanced Arabic rhetorical eloquence.'
      }
    ]
  },
  {
    id: 'year-7',
    yearName: 'Year 7 (Darja Sabia - Mawquf \'Alaih / Advanced 2)',
    arabicName: 'السَّنَةُ السَّابِعَةُ - المَوْقُوفُ عَلَيْهِ',
    englishTitle: 'Comprehensive Hadith Compendiums & Pre-Dawrah Preparation',
    description: 'Thorough textual study of comprehensive Hadith compendiums and juristic Hadith analysis.',
    subjects: [
      {
        name: 'Mishkat-ul-Masabih',
        arabicName: 'مشكاة المصابيح',
        coreBooks: ['Mishkat-ul-Masabih (Volume 1 & 2)'],
        description: 'Comprehensive thematic study of Hadith across all chapters of Fiqh, ethics, and spirituality.'
      },
      {
        name: 'Sharh Ma\'ani Al-Athar',
        arabicName: 'شرح معاني الآثار للإمام الطحاوي',
        coreBooks: ['Sharh Ma\'ani al-Athar (Imam At-Tahawi)'],
        description: 'Reconciling apparent contradictions in Hadith and defending Hanafi juristic positions.'
      },
      {
        name: 'Exegetical Exegesis',
        arabicName: 'التفسير التحليلي',
        coreBooks: ['Tafsir Al-Madarik (An-Nasafi)', 'Tafsir Ibn Kathir'],
        description: 'In-depth analytical Quranic exegesis.'
      }
    ]
  },
  {
    id: 'year-8',
    yearName: 'Year 8 (Dora-e-Hadith / Al-Alamiyyah Final)',
    arabicName: 'السَّنَةُ الثَّامِنَةُ - دَوْرَةُ الحَدِيثِ (العَالَمِيَّة)',
    englishTitle: 'Mastery of the Sihah Sittah (The Six Authentic Hadith Compendiums)',
    description: 'The pinnacle of Dars-e-Nizami: exhaustive textual reading, chain analysis, and juristic extraction of the primary Hadith books.',
    subjects: [
      {
        name: 'Sahih Al-Bukhari',
        arabicName: 'صحيح البخاري',
        coreBooks: ['Sahih Al-Bukhari (Full Text)'],
        description: 'Exhaustive reading, chapter heading analysis (Tarajam), Isnad critique, and legal extraction.'
      },
      {
        name: 'Sahih Muslim',
        arabicName: 'صحيح مسلم',
        coreBooks: ['Sahih Muslim (Full Text)'],
        description: 'In-depth study of Imam Muslim\'s methodology, narrators, and juristic arrangement.'
      },
      {
        name: 'Sunan Compendiums',
        arabicName: 'السنن الأربعة والآثار',
        coreBooks: ['Sunan At-Tirmidhi', 'Sunan Abu Dawud', 'Sunan An-Nasa\'i', 'Sunan Ibn Majah', 'Al-Muwatta (Imam Malik & Imam Muhammad)'],
        description: 'Comparative Hadith jurisprudence across all major Sunni legal schools.'
      }
    ]
  }
];
