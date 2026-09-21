import React from 'react';
import { X, ExternalLink, Download, AlertCircle, BookOpen } from 'lucide-react';
import { DarsNizamiBookItem } from '../../types';
import { IslamicBookCover } from './IslamicBookCover';
import { downloadDirectPdf, openPdfReader } from '../../utils/pdfDownloader';

interface PdfModalProps {
  book: DarsNizamiBookItem | null;
  onClose: () => void;
}

export const PdfModal: React.FC<PdfModalProps> = ({ book, onClose }) => {
  if (!book) return null;

  const pdfUrl = book.pdfUrl;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-2 sm:p-4 md:p-6 bg-slate-950/85 backdrop-blur-sm animate-fade-in select-none">
      <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl w-full max-w-5xl h-[92vh] flex flex-col shadow-2xl overflow-hidden">
        {/* Header */}
        <div className="px-4 py-3 sm:px-6 sm:py-3.5 border-b border-slate-200 dark:border-slate-800 flex items-center justify-between gap-3 bg-slate-50/90 dark:bg-slate-950/70">
          <div className="min-w-0 flex-1 flex items-center gap-3">
            {/* Miniature Book Cover in Modal Header */}
            <div className="hidden sm:block shrink-0 shadow-md">
              <IslamicBookCover
                book={book}
                bookId={book.id}
                title={book.name}
                titleUrdu={book.nameUrdu}
                pdfUrl={book.pdfUrl}
                author={book.author}
                category={book.category}
                classNameUrdu={book.classNameUrdu}
                typeUrdu={book.typeUrdu || book.type}
                size="sm"
              />
            </div>

            <div className="min-w-0 flex-1">
              <div className="flex items-center gap-2 flex-wrap">
                <span className="px-2 py-0.5 rounded text-[10px] font-bold bg-emerald-100 dark:bg-emerald-950 text-emerald-800 dark:text-emerald-300 border border-emerald-300 dark:border-emerald-800">
                  {book.classNameUrdu}
                </span>
                <span className="px-2 py-0.5 rounded text-[10px] font-semibold bg-amber-100 dark:bg-amber-950 text-amber-900 dark:text-amber-300">
                  {book.category}
                </span>
                <span className="px-2 py-0.5 rounded text-[10px] font-medium bg-slate-200 dark:bg-slate-800 text-slate-700 dark:text-slate-300">
                  {book.typeUrdu || book.type}
                </span>
              </div>
              <h3 className="text-base sm:text-lg font-bold text-slate-900 dark:text-slate-100 truncate mt-1 font-urdu" dir="rtl">
                {book.nameUrdu || book.name}
              </h3>
              <p className="text-xs text-slate-500 dark:text-slate-400 truncate">
                {book.name}
              </p>
            </div>
          </div>

          <div className="flex items-center gap-2 shrink-0">
            {/* Direct Instant Download */}
            <button
              onClick={() => downloadDirectPdf(book.pdfUrl, book.nameUrdu || book.name)}
              className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-bold bg-amber-400 hover:bg-amber-300 text-slate-950 transition-colors shadow-sm font-urdu cursor-pointer"
              title="فائل فوری ڈاؤن لوڈ کریں"
            >
              <Download className="w-3.5 h-3.5 text-slate-950" />
              <span>ڈاؤن لوڈ کریں</span>
            </button>

            {/* Dedicated Page Reader in New Tab */}
            <button
              onClick={() => openPdfReader(book)}
              className="hidden sm:inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-semibold bg-emerald-700 hover:bg-emerald-800 text-white transition-colors shadow-sm font-urdu cursor-pointer"
              title="فل پیج ریڈر پر کھولیں"
            >
              <BookOpen className="w-3.5 h-3.5" />
              <span>نئے پیج پر پڑھیں</span>
            </button>

            {/* Direct Raw Window */}
            <a
              href={pdfUrl}
              target="_blank"
              rel="noopener noreferrer"
              className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-semibold bg-slate-100 dark:bg-slate-800 hover:bg-slate-200 dark:hover:bg-slate-700 text-slate-700 dark:text-slate-200 transition-colors font-urdu cursor-pointer"
              title="نئی ونڈو میں براہِ راست کھولیں"
            >
              <ExternalLink className="w-3.5 h-3.5" />
              <span className="hidden sm:inline">نئی ونڈو</span>
            </a>

            <button
              onClick={onClose}
              className="p-1.5 rounded-lg text-slate-500 hover:text-slate-900 dark:text-slate-400 dark:hover:text-slate-100 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors cursor-pointer"
              aria-label="بند کریں"
            >
              <X className="w-5 h-5" />
            </button>
          </div>
        </div>

        {/* Notice for PDF viewer */}
        <div className="bg-amber-50 dark:bg-amber-950/40 px-4 py-2 border-b border-amber-200/60 dark:border-amber-900/40 text-[12px] text-amber-800 dark:text-amber-300 flex items-center justify-between gap-2 font-urdu">
          <div className="flex items-center gap-2">
            <AlertCircle className="w-4 h-4 shrink-0 text-amber-600 dark:text-amber-400" />
            <span>براہِ راست پڑھنے کے لیے "نئے پیج پر پڑھیں" یا بغیر کسی انتظار کے "ڈاؤن لوڈ کریں" پر کلک کریں۔</span>
          </div>
          <button
            onClick={() => openPdfReader(book)}
            className="underline font-bold shrink-0 hover:text-amber-900 dark:hover:text-amber-200 cursor-pointer"
          >
            فل اسکرین ریڈر پیج کھولیں ↗
          </button>
        </div>

        {/* PDF Frame */}
        <div className="flex-1 bg-slate-100 dark:bg-slate-950 relative overflow-hidden">
          <iframe
            src={`${pdfUrl}#toolbar=1&navpanes=0`}
            title={book.name}
            className="w-full h-full border-0"
            loading="lazy"
          />
        </div>
      </div>
    </div>
  );
};
