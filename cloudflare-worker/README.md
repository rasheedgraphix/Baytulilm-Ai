# Baytul Ilm AI — Cloudflare Workers Free Gemini Proxy

This folder contains the complete, standalone server-side proxy for **Baytul Ilm AI** running on **Cloudflare Workers Free Tier**.

---

## 🌟 Why Cloudflare Workers Free?
1. **100% Free Forever**: 100,000 requests per day at zero cost.
2. **Zero Payment Required**: No credit card, no Firebase Blaze upgrade, no billing activation.
3. **Ironclad API Key Security**: `GEMINI_API_KEY` is stored strictly as an encrypted Cloudflare Worker secret and never shipped inside the Android APK.
4. **Sub-second Global Latency**: Hosted across Cloudflare's edge network across 300+ cities worldwide.

---

## 🚀 Quick Deployment Guide (5 Easy Steps)

### Step 1: Install Wrangler CLI (if not already installed)
Open your terminal on your computer and run:
```bash
npm install -g wrangler
```

### Step 2: Log in to Cloudflare (Free Account)
```bash
wrangler login
```
This opens your browser to authenticate your free Cloudflare account.

### Step 3: Navigate to Worker Directory
```bash
cd cloudflare-worker
```

### Step 4: Add your Gemini API Key as a Secret
Run the following command in terminal:
```bash
npx wrangler secret put GEMINI_API_KEY
```
When prompted in terminal, paste your Gemini API key from Google AI Studio (`https://aistudio.google.com/app/apikey`).
*(Note: The terminal will not show the characters as you paste for security).*

### Step 5: Deploy the Worker
```bash
npx wrangler deploy
```

Once deployment completes, Wrangler will print your live worker URL, for example:
```text
Published baytul-ilm-ai-proxy (0.32 sec)
  https://baytul-ilm-ai-proxy.<your-subdomain>.workers.dev
```

---

## 📱 Connecting to Android App

Open `app/src/main/java/com/example/util/AppConfig.kt` and set your worker URL:

```kotlin
const val CLOUDFLARE_WORKER_URL = "https://baytul-ilm-ai-proxy.<your-subdomain>.workers.dev"
```

Then compile or run your Android app:
```bash
gradle assembleDebug
```

---

## 🔒 Security & Daily Limit Features
- **Daily Limit**: 5 AI questions per user per day.
- **Client & Server Coordination**: Quota checks run on the proxy edge and in the Android app.
- **Authentication**: Supports Firebase Auth ID Token in `Authorization: Bearer <token>` or user ID.
- **Zero Secrets in APK**: The APK contains no Gemini keys or cloud credentials.
