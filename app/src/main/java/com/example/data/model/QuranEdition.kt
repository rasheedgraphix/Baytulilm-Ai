package com.example.data.model

data class QuranEdition(
    val id: String,
    val title: String,
    val titleUrdu: String,
    val scriptTypeUrdu: String,
    val lines: Int,
    val publisherUrdu: String,
    val descriptionUrdu: String,
    val pdfUrl: String,
    val isTajweedi: Boolean = false,
    val totalPages: Int = 611
)
