package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon

data class ScrambleQuestion(val original: String, val scrambled: String)

enum class Difficulty { EASY, NORMAL, HARD }

// Repository to store question data statically to prevent allocation overhead during recomposition
private object ScrambleRepository {
    private val easy = listOf(
        ScrambleQuestion("MOUSE", "OUSEM"),
        ScrambleQuestion("CPU", "PUC"),
        ScrambleQuestion("APP", "PPA"),
        ScrambleQuestion("LAPTOP", "LAPOTP"),
        ScrambleQuestion("TABLET", "TABELT"),
        ScrambleQuestion("MONITOR", "NOMTIOR"),
        ScrambleQuestion("PRINTER", "RIPNTER"),
        ScrambleQuestion("KEYS", "KYES"),
        ScrambleQuestion("DESK", "DSEK"),
        ScrambleQuestion("GAMES", "EGAMS")
    )
    private val normal = listOf(
        ScrambleQuestion("DESKTOP", "DSEKTOP"),
        ScrambleQuestion("KEYBOARD", "YEKBOARD"),
        ScrambleQuestion("SPEAKERS", "SPEKAERS"),
        ScrambleQuestion("HARDWARE", "RAHDAWRE"),
        ScrambleQuestion("SOFTWARE", "FOSWTARE"),
        ScrambleQuestion("BROWSER", "ROWBRES"),
        ScrambleQuestion("MESSENGER", "ESNEMGERS"),
        ScrambleQuestion("YOUTUBE", "TOUYUBE"),
        ScrambleQuestion("DUOLINGO", "LIDOUNGO"),
        ScrambleQuestion("UNIT", "TINU")
    )
    private val hard = listOf(
        ScrambleQuestion("COMPUTER", "RETUPMOC"),
        ScrambleQuestion("SMARTPHONE", "EPHONSMART"),
        ScrambleQuestion("CENTRAL", "LARTCEN"),
        ScrambleQuestion("SYSTEM", "MYSTES"),
        ScrambleQuestion("MICROSOFT", "OSMICROFT"),
        ScrambleQuestion("CHROME", "MORCHE"),
        ScrambleQuestion("MINECRAFT", "TRAFMINEC"),
        ScrambleQuestion("ROBLOX", "XOBLOR"),
        ScrambleQuestion("COMMUNICATE", "MUNICOACTEM"),
        ScrambleQuestion("INSTRUCTIONS", "STRUCINNOTIS")
    )

    fun getQuestions(difficulty: Difficulty): List<ScrambleQuestion> = when (difficulty) {
        Difficulty.EASY -> easy
        Difficulty.NORMAL -> normal
        Difficulty.HARD -> hard
    }
}

@Composable
private fun ScrambleCloud(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(160.dp, 90.dp)) {
        val baseColor = Color.White.copy(alpha = 0.25f)
        val innerColor = Color.White.copy(alpha = 0.15f)

        // Stylized fluffy cloud using multiple overlapping circles
        drawCircle(color = baseColor, radius = size.height * 0.5f, center = Offset(size.width * 0.7f, size.height * 0.55f))
        
        // Inner highlights for depth
        drawCircle(color = innerColor, radius = size.height * 0.3f, center = Offset(size.width * 0.4f, size.height * 0.45f))
        drawCircle(color = innerColor, radius = size.height * 0.25f, center = Offset(size.width * 0.6f, size.height * 0.4f))
    }
}

@Composable
fun WordScrambleGameScreen(
    onBackClick: () -> Unit,
    viewModel: CourseViewModel? = null,
    initialDifficulty: Difficulty? = null,
    initialGameOver: Boolean = false
) {
    var currentDifficulty by remember { mutableStateOf<Difficulty?>(initialDifficulty) }
    
    val realViewModel: CourseViewModel? = if (LocalInspectionMode.current) null else {
        viewModel ?: viewModel()
    }

    AnimatedContent(
        targetState = currentDifficulty,
        transitionSpec = {
            (fadeIn() + scaleIn(initialScale = 0.95f)) togetherWith (fadeOut() + scaleOut(targetScale = 0.95f))
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
                onBackClick = { currentDifficulty = null },
                viewModel = realViewModel,
                initialGameOver = initialGameOver
            )
        }
    }
}

@Composable
fun DifficultySelectionScreen(onBackClick: () -> Unit, onSelect: (Difficulty) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF4A90E2), Color(0xFFA173FA))))
    ) {
        // Corner Cloud Decorations (Reference-based placement)
        ScrambleCloud(Modifier.align(Alignment.TopStart).offset(x = (-40).dp, y = 10.dp))
        ScrambleCloud(Modifier.align(Alignment.TopEnd).offset(x = 40.dp, y = 20.dp).scale(0.8f))
        ScrambleCloud(Modifier.align(Alignment.BottomStart).offset(x = (-50).dp, y = 40.dp).scale(1.8f))
        ScrambleCloud(Modifier.align(Alignment.BottomEnd).offset(x = 50.dp, y = 30.dp).scale(1.6f))

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 16.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                text = "Word Scramble",
                color = Color.White,
                fontFamily = Kavoon,
                fontSize = 57.sp,
                lineHeight = 48.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.weight(0.8f))
            
            Text(
                text = "Select Difficulty",
                color = Color.White,
                fontFamily = Kavoon,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))

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
        modifier = Modifier.fillMaxWidth().height(80.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(20.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontFamily = Kavoon,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun WavyProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    
    // Smooth foreground wave phase
    val phase1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase1"
    )

    // Background wave phase (opposite direction, different speed for depth)
    val phase2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase2"
    )

    // Smoothly animate the progress fill
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(28.dp)
            .clip(RoundedCornerShape(14.dp)) // Removes pointed edges (pill shape)
            .background(Color.White.copy(alpha = 0.2f))
            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val fillWidth = width * animatedProgress
            
            if (fillWidth > 0) {
                // Secondary (background) layer of water
                val wavePath2 = Path().apply {
                    val waveHeight = 5.dp.toPx()
                    val waveLength = 65.dp.toPx()
                    
                    moveTo(0f, height)
                    lineTo(0f, height * 0.25f)
                    
                    var x = 0f
                    while (x <= fillWidth) {
                        val y = height * 0.3f + Math.sin((x / waveLength * 2 * Math.PI) + phase2).toFloat() * waveHeight
                        lineTo(x, y)
                        x += 2f
                    }
                    
                    lineTo(fillWidth, height)
                    close()
                }
                drawPath(path = wavePath2, color = Color(0xFFA173FA).copy(alpha = 0.6f))

                // Primary (foreground) layer of water
                val wavePath1 = Path().apply {
                    val waveHeight = 4.dp.toPx()
                    val waveLength = 45.dp.toPx()
                    
                    moveTo(0f, height)
                    lineTo(0f, height * 0.45f)
                    
                    var x = 0f
                    while (x <= fillWidth) {
                        val y = height * 0.45f + Math.sin((x / waveLength * 2 * Math.PI) + phase1).toFloat() * waveHeight
                        lineTo(x, y)
                        x += 2f
                    }
                    
                    lineTo(fillWidth, height)
                    close()
                }
                drawPath(path = wavePath1, color = Color.White)
            }
        }
    }
}

@Composable
fun ActiveGameScreen(
    difficulty: Difficulty, 
    onBackClick: () -> Unit, 
    viewModel: CourseViewModel? = null,
    initialGameOver: Boolean = false
) {
    var sessionKey by remember { mutableStateOf(0) }
    
    // Ensure all 10 questions are picked and shuffled for the round
    val questions = remember(difficulty, sessionKey) {
        ScrambleRepository.getQuestions(difficulty).shuffled().take(10)
    }

    var currentIndex by remember { mutableStateOf(0) }
    var userInput by remember { mutableStateOf("") }
    var isGameOver by remember { mutableStateOf(initialGameOver) }
    var score by remember { mutableStateOf(if (initialGameOver) 7 else 0) }
    var isAnswerChecked by remember { mutableStateOf(false) }
    
    // Fix for regression bug: store stable values for feedback text during exit animations
    var feedbackWord by remember { mutableStateOf("") }
    var feedbackIsCorrect by remember { mutableStateOf(false) }
    
    // Tracking results for each of the 10 questions for the final summary
    val results = remember(difficulty, sessionKey) { mutableStateListOf<Boolean?>() }
    LaunchedEffect(questions) {
        results.clear()
        repeat(questions.size) { index ->
            if (initialGameOver) {
                results.add(index % 3 != 0)
            } else {
                results.add(null)
            }
        }
    }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // UX performance: Auto-focus the input field
    LaunchedEffect(currentIndex, isGameOver) {
        if (!isGameOver && !isAnswerChecked) {
            focusRequester.requestFocus()
        }
    }

    val onActionClick: () -> Unit = {
        if (!isAnswerChecked) {
            val isCorrect = userInput.trim().equals(questions[currentIndex].original, ignoreCase = true)
            if (isCorrect) score++
            results[currentIndex] = isCorrect
            
            // Set feedback states before triggering visibility
            feedbackWord = questions[currentIndex].original
            feedbackIsCorrect = isCorrect
            
            isAnswerChecked = true
            focusManager.clearFocus()
        } else {
            if (currentIndex < questions.size - 1) {
                // Reset isAnswerChecked BEFORE incrementing currentIndex
                isAnswerChecked = false
                currentIndex++
                userInput = ""
            } else {
                isGameOver = true
                // Save cumulative points to database (adds even if not perfect)
                viewModel?.updateGameScore("Word Scramble", score)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF4A90E2), Color(0xFFA173FA))))
    ) {
        // Corner Cloud Decorations (Reference-based placement)
        ScrambleCloud(Modifier.align(Alignment.TopStart).offset(x = (-40).dp, y = 10.dp))
        ScrambleCloud(Modifier.align(Alignment.TopEnd).offset(x = 40.dp, y = 20.dp).scale(0.8f))
        ScrambleCloud(Modifier.align(Alignment.BottomStart).offset(x = (-50).dp, y = 40.dp).scale(1.8f))
        ScrambleCloud(Modifier.align(Alignment.BottomEnd).offset(x = 50.dp, y = 30.dp).scale(1.6f))

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            ) {
                IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                }
            }

            if (!isGameOver) {
                Spacer(modifier = Modifier.height(8.dp))
                
                // Refactored Progress Line with Animated Premium Water Wobbling Effect
                WavyProgressIndicator(
                    progress = (currentIndex + 1).toFloat() / questions.size,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                Text("Question ${currentIndex + 1} of ${questions.size}", color = Color.White.copy(alpha = 0.9f),fontFamily = Kavoon, fontSize = 16.sp)

                // Layout pushed significantly more upward to group interaction components
                Spacer(modifier = Modifier.weight(0.4f))

                Card(
                    modifier = Modifier.fillMaxWidth().height(140.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                    border = BorderStroke(2.dp, Color.White.copy(alpha = 0.4f))
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(questions[currentIndex].scrambled, fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 8.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = userInput,
                    onValueChange = { if (!isAnswerChecked) userInput = it.uppercase() },
                    label = { Text("Enter your guess",fontFamily = Kavoon, color = Color.White.copy(alpha = 0.7f)) },
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White, unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    enabled = !isAnswerChecked,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onActionClick() })
                )

                Spacer(modifier = Modifier.height(12.dp))
                
                // Consistent container for feedback to avoid layout shifting
                Box(modifier = Modifier.height(48.dp), contentAlignment = Alignment.Center) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = isAnswerChecked,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Text(
                            text = if (feedbackIsCorrect) "Correct! Well done." else "Incorrect! The word was $feedbackWord",
                            color = if (feedbackIsCorrect) Color(0xFFC8E6C9) else Color(0xFFFFCDD2),
                            fontSize = 18.sp, fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                // Properly placed more upward main action button
                Button(
                    onClick = { onActionClick() },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF4A90E2))
                ) {
                    val btnText = if (!isAnswerChecked) "CHECK ANSWER" else if (currentIndex < questions.size - 1) "NEXT QUESTION" else "FINISH"
                    Text(btnText, fontFamily = Kavoon, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                }

                // Generous weight-based layout space pushing difficulty text cleanly down to the center bottom area
                Spacer(modifier = Modifier.weight(1f))
                
                // Difficulty text put down at the center
                Text(
                    text = "DIFFICULTY: ${difficulty.name}", 
                    color = Color.White,
                    fontFamily = Kavoon,
                    fontSize = 18.sp, 
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))

            } else {
                // End of Round Summary
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Animations for Game Over Summary
                    val infiniteTransition = rememberInfiniteTransition(label = "congratsAnim")
                    val scale by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 1.08f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1200, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "pulseScale"
                    )
                    
                    val shineTranslate by infiniteTransition.animateFloat(
                        initialValue = -500f,
                        targetValue = 1500f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2500, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "shineTranslate"
                    )

                    var targetScore by remember { mutableIntStateOf(0) }
                    LaunchedEffect(Unit) {
                        targetScore = score
                    }
                    val animatedScore by animateIntAsState(
                        targetValue = targetScore,
                        animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
                        label = "scoreCount"
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Text(
                        text = "CONGRATULATIONS!",
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(Color.White, Color(0xFFFFD700), Color.White, Color(0xFFFFD700), Color.White),
                                start = Offset(shineTranslate, 0f),
                                end = Offset(shineTranslate + 300f, 300f)
                            )
                        ),
                        fontFamily = Kavoon,
                        fontSize = 29.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                    )
                    
                    Text(
                        text = "Final Score: $animatedScore / ${questions.size}",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Round Summary:", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    questions.forEachIndexed { index, question ->
                        val isCorrect = results.getOrNull(index) == true
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Clear,
                                    contentDescription = null,
                                    tint = if (isCorrect) Color.Green else Color.Red
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Word: ${question.original}", color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("Scrambled: ${question.scrambled}", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = {
                            sessionKey++
                            currentIndex = 0
                            userInput = ""
                            isGameOver = false
                            score = 0
                            isAnswerChecked = false
                        },
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF4A90E2))
                    ) {
                        Text("PLAY AGAIN", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Difficulty Selection Screen")
@Composable
fun WordScrambleDifficultySelectionPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        WordScrambleGameScreen(onBackClick = {}, initialDifficulty = null)
    }
}

@Preview(showBackground = true, name = "Active Game Screen (Easy)")
@Composable
fun WordScrambleActiveGameEasyPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        WordScrambleGameScreen(onBackClick = {}, initialDifficulty = Difficulty.EASY)
    }
}

@Preview(showBackground = true, name = "Active Game Screen (Normal)")
@Composable
fun WordScrambleActiveGameNormalPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        WordScrambleGameScreen(onBackClick = {}, initialDifficulty = Difficulty.NORMAL)
    }
}

@Preview(showBackground = true, name = "Active Game Screen (Hard)")
@Composable
fun WordScrambleActiveGameHardPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        WordScrambleGameScreen(onBackClick = {}, initialDifficulty = Difficulty.HARD)
    }
}

@Preview(showBackground = true, name = "Game Result Summary Screen")
@Composable
fun WordScrambleGameResultPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        WordScrambleGameScreen(onBackClick = {}, initialDifficulty = Difficulty.NORMAL, initialGameOver = true)
    }
}
