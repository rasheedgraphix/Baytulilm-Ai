package com.example.ui.components

import android.graphics.Bitmap as AndroidBitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.BookEntity
import com.example.ui.theme.*
import com.example.util.LocalAppLanguage
import com.example.util.lStr

@Composable
fun BookCoverThumbnailView(
    book: BookEntity,
    thumbnail: AndroidBitmap?,
    modifier: Modifier = Modifier,
    width: androidx.compose.ui.unit.Dp = 92.dp,
    height: androidx.compose.ui.unit.Dp = 126.dp
) {
    val lang = LocalAppLanguage.current
    val displayTitle = book.getDisplayName(lang.code)
    val scale = (width.value / 92f).coerceIn(0.8f, 2.8f)

    // Straight, upright rectangular Book Cover (0° straight alignment, real first page)
    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF1E293B)),
        contentAlignment = Alignment.Center
    ) {
        if (thumbnail != null) {
            // Actual first page / authentic cover of the book rendered straight and crisp
            Image(
                bitmap = thumbnail.asImageBitmap(),
                contentDescription = book.title,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            // Clean, elegant reader placeholder until authentic PDF first page is rendered
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF0F172A))
                    .padding((6 * scale).dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top subject tag
                    Text(
                        text = book.subject.ifBlank { book.darja },
                        color = Color(0xFF38BDF8),
                        fontSize = (8f * scale).sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Center book icon & title
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Book,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size((22 * scale).dp)
                        )
                        Spacer(modifier = Modifier.height((4 * scale).dp))
                        Text(
                            text = displayTitle,
                            color = Color.White,
                            fontSize = (9.5f * scale).sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = (13f * scale).sp
                        )
                    }

                    // Bottom authentic page hint
                    Text(
                        text = "صفحہ اول (PDF)",
                        color = Color(0xFF64748B),
                        fontSize = (7f * scale).sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun GenericBookCover(
    title: String,
    subtitle: String = "",
    tag: String = "",
    author: String = "",
    themeColor: Color = Color(0xFF047857),
    modifier: Modifier = Modifier,
    width: androidx.compose.ui.unit.Dp = 64.dp,
    height: androidx.compose.ui.unit.Dp = 88.dp,
    badgeText: String? = null
) {
    val scale = (width.value / 64f).coerceIn(0.7f, 2.5f)
    val goldAccent = Color(0xFFFDE68A)
    val darkBase = Color(
        red = (themeColor.red * 0.35f),
        green = (themeColor.green * 0.35f),
        blue = (themeColor.blue * 0.35f)
    )

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape((6 * scale).dp))
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(themeColor, darkBase, Color(0xFF0F172A))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding((4 * scale).dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top tag / badge
            if (tag.isNotBlank() || badgeText != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape((3 * scale).dp))
                        .background(Color.Black.copy(alpha = 0.35f))
                        .padding(vertical = (1.5f * scale).dp, horizontal = (2 * scale).dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badgeText ?: tag,
                        color = goldAccent,
                        fontSize = (7.5f * scale).sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else {
                Spacer(modifier = Modifier.height((2 * scale).dp))
            }

            // Center Title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = (1 * scale).dp)
                    .clip(RoundedCornerShape((4 * scale).dp))
                    .background(Color.Black.copy(alpha = 0.45f))
                    .padding(horizontal = (3 * scale).dp, vertical = (3 * scale).dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = (9f * scale).sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = (12f * scale).sp
                )
            }

            // Bottom author or subtitle
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (author.isNotBlank()) {
                    Text(
                        text = author,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = (6.5f * scale).sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                } else if (subtitle.isNotBlank()) {
                    Text(
                        text = subtitle,
                        color = goldAccent.copy(alpha = 0.85f),
                        fontSize = (6.5f * scale).sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        // Spine Crease / Hinge shadow on the left
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width((5 * scale).dp)
                .align(Alignment.CenterStart)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.5f),
                            Color.White.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

@Composable
fun BookCard(
    book: BookEntity,
    onLoadThumbnail: suspend (BookEntity) -> AndroidBitmap?,
    onReadClick: () -> Unit,
    onDetailClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onBookmarkToggle: () -> Unit,
    onDownloadClick: () -> Unit,
    onAiChatClick: (() -> Unit)? = null,
    onAiQuizClick: (() -> Unit)? = null,
    onAiNotesClick: (() -> Unit)? = null,
    onNavigate: (String) -> Unit = {},
    downloadProgress: Float? = null,
    isDownloading: Boolean = false,
    onCancelDownload: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var thumbnail by remember(book.id) { mutableStateOf<AndroidBitmap?>(null) }
    
    LaunchedEffect(book.id, book.pdfUrl, book.isDownloaded, isDownloading) {
        thumbnail = onLoadThumbnail(book)
    }

    Card(
        onClick = onDetailClick,
        modifier = modifier
            .fillMaxWidth()
            .testTag("book_card_${book.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Book Cover (Rendered first page or authentic Islamic cover)
                BookCoverThumbnailView(
                    book = book,
                    thumbnail = thumbnail,
                    width = 92.dp,
                    height = 126.dp
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Book Information
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val (darjaBg, darjaTxt) = when {
                            book.darja.contains("اولیٰ") || book.darja.contains("اول") -> Color(0xFFDCFCE7) to Color(0xFF15803D)
                            book.darja.contains("ثانیہ") || book.darja.contains("دوم") -> Color(0xFFE0F2FE) to Color(0xFF0369A1)
                            book.darja.contains("ثالثہ") || book.darja.contains("سوم") -> Color(0xFFEDE9FE) to Color(0xFF6D28D9)
                            book.darja.contains("رابعہ") || book.darja.contains("چہارم") -> Color(0xFFFFE4E6) to Color(0xFFBE123C)
                            book.darja.contains("خامسہ") || book.darja.contains("پنجم") -> Color(0xFFFEF3C7) to Color(0xFFB45309)
                            book.darja.contains("سادسہ") || book.darja.contains("ششم") -> Color(0xFFCCFBF1) to Color(0xFF0F766E)
                            book.darja.contains("سابعہ") || book.darja.contains("ہفتم") -> Color(0xFFF3E8FF) to Color(0xFF7E22CE)
                            book.darja.contains("حدیث") || book.darja.contains("ثامنہ") -> Color(0xFFFEF9C3) to Color(0xFFA16207)
                            else -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.primary
                        }

                        Text(
                            text = book.darja,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = darjaTxt,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(darjaBg)
                                .padding(horizontal = 7.dp, vertical = 2.5.dp)
                        )

                        Row {
                            IconButton(
                                onClick = onBookmarkToggle,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = if (book.isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = if (book.isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            IconButton(
                                onClick = onFavoriteToggle,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = if (book.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (book.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    val currentLang = LocalAppLanguage.current
                    Text(
                        text = book.getDisplayName(currentLang.code),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    val authorLabel = if (currentLang.code == "en") "By " else "مصنف: "
                    val subjectLabel = if (currentLang.code == "en") "Subject: " else "مضمون: "

                    Text(
                        text = "$authorLabel${book.author}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    val (subjectBg, subjectTxt) = when {
                        book.subject.contains("حدیث") || book.subject.contains("hadith") -> ChipHadithBg to ChipHadithText
                        book.subject.contains("فقہ") || book.subject.contains("اصول") -> ChipFiqhBg to ChipFiqhText
                        book.subject.contains("نحو") || book.subject.contains("صرف") -> ChipNahwBg to ChipNahwText
                        book.subject.contains("تفسیر") || book.subject.contains("قرآن") -> ChipTafseerBg to ChipTafseerText
                        book.subject.contains("دعا") || book.subject.contains("اذکار") -> ChipDuaBg to ChipDuaText
                        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "${book.subject} • ${book.type}",
                            fontSize = 11.sp,
                            color = subjectTxt,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(subjectBg)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isDownloading) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp)
                        ) {
                            val pct = ((downloadProgress ?: 0f) * 100).toInt().coerceIn(0, 100)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (pct > 0) "Downloading... $pct%" else "Downloading...",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                TextButton(
                                    onClick = { onCancelDownload?.invoke() },
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
                                    modifier = Modifier.height(26.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Cancel",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = lStr("cancel"),
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { downloadProgress ?: 0f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                            )
                        }
                    } else {
                        // Action Buttons Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = onReadClick,
                                modifier = Modifier.height(34.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = lStr("read_now"),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = lStr("read_now"), fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = onDownloadClick,
                                modifier = Modifier.height(34.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = if (book.isDownloaded) Icons.Filled.Download else Icons.Outlined.DownloadForOffline,
                                    contentDescription = lStr("download_pdf"),
                                    tint = if (book.isDownloaded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (book.isDownloaded) lStr("downloaded") else lStr("downloads"),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // AI Smart Suite Buttons Row (AI Chat ✨)
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                onClick = { onAiChatClick?.invoke() },
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Chat",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "AI Chat ✨",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Copyright Notice
            com.example.ui.components.CopyrightDisclaimerComponent(
                onContactClick = { onNavigate(com.example.ui.navigation.Screen.About.route) }
            )
        }
    }
}

