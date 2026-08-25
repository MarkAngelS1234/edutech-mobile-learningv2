package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme

enum class QuestionType { MULTIPLE_CHOICE, IDENTIFICATION }

data class ThinkAndChooseQuestion(
    val clue: String,
    val definition: String,
    val type: QuestionType,
    val answer: String,
    val options: List<String> = emptyList()
)

@Composable
fun ThinkAndChooseGameScreen(onBackClick: () -> Unit) {
    val questions = remember {
        listOf(
            ThinkAndChooseQuestion(
                clue = "The Brain",
                definition = "I am the main part of the computer that processes all instructions.",
                type = QuestionType.MULTIPLE_CHOICE,
                answer = "CPU",
                options = listOf("Monitor", "CPU", "Mouse", "Keyboard")
            ),
            ThinkAndChooseQuestion(
                clue = "The Pointer",
                definition = "I am a small device used to point, click, and drag items on the screen.",
                type = QuestionType.MULTIPLE_CHOICE,
                answer = "Mouse",
                options = listOf("Printer", "Mouse", "Speaker", "RAM")
            ),
            ThinkAndChooseQuestion(
                clue = "The Display",
                definition = "I show you pictures, videos, and text on a screen.",
                type = QuestionType.MULTIPLE_CHOICE,
                answer = "Monitor",
                options = listOf("Scanner", "Monitor", "Webcam", "USB")
            ),
            ThinkAndChooseQuestion(
                clue = "The Typer",
                definition = "I have many keys with letters and numbers used for typing.",
                type = QuestionType.MULTIPLE_CHOICE,
                answer = "Keyboard",
                options = listOf("Keyboard", "Microphone", "Headphones", "Joystick")
            ),
            ThinkAndChooseQuestion(
                clue = "Global Network",
                definition = "I connect computers all around the world so people can share info.",
                type = QuestionType.MULTIPLE_CHOICE,
                answer = "Internet",
                options = listOf("Wi-Fi", "Bluetooth", "Internet", "Ethernet")
            ),
            ThinkAndChooseQuestion(
                clue = "Digital Instructions",
                definition = "A set of programs and instructions that tell a computer what to do.",
                type = QuestionType.IDENTIFICATION,
                answer = "SOFTWARE"
            ),
            ThinkAndChooseQuestion(
                clue = "Physical Parts",
                definition = "The actual, touchable parts of a computer system.",
                type = QuestionType.IDENTIFICATION,
                answer = "HARDWARE"
            ),
            ThinkAndChooseQuestion(
                clue = "Web Viewer",
                definition = "A special program used to access and view websites like Chrome or Safari.",
                type = QuestionType.IDENTIFICATION,
                answer = "BROWSER"
            ),
            ThinkAndChooseQuestion(
                clue = "Paper Maker",
                definition = "An output device that puts digital text and images onto paper.",
                type = QuestionType.IDENTIFICATION,
                answer = "PRINTER"
            ),
            ThinkAndChooseQuestion(
                clue = "Info Finder",
                definition = "A tool like Google used to search for information on the World Wide Web.",
                type = QuestionType.IDENTIFICATION,
                answer = "SEARCH ENGINE"
            )
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }
    var userInput by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var showFeedback by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                }
                Text(
                    "Think & Choose",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            if (!isGameOver) {
                val currentQuestion = questions[currentIndex]
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    "Question ${currentIndex + 1}/${questions.size}",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Clue and Definition Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "CLUE: ${currentQuestion.clue}",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentQuestion.definition,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Input Section
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (currentQuestion.type == QuestionType.MULTIPLE_CHOICE) {
                        currentQuestion.options.forEach { option ->
                            OptionButton(
                                text = option,
                                isSelected = selectedOption == option,
                                isEnabled = !showFeedback,
                                onClick = { selectedOption = option }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    } else {
                        OutlinedTextField(
                            value = userInput,
                            onValueChange = { if (!showFeedback) userInput = it },
                            label = { Text("Type your answer here", color = Color.White.copy(alpha = 0.6f)) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                cursorColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true,
                            enabled = !showFeedback
                        )
                    }

                    if (showFeedback) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = if (isCorrect) "CORRECT! Great job!" else "INCORRECT. The answer was ${currentQuestion.answer}",
                            color = if (isCorrect) Color(0xFFC8E6C9) else Color(0xFFFFCDD2),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Action Button
                Button(
                    onClick = {
                        if (!showFeedback) {
                            val currentAnswer = currentQuestion.answer
                            val userProvided = if (currentQuestion.type == QuestionType.MULTIPLE_CHOICE) {
                                selectedOption ?: ""
                            } else {
                                userInput.trim().uppercase()
                            }

                            isCorrect = userProvided.equals(currentAnswer, ignoreCase = true)
                            if (isCorrect) score++
                            showFeedback = true
                        } else {
                            if (currentIndex < questions.size - 1) {
                                currentIndex++
                                userInput = ""
                                selectedOption = null
                                showFeedback = false
                            } else {
                                isGameOver = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF4A90E2)
                    ),
                    enabled = if (!showFeedback) {
                        if (currentQuestion.type == QuestionType.MULTIPLE_CHOICE) selectedOption != null else userInput.isNotBlank()
                    } else true
                ) {
                    Text(
                        text = if (!showFeedback) "Submit Answer" else if (currentIndex < questions.size - 1) "Next Question" else "Finish",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                // Game Over Screen
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "GAME OVER!",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Score: $score/${questions.size}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                    Button(
                        onClick = {
                            currentIndex = 0
                            score = 0
                            isGameOver = false
                            userInput = ""
                            selectedOption = null
                            showFeedback = false
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF4A90E2))
                    ) {
                        Text("Play Again", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, Color.White),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text("Exit to Menu", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun OptionButton(text: String, isSelected: Boolean, isEnabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = isEnabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.White else Color.White.copy(alpha = 0.2f),
            contentColor = if (isSelected) Color(0xFF4A90E2) else Color.White,
            disabledContainerColor = if (isSelected) Color.White.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.1f),
            disabledContentColor = if (isSelected) Color(0xFF4A90E2).copy(alpha = 0.5f) else Color.White.copy(alpha = 0.5f)
        ),
        border = if (!isSelected) BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)) else null
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ThinkAndChoosePreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ThinkAndChooseGameScreen(onBackClick = {})
    }
}
