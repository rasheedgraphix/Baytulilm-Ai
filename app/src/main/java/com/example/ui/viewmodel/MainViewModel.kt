package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.BookDoc
import com.example.data.model.BookEntity
import com.example.data.model.BookmarkDoc
import com.example.data.model.BookmarkEntity
import com.example.data.model.ReadingHistoryDoc
import com.example.data.model.RecentReadingEntity
import com.example.data.model.TasbeehRecordEntity
import com.example.data.repository.BookRepository
import com.example.data.repository.VerifiedIslamicContentRepository
import com.example.util.CityLocation
import com.example.util.PdfManager
import com.example.util.PrayerTimeCalculator
import com.example.util.PrayerTimeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

data class BookDownloadProgress(
    val bookId: String,
    val isDownloading: Boolean = false,
    val progress: Float = 0f, // 0.0f to 1.0f
    val bytesRead: Long = 0L,
    val totalBytes: Long = 0L,
    val isCancelled: Boolean = false,
    val isCompleted: Boolean = false,
    val errorMessage: String? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = BookRepository(
        bookDao = db.bookDao(),
        bookmarkDao = db.bookmarkDao(),
        recentReadingDao = db.recentReadingDao(),
        tasbeehDao = db.tasbeehDao()
    )
    val firebaseRepository = com.example.data.repository.FirebaseRepository()
    val adminRepository = com.example.data.repository.AdminRepository()
    val lmsRepository = com.example.data.repository.LmsRepository()
    private val pdfManager = PdfManager(application)

    val allBooks: StateFlow<List<BookEntity>> = repository.allBooks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favoriteBooks: StateFlow<List<BookEntity>> = repository.favoriteBooks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val bookmarkedBooks: StateFlow<List<BookEntity>> = repository.bookmarkedBooks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val downloadedBooks: StateFlow<List<BookEntity>> = repository.downloadedBooks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allBookmarks: StateFlow<List<BookmarkEntity>> = repository.allBookmarks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val recentReadings: StateFlow<List<RecentReadingEntity>> = repository.recentReadings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val tasbeehRecords: StateFlow<List<TasbeehRecordEntity>> = repository.tasbeehRecords.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _activeDownloads = MutableStateFlow<Map<String, BookDownloadProgress>>(emptyMap())
    val activeDownloads: StateFlow<Map<String, BookDownloadProgress>> = _activeDownloads.asStateFlow()

    private val _coverUpdateTrigger = MutableStateFlow(0L)
    val coverUpdateTrigger: StateFlow<Long> = _coverUpdateTrigger.asStateFlow()

    private val downloadJobs = ConcurrentHashMap<String, Job>()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val searchResults: StateFlow<List<BookEntity>> = _searchQuery.flatMapLatest { query ->
        if (query.isBlank()) {
            repository.allBooks
        } else {
            repository.searchBooks(query)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Current selected Darja filter
    private val _selectedDarja = MutableStateFlow("All")
    val selectedDarja: StateFlow<String> = _selectedDarja.asStateFlow()

    // Current selected Subject filter
    private val _selectedSubject = MutableStateFlow("All")
    val selectedSubject: StateFlow<String> = _selectedSubject.asStateFlow()

    // Active Tasbeeh Count State
    private val _tasbeehCount = MutableStateFlow(0)
    val tasbeehCount: StateFlow<Int> = _tasbeehCount.asStateFlow()

    private val _selectedDhikr = MutableStateFlow("SubhanAllah")
    val selectedDhikr: StateFlow<String> = _selectedDhikr.asStateFlow()

    private val _tasbeehTarget = MutableStateFlow(33)
    val tasbeehTarget: StateFlow<Int> = _tasbeehTarget.asStateFlow()

    // Location and Prayer Times Persistence & State
    private val prefs = application.getSharedPreferences("prayer_city_prefs", Context.MODE_PRIVATE)

    private fun loadSavedCity(): CityLocation {
        val cityNameEng = prefs.getString("city_name_eng", null)
        if (cityNameEng != null) {
            val found = PrayerTimeCalculator.defaultCities.find { it.nameEnglish.equals(cityNameEng, ignoreCase = true) }
            if (found != null) return found
        }
        val lat = prefs.getFloat("city_lat", -999f).toDouble()
        val lng = prefs.getFloat("city_lng", -999f).toDouble()
        if (lat != -999.0 && lng != -999.0) {
            val nameUrdu = prefs.getString("city_name_urdu", "شہر") ?: "شہر"
            val tz = prefs.getString("city_tz", "Asia/Karachi") ?: "Asia/Karachi"
            return CityLocation(nameUrdu, cityNameEng ?: "Selected City", lat, lng, tz)
        }
        return PrayerTimeCalculator.defaultCities.first { it.nameEnglish == "Islamabad" }
    }

    private fun saveCityToPrefs(city: CityLocation) {
        prefs.edit()
            .putString("city_name_eng", city.nameEnglish)
            .putString("city_name_urdu", city.nameUrdu)
            .putFloat("city_lat", city.lat.toFloat())
            .putFloat("city_lng", city.lng.toFloat())
            .putString("city_tz", city.timeZoneId)
            .apply()
    }

    private val _selectedCity = MutableStateFlow<CityLocation>(loadSavedCity())
    val selectedCity: StateFlow<CityLocation> = _selectedCity.asStateFlow()

    private val _locationLabel = MutableStateFlow<String>("${_selectedCity.value.nameUrdu} (${_selectedCity.value.nameEnglish})")
    val locationLabel: StateFlow<String> = _locationLabel.asStateFlow()

    private val _prayerTimes = MutableStateFlow<List<PrayerTimeData>>(emptyList())
    val prayerTimes: StateFlow<List<PrayerTimeData>> = _prayerTimes.asStateFlow()

    val dailyHadith = VerifiedIslamicContentRepository.getDailyHadith()
    val dailyAyah = VerifiedIslamicContentRepository.getDailyAyah()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.seedInitialDataIfEmpty()
        }

        recalculatePrayerTimes()

        // Periodic check for prayer time entry when app is running
        viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                if (_prayerTimes.value.isNotEmpty()) {
                    com.example.util.PrayerAudioNotifier.checkAndTriggerPrayerTime(
                        getApplication(),
                        _prayerTimes.value
                    )
                }
                kotlinx.coroutines.delay(20_000L)
            }
        }

        // Sync Firestore real-time books with local Room DB
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.booksDatabase.collectLatest { firestoreBooks ->
                if (firestoreBooks.isNotEmpty()) {
                    val currentCount = repository.getBooksCount()
                    if (currentCount == 0 || firestoreBooks.size > currentCount) {
                        val entities = firestoreBooks.map { doc ->
                            BookEntity(
                                id = doc.id,
                                title = doc.title,
                                author = doc.author,
                                subject = doc.subject,
                                darja = doc.darja,
                                language = doc.language,
                                type = if (doc.isSharh) "Shurooh" else if (doc.isTranslation) "Translation" else "Main Book",
                                description = doc.description,
                                coverResName = "img_hero_banner",
                                pdfUrl = doc.pdfUrl,
                                coverUrl = doc.coverImage,
                                pageCount = doc.pages,
                                rating = doc.rating
                            )
                        }
                        repository.insertBooks(entities)
                    }
                }
            }
        }
    }

    fun selectCity(city: CityLocation) {
        _selectedCity.value = city
        _locationLabel.value = "${city.nameUrdu} (${city.nameEnglish})"
        saveCityToPrefs(city)
        recalculatePrayerTimes()
    }

    fun detectAndSetCurrentLocation(context: Context, onComplete: (Boolean, String) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            val detected = com.example.util.LocationHelper.getCurrentLocation(context)
            if (detected != null) {
                selectCity(detected)
                onComplete(true, "${detected.nameUrdu} (${detected.nameEnglish})")
            } else {
                onComplete(false, "مقام کا تعین نہ ہو سکا")
            }
        }
    }

    fun recalculatePrayerTimes() {
        val city = _selectedCity.value
        val timeZone = java.util.TimeZone.getTimeZone(city.timeZoneId)
        val calculated = PrayerTimeCalculator.calculatePrayerTimes(
            lat = city.lat,
            lng = city.lng,
            date = java.util.Date(),
            overrideTimeZone = timeZone
        )
        _prayerTimes.value = calculated
        com.example.util.PrayerAudioNotifier.schedulePrayerAlarms(getApplication(), calculated)
        try {
            com.example.widget.AppWidgetUpdateHelper.updateAllWidgets(getApplication())
        } catch (e: Exception) {
            // Widget update safety
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedDarja(darja: String) {
        _selectedDarja.value = darja
    }

    fun setSelectedSubject(subject: String) {
        _selectedSubject.value = subject
    }

    fun toggleFavorite(book: BookEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(book.id, book.isFavorite)
        }
    }

    fun toggleBookmark(book: BookEntity) {
        viewModelScope.launch {
            repository.toggleBookmark(book.id, book.isBookmarked)
            val bmDoc = BookmarkDoc(
                id = "bm_${book.id}_${System.currentTimeMillis()}",
                bookId = book.id,
                bookTitle = book.title,
                page = book.lastReadPage
            )
            firebaseRepository.saveBookmarkToFirestore(bmDoc)
        }
    }

    fun saveBookmarkPage(bookId: String, bookTitle: String, page: Int, note: String) {
        viewModelScope.launch {
            repository.savePageBookmark(bookId, bookTitle, page, note)
            val bmDoc = BookmarkDoc(
                id = "bm_${bookId}_$page",
                bookId = bookId,
                bookTitle = bookTitle,
                page = page,
                note = note
            )
            firebaseRepository.saveBookmarkToFirestore(bmDoc)
        }
    }

    fun updateReadingProgress(book: BookEntity, pageNumber: Int) {
        viewModelScope.launch {
            repository.recordReadingProgress(book.id, book.title, book.author, pageNumber, book.pageCount)
            val histDoc = ReadingHistoryDoc(
                id = "rh_${book.id}",
                bookId = book.id,
                bookTitle = book.title,
                currentPage = pageNumber,
                totalPages = book.pageCount,
                lastOpened = System.currentTimeMillis()
            )
            firebaseRepository.saveReadingProgressToFirestore(histDoc)
        }
    }

    suspend fun getThumbnail(book: BookEntity): Bitmap? {
        return pdfManager.getOrExtractBookCover(
            bookId = book.id,
            pdfUrl = book.pdfUrl,
            coverUrl = book.coverUrl,
            book = book
        )
    }

    fun downloadBook(book: BookEntity, onProgress: (Float) -> Unit = {}, onResult: (Boolean) -> Unit = {}) {
        // Cancel existing active job for this book if any
        downloadJobs[book.id]?.cancel()

        _activeDownloads.update { map ->
            map + (book.id to BookDownloadProgress(
                bookId = book.id,
                isDownloading = true,
                progress = 0f,
                isCancelled = false,
                isCompleted = false
            ))
        }

        val job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = pdfManager.downloadOrGetPdf(
                    bookId = book.id,
                    pdfUrl = book.pdfUrl,
                    onProgress = { bytesRead, totalBytes, prog ->
                        val safeProg = if (prog >= 0f) prog else 0f
                        _activeDownloads.update { map ->
                            map + (book.id to BookDownloadProgress(
                                bookId = book.id,
                                isDownloading = true,
                                progress = safeProg,
                                bytesRead = bytesRead,
                                totalBytes = totalBytes,
                                isCancelled = false,
                                isCompleted = false
                            ))
                        }
                        onProgress(safeProg)
                    }
                )
                if (result.isSuccess) {
                    val downloadedFile = result.getOrNull()
                    if (downloadedFile != null && downloadedFile.exists()) {
                        pdfManager.invalidateCoverCache(book.id)
                        pdfManager.extractAndSaveFirstPageCover(book.id, downloadedFile)
                        // Directly copy to device's public Downloads folder (My Files / Downloads)
                        pdfManager.copyPdfToDeviceDownloads(book.id, book.title)
                    }
                    repository.setDownloadStatus(book.id, true, 1.0f)
                    _coverUpdateTrigger.update { System.currentTimeMillis() }
                    _activeDownloads.update { map ->
                        map + (book.id to BookDownloadProgress(
                            bookId = book.id,
                            isDownloading = false,
                            progress = 1.0f,
                            isCancelled = false,
                            isCompleted = true
                        ))
                    }
                    onResult(true)
                } else {
                    repository.setDownloadStatus(book.id, false, 0f)
                    _activeDownloads.update { map ->
                        map + (book.id to BookDownloadProgress(
                            bookId = book.id,
                            isDownloading = false,
                            progress = 0f,
                            isCancelled = false,
                            isCompleted = false,
                            errorMessage = result.exceptionOrNull()?.message ?: "Download failed"
                        ))
                    }
                    onResult(false)
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                repository.setDownloadStatus(book.id, false, 0f)
                _activeDownloads.update { map ->
                    map + (book.id to BookDownloadProgress(
                        bookId = book.id,
                        isDownloading = false,
                        progress = 0f,
                        isCancelled = true,
                        isCompleted = false,
                        errorMessage = "Download cancelled"
                    ))
                }
                onResult(false)
            } catch (e: Exception) {
                repository.setDownloadStatus(book.id, false, 0f)
                _activeDownloads.update { map ->
                    map + (book.id to BookDownloadProgress(
                        bookId = book.id,
                        isDownloading = false,
                        progress = 0f,
                        isCancelled = false,
                        isCompleted = false,
                        errorMessage = e.message ?: "Download failed"
                    ))
                }
                onResult(false)
            } finally {
                downloadJobs.remove(book.id)
            }
        }
        downloadJobs[book.id] = job
    }

    fun cancelDownload(bookId: String) {
        val job = downloadJobs.remove(bookId)
        job?.cancel()
        _activeDownloads.update { map ->
            val current = map[bookId]
            if (current != null) {
                map + (bookId to current.copy(
                    isDownloading = false,
                    isCancelled = true,
                    progress = 0f,
                    errorMessage = "Download cancelled"
                ))
            } else {
                map + (bookId to BookDownloadProgress(
                    bookId = bookId,
                    isDownloading = false,
                    isCancelled = true,
                    progress = 0f,
                    errorMessage = "Download cancelled"
                ))
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.setDownloadStatus(bookId, false, 0f)
            val file = pdfManager.getLocalPdfFile(bookId)
            if (file.exists() && file.length() < 10000) {
                file.delete()
            }
        }
    }

    fun deleteDownload(bookId: String) {
        cancelDownload(bookId)
        viewModelScope.launch(Dispatchers.IO) {
            val file = pdfManager.getLocalPdfFile(bookId)
            if (file.exists()) {
                file.delete()
            }
            val downloadedCover = pdfManager.getDownloadedCoverFile(bookId)
            if (downloadedCover.exists()) {
                downloadedCover.delete()
            }
            pdfManager.invalidateCoverCache(bookId)
            repository.setDownloadStatus(bookId, false, 0f)
            _coverUpdateTrigger.update { System.currentTimeMillis() }
        }
    }

    fun addNewBook(book: BookEntity) {
        viewModelScope.launch {
            repository.insertBook(book)
            val doc = BookDoc(
                id = book.id,
                title = book.title,
                author = book.author,
                subject = book.subject,
                darja = book.darja,
                language = book.language,
                description = book.description,
                pdfUrl = book.pdfUrl,
                coverImage = book.coverUrl,
                pages = book.pageCount,
                rating = book.rating
            )
            firebaseRepository.addNewBook(doc, "Admin")
        }
    }

    fun deleteBook(bookId: String) {
        viewModelScope.launch {
            repository.deleteBook(bookId)
            firebaseRepository.deleteBook(bookId)
        }
    }


    override fun onCleared() {
        super.onCleared()
        firebaseRepository.destroy()
        adminRepository.destroy()
    }
}
