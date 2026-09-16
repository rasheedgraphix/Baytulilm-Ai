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
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            val cts = CancellationTokenSource()

            var loc: Location? = null
            try {
                // High accuracy current location request
                val currentTask = fusedClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cts.token
                )
                loc = com.google.android.gms.tasks.Tasks.await(currentTask, 4000, java.util.concurrent.TimeUnit.MILLISECONDS)
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
                val geocoder = Geocoder(context, Locale("ur", "PK"))
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    detectedCityUr = address.locality ?: address.subAdminArea ?: address.adminArea
                }

                val geocoderEn = Geocoder(context, Locale.ENGLISH)
                val addressesEn = geocoderEn.getFromLocation(lat, lng, 1)
                if (!addressesEn.isNullOrEmpty()) {
                    val addressEn = addressesEn[0]
                    detectedCityEn = addressEn.locality ?: addressEn.subAdminArea ?: addressEn.adminArea
                }
            }
        } catch (e: Exception) {
            // Geocoder might fail if network is slow/offline
        }

        // 2. Find closest known city from database to get authentic Urdu name & Timezone if missing
        val nearest = findNearestDefaultCity(lat, lng)

        val finalNameUrdu = detectedCityUr ?: nearest.nameUrdu
        val finalNameEnglish = detectedCityEn ?: nearest.nameEnglish
        val timeZoneId = nearest.timeZoneId.ifBlank { TimeZone.getDefault().id }

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
}
