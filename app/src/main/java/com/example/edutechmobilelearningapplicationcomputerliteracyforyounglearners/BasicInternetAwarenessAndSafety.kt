package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
fun BasicInternetAwarenessAndSafetyScreen(onBackClick: () -> Unit, viewModel: CourseViewModel = viewModel()) {
    var showAssessment by remember { mutableStateOf(false) }
    var isVideoFinished by remember { mutableStateOf(false) }

    if (showAssessment) {
        BasicInternetAwarenessAndSafetyAssessmentScreen(onBackClick = { showAssessment = false })
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF4A90E2), Color(0xFF50E3C2))
                    )
                )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header with Centered Title
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Internet Awareness & Safety",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = Kavoon,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
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
                                        listOf(Color(0xFF4A90E2), Color(0xFF50E3C2))
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            SafetyVideoPlayer(
                                videoResId = R.raw.onlinesafety,
                                onVideoFinished = { 
                                    isVideoFinished = true 
                                    viewModel.markCourseCompleted(ProgressTracker.COURSE_SAFETY)
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Assessment Button - Enabled only after video finishes
                        Button(
                            onClick = { showAssessment = true },
                            enabled = isVideoFinished,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4A90E2),
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

                        // --- LEARNING GOALS SECTION ---
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF4A90E2).copy(0.1f), Color(0xFF50E3C2).copy(0.1f))
                                    )
                                )
                                .padding(20.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Learning Goals:",
                                    color = Color(0xFF4A90E2),
                                    fontFamily = Kavoon,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                listOf(
                                    "1. Understand how to stay safe online.",
                                    "2. Learn smart safety rules for kids.",
                                    "3. Protect your personal information."
                                ).forEach { goal ->
                                    Text(
                                        text = goal,
                                        fontSize = 13.sp,
                                        color = Color.DarkGray,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // --- OVERVIEW SECTION ---
                        Text(
                            text = "Overview",
                            color = Color(0xFF4A90E2),
                            fontSize = 22.sp,
                            fontFamily = Kavoon,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "The internet is a wonderful place to learn and play, but it's important to know how to stay safe. Just like in the real world, there are rules to follow online to keep you and your family safe.",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            lineHeight = 20.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

/**
 * SafetyVideoPlayer - Reusable Media3 ExoPlayer component.
 */
@Composable
fun SafetyVideoPlayer(videoResId: Int, onVideoFinished: () -> Unit) {
    val context = LocalContext.current
    val isInspectionMode = LocalInspectionMode.current
    val currentOnVideoFinished by rememberUpdatedState(onVideoFinished)

    if (isInspectionMode) {
        Box(
            modifier = Modifier.fillMaxWidth().height(200.dp).background(Color.Black.copy(0.6f)),
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

    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = true
                setBackgroundColor(android.graphics.Color.BLACK)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
    )
}

@Preview(showBackground = true)
@Composable
fun BasicInternetAwarenessAndSafetyPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        BasicInternetAwarenessAndSafetyScreen(onBackClick = {})
    }
}
