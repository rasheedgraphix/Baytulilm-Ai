import React, { useEffect, useRef } from 'react';

interface AdsterraBannerProps {
  className?: string;
}

export const AdsterraBanner: React.FC<AdsterraBannerProps> = ({ className = '' }) => {
  const adContainerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const container = adContainerRef.current;
    if (!container) return;

    // Clear previous if any
    container.innerHTML = '';

    const iframe = document.createElement('iframe');
    iframe.width = '728';
    iframe.height = '90';
    iframe.style.border = 'none';
    iframe.style.overflow = 'hidden';
    iframe.scrolling = 'no';
    iframe.title = 'Advertisement';

    container.appendChild(iframe);

    const iframeDoc = iframe.contentWindow?.document || iframe.contentDocument;
    if (iframeDoc) {
      iframeDoc.open();
      iframeDoc.write(`
        <!DOCTYPE html>
        <html>
          <head>
            <style>body { margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; background: transparent; }</style>
          </head>
          <body>
            <script type="text/javascript">
              atOptions = {
                'key' : 'aa6b9666ef280f5a94b03deab3915b35',
                'format' : 'iframe',
                'height' : 90,
                'width' : 728,
                'params' : {}
              };
            </script>
            <script type="text/javascript" src="//www.topcreativeformat.com/aa6b9666ef280f5a94b03deab3915b35/invoke.js"></script>
          </body>
        </html>
      `);
      iframeDoc.close();
    }
  }, []);

  return (
    <div className={`w-full flex flex-col items-center justify-center my-6 overflow-hidden ${className}`}>
      <span className="text-[10px] uppercase tracking-wider text-slate-400 dark:text-slate-500 mb-1 font-mono">
        Sponsored
      </span>
      <div 
        ref={adContainerRef} 
        className="w-full max-w-[728px] min-h-[90px] flex items-center justify-center bg-slate-100/50 dark:bg-slate-900/50 rounded-lg overflow-hidden border border-slate-200/60 dark:border-slate-800/60"
      />
    </div>
  );
};
