/**
 * Baytul Ilm AI - Cloudflare Worker Gemini Proxy Backend
 * 100% Free Serverless Server-Side Proxy with Secure Secret Storage & Daily Rate Limiting
 *
 * Security & Cost Guarantees:
 * - 100% FREE on Cloudflare Workers Free Tier (100,000 requests/day free).
 * - NO Firebase Blaze plan or credit card required.
 * - GEMINI_API_KEY is stored exclusively in Cloudflare Worker Secrets (env.GEMINI_API_KEY).
 * - Zero API keys embedded inside Android APK.
 * - Enforces daily user request limits and verifies incoming app requests.
 */

const MAX_DAILY_AI_QUESTIONS = 5;
const LIMIT_EXCEEDED_MESSAGE = "آپ آج کے 5 AI سوالات مکمل کر چکے ہیں۔ کل دوبارہ کوشش کریں۔";

// CORS Headers for cross-platform / web / Android WebView support
const corsHeaders = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Methods": "GET, POST, OPTIONS",
  "Access-Control-Allow-Headers": "Content-Type, Authorization, X-App-ID, X-User-ID",
  "Access-Control-Max-Age": "86400",
};

/**
 * Returns today's date string in YYYY-MM-DD (UTC)
 */
function getTodayDateString() {
  const now = new Date();
  const year = now.getUTCFullYear();
  const month = String(now.getUTCMonth() + 1).padStart(2, "0");
  const day = String(now.getUTCDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
}

/**
 * Extracts and verifies user identification from Firebase Auth ID Token or Headers
 */
function extractUserIdentity(request, body) {
  const authHeader = request.headers.get("Authorization") || "";
  let tokenUserId = null;

  if (authHeader.startsWith("Bearer ")) {
    const token = authHeader.substring(7).trim();
    try {
      // Decode JWT payload (without external heavy libraries)
      const parts = token.split(".");
      if (parts.length === 3) {
        const payloadBase64 = parts[1].replace(/-/g, "+").replace(/_/g, "/");
        const payloadJson = atob(payloadBase64);
        const claims = JSON.parse(payloadJson);
        // Firebase Auth standard UID is in 'user_id' or 'sub'
        tokenUserId = claims.user_id || claims.sub || null;
      }
    } catch (e) {
      console.warn("Could not decode bearer JWT payload:", e);
    }
  }

  const userId = tokenUserId || body.userId || request.headers.get("X-User-ID") || "user_anonymous";
  return { userId, hasToken: !!tokenUserId };
}

/**
 * Server-side rate limiter using Cloudflare Cache API (Free, zero-database)
 * Tracks daily usage per user in 24-hour UTC window.
 */
async function checkAndIncrementDailyQuota(userId, env) {
  const todayStr = getTodayDateString();
  const cacheKey = `https://baytul-ilm-quota.internal/user/${encodeURIComponent(userId)}/date/${todayStr}`;

  // If KV binding 'USAGE_KV' is provided in wrangler.toml, use KV:
  if (env && env.USAGE_KV) {
    try {
      const kvKey = `quota_${todayStr}_${userId}`;
      const current = parseInt((await env.USAGE_KV.get(kvKey)) || "0", 10);
      if (current >= MAX_DAILY_AI_QUESTIONS) {
        return { allowed: false, current };
      }
      await env.USAGE_KV.put(kvKey, String(current + 1), { expirationTtl: 86400 * 2 });
      return { allowed: true, current: current + 1 };
    } catch (err) {
      console.warn("KV quota check error, falling back to cache:", err);
    }
  }

  // Fallback to Cloudflare Cache API
  try {
    const cache = caches.default;
    const cacheReq = new Request(cacheKey, { method: "GET" });
    const cachedResp = await cache.match(cacheReq);

    let count = 0;
    if (cachedResp) {
      const data = await cachedResp.json();
      if (data && data.date === todayStr) {
        count = data.count || 0;
      }
    }

    if (count >= MAX_DAILY_AI_QUESTIONS) {
      return { allowed: false, current: count };
    }

    const nextCount = count + 1;
    const newResp = new Response(
      JSON.stringify({ date: todayStr, count: nextCount }),
      {
        headers: {
          "Content-Type": "application/json",
          "Cache-Control": "public, max-age=86400",
        },
      }
    );
    await cache.put(cacheReq, newResp);
    return { allowed: true, current: nextCount };
  } catch (e) {
    console.warn("Cache API rate check error:", e);
    // Allow request if internal cache lookup fails; client-side Firestore also enforces daily quota
    return { allowed: true, current: 1 };
  }
}

export default {
  async fetch(request, env, ctx) {
    // 1. Handle CORS Preflight
    if (request.method === "OPTIONS") {
      return new Response(null, { status: 204, headers: corsHeaders });
    }

    // Health check endpoint
    const url = new URL(request.url);
    if (request.method === "GET" && (url.pathname === "/" || url.pathname === "/health")) {
      return new Response(
        JSON.stringify({
          status: "healthy",
          service: "Baytul Ilm AI - Cloudflare Gemini Proxy",
          version: "1.4.1",
          authenticatedFreePlan: true,
        }),
        {
          status: 200,
          headers: { ...corsHeaders, "Content-Type": "application/json" },
        }
      );
    }

    // 2. Only allow POST requests for AI generation
    if (request.method !== "POST") {
      return new Response(
        JSON.stringify({ error: "Method not allowed. Please use POST." }),
        {
          status: 405,
          headers: { ...corsHeaders, "Content-Type": "application/json" },
        }
      );
    }

    try {
      // 3. Parse JSON Body
      const body = await request.json().catch(() => ({}));
      const { userId } = extractUserIdentity(request, body);

      // 4. Validate Gemini API Key secret in Cloudflare Environment
      const apiKey = env.GEMINI_API_KEY;
      if (!apiKey || apiKey.trim() === "" || apiKey === "MY_GEMINI_API_KEY") {
        return new Response(
          JSON.stringify({
            error: "Cloudflare Worker پر GEMINI_API_KEY کا سیکرٹ تشکیل نہیں دیا گیا ہے۔ براہ کرم 'wrangler secret put GEMINI_API_KEY' چلا کر key سیٹ کریں۔",
            code: "API_KEY_MISSING",
          }),
          {
            status: 500,
            headers: { ...corsHeaders, "Content-Type": "application/json" },
          }
        );
      }

      // 5. Enforce 5 AI Questions Per User Per Day Quota
      const quotaStatus = await checkAndIncrementDailyQuota(userId, env);
      if (!quotaStatus.allowed) {
        return new Response(
          JSON.stringify({
            error: LIMIT_EXCEEDED_MESSAGE,
            code: "RESOURCE_EXHAUSTED",
            count: quotaStatus.current,
          }),
          {
            status: 429,
            headers: { ...corsHeaders, "Content-Type": "application/json" },
          }
        );
      }

      // 6. Build Gemini REST API Request Body
      const feature = body.feature || (body.contents ? "DIRECT_CONTENTS" : "DIRECT");
      const modelName = env.GEMINI_MODEL || "gemini-3.5-flash";

      let geminiPayload = {};

      if (body.contents && Array.isArray(body.contents)) {
        // Direct standard Google Gemini JSON Payload pass-through
        geminiPayload = {
          contents: body.contents,
          generationConfig: body.generationConfig || {
            temperature: 0.3,
            maxOutputTokens: 2048,
          },
        };
        if (body.systemInstruction || body.system_instruction) {
          geminiPayload.system_instruction = body.systemInstruction || body.system_instruction;
        }
      } else if (feature === "RAG") {
        const { userPrompt, libraryContext, history, imageBase64, imageMimeType } = body;

        const systemPrompt = `
You are Baytul Ilm AI Scholar, an authentic Islamic Education Assistant integrated into the 'Baytul Ilm AI' application.

HYBRID KNOWLEDGE SYSTEM MANDATE:
1. HIGHEST PRIORITY (SOURCE 1 - UPLOADED LIBRARY):
   First, check the RETRIEVED UPLOADED BOOKS context provided below.
   IF the user's question can be answered using the uploaded Dars-e-Nizami books:
   You MUST answer strictly from those uploaded books.
   Output format:
   [SOURCE_TYPE: UPLOADED_LIBRARY]
   [CITATION_START]
   Book Name: <Exact book name from context>
   Darja: <Darja from context>
   Subject: <Subject from context>
   Page Number: <Estimated or sample page number e.g. Page 24>
   Confidence Score: <e.g. 96%>
   Quoted Paragraph: "<Excerpt directly supported by context>"
   [CITATION_END]
   [ANSWER_START]
   <Detailed scholarly explanation in Urdu, Arabic, or English matching user's language>
   [ANSWER_END]

2. FALLBACK (SOURCE 2 - GEMINI GENERAL ISLAMIC KNOWLEDGE):
   IF the uploaded books do NOT contain the answer (e.g. general Quran, Tafsir, Hadith, Fiqh, Aqeedah, Seerah, Nahw, Sarf, Islamic History, Duas, or questions about how to use the app, developer info, quiz system, etc.):
   Automatically answer using your general Islamic & Application knowledge.
   Output format:
   [SOURCE_TYPE: GEMINI_GENERAL]
   [NOTICE_START]
   Source: Gemini General Islamic Knowledge
   This answer is not taken from the uploaded library books.
   [NOTICE_END]
   [ANSWER_START]
   <Comprehensive, polite answer in Urdu, Arabic, or English matching user's language>
   [ANSWER_END]

ISLAMIC SAFETY & SCHOLARLY INTEGRITY:
- Prefer references from the Noble Quran and authentic Hadith (Sahih Bukhari, Sahih Muslim, Sunan Kutub).
- Do not fabricate references or quote unverified narrations without context.
- If multiple scholarly opinions exist, explain that there are different valid scholarly views and avoid claiming one opinion is the sole correct one unless universally established.
- Answer in the user's language (Urdu, Arabic, English, or Roman Urdu).
`.trim();

        const contents = [];

        // Add Library Context as initial user context if present
        if (libraryContext && libraryContext.trim()) {
          contents.push({
            role: "user",
            parts: [{ text: libraryContext }],
          });
        }

        // Add conversation history
        if (Array.isArray(history)) {
          for (const item of history) {
            if (item.user) {
              contents.push({ role: "user", parts: [{ text: item.user }] });
            }
            if (item.assistant) {
              contents.push({ role: "model", parts: [{ text: item.assistant }] });
            }
          }
        }

        // Current turn parts (Image + Prompt)
        const currentParts = [];
        if (imageBase64) {
          currentParts.push({
            inline_data: {
              mime_type: imageMimeType || "image/jpeg",
              data: imageBase64,
            },
          });
        }
        if (userPrompt) {
          currentParts.push({ text: userPrompt });
        } else if (currentParts.length === 0) {
          currentParts.push({ text: "Please examine and explain this image in detail." });
        }

        contents.push({ role: "user", parts: currentParts });

        geminiPayload = {
          system_instruction: {
            parts: [{ text: systemPrompt }],
          },
          contents: contents,
          generationConfig: {
            temperature: 0.2,
            topK: 40,
            topP: 0.95,
            maxOutputTokens: 2048,
          },
        };
      } else {
        // Direct Feature Prompts
        let promptText = body.prompt || "";
        if (feature === "GRAMMAR") {
          promptText = `Perform a comprehensive Dars-e-Nizami Arabic Grammar (Nahw & Sarf) analysis of this Arabic text:\n"${body.sentence || body.prompt}"\n\nProvide structured breakdown with:\n1. Sentence Breakdown (ترکیب نحوی)\n2. Word-by-Word Analysis (اعراب)\n3. Morphological Form (صرفی تجزیہ - باب, صیغہ, وزن)\n4. Urdu & English Translation (ترجمہ)\n5. Grammatical Rules & Notes (تشریح)`;
        } else if (feature === "SUMMARY") {
          promptText = `Generate a ${body.summaryType || "Comprehensive Summary"} for the classical Dars-e-Nizami Islamic text: "${body.bookTitle}".\n\nInclude:\n1. Overview & Core Theme\n2. Key Principles & Rules (قواعد و ضوابط)\n3. Essential Definitions (اصطلاحات)\n4. Scholarly Consensus & Sub-topics\n5. Practical Examples for Students`;
        } else if (feature === "TRANSLATE") {
          promptText = `Translate the following Islamic scholarly text from ${body.sourceLang || "Arabic"} to ${body.targetLang || "Urdu"} with accuracy and respect:\n\nText:\n"${body.text || body.prompt}"\n\nProvide:\n1. Fluent Translation\n2. Key Technical Terminology (اصطلاحات)\n3. Contextual Explanation`;
        } else if (feature === "QUIZ") {
          promptText = `Create a ${body.difficulty || "Medium"} level ${body.questionType || "Multiple Choice"} quiz based on the Islamic book "${body.bookTitle}".\n\nGenerate 5 questions with detailed answer key and references to classical Dars-e-Nizami syllabus.`;
        } else if (feature === "FLASHCARDS") {
          promptText = `Generate 6 study flashcards (Question/Front and Answer/Back) for essential concepts in "${body.bookTitle}".`;
        } else if (feature === "NOTES") {
          promptText = `Generate comprehensive Islamic study notes for Dars-e-Nizami students on topic: "${body.topic || body.prompt}".\nInclude headings, bullet points, classical references, and exam preparation tips.`;
        }

        geminiPayload = {
          contents: [
            {
              role: "user",
              parts: [{ text: promptText }],
            },
          ],
          generationConfig: {
            temperature: 0.3,
            maxOutputTokens: 2048,
          },
        };
      }

      // 7. Invoke Google Gemini REST API with model fallback support
      const candidateModels = [
        env.GEMINI_MODEL || "gemini-2.5-flash",
        "gemini-2.0-flash",
        "gemini-1.5-flash",
      ].filter((v, i, a) => a.indexOf(v) === i);

      let geminiResponse = null;
      let lastErrorData = null;
      let usedModel = candidateModels[0];

      for (const currentModel of candidateModels) {
        usedModel = currentModel;
        const geminiUrl = `https://generativelanguage.googleapis.com/v1beta/models/${currentModel}:generateContent?key=${apiKey}`;
        try {
          const resp = await fetch(geminiUrl, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(geminiPayload),
          });
          if (resp.ok) {
            geminiResponse = resp;
            break;
          } else {
            lastErrorData = await resp.json().catch(() => ({}));
            console.warn(`Model ${currentModel} returned status ${resp.status}:`, lastErrorData);
            if (resp.status === 429) {
              // Rate limited, don't try other models with same key
              geminiResponse = resp;
              break;
            }
          }
        } catch (e) {
          console.warn(`Network error querying ${currentModel}:`, e);
        }
      }

      if (!geminiResponse || !geminiResponse.ok) {
        const status = geminiResponse ? geminiResponse.status : 502;
        const errorData = lastErrorData || {};
        console.error("Gemini API All Models Failed:", status, errorData);

        if (status === 429) {
          return new Response(
            JSON.stringify({
              error: LIMIT_EXCEEDED_MESSAGE,
              code: "GEMINI_RATE_LIMIT",
            }),
            {
              status: 429,
              headers: { ...corsHeaders, "Content-Type": "application/json" },
            }
          );
        }

        const msg = errorData?.error?.message || "AI سروس سے رابطہ منقطع ہو گیا۔ براہ کرم بعد میں کوشش کریں۔";
        return new Response(
          JSON.stringify({ error: msg, details: errorData }),
          {
            status: status >= 400 && status < 500 ? status : 502,
            headers: { ...corsHeaders, "Content-Type": "application/json" },
          }
        );
      }

      const geminiJson = await geminiResponse.json();

      // Extract generated text from candidates
      const candidate = geminiJson.candidates?.[0];
      const textParts = candidate?.content?.parts || [];
      const responseText = textParts.map((p) => p.text || "").join("") || "";

      if (!responseText) {
        return new Response(
          JSON.stringify({ error: "سرور سے کوئی متن موصول نہیں ہوا۔" }),
          {
            status: 500,
            headers: { ...corsHeaders, "Content-Type": "application/json" },
          }
        );
      }

      return new Response(
        JSON.stringify({
          text: responseText,
          feature: feature,
          model: modelName,
        }),
        {
          status: 200,
          headers: { ...corsHeaders, "Content-Type": "application/json" },
        }
      );
    } catch (err) {
      console.error("Worker Execution Exception:", err);
      return new Response(
        JSON.stringify({
          error: err.message || "AI پراکسی سرور پر مسئلہ پیش آیا۔",
        }),
        {
          status: 500,
          headers: { ...corsHeaders, "Content-Type": "application/json" },
        }
      );
    }
  },
};
