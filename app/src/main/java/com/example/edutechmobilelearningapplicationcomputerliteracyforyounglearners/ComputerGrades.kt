package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners


import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
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
    onGradeClick: (String) -> Unit = {}
) {
    val isPreview = LocalInspectionMode.current

    // Internal state to manage which lesson is currently being displayed
    var currentLesson by remember { mutableStateOf<String?>(null) }

    // Handle internal lesson navigation
    when (currentLesson) {
        "Introduction to Computers", "Computer Hardware" -> {
            IntroductionToComputerScreen(onBackClick = { currentLesson = null })
        }
        "Computer Software" -> {
            IntroductionToSoftware(onBackClick = { currentLesson = null })
        }
        "Internet Basics" -> {
            InternetBasicsScreen(onBackClick = { currentLesson = null })
        }
        "Online Safety Awareness and Safety" -> {
            BasicInternetAwarenessAndSafetyScreen(onBackClick = { currentLesson = null })
        }
        else -> {
            // Main Grade List View
            GradeListContent(
                isPreview = isPreview,
                onBackClick = onBackClick,
                onGradeSelected = { grade ->
                    val subLessons = listOf(
                        "Introduction to Computers",
                        "Computer Hardware",
                        "Computer Software",
                        "Internet Basics",
                        "Online Safety Awareness and Safety"
                    )
                    if (grade in subLessons) {
                        currentLesson = grade
                    }
                    onGradeClick(grade)
                }
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
    onGradeSelected: (String) -> Unit
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
                    listOf(
                        Color(0xFF4A90E2), // Primary Blue
                        Color(0xFF50E3C2)  // Vibrant Teal
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
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

            // Screen Titles
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        alpha = headerProgress.value
                        translationY = (1f - headerProgress.value) * 40f
                    }
            ) {
                Text(
                    text = "Select your level",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Computer Grades",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(29.dp))

            // Scrollable list of Grade Levels
            val grades = listOf(
                "Introduction to Computers",
                "Computer Hardware",
                "Computer Software",
                "Internet Basics",
                "Online Safety Awareness and Safety"
            )
            val listState = rememberLazyListState()

            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(grades, key = { _, grade -> grade }) { index, grade ->

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
                        subtitle = "Computer Literacy Basics",
                        animProgress = combinedProgress,
                        onClick = { onGradeSelected(grade) }
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
    subtitle: String,
    animProgress: Float,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .graphicsLayer {
                alpha = animProgress
                translationY = (1f - animProgress) * 40f
            },
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        shadowElevation = (4 * animProgress).dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = grade,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = Color.Gray
            )
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
