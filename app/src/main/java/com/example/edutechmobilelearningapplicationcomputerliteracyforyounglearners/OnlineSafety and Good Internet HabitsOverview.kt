package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon

@Composable
fun OnlineSafetyAndGoodInternetHabitsOverviewScreen(
    onBackClick: () -> Unit,
    onStartLearningClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF4A90E2),
                        Color(0xFFA173FA)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 850.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 40.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 20.dp
                    )
            ) {
                EduTechBackButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                Text(
                    text = "Topic Overview",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = Kavoon,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 110.dp)
                )
            }

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
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))

                        Image(
                            painter = painterResource(id = R.drawable.basic_internet_awareness_and_safety),
                            contentDescription = "Online Safety Overview",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = "Online Safety and Good Internet Habits",
                            fontSize = 22.sp,
                            fontFamily = Kavoon,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8E6CCB),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Staying safe online is very important! In this lesson, we will learn how to protect ourselves while using the internet. We'll talk about keeping personal information private and how to be a kind digital citizen.",
                            fontSize = 15.sp,
                            fontFamily = FontFamily.SansSerif,
                            color = Color(0xFF37474F),
                            textAlign = TextAlign.Start,
                            lineHeight = 22.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = onStartLearningClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = RoundedCornerShape(32.dp),
                        border = BorderStroke(2.dp, Color(0xFF8E6CCB)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF8E6CCB)
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = "START LEARNING",
                            fontSize = 18.sp,
                            fontFamily = Kavoon,
                            color = Color(0xFF8E6CCB)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnlineSafetyAndGoodInternetHabitsOverviewPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        OnlineSafetyAndGoodInternetHabitsOverviewScreen(
            onBackClick = {},
            onStartLearningClick = {}
        )
    }
}
