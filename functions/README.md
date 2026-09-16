# Baytul Ilm AI - Server-Side Cloud Functions

## Security & Architecture Overview
- **Zero Client Key Leakage:** The Gemini API Key is NEVER packaged in the Android APK.
- **Server-Side Quota Enforcement:** 5 AI questions per user per day are enforced and transacted on the server in Firestore.
- **Authentication:** All requests verify the Firebase Authentication ID token.

## Deployment Steps
1. Install Firebase CLI (if not already installed):
   ```bash
   npm install -g firebase-tools
   ```
2. Log in and select project:
   ```bash
   firebase login
   firebase use baytul-ilm-ai
   ```
3. Set your Gemini API Key in Firebase Secret Manager:
   ```bash
   firebase functions:secrets:set GEMINI_API_KEY
   ```
4. Deploy the Cloud Function:
   ```bash
   firebase deploy --only functions
   ```
