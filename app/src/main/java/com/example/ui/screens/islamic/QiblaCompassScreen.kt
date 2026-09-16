package com.example.ui.screens.islamic

import android.Manifest
import android.content.Context
import android.hardware.GeomagneticField
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlin.math.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun QiblaCompassScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val vibrator = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vm.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    var rawMagneticHeading by remember { mutableFloatStateOf(0f) }
    var trueHeading by remember { mutableFloatStateOf(0f) }
    var manualDragHeading by remember { mutableFloatStateOf(0f) }
    var isManualMode by remember { mutableStateOf(false) }
    var declination by remember { mutableFloatStateOf(0f) }
    
    var userLocation by remember { mutableStateOf<Location?>(null) }
    var qiblaBearing by remember { mutableStateOf(255.0) } // Default for Swabi/Pakistan (~255°)
    var distanceToKaaba by remember { mutableIntStateOf(3400) }
    var isLocationAvailable by remember { mutableStateOf(false) }
    var isHardwareSensorPresent by remember { mutableStateOf(false) }
    var activeSensorType by remember { mutableStateOf("چیک کیا جا رہا ہے...") }
    var showCalibrationDialog by remember { mutableStateOf(false) }

    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    // Location
    fun applyLocation(loc: Location) {
        userLocation = loc
        isLocationAvailable = true
        val bearing = QiblaHelper.calculateQiblaDirection(loc.latitude, loc.longitude)
        qiblaBearing = bearing
        distanceToKaaba = QiblaHelper.calculateDistanceToKaabaKm(loc.latitude, loc.longitude)
        val geoField = GeomagneticField(
            loc.latitude.toFloat(),
            loc.longitude.toFloat(),
            loc.altitude.toFloat(),
            System.currentTimeMillis()
        )
        declination = geoField.declination
    }

    fun updateLocation() {
        if (locationPermissionState.status.isGranted) {
            try {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? android.location.LocationManager
                if (locationManager != null) {
                    val gpsLoc = try { locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER) } catch (e: SecurityException) { null }
                    val netLoc = try { locationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER) } catch (e: SecurityException) { null }
                    val passiveLoc = try { locationManager.getLastKnownLocation(android.location.LocationManager.PASSIVE_PROVIDER) } catch (e: SecurityException) { null }
                    val bestLoc = gpsLoc ?: netLoc ?: passiveLoc
                    if (bestLoc != null) {
                        applyLocation(bestLoc)
                    } else {
                        // Request single location update via LocationManager
                        val listener = object : android.location.LocationListener {
                            override fun onLocationChanged(loc: Location) {
                                applyLocation(loc)
                                try { locationManager.removeUpdates(this) } catch (e: Throwable) {}
                            }
                            @Deprecated("Deprecated in Java")
                            override fun onStatusChanged(provider: String?, status: Int, extras: android.os.Bundle?) {}
                            override fun onProviderEnabled(provider: String) {}
                            override fun onProviderDisabled(provider: String) {}
                        }
                        try {
                            if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                                locationManager.requestSingleUpdate(android.location.LocationManager.GPS_PROVIDER, listener, null)
                            } else if (locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
                                locationManager.requestSingleUpdate(android.location.LocationManager.NETWORK_PROVIDER, listener, null)
                            }
                        } catch (e: SecurityException) {
                            // permission not granted
                        } catch (e: Throwable) {
                            // ignore
                        }
                    }
                }
            } catch (e: Throwable) {
                // ignore
            }
        }
    }

    LaunchedEffect(locationPermissionState.status) {
        if (locationPermissionState.status.isGranted) {
            updateLocation()
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    // Hardware Sensors
    DisposableEffect(Unit) {
        val rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        val gravity = FloatArray(3)
        val geomagnetic = FloatArray(3)
        var hasGravity = false
        var hasGeomagnetic = false

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return

                var calculatedHeading = 0f
                if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                    val rotationMatrix = FloatArray(9)
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(rotationMatrix, orientation)
                    calculatedHeading = (Math.toDegrees(orientation[0].toDouble()).toFloat() + 360f) % 360f
                    activeSensorType = "Rotation Vector (حقیقی اورینٹیشن)"
                    isHardwareSensorPresent = true
                } else if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    System.arraycopy(event.values, 0, gravity, 0, 3)
                    hasGravity = true
                } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    System.arraycopy(event.values, 0, geomagnetic, 0, 3)
                    hasGeomagnetic = true
                }

                if (event.sensor.type != Sensor.TYPE_ROTATION_VECTOR && hasGravity && hasGeomagnetic) {
                    val r = FloatArray(9)
                    val i = FloatArray(9)
                    if (SensorManager.getRotationMatrix(r, i, gravity, geomagnetic)) {
                        val orientation = FloatArray(3)
                        SensorManager.getOrientation(r, orientation)
                        calculatedHeading = (Math.toDegrees(orientation[0].toDouble()).toFloat() + 360f) % 360f
                        activeSensorType = "Accelerometer + Magnetometer"
                        isHardwareSensorPresent = true
                    }
                }

                if (calculatedHeading > 0f || rawMagneticHeading > 0f) {
                    rawMagneticHeading = calculatedHeading
                    if (!isManualMode) {
                        trueHeading = (calculatedHeading + declination + 360f) % 360f
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        var registered = false
        if (rotationVectorSensor != null) {
            registered = sensorManager.registerListener(listener, rotationVectorSensor, SensorManager.SENSOR_DELAY_UI)
        }
        if (!registered && accelerometer != null && magnetometer != null) {
            val regAcc = sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)
            val regMag = sensorManager.registerListener(listener, magnetometer, SensorManager.SENSOR_DELAY_UI)
            registered = regAcc && regMag
        }

        isHardwareSensorPresent = registered
        if (!registered) {
            activeSensorType = "پریویو / نو سینسر موڈ (ٹچ کنٹرول فعال ہے)"
        }

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    // Effective heading used for UI
    val currentTargetHeading = if (isManualMode || !isHardwareSensorPresent) manualDragHeading else trueHeading

    val animatedHeading by animateFloatAsState(
        targetValue = currentTargetHeading,
        animationSpec = spring(stiffness = 350f),
        label = "HeadingAnim"
    )

    // Calculate Relative angle & Turn direction
    val relativeAngle = ((qiblaBearing - animatedHeading + 540) % 360) - 180
    val isAligned = abs(relativeAngle) <= 3.0
    var wasAligned by remember { mutableStateOf(false) }

    // Haptic vibration trigger on transition to ALIGNED
    LaunchedEffect(isAligned) {
        if (isAligned && !wasAligned) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(100)
                }
            } catch (e: Exception) {
                // ignore
            }
        }
        wasAligned = isAligned
    }

    val themeColor by animateColorAsState(
        targetValue = when {
            isAligned -> Color(0xFF10B981) // Green
            abs(relativeAngle) <= 15.0 -> Color(0xFFF59E0B) // Amber
            else -> Color(0xFF3B82F6) // Blue
        },
        label = "ThemeColor"
    )

    if (showCalibrationDialog) {
        AlertDialog(
            onDismissRequest = { showCalibrationDialog = false },
            title = { Text("کمپاس کیلیبریشن (Calibration)") },
            text = {
                Column {
                    Text("کمپاس کی درستگی کو بہتر بنانے کے لیے اپنے فون کو ہوا میں انگریزی ہندسے 8 (Figure-8) کی شکل میں 3 سے 4 بار گھمائیں۔")
                    Spacer(Modifier.height(12.dp))
                    Text("اس سے فون کے مقناطیسی سینسرز خود بخود ری سیٹ ہو جاتے ہیں۔", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            confirmButton = {
                TextButton(onClick = { showCalibrationDialog = false }) {
                    Text("ٹھیک ہے")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("قبلہ نما (Qibla Compass)", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showCalibrationDialog = true }) {
                        Icon(Icons.Default.CompassCalibration, contentDescription = "Calibrate")
                    }
                    IconButton(onClick = { updateLocation() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh GPS")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Location Permission Banner
            if (!locationPermissionState.status.isGranted) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("لوکیشن کی اجازت درکار ہے", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("قبلہ کے درست زاویے کے لیے GPS لوکیشن ضروری ہے۔", fontSize = 12.sp)
                        }
                        Button(
                            onClick = { locationPermissionState.launchPermissionRequest() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("اجازت دیں", fontSize = 12.sp)
                        }
                    }
                }
            }

            // Status Banner with Dynamic Direction Guidance
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isAligned) Color(0xFF064E3B) else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isAligned) "🕋 آپ قبلہ رخ ہیں (QIBLA ALIGNED)" 
                                   else if (relativeAngle > 0) "دائیں طرف گھمائیں: ${abs(relativeAngle).toInt()}° ↪" 
                                   else "بائیں طرف گھمائیں: ↩ ${abs(relativeAngle).toInt()}°",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAligned) Color(0xFF6EE7B7) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "سمت: ${QiblaHelper.getDirectionName(qiblaBearing)} (${qiblaBearing.toInt()}°)",
                            fontSize = 13.sp,
                            color = if (isAligned) Color(0xFFA7F3D0) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(themeColor.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = if (isAligned) "🕋" else "🧭", fontSize = 22.sp)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Main Interactive Compass Dial (Supports Touch Dragging for Preview/Testing)
            Box(
                modifier = Modifier
                    .size(310.dp)
                    .padding(6.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            isManualMode = true
                            // Rotate compass with touch drag
                            manualDragHeading = (manualDragHeading - dragAmount.x * 0.5f + 360f) % 360f
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val radius = size.minDimension / 2f
                    val centerOffset = Offset(size.width / 2f, size.height / 2f)

                    // Outer Bezel
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF1E293B), Color(0xFF0F172A)),
                            center = centerOffset,
                            radius = radius
                        ),
                        radius = radius,
                        center = centerOffset
                    )

                    drawCircle(
                        color = themeColor.copy(alpha = 0.6f),
                        radius = radius,
                        center = centerOffset,
                        style = Stroke(width = 4f)
                    )

                    // Rotating Compass Ring (North, East, South, West & Ticks)
                    // The dial rotates by -animatedHeading so North aligns to Real North
                    rotate(-animatedHeading, pivot = centerOffset) {
                        for (i in 0 until 360 step 5) {
                            val isMajor = (i % 30 == 0)
                            val isCardinal = (i % 90 == 0)
                            val tickLength = if (isCardinal) 22f else if (isMajor) 14f else 8f
                            val tickColor = if (isCardinal) Color.White else if (isMajor) Color(0xFFCBD5E1) else Color(0xFF64748B)
                            val strokeWidth = if (isCardinal) 3.5f else if (isMajor) 2f else 1f

                            rotate(i.toFloat(), pivot = centerOffset) {
                                drawLine(
                                    color = tickColor,
                                    start = Offset(centerOffset.x, centerOffset.y - radius + 6f),
                                    end = Offset(centerOffset.x, centerOffset.y - radius + 6f + tickLength),
                                    strokeWidth = strokeWidth,
                                    cap = StrokeCap.Round
                                )
                            }
                        }

                        // Fixed Kaaba position on the compass dial (At exact Qibla bearing)
                        rotate(qiblaBearing.toFloat(), pivot = centerOffset) {
                            drawLine(
                                color = Color(0xFFF59E0B),
                                start = Offset(centerOffset.x, centerOffset.y - radius + 4f),
                                end = Offset(centerOffset.x, centerOffset.y - radius + 28f),
                                strokeWidth = 6f,
                                cap = StrokeCap.Round
                            )
                            drawCircle(
                                color = Color(0xFFF59E0B),
                                radius = 10f,
                                center = Offset(centerOffset.x, centerOffset.y - radius + 42f)
                            )
                        }

                        // North Marker (Red Arrow)
                        val northPath = Path().apply {
                            moveTo(centerOffset.x, centerOffset.y - radius + 28f)
                            lineTo(centerOffset.x - 10f, centerOffset.y - radius + 44f)
                            lineTo(centerOffset.x + 10f, centerOffset.y - radius + 44f)
                            close()
                        }
                        drawPath(northPath, color = Color(0xFFEF4444))
                    }

                    // Inner Dial Ring
                    drawCircle(
                        color = Color(0xFF0F172A),
                        radius = radius * 0.70f,
                        center = centerOffset
                    )

                    // Dynamic Qibla Pointer Needle (Points relative to current heading)
                    val qiblaRelativeDeg = (qiblaBearing.toFloat() - animatedHeading)
                    rotate(qiblaRelativeDeg, pivot = centerOffset) {
                        val needleLength = radius * 0.65f
                        val needlePath = Path().apply {
                            moveTo(centerOffset.x, centerOffset.y - needleLength)
                            lineTo(centerOffset.x - 12f, centerOffset.y - 18f)
                            lineTo(centerOffset.x, centerOffset.y)
                            lineTo(centerOffset.x + 12f, centerOffset.y - 18f)
                            close()
                        }
                        drawPath(
                            path = needlePath,
                            brush = Brush.verticalGradient(
                                colors = if (isAligned) 
                                    listOf(Color(0xFF10B981), Color(0xFF047857))
                                else 
                                    listOf(Color(0xFFF59E0B), Color(0xFFB45309)),
                                startY = centerOffset.y - needleLength,
                                endY = centerOffset.y
                            )
                        )

                        // Kaaba beacon at needle tip
                        drawCircle(
                            color = if (isAligned) Color(0xFF10B981) else Color(0xFFF59E0B),
                            radius = 14f,
                            center = Offset(centerOffset.x, centerOffset.y - needleLength - 12f)
                        )
                    }

                    // Center Hub
                    drawCircle(color = Color(0xFFE2E8F0), radius = 16f, center = centerOffset)
                    drawCircle(color = themeColor, radius = 10f, center = centerOffset)
                }

                // Overlay Text for N, S, E, W
                Text("N", color = Color(0xFFEF4444), fontWeight = FontWeight.Black, fontSize = 16.sp, modifier = Modifier.align(Alignment.TopCenter).padding(top = 18.dp))
                Text("S", color = Color(0xFFE2E8F0), fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 18.dp))
                Text("E", color = Color(0xFFE2E8F0), fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.align(Alignment.CenterEnd).padding(end = 18.dp))
                Text("W", color = Color(0xFFE2E8F0), fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.align(Alignment.CenterStart).padding(start = 18.dp))

                // Center Icon when Aligned
                if (isAligned) {
                    Text(
                        text = "🕋",
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.Center).padding(bottom = 120.dp)
                    )
                }
            }

            // Interactive Preview Controls
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.TouchApp, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(6.dp))
                    Text("ڈائل کو انگلی/ماؤس سے گھمائیں", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
                }

                if (isManualMode && isHardwareSensorPresent) {
                    TextButton(onClick = { isManualMode = false }) {
                        Icon(Icons.Default.RotateRight, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("سینسر موڈ", fontSize = 12.sp)
                    }
                }
            }

            // Quick Angle Preset Buttons for testing preview
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                    onClick = {
                        isManualMode = true
                        manualDragHeading = 0f // North
                    }
                ) {
                    Text("شمال 0°", fontSize = 11.sp)
                }
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                    onClick = {
                        isManualMode = true
                        manualDragHeading = 90f // East
                    }
                ) {
                    Text("مشرق 90°", fontSize = 11.sp)
                }
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                    onClick = {
                        isManualMode = true
                        manualDragHeading = qiblaBearing.toFloat() // Exact Qibla Alignment
                    }
                ) {
                    Text("قبلہ رخ ${qiblaBearing.toInt()}° 🕋", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(12.dp))

            // Primary Telemetry Metrics Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("قبلہ زاویہ (Qibla)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${qiblaBearing.toInt()}°", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("فون رخ (Heading)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${animatedHeading.toInt()}°", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("مکہ فاصلہ", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("$distanceToKaaba km", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Live Diagnostics / Telemetry Panel
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("تشخیصی معلومات (Diagnostics)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                    
                    Text("• مقام: ${if (userLocation != null) "${String.format("%.4f", userLocation!!.latitude)}°, ${String.format("%.4f", userLocation!!.longitude)}°" else "صوابی/پاکستان (GPS انتظار)"}", fontSize = 12.sp)
                    Text("• مقناطیسی انحراف (Declination): ${String.format("%.1f", declination)}°", fontSize = 12.sp)
                    Text("• فعال موڈ: $activeSensorType", fontSize = 12.sp)
                    Text("• فرق زاویہ (Relative Angle): ${relativeAngle.toInt()}°", fontSize = 12.sp)
                }
            }
        }
    }
}
