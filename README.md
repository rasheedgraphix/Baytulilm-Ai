# 🕌 Baytul Ilm AI (بيت العلم)

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)](https://firebase.google.com/)
[![Gemini AI](https://img.shields.io/badge/AI-Google%20Gemini-8E75B2?style=for-the-badge&logo=google&logoColor=white)](https://ai.google.dev/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)

> **Modern Islamic Learning Platform & AI Scholar**  
> An end-to-end, feature-rich Android application designed for traditional Islamic studies (*Dars-e-Nizami*), AI-assisted scholarly guidance, PDF Kutub library, LMS class management, prayer utilities, and interactive assessments.

---

## 📌 Project Overview

**Baytul Ilm AI** bridges modern artificial intelligence with authentic Islamic education. Built natively for Android using Kotlin and Jetpack Compose, the application serves students of Islamic knowledge (*Talib-e-Ilm*), teachers (*Asatizah*), and scholars. It offers a structured curriculum covering classical Dars-e-Nizami years, an interactive AI Scholar powered by Google Gemini, full digital library access, interactive quizzes, live online classes, and daily worship tools.

---

## ❌ Problem Statement

Traditional Islamic education and Dars-e-Nizami studies face several digital accessibility challenges:
1. **Fragmented Resources**: Classical texts (*Kutub*), commentaries (*Shurooh*), and curricula are scattered across unorganized PDF files and physical books.
2. **Lack of Instant Tutoring**: Students studying complex subjects like *Nahw* (Arabic Grammar), *Sarf* (Morphology), *Usul al-Fiqh*, and *Hadith* often lack 24/7 access to guided study assistance.
3. **Outdated Learning Tools**: Existing Islamic apps rarely combine academic Learning Management Systems (LMS), assignment submission, live class scheduling, and real-time prayer tools into a single, cohesive experience.
4. **Security & Role Isolation**: Educational institutions need secure role-based controls for Super Admins, Admins, Teachers, and Students without compromising user privacy.

---

## ✨ Solution

**Baytul Ilm AI** delivers a unified, modern, and beautiful Android mobile application:
- **AI Scholar & AI Teacher**: Instant interactive explanations, verse analysis, grammatical breakdown, and scholarly Q&A powered by Google Gemini AI.
- **Dars-e-Nizami Digital Library**: Year-by-year organized curriculum with built-in PDF reader, bookmarking, and offline access.
- **Complete LMS & Class Suite**: Live class joining (Google Meet, Zoom, YouTube), assignment tracking, course materials, and progress analytics.
- **Quiz & Certification Engine**: Interactive quizzes per subject with instant scoring, result histories, and verifiable course completion certificates.
- **Islamic Utilities**: Accurate prayer schedules, Qibla compass, digital Tasbeeh counter, and Hijri calendar.
- **Role-Based Admin Panel**: Comprehensive admin dashboard to manage users, upload course materials, organize quizzes, and monitor platform analytics.

---

## 🚀 Features List

### 🧠 1. AI Scholar & AI Teacher
- **Interactive Chat**: Ask questions regarding Fiqh, Hadith, Tafseer, and Arabic grammar.
- **Guided Explanations**: Uses custom prompts tailored for Islamic scholarship to ensure accurate, respectful, and structured responses.
- **Contextual Learning**: AI acts as a patient teacher guiding students through complex classical texts.

### 🔐 2. Firebase Authentication & Google Sign-In
- **Email & Password Authentication**: Secure sign-up, sign-in, email verification, and password reset flows.
- **Native Google Sign-In**: Powered by Android's modern `Credential Manager` API (`GetGoogleIdOption`).
- **Profile Management**: Profile photo updates, personal details editing, and account deletion options.

### 📚 3. PDF Library & Dars-e-Nizami Curriculum
- **Classified Curriculum**: Organized by study levels (e.g., Aama 1st/2nd Year, Khasa 1st/2nd Year, Aaliya, Aalima, Master's in Islamic Studies).
- **Embedded PDF Reader**: View classical *Kutub* and notes directly inside the app.
- **Offline Reading & Bookmarks**: Save books locally for offline reading and bookmark critical chapters.

### 📝 4. Quiz & Assessment System
- **Subject-Specific Quizzes**: Test knowledge on Fiqh, Quranic Sciences, Hadith, and Arabic Grammar.
- **Instant Grading & Review**: View score breakdowns, detailed answer explanations, and retake quizzes.
- **Certificates**: Automatically generate and view completion certificates upon passing milestone tests.

### 🕋 5. Daily Worship & Prayer Tools
- **Accurate Prayer Times**: Real-time prayer countdown and timings based on location.
- **Qibla Compass**: Digital direction finder for daily Salah.
- **Digital Tasbeeh Counter**: Customizable dhikr counter with vibration feedback and reset capability.
- **Hijri Calendar**: View Islamic dates and major upcoming Islamic events.

### 🎓 6. Learning Management System (LMS)
- **Live Classes**: Join virtual lectures via Google Meet, Zoom, YouTube, or Jitsi Meet.
- **Assignments**: View due assignments, instructions, and submission statuses.
- **Course Dashboard**: Track enrolled subjects, attendance records, and teacher announcements.

### 🛡️ 7. Admin Panel & Role Management
- **Role Hierarchy**: Strict permission rules for Super Admin, Admin, Teacher, and Student.
- **Content Operations**: Upload new books, add notes, publish announcements, and manage quiz banks.
- **User Management**: Modify user roles, view active registrations, and review system logs.

---

## 🛠️ Tech Stack

| Domain | Technology / Library |
| :--- | :--- |
| **Language** | Kotlin 100% |
| **UI Framework** | Jetpack Compose, Material Design 3 (M3) |
| **Architecture** | MVVM (Model-View-ViewModel) + Clean Architecture Principles |
| **Concurrency & State** | Kotlin Coroutines, StateFlow, `collectAsStateWithLifecycle` |
| **AI Integration** | Google Gemini REST API / Firebase AI SDK |
| **Authentication** | Firebase Auth, Android Credential Manager (`androidx.credentials`) |
| **Backend & Cloud** | Firebase Firestore, Firebase Storage |
| **Local Persistence** | Room Database, Encrypted Shared Preferences |
| **Networking & Media** | Retrofit, OkHttp, Coil (Image Loading) |
| **Navigation** | Navigation Compose with Type-Safe Screen Routes |

---

## 📥 Installation Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-org/baytul-ilm-ai.git
   cd baytul-ilm-ai
   ```

2. **Open in Android Studio**
   - Open **Android Studio Ladybug** (or later).
   - Allow Gradle to sync dependencies automatically.

3. **Configure Google Services**
   - Place your `google-services.json` inside the `app/` root directory.
   - Ensure your SHA-1 fingerprint is registered in the Firebase Console for Google Sign-In support.

4. **Build & Run**
   - Connect an Android device (Android 8.0 / API 26+) or launch an Android Virtual Device (AVD).
   - Press **Run** or execute:
     ```bash
     ./gradlew assembleDebug
     ```

---

## 🔑 Environment Variables & Secrets

API Keys and Sensitive Configs are managed via `BuildConfig` and local environment files:

1. Create a `.env` file in the root directory (refer to `.env.example`):
   ```env
   GEMINI_API_KEY=your_gemini_api_key_here
   GOOGLE_WEB_CLIENT_ID=your_google_web_client_id_here
   ```
2. The `secrets-gradle-plugin` injects these keys directly into `BuildConfig` at build time:
   - `BuildConfig.GEMINI_API_KEY`
   - `BuildConfig.GOOGLE_WEB_CLIENT_ID`

---

## 🚀 Deployment Instructions

### Generating Signed Release APK / AAB

1. Create a production keystore if you don't have one:
   ```bash
   keytool -genkey -v -keystore release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias baytulilm
   ```
2. Configure `app/build.gradle.kts` release signing config or environment variables.
3. Build the App Bundle (AAB) for Google Play:
   ```bash
   ./gradlew bundleRelease
   ```
   Or generate the APK directly:
   ```bash
   ./gradlew assembleRelease
   ```

---

## 📱 Screenshots

| Login & Authentication | Home Dashboard | AI Scholar |
| :---: | :---: | :---: |
| *Clean Material 3 Login with Google Sign-In* | *Quick access to Dars-e-Nizami, Prayer & LMS* | *24/7 AI-assisted Islamic Q&A & Tutoring* |

| Dars-e-Nizami Library | Prayer & Qibla Tools | Admin Control Panel |
| :---: | :---: | :---: |
| *Organized PDF Kutub curriculum* | *Accurate Salah times & Qibla direction* | *Role-based management & user permissions* |

---

## 🔮 Future Improvements

- [ ] **Offline Speech-to-Text**: Voice-guided AI Scholar questions in Urdu and Arabic.
- [ ] **Audio Recitation Integration**: Synchronized Quranic recitations with word-by-word translation.
- [ ] **Multi-language Support**: Full translation support for English, Urdu, Arabic, and Pashto.
- [ ] **Push Notification Scheduler**: Reminders for upcoming live classes, assignment deadlines, and daily Azkar.

---

## 👨‍💻 Developer Information

- **Developer**: Hafiz Nouman Ur Rasheed
- **Email**: `hafiznoumanurrasheed4@gmail.com`
- **Platform**: Built with **Google AI Studio** & Jetpack Compose
- **License**: MIT License

---

*“Seeking knowledge is an obligation upon every Muslim.” (Sunan Ibn Majah)*
