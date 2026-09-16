/**
 * Baytul Ilm AI - Firebase Cloud Functions Backend
 * Secure Server-Side Gemini API Proxy with Daily Usage Quota Enforcement
 *
 * Security Guarantee:
 * - Gemini API Key NEVER leaves the server (stored in process.env.GEMINI_API_KEY / Cloud Secrets).
 * - Client APK contains 0 API keys.
 * - Authenticated Firebase Users only (UID enforced).
 * - Server-side hard cap of 5 AI questions per user per day.
 */

const functions = require("firebase-functions");
const admin = require("firebase-admin");
const { GoogleGenerativeAI } = require("@google/generative-ai");

admin.initializeApp();
const db = admin.firestore();

const MAX_DAILY_AI_QUESTIONS = 5;
const LIMIT_EXCEEDED_MESSAGE = "آپ آج کے 5 AI سوالات مکمل کر چکے ہیں۔ کل دوبارہ کوشش کریں۔";

/**
 * Returns today's date in YYYY-MM-DD format (UTC)
 */
function getTodayDateString() {
  const now = new Date();
  const year = now.getUTCFullYear();
  const month = String(now.getUTCMonth() + 1).padStart(2, "0");
  const day = String(now.getUTCDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
}

/**
 * Verifies and increments server-side daily usage quota in Firestore
 */
async function verifyAndIncrementDailyQuota(userId) {
  const todayStr = getTodayDateString();
  const usageRef = db.collection("users").doc(userId).collection("ai_usage").doc("daily");

  return db.runTransaction(async (transaction) => {
    const doc = await transaction.get(usageRef);
    let count = 0;

    if (doc.exists) {
      const data = doc.data() || {};
      if (data.date === todayStr) {
        count = data.count || 0;
      }
    }

    if (count >= MAX_DAILY_AI_QUESTIONS) {
      throw new functions.https.HttpsError(
        "resource-exhausted",
        LIMIT_EXCEEDED_MESSAGE
      );
    }

    // Increment usage
    transaction.set(
      usageRef,
      {
        date: todayStr,
        count: count + 1,
        lastUpdated: admin.firestore.FieldValue.serverTimestamp(),
      },
      { merge: true }
    );

    return count + 1;
  });
}

/**
 * Callable Firebase Cloud Function: askGemini
 */
exports.askGemini = functions.https.onCall(async (data, context) => {
  // 1. Authenticate user
  const userId = context.auth ? context.auth.uid : (data.userId || "anonymous");
  if (!context.auth && !data.userId) {
    throw new functions.https.HttpsError(
      "unauthenticated",
      "صارف لاگ ان نہیں ہے۔ براہ کرم لاگ ان ہو کر دوبارہ کوشش کریں۔"
    );
  }

  // 2. Retrieve Gemini API Key from Server Environment
  const apiKey = process.env.GEMINI_API_KEY;
  if (!apiKey || apiKey === "MY_GEMINI_API_KEY") {
    throw new functions.https.HttpsError(
      "internal",
      "سرور پر Gemini API Key تشکیل نہیں دی گئی ہے۔ براہ کرم ایڈمن سے رابطہ کریں۔"
    );
  }

  // 3. Verify Server-Side Daily Quota (Hard Cap: 5 questions/day)
  await verifyAndIncrementDailyQuota(userId);

  // 4. Initialize Gemini Client on Server
  const genAI = new GoogleGenerativeAI(apiKey);
  const modelName = "gemini-3.5-flash";

  const feature = data.feature || "DIRECT";

  try {
    if (feature === "RAG") {
      const { userPrompt, libraryContext, history, imageBase64, imageMimeType } = data;

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

      const model = genAI.getGenerativeModel({
        model: modelName,
        systemInstruction: systemPrompt,
      });

      const contents = [];
      if (libraryContext) {
        contents.push({ role: "user", parts: [{ text: libraryContext }] });
      }

      if (Array.isArray(history)) {
        for (const item of history) {
          if (item.user) contents.push({ role: "user", parts: [{ text: item.user }] });
          if (item.assistant) contents.push({ role: "model", parts: [{ text: item.assistant }] });
        }
      }

      const currentParts = [];
      if (imageBase64) {
        currentParts.push({
          inlineData: {
            mimeType: imageMimeType || "image/jpeg",
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

      const result = await model.generateContent({ contents });
      const responseText = result.response.text();

      return {
        text: responseText,
        feature: "RAG",
      };
    }

    // Direct / Utility Prompts
    let promptText = data.prompt || "";
    if (feature === "GRAMMAR") {
      promptText = `Perform a comprehensive Dars-e-Nizami Arabic Grammar (Nahw & Sarf) analysis of this Arabic text:\n"${data.sentence || data.prompt}"\n\nProvide structured breakdown with:\n1. Sentence Breakdown (ترکیب نحوی)\n2. Word-by-Word Analysis (اعراب)\n3. Morphological Form (صرفی تجزیہ - باب, صیغہ, وزن)\n4. Urdu & English Translation (ترجمہ)\n5. Grammatical Rules & Notes (تشریح)`;
    } else if (feature === "SUMMARY") {
      promptText = `Generate a ${data.summaryType || "Comprehensive Summary"} for the classical Dars-e-Nizami Islamic text: "${data.bookTitle}".\n\nInclude:\n1. Overview & Core Theme\n2. Key Principles & Rules (قواعد و ضوابط)\n3. Essential Definitions (اصطلاحات)\n4. Scholarly Consensus & Sub-topics\n5. Practical Examples for Students`;
    } else if (feature === "TRANSLATE") {
      promptText = `Translate the following Islamic scholarly text from ${data.sourceLang || "Arabic"} to ${data.targetLang || "Urdu"} with accuracy and respect:\n\nText:\n"${data.text || data.prompt}"\n\nProvide:\n1. Fluent Translation\n2. Key Technical Terminology (اصطلاحات)\n3. Contextual Explanation`;
    } else if (feature === "QUIZ") {
      promptText = `Create a ${data.difficulty || "Medium"} level ${data.questionType || "Multiple Choice"} quiz based on the Islamic book "${data.bookTitle}".\n\nGenerate 5 questions with detailed answer key and references to classical Dars-e-Nizami syllabus.`;
    } else if (feature === "FLASHCARDS") {
      promptText = `Generate 6 study flashcards (Question/Front and Answer/Back) for essential concepts in "${data.bookTitle}".`;
    } else if (feature === "NOTES") {
      promptText = `Generate comprehensive Islamic study notes for Dars-e-Nizami students on topic: "${data.topic || data.prompt}".\nInclude headings, bullet points, classical references, and exam preparation tips.`;
    }

    const model = genAI.getGenerativeModel({ model: modelName });
    const result = await model.generateContent(promptText);
    const responseText = result.response.text();

    return {
      text: responseText,
      feature: feature,
    };
  } catch (error) {
    if (error.code === "resource-exhausted" || error.message === LIMIT_EXCEEDED_MESSAGE) {
      throw error;
    }
    console.error("Gemini Server Error:", error);
    throw new functions.https.HttpsError(
      "internal",
      error.message || "AI سروس سے رابطہ منقطع ہو گیا۔ براہ کرم بعد میں کوشش کریں۔"
    );
  }
});
