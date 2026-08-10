# Add project specific ProGuard rules here.

# Keep Room database & entities
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Keep models and entities
-keep class com.example.data.model.** { *; }

# Keep Moshi & Retrofit annotations and serialized fields
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes *Annotation*
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**

# Keep Coroutines
-keepclassmembers class kotlinx.coroutines.** { *; }

# Preserve line numbers for Crashlytics stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

