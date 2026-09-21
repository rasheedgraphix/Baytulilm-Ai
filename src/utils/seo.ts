// Comprehensive SEO Manager for Baytul Ilm AI
// Supports dynamic meta tags, brand variations, and book-specific schemas for Google Search

export interface SEOProps {
  title?: string;
  description?: string;
  keywords?: string[];
  canonicalUrl?: string;
  ogImage?: string;
  ogType?: 'website' | 'article' | 'book';
  bookData?: {
    name: string;
    author?: string;
    category?: string;
    language?: string;
    description?: string;
    fileUrl?: string;
  };
}

// Universal brand keywords matching all phonetic spellings and Urdu variants
export const BRAND_KEYWORDS = [
  'Baytul Ilm AI',
  'Baitul Ilm AI',
  'Bayt ul Ilm AI',
  'Bait ul Ilm AI',
  'Baytul Ilm',
  'Baitul Ilm',
  'Bayt ul Ilm',
  'Bait ul Ilm',
  'Baitulilm',
  'Baytulilm',
  'بیت العلم AI',
  'بیت العلم',
  'بیت العلوم',
  'بیت العلم موبائل ایپ',
  'بیت العلم آن لائن لائبریری',
  'Islamic Education Platform',
  'Dars-e-Nizami Online',
  'Islamic AI Portal',
  'Dars e Nizami Books PDF',
  'درس نظامی کتب',
  'Haramain Live',
  'Haramain Live Makkah Madinah',
  'Makkah Live Stream 24/7',
  'Madinah Live Stream 24/7',
  'حرمین لائیو',
  'مکہ مکرمہ لائیو',
  'مدینہ منورہ لائیو',
  'Baitul Ilm App Download',
  'Baytul Ilm APK Download',
  'Baitul Ilm Android App',
  'Quran Tafseer Online',
  'Kanzud Daqaiq',
  'Hidayah',
  'Noor ul Anwar',
  'Sharh Jami',
  'Kafiya',
  'Mishkat Sharif',
  'Sahih Bukhari',
  'Fatawa Razawiyya',
  'Fatawa Alamgiri',
  'Al Qamoos ul Waheed',
  'Urdu Islamic Books Library'
];

export function updatePageSEO({
  title,
  description,
  keywords = [],
  canonicalUrl,
  ogImage = 'https://bait-ul-ilm-web.ai.studio/app-logo.png',
  ogType = 'website',
  bookData
}: SEOProps) {
  // 1. Update Title
  const formattedTitle = title
    ? `${title} | Baytul Ilm AI (بیت العلم)`
    : 'Baytul Ilm AI (بیت العلم) – Islamic Education, Dars-e-Nizami & Complete Library';
  document.title = formattedTitle;

  // 2. Helper to set or create meta tag
  const setMeta = (attr: 'name' | 'property', value: string, content: string) => {
    let element = document.querySelector(`meta[${attr}="${value}"]`);
    if (!element) {
      element = document.createElement('meta');
      element.setAttribute(attr, value);
      document.head.appendChild(element);
    }
    element.setAttribute('content', content);
  };

  // 3. Update Description
  const defaultDesc =
    'Baytul Ilm AI (بیت العلم) is a comprehensive Islamic educational platform providing the complete 8-Year Dars-e-Nizami curriculum, Quran Tafseer, Fatawa, Arabic Dictionaries, and AI-powered learning tools.';
  const finalDesc = description || defaultDesc;
  setMeta('name', 'description', finalDesc);
  setMeta('property', 'og:description', finalDesc);
  setMeta('name', 'twitter:description', finalDesc);

  // 4. Update Keywords (combine custom + brand keywords)
  const allKeywords = Array.from(new Set([...keywords, ...BRAND_KEYWORDS])).join(', ');
  setMeta('name', 'keywords', allKeywords);

  // 5. Update OpenGraph & Twitter
  setMeta('property', 'og:title', formattedTitle);
  setMeta('property', 'og:type', ogType);
  setMeta('property', 'og:image', ogImage);
  setMeta('name', 'twitter:title', formattedTitle);
  setMeta('name', 'twitter:image', ogImage);

  if (canonicalUrl) {
    setMeta('property', 'og:url', canonicalUrl);
    let link = document.querySelector('link[rel="canonical"]');
    if (!link) {
      link = document.createElement('link');
      link.setAttribute('rel', 'canonical');
      document.head.appendChild(link);
    }
    link.setAttribute('href', canonicalUrl);
  }

  // 6. Dynamic JSON-LD structured data for books or courses
  const existingDynamicSchema = document.getElementById('dynamic-page-schema');
  if (existingDynamicSchema) {
    existingDynamicSchema.remove();
  }

  if (bookData) {
    const bookSchema = {
      '@context': 'https://schema.org',
      '@type': 'Book',
      'name': bookData.name,
      'author': {
        '@type': 'Person',
        'name': bookData.author || 'Islamic Scholar'
      },
      'genre': bookData.category || 'Islamic Education',
      'inLanguage': bookData.language || 'Urdu / Arabic',
      'description': bookData.description || `${bookData.name} - Free online PDF reading and download on Baytul Ilm AI (بیت العلم).`,
      'publisher': {
        '@type': 'Organization',
        'name': 'Baytul Ilm AI (بیت العلم)',
        'url': 'https://bait-ul-ilm-web.ai.studio/'
      },
      'offers': {
        '@type': 'Offer',
        'price': '0',
        'priceCurrency': 'USD',
        'availability': 'https://schema.org/InStock'
      }
    };

    const script = document.createElement('script');
    script.id = 'dynamic-page-schema';
    script.type = 'application/ld+json';
    script.text = JSON.stringify(bookSchema);
    document.head.appendChild(script);
  }
}
