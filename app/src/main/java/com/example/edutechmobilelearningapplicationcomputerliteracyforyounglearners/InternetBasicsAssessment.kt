package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon

val internetBasicsQuestionsList = listOf(
    ComputerQuestion(1, "What is the Internet?", listOf("A computer part", "A way for devices to connect with each other", "A computer game", "A keyboard"), 1),
    ComputerQuestion(2, "Which one is a device?", listOf("Phone", "Wi-Fi", "Internet", "Website"), 0),
    ComputerQuestion(3, "What does Wi-Fi help your device do?", listOf("Connect to the Internet", "Print paper", "Play music without a device", "Turn into a computer"), 0),
    ComputerQuestion(4, "What does it mean when your device is online?", listOf("Your device is turned off", "Your device is connected to the Internet", "Your device has no battery", "Your device is being repaired"), 1),
    ComputerQuestion(5, "What can we do with the Internet?", listOf("Learn new things", "Watch videos", "Send messages", "All of these"), 3),
    ComputerQuestion(6, "Which one is a way to connect to the Internet?", listOf("Wi-Fi", "Keyboard", "Monitor", "Printer"), 0),
    ComputerQuestion(7, "Why should we use the Internet carefully?", listOf("Because it can be useful and fun", "Because we should make smart choices online", "Because it makes our device bigger", "Because it turns off the Wi-Fi"), 1)
)

@Composable
fun InternetBasicsAssessmentScreen(
    onBackClick: () -> Unit,
    onCloseAssessment: () -> Unit = onBackClick,
    onCheckProgressClick: () -> Unit = {},
    viewModel: CourseViewModel? = null,
    isFinishedPreview: Boolean = false,
    scorePreview: Int = 0
) {
    val realViewModel: CourseViewModel? = if (LocalInspectionMode.current) null else {
        viewModel ?: viewModel()
    }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswerIndex by remember { mutableIntStateOf(-1) }
    var showFeedback by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(scorePreview) }
    var isAssessmentFinished by remember { mutableStateOf(isFinishedPreview) }

    // BGM Management: Assessment Mode
    DisposableEffect(Unit) {
        BGMManager.setForcedSilence(true)
        onDispose {
            BGMManager.setForcedSilence(false)
        }
    }

    val currentQuestion = internetBasicsQuestionsList[currentQuestionIndex]

    val primaryPurple = Color(0xFF8E6CCB)
    val lightBackground = Color(0xFFF8F9FF)
    val optionSelectedColor = Color(0xFFE0E0FF)
    val disabledPurple = Color(0xFFD1C4E9)

    val backgroundBrush = Brush.verticalGradient(
        listOf(
            Color(0xFFEAF4FF),
            Color(0xFFF3ECFF)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                ) {
                    EduTechBackButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    Text(
                        text = "Quick Quiz",
                        fontSize = 22.sp,
                        fontFamily = Kavoon,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3436),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 110.dp)
                    )
                }
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(modifier = Modifier.widthIn(max = 850.dp).fillMaxWidth().fillMaxHeight()) {
                    if (isAssessmentFinished) {
                    LaunchedEffect(Unit) {
                        realViewModel?.updateAssessmentScore(
                            ProgressTracker.COURSE_INTERNET,
                            score,
                            internetBasicsQuestionsList.size
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Assessment Completed!",
                            fontSize = 24.sp,
                            fontFamily = Kavoon,
                            color = primaryPurple,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Your Score: $score / ${internetBasicsQuestionsList.size}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(
                            onClick = onCloseAssessment,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryPurple,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(25.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(text = "Back to Lesson", fontFamily = Kavoon, color = Color.White, fontSize = 18.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedButton(
                            onClick = onCheckProgressClick,
                            border = BorderStroke(2.dp, primaryPurple),
                            shape = RoundedCornerShape(25.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = primaryPurple)
                        ) {
                            Text(text = "Check Progress", fontFamily = Kavoon, color = primaryPurple, fontSize = 18.sp)
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFEBEBFF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = primaryPurple,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color(0xFFF0F0F7),
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = "${currentQuestionIndex + 1}/${internetBasicsQuestionsList.size}",
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = currentQuestion.question,
                            fontSize = 24.sp,
                            fontFamily = Kavoon,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3436)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        currentQuestion.options.forEachIndexed { index, option ->
                            val isSelected = index == selectedAnswerIndex
                            val isCorrect = index == currentQuestion.correctAnswerIndex
                            
                            val borderColor = when {
                                showFeedback && isCorrect -> Color(0xFF4CAF50)
                                showFeedback && isSelected && !isCorrect -> Color(0xFFF44336)
                                isSelected -> primaryPurple
                                else -> Color(0xFFE0E0E0)
                            }
                            
                            val bgColor = when {
                                showFeedback && isCorrect -> Color(0xFFE8F5E9)
                                showFeedback && isSelected && !isCorrect -> Color(0xFFFFEBEE)
                                isSelected -> optionSelectedColor
                                else -> Color.White
                            }

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable(enabled = !showFeedback) {
                                        selectedAnswerIndex = index
                                    },
                                shape = RoundedCornerShape(16.dp),
                                color = bgColor,
                                border = BorderStroke(2.dp, borderColor),
                                shadowElevation = 2.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .border(2.dp, borderColor, CircleShape)
                                            .padding(4.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) borderColor else Color.Transparent)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(
                                        text = option,
                                        fontSize = 18.sp,
                                        color = Color(0xFF37474F),
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                        
                        if (showFeedback) {
                            val isUserCorrect = selectedAnswerIndex == currentQuestion.correctAnswerIndex
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (isUserCorrect) "Correct! Well done! \uD83C\uDF1F" else "Not quite right! \u274C",
                                color = if (isUserCorrect) Color(0xFF2E7D32) else Color(0xFFC62828),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(120.dp))
                    }
                }

                if (!isAssessmentFinished) {
                    Surface(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        color = lightBackground,
                        shadowElevation = 8.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (!showFeedback) {
                                        if (selectedAnswerIndex != -1) {
                                            showFeedback = true
                                            if (selectedAnswerIndex == currentQuestion.correctAnswerIndex) {
                                                score++
                                            }
                                        }
                                    } else {
                                        if (currentQuestionIndex < internetBasicsQuestionsList.size - 1) {
                                            currentQuestionIndex++
                                            selectedAnswerIndex = -1
                                            showFeedback = false
                                        } else {
                                            isAssessmentFinished = true
                                        }
                                    }
                                },
                                enabled = selectedAnswerIndex != -1,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryPurple,
                                    contentColor = Color.White,
                                    disabledContainerColor = disabledPurple,
                                    disabledContentColor = Color.White.copy(alpha = 0.6f)
                                )
                            ) {
                                Text(
                                    text = if (showFeedback) "Next" else "Check Answer",
                                    fontSize = 18.sp,
                                    fontFamily = Kavoon,
                                    color = Color.White
                                )
                            }
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
fun InternetBasicsAssessmentScreenPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        InternetBasicsAssessmentScreen(onBackClick = {})
    }
}

@Preview(showBackground = true, name = "Assessment Result")
@Composable
fun InternetBasicsAssessmentResultPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        InternetBasicsAssessmentScreen(
            onBackClick = {},
            isFinishedPreview = true,
            scorePreview = 6
        )
    }
}
