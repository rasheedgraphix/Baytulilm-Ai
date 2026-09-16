package com.example.ui.screens.islamic

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.data.repository.IslamicData
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.util.AppConfig

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HaramainLiveScreen(
    onNavigateBack: (() -> Unit)? = null,
    isTopLevel: Boolean = true,
    onFullscreenChange: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val activity = remember(context) {
        var ctx = context
        while (ctx is ContextWrapper) {
            if (ctx is Activity) return@remember ctx
            ctx = ctx.baseContext
        }
        null
    }

    val streams = IslamicData.LIVE_STREAMS
    var selectedStreamIndex by remember { mutableIntStateOf(0) }
    val currentStream = streams.getOrElse(selectedStreamIndex) { streams.first() }

    // Stream URLs and backup index
    val streamUrls = remember(currentStream) {
        if (currentStream.streamUrls.isNotEmpty()) currentStream.streamUrls else listOf(currentStream.url)
    }
    var currentSourceIndex by remember(currentStream.id) { mutableIntStateOf(0) }

    // Player states
    var isPlaying by remember { mutableStateOf(true) }
    var isMuted by remember { mutableStateOf(false) }
    var isBuffering by remember { mutableStateOf(true) }
    var playerError by remember { mutableStateOf<String?>(null) }
    var isFullscreen by rememberSaveable { mutableStateOf(false) }
    var isZoomFill by rememberSaveable { mutableStateOf(false) }
    val activeFullscreenResizeMode = if (isZoomFill) AspectRatioFrameLayout.RESIZE_MODE_ZOOM else AspectRatioFrameLayout.RESIZE_MODE_FIT
    var showControlsOverlay by remember { mutableStateOf(true) }
    var showServerDialog by remember { mutableStateOf(false) }

    // Native Media3 ExoPlayer instance with fast-startup Low-Latency LoadControl and cross-protocol data source
    val exoPlayer = remember(context) {
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setUserAgent("Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Mobile Safari/537.36 BaytulIlmApp")
            .setConnectTimeoutMs(20000)
            .setReadTimeoutMs(20000)
            .setAllowCrossProtocolRedirects(true)

        val mediaSourceFactory = DefaultMediaSourceFactory(context)
            .setDataSourceFactory(httpDataSourceFactory)

        val renderersFactory = DefaultRenderersFactory(context)
            .setEnableDecoderFallback(true)

        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                /* minBufferMs = */ 1500,
                /* maxBufferMs = */ 15000,
                /* bufferForPlaybackMs = */ 500,
                /* bufferForPlaybackAfterRebufferMs = */ 1000
            )
            .setPrioritizeTimeOverSizeThresholds(true)
            .build()

        ExoPlayer.Builder(context, renderersFactory)
            .setMediaSourceFactory(mediaSourceFactory)
            .setLoadControl(loadControl)
            .build().apply {
                playWhenReady = true
                repeatMode = Player.REPEAT_MODE_OFF
            }
    }

    // Release player on disposal (unless changing configurations like rotating screen)
    DisposableEffect(exoPlayer) {
        onDispose {
            if (activity?.isChangingConfigurations != true) {
                exoPlayer.stop()
                exoPlayer.release()
            }
        }
    }

    // Direct helper to launch official YouTube live stream
    val openYouTubeLive = {
        val target = if (currentStream.youtubeUrl.isNotBlank()) {
            currentStream.youtubeUrl
        } else {
            "https://www.youtube.com/@SaudiQuranTv/live"
        }
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(target)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {}
    }

    // Buffering duration tracker to detect slow internet
    var bufferingDurationSeconds by remember { mutableIntStateOf(0) }
    LaunchedEffect(isBuffering, playerError) {
        if (isBuffering && playerError == null) {
            bufferingDurationSeconds = 0
            while (true) {
                kotlinx.coroutines.delay(1000)
                bufferingDurationSeconds++
            }
        } else {
            bufferingDurationSeconds = 0
        }
    }

    // Lifecycle observer for background pause & resume
    DisposableEffect(lifecycleOwner, exoPlayer) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    exoPlayer.pause()
                }
                Lifecycle.Event.ON_RESUME -> {
                    if (isPlaying) {
                        exoPlayer.play()
                    }
                }
                Lifecycle.Event.ON_DESTROY -> {
                    if (activity?.isChangingConfigurations != true) {
                        exoPlayer.stop()
                        exoPlayer.release()
                    }
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Function to load the active stream source
    fun loadSource(sourceIdx: Int) {
        val validIdx = sourceIdx.coerceIn(0, streamUrls.size - 1)
        val targetUrl = streamUrls[validIdx]
        isBuffering = true
        playerError = null

        try {
            val mediaItem = MediaItem.Builder()
                .setUri(targetUrl)
                .setMimeType(MimeTypes.APPLICATION_M3U8)
                .build()
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = isPlaying
            exoPlayer.volume = if (isMuted) 0f else 1f
        } catch (e: Exception) {
            playerError = "اسٹریم شروع نہیں ہو سکی: ${e.localizedMessage}"
            isBuffering = false
        }
    }

    // Player listener for playback states and auto-fallback
    DisposableEffect(exoPlayer, currentSourceIndex, streamUrls) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        isBuffering = true
                        playerError = null
                    }
                    Player.STATE_READY -> {
                        isBuffering = false
                        playerError = null
                    }
                    Player.STATE_ENDED -> {
                        isBuffering = false
                    }
                    Player.STATE_IDLE -> {}
                }
            }

            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onPlayerError(error: PlaybackException) {
                isBuffering = false
                // Multi-Stream Automatic Backup Fallback
                if (currentSourceIndex < streamUrls.size - 1) {
                    val nextIdx = currentSourceIndex + 1
                    currentSourceIndex = nextIdx
                    loadSource(nextIdx)
                } else {
                    playerError = "براہِ راست نشریات کا کنکشن منقطع ہو گیا ہے۔ براہِ کرم متبادل سرور منتخب کریں یا دوبارہ کوشش کریں۔"
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
        }
    }

    // Trigger load when stream or source changes
    LaunchedEffect(currentStream.id, currentSourceIndex) {
        loadSource(currentSourceIndex)
    }

    // Sync mute state
    LaunchedEffect(isMuted) {
        exoPlayer.volume = if (isMuted) 0f else 1f
    }

    var showFullscreenControls by remember { mutableStateOf(false) }

    // Auto-hide controls in fullscreen after 3.5 seconds
    LaunchedEffect(showFullscreenControls, isFullscreen) {
        if (isFullscreen && showFullscreenControls) {
            kotlinx.coroutines.delay(3500)
            showFullscreenControls = false
        }
    }

    val enterFullscreen = {
        isFullscreen = true
        showFullscreenControls = true
        onFullscreenChange(true)
        setSystemBarsVisible(activity, false)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }

    val toggleOrientation = {
        if (activity != null) {
            val current = activity.requestedOrientation
            if (current == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE ||
                current == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            }
        }
    }

    val exitFullscreen = {
        isFullscreen = false
        showFullscreenControls = false
        onFullscreenChange(false)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setSystemBarsVisible(activity, true)
    }

    // Sync fullscreen state with host
    LaunchedEffect(isFullscreen) {
        onFullscreenChange(isFullscreen)
        setSystemBarsVisible(activity, !isFullscreen)
    }

    // Fullscreen back handler
    BackHandler(enabled = isFullscreen) {
        exitFullscreen()
    }

    // Ensure system bars and orientation reset when leaving screen
    DisposableEffect(Unit) {
        onDispose {
            if (activity?.isChangingConfigurations != true) {
                onFullscreenChange(false)
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                setSystemBarsVisible(activity, true)
            }
        }
    }

    // Pulsing animation for Live 🔴 indicator
    val infiniteTransition = rememberInfiniteTransition(label = "live_pulse")
    val liveAlpha by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "live_indicator_alpha"
    )

    // Fullscreen View - Complete edge-to-edge video, zero top/bottom bars
    if (isFullscreen) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Native Media3 Player Surface filling available screen
            NativePlayerSurface(
                exoPlayer = exoPlayer,
                modifier = Modifier.fillMaxSize(),
                resizeMode = activeFullscreenResizeMode
            )

            // Transparent touch interceptor covering full screen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null
                    ) {
                        showFullscreenControls = !showFullscreenControls
                    }
            )

            // Buffering Indicator in Fullscreen
            if (isBuffering && playerError == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(14.dp))
                            .padding(20.dp)
                    ) {
                        CircularProgressIndicator(
                            color = IslamicGold,
                            modifier = Modifier.size(44.dp),
                            strokeWidth = 3.5.dp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "${currentStream.title} لائیو لوڈ ہو رہا ہے...",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        if (bufferingDurationSeconds >= 6) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "انٹرنیٹ سست ہو سکتا ہے... بفرنگ جاری ہے",
                                color = Color(0xFFFFCC80),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilledTonalButton(
                                    onClick = {
                                        val next = (currentSourceIndex + 1) % streamUrls.size
                                        currentSourceIndex = next
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.Dns, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("متبادل سرور", fontSize = 11.sp)
                                }
                                OutlinedButton(
                                    onClick = openYouTubeLive,
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, Color(0xFFFF5252)),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text("یوٹیوب لائیو ▶", color = Color(0xFFFF5252), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Error Overlay in Fullscreen with Retry
            if (playerError != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.85f))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFFFB74D),
                            modifier = Modifier.size(42.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "براہِ راست نشریات لوڈ نہیں ہو سکی",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "اگر آپ کا انٹرنیٹ سست ہے تو متبادل سرور یا یوٹیوب لائیو آزمائیں۔",
                            color = Color.LightGray,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = {
                                    playerError = null
                                    isBuffering = true
                                    loadSource(currentSourceIndex)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = IslamicGold),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("دوبارہ چلائیں", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            FilledTonalButton(
                                onClick = {
                                    val next = (currentSourceIndex + 1) % streamUrls.size
                                    currentSourceIndex = next
                                },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Dns, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("سرور ${((currentSourceIndex + 1) % streamUrls.size) + 1}", fontSize = 12.sp)
                            }
                            OutlinedButton(
                                onClick = openYouTubeLive,
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFFF5252))
                            ) {
                                Text("یوٹیوب ▶", color = Color(0xFFFF5252), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = exitFullscreen,
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                            ) {
                                Text("بند کریں", color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Controls overlay: Top Bar, Center Play/Pause, and Bottom Bar
            AnimatedVisibility(
                visible = showFullscreenControls,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                ) {
                    // Top Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopStart)
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back / Exit Fullscreen Button
                        Surface(
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.65f),
                            border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f))
                        ) {
                            IconButton(
                                onClick = exitFullscreen,
                                modifier = Modifier.size(42.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Exit Fullscreen",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        // Stream Switchers (Makkah & Madinah) + Live Badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFFD32F2F).copy(alpha = liveAlpha)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Box(modifier = Modifier.size(6.dp).background(Color.White, CircleShape))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("LIVE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            streams.forEachIndexed { idx, stm ->
                                val isSelected = selectedStreamIndex == idx
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isSelected) IslamicGold else Color.Black.copy(alpha = 0.65f),
                                    border = BorderStroke(1.dp, if (isSelected) IslamicGold else Color.White.copy(alpha = 0.3f)),
                                    modifier = Modifier.clickable {
                                        if (selectedStreamIndex != idx) {
                                            selectedStreamIndex = idx
                                            currentSourceIndex = 0
                                            playerError = null
                                            isBuffering = true
                                        }
                                    }
                                ) {
                                    Text(
                                        text = "${stm.icon} ${if (idx == 0) "مکہ مکرمہ" else "مدینہ منورہ"}",
                                        color = if (isSelected) Color.Black else Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        // Server Switcher Button
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.Black.copy(alpha = 0.65f),
                            border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f)),
                            modifier = Modifier.clickable {
                                val next = (currentSourceIndex + 1) % streamUrls.size
                                currentSourceIndex = next
                            }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Icon(
                                    Icons.Default.Dns,
                                    contentDescription = null,
                                    tint = IslamicGoldLight,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "سرور ${currentSourceIndex + 1}",
                                    color = IslamicGoldLight,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Center Play / Pause
                    IconButton(
                        onClick = {
                            if (isPlaying) {
                                exoPlayer.pause()
                                isPlaying = false
                            } else {
                                exoPlayer.play()
                                isPlaying = true
                            }
                        },
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.Center)
                            .background(Color.Black.copy(alpha = 0.65f), CircleShape)
                            .border(1.5.dp, IslamicGold, CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // Bottom Bar: Location info on left, action controls on right
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.Black.copy(alpha = 0.65f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${currentStream.icon} ${currentStream.location}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Aspect Ratio Toggle: Fit vs Fill (Zoom)
                            Surface(
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.65f),
                                border = BorderStroke(1.dp, if (isZoomFill) IslamicGold else Color.White.copy(alpha = 0.3f))
                            ) {
                                IconButton(
                                    onClick = { isZoomFill = !isZoomFill },
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isZoomFill) Icons.Default.FitScreen else Icons.Default.AspectRatio,
                                        contentDescription = if (isZoomFill) "Fit" else "Fill",
                                        tint = if (isZoomFill) IslamicGold else Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            // Mute / Unmute Button
                            Surface(
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.65f),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                            ) {
                                IconButton(
                                    onClick = { isMuted = !isMuted },
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isMuted) Icons.AutoMirrored.Filled.VolumeMute else Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = if (isMuted) "Unmute" else "Mute",
                                        tint = if (isMuted) Color(0xFFFF8A80) else Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            // Screen Orientation Toggle (Portrait/Landscape)
                            Surface(
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.65f),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                            ) {
                                IconButton(
                                    onClick = toggleOrientation,
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ScreenRotation,
                                        contentDescription = "Toggle Orientation",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            // Fullscreen Exit Button
                            Surface(
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.65f),
                                border = BorderStroke(1.dp, IslamicGold)
                            ) {
                                IconButton(
                                    onClick = exitFullscreen,
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FullscreenExit,
                                        contentDescription = "Exit Fullscreen",
                                        tint = IslamicGold,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        return
    }

    // Normal In-App Mode (Beautiful, spiritual layout)
    val content = @Composable {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            // 1. Spiritual Top Hero Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEF3C7))
                                .border(1.2.dp, IslamicGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🕋", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "حرمین شریفین براہِ راست",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "24 گھنٹے مکہ مکرمہ و مدینہ منورہ سے لائیو نشریات",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Pulsing Live Indicator
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFD32F2F).copy(alpha = liveAlpha),
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .background(Color.White, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "LIVE HD",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Sacred Mosques Switcher (Makkah & Madinah Cards)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                streams.forEachIndexed { index, stream ->
                    val isSelected = selectedStreamIndex == index
                    val isMakkah = index == 0

                    val containerColor by animateColorAsState(
                        targetValue = if (isSelected) {
                            if (isMakkah) Color(0xFF2C240E) else Color(0xFF0C2B1D)
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                        label = "card_color"
                    )

                    val borderColor by animateColorAsState(
                        targetValue = if (isSelected) {
                            if (isMakkah) IslamicGold else Color(0xFF38B27B)
                        } else {
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        },
                        label = "border_color"
                    )

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(84.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                if (selectedStreamIndex != index) {
                                    selectedStreamIndex = index
                                    currentSourceIndex = 0
                                    playerError = null
                                    isBuffering = true
                                }
                            }
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        color = containerColor,
                        shadowElevation = if (isSelected) 6.dp else 1.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) {
                                            if (isMakkah) IslamicGold.copy(alpha = 0.25f) else Color(0xFF38B27B).copy(alpha = 0.25f)
                                        } else {
                                            MaterialTheme.colorScheme.surfaceVariant
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = stream.icon, fontSize = 24.sp)
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isMakkah) "مکہ مکرمہ" else "مدینہ منورہ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                                Text(
                                    text = if (isMakkah) "المسجد الحرام" else "المسجد النبوی",
                                    fontSize = 11.sp,
                                    color = if (isSelected) {
                                        if (isMakkah) IslamicGoldLight else Color(0xFF86E3B8)
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    },
                                    maxLines = 1
                                )
                                if (isSelected) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(top = 2.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(5.dp)
                                                .background(Color(0xFFD32F2F), CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "چل رہا ہے",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Cinematic Video Player Surface Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(12.dp, RoundedCornerShape(20.dp))
                    .border(
                        width = 1.5.dp,
                        color = IslamicGold.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(20.dp)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    // Actual Native Video Surface
                    NativePlayerSurface(
                        exoPlayer = exoPlayer,
                        modifier = Modifier.fillMaxSize(),
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    )

                    // Tap overlay to toggle controls
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                                indication = null
                            ) {
                                showControlsOverlay = !showControlsOverlay
                            }
                    )

                    // Top/Center/Bottom Controls Overlay
                    if (showControlsOverlay && playerError == null) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color.Black.copy(alpha = 0.65f),
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.75f)
                                        )
                                    )
                                )
                        ) {
                            // Top Bar inside Player
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.TopStart)
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFD32F2F).copy(alpha = liveAlpha)
                                    ) {
                                        Text(
                                            text = "🔴 LIVE",
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${currentStream.icon} ${currentStream.title}",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color.Black.copy(alpha = 0.6f)
                                ) {
                                    Text(
                                        text = "سروَر ${currentSourceIndex + 1}",
                                        color = IslamicGoldLight,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            // Center Play / Pause Button
                            IconButton(
                                onClick = {
                                    if (isPlaying) {
                                        exoPlayer.pause()
                                        isPlaying = false
                                    } else {
                                        exoPlayer.play()
                                        isPlaying = true
                                    }
                                },
                                modifier = Modifier
                                    .size(56.dp)
                                    .align(Alignment.Center)
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                    .border(1.5.dp, IslamicGold, CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Pause" else "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            // Bottom Bar inside Player
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Mute / Unmute Button
                                IconButton(
                                    onClick = { isMuted = !isMuted },
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = if (isMuted) Icons.AutoMirrored.Filled.VolumeMute else Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = if (isMuted) "Unmute" else "Mute",
                                        tint = if (isMuted) Color(0xFFFF8A80) else Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                // Fullscreen Expand Button
                                IconButton(
                                    onClick = enterFullscreen,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Fullscreen,
                                        contentDescription = "Fullscreen",
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Buffering Indicator
                    if (isBuffering && playerError == null) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.7f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                CircularProgressIndicator(
                                    color = IslamicGold,
                                    strokeWidth = 3.dp,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "${currentStream.title} سے براہِ راست رابطہ ہو رہا ہے...",
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                                if (bufferingDurationSeconds >= 6) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "انٹرنیٹ سست ہو سکتا ہے... بفرنگ جاری ہے",
                                        color = Color(0xFFFFCC80),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        FilledTonalButton(
                                            onClick = {
                                                val next = (currentSourceIndex + 1) % streamUrls.size
                                                currentSourceIndex = next
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Icon(Icons.Default.Dns, contentDescription = null, modifier = Modifier.size(13.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("متبادل سرور", fontSize = 11.sp)
                                        }
                                        OutlinedButton(
                                            onClick = openYouTubeLive,
                                            shape = RoundedCornerShape(8.dp),
                                            border = BorderStroke(1.dp, Color(0xFFFF5252)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text("یوٹیوب لائیو ▶", color = Color(0xFFFF5252), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Error Screen with Retry
                    if (playerError != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFF141414))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color(0xFFFFB74D),
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "براہِ راست نشریات لوڈ نہیں ہو سکی",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "اگر آپ کا انٹرنیٹ سست ہے تو متبادل سرور یا یوٹیوب لائیو آزمائیں۔",
                                    color = Color.LightGray,
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = {
                                            playerError = null
                                            isBuffering = true
                                            loadSource(currentSourceIndex)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = IslamicGold),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Black)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("دوبارہ چلائیں", fontSize = 11.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                                    }

                                    FilledTonalButton(
                                        onClick = {
                                            val next = (currentSourceIndex + 1) % streamUrls.size
                                            currentSourceIndex = next
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Icon(Icons.Default.Dns, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("سرور ${((currentSourceIndex + 1) % streamUrls.size) + 1}", fontSize = 11.sp)
                                    }

                                    OutlinedButton(
                                        onClick = openYouTubeLive,
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, Color(0xFFFF5252)),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text("یوٹیوب ▶", color = Color(0xFFFF5252), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Quick Server Switcher Strip
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Dns,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "براہِ راست نشریاتی سرورز:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        IconButton(
                            onClick = {
                                playerError = null
                                isBuffering = true
                                loadSource(currentSourceIndex)
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        streamUrls.forEachIndexed { index, _ ->
                            val isCurrent = currentSourceIndex == index
                            val label = when (index) {
                                0 -> "سرور 1 (HD)"
                                1 -> "سرور 2 (تیز)"
                                else -> "سرور ${index + 1}"
                            }
                            FilterChip(
                                selected = isCurrent,
                                onClick = { currentSourceIndex = index },
                                label = {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                leadingIcon = if (isCurrent) {
                                    {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .background(Color(0xFFD32F2F), CircleShape)
                                        )
                                    }
                                } else null,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Direct YouTube Live chip
                        FilterChip(
                            selected = false,
                            onClick = openYouTubeLive,
                            label = {
                                Text(
                                    text = "یوٹیوب ▶",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD32F2F)
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 5. Spiritual Section: Virtues of the Two Holy Mosques (فضائل حرمین شریفین)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFDCF8EC)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "✨", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "فضیلتِ حرمین شریفین (حدیثِ مبارک)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "قال رسول الله ﷺ: «صَلَاةٌ فِي مَسْجِدِي هَذَا خَيْرٌ مِنْ أَلْفِ صَلَاةٍ فِيمَا سِوَاهُ إِلَّا الْمَسْجِدَ الْحَرَامَ، وَصَلَاةٌ فِي الْمَسْجِدِ الْحَرَامِ خَيْرٌ مِنْ مِائَةِ أَلْفِ صَلَاةٍ فِيمَا سِوَاهُ»",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Right
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "رسول اللہ ﷺ نے فرمایا: میری اس مسجد (مسجدِ نبوی) میں ایک نماز دیگر مساجد کی 1,000 نمازوں سے افضل ہے سوائے مسجدِ حرام کے، اور مسجدِ حرام میں ایک نماز دیگر مساجد کی 100,000 (ایک لاکھ) نمازوں سے افضل ہے۔ (صحیح بخاری و مسلم)",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Dua Section
                    Text(
                        text = if (selectedStreamIndex == 0) "کعبہ شریف کی زیارت کی دعا:" else "روضۂ رسول ﷺ پر صلوٰۃ و سلام:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFFF8E1),
                        border = androidx.compose.foundation.BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            if (selectedStreamIndex == 0) {
                                Text(
                                    text = "اللَّهُمَّ زِدْ هَذَا الْبَيْتَ تَشْرِيفًا وَتَعْظِيمًا وَتَكْرِيمًا وَمَهَابَةً، وَزِدْ مَنْ شَرَّفَهُ وَعَظَّمَهُ مِمَّنْ حَجَّهُ أَوِ اعْتَمَرَهُ تَشْرِيفًا وَتَكْرِيمًا وَتَعْظِيمًا وَبِرًّا",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF5D4037),
                                    lineHeight = 22.sp,
                                    textAlign = TextAlign.Right
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "ترجمہ: اے اللہ! اس گھر کی عزت، عظمت، بزرگی اور رعب میں مزید اضافہ فرما، اور جو اس کا حج یا عمرہ کر کے اس کی تعظیم کرے اس کے شرف اور نیکی میں بھی اضافہ فرما۔",
                                    fontSize = 11.sp,
                                    color = Color(0xFF4E342E),
                                    lineHeight = 16.sp
                                )
                            } else {
                                Text(
                                    text = "الصَّلَاةُ وَالسَّلَامُ عَلَيْكَ يَا سَيِّدِي يَا رَسُولَ اللَّهِ، الصَّلَاةُ وَالسَّلَامُ عَلَيْكَ يَا حَبِيبَ اللَّهِ، وَعَلَى آلِكَ وَأَصْحَابِكَ يَا نُورَ اللَّهِ",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20),
                                    lineHeight = 22.sp,
                                    textAlign = TextAlign.Right
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "اے اللہ کے پیارے رسول! آپ پر اور آپ کی آل و اصحاب پر درود و سلام ہو، اور آپ کی شفاعت ہم سب کا مقدر بنے۔ آمین",
                                    fontSize = 11.sp,
                                    color = Color(0xFF2E7D32),
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 6. Share Stream Button (ثوابِ جاریہ)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ثوابِ جاریہ میں حصہ لیں",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "دوستوں اور اہل خانہ کو حرمین کی لائیو زیارت شیئر کریں",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    FilledTonalButton(
                        onClick = {
                            try {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "بیت العلم ایپ پر مکہ مکرمہ اور مدینہ منورہ سے 24 گھنٹے براہِ راست HD نشریات دیکھیں:\n${currentStream.title} - ${currentStream.location}\nاللہ تعالیٰ ہم سب کو بار بار حرمین شریفین کی باادب زیارت نصیب فرمائے۔"
                                    )
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Stream"))
                            } catch (e: Exception) {
                                AppConfig.shareAppWithWebsite(context)
                            }
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("شیئر کریں", fontSize = 12.sp)
                    }
                }
            }
        }
    }

    // If top-level, we don't need an extra topAppBar; if not, wrap with Scaffold and Back arrow
    if (isTopLevel) {
        content()
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "حرمین شریفین لائیو",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFD32F2F).copy(alpha = liveAlpha)
                            ) {
                                Text(
                                    text = "🔴 LIVE",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        if (onNavigateBack != null) {
                            IconButton(onClick = onNavigateBack) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                )
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                content()
            }
        }
    }

    // Server Selection Dialog
    if (showServerDialog) {
        AlertDialog(
            onDismissRequest = { showServerDialog = false },
            title = { Text("لائیو اسٹریم سرور منتخب کریں", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "اگر ویڈیو بفرنگ کرے یا سست ہو تو متبادل سرور منتخب کریں:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    streamUrls.forEachIndexed { idx, _ ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    currentSourceIndex = idx
                                    showServerDialog = false
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = currentSourceIndex == idx,
                                onClick = {
                                    currentSourceIndex = idx
                                    showServerDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (idx == 0) "سرور 1 (High Quality HD)" else "سرور ${idx + 1} (Alternative)",
                                fontSize = 13.sp,
                                fontWeight = if (currentSourceIndex == idx) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showServerDialog = false
                                openYouTubeLive()
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "▶", fontSize = 16.sp, color = Color(0xFFD32F2F))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "یوٹیوب آفیشل لائیو (سعودی ٹی وی)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD32F2F)
                            )
                            Text(
                                text = "کمزور انٹرنیٹ پر بھی بغیر رکے چلتا ہے",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showServerDialog = false }) {
                    Text("بند کریں")
                }
            }
        )
    }
}

/**
 * AndroidView host for Media3 PlayerView
 */
@OptIn(UnstableApi::class)
@Composable
private fun NativePlayerSurface(
    exoPlayer: ExoPlayer,
    modifier: Modifier = Modifier,
    resizeMode: Int = AspectRatioFrameLayout.RESIZE_MODE_FIT
) {
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                player = exoPlayer
                useController = false
                setResizeMode(resizeMode)
                setBackgroundColor(android.graphics.Color.BLACK)
                keepScreenOn = true
                isClickable = false
                isFocusable = false
            }
        },
        update = { playerView ->
            if (playerView.player != exoPlayer) {
                playerView.player = exoPlayer
            }
            playerView.setResizeMode(resizeMode)
            playerView.keepScreenOn = true
        },
        onRelease = { playerView ->
            playerView.player = null
        },
        modifier = modifier
    )
}

/**
 * Helper to toggle status & navigation bars and cutout mode for immersive fullscreen
 */
private fun setSystemBarsVisible(activity: Activity?, visible: Boolean) {
    if (activity == null) return
    val window = activity.window ?: return

    val insetsController = androidx.core.view.WindowCompat.getInsetsController(window, window.decorView)
    if (visible) {
        insetsController.show(androidx.core.view.WindowInsetsCompat.Type.systemBars())
    } else {
        insetsController.hide(androidx.core.view.WindowInsetsCompat.Type.systemBars())
        insetsController.systemBarsBehavior =
            androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        window.attributes = window.attributes.apply {
            layoutInDisplayCutoutMode = if (visible) {
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
            } else {
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        }
    }
}
