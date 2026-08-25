package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import android.media.MediaPlayer
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon
import kotlinx.coroutines.delay
import kotlin.math.PI

/* ---------------- NAVIGATOR ---------------- */

@Composable
fun DashboardAppNavigator() {
    val context = LocalContext.current

    // --- CONFIGURATION (EDITABLE) ---
    var bgmVolume by remember { mutableStateOf(0.8f) } // Shared BGM Volume State
    val loadingTime = 4000L // Editable duration for the loading screen in ms
    // --------------------------------

    // Initialize MediaPlayer for wreckitralph.mp3
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.wreckitralph).apply {
            isLooping = true // The music will repeat after it ends
            setVolume(bgmVolume, bgmVolume)
        }
    }
    // Sync MediaPlayer volume whenever bgmVolume state changes
    LaunchedEffect(bgmVolume) {
        mediaPlayer.setVolume(bgmVolume, bgmVolume)
    }

    // Automatically play music when the navigator starts and release it when it's closed
    DisposableEffect(Unit) {
        mediaPlayer.start()
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    // Requirement 1: Directly boot into DashboardEntranceScreen
    var screenState by remember { mutableStateOf("entrance") }

    AnimatedContent(
        targetState = screenState,
        transitionSpec = {
            fadeIn(tween(500)) togetherWith fadeOut(tween(500))
        },
        label = "screenTransition"
    ) { target ->
        when (target) {
            "entrance" -> DashboardEntranceScreen(
                loadingTime = loadingTime,
                onStartClick = { screenState = "main_menu" }
            )
            "main_menu" -> MainMenuScreen(
                bgmVolume = bgmVolume,
                onBgmVolumeChange = { bgmVolume = it },
                onCoursesClick = { screenState = "computer_grades" }
            )
            "computer_grades" -> ComputerGradesScreen(
                onBackClick = { screenState = "main_menu" }
            )
        }
    }
}

/* ---------------- ENTRANCE SCREEN ---------------- */

@Composable
fun DashboardEntranceScreen(
    loadingTime: Long = 4000L,
    onStartClick: () -> Unit
) {
    // Requirement 2: Integrate local loading state
    var isLoading by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        val totalSteps = 100
        val delayPerStep = loadingTime / totalSteps
        for (i in 1..totalSteps) {
            delay(delayPerStep)
            progress = i / 100f
        }
        delay(200) // Small pause at 100%
        isLoading = false
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Border circulating animation
    val borderRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "borderRotation"
    )

    // Continuous Forward Rotation for the first icon
    val rotationForward by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotationForward"
    )

    // Continuous Backward Rotation for the second icon
    val rotationBackward by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotationBackward"
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "press"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF4A90E2), Color(0xFF50E3C2))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            // Main Title Area with Icons behind the text
            Box(contentAlignment = Alignment.Center) {
                // Rotating Settings Icons
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .offset(x = 150.dp, y = (-30).dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_settings),
                        contentDescription = null,
                        modifier = Modifier
                            .size(71.dp)
                            .rotate(rotationForward)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.icon_settings),
                        contentDescription = null,
                        modifier = Modifier
                            .size(59.dp)
                            .offset(x = 15.dp, y = 48.dp)
                            .rotate(rotationBackward)
                    )
                }

                // Title Text in front
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.wrapContentSize()
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.cshap_e),
                            contentDescription = null,
                            modifier = Modifier
                                .size(110.dp)
                                .offset(x = (-67).dp, y = 2.dp)
                        )
                        Text(
                            text = "EduTech",
                            fontSize = 64.sp,
                            fontFamily = Kavoon,
                            color = Color.White,
                            lineHeight = 64.sp
                        )
                    }
                    Text(
                        text = "Learning",
                        fontSize = 32.sp,
                        fontFamily = Kavoon,
                        color = Color.White,
                        modifier = Modifier.offset(y = (-27).dp, x = (-5).dp),
                        lineHeight = 32.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Computer Literacy for Young Learners",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))

            // Requirement 3, 4, 6: Transition between Loading and Button
            AnimatedContent(
                targetState = isLoading,
                transitionSpec = {
                    if (!targetState) {
                        // Loading finishes -> Loading fades out -> 'Press to Start' button fades/slides up
                        (fadeIn(animationSpec = tween(600)) + slideInVertically(
                            animationSpec = tween(600),
                            initialOffsetY = { it / 2 }
                        )) togetherWith fadeOut(animationSpec = tween(400))
                    } else {
                        fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
                    }
                },
                label = "loadingToButton"
            ) { loading ->
                if (loading) {
                    // Requirement 5: Reusable LoadingComponent from LoadingScreen.kt
                    LoadingComponent(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(110.dp) // Maintain consistent height to avoid jumps
                    )
                } else {
                    Button(
                        onClick = onStartClick,
                        interactionSource = interactionSource,
                        modifier = Modifier
                            .scale(pulseScale * pressedScale)
                            .height(55.dp)
                            .widthIn(max = 320.dp)
                            .fillMaxWidth(0.8f)
                            .drawWithContent {
                                drawContent()
                                val strokeWidth = 3.dp.toPx()
                                val cornerRadiusPx = 30.dp.toPx()
                                val inset = strokeWidth / 2
                                val drawSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                                val actualRadius = cornerRadiusPx - inset
                                val perimeter = 2 * (drawSize.width + drawSize.height) + (2 * PI.toFloat() - 8) * actualRadius
                                val gapLength = 100f
                                val dashLength = perimeter - gapLength
                                val phase = borderRotation * perimeter
                                drawRoundRect(
                                    brush = Brush.linearGradient(
                                        colors = listOf(Color(0xFF4A90E2), Color(0xFF50E3C2)),
                                        start = Offset.Zero,
                                        end = Offset(size.width, size.height)
                                    ),
                                    topLeft = Offset(inset, inset),
                                    size = drawSize,
                                    cornerRadius = CornerRadius(actualRadius),
                                    style = Stroke(
                                        width = strokeWidth,
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round,
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(dashLength, gapLength),
                                            phase
                                        )
                                    )
                                )
                            },
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF4A90E2)
                        )
                    ) {
                        Text(
                            text = "Press to Start!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEntrance() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        DashboardEntranceScreen(onStartClick = {})
    }
}
