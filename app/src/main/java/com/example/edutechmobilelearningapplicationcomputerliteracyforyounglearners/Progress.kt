package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun ProgressScreen(onBackClick: () -> Unit, viewModel: CourseViewModel? = null) {
    val isInspectionMode = LocalInspectionMode.current
    val actualViewModel = if (isInspectionMode) null else viewModel ?: viewModel()

    val scrollState = rememberScrollState()

    // Observe the automated stats and progress list from the database
    val progressList by if (isInspectionMode) {
        remember { mutableStateOf(emptyList<CourseProgress>()) }
    } else {
        actualViewModel!!.allProgress.collectAsState(initial = emptyList())
    }
    
    val overallStats by if (isInspectionMode) {
        remember { mutableStateOf(OverallProgress(id = 1, percentage = 0, courseBadgesCount = 0)) }
    } else {
        actualViewModel!!.overallStats.collectAsState()
    }
    
    val progressMap = progressList.associateBy { it.courseName }
    val allCourses = ProgressTracker.ALL_COURSES
    val progressPercentage = overallStats.percentage

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf( Color(0xFF4A90E2), Color(0xFFA173FA)
                    )
                )
            )
    ) {
        // Minimal, presentable cloud background decorations (not overcrowded)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Top Right subtle cloud
            drawCircle(
                color = Color.White.copy(alpha = 0.12f),
                radius = 35.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width - 15.dp.toPx(), 60.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = 25.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width - 45.dp.toPx(), 75.dp.toPx())
            )

            // Bottom Left subtle puff
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = 80.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(0f, height - 10.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.08f),
                radius = 60.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(50.dp.toPx(), height)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 800.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Centered Header
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                EduTechBackButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Text(
                    text = "Your Progress",
                    fontFamily = Kavoon,
                    fontSize = 29.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Progress Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Overall Progress",
                        fontWeight = FontWeight.Bold,
                        fontFamily = Kavoon,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Circular Water Wobbling Progress
                    CircularWaterProgressIndicator(
                        progress = progressPercentage / 100f,
                        size = 180.dp
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        "$progressPercentage% Completed",
                        fontFamily = Kavoon,
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Course Checklist Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Course Checklist",
                        fontWeight = FontWeight.Bold,
                        fontFamily = Kavoon,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        allCourses.forEach { courseName ->
                            val progress = progressMap[courseName]
                            val isDone = progress?.isCompleted == true
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (isDone) Color(0xFF4CAF50) else Color.LightGray,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = courseName,
                                        fontFamily = Kavoon,
                                        fontSize = 16.sp,
                                        color = if (isDone) Color.Black else Color.Gray,
                                        fontWeight = if (isDone) FontWeight.Medium else FontWeight.Normal
                                    )
                                    if (isDone && progress!!.totalQuestions > 0) {
                                        Text(
                                            text = "Assessment Score: ${progress.assessmentScore} / ${progress.totalQuestions}",
                                            fontSize = 12.sp,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "@EduTechMobile 2026",
                color = Color.White.copy(alpha = 0.9f),
                fontFamily = Kavoon,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun CircularWaterProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    size: Dp = 180.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "water_wave")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_offset"
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.2f))
            .border(4.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    )
    {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.toPx()
            val height = size.toPx()
            val waveHeight = 10.dp.toPx()
            val waveLength = width
            
            val fillLevel = height * (1 - progress)

            val waterPath = Path().apply {
                moveTo(0f, height)
                lineTo(0f, fillLevel)
                
                for (x in 0..width.toInt()) {
                    val y = fillLevel + waveHeight * sin((x / waveLength) * 2 * PI + waveOffset).toFloat()
                    lineTo(x.toFloat(), y)
                }
                
                lineTo(width, height)
                close()
            }

            drawPath(
                path = waterPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF4A90E2),
                        Color(0xFFA173FA)
                    )
                )
            )
        }
        
        Text(
            text = "${(progress * 100).toInt()}%",
            fontFamily = Kavoon,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = if (progress > 0.5f) Color.White else Color(0xFF4A90E2),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ProgressScreen(onBackClick = {})
    }
}
