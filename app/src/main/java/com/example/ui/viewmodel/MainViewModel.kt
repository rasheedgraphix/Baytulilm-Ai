package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
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
import com.example.util.PrayerTimeCalculator
import com.example.util.PrayerTimeData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }

        recalculatePrayerTimes()

        // Sync Firestore real-time books with local Room DB
        viewModelScope.launch {
            firebaseRepository.booksDatabase.collectLatest { firestoreBooks ->
                if (firestoreBooks.isNotEmpty()) {
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

    fun selectCity(city: CityLocation) {
        _selectedCity.value = city
        _locationLabel.value = "${city.nameUrdu} (${city.nameEnglish})"
        saveCityToPrefs(city)
        recalculatePrayerTimes()
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

    fun downloadBook(book: BookEntity) {
        viewModelScope.launch {
            repository.setDownloadStatus(book.id, false, 0.3f)
            kotlinx.coroutines.delay(800)
            repository.setDownloadStatus(book.id, false, 0.7f)
            kotlinx.coroutines.delay(800)
            repository.setDownloadStatus(book.id, true, 1.0f)
        }
    }

    fun deleteDownload(bookId: String) {
        viewModelScope.launch {
            repository.setDownloadStatus(bookId, false, 0f)
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
