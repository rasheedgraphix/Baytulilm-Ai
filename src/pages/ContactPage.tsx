import React, { useState } from 'react';
import { SectionHeader } from '../components/common/SectionHeader';
import { APP_CONFIG } from '../config/appConfig';
import { Mail, Phone, MapPin, Youtube, User, ExternalLink, Send, Check, Info } from 'lucide-react';
import { IslamicPatternBg } from '../components/layout/IslamicPatternBg';
import { useLanguage } from '../context/LanguageContext';

export const ContactPage: React.FC = () => {
  const [submitted, setSubmitted] = useState(false);
  const [formData, setFormData] = useState({ name: '', email: '', message: '' });
  const { language, isRtl } = useLanguage();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.email || !formData.message) return;

    // Trigger direct client mailto
    const subject = encodeURIComponent(`Inquiry from ${formData.name || 'Student'}`);
    const body = encodeURIComponent(`Name: ${formData.name}\nEmail: ${formData.email}\n\nMessage:\n${formData.message}`);
    window.location.href = `mailto:hafiznoumanrasheed@gmail.com?subject=${subject}&body=${body}`;

    setSubmitted(true);
  };

  const getBadge = () => {
    if (language === 'ps') return 'اړیکه او مرسته';
    if (language === 'ur') return 'رابطہ اور تکنیکی معاونت';
    return 'Get in Touch';
  };

  const getTitle = () => {
    if (language === 'ps') return 'د اړیکې او تخنیکي ملاتړ څانګه';
    if (language === 'ur') return 'رابطہ و رہنمائی پورٹل';
    return 'Contact & Support Portal';
  };

  const getSubtitle = () => {
    if (language === 'ps') return 'د اپلیکیشن په اړه خپلې نېکې مشورې، وړاندیزونه، ستونزې او پوښتنې موږ سره شریکې کړئ.';
    if (language === 'ur') return 'ایپ کے بارے میں تجاویز، تکنیکی مسائل کی نشاندہی یا کتب و نصاب میں اضافے کے لیے ہم سے رابطہ کریں۔';
    return 'Send feedback, report technical bugs, suggest syllabus additions, or reach out directly to our team.';
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
            arabicTitle="التَّوَاصُلُ وَالدَّعْمُ الفَنِّي"
            subtitle={getSubtitle()}
          />
        </div>
      </section>

      <section className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8" dir={isRtl ? 'rtl' : 'ltr'}>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
          {/* Contact Details & App Information */}
          <div className="space-y-6">
            <div className="bg-white dark:bg-slate-900 rounded-3xl p-6 border border-slate-200/80 dark:border-slate-800 space-y-4">
              <h3 className="text-xl font-bold text-slate-900 dark:text-slate-100">
                {language === 'ps' ? 'د مستقیمې اړیکې معلومات' : language === 'ur' ? 'براہ راست رابطہ کی معلومات' : 'Direct Contact Information'}
              </h3>

              <div className="space-y-3 text-sm">
                {/* Contact Name */}
                <div className="flex items-center gap-3 p-3 rounded-xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800">
                  <div className="p-2 rounded-lg bg-emerald-100 dark:bg-emerald-950 text-emerald-800 dark:text-emerald-300 shrink-0">
                    <User className="w-5 h-5" />
                  </div>
                  <div>
                    <span className="text-xs text-slate-500 block">
                      {language === 'ps' ? 'مسؤل / جوړوونکی' : language === 'ur' ? 'نامِ ڈیولپر' : 'Contact Name'}
                    </span>
                    <span className="font-semibold text-slate-900 dark:text-slate-100">
                      {language === 'ps' || language === 'ur' ? 'حافظ نعمان الرشید' : 'Nouman Ur Rasheed'}
                    </span>
                  </div>
                </div>

                {/* Email Address - Clickable */}
                <a
                  href="mailto:hafiznoumanrasheed@gmail.com"
                  className="flex items-center gap-3 p-3 rounded-xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 hover:border-emerald-500 transition-all group"
                >
                  <div className="p-2 rounded-lg bg-emerald-100 dark:bg-emerald-950 text-emerald-800 dark:text-emerald-300 shrink-0">
                    <Mail className="w-5 h-5" />
                  </div>
                  <div className="flex-1 overflow-hidden">
                    <span className="text-xs text-slate-500 block">
                      {language === 'ps' ? 'برېښنالیک پته' : language === 'ur' ? 'ای میل ایڈریس' : 'Email Address'}
                    </span>
                    <span className="font-semibold text-slate-900 dark:text-slate-100 group-hover:text-emerald-700 dark:group-hover:text-emerald-400 break-all flex items-center gap-1">
                      hafiznoumanrasheed@gmail.com <ExternalLink className="w-3 h-3 text-slate-400 shrink-0" />
                    </span>
                  </div>
                </a>

                {/* WhatsApp - Clickable */}
                <a
                  href="https://wa.me/923455067874"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="flex items-center gap-3 p-3 rounded-xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 hover:border-emerald-500 transition-all group"
                >
                  <div className="p-2 rounded-lg bg-emerald-100 dark:bg-emerald-950 text-emerald-800 dark:text-emerald-300 shrink-0">
                    <Phone className="w-5 h-5" />
                  </div>
                  <div className="flex-1">
                    <span className="text-xs text-slate-500 block">
                      {language === 'ps' ? 'واټس‌اپ رابطه' : language === 'ur' ? 'واٹس ایپ رابطہ' : 'WhatsApp'}
                    </span>
                    <span className="font-semibold text-slate-900 dark:text-slate-100 group-hover:text-emerald-700 dark:group-hover:text-emerald-400 flex items-center gap-1 font-mono">
                      03455067874 <ExternalLink className="w-3 h-3 text-slate-400 shrink-0" />
                    </span>
                  </div>
                </a>

                {/* Location */}
                <div className="flex items-center gap-3 p-3 rounded-xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800">
                  <div className="p-2 rounded-lg bg-emerald-100 dark:bg-emerald-950 text-emerald-800 dark:text-emerald-300 shrink-0">
                    <MapPin className="w-5 h-5" />
                  </div>
                  <div>
                    <span className="text-xs text-slate-500 block">
                      {language === 'ps' ? 'موقعیت / سیمه' : language === 'ur' ? 'مقام / پتہ' : 'Location'}
                    </span>
                    <span className="font-semibold text-slate-900 dark:text-slate-100">
                      {language === 'ps' ? 'صوابۍ، خیبر پښتونخوا، پاکستان' : language === 'ur' ? 'صوابی، خیبر پختونخوا، پاکستان' : 'Swabi, KPK, Pakistan'}
                    </span>
                  </div>
                </div>

                {/* YouTube Channel - Clickable in new tab */}
                <a
                  href="https://www.youtube.com/@FactVerseMedia-r3c"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="flex items-center gap-3 p-3 rounded-xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 hover:border-emerald-500 transition-all group"
                >
                  <div className="p-2 rounded-lg bg-red-100 dark:bg-red-950/80 text-red-600 dark:text-red-400 shrink-0">
                    <Youtube className="w-5 h-5" />
                  </div>
                  <div className="flex-1 overflow-hidden">
                    <span className="text-xs text-slate-500 block">
                      {language === 'ps' ? 'یوټیوب چینل' : language === 'ur' ? 'یوٹیوب چینل' : 'YouTube Channel'}
                    </span>
                    <span className="font-semibold text-slate-900 dark:text-slate-100 group-hover:text-emerald-700 dark:group-hover:text-emerald-400 flex items-center gap-1 truncate">
                      @FactVerseMedia-r3c <ExternalLink className="w-3 h-3 text-slate-400 shrink-0" />
                    </span>
                  </div>
                </a>
              </div>
            </div>

            {/* Application Information Box */}
            <div className="bg-slate-900 text-white rounded-3xl p-6 border border-slate-800 space-y-3">
              <div className="flex items-center gap-2 text-emerald-400 font-bold text-sm">
                <Info className="w-4 h-4" />
                <span>{language === 'ps' ? 'د اپلیکیشن مشخصات' : language === 'ur' ? 'ایپلیکیشن کی شناخت و تفصیل' : 'Application Identity'}</span>
              </div>
              <div className="space-y-1 text-xs text-slate-300 font-mono">
                <div>App Name: {APP_CONFIG.appName}</div>
                <div>Package: {APP_CONFIG.packageName}</div>
                <div>Version: {APP_CONFIG.version} (Build {APP_CONFIG.versionCode})</div>
                <div>Developer: {APP_CONFIG.developerName}</div>
              </div>
            </div>
          </div>

          {/* Contact Form */}
          <div className="bg-white dark:bg-slate-900 rounded-3xl p-6 border border-slate-200/80 dark:border-slate-800 space-y-4">
            <h3 className="text-xl font-bold text-slate-900 dark:text-slate-100">
              {language === 'ps' ? 'د برېښنالیک پیغام استول' : language === 'ur' ? 'ای میل پیغام بھیجیں' : 'Send Email Message'}
            </h3>

            {submitted ? (
              <div className="p-6 rounded-2xl bg-emerald-50 dark:bg-emerald-950/60 border border-emerald-300 dark:border-emerald-800 text-emerald-900 dark:text-emerald-200 text-center space-y-3">
                <div className="w-12 h-12 rounded-full bg-emerald-700 text-white flex items-center justify-center mx-auto font-bold">
                  <Check className="w-6 h-6" />
                </div>
                <h4 className="font-bold text-base">
                  {language === 'ps' ? 'د ایمیل سافټویر خلاص شو' : language === 'ur' ? 'ای میل کلائنٹ کھل گیا ہے' : 'Opening Mail Client'}
                </h4>
                <p className="text-xs leading-relaxed text-slate-600 dark:text-slate-300">
                  {language === 'ps'
                    ? `ستاسو پیغام چمتو شو او په ${APP_CONFIG.supportEmail} پته لېږلو ته تیار دی.`
                    : language === 'ur'
                    ? `آپ کا پیغام تیار ہے اور ${APP_CONFIG.supportEmail} پر ارسال کے لیے ای میل ایپ میں کھول دیا گیا ہے۔`
                    : `Your device's default email client has been launched with your message addressed to ${APP_CONFIG.supportEmail}. Please confirm send inside your email app.`}
                </p>
                <div className="pt-2">
                  <a
                    href={`mailto:${APP_CONFIG.supportEmail}`}
                    className="inline-block px-4 py-2 rounded-xl bg-emerald-800 text-white text-xs font-bold hover:bg-emerald-900 transition-colors"
                  >
                    {language === 'ps' ? 'د ایمیل ایپ بیا خلاصول' : language === 'ur' ? 'دوبارہ ای میل کھولیں' : 'Open Email Application Again'}
                  </a>
                </div>
              </div>
            ) : (
              <form id="contact-inquiry-form" name="contactForm" onSubmit={handleSubmit} className="space-y-4 text-xs sm:text-sm">
                <div className="space-y-1">
                  <label htmlFor="contact-name" className="font-semibold text-slate-700 dark:text-slate-300">
                    {language === 'ps' ? 'ستاسو مبارک نوم' : language === 'ur' ? 'آپ کا نام' : 'Your Name'}
                  </label>
                  <input
                    id="contact-name"
                    name="name"
                    type="text"
                    required
                    placeholder={language === 'ps' ? 'طالب العلم' : language === 'ur' ? 'طالب علم' : 'Taleb-e-Ilm'}
                    value={formData.name}
                    onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                    className="w-full p-3 rounded-xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 text-slate-900 dark:text-slate-100 focus:outline-none focus:border-emerald-600"
                  />
                </div>

                <div className="space-y-1">
                  <label htmlFor="contact-email" className="font-semibold text-slate-700 dark:text-slate-300">
                    {language === 'ps' ? 'برېښنالیک (ای میل)' : language === 'ur' ? 'ای میل ایڈریس' : 'Email Address'}
                  </label>
                  <input
                    id="contact-email"
                    name="email"
                    type="email"
                    required
                    placeholder="student@example.com"
                    value={formData.email}
                    onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                    className="w-full p-3 rounded-xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 text-slate-900 dark:text-slate-100 focus:outline-none focus:border-emerald-600"
                  />
                </div>

                <div className="space-y-1">
                  <label htmlFor="contact-message" className="font-semibold text-slate-700 dark:text-slate-300">
                    {language === 'ps' ? 'ستاسو پیغام یا وړاندیز' : language === 'ur' ? 'آپ کا پیغام / تجویز' : 'Message / Inquiry'}
                  </label>
                  <textarea
                    id="contact-message"
                    name="message"
                    rows={4}
                    required
                    placeholder={
                      language === 'ps'
                        ? 'خپله پوښتنه، مشوره یا وړاندیز دلته ولیکئ...'
                        : language === 'ur'
                        ? 'اپنی رائے، مسئلہ یا تجویز یہاں تحریر فرمائیں...'
                        : 'Describe your inquiry or feature suggestion...'
                    }
                    value={formData.message}
                    onChange={(e) => setFormData({ ...formData, message: e.target.value })}
                    className="w-full p-3 rounded-xl bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 text-slate-900 dark:text-slate-100 focus:outline-none focus:border-emerald-600"
                  ></textarea>
                </div>

                <button
                  type="submit"
                  className="w-full py-3.5 rounded-xl bg-emerald-800 hover:bg-emerald-900 text-white font-bold transition-all flex items-center justify-center gap-2 cursor-pointer shadow-md"
                >
                  <Send className="w-4 h-4" />
                  <span>
                    {language === 'ps' ? 'د ایمیل له لارې استول' : language === 'ur' ? 'ای میل کے ذریعے بھیجیں' : 'Send via Email Client'}
                  </span>
                </button>
              </form>
            )}
          </div>
        </div>
      </section>
    </div>
  );
};
