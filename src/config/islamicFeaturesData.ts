export interface SurahSample {
  id: number;
  nameArabic: string;
  nameEnglish: string;
  nameUrdu: string;
  namePashto: string;
  versesCount: number;
  revelationType: 'Meccan' | 'Medinan';
  sampleAyahArabic: string;
  sampleAyahEnglish: string;
  sampleAyahUrdu: string;
  sampleAyahPashto: string;
}

export interface MasnoonDuaItem {
  id: string;
  titleEnglish: string;
  titleUrdu: string;
  titlePashto: string;
  category: 'daily' | 'morning_evening' | 'travel' | 'protection' | 'prayer';
  arabic: string;
  transliteration: string;
  translationEnglish: string;
  translationUrdu: string;
  translationPashto: string;
  reference: string;
}

export interface AsmaulHusnaItem {
  number: number;
  arabic: string;
  transliteration: string;
  meaningEnglish: string;
  meaningUrdu: string;
  meaningPashto: string;
  benefitUrdu: string;
}

export interface AsmaunNabiItem {
  number: number;
  arabic: string;
  transliteration: string;
  meaningEnglish: string;
  meaningUrdu: string;
  meaningPashto: string;
  honorific: string;
  virtueUrdu: string;
}

export interface QiblaCity {
  cityName: string;
  cityNameUrdu: string;
  cityNamePashto: string;
  country: string;
  qiblaAngle: number; // Degrees from North
  distanceKm: number;
}

export interface TasbeehZikr {
  id: string;
  arabic: string;
  transliteration: string;
  meaningUrdu: string;
  meaningPashto: string;
  meaningEnglish: string;
  defaultTarget: number;
}

// 1. Quran Sample Surahs
export const QURAN_SAMPLES: SurahSample[] = [
  {
    id: 1,
    nameArabic: 'الفاتحة',
    nameEnglish: 'Al-Fatihah (The Opening)',
    nameUrdu: 'سورۃ الفاتحہ',
    namePashto: 'سورت الفاتحه',
    versesCount: 7,
    revelationType: 'Meccan',
    sampleAyahArabic: 'بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ • الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ • الرَّحْمَٰنِ الرَّحِيمِ • مَالِكِ يَوْمِ الدِّينِ',
    sampleAyahEnglish: 'In the name of Allah, the Entirely Merciful, the Especially Merciful. [All] praise is [due] to Allah, Lord of the worlds.',
    sampleAyahUrdu: 'شروع اللہ کے نام سے جو بڑا مہربان نہایت رحم والا ہے۔ سب تعریفیں اللہ کے لیے ہیں جو تمام جہانوں کا پالنے والا ہے۔',
    sampleAyahPashto: 'د الله په نامه سره چې ډېر زیات مهربان، بې حده رحم کوونکی دی. ټولې ستاینې یوازې د الله تعالی لپاره دي چې د ټولو جهانونو پالونکی دی.'
  },
  {
    id: 36,
    nameArabic: 'يس',
    nameEnglish: 'Ya-Sin',
    nameUrdu: 'سورۃ یٰسٓ',
    namePashto: 'سورت یٰسٓ',
    versesCount: 83,
    revelationType: 'Meccan',
    sampleAyahArabic: 'يسٓ • وَالْقُرْآنِ الْحَكِيمِ • إِنَّكَ لَمِنَ الْمُرْسَلِينَ • عَلَىٰ صِرَاطٍ مُّسْتَقِيمٍ',
    sampleAyahEnglish: 'Ya, Seen. By the wise Quran. Indeed you, [O Muhammad], are from among the messengers, On a straight path.',
    sampleAyahUrdu: 'یٰسٓ۔ قسم ہے قرآن حکمت والے کی۔ بے شک آپ ضرور رسولوں میں سے ہیں، سیدھے راستے پر۔',
    sampleAyahPashto: 'یٰسٓ. قسم دی په حکمت ډک قرآن. بې شکه ته خامخا له استازو (رسولانو) څخه یې، په سمه او نېغه لاره باندې.'
  },
  {
    id: 55,
    nameArabic: 'الرحمن',
    nameEnglish: 'Ar-Rahman (The Beneficent)',
    nameUrdu: 'سورۃ الرحمٰن',
    namePashto: 'سورت الرحمٰن',
    versesCount: 78,
    revelationType: 'Medinan',
    sampleAyahArabic: 'الرَّحْمَٰنُ • عَلَّمَ الْقُرْآنَ • خَلَقَ الْإِنسَانَ • عَلَّمَهُ الْبَيَانَ • فَبِأَيِّ آلَاءِ رَبِّكُمَا تُكَذِّبَانِ',
    sampleAyahEnglish: 'The Most Merciful. Taught the Quran. Created man. [And] taught him speech. So which of the favors of your Lord would you deny?',
    sampleAyahUrdu: 'نہایت مہربان خدا نے۔ قرآن سکھایا۔ اسی نے انسان کو پیدا کیا۔ اسے بولنا سکھایا۔ پس تم اپنے پروردگار کی کون کون سی نعمتوں کو جھٹلاؤ گے؟',
    sampleAyahPashto: 'ډېر زیات مهربان (الله) قرآن وښود. انسان یې پیدا کړ. هغه ته یې بیان او خبرې ور زده کړې. نو تاسو د خپل رب د کومو کومو نعمتونو انکار کوئ؟'
  },
  {
    id: 67,
    nameArabic: 'الملك',
    nameEnglish: 'Al-Mulk (The Sovereignty)',
    nameUrdu: 'سورۃ الملک',
    namePashto: 'سورت الملک',
    versesCount: 30,
    revelationType: 'Meccan',
    sampleAyahArabic: 'تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ • الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا',
    sampleAyahEnglish: 'Blessed is He in whose hand is dominion, and He is over all things competent - [He] who created death and life to test you as to which of you is best in deed.',
    sampleAyahUrdu: 'بڑی برکت والی ہے وہ ذات جس کے ہاتھ میں ساری بادشاہی ہے اور وہ ہر چیز پر قادر ہے۔ جس نے موت اور زندگی کو پیدا کیا تاکہ تمہیں آزمائے کہ تم میں کس کے عمل اچھے ہیں۔',
    sampleAyahPashto: 'ډېر با برکته دی هغه ذات چې پاچاهي د هغه په لاس کښې ده او هغه په هر څه قادر دی. هغه ذات چې مرګ او ژوند یې پیدا کړ ترڅو تاسو وازمویي چې څوک غوره عمل کوونکي دي.'
  },
  {
    id: 112,
    nameArabic: 'الإخلاص',
    nameEnglish: 'Al-Ikhlas (Purity of Faith)',
    nameUrdu: 'سورۃ الاخلاص',
    namePashto: 'سورت الاخلاص',
    versesCount: 4,
    revelationType: 'Meccan',
    sampleAyahArabic: 'قُلْ هُوَ اللَّهُ أَحَدٌ • اللَّهُ الصَّمَدُ • لَمْ يَلِدْ وَلَمْ يُولَدْ • وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ',
    sampleAyahEnglish: 'Say, "He is Allah, [who is] One, Allah, the Eternal Refuge. He neither begets nor is born, Nor is there to Him any equivalent."',
    sampleAyahUrdu: 'آپ فرما دیجیے کہ وہ اللہ ایک ہے۔ اللہ بے نیاز ہے۔ نہ اس کی کوئی اولاد ہے اور نہ وہ کسی کی اولاد ہے۔ اور نہ کوئی اس کا ہمسر ہے۔',
    sampleAyahPashto: 'ووایه چې هغه الله یو او یکتا دی. الله بې نیازه دی. نه یې څوک زېږولي او نه له چا زېږېدلی دی. او نه هم د هغه کوم سیال او برابر شته.'
  }
];

// 2. Masnoon Duas
export const MASNOON_DUAS: MasnoonDuaItem[] = [
  {
    id: 'dua-morning',
    titleEnglish: 'Morning Remembrance',
    titleUrdu: 'صبح کے وقت کی مسنون دعا',
    titlePashto: 'د سهار مسنونه دعا',
    category: 'morning_evening',
    arabic: 'أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ',
    transliteration: "Asbahna wa-asbahal-mulku lillah, wal-hamdu lillah, la ilaha illallahu wahdahu la shareeka lah",
    translationEnglish: 'We have reached the morning and the kingdom belongs to Allah, and all praise is for Allah. None has the right to be worshipped except Allah alone, without any partner.',
    translationUrdu: 'ہم نے صبح کی اور اللہ کے سارے ملک نے صبح کی، اور تمام تعریفیں اللہ ہی کے لیے ہیں، اللہ کے سوا کوئی معبود نہیں، وہ اکیلا ہے، اس کا کوئی شریک نہیں۔',
    translationPashto: 'موږ سهار کړ او د الله ټولې پاچاهۍ سهار کړ، او ټولې ستاینې الله لره دي، بې له الله بل هیڅ معبود نشته، هغه یو دی او شریک نه لري.',
    reference: 'صحیح مسلم: 2723'
  },
  {
    id: 'dua-evening',
    titleEnglish: 'Evening Remembrance',
    titleUrdu: 'شام کے وقت کی مسنون دعا',
    titlePashto: 'د ماښام مسنونه دعا',
    category: 'morning_evening',
    arabic: 'أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ',
    transliteration: "Amsayna wa-amsal-mulku lillah, wal-hamdu lillah, la ilaha illallahu wahdahu la shareeka lah",
    translationEnglish: 'We have reached the evening and the kingdom belongs to Allah, and all praise is for Allah. None has the right to be worshipped except Allah alone, without partner.',
    translationUrdu: 'ہم نے شام کی اور اللہ کے سارے ملک نے شام کی، اور تمام تعریفیں اللہ ہی کے لیے ہیں، اللہ کے سوا کوئی معبود نہیں، وہ اکیلا ہے، اس کا کوئی شریک نہیں۔',
    translationPashto: 'موږ ماښام کړ او د الله ټولې پاچاهۍ ماښام کړ، او ټولې ستاینې الله لره دي، بې له الله بل هیڅ معبود نشته، هغه یو دی او شریک نه لري.',
    reference: 'صحیح مسلم: 2723'
  },
  {
    id: 'dua-sleep',
    titleEnglish: 'Before Sleeping',
    titleUrdu: 'سوتے وقت کی مسنون دعا',
    titlePashto: 'د خوب کولو مسنونه دعا',
    category: 'daily',
    arabic: 'بِاسْمِكَ رَبِّي وَضَعْتُ جَنْبِي، وَبِكَ أَرْفَعُهُ، فَإِنْ أَمْسَكْتَ نَفْسِي فَارْحَمْهَا، وَإِنْ أَرْسَلْتَهَا فَاحْفَظْهَا',
    transliteration: "Bismika Rabbi wada'tu janbi, wa bika arfa'uh, fa-in amsakta nafsi farhamha, wa-in arsaltaha fahfazha",
    translationEnglish: 'In Your name my Lord, I lie down and in Your name I rise. If You take my soul, have mercy upon it, and if You release it, protect it.',
    translationUrdu: 'اے میرے رب! تیرے ہی نام کے ساتھ میں نے اپنا پہلو رکھا اور تیرے ہی نام کے ساتھ میں اسے اٹھاؤں گا۔ اگر تو میری روح روک لے تو اس پر رحم فرما اور اگر تو اسے چھوڑ دے تو اس کی حفاظت فرما۔',
    translationPashto: 'ای زما ربه! ستا په نامه مې خپله ډډه کېښوده او ستا په نامه به یې پورته کوم، که زما روح دې واخیست نو ورباندې رحم وکړه او که دې پرېښود نو خوندیتوب یې وکړه.',
    reference: 'صحیح البخاری: 6320'
  },
  {
    id: 'dua-travel',
    titleEnglish: 'Dua for Traveling',
    titleUrdu: 'سفر کی مسنون دعا',
    titlePashto: 'د سفر مسنونه دعا',
    category: 'travel',
    arabic: 'سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَٰذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ وَإِنَّا إِلَىٰ رَبِّنَا لَمُنقَلِبُونَ',
    transliteration: "Subhanalladhi sakh-khara lana hadha wa ma kunna lahu muqrineen, wa inna ila Rabbina lamunqaliboon",
    translationEnglish: 'Glory to Him who has brought this into our service, whereas we would not have been able to subdue it, and to our Lord we shall surely return.',
    translationUrdu: 'پاک ہے وہ ذات جس نے ہمارے لیے اس سواری کو مسخر کیا حالانکہ ہم اسے قابو کرنے والے نہ تھے، اور یقیناً ہم اپنے رب ہی کی طرف لوٹنے والے ہیں۔',
    translationPashto: 'پاک دی هغه ذات چې دا سوارلي یې موږ ته مسخره او تابع کړه، موږ د دې قابو کوونکي نه وو، او بې شکه موږ د خپل رب لوري ته ستنېدونکي یو.',
    reference: 'جامع الترمذی: 3446'
  },
  {
    id: 'dua-protection',
    titleEnglish: 'Dua for Protection from Evils',
    titleUrdu: 'حفاظت اور آفات سے پناہ کی دعا',
    titlePashto: 'له بلاګانو او شرونو د خوندیتوب دعا',
    category: 'protection',
    arabic: 'بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ',
    transliteration: "Bismillahilladhi la yadurru ma'asmihi shay'un fil-ardi wa la fis-samaa'i wa huwas-Sami'ul-'Aleem",
    translationEnglish: 'In the Name of Allah, with Whose Name nothing on earth or in the heaven can cause harm, and He is the All-Hearing, the All-Knowing.',
    translationUrdu: 'اللہ کے نام سے جس کے نام کی برکت سے زمین اور آسمان میں کوئی چیز نقصان نہیں پہنچا سکتی اور وہی خوب سننے والا جاننے والا ہے۔',
    translationPashto: 'د هغه الله په نامه چې د هغه له نامه سره نه په ځمکه او نه په اسمان کښې هیڅ شی زیان رسولی شي او هغه ښه اورېدونکی او پوه دی.',
    reference: 'سنن ابی داود: 5088'
  },
  {
    id: 'dua-knowledge',
    titleEnglish: 'Dua for Increase in Knowledge',
    titleUrdu: 'علم میں برکت و اضافے کی دعا',
    titlePashto: 'په علم کښې د زیاتوالي قرآني دعا',
    category: 'daily',
    arabic: 'رَّبِّ زِدْنِي عِلْمًا',
    transliteration: "Rabbi zidnee 'ilma",
    translationEnglish: 'My Lord, increase me in knowledge.',
    translationUrdu: 'اے میرے پروردگار! میرے علم میں اضافہ فرما۔',
    translationPashto: 'ای زما ربه! زما په علم کښې زیاتوالی راوله.',
    reference: 'سورۃ طٰہٰ: 114'
  }
];

// 3. 99 Names of Allah (Asma-ul-Husna) Selection
export const ASMA_UL_HUSNA: AsmaulHusnaItem[] = [
  {
    number: 1,
    arabic: 'الرَّحْمَٰنُ',
    transliteration: 'Ar-Rahmaan',
    meaningEnglish: 'The Entirely Merciful',
    meaningUrdu: 'بے حد رحم فرمانے والا',
    meaningPashto: 'ډېر زیات مهربان',
    benefitUrdu: 'ہر فرض نماز کے بعد 100 مرتبہ پڑھنے سے دل کا نفاق اور غفلت دور ہوتی ہے۔'
  },
  {
    number: 2,
    arabic: 'الرَّحِيمُ',
    transliteration: 'Ar-Raheem',
    meaningEnglish: 'The Especially Merciful',
    meaningUrdu: 'نہایت مہربان، دائمی رحمت والا',
    meaningPashto: 'بې حده رحم کوونکی',
    benefitUrdu: 'روزانہ پڑھنے سے تمام دنیاوی و دینی آفات سے حفاظت رہتی ہے۔'
  },
  {
    number: 3,
    arabic: 'الْمَلِكُ',
    transliteration: 'Al-Malik',
    meaningEnglish: 'The Absolute Sovereign Ruler',
    meaningUrdu: 'حقیقی بادشاہ اور کائنات کا حاکم',
    meaningPashto: 'حقیقي پاچا او واکمن',
    benefitUrdu: 'کثرت سے پڑھنے والا محتاجی اور غربت سے محفوظ رہتا ہے۔'
  },
  {
    number: 4,
    arabic: 'الْقُدُّوسُ',
    transliteration: 'Al-Quddoos',
    meaningEnglish: 'The Most Sacred and Pure',
    meaningUrdu: 'تمام عیوب اور نقائص سے پاک',
    meaningPashto: 'له ټولو عیبونو پاک',
    benefitUrdu: 'روزانہ پڑھنے سے دل روحانی بیماریوں اور وسوسوں سے پاک ہوتا ہے۔'
  },
  {
    number: 5,
    arabic: 'السَّلَامُ',
    transliteration: 'As-Salaam',
    meaningEnglish: 'The Source of Peace and Safety',
    meaningUrdu: 'سلامتی دینے والا، امن کا سرچشمہ',
    meaningPashto: 'سلامتي او امن ورکوونکی',
    benefitUrdu: 'بیمار پر 115 مرتبہ پڑھ کر دم کرنے سے شفا حاصل ہوتی ہے۔'
  },
  {
    number: 6,
    arabic: 'الْمُؤْمِنُ',
    transliteration: "Al-Mu'min",
    meaningEnglish: 'The Granter of Security and Faith',
    meaningUrdu: 'امن و امان بخشنے والا، ایمان عطا کرنے والا',
    meaningPashto: 'امن او ایمان بخښونکی',
    benefitUrdu: 'خوف کے وقت پڑھنے سے مکمل امن اور تسلی نصیب ہوتی ہے۔'
  },
  {
    number: 7,
    arabic: 'الْمُهَيْمِنُ',
    transliteration: 'Al-Muhaymin',
    meaningEnglish: 'The Guardian and Overseer',
    meaningUrdu: 'سب کا نگہبان اور محافظ',
    meaningPashto: 'ساتونکی او څارونکی',
    benefitUrdu: 'غسل کے بعد 115 بار پڑھنے سے باطنی نور نصیب ہوتا ہے۔'
  },
  {
    number: 8,
    arabic: 'الْعَزِيزُ',
    transliteration: "Al-'Azeez",
    meaningEnglish: 'The All-Mighty and Invincible',
    meaningUrdu: 'سب پر غالب، زبردست قوت والا',
    meaningPashto: 'غالب او عزتمن',
    benefitUrdu: '40 دن تک روزانہ 40 بار پڑھنے سے اللہ تعالی عزت و خودداری عطا فرماتا ہے۔'
  },
  {
    number: 9,
    arabic: 'الْجَبَّارُ',
    transliteration: 'Al-Jabbaar',
    meaningEnglish: 'The Restorer and Compeller',
    meaningUrdu: 'ٹوٹے دلوں کو جوڑنے والا، زبردست بااختیار',
    meaningPashto: 'زورور او سموونکی',
    benefitUrdu: 'صبح و شام 226 بار پڑھنے سے ظالموں کے شر سے نجات ملتی ہے۔'
  },
  {
    number: 10,
    arabic: 'الْمُتَكَبِّرُ',
    transliteration: 'Al-Mutakabbir',
    meaningEnglish: 'The Supreme and Majestic',
    meaningUrdu: 'بڑائی اور عظمت والا',
    meaningPashto: 'لوړ او عظمت والا',
    benefitUrdu: 'نیک کام کے آغاز پر پڑھنے سے اس میں کامیابی اور برکت ہوتی ہے۔'
  },
  {
    number: 11,
    arabic: 'الْخَالِقُ',
    transliteration: 'Al-Khaaliq',
    meaningEnglish: 'The Creator of All Things',
    meaningUrdu: 'عدم سے وجود بخشنے والا پیدا فرمانے والا',
    meaningPashto: 'پیدا کوونکی',
    benefitUrdu: 'رات کو تلاوت کرنے والے کے لیے فرشتے نیکی لکھتے ہیں۔'
  },
  {
    number: 12,
    arabic: 'الْغَفَّارُ',
    transliteration: 'Al-Ghaffaar',
    meaningEnglish: 'The All-Forgiving',
    meaningUrdu: 'گناہوں کو بار بار بخشنے والا',
    meaningPashto: 'ډېر زیات بخښونکی',
    benefitUrdu: 'جمعہ کے بعد 100 مرتبہ پڑھنے سے گناہوں کی مغفرت ہوتی ہے۔'
  },
  {
    number: 13,
    arabic: 'الْوَهَّابُ',
    transliteration: 'Al-Wahhaab',
    meaningEnglish: 'The Supreme Bestower',
    meaningUrdu: 'بے انتہا عطا فرمانے والا داتا',
    meaningPashto: 'ډېر زیات عطا کوونکی',
    benefitUrdu: 'سجدے میں 14 بار پڑھنے سے رزق اور کشادگی نصیب ہوتی ہے۔'
  },
  {
    number: 14,
    arabic: 'الرَّزَّاقُ',
    transliteration: 'Ar-Razzaaq',
    meaningEnglish: 'The Ultimate Provider',
    meaningUrdu: 'تمام مخلوق کو روزی دینے والا',
    meaningPashto: 'روزي رسوونکی',
    benefitUrdu: 'صبح کے وقت گھر کے چاروں کونوں میں 10-10 بار پڑھنے سے رزق میں برکت ہوتی ہے۔'
  },
  {
    number: 15,
    arabic: 'الْفَتَّاحُ',
    transliteration: 'Al-Fattaah',
    meaningEnglish: 'The Supreme Opener of Doors',
    meaningUrdu: 'مشکلات کھولنے والا، فتح و نصرت دینے والا',
    meaningPashto: 'لارې پرانیستونکی او بری ورکوونکی',
    benefitUrdu: 'فجر کے بعد دونوں ہاتھ سینے پر رکھ کر 71 بار پڑھنے سے دل کا زنگ دور ہوتا ہے۔'
  },
  {
    number: 16,
    arabic: 'الْعَلِيمُ',
    transliteration: "Al-'Aleem",
    meaningEnglish: 'The All-Knowing',
    meaningUrdu: 'سب کچھ جاننے والا، ازل سے ابد تک کا باخبر',
    meaningPashto: 'په هر څه پوهېدونکی',
    benefitUrdu: 'کثرت سے پڑھنے سے اللہ تعالیٰ علم و حکمت کے دروازے کھولتا ہے۔'
  }
];

// 4. Blessed Names of Prophet Muhammad ﷺ (Asma-un-Nabi)
export const ASMA_UN_NABI: AsmaunNabiItem[] = [
  {
    number: 1,
    arabic: 'مُحَمَّدٌ ﷺ',
    transliteration: 'Muhammad ﷺ',
    meaningEnglish: 'The Praised One',
    meaningUrdu: 'جس کی کثرت سے تعریف کی گئی ہو',
    meaningPashto: 'هغه چې ډېره زیاته ستاینه یې شوې ده',
    honorific: 'رسول الله و حبیب الله',
    virtueUrdu: 'اسم گرامی پر درود بھیجنے سے 10 گناہ معاف اور 10 درجات بلند ہوتے ہیں۔'
  },
  {
    number: 2,
    arabic: 'أَحْمَدُ ﷺ',
    transliteration: 'Ahmad ﷺ',
    meaningEnglish: 'The Most Praising',
    meaningUrdu: 'اللہ کی سب سے زیادہ حمد و ثناء بیان کرنے والے',
    meaningPashto: 'د الله ډېر زیات ثنا ویونکی',
    honorific: 'صاحب لواء الحمد',
    virtueUrdu: 'انجیل شریف میں سیدنا عیسیٰ علیہ السلام نے اسی مبارک نام سے بشارت دی۔'
  },
  {
    number: 3,
    arabic: 'الْمَاحِي ﷺ',
    transliteration: 'Al-Mahi ﷺ',
    meaningEnglish: 'The Obliterator of Disbelief',
    meaningUrdu: 'وہ مبارک ہستی جن کے ذریعے اللہ نے کفر و شرک کو مٹایا',
    meaningPashto: 'هغه چې کفر او تیاره یې له منځه یوړه',
    honorific: 'فاتح مکہ و مزیل الشرک',
    virtueUrdu: 'صحیح بخاری کی حدیث میں حضور ﷺ نے خود اس اسم مبارک کی صراحت فرمائی۔'
  },
  {
    number: 4,
    arabic: 'الْحَاشِرُ ﷺ',
    transliteration: 'Al-Hashir ﷺ',
    meaningEnglish: 'The Gatherer',
    meaningUrdu: 'جن کے مبارک قدموں کے پیچھے تمام لوگوں کا حشر ہوگا',
    meaningPashto: 'هغه چې خلک به یې تر شا راټولیږي',
    honorific: 'امام یوم القیامہ',
    virtueUrdu: 'قیامت کے دن اولین و آخرین آپ ﷺ کی شفاعت کے متلاشی ہوں گے۔'
  },
  {
    number: 5,
    arabic: 'الْعَاقِبُ ﷺ',
    transliteration: 'Al-Aqib ﷺ',
    meaningEnglish: 'The Ultimate Last Prophet',
    meaningUrdu: 'سب کے بعد تشریف لانے والے، جن کے بعد کوئی نیا نبی نہیں',
    meaningPashto: 'وروستی پیغمبر چې وروسته ترې بل پیغمبر نه راځي',
    honorific: 'خاتم النبیین و سید المرسلین',
    virtueUrdu: 'عقیدہ ختم نبوت کا عظیم الشان مظہر اور امت کے لیے شفاعت کا ضامن۔'
  },
  {
    number: 6,
    arabic: 'رَحْمَةٌ لِّلْعَالَمِينَ ﷺ',
    transliteration: 'Rahmatul-lil-Alameen ﷺ',
    meaningEnglish: 'Mercy unto the Worlds',
    meaningUrdu: 'تمام جہانوں کے لیے مجسم رحمت',
    meaningPashto: 'د ټولو نړیو لپاره لویه او بې سارې رحمت',
    honorific: 'شفیع المذنبین',
    virtueUrdu: 'قرآن مجید کا ارشاد: وَمَا أَرْسَلْنَاكَ إِلَّا رَحْمَةً لِّلْعَالَمِينَ۔'
  },
  {
    number: 7,
    arabic: 'الشَّاهِدُ وَالْمُبَشِّرُ وَالنَّذِيرُ ﷺ',
    transliteration: 'Ash-Shahid, Al-Mubashir, An-Nadheer ﷺ',
    meaningEnglish: 'The Witness, Bringer of Good Tidings, and Warner',
    meaningUrdu: 'گواہی دینے والے، خوشخبری سنانے والے اور خبردار فرمانے والے',
    meaningPashto: 'ګواهي ورکوونکی، زېری ورکوونکی او وېروونکی',
    honorific: 'سراج منیر',
    virtueUrdu: 'امت کو جنت کی بشارت اور جہنم کی ہلاکت سے بچانے والے مشفق ہادی۔'
  },
  {
    number: 8,
    arabic: 'طٰهٰ وَ يٰسٓ ﷺ',
    transliteration: 'Taha & Yasin ﷺ',
    meaningEnglish: 'Pure Purifier & Master of Humanity',
    meaningUrdu: 'پاکیزہ و مصفٰی، بنی نوع انسان کے سردار',
    meaningPashto: 'پاک او د بشریت ستر سالار',
    honorific: 'صاحب التاج و المعراج',
    virtueUrdu: 'قرآن مجید کے مقدس حروف مقطعات جن سے اللہ نے اپنے حبیب ﷺ کو مخاطب فرمایا۔'
  }
];

// 5. Haramain Live Streams
export const HARAMAIN_STREAMS = [
  {
    id: 'makkah-live',
    title: 'Makkah Live Stream',
    titleUrdu: 'مکہ مکرمہ - مسجد الحرام لائیو',
    titlePashto: 'مکه مکرمه - مسجد الحرام لایو',
    location: 'Masjid al-Haram, Makkah Mukarramah',
    locationArabic: 'المسجد الحرام - مكة المكرمة',
    descriptionUrdu: 'خانہ کعبہ اور طواف کی 24 گھنٹے براہِ راست باقاعدہ لائیو نشریات۔',
    descriptionPashto: 'د کعبې شریفې او طواف ۲۴ ساعته مستقیمې ژوندۍ خپرونې.',
    streamEmbedUrl: 'https://win.holol.com/live/quran/index.m3u8',
    officialUrl: 'https://win.holol.com/live/quran/index.m3u8',
    isLive: true
  },
  {
    id: 'madinah-live',
    title: 'Madinah Live Stream',
    titleUrdu: 'مدینہ منورہ - مسجد نبوی شریف لائیو',
    titlePashto: 'مدینه منوره - مسجد نبوي شریف لایو',
    location: 'Masjid an-Nabawi, Madinah Munawwarah',
    locationArabic: 'المسجد النبوي الشريف - المدينة المنورة',
    descriptionUrdu: 'روضہ اطہر اور گنبد خضریٰ کی براہِ راست ایچ ڈی کوالٹی لائیو نشریات۔',
    descriptionPashto: 'د روضې مبارکې او شنه ګنبد ۲۴ ساعته مستقیمې ژوندۍ خپرونې.',
    streamEmbedUrl: 'https://win.holol.com/live/sunnah/index.m3u8',
    officialUrl: 'https://win.holol.com/live/sunnah/index.m3u8',
    isLive: true
  }
];

// 6. Qibla Compass Cities
export const QIBLA_CITIES: QiblaCity[] = [
  { cityName: 'Islamabad', cityNameUrdu: 'اسلام آباد', cityNamePashto: 'اسلام اباد', country: 'Pakistan', qiblaAngle: 257, distanceKm: 3570 },
  { cityName: 'Lahore', cityNameUrdu: 'لاہور', cityNamePashto: 'لاهور', country: 'Pakistan', qiblaAngle: 260, distanceKm: 3680 },
  { cityName: 'Karachi', cityNameUrdu: 'کراچی', cityNamePashto: 'کراچۍ', country: 'Pakistan', qiblaAngle: 271, distanceKm: 2790 },
  { cityName: 'Peshawar', cityNameUrdu: 'پشاور', cityNamePashto: 'پېښور', country: 'Pakistan', qiblaAngle: 254, distanceKm: 3450 },
  { cityName: 'Quetta', cityNameUrdu: 'کوئٹہ', cityNamePashto: 'کوټه', country: 'Pakistan', qiblaAngle: 264, distanceKm: 2980 },
  { cityName: 'Kabul', cityNameUrdu: 'کابل', cityNamePashto: 'کابل', country: 'Afghanistan', qiblaAngle: 251, distanceKm: 3290 },
  { cityName: 'Kandahar', cityNameUrdu: 'قندھار', cityNamePashto: 'کندهار', country: 'Afghanistan', qiblaAngle: 254, distanceKm: 2940 },
  { cityName: 'London', cityNameUrdu: 'لندن', cityNamePashto: 'لندن', country: 'United Kingdom', qiblaAngle: 119, distanceKm: 4790 },
  { cityName: 'Dubai', cityNameUrdu: 'دبئی', cityNamePashto: 'دوبۍ', country: 'UAE', qiblaAngle: 258, distanceKm: 1680 },
  { cityName: 'Istanbul', cityNameUrdu: 'استنبول', cityNamePashto: 'استنبول', country: 'Turkey', qiblaAngle: 153, distanceKm: 2410 }
];

// 7. Tasbeeh Azkar Presets
export const TASBEEH_PRESETS: TasbeehZikr[] = [
  {
    id: 'subhanallah',
    arabic: 'سُبْحَانَ اللَّهِ',
    transliteration: 'SubhanAllah',
    meaningUrdu: 'اللہ ہر عیب و نقص سے پاک ہے',
    meaningPashto: 'الله تعالی له هر عیب څخه پاک دی',
    meaningEnglish: 'Glory be to Allah',
    defaultTarget: 33
  },
  {
    id: 'alhamdulillah',
    arabic: 'الْحَمْدُ لِلَّهِ',
    transliteration: 'Alhamdulillah',
    meaningUrdu: 'تمام تعریفیں اللہ ہی کے لیے ہیں',
    meaningPashto: 'ټولې ستاینې یوازې الله لره دي',
    meaningEnglish: 'All praise is due to Allah',
    defaultTarget: 33
  },
  {
    id: 'allahuakbar',
    arabic: 'اللَّهُ أَكْبَرُ',
    transliteration: 'Allahu Akbar',
    meaningUrdu: 'اللہ سب سے بڑا اور برتر ہے',
    meaningPashto: 'الله تعالی تر ټولو لوی دی',
    meaningEnglish: 'Allah is the Greatest',
    defaultTarget: 34
  },
  {
    id: 'astaghfirullah',
    arabic: 'أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ',
    transliteration: 'Astaghfirullaha wa atoobu ilayh',
    meaningUrdu: 'میں اللہ سے مغفرت طلب کرتا ہوں اور اسی کی طرف رجوع کرتا ہوں',
    meaningPashto: 'زه له الله بخښنه غواړم او هغه ته توبه باسم',
    meaningEnglish: 'I seek forgiveness from Allah and repent to Him',
    defaultTarget: 100
  },
  {
    id: 'durood',
    arabic: 'اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ',
    transliteration: "Allahumma salli 'ala Muhammadin wa 'ala aali Muhammad",
    meaningUrdu: 'اے اللہ! رحمت نازل فرما محمد ﷺ پر اور ان کی آل پر',
    meaningPashto: 'ای الله! رحمت ولېږه په محمد ﷺ او د هغه په کورنۍ باندې',
    meaningEnglish: 'O Allah, bestow peace upon Muhammad and the family of Muhammad',
    defaultTarget: 100
  },
  {
    id: 'tahlil',
    arabic: 'لَا إِلَٰهَ إِلَّا اللَّهُ مُحَمَّدٌ رَّسُولُ اللَّهِ',
    transliteration: 'La ilaha illallahu Muhammadur Rasulullah',
    meaningUrdu: 'اللہ کے سوا کوئی معبود نہیں، محمد ﷺ اللہ کے رسول ہیں',
    meaningPashto: 'بې له الله بل هیڅ معبود نشته، محمد ﷺ د الله استازی دی',
    meaningEnglish: 'There is no deity worthy of worship except Allah, Muhammad is the Messenger of Allah',
    defaultTarget: 100
  }
];

// 8. Islamic Library Books Overview
export const LIBRARY_CLASSICS = [
  {
    categoryUrdu: 'علم الصرف و النحو',
    categoryPashto: 'د صرف او نحو علم',
    categoryEnglish: 'Arabic Grammar & Morphology',
    books: [
      { name: 'شرح مائۃ عامل (Sharh Miat Amil)', author: 'الشیخ عبد القاہر الجرجانی', level: 'درجہ اولیٰ' },
      { name: 'ہدایۃ النحو (Hidayat-un-Nahw)', author: 'الشیخ سراج الدین عثمان', level: 'درجہ ثانیہ' },
      { name: 'الکافیہ (Al-Kafiyah)', author: 'ابن الحاجب النحوی', level: 'درجہ ثالثہ' },
      { name: 'علم الصیغہ (Ilm-us-Seegha)', author: 'مفتی عنایت احمد کاکوروی', level: 'درجہ اولیٰ' }
    ]
  },
  {
    categoryUrdu: 'فقہ و اصول الفقہ',
    categoryPashto: 'فقه او اصول الفقه',
    categoryEnglish: 'Islamic Jurisprudence & Legal Theory',
    books: [
      { name: 'نور الایضاح (Nur al-Idah)', author: 'امام حسن بن عمار الشرنبلالی', level: 'درجہ اولیٰ' },
      { name: 'مختصر القدوری (Mukhtasar al-Quduri)', author: 'امام ابو الحسین احمد القدوری', level: 'درجہ ثانیہ' },
      { name: 'الہدایہ (Al-Hidayah)', author: 'امام برہان الدین المرغینانی', level: 'درجہ سابعہ' },
      { name: 'اصول الشاشی (Usul ash-Shashi)', author: 'امام نظام الدین الشاشی', level: 'درجہ ثالثہ' }
    ]
  },
  {
    categoryUrdu: 'علوم الحدیث و سنن',
    categoryPashto: 'د حدیثو علوم او سنن',
    categoryEnglish: 'Hadith Sciences & Canonical Compilations',
    books: [
      { name: 'صحیح البخاری (Sahih al-Bukhari)', author: 'امام محمد بن اسماعیل البخاری', level: 'دورۂ حدیث' },
      { name: 'صحیح مسلم (Sahih Muslim)', author: 'امام مسلم بن الحجاج النیسابوری', level: 'دورۂ حدیث' },
      { name: 'مشکاۃ المصابیح (Mishkat al-Masabih)', author: 'علامہ ولی الدین التبریزی', level: 'درجہ سادسہ' },
      { name: 'نخبۃ الفکر (Nukhbat al-Fikar)', author: 'حافظ ابن حجر العسقلانی', level: 'درجہ خامسہ' }
    ]
  }
];
