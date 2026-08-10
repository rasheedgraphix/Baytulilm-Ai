package com.example.ui.screens.subjects

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
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

data class SubjectItem(
    val name: String,
    val nameUrdu: String,
    val description: String,
    val descriptionUrdu: String
)

@Composable
fun SubjectsScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val allBooks by viewModel.allBooks.collectAsStateWithLifecycle()
    val lang = LocalAppLanguage.current
    val isUrdu = lang.code == "ur" || lang.isRtl

    val subjectsList = listOf(
        SubjectItem("Nahw", "علم النحو", "Arabic Syntax & Sentence Grammar", "عربی گرامر اور جملوں کی ساخت"),
        SubjectItem("Sarf", "علم الصرف", "Morphology & Word Derivation", "الفاظ کی گردان اور ابواب کی ساخت"),
        SubjectItem("Fiqh", "علم الفقہ", "Islamic Jurisprudence & Rulings", "اسلامی قوانین اور شرعی مسائل"),
        SubjectItem("Usul Fiqh", "اصولِ فقہ", "Principles & Legal Methodology", "شرعی ادلہ اور اصولِ استنباط"),
        SubjectItem("Hadith", "علم الحدیث", "Prophetic Traditions & Narrations", "احادیثِ مبارکہ اور اصولِ حدیث"),
        SubjectItem("Tafseer", "علم التفسیر", "Quranic Exegesis & Interpretation", "قرآن کریم کی معتبر تفاسیر"),
        SubjectItem("Balagha", "علم البلاغت", "Arabic Rhetoric & Literary Arts", "معانی، بیان اور فصاحت و بلاغت"),
        SubjectItem("Mantiq", "علم المنطق", "Formal Logic & Reasoning", "قواعدِ استدلال اور صوری منطق"),
        SubjectItem("Falsafa", "الفلسفۃ", "Islamic Philosophy & Rationality", "اسلامی فلسفہ و معقولات"),
        SubjectItem("Aqeedah", "علم العقائد", "Theology & Islamic Beliefs", "اہلِ سنت والجماعت کے عقائد"),
        SubjectItem("Arabic Literature", "الأدب العربي", "Classic Prose & Poetry", "عربی نثر، نظم اور شروحات"),
        SubjectItem("Insha", "انشاء و تحریر", "Arabic Essay & Composition", "عربی تحریر اور انشاء پردازی"),
        SubjectItem("Tajweed", "تجوید القرآن", "Quranic Recitation Rules", "تلاوتِ قرآن کے صوتی قواعد"),
        SubjectItem("History", "تاریخِ اسلام", "Islamic History & Civilizations", "اسلامی تاریخ اور سیرتِ طیبہ")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = lStr("subjects"),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = lStr("classes_curriculum_header"),
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(subjectsList) { subject ->
                val booksCount = allBooks.count { it.subject.equals(subject.name, ignoreCase = true) }
                val displayName = if (isUrdu) subject.nameUrdu else subject.name
                val displayDesc = if (isUrdu) subject.descriptionUrdu else subject.description

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp)
                        .clickable { onNavigate(Screen.SubjectDetail.createRoute(subject.name)) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Category,
                                    contentDescription = displayName,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = displayName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1
                            )
                        }

                        Text(
                            text = displayDesc,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )

                        Text(
                            text = "$booksCount ${lStr("books")}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
