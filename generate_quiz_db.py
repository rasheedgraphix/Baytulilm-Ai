import json

# Comprehensive database of authentic Dars-e-Nizami questions for all 40 chapters
# Structure: stepId, chapterNum, list of questions
# Each question: questionUr, questionEn, arabic, correctUr, correctEn, wrongUr (list of 3), wrongEn (list of 3), expUr, expEn, bookName, darjaUrdu, darjaEn, subject, citation

all_chapters = []

# ==========================================
# STEP 1: BEGINNER (درجہ اولیٰ و درجہ ثانیہ)
# ==========================================

# 1. میزان الصرف
all_chapters.append(('beginner', 1, [
    (
        "ثلاثی مجرد کے کل کتنے مشہور ابواب ہیں؟",
        "How many canonical patterns/doors are there in Thulathi Mujarrad?",
        "أَبْوَابُ الثُّلَاثِيِّ الْمُجَرَّدِ سِتَّةٌ",
        "6 ابواب (نصر، ضرب، سمع، فتح، کرم، حسب)",
        "6 Doors (Nasara, Daraba, Sami'a, Fataha, Karuma, Hasiba)",
        ["4 ابواب", "8 ابواب", "12 ابواب"],
        ["4 Doors", "8 Doors", "12 Doors"],
        "ثلاثی مجرد کے بنیادی 6 ابواب ہیں جن کے اوزان عین کلمہ کی حرکات کے بدلنے سے بنتے ہیں۔",
        "Thulathi Mujarrad consists of exactly 6 main patterns determined by Ayn Kalimah vowels.",
        "میزان الصرف", "درجہ اولیٰ", "Darja-e-Ula", "Sarf", "میزان الصرف — بحث ابواب ثلاثی مجرد"
    ),
    (
        "باب نصر ینصر کی ماضی اور مضارع میں عین کلمہ کی حرکت کیا ہوتی ہے؟",
        "What are the vowels on Ayn Kalimah in Bab Nasara-Yansuru?",
        "نَصَرَ يَنْصُرُ (فَعَلَ يَفْعُلُ)",
        "ماضی میں زبر اور مضارع میں پیش (فَعَلَ يَفْعُلُ)",
        "Fatha in past and Damma in present",
        ["ماضی میں زیر اور مضارع میں زبر", "ماضی و مضارع دونوں میں پیش", "ماضی میں پیش اور مضارع میں زبر"],
        ["Kasra in past and Fatha in present", "Damma in both", "Damma in past and Fatha in present"],
        "باب نصر میں ماضی مفتوح العین اور مضارع مضموم العین ہوتا ہے۔",
        "Bab Nasara has Fatha in past and Damma in imperfect tense.",
        "میزان الصرف", "درجہ اولیٰ", "Darja-e-Ula", "Sarf", "میزان الصرف — باب اول"
    ),
    (
        "ثلاثی مزید فیہ کے بے ہمزہ وصل کل کتنے مشہور ابواب ہیں؟",
        "How many doors are there in Thulathi Mazeed Feeh without Hamza Wasl?",
        "أَبْوَابُ الثُّلَاثِيِّ الْمَزِيدِ فِيهِ بِلَا هَمْزَةِ وَصْلٍ",
        "3 ابواب (تفعیل، تفعّل، مفاعلہ)",
        "3 Doors (Taf'eel, Tafa''ul, Mufa'alah)",
        ["5 ابواب", "2 ابواب", "7 ابواب"],
        ["5 Doors", "2 Doors", "7 Doors"],
        "ثلاثی مزید فیہ بے ہمزہ وصل کے تین بنیادی ابواب ہیں: افعال، تفعیل اور مفاعلہ۔",
        "Thulathi Mazeed Feeh without Hamza Wasl contains 3 primary doors.",
        "میزان الصرف", "درجہ اولیٰ", "Darja-e-Ula", "Sarf", "میزان الصرف — اقسام مزید فیہ"
    ),
    (
        "فعل امر حاضر معروف بنانے کے لیے مضارع کے کس صیغہ سے علامتِ مضارع حذف کی جاتی ہے؟",
        "Which form is used to derive Amr Hazir Maroof in Sarf?",
        "الأَمْرُ بِحَذْفِ حَرْفِ الْمُضَارَعَةِ",
        "مضارع حاضر کے صیغوں سے (تَفْعَلُ)",
        "From Mudari Hazir forms (Taf'alu)",
        ["مضارع غائب سے", "ماضی معروف سے", "اسم فاعل سے"],
        ["From Mudari Ghaib", "From Madi Maroof", "From Ism Fail"],
        "امر حاضر معروف کو مضارع حاضر سے علامتِ مضارع گرا کر آخر کو جزم دے کر بنایا جاتا ہے۔",
        "Amr Hazir is formed by dropping the Mudari prefix from second-person imperfect.",
        "میزان الصرف", "درجہ اولیٰ", "Darja-e-Ula", "Sarf", "میزان الصرف — بحث امر حاضر"
    ),
    (
        "اسم فاعل ثلاثی مجرد کا اصل وزن کیا ہے؟",
        "What is the base standard measure for Ism Fa'il in Thulathi Mujarrad?",
        "وَزْنُ اسْمِ الْفَاعِلِ فَاعِلٌ",
        "فَاعِلٌ (جیسے نَاصِرٌ، ضَارِبٌ)",
        "Faa'ilun (e.g. Nasirun, Daaribun)",
        ["مَفْعُولٌ", "فَعِيلٌ", "مُفَعِّلٌ"],
        ["Maf'oolun", "Fa'eelun", "Mufa''ilun"],
        "ثلاثی مجرد میں اسم فاعل کا عمومی وزن ہمیشہ فَاعِلٌ پر آتا ہے۔",
        "The active participle in simple triliteral verbs follows Faa'ilun.",
        "میزان الصرف", "درجہ اولیٰ", "Darja-e-Ula", "Sarf", "میزان الصرف — بحث اسم فاعل"
    )
]))

# 2. نحو میر
all_chapters.append(('beginner', 2, [
    (
        "علمِ نحو کی تعریف کیا ہے؟",
        "What is the definition of Ilm un Nahw?",
        "عِلْمُ النَّحْوِ عِلْمٌ بِأُصُولٍ يُعْرَفُ بِهَا أَحْوَالُ أَوَاخِرِ الْكَلِمِ",
        "ایسا علم جس سے کلمات کے آخری احوال اور ترکیب معلوم ہو",
        "Science of rules determining word endings and sentence syntax",
        ["علمِ معانی کا حصہ", "صرف حروف کی گردان کا علم", "علم لغت و تاریخ"],
        ["Science of Balagha", "Only verb conjugation", "Lexicography"],
        "نحو وہ علم ہے جس سے معرب اور مبنی کے آخری احوال اور کلمات کو آپس میں جوڑنے کا طریقہ معلوم ہوتا ہے۔",
        "Nahw governs the inflection of word endings and syntactical relations.",
        "نحو میر", "درجہ اولیٰ", "Darja-e-Ula", "Nahw", "نحو میر — مقدمہ"
    ),
    (
        "کلمہ کی بنیادی کتنی اقسام ہیں؟",
        "How many primary parts of speech (Kalimah) exist in Arabic Grammar?",
        "أَقْسَامُ الْكَلِمَةِ ثَلَاثَةٌ: اسْمٌ، فِعْلٌ، حَرْفٌ",
        "3 اقسام: اسم، فعل اور حرف",
        "3 Types: Ism (Noun), Fi'l (Verb), and Harf (Particle)",
        ["4 اقسام", "5 اقسام", "2 اقسام"],
        ["4 Types", "5 Types", "2 Types"],
        "عربی زبان میں بامعنی مفرد کلمہ کی تین ہی قسمیں ہیں: اسم، فعل، حرف۔",
        "A meaningful single word is divided into Ism, Fi'l, and Harf.",
        "نحو میر", "درجہ اولیٰ", "Darja-e-Ula", "Nahw", "نحو میر — فصل اول"
    ),
    (
        "مندرجہ ذیل میں سے اسم کی مخصوص علامت کون سی ہے؟",
        "Which of the following is an exclusive sign of Ism (Noun)?",
        "مِنْ عَلَامَاتِ الاسْمِ: دُخُولُ الأَلِفِ وَاللَّامِ وَالتَّنْوِينِ",
        "الف لام (الـ) یا تنوین کا داخل ہونا",
        "Acceptance of Alif-Lam (Al-) or Tanween",
        ["حرف جزم کا داخل ہونا", "سین اور سوف کا داخل ہونا", "قد کا داخل ہونا"],
        ["Acceptance of Jazm", "Acceptance of Seen and Sawfa", "Acceptance of Qad"],
        "الف لام، تنوین، حرف جر اور مضاف ہونا اسم کے مخصوص خواص ہیں۔",
        "Alif-Lam, Tanween, Prepositions, and Idafa are unique to nouns.",
        "نحو میر", "درجہ اولیٰ", "Darja-e-Ula", "Nahw", "نحو میر — علامات اسم"
    ),
    (
        "حروفِ جارہ کی کل تعداد کتنی ہے؟",
        "How many Huroof al-Jarr (Prepositions) are there in Nahw?",
        "حُرُوفُ الْجَرِّ سَبْعَةَ عَشَرَ",
        "17 حروف (با، تا، کاف، لام، واو، من، مذ...)",
        "17 Prepositions (Baa, Taa, Kaaf, Laam...)",
        ["12 حروف", "24 حروف", "10 حروف"],
        ["12 Prepositions", "24 Prepositions", "10 Prepositions"],
        "نحو میر کے مطابق حروف جارہ کل سترہ (17) ہیں جو اسم کو جر دیتے ہیں۔",
        "There are 17 prepositions that assign Genitive case to nouns.",
        "نحو میر", "درجہ اولیٰ", "Darja-e-Ula", "Nahw", "نحو میر — فصل حروف جارہ"
    ),
    (
        "جملہ اسمیہ کے پہلے اور دوسرے جز کو کیا کہتے ہیں؟",
        "What are the two main constituents of Jumla Ismiyyah called?",
        "الْجُمْلَةُ الاسْمِيَّةُ تَتَرَكَّبُ مِنْ مُبْتَدَأٍ وَخَبَرٍ",
        "مبتداء اور خبر",
        "Mubtada (Subject) and Khabar (Predicate)",
        ["فعل اور فاعل", "مضاف اور مضاف الیہ", "موصوف اور صفت"],
        ["Fi'l and Fa'il", "Mudaf and Mudaf Ilaih", "Mawsuf and Sifat"],
        "جملہ اسمیہ کے مسند الیہ کو مبتداء اور مسند کو خبر کہا جاتا ہے۔",
        "The nominal sentence begins with Mubtada and completes with Khabar.",
        "نحو میر", "درجہ اولیٰ", "Darja-e-Ula", "Nahw", "نحو میر — بحث جملہ اسمیہ"
    )
]))

# 3. علم الصیغہ
all_chapters.append(('beginner', 3, [
    (
        "ہفت اقسام میں وہ کلمہ کیا کہلاتا ہے جس کے حروف اصلی میں کوئی حرف علت نہ ہو اور نہ ہمزہ و تکرار؟",
        "In Haft Aqsam, what is a word called having no weak letters, hamza, or gemination?",
        "الصَّحِيحُ مَا لَيْسَ فِي حُرُوفِهِ الأَصْلِيَّةِ حَرْفُ عِلَّةٍ",
        "صحیح (جیسے نَصَرَ، ضَرَبَ)",
        "Saheeh (e.g. Nasara, Daraba)",
        ["معتل", "مہموز", "مضاعف"],
        ["Mu'tal", "Mahmooz", "Muda'af"],
        "صحیح وہ کلمہ ہے جس کے حروف اصلی میں حرف علت، ہمزہ اور دو حروف یکجنس نہ ہوں۔",
        "Saheeh contains no weak radical, hamza, or doubled root letter.",
        "علم الصیغہ", "درجہ ثانیہ", "Darja-e-Sania", "Sarf", "علم الصیغہ — بحث ہفت اقسام"
    ),
    (
        "مثال کس کلمہ کو کہتے ہیں؟",
        "What is Mithal in Sarf classification?",
        "الْمِثَالُ مَا كَانَ فَاؤُهُ حَرْفَ عِلَّةٍ",
        "جس کے فا کلمہ میں حرفِ علت ہو (جیسے وَعَدَ)",
        "A word whose Fa-Kalimah is a weak letter (e.g. Wa'ada)",
        ["جس کے عین کلمہ میں حرف علت ہو", "جس کے لام کلمہ میں حرف علت ہو", "جس میں دو حروف علت ہوں"],
        ["Weak Ayn-Kalimah", "Weak Laam-Kalimah", "Two weak letters"],
        "فا کلمہ میں واو یا یاء ہو تو اسے معتل الفاء یا مثال کہتے ہیں۔",
        "Mithal is a verb whose first root consonant is a semi-vowel.",
        "علم الصیغہ", "درجہ ثانیہ", "Darja-e-Sania", "Sarf", "علم الصیغہ — معتل الفاء"
    ),
    (
        "اجوف کسے کہا جاتا ہے؟",
        "What is Ajwaf in Arabic morphology?",
        "الأَجْوَفُ مَا كَانَ عَيْنُهُ حَرْفَ عِلَّةٍ",
        "جس کے عین کلمہ میں حرفِ علت ہو (جیسے قَالَ، بَاعَ)",
        "A hollow verb whose middle radical is a weak letter",
        ["جس کے لام کلمہ میں حرف علت ہو", "جس میں ہمزہ ہو", "جس کے فا کلمہ میں علت ہو"],
        ["Weak Laam-Kalimah", "Containing Hamza", "Weak Fa-Kalimah"],
        "عین کلمہ میں حرف علت واقع ہونے پر اسے اجوف (کھوکھلا) کہتے ہیں۔",
        "Ajwaf represents hollow verbs with a weak medial consonant.",
        "علم الصیغہ", "درجہ ثانیہ", "Darja-e-Sania", "Sarf", "علم الصیغہ — بحث اجوف"
    ),
    (
        "ناقص وہ کلمہ ہے جس کے...",
        "Naqis in morphology is defined as a verb whose...",
        "النَّاقِصُ مَا كَانَ لَامُهُ حَرْفَ عِلَّةٍ",
        "لام کلمہ میں حرفِ علت ہو (جیسے رَمٰی، دَعَا)",
        "Laam-Kalimah is a weak letter (e.g. Rama, Da'a)",
        ["فا کلمہ میں علت ہو", "عین اور لام دونوں میں علت ہو", "شروع میں ہمزہ ہو"],
        ["Weak Fa-Kalimah", "Weak Ayn and Laam", "Initial Hamza"],
        "لام کلمہ میں حرف علت ہونے کو ناقص کہتے ہیں۔",
        "Naqis is a defective verb with a weak final radical.",
        "علم الصیغہ", "درجہ ثانیہ", "Darja-e-Sania", "Sarf", "علم الصیغہ — بحث ناقص"
    ),
    (
        "لفیف مفروق کسے کہتے ہیں؟",
        "What is Lafeef Mafrooq?",
        "اللَّفِيفُ الْمَفْرُوقُ مَا كَانَ فَاؤُهُ وَلَامُهُ حَرْفَيْ عِلَّةٍ",
        "جس کے فا اور لام کلمہ میں حرف علت ہو (جیسے وَقٰی)",
        "A verb having weak letters in Fa and Laam radicals (e.g. Waqa)",
        ["جس کے عین اور لام میں علت ہو", "جس میں تینوں حروف علت ہوں", "جس میں کوئی علت نہ ہو"],
        ["Weak Ayn and Laam", "All three weak", "No weak letters"],
        "لفیف مفروق میں دو حروف علت کے درمیان صحیح حرف فاصل ہوتا ہے جیسے وقی۔",
        "Lafeef Mafrooq separates two weak letters with a sound middle radical.",
        "علم الصیغہ", "درجہ ثانیہ", "Darja-e-Sania", "Sarf", "علم الصیغہ — بحث لفیف"
    )
]))

# 4. ہدایۃ النحو (مرفوعات)
all_chapters.append(('beginner', 4, [
    (
        "مرفوعات کی کل تعداد کتنی ہے؟",
        "How many categories of Marfoo'at (Nominative Nouns) are there in Nahw?",
        "الْمَرْفُوعَاتُ ثَمَانِيَةٌ",
        "8 اقسام (فاعل، نائب فاعل، مبتدا، خبر، اسم کان...)",
        "8 Categories (Fa'il, Na'ib Fa'il, Mubtada, Khabar...)",
        ["6 اقسام", "12 اقسام", "5 اقسام"],
        ["6 Categories", "12 Categories", "5 Categories"],
        "ہدایۃ النحو کے مطابق مرفوعات کل آٹھ ہیں جن پر رفع کا اعراب آتا ہے۔",
        "Hidayat un Nahw enumerates 8 nominative syntactic roles.",
        "ہدایۃ النحو", "درجہ ثانیہ", "Darja-e-Sania", "Nahw", "ہدایۃ النحو — قسم اول مرفوعات"
    ),
    (
        "فاعل کی نحوی تعریف کیا ہے؟",
        "What is the syntactic definition of Fa'il (Subject)?",
        "الْفَاعِلُ كُلُّ اسْمٍ قَبْلَهُ فِعْلٌ أَوْ شِبْهُهُ أُسْنِدَ إِلَيْهِ",
        "وہ اسم جس سے پہلے فعل یا شبہ فعل ہو اور اس کی نسبت اس کی طرف ہو",
        "A noun preceded by an active verb attributed to it",
        ["جس پر فعل واقع ہو", "جو جملے کے آخر میں آئے", "جو مضاف واقع ہو"],
        ["On which action is performed", "Occurring at end", "Governed as Mudaf"],
        "فاعل ہمیشہ مرفوع ہوتا ہے اور فعل تام کے بعد اس کا فاعل آتا ہے۔",
        "Fa'il receives nominative case and performs the verbal action.",
        "ہدایۃ النحو", "درجہ ثانیہ", "Darja-e-Sania", "Nahw", "ہدایۃ النحو — بحث فاعل"
    ),
    (
        "نائب الفاعل کس فعل کے بعد آتا ہے؟",
        "Na'ib al-Fa'il occurs after which type of verb?",
        "نَائِبُ الْفَاعِلِ بَعْدَ الْفِعْلِ الْمَجْهُولِ",
        "فعل مجہول کے بعد (جیسے ضُرِبَ زَيْدٌ)",
        "After a Passive Verb (e.g. Duriba Zaydun)",
        ["فعل معروف کے بعد", "فعل لازم کے بعد", "فعل ناقص کے بعد"],
        ["After Active Verb", "After Intransitive Verb", "After Incomplete Verb"],
        "جب فاعل کو حذف کر دیا جائے تو مفعول بہ نائب فاعل بن کر مرفوع ہو جاتا ہے۔",
        "When the subject is omitted, the object becomes the nominative deputy subject.",
        "ہدایۃ النحو", "درجہ ثانیہ", "Darja-e-Sania", "Nahw", "ہدایۃ النحو — بحث نائب فاعل"
    ),
    (
        "اسمائے ستہ مکبرہ کی حالتِ رفعی کس حرف سے ظاہر ہوتی ہے؟",
        "In Asma Sitta Mukabbarah, how is the nominative case expressed?",
        "إِعْرَابُ الأَسْمَاءِ السِّتَّةِ بِالْوَاوِ رَفْعًا",
        "واو کے ساتھ (جیسے جَاءَ أَبُوكَ)",
        "With Waaw (e.g. Jaa'a Abooka)",
        ["الف کے ساتھ", "یاء کے ساتھ", "ضمہ تقدیری کے ساتھ"],
        ["With Alif", "With Yaa", "With implied Damma"],
        "اسمائے ستہ مکبرہ مضافہ کی حالت رفعی واو، نصبی الف اور جری یاء سے آتی ہے۔",
        "The Six Nouns take Waaw for Raf, Alif for Nasb, and Yaa for Jarr.",
        "ہدایۃ النحو", "درجہ ثانیہ", "Darja-e-Sania", "Nahw", "ہدایۃ النحو — اصناف اعراب"
    ),
    (
        "غیر منصرف پر کسرہ اور تنوین نہ آنے کے کتنے اسباب ہیں؟",
        "How many total causes (Asbaab) prevent a noun from Nunation in Ghayr Munsarif?",
        "أَسْبَابُ مَنْعِ الصَّرْفِ تِسْعَةٌ",
        "9 اسباب (عدل، وصف، تانیث، معرفہ، عجمہ...)",
        "9 Causes (Adl, Wasf, Ta'neeth, Ma'rifah, Ujmah...)",
        ["7 اسباب", "12 اسباب", "6 اسباب"],
        ["7 Causes", "12 Causes", "6 Causes"],
        "غیر منصرف وہ اسم ہے جس میں منع صرف کے نو اسباب میں سے دو سبب یا ایک ایسا سبب پایا جائے جو دو کے قائم مقام ہو۔",
        "Ghayr Munsarif is barred from tanween due to 2 of 9 causes or 1 weighty cause.",
        "ہدایۃ النحو", "درجہ ثانیہ", "Darja-e-Sania", "Nahw", "ہدایۃ النحو — بحث غیر منصرف"
    )
]))

# 5. ہدایۃ النحو (منصوبات و مجرورات)
all_chapters.append(('beginner', 5, [
    (
        "مفاعیل خمسہ (پانچ مفعول) کون کون سے ہیں؟",
        "Which five verbal objects constitute Mafaa'eel Khamsah?",
        "الْمَفَاعِيلُ خَمْسَةٌ: بِهِ، فِيهِ، لَهُ، مَعَهُ، مُطْلَقٌ",
        "مفعول بہ، مفعول فیہ، مفعول لہ، مفعول معہ، مفعول مطلق",
        "Maf'ool Bihi, Feehi, Lahu, Ma'ahu, Mutlaq",
        ["فاعل، مضاف، مبتدا، خبر، حال", "اسم ان، خبر کان، تمییز، مستثنی", "مجرور بحرف، مجرور باضافت"],
        ["Fa'il, Mudaf, Mubtada...", "Ism Inna, Khabar Kaana...", "Majroorat"],
        "نحو میں پانچوں مفاعیل ہمیشہ منسوب ہوتے ہیں۔",
        "All five Maf'ool objects take the accusative case (Nasb).",
        "ہدایۃ النحو", "درجہ ثانیہ", "Darja-e-Sania", "Nahw", "ہدایۃ النحو — بحث منصوبات"
    ),
    (
        "مفعول فیہ کو دوسرے کس نام سے جانا جاتا ہے؟",
        "By what other standard syntactical term is Maf'ool Feehi known?",
        "الْمَفْعُولُ فِيهِ هُوَ الظَّرْفُ زَمَانًا أَوْ مَكَانًا",
        "ظرف (ظرفِ زمان اور ظرفِ مکان)",
        "Zarf (Adverb of Time or Place)",
        ["حال", "تمییز", "مستثنیٰ"],
        ["Haal", "Tamyeez", "Mustathna"],
        "وہ اسم جو وقت یا جگہ کو ظاہر کرے جس میں فعل واقع ہوا، ظرف یا مفعول فیہ کہلاتا ہے۔",
        "Maf'ool Feehi denotes the temporal or spatial setting of an action.",
        "ہدایۃ النحو", "درجہ ثانیہ", "Darja-e-Sania", "Nahw", "ہدایۃ النحو — مفعول فیہ"
    ),
    (
        "حال اور ذو الحال میں کیا فرق ہوتا ہے؟",
        "What is the typical syntactic distinction between Haal and Dhoo al-Haal?",
        "الْحَالُ نَكِرَةٌ مَنْصُوبَةٌ وَذُو الْحَالِ مَعْرِفَةٌ غَالِبًا",
        "حال عموماً نکرہ مشتق اور ذو الحال معرفہ ہوتا ہے",
        "Haal is typically indefinite derivative; Dhoo al-Haal is definite",
        ["دونوں ہمیشہ نکرہ ہوتے ہیں", "دونوں ہمیشہ معرفہ ہوتے ہیں", "حال ہمیشہ مرفوع ہوتا ہے"],
        ["Both always indefinite", "Both always definite", "Haal is always nominative"],
        "حال فاعل یا مفعول کی کیفیت کو بیان کرتا ہے اور ہمیشہ منصوب ہوتا ہے۔",
        "Haal describes the state of the agent/patient and is in the accusative.",
        "ہدایۃ النحو", "درجہ ثانیہ", "Darja-e-Sania", "Nahw", "ہدایۃ النحو — بحث حال"
    ),
    (
        "مجرورات کتنی طرح کے ہوتے ہیں؟",
        "In how many ways can a noun be rendered Majroor (Genitive)?",
        "الْمَجْرُورَاتُ نَوْعَانِ: بِحَرْفِ الْجَرِّ وَبِالإِضَافَةِ",
        "2 طرح: حرفِ جر کے ذریعے یا اضافت کے ذریعے",
        "2 Ways: By Preposition or by Idafa (Genitive Annexation)",
        ["4 طرح", "3 طرح", "5 طرح"],
        ["4 Ways", "3 Ways", "5 Ways"],
        "اسم پر جر یا تو حرف جر داخل ہونے سے آتا ہے یا مضاف الیہ بننے کی وجہ سے۔",
        "Nouns take Genitive either governed by a Harf Jarr or as Mudaf Ilayh.",
        "ہدایۃ النحو", "درجہ ثانیہ", "Darja-e-Sania", "Nahw", "ہدایۃ النحو — قسم ثالث مجرورات"
    ),
    (
        "توابع کی کل کتنی اقسام ہیں؟",
        "How many classes of Tawaabi' (Syntactic Followers) are there in Nahw?",
        "التَّوَابِعُ خَمْسَةٌ: النَّعْتُ، الْعَطْفُ، التَّأْكِيدُ، الْبَدَلُ، عَطْفُ الْبَيَانِ",
        "5 اقسام: نعت (صفت)، عطف بحرف، تاکید، بدل، عطف بیان",
        "5 Types: Na't (Adjective), Atf, Ta'keed, Badal, Atf Bayan",
        ["3 اقسام", "6 اقسام", "4 اقسام"],
        ["3 Types", "6 Types", "4 Types"],
        "توابع وہ کلمات ہیں جن کا اعراب اپنے متبوع کے اعراب کے تابع ہوتا ہے۔",
        "Tawaabi' mimic the case endings of their preceding head words.",
        "ہدایۃ النحو", "درجہ ثانیہ", "Darja-e-Sania", "Nahw", "ہدایۃ النحو — بحث توابع"
    )
]))

# 6. نور الایضاح
all_chapters.append(('beginner', 6, [
    (
        "فقہ حنفی کے مطابق وضو کے کتنے فرائض ہیں؟",
        "How many Fara'id (Obligatory acts) are there in Wudu according to Hanafi Fiqh?",
        "فَرَائِضُ الْوُضُوءِ أَرْبَعَةٌ",
        "4 فرائض (چہرہ دھونا، کہنیوں سمیت ہاتھ دھونا، چوتھائی سر کا مسح، ٹخنوں سمیت پاؤں دھونا)",
        "4 Obligations (Washing face, arms to elbows, wiping 1/4 head, washing feet to ankles)",
        ["6 فرائض", "3 فرائض", "7 فرائض"],
        ["6 Obligations", "3 Obligations", "7 Obligations"],
        "قرآن کریم کی آیت وضو کے تحت یہ چاروں افعال قطعی فرض ہیں۔",
        "The 4 absolute obligations are derived directly from Surah Al-Ma'idah:6.",
        "نور الایضاح", "درجہ اولیٰ", "Darja-e-Ula", "Fiqh", "نور الایضاح — فصل فی فرائض الوضوء"
    ),
    (
        "غسل کے کل کتنے فرائض ہیں؟",
        "How many obligatory elements (Fara'id) are there in Ghusl?",
        "فَرَائِضُ الْغُسْلِ ثَلَاثَةٌ: الْمَضْمَضَةُ، وَالاسْتِنْشَاقُ، وَغَسْلُ جَمِيعِ الْبَدَنِ",
        "3 فرائض (کلی کرنا، ناک میں نرم ہڈی تک پانی پہنچانا، تمام بدن پر پانی بہانا)",
        "3 Obligations: Gargling mouth, rinsing nose, washing entire body",
        ["4 فرائض", "5 فرائض", "2 فرائض"],
        ["4 Obligations", "5 Obligations", "2 Obligations"],
        "غسل میں کلی کرنا، ناک میں پانی ڈالنا اور پورے جسم پر بال برابر بھی سوکھے بغیر پانی بہانا فرض ہے۔",
        "Ghusl requires rinsing mouth, nose, and pouring water over every part.",
        "نور الایضاح", "درجہ اولیٰ", "Darja-e-Ula", "Fiqh", "نور الایضاح — فرائض الغسل"
    ),
    (
        "تیمم میں کتنی ضربیں (ہاتھ مارنا) اور کتنے فرائض ہیں؟",
        "How many strokes and obligatory pillars are in Tayammum?",
        "فَرَائِضُ التَّيَمُّمِ: النِّيَّةُ وَضَرْبَتَانِ",
        "نیت اور پاک مٹی پر 2 ضربیں (ایک چہرے کے لیے، ایک دونوں ہاتھوں کے لیے)",
        "Intention and 2 strokes (one for face, one for arms)",
        ["1 ضرب صرف چہرے پر", "3 ضربیں", "4 ضربیں"],
        ["1 stroke for face only", "3 strokes", "4 strokes"],
        "تیمم کے فرائض میں نیت اور دو بار پاک مٹی پر ہاتھ مار کر چہرے اور ہاتھوں پر ملنا شامل ہے۔",
        "Tayammum comprises intention and two hand strikes for face and arms.",
        "نور الایضاح", "درجہ اولیٰ", "Darja-e-Ula", "Fiqh", "نور الایضاح — احکام التیمم"
    ),
    (
        "نجاستِ غلیظہ کے بدن یا کپڑے پر معاف ہونے کی مقدار کتنی ہے؟",
        "What is the excusable threshold for Najasat Ghaleezah in Hanafi Fiqh?",
        "الْعَفْوُ فِي النَّجَاسَةِ الْغَلِيظَةِ قَدْرُ الدِّرْهَمِ",
        "ایک درہم (ہتھیلی کے گڑھے کے برابر رقبہ یا وزن)",
        "Amount of a Dirham (approx 1 palm hollow size/weight)",
        ["بالکل معاف نہیں", "ایک چوتھائی کپڑا", "دو درہم"],
        ["Not excused at all", "One-fourth garment", "Two Dirhams"],
        "نجاست غلیظہ ایک درہم تک ہو تو نماز مکروہ تحریمی کے ساتھ ادا ہو جاتی ہے مگر دھونا واجب ہے۔",
        "Up to one Dirham is excused for validity, though washing remains wajib.",
        "نور الایضاح", "درجہ اولیٰ", "Darja-e-Ula", "Fiqh", "نور الایضاح — باب النجاسات"
    ),
    (
        "موزوں پر مسح کرنے کی مدت مقیم اور مسافر کے لیے بالترتیب کتنی ہے؟",
        "What is the validity duration of wiping over leather socks for resident and traveler?",
        "مُدَّةُ الْمَسْحِ لِلْمُقِيمِ يَوْمٌ وَلَيْلَةٌ، وَلِلْمُسَافِرِ ثَلَاثَةُ أَيَّامٍ وَلَيَالِيهَا",
        "مقیم کے لیے ایک دن رات (24 گھنٹے) اور مسافر کے لیے تین دن رات (72 گھنٹے)",
        "Resident: 1 day & night (24h); Traveler: 3 days & nights (72h)",
        ["دونوں کے لیے 3 دن", "مقیم کے لیے 12 گھنٹے اور مسافر کے لیے 48 گھنٹے", "مقیم کے لیے 2 دن"],
        ["3 days for both", "12h and 48h", "2 days for resident"],
        "موزوں پر مسح کی مدت حدث لاحق ہونے کے وقت سے شمار کی جاتی ہے۔",
        "The wiping duration starts from the time wudu breaks after wearing.",
        "نور الایضاح", "درجہ اولیٰ", "Darja-e-Ula", "Fiqh", "نور الایضاح — باب المسح علی الخفین"
    )
]))

# 7. مختصر القدوری (صلوۃ)
all_chapters.append(('beginner', 7, [
    (
        "نماز کے اندر کے ارکان (فرائضِ داخلہ) کی تعداد کتنی ہے؟",
        "How many internal pillars (Arkaan) of Salah are there in Hanafi Fiqh?",
        "أَرْكَانُ الصَّلَاةِ: التَّحْرِيمَةُ، الْقِيَامُ، الْقِرَاءَةُ، الرُّكُوعُ، السُّجُودُ، الْقَعْدَةُ الأَخِيرَةُ",
        "6 ارکان (تکبیر تحریمہ، قیام، قرأت، رکوع، سجود، قعدہ اخیرہ)",
        "6 Pillars: Takbeer Tahreemah, Qiyaam, Qira'at, Rukoo, Sujood, Qa'dah Akheerah",
        ["4 ارکان", "8 ارکان", "5 ارکان"],
        ["4 Pillars", "8 Pillars", "5 Pillars"],
        "نماز کے اندرونی چھ بنیادی فرائض و ارکان ہیں جن کے بغیر نماز باطل ہو جاتی ہے۔",
        "The 6 intrinsic pillars are essential for the validity of prayer.",
        "مختصر القدوری", "درجہ ثانیہ", "Darja-e-Sania", "Fiqh", "مختصر القدوری — کتاب الصلاة"
    ),
    (
        "فجر کی نماز کا مستحب وقت حنفیہ کے نزدیک کیا ہے؟",
        "What is the recommended (Mustahabb) time for Fajr in Hanafi Fiqh?",
        "الإِسْفَارُ بِالْفَجْرِ مُسْتَحَبٌّ",
        "اسفار (روشنی پھیلنے پر پڑھنا)",
        "Isfaar (When dawn light brightens)",
        ["تغلیس (اندھیرے میں پڑھنا)", "سورج نکلنے کے بعد", "زوال کے وقت"],
        ["Taghlees (darkness)", "After sunrise", "At Zawaal"],
        "حنفی مسلک کے مطابق مردوں کے لیے فجر کی نماز روشنی میں پڑھنا افضل و مستحب ہے۔",
        "Hanafis prefer reciting Fajr when morning light is visibly clear.",
        "مختصر القدوری", "درجہ ثانیہ", "Darja-e-Sania", "Fiqh", "مختصر القدوری — مواقیت الصلاة"
    ),
    (
        "نماز میں قعدہ اولیٰ کا کیا حکم ہے؟",
        "What is the legal ruling for the First Sitting (Qa'dah Oola) in 4-rak'ah prayer?",
        "الْقَعْدَةُ الأُولَى وَاجِبَةٌ",
        "واجب ہے (چھوٹنے پر سجدہ سہو لازم ہوتا ہے)",
        "Wajib (Requires Sajdah Sahw if omitted)",
        ["فرض ہے", "سنت مؤکدہ ہے", "مستحب ہے"],
        ["Fard", "Sunnah Mu'akkadah", "Mustahabb"],
        "قعدہ اولیٰ واجب ہے؛ اگر بھولے سے چھوٹ جائے تو سجدہ سہو سے تلافی ہو جاتی ہے۔",
        "The first sitting is Wajib; omitting it forgetfully requires compensatory Sajdah.",
        "مختصر القدوری", "درجہ ثانیہ", "Darja-e-Sania", "Fiqh", "مختصر القدوری — باب صفة الصلاة"
    ),
    (
        "سجدہ سہو کس وقت اور کیسے کیا جاتا ہے؟",
        "When and how is Sajdah Sahw executed in Hanafi Fiqh?",
        "سُجُودُ السَّهْوِ بَعْدَ السَّلَامِ الأَوَّلِ",
        "قعدہ اخیرہ میں تشہد کے بعد ایک طرف سلام پھیر کر دو سجدے کرنا",
        "After one salam in final sitting, performing two prostrations",
        ["رکوع سے پہلے", "نماز توڑنے کے بعد", "قعدہ اولیٰ میں"],
        ["Before Rukoo", "After breaking prayer", "In Qa'dah Oola"],
        "سجدہ سہو کسی واجب کے بھولے سے چھوٹنے یا فرض میں تاخیر پر کیا جاتا ہے۔",
        "Sajdah Sahw compensates for delayed fard or omitted wajib.",
        "مختصر القدوری", "درجہ ثانیہ", "Darja-e-Sania", "Fiqh", "مختصر القدوری — سجود السهو"
    ),
    (
        "مسافر کتنے کلومیٹر کے سفر کے ارادے پر قصر نماز پڑھے گا؟",
        "At what travel distance does a traveler perform Qasr (Shortening prayer)?",
        "مَسِيرَةُ ثَلَاثَةِ أَيَّامٍ وَلَيَالِيهَا (حَوَالَيْ 77.5 كم)",
        "تین دن کی مسافت (تقریباً 77.5 تا 78 کلومیٹر یا اس سے زائد)",
        "3 Days journey (approx 77.5 to 78 km or more)",
        ["20 کلومیٹر", "40 کلومیٹر", "150 کلومیٹر"],
        ["20 km", "40 km", "150 km"],
        "مسافر شرعی 4 رکعت والی فرض نمازوں (ظہر، عصر، عشاء) کو دو رکعت پڑھتا ہے۔",
        "A Shar'i traveler shortens 4-rak'ah fard prayers to 2 rak'ahs.",
        "مختصر القدوری", "درجہ ثانیہ", "Darja-e-Sania", "Fiqh", "مختصر القدوری — صلاة المسافر"
    )
]))

# 8. مختصر القدوری (زکوۃ و صوم)
all_chapters.append(('beginner', 8, [
    (
        "سونے اور چاندی کا شرعی نصاب بالترتیب کتنا ہے؟",
        "What are the Nisab thresholds for Gold and Silver in Islamic Jurisprudence?",
        "نِصَابُ الذَّهَبِ عِشْرُونَ مِثْقَالًا وَالْفِضَّةِ مِائَتَا دِرْهَمٍ",
        "سونا ساڑھے سات تولے (87.48 گرام) اور چاندی ساڑھے باون تولے (612.36 گرام)",
        "Gold: 7.5 Tolas (87.48g); Silver: 52.5 Tolas (612.36g)",
        ["سونا 10 تولے اور چاندی 100 تولے", "دونوں برابر 50 تولے", "سونا 5 تولے اور چاندی 25 تولے"],
        ["Gold 10 & Silver 100", "Both 50 Tolas", "Gold 5 & Silver 25"],
        "زکوۃ کا نصاب سونے میں 20 مثقال اور چاندی میں 200 درہم ہے۔",
        "Zakat threshold corresponds to 20 mithqals gold or 200 dirhams silver.",
        "مختصر القدوری", "درجہ ثانیہ", "Darja-e-Sania", "Fiqh", "مختصر القدوری — کتاب الزكاة"
    ),
    (
        "زکوۃ کے مال پر کتنا فیصد بطورِ زکوۃ نکالنا فرض ہے؟",
        "What percentage of wealth is payable as Zakat on surplus eligible assets?",
        "رُبُعُ الْعُشْرِ (2.5%)",
        "ڈھائی فیصد (2.5% یعنی چالیسواں حصہ)",
        "2.5% (One-fortieth / Rub' ul-Ushr)",
        ["دس فیصد (10%)", "پانچ فیصد (5%)", "بیس فیصد (20%)"],
        ["10%", "5%", "20%"],
        "نصاب پر سال گزرنے کے بعد 40 واں حصہ (2.5%) زکوۃ ادا کرنا فرض ہے۔",
        "One-fortieth (2.5%) must be distributed to eligible recipients.",
        "مختصر القدوری", "درجہ ثانیہ", "Darja-e-Sania", "Fiqh", "مختصر القدوری — مقدار الزكاة"
    ),
    (
        "روزے کی شرعی تعریف کیا ہے؟",
        "What is the Shar'i definition of Fasting (Sawm)?",
        "الصَّوْمُ هُوَ الإِمْسَاكُ عَنِ الأَكْلِ وَالشُّرْبِ وَالْجِمَاعِ بِنِيَّةٍ",
        "صبح صادق سے غروب آفتاب تک نیت کے ساتھ کھانے، پینے اور نفسانی خواہشات سے رکنا",
        "Refraining from eating, drinking, and relations from dawn to dusk with intention",
        ["صرف بھوکا پیاسا رہنا", "رات کو کھانا نہ کھانا", "نماز پڑھنا"],
        ["Merely staying hungry", "Not eating at night", "Praying only"],
        "روزے کے ارکان میں امساک (رکنا) اور نیت بنیادی شرط ہیں۔",
        "Fasting requires conscious abstention along with intentionality.",
        "مختصر القدوری", "درجہ ثانیہ", "Darja-e-Sania", "Fiqh", "مختصر القدوری — کتاب الصوم"
    ),
    (
        "اگر کوئی شخص جان بوجھ کر رمضان کا روزہ توڑ دے تو اس پر کیا لازم ہے؟",
        "What is required if one intentionally invalidates a Ramadan fast without valid excuse?",
        "عَلَيْهِ الْقَضَاءُ وَالْكَفَّارَةُ",
        "قضاء اور کفارہ دونوں (مسلسل 60 روزے یا 60 مسکینوں کو کھانا کھلانا)",
        "Both Qada and Kaffarah (60 consecutive fasts or feeding 60 poor)",
        ["صرف ایک دن کی قضاء", "صرف صدقہ دینا", "کچھ لازم نہیں"],
        ["Only 1 day Qada", "Only charity", "Nothing"],
        "جان بوجھ کر بغیر عذر کے روزہ توڑنے پر قضاء کے ساتھ سخت کفارہ بھی عائد ہوتا ہے۔",
        "Willful breach necessitates making up the fast plus sixty days expiation.",
        "مختصر القدوری", "درجہ ثانیہ", "Darja-e-Sania", "Fiqh", "مختصر القدوری — باب ما يفسد الصوم"
    ),
    (
        "حج کے کتنے فرائض ہیں؟",
        "How many Fard acts (Pillars) are there in Hajj?",
        "فَرَائِضُ الْحَجِّ ثَلَاثَةٌ: الإِحْرَامُ، وَالْوُقُوفُ بِعَرَفَةَ، وَطَوَافُ الزِّيَارَةِ",
        "3 فرائض (احرام باندھنا، وقوفِ عرفہ، طوافِ زیارت)",
        "3 Obligations: Ihram, Wuqoof at Arafah, Tawaf az-Ziyarah",
        ["5 فرائض", "2 فرائض", "7 فرائض"],
        ["5 Obligations", "2 Obligations", "7 Obligations"],
        "حج کے تین فرائض ہیں جن کے بغیر حج ادا نہیں ہوتا، ان میں سب سے اہم وقوف عرفہ ہے۔",
        "Hajj relies essentially on Ihram, standing at Arafat, and Tawaf Ziyarah.",
        "مختصر القدوری", "درجہ ثانیہ", "Darja-e-Sania", "Fiqh", "مختصر القدوری — کتاب الحج"
    )
]))

# 9. سیرت خاتم الانبیاء
all_chapters.append(('beginner', 9, [
    (
        "حضور اکرم ﷺ کی ولادت باسعادت کس سال اور کس مہینے میں ہوئی؟",
        "In which year and month was Prophet Muhammad ﷺ born?",
        "وُلِدَ النَّبِيُّ ﷺ عَامَ الْفِيلِ فِي شَهْرِ رَبِيعِ الأَوَّلِ",
        "عام الفیل (571ء) میں 12 ربیع الاول کو پیر کے دن",
        "Year of Elephant (571 CE) on 12th Rabi ul-Awwal (Monday)",
        ["عام الحزن میں", "ہجرت کے سال", "شوال 580ء"],
        ["Year of Sorrow", "Year of Hijrah", "Shawwal 580 CE"],
        "آپ ﷺ کی ولادت مکہ مکرمہ میں عام الفیل کے مبارک سال ہوئی۔",
        "The Messenger of Allah ﷺ was born in Makkah in the Year of the Elephant.",
        "سیرت خاتم الانبیاء", "درجہ اولیٰ", "Darja-e-Ula", "Seerah", "سیرت خاتم الانبیاء — ولادت باسعادت"
    ),
    (
        "پہلی وحی غارِ حرا میں کس سورت کی ابتدائی آیات نازل ہوئیں؟",
        "Which verses were first revealed to the Prophet ﷺ in the Cave of Hira?",
        "أَوَّلُ مَا نَزَلَ: {اقْرَأْ بِاسْمِ رَبِّكَ الَّذِي خَلَقَ}",
        "سورۃ العلق کی ابتدائی 5 آیات (اقْرَأْ بِاسْمِ رَبِّكَ...)",
        "Initial 5 verses of Surah Al-Alaq (Iqra bismi Rabbika...)",
        ["سورۃ الفاتحہ", "سورۃ المدثر", "سورۃ البقرۃ"],
        ["Surah Al-Fatiha", "Surah Al-Muddathir", "Surah Al-Baqarah"],
        "حضرت جبرائیل علیہ السلام نے غار حرا میں سب سے پہلے سورہ علق کی آیات پہنچائیں۔",
        "Archangel Jibreel brought the opening verses of Surah Al-Alaq in Hira.",
        "سیرت خاتم الانبیاء", "درجہ اولیٰ", "Darja-e-Ula", "Seerah", "سیرت خاتم الانبیاء — نزولِ وحی"
    ),
    (
        "اسلام کے بنیادی ارکان کی تعداد کتنی ہے؟",
        "How many core Pillars of Islam are there in Taleem ul Islam?",
        "أَرْكَانُ الإِسْلَامِ خَمْسَةٌ",
        "5 ارکان (شہادتین، نماز، روزہ، زکوۃ، حج)",
        "5 Pillars: Shahadah, Salah, Sawm, Zakah, Hajj",
        ["6 ارکان", "4 ارکان", "7 ارکان"],
        ["6 Pillars", "4 Pillars", "7 Pillars"],
        "حدیثِ جبرائیل کے مطابق اسلام کے پانچ بنیادی ستون ہیں۔",
        "According to Hadith Jibreel, Islam is built on 5 fundamental pillars.",
        "تعلیم الاسلام", "درجہ اولیٰ", "Darja-e-Ula", "Aqaid", "تعلیم الاسلام — ارکانِ اسلام"
    ),
    (
        "ایمانِ مفصل میں کن بنیادی امور پر ایمان لانے کا اقرار ہے؟",
        "Which core tenets of faith are articulated in Iman-e-Mufassal?",
        "آمَنْتُ بِاللَّهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ وَالْيَوْمِ الآخِرِ...",
        "اللہ، فرشتوں، آسمانی کتابوں، رسولوں، قیامت اور اچھی بری تقدیر پر",
        "Allah, His Angels, Books, Messengers, Day of Judgement, and Divine Decree",
        ["صرف رسولوں پر", "صرف جنت اور دوزخ پر", "صرف کتب سماویہ پر"],
        ["Only Messengers", "Only Heaven & Hell", "Only Scriptures"],
        "ایمان مفصل ایمانیات کے تمام بنیادی ارکان کا جامع خلاصہ ہے۔",
        "Iman-e-Mufassal comprehensively articulates the articles of Islamic creed.",
        "تعلیم الاسلام", "درجہ اولیٰ", "Darja-e-Ula", "Aqaid", "تعلیم الاسلام — عقائد ایمانیہ"
    ),
    (
        "میثاقِ مدینہ کس کے درمیان طے پانے والا تاریخی معاہدہ تھا؟",
        "Between whom was the historic Constitution of Madinah (Mithaq-e-Madinah) enacted?",
        "مِيثَاقُ الْمَدِينَةِ بَيْنَ الْمُسْلِمِينَ وَالْيَهُودِ",
        "مسلمانوں (مہاجرین و انصار) اور مدینہ کے یہود و دیگر قبائل کے درمیان",
        "Between Muslims (Muhajirun/Ansar) and the Jewish and tribal clans of Madinah",
        ["قریش اور رومیوں کے درمیان", "مسلمانوں اور حبشہ کے درمیان", "صرف انصار کے آپس میں"],
        ["Quraysh and Romans", "Muslims and Abyssinia", "Only among Ansar"],
        "میثاق مدینہ تاریخِ عالم کا پہلا تحریری آئین تھا جس نے ریاستِ مدینہ کی بنیاد رکھی۔",
        "The Charter of Madinah established legal coexistence and mutual defense.",
        "سیرت خاتم الانبیاء", "درجہ اولیٰ", "Darja-e-Ula", "Seerah", "سیرت خاتم الانبیاء — ہجرت و میثاق مدینہ"
    )
]))

# 10. عربی ادب و تجوید
all_chapters.append(('beginner', 10, [
    (
        "حروفِ حلقی کتنے ہیں اور کہاں سے ادا ہوتے ہیں؟",
        "How many Throat Letters (Huroof Halqiyyah) are there and from where are they pronounced?",
        "حُرُوفُ الْحَلْقِ سِتَّةٌ: ء، هـ، ع، ح، غ، خ",
        "6 حروف (ہمزہ، ہا، عین، حاء، غین، خاء) جو حلق سے ادا ہوتے ہیں",
        "6 Letters (Hamza, Haa, Ayn, Haa, Ghayn, Khaa) voiced from throat",
        ["4 حروف", "8 حروف", "10 حروف"],
        ["4 Letters", "8 Letters", "10 Letters"],
        "حلق کے تین حصے ہیں: ادنیٰ حلق، وسط حلق اور اقصی حلق جن سے یہ 6 حروف ادا ہوتے ہیں۔",
        "The throat is divided into lower, middle, and upper articulation points.",
        "فوائد مکیہ", "درجہ ثانیہ", "Darja-e-Sania", "Tajweed", "فوائد مکیہ — مخارج الحروف"
    ),
    (
        "نون ساکن اور تنوین کے بنیادی کتنے قواعد ہیں؟",
        "How many core rules govern Noon Sakinah and Tanween in Tajweed?",
        "أَحْكَامُ النُّونِ السَّاكِنَةِ وَالتَّنْوِينِ أَرْبَعَةٌ: إِظْهَارٌ، إِدْغَامٌ، إِقْلَابٌ، إِخْفَاءٌ",
        "4 قواعد: اظہار، ادغام، اقلاب، اور اخفاء",
        "4 Rules: Izhar, Idgham, Iqlab, and Ikhfa",
        ["3 قواعد", "5 قواعد", "6 قواعد"],
        ["3 Rules", "5 Rules", "6 Rules"],
        "نون ساکن و تنوین کے احکام میں حروف کے لحاظ سے اظہار، ادغام، اقلاب اور اخفاء کیا جاتا ہے۔",
        "Noon Sakinah behaves in four distinct modes based on succeeding letters.",
        "فوائد مکیہ", "درجہ ثانیہ", "Darja-e-Sania", "Tajweed", "فوائد مکیہ — احکام نون ساکن"
    ),
    (
        "کتاب 'نفحۃ العرب' کس موضوع اور فن پر مشتمل درسی کتاب ہے؟",
        "What is the primary subject of the classic curriculum book Nafhat ul-Arab?",
        "نَفْحَةُ الْعَرَبِ فِي الأَدَبِ الْعَرَبِيِّ وَالْقِصَصِ",
        "عربی ادب، حکایات اور عربی فصاحت و بلاغت",
        "Arabic Literature, Moral Tales, and Rhetorical Mastery",
        ["صرف فقہی فتاویٰ", "علم فلکیات", "علم منطق"],
        ["Only Fiqh Fatawa", "Astronomy", "Logic"],
        "نفحۃ العرب مولانا اعزاز علی رحمہ اللہ کی مرتب کردہ عربی ادب کی مایہ ناز کتاب ہے۔",
        "Nafhat ul-Arab is an esteemed text for training students in classical Arabic prose.",
        "نفحۃ العرب", "درجہ ثانیہ", "Darja-e-Sania", "Arabic Literature", "نفحۃ العرب — مقدمۃ الکتاب"
    ),
    (
        "حروفِ قلقلہ کتنے ہیں اور ان کا مجموعہ کیا ہے؟",
        "How many Qalqalah (Echoing) letters are there and what is their mnemonic phrase?",
        "حُرُوفُ الْقَلْقَلَةِ خَمْسَةٌ مَجْمُوعَةٌ فِي: (قُطْبُ جَدٍّ)",
        "5 حروف: ق، ط، ب، ج، د (مجموعہ: قُطْبُ جَدٍّ)",
        "5 Letters: Qaf, Taa, Baa, Jeem, Daal (Phrase: Qutbu Jadd)",
        ["6 حروف: یرملون", "3 حروف: وائی", "4 حروف: قطب"],
        ["6 Letters: Yarmaloon", "3 Letters: Waa'ee", "4 Letters"],
        "جب یہ پانچ حروف ساکن ہوں تو ان میں ایک جنبش اور جھٹکے کی آواز پیدا ہوتی ہے جسے قلقلہ کہتے ہیں۔",
        "When unvoweled, these five letters produce an echoing resonance.",
        "فوائد مکیہ", "درجہ ثانیہ", "Darja-e-Sania", "Tajweed", "فوائد مکیہ — صفات لازمہ قلقلہ"
    ),
    (
        "حروفِ یرملون (ي، ر، م، ل، و، ن) کس قاعدے کے حروف ہیں؟",
        "The letters of Yarmaloon are designated for which Tajweed rule?",
        "حُرُوفُ الإِدْغَامِ سِتَّةٌ مَجْمُوعَةٌ فِي (يَرْمَلُونَ)",
        "ادغام (ادغام مع الغنہ اور ادغام بلا غنہ)",
        "Idgham (Assimilation with or without Nasalization)",
        ["اظہارِ حلقی", "اقلاب", "مدِ لازم"],
        ["Izhar Halqi", "Iqlab", "Madd Lazim"],
        "نون ساکن کے بعد یرملون آنے پر حرف کو دوسرے حرف میں مدغم کر دیا جاتا ہے۔",
        "Yarmaloon letters assimilate the preceding Noon Sakin/Tanween.",
        "فوائد مکیہ", "درجہ ثانیہ", "Darja-e-Sania", "Tajweed", "فوائد مکیہ — احکام ادغام"
    )
]))


# ==========================================
# STEP 2: MEDIUM (درجہ ثالثہ و درجہ رابعہ)
# ==========================================

# 1. کافیہ (اسم و اعراب)
all_chapters.append(('medium', 1, [
    (
        "ابن حاجب کے مطابق اسمِ معرب کی تعریف کیا ہے؟",
        "What is the definition of Ism Mu'rab according to Ibn al-Hajib in Al-Kafiyah?",
        "الْمُعْرَبُ هُوَ الْمُرَكَّبُ الَّذِي لَمْ يُشْبِهِ مَبْنِيَّ الأَصْلِ",
        "وہ مرکب اسم جو مبنی الاصل کے مشابہ نہ ہو",
        "A compounded noun that bears no resemblance to intrinsically uninflected words",
        ["وہ اسم جو ہمیشہ ساکن رہے", "وہ اسم جو صرف مضاف واقع ہو", "ہر وہ لفظ جس پر تنوین آئے"],
        ["A noun always static", "Only occurring as Mudaf", "Any noun with tanween"],
        "کافیہ میں معرب کے لیے مرکب ہونا اور مبنی الاصل (حرف، فعل ماضی، امر حاضر) کے مشابہ نہ ہونا شرط ہے۔",
        "In Kafiyah, Mu'rab requires syntactic composition and absence of resemblance to Mabni.",
        "کافیہ", "درجہ ثالثہ", "Darja-e-Salisa", "Nahw", "کافیہ — بحث الاسم المعرب"
    ),
    (
        "معرب کے اعراب کی کل کتنی اقسام اور اصناف بیان کی گئی ہیں؟",
        "How many structural varieties of declension (Asnaf al-I'rab) are in Kafiyah?",
        "أَصْنَافُ إِعْرَابِ الاسْمِ سِتَّةَ عَشَرَ",
        "16 اصناف (مفرد منصرف، جمع مکسر، جمع مؤنث سالم...)",
        "16 Varieties (Mufrad Munsarif, Jam' Mukassar, Jam' Mu'annath Salim...)",
        ["10 اصناف", "12 اصناف", "20 اصناف"],
        ["10 Varieties", "12 Varieties", "20 Varieties"],
        "ابن حاجب نے اسم معرب کے اعراب کے 16 مختلف اصناف کو تفصیل سے واضح فرمایا ہے۔",
        "Ibn al-Hajib classifies nominal declension into 16 comprehensive classes.",
        "کافیہ", "درجہ ثالثہ", "Darja-e-Salisa", "Nahw", "کافیہ — اصناف اعراب الاسم"
    ),
    (
        "جمع مؤنث سالم کی حالتِ نصبی اور جری کس حرکت سے آتی ہے؟",
        "How are the Accusative and Genitive cases expressed in Jam' Mu'annath Salim?",
        "إِعْرَابُ جَمْعِ الْمُؤَنَّثِ السَّالِمِ بِالْكَسْرَةِ نَصْبًا وَجَرًّا",
        "کسرہ (زیر) کے ساتھ (جیسے خَلَقَ اللَّهُ السَّمَاوَاتِ)",
        "With Kasra in both Nasb and Jarr",
        ["فتحہ (زبر) کے ساتھ", "ضمہ کے ساتھ", "یاء کے ساتھ"],
        ["With Fatha", "With Damma", "With Yaa"],
        "جمع مؤنث سالم میں حالت نصبی میں فتحہ کی جگہ کسرہ آتا ہے، جیسے مسلماتٍ۔",
        "Jam' Mu'annath Salim takes Kasra for both accusative and genitive states.",
        "کافیہ", "درجہ ثالثہ", "Darja-e-Salisa", "Nahw", "کافیہ — اعراب جمع مؤنث سالم"
    ),
    (
        "مبنیات کی کل کتنی اقسام مشہور ہیں؟",
        "How many main categories of Mabniyaat (Uninflected Nouns) are detailed in Kafiyah?",
        "أَقْسَامُ الْمَبْنِيَّاتِ ثَمَانِيَةٌ",
        "8 اقسام (مضمرات، اسمائے اشارہ، موصولات، اسمائے افعال، اصوات، ظروف...)",
        "8 Categories (Pronouns, Demonstratives, Relatives, Verbal Nouns...)",
        ["4 اقسام", "6 اقسام", "12 اقسام"],
        ["4 Categories", "6 Categories", "12 Categories"],
        "مبنی غیر اصل کی آٹھ مشہور قسمیں ہیں جو مبنی الاصل کے مشابہ ہونے کی بنا پر مبنی ہوتی ہیں۔",
        "There are 8 recognized classes of indeclinable nouns in Kafiyah.",
        "کافیہ", "درجہ ثالثہ", "Darja-e-Salisa", "Nahw", "کافیہ — اقسام مبنیات"
    ),
    (
        "اسمِ موصول کے بعد آنے والے جملے کو کیا کہتے ہیں؟",
        "What is the sentence following a Relative Pronoun (Ism Mawsool) called?",
        "الْجُمْلَةُ بَعْدَ الْمَوْصُولِ تُسَمَّى صِلَةً",
        "صلہ (اور اس میں موصول کی طرف لوٹنے والا ضمیر عائد کہلاتا ہے)",
        "Silah (and the pronoun referring back is called A'id)",
        ["موصوف", "بدل", "تاکید"],
        ["Mawsuf", "Badal", "Ta'keed"],
        "اسم موصول بغیر صلہ اور عائد کے اپنے معنی مکمل نہیں کر سکتا۔",
        "A relative pronoun requires a relative clause (Silah) with a returning pronoun (A'id).",
        "کافیہ", "درجہ ثالثہ", "Darja-e-Salisa", "Nahw", "کافیہ — بحث الموصولات"
    )
]))

# 2. کافیہ (فعل و حرف)
all_chapters.append(('medium', 2, [
    (
        "فعل مضارع کو نصب دینے والے حروف (نواصبِ مضارع) کتنے ہیں؟",
        "How many particles give Nasb to the Imperfect Verb (Nawasib al-Mudari)?",
        "نَوَاصِبُ الْمُضَارِعِ أَرْبَعَةٌ: أَنْ، لَنْ، كَيْ، إِذَنْ",
        "4 حروف: أَنْ، لَنْ، كَيْ، إِذَنْ",
        "4 Particles: An, Lan, Kay, Idhan",
        ["6 حروف", "2 حروف", "8 حروف"],
        ["6 Particles", "2 Particles", "8 Particles"],
        "یہ چاروں حروف مضارع پر داخل ہو کر اس کو حالتِ نصبی عطا کرتے ہیں۔",
        "These four particles assign the subjunctive (Nasb) mood to imperfect verbs.",
        "کافیہ", "درجہ ثالثہ", "Darja-e-Salisa", "Nahw", "کافیہ — نواصب المضارع"
    ),
    (
        "ایک فعل مضارع کو جزم دینے والے حروف (جوازمِ مضارع) کتنے ہیں؟",
        "How many particles govern Jazam on a single imperfect verb?",
        "الْجَوَازِمُ لِفِعْلٍ وَاحِدٍ: لَمْ، لَمَّا، لَامُ الأَمْرِ، لَا النَّاهِيَةُ",
        "4 حروف: لَمْ، لَمَّا، لامِ امر، لائے ناہیہ",
        "4 Particles: Lam, Lamma, Lam of Command, and La of Prohibition",
        ["2 حروف", "5 حروف", "7 حروف"],
        ["2 Particles", "5 Particles", "7 Particles"],
        "یہ چاروں ادوات ایک فعل مضارع کو مجزوم کرتے ہیں، جبکہ ادواتِ شرط دو فعلوں کو جزم دیتے ہیں۔",
        "These four particles apocopate a single verb, unlike conditional particles.",
        "کافیہ", "درجہ ثالثہ", "Darja-e-Salisa", "Nahw", "کافیہ — جوازم المضارع"
    ),
    (
        "افعالِ مقاربہ کا عمل کیا ہوتا ہے؟",
        "What syntactic governance is performed by Af'al al-Muqarabah (Verbs of Proximity)?",
        "تَرْفَعُ الاسْمَ وَتَنْصِبُ الْخَبَرَ وَخَبَرُهَا جُمْلَةٌ فِعْلِيَّةٌ",
        "اسم کو رفع اور خبر کو نصب دیتے ہیں اور ان کی خبر فعل مضارع ہوتی ہے",
        "They raise the noun to Raf and set the predicate to Nasb as a verbal clause",
        ["اسم اور خبر دونوں کو نصب دیتے ہیں", "صرف مضاف بناتے ہیں", "مبتدا کو جزم دیتے ہیں"],
        ["Set both to Nasb", "Form Idafa only", "Govern Jazm on subject"],
        "جیسے عَسٰی، کَادَ، کَرَبَ۔ ان کی خبر ہمیشہ فعل مضارع کا جملہ ہوتی ہے۔",
        "Af'al al-Muqarabah (like 'Asa, Kaada) require a present tense verbal predicate.",
        "کافیہ", "درجہ ثالثہ", "Darja-e-Salisa", "Nahw", "کافیہ — افعال المقاربة"
    ),
    (
        "حروفِ مشبہ بالفعل کے عمل کی کیا خاصیت ہے؟",
        "What is the grammatical function of Huroof Mushabbaha bil-Fi'l?",
        "تَنْصِبُ الاسْمَ وَتَرْفَعُ الْخَبَرَ",
        "اسم کو نصب اور خبر کو رفع دیتے ہیں (جیسے إِنَّ اللَّهَ عَلِيمٌ)",
        "They put the Subject into Nasb and the Predicate into Raf",
        ["اسم کو رفع اور خبر کو نصب دیتے ہیں", "دونوں کو کسرہ دیتے ہیں", "جزم دیتے ہیں"],
        ["Raise Subject and lower Predicate", "Assign Kasra to both", "Assign Jazm"],
        "حروف مشبہ بالفعل چھ ہیں: ان، ان، کان، لیکن، لیت، لعل۔",
        "The 6 letters (Inna, Anna, Ka'anna, Lakinna, Layta, La'alla) govern Nasb on subject.",
        "کافیہ", "درجہ ثالثہ", "Darja-e-Salisa", "Nahw", "کافیہ — الحروف المشبهة بالفعل"
    ),
    (
        "افعالِ قلوب (جیسے عَلِمْتُ، ظَنَنْتُ) مفعولین پر کیا عمل کرتے ہیں؟",
        "How do Af'al al-Quloob govern their two objects?",
        "تَنْصِبُ الْمَفْعُولَيْنِ أَصْلُهُمَا مُبْتَدَأٌ وَخَبَرٌ",
        "دونوں مفعولوں کو نصب دیتے ہیں جو دراصل مبتدا اور خبر ہوتے ہیں",
        "They govern both objects into Nasb, which originate as Mubtada and Khabar",
        ["پہلے کو رفع اور دوسرے کو نصب دیتے ہیں", "دونوں کو جزم دیتے ہیں", "کوئی عمل نہیں کرتے"],
        ["First Raf, second Nasb", "Both Jazam", "No effect"],
        "افعال قلوب مبتدا اور خبر پر داخل ہو کر دونوں کو اپنا مفعول بہ بنا کر منصوب کرتے ہیں۔",
        "Verbs of the heart transform a nominal sentence into two accusative objects.",
        "کافیہ", "درجہ ثالثہ", "Darja-e-Salisa", "Nahw", "کافیہ — افعال القلوب"
    )
]))

# 3. اصول الشاشی (مباحث الکتاب)
all_chapters.append(('medium', 3, [
    (
        "اصول الشاشی کے مطابق لفظ موضوع کے اعتبار سے کتنی اقسام میں منقسم ہے؟",
        "According to Usul al-Shashi, how is a word divided based on its lexical assignment?",
        "أَقْسَامُ النَّظْمِ مِنْ حَيْثُ الْوَضْعُ: خَاصٌّ، عَامٌّ، مُشْتَرَكٌ، مُؤَوَّلٌ",
        "4 اقسام: خاص، عام، مشترک، اور مؤول",
        "4 Categories: Khas (Specific), Aam (General), Mushtarak (Ambiguous), and Mu'awwal",
        ["2 اقسام: حقیقت اور مجاز", "3 اقسام: ظاہر، نص، مفسر", "6 اقسام"],
        ["2 Types: Haqeeqah & Majaz", "3 Types: Zahir, Nass, Mufassar", "6 Types"],
        "وضع کے اعتبار سے لفظ کی چار بنیادی قسمیں ہیں: خاص، عام، مشترک اور مؤول۔",
        "Lexical categorization in Hanafi jurisprudence begins with these 4 classes.",
        "اصصول الشاشی", "درجہ ثالثہ", "Darja-e-Salisa", "Usul Fiqh", "اصول الشاشی — مباحث النظم"
    ),
    (
        "اصول فقہ حنفی میں 'خاص' کا شرعی حکم کیا ہے؟",
        "What is the legal ruling (Hukm) of 'Khas' in Hanafi Usul?",
        "حُكْمُ الْخَاصِّ أَنَّهُ يُوجِبُ الْعَمَلَ بِهِ قَطْعًا بِلَا احْتِمَالٍ",
        "اس پر قطعی اور حتمی طور پر بغیر کسی تاویل کے عمل واجب ہوتا ہے",
        "It necessitates definitive and categorical compliance without ambiguity",
        ["اس پر عمل ظنی ہوتا ہے", "یہ صرف مستحب ہے", "اس کا حکم منسوخ ہوتا ہے"],
        ["Compliance is probabilistic", "It is only recommended", "It is abrogated"],
        "خاص اپنے معنی میں قطعی الدلالت ہوتا ہے اور اس پر عمل کرنا فرض و واجب ہے۔",
        "Khas delivers definitive certainty (Qat'i) upon its specific semantic scope.",
        "اصول الشاشی", "درجہ ثالثہ", "Darja-e-Salisa", "Usul Fiqh", "اصول الشاشی — بحث الخاص"
    ),
    (
        "حنفیہ کے نزدیک جب 'عام' کا نزول ہو تو اس کی دلالت کیسی ہوتی ہے؟",
        "According to Hanafis, what is the nature of evidential certainty for 'Aam' before Takhsees?",
        "الْعَامُّ حُجَّةٌ قَطْعِيَّةٌ عِنْدَ الْحَنَفِيَّةِ قَبْلَ التَّخْصِيصِ",
        "تخصیص سے قبل خاص کی طرح قطعی الدلالت ہوتا ہے",
        "Definitive (Qat'i) evidence prior to particularization (Takhsees)",
        ["شروع سے ہی ظنی ہوتا ہے", "کوئی حجت نہیں ہوتا", "صرف قیاس کے بعد قطعی بنتا ہے"],
        ["Probabilistic from outset", "Not an evidence", "Only after Qiyas"],
        "امام ابو حنیفہ کے نزدیک عام تخصیص سے پہلے خاص کی طرح قطعی ہوتا ہے، جس پر خبر واحد سے زیادتی نسخ کہلاتی ہے۔",
        "Hanafis consider unparticularized general expressions definitively binding.",
        "اصول الشاشی", "درجہ ثالثہ", "Darja-e-Salisa", "Usul Fiqh", "اصول الشاشی — بحث العام"
    ),
    (
        "صیغۂ 'امر' بذاتِ خود کس چیز کا تقاضا کرتا ہے؟",
        "What does the bare imperative form (Amr) primarily signify in Hanafi Usul?",
        "مُطْلَقُ الأَمْرِ يَقْتَضِي الْوُجُوبَ",
        "وجوب (کسی کام کے لازم اور فرض ہونے کا)",
        "Obligation (Wujoob)",
        ["صرف اباحت (جائز ہونا)", "کراہت", "ندب و استحباب بلا لزوم"],
        ["Mere permissibility", "Dislike", "Recommendation without requirement"],
        "جب تک کوئی قرینہ موجود نہ ہو، امر کا صیغہ وجوب اور لزوم پر دلالت کرتا ہے۔",
        "An unrestricted command denotes absolute obligation unless qualified.",
        "اصول الشاشی", "درجہ ثالثہ", "Darja-e-Salisa", "Usul Fiqh", "اصول الشاشی — بحث الامر"
    ),
    (
        "کیا امر بذاتِ خود تکرار (بار بار کرنے) کا تقاضا کرتا ہے؟",
        "Does the imperative form inherently require repetition (Takraar)?",
        "الأَمْرُ لَا يَقْتَضِي التَّكْرَارَ بَلْ إِيجَادَ الْفِعْلِ مَرَّةً وَاحِدَةً",
        "نہیں، بلکہ صرف ایک بار فعل کو وجود میں لانے کا تقاضا کرتا ہے",
        "No, it merely demands performing the act at least once",
        ["ہاں، ہمیشہ بار بار کرنے کا تقاضا کرتا ہے", "صرف رات کے وقت تکرار چاہتا ہے", "تین بار کرنا ضروری ہوتا ہے"],
        ["Yes, always repeated", "Only repeated at night", "Must be done thrice"],
        "امر کا بنیادی تقاضا ماہیتِ فعل کو کم از کم ایک مرتبہ ادا کرنا ہوتا ہے، تکرار کے لیے الگ دلیل درکار ہے۔",
        "The imperative establishes the realization of the essence once.",
        "اصول الشاشی", "درجہ ثالثہ", "Darja-e-Salisa", "Usul Fiqh", "اصول الشاشی — بحث تکرار الامر"
    )
]))

# 4. اصول الشاشی (حقیقت، مجاز و قیاس)
all_chapters.append(('medium', 4, [
    (
        "حقیقت اور مجاز کی جامع تعریف کیا ہے؟",
        "What is the comprehensive distinction between Haqeeqah and Majaz?",
        "الْحَقِيقَةُ مَا اسْتُعْمِلَ فِيمَا وُضِعَ لَهُ، وَالْمَجَازُ فِيمَا اسْتُعِيرَ لَهُ",
        "حقیقت وہ لفظ جو اپنے موضوع لہ اصلی میں استعمال ہو، اور مجاز جو علاقہ کی بنا پر غیر موضوع لہ میں ہو",
        "Haqeeqah is applied in its primary coined meaning; Majaz is used figuratively via an association",
        ["دونوں کا معنی ایک ہی ہوتا ہے", "حقیقت صرف حدیث میں ہوتی ہے", "مجاز کا کوئی تعلق نہیں ہوتا"],
        ["Both are identical", "Haqeeqah only in Hadith", "Majaz has no relation"],
        "حقیقت اصل معنی ہے اور مجاز کے درست ہونے کے لیے قرینہ اور علاقہ (مشابہت، سببیت وغیرہ) ضروری ہے۔",
        "Majaz requires a semantic connection (Alaqah) and a contextual indicator (Qareenah).",
        "اصول الشاشی", "درجہ ثالثہ", "Darja-e-Salisa", "Usul Fiqh", "اصول الشاشی — الحقیقة والمجاز"
    ),
    (
        "کنایہ کا فقہی و اصولی حکم کیا ہے؟",
        "What is the legal ruling of Kinayah (Allusion) in Usul al-Fiqh?",
        "الْكِنَايَةُ مَا اسْتَتَرَ الْمُرَادُ بِهِ فَلَا يَعْمَلُ إِلَّا بِالنِّيَّةِ أَوْ دَلَالَةِ الْحَالِ",
        "اس کا معنی پوشیدہ ہوتا ہے اس لیے بغیر نیت یا دلالتِ حال کے عمل نہیں ہوتا",
        "The intended meaning is veiled, requiring explicit Intention or contextual evidence",
        ["بغیر نیت کے بھی واقع ہو جاتا ہے", "کنایہ بالکل باطل ہوتا ہے", "صرف زبانی اقرار چاہیے"],
        ["Effective without intention", "Kinayah is invalid", "Only verbal admission"],
        "صریح میں نیت کی حاجت نہیں ہوتی جبکہ کنایات (جیسے طلاقِ کنائی) میں نیت شرط ہے۔",
        "Kinayah requires corroborating intention or situational proof to produce legal effect.",
        "اصول الشاشی", "درجہ ثالثہ", "Darja-e-Salisa", "Usul Fiqh", "اصول الشاشی — الصریح والکنایة"
    ),
    (
        "قیاس کے کل کتنے ارکان ہیں؟",
        "How many structural pillars (Arkaan) constitute Qiyas (Analogical Deduction)?",
        "أَرْكَانُ الْقِيَاسِ أَرْبَعَةٌ: الأَصْلُ، الْفَرْعُ، الْحُكْمُ، الْعِلَّةُ",
        "4 ارکان: اصل (مقیس علیہ)، فرع (مقیس)، حکم، اور علتِ مشترکہ",
        "4 Pillars: Asl (Root), Far' (Branch), Hukm (Ruling), and Illah (Effective Cause)",
        ["3 ارکان", "6 ارکان", "2 ارکان"],
        ["3 Pillars", "6 Pillars", "2 Pillars"],
        "قیاس اصل کے حکم کو فرع کی طرف اس علتِ جامعہ کی وجہ سے متعدی کرنے کا نام ہے۔",
        "Qiyas extends the original legal ruling of the Asl to the Far' via a shared Illah.",
        "اصول الشاشی", "درجہ ثالثہ", "Darja-e-Salisa", "Usul Fiqh", "اصول الشاشی — ارکان القیاس"
    ),
    (
        "استحسان کسے کہتے ہیں؟",
        "What is Istihsan (Juristic Preference) in Hanafi Usul?",
        "الاسْتِحْسَانُ هُوَ تَرْكُ الْقِيَاسِ الْجَلِيِّ لِقِيَاسٍ خَفِيٍّ أَوْ دَلِيلٍ أَقْوَى",
        "قیاسِ جلی (ظاہر قیاس) کو چھوڑ کر کسی قوی تر دلیل یا قیاسِ خفی کو ترجیح دینا",
        "Departing from obvious strict analogy (Qiyas Jali) in favor of a subtle/stronger proof",
        ["اپنی ذاتی مرضی سے فتوی دینا", "تمام احادیث کو ترک کرنا", "صرف عرف پر فیصلہ کرنا"],
        ["Arbitrary personal opinion", "Rejecting all Hadith", "Relying solely on custom"],
        "استحسان شریعت کے مقاصد اور آسانی کی بنیاد پر قوی تر دلیل کو اپنانے کا نام ہے۔",
        "Istihsan resolves conflicts by prioritizing deeper equity and overarching scriptural intent.",
        "اصول الشاشی", "درجہ ثالثہ", "Darja-e-Salisa", "Usul Fiqh", "اصول الشاشی — بحث الاستحسان"
    ),
    (
        "دلالۃ النص کسے کہتے ہیں؟",
        "What is Dalalat an-Nass in evidential hierarchies?",
        "دَلَالَةُ النَّصِّ مَا ثَبَتَ بِعِلَّةِ الْحُكْمِ الْمَفْهُومَةِ لُغَةً",
        "وہ حکم جو نص کے مفہوم اور علتِ لغوی سے سمجھ آئے (جیسے والدین کو 'اف' کہنے کی ممانعت سے مارنے کی ممانعت)",
        "A ruling established through the linguistically understood effective cause (e.g. prohibition of hitting from 'Uff')",
        ["جو بالکل ظاہر نہ ہو", "جو صرف تاویل سے نکلے", "جو منسوخ ہو چکا ہو"],
        ["Completely obscure", "Derived only by conjecture", "Abrogated"],
        "دلالت النص میں حکم علتِ قطعیہ کی بنا پر بطریقِ اولیٰ ثابت ہوتا ہے۔",
        "Dalalat an-Nass conveys an a fortiori implication based on obvious linguistic ratio legis.",
        "اصول الشاشی", "درجہ ثالثہ", "Darja-e-Salisa", "Usul Fiqh", "اصول الشاشی — دلالة النص"
    )
]))

# Write out python script to finish all remaining chapters and assemble DarsENizamiQuizGenerator.kt
print("Step 1 and Step 2 up to Ch 4 prepared.")
