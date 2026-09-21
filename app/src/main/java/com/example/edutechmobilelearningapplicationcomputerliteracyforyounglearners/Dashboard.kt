package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import android.annotation.SuppressLint
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
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.R
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.airbnb.lottie.compose.*
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon
import kotlinx.coroutines.delay
import kotlin.math.PI

/* ---------------- NAVIGATOR ---------------- */

@Composable
fun DashboardAppNavigator() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // --- CONFIGURATION (EDITABLE) ---
    val loadingTime = 4000L // Editable duration for the loading screen in ms
    // --------------------------------

    // Initialize BGMManager
    DisposableEffect(Unit) {
        BGMManager.initialize(context.applicationContext)
        onDispose {
            BGMManager.release()
        }
    }

    // Handle App Lifecycle for BGM
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> BGMManager.setAppBackgrounded(false)
                Lifecycle.Event.ON_PAUSE -> BGMManager.setAppBackgrounded(true)
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
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
                bgmVolume = BGMManager.baseVolume,
                onBgmVolumeChange = { BGMManager.baseVolume = it; BGMManager.applyVolume() },
                onCoursesClick = { screenState = "computer_grades" }
            )
            "computer_grades" -> ComputerGradesScreen(
                onBackClick = { screenState = "main_menu" }
            )
        }
    }
}

/* ---------------- ENTRANCE SCREEN ---------------- */

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DashboardEntranceScreen(
    loadingTime: Long = 4000L,
    initialLoading: Boolean = true,
    onStartClick: () -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()
    )
    {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        // Proportional scaling factor based on a reference width
        val scaleFactor = (screenWidth / 800.dp).coerceIn(1f, 1.5f)

        // Requirement 2: Integrate local loading state
        var isLoading by remember { mutableStateOf(initialLoading) }
        var progress by remember { mutableStateOf(if (initialLoading) 0f else 1f) }

        LaunchedEffect(initialLoading) {
            if (!initialLoading) return@LaunchedEffect
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

        // Lottie Character Animation Setup (@or_byyy.json)
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.or_byyy))
        val lottieProgress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever
        )

        // Character entrance animation from BOTTOM-LEFT
        // Start position: Off-screen to the left and bottom
        // End position: Settled at bottom-left corner, behind/below the main UI content
        val characterOffsetX by animateDpAsState(
            targetValue = if (isLoading) (-screenWidth - 100.dp) else (-100 * scaleFactor).dp,
            animationSpec = tween(2500, easing = FastOutSlowInEasing),
            label = "characterSlideX"
        )
        val characterOffsetY by animateDpAsState(
            targetValue = if (isLoading) (screenHeight + 70.dp) else (70 * scaleFactor).dp,
            animationSpec = tween(2500, easing = FastOutSlowInEasing),
            label = "characterSlideY"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF4A90E2), Color(0xFFA173FA))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Layer 1: Character @orbby_yy.json (Placed before Column to be behind in Z-order)
            LottieAnimation(
                composition = composition,
                progress = { lottieProgress },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = characterOffsetX, y = characterOffsetY)
                    .size((380 * scaleFactor).dp) // Large and clearly visible
                    .zIndex(0f), // Set behind Layer 2
                contentScale = ContentScale.Fit
            )

            // Layer 2: Main UI (Logo, Title, Button)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = (800 * scaleFactor).dp)
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding((32 * scaleFactor).dp)
                    .verticalScroll(rememberScrollState())
                    .zIndex(1f), // Higher zIndex ensures button is on top and clickable
                verticalArrangement = Arrangement.Center
            ) {
                // Main Title Area with Icons behind the text
                Box(contentAlignment = Alignment.Center) {
                    // Rotating Settings Icons
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .offset(x = (150 * scaleFactor).dp, y = (-30 * scaleFactor).dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_settings),
                            contentDescription = null,
                            modifier = Modifier
                                .size((71 * scaleFactor).dp)
                                .rotate(rotationForward)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.icon_settings),
                            contentDescription = null,
                            modifier = Modifier
                                .size((59 * scaleFactor).dp)
                                .offset(x = (15 * scaleFactor).dp, y = (48 * scaleFactor).dp)
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
                                    .size((110 * scaleFactor).dp)
                                    .offset(x = (-67 * scaleFactor).dp, y = (2 * scaleFactor).dp)
                            )
                            Text(
                                text = "EduTech",
                                fontSize = (64 * scaleFactor).sp,
                                fontFamily = Kavoon,
                                color = Color.White,
                                lineHeight = (64 * scaleFactor).sp
                            )
                        }
                        Text(
                            text = "Learning",
                            fontSize = (32 * scaleFactor).sp,
                            fontFamily = Kavoon,
                            color = Color.White,
                            modifier = Modifier.offset(y = (-27 * scaleFactor).dp, x = (-5 * scaleFactor).dp),
                            lineHeight = (32 * scaleFactor).sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height((12 * scaleFactor).dp)) // Tightened from 16
                Text(
                    text = "Computer Literacy for Young Learners",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = (20 * scaleFactor).sp, // Scaled and increased base from 18
                    fontFamily = Kavoon,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height((20 * scaleFactor).dp)) // Tightened from 24

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
                                .fillMaxWidth(0.85f)
                                .height((110 * scaleFactor).dp) // Maintain consistent height to avoid jumps
                        )
                    } else {
                        Button(
                            onClick = onStartClick,
                            interactionSource = interactionSource,
                            modifier = Modifier
                                .scale(pulseScale * pressedScale)
                                .height((60 * scaleFactor).dp) // Scaled and increased base from 55
                                .widthIn(max = (350 * scaleFactor).dp) // Scaled and increased base from 320
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
                                            colors = listOf(Color(0xFF4A90E2), Color(0xFFA173FA)),
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
                                fontSize = (20 * scaleFactor).sp, // Scaled and increased base from 18
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = Kavoon
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height((32 * scaleFactor).dp)) // Tightened from 48
            }
        }
    }
}




















@Preview(showBackground = true, name = "Loading Screen")
@Composable
fun PreviewEntranceLoading() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        DashboardEntranceScreen(initialLoading = true, onStartClick = {})
    }
}

@Preview(showBackground = true, name = "Press to Start Screen")
@Composable
fun PreviewEntranceStartButton() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        DashboardEntranceScreen(initialLoading = false, onStartClick = {})
    }
}
