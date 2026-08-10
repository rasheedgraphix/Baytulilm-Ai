package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.util.LanguageManager

private val DarkColorScheme =
  darkColorScheme(
    primary = EmeraldDarkPrimary,
    secondary = JadeDarkSecondary,
    tertiary = GoldDarkTertiary,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onPrimary = Color(0xFF003825),
    onSecondary = Color(0xFF003822),
    onBackground = Color(0xFFE1E3DF),
    onSurface = Color(0xFFE1E3DF)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = EmeraldPrimary,
    secondary = JadeSecondary,
    tertiary = GoldTertiary,
    primaryContainer = EmeraldContainerLight,
    onPrimaryContainer = OnEmeraldContainerLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF191C1A),
    onSurface = Color(0xFF191C1A)
  )

@Composable
fun BaytulIlmTheme(
  themeMode: String = LanguageManager.currentTheme.collectAsState().value,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val systemInDark = isSystemInDarkTheme()
  val darkTheme = when (themeMode) {
    "Light" -> false
    "Dark" -> true
    else -> systemInDark
  }

  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

@Composable
fun RasheedIslamicTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  BaytulIlmTheme(themeMode = if (darkTheme) "Dark" else "Light", dynamicColor = dynamicColor, content = content)
}

