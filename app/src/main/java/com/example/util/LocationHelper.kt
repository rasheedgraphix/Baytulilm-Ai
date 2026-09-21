package com.example.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import java.util.TimeZone
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object LocationHelper {

    fun hasLocationPermission(context: Context): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(context: Context): CityLocation? = withContext(Dispatchers.IO) {
        if (!hasLocationPermission(context)) return@withContext null

        try {
            var loc: Location? = null

            // 1. Try Google Play Services FusedLocationProviderClient first
            try {
                val fusedClient = LocationServices.getFusedLocationProviderClient(context)
                val cts = CancellationTokenSource()

                try {
                    val currentTask = fusedClient.getCurrentLocation(
                        Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                        cts.token
                    )
                    loc = com.google.android.gms.tasks.Tasks.await(currentTask, 3000, java.util.concurrent.TimeUnit.MILLISECONDS)
                } catch (e: Exception) {
                    // Fallback to last location
                }

                if (loc == null) {
                    try {
                        val lastTask = fusedClient.lastLocation
                        loc = com.google.android.gms.tasks.Tasks.await(lastTask, 2000, java.util.concurrent.TimeUnit.MILLISECONDS)
                    } catch (e: Exception) {
                        // Ignored
                    }
                }
            } catch (e: Exception) {
                // Fused client not available
            }

            // 2. Fallback to Android standard LocationManager (GPS, Network, Passive)
            if (loc == null) {
                try {
                    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? android.location.LocationManager
                    if (locationManager != null) {
                        val gpsLoc = try { locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER) } catch (e: SecurityException) { null }
                        val netLoc = try { locationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER) } catch (e: SecurityException) { null }
                        val passiveLoc = try { locationManager.getLastKnownLocation(android.location.LocationManager.PASSIVE_PROVIDER) } catch (e: SecurityException) { null }
                        
                        loc = gpsLoc ?: netLoc ?: passiveLoc
                    }
                } catch (e: Exception) {
                    // Ignored
                }
            }

            if (loc != null) {
                return@withContext resolveCityFromCoordinates(context, loc.latitude, loc.longitude)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return@withContext null
    }

    private fun resolveCityFromCoordinates(context: Context, lat: Double, lng: Double): CityLocation {
        // 1. Try Geocoder for accurate local city/town name
        var detectedCityEn: String? = null
        var detectedCityUr: String? = null

        try {
            if (Geocoder.isPresent()) {
                try {
                    val geocoderUr = Geocoder(context, Locale("ur", "PK"))
                    val addressesUr = geocoderUr.getFromLocation(lat, lng, 1)
                    if (!addressesUr.isNullOrEmpty()) {
                        val address = addressesUr[0]
                        detectedCityUr = address.locality ?: address.subAdminArea ?: address.adminArea
                    }
                } catch (e: Exception) {
                    // Geocoder ur failed
                }

                try {
                    val geocoderEn = Geocoder(context, Locale.ENGLISH)
                    val addressesEn = geocoderEn.getFromLocation(lat, lng, 1)
                    if (!addressesEn.isNullOrEmpty()) {
                        val addressEn = addressesEn[0]
                        detectedCityEn = addressEn.locality ?: addressEn.subAdminArea ?: addressEn.adminArea
                    }
                } catch (e: Exception) {
                    // Geocoder en failed
                }
            }
        } catch (e: Exception) {
            // Geocoder might fail if network is slow/offline
        }

        // 2. Find closest known city from database
        val nearest = findNearestDefaultCity(lat, lng)

        val finalNameUrdu = detectedCityUr?.ifBlank { null } ?: nearest.nameUrdu
        val finalNameEnglish = detectedCityEn?.ifBlank { null } ?: nearest.nameEnglish
        
        // Exact timezone detection based on coordinates
        val timeZoneId = when {
            lat in 23.0..37.5 && lng in 60.0..78.0 -> "Asia/Karachi"
            lat in 16.0..32.0 && lng in 34.0..55.0 -> "Asia/Riyadh"
            lat in 8.0..37.0 && lng in 68.0..97.0 -> "Asia/Kolkata"
            lat in 20.0..27.0 && lng in 88.0..93.0 -> "Asia/Dhaka"
            lat in 24.0..26.5 && lng in 51.0..56.5 -> "Asia/Dubai"
            else -> nearest.timeZoneId.ifBlank { TimeZone.getDefault().id }
        }

        return CityLocation(
            nameUrdu = finalNameUrdu,
            nameEnglish = finalNameEnglish,
            lat = lat,
            lng = lng,
            timeZoneId = timeZoneId
        )
    }

    private fun findNearestDefaultCity(lat: Double, lng: Double): CityLocation {
        var minDistance = Double.MAX_VALUE
        var closest = PrayerTimeCalculator.defaultCities.first()

        for (city in PrayerTimeCalculator.defaultCities) {
            val dist = calculateDistanceKm(lat, lng, city.lat, city.lng)
            if (dist < minDistance) {
                minDistance = dist
                closest = city
            }
        }
        return closest
    }

    // Haversine formula for exact distance between GPS coordinates
    private fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // Earth radius in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    fun getCityName(context: Context): String {
        val prefs = context.getSharedPreferences("prayer_city_prefs", Context.MODE_PRIVATE)
        val cityNameEng = prefs.getString("city_name_eng", null)
        if (!cityNameEng.isNullOrBlank() && !cityNameEng.equals("Ukiah", ignoreCase = true)) return cityNameEng
        val nameUrdu = prefs.getString("city_name_urdu", null)
        if (!nameUrdu.isNullOrBlank() && !nameUrdu.contains("Ukiah", ignoreCase = true)) return nameUrdu
        return "راولپنڈی"
    }
}
