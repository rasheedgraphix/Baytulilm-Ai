import React from 'react';
import { SectionHeader } from '../components/common/SectionHeader';
import { APP_CONFIG } from '../config/appConfig';
import { IslamicPatternBg } from '../components/layout/IslamicPatternBg';
import { useLanguage } from '../context/LanguageContext';

export const PrivacyPolicyPage: React.FC = () => {
  const { language, isRtl } = useLanguage();

  const getBadge = () => {
    if (language === 'ps') return 'قانوني او محرمیت معلومات';
    if (language === 'ur') return 'قانونی و رازداری کی پالیسی';
    return 'Legal Transparency';
  };

  const getTitle = () => {
    if (language === 'ps') return 'د رازدارۍ او محرمیت تګلاره';
    if (language === 'ur') return 'پرائیویسی پالیسی و رازداری';
    return 'Privacy Policy';
  };

  const getSubtitle = () => {
    if (language === 'ps') return `د بیت العلم AI انډرایډ اپلیکیشن رسمي قانوني او معلوماتي تګلاره (${APP_CONFIG.packageName}).`;
    if (language === 'ur') return `بیت العلم AI اینڈرائیڈ ایپلیکیشن کی باضابطہ پرائیویسی پالیسی (${APP_CONFIG.packageName})۔`;
    return `Official Privacy Policy for the Baytul Ilm AI Android Application (${APP_CONFIG.packageName}).`;
  };

  return (
    <div className="pt-28 pb-20 space-y-12">
      {/* Header Banner */}
      <section className="relative bg-emerald-950 text-white py-16 overflow-hidden">
        <IslamicPatternBg variant="hero" />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10 space-y-4 text-center">
          <SectionHeader
            badge={getBadge()}
            title={getTitle()}
            arabicTitle="سِيَاسَةُ الْخُصُوصِيَّة"
            subtitle={getSubtitle()}
          />
        </div>
      </section>

      <section className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8" dir={isRtl ? 'rtl' : 'ltr'}>
        <div className="bg-white dark:bg-slate-900 rounded-3xl p-8 border border-slate-200/80 dark:border-slate-800 shadow-sm space-y-8 text-sm text-slate-700 dark:text-slate-300 leading-relaxed">
          <div className="space-y-2 pb-6 border-b border-slate-200 dark:border-slate-800">
            <span className="text-xs font-mono text-slate-500 block">
              {language === 'ps' ? 'د تطبیق نېټه: اګست ۲۰۲۶' : language === 'ur' ? 'لاگو ہونے کی تاریخ: اگست 2026' : 'Effective Date: August 2026'}
            </span>
            <h2 className="text-xl font-bold text-slate-900 dark:text-slate-100">
              {language === 'ps' ? '۱. عمومي پېژندنه او ژمنتیا' : language === 'ur' ? '1. تعارف اور عزم' : '1. Overview & Commitment'}
            </h2>
            <p>
              {language === 'ps'
                ? `بیت العلم AI ستاسو د شخصي حریم او معلوماتو پوره درناوی کوي. دا پالیسي روښانه کوي چې زموږ انډرایډ علمي اپلیکیشن (${APP_CONFIG.packageName}) څنګه کار کوي او د معلوماتو خونديتوب څنګه تضمینوي.`
                : language === 'ur'
                ? `بیت العلم AI اپنے صارفین کی پرائیویسی کا مکمل احترام کرتا ہے۔ یہ پرائیویسی پالیسی وضاحت کرتی ہے کہ ہماری تعلیمی و دینی اینڈرائیڈ ایپ (${APP_CONFIG.packageName}) صارفین کے ڈیٹا کے تحفظ کو کس طرح یقینی بناتی ہے۔`
                : `${APP_CONFIG.appName} ("we", "our", "us") respects the privacy of our users ("user", "you"). This Privacy Policy explains how our Android educational application (${APP_CONFIG.packageName}) and official website collect, use, and safeguard your information when you use our services.`}
            </p>
          </div>

          <div className="space-y-2">
            <h2 className="text-lg font-bold text-slate-900 dark:text-slate-100">
              {language === 'ps' ? '۲. آفلاین کارېدنه او د معلوماتو خونديتوب' : language === 'ur' ? '2. آف لائن استعمال اور ڈیٹا کا تحفظ' : '2. Data Collection & Offline Privacy'}
            </h2>
            <p>
              <strong>{language === 'ps' ? 'په موبایل کې محلي زېرمه:' : language === 'ur' ? 'ڈیوائس میں لوکل اسٹوریج:' : 'Local On-Device Storage:'}</strong>{' '}
              {language === 'ps'
                ? 'دا اپلیکیشن په بشپړ ډول آفلاین کار کوي. ستاسو بک مارکونه، نوټونه او د درسي کتب لوستلو پرمختګ یوازې ستاسو په خپل موبایل کې خوندي کېږي.'
                : language === 'ur'
                ? 'بیت العلم AI بنیادی طور پر مکمل آف لائن چلنے کے لیے ڈیزائن کی گئی ہے۔ آپ کے بک مارکس، نوٹس اور درسی پیش رفت مکمل طور پر آپ کے فون کے انٹرنل اسٹوریج میں محفوظ رہتی ہے۔'
                : 'Baytul Ilm AI is engineered to operate predominantly offline. Your study bookmarks, quiz progress, Dars-e-Nizami syllabus completion tracking, and saved notes are stored locally on your device\'s internal storage using standard Android key-value preferences and databases.'}
            </p>
            <p>
              <strong>{language === 'ps' ? 'هیڅ سوداګریز اعلانات یا تعقیب نشته:' : language === 'ur' ? 'کوئی کمرشل اشتہارات یا ٹریکنگ نہیں:' : 'No Unsolicited Tracking:'}</strong>{' '}
              {language === 'ps'
                ? 'موږ په اپلیکیشن کې هیڅ ډول سوداګریز اعلانات (Ads) یا د کاروونکو شخصي تعقیب کوونکي وسایل نه لرو.'
                : language === 'ur'
                ? 'ہم اپنی ایپلی کیشن میں کوئی غیر ضروری تجارتی اشتہارات، لوکیشن ٹریکنگ یا نجی معلومات چرانے والے ٹولز استعمال نہیں کرتے۔'
                : 'We do not include third-party advertising SDKs, location-tracking services, or invasive analytics tools in our mobile builds.'}
            </p>
          </div>

          <div className="space-y-2">
            <h2 className="text-lg font-bold text-slate-900 dark:text-slate-100">
              {language === 'ps' ? '۳. د مصنوعي ذهانت (AI) مرستیال' : language === 'ur' ? '3. مصنوعی ذہانت (AI) لرننگ اسسٹنٹ' : '3. AI Learning Assistant Queries'}
            </h2>
            <p>
              {language === 'ps'
                ? 'کله چې تاسو د AI د مرستیال له لارې علمي او نحوي پوښتنې کوئ، پوښتنه یوازې د علمي ځواب لپاره په محفوظ SSL/TLS چینل لېږل کېږي او له شخصي هویت سره نه تړل کېږي.'
                : language === 'ur'
                ? 'جب آپ اختیاری AI اسسٹنٹ کے ذریعے صرف و نحو یا فقہی اصطلاحات کے متعلق سوالات پوچھتے ہیں، تو سوال محفوظ SSL/TLS کے ذریعے پروسیس ہوتا ہے اور آپ کی ذاتی شناخت کے ساتھ محفوظ نہیں کیا جاتا۔'
                : 'When you interact with the optional AI Learning Assistant feature, your submitted text question is processed to generate contextual explanations regarding Arabic grammar, Fiqh terminology, or Hadith classification. Questions are transmitted securely over SSL/TLS and are not linked to your personal identity.'}
            </p>
          </div>

          <div className="space-y-2">
            <h2 className="text-lg font-bold text-slate-900 dark:text-slate-100">
              {language === 'ps' ? '۴. حقوق او فکري ملکیت' : language === 'ur' ? '4. اشاعت و ڈیجیٹل حقوق' : '4. Intellectual Property & Digital Rights'}
            </h2>
            <p>
              {language === 'ps'
                ? 'بیت العلم AI یوازې د اسلامي علومو د زده کړې او د طالبانو د اسانتیا لپاره یو منظم ترتیب شوی فهرست وړاندې کوي.'
                : language === 'ur'
                ? 'بیت العلم AI درسِ نظامی اور علومِ اسلامیہ کے طلبہ و شائقین کے لیے ایک باضابطہ تعلیمی اشاریہ اور فکری معاون کا کردار ادا کرتی ہے۔'
                : 'Baytul Ilm AI provides a structured educational index and reference system. Some third-party educational materials may be subject to copyright or publisher rights. Users should access and distribute copyrighted materials only with appropriate authorization.'}
            </p>
          </div>

          <div className="space-y-2">
            <h2 className="text-lg font-bold text-slate-900 dark:text-slate-100">
              {language === 'ps' ? '۵. د اړیکې او مرستې معلومات' : language === 'ur' ? '5. رابطہ برائے رہنمائی' : '5. Contact Information'}
            </h2>
            <p>
              {language === 'ps'
                ? 'د هر ډول پوښتنې، مشورې یا تخنیکي ملاتړ لپاره موږ سره پر لاندې برېښنالیک اړیکه ونیسئ:'
                : language === 'ur'
                ? 'کسی بھی قسم کے استفسار، رازداری کے سوالات یا تکنیکی معاونت کے لیے ہم سے رابطہ فرمائیں:'
                : 'For privacy inquiries, technical support, or copyright verification, please contact us at:'} <br />
              <strong className="text-emerald-700 dark:text-emerald-400 font-mono">{APP_CONFIG.supportEmail}</strong>
            </p>
          </div>
        </div>
      </section>
    </div>
  );
};
