package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme

data class ScrambleQuestion(val original: String, val scrambled: String)

enum class Difficulty { EASY, NORMAL, HARD }

@Composable
fun WordScrambleGameScreen(onBackClick: () -> Unit) {
    var currentDifficulty by remember { mutableStateOf<Difficulty?>(null) }

    AnimatedContent(
        targetState = currentDifficulty,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "DifficultyTransition"
    ) { difficulty ->
        if (difficulty == null) {
            DifficultySelectionScreen(
                onBackClick = onBackClick,
                onSelect = { currentDifficulty = it }
            )
        } else {
            ActiveGameScreen(
                difficulty = difficulty,
                onBackClick = { currentDifficulty = null }
            )
        }
    }
}

@Composable
fun DifficultySelectionScreen(onBackClick: () -> Unit, onSelect: (Difficulty) -> Unit) {
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
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                }
                Text(
                    "Word Scramble",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                "Select Difficulty",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(32.dp))

            DifficultyButton("Easy", Color(0xFF66BB6A)) { onSelect(Difficulty.EASY) }
            Spacer(modifier = Modifier.height(16.dp))
            DifficultyButton("Normal", Color(0xFFFFA726)) { onSelect(Difficulty.NORMAL) }
            Spacer(modifier = Modifier.height(16.dp))
            DifficultyButton("Hard", Color(0xFFEF5350)) { onSelect(Difficulty.HARD) }

            Spacer(modifier = Modifier.weight(1.5f))
        }
    }
}

@Composable
fun DifficultyButton(label: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(60.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(label, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun ActiveGameScreen(difficulty: Difficulty, onBackClick: () -> Unit) {
    val questions = remember(difficulty) {
        when (difficulty) {
            Difficulty.EASY -> listOf(
                ScrambleQuestion("MOUSE", "SEUOM"),
                ScrambleQuestion("SCREEN", "NEERCS"),
                ScrambleQuestion("CHIP", "PIHC"),
                ScrambleQuestion("WEB", "BEW"),
                ScrambleQuestion("DISK", "KSID")
            )
            Difficulty.NORMAL -> listOf(
                ScrambleQuestion("KEYBOARD", "DRAOBYEK"),
                ScrambleQuestion("MONITOR", "ROTINOM"),
                ScrambleQuestion("PRINTER", "RETNIRP"),
                ScrambleQuestion("LAPTOP", "POTPAL"),
                ScrambleQuestion("WINDOWS", "SWODNIW")
            )
            Difficulty.HARD -> listOf(
                ScrambleQuestion("ALGORITHM", "MHTIROGLA"),
                ScrambleQuestion("HARDWARE", "ERAWDRAH"),
                ScrambleQuestion("SOFTWARE", "ERAWTFOS"),
                ScrambleQuestion("PROCESSOR", "ROSSECORP"),
                ScrambleQuestion("DATABASE", "ESABATAD")
            )
        }
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var userInput by remember { mutableStateOf("") }
    var isGameOver by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var feedbackMessage by remember { mutableStateOf("") }

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
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                }
                Text(
                    "Round: ${difficulty.name}",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            if (!isGameOver) {
                Spacer(modifier = Modifier.height(40.dp))
                
                Text(
                    "Question ${currentIndex + 1}/5",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            questions[currentIndex].scrambled,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 4.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = userInput,
                    onValueChange = { userInput = it.uppercase() },
                    label = { Text("Your Answer", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    feedbackMessage,
                    color = if (feedbackMessage.contains("Correct")) Color(0xFFC8E6C9) else Color(0xFFFFCDD2),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        if (userInput == questions[currentIndex].original) {
                            score++
                            feedbackMessage = "Correct! Well done."
                        } else {
                            feedbackMessage = "Incorrect! The word was ${questions[currentIndex].original}"
                        }
                        
                        if (currentIndex < 4) {
                            currentIndex++
                            userInput = ""
                        } else {
                            isGameOver = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF4A90E2))
                ) {
                    Text(if (currentIndex < 4) "Next Question" else "Finish", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    "Game Over!",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Black
                )
                
                Text(
                    "Your Score: $score/5",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        currentIndex = 0
                        userInput = ""
                        isGameOver = false
                        score = 0
                        feedbackMessage = ""
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF4A90E2))
                ) {
                    Text("Play Again", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.weight(1.5f))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WordScramblePreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        WordScrambleGameScreen(onBackClick = {})
    }
}
