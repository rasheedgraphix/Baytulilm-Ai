package com.example.data.model

data class Fatawa(
    val id: String,
    val title: String,
    val titleUrdu: String,
    val author: String,
    val authorUrdu: String,
    val institution: String = "",
    val institutionUrdu: String = "",
    val language: String,
    val description: String,
    val rightsStatus: RightsStatus = RightsStatus.RIGHTS_UNVERIFIED,
    val volumes: List<FatawaVolume>
)

data class FatawaVolume(
    val id: String,
    val fatawaId: String,
    val volumeNumber: Int,
    val title: String,
    val titleUrdu: String,
    val pdfUrl: String,
    val downloadStatus: DownloadStatus = DownloadStatus.NotDownloaded,
    val downloadProgress: Float = 0f,
    val downloadedBytes: Long = 0,
    val totalBytes: Long = 0
)
