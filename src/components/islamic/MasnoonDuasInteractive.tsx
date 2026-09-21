import React, { useState, useMemo } from 'react';
import {
  Search,
  BookOpen,
  Copy,
  Check,
  Printer,
  Sparkles,
  Heart,
  Share2,
  Filter,
  Bookmark,
  ChevronDown
} from 'lucide-react';
import { AUTHENTIC_100_DUAS, DUA_CATEGORIES, DuaItem } from '../../data/duasData';
import { useLanguage } from '../../context/LanguageContext';

export const MasnoonDuasInteractive: React.FC = () => {
  const { isRtl } = useLanguage();
  const [searchTerm, setSearchTerm] = useState<string>('');
  const [selectedCategory, setSelectedCategory] = useState<string>('سب');
  const [copiedId, setCopiedId] = useState<number | null>(null);
  const [bookmarkedIds, setBookmarkedIds] = useState<number[]>([]);
  const [showOnlyBookmarks, setShowOnlyBookmarks] = useState<boolean>(false);

  // Filtered Duas
  const filteredDuas = useMemo(() => {
    return AUTHENTIC_100_DUAS.filter((dua) => {
      if (showOnlyBookmarks && !bookmarkedIds.includes(dua.id)) {
        return false;
      }
      if (selectedCategory !== 'سب' && dua.category !== selectedCategory) {
        return false;
      }
      if (!searchTerm.trim()) return true;
      const q = searchTerm.trim().toLowerCase();
      return (
        dua.title.toLowerCase().includes(q) ||
        dua.arabic.includes(q) ||
        dua.urdu.includes(q) ||
        dua.translit.toLowerCase().includes(q) ||
        dua.ref.toLowerCase().includes(q) ||
        dua.category.includes(q)
      );
    });
  }, [searchTerm, selectedCategory, showOnlyBookmarks, bookmarkedIds]);

  const handleCopy = (dua: DuaItem) => {
    const textToCopy = `${dua.title}\n\n${dua.arabic}\n\nترجمہ: ${dua.urdu}\n\nتلفظ: ${dua.translit}\nحوالہ: ${dua.ref}\n(ماخوذ از بیت العلم پورٹل)`;
    navigator.clipboard.writeText(textToCopy);
    setCopiedId(dua.id);
    setTimeout(() => {
      setCopiedId(null);
    }, 2000);
  };

  const toggleBookmark = (id: number) => {
    setBookmarkedIds((prev) =>
      prev.includes(id) ? prev.filter((item) => item !== id) : [...prev, id]
    );
  };

  const handlePrint = () => {
    window.print();
  };

  return (
    <div className="w-full space-y-6 font-urdu" dir="rtl">
      {/* Header Controls & Filters */}
      <div className="no-print bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-3xl p-4 sm:p-6 shadow-sm space-y-5">
        
        {/* Search and Action Bar */}
        <div className="flex flex-col sm:flex-row gap-3 items-stretch sm:items-center justify-between">
          <div className="relative flex-1">
            <Search className="w-5 h-5 absolute right-3.5 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none" />
            <input
              type="text"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              placeholder="دعا کا عنوان، عربی الفاظ، اردو ترجمہ یا حوالہ تلاش کریں..."
              className="w-full h-12 pr-11 pl-4 bg-slate-50 dark:bg-slate-950 border border-slate-200 dark:border-slate-800 rounded-2xl text-sm sm:text-base text-slate-900 dark:text-slate-100 placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-emerald-500/30 focus:border-emerald-500 text-right font-urdu transition-all shadow-inner"
            />
            {searchTerm && (
              <button
                onClick={() => setSearchTerm('')}
                className="absolute left-3.5 top-1/2 -translate-y-1/2 text-xs text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 font-sans"
              >
                ✕
              </button>
            )}
          </div>

          <div className="flex items-center gap-2">
            {/* Bookmarks Toggle */}
            <button
              onClick={() => setShowOnlyBookmarks(!showOnlyBookmarks)}
              className={`h-12 px-4 rounded-2xl text-xs sm:text-sm font-bold flex items-center gap-2 transition-all border shrink-0 ${
                showOnlyBookmarks
                  ? 'bg-rose-500 text-white border-rose-600 shadow-md'
                  : 'bg-slate-50 dark:bg-slate-950 text-slate-700 dark:text-slate-300 border-slate-200 dark:border-slate-800 hover:bg-slate-100 dark:hover:bg-slate-800'
              }`}
            >
              <Bookmark className={`w-4 h-4 ${showOnlyBookmarks ? 'fill-current' : ''}`} />
              <span>محفوظ دعائیں ({bookmarkedIds.length})</span>
            </button>

            {/* Print Button */}
            <button
              onClick={handlePrint}
              className="h-12 px-4 rounded-2xl bg-slate-50 dark:bg-slate-950 hover:bg-slate-100 dark:hover:bg-slate-800 text-slate-700 dark:text-slate-300 border border-slate-200 dark:border-slate-800 text-xs sm:text-sm font-bold flex items-center gap-2 transition-all shrink-0"
              title="A4 پرنٹ یا پی ڈی ایف محفوظ کریں"
            >
              <Printer className="w-4 h-4 text-emerald-500" />
              <span className="hidden sm:inline">پرنٹ / PDF</span>
            </button>
          </div>
        </div>

        {/* Categories Chips */}
        <div className="space-y-2">
          <div className="flex items-center gap-1.5 text-xs text-slate-500 dark:text-slate-400 font-bold">
            <Filter className="w-3.5 h-3.5" />
            <span>زمرہ جات کے مطابق فلٹر کریں:</span>
          </div>

          <div className="flex flex-wrap gap-2">
            {DUA_CATEGORIES.map((cat) => {
              const isSelected = selectedCategory === cat && !showOnlyBookmarks;
              return (
                <button
                  key={cat}
                  onClick={() => {
                    setSelectedCategory(cat);
                    setShowOnlyBookmarks(false);
                  }}
                  className={`px-3.5 py-1.5 rounded-full text-xs sm:text-sm font-urdu font-bold transition-all border ${
                    isSelected
                      ? 'bg-emerald-600 text-white border-emerald-600 shadow-md shadow-emerald-900/20'
                      : 'bg-slate-50 dark:bg-slate-950 text-slate-600 dark:text-slate-300 border-slate-200 dark:border-slate-800 hover:bg-slate-100 dark:hover:bg-slate-800'
                  }`}
                >
                  {cat}
                </button>
              );
            })}
          </div>
        </div>

        {/* Results Counter Bar */}
        <div className="flex items-center justify-between text-xs text-slate-500 dark:text-slate-400 pt-2 border-t border-slate-100 dark:border-slate-800">
          <div>
            مجموعی: <strong>{filteredDuas.length}</strong> دعائیں دستیاب ہیں
            {selectedCategory !== 'سب' && ` (${selectedCategory})`}
          </div>
          <div className="text-[11px] text-emerald-600 dark:text-emerald-400 font-bold">
            مستند مکمل عربی متن مع صحیح احادیث و قرآنی حوالہ جات
          </div>
        </div>
      </div>

      {/* 100 Duas Cards Grid / List */}
      <div className="space-y-5">
        {filteredDuas.map((dua) => {
          const isCopied = copiedId === dua.id;
          const isBookmarked = bookmarkedIds.includes(dua.id);

          return (
            <div
              key={dua.id}
              className="dua-card bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-3xl overflow-hidden shadow-sm hover:shadow-md transition-all space-y-0 break-inside-avoid"
            >
              {/* Card Header: Title + ID Badge + Action buttons */}
              <div className="flex items-center justify-between gap-3 px-4 sm:px-6 py-3.5 bg-slate-50/80 dark:bg-slate-950/70 border-b border-slate-100 dark:border-slate-800">
                <div className="flex items-center gap-3 min-w-0">
                  <span className="flex-shrink-0 w-8 h-8 rounded-full bg-slate-900 dark:bg-emerald-600 text-white flex items-center justify-center text-xs font-mono font-black shadow-inner">
                    {dua.id}
                  </span>
                  <div className="min-w-0">
                    <h2 className="text-base sm:text-lg font-black text-slate-900 dark:text-white truncate">
                      {dua.title}
                    </h2>
                    <div className="flex items-center gap-2 mt-0.5">
                      <span className="text-[10px] px-2 py-0.5 rounded-full bg-emerald-500/10 text-emerald-700 dark:text-emerald-300 border border-emerald-500/20 font-bold">
                        {dua.category}
                      </span>
                      <span className="text-[10px] text-slate-500 dark:text-slate-400 font-sans truncate">
                        {dua.ref}
                      </span>
                    </div>
                  </div>
                </div>

                {/* Right Action Icons (Copy, Bookmark) */}
                <div className="no-print flex items-center gap-1.5 shrink-0">
                  <button
                    onClick={() => toggleBookmark(dua.id)}
                    className={`p-2 rounded-xl transition-all ${
                      isBookmarked
                        ? 'text-rose-500 bg-rose-50 dark:bg-rose-950/30'
                        : 'text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 hover:bg-slate-100 dark:hover:bg-slate-800'
                    }`}
                    title={isBookmarked ? 'محفوظ سے ہٹائیں' : 'دعا محفوظ کریں'}
                  >
                    <Bookmark className={`w-4 h-4 ${isBookmarked ? 'fill-current' : ''}`} />
                  </button>

                  <button
                    onClick={() => handleCopy(dua)}
                    className={`px-3 py-1.5 rounded-xl text-xs font-bold flex items-center gap-1.5 transition-all ${
                      isCopied
                        ? 'bg-emerald-600 text-white shadow'
                        : 'bg-slate-200/70 dark:bg-slate-800 hover:bg-emerald-600 hover:text-white text-slate-700 dark:text-slate-200'
                    }`}
                    title="مکمل دعا اور ترجمہ کاپی کریں"
                  >
                    {isCopied ? (
                      <>
                        <Check className="w-3.5 h-3.5" />
                        <span>کاپی ہو گئی</span>
                      </>
                    ) : (
                      <>
                        <Copy className="w-3.5 h-3.5" />
                        <span>کاپی</span>
                      </>
                    )}
                  </button>
                </div>
              </div>

              {/* Card Body: Arabic & Urdu boxes */}
              <div className="p-4 sm:p-6 space-y-4">
                
                {/* Arabic Dua Box (Blue Accent) */}
                <div className="relative rounded-2xl bg-blue-50/80 dark:bg-blue-950/20 border border-blue-200 dark:border-blue-900/50 p-4 sm:p-5">
                  <div className="flex justify-end mb-2">
                    <span className="inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-md bg-white dark:bg-slate-900 border border-blue-200 dark:border-blue-800 text-[11px] font-black text-blue-700 dark:text-blue-300 shadow-sm font-urdu">
                      <span className="w-1.5 h-1.5 rounded-full bg-blue-500" />
                      عربی دعا (مکمل متن)
                    </span>
                  </div>
                  <p
                    className="text-xl sm:text-2xl md:text-3xl text-slate-900 dark:text-slate-100 text-right leading-[2.2] select-text"
                    style={{ fontFamily: "'Amiri', 'Traditional Arabic', serif" }}
                  >
                    {dua.arabic}
                  </p>
                </div>

                {/* Urdu Translation Box (Green Accent) */}
                <div className="relative rounded-2xl bg-emerald-50/80 dark:bg-emerald-950/20 border border-emerald-200 dark:border-emerald-900/50 p-4 sm:p-5">
                  <div className="flex justify-end mb-2">
                    <span className="inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-md bg-white dark:bg-slate-900 border border-emerald-200 dark:border-emerald-800 text-[11px] font-black text-emerald-700 dark:text-emerald-300 shadow-sm font-urdu">
                      <span className="w-1.5 h-1.5 rounded-full bg-emerald-500" />
                      اردو ترجمہ
                    </span>
                  </div>
                  <p className="text-sm sm:text-base md:text-lg text-emerald-950 dark:text-emerald-200 text-right leading-relaxed font-urdu">
                    {dua.urdu}
                  </p>
                </div>

                {/* Transliteration & Reference Row */}
                <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-2 pt-2 border-t border-slate-100 dark:border-slate-800/80 text-xs">
                  <div className="text-slate-500 dark:text-slate-400 italic text-right font-sans" dir="ltr">
                    <span className="text-[10px] font-bold text-slate-400 dark:text-slate-500 not-italic mr-1.5 font-urdu">
                      تلفظ:
                    </span>
                    {dua.translit}
                  </div>

                  <div className="flex items-center gap-2 self-end sm:self-auto text-[11px] text-slate-500 dark:text-slate-400">
                    <span className="px-2.5 py-1 rounded-xl bg-slate-100 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 font-bold">
                      {dua.ref}
                    </span>
                  </div>
                </div>

              </div>
            </div>
          );
        })}

        {filteredDuas.length === 0 && (
          <div className="text-center py-16 bg-white dark:bg-slate-900 rounded-3xl border border-dashed border-slate-300 dark:border-slate-700 p-8 space-y-3">
            <BookOpen className="w-10 h-10 mx-auto text-slate-400" />
            <div className="text-base font-bold text-slate-700 dark:text-slate-300">
              کوئی دعا نہیں ملی
            </div>
            <p className="text-xs text-slate-500">
              برائے مہربانی تلاش کا لفظ بدل کر دیکھیں یا اوپر دیے گئے زمرہ جات میں سے انتخاب کریں۔
            </p>
            <button
              onClick={() => {
                setSearchTerm('');
                setSelectedCategory('سب');
                setShowOnlyBookmarks(false);
              }}
              className="px-4 py-2 rounded-xl bg-emerald-600 text-white text-xs font-bold"
            >
              تمام دعائیں دکھائیں
            </button>
          </div>
        )}
      </div>

      {/* Footer Note */}
      <div className="bg-slate-100 dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-3xl p-6 text-center text-xs text-slate-500 dark:text-slate-400 space-y-2 leading-relaxed">
        <p className="font-bold text-slate-700 dark:text-slate-300 text-sm">
          ۱۰۰ مشہور و مستند مسنون و قرآنی دعائیں
        </p>
        <p>
          یہ تمام دعائیں مستند کتب احادیث (صحیح بخاری، صحیح مسلم، سنن ترمذی، سنن ابوداؤد، سنن ابن ماجہ) اور قرآن مجید سے مکمل عربی متن اور اعراب کے ساتھ لی گئی ہیں۔
        </p>
      </div>
    </div>
  );
};
