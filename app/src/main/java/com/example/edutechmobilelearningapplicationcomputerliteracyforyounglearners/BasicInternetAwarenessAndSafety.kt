package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import android.app.Activity
import android.content.pm.ActivityInfo
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
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon

/**
 * BasicInternetAwarenessAndSafetyScreen - Detailed lesson content for "Basic Internet Awareness and Safety".
 */
@Composable
fun BasicInternetAwarenessAndSafetyScreen(
    onBackClick: () -> Unit,
    onCheckProgressClick: () -> Unit = {},
    viewModel: CourseViewModel? = if (LocalInspectionMode.current) null else viewModel()
) {
    BasicInternetAwarenessAndSafetyContent(
        onBackClick = onBackClick,
        onCheckProgressClick = onCheckProgressClick,
        onMarkCompleted = {
            viewModel?.markCourseCompleted(ProgressTracker.COURSE_SAFETY)
        },
        viewModel = viewModel
    )
}

/**
 * BasicInternetAwarenessAndSafetyContent - Stateless version for previews and testing.
 */
@Composable
fun BasicInternetAwarenessAndSafetyContent(
    onBackClick: () -> Unit, 
    onCheckProgressClick: () -> Unit = {},
    onMarkCompleted: () -> Unit,
    viewModel: CourseViewModel? = null
) {
    var showAssessment by remember { mutableStateOf(false) }
    var isVideoFinished by remember { mutableStateOf(false) }

    if (showAssessment) {
        BasicInternetAwarenessAndSafetyAssessmentScreen(
            onBackClick = { showAssessment = false },
            onCheckProgressClick = onCheckProgressClick,
            viewModel = viewModel
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF7B6FE8),
                            Color(0xFFA173FA)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = 950.dp) // Slightly wider to accommodate longer title
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
                        text = "Internet Awareness & Safety",
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

                // Main Content Card
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
                            SafetyVideoPlayer(
                                videoResId = R.raw.onlinesafety,
                                onVideoFinished = { 
                                    isVideoFinished = true 
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Assessment Button - Enabled only after video finishes
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

                        SafetyLessonTopic(
                            title = "Be Kind Online",
                            description = "Use friendly words online, just as you would in person. Never tease, bully, or hurt someone's feelings."
                        )

                        SafetyLessonTopic(
                            title = "Take Breaks",
                            description = "Give your eyes, brain, and body a rest from screens. Stand up, stretch, or play outside for a while."
                        )

                        SafetyLessonTopic(
                            title = "Think Before You Post",
                            description = "Pause before sharing. Do not post private information, photos, or messages that could upset someone."
                        )

                        SafetyLessonTopic(
                            title = "Don't Believe Everything You See Online",
                            description = "Some things online can be wrong or made up. Ask a trusted adult when something seems strange or confusing."
                        )

                        SafetyLessonTopic(
                            title = "Ask Before You Download or Buy",
                            description = "Always ask a parent, teacher, or trusted adult before downloading an app, clicking a link, or buying something."
                        )

                        SafetyLessonTopic(
                            title = "Good Internet Habits",
                            description = "Smart choices help keep you safe and happy online.",
                            items = listOf(
                                "• Keep passwords private",
                                "• Tell an adult if something feels wrong",
                                "• Balance screen time with other activities"
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * SafetyVideoPlayer - Reusable Media3 ExoPlayer component with landscape fullscreen support.
 */
@Composable
fun SafetyVideoPlayer(videoResId: Int, onVideoFinished: () -> Unit) {
    val context = LocalContext.current
    val isInspectionMode = LocalInspectionMode.current
    val currentOnVideoFinished by rememberUpdatedState(onVideoFinished)

    var isFullscreen by remember { mutableStateOf(false) }

    // Handle orientation and system UI changes
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

    if (isInspectionMode) {
        Box(
            modifier = Modifier.widthIn(max = 750.dp).fillMaxWidth().aspectRatio(16 / 9f).background(Color.Black.copy(0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(64.dp))
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

    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    currentOnVideoFinished()
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    Box(
        modifier = if (isFullscreen) {
            Modifier.fillMaxSize().background(Color.Black)
        } else {
            Modifier.widthIn(max = 750.dp).fillMaxWidth().aspectRatio(16 / 9f).clip(RoundedCornerShape(12.dp))
        }
    ) {
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = true
                    setBackgroundColor(android.graphics.Color.BLACK)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        IconButton(
            onClick = { isFullscreen = !isFullscreen },
            modifier = Modifier.align(Alignment.TopEnd).padding(if (isFullscreen) 16.dp else 8.dp)
        ) {
            Icon(
                imageVector = if (isFullscreen) {
                    Icons.Default.FullscreenExit
                } else {
                    Icons.Default.Fullscreen
                },
                contentDescription = if (isFullscreen) "Exit Fullscreen" else "Enter Fullscreen",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun SafetyLessonTopic(
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
fun BasicInternetAwarenessAndSafetyPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        BasicInternetAwarenessAndSafetyContent(onBackClick = {}, onMarkCompleted = {})
    }
}
