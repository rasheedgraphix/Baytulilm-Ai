package com.example.data.model

data class Tafseer(
    val id: String,
    val title: String,
    val titleUrdu: String,
    val author: String,
    val authorUrdu: String,
    val language: String,
    val description: String,
    val rightsStatus: RightsStatus,
    val volumes: List<TafseerVolume>
)

data class TafseerVolume(
    val id: String,
    val tafseerId: String,
    val volumeNumber: Int,
    val title: String,
    val titleUrdu: String,
    val pdfUrl: String,
    val downloadStatus: DownloadStatus = DownloadStatus.NotDownloaded,
    val downloadProgress: Float = 0f,
    val downloadedBytes: Long = 0,
    val totalBytes: Long = 0
)

enum class RightsStatus {
    PUBLIC_DOMAIN,
    CC_LICENSED,
    PERMISSION_REQUIRED,
    RIGHTS_UNVERIFIED
}

enum class DownloadStatus {
    NotDownloaded,
    Downloading,
    Downloaded
}
