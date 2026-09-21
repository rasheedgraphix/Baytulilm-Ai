package com.example

import android.os.Bundle
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.util.NotificationPermissionHelper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.IslamicDrawerContent
import com.example.ui.navigation.Screen
import com.example.ui.screens.admin.AdminAnalyticsScreen
import com.example.ui.screens.admin.AdminBackupSettingsScreen
import com.example.ui.screens.admin.AdminBookManagementScreen
import com.example.ui.screens.admin.AdminCertificatesScreen
import com.example.ui.screens.admin.AdminContentManagementScreen
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.admin.AdminPushNotificationsScreen
import com.example.ui.screens.admin.AdminQuizManagementScreen
import com.example.ui.screens.admin.AdminRoleManagementScreen
import com.example.ui.screens.admin.AdminSecurityScreen
import com.example.ui.screens.admin.AdminShuroohTranslationsScreen
import com.example.ui.screens.admin.AdminUserManagementScreen
import com.example.ui.screens.ai.AiAssistantScreen
import com.example.ui.screens.ai.AiTeacherScreen
import com.example.ui.screens.islamic.AsmaUlHusnaScreen
import com.example.ui.screens.islamic.AsmaUnNabiScreen
import com.example.ui.screens.islamic.DaimeAuqatScreen
import com.example.ui.screens.islamic.HaramainLiveScreen
import com.example.ui.screens.islamic.IslamicCalendarScreen
import com.example.ui.screens.islamic.QiblaCompassScreen
import com.example.ui.screens.islamic.TasbeehScreen
import com.example.ui.screens.islamic.QuranScreen
import com.example.ui.screens.islamic.TafseerScreen
import com.example.ui.screens.islamic.TafseerDetailScreen
import com.example.ui.screens.islamic.FatawaScreen
import com.example.ui.screens.islamic.FatawaDetailScreen
import com.example.ui.screens.islamic.LughatScreen
import com.example.ui.screens.islamic.FamousDuasScreen
import com.example.ui.screens.islamic.GenericPdfScreen
import com.example.ui.screens.auth.EmailVerificationScreen
import com.example.ui.screens.auth.ForgotPasswordScreen
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.auth.RegisterScreen
import com.example.ui.screens.auth.SignupScreen
import com.example.ui.screens.book.BookDetailScreen
import com.example.ui.screens.book.BookViewerScreen
import com.example.ui.screens.bookmarks.BookmarksScreen
import com.example.ui.screens.darjat.DarjatScreen
import com.example.ui.screens.downloads.DownloadsScreen
import com.example.ui.screens.downloads.OfflineScreen
import com.example.ui.screens.favorites.FavoritesScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.library.BooksScreen
import com.example.ui.screens.library.LibraryScreen
import com.example.ui.screens.lms.LmsAchievementsScreen
import com.example.ui.screens.lms.LmsAssignmentsScreen
import com.example.ui.screens.lms.LmsCertificatesScreen
import com.example.ui.screens.lms.LmsLeaderboardScreen
import com.example.ui.screens.lms.LmsNotesScreen
import com.example.ui.screens.lms.LmsProfileEditScreen
import com.example.ui.screens.lms.LmsStudyPlannerScreen
import com.example.ui.screens.lms.StudentDashboardScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.quiz.QuizScreen
import com.example.util.LocalAppLanguage
import com.example.ui.screens.language.LanguageSelectionScreen
import com.example.ui.screens.recent.RecentScreen
import com.example.ui.screens.search.SearchScreen
import com.example.ui.screens.settings.AboutContactScreen
import com.example.ui.screens.settings.PrivacyPolicyScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.subjects.SubjectsScreen
import com.example.ui.screens.madrasa.AdminBulkOpsScreen
import com.example.ui.screens.madrasa.AudioLecturesScreen
import com.example.ui.screens.madrasa.DirectMessagingScreen
import com.example.ui.screens.madrasa.DiscussionForumScreen
import com.example.ui.screens.madrasa.ExamResultsScreen
import com.example.ui.screens.madrasa.LiveClassesScreen
import com.example.ui.screens.madrasa.OcrAssistantScreen
import com.example.ui.screens.madrasa.ParentDashboardScreen
import com.example.ui.screens.madrasa.VideoCoursesScreen
import com.example.ui.theme.BaytulIlmTheme
import com.example.ui.theme.RasheedIslamicTheme
import android.view.KeyEvent
import com.example.ui.viewmodel.AiViewModel
import com.example.ui.viewmodel.AuthViewModel
import android.content.Intent
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.QuizViewModel
import com.example.util.LanguageManager
import com.example.util.LocalizedApp
import com.example.util.lStr
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager.init(this)
        com.example.ui.screens.islamic.HijriHelper.init(this)
        
        // Optimize cold start: Run heavy AdMob & FCM initializations asynchronously on background thread
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            try {
                com.example.util.AdMobManager.initialize(applicationContext)
                com.example.util.AdMobManager.loadInterstitial(applicationContext)
            } catch (t: Throwable) {
                android.util.Log.d("MainActivity", "AdMob init error: ${t.message}")
            }
            try {
                com.example.data.fcm.BaytulIlmFirebaseMessagingService.initializeFCM(applicationContext)
            } catch (t: Throwable) {
                android.util.Log.d("MainActivity", "FCM init skipped: ${t.message}")
            }
        }
        
        enableEdgeToEdge()
        setContent {
            LocalizedApp(context = this) {
                BaytulIlmTheme {
                    BaytulIlmApp()
                }
            }
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN ||
            keyCode == KeyEvent.KEYCODE_VOLUME_UP ||
            keyCode == KeyEvent.KEYCODE_VOLUME_MUTE ||
            keyCode == KeyEvent.KEYCODE_HEADSETHOOK) {

            var silenced = false
            if (com.example.util.PrayerAudioNotifier.isPlayingAllahuAkbar.value) {
                com.example.util.PrayerAudioNotifier.dismissAlert(this)
                silenced = true
            }
            if (com.example.util.KalimaShahadatPlayer.isPlaying.value) {
                com.example.util.KalimaShahadatPlayer.stop()
                silenced = true
            }
            if (silenced) {
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            val keyCode = event.keyCode
            if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN ||
                keyCode == KeyEvent.KEYCODE_VOLUME_UP ||
                keyCode == KeyEvent.KEYCODE_VOLUME_MUTE ||
                keyCode == KeyEvent.KEYCODE_HEADSETHOOK) {

                if (com.example.util.PrayerAudioNotifier.isPlayingAllahuAkbar.value) {
                    com.example.util.PrayerAudioNotifier.dismissAlert(this)
                    return true
                }
                if (com.example.util.KalimaShahadatPlayer.isPlaying.value) {
                    com.example.util.KalimaShahadatPlayer.stop()
                    return true
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaytulIlmApp() {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            NotificationPermissionHelper.recordPrompt(context)
        }
    )

    androidx.compose.runtime.LaunchedEffect(Unit) {
        if (NotificationPermissionHelper.shouldPrompt(context)) {
            NotificationPermissionHelper.recordPrompt(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val isKalimaPlaying by com.example.util.KalimaShahadatPlayer.isPlaying.collectAsState()
    val isPrayerPlaying by com.example.util.PrayerAudioNotifier.isPlayingAllahuAkbar.collectAsState()
    val prayerAlertText by com.example.util.PrayerAudioNotifier.currentPrayerAlert.collectAsState()

    androidx.compose.runtime.LaunchedEffect(Unit) {
        com.example.util.KalimaShahadatPlayer.playOnAppLaunch(context)
    }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        com.example.util.BookDownloadManager.downloadCompletedEvent.collect {
            val activity = context as? android.app.Activity
            if (activity != null) {
                com.example.util.AdMobManager.showInterstitial(activity)
            }
        }
    }

    val mainViewModel: MainViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    val bottomNavItems = listOf(
        com.example.ui.components.BottomNavItem(Screen.Library, Icons.Default.LocalLibrary, "library", "nav_library"),
        com.example.ui.components.BottomNavItem(Screen.HaramainLive, Icons.Default.LiveTv, "haramain_live", "nav_live"),
        com.example.ui.components.BottomNavItem(Screen.Home, Icons.Default.Home, "home", "nav_home"),
        com.example.ui.components.BottomNavItem(Screen.Quiz, Icons.Default.Quiz, "quiz", "nav_quiz"),
        com.example.ui.components.BottomNavItem(Screen.Profile, Icons.Default.Person, "profile", "nav_profile")
    )

    var isLiveFullscreen by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(currentRoute) {
        if (currentRoute != Screen.HaramainLive.route) {
            isLiveFullscreen = false
        } else {
            scope.launch { drawerState.close() }
        }
    }

    LaunchedEffect(isLiveFullscreen) {
        if (isLiveFullscreen) {
            scope.launch { drawerState.close() }
        }
    }

    val isTopLevelRoute = (bottomNavItems.any { it.screen.route == currentRoute } || currentRoute == Screen.AiAssistant.route) && !isLiveFullscreen

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !isLiveFullscreen && currentRoute != Screen.HaramainLive.route,
        scrimColor = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.45f),
        drawerContent = {
            if (!isLiveFullscreen && currentRoute != Screen.HaramainLive.route) {
                ModalDrawerSheet(
                    drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
                    drawerContainerColor = MaterialTheme.colorScheme.surface,
                    drawerTonalElevation = 6.dp,
                    windowInsets = WindowInsets.statusBars,
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.78f)
                        .widthIn(min = 270.dp, max = 340.dp)
                ) {
                    IslamicDrawerContent(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            scope.launch { drawerState.close() }
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onClose = {
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            contentWindowInsets = if (isLiveFullscreen) WindowInsets(0, 0, 0, 0) else ScaffoldDefaults.contentWindowInsets,
            topBar = {
                if (isTopLevelRoute && currentRoute != Screen.AiAssistant.route) {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(androidx.compose.ui.graphics.Color(0xFFECFDF5)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = null,
                                        tint = androidx.compose.ui.graphics.Color(0xFF059669),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = lStr("app_name"),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = androidx.compose.ui.graphics.Color(0xFF0F172A)
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Open Drawer Menu",
                                    tint = androidx.compose.ui.graphics.Color(0xFF0F172A)
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                navController.navigate(Screen.AiAssistant.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "AI Assistant",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(onClick = {
                                com.example.util.AppConfig.shareAppWithWebsite(context)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share Website",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                            actionIconContentColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            },
            bottomBar = {
                if (isTopLevelRoute && currentRoute != Screen.AiAssistant.route) {
                    com.example.ui.components.BaytulIlmBottomNavigationBar(
                        items = bottomNavItems,
                        currentRoute = currentRoute,
                        onItemSelected = { screen ->
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(if (isLiveFullscreen) Modifier else Modifier.padding(innerPadding))
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                composable(Screen.LanguageSelection.route) {
                    LanguageSelectionScreen(
                        onLanguageSelected = {
                            val currentUser = runCatching { FirebaseAuth.getInstance().currentUser }.getOrNull()
                            val targetRoute = when {
                                currentUser != null && !currentUser.isEmailVerified -> Screen.EmailVerification.route
                                currentUser != null -> Screen.Home.route
                                else -> Screen.Login.route
                            }
                            navController.navigate(targetRoute) {
                                popUpTo(Screen.LanguageSelection.route) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Screen.Splash.route) {
                    val userState by authViewModel.userState.collectAsState()
                    val isFirstLaunch by LanguageManager.isFirstLaunch.collectAsState()

                    androidx.compose.runtime.LaunchedEffect(userState, isFirstLaunch) {
                        val currentUser = runCatching { FirebaseAuth.getInstance().currentUser }.getOrNull()
                        val targetRoute = when {
                            isFirstLaunch -> Screen.LanguageSelection.route
                            currentUser != null && !currentUser.isEmailVerified -> Screen.EmailVerification.route
                            currentUser != null -> Screen.Home.route
                            else -> Screen.Login.route
                        }
                        navController.navigate(targetRoute) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }

                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        androidx.compose.material3.CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                composable(Screen.EmailVerification.route) {
                    EmailVerificationScreen(
                        authViewModel = authViewModel,
                        onNavigateToLogin = {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onNavigateToRegister = {
                            navController.navigate(Screen.Register.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Screen.Home.route) {
                    val userState by authViewModel.userState.collectAsState()
                    val currentUser = runCatching { FirebaseAuth.getInstance().currentUser }.getOrNull()

                    androidx.compose.runtime.LaunchedEffect(currentUser?.uid, currentUser?.isEmailVerified) {
                        if (currentUser != null && !currentUser.isEmailVerified) {
                            navController.navigate(Screen.EmailVerification.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    }

                    HomeScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.Library.route) {
                    LibraryScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.Offline.route) {
                    OfflineScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.Quiz.route) {
                    val quizViewModel: QuizViewModel = viewModel()
                    QuizScreen(
                        viewModel = quizViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.AiAssistant.route) {
                    val aiViewModel: AiViewModel = viewModel()
                    AiAssistantScreen(aiViewModel = aiViewModel, mainViewModel = mainViewModel)
                }

                composable(Screen.AiTeacher.route) {
                    val aiViewModel: AiViewModel = viewModel()
                    AiTeacherScreen(
                        aiViewModel = aiViewModel,
                        mainViewModel = mainViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.AsmaUlHusna.route) {
                    AsmaUlHusnaScreen(onNavigateBack = { navController.popBackStack() })
                }
                composable(Screen.AsmaUnNabi.route) {
                    AsmaUnNabiScreen(onNavigateBack = { navController.popBackStack() })
                }
                composable(Screen.HaramainLive.route) {
                    HaramainLiveScreen(
                        onNavigateBack = {
                            if (navController.previousBackStackEntry != null) {
                                navController.popBackStack()
                            }
                        },
                        isTopLevel = true,
                        onFullscreenChange = { isLiveFullscreen = it }
                    )
                }
                composable(Screen.DaimeAuqat.route) {
                    DaimeAuqatScreen(onNavigateBack = { navController.popBackStack() })
                }
                composable(Screen.IslamicCalendar.route) {
                    IslamicCalendarScreen(onNavigateBack = { navController.popBackStack() })
                }
                composable(Screen.QiblaCompass.route) {
                    QiblaCompassScreen(onNavigateBack = { navController.popBackStack() })
                }
                composable(Screen.Tasbeeh.route) {
                    TasbeehScreen(onNavigateBack = { navController.popBackStack() })
                }
                composable(Screen.QuranPak.route) {
                    QuranScreen(
                        onNavigate = { route -> navController.navigate(route) },
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.DuaBook.route) {
                    FamousDuasScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.FamousDuas.route) {
                    FamousDuasScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Tafaseer.route) {
                    TafseerScreen(
                        onNavigate = { route -> navController.navigate(route) },
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.TafseerDetail.route) { backStackEntry ->
                    val tafseerId = backStackEntry.arguments?.getString("tafseerId") ?: ""
                    TafseerDetailScreen(
                        tafseerId = tafseerId,
                        onNavigate = { route -> navController.navigate(route) },
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Fatawa.route) {
                    FatawaScreen(
                        onNavigate = { route -> navController.navigate(route) },
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.FatawaDetail.route) { backStackEntry ->
                    val fatawaId = backStackEntry.arguments?.getString("fatawaId") ?: ""
                    FatawaDetailScreen(
                        fatawaId = fatawaId,
                        onNavigate = { route -> navController.navigate(route) },
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Lughat.route) {
                    LughatScreen(
                        onNavigate = { route -> navController.navigate(route) },
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Profile.route) {
                    ProfileScreen(
                        viewModel = mainViewModel,
                        authViewModel = authViewModel,
                        onNavigate = { route ->
                            if (route == Screen.Login.route) {
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            } else {
                                navController.navigate(route)
                            }
                        }
                    )
                }

                composable(Screen.Login.route) {
                    LoginScreen(
                        authViewModel = authViewModel,
                        onNavigate = { route -> navController.navigate(route) },
                        onLoginSuccess = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable(Screen.Register.route) {
                    SignupScreen(
                        authViewModel = authViewModel,
                        onNavigate = { route -> navController.navigate(route) },
                        onSignupSuccess = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable(Screen.Signup.route) {
                    SignupScreen(
                        authViewModel = authViewModel,
                        onNavigate = { route -> navController.navigate(route) },
                        onSignupSuccess = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Signup.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable(Screen.ForgotPassword.route) {
                    ForgotPasswordScreen(
                        authViewModel = authViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.Darjat.route) {
                    DarjatScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(
                    route = Screen.DarjaDetail.route,
                    arguments = listOf(
                        androidx.navigation.navArgument("darjaName") { type = androidx.navigation.NavType.StringType },
                        androidx.navigation.navArgument("mode") {
                            type = androidx.navigation.NavType.StringType
                            defaultValue = "all"
                        }
                    )
                ) { backStackEntry ->
                    val darjaName = backStackEntry.arguments?.getString("darjaName") ?: ""
                    val initialMode = backStackEntry.arguments?.getString("mode") ?: "all"
                    BooksScreen(
                        darjaId = darjaName,
                        initialMode = initialMode,
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Subjects.route) {
                    SubjectsScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.SubjectDetail.route) { backStackEntry ->
                    val subjectName = backStackEntry.arguments?.getString("subjectName") ?: ""
                    LibraryScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.BookDetail.route) { backStackEntry ->
                    val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                    BookDetailScreen(
                        bookId = bookId,
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.BookViewer.route) { backStackEntry ->
                    val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                    BookViewerScreen(
                        bookId = bookId,
                        viewModel = mainViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Search.route) {
                    SearchScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.Bookmarks.route) {
                    BookmarksScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.Favorites.route) {
                    FavoritesScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.Recent.route) {
                    RecentScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }


                composable(Screen.Downloads.route) {
                    DownloadsScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.Settings.route) {
                    SettingsScreen(
                        authViewModel = authViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.About.route) {
                    AboutContactScreen()
                }

                composable(Screen.PrivacyPolicy.route) {
                    PrivacyPolicyScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                // Student Dashboard & LMS Routes
                composable(Screen.StudentDashboard.route) {
                    StudentDashboardScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.LmsStudyPlanner.route) {
                    LmsStudyPlannerScreen(viewModel = mainViewModel)
                }

                composable(Screen.LmsNotes.route) {
                    LmsNotesScreen(viewModel = mainViewModel)
                }

                composable(Screen.LmsAssignments.route) {
                    LmsAssignmentsScreen(viewModel = mainViewModel)
                }

                composable(Screen.LmsCertificates.route) {
                    LmsCertificatesScreen(viewModel = mainViewModel)
                }

                composable(Screen.LmsAchievements.route) {
                    LmsAchievementsScreen(viewModel = mainViewModel)
                }

                composable(Screen.LmsLeaderboard.route) {
                    LmsLeaderboardScreen(viewModel = mainViewModel)
                }

                composable(Screen.LmsProfileEdit.route) {
                    LmsProfileEditScreen(
                        viewModel = mainViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                // Digital Madrasa Extended Routes
                composable(Screen.LiveClasses.route) {
                    LiveClassesScreen(
                        lmsRepository = mainViewModel.lmsRepository,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.VideoCourses.route) {
                    VideoCoursesScreen(
                        lmsRepository = mainViewModel.lmsRepository,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.AudioLectures.route) {
                    AudioLecturesScreen(
                        lmsRepository = mainViewModel.lmsRepository,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.ExamResults.route) {
                    ExamResultsScreen(
                        lmsRepository = mainViewModel.lmsRepository,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.DiscussionForum.route) {
                    DiscussionForumScreen(
                        lmsRepository = mainViewModel.lmsRepository,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.DirectMessaging.route) {
                    DirectMessagingScreen(
                        lmsRepository = mainViewModel.lmsRepository,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.OcrAssistant.route) {
                    val aiViewModel: AiViewModel = viewModel()
                    OcrAssistantScreen(
                        aiViewModel = aiViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.ParentDashboard.route) {
                    ParentDashboardScreen(
                        lmsRepository = mainViewModel.lmsRepository,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.AdminBulkOps.route) {
                    AdminBulkOpsScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                // Admin & CMS Routes
                composable(Screen.AdminDashboard.route) {
                    AdminDashboardScreen(
                        viewModel = mainViewModel,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Screen.AdminRoleManagement.route) {
                    AdminRoleManagementScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminBookManagement.route) {
                    AdminBookManagementScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminShuroohTranslations.route) {
                    AdminShuroohTranslationsScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminQuizManagement.route) {
                    AdminQuizManagementScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminContentManagement.route) {
                    AdminContentManagementScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminPushNotifications.route) {
                    AdminPushNotificationsScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminUserManagement.route) {
                    AdminUserManagementScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminAnalytics.route) {
                    AdminAnalyticsScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminCertificates.route) {
                    AdminCertificatesScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminBackupSettings.route) {
                    AdminBackupSettingsScreen(viewModel = mainViewModel)
                }

                composable(Screen.AdminSecurity.route) {
                    AdminSecurityScreen(viewModel = mainViewModel)
                }
            }

            AnimatedVisibility(
                visible = !isLiveFullscreen && (isPrayerPlaying || prayerAlertText != null),
                enter = fadeIn() + slideInVertically { -it },
                exit = fadeOut() + slideOutVertically { -it },
                modifier = Modifier
                    .align(androidx.compose.ui.Alignment.TopCenter)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                androidx.compose.material3.Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 8.dp,
                    tonalElevation = 6.dp,
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Allahu Akbar Prayer Alert",
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            androidx.compose.foundation.layout.Column {
                                Text(
                                    text = "اللهُ أَكْبَرُ - وقتِ نماز داخل ہو گیا",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Text(
                                    text = prayerAlertText ?: "حی علی الصلاۃ - نماز کی تیاری کریں",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        IconButton(
                            onClick = { com.example.util.PrayerAudioNotifier.dismissAlert(context) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "بند کریں (Dismiss)",
                                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = !isLiveFullscreen && (isKalimaPlaying && !isPrayerPlaying),
                enter = fadeIn() + slideInVertically { -it },
                exit = fadeOut() + slideOutVertically { -it },
                modifier = Modifier
                    .align(androidx.compose.ui.Alignment.TopCenter)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                androidx.compose.material3.Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 6.dp,
                    tonalElevation = 4.dp,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Salawat Recitation",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            androidx.compose.foundation.layout.Column {
                                Text(
                                    text = "صَلُّوا عَلَى النَّبِيِّ ﷺ",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "اَللّٰهُمَّ صَلِّ وَسَلِّمْ عَلَىٰ سَيِّدِنَا مُحَمَّدٍ",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        IconButton(
                            onClick = { com.example.util.KalimaShahadatPlayer.stop() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "بند کریں (Stop)",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
}

@Composable
fun RasheedIslamicApp() {
    BaytulIlmApp()
}
