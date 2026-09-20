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
import androidx.compose.material.icons.filled.Star
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

val introToComputerQuestions = listOf(
    ComputerQuestion(1, "What is a computer?", listOf("A toy", "A device that helps us do things", "A book", "A chair"), 1),
    ComputerQuestion(2, "What does a computer follow?", listOf("Instructions", "Songs", "Colors", "Stories"), 0),
    ComputerQuestion(3, "What can you do with a computer?", listOf("Write", "Draw", "Watch videos", "All of these"), 3),
    ComputerQuestion(4, "Where can you find computers?", listOf("At home", "At school", "In hospitals", "All of these"), 3),
    ComputerQuestion(5, "Which computer usually stays on a desk?", listOf("Tablet", "Smartphone", "Laptop", "Desktop computer"), 3),
    ComputerQuestion(6, "Which computer can you carry in a bag?", listOf("Laptop", "Desktop computer", "Monitor", "Printer"), 0),
    ComputerQuestion(7, "Which computer lets you tap and swipe the screen?", listOf("Desktop", "Tablet", "Printer", "Keyboard"), 1),
    ComputerQuestion(8, "Which computer can fit in your hand or pocket?", listOf("Desktop computer", "Laptop", "Smartphone", "Monitor"), 2),
    ComputerQuestion(9, "Why are a laptop, tablet, and smartphone called computers?", listOf("They are all the same size.", "They can follow instructions and help us do things.", "They all have keyboards.", "They are all used for games."), 1),
    ComputerQuestion(10, "Which one is NOT a type of computer from our lesson?", listOf("Laptop", "Tablet", "Smartphone", "Television"), 3)
)

@Composable
fun IntroductionToComputerAssessmentScreen(
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

    val currentQuestion = introToComputerQuestions[currentQuestionIndex]

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
                            ProgressTracker.COURSE_INTRO,
                            score,
                            introToComputerQuestions.size
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
                            text = "Your Score: $score / ${introToComputerQuestions.size}",
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
                                    text = "${currentQuestionIndex + 1}/${introToComputerQuestions.size}",
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
                                        if (currentQuestionIndex < introToComputerQuestions.size - 1) {
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
fun IntroductionToComputerAssessmentPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        IntroductionToComputerAssessmentScreen(onBackClick = {})
    }
}

@Preview(showBackground = true, name = "Assessment Result")
@Composable
fun IntroductionToComputerAssessmentResultPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        IntroductionToComputerAssessmentScreen(
            onBackClick = {},
            isFinishedPreview = true,
            scorePreview = 9
        )
    }
}
