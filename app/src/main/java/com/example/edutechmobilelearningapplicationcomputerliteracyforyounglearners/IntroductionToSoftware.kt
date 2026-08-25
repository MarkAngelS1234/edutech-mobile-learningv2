package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon

/**
 * IntroductionToSoftware - Detailed lesson content for the "Introduction to Software" topic.
 * Layout is matched to IntroductionToComputer.kt (the first picture).
 */
@Composable
fun IntroductionToSoftware(onBackClick: () -> Unit) {
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
                    text = "Introduction to Software",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = Kavoon,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

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
                                    "1. Understand what software is.",
                                    "2. Learn how software helps hardware.",
                                    "3. Discover apps you use every day."
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

                    // --- OVERVIEW SECTION ---
                    // Image and "Types of Software" have been removed as requested.
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
                        Column {
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
                                text = "Software is a set of instructions that tells a computer hardware what to do. Unlike hardware, which you can touch, software is digital and invisible.",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IntroductionToSoftwarePreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        IntroductionToSoftware(onBackClick = {})
    }
}
