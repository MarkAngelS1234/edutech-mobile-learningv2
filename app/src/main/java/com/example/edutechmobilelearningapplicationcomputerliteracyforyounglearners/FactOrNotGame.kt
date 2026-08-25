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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import kotlinx.coroutines.delay

data class FactQuestion(val statement: String, val isFact: Boolean)

@Composable
fun FactOrNotGameScreen(onBackClick: () -> Unit) {
    var gameState by remember { mutableStateOf("intro") }
    var currentRound by remember { mutableIntStateOf(1) } // 1: Easy, 2: Normal, 3: Hard
    var totalScore by remember { mutableIntStateOf(0) }
    var countdown by remember { mutableIntStateOf(3) }

    val easyQuestions = remember {
        listOf(
            FactQuestion("A mouse is used to type letters.", false),
            FactQuestion("The monitor shows you pictures.", true),
            FactQuestion("You can use a computer to play games.", true),
            FactQuestion("A printer puts digital text onto paper.", true),
            FactQuestion("Computers can run without electricity.", false)
        )
    }

    val normalQuestions = remember {
        listOf(
            FactQuestion("RAM stands for Read Access Memory.", false),
            FactQuestion("Hardware are the physical parts of a computer.", true),
            FactQuestion("The CPU is the 'brain' of the computer.", true),
            FactQuestion("An operating system is a type of hardware.", false),
            FactQuestion("Keyboard is an input device.", true)
        )
    }

    val hardQuestions = remember {
        listOf(
            FactQuestion("Binary uses only the numbers 0 and 1.", true),
            FactQuestion("A firewall is a physical wall to protect computers.", false),
            FactQuestion("SSD is faster than a traditional Hard Drive.", true),
            FactQuestion("The first computer programmer was Ada Lovelace.", true),
            FactQuestion("Artificial Intelligence can think exactly like a human.", false)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF4A90E2), Color(0xFF50E3C2))
                )
            )
    ) {
        AnimatedContent(
            targetState = gameState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "FactOrNotTransition"
        ) { state ->
            when (state) {
                "intro" -> FactOrNotIntro(
                    onBackClick = onBackClick,
                    onStart = { gameState = "directives" }
                )
                "directives" -> FactOrNotDirectives(
                    onStart = { gameState = "round_announcement" }
                )
                "round_announcement" -> {
                    val roundTitle = when(currentRound) {
                        1 -> "EASY ROUND"
                        2 -> "NORMAL ROUND"
                        else -> "HARD ROUND"
                    }
                    LaunchedEffect(Unit) {
                        delay(2000)
                        countdown = 3
                        gameState = "countdown"
                    }
                    RoundAnnouncement(roundTitle)
                }
                "countdown" -> {
                    LaunchedEffect(Unit) {
                        while (countdown > 0) {
                            delay(1000)
                            countdown--
                        }
                        gameState = "playing"
                    }
                    FactOrNotCountdown(countdown)
                }
                "playing" -> {
                    val currentQuestions = when(currentRound) {
                        1 -> easyQuestions
                        2 -> normalQuestions
                        else -> hardQuestions
                    }
                    FactOrNotPlaying(
                        questions = currentQuestions,
                        onRoundComplete = { roundScore ->
                            totalScore += roundScore
                            if (currentRound < 3) {
                                currentRound++
                                gameState = "round_announcement"
                            } else {
                                gameState = "result"
                            }
                        }
                    )
                }
                "result" -> {
                    FactOrNotResult(
                        finalScore = totalScore,
                        onPlayAgain = {
                            gameState = "intro"
                            currentRound = 1
                            totalScore = 0
                        },
                        onExit = onBackClick
                    )
                }
            }
        }
    }
}

@Composable
fun FactOrNotIntro(onBackClick: () -> Unit, onStart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            "Welcome to Fact or Not!", 
            fontSize = 32.sp, 
            color = Color.White, 
            fontWeight = FontWeight.Black, 
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Test your computer knowledge!", 
            fontSize = 18.sp, 
            color = Color.White.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth(0.7f).height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF4A90E2)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Play Game", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.weight(1.5f))
    }
}

@Composable
fun FactOrNotDirectives(onStart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Directives", fontSize = 28.sp, color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
        ) {
            Text(
                "Read the statement carefully. Decide if it is a FACT or NOT (True or False).\n\nThere are 3 rounds: Easy, Normal, and Hard.\nEach round has 5 questions.",
                modifier = Modifier.padding(24.dp),
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                lineHeight = 26.sp
            )
        }
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF4A90E2)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Got it!", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}

@Composable
fun RoundAnnouncement(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = title,
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FactOrNotCountdown(count: Int) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = if (count > 0) count.toString() else "GO!",
            fontSize = 120.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
        )
    }
}

@Composable
fun FactOrNotPlaying(questions: List<FactQuestion>, onRoundComplete: (Int) -> Unit) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var roundScore by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text("Question ${currentIndex + 1}/5", color = Color.White.copy(alpha = 0.7f), fontSize = 18.sp)
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text(
                    questions[currentIndex].statement,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = {
                    if (questions[currentIndex].isFact) roundScore++
                    if (currentIndex < 4) currentIndex++ else onRoundComplete(roundScore)
                },
                modifier = Modifier.weight(1f).height(70.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("FACT", fontWeight = FontWeight.Black, fontSize = 20.sp)
            }
            Button(
                onClick = {
                    if (!questions[currentIndex].isFact) roundScore++
                    if (currentIndex < 4) currentIndex++ else onRoundComplete(roundScore)
                },
                modifier = Modifier.weight(1f).height(70.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("NOT", fontWeight = FontWeight.Black, fontSize = 20.sp)
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun FactOrNotResult(finalScore: Int, onPlayAgain: () -> Unit, onExit: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Game Over!", fontSize = 40.sp, color = Color.White, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Total Score: $finalScore / 15", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onPlayAgain,
            modifier = Modifier.fillMaxWidth(0.7f).height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF4A90E2)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Play Again", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onExit,
            modifier = Modifier.fillMaxWidth(0.7f).height(56.dp),
            border = BorderStroke(2.dp, Color.White),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Back to Games")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FactOrNotPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        FactOrNotGameScreen(onBackClick = {})
    }
}
