package com.example.ui.screens.islamic

import kotlin.math.*

object QiblaHelper {
    const val KAABA_LAT = 21.422487
    const val KAABA_LON = 39.826206

    fun calculateQiblaDirection(userLat: Double, userLon: Double): Double {
        val lat1 = Math.toRadians(userLat)
        val lon1 = Math.toRadians(userLon)
        val lat2 = Math.toRadians(KAABA_LAT)
        val lon2 = Math.toRadians(KAABA_LON)

        val dLon = lon2 - lon1
        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)

        val bearing = Math.toDegrees(atan2(y, x))
        return (bearing + 360.0) % 360.0
    }

    fun calculateDistanceToKaabaKm(userLat: Double, userLon: Double): Int {
        val r = 6371.0
        val dLat = Math.toRadians(KAABA_LAT - userLat)
        val dLon = Math.toRadians(KAABA_LON - userLon)
        val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(userLat)) * cos(Math.toRadians(KAABA_LAT)) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return (r * c).toInt()
    }

    fun getDirectionName(bearing: Double): String {
        val b = (bearing + 360.0) % 360.0
        return when {
            b >= 337.5 || b < 22.5 -> "شمال (North)"
            b >= 22.5 && b < 67.5 -> "شمال مشرق (North-East)"
            b >= 67.5 && b < 112.5 -> "مشرق (East)"
            b >= 112.5 && b < 157.5 -> "جنوب مشرق (South-East)"
            b >= 157.5 && b < 202.5 -> "جنوب (South)"
            b >= 202.5 && b < 247.5 -> "جنوب مغرب (South-West)"
            b >= 247.5 && b < 292.5 -> "مغرب (West)"
            else -> "شمال مغرب (North-West)"
        }
    }
}
