package com.example.ui.screens.islamic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Fatawa
import com.example.data.repository.FatawaRepository
import com.example.ui.components.GenericBookCover
import com.example.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FatawaScreen(
    onNavigate: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val fatawaList = remember { FatawaRepository.getFatawaList() }
    var searchQuery by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("all") }

    val languages = remember {
        listOf(
            "all" to "تمام فتاویٰ",
            "Urdu" to "اردو فتاویٰ",
            "Arabic" to "عربی فتاویٰ"
        )
    }

    val filteredFatawa = remember(searchQuery, selectedLanguage, fatawaList) {
        fatawaList.filter { fatwa ->
            val matchesLanguage = when (selectedLanguage) {
                "all" -> true
                "Urdu" -> fatwa.language.contains("Urdu", ignoreCase = true)
                "Arabic" -> fatwa.language.contains("Arabic", ignoreCase = true)
                else -> true
            }
            val matchesSearch = searchQuery.isBlank() ||
                fatwa.titleUrdu.contains(searchQuery, ignoreCase = true) ||
                fatwa.title.contains(searchQuery, ignoreCase = true) ||
                fatwa.authorUrdu.contains(searchQuery, ignoreCase = true) ||
                fatwa.author.contains(searchQuery, ignoreCase = true) ||
                fatwa.institutionUrdu.contains(searchQuery, ignoreCase = true) ||
                fatwa.description.contains(searchQuery, ignoreCase = true)

            matchesLanguage && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "فتاویٰ و فقہی انسائیکلوپیڈیا",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "کل ${fatawaList.size} معتبر مجموعہ ہائے فتاویٰ",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("fatawa_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "واپس"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("fatawa_search_input"),
                placeholder = { Text("فتاویٰ کا نام، مفتی، ادارہ یا موضوع تلاش کریں...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "تلاش"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "صاف کریں"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Language Filter Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(languages) { (key, label) ->
                    val isSelected = selectedLanguage == key
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedLanguage = key },
                        label = { Text(label) },
                        modifier = Modifier.testTag("filter_fatawa_$key")
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (filteredFatawa.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Gavel,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "کوئی فتاویٰ نہیں ملا",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "براہ کرم دوسرا لفظ تلاش کریں یا فلٹر تبدیل کریں",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredFatawa, key = { it.id }) { fatwa ->
                        FatawaItemCard(
                            fatwa = fatwa,
                            onClick = {
                                onNavigate(Screen.FatawaDetail.createRoute(fatwa.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FatawaItemCard(
    fatwa: Fatawa,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("fatawa_card_${fatwa.id}"),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Authentic Fatawa Book Cover
            GenericBookCover(
                title = fatwa.titleUrdu,
                tag = fatwa.language,
                badgeText = "${fatwa.volumes.size} جلدیں",
                author = fatwa.authorUrdu,
                themeColor = Color(0xFF6B1A25), // Rich fiqh burgundy
                width = 62.dp,
                height = 86.dp
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = fatwa.titleUrdu,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (fatwa.title.isNotEmpty() && fatwa.title != fatwa.titleUrdu) {
                    Text(
                        text = fatwa.title,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "مصنف / مفتی: ${fatwa.authorUrdu}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (fatwa.institutionUrdu.isNotEmpty()) {
                    Text(
                        text = "ادارہ / مرکز: ${fatwa.institutionUrdu}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (fatwa.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = fatwa.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SuggestionChip(
                        onClick = {},
                        label = {
                            Text(
                                text = "${fatwa.volumes.size} ${if (fatwa.volumes.size == 1) "جلد" else "جلدیں"}",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )

                    SuggestionChip(
                        onClick = {},
                        label = {
                            Text(
                                text = fatwa.language,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }
            }
        }
    }
}
