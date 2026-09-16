package com.example.data.model

data class AsmaUlHusna(
    val id: Int,
    val arabicName: String,
    val transliteration: String = "",
    val urduMeaning: String,
    val description: String = ""
)

data class AsmaUnNabi(
    val id: Int,
    val arabicName: String,
    val transliteration: String = "",
    val urduMeaning: String,
    val description: String = "",
    val reference: String? = null
)

data class IslamicEvent(
    val id: Int,
    val gregorianDate: String,
    val hijriDate: String,
    val eventName: String,
    val description: String
)

data class LiveStream(
    val id: Int,
    val title: String,
    val url: String,
    val icon: String,
    val streamUrls: List<String> = emptyList(),
    val youtubeUrl: String = "",
    val location: String = "Saudi Arabia",
    val description: String = ""
)

data class FamousDua(
    val id: Int,
    val title: String,
    val category: String,
    val arabic: String,
    val transliteration: String,
    val urdu: String,
    val reference: String
)
