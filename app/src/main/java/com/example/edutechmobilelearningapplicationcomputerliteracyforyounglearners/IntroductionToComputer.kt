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
 * IntroductionToComputerScreen - Detailed lesson content for the "Introduction to Computer" topic.
 */
@Composable
fun IntroductionToComputerScreen(
    onBackClick: () -> Unit,
    onCheckProgressClick: () -> Unit = {},
    viewModel: CourseViewModel? = if (LocalInspectionMode.current) null else viewModel()
) {
    IntroductionToComputerContent(
        onBackClick = onBackClick,
        onCheckProgressClick = onCheckProgressClick,
        onMarkCompleted = {
            viewModel?.markCourseCompleted(ProgressTracker.COURSE_INTRO)
        },
        viewModel = viewModel
    )
}

/**
 * IntroductionToComputerContent - Stateless version of the screen for previews and testing.
 */
@Composable
fun IntroductionToComputerContent(
    onBackClick: () -> Unit, 
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
        IntroductionToComputerAssessmentScreen(
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
                        listOf(Color(0xFF7B6FE8), Color(0xFFA173FA))
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
                        text = "Introduction to Computer",
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

                // Content Card
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
                        // Video Lesson section
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
                            VideoPlayer(
                                videoResId = R.raw.introductiontocomputer,
                                onVideoFinished = { 
                                    isVideoFinished = true 
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(40.dp))

                        // Interactive Assessment Button - only activated after watching the video fully
                        Button(
                            onClick = { showAssessment = true },
                            enabled = isVideoFinished,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF48E6CCB),
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

                        ComputerLessonTopic(
                            title = "What is a Computer?",
                            description = "A computer is a smart machine that follows instructions to help us learn, create, and solve problems."
                        )

                        ComputerLessonTopic(
                            title = "Types of Computers",
                            description = "Computers come in different shapes and sizes.",
                            items = listOf(
                                "• Desktop - stays on a desk",
                                "• Laptop - easy to carry",
                                "• Tablet - works with a touch screen",
                                "• Smartphone - fits in your pocket"
                            )
                        )

                        ComputerLessonTopic(
                            title = "What Can Computers Do?",
                            description = "Computers can help us write, draw, do math, watch videos, play games, and learn new things."
                        )

                        ComputerLessonTopic(
                            title = "Where Are Computers Used?",
                            description = "You can find computers at home, in schools, hospitals, stores, offices, and many other places."
                        )
                    }
                }
            }
        }
    }
}

/**
 * A reusable Video Player component using Media3 ExoPlayer with landscape fullscreen support.
 */
@Composable
fun VideoPlayer(videoResId: Int, onVideoFinished: () -> Unit) {
    val context = LocalContext.current
    val isInspectionMode = LocalInspectionMode.current
    val currentOnVideoFinished by rememberUpdatedState(onVideoFinished)

    var isFullscreen by remember { mutableStateOf(false) }

    // Orientation and system UI logic
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
            modifier = Modifier
                .widthIn(max = 750.dp)
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    text = "Video Lesson",
                    color = Color.White,
                    fontFamily = Kavoon,
                    fontSize = 16.sp
                )
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

    // BGM Management: Video Playback & Fullscreen
    val isPlaying = remember { mutableStateOf(false) }
    
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
            exoPlayer.release()
            BGMManager.setForcedSilence(false)
        }
    }

    LaunchedEffect(isFullscreen) {
        BGMManager.setForcedSilence(isPlaying.value || isFullscreen)
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
private fun ComputerLessonTopic(
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
fun IntroToComputerPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        IntroductionToComputerContent(onBackClick = {}, onMarkCompleted = {})
    }
}
