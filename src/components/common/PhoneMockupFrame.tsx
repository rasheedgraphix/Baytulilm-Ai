import React, { useState } from 'react';
import { BookOpen, GraduationCap, HelpCircle, Bot, Bookmark, User, Wifi, Battery, Signal, Sparkles } from 'lucide-react';
import { APP_CONFIG } from '../../config/appConfig';
import { BrandLogo } from './BrandLogo';

interface PhoneMockupFrameProps {
  initialTab?: string;
  className?: string;
}

export const PhoneMockupFrame: React.FC<PhoneMockupFrameProps> = ({
  initialTab = 'home',
  className = ''
}) => {
  const [activeTab, setActiveTab] = useState(initialTab);

  return (
    <div className={`relative mx-auto w-full max-w-[280px] sm:max-w-[340px] ${className}`}>
      {/* Outer Phone Hardware Shell */}
      <div className="relative bg-slate-900 rounded-[44px] p-3 shadow-2xl ring-1 ring-slate-800 shadow-emerald-950/20 border-4 border-slate-800/80">
        {/* Camera Punchhole & Ear Speaker Notch */}
        <div className="absolute top-5 left-1/2 -translate-x-1/2 w-28 h-5 bg-slate-950 rounded-full z-30 flex items-center justify-center gap-2 px-3">
          <div className="w-2.5 h-2.5 rounded-full bg-slate-900 ring-1 ring-slate-800"></div>
          <div className="w-10 h-1 rounded-full bg-slate-900"></div>
        </div>

        {/* Inner Glass Screen */}
        <div className="relative bg-slate-950 rounded-[36px] overflow-hidden border border-slate-800 aspect-[9/19] flex flex-col text-slate-100 select-none">
          {/* Top Status Bar */}
          <div className="pt-3 px-6 pb-2 flex items-center justify-between text-[11px] font-medium text-slate-400 bg-emerald-950/80 border-b border-emerald-900/40 z-20 shrink-0">
            <span>09:41</span>
            <div className="flex items-center gap-1.5 text-emerald-400">
              <Signal className="w-3 h-3" />
              <Wifi className="w-3 h-3" />
              <Battery className="w-3.5 h-3.5" />
            </div>
          </div>

          {/* App Header Inside Phone */}
          <div className="bg-emerald-900/90 px-4 py-2.5 border-b border-emerald-800/60 flex items-center justify-between z-20 shrink-0">
            <div className="flex items-center gap-2">
              <BrandLogo size="xs" />
              <div>
                <h4 className="text-xs font-bold text-white tracking-tight leading-none">
                  Baytul Ilm AI
                </h4>
                <span className="text-[9px] text-emerald-300 font-mono">v{APP_CONFIG.version}</span>
              </div>
            </div>
            <span className="text-[10px] px-2 py-0.5 rounded-full bg-emerald-800/80 text-emerald-200 border border-emerald-700/60">
              Android
            </span>
          </div>

          {/* Dynamic Content View Inside Screen */}
          <div className="flex-1 overflow-y-auto p-3 space-y-3 bg-gradient-to-b from-slate-950 via-emerald-950/20 to-slate-950 text-xs">
            {activeTab === 'home' && (
              <div className="space-y-3 animate-fade-in">
                {/* Greeting Card */}
                <div className="p-3 rounded-xl bg-gradient-to-r from-emerald-900/60 to-teal-900/60 border border-emerald-700/50 space-y-1">
                  <div className="flex items-center justify-between">
                    <span className="text-[10px] text-emerald-300 font-semibold uppercase tracking-wider">
                      Student Dashboard
                    </span>
                    <span className="text-[10px] text-amber-300 font-serif">السلام عليكم</span>
                  </div>
                  <h5 className="font-bold text-white text-sm">Welcome back, Taleb-e-Ilm</h5>
                  <p className="text-[11px] text-emerald-200/90">Dars-e-Nizami Year 2 • Class Progress: 68%</p>
                </div>

                {/* Quick Action Grid */}
                <div className="grid grid-cols-2 gap-2">
                  <div className="p-2.5 rounded-xl bg-slate-900/80 border border-slate-800 flex items-center gap-2">
                    <div className="p-1.5 rounded-lg bg-emerald-500/20 text-emerald-400">
                      <GraduationCap className="w-4 h-4" />
                    </div>
                    <div>
                      <div className="font-bold text-[11px]">Dars-e-Nizami</div>
                      <div className="text-[9px] text-slate-400">8 Academic Levels</div>
                    </div>
                  </div>
                  <div className="p-2.5 rounded-xl bg-slate-900/80 border border-slate-800 flex items-center gap-2">
                    <div className="p-1.5 rounded-lg bg-amber-500/20 text-amber-400">
                      <HelpCircle className="w-4 h-4" />
                    </div>
                    <div>
                      <div className="font-bold text-[11px]">Daily Quiz</div>
                      <div className="text-[9px] text-slate-400">MCQs & Results</div>
                    </div>
                  </div>
                </div>

                {/* AI Assistant Banner */}
                <div className="p-2.5 rounded-xl bg-emerald-950/60 border border-emerald-800/80 flex items-center gap-2">
                  <div className="p-2 rounded-lg bg-emerald-700 text-white shrink-0">
                    <Bot className="w-4 h-4" />
                  </div>
                  <div className="flex-1 min-w-0">
                    <div className="font-bold text-white flex items-center gap-1 text-[11px]">
                      AI Grammar Tutor <Sparkles className="w-3 h-3 text-amber-400" />
                    </div>
                    <div className="text-[9px] text-emerald-300/80 truncate">Ask questions on Sarf & Nahw</div>
                  </div>
                </div>

                {/* Recent Activity */}
                <div className="space-y-1.5">
                  <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider">
                    Recent Study Books
                  </span>
                  <div className="p-2 rounded-lg bg-slate-900/60 border border-slate-800 flex items-center justify-between text-[11px]">
                    <span className="font-medium text-slate-200">Mukhtasar-ul-Quduri (Fiqh)</span>
                    <span className="text-emerald-400 font-mono text-[10px]">Ch 4</span>
                  </div>
                  <div className="p-2 rounded-lg bg-slate-900/60 border border-slate-800 flex items-center justify-between text-[11px]">
                    <span className="font-medium text-slate-200">Hidayat-un-Nahw (Grammar)</span>
                    <span className="text-emerald-400 font-mono text-[10px]">Ch 2</span>
                  </div>
                </div>
              </div>
            )}

            {activeTab === 'dars' && (
              <div className="space-y-2.5 animate-fade-in">
                <div className="flex items-center justify-between">
                  <h5 className="font-bold text-emerald-300 text-xs">Dars-e-Nizami Classes</h5>
                  <span className="text-[9px] bg-emerald-900/60 text-emerald-200 px-2 py-0.5 rounded-full">Curriculum</span>
                </div>
                <div className="space-y-2">
                  <div className="p-2 rounded-lg bg-emerald-900/40 border border-emerald-700/60 space-y-1">
                    <div className="font-bold text-white text-[11px]">A'la - Year 1 (الإعدادية)</div>
                    <div className="text-[10px] text-emerald-200">Mizan-us-Sarf • Nahw Mir • Nur-ul-Izah</div>
                  </div>
                  <div className="p-2 rounded-lg bg-slate-900 border border-slate-800 space-y-1">
                    <div className="font-bold text-slate-200 text-[11px]">Saniya - Year 2 (الثنائية)</div>
                    <div className="text-[10px] text-slate-400">Ilm-us-Sighah • Hidayat-un-Nahw • Quduri</div>
                  </div>
                  <div className="p-2 rounded-lg bg-slate-900 border border-slate-800 space-y-1">
                    <div className="font-bold text-slate-200 text-[11px]">Salisa - Year 3 (الثالثة)</div>
                    <div className="text-[10px] text-slate-400">Usul-ush-Shashi • Kanz • Durus-ul-Balaghat</div>
                  </div>
                  <div className="p-2 rounded-lg bg-amber-950/40 border border-amber-800/40 space-y-1">
                    <div className="font-bold text-amber-200 text-[11px]">Dora-e-Hadith (دورة الحديث)</div>
                    <div className="text-[10px] text-amber-300/80">Sihah Sittah • Bukhari • Muslim • Tirmidhi</div>
                  </div>
                </div>
              </div>
            )}

            {activeTab === 'quiz' && (
              <div className="space-y-2.5 animate-fade-in">
                <div className="p-2.5 rounded-xl bg-slate-900 border border-slate-800 space-y-2">
                  <div className="flex items-center justify-between text-[10px]">
                    <span className="px-2 py-0.5 rounded bg-emerald-950 text-emerald-300 font-bold">Nahw Quiz</span>
                    <span className="text-amber-400 font-mono">Timer 00:45</span>
                  </div>
                  <p className="font-semibold text-slate-100 text-[11px]">
                    Q: What is the grammatical function of "Al-Ism" in Arabic?
                  </p>
                  <div className="space-y-1 text-[10px]">
                    <div className="p-1.5 rounded bg-emerald-900/60 border border-emerald-600 text-emerald-100 font-medium flex items-center justify-between">
                      <span>A. Conveys meaning without time</span>
                      <span>✓ Correct</span>
                    </div>
                    <div className="p-1.5 rounded bg-slate-950 border border-slate-800 text-slate-400">
                      B. Always tied to past tense
                    </div>
                  </div>
                </div>
                <div className="p-2 rounded-lg bg-emerald-950/60 border border-emerald-800 text-emerald-200 text-[10px] space-y-1">
                  <span className="font-bold block">Explanation Note:</span>
                  <p className="text-slate-300 leading-tight">
                    An Ism indicates an independent meaning not associated with past, present, or future time.
                  </p>
                </div>
              </div>
            )}

            {activeTab === 'ai' && (
              <div className="space-y-2.5 animate-fade-in flex flex-col h-full justify-between">
                <div className="space-y-2">
                  <div className="p-2 rounded-lg bg-slate-900 border border-slate-800 text-[11px] ml-4 text-slate-200">
                    User: Explain the difference between Sarf and Nahw?
                  </div>
                  <div className="p-2 rounded-lg bg-emerald-950/80 border border-emerald-800/80 text-[11px] mr-4 text-emerald-100 space-y-1">
                    <span className="font-bold text-amber-300 flex items-center gap-1 text-[10px]">
                      <Bot className="w-3 h-3" /> Baytul Ilm AI:
                    </span>
                    <p className="leading-snug text-[10px] text-slate-200">
                      <strong>Sarf (Morphology)</strong> studies single word structures and conjugations. <strong>Nahw (Syntax)</strong> studies sentence formation and end-case vowels (I'rab).
                    </p>
                  </div>
                </div>
                <div className="p-1.5 rounded-lg bg-slate-900 border border-slate-800 flex items-center gap-1">
                  <input
                    id="mockup-chat-input"
                    name="mockupChat"
                    type="text"
                    disabled
                    placeholder="Ask about Sarf, Nahw, Fiqh..."
                    className="bg-transparent text-[10px] flex-1 text-slate-300 focus:outline-none"
                    aria-label="Mockup Input"
                  />
                  <div className="w-5 h-5 rounded bg-emerald-700 text-white flex items-center justify-center text-[9px] font-bold">
                    ↑
                  </div>
                </div>
              </div>
            )}
          </div>

          {/* Bottom App Navigation Bar */}
          <div className="bg-slate-950 border-t border-slate-800 px-2 py-1.5 grid grid-cols-4 gap-1 text-center text-[9px] shrink-0">
            <button
              onClick={() => setActiveTab('home')}
              className={`p-1 rounded flex flex-col items-center gap-0.5 transition-colors ${
                activeTab === 'home' ? 'text-emerald-400 font-bold bg-emerald-950/60' : 'text-slate-500'
              }`}
            >
              <BookOpen className="w-3.5 h-3.5" />
              <span>Home</span>
            </button>

            <button
              onClick={() => setActiveTab('dars')}
              className={`p-1 rounded flex flex-col items-center gap-0.5 transition-colors ${
                activeTab === 'dars' ? 'text-emerald-400 font-bold bg-emerald-950/60' : 'text-slate-500'
              }`}
            >
              <GraduationCap className="w-3.5 h-3.5" />
              <span>Dars</span>
            </button>

            <button
              onClick={() => setActiveTab('quiz')}
              className={`p-1 rounded flex flex-col items-center gap-0.5 transition-colors ${
                activeTab === 'quiz' ? 'text-emerald-400 font-bold bg-emerald-950/60' : 'text-slate-500'
              }`}
            >
              <HelpCircle className="w-3.5 h-3.5" />
              <span>Quiz</span>
            </button>

            <button
              onClick={() => setActiveTab('ai')}
              className={`p-1 rounded flex flex-col items-center gap-0.5 transition-colors ${
                activeTab === 'ai' ? 'text-emerald-400 font-bold bg-emerald-950/60' : 'text-slate-500'
              }`}
            >
              <Bot className="w-3.5 h-3.5" />
              <span>AI Tutor</span>
            </button>
          </div>

          {/* Bottom Home Indicator Bar */}
          <div className="pb-1 bg-slate-950 flex justify-center">
            <div className="w-20 h-1 bg-slate-700 rounded-full"></div>
          </div>
        </div>
      </div>
    </div>
  );
};
