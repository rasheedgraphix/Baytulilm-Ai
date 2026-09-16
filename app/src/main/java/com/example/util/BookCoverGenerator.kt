package com.example.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.example.data.model.BookEntity
import kotlin.math.abs

/**
 * Generates authentic, high-resolution Islamic Book First Page & Title Covers (سرورقِ کتاب).
 * Used to provide authentic covers for all Dars-e-Nizami books before downloading.
 */
object BookCoverGenerator {

    data class CoverPalette(
        val topLeather: Int,
        val bottomLeather: Int,
        val subLeather: Int,
        val goldPrimary: Int,
        val goldSecondary: Int,
        val plateBackground: Int
    )

    fun getPaletteForBook(book: BookEntity): CoverPalette {
        val s = (book.subject + " " + book.darja).lowercase()
        val hash = abs(book.id.hashCode()) % 3

        return when {
            s.contains("حدیث") || s.contains("hadith") -> {
                // Imperial Emerald Green & Gold (حدیث شریف کا روایتی سبز و زریں غلاف)
                when (hash) {
                    0 -> CoverPalette(0xFF0D3D28.toInt(), 0xFF031910.toInt(), 0xFF08271A.toInt(), 0xFFF7D973.toInt(), 0xFFD4AF37.toInt(), 0xEE051D13.toInt())
                    1 -> CoverPalette(0xFF09442C.toInt(), 0xFF021E13.toInt(), 0xFF052B1B.toInt(), 0xFFFBE18A.toInt(), 0xFFDAAE3E.toInt(), 0xEE032014.toInt())
                    else -> CoverPalette(0xFF073322.toInt(), 0xFF01140C.toInt(), 0xFF042015.toInt(), 0xFFEFCF62.toInt(), 0xFFC99E2F.toInt(), 0xEE02170E.toInt())
                }
            }
            s.contains("فقہ") || s.contains("fiqh") || s.contains("اصول") -> {
                // Royal Ruby / Burgundy & Antique Gold (فقہ و اصول کا روایتی یاقوتی و سنہری سرورق)
                when (hash) {
                    0 -> CoverPalette(0xFF50121C.toInt(), 0xFF220509.toInt(), 0xFF360B12.toInt(), 0xFFF7DC83.toInt(), 0xFFDCB14B.toInt(), 0xEE2A060C.toInt())
                    1 -> CoverPalette(0xFF631823.toInt(), 0xFF29080E.toInt(), 0xFF430E17.toInt(), 0xFFFCE392.toInt(), 0xFFE2B755.toInt(), 0xEE330911.toInt())
                    else -> CoverPalette(0xFF3F0B13.toInt(), 0xFF180306.toInt(), 0xFF2B070D.toInt(), 0xFFECC464.toInt(), 0xFFC99732.toInt(), 0xEE1E0308.toInt())
                }
            }
            s.contains("نحو") || s.contains("nahw") -> {
                // Deep Midnight Sapphire & Gold (علمِ نحو کا شاہی نیلا و سنہری غلاف)
                when (hash) {
                    0 -> CoverPalette(0xFF1B365D.toInt(), 0xFF0A1628.toInt(), 0xFF12223B.toInt(), 0xFFF5D678.toInt(), 0xFFD8B042.toInt(), 0xEE0E1C30.toInt())
                    1 -> CoverPalette(0xFF244473.toInt(), 0xFF0E1C32.toInt(), 0xFF172C4C.toInt(), 0xFFFCE18F.toInt(), 0xFFE0BB50.toInt(), 0xEE12223B.toInt())
                    else -> CoverPalette(0xFF152947.toInt(), 0xFF070F1C.toInt(), 0xFF0D1B30.toInt(), 0xFFEAC15B.toInt(), 0xFFC99B2D.toInt(), 0xEE0A1424.toInt())
                }
            }
            s.contains("صرف") || s.contains("sarf") -> {
                // Forest Jade & Warm Gold (علمِ صرف کا جنگلی زمردی و سنہری سرورق)
                when (hash) {
                    0 -> CoverPalette(0xFF104A30.toInt(), 0xFF052115.toInt(), 0xFF0A301F.toInt(), 0xFFF7DC85.toInt(), 0xFFDCB24A.toInt(), 0xEE072618.toInt())
                    1 -> CoverPalette(0xFF165C3C.toInt(), 0xFF072A1B.toInt(), 0xFF0E3E28.toInt(), 0xFFFBE496.toInt(), 0xFFE3B957.toInt(), 0xEE0A3120.toInt())
                    else -> CoverPalette(0xFF0C3824.toInt(), 0xFF03170E.toInt(), 0xFF072417.toInt(), 0xFFE8BF5C.toInt(), 0xFFC7982B.toInt(), 0xEE051B10.toInt())
                }
            }
            s.contains("تفسیر") || s.contains("قرآن") || s.contains("تجوید") -> {
                // Midnight Teal & Gold (تفسیر و علوم القرآن کا گہرا فیروزی و زریں غلاف)
                when (hash) {
                    0 -> CoverPalette(0xFF0A3C3A.toInt(), 0xFF031918.toInt(), 0xFF062826.toInt(), 0xFFF5D679.toInt(), 0xFFD6AE3F.toInt(), 0xEE052120.toInt())
                    1 -> CoverPalette(0xFF0E4C49.toInt(), 0xFF052322.toInt(), 0xFF093634.toInt(), 0xFFFCE18F.toInt(), 0xFFDFB94F.toInt(), 0xEE072C2A.toInt())
                    else -> CoverPalette(0xFF072D2C.toInt(), 0xFF021211.toInt(), 0xFF041E1D.toInt(), 0xFFEBC45F.toInt(), 0xFFC99B2F.toInt(), 0xEE031716.toInt())
                }
            }
            s.contains("منطق") || s.contains("بلاغت") || s.contains("ادب") -> {
                // Royal Regal Purple / Amethyst & Gold (منطق و بلاغت کا جامنی و سنہری سرورق)
                when (hash) {
                    0 -> CoverPalette(0xFF381545.toInt(), 0xFF16041D.toInt(), 0xFF250C2E.toInt(), 0xFFF7D974.toInt(), 0xFFDCB13F.toInt(), 0xEE1E0826.toInt())
                    1 -> CoverPalette(0xFF481D58.toInt(), 0xFF1D0727.toInt(), 0xFF30113C.toInt(), 0xFFFDE894.toInt(), 0xFFE5BE53.toInt(), 0xEE270C34.toInt())
                    else -> CoverPalette(0xFF2B0D36.toInt(), 0xFF0F0214.toInt(), 0xFF1B0723.toInt(), 0xFFECC75F.toInt(), 0xFFC99B2D.toInt(), 0xEE14031B.toInt())
                }
            }
            else -> {
                // Classic Moroccan Leather Brown & Gold (کلاسیکی چرمی براؤن و سنہری سرورق)
                when (hash) {
                    0 -> CoverPalette(0xFF422312.toInt(), 0xFF1B0C05.toInt(), 0xFF2B160B.toInt(), 0xFFF3D274.toInt(), 0xFFD5A93D.toInt(), 0xEE221006.toInt())
                    1 -> CoverPalette(0xFF502C17.toInt(), 0xFF231107.toInt(), 0xFF351C0E.toInt(), 0xFFF8DD89.toInt(), 0xFFDEB54F.toInt(), 0xEE2B150A.toInt())
                    else -> CoverPalette(0xFF33190C.toInt(), 0xFF130703.toInt(), 0xFF210E06.toInt(), 0xFFE5BF5A.toInt(), 0xFFC49326.toInt(), 0xEE180A04.toInt())
                }
            }
        }
    }

    /**
     * Generates a realistic, highly detailed Islamic book first page / cover Bitmap.
     */
    fun generateCoverBitmap(
        book: BookEntity,
        width: Int = 540,
        height: Int = 760
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val palette = getPaletteForBook(book)

        // 1. Rich Leather Background Gradient
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                intArrayOf(palette.topLeather, palette.subLeather, palette.bottomLeather),
                floatArrayOf(0.0f, 0.45f, 1.0f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // 2. Realistic 3D Book Spine & Crease on the Left
        val spinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, 36f, 0f,
                intArrayOf(
                    Color.argb(160, 0, 0, 0),
                    Color.argb(80, 255, 255, 255),
                    Color.argb(20, 0, 0, 0),
                    Color.argb(120, 0, 0, 0)
                ),
                floatArrayOf(0.0f, 0.35f, 0.70f, 1.0f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, 36f, height.toFloat(), spinePaint)

        // Subtle embossed spine stitch line
        val stitchPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = palette.goldSecondary
            alpha = 70
            strokeWidth = 2f
            style = Paint.Style.STROKE
        }
        canvas.drawLine(34f, 20f, 34f, height - 20f, stitchPaint)

        // 3. Ornate Double Golden Borders (چار دیواری)
        val margin = 26f
        val outerBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = palette.goldPrimary
            style = Paint.Style.STROKE
            strokeWidth = 4.5f
        }
        val outerRect = RectF(margin + 12f, margin, width - margin, height - margin)
        canvas.drawRoundRect(outerRect, 14f, 14f, outerBorderPaint)

        val innerBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = palette.goldSecondary
            style = Paint.Style.STROKE
            strokeWidth = 1.6f
        }
        val innerRect = RectF(margin + 20f, margin + 8f, width - margin - 8f, height - margin - 8f)
        canvas.drawRoundRect(innerRect, 10f, 10f, innerBorderPaint)

        // Corner Flourishes (چاروں کونوں پر سنہری دیدہ زیب نقش)
        drawCornerFlourish(canvas, innerRect.left, innerRect.top, 1, 1, palette.goldPrimary)
        drawCornerFlourish(canvas, innerRect.right, innerRect.top, -1, 1, palette.goldPrimary)
        drawCornerFlourish(canvas, innerRect.left, innerRect.bottom, 1, -1, palette.goldPrimary)
        drawCornerFlourish(canvas, innerRect.right, innerRect.bottom, -1, -1, palette.goldPrimary)

        val textCenterX = (innerRect.left + innerRect.right) / 2f

        // 4. Bismillah Calligraphy Header
        val bismillahPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = palette.goldPrimary
            textSize = 21f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", textCenterX, margin + 42f, bismillahPaint)

        // Decorative Divider under Bismillah
        val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = palette.goldSecondary
            alpha = 180
            textSize = 12f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("❖ ───  ۞  ─── ❖", textCenterX, margin + 64f, dividerPaint)

        // 5. Darja & Series Cartouche Badge
        val darjaBadgeRect = RectF(textCenterX - 145f, margin + 78f, textCenterX + 145f, margin + 114f)
        val badgeBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            alpha = 100
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(darjaBadgeRect, 18f, 18f, badgeBgPaint)

        val badgeBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = palette.goldSecondary
            style = Paint.Style.STROKE
            strokeWidth = 1.2f
        }
        canvas.drawRoundRect(darjaBadgeRect, 18f, 18f, badgeBorderPaint)

        val darjaTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = palette.goldPrimary
            textSize = 15f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        val darjaLabel = if (book.darja.isNotBlank()) "نصابِ درسِ نظامی • ${book.darja}" else "سلسلۂ کتبِ درسِ نظامی"
        canvas.drawText(darjaLabel, textCenterX, margin + 102f, darjaTextPaint)

        // 6. Central Main Title Plate (کتاب کا جلی اور مرصع عنوان)
        val plateTop = margin + 130f
        val plateBottom = plateTop + 195f
        val plateLeft = innerRect.left + 16f
        val plateRight = innerRect.right - 16f
        val titlePlateRect = RectF(plateLeft, plateTop, plateRight, plateBottom)

        val platePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = palette.plateBackground
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(titlePlateRect, 12f, 12f, platePaint)

        val plateBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = palette.goldPrimary
            style = Paint.Style.STROKE
            strokeWidth = 2.4f
        }
        canvas.drawRoundRect(titlePlateRect, 12f, 12f, plateBorder)

        // Inner golden border of plate
        val plateInner = RectF(plateLeft + 4f, plateTop + 4f, plateRight - 4f, plateBottom - 4f)
        val plateInnerBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = palette.goldSecondary
            style = Paint.Style.STROKE
            strokeWidth = 1.0f
            alpha = 160
        }
        canvas.drawRoundRect(plateInner, 8f, 8f, plateInnerBorder)

        // Draw Title using StaticLayout for high-quality multiline rendering
        val displayTitle = book.title.ifBlank { "کتابِ درسِ نظامی" }
        val titlePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
            textSize = when {
                displayTitle.length <= 18 -> 31f
                displayTitle.length <= 32 -> 25f
                displayTitle.length <= 50 -> 21f
                else -> 18f
            }
            setShadowLayer(5f, 0f, 2f, Color.argb(200, 0, 0, 0))
        }

        val availableTitleWidth = (titlePlateRect.width() - 24f).toInt().coerceAtLeast(100)
        val titleLayout = StaticLayout.Builder.obtain(
            displayTitle, 0, displayTitle.length, titlePaint, availableTitleWidth
        ).setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setLineSpacing(3f, 1.15f)
            .build()

        val titleHeight = titleLayout.height
        val titleY = plateTop + (titlePlateRect.height() - titleHeight) / 2f
        canvas.save()
        canvas.translate(textCenterX, titleY)
        titleLayout.draw(canvas)
        canvas.restore()

        // 7. Subtitle / Book Type / Edition Description
        val typeLabel = when {
            book.type.isNotBlank() -> book.type
            book.titleUrdu.isNotBlank() && book.titleUrdu != book.title -> book.titleUrdu
            else -> "نسخہ معتمدہ مع تعلیقات و حواشی"
        }
        val typePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = palette.goldPrimary
            textSize = 15.5f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("《 $typeLabel 》", textCenterX, plateBottom + 34f, typePaint)

        // 8. Subject Badge (مضمون)
        val subjPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            alpha = 230
            textSize = 14f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textAlign = Paint.Align.CENTER
        }
        val subjText = "مضمون: ${book.subject.ifBlank { "علومِ اسلامیہ" }}"
        canvas.drawText(subjText, textCenterX, plateBottom + 64f, subjPaint)

        // 9. Author Section (مصنف / شارح)
        val authorSectionTop = plateBottom + 86f
        val authorLabelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = palette.goldSecondary
            textSize = 14f
            typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("تالیف و تحقیق:", textCenterX, authorSectionTop + 8f, authorLabelPaint)

        val authorName = book.author.ifBlank { "علمائے اہل سنت والجماعت" }
        val authorPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = when {
                authorName.length <= 25 -> 20f
                authorName.length <= 40 -> 17f
                else -> 15f
            }
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
            setShadowLayer(3f, 0f, 1f, Color.argb(180, 0, 0, 0))
        }

        val authorLayout = StaticLayout.Builder.obtain(
            authorName, 0, authorName.length, authorPaint, (innerRect.width() - 40f).toInt()
        ).setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setLineSpacing(2f, 1.1f)
            .build()

        canvas.save()
        canvas.translate(textCenterX, authorSectionTop + 24f)
        authorLayout.draw(canvas)
        canvas.restore()

        // 10. Bottom Publisher / Maktaba Emblem & Seal (المطبع والناشر)
        val sealY = height - margin - 58f
        val sealRadius = 26f
        val sealPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = palette.goldSecondary
            style = Paint.Style.STROKE
            strokeWidth = 1.8f
        }
        canvas.drawCircle(textCenterX, sealY, sealRadius, sealPaint)

        val sealInnerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = palette.goldPrimary
            style = Paint.Style.STROKE
            strokeWidth = 1f
            alpha = 180
        }
        canvas.drawCircle(textCenterX, sealY, sealRadius - 4f, sealInnerPaint)

        val sealTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = palette.goldPrimary
            textSize = 9.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("مکتبۃ", textCenterX, sealY - 4f, sealTextPaint)
        canvas.drawText("بیت العلم", textCenterX, sealY + 9f, sealTextPaint)

        // Bottom imprint text
        val imprintPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            alpha = 175
            textSize = 10.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("طبع و اشاعت: وفاق المدارس العربیہ • نسخہ معتمدہ", textCenterX, height - margin - 14f, imprintPaint)

        return bitmap
    }

    private fun drawCornerFlourish(canvas: Canvas, x: Float, y: Float, dirX: Int, dirY: Int, color: Int) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = color
            style = Paint.Style.STROKE
            strokeWidth = 1.8f
        }
        val size = 16f
        val path = Path().apply {
            moveTo(x + (dirX * size), y)
            lineTo(x + (dirX * 6f), y)
            lineTo(x + (dirX * 6f), y + (dirY * 6f))
            lineTo(x, y + (dirY * 6f))
            lineTo(x, y + (dirY * size))
        }
        canvas.drawPath(path, paint)

        // Tiny diamond in corner
        val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = color
            style = Paint.Style.FILL
        }
        canvas.drawCircle(x + (dirX * 10f), y + (dirY * 10f), 2f, dotPaint)
    }
}
