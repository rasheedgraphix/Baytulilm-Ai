# Baytul Ilm AI - Official Landing Page & APK Download Portal

**Baytul Ilm AI (بَيْتُ الْعِلْمِ AI)** is the official web portal and Android application (`com.baytulilmai.app`) designed for Islamic education, Dars-e-Nizami madrasa students, teachers, and independent scholars.

---

## 📱 Application Overview

- **Package Name:** `com.baytulilmai.app`
- **Current Version:** `1.3.3` (Release `v1.3.3`)
- **Min Android Target:** Android 7.0+ (Nougat, API Level 24+)
- **Target Android SDK:** Android 14 (API Level 34)
- **Primary Audience:** Dars-e-Nizami students (A'la to Dora-e-Hadith), Islamic scholars, Arabic grammar learners.

---

## ✨ Key Features Highlighted

1. **Islamic Library & Index:** Organized directory of classical Islamic works.
2. **Dars-e-Nizami Resources:** Systematic curriculum mapping from Year 1 (A'la) to Year 8 (Dora-e-Hadith).
3. **Quranic Resources:** Word-by-word grammar, morphological analysis, and translations.
4. **Chapter-wise Quizzes:** Topic-focused MCQ pools with instant feedback and explanations.
5. **3 Difficulty Levels:** Beginner (Mubtadi), Intermediate (Mutawassit), and Advanced (Mutaqaddim).
6. **Instant Quiz Results:** Detailed performance breakdowns and score history.
7. **Progress Analytics:** Daily learning streak tracking and subject mastery charts.
8. **AI Learning Assistant:** Interactive static demo showcasing instant explanations on Sarf, Nahw, and Fiqh.
9. **PDF Library Index:** Structured directory for managing personal study documents.
10. **Student Profile:** Badges, history logs, and study milestones.
11. **Smart Bookmarks:** One-tap saving for difficult rules and questions.
12. **Amoled Dark / Light Mode:** Eye-friendly theme for late-night reading.

---

## 🔗 How to Configure Your APK Download URL

To point the **DOWNLOAD APK** buttons to your live Android APK build on GitHub:

1. Open `src/config/appConfig.ts`.
2. Locate the line:
   ```typescript
   export const APK_DOWNLOAD_URL = "REPLACE_WITH_GITHUB_RELEASE_APK_URL";
   ```
3. Replace `"REPLACE_WITH_GITHUB_RELEASE_APK_URL"` with your actual GitHub Release APK download link, for example:
   ```typescript
   export const APK_DOWNLOAD_URL = "https://github.com/your-username/baytul-ilm-ai/releases/download/v1.2.0/BaytulIlmAI-v1.2.0.apk";
   ```

---

## 🚀 How to Deploy to GitHub & Cloudflare Pages

### Option A: Cloudflare Pages / Vercel / Netlify (Static & Full-Stack Deployment)

1. **Build Output:**
   Run `npm run build` in the project root. This creates:
   - Client static SPA build in `dist/`
   - Production server script in `dist/server.cjs`

2. **Deploy to Cloudflare Pages:**
   - Connect your GitHub repository to Cloudflare Pages.
   - Set Build Command: `npm run build`
   - Set Build Output Directory: `dist`

---

## 🛠 Local Development Commands

```bash
# Start local development server (Express + Vite API)
npm run dev

# Build for production
npm run build

# Start production server
npm run start

# Run type checks / linter
npm run lint
```

---

## 🛡 Disclaimer & Copyright Notice

Baytul Ilm AI respects intellectual property rights. This web portal and mobile app do not host, store, or stream copyrighted PDF ebooks directly without authorization. The app acts as an educational reference manager and chapter assessment platform.
