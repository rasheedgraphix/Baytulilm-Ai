package com.example.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import com.example.R
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.sin

object ClockBitmapHelper {

    /**
     * Draws the Al-Fatiha Holy Kaaba Dial with real-time Silver Baton Hands & Red Center Pin
     */
    fun renderKaabaClockBitmap(context: Context, size: Int = 400): Bitmap {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val center = size / 2f

        // 1. Draw Dial Face
        try {
            val options = BitmapFactory.Options().apply {
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }
            val dialBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.img_kaaba_clock_dial, options)
            if (dialBitmap != null) {
                val srcRect = Rect(0, 0, dialBitmap.width, dialBitmap.height)
                val dstRect = Rect(0, 0, size, size)
                val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
                canvas.drawBitmap(dialBitmap, srcRect, dstRect, paint)
            } else {
                drawFallbackKaabaDial(canvas, size, center)
            }
        } catch (e: Throwable) {
            drawFallbackKaabaDial(canvas, size, center)
        }

        // Current time
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val hourAngle = (hour + minute / 60f) * 30f
        val minuteAngle = minute * 6f

        // Draw Hour Hand (Silver Baton with white lumen)
        drawBatonHand(
            canvas = canvas,
            center = center,
            angleDeg = hourAngle,
            length = size * 0.28f,
            width = size * 0.032f,
            tailLength = size * 0.06f,
            color = Color.parseColor("#E2E8F0"),
            innerColor = Color.parseColor("#FFFFFF")
        )

        // Draw Minute Hand (Longer Silver Baton)
        drawBatonHand(
            canvas = canvas,
            center = center,
            angleDeg = minuteAngle,
            length = size * 0.38f,
            width = size * 0.024f,
            tailLength = size * 0.07f,
            color = Color.parseColor("#CBD5E1"),
            innerColor = Color.parseColor("#FFFFFF")
        )

        // Draw Center Pin: Silver outer cap, Red center dot
        val paintPinOuter = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#E2E8F0")
            style = Paint.Style.FILL
        }
        canvas.drawCircle(center, center, size * 0.028f, paintPinOuter)

        val paintPinInner = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#EF4444")
            style = Paint.Style.FILL
        }
        canvas.drawCircle(center, center, size * 0.016f, paintPinInner)

        return bitmap
    }

    /**
     * Draws the Neon Cyan Tactical Clock (matching uploaded image exactly)
     */
    fun renderNeonTacticalClockBitmap(size: Int = 400): Bitmap {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val center = size / 2f
        val radius = size / 2f - 4f

        // 1. Dial Background - Deep Carbon AMOLED Black
        val paintBg = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#040608")
            style = Paint.Style.FILL
        }
        canvas.drawCircle(center, center, radius, paintBg)

        // Dial border
        val paintBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#0F172A")
            style = Paint.Style.STROKE
            strokeWidth = size * 0.015f
        }
        canvas.drawCircle(center, center, radius, paintBorder)

        // 2. 60 Minute Radial Ticks (Neon Cyan & Teal)
        val tickMajorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#2DD4BF")
            strokeWidth = size * 0.014f
            strokeCap = Paint.Cap.ROUND
        }
        val tickMinorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#0F766E")
            strokeWidth = size * 0.006f
            strokeCap = Paint.Cap.ROUND
        }

        val outerTickR = radius - size * 0.02f
        for (i in 0 until 60) {
            val angleRad = Math.toRadians((i * 6 - 90).toDouble())
            val isMajor = (i % 5 == 0)
            val innerTickR = if (isMajor) outerTickR - size * 0.065f else outerTickR - size * 0.035f
            val paint = if (isMajor) tickMajorPaint else tickMinorPaint

            val startX = (center + innerTickR * cos(angleRad)).toFloat()
            val startY = (center + innerTickR * sin(angleRad)).toFloat()
            val stopX = (center + outerTickR * cos(angleRad)).toFloat()
            val stopY = (center + outerTickR * sin(angleRad)).toFloat()

            canvas.drawLine(startX, startY, stopX, stopY, paint)
        }

        // 3. Inner Red Accent Ring (as in the photo)
        val redRingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#DC2626")
            style = Paint.Style.STROKE
            strokeWidth = size * 0.007f
        }
        canvas.drawCircle(center, center, size * 0.23f, redRingPaint)

        // 4. Large Glowing Cyan Numbers: 12, 03, 06, 09
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#2DD4BF")
            textSize = size * 0.095f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
            setShadowLayer(size * 0.03f, 0f, 0f, Color.parseColor("#802DD4BF"))
        }

        val numOffset = size * 0.17f
        // 12 (top)
        canvas.drawText("12", center, center - numOffset + textPaint.textSize * 0.35f, textPaint)
        // 06 (bottom)
        canvas.drawText("06", center, center + numOffset + textPaint.textSize * 0.35f, textPaint)
        // 03 (right)
        canvas.drawText("03", center + numOffset, center + textPaint.textSize * 0.35f, textPaint)
        // 09 (left)
        canvas.drawText("09", center - numOffset, center + textPaint.textSize * 0.35f, textPaint)

        // Current time
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val hourAngle = (hour + minute / 60f) * 30f
        val minuteAngle = (minute + second / 60f) * 6f

        // 5. Glowing Cyan Sword Hands
        // Hour Hand
        drawSwordHand(
            canvas = canvas,
            center = center,
            angleDeg = hourAngle,
            length = size * 0.26f,
            width = size * 0.035f,
            tail = size * 0.07f,
            color = Color.parseColor("#14B8A6"),
            lumenColor = Color.parseColor("#99F6E4")
        )

        // Minute Hand
        drawSwordHand(
            canvas = canvas,
            center = center,
            angleDeg = minuteAngle,
            length = size * 0.36f,
            width = size * 0.026f,
            tail = size * 0.08f,
            color = Color.parseColor("#2DD4BF"),
            lumenColor = Color.parseColor("#CCFBF1")
        )

        // 6. Red Second Hand (Thin Needle)
        val secondAngle = second * 6f
        canvas.save()
        canvas.rotate(secondAngle, center, center)

        val redSecondPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#EF4444")
            strokeWidth = size * 0.007f
            strokeCap = Paint.Cap.ROUND
        }
        canvas.drawLine(center, center + size * 0.09f, center, center - size * 0.40f, redSecondPaint)

        // Center Black Hub with Red Accent
        val hubPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#040608")
            style = Paint.Style.FILL
        }
        canvas.drawCircle(center, center, size * 0.035f, hubPaint)

        val hubBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#EF4444")
            style = Paint.Style.FILL
        }
        canvas.drawCircle(center, center, size * 0.016f, hubBorderPaint)

        canvas.restore()

        return bitmap
    }

    private fun drawSwordHand(
        canvas: Canvas,
        center: Float,
        angleDeg: Float,
        length: Float,
        width: Float,
        tail: Float,
        color: Int,
        lumenColor: Int
    ) {
        canvas.save()
        canvas.rotate(angleDeg, center, center)

        val halfW = width / 2f
        val path = Path().apply {
            moveTo(center - halfW, center + tail * 0.4f)
            lineTo(center - halfW * 0.8f, center - length * 0.82f)
            lineTo(center, center - length)
            lineTo(center + halfW * 0.8f, center - length * 0.82f)
            lineTo(center + halfW, center + tail * 0.4f)
            lineTo(center, center + tail)
            close()
        }

        val handPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = color
            style = Paint.Style.FILL
            setShadowLayer(6f, 0f, 0f, color)
        }
        canvas.drawPath(path, handPaint)

        // Inner Luminous Strip
        val lumenPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = lumenColor
            strokeWidth = width * 0.32f
            strokeCap = Paint.Cap.ROUND
        }
        canvas.drawLine(center, center + tail * 0.2f, center, center - length * 0.86f, lumenPaint)

        canvas.restore()
    }

    private fun drawBatonHand(
        canvas: Canvas,
        center: Float,
        angleDeg: Float,
        length: Float,
        width: Float,
        tailLength: Float,
        color: Int,
        innerColor: Int
    ) {
        canvas.save()
        canvas.rotate(angleDeg, center, center)

        val halfW = width / 2f
        val rectF = RectF(center - halfW, center - length, center + halfW, center + tailLength)

        val handPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = color
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(rectF, halfW * 0.5f, halfW * 0.5f, handPaint)

        // Inner lumen stripe
        val innerHalfW = halfW * 0.45f
        val innerRect = RectF(center - innerHalfW, center - length * 0.90f, center + innerHalfW, center - length * 0.15f)
        val lumenPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = innerColor
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(innerRect, innerHalfW * 0.5f, innerHalfW * 0.5f, lumenPaint)

        canvas.restore()
    }

    private fun drawFallbackKaabaDial(canvas: Canvas, size: Int, center: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#09101A")
            style = Paint.Style.FILL
        }
        canvas.drawCircle(center, center, center - 2f, paint)
    }
}
