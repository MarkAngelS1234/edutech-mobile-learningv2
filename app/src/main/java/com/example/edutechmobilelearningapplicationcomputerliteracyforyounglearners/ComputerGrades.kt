package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.*
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon
import kotlinx.coroutines.delay
import kotlin.math.min

/**
 * ComputerGradesScreen - A screen that displays a list of computer literacy grade levels.
 *
 * This screen features:
 * - Animated entrance transitions for headers and list items.
 * - Dynamic scroll-based fading effects.
 * - Clean cards showing available grade levels.
 * - Internal navigation to specific lessons.
 *
 * @param onBackClick Function to execute when the user navigates back.
 * @param onGradeClick Function to execute when a specific grade level is selected.
 */
@Composable
fun ComputerGradesScreen(
    onBackClick: () -> Unit,
    onGradeClick: (String) -> Unit = {},
    viewModel: CourseViewModel? = null
) {
    val isPreview = LocalInspectionMode.current
    val actualViewModel: CourseViewModel? = if (isPreview) null else {
        viewModel ?: viewModel()
    }

    // Internal state to manage which lesson is currently being displayed
    var currentLesson by remember { mutableStateOf<String?>(null) }
    // Remembers where we were before going to the Progress screen
    var previousLesson by remember { mutableStateOf<String?>(null) }

    // Handle internal lesson navigation
    when (currentLesson) {
        "Progress" -> {
            ProgressScreen(
                onBackClick = { currentLesson = previousLesson },
                viewModel = actualViewModel
            )
        }
        "Introduction to Computers Overview" -> {
            IntroductionToComputerOverviewScreen(
                onBackClick = { currentLesson = null },
                onStartLearningClick = { currentLesson = "Introduction to Computers" }
            )
        }
        "Introduction to Computers" -> {
            IntroductionToComputerScreen(
                onBackClick = { currentLesson = "Introduction to Computers Overview" },
                onCheckProgressClick = { 
                    previousLesson = "Introduction to Computers"
                    currentLesson = "Progress" 
                },
                viewModel = actualViewModel
            )
        }
        
        "Computer Hardware Overview" -> {
            ComputerHardwareOverviewScreen(
                onBackClick = { currentLesson = null },
                onStartLearningClick = { currentLesson = "Computer Hardware" }
            )
        }
        "Computer Hardware" -> {
           ComputerHardware(
               onBackClick = { currentLesson = "Computer Hardware Overview" },
               onCheckProgressClick = {
                   previousLesson = "Computer Hardware"
                   currentLesson = "Progress"
               },
               viewModel = actualViewModel
           )
        }
        
        "Computer Software Overview" -> {
            ComputerSoftwareOverviewScreen(
                onBackClick = { currentLesson = null },
                onStartLearningClick = { currentLesson = "Computer Software" }
            )
        }
        "Computer Software" -> {
            IntroductionToSoftware(
                onBackClick = { currentLesson = "Computer Software Overview" },
                onCheckProgressClick = {
                    previousLesson = "Computer Software"
                    currentLesson = "Progress"
                },
                viewModel = actualViewModel
            )
        }
        
        "Internet Basics Overview" -> {
            InternetBasicsOverviewScreen(
                onBackClick = { currentLesson = null },
                onStartLearningClick = { currentLesson = "Internet Basics" }
            )
        }
        "Internet Basics" -> {
            InternetBasicsScreen(
                onBackClick = { currentLesson = "Internet Basics Overview" },
                onCheckProgressClick = {
                    previousLesson = "Internet Basics"
                    currentLesson = "Progress"
                },
                viewModel = actualViewModel!!
            )
        }
        
        "Online Safety Overview" -> {
            BasicInternetAwarenessAndSafetyOverviewScreen(
                onBackClick = { currentLesson = null },
                onStartLearningClick = { currentLesson = "Online Safety Awareness and Safety" }
            )
        }
        "Online Safety Awareness and Safety" -> {
            BasicInternetAwarenessAndSafetyScreen(
                onBackClick = { currentLesson = null },
                onCheckProgressClick = {
                    previousLesson = "Online Safety Awareness and Safety"
                    currentLesson = "Progress"
                },
                viewModel = actualViewModel
            )
        }
        
        else -> {
            // Main Grade List View
            GradeListContent(
                isPreview = isPreview,
                onBackClick = onBackClick,
                onGradeSelected = { grade ->
                    when (grade) {
                        ProgressTracker.COURSE_INTRO -> currentLesson = "Introduction to Computers Overview"
                        ProgressTracker.COURSE_HARDWARE -> currentLesson = "Computer Hardware Overview"
                        ProgressTracker.COURSE_SOFTWARE -> currentLesson = "Computer Software Overview"
                        ProgressTracker.COURSE_INTERNET -> currentLesson = "Internet Basics Overview"
                        ProgressTracker.COURSE_SAFETY -> currentLesson = "Online Safety Overview"
                    }
                    onGradeClick(grade)
                },
                viewModel = actualViewModel
            )
        }
    }
}

/**
 * GradeListContent - The main UI content showing the list of grades.
 */
@Composable
private fun GradeListContent(
    isPreview: Boolean,
    onBackClick: () -> Unit,
    onGradeSelected: (String) -> Unit,
    viewModel: CourseViewModel? = null
) {
    // Animation state for the header's entrance
    val headerProgress = remember { Animatable(if (isPreview) 1f else 0f) }

    // Trigger entrance animations
    LaunchedEffect(Unit) {
        if (!isPreview) {
            headerProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF4A90E2), // Primary Blue
                        Color(0xFFA173FA)  // Vibrant Teal
                    )
                )
            )
    ) {
        // Clean, presentable cloud background decorations inspired by reference photo
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Top Left Cloud (Subtle, soft white)
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 35.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(10.dp.toPx(), 50.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 25.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(40.dp.toPx(), 55.dp.toPx())
            )

            // Top Right Cloud
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 40.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width - 15.dp.toPx(), 60.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 30.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width - 45.dp.toPx(), 70.dp.toPx())
            )

            // Bottom Left Cloud Puff
            drawCircle(
                color = Color.White.copy(alpha = 0.12f),
                radius = 85.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(0f, height - 10.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = 60.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(55.dp.toPx(), height)
            )

            // Bottom Right Cloud Puff
            drawCircle(
                color = Color.White.copy(alpha = 0.12f),
                radius = 95.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width, height - 15.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = 65.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width - 65.dp.toPx(), height)
            )
        }

        // Add orbby_yy.json at the bottom-left corner of the screen, below the course cards.
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.orbby_yy))
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-129).dp, y = 85.dp)
                .size(370.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 850.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 20.dp)
                .zIndex(1f), // Ensure course cards are drawn above background animations
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Back navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate back",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Screen Titles - Centered with customization functionality
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        alpha = headerProgress.value
                        translationY = (1f - headerProgress.value) * 40f
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Courses",
                    fontFamily = Kavoon,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    // Functionality customization: Clickable modifier for interaction
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .clickable {
                            // Add custom functionality here (e.g., search, filter, or refresh)
                        }
                )

                // Optional customization: decorative underline
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(4.dp)
                        .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(2.dp))
                )
            }

            Spacer(modifier = Modifier.height(29.dp))

            // Scrollable list of Grade Levels
            val grades = ProgressTracker.ALL_COURSES

            val progressList by if (isPreview) {
                remember { mutableStateOf(emptyList<CourseProgress>()) }
            } else {
                viewModel!!.allProgress.collectAsState(initial = emptyList())
            }
            val progressMap = remember(progressList) { progressList.associateBy { it.courseName } }

            val listState = rememberLazyListState()

            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(grades, key = { _, grade -> grade }) { index, grade ->

                    val isCompleted = progressMap[grade]?.isCompleted == true
                    val isUnlocked = index == 0 || progressMap[grades[index - 1]]?.isCompleted == true

                    // Item entrance animation
                    val entranceProgress = remember { Animatable(if (isPreview) 1f else 0f) }
                    LaunchedEffect(Unit) {
                        if (!isPreview) {
                            delay(index * 100L)
                            entranceProgress.animateTo(1f, tween(600))
                        }
                    }

                    // Scroll-dependent transparency
                    val scrollAlpha by remember {
                        derivedStateOf {
                            if (isPreview) return@derivedStateOf 1f
                            val layoutInfo = listState.layoutInfo
                            val itemInfo = layoutInfo.visibleItemsInfo.find { it.index == index }

                            if (itemInfo != null) {
                                val viewportHeight = (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset).toFloat()
                                val itemTop = itemInfo.offset.toFloat()
                                val itemBottom = (itemInfo.offset + itemInfo.size).toFloat()

                                val fadeZone = 150f
                                val topAlpha = (itemBottom / fadeZone).coerceIn(0f, 1f)
                                val bottomAlpha = ((viewportHeight - itemTop) / fadeZone).coerceIn(0f, 1f)

                                min(topAlpha, bottomAlpha)
                            } else {
                                0f
                            }
                        }
                    }

                    val combinedProgress = entranceProgress.value * scrollAlpha

                    GradeListItem(
                        grade = grade,
                        animProgress = combinedProgress,
                        isLocked = !isUnlocked,
                        isCompleted = isCompleted,
                        onClick = { if (isUnlocked) onGradeSelected(grade) }
                    )
                }
            }
        }
    }
}

/**
 * GradeListItem - A single card component representing a grade level.
 */
@Composable
fun GradeListItem(
    grade: String,
    animProgress: Float,
    isLocked: Boolean,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        enabled = !isLocked,
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .graphicsLayer {
                alpha = animProgress
                translationY = (1f - animProgress) * 40f
            },
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(3.dp, if (isLocked) Color.LightGray else Color(0xFF6C5CE7)),
        shadowElevation = if (isLocked) 0.dp else (4 * animProgress).dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)
                .graphicsLayer {
                    alpha = if (isLocked) 0.6f else 1f
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = grade,
                    fontFamily = Kavoon,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isLocked) Color.Gray else Color(0xFF6C5CE7)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = if (isLocked) Color.Gray else Color(0xFF6C5CE7),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isLocked) "Locked" else if (isCompleted) "Completed" else "Available",
                        fontSize = 13.sp,
                        color = if (isCompleted) Color(0xFF4CAF50) else Color.Gray
                    )
                }
            }

            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(32.dp)
                )
            } else if (isLocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComputerGradesPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ComputerGradesScreen(onBackClick = {})
    }
}
