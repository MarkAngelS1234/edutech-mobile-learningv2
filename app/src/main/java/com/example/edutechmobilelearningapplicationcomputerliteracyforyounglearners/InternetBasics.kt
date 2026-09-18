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
 * InternetBasicsScreen - Detailed lesson content for "Internet Basics".
 */
@Composable
fun InternetBasicsScreen(
    onBackClick: () -> Unit, 
    onCheckProgressClick: () -> Unit = {},
    viewModel: CourseViewModel = viewModel()
) {
    InternetBasicsContent(
        onBackClick = onBackClick,
        onCheckProgressClick = onCheckProgressClick,
        onMarkCompleted = {
            viewModel.markCourseCompleted(ProgressTracker.COURSE_INTERNET)
        },
        viewModel = viewModel
    )
}

/**
 * InternetBasicsContent - Stateless version of the screen for easier testing and previews.
 */
@Composable
fun InternetBasicsContent(
    onBackClick: () -> Unit, 
    onCheckProgressClick: () -> Unit = {},
    onMarkCompleted: () -> Unit,
    viewModel: CourseViewModel? = null
) {
    var showAssessment by remember { mutableStateOf(false) }
    var isVideoFinished by remember { mutableStateOf(false) }

    if (showAssessment) {
        InternetBasicsAssessmentScreen(
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
                // --- HEADER SECTION ---
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
                        text = "Internet Basics",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = Kavoon,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                // --- MAIN CONTENT CARD ---
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
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
                            InternetVideoPlayer(
                                videoResId = R.raw.internetbacis,
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

                        LessonTopic(
                            title = "What is the Internet?",
                            description = "The Internet is a giant network that lets computers and people share information from all around the world."
                        )

                        LessonTopic(
                            title = "Devices That Connect to the Internet",
                            description = "Phones, tablets, computers, and smart TVs can go online when they are connected to the Internet."
                        )

                        LessonTopic(
                            title = "Wi-Fi and Internet Connection",
                            description = "Wi-Fi is a wireless signal that helps your device connect to the Internet without using a cable."
                        )

                        LessonTopic(
                            title = "What Can We Do Online?",
                            description = "The Internet can help us do many useful and fun things!",
                            items = listOf(
                                "• Search for information",
                                "• Watch videos",
                                "• Communicate with others",
                                "• Learn new things",
                                "• Play games"
                            )
                        )

                        LessonTopic(
                            title = "Basic Internet Use",
                            description = "Use a browser to visit websites. Ask a trusted adult for help, and never share your name, address, or password online."
                        )
                    }
                }
            }
        }
    }
}

/**
 * InternetVideoPlayer - Reusable Media3 ExoPlayer component.
 */
@Composable
fun InternetVideoPlayer(videoResId: Int, onVideoFinished: () -> Unit) {
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

/**
 * Reusable topic heading and gradient content card.
 */
@Composable
private fun LessonTopic(
    title: String,
    description: String? = null,
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
            if (description != null) {
                Text(text = description, fontSize = 14.sp, color = Color.White.copy(0.9f), lineHeight = 20.sp)
            }
            if (items != null) {
                if (description != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items.forEach { point ->
                    Text(text = point, fontSize = 14.sp, color = Color.White.copy(0.95f), modifier = Modifier.padding(vertical = 2.dp))
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Preview(showBackground = true)
@Composable
fun InternetBasicsPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        InternetBasicsContent(onBackClick = {}, onMarkCompleted = {})
    }
}
