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
 * ComputerHardware - Detailed lesson content for the "Computer Hardware" topic.
 */
@Composable
fun ComputerHardware(
    onBackClick: () -> Unit,
    onCheckProgressClick: () -> Unit = {},
    viewModel: CourseViewModel? = if (LocalInspectionMode.current) null else viewModel()
) {
    ComputerHardwareContent(
        onBackClick = onBackClick,
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
    onCheckProgressClick: () -> Unit = {},
    onMarkCompleted: () -> Unit,
    viewModel: CourseViewModel? = null
) {
    var showAssessment by remember { mutableStateOf(false) }
    var isVideoFinished by remember { mutableStateOf(false) }

    if (showAssessment) {
        ComputerHardwareAssessmentScreen(
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
                modifier = Modifier.fillMaxSize()
            ) {
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
                        text = "Introduction to Hardware",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = Kavoon,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
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
                                .padding(16.dp)
                        ) {
                            VideoPlayerHardware(
                                videoResId = R.raw.computerhardware,
                                onVideoFinished = { 
                                    isVideoFinished = true 
                                    onMarkCompleted()
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
 */
@Composable
fun VideoPlayerHardware(videoResId: Int, onVideoFinished: () -> Unit) {
    val context = LocalContext.current
    val isInspectionMode = LocalInspectionMode.current
    val currentOnVideoFinished by rememberUpdatedState(onVideoFinished)

    if (isInspectionMode) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
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
