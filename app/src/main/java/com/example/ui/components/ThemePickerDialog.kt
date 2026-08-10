package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.util.LanguageManager

data class ThemeOption(
    val id: String,
    val titleKey: String,
    val icon: ImageVector
)

@Composable
fun ThemePickerDialog(
    onDismissRequest: () -> Unit,
    onThemeSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val currentLang by LanguageManager.currentLanguage.collectAsState()
    val currentTheme by LanguageManager.currentTheme.collectAsState()

    val options = listOf(
        ThemeOption("System", "system_theme", Icons.Default.SettingsSuggest),
        ThemeOption("Light", "light_theme", Icons.Default.LightMode),
        ThemeOption("Dark", "dark_theme", Icons.Default.DarkMode)
    )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = LanguageManager.getString("theme_settings", currentLang.code),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                options.forEach { option ->
                    val isSelected = currentTheme == option.id

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                LanguageManager.setTheme(context, option.id)
                                onThemeSelected(option.id)
                                onDismissRequest()
                            }
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                            else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = option.icon,
                                contentDescription = option.id,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = LanguageManager.getString(option.titleKey, currentLang.code),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            } else {
                                RadioButton(
                                    selected = false,
                                    onClick = null,
                                    colors = RadioButtonDefaults.colors(unselectedColor = MaterialTheme.colorScheme.outline),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(LanguageManager.getString("cancel", currentLang.code))
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
