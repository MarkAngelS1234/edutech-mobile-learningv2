package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.annotation.OptIn
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon

/**
 * ComputerHardware - Detailed lesson content for the "Computer Hardware" topic.
 */
@Composable
fun ComputerHardware(
    onBackClick: () -> Unit,
    onCloseAssessment: () -> Unit = {},
    onCheckProgressClick: () -> Unit = {},
    viewModel: CourseViewModel? = if (LocalInspectionMode.current) null else viewModel()
) {
    ComputerHardwareContent(
        onBackClick = onBackClick,
        onCloseAssessment = onCloseAssessment,
        onCheckProgressClick = onCheckProgressClick,
        onMarkCompleted = {
            viewModel?.markCourseCompleted(ProgressTracker.COURSE_HARDWARE)
        },
        viewModel = viewModel
    )
}

/**
 * ComputerHardwareContent - Stateless version for previews and testing.
 */
@Composable
fun ComputerHardwareContent(
    onBackClick: () -> Unit,
    onCloseAssessment: () -> Unit = {},
    onCheckProgressClick: () -> Unit = {},
    onMarkCompleted: () -> Unit,
    viewModel: CourseViewModel? = null
) {
    var showAssessment by remember { mutableStateOf(false) }
    var isVideoFinished by remember { mutableStateOf(false) }

    // BGM Management: Lesson Mode
    DisposableEffect(Unit) {
        BGMManager.setLessonMode(true)
        onDispose {
            BGMManager.setLessonMode(false)
        }
    }

    if (showAssessment) {
        ComputerHardwareAssessmentScreen(
            onBackClick = { showAssessment = false },
            onCloseAssessment = onCloseAssessment,
            onCheckProgressClick = onCheckProgressClick,
            viewModel = viewModel
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF4A90E2), Color(0xFFA173FA))
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = 900.dp)
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                // Header with Centered Title
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 20.dp)
                ) {
                    EduTechBackButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    Text(
                        text = "Introduction to Hardware",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = Kavoon,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 110.dp)
                    )
                }

                // Main Content Card (White Background)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // --- VIDEO LESSON SECTION ---
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF4A90E2), Color(0xFFA173FA))
                                    )
                                )
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            VideoPlayerHardware(
                                videoResId = R.raw.computerhardware,
                                onVideoFinished = {
                                    isVideoFinished = true
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Assessment Button
                        Button(
                            onClick = { showAssessment = true },
                            enabled = isVideoFinished,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF8E6CCB),
                                disabledContainerColor = Color(0xFFB0BEC5)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(
                                text = if (isVideoFinished) "Go to Learning Assessment" else "Watch Video to Unlock Assessment",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontFamily = Kavoon
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        HardwareLessonTopic(
                            title = "What is Computer Hardware?",
                            description = "Hardware is the physical part of a computer that you can see and touch."
                        )

                        HardwareLessonTopic(
                            title = "Input Devices",
                            description = "Input devices help us give information and instructions to the computer.",
                            items = listOf(
                                "• Keyboard - types letters and numbers",
                                "• Mouse - points, clicks, and selects"
                            )
                        )

                        HardwareLessonTopic(
                            title = "Output Devices",
                            description = "Output devices show us the computer's work.",
                            items = listOf(
                                "• Monitor - shows pictures and words",
                                "• Speakers - play sounds",
                                "• Printer - puts work on paper"
                            )
                        )

                        HardwareLessonTopic(
                            title = "Processing",
                            description = "The CPU is like the computer's brain. It follows instructions and helps the computer work quickly.",
                            items = listOf(
                                "• CPU - processes information",
                                "• System unit - holds important computer parts"
                            )
                        )

                        HardwareLessonTopic(
                            title = "Other Common Computer Parts",
                            description = "Webcams, microphones, headphones, and storage devices are other parts that help a computer do more."
                        )
                    }
                }
            }
        }
    }
}

/**
 * Reusable Video Player for Hardware screen.
 *
 * Fullscreen is rendered in its own Dialog window so it is not constrained by the
 * parent's width/padding/scroll layout (that was why "fullscreen" previously looked
 * like a small centered block instead of truly filling the screen).
 *
 * Playback is tied to the host lifecycle and to this composable's presence in the
 * composition, so navigating away (or the app going to background) reliably stops
 * audio instead of leaving it playing in the background.
 */
@Composable
fun VideoPlayerHardware(videoResId: Int, onVideoFinished: () -> Unit) {
    val context = LocalContext.current
    val isInspectionMode = LocalInspectionMode.current
    val currentOnVideoFinished by rememberUpdatedState(onVideoFinished)
    val lifecycleOwner = LocalLifecycleOwner.current

    var isFullscreen by remember { mutableStateOf(false) }

    if (isInspectionMode) {
        Box(
            modifier = Modifier
                .widthIn(max = 750.dp)
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(64.dp))
                Text("Video Lesson", color = Color.White, fontFamily = Kavoon)
            }
        }
        return
    }

    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri("android.resource://${context.packageName}/$videoResId")
            setMediaItem(mediaItem)
            setPlaybackParameters(PlaybackParameters(1.0f))
            playWhenReady = false
            prepare()
        }
    }

    val isPlaying = remember { mutableStateOf(false) }

    // Pause playback whenever the host Activity/screen goes into the background
    // (app backgrounded, screen turned off, etc.) so audio never keeps running silently.
    DisposableEffect(lifecycleOwner, exoPlayer) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE || event == Lifecycle.Event.ON_STOP) {
                exoPlayer.pause()
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    // Player listener + guaranteed stop/release when this composable leaves the
    // composition (i.e. the user navigates away from this lesson screen). This is
    // what fixes audio continuing to play after leaving the tab, and also ensures a
    // fresh player is created (no duplicate/overlapping audio) if the lesson is reopened.
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying.value = playing
                BGMManager.setForcedSilence(playing || isFullscreen)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    currentOnVideoFinished()
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.pause()
            exoPlayer.stop()
            exoPlayer.clearMediaItems()
            exoPlayer.release()
            BGMManager.setForcedSilence(false)
        }
    }

    LaunchedEffect(isFullscreen) {
        BGMManager.setForcedSilence(isPlaying.value || isFullscreen)
    }

    // Orientation + system bars for fullscreen, applied to the underlying Activity window
    // (the fullscreen Dialog sits above it, but hiding bars here keeps them hidden while
    // the dialog is open and restores them correctly when it closes).
    DisposableEffect(isFullscreen) {
        val activity = context as? Activity
        val window = activity?.window
        if (activity != null && window != null) {
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            if (isFullscreen) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                insetsController.hide(WindowInsetsCompat.Type.systemBars())
                insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                insetsController.show(WindowInsetsCompat.Type.systemBars())
            }
        }
        onDispose {
            if (isFullscreen) {
                val activityDispose = context as? Activity
                val windowDispose = activityDispose?.window
                activityDispose?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                if (windowDispose != null) {
                    WindowCompat.getInsetsController(windowDispose, windowDispose.decorView)
                        .show(WindowInsetsCompat.Type.systemBars())
                }
            }
        }
    }

    // --- Inline (non-fullscreen) player, same size/position as before ---
    if (!isFullscreen) {
        Box(
            modifier = Modifier
                .widthIn(max = 750.dp)
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clip(RoundedCornerShape(12.dp))
        ) {
            PlayerSurfaceHardware(
                exoPlayer = exoPlayer,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = { isFullscreen = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Fullscreen,
                    contentDescription = "Enter Fullscreen",
                    tint = Color.White
                )
            }
        }
    }

    // --- True fullscreen player, rendered in its own full-screen Dialog window so it
    // is never constrained by the lesson layout's max width, padding, or scroll container ---
    if (isFullscreen) {
        Dialog(
            onDismissRequest = { isFullscreen = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
            SideEffect {
                dialogWindow?.let { window ->
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    WindowCompat.setDecorFitsSystemWindows(window, false)
                    val controller = WindowCompat.getInsetsController(window, window.decorView)
                    controller.hide(WindowInsetsCompat.Type.systemBars())
                    controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                // fillMaxSize + RESIZE_MODE_FIT letterboxes instead of stretching/cropping,
                // so the video's real aspect ratio is preserved edge-to-edge.
                PlayerSurfaceHardware(
                    exoPlayer = exoPlayer,
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = { isFullscreen = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FullscreenExit,
                        contentDescription = "Exit Fullscreen",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Thin AndroidView wrapper around PlayerView, shared by the inline and fullscreen
 * layouts so both attach to the same single ExoPlayer instance (no duplicate players,
 * no overlapping audio).
 */
@OptIn(markerClass = [UnstableApi::class])
@Composable
private fun PlayerSurfaceHardware(exoPlayer: ExoPlayer, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = true
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                setBackgroundColor(android.graphics.Color.BLACK)
            }
        },
        update = { view ->
            view.player = exoPlayer
        },
        onRelease = { view ->
            view.player = null
        },
        modifier = modifier
    )
}

@Composable
private fun HardwareLessonTopic(
    title: String,
    description: String,
    items: List<String>? = null
) {
    Text(
        text = title,
        color = Color.Black,
        fontSize = 18.sp,
        fontFamily = Kavoon,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF4A90E2), Color(0xFFA173FA))
                )
            )
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.White.copy(0.9f),
                lineHeight = 20.sp
            )
            if (items != null) {
                Spacer(modifier = Modifier.height(8.dp))
                items.forEach { item ->
                    Text(
                        text = item,
                        fontSize = 14.sp,
                        color = Color.White.copy(0.95f),
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Preview(showBackground = true)
@Composable
fun ComputerHardwarePreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ComputerHardwareContent(onBackClick = {}, onMarkCompleted = {})
    }
}