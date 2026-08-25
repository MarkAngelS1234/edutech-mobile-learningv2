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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon

/**
 * BasicInternetAwarenessAndSafetyScreen - Detailed lesson content for "Basic Internet Awareness and Safety".
 * Updated to match the layout of the "Introduction to Software" screen (first picture)
 * with a simplified Overview section as requested.
 */
@Composable
fun BasicInternetAwarenessAndSafetyScreen(onBackClick: () -> Unit) {
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
            // Header with Centered Title (Layout from Picture 1)
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
                    text = "Internet Awareness & Safety",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = Kavoon,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main Content Card (White background with rounded top corners)
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
                    // --- LEARNING GOALS SECTION (Gradient Box) ---
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
                                    "1. Understand how to stay safe online.",
                                    "2. Learn smart safety rules for kids.",
                                    "3. Protect your personal information."
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

                    // --- OVERVIEW SECTION (Second Gradient Box) ---
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
                                modifier = Modifier.semantics { contentDescription = "Overview Section" },
                                color = Color.White,
                                fontSize = 22.sp,
                                fontFamily = Kavoon,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 26.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "The internet is a wonderful place to learn and play, but it's important to know how to stay safe. Just like in the real world, there are rules to follow online.",
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
fun BasicInternetAwarenessAndSafetyPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        BasicInternetAwarenessAndSafetyScreen(onBackClick = {})
    }
}
