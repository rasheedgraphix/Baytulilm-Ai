package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.util.LocalAppLanguage

/**
 * WhatsApp Meta AI style floating button.
 * Features an iridescent gradient ring, glowing AI icon, subtle pulse, and instant click color feedback.
 */
@Composable
fun WhatsAppStyleAiFloatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val langCode = LocalAppLanguage.current.code

    // Gentle pulsing animation like Meta AI on WhatsApp
    val infiniteTransition = rememberInfiniteTransition(label = "ai_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    // Meta AI iridescent multi-color gradient (Cyan -> Islamic Emerald -> Royal Indigo -> Gold)
    val gradientBrush = Brush.sweepGradient(
        listOf(
            Color(0xFF00C6FF), // Bright Cyan
            Color(0xFF0072FF), // Royal Blue
            Color(0xFF0F9D58), // Islamic Emerald
            Color(0xFFFFD700), // Gold
            Color(0xFF7F00FF), // Violet
            Color(0xFF00C6FF)  // Loop back
        )
    )

    val pressedScale = if (isPressed) 0.92f else 1.0f

    Surface(
        modifier = modifier
            .scale(pressedScale * pulseScale)
            .shadow(
                elevation = if (isPressed) 4.dp else 10.dp,
                shape = CircleShape,
                ambientColor = Color(0xFF0072FF).copy(alpha = 0.4f),
                spotColor = Color(0xFF0F9D58).copy(alpha = 0.5f)
            )
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = Color.White),
                onClick = onClick
            )
            .testTag("whatsapp_meta_ai_button"),
        shape = CircleShape,
        color = Color.Transparent
    ) {
        // Outer iridescent border ring
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(gradientBrush)
                .padding(3.dp),
            contentAlignment = Alignment.Center
        ) {
            val innerBrush = if (isPressed) {
                Brush.linearGradient(
                    listOf(
                        Color(0xFF0072FF),
                        Color(0xFF00C6FF)
                    )
                )
            } else {
                Brush.linearGradient(
                    listOf(
                        Color(0xFF0E1A2B), // Deep Navy
                        Color(0xFF0A2B1D)  // Deep Emerald
                    )
                )
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(innerBrush)
                    .size(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = if (langCode == "ps") "د AI مرستیال" else if (langCode == "ur") "معاون AI" else "AI Assistant",
                    tint = if (isPressed) Color(0xFFFFD700) else Color(0xFFE0F2FE),
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}
