package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.util.DarsNizamiMatcher

@Composable
fun DarjaClassIconBadge(
    classId: String,
    className: String = "",
    order: Int? = null,
    size: Dp = 76.dp,
    modifier: Modifier = Modifier
) {
    val classOrder = DarsNizamiMatcher.getClassOrder(if (classId.isNotBlank()) classId else className, order)
    val arabicTitle = DarsNizamiMatcher.getArabicClassTitle(if (classId.isNotBlank()) classId else className, classOrder)
    val arabicNumeral = DarsNizamiMatcher.getArabicNumeral(if (classId.isNotBlank()) classId else className, classOrder)

    val gradientColors = when (classOrder) {
        1 -> listOf(Darja1Start, Darja1End) // Fresh Emerald
        2 -> listOf(Darja2Start, Darja2End) // Ocean Blue
        3 -> listOf(Darja3Start, Darja3End) // Indigo Violet
        4 -> listOf(Darja4Start, Darja4End) // Ruby Rose
        5 -> listOf(Darja5Start, Darja5End) // Amber Sunset
        6 -> listOf(Darja6Start, Darja6End) // Bright Teal
        7 -> listOf(Darja7Start, Darja7End) // Royal Purple
        8 -> listOf(Darja8Start, Darja8End) // Radiant Gold / Bronze
        else -> listOf(Darja1Start, Darja1End)
    }

    val goldAccent = Color(0xFFFFDF78)
    val goldBorder = Color(0xFFD4AF37).copy(alpha = 0.5f)

    Surface(
        modifier = modifier
            .size(size)
            .shadow(4.dp, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.2.dp, goldBorder),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(colors = gradientColors))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            // Small top-right numeral indicator badge
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
                    .size(if (size >= 70.dp) 18.dp else 15.dp),
                shape = CircleShape,
                color = goldAccent.copy(alpha = 0.25f),
                border = BorderStroke(0.8.dp, goldAccent.copy(alpha = 0.6f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = arabicNumeral,
                        fontSize = if (size >= 70.dp) 9.5.sp else 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldAccent,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Big Prominent Arabic Class Text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 2.dp)
            ) {
                val words = arabicTitle.split(" ")
                if (words.size >= 2) {
                    Text(
                        text = words[0], // "الصف"
                        fontSize = if (size >= 70.dp) 11.sp else 9.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.88f),
                        textAlign = TextAlign.Center,
                        lineHeight = if (size >= 70.dp) 12.sp else 10.sp
                    )
                    Text(
                        text = words.drop(1).joinToString(" "), // "الأول", "الثاني", etc.
                        fontSize = if (size >= 70.dp) 14.5.sp else 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = goldAccent,
                        textAlign = TextAlign.Center,
                        lineHeight = if (size >= 70.dp) 16.sp else 13.sp
                    )
                } else {
                    Text(
                        text = arabicTitle,
                        fontSize = if (size >= 70.dp) 13.5.sp else 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = goldAccent,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
