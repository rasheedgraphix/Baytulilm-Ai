import React, { useState } from 'react';
import logo192 from '../../assets/logo-192.png';
import logoFull from '../../assets/logo.jpg';

interface BrandLogoProps {
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl' | '2xl' | '3xl' | 'nav';
  className?: string;
  withGlow?: boolean;
  rounded?: boolean;
}

const sizeMap = {
  xs: 'w-6 h-6',
  sm: 'w-8 h-8',
  md: 'w-10 h-10',
  lg: 'w-14 h-14',
  xl: 'w-20 h-20',
  '2xl': 'w-28 h-28',
  '3xl': 'w-36 h-36',
  nav: 'w-7 h-7 sm:w-9 sm:h-9',
};

// Stable sources without spaces or dynamic timestamps
const LOGO_SOURCES = [
  logo192,
  '/logo-192.png',
  logoFull,
  '/app-logo.jpg',
  '/app-logo.png',
  '/logo.jpg',
];

export const BrandLogo: React.FC<BrandLogoProps> = ({
  size = 'md',
  className = '',
  withGlow = false,
  rounded = true,
}) => {
  const [imgIndex, setImgIndex] = useState(0);

  const handleImgError = () => {
    if (imgIndex < LOGO_SOURCES.length - 1) {
      setImgIndex((prev) => prev + 1);
    }
  };

  const isExhausted = imgIndex >= LOGO_SOURCES.length;

  return (
    <div
      className={`relative inline-flex items-center justify-center shrink-0 aspect-square ${sizeMap[size]} ${className}`}
    >
      {withGlow && (
        <div className="absolute inset-0 bg-[#d4af37]/30 rounded-2xl blur-lg -z-10 animate-pulse" />
      )}

      {!isExhausted ? (
        <img
          src={LOGO_SOURCES[imgIndex]}
          srcSet={imgIndex === 0 ? `${logo192} 1x, ${logoFull} 2x` : undefined}
          alt="Baytul Ilm AI Logo"
          referrerPolicy="no-referrer"
          decoding="async"
          loading="eager"
          onError={handleImgError}
          className={`w-full h-full object-cover select-none shadow-md ${
            rounded ? 'rounded-2xl sm:rounded-3xl border border-[#d4af37]/40' : ''
          }`}
        />
      ) : (
        <svg
          viewBox="0 0 128 128"
          className="w-full h-full drop-shadow-md select-none rounded-2xl border border-[#d4af37]/40"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          aria-label="Baytul Ilm AI Official Logo"
          role="img"
        >
          <defs>
            <linearGradient id="blBgGrad" x1="0%" y1="0%" x2="100%" y2="100%">
              <stop offset="0%" stopColor="#064e3b" />
              <stop offset="50%" stopColor="#042f2e" />
              <stop offset="100%" stopColor="#021c1b" />
            </linearGradient>
            <linearGradient id="blGoldGrad" x1="0%" y1="0%" x2="100%" y2="100%">
              <stop offset="0%" stopColor="#fef08a" />
              <stop offset="35%" stopColor="#fbbf24" />
              <stop offset="70%" stopColor="#d97706" />
              <stop offset="100%" stopColor="#b45309" />
            </linearGradient>
          </defs>
          <rect x="4" y="4" width="120" height="120" rx="28" fill="url(#blBgGrad)" stroke="#10b981" strokeWidth="2" strokeOpacity="0.4" />
          <rect x="8" y="8" width="112" height="112" rx="24" fill="none" stroke="url(#blGoldGrad)" strokeWidth="1.5" strokeOpacity="0.6" />
          <path d="M64 20 C52 32, 28 48, 28 82 C28 98, 42 106, 64 106 C86 106, 100 98, 100 82 C100 48, 76 32, 64 20 Z" fill="#065f46" fillOpacity="0.4" stroke="url(#blGoldGrad)" strokeWidth="1.5" />
          <text x="64" y="100" textAnchor="middle" fill="url(#blGoldGrad)" fontSize="9" fontWeight="bold" fontFamily="serif" letterSpacing="0.08em">
            BAYTUL ILM AI
          </text>
        </svg>
      )}
    </div>
  );
};
