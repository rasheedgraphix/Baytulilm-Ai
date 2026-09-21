import { AppScreenshot } from '../types';
import homeImg from '../assets/images/real_screenshot_home_1786582226379.jpg';
import libraryImg from '../assets/images/real_screenshot_library_1786582239390.jpg';
import quizImg from '../assets/images/real_screenshot_quiz_1786582251020.jpg';
import aiImg from '../assets/images/real_screenshot_ai_1786582263101.jpg';
import offlineImg from '../assets/images/real_screenshot_offline_1786582277604.jpg';
import profileImg from '../assets/images/real_screenshot_profile_1786582291674.jpg';

export const APP_SCREENSHOTS: AppScreenshot[] = [
  {
    id: 's1',
    title: 'Main App Home & Prayer Times',
    titleUrdu: 'مین ہوم اسکرین اور اوقاتِ نماز',
    titlePashto: 'اصلي سکرین او د لمانځه وختونه',
    caption: 'Official Baytul Ilm AI v1.4.4 Android home screen featuring Islamabad prayer times, search bar, and Dars-e-Nizami syllabus highlights.',
    captionUrdu: 'بیت العلم AI ورژن 1.4.4 ہوم اسکرین، اسلام آباد کے اوقاتِ نماز، تیز رفتار سرچ بار اور نصاب کی جھلکیاں۔',
    captionPashto: 'د بیت العلم AI نسخه 1.4.4 اصلي سکرین، د لمانځه کره وختونه او د درسِ نظامي نصاب نښې.',
    category: 'Home',
    imageUrl: homeImg
  },
  {
    id: 's2',
    title: 'Digital Library & Dars-e-Nizami Syllabus',
    titleUrdu: 'ڈیجیٹل کتب خانہ اور درسِ نظامی',
    titlePashto: 'ډیجیټل کتابتون او د درسِ نظامي نصاب',
    caption: 'Systematic breakdown of 8 academic syllabus levels (Darja Ula to Dora-e-Hadith) with subjects, books, and syllabus progress tracking.',
    captionUrdu: 'درجہ اولیٰ سے دورۂ حدیث تک 8 تعلیمی درجات کی تمام درسی کتب، شروحات اور تعلیمی پیش رفت۔',
    captionPashto: 'له لومړۍ درجې څخه تر دورۂ حدیث پورې د ۸ کلونو درسي کتابونه او د زده کړې پرمختګ.',
    category: 'Library',
    imageUrl: libraryImg
  },
  {
    id: 's3',
    title: 'Islamic Quiz & Assessment System',
    titleUrdu: 'دینی کوئز اور امتحانی نظام',
    titlePashto: 'دیني کوئز او ازموینې سیسټم',
    caption: 'Chapter-wise quiz module featuring Elementary (ابتدائی), Intermediate (درمیانی), and Advanced (اعلیٰ) level assessments for madrasa students.',
    captionUrdu: 'ابتدائی، درمیانی اور اعلیٰ درجات کے لیے سبق وار کثیر الانتخابی سوالات اور فوری تفصیلی نتائج۔',
    captionPashto: 'د مدرسې د زده کوونکو لپاره په دریو کچو (ابتدایي، منځنۍ او لوړه) دیني او درسي پوښتنې.',
    category: 'Quiz',
    imageUrl: quizImg
  },
  {
    id: 's4',
    title: 'Baytul Ilm AI Scholar (Gemini AI)',
    titleUrdu: 'بیت العلم AI اسکالر (علمی معاون)',
    titlePashto: 'د بیت العلم AI علمي مرستیال',
    caption: 'On-device Islamic AI Scholar providing verified textbook references, page citations, and Sarf/Nahw syntax explanations.',
    captionUrdu: 'صرف، نحو اور فقہی اصطلاحات کے درست جوابات اور معتبر کتب کے مستند حوالے فراہم کرنے والا AI اسکالر۔',
    captionPashto: 'د ګرامر، صرف او نحوې د اصولو او فقهي مسئلو مستند او معتبر تشریح کوونکی AI معاون.',
    category: 'AI Scholar',
    imageUrl: aiImg
  },
  {
    id: 's5',
    title: 'Offline Library & Downloaded Content',
    titleUrdu: 'آف لائن کتب خانہ اور محفوظ مواد',
    titlePashto: 'آفلاین کتابتون او خوندي شوي مواد',
    caption: 'Access Kutub, PDFs, study notes, and quiz packs without requiring an active internet connection.',
    captionUrdu: 'انٹرنیٹ کے بغیر مطالعہ کے لیے کتب، پی ڈی ایف، ذاتی نوٹس اور کوئز پیک تک مکمل رسائی۔',
    captionPashto: 'بې له انټرنیټه د کتابونو، پی ډي ایف، یادښتونو او کوئزونو کارولو بشپړه اسانتیا.',
    category: 'Offline Mode',
    imageUrl: offlineImg
  },
  {
    id: 's6',
    title: 'User Profile & App Settings',
    titleUrdu: 'یوزر پروفائل اور ایپ سیٹنگز',
    titlePashto: 'پروفایل او د اپلیکیشن ترتیبات',
    caption: 'Student profile manager with student verification badge, reading stats, language toggle, and dark/light mode customization.',
    captionUrdu: 'طالب علم کا پروفائل، مطالعہ کے اعداد و شمار، ڈارک و لائٹ موڈ اور کثیر لسانی ترتیبات۔',
    captionPashto: 'د زده کوونکي پېژندپاڼه، د لوستلو احصائیه، د ژبو بدلول او د ډارک موډ ترتیبات.',
    category: 'Profile',
    imageUrl: profileImg
  }
];
