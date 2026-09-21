import React, { useEffect, useRef } from 'react';

interface AdsterraNativeProps {
  className?: string;
}

export const AdsterraNative: React.FC<AdsterraNativeProps> = ({ className = '' }) => {
  const containerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const parent = containerRef.current;
    if (!parent) return;

    // Clear previous
    parent.innerHTML = '';

    const adDiv = document.createElement('div');
    adDiv.id = 'container-54183d7fa8f6c551a4dcd08bb59bfe8e';
    parent.appendChild(adDiv);

    const script = document.createElement('script');
    script.async = true;
    script.setAttribute('data-cfasync', 'false');
    script.src = 'https://pl31439613.profitableratecpmnetwork.com/54183d7fa8f6c551a4dcd08bb59bfe8e/invoke.js';

    parent.appendChild(script);

    return () => {
      if (parent) {
        parent.innerHTML = '';
      }
    };
  }, []);

  return (
    <div className={`w-full flex flex-col items-center justify-center my-6 overflow-hidden ${className}`}>
      <span className="text-[10px] uppercase tracking-wider text-slate-400 dark:text-slate-500 mb-1 font-mono">
        Partner Recommendations
      </span>
      <div 
        ref={containerRef} 
        className="w-full min-h-[120px] rounded-xl bg-slate-100/50 dark:bg-slate-900/50 border border-slate-200/60 dark:border-slate-800/60 p-2"
      />
    </div>
  );
};
