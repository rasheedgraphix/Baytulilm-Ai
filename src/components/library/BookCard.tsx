import React, { useState } from 'react';
import { BookOpen, Download, Copy, Check, Share2, Sparkles, Eye } from 'lucide-react';
import { DarsNizamiBookItem } from '../../types';
import { IslamicBookCover } from './IslamicBookCover';
import { openPdfReader, downloadDirectPdf } from '../../utils/pdfDownloader';

interface BookCardProps {
  book: DarsNizamiBookItem;
  onSelectForView?: (book: DarsNizamiBookItem) => void;
}

export const BookCard: React.FC<BookCardProps> = ({ book, onSelectForView }) => {
  const [copied, setCopied] = useState(false);

  const handleReadClick = () => {
    if (onSelectForView) {
      onSelectForView(book);
    } else {
      openPdfReader(book);
    }
  };

  const handleDownloadClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    downloadDirectPdf(book.pdfUrl, book.nameUrdu || book.name);
  };

  const handleCopyLink = async (e: React.MouseEvent) => {
    e.stopPropagation();
    try {
      await navigator.clipboard.writeText(book.pdfUrl);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    } catch {
      // Fallback
    }
  };

  const handleShare = async (e: React.MouseEvent) => {
    e.stopPropagation();
    if (navigator.share) {
      try {
        await navigator.share({
          title: book.nameUrdu || book.name,
          text: `${book.nameUrdu} - درسِ نظامی کی درسی کتاب مع شروحات | بیت العلم AI`,
          url: book.pdfUrl,
        });
      } catch {
        // User cancelled or error
      }
    } else {
      handleCopyLink(e);
    }
  };

  // Color coding by type
  const getTypeBadgeClass = (type: string) => {
    if (type.includes('Main') || type.includes('اصل')) {
      return 'bg-emerald-100 dark:bg-emerald-950/80 text-emerald-900 dark:text-emerald-300 border-emerald-300 dark:border-emerald-800';
    }
    if (type.includes('Sharh') || type.includes('شرح')) {
      return 'bg-indigo-100 dark:bg-indigo-950/80 text-indigo-900 dark:text-indigo-300 border-indigo-300 dark:border-indigo-800';
    }
    if (type.includes('Translation') || type.includes('ترجمہ')) {
      return 'bg-amber-100 dark:bg-amber-950/80 text-amber-900 dark:text-amber-300 border-amber-300 dark:border-amber-800';
    }
    if (type.includes('Darsi') || type.includes('تقریر')) {
      return 'bg-purple-100 dark:bg-purple-950/80 text-purple-900 dark:text-purple-300 border-purple-300 dark:border-purple-800';
    }
    return 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 border-slate-300 dark:border-slate-700';
  };

  return (
    <div className="group relative bg-white dark:bg-slate-900/90 border border-slate-200/90 dark:border-slate-800/90 rounded-2xl p-3.5 sm:p-4 transition-all duration-200 hover:shadow-xl hover:border-emerald-500/50 dark:hover:border-emerald-500/40 flex flex-col justify-between">
      {/* Book Cover and Information Layout */}
      <div className="flex items-start gap-3.5 sm:gap-4">
        {/* Interactive 3D Book Cover */}
        <div
          onClick={handleReadClick}
          className="relative shrink-0 cursor-pointer group/cover"
          title={`${book.nameUrdu || book.name} کا مطالعہ کریں`}
        >
          <IslamicBookCover
            book={book}
            bookId={book.id}
            title={book.name}
            titleUrdu={book.nameUrdu}
            author={book.author}
            category={book.category}
            classNameUrdu={book.classNameUrdu}
            typeUrdu={book.typeUrdu || book.type}
            pdfUrl={book.pdfUrl}
            coverUrl={book.coverUrl}
            size="md"
          />

          {/* Quick Hover Read Indicator */}
          <div className="absolute inset-0 z-40 bg-black/40 opacity-0 group-hover/cover:opacity-100 transition-opacity rounded-r-lg rounded-l-[3px] flex items-center justify-center pointer-events-none backdrop-blur-[1px]">
            <span className="inline-flex items-center gap-1 px-2 py-1 rounded-full text-[10px] font-bold bg-amber-400 text-slate-950 shadow-md transform scale-95 group-hover/cover:scale-100 transition-transform font-urdu">
              <Eye className="w-3 h-3" />
              <span>مطالعہ</span>
            </span>
          </div>
        </div>

        {/* Book Details */}
        <div className="flex-1 min-w-0 flex flex-col justify-between h-full">
          <div>
            {/* Top badges */}
            <div className="flex items-center gap-1.5 flex-wrap mb-2">
              <span className="px-2 py-0.5 rounded-full text-[10px] font-bold bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 border border-slate-200 dark:border-slate-700">
                {book.classNameUrdu}
              </span>
              <span className={`px-2 py-0.5 rounded-full text-[10px] font-semibold border ${getTypeBadgeClass(book.type)}`}>
                {book.typeUrdu || book.type}
              </span>
              <span className="px-2 py-0.5 rounded-full text-[10px] font-semibold bg-emerald-50 dark:bg-emerald-950/60 text-emerald-800 dark:text-emerald-300 border border-emerald-200/60 dark:border-emerald-900/60">
                {book.category}
              </span>
            </div>

            {/* Urdu Book Title */}
            <h3
              onClick={handleReadClick}
              className="text-base sm:text-lg font-extrabold text-slate-900 dark:text-slate-100 leading-snug font-urdu tracking-tight group-hover:text-emerald-700 dark:group-hover:text-emerald-400 transition-colors text-right cursor-pointer line-clamp-2"
              dir="rtl"
              title={book.nameUrdu}
            >
              {book.nameUrdu}
            </h3>

            {/* English/Roman subtitle */}
            <p className="text-xs text-slate-500 dark:text-slate-400 mt-1 line-clamp-2 leading-relaxed">
              {book.name}
            </p>

            {/* Edition badge if available */}
            {book.edition && (
              <div className="mt-1.5 flex items-center gap-1 text-[11px] text-amber-700 dark:text-amber-400 font-medium font-urdu">
                <Sparkles className="w-3 h-3 text-amber-500 shrink-0" />
                <span className="truncate">{book.edition}</span>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Action Buttons */}
      <div className="mt-3.5 pt-3 border-t border-slate-100 dark:border-slate-800/80 flex items-center justify-between gap-2">
        <div className="flex items-center gap-1.5">
          {/* Read Button */}
          <button
            onClick={handleReadClick}
            className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-xl text-xs font-bold bg-emerald-700 hover:bg-emerald-800 text-white transition-colors shadow-sm font-urdu cursor-pointer"
            title="آن لائن مطالعہ کریں"
          >
            <BookOpen className="w-3.5 h-3.5" />
            <span>مطالعہ کریں</span>
          </button>

          {/* Direct Download Button */}
          <button
            onClick={handleDownloadClick}
            className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-xl text-xs font-bold bg-amber-400 hover:bg-amber-300 text-slate-950 transition-colors shadow-sm font-urdu cursor-pointer"
            title="PDF فائل فوری ڈاؤن لوڈ کریں"
          >
            <Download className="w-3.5 h-3.5 text-slate-950" />
            <span>ڈاؤن لوڈ</span>
          </button>
        </div>

        <div className="flex items-center gap-1">
          <button
            onClick={handleCopyLink}
            className="p-1.5 rounded-lg text-slate-400 hover:text-slate-700 dark:hover:text-slate-200 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors cursor-pointer"
            title={copied ? 'لنک کاپی ہو گیا!' : 'PDF لنک کاپی کریں'}
            aria-label="Copy PDF link"
          >
            {copied ? <Check className="w-4 h-4 text-emerald-600" /> : <Copy className="w-4 h-4" />}
          </button>
          <button
            onClick={handleShare}
            className="p-1.5 rounded-lg text-slate-400 hover:text-slate-700 dark:hover:text-slate-200 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors cursor-pointer"
            title="شیئر کریں"
            aria-label="Share book"
          >
            <Share2 className="w-4 h-4" />
          </button>
        </div>
      </div>
    </div>
  );
};
