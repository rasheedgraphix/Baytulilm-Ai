import React from 'react';

interface IslamicPatternBgProps {
  className?: string;
  variant?: 'subtle' | 'hero' | 'accent' | 'gold';
}

export const IslamicPatternBg: React.FC<IslamicPatternBgProps> = ({
  className = '',
  variant = 'subtle'
}) => {
  const opacityClass =
    variant === 'hero'
      ? 'opacity-15'
      : variant === 'accent'
      ? 'opacity-20'
      : variant === 'gold'
      ? 'opacity-25'
      : 'opacity-10';

  return (
    <div
      className={`absolute inset-0 pointer-events-none overflow-hidden ${className}`}
      aria-hidden="true"
    >
      <svg
        className={`w-full h-full ${variant === 'gold' ? 'text-amber-400' : 'text-emerald-400/80'} ${opacityClass}`}
        xmlns="http://www.w3.org/2000/svg"
        width="100%"
        height="100%"
      >
        <defs>
          <pattern
            id="islamic-star-pattern"
            width="96"
            height="96"
            patternUnits="userSpaceOnUse"
          >
            {/* 8-pointed star and ornate geometric lattice motif */}
            <path
              d="M48 0 L58 28 L86 18 L76 48 L96 68 L68 68 L48 96 L28 68 L0 68 L20 48 L10 18 L38 28 Z"
              fill="none"
              stroke="currentColor"
              strokeWidth="1.2"
            />
            <path
              d="M48 14 L55 33 L76 25 L69 48 L83 62 L60 62 L48 82 L36 62 L13 62 L27 48 L20 25 L41 33 Z"
              fill="none"
              stroke="currentColor"
              strokeWidth="0.6"
              strokeDasharray="2,2"
            />
            {/* Central Octagon & Rosette */}
            <circle cx="48" cy="48" r="8" fill="none" stroke="currentColor" strokeWidth="0.8" />
            <circle cx="48" cy="48" r="3" fill="currentColor" opacity="0.4" />
            
            {/* Corner Stars */}
            <circle cx="0" cy="0" r="10" fill="none" stroke="currentColor" strokeWidth="0.6" />
            <circle cx="96" cy="0" r="10" fill="none" stroke="currentColor" strokeWidth="0.6" />
            <circle cx="0" cy="96" r="10" fill="none" stroke="currentColor" strokeWidth="0.6" />
            <circle cx="96" cy="96" r="10" fill="none" stroke="currentColor" strokeWidth="0.6" />
            
            {/* Diagonal interconnecting lines */}
            <line x1="0" y1="0" x2="96" y2="96" stroke="currentColor" strokeWidth="0.4" opacity="0.3" />
            <line x1="96" y1="0" x2="0" y2="96" stroke="currentColor" strokeWidth="0.4" opacity="0.3" />
          </pattern>
        </defs>
        <rect width="100%" height="100%" fill="url(#islamic-star-pattern)" />
      </svg>
    </div>
  );
};
