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

val internetSafetyQuestions = listOf(
    SafetyQuestion(1, "What should you use when talking to others online?", listOf("Kind words", "Bad words", "Angry words"), 0),
    SafetyQuestion(2, "What should you do after using a screen for a long time?", listOf("Take a break", "Keep playing", "Watch more videos"), 0),
    SafetyQuestion(3, "What should you do before you post something?", listOf("Think first", "Post it right away", "Share it with everyone"), 0),
    SafetyQuestion(4, "Is everything you see online true?", listOf("Yes", "No", "Maybe always"), 1),
    SafetyQuestion(5, "What should you do if you are not sure about something online?", listOf("Ask an adult", "Share it", "Click it"), 0),
    SafetyQuestion(6, "What should you do before you download an app?", listOf("Ask an adult", "Download it right away", "Press “Buy”"), 0),
    SafetyQuestion(7, "What should we do to stay safe online?", listOf("Follow the five rules", "Share everything", "Believe everything we see"), 0)
)

@Composable
fun OnlineSafetyAndGoodInternetHabitsAssessmentScreen(
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

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswerIndex by remember { mutableStateOf(-1) }
    var showFeedback by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(scorePreview) }
    var isAssessmentFinished by remember { mutableStateOf(isFinishedPreview) }
    
    // BGM Management: Assessment Mode
    DisposableEffect(Unit) {
        BGMManager.setForcedSilence(true)
        onDispose {
            BGMManager.setForcedSilence(false)
        }
    }

    val currentQuestion = internetSafetyQuestions[currentQuestionIndex]

    val primaryPurple = Color(0xFF6C5CE7)
    val lightBackground = Color(0xFFF8F9FF)
    val optionSelectedColor = Color(0xFFE0E0FF)

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
        containerColor = lightBackground
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
                        ProgressTracker.COURSE_SAFETY,
                        score,
                        internetSafetyQuestions.size
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
                        text = "Your Score: $score / ${internetSafetyQuestions.size}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = onCloseAssessment,
                        colors = ButtonDefaults.buttonColors(containerColor = primaryPurple),
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
                            .height(56.dp)
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
                                text = "${currentQuestionIndex + 1}/${internetSafetyQuestions.size}",
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
                            border = androidx.compose.foundation.BorderStroke(2.dp, borderColor),
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
                                    color = Color(0xFF2D3436),
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                    
                    if (showFeedback) {
                        val isUserCorrect = selectedAnswerIndex == currentQuestion.correctAnswerIndex
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (isUserCorrect) "Correct! Well done! 🌟" else "Not quite right! ❌",
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
                                    if (currentQuestionIndex < internetSafetyQuestions.size - 1) {
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
                                disabledContainerColor = Color(0xFFCBCBEB)
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

@Preview(showBackground = true)
@Composable
fun OnlineSafetyAndGoodInternetHabitsAssessmentPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        OnlineSafetyAndGoodInternetHabitsAssessmentScreen(onBackClick = {})
    }
}

@Preview(showBackground = true, name = "Assessment Result")
@Composable
fun OnlineSafetyAndGoodInternetHabitsAssessmentResultPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        OnlineSafetyAndGoodInternetHabitsAssessmentScreen(
            onBackClick = {},
            isFinishedPreview = true,
            scorePreview = 5
        )
    }
}
