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
import com.example.data.model.Tafseer
import com.example.data.repository.TafseerRepository
import com.example.ui.components.GenericBookCover
import com.example.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TafseerScreen(
    onNavigate: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val tafaseer = remember { TafseerRepository.getTafaseer() }
    var searchQuery by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("all") }

    val languages = remember {
        listOf(
            "all" to "تمام تفاسیر",
            "Urdu" to "اردو",
            "Arabic" to "عربی",
            "Pashto" to "پشتو"
        )
    }

    val filteredTafaseer = remember(searchQuery, selectedLanguage, tafaseer) {
        tafaseer.filter { tafseer ->
            val matchesLanguage = when (selectedLanguage) {
                "all" -> true
                "Urdu" -> tafseer.language.contains("Urdu", ignoreCase = true)
                "Arabic" -> tafseer.language.contains("Arabic", ignoreCase = true)
                "Pashto" -> tafseer.language.contains("Pashto", ignoreCase = true)
                else -> true
            }
            val matchesSearch = searchQuery.isBlank() ||
                tafseer.titleUrdu.contains(searchQuery, ignoreCase = true) ||
                tafseer.title.contains(searchQuery, ignoreCase = true) ||
                tafseer.authorUrdu.contains(searchQuery, ignoreCase = true) ||
                tafseer.author.contains(searchQuery, ignoreCase = true) ||
                tafseer.description.contains(searchQuery, ignoreCase = true)

            matchesLanguage && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "تفاسیر قرآن مجید",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "کل ${tafaseer.size} تفاسیر و شروحات",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("tafseer_back_button")
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
            // Search Box
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("tafseer_search_input"),
                placeholder = { Text("تفسیر کا نام، مصنف یا موضوع تلاش کریں...") },
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
                        modifier = Modifier.testTag("filter_chip_$key")
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (filteredTafaseer.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "کوئی تفسیر نہیں ملی",
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
                    items(filteredTafaseer, key = { it.id }) { tafseer ->
                        TafseerItemCard(
                            tafseer = tafseer,
                            onClick = {
                                onNavigate(Screen.TafseerDetail.createRoute(tafseer.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TafseerItemCard(
    tafseer: Tafseer,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("tafseer_card_${tafseer.id}"),
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
            // Authentic Tafseer Book Cover
            GenericBookCover(
                title = tafseer.titleUrdu,
                tag = tafseer.language,
                badgeText = "${tafseer.volumes.size} جلدیں",
                author = tafseer.authorUrdu,
                themeColor = Color(0xFF1E3A8A), // Deep scholarly navy
                width = 62.dp,
                height = 86.dp
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tafseer.titleUrdu,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (tafseer.title.isNotEmpty() && tafseer.title != tafseer.titleUrdu) {
                    Text(
                        text = tafseer.title,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "مصنف: ${tafseer.authorUrdu}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (tafseer.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = tafseer.description,
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
                                text = "${tafseer.volumes.size} ${if (tafseer.volumes.size == 1) "جلد" else "جلدیں"}",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )

                    SuggestionChip(
                        onClick = {},
                        label = {
                            Text(
                                text = tafseer.language,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }
            }
        }
    }
}

