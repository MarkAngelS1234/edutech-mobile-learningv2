package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon

@Composable
fun InternetBasicsOverviewScreen(
    onBackClick: () -> Unit,
    onStartLearningClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF7B6FE8),
                        Color(0xFFA173FA)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 40.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 20.dp
                    ),
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
                    text = "Topic Overview",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = Kavoon,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            // Main Content
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(
                    topStart = 32.dp,
                    topEnd = 32.dp
                ),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        Image(
                            painter = painterResource(
                                id = R.drawable.internet_basics
                            ),
                            contentDescription = "Internet Basics Overview",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(
                                    RoundedCornerShape(20.dp)
                                ),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(
                            modifier = Modifier.height(32.dp)
                        )

                        Text(
                            text = "Internet Basics",
                            fontSize = 24.sp,
                            fontFamily = Kavoon,
                            color = Color(0xFF8E6CCB),
                            textAlign = TextAlign.Center
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        Text(
                            text = "What is the internet? In this lesson, we will explore the global network that connects computers everywhere. You'll learn about websites, browsers, and how we use the internet to find information.",
                            fontSize = 16.sp,
                            color = Color(0xFF37474F),
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp
                        )
                    }

                    // Start Learning Button
                    Button(
                        onClick = onStartLearningClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = RoundedCornerShape(32.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF8E6CCB)
                        ),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true)
                    ) {
                        Text(
                            text = "START LEARNING",
                            fontSize = 18.sp,
                            fontFamily = Kavoon,
                            color = Color(0xFF8E6CCB)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InternetBasicsOverviewPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        InternetBasicsOverviewScreen(
            onBackClick = {},
            onStartLearningClick = {}
        )
    }
}