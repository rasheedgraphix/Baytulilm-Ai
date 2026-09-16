package com.example.ui.screens.darjat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.DarjaClassIconBadge
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.DarsNizamiMatcher
import com.example.util.LocalAppLanguage
import com.example.util.lStr

data class DarjaItemInfo(
    val id: String,
    val name: String,
    val nameUrdu: String,
    val arabicName: String = "",
    val yearName: String,
    val yearNameUrdu: String,
    val yearNamePashto: String = "",
    val keySubjects: String,
    val keySubjectsUrdu: String,
    val keySubjectsPashto: String = "",
    val description: String,
    val descriptionUrdu: String,
    val descriptionPashto: String = "",
    val progress: Float = 0.25f
) {
    fun getDisplayName(langCode: String): String = if (langCode == "ur" || langCode == "ps") nameUrdu else name
    fun getYearName(langCode: String): String = when (langCode) {
        "ps" -> yearNamePashto
        "ur" -> yearNameUrdu
        else -> yearName
    }
    fun getKeySubjects(langCode: String): String = when (langCode) {
        "ps" -> keySubjectsPashto
        "ur" -> keySubjectsUrdu
        else -> keySubjects
    }
    fun getDescription(langCode: String): String = when (langCode) {
        "ps" -> descriptionPashto
        "ur" -> descriptionUrdu
        else -> description
    }
}

@Composable
fun DarjatScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val allBooks by viewModel.allBooks.collectAsStateWithLifecycle()
    val lang = LocalAppLanguage.current
    val langCode = lang.code

    val darjaList = listOf(
        DarjaItemInfo("darja_ula", "Darja-e-Ula", "درجہ اولیٰ", "الصف الأول", "پہلا سال", "پہلا سال", "لومړی کال", "نحو، صرف، فقہ، ادب", "نحو، صرف، فقہ، ادب", "نحو، صرف، فقه، ادب", "Foundation level covering basic Arabic syntax, morphology, and Mukhtasar al-Quduri.", "بنیادی درجہ جس میں عربی نحو، صرف اور مختصر القدوری کا احاطہ کیا جاتا ہے۔", "بنیادي درجه چې د عربي نحو، صرف او مختصر القدوري پوښښ کوي.", 0.65f),
        DarjaItemInfo("darja_sania", "Darja-e-Sania", "درجہ ثانیہ", "الصف الثاني", "دوسرا سال", "دوسرا سال", "دویم کال", "نحو، انشاء، فقہ، تجوید", "نحو، انشاء، فقہ، تجوید", "نحو، انشاء، فقه، تجوید", "Intermediate syntax (Hidayat un Nahw) and expanded Hanafi Fiqh.", "درمیانی نحو (ہدایۃ النحو) اور تفصیلی حنفی فقہ۔", "منځنۍ نحو (ہدایۃ النحو) او تفصیلي حنفي فقه.", 0.45f),
        DarjaItemInfo("darja_salisa", "Darja-e-Salisa", "درجہ ثالثہ", "الصف الثالث", "تیسرا سال", "تیسرا سال", "درېیم کال", "کافیہ، اصول فقہ، ادب", "کافیہ، اصول فقہ، ادب", "کافیه، اصول فقه، ادب", "Classical grammar theory, Usul al-Shashi, and classical Arabic eloquence.", "کلاسیکی قواعد، اصول الشاشی اور فصاحت و بلاغت۔", "کلاسیکي قواعد، اصول الشاشي او فصاحت و بلاغت.", 0.30f),
        DarjaItemInfo("darja_rabia", "Darja-e-Rabia", "درجہ رابعہ", "الصف الرابع", "چوتھا سال", "چوتھا سال", "څلورم کال", "شرح جامی، منطق، فقہ", "شرح جامی، منطق، فقہ", "شرح جامی، منطق، فقه", "Advanced commentary on grammar, formal logic (Mirqat), and legal procedures.", "قواعد کی تفصیلی شرح، منطق (مرقات) اور فقہی مباحث۔", "د ګرامر تفصیلي شرح، منطق (مرقات) او فقهي مباحث.", 0.20f),
        DarjaItemInfo("darja_khamisa", "Darja-e-Khamisa", "درجہ خامسہ", "الصف الخامس", "پانچواں سال", "پانچواں سال", "پنځم کال", "بلاغت، شرح وقایہ، عقائد", "بلاغت، شرح وقایہ، عقائد", "بلاغت، شرح وقایه، عقائد", "Rhetoric (Mukhtasar al-Ma'ani), Aqeedah Tahawiyyah, and Wiqayah.", "بلاغت (مختصر المعانی)، شرح العقیدہ الطحاویہ اور وقایہ۔", "بلاغت (مختصر المعاني)، شرح العقیده الطحاویه او وقایه.", 0.15f),
        DarjaItemInfo("darja_sadisa", "Class 6th", "درجہ سادسہ", "الصف السادس", "چھٹا سال", "چھٹا سال", "شپږم کال", "الہدایہ، اصول تفسیر، عقائد، سراجی", "الہدایہ، اصول تفسیر، عقائد، سراجی", "الهدایه، اصول تفسیر، عقائد، سراجي", "Sixth Year Dars-e-Nizami (Class 6th / Darja Sadisa) curriculum.", "چھٹا سال درسِ نظامی نصاب (الہدایہ، تفسیر جلالین اور فرائض)۔", "شپږم کال درس نظامي نصاب (الهدایه، تفسیر جلالین او فرائض).", 0.10f),
        DarjaItemInfo("darja_sabia", "Darja-e-Sabi'a", "درجہ سابعہ", "الصف السابع", "ساتواں سال", "ساتواں سال", "اووم کال", "الہدایہ، مشکوۃ، نخبۃ الفکر", "الہدایہ، مشکوۃ، نخبۃ الفکر", "الهدایه، مشکوۃ، نخبۃ الفکر", "Advanced comparative law, Hadith canons (Mishkat), and Hadith methodology.", "تقابلی فقہ، مشکوۃ المصابیح اور اصولِ حدیث۔", "تقابلي فقه، مشکوۃ المصابیح او د حدیث اصول.", 0.05f),
        DarjaItemInfo("dora_hadith", "Dora Hadith", "دورۂ حدیث شریف", "الصف الثامن", "تکمیلی سال", "تکمیلی سال", "وروستی/تکمیلي کال", "صحاح ستہ (بخاری، مسلم، ترمذی)", "صحاح ستہ (بخاری، مسلم، ترمذی)", "صحاح ستة (بخاري، مسلم، ترمذي)", "The culmination of Dars-e-Nizami with exhaustive study of the 6 major Hadith compilations.", "درسِ نظامی کا آخری سال، صحاح ستہ اور شروحات کا گہرا مطالعہ۔", "د درس نظامي وروستی کال، د صحاح ستة عمیقه مطالعه.", 0.80f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header Banner with Modern Vibrant Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    androidx.compose.ui.graphics.Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF064E3B), // Deep Emerald
                            Color(0xFF0F766E), // Ocean Teal
                            Color(0xFF1E3A8A)  // Deep Royal Blue
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = lStr("classes_curriculum_header"),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = lStr("explore_library_desc"),
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "${darjaList.size} ${lStr("classes_curriculum_header")}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(darjaList) { darja ->
                val matchedBooks = DarsNizamiMatcher.getBooksForClass(allBooks, darja.id)
                val distinctSubjects = DarsNizamiMatcher.getDistinctSubjectsForClass(allBooks, darja.id)
                val booksCount = if (matchedBooks.isNotEmpty()) matchedBooks.size else 0
                val subjectsCount = if (distinctSubjects.isNotEmpty()) distinctSubjects.size else 0

                val displayName = darja.getDisplayName(langCode)
                val displayYear = darja.getYearName(langCode)
                val displaySubjects = darja.getKeySubjects(langCode)
                val displayDesc = darja.getDescription(langCode)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(Screen.DarjaDetail.createRoute(darja.id)) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Arabic Class Icon Badge (e.g. الصف الأول)
                            DarjaClassIconBadge(
                                classId = darja.id,
                                className = darja.name,
                                size = 64.dp
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = displayName,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    // Level Badge
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.tertiaryContainer
                                    ) {
                                        Text(
                                            text = displayYear,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                val subtitleText = if (langCode == "ur" || langCode == "ps") darja.arabicName else darja.name
                                if (subtitleText.isNotBlank()) {
                                    Text(
                                        text = subtitleText,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(top = 1.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = displayDesc,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = 15.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Key Subjects Pill
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoStories,
                                    contentDescription = null,
                                    modifier = Modifier.size(13.dp),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${lStr("subjects")}: $displaySubjects",
                                    fontSize = 10.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Action Row & Progress
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Book,
                                            contentDescription = null,
                                            modifier = Modifier.size(13.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "$booksCount ${lStr("books")}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoStories,
                                            contentDescription = null,
                                            modifier = Modifier.size(13.dp),
                                            tint = MaterialTheme.colorScheme.secondary
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "$subjectsCount ${lStr("subjects")}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                }
                            }

                            Button(
                                onClick = { onNavigate(Screen.DarjaDetail.createRoute(darja.id)) },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text(
                                    text = lStr("open_class"),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = lStr("open_class"),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

