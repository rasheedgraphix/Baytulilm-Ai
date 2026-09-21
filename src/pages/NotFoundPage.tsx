import React from 'react';
import { Link } from 'react-router-dom';
import { BookOpen, Home, Download } from 'lucide-react';
import { DownloadButton } from '../components/common/DownloadButton';

export const NotFoundPage: React.FC = () => {
  return (
    <div className="pt-36 pb-24 max-w-2xl mx-auto px-4 text-center space-y-6">
      <div className="w-16 h-16 rounded-2xl bg-amber-100 dark:bg-amber-950 text-amber-800 dark:text-amber-300 flex items-center justify-center font-bold text-2xl mx-auto">
        404
      </div>
      <h1 className="text-3xl font-extrabold text-slate-900 dark:text-slate-100">
        Page Not Found
      </h1>
      <p className="text-slate-600 dark:text-slate-400 text-sm leading-relaxed">
        The requested page does not exist or has been moved. Use the navigation links below to return to the official Baytul Ilm AI portal.
      </p>

      <div className="pt-4 flex flex-wrap justify-center gap-4">
        <Link
          to="/"
          className="px-6 py-3 rounded-xl bg-emerald-800 hover:bg-emerald-900 text-white font-bold text-sm flex items-center gap-2"
        >
          <Home className="w-4 h-4" /> Go to Home
        </Link>
        <DownloadButton variant="outline" size="md" />
      </div>
    </div>
  );
};
