export interface FeatureItem {
  id: string;
  title: string;
  titleUrdu?: string;
  titlePashto?: string;
  description: string;
  descriptionUrdu?: string;
  descriptionPashto?: string;
  iconName: string;
  category: 'core' | 'academic' | 'ai' | 'study';
  highlights: string[];
  highlightsUrdu?: string[];
  highlightsPashto?: string[];
}

export interface DarsNizamiLevel {
  id: string;
  yearName: string;
  arabicName: string;
  englishTitle: string;
  description: string;
  subjects: {
    name: string;
    arabicName: string;
    coreBooks: string[];
    description: string;
  }[];
}

export interface DarsNizamiBookItem {
  id: string;
  name: string;
  nameUrdu?: string;
  classLevel: '1st' | '2nd' | '3rd' | '4th' | '5th' | '6th' | '7th' | '8th' | 'quran' | 'tafaseer' | 'lughat' | 'fatawa' | 'tareekh' | 'tajweed-lil-huffaz' | 'tajweed-lil-ulama';
  classNameUrdu: string;
  classNameEnglish: string;
  category: string;
  type: string;
  typeUrdu?: string;
  pdfUrl: string;
  edition?: string;
  author?: string;
  coverUrl?: string;
}

export interface QuizPreviewQuestion {
  id: string;
  subject: string;
  level: string;
  difficulty: 'Beginner' | 'Intermediate' | 'Advanced';
  question: string;
  options: string[];
  correctAnswer: number;
  explanation: string;
}

export interface AppScreenshot {
  id: string;
  title: string;
  titleUrdu?: string;
  titlePashto?: string;
  caption: string;
  captionUrdu?: string;
  captionPashto?: string;
  category: 'Home' | 'Dars-e-Nizami' | 'Quiz' | 'AI Tutor' | 'AI Scholar' | 'Library' | 'Offline Mode' | 'Profile';
  imageUrl: string;
}

export interface FaqItem {
  id: string;
  question: string;
  questionUrdu?: string;
  questionPashto?: string;
  answer: string;
  answerUrdu?: string;
  answerPashto?: string;
  category: 'general' | 'installation' | 'content' | 'ai';
}

export interface AiChatMessage {
  id: string;
  sender: 'user' | 'ai';
  text: string;
  timestamp: string;
  sources?: string[];
}
