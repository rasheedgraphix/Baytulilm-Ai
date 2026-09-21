export type Language = 'en' | 'ur' | 'ps';

export interface TranslationDictionary {
  [key: string]: {
    en: string;
    ur: string;
    ps: string;
  };
}

export const TRANSLATIONS: TranslationDictionary = {
  // ==========================================
  // Brand & General
  // ==========================================
  appName: {
    en: 'Baytul Ilm AI',
    ur: 'بیت العلم AI',
    ps: 'بیت العلم AI',
  },
  appTagline: {
    en: 'Comprehensive Android Islamic Education & Dars-e-Nizami Learning Platform',
    ur: 'جامع اسلامی تعلیمی پلیٹ فارم اور 8 سالہ درسِ نظامی کا ڈیجیٹل نصاب',
    ps: 'د اسلامي زده کړو او ۸ کلن درسِ نظامي جامع انډرایډ تعلیمي پلیټفارم',
  },
  officialAppBadge: {
    en: 'Official Android App',
    ur: 'آفیشل اینڈرائیڈ ایپ',
    ps: 'رسمي انډرایډ اپلیکیشن',
  },
  latestVersionBadge: {
    en: 'Latest Version: 1.4.4',
    ur: 'ورژن 1.4.4',
    ps: 'نوې نسخه: ۱.۴.۴',
  },
  downloadLatestApk: {
    en: 'Download Latest APK',
    ur: 'تازہ ترین APK ڈاؤن لوڈ کریں',
    ps: 'نوې APK فایل ډاونلوډ کړئ',
  },
  downloadApkBtn: {
    en: 'Download Latest APK',
    ur: 'تازہ ترین APK ڈاؤن لوڈ کریں',
    ps: 'نوې APK فایل ډاونلوډ کړئ',
  },
  exploreFeatures: {
    en: 'Explore Features',
    ur: 'تمام خصوصیات دیکھیں',
    ps: 'ټولې ځانګړتیاوې او سهولتونه',
  },
  tryIslamicTools: {
    en: 'Islamic Features & Hub',
    ur: 'اسلامی خصوصیات و سہولیات',
    ps: 'اسلامي خدمتونه او اوزار',
  },

  // ==========================================
  // Navigation Links
  // ==========================================
  navHome: {
    en: 'Home',
    ur: 'ہوم',
    ps: 'اصلي پاڼه',
  },
  navFeatures: {
    en: 'Features',
    ur: 'خصوصیات',
    ps: 'ځانګړتیاوې',
  },
  navLibrary: {
    en: 'Books Library',
    ur: 'درسِ نظامی لائبریری',
    ps: 'د درسِ نظامي کتابتون',
  },
  navHaramainLive: {
    en: 'Haramain Live',
    ur: 'حرمین شریفین لائیو',
    ps: 'حرمین شریفین ژوندی',
  },
  navIslamicTools: {
    en: 'Islamic Features',
    ur: 'اسلامی خصوصیات',
    ps: 'اسلامي اسانتیاوې',
  },
  navScreenshots: {
    en: 'Screenshots',
    ur: 'ایپ اسکرین شاٹس',
    ps: 'د اپلیکیشن عکسونه',
  },
  navDownload: {
    en: 'Download APK',
    ur: 'ڈاؤن لوڈ APK',
    ps: 'APK ډاونلوډ',
  },
  navPrivacy: {
    en: 'Privacy Policy',
    ur: 'پرائیویسی پالیسی',
    ps: 'د محرمیت تګلاره',
  },
  navContact: {
    en: 'Contact & Support',
    ur: 'رابطہ و رہنمائی',
    ps: 'اړیکه او مرسته',
  },

  // ==========================================
  // Hero Section
  // ==========================================
  heroHeadingTitle: {
    en: 'Comprehensive Islamic Education Platform',
    ur: 'جامع اسلامی تعلیمی پلیٹ فارم',
    ps: 'د اسلامي زده کړو او درسِ نظامي جامع پلیټفارم',
  },
  heroTitle1: {
    en: 'The Comprehensive',
    ur: 'جامع اسلامی تعلیمی پلیٹ فارم',
    ps: 'د اسلامي زده کړو جامع پلیټفارم',
  },
  heroTitleHighlight: {
    en: 'Islamic Education Platform',
    ur: 'بیت العلم AI',
    ps: 'بیت العلم AI',
  },
  heroTitle2: {
    en: 'For Android',
    ur: 'اینڈرائیڈ ایپ',
    ps: 'انډرایډ اپلیکیشن',
  },
  heroSubtitle: {
    en: 'Baytul Ilm AI is a comprehensive Islamic educational platform featuring the Holy Quran, Masnoon Duas, 99 Names of Allah, Names of Muhammad ﷺ, Digital Library, Islamic Quizzes, Qibla Compass, Tasbeeh, Haramain Sharifain Live Streaming, and an AI Islamic Assistant.',
    ur: 'بیت العلم AI ایک جامع اسلامی تعلیمی پلیٹ فارم ہے، جس میں قرآن مجید، مسنون دعائیں، 99 اسمائے حسنیٰ، اسمائے محمد ﷺ، ڈیجیٹل کتب خانہ، دینی کوئز، قبلہ کمپاس، تسبیح، حرمین شریفین کی لائیو نشریات اور AI اسلامی معاون شامل ہیں۔',
    ps: 'بیت العلم AI د اسلامي زده کړو یو پرمختللی او جامع انډرایډ اپلیکیشن دی چې پکې قرآن کریم، مسنونې دعاګانې، د الله تعالی ۹۹ مبارک نومونه، د نبي کریم ﷺ نومونه، د درسِ نظامي ډیجیټل کتابتون، دیني ازموینې (کوئز)، د قبلې قطب نما، ډیجیټل تسبېح، د حرمین شریفین ۲۴ ساعته ژوندۍ خپرونې او پوه AI مرستیال شامل دي.',
  },
  heroBullet1: {
    en: 'Holy Quran with Tafseer',
    ur: 'قرآن مجید مع ترجمہ و تفاسیر',
    ps: 'قرآن کریم له ژباړې او تفسیر سره',
  },
  heroBullet2: {
    en: 'Masnoon Duas & 99 Names of Allah',
    ur: 'مسنون دعائیں اور 99 اسمائے حسنیٰ',
    ps: 'مسنونې دعاګانې او د الله تعالی ۹۹ نومونه',
  },
  heroBullet3: {
    en: 'Haramain Live & Qibla Compass',
    ur: 'حرمین شریفین لائیو و قبلہ کمپاس',
    ps: 'د حرمین شریفین ژوندۍ خپرونې او د قبلې لوری',
  },
  heroBullet4: {
    en: 'Digital Library & AI Tutor',
    ur: 'ڈیجیٹل کتب خانہ و AI اسلامی معاون',
    ps: 'د درسِ نظامي کتابتون او ذکي AI مرستیال',
  },

  // ==========================================
  // Core Islamic Features (8 Features)
  // ==========================================
  featureQuranTitle: {
    en: 'Holy Quran',
    ur: 'قرآن مجید',
    ps: 'قرآن کریم',
  },
  featureQuranDesc: {
    en: 'Complete Quran with word-by-word grammatical breakdown, translations in Urdu, Pashto & English, Tafseer references, and verse search.',
    ur: 'مکمل قرآن مجید مع لفظ بہ لفظ ترجمہ، اعراب، اردو و پشتو تفاسیر اور آیات تلاش کرنے کی آسان سہولت۔',
    ps: 'بشپړ قرآن کریم له کلمه په کلمه ژباړې، اعرابو، معتبرو پښتو او اردو تفاسیرو او اسانه لټون سره.',
  },

  featureDuasTitle: {
    en: 'Masnoon Duas',
    ur: 'مسنون دعائیں',
    ps: 'مسنونې دعاګانې',
  },
  featureDuasDesc: {
    en: 'Authentic daily supplications for morning, evening, travel, prayer, and protection with Arabic text, Urdu translation, and Hadith references.',
    ur: 'صبح و شام، سفر، نماز، حفاظت اور روزمرہ کی مستند دعائیں مع عربی متن، ترجمہ اور احادیث کے حوالہ جات۔',
    ps: 'د سهار او ماښام، سفر، لمانځه، خوندیتوب او ورځني ژوند مستندې دعاګانې له عربي متن، پښتو ژباړې او حدیثي حوالو سره.',
  },

  featureAsmaulHusnaTitle: {
    en: '99 Names of Allah',
    ur: '99 اسمائے حسنیٰ',
    ps: 'د الله تعالی ۹۹ مبارک نومونه',
  },
  featureAsmaulHusnaDesc: {
    en: 'Blessed 99 Names of Allah with beautiful Arabic calligraphy, Urdu meanings, and spiritual virtues.',
    ur: 'اللہ تعالیٰ کے 99 مبارک نام مع خوبصورت عربی خطاطی، سلیس اردو معانی اور روحانی خواص و برکات۔',
    ps: 'د الله جل جلاله ۹۹ مبارک نومونه (اسماء الحسنیٰ) په ښکلې خطاطۍ، پښتو ماناوو او فضائلو سره.',
  },

  featureAsmaunNabiTitle: {
    en: 'Names of Muhammad ﷺ',
    ur: 'اسمائے محمد ﷺ',
    ps: 'د نبي کریم ﷺ مبارک نومونه',
  },
  featureAsmaunNabiDesc: {
    en: 'Blessed names and noble titles of the Prophet Muhammad ﷺ with meanings and virtues.',
    ur: 'رسول اللہ ﷺ کے مبارک اسماء و القاب مع مفاہیم، درود و سلام کے فضائل اور دینی اہمیت۔',
    ps: 'د رسول الله ﷺ مبارک نومونه او القاب له پښتو ماناوو، د درود شریف له برکتونو او فضائلو سره.',
  },

  featureHaramainLiveTitle: {
    en: 'Haramain Sharifain Live',
    ur: 'حرمین شریفین لائیو',
    ps: 'د حرمین شریفین ژوندۍ خپرونې',
  },
  featureHaramainLiveDesc: {
    en: '24/7 crystal-clear live broadcast directly from Masjid al-Haram (Makkah) and Masjid an-Nabawi (Madinah).',
    ur: 'مسجد الحرام (مکہ مکرمہ) اور مسجد نبوی (مدینہ منورہ) کی 24 گھنٹے براہِ راست نشریات اپنے موبائل میں دیکھیں۔',
    ps: 'د مسجد الحرام (مکه مکرمه) او مسجد نبوي (مدینه منوره) ۲۴ ساعته مستقیمې ژوندۍ خپرونې په لوړ کیفیت سره.',
  },

  featureQiblaTitle: {
    en: 'Qibla Compass',
    ur: 'قبلہ کمپاس',
    ps: 'د قبلې قطب نما',
  },
  featureQiblaDesc: {
    en: 'Accurate Qibla compass calculating the exact direction to the Holy Kaaba from anywhere in the world.',
    ur: 'درست قبلہ رخ بتانے والا ڈیجیٹل کمپاس جو دنیا کے کسی بھی مقام سے خانہ کعبہ کی سمت کی رہنمائی کرتا ہے۔',
    ps: 'د قبلې سمارټ قطب نما چې د نړۍ له هر ګوټ څخه د کعبې شریفې دقیق لوری او واټن ښيي.',
  },

  featureTasbeehTitle: {
    en: 'Digital Tasbeeh',
    ur: 'تسبیح',
    ps: 'ډیجیټل تسبېح',
  },
  featureTasbeehDesc: {
    en: 'Interactive digital Tasbeeh counter with custom targets (33, 99, 1000) and preset masnoon azkar.',
    ur: 'انٹرایکٹو ڈیجیٹل تسبیح کاؤنٹر، مسنون اذکار (سبحان اللہ، الحمد للہ، اللہ اکبر، درود پاک) اور تسبیح محفوظ کرنے کی سہولت۔',
    ps: 'ډیجیټل سمارټ تسبېح د مسنونو اذکارو (سبحان الله، الحمد لله، الله اکبر، استغفار او درود) او د شمېر خوندي کولو له اسانتیا سره.',
  },

  featureLibraryTitle: {
    en: 'Digital Islamic Library',
    ur: 'ڈیجیٹل کتب خانہ',
    ps: 'ډیجیټل اسلامي کتابتون',
  },
  featureLibraryDesc: {
    en: 'Digital library containing the complete 8-year Dars-e-Nizami curriculum, books on Sarf, Nahw, Fiqh, Hadith, and research treatises.',
    ur: '8 سالہ درسِ نظامی کا مکمل نصاب، علم الصرف، النحو، فقہ، اصول، حدیث شریف اور عربی درسی کتب کا جامع ذخیرہ۔',
    ps: 'د ۸ کلن درسِ نظامي بشپړ نصاب، د علم الصرف، نحوه، فقه، اصول، حدیث شریف او دیني کتبو جامع او منظم ډیجیټل ذخیره.',
  },

  // ==========================================
  // Additional Academic Features
  // ==========================================
  featureDarsNizamiTitle: {
    en: '8-Year Dars-e-Nizami Curriculum',
    ur: '8 سالہ درسِ نظامی نصاب',
    ps: 'د ۸ کلن درسِ نظامي نصاب',
  },
  featureDarsNizamiDesc: {
    en: 'Structured syllabus from Darja Ula to Dora-e-Hadith with textbook outlines, summaries, and exam guidelines.',
    ur: 'درجہ اولیٰ سے دورۂ حدیث تک تمام درجات کی کتب، اہم مباحث اور امتحانی رہنمائی کی مکمل ترتیب۔',
    ps: 'له لومړۍ درجې څخه تر دورۂ حدیث پورې د ټولو کتابونو، مهمو موضوعاتو او امتحاني مرستو بشپړ لارښود.',
  },

  featureQuizTitle: {
    en: 'Islamic Quizzes',
    ur: 'دینی کوئز',
    ps: 'دیني پوښتنې او ازموینې',
  },
  featureQuizDesc: {
    en: 'Chapter-wise interactive quizzes on Arabic grammar, Fiqh rulings, and Hadith sciences for madrasa students.',
    ur: 'صرف، نحو، فقہ، اصول اور علوم الحدیث کے موضوعات پر امتحانی اور خود تشخیصی سوالات مع تفصیلی جوابات۔',
    ps: 'د صرف، نحو، فقه، اصول او حدیثو په موضوعاتو باندې څو انتخابي پوښتنې له بشپړو تشریحي ځوابونو سره.',
  },

  featureAiTitle: {
    en: 'AI Islamic Assistant',
    ur: 'AI اسلامی معاون',
    ps: 'پوه AI اسلامي مرستیال',
  },
  featureAiDesc: {
    en: 'Contextual AI tutor providing verified answers to queries on Arabic grammar, conjugations, and classical texts.',
    ur: 'عربی گرامر، گردانوں کے ابہام اور فقہی و لغوی اصطلاحات کے حل کے لیے ہمہ وقت حاضر مستند AI معاون۔',
    ps: 'د عربي ګرامر، ګردانونو، فقهي اصطلاحاتو او نحوي مسئلو د حل لپاره ۲۴ ساعته چمتو پوه AI مرستیال.',
  },

  // ==========================================
  // Download Page & Section
  // ==========================================
  downloadPortalTitle: {
    en: 'Download Baytul Ilm AI Android APK',
    ur: 'بیت العلم AI اینڈرائیڈ APK ڈاؤن لوڈ کریں',
    ps: 'د بیت العلم AI انډرایډ APK ډاونلوډ کړئ',
  },
  downloadPortalSubtitle: {
    en: 'Get the official release package (v1.4.4) directly for your Android smartphone.',
    ur: 'اپنے اینڈرائیڈ فون کے لیے آفیشل تازہ ترین ورژن 1.4.4 براہِ راست حاصل کریں۔',
    ps: 'د خپل انډرایډ موبایل لپاره نوې رسمي نسخه (۱.۴.۴) په مستقیم ډول ترلاسه کړئ.',
  },
  directDownloadNote: {
    en: 'Clicking the button will download the official Version 1.4.4 APK package directly.',
    ur: 'بٹن پر کلک کرنے سے براہِ راست ورژن 1.4.4 کی باضابطہ APK فائل ڈاؤن لوڈ ہوگی۔',
    ps: 'په تڼۍ کلیک کولو سره به د ۱.۴.۴ نسخې رسمي APK مستقیم ډاونلوډ شي.',
  },
  packageInfo: {
    en: 'Package: com.baytulilmai.app',
    ur: 'پیکج: com.baytulilmai.app',
    ps: 'پیکج: com.baytulilmai.app',
  },
  approxSize: {
    en: 'Size: 53 MB',
    ur: 'سائز: 53 MB',
    ps: 'حجم: ۵۳ MB',
  },
  minRequirement: {
    en: 'Minimum Requirement: Android 7.0+ (Nougat, API 24+)',
    ur: 'کم از کم ضروری سسٹم: اینڈرائیڈ 7.0+ (Nougat)',
    ps: 'لږترلږه اړتیا: انډرایډ ۷.۰+ (نوګټ)',
  },
  targetRequirement: {
    en: 'Target SDK: Android 14 (API 34)',
    ur: 'ٹارگٹ ایس ڈی کے: اینڈرائیڈ 14 (API 34)',
    ps: 'هدفي SDK: انډرایډ ۱۴ (API ۳۴)',
  },
  checksumVerified: {
    en: 'SHA-256 Checksum Verified',
    ur: 'SHA-256 چیک سم تصدیق شدہ',
    ps: 'د SHA-256 تصدیق شوی چیک سم',
  },
  virusTotalClean: {
    en: 'VirusTotal Clean (0/72 engines flagged)',
    ur: 'وائرس ٹوٹل کلین (مکمل محفوظ)',
    ps: 'د ویروس څخه ۱۰۰٪ پاک او خوندي',
  },

  // ==========================================
  // Installation Steps
  // ==========================================
  installGuideTitle: {
    en: 'How to Install APK on Android',
    ur: 'اینڈرائیڈ پر APK انسٹال کرنے کا آسان طریقہ',
    ps: 'په انډرایډ موبایل کې د APK د انسټالولو آسانه طریقه',
  },
  installGuideSubtitle: {
    en: 'Follow these 4 simple steps to install the official Android application on your smartphone.',
    ur: 'اپنے فون میں بیت العلم AI انسٹال کرنے کے لیے درج ذیل 4 آسان مراحل پر عمل کریں۔',
    ps: 'په خپل مبایل کې د بیت العلم AI انسټالولو لپاره لاندې ۴ ساده مرحلې تعقیب کړئ.',
  },
  installStep1Title: {
    en: '1. Download Latest APK',
    ur: '1. تازہ ترین APK ڈاؤن لوڈ کریں',
    ps: '۱. نوې APK فایل ډاونلوډ کړئ',
  },
  installStep1Desc: {
    en: 'Tap on the "Download Latest APK" button to save the Baytul.Ilm.AI (v1.4.4) file to your phone.',
    ur: 'ویب سائٹ کے "تازہ ترین APK ڈاؤن لوڈ کریں" بٹن پر کلک کر کے ورژن 1.4.4 کی فائل اپنے فون میں محفوظ کریں۔',
    ps: 'د "Download Latest APK" تڼۍ کېکاږئ ترڅو د v1.4.4 فایل ستاسو په مبایل کې خوندي شي.',
  },
  installStep2Title: {
    en: '2. Allow Unknown Sources',
    ur: '2. نامعلوم ذرائع سے انسٹالیشن کی اجازت دیں',
    ps: '۲. د نامعلومو سرچینو اجازه ورکړئ',
  },
  installStep2Desc: {
    en: 'When prompted by Android, open Settings and enable "Install unknown apps" for your browser or file manager.',
    ur: 'اگر فون اجازت مانگے تو سیٹنگز میں جا کر براؤزر یا فائل مینیجر کے لیے "Install unknown apps" کو فعال کریں۔',
    ps: 'که موبایل اجازه وغوښته، په ترتیباتو (سیټنګز) کې خپل براوزر یا فایل مینیجر ته د انسټال اجازه فعاله کړئ.',
  },
  installStep3Title: {
    en: '3. Run the Installer',
    ur: '3. فائل پر کلک کر کے انسٹال کریں',
    ps: '۳. ډاونلوډ شوی فایل انسټال کړئ',
  },
  installStep3Desc: {
    en: 'Open your Downloads folder, tap the downloaded APK file, and choose "Install".',
    ur: 'ڈاؤن لوڈز فولڈر کھولیں، ڈاؤن لوڈ شدہ APK فائل پر ٹیپ کریں اور "انسٹال" کا انتخاب کریں۔',
    ps: 'د ډاونلوډونو فولډر پرانیزئ، APK فایل باندې کلیک وکړئ او د "Install" غوراوی وټاکئ.',
  },
  installStep4Title: {
    en: '4. Launch & Study',
    ur: '4. ایپ کھولیں اور مطالعہ شروع کریں',
    ps: '۴. اپلیکیشن پرانیزئ او ګټه واخلئ',
  },
  installStep4Desc: {
    en: 'Launch Baytul Ilm AI to access the Quran, Duas, Dars-e-Nizami books, quizzes, and AI tutor.',
    ur: 'بیت العلم AI کھول کر قرآن مجید، مسنون دعائیں، کتب اور دینی کوئز سے استفادہ شروع کریں۔',
    ps: 'بیت العلم AI پرانیزئ او د قرآن کریم، مسنونو دعاګانو، کتبو او دیني ازموینو څخه زده کړه پیل کړئ.',
  },

  // ==========================================
  // Interactive Showcase Section
  // ==========================================
  interactiveHubBadge: {
    en: 'Core Islamic Features',
    ur: 'اہم اسلامی خصوصیات',
    ps: 'مهم اسلامي خدمتونه',
  },
  interactiveHubTitle: {
    en: 'Explore Core Islamic Features of Baytul Ilm AI',
    ur: 'بیت العلم AI کی بنیادی اسلامی خصوصیات کا مشاہدہ کریں',
    ps: 'د بیت العلم AI د اساسي اسلامي خصوصیاتو کتنه وکړئ',
  },
  interactiveHubSubtitle: {
    en: 'Experience these interactive features in your browser, and download the official Android APK for full offline access.',
    ur: 'ان تمام اسلامی سہولیات کا آن لائن مشاہدہ کریں اور آف لائن استعمال کے لیے آفیشل اینڈرائیڈ APK ڈاؤن لوڈ کریں۔',
    ps: 'دا ټول اسلامي خدمتونه انلاین وازمویئ او د تلپاتې افلاین کارولو لپاره رسمي انډرایډ APK ډاونلوډ کړئ.',
  },

  // ==========================================
  // FAQs Section
  // ==========================================
  faqTitle: {
    en: 'Frequently Asked Questions',
    ur: 'عام طور پر پوچھے جانے والے سوالات',
    ps: 'ډېر پوښتل کېدونکي سوالونه',
  },
  faqSubtitle: {
    en: 'Find clear answers about Baytul Ilm AI, installation, curriculum, and features.',
    ur: 'بیت العلم AI، انسٹالیشن، نصاب اور خصوصیات سے متعلق تفصیلی سوالات کے جوابات۔',
    ps: 'د بیت العلم AI اپلیکیشن، انسټالولو، تعلیمي نصاب او اسانتیاوو په اړه د پوښتنو ځوابونه دلته ومومئ.',
  },

  // ==========================================
  // Footer & Legal
  // ==========================================
  footerNavigation: {
    en: 'Navigation',
    ur: 'اہم صفحات',
    ps: 'مهمې پاڼې',
  },
  footerLegal: {
    en: 'Support & Legal',
    ur: 'رہنمائی و پرائیویسی',
    ps: 'ملاتړ او محرمیت',
  },
  footerSecurity: {
    en: 'Security & Verification',
    ur: 'سیکیورٹی و تصدیق',
    ps: 'امنیت او تصدیق',
  },
  footerCopyright: {
    en: 'Baytul Ilm AI - Comprehensive Android Islamic Education Platform. All rights reserved.',
    ur: 'بیت العلم AI - جامع اسلامی تعلیمی اینڈرائیڈ پلیٹ فارم۔ جملہ حقوق محفوظ ہیں۔',
    ps: 'بیت العلم AI - د اسلامي زده کړو جامع انډرایډ اپلیکیشن. ټول حقونه خوندي دي.',
  },
  footerDisclaimer: {
    en: 'Baytul Ilm AI provides a structured educational index and reference system. Educational materials are presented for academic reference.',
    ur: 'بیت العلم AI ایک باقاعدہ تعلیمی اشاریہ اور حوالہ جاتی نظام فراہم کرتا ہے۔ تمام درسی و تعلیمی کتب خالصتاً دینی و تعلیمی استفادے کے لیے فراہم کی جاتی ہیں۔',
    ps: 'بیت العلم AI یو منظم تعلیمي او مآخذي نظام وړاندې کوي. ټول دیني او علمي مواد یوازې د زده کړې او څېړنې د استفادې لپاره دي.',
  }
};
