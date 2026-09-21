/**
 * Apps Registry Data Structure
 * Supports single-app presentation for Baytul Ilm AI today,
 * with scalable architecture for expanding to multiple applications in the future.
 */

export interface AppInfo {
  id: string;
  name: string;
  slug: string;
  tagline: string;
  description: string;
  packageName: string;
  version: string;
  versionCode: number;
  releaseDate: string;
  fileSize: string;
  minAndroidVersion: string;
  targetAndroidVersion: string;
  downloadUrl: string;
  sha256Checksum: string;
  virusTotalStatus: string;
  isVirusTotalClean: boolean;
  isActive: boolean; // Controls whether published/visible
  features: string[];
  iconText: string;
}

export const APPS_REGISTRY: AppInfo[] = [
  {
    id: 'baytul-ilm-ai',
    name: 'Baytul Ilm AI',
    slug: 'baytul-ilm-ai',
    tagline: 'Official Android Islamic Education & Dars-e-Nizami Study Platform',
    description: 'An all-in-one Android study companion featuring the Dars-e-Nizami curriculum index, Quranic study resources, chapter-wise quizzes, on-device AI tutor, progress tracking, and reference index.',
    packageName: 'com.baytulilmai.app',
    version: '1.4.4',
    versionCode: 16,
    releaseDate: 'Official Release (v1.4.4)',
    fileSize: '53 MB',
    minAndroidVersion: 'Android 7.0+ (Nougat, API Level 24+)',
    targetAndroidVersion: 'Android 14 (API Level 34)',
    downloadUrl: 'https://github.com/rasheedgraphix/Baytulilm-Ai/releases/download/v1.4.4/Baytul.Ilm.AI.2.apk',
    sha256Checksum: 'f2c3d8e192a0487b3e1205c091ad5463728f1109a27e36125439a1c029348e71',
    virusTotalStatus: 'Clean (0/72 engines flagged)',
    isVirusTotalClean: true,
    isActive: true,
    features: [
      'Holy Quran (قرآن مجید)',
      'Masnoon Duas (مسنون دعائیں)',
      '99 Names of Allah (اسماء الحسنیٰ)',
      'Names of Prophet Muhammad ﷺ (اسماء النبی)',
      'Haramain Sharifain Live (حرمین شریفین لائیو)',
      'Qibla Compass (قبلہ کمپاس)',
      'Digital Tasbeeh Counter (ڈیجیٹل تسبیح)',
      'Islamic Library & Dars-e-Nizami Index',
      'Chapter-wise Quizzes & Progress',
      'AI Learning Assistant'
    ],
    iconText: 'ب'
  }
  // Future applications will be added here as active: false or registered dynamically
];

/**
 * Returns the primary active app (Baytul Ilm AI)
 */
export const getPrimaryApp = (): AppInfo => {
  const primary = APPS_REGISTRY.find((app) => app.isActive);
  return primary || APPS_REGISTRY[0];
};

/**
 * Returns all active applications
 */
export const getActiveApps = (): AppInfo[] => {
  return APPS_REGISTRY.filter((app) => app.isActive);
};
