/**
 * Accurate Prayer Times Calculation Engine
 * 
 * Supports:
 * - University of Islamic Sciences, Karachi (جامعہ علوم اسلامیہ بنوری ٹاؤن، کراچی) calculation method:
 *   - Fajr angle: 18.0°
 *   - Isha angle: 18.0°
 *   - Asr: Hanafi juristic method (shadow length = object shadow + 2x object height)
 * - Also supports Shafi'i/Standard Asr (shadow factor = 1) if selected
 * - Other international calculation methods (ISNA, MWL, Umm Al-Qura, Egyptian, Tehran)
 * - Exact solar altitude calculations, equation of time, solar declination, refraction correction
 * - Automatic next prayer detection & live countdown timer
 * - Sehri (Imsak/Fajr end of Suhoor), Sunrise (Tuloo-e-Aftab), Ishraq, Chasht (Duha), Zawal/Dhuhr, Asr (Hanafi/Shafi'i), Maghrib/Iftar, Isha, Tahajjud timing
 */

export interface PrayerCalculationMethod {
  id: string;
  nameUrdu: string;
  nameEnglish: string;
  fajrAngle: number;
  ishaAngle: number;
  maghribMinutes?: number;
  ishaMinutes?: number;
}

export const CALCULATION_METHODS: Record<string, PrayerCalculationMethod> = {
  karachi: {
    id: 'karachi',
    nameUrdu: 'جامعہ علوم اسلامیہ علامہ بنوری ٹاؤن کراچی (مستند طریقہ پاکستان و برصغیر)',
    nameEnglish: 'University of Islamic Sciences, Karachi',
    fajrAngle: 18.0,
    ishaAngle: 18.0,
  },
  makkah: {
    id: 'makkah',
    nameUrdu: 'جامعہ ام القریٰ مکہ مکرمہ (سعودی عرب)',
    nameEnglish: 'Umm Al-Qura University, Makkah',
    fajrAngle: 18.5,
    ishaAngle: 0,
    ishaMinutes: 90,
  },
  mwl: {
    id: 'mwl',
    nameUrdu: 'رابطۃ العالم الاسلامی (Muslim World League)',
    nameEnglish: 'Muslim World League',
    fajrAngle: 18.0,
    ishaAngle: 17.0,
  },
  isna: {
    id: 'isna',
    nameUrdu: 'اسلامک سوسائٹی آف نارتھ امریکہ (ISNA)',
    nameEnglish: 'Islamic Society of North America',
    fajrAngle: 15.0,
    ishaAngle: 15.0,
  },
  egypt: {
    id: 'egypt',
    nameUrdu: 'مصر ہیئت عامہ مصریہ للمساحہ (مصر و افریقہ)',
    nameEnglish: 'Egyptian General Authority of Survey',
    fajrAngle: 19.5,
    ishaAngle: 17.5,
  }
};

export interface CityPreset {
  id: string;
  nameUrdu: string;
  nameEnglish: string;
  countryUrdu: string;
  countryEnglish: string;
  latitude: number;
  longitude: number;
  timezone: number; // UTC offset in hours
  isDefault?: boolean;
}

export const POPULAR_CITIES: CityPreset[] = [
  // Major Pakistan Cities
  { id: 'karachi', nameUrdu: 'کراچی', nameEnglish: 'Karachi', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 24.8607, longitude: 67.0011, timezone: 5, isDefault: true },
  { id: 'lahore', nameUrdu: 'لاہور', nameEnglish: 'Lahore', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 31.5204, longitude: 74.3587, timezone: 5 },
  { id: 'islamabad', nameUrdu: 'اسلام آباد', nameEnglish: 'Islamabad', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 33.6844, longitude: 73.0479, timezone: 5 },
  { id: 'rawalpindi', nameUrdu: 'راولپنڈی', nameEnglish: 'Rawalpindi', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 33.5651, longitude: 73.0169, timezone: 5 },
  { id: 'peshawar', nameUrdu: 'پشاور', nameEnglish: 'Peshawar', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 34.0151, longitude: 71.5249, timezone: 5 },
  { id: 'quetta', nameUrdu: 'کوئٹہ', nameEnglish: 'Quetta', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 30.1798, longitude: 66.9750, timezone: 5 },
  { id: 'faisalabad', nameUrdu: 'فیصل آباد', nameEnglish: 'Faisalabad', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 31.4504, longitude: 73.1350, timezone: 5 },
  { id: 'multan', nameUrdu: 'ملتان', nameEnglish: 'Multan', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 30.1575, longitude: 71.5249, timezone: 5 },
  { id: 'gujranwala', nameUrdu: 'گوجرانوالہ', nameEnglish: 'Gujranwala', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 32.1877, longitude: 74.1945, timezone: 5 },
  { id: 'sialkot', nameUrdu: 'سیالکوٹ', nameEnglish: 'Sialkot', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 32.4945, longitude: 74.5229, timezone: 5 },
  { id: 'hyderabad', nameUrdu: 'حیدرآباد', nameEnglish: 'Hyderabad', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 25.3960, longitude: 68.3578, timezone: 5 },
  { id: 'sukkur', nameUrdu: 'سکھر', nameEnglish: 'Sukkur', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 27.7052, longitude: 68.8574, timezone: 5 },
  { id: 'bahawalpur', nameUrdu: 'بہاولپور', nameEnglish: 'Bahawalpur', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 29.3544, longitude: 71.6911, timezone: 5 },
  { id: 'sargodha', nameUrdu: 'سرگودھا', nameEnglish: 'Sargodha', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 32.0836, longitude: 72.6711, timezone: 5 },
  { id: 'abbottabad', nameUrdu: 'ایبٹ آباد', nameEnglish: 'Abbottabad', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 34.1688, longitude: 73.2215, timezone: 5 },
  { id: 'gilgit', nameUrdu: 'گلگت', nameEnglish: 'Gilgit', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 35.9221, longitude: 74.3087, timezone: 5 },
  { id: 'muzaffarabad', nameUrdu: 'مظفر آباد', nameEnglish: 'Muzaffarabad', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 34.3700, longitude: 73.4711, timezone: 5 },
  { id: 'swat', nameUrdu: 'سوات (مینگورہ)', nameEnglish: 'Swat', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 34.7717, longitude: 72.3602, timezone: 5 },
  { id: 'mirpur', nameUrdu: 'میرپور (آزاد کشمیر)', nameEnglish: 'Mirpur', countryUrdu: 'پاکستان', countryEnglish: 'Pakistan', latitude: 33.1484, longitude: 73.7519, timezone: 5 },

  // International Holy & Major Cities
  { id: 'makkah', nameUrdu: 'مکہ مکرمہ', nameEnglish: 'Makkah', countryUrdu: 'سعودی عرب', countryEnglish: 'Saudi Arabia', latitude: 21.3891, longitude: 39.8579, timezone: 3 },
  { id: 'madinah', nameUrdu: 'مدینہ منورہ', nameEnglish: 'Madinah', countryUrdu: 'سعودی عرب', countryEnglish: 'Saudi Arabia', latitude: 24.5247, longitude: 39.5692, timezone: 3 },
  { id: 'jerusalem', nameUrdu: 'القدس شریف (بیت المقدس)', nameEnglish: 'Jerusalem', countryUrdu: 'فلسطین', countryEnglish: 'Palestine', latitude: 31.7683, longitude: 35.2137, timezone: 3 },
  { id: 'dubai', nameUrdu: 'دبئی', nameEnglish: 'Dubai', countryUrdu: 'متحدہ عرب امارات', countryEnglish: 'UAE', latitude: 25.2048, longitude: 55.2708, timezone: 4 },
  { id: 'riyadh', nameUrdu: 'ریاض', nameEnglish: 'Riyadh', countryUrdu: 'سعودی عرب', countryEnglish: 'Saudi Arabia', latitude: 24.7136, longitude: 46.6753, timezone: 3 },
  { id: 'doha', nameUrdu: 'دوحہ', nameEnglish: 'Doha', countryUrdu: 'قطر', countryEnglish: 'Qatar', latitude: 25.2854, longitude: 51.5310, timezone: 3 },
  { id: 'istanbul', nameUrdu: 'استنبول', nameEnglish: 'Istanbul', countryUrdu: 'ترکیہ', countryEnglish: 'Turkey', latitude: 41.0082, longitude: 28.9784, timezone: 3 },
  { id: 'cairo', nameUrdu: 'قاہرہ', nameEnglish: 'Cairo', countryUrdu: 'مصر', countryEnglish: 'Egypt', latitude: 30.0444, longitude: 31.2357, timezone: 2 },
  { id: 'kabul', nameUrdu: 'کابل', nameEnglish: 'Kabul', countryUrdu: 'افغانستان', countryEnglish: 'Afghanistan', latitude: 34.5553, longitude: 69.2075, timezone: 4.5 },
  { id: 'dhaka', nameUrdu: 'ڈھاکہ', nameEnglish: 'Dhaka', countryUrdu: 'بنگلہ دیش', countryEnglish: 'Bangladesh', latitude: 23.8103, longitude: 90.4125, timezone: 6 },
  { id: 'delhi', nameUrdu: 'نئی دہلی', nameEnglish: 'New Delhi', countryUrdu: 'بھارت', countryEnglish: 'India', latitude: 28.6139, longitude: 77.2090, timezone: 5.5 },
  { id: 'mumbai', nameUrdu: 'ممبئی', nameEnglish: 'Mumbai', countryUrdu: 'بھارت', countryEnglish: 'India', latitude: 19.0760, longitude: 72.8777, timezone: 5.5 },
  { id: 'london', nameUrdu: 'لندن', nameEnglish: 'London', countryUrdu: 'برطانیہ', countryEnglish: 'United Kingdom', latitude: 51.5074, longitude: -0.1278, timezone: 0 },
  { id: 'newyork', nameUrdu: 'نیویارک', nameEnglish: 'New York', countryUrdu: 'امریکہ', countryEnglish: 'USA', latitude: 40.7128, longitude: -74.0060, timezone: -4 },
  { id: 'toronto', nameUrdu: 'ٹورنٹو', nameEnglish: 'Toronto', countryUrdu: 'کینیڈا', countryEnglish: 'Canada', latitude: 43.6532, longitude: -79.3832, timezone: -4 },
  { id: 'sydney', nameUrdu: 'سڈنی', nameEnglish: 'Sydney', countryUrdu: 'آسٹریلیا', countryEnglish: 'Australia', latitude: -33.8688, longitude: 151.2093, timezone: 10 },
  { id: 'kualalumpur', nameUrdu: 'کوالالمپور', nameEnglish: 'Kuala Lumpur', countryUrdu: 'ملائیشیا', countryEnglish: 'Malaysia', latitude: 3.1390, longitude: 101.6869, timezone: 8 },
  { id: 'jakarta', nameUrdu: 'جکارتہ', nameEnglish: 'Jakarta', countryUrdu: 'انڈونیشیا', countryEnglish: 'Indonesia', latitude: -6.2088, longitude: 106.8456, timezone: 7 },
];

export interface CalculatedPrayerTimes {
  fajr: Date;
  sunrise: Date;
  ishraq: Date;
  chasht: Date;
  dhuhr: Date;
  asr: Date; // Hanafi Asr
  asrShafi: Date;
  maghrib: Date;
  isha: Date;
  tahajjud: Date;
  sehriEnd: Date;
  
  // Formatted string times
  fajrStr: string;
  sunriseStr: string;
  ishraqStr: string;
  chashtStr: string;
  dhuhrStr: string;
  asrStr: string;
  asrShafiStr: string;
  maghribStr: string;
  ishaStr: string;
  tahajjudStr: string;
  sehriEndStr: string;

  nextPrayer: {
    key: 'fajr' | 'sunrise' | 'dhuhr' | 'asr' | 'maghrib' | 'isha';
    nameUrdu: string;
    nameEnglish: string;
    time: Date;
    timeStr: string;
    remainingMinutes: number;
    remainingHours: number;
    remainingSeconds: number;
  };
}

// Astronomical Math Utilities
const d2r = (deg: number) => (deg * Math.PI) / 180.0;
const r2d = (rad: number) => (rad * 180.0) / Math.PI;
const sin = (deg: number) => Math.sin(d2r(deg));
const cos = (deg: number) => Math.cos(d2r(deg));
const tan = (deg: number) => Math.tan(d2r(deg));
const asin = (x: number) => r2d(Math.asin(x));
const acos = (x: number) => r2d(Math.acos(x));
const atan = (x: number) => r2d(Math.atan(x));

const fixAngle = (a: number) => {
  let res = a - 360.0 * Math.floor(a / 360.0);
  return res < 0 ? res + 360.0 : res;
};

const fixHour = (h: number) => {
  let res = h - 24.0 * Math.floor(h / 24.0);
  return res < 0 ? res + 24.0 : res;
};

// Calculate Julian Date from Gregorian Date
function getJulianDate(year: number, month: number, day: number): number {
  if (month <= 2) {
    year -= 1;
    month += 12;
  }
  const A = Math.floor(year / 100);
  const B = 2 - A + Math.floor(A / 4);
  return Math.floor(365.25 * (year + 4716)) + Math.floor(30.6001 * (month + 1)) + day + B - 1524.5;
}

// Solar coordinates
function sunPosition(jd: number) {
  const D = jd - 2451545.0;
  const g = fixAngle(357.529 + 0.98560028 * D);
  const q = fixAngle(280.459 + 0.98564736 * D);
  const L = fixAngle(q + 1.915 * sin(g) + 0.02 * sin(2 * g));

  const e = 23.439 - 0.00000036 * D;
  const RA = fixAngle(atan(cos(e) * tan(L))) / 15.0;
  const RaAdjusted = fixHour(RA + (Math.floor(L / 90) - Math.floor(RA * 15 / 90)) * 6);
  const declination = asin(sin(e) * sin(L));
  const equationOfTime = q / 15.0 - RaAdjusted;

  return { declination, equationOfTime };
}

// Sun angle time
function sunAngleTime(angle: number, declination: number, lat: number, direction: 'ccw' | 'cw') {
  const numerator = -sin(angle) - sin(lat) * sin(declination);
  const denominator = cos(lat) * cos(declination);
  const x = numerator / denominator;
  if (x < -1 || x > 1) return null;
  const val = acos(x) / 15.0;
  return direction === 'ccw' ? val : -val;
}

// Asr shadow time
function asrTime(factor: number, declination: number, lat: number) {
  const shadowAngle = -atan(1.0 / (factor + tan(Math.abs(lat - declination))));
  return sunAngleTime(shadowAngle, declination, lat, 'ccw');
}

export function formatTime12H(date: Date): string {
  let hours = date.getHours();
  const minutes = date.getMinutes();
  const ampm = hours >= 12 ? 'PM' : 'AM';
  hours = hours % 12;
  hours = hours ? hours : 12; // 0 becomes 12
  const minStr = minutes < 10 ? '0' + minutes : minutes.toString();
  return `${hours}:${minStr} ${ampm}`;
}

export function formatTimeUrdu(date: Date): string {
  let hours = date.getHours();
  const minutes = date.getMinutes();
  const isNight = hours < 4 || hours >= 20;
  const isMorning = hours >= 4 && hours < 12;
  const isAfternoon = hours >= 12 && hours < 17;
  const isEvening = hours >= 17 && hours < 20;

  let period = 'صبح';
  if (isAfternoon) period = 'دوپہر';
  else if (isEvening) period = 'شام';
  else if (isNight) period = 'رات';

  hours = hours % 12;
  hours = hours ? hours : 12;
  const minStr = minutes < 10 ? '0' + minutes : minutes.toString();
  return `${hours}:${minStr} ${period}`;
}

/**
 * Main Calculation Function
 */
export function calculatePrayerTimes(
  date: Date,
  lat: number,
  lng: number,
  timezoneOffsetHours: number,
  methodId: string = 'karachi',
  asrJuristic: 'hanafi' | 'shafii' = 'hanafi'
): CalculatedPrayerTimes {
  const method = CALCULATION_METHODS[methodId] || CALCULATION_METHODS.karachi;
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();

  const jd = getJulianDate(year, month, day);
  const { declination, equationOfTime } = sunPosition(jd);

  // Solar noon (Zawal end / Dhuhr start)
  const noon = fixHour(12 + timezoneOffsetHours - lng / 15.0 - equationOfTime);

  // Fajr
  const fajrDiff = sunAngleTime(method.fajrAngle, declination, lat, 'cw');
  const fajrHour = fajrDiff !== null ? noon + fajrDiff : noon - 1.5;

  // Sunrise (Tuloo) - Sun altitude -0.833°
  const sunriseDiff = sunAngleTime(0.833, declination, lat, 'cw');
  const sunriseHour = sunriseDiff !== null ? noon + sunriseDiff : noon - 1.2;

  // Asr Hanafi (Shadow factor 2)
  const asrHanafiDiff = asrTime(2, declination, lat);
  const asrHanafiHour = asrHanafiDiff !== null ? noon + asrHanafiDiff : noon + 3.5;

  // Asr Shafi'i (Shadow factor 1)
  const asrShafiiDiff = asrTime(1, declination, lat);
  const asrShafiiHour = asrShafiiDiff !== null ? noon + asrShafiiDiff : noon + 2.5;

  // Maghrib (Sunset / Ghuroob) - Sun altitude -0.833° + small twilight
  const sunsetDiff = sunAngleTime(0.833, declination, lat, 'ccw');
  const maghribHour = sunsetDiff !== null ? noon + sunsetDiff : noon + 1.2;

  // Isha
  let ishaHour: number;
  if (method.ishaMinutes) {
    ishaHour = maghribHour + method.ishaMinutes / 60.0;
  } else {
    const ishaDiff = sunAngleTime(method.ishaAngle, declination, lat, 'ccw');
    ishaHour = ishaDiff !== null ? noon + ishaDiff : noon + 2.5;
  }

  // Convert decimal hours into Date objects on the same day
  const toDate = (h: number) => {
    const normalizedH = fixHour(h);
    const d = new Date(date);
    const hours = Math.floor(normalizedH);
    const minutes = Math.floor((normalizedH - hours) * 60);
    const seconds = Math.floor(((normalizedH - hours) * 60 - minutes) * 60);
    d.setHours(hours, minutes, seconds, 0);
    return d;
  };

  const fajr = toDate(fajrHour);
  const sunrise = toDate(sunriseHour);
  const dhuhr = toDate(noon);
  const asrHanafi = toDate(asrHanafiHour);
  const asrShafi = toDate(asrShafiiHour);
  const maghrib = toDate(maghribHour);
  const isha = toDate(ishaHour);

  // Extra Islamic Timings
  // Sehri End: 10 minutes before Fajr
  const sehriEnd = new Date(fajr.getTime() - 10 * 60 * 1000);
  
  // Ishraq: 15-20 minutes after Sunrise
  const ishraq = new Date(sunrise.getTime() + 18 * 60 * 1000);

  // Chasht (Duha): midway between Sunrise and Dhuhr
  const chasht = new Date((sunrise.getTime() + dhuhr.getTime()) / 2);

  // Tahajjud: last third of night (between Maghrib and Fajr)
  const nightDuration = fajr.getTime() + 24 * 3600 * 1000 - maghrib.getTime();
  const tahajjud = new Date(maghrib.getTime() + (2 * nightDuration) / 3);

  const activeAsr = asrJuristic === 'hanafi' ? asrHanafi : asrShafi;

  // Determine Next Prayer
  const now = new Date();
  const schedule: Array<{ key: 'fajr' | 'sunrise' | 'dhuhr' | 'asr' | 'maghrib' | 'isha'; nameUrdu: string; nameEnglish: string; time: Date }> = [
    { key: 'fajr', nameUrdu: 'فجر', nameEnglish: 'Fajr', time: fajr },
    { key: 'sunrise', nameUrdu: 'طلوعِ آفتاب', nameEnglish: 'Sunrise', time: sunrise },
    { key: 'dhuhr', nameUrdu: 'ظہر', nameEnglish: 'Dhuhr', time: dhuhr },
    { key: 'asr', nameUrdu: 'عصر', nameEnglish: 'Asr', time: activeAsr },
    { key: 'maghrib', nameUrdu: 'مغرب', nameEnglish: 'Maghrib', time: maghrib },
    { key: 'isha', nameUrdu: 'عشاء', nameEnglish: 'Isha', time: isha },
  ];

  let nextP = schedule.find(p => p.time.getTime() > now.getTime());
  if (!nextP) {
    // Next prayer is tomorrow's Fajr
    const tomorrowFajr = new Date(fajr.getTime() + 24 * 60 * 60 * 1000);
    nextP = {
      key: 'fajr',
      nameUrdu: 'فجر',
      nameEnglish: 'Fajr',
      time: tomorrowFajr,
    };
  }

  const diffMs = Math.max(0, nextP.time.getTime() - now.getTime());
  const diffSec = Math.floor(diffMs / 1000);
  const remainingHours = Math.floor(diffSec / 3600);
  const remainingMinutes = Math.floor((diffSec % 3600) / 60);
  const remainingSeconds = diffSec % 60;

  return {
    fajr,
    sunrise,
    ishraq,
    chasht,
    dhuhr,
    asr: asrHanafi,
    asrShafi,
    maghrib,
    isha,
    tahajjud,
    sehriEnd,

    fajrStr: formatTime12H(fajr),
    sunriseStr: formatTime12H(sunrise),
    ishraqStr: formatTime12H(ishraq),
    chashtStr: formatTime12H(chasht),
    dhuhrStr: formatTime12H(dhuhr),
    asrStr: formatTime12H(asrHanafi),
    asrShafiStr: formatTime12H(asrShafi),
    maghribStr: formatTime12H(maghrib),
    ishaStr: formatTime12H(isha),
    tahajjudStr: formatTime12H(tahajjud),
    sehriEndStr: formatTime12H(sehriEnd),

    nextPrayer: {
      key: nextP.key,
      nameUrdu: nextP.nameUrdu,
      nameEnglish: nextP.nameEnglish,
      time: nextP.time,
      timeStr: formatTime12H(nextP.time),
      remainingMinutes,
      remainingHours,
      remainingSeconds
    }
  };
}
