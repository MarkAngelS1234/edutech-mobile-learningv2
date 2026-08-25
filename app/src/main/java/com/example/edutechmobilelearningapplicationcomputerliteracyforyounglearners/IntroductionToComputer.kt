package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon


/**
 * IntroductionToComputerScreen - Detailed lesson content for the "Introduction to Computer" topic.
 */
@Composable
fun IntroductionToComputerScreen(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF4A90E2), Color(0xFF50E3C2))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            // Header with Centered Title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
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
                    text = "Introduction to Computer",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = Kavoon,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Content Card - Extended to the bottom by removing bottom padding and adjusting shape
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
                        .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                ) {
                    // --- LEARNING GOALS SECTION ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF4A90E2), Color(0xFF50E3C2))
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Text(
                                text = "Learning Goals:",
                                color = Color.White,
                                fontFamily = Kavoon,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Column(modifier = Modifier.padding(start = 4.dp, top = 4.dp)) {
                                listOf(
                                    "1. Understand what a computer is.",
                                    "2. Identify the main parts of a computer.",
                                    "3. Learn the functions of each part.",
                                    "4. Discover how computers help us."
                                ).forEach { goal ->
                                    Text(
                                        text = goal,
                                        fontSize = 13.sp,
                                        color = Color.White.copy(alpha = 0.95f),
                                        modifier = Modifier.padding(vertical = 2.dp),
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    // --- END OF LEARNING GOALS SECTION ---

                    // "Overview" section - Extended to fill the remaining bottom space
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF4A90E2), Color(0xFF50E3C2))
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Computer Image with Circular Border (Arc style)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(110.dp)
                                    .padding(top = 8.dp)
                            ) {
                                Canvas(modifier = Modifier.size(100.dp)) {
                                    drawArc(
                                        color = Color.White.copy(alpha = 0.8f),
                                        startAngle = 140f,
                                        sweepAngle = 280f,
                                        useCenter = false,
                                        style = Stroke(width = 1.5.dp.toPx())
                                    )
                                }
                                Image(
                                    painter = painterResource(id = R.drawable.computer_1),
                                    contentDescription = "Computer",
                                    modifier = Modifier.size(85.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Text Content (Title and Description)
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Overview",
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontFamily = Kavoon,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 26.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "A computer is an electronic device that manipulates information, or data. It has the ability to store, retrieve, and process data.",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.9f),
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IntroToComputerPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        IntroductionToComputerScreen(onBackClick = {})
    }
}
