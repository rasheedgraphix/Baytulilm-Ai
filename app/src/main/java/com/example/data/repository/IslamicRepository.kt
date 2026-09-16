package com.example.data.repository

import com.example.data.model.AsmaUlHusna
import com.example.data.model.AsmaUnNabi

object IslamicRepository {

    fun getAsmaUlHusna(): List<AsmaUlHusna> {
        return listOf(
            AsmaUlHusna(1, "اللہ", "اللہ", "سب سے بڑا نام"),
            AsmaUlHusna(2, "الرحمن", "بہت مہربان", "سب پر رحم کرنے والا"),
            AsmaUlHusna(3, "الرحیم", "نہایت رحم والا", "مومنوں پر رحم کرنے والا"),
            AsmaUlHusna(4, "الملک", "بادشاہ", "تمام کائنات کا بادشاہ"),
            AsmaUlHusna(5, "القدوس", "سب سے پاک", "ہر عیب سے پاک"),
            AsmaUlHusna(6, "السلام", "سلامتی دینے والا", "امن و سکون دینے والا"),
            AsmaUlHusna(7, "المؤمن", "امن دینے والا", "ایمان و امن بخشنے والا"),
            AsmaUlHusna(8, "المھیمن", "نگہبان", "سب کا محافظ"),
            AsmaUlHusna(9, "العزیز", "غلبہ والا", "سب سے زیادہ عزت والا"),
            AsmaUlHusna(10, "الجبار", "زبردست", "اپنی مرضی چلانے والا"),
            // ... (adding all 99)
        )
    }

    fun getAsmaUnNabi(): List<AsmaUnNabi> {
        return listOf(
            AsmaUnNabi(1, "محمد", "تعریف کیا گیا", "جن کی کثرت سے تعریف کی گئی ہو", "قرآن: 48:29"),
            AsmaUnNabi(2, "احمد", "سب سے زیادہ تعریف کرنے والا", "جو اللہ کی سب سے زیادہ تعریف کرنے والا ہو", "قرآن: 61:6"),
            // ... (fill more later)
        )
    }
}
