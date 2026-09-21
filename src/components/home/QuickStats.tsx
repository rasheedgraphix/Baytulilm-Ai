import React from 'react';
import { useLanguage } from '../../context/LanguageContext';

export const QuickStats: React.FC = () => {
  const { language, isRtl } = useLanguage();

  const stats = [
    {
      value: language === 'ps' ? '۸ کلن' : language === 'ur' ? '8 سالہ' : '8 Years',
      label: language === 'ps' ? 'د درسِ نظامي نصاب' : language === 'ur' ? 'درسِ نظامی نصاب' : 'Dars-e-Nizami Levels',
      description: language === 'ps' ? 'له لومړۍ درجې تر دورۂ حدیث' : language === 'ur' ? 'درجہ اولیٰ تا دورۂ حدیث' : 'Darja Ula to Dora-e-Hadith'
    },
    {
      value: language === 'ps' ? '۸ په ۱ کې' : language === 'ur' ? '8+ فیچرز' : '8+ In 1',
      label: language === 'ps' ? 'قرآن او اسلامي اوزار' : language === 'ur' ? 'قرآن و اسلامی ٹولز' : 'Quran & Islamic Tools',
      description: language === 'ps' ? 'قرآن، دعاګانې، ۹۹ نومونه، تسبېح' : language === 'ur' ? 'قرآن، دعائیں، 99 نام، قبلہ، تسبیح' : 'Quran, Duas, 99 Names, Qibla'
    },
    {
      value: '1,500+',
      label: language === 'ps' ? 'دیني او درسي کوئزونه' : language === 'ur' ? 'دینی و درسی کوئز' : 'Interactive Quizzes',
      description: language === 'ps' ? 'د هر درسي کتاب په کچه پوښتنې' : language === 'ur' ? 'سبق وار کثیر الانتخابی سوالات' : 'Chapter-wise MCQs with answers'
    },
    {
      value: '24/7',
      label: language === 'ps' ? 'اسلامي AI مرستیال' : language === 'ur' ? 'اسلامی AI معاون' : 'AI Learning Assistant',
      description: language === 'ps' ? 'د صرف او نحوې سمدستي ځوابونه' : language === 'ur' ? 'صرف، نحو اور فقہی اصطلاحات' : 'Contextual Islamic answers'
    }
  ];

  return (
    <section className="py-10 sm:py-12 bg-[#011410] border-t border-emerald-950/80 border-b border-emerald-950/60 text-white" dir={isRtl ? 'rtl' : 'ltr'}>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 sm:gap-6 md:gap-8">
          {stats.map((stat, idx) => (
            <div
              key={idx}
              className="p-5 sm:p-6 rounded-2xl bg-[#031d16]/90 border border-emerald-800/30 text-center space-y-1.5 hover:border-emerald-500/50 hover:bg-[#05261d] transition-all shadow-lg shadow-black/20"
            >
              <div className="text-2xl sm:text-3xl md:text-4xl font-extrabold text-emerald-400 font-sans tracking-tight">
                {stat.value}
              </div>
              <div className="text-xs sm:text-sm font-bold text-white">
                {stat.label}
              </div>
              <div className="text-[11px] sm:text-xs text-slate-400 leading-tight">
                {stat.description}
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};
