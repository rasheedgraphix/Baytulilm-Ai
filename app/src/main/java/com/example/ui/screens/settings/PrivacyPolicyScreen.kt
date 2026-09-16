package com.example.ui.screens.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IslamicGold
import com.example.util.LanguageManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val currentLang by LanguageManager.currentLanguage.collectAsState()
    var isUrdu by remember { mutableStateOf(currentLang.code == "ur" || currentLang.code == "ar" || currentLang.code == "ps") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val policyUrl = "https://rasheedgraphix.github.io/baytul-ilm-website/privacy_policy.html"
    val supportEmail = "hafiznoumanurrasheed4@gmail.com"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (isUrdu) "رازداری کی پالیسی" else "Privacy Policy",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = if (isUrdu) "گوگل پلے اور ایمیزون اسٹور سے منظور شدہ ضوابط" else "Google Play & Amazon Appstore Compliant",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Quick language toggle between Urdu & English
                    FilledTonalButton(
                        onClick = { isUrdu = !isUrdu },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.Language,
                            contentDescription = "Language",
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isUrdu) "English" else "اردو",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Compliance Guarantee Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isUrdu) "بیت العلم - قانونی و اخلاقی تحفظ کا عزم" else "Baytul Ilm - Full Data Safety & Compliance",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = if (isUrdu) "گوگل پلے اور ایمیزون اسٹور کی مکمل پالیسیوں کے عین مطابق" else "100% Aligned with Google Play & Amazon Appstore Policies",
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Store Badges Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ComplianceBadge(
                            title = if (isUrdu) "گوگل پلے محفوظ" else "Google Play Verified",
                            icon = Icons.Default.CheckCircle,
                            modifier = Modifier.weight(1f)
                        )
                        ComplianceBadge(
                            title = if (isUrdu) "ایمیزون ایپ اسٹور" else "Amazon Appstore",
                            icon = Icons.Default.CheckCircle,
                            modifier = Modifier.weight(1f)
                        )
                        ComplianceBadge(
                            title = if (isUrdu) "کوئی اشتہار نہیں" else "100% Ad-Free",
                            icon = Icons.Default.Block,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Quick Copy & Export Action Bar
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Copy URL
                    TextButton(
                        onClick = {
                            copyToClipboard(context, policyUrl, "Privacy Policy URL copied to clipboard")
                        }
                    ) {
                        Icon(Icons.Default.Link, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isUrdu) "لنک کاپی کریں" else "Copy URL", fontSize = 11.5.sp)
                    }

                    // Share
                    TextButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Baytul Ilm Privacy Policy")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Baytul Ilm Privacy Policy / بیت العلم رازداری کی پالیسی:\n$policyUrl\n\nContact: $supportEmail"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Privacy Policy"))
                        }
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isUrdu) "شیئر کریں" else "Share", fontSize = 11.5.sp)
                    }

                    // Open Web
                    TextButton(
                        onClick = {
                            runCatching {
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(policyUrl))
                                context.startActivity(browserIntent)
                            }
                        }
                    ) {
                        Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isUrdu) "آن لائن کھولیں" else "Web Page", fontSize = 11.5.sp)
                    }
                }
            }

            // Section 1: Introduction & App Scope
            PrivacySectionCard(
                icon = Icons.Default.Info,
                title = if (isUrdu) "۱. تعارف اور ایپلیکیشن کا دائرہ کار" else "1. Introduction & App Purpose",
                content = if (isUrdu)
                    "بیت العلم (Baytul Ilm) ایک مستند دینی، علمی اور تعلیمی پلیٹ فارم ہے جو طلبہ، علماء اور عام مسلمانوں کے لیے درجاتِ نظامیہ کی درسی کتب، قرآن پاک، احادیثِ نبویہ، تفاسیر، اذکار، قبلہ کمپاس اور حرمین شریفین (مکہ مکرمہ و مدینہ منورہ) کی براہِ راست نشریات فراہم کرتا ہے۔ ہم اپنے صارفین کے ذاتی و نجی کوائف کے تحفظ اور مکمل شفافیت کے لیے سختی سے پرعزم ہیں۔ یہ پالیسی گوگل پلے کونسول (Google Play Console) اور ایمیزون ایپ اسٹور (Amazon Appstore) کے تمام تقاضوں کو پورا کرتی ہے۔"
                else
                    "Baytul Ilm is an authentic Islamic educational application providing students, scholars, and Muslims worldwide with Dars-e-Nizami textbooks, Quran Pak, Hadith, Tafaseer, daily supplications, Qibla compass, and live official Haramain broadcasting. We are firmly committed to user privacy, transparent data handling, and strict compliance with all Google Play Store and Amazon Appstore Developer Policies."
            )

            // Section 2: Data Collection & Storage
            PrivacySectionCard(
                icon = Icons.Default.Storage,
                title = if (isUrdu) "۲. کوائف کا اندراج اور مقامی اسٹوریج" else "2. Information We Collect & Storage",
                content = if (isUrdu)
                    "• اکاؤنٹ کی معلومات (اختیاری): صارف رجسٹریشن یا لاگ ان کے وقت نام اور ای میل ایڈریس کو فائر بیس (Google Firebase Authentication) کے ذریعے محفوظ رکھا جاتا ہے۔ مہمان صارف (Guest Mode) کے طور پر بغیر لاگ ان کیے بھی تمام درسی و دینی مواد مکمل طور پر پڑھا جا سکتا ہے۔\n" +
                    "• مقامی اسٹوریج (Android Room SQLite): آپ کے ذاتی بک مارکس، مطالعہ کی تاریخ، نوٹس اور ڈیجیٹل تسبیح کی گنتی صرف اور صرف آپ کے موبائل میں محفوظ ہوتی ہے اور کبھی باہر نہیں بھیجی جاتی۔\n" +
                    "• ایپ کی ترجیحات: زبان (اردو، انگریزی، پشتو وغیرہ) اور تھیم کی ترجیحات مقامی طور پر رکھی جاتی ہیں۔"
                else
                    "• Optional Account Details: When you register or sign in, your name and email are securely handled via Google Firebase Authentication. You may use all core reading features in Guest Mode without creating an account.\n" +
                    "• Local Persistence (Room SQLite): Bookmarks, reading history, personal study notes, quiz progress, and Tasbeeh counts are stored locally on your device via Android Room database and are never uploaded to remote tracking servers.\n" +
                    "• App Preferences: Language (Urdu, English, Pashto) and visual display settings are preserved strictly on-device."
            )

            // Section 3: STRICT PERMISSIONS DISCLOSURE (MANDATORY FOR PLAY STORE & AMAZON)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isUrdu) "۳. ڈیوائس پرمشنز کی جامع وضاحت (Play Store & Amazon)" else "3. Manifest Permissions Explicitly Disclosed",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (isUrdu)
                            "ایپ اسٹورز کی پالیسیوں کے تحت درج ذیل اجازت ناموں کا مقصد اور استعمال واضح کیا جاتا ہے:"
                        else
                            "In accordance with Google Play and Amazon Developer policies, here is the exact rationale for every device permission declared in our manifest:",
                        fontSize = 12.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PermissionItem(
                        permissionName = "android.permission.ACCESS_FINE_LOCATION & COARSE_LOCATION",
                        purpose = if (isUrdu)
                            "مقام کی اجازت صرف اور صرف نماز کے درست اوقات، طلوع و غروبِ آفتاب اور قبلہ رخ (Qibla Compass) کے درست زاویے کا تعین کرنے کے لیے استعمال ہوتی ہے۔ یہ معلومات موبائل کے اندر ہی پروسیس ہوتی ہیں، بیک گراؤنڈ میں صارف کو ٹریک نہیں کیا جاتا، اور نہ ہی کسی اشتہاری کمپنی کو دی جاتی ہیں۔"
                        else
                            "Location access is used strictly on-device in real-time to calculate accurate local prayer times (Fajr, Dhuhr, Asr, Maghrib, Isha) and compute the precise Qibla compass azimuth toward the Kaaba in Makkah. Location data is NEVER tracked in the background when the app is closed, NEVER logged on external servers, and NEVER shared with advertisers or third parties."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PermissionItem(
                        permissionName = "android.permission.CAMERA",
                        purpose = if (isUrdu)
                            "کیمرے کی اجازت صرف اس وقت مانگی جاتی ہے جب صارف 'کتاب کا اسکینر (Book Page OCR)' کھول کر درسی کتاب کے کسی صفحے کی تصویر کھینچے تاکہ اس پر لکھی ہوئی عربی یا اردو عبارت کو پڑھا جا سکے۔ کیمرے سے لی گئی تصاویر صرف تحریر کے اخراج کے لیے پروسیس ہوتی ہیں، وہ مستقل محفوظ نہیں ہوتیں اور نہ ہی چہرے یا بائیو میٹرک شناخت کے لیے استعمال کی جاتی ہیں۔"
                        else
                            "Camera access is requested exclusively when the user utilizes the optional Book Page OCR / AI Scanner to snap a picture of an Arabic or Urdu textbook page for text extraction and translation. Photos are processed locally for optical character recognition, are NEVER stored permanently on external servers, and are NEVER used for facial or biometric recognition."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PermissionItem(
                        permissionName = "android.permission.INTERNET & ACCESS_NETWORK_STATE",
                        purpose = if (isUrdu)
                            "حرمین شریفین (مکہ و مدینہ) کی سرکاری لائیو نشریات دیکھنے، پی ڈی ایف کتب اور تفاسیر ڈاؤن لوڈ کرنے اور اہم اعلانات حاصل کرنے کے لیے ضروری ہے۔"
                        else
                            "Required to stream authorized public live broadcasts from Makkah and Madinah, download official Islamic books/PDFs for offline study, and sync cloud announcements."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PermissionItem(
                        permissionName = "android.permission.POST_NOTIFICATIONS & SCHEDULE_EXACT_ALARM",
                        purpose = if (isUrdu)
                            "نماز کے اوقات پر اذان کی بروقت یاد دہانی، یومیہ حدیث مبارکہ اور درسی اعلانات کے پیغامات پہنچانے کے لیے استعمال ہوتا ہے۔"
                        else
                            "Used strictly for optional prayer reminders, Adhan alerts at exact times, daily Hadith notifications, and academic notices. Users can enable or disable notifications at any time."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PermissionItem(
                        permissionName = "android.permission.VIBRATE",
                        purpose = if (isUrdu)
                            "ڈیجیٹل تسبیح کاؤنٹر میں تسبیح پڑھتے وقت ہلکی وائبریشن فیڈ بیک کے لیے استعمال ہوتا ہے۔"
                        else
                            "Provides subtle haptic vibration feedback when incrementing the Digital Tasbeeh counter."
                    )
                }
            }

            // Section 4: Zero Ads & No Selling of Data
            PrivacySectionCard(
                icon = Icons.Default.Block,
                title = if (isUrdu) "۴. اشتہارات کی عدم موجودگی اور ڈیٹا فروخت نہ کرنے کی ضمانت" else "4. 100% Ad-Free & No Data Monetization Guarantee",
                content = if (isUrdu)
                    "• کوئی تجارتی اشتہارات نہیں: بیت العلم میں کوئی کمرشل بینر اشتہار، پاپ اپ یا تیسری پارٹی کے اشتہاری نیٹ ورکس (جیسے ایڈموب وغیرہ) موجود نہیں ہیں۔ یہ ایک خالص اسلامی تعلیمی ایپلی کیشن ہے۔\n" +
                    "• ڈیٹا فروخت نہ کرنے کا قطعی عہد: ہم کسی بھی صارف کا نام، ای میل، لوکیشن یا تعلیمی ڈیٹا کسی بھی تجارتی کمپنی، ڈیٹا بروکر یا مارکیٹنگ ایجنسی کو فروخت، کرایہ پر یا شیئر نہیں کرتے۔"
                else
                    "• Strictly No Commercial Advertisements: Baytul Ilm does NOT display commercial third-party ads, interstitials, or tracking banners. It remains a respectful, distraction-free Islamic learning platform.\n" +
                    "• Explicit Zero Data Selling Guarantee: We NEVER sell, rent, lease, monetize, or trade any personal information, location coordinates, or educational data to third-party brokers, advertisers, or commercial entities under any circumstances."
            )

            // Section 5: Children's Safety & Families Policy (COPPA / GDPR)
            PrivacySectionCard(
                icon = Icons.Default.ChildCare,
                title = if (isUrdu) "۵. بچوں کی حفاظت اور خاندانی پالیسی (COPPA و Google Families)" else "5. Children's Privacy & Google Families Compliance",
                content = if (isUrdu)
                    "ہم گوگل پلے کی فیملیز پالیسی اور امریکی قانون کوپا (COPPA) کی مکمل پاسداری کرتے ہیں۔ ایپ طلبہ، بچوں اور تمام عمر کے مسلمانوں کے لیے مکمل محفوظ ہے۔ اس میں کوئی نامناسب، غیر اخلاقی یا غیر شرعی مواد موجود نہیں ہے۔ ہم ۱۳ سال سے کم عمر بچوں سے کوئی ذاتی یا حساس کوائف طلب یا جمع نہیں کرتے۔"
                else
                    "Baytul Ilm strictly complies with the Google Play Families Policy, the Children's Online Privacy Protection Act (COPPA), and the European GDPR. The application provides wholesome, verified Islamic educational content suitable for all ages. We do not knowingly collect or solicit personal data from children under 13 years of age."
            )

            // Section 6: Third-Party Service Providers
            PrivacySectionCard(
                icon = Icons.Default.CloudQueue,
                title = if (isUrdu) "۶. تیسری پارٹی کی سروسز" else "6. Third-Party Services & Integrations",
                content = if (isUrdu)
                    "ایپ صرف درج ذیل معتبر سروسز کے ساتھ کام کرتی ہے:\n" +
                    "• گوگل فائر بیس (Google Firebase): لاگ ان، کلاؤڈ میسجنگ اور کریش رپورٹنگ کے لیے۔\n" +
                    "• گوگل پلے سروسز: لوکیشن اور سسٹم لائبریریوں کی مدد کے لیے۔\n" +
                    "• حرمین شریفین سرکاری لائیو اسٹریمز: مسجد الحرام اور مسجد نبوی کے آفیشل یوٹیوب و سی ڈی این لنکس۔"
                else
                    "The app uses industry-standard, reputable infrastructure providers:\n" +
                    "• Google Firebase (Authentication, Cloud Messaging, Crashlytics) for secure authentication and crash reporting.\n" +
                    "• Google Play Services for standard Android platform integration.\n" +
                    "• Official Haramain Live endpoints for public live broadcasting from Makkah and Madinah."
            )

            // Section 7: Mandatory Account & Data Deletion (Google Play Mandate)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.DeleteForever,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isUrdu) "۷. کوائف و اکاؤنٹ کے خاتمے کا حق (Data Deletion)" else "7. Mandatory Account & Data Deletion Policy",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (isUrdu)
                            "گوگل پلے اسٹور کی لازمی ڈیلیشن پالیسی کے تحت ہر صارف کو اپنا ڈیٹا مکمل طور پر حذف کرنے کا حق حاصل ہے:\n\n" +
                            "۱. ایپ کے اندر سے: سیٹنگز یا پروفائل اسکرین میں جا کر آپ کسی بھی وقت اپنا اکاؤنٹ حذف کر سکتے ہیں یا موبائل کا ایپ ڈیٹا صاف کر کے مقامی ریکارڈز ختم کر سکتے ہیں۔\n" +
                            "۲. بذریعہ ای میل: آپ ڈویلپر کو ای میل ($supportEmail) بھیج کر اپنے اکاؤنٹ اور تمام متعلقہ ڈیٹا کو فوری و مستقل طور پر حذف کرنے کی درخواست کر سکتے ہیں۔ آپ کی درخواست پر ۴۸ گھنٹوں کے اندر تمام ریکارڈز مستقل ختم کر دیے جاتے ہیں۔"
                        else
                            "In compliance with Google Play's mandatory Account Deletion Policy, users retain complete autonomy over their personal data:\n\n" +
                            "1. In-App Deletion: You can delete your account and associated history directly via the Profile / Settings screen, or wipe all locally cached databases in your device settings.\n" +
                            "2. Email Request: You can request complete permanent erasure of your account and cloud data by sending an email to $supportEmail with the subject 'Delete My Account / Data'. Requests are processed within 48 hours.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { showDeleteDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isUrdu) "کوائف اور اکاؤنٹ کے خاتمے کی درخواست کریں" else "Request Account & Data Deletion",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Section 8: Developer Identity & Contact Information
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.ContactMail,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isUrdu) "۸. ڈویلپر کی شناخت اور رابطہ کی تفصیلات" else "8. Official Developer Contact & Identity",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            ContactRow(
                                label = if (isUrdu) "ایپ کا نام:" else "Application:",
                                value = "Baytul Ilm (بیت العلم)"
                            )
                            ContactRow(
                                label = if (isUrdu) "ڈویلپر / ایڈمنسٹریٹر:" else "Lead Developer:",
                                value = "Hafiz Nouman Ur Rasheed"
                            )
                            ContactRow(
                                label = if (isUrdu) "آفیشل ای میل:" else "Official Support Email:",
                                value = supportEmail
                            )
                            ContactRow(
                                label = if (isUrdu) "پبلک پالیسی یو آر ایل:" else "Public Policy URL:",
                                value = policyUrl
                            )
                            ContactRow(
                                label = if (isUrdu) "آخری تجدید:" else "Effective / Last Updated:",
                                value = "September 2026 (Version 1.0)"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:$supportEmail")
                                    putExtra(Intent.EXTRA_SUBJECT, "Baytul Ilm Privacy & Compliance Inquiry")
                                }
                                runCatching { context.startActivity(emailIntent) }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isUrdu) "ای میل بھیجیں" else "Email Us", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                val fullText = buildPrivacyPolicyText(isUrdu, policyUrl, supportEmail)
                                copyToClipboard(context, fullText, if (isUrdu) "مکمل پالیسی کاپی ہو گئی" else "Full Privacy Policy copied")
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isUrdu) "مکمل پالیسی کاپی کریں" else "Copy Full Text", fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Data Deletion Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(
                    Icons.Default.DeleteForever,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = if (isUrdu) "اکاؤنٹ اور کوائف کا خاتمہ" else "Account & Data Erasure Request",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = if (isUrdu)
                        "آپ کا تمام مقامی ڈیٹا (بک مارکس، نوٹس، اور سیٹنگز) صاف کیا جا سکتا ہے۔ کلاؤڈ ڈیٹا اور اکاؤنٹ کی مکمل منسوخی کے لیے 'ای میل بھیجیں' پر کلک کریں، ہماری ٹیم ۴۸ گھنٹوں میں مکمل صفائی کر دے گی۔"
                    else
                        "You can wipe all locally cached reading data and bookmarks, or email our support team ($supportEmail) for permanent account purge within 48 hours.",
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:$supportEmail")
                            putExtra(Intent.EXTRA_SUBJECT, "Request for Account & Data Deletion - Baytul Ilm")
                            putExtra(Intent.EXTRA_TEXT, "Please permanently delete my account and all associated cloud data from Baytul Ilm servers.")
                        }
                        runCatching { context.startActivity(emailIntent) }
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(if (isUrdu) "ڈیلیشن ای میل بھیجیں" else "Send Deletion Email")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(if (isUrdu) "منسوخ کریں" else "Cancel")
                }
            }
        )
    }
}

@Composable
private fun ComplianceBadge(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun PrivacySectionCard(
    icon: ImageVector,
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = content,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun PermissionItem(
    permissionName: String,
    purpose: String
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = permissionName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = purpose,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun ContactRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(0.6f)
        )
    }
}

private fun copyToClipboard(context: Context, text: String, toastMessage: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Baytul Ilm Privacy Policy", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
}

private fun buildPrivacyPolicyText(isUrdu: Boolean, url: String, email: String): String {
    return if (isUrdu) {
        """
        بیت العلم - رازداری کی مکمل پالیسی (Privacy Policy)
        تاریخِ اجراء: ستمبر 2026
        ڈویلپر: حافظ نعمان الرشید
        رابطہ: $email
        ویب سائٹ: $url

        ۱. تعارف: بیت العلم ایک جامع اسلامی و تعلیمی ایپ ہے۔ ہم صارفین کی نجی معلومات کے تحفظ کے پابند ہیں۔
        ۲. لوکیشن پرمشن: صرف نماز کے درست اوقات اور قبلہ رخ کے لیے استعمال ہوتی ہے۔ بیک گراؤنڈ میں ٹریکنگ نہیں کی جاتی اور نہ ہی کسی کو بیچی جاتی ہے۔
        ۳. کیمرہ پرمشن: صرف کتاب کے اسکینر (OCR) میں کتاب کا صفحہ اسکین کرنے کے لیے ہے۔ کوئی ذاتی تصویر محفوظ نہیں ہوتی۔
        ۴. نوٹیفکیشن: اذان اور یومیہ حدیث کی یاد دہانی کے لیے اختیاری ہے۔
        ۵. اشتہارات: ایپ 100% اشتہارات سے پاک ہے۔ کوئی تجارتی ایڈورٹائزنگ نہیں ہے۔
        ۶. ڈیٹا کی فروخت: ہم کوئی بھی ڈیٹا کسی تیسری پارٹی یا اشتہاری کمپنی کو فروخت نہیں کرتے۔
        ۷. بچوں کا تحفظ: ایپ تمام عمر کے افراد اور بچوں کے لیے محفوظ ہے اور کوپا (COPPA) کے مطابق ہے۔
        ۸. کوائف کے خاتمے کا حق: صارف کسی بھی وقت اپنا اکاؤنٹ اور ڈیٹا حذف کروا سکتا ہے۔
        """.trimIndent()
    } else {
        """
        Baytul Ilm - Full Privacy Policy
        Effective Date: September 2026
        Developer: Hafiz Nouman Ur Rasheed
        Email: $email
        Public URL: $url

        1. Overview: Baytul Ilm is an authentic Islamic educational platform. We strictly safeguard user privacy.
        2. Location Permission: Used strictly on-device in real-time to compute accurate prayer times and Qibla compass direction. Never tracked in background, never shared or sold.
        3. Camera Permission: Used solely for the Book Page OCR / AI Scanner to transcribe Arabic/Urdu book text. No personal photos stored, no facial recognition.
        4. Notifications: Used optionally for prayer time Azan reminders and daily Hadith notifications.
        5. Advertising: 100% Ad-free. Zero third-party commercial ad networks.
        6. Zero Data Monetization: We never sell, rent, or trade user data.
        7. Children's Privacy: Complies with COPPA, GDPR, and Google Play Families Policy.
        8. Data Deletion: Users can request complete account and data deletion anytime by emailing $email or in app settings.
        """.trimIndent()
    }
}

