import React, { useState, useEffect, useMemo, useRef } from 'react';
import {
  Clock,
  Compass,
  MapPin,
  Search,
  Crosshair,
  Calendar,
  Sparkles,
  ChevronDown,
  Volume2,
  VolumeX,
  RefreshCw,
  Sun,
  Moon,
  Sunrise,
  Sunset,
  ArrowRight,
  Info,
  Check,
  Building2,
  Sliders
} from 'lucide-react';
import {
  POPULAR_CITIES,
  CALCULATION_METHODS,
  CityPreset,
  PrayerCalculationMethod,
  calculatePrayerTimes,
  CalculatedPrayerTimes,
  formatTimeUrdu
} from '../../utils/prayerTimes';
import { useLanguage } from '../../context/LanguageContext';

export const PrayerTimesWidget: React.FC = () => {
  const { isRtl } = useLanguage();

  // Location State (Default: Karachi, Pakistan - University of Islamic Sciences)
  const defaultCity = POPULAR_CITIES.find((c) => c.id === 'karachi') || POPULAR_CITIES[0];
  const [selectedCity, setSelectedCity] = useState<CityPreset>(defaultCity);
  const [customLocationName, setCustomLocationName] = useState<string>('');
  const [isUsingGps, setIsUsingGps] = useState<boolean>(false);
  const [gpsCoords, setGpsCoords] = useState<{ lat: number; lng: number } | null>(null);

  // Settings
  const [calculationMethod, setCalculationMethod] = useState<string>('karachi');
  const [asrJuristic, setAsrJuristic] = useState<'hanafi' | 'shafii'>('hanafi');
  const [showSettings, setShowSettings] = useState<boolean>(false);

  // Search & Selector State
  const [citySearch, setCitySearch] = useState<string>('');
  const [isCityDropdownOpen, setIsCityDropdownOpen] = useState<boolean>(false);
  const dropdownRef = useRef<HTMLDivElement>(null);

  // GPS Loading & Error
  const [gpsLoading, setGpsLoading] = useState<boolean>(false);
  const [gpsError, setGpsError] = useState<string | null>(null);

  // Live Timer
  const [currentTime, setCurrentTime] = useState<Date>(new Date());

  // Update clock every second
  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentTime(new Date());
    }, 1000);
    return () => clearInterval(timer);
  }, []);

  // Close dropdown on outside click
  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(e.target as Node)) {
        setIsCityDropdownOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  // Active Coordinates & Timezone
  const currentCoords = useMemo(() => {
    if (isUsingGps && gpsCoords) {
      // Calculate approximate timezone offset from longitude (or browser default)
      const tz = -currentTime.getTimezoneOffset() / 60;
      return {
        lat: gpsCoords.lat,
        lng: gpsCoords.lng,
        tz: tz,
        nameUrdu: customLocationName || 'آپ کا موجودہ مقام (GPS)',
        nameEnglish: customLocationName || 'Your Current Location (GPS)',
      };
    }
    return {
      lat: selectedCity.latitude,
      lng: selectedCity.longitude,
      tz: selectedCity.timezone,
      nameUrdu: selectedCity.nameUrdu,
      nameEnglish: selectedCity.nameEnglish,
    };
  }, [isUsingGps, gpsCoords, selectedCity, customLocationName, currentTime]);

  // Calculate Times
  const prayerTimes: CalculatedPrayerTimes = useMemo(() => {
    return calculatePrayerTimes(
      currentTime,
      currentCoords.lat,
      currentCoords.lng,
      currentCoords.tz,
      calculationMethod,
      asrJuristic
    );
  }, [currentTime, currentCoords, calculationMethod, asrJuristic]);

  // Handle GPS location request
  const handleGetLiveLocation = () => {
    if (!navigator.geolocation) {
      setGpsError('آپ کا براؤزر لوکیشن کو سپورٹ نہیں کرتا۔');
      return;
    }
    setGpsLoading(true);
    setGpsError(null);

    navigator.geolocation.getCurrentPosition(
      (position) => {
        const { latitude, longitude } = position.coords;
        setGpsCoords({ lat: latitude, lng: longitude });
        setIsUsingGps(true);
        setCustomLocationName('موجودہ لائیو مقام');
        setGpsLoading(false);
      },
      (error) => {
        setGpsLoading(false);
        if (error.code === error.PERMISSION_DENIED) {
          setGpsError('براہِ کرم براؤزر میں لوکیشن کی اجازت فراہم کریں، یا نیچے سے اپنا شہر منتخب کریں۔');
        } else {
          setGpsError('مقام حاصل کرنے میں دشواری پیش آئی۔ برائے مہربانی شہر تلاش کریں۔');
        }
      },
      { timeout: 10000, enableHighAccuracy: true }
    );
  };

  // Filter cities by search term
  const filteredCities = useMemo(() => {
    if (!citySearch.trim()) return POPULAR_CITIES;
    const q = citySearch.toLowerCase().trim();
    return POPULAR_CITIES.filter(
      (c) =>
        c.nameUrdu.includes(q) ||
        c.nameEnglish.toLowerCase().includes(q) ||
        c.countryUrdu.includes(q) ||
        c.countryEnglish.toLowerCase().includes(q)
    );
  }, [citySearch]);

  const prayerCards = [
    {
      key: 'fajr',
      nameUrdu: 'فجر',
      nameArabic: 'الفجر',
      nameEnglish: 'Fajr',
      timeStr: prayerTimes.fajrStr,
      icon: Sunrise,
      gradient: 'from-indigo-900/60 to-slate-900/80',
      activeColor: 'border-indigo-400 text-indigo-300 ring-2 ring-indigo-400/30',
      tag: 'سحری ختم',
      tagTime: prayerTimes.sehriEndStr,
    },
    {
      key: 'sunrise',
      nameUrdu: 'طلوعِ آفتاب',
      nameArabic: 'الشروق',
      nameEnglish: 'Sunrise',
      timeStr: prayerTimes.sunriseStr,
      icon: Sun,
      gradient: 'from-amber-900/40 to-slate-900/80',
      activeColor: 'border-amber-400 text-amber-300 ring-2 ring-amber-400/30',
      tag: 'اشراق',
      tagTime: prayerTimes.ishraqStr,
    },
    {
      key: 'dhuhr',
      nameUrdu: 'ظہر',
      nameArabic: 'الظهر',
      nameEnglish: 'Dhuhr',
      timeStr: prayerTimes.dhuhrStr,
      icon: Sun,
      gradient: 'from-amber-800/40 to-slate-900/80',
      activeColor: 'border-amber-400 text-amber-300 ring-2 ring-amber-400/30',
      tag: 'چاشت',
      tagTime: prayerTimes.chashtStr,
    },
    {
      key: 'asr',
      nameUrdu: asrJuristic === 'hanafi' ? 'عصر (حنفی)' : 'عصر (شافعی)',
      nameArabic: 'العصر',
      nameEnglish: 'Asr',
      timeStr: asrJuristic === 'hanafi' ? prayerTimes.asrStr : prayerTimes.asrShafiStr,
      icon: Sun,
      gradient: 'from-orange-900/50 to-slate-900/80',
      activeColor: 'border-orange-400 text-orange-300 ring-2 ring-orange-400/30',
      tag: asrJuristic === 'hanafi' ? 'مثلین' : 'مثل اول',
      tagTime: asrJuristic === 'hanafi' ? 'حنفی' : 'شافعی',
    },
    {
      key: 'maghrib',
      nameUrdu: 'مغرب',
      nameArabic: 'المغرب',
      nameEnglish: 'Maghrib',
      timeStr: prayerTimes.maghribStr,
      icon: Sunset,
      gradient: 'from-rose-900/50 to-slate-900/80',
      activeColor: 'border-rose-400 text-rose-300 ring-2 ring-rose-400/30',
      tag: 'افطار',
      tagTime: prayerTimes.maghribStr,
    },
    {
      key: 'isha',
      nameUrdu: 'عشاء',
      nameArabic: 'العشاء',
      nameEnglish: 'Isha',
      timeStr: prayerTimes.ishaStr,
      icon: Moon,
      gradient: 'from-emerald-950 to-slate-900/80',
      activeColor: 'border-emerald-400 text-emerald-300 ring-2 ring-emerald-400/30',
      tag: 'تہجد',
      tagTime: prayerTimes.tahajjudStr,
    },
  ];

  return (
    <div className="w-full bg-gradient-to-b from-slate-900 via-emerald-950/40 to-slate-950 text-white rounded-3xl border border-emerald-500/30 shadow-2xl p-4 sm:p-7 space-y-6 relative overflow-hidden font-urdu" dir="rtl">
      {/* Subtle Glow Background */}
      <div className="absolute -top-24 -right-24 w-80 h-80 bg-emerald-500/10 blur-[100px] rounded-full pointer-events-none" />
      <div className="absolute -bottom-24 -left-24 w-80 h-80 bg-amber-500/10 blur-[100px] rounded-full pointer-events-none" />

      {/* Top Header: Title + Location Controls + Live Clock */}
      <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-4 border-b border-white/10 pb-5 relative z-10">
        
        {/* Title & Method */}
        <div className="space-y-1">
          <div className="flex items-center gap-2 flex-wrap">
            <span className="p-1.5 rounded-xl bg-emerald-500/20 text-emerald-400 border border-emerald-500/30">
              <Clock className="w-5 h-5" />
            </span>
            <h2 className="text-xl sm:text-2xl font-black font-arabic text-transparent bg-clip-text bg-gradient-to-r from-emerald-100 via-amber-100 to-amber-300">
              اوقاتِ نماز (Prayer Times)
            </h2>
            <span className="text-[11px] px-2.5 py-0.5 rounded-full bg-emerald-900/80 text-emerald-300 border border-emerald-500/40 font-bold">
              جامعہ علوم اسلامیہ بنوری ٹاؤن کراچی
            </span>
          </div>

          <p className="text-xs text-slate-300">
            مستند و دقیق حسابی اوقات برائے کراچی و تمام بلادِ اسلامیہ مع لائیو لوکیشن اور شہر کے نام سے تلاش
          </p>
        </div>

        {/* Live Clock & Next Prayer Badge */}
        <div className="flex items-center gap-3 self-stretch sm:self-auto justify-between sm:justify-end bg-slate-950/70 p-2.5 sm:px-4 sm:py-2 rounded-2xl border border-emerald-500/30">
          <div className="text-right">
            <div className="text-[10px] text-emerald-400 font-bold">اگلی نماز: {prayerTimes.nextPrayer.nameUrdu}</div>
            <div className="text-xs text-amber-200 font-mono">
              باقی: {prayerTimes.nextPrayer.remainingHours} گھنٹے {prayerTimes.nextPrayer.remainingMinutes} منٹ
            </div>
          </div>
          <div className="h-7 w-[1px] bg-white/20" />
          <div className="text-left font-mono">
            <div className="text-base sm:text-lg font-black text-emerald-300">
              {currentTime.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: true })}
            </div>
            <div className="text-[10px] text-slate-400">
              {currentTime.toLocaleDateString('ur-PK', { weekday: 'short', day: 'numeric', month: 'short', year: 'numeric' })}
            </div>
          </div>
        </div>
      </div>

      {/* Location Selector Bar */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-3 relative z-20">
        
        {/* City Selector with Search Dropdown */}
        <div className="lg:col-span-6 relative" ref={dropdownRef}>
          <div className="flex items-center gap-2">
            <button
              onClick={() => setIsCityDropdownOpen(!isCityDropdownOpen)}
              className="flex-1 flex items-center justify-between gap-2 px-4 py-3 rounded-2xl bg-slate-900/90 hover:bg-slate-850 border border-emerald-500/40 text-white text-sm font-bold transition-all shadow-inner"
            >
              <div className="flex items-center gap-2 min-w-0">
                <Building2 className="w-4 h-4 text-emerald-400 shrink-0" />
                <span className="truncate">
                  {currentCoords.nameUrdu} ({isUsingGps ? 'GPS' : selectedCity.countryUrdu})
                </span>
              </div>
              <ChevronDown className={`w-4 h-4 text-emerald-400 transition-transform ${isCityDropdownOpen ? 'rotate-180' : ''}`} />
            </button>

            {/* GPS Auto Detect Button */}
            <button
              onClick={handleGetLiveLocation}
              disabled={gpsLoading}
              className={`px-3.5 py-3 rounded-2xl font-bold text-xs flex items-center gap-1.5 transition-all shrink-0 ${
                isUsingGps
                  ? 'bg-emerald-600 text-white border border-emerald-400 shadow-lg shadow-emerald-900/40'
                  : 'bg-slate-800 hover:bg-slate-700 text-emerald-300 border border-emerald-500/30'
              }`}
              title="اپنے موجودہ مقام کے مطابق لائیو اوقات حاصل کریں"
            >
              {gpsLoading ? (
                <RefreshCw className="w-4 h-4 animate-spin text-white" />
              ) : (
                <Crosshair className="w-4 h-4 text-amber-400" />
              )}
              <span className="hidden sm:inline">لائیو لوکیشن (GPS)</span>
            </button>
          </div>

          {/* City Dropdown Menu */}
          {isCityDropdownOpen && (
            <div className="absolute top-full right-0 left-0 mt-2 bg-slate-900/95 backdrop-blur-xl border border-emerald-500/40 rounded-2xl shadow-2xl p-3 z-50 space-y-2 max-h-80 overflow-hidden flex flex-col">
              {/* Search input */}
              <div className="relative">
                <Search className="w-4 h-4 absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none" />
                <input
                  type="text"
                  value={citySearch}
                  onChange={(e) => setCitySearch(e.target.value)}
                  placeholder="شہر یا ملک کا نام تلاش کریں (مثلاً کراچی، لاہور، اسلام آباد، مکہ، لندن)..."
                  className="w-full pl-3 pr-9 py-2 rounded-xl bg-slate-950 border border-slate-700 text-xs text-white focus:outline-none focus:border-emerald-500 text-right"
                  autoFocus
                />
              </div>

              {/* City List */}
              <div className="overflow-y-auto space-y-1 flex-1 pr-1 scrollbar-thin scrollbar-thumb-emerald-700">
                {filteredCities.map((city) => {
                  const isSelected = !isUsingGps && selectedCity.id === city.id;
                  return (
                    <button
                      key={city.id}
                      onClick={() => {
                        setSelectedCity(city);
                        setIsUsingGps(false);
                        setIsCityDropdownOpen(false);
                        setCitySearch('');
                      }}
                      className={`w-full flex items-center justify-between px-3 py-2 rounded-xl text-xs font-urdu transition-all text-right ${
                        isSelected
                          ? 'bg-emerald-600/30 text-emerald-300 border border-emerald-500/50 font-bold'
                          : 'hover:bg-slate-800/80 text-slate-200'
                      }`}
                    >
                      <div className="flex items-center gap-2">
                        <MapPin className={`w-3.5 h-3.5 ${isSelected ? 'text-amber-400' : 'text-slate-400'}`} />
                        <span>{city.nameUrdu}</span>
                        <span className="text-[10px] text-slate-400 font-sans">({city.nameEnglish})</span>
                      </div>
                      <span className="text-[10px] text-slate-400">{city.countryUrdu}</span>
                    </button>
                  );
                })}
                {filteredCities.length === 0 && (
                  <div className="py-4 text-center text-xs text-slate-400">
                    کوئی شہر نہیں ملا۔ برائے مہربانی املا چیک کریں۔
                  </div>
                )}
              </div>
            </div>
          )}
        </div>

        {/* Quick City Buttons (Karachi, Lahore, Islamabad, Makkah, Madinah) */}
        <div className="lg:col-span-6 flex items-center gap-1.5 overflow-x-auto pb-1 scrollbar-none">
          <span className="text-xs text-slate-400 shrink-0 hidden sm:inline">مشہور شہر:</span>
          {['karachi', 'lahore', 'islamabad', 'peshawar', 'quetta', 'makkah', 'madinah'].map((cityId) => {
            const city = POPULAR_CITIES.find((c) => c.id === cityId);
            if (!city) return null;
            const isSelected = !isUsingGps && selectedCity.id === city.id;
            return (
              <button
                key={city.id}
                onClick={() => {
                  setSelectedCity(city);
                  setIsUsingGps(false);
                }}
                className={`px-3 py-1.5 rounded-xl text-xs font-urdu whitespace-nowrap transition-all border ${
                  isSelected
                    ? 'bg-emerald-600 text-white border-emerald-400 shadow-md shadow-emerald-950 font-bold'
                    : 'bg-slate-900/80 hover:bg-slate-800 text-slate-300 border-white/10'
                }`}
              >
                {city.nameUrdu}
              </button>
            );
          })}
        </div>
      </div>

      {/* GPS Error Notification */}
      {gpsError && (
        <div className="p-3 rounded-2xl bg-rose-950/80 border border-rose-500/40 text-rose-200 text-xs flex items-center justify-between gap-2">
          <span>{gpsError}</span>
          <button onClick={() => setGpsError(null)} className="text-rose-400 hover:text-white font-bold">✕</button>
        </div>
      )}

      {/* Main 6 Prayer Cards Grid */}
      <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-3 sm:gap-4">
        {prayerCards.map((card) => {
          const isNext = prayerTimes.nextPrayer.key === card.key;
          const IconComponent = card.icon;
          return (
            <div
              key={card.key}
              className={`relative rounded-2xl p-4 transition-all duration-300 flex flex-col justify-between space-y-3 bg-gradient-to-b ${card.gradient} border ${
                isNext
                  ? 'border-amber-400 ring-2 ring-amber-400/40 shadow-lg shadow-amber-950/50 scale-[1.02]'
                  : 'border-white/10 hover:border-white/20'
              }`}
            >
              {/* Next Prayer Floating Indicator */}
              {isNext && (
                <div className="absolute -top-2.5 left-1/2 -translate-x-1/2 px-2.5 py-0.5 rounded-full bg-gradient-to-r from-amber-500 to-amber-600 text-slate-950 font-black text-[9px] shadow uppercase tracking-wider flex items-center gap-1">
                  <Sparkles className="w-2.5 h-2.5" />
                  <span>اگلی نماز</span>
                </div>
              )}

              {/* Card Header */}
              <div className="flex items-center justify-between">
                <div className="text-right">
                  <div className="text-sm sm:text-base font-black font-arabic text-white">{card.nameUrdu}</div>
                  <div className="text-[10px] text-slate-400">{card.nameEnglish}</div>
                </div>
                <div className={`p-2 rounded-xl bg-white/10 ${isNext ? 'text-amber-300' : 'text-slate-300'}`}>
                  <IconComponent className="w-4 h-4" />
                </div>
              </div>

              {/* Prayer Time Display */}
              <div className="text-center py-1 bg-black/20 rounded-xl border border-white/5">
                <div className="text-base sm:text-lg lg:text-xl font-black text-white font-mono tracking-tight">
                  {card.timeStr}
                </div>
              </div>

              {/* Bottom Extra Info Tag */}
              <div className="flex items-center justify-between text-[10px] text-slate-300 border-t border-white/10 pt-2">
                <span className="text-slate-400">{card.tag}:</span>
                <span className="font-bold text-amber-200 font-mono">{card.tagTime}</span>
              </div>
            </div>
          );
        })}
      </div>

      {/* Auxiliary Timings (Sehri, Ishraq, Chasht, Zawal, Tahajjud) + Method Selector */}
      <div className="bg-slate-950/80 rounded-2xl p-4 border border-white/10 space-y-4">
        
        {/* Auxiliary Islamic Timings Bar */}
        <div className="flex items-center justify-between gap-2 flex-wrap border-b border-white/10 pb-3">
          <div className="flex items-center gap-1.5 text-xs text-amber-300 font-bold">
            <Sparkles className="w-4 h-4 text-amber-400" />
            <span>دیگر مسنون اوقات:</span>
          </div>

          <div className="flex items-center gap-3 sm:gap-6 flex-wrap text-xs text-slate-300">
            <div className="flex items-center gap-1.5">
              <span className="text-slate-400">سحری کا آخری وقت:</span>
              <span className="font-mono font-bold text-emerald-300">{prayerTimes.sehriEndStr}</span>
            </div>
            <div className="flex items-center gap-1.5">
              <span className="text-slate-400">نمازِ اشراق:</span>
              <span className="font-mono font-bold text-amber-300">{prayerTimes.ishraqStr}</span>
            </div>
            <div className="flex items-center gap-1.5">
              <span className="text-slate-400">نمازِ چاشت (ضحیٰ):</span>
              <span className="font-mono font-bold text-amber-300">{prayerTimes.chashtStr}</span>
            </div>
            <div className="flex items-center gap-1.5">
              <span className="text-slate-400">افطار / مغرب:</span>
              <span className="font-mono font-bold text-rose-300">{prayerTimes.maghribStr}</span>
            </div>
            <div className="flex items-center gap-1.5">
              <span className="text-slate-400">نمازِ تہجد:</span>
              <span className="font-mono font-bold text-purple-300">{prayerTimes.tahajjudStr}</span>
            </div>
          </div>
        </div>

        {/* Juristic & Method Quick Switch */}
        <div className="flex items-center justify-between gap-3 flex-wrap text-xs">
          
          {/* Asr Juristic Method Switch (Hanafi / Shafi'i) */}
          <div className="flex items-center gap-2">
            <span className="text-slate-400">مسلکِ عصر:</span>
            <div className="inline-flex bg-slate-900 rounded-xl p-0.5 border border-white/10">
              <button
                onClick={() => setAsrJuristic('hanafi')}
                className={`px-2.5 py-1 rounded-lg text-xs font-bold transition-all ${
                  asrJuristic === 'hanafi'
                    ? 'bg-emerald-600 text-white shadow'
                    : 'text-slate-400 hover:text-white'
                }`}
              >
                حنفی (مثلین - مستند برصغیر)
              </button>
              <button
                onClick={() => setAsrJuristic('shafii')}
                className={`px-2.5 py-1 rounded-lg text-xs font-bold transition-all ${
                  asrJuristic === 'shafii'
                    ? 'bg-emerald-600 text-white shadow'
                    : 'text-slate-400 hover:text-white'
                }`}
              >
                شافعی / مالکی / حنبلی (مثل اول)
              </button>
            </div>
          </div>

          {/* Toggle Calculation Method Settings */}
          <button
            onClick={() => setShowSettings(!showSettings)}
            className="flex items-center gap-1.5 text-emerald-400 hover:text-emerald-300 transition-colors"
          >
            <Sliders className="w-3.5 h-3.5" />
            <span>حسابی طریقہ کار تبدیل کریں ({CALCULATION_METHODS[calculationMethod]?.nameEnglish || 'Karachi'})</span>
          </button>
        </div>

        {/* Expandable Advanced Method Settings */}
        {showSettings && (
          <div className="pt-3 border-t border-white/10 space-y-2">
            <div className="text-xs text-slate-300 font-bold">حسابی قاعدہ (Calculation Method):</div>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-2">
              {(Object.values(CALCULATION_METHODS) as PrayerCalculationMethod[]).map((m) => {
                const isSelected = calculationMethod === m.id;
                return (
                  <button
                    key={m.id}
                    onClick={() => setCalculationMethod(m.id)}
                    className={`p-2.5 rounded-xl border text-right text-xs transition-all flex items-start gap-2 ${
                      isSelected
                        ? 'bg-emerald-900/60 border-emerald-400 text-emerald-200 font-bold'
                        : 'bg-slate-900/60 border-white/10 text-slate-300 hover:bg-slate-900'
                    }`}
                  >
                    <Check className={`w-4 h-4 mt-0.5 shrink-0 ${isSelected ? 'text-emerald-400' : 'opacity-0'}`} />
                    <div>
                      <div>{m.nameUrdu}</div>
                      <div className="text-[10px] text-slate-400 font-sans">{m.nameEnglish}</div>
                    </div>
                  </button>
                );
              })}
            </div>
          </div>
        )}
      </div>

    </div>
  );
};
