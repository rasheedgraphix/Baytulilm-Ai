package com.example.ui.screens.madrasa

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.AiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OcrAssistantScreen(
    aiViewModel: AiViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var isProcessing by remember { mutableStateOf(false) }

    var arabicText by remember {
        mutableStateOf("الطهور شطر الإيمان والحمد لله تملأ الميزان وسبحان الله والحمد لله تملآن ما بين السماوات والأرض")
    }
    var urduText by remember {
        mutableStateOf("پاکیزگی ایمان کا نصف حصہ ہے اور الحمد للہ ترازو کو بھر دیتا ہے۔")
    }
    var englishText by remember {
        mutableStateOf("Purity is half of faith, and Al-hamdulillah fills the scale of good deeds.")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Page OCR & AI Scanner", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DocumentScanner, contentDescription = null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Arabic & Urdu Book Page OCR", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("Capture or upload Kitab pages to extract text & generate AI notes.", fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    isProcessing = true
                                    // Simulate camera capture & OCR processing
                                    isProcessing = false
                                    Toast.makeText(context, "Page captured and Arabic text extracted!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.PhotoCamera, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Camera")
                            }

                            OutlinedButton(
                                onClick = {
                                    Toast.makeText(context, "Selected page from Gallery!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Gallery")
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Extracted Arabic Text (النص العربي)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                            IconButton(onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Arabic OCR", arabicText))
                                Toast.makeText(context, "Copied Arabic text!", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(18.dp))
                            }
                        }

                        OutlinedTextField(
                            value = arabicText,
                            onValueChange = { arabicText = it },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 4
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Urdu Translation (اردو ترجمہ)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        OutlinedTextField(
                            value = urduText,
                            onValueChange = { urduText = it },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("English Translation", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        OutlinedTextField(
                            value = englishText,
                            onValueChange = { englishText = it },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    aiViewModel.sendRAGMessage("Please explain this extracted Kitab passage and provide Fiqhi & Nahwi analysis: $arabicText", emptyList())
                                    Toast.makeText(context, "Sent to AI Scholar for Analysis!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ask AI")
                            }

                            OutlinedButton(
                                onClick = {
                                    aiViewModel.saveNote("OCR Note: Kitab Passage", "$arabicText\n\nUrdu: $urduText\n\nEnglish: $englishText")
                                    Toast.makeText(context, "Saved to AI Study Notes!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.BookmarkAdd, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Save Note")
                            }
                        }
                    }
                }
            }
        }
    }
}
