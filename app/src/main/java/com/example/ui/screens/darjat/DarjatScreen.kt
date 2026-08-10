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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.LocalAppLanguage
import com.example.util.lStr

data class DarjaItemInfo(
    val id: String,
    val name: String,
    val nameUrdu: String,
    val yearName: String,
    val yearNameUrdu: String,
    val keySubjects: String,
    val keySubjectsUrdu: String,
    val description: String,
    val descriptionUrdu: String
)

@Composable
fun DarjatScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val allBooks by viewModel.allBooks.collectAsStateWithLifecycle()
    val lang = LocalAppLanguage.current
    val isUrdu = lang.code == "ur" || lang.isRtl

    val darjaList = listOf(
        DarjaItemInfo("darja_ula", "Darja-e-Ula", "درجہ اولیٰ", "First Year", "پہلا سال", "Nahw, Sarf, Fiqh, Literature", "نحو، صرف، فقہ، ادب", "Foundation level covering basic Arabic syntax, morphology, and Mukhtasar al-Quduri.", "بنیادی درجہ جس میں عربی نحو، صرف اور مختصر القدوری کا احاطہ کیا جاتا ہے۔"),
        DarjaItemInfo("darja_sania", "Darja-e-Sania", "درجہ ثانیہ", "Second Year", "دوسرا سال", "Nahw, Insha, Fiqh, Tajweed", "نحو، انشاء، فقہ، تجوید", "Intermediate syntax (Hidayat un Nahw) and expanded Hanafi Fiqh.", "درمیانی نحو (ہدایۃ النحو) اور تفصیلی حنفی فقہ۔"),
        DarjaItemInfo("darja_salisa", "Darja-e-Salisa", "درجہ ثالثہ", "Third Year", "تیسرا سال", "Kafiyah, Usul Fiqh, Adab", "کافیہ، اصول فقہ، ادب", "Classical grammar theory, Usul al-Shashi, and classical Arabic eloquence.", "کلاسیکی قواعد، اصول الشاشی اور فصاحت و بلاغت۔"),
        DarjaItemInfo("darja_rabia", "Darja-e-Rabia", "درجہ رابعہ", "Fourth Year", "چوتھا سال", "Sharh Jami, Mantiq, Fiqh", "شرح جامی، منطق، فقہ", "Advanced commentary on grammar, formal logic (Mirqat), and legal procedures.", "قواعد کی تفصیلی شرح، منطق (مرقات) اور فقہی مباحث۔"),
        DarjaItemInfo("darja_khamisa", "Darja-e-Khamisa", "درجہ خامسہ", "Fifth Year", "پانچواں سال", "Balagha, Sharh Wiqayah, Aqeedah", "بلاغت، شرح وقایہ، عقائد", "Rhetoric (Mukhtasar al-Ma'ani), Aqeedah Tahawiyyah, and Wiqayah.", "بلاغت (مختصر المعانی)، شرح العقیدہ الطحاویہ اور وقایہ۔"),
        DarjaItemInfo("darja_sadisa", "Class 6th", "درجہ سادسہ", "Sixth Year", "چھٹا سال", "Al-Hidayah, Usul Tafseer, Aqa'id, Siraji", "الہدایہ، اصول تفسیر، عقائد، سراجی", "Sixth Year Dars-e-Nizami (Class 6th / Darja Sadisa) curriculum.", "چھٹا سال درسِ نظامی نصاب (الہدایہ، تفسیر جلالین اور فرائض)۔"),
        DarjaItemInfo("darja_sabia", "Darja-e-Sabi'a", "درجہ سابعہ", "Seventh Year", "ساتواں سال", "Al-Hidayah Vol 2, Mishkat, Nukhbah", "الہدایہ، مشکوۃ، نخبۃ الفکر", "Advanced comparative law, Hadith canons (Mishkat), and Hadith methodology.", "تقابلی فقہ، مشکوۃ المصابیح اور اصولِ حدیث۔"),
        DarjaItemInfo("dora_hadith", "Dora Hadith", "دورۂ حدیث شریف", "Final Master Year", "تکمیلی سال", "Sihah Sittah (Bukhari, Muslim, Tirmidhi)", "صحاح ستہ (بخاری، مسلم، ترمذی)", "The culmination of Dars-e-Nizami with exhaustive study of the 6 major Hadith compilations.", "درسِ نظامی کا آخری سال، صحاح ستہ اور شروحات کا گہرا مطالعہ۔")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = lStr("classes_curriculum_header"),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = lStr("explore_library_desc"),
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(darjaList) { darja ->
                val booksCount = allBooks.count { 
                    it.darja.equals(darja.name, ignoreCase = true) ||
                    (darja.name.contains("6th", ignoreCase = true) && (it.darja.contains("6", ignoreCase = true) || it.darja.contains("Sadisa", ignoreCase = true)))
                }

                val displayName = if (isUrdu) darja.nameUrdu else darja.name
                val displayYear = if (isUrdu) darja.yearNameUrdu else darja.yearName
                val displaySubjects = if (isUrdu) darja.keySubjectsUrdu else darja.keySubjects
                val displayDesc = if (isUrdu) darja.descriptionUrdu else darja.description

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(Screen.DarjaDetail.createRoute(darja.id)) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Grade,
                                contentDescription = displayName,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = displayName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "($displayYear)",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "${lStr("subjects")}: $displaySubjects",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = displayDesc,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 2.dp)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "$booksCount ${lStr("books")}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = lStr("open_class"),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
