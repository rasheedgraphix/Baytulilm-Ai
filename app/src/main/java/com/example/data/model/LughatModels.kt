package com.example.data.model

data class LughatItem(
    val id: String,
    val title: String,
    val titleUrdu: String,
    val languageTypeUrdu: String,
    val compilerUrdu: String,
    val descriptionUrdu: String,
    val pdfUrl: String,
    val totalPages: Int = 850
)
