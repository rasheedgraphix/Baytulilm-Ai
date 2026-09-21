import React, { useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate, useLocation } from 'react-router-dom';
import { Navbar } from './components/layout/Navbar';
import { Footer } from './components/layout/Footer';
import { HomePage } from './pages/HomePage';
import { FeaturesPage } from './pages/FeaturesPage';
import { LibraryPage } from './pages/LibraryPage';
import { ScreenshotsPage } from './pages/ScreenshotsPage';
import { DownloadPage } from './pages/DownloadPage';
import { PrivacyPolicyPage } from './pages/PrivacyPolicyPage';
import { ContactPage } from './pages/ContactPage';
import { HaramainLivePage } from './pages/HaramainLivePage';
import { DarsNizamiClassesPage } from './pages/DarsNizamiClassesPage';
import { DarsNizamiClassDetailPage } from './pages/DarsNizamiClassDetailPage';
import { QuranPage } from './pages/QuranPage';
import { TafaseerPage } from './pages/TafaseerPage';
import { LughatPage } from './pages/LughatPage';
import { FatawaPage } from './pages/FatawaPage';
import { TareekhPage } from './pages/TareekhPage';
import { TajweedLilHuffazPage } from './pages/TajweedLilHuffazPage';
import { TajweedLilUlamaPage } from './pages/TajweedLilUlamaPage';
import { AsmaUlHusnaPage } from './pages/AsmaUlHusnaPage';
import { AsmaUnNabiPage } from './pages/AsmaUnNabiPage';
import { PrayerTimesPage } from './pages/PrayerTimesPage';
import { MasnoonDuasPage } from './pages/MasnoonDuasPage';
import { PdfReaderPage } from './pages/PdfReaderPage';
import { NotFoundPage } from './pages/NotFoundPage';
import { LanguageProvider } from './context/LanguageContext';

// Scroll to top or anchor target on route navigation
const ScrollToTop = () => {
  const { pathname, hash } = useLocation();

  useEffect(() => {
    if (hash) {
      const id = hash.replace('#', '');
      const element = document.getElementById(id);
      if (element) {
        setTimeout(() => {
          element.scrollIntoView({ behavior: 'smooth' });
        }, 150);
        return;
      }
    }
    window.scrollTo(0, 0);
  }, [pathname, hash]);

  return null;
};

export default function App() {
  return (
    <LanguageProvider>
      <BrowserRouter>
        <ScrollToTop />
        <div className="min-h-screen w-full max-w-full overflow-x-hidden flex flex-col bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 font-sans antialiased selection:bg-emerald-500 selection:text-white">
          {/* Responsive Fixed Navigation Header */}
          <Navbar />

          {/* Dynamic Page Router Views */}
          <main className="flex-1 w-full max-w-full overflow-x-hidden">
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/features" element={<FeaturesPage />} />
              <Route path="/library" element={<LibraryPage />} />
              <Route path="/screenshots" element={<ScreenshotsPage />} />
              <Route path="/download" element={<DownloadPage />} />

              {/* Dedicated Department Routes */}
              <Route path="/dars-e-nizami" element={<DarsNizamiClassesPage />} />
              <Route path="/dars-nizami-classes" element={<DarsNizamiClassesPage />} />
              <Route path="/dars-e-nizami/:classLevel" element={<DarsNizamiClassDetailPage />} />
              <Route path="/darse-nizami/:classLevel" element={<DarsNizamiClassDetailPage />} />

              <Route path="/quran" element={<QuranPage />} />
              <Route path="/quran-majeed" element={<QuranPage />} />
              <Route path="/tafaseer" element={<TafaseerPage />} />
              <Route path="/lughat" element={<LughatPage />} />
              <Route path="/fatawa" element={<FatawaPage />} />
              <Route path="/tareekh" element={<TareekhPage />} />
              <Route path="/history" element={<TareekhPage />} />
              <Route path="/tajweed-lil-huffaz" element={<TajweedLilHuffazPage />} />
              <Route path="/tajweed-lil-ulama" element={<TajweedLilUlamaPage />} />
              <Route path="/tajweed-ulama" element={<TajweedLilUlamaPage />} />
              <Route path="/tajweed" element={<TajweedLilHuffazPage />} />
              <Route path="/asma-ul-husna" element={<AsmaUlHusnaPage />} />
              <Route path="/asmaulhusna" element={<AsmaUlHusnaPage />} />
              <Route path="/asma-un-nabi" element={<AsmaUnNabiPage />} />
              <Route path="/asmaunnabi" element={<AsmaUnNabiPage />} />
              <Route path="/prayer-times" element={<PrayerTimesPage />} />
              <Route path="/namaz-times" element={<PrayerTimesPage />} />
              <Route path="/namaz" element={<PrayerTimesPage />} />
              <Route path="/awqat-e-namaz" element={<PrayerTimesPage />} />
              <Route path="/duas" element={<MasnoonDuasPage />} />
              <Route path="/masnoon-duas" element={<MasnoonDuasPage />} />
              <Route path="/dua" element={<MasnoonDuasPage />} />

              {/* Dedicated Online PDF Reader Routes */}
              <Route path="/reader" element={<PdfReaderPage />} />
              <Route path="/read-pdf" element={<PdfReaderPage />} />
              <Route path="/pdf-reader" element={<PdfReaderPage />} />

              {/* 24/7 Haramain Live Routes */}
              <Route path="/haramain-live" element={<HaramainLivePage />} />
              <Route path="/live" element={<HaramainLivePage />} />
              <Route path="/makkah-live" element={<HaramainLivePage />} />
              <Route path="/madinah-live" element={<HaramainLivePage />} />

              <Route path="/privacy-policy" element={<PrivacyPolicyPage />} />
              <Route path="/contact" element={<ContactPage />} />

              {/* In-page Anchor & Tool aliases */}
              <Route path="/islamic-tools" element={<HomePage />} />
              <Route path="/installation-guide" element={<HomePage />} />
              <Route path="/tools" element={<HomePage />} />
              <Route path="/books" element={<Navigate to="/library" replace />} />
              <Route path="/dars-e-nizami-books" element={<Navigate to="/dars-e-nizami" replace />} />
              <Route path="/apk" element={<Navigate to="/download" replace />} />

              {/* Legacy route redirects */}
              <Route path="/about" element={<Navigate to="/" replace />} />
              <Route path="/quiz-system" element={<Navigate to="/features" replace />} />
              <Route path="/ai-assistant" element={<Navigate to="/features" replace />} />

              <Route path="*" element={<NotFoundPage />} />
            </Routes>
          </main>

          {/* Global Footer */}
          <Footer />
        </div>
      </BrowserRouter>
    </LanguageProvider>
  );
}

