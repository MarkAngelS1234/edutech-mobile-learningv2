package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners


import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

enum class GameState { INSTRUCTIONS, PLAYING, GAME_OVER }

data class SoftwareGameQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)
@Composable
fun CloudDecoration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(120.dp, 60.dp)) {
        drawCircle(
            color = Color.White.copy(alpha = 0.3f),
            radius = size.height * 0.5f,
            center = Offset(size.width * 0.3f, size.height * 0.5f)
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.3f),
            radius = size.height * 0.7f,
            center = Offset(size.width * 0.5f, size.height * 0.4f)
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.3f),
            radius = size.height * 0.5f,
            center = Offset(size.width * 0.7f, size.height * 0.5f)
        )
    }
}

@Composable
fun ThinkAndChooseTitle(
    text: String,
    modifier: Modifier = Modifier,
    lineHeight: TextUnit = 45.sp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = text,
            color = Color.White,
            fontFamily = Kavoon,
            fontSize = 59.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = lineHeight,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun ThinkAndChooseGameScreen(
    onBackClick: () -> Unit,
    viewModel: CourseViewModel? = null,
    initialGameState: GameState = GameState.INSTRUCTIONS,
    initialScore: Int = 0,
    titleLineHeight: TextUnit = 50.sp
) {
    val realViewModel: CourseViewModel? = if (LocalInspectionMode.current) null else {
        viewModel ?: viewModel()
    }

    val questions = remember {
        listOf(
            SoftwareGameQuestion(
                "Which software should you use, when you want to write a story?",
                listOf("Microsoft Word", "YouTube"),
                "Microsoft Word"
            ),
            SoftwareGameQuestion(
                "You want to search for information about Dinosaurs. Which software should you use?",
                listOf("Duolingo", "Google Chrome"),
                "Google Chrome"
            ),
            SoftwareGameQuestion(
                "You want to send a message to your friend. Which software should you use?",
                listOf("Minecraft", "Messenger"),
                "Messenger"
            ),
            SoftwareGameQuestion(
                "You want to watch a learning video. Which software should you use?",
                listOf("YouTube", "Microsoft Word"),
                "YouTube"
            ),
            SoftwareGameQuestion(
                "You want to practice a new language. Which software should you use?",
                listOf("Minecraft", "Duolingo"),
                "Duolingo"
            ),
            SoftwareGameQuestion(
                "You want to change the size of the words in your story. Which software can help you?",
                listOf("YouTube", "Microsoft Word"),
                "Microsoft Word"
            ),
            SoftwareGameQuestion(
                "You want to read information from a website. Which software can you use?",
                listOf("Candy Crush", "Google Chrome"),
                "Google Chrome"
            ),
            SoftwareGameQuestion(
                "You want to talk to a family member through a video call. Which software can you use?",
                listOf("Messenger", "Microsoft Word"),
                "Messenger"
            ),
            SoftwareGameQuestion(
                "You want to watch a video that teaches you how to draw. Which software should you choose?",
                listOf("Roblox", "YouTube"),
                "YouTube"
            ),
            SoftwareGameQuestion(
                "You want to answer language questions and match words. Which software should you choose?",
                listOf("Messenger", "Duolingo"),
                "Duolingo"
            ),
            SoftwareGameQuestion(
                "You want to build different things in a game. Which software should you choose?",
                listOf("Minecraft", "Microsoft Word"),
                "Minecraft"
            ),
            SoftwareGameQuestion(
                "You want to play a game where you match pieces to solve puzzles. Which software should you choose?",
                listOf("Minecraft", "Candy Crush"),
                "Candy Crush"
            ),
            SoftwareGameQuestion(
                "You want to play a game and avoid obstacles while moving quickly. Which software should you choose?",
                listOf("Subway Surfers", "Google Chrome"),
                "Subway Surfers"
            ),
            SoftwareGameQuestion(
                "You want to play a game where you can create and explore different worlds. Which software should you choose?",
                listOf("Roblox", "Microsoft Word"),
                "Roblox"
            ),
            SoftwareGameQuestion(
                "You want to watch videos for fun or learning. Which software should you choose?",
                listOf("YouTube", "Minecraft"),
                "YouTube"
            )
        )
    }
    var gameState by remember { mutableStateOf(initialGameState) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(if (initialGameState == GameState.GAME_OVER && initialScore == 0) 12 else initialScore) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var showFeedback by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF4A90E2), Color(0xFFA173FA))
                )
            )
    ) {
        // Decorations
        CloudDecoration(Modifier.align(Alignment.TopStart).offset(x = (-30).dp, y = 20.dp))
        CloudDecoration(Modifier.align(Alignment.TopEnd).offset(x = 40.dp, y = (-10).dp))
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header (Back button stays at the top)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFFA173FA)
                    )
                }
            }

            // Title "Find the One" at the Top Center
            ThinkAndChooseTitle(
                text = "Find the Right One",
                modifier = Modifier.padding(top = 18.dp, bottom = 14.dp),
                lineHeight = titleLineHeight
            )

            // Main Content area
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = gameState,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(800)) + scaleIn(initialScale = 0.8f, animationSpec = tween(800)))
                            .togetherWith(fadeOut(animationSpec = tween(500)))
                    },
                    label = "StateTransition"
                ) { state ->
                    when (state) {
                        GameState.INSTRUCTIONS -> {
                            InstructionContent(onStartClick = { gameState = GameState.PLAYING })
                        }
                        GameState.PLAYING -> {
                            val currentQuestion = questions[currentIndex]

                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(32.dp))

                                Text(
                                    "Question ${currentIndex + 1}/${questions.size}",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontFamily = Kavoon,
                                    fontSize = 18.sp
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Question Card
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 24.dp),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
                                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                                ) {
                                    Text(
                                        text = currentQuestion.question,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(24.dp)
                                    )
                                }

                                // Options Section
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .verticalScroll(rememberScrollState()),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    currentQuestion.options.forEachIndexed { index, option ->
                                        OptionButton(
                                            index = index,
                                            text = option,
                                            isSelected = selectedOption == option,
                                            isEnabled = !showFeedback,
                                            onClick = { selectedOption = option }
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }

                                    if (showFeedback) {
                                        Spacer(modifier = Modifier.height(24.dp))
                                        Text(
                                            text = if (isCorrect) "CORRECT! ✨" else "INCORRECT. The right choice is ${currentQuestion.correctAnswer}",
                                            color = if (isCorrect) Color(0xFFC8E6C9) else Color(0xFFFFCDD2),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                // Action Button
                                Button(
                                    onClick = {
                                        if (!showFeedback) {
                                            isCorrect = selectedOption == currentQuestion.correctAnswer
                                            if (isCorrect) score++
                                            showFeedback = true
                                        } else {
                                            if (currentIndex < questions.size - 1) {
                                                currentIndex++
                                                selectedOption = null
                                                showFeedback = false
                                            } else {
                                                gameState = GameState.GAME_OVER
                                                // Save progress when game finishes
                                                realViewModel?.updateGameScore("Find the Right One", score)
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White,
                                        contentColor = Color(0xFFFFFFFF)
                                    ),
                                    enabled = if (!showFeedback) selectedOption != null else true
                                ) {
                                    Text(
                                        text = if (!showFeedback) "Submit Answer" else if (currentIndex < questions.size - 1) "Next Question" else "Finish",
                                        fontSize = 18.sp,
                                        fontFamily = Kavoon,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        GameState.GAME_OVER -> {
                            GameOverContent(
                                score = score,
                                total = questions.size,
                                onPlayAgain = {
                                    currentIndex = 0
                                    score = 0
                                    gameState = GameState.INSTRUCTIONS
                                    selectedOption = null
                                    showFeedback = false
                                },
                                onExit = onBackClick
                            )
                        }
                    }
                }
            }
        }

        // Bottom Decorations
        CloudDecoration(Modifier.align(Alignment.BottomStart).offset(x = (-40).dp, y = 30.dp).scale(1.5f))
    }
}

@Composable
fun InstructionContent(onStartClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Main Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                shape = RoundedCornerShape(40.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f)),
                border = BorderStroke(3.dp, Color(0xFFC5CAE9))
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Select the appropriate software name based on a question/situation",
                        fontSize = 18.sp,
                        fontFamily = Kavoon,
                        color = Color(0xFF1A237E),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth(),
                        lineHeight = 26.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "Read carefully and choose wisely.\nGood luck!",
                        fontSize = 18.sp,
                        fontFamily = Kavoon,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF673AB7),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth(),
                        lineHeight = 26.sp
                    )
                }
            }
            
            // Title pill overlay
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0xFF9575CD),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(
                            "Game Instructions",
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                            fontSize = 22.sp,
                            fontFamily = Kavoon,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(48.dp))

        // Start Button
        Box(contentAlignment = Alignment.Center) {
            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(72.dp),
                shape = RoundedCornerShape(36.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(4.dp, Color(0xFF9575CD))
            ) {
                Text(
                    "START GAME",
                    fontFamily = Kavoon,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF9575CD)
                )
            }

        }
    }
}

@Composable
fun GameOverContent(score: Int, total: Int, onPlayAgain: () -> Unit, onExit: () -> Unit) {
    var startAnimations by remember { mutableStateOf(false) }
    val animatedScore = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        startAnimations = true
        delay(600)
        animatedScore.animateTo(
            targetValue = score.toFloat(),
            animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
        )
    }

    val infiniteTransition = rememberInfiniteTransition(label = "gameOverEffects")

    // Shining effect offset
    val shineOffset by infiniteTransition.animateFloat(
        initialValue = -1000f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "shine"
    )

    // Morph sequence (bouncing scale and rotation)
    val morphScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "morphScale"
    )

    val wellDoneScale by animateFloatAsState(
        targetValue = if (startAnimations) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "entryScale"
    )

    // Toned down shine gradient
    val shineBrush = Brush.linearGradient(
        colors = listOf(Color(0xFFFFE082), Color.White, Color(0xFFFFE082)),
        start = Offset(shineOffset, 0f),
        end = Offset(shineOffset + 300f, 300f)
    )

    // Refactored dynamic response based on score percentage
    val (resultTitle, resultMessage) = when {
        score == total -> "PERFECT! 🏆" to "Fantastic! You're a software expert! \uD83D\uDC4F"
        score >= total * 0.8 -> "EXCELLENT! 🌟" to "Great job! You know your software! \uD83D\uDC4F"
        score >= total * 0.5 -> "WELL DONE! 👍" to "Good effort! Keep practicing! \uD83D\uDC4F"
        else -> "KEEP TRYING! 📚" to "Don't give up!"
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = resultTitle,
            fontSize = 50.sp,
            fontWeight = FontWeight.Black,
            fontFamily = Kavoon,
            textAlign = TextAlign.Center,
            style = TextStyle(
                brush = shineBrush,
                shadow = Shadow(color = Color(0xFFB8860B).copy(alpha = 0.5f), offset = Offset(2f, 2f), blurRadius = 4f)
            ),
            modifier = Modifier
                .scale(wellDoneScale * morphScale)
                .graphicsLayer {
                    rotationZ = (morphScale - 1f) * 30f
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your Score",
            fontSize = 20.sp,
            color = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.graphicsLayer {
                alpha = if (startAnimations) 1f else 0f
            }
        )

        Text(
            text = "${animatedScore.value.roundToInt()} / $total",
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.graphicsLayer {
                alpha = if (startAnimations) 1f else 0f
                scaleX = wellDoneScale
                scaleY = wellDoneScale
            }
        )
        
        AnimatedVisibility(
            visible = startAnimations && animatedScore.value >= score.toFloat() * 0.3f,
            enter = fadeIn(tween(800)) + expandVertically()
        ) {
            Text(
                resultMessage,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        AnimatedVisibility(
            visible = startAnimations && animatedScore.value >= score.toFloat() * 0.9f,
            enter = fadeIn(tween(1000)) + slideInVertically(initialOffsetY = { it / 2 })
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = onPlayAgain,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF2575FC))
                ) {
                    Text("Play Again", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = onExit,
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

@Composable
fun OptionButton(
    index: Int,
    text: String,
    isSelected: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    val letter = ('A' + index).toString()

    val (bgBrush, circleBrush, textColor, borderColor) = if (index % 2 == 0) {
        // Blue Theme (A)
        val colors = listOf(Color(0xFFE3F2FD), Color(0xFFE1F5FE))
        val circColors = listOf(Color(0xFF64B5F6), Color(0xFF2196F3))
        Quadruple(
            Brush.verticalGradient(colors),
            Brush.verticalGradient(circColors),
            Color(0xFF1A237E),
            Color(0xFF90CAF9)
        )
    } else {
        // Purple Theme (B)
        val colors = listOf(Color(0xFFF3E5F5), Color(0xFFEDE7F6))
        val circColors = listOf(Color(0xFFBA68C8), Color(0xFF9575CD))
        Quadruple(
            Brush.verticalGradient(colors),
            Brush.verticalGradient(circColors),
            Color(0xFF1A237E),
            Color(0xFFCE93D8)
        )
    }

    val iconRes = when (text) {
        "Microsoft Word" -> R.drawable.ms_word
        "YouTube" -> R.drawable.yout_ube
        "Duolingo" -> R.drawable.dou_lingo
        "Google Chrome" -> R.drawable.chrom_e
        "Minecraft" -> R.drawable.mine_craft
        "Candy Crush" -> R.drawable.cand_ycrush
        "Subway Surfers" -> R.drawable.sub_waysurf
        "Messenger" -> R.drawable.messenge_r
        "Roblox" -> R.drawable.roblo_x
        else -> 0
    }

    Surface(
        onClick = onClick,
        enabled = isEnabled,
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp)
            .padding(vertical = 4.dp),
        color = Color.Transparent,
        border = if (isSelected) BorderStroke(4.dp, Color.White) else BorderStroke(2.dp, borderColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgBrush)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                // Circle with Letter
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(circleBrush, CircleShape)
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = letter,
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = Kavoon
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Software Name
                Text(
                    text = text,
                    color = textColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Kavoon,
                    modifier = Modifier.weight(1f)
                )

                // Icon
                if (iconRes != 0) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .padding(end = 4.dp)
                    )
                }
            }
        }
    }
}

data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

@Preview(showBackground = true, name = "Game Instructions Screen")
@Composable
fun ThinkAndChooseInstructionsPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ThinkAndChooseGameScreen(onBackClick = {}, initialGameState = GameState.INSTRUCTIONS)
    }
}

@Preview(showBackground = true, name = "Game Active Screen")
@Composable
fun ThinkAndChoosePlayingPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ThinkAndChooseGameScreen(onBackClick = {}, initialGameState = GameState.PLAYING)
    }
}

@Preview(showBackground = true, name = "Game Results - Perfect 15/15")
@Composable
fun ThinkAndChooseGameOverPerfectPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ThinkAndChooseGameScreen(
            onBackClick = {},
            initialGameState = GameState.GAME_OVER,
            initialScore = 15
        )
    }
}

@Preview(showBackground = true, name = "Game Results - Excellent 12/15")
@Composable
fun ThinkAndChooseGameOverExcellentPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ThinkAndChooseGameScreen(
            onBackClick = {},
            initialGameState = GameState.GAME_OVER,
            initialScore = 12
        )
    }
}

@Preview(showBackground = true, name = "Game Results - Good 8/15")
@Composable
fun ThinkAndChooseGameOverGoodPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ThinkAndChooseGameScreen(
            onBackClick = {},
            initialGameState = GameState.GAME_OVER,
            initialScore = 8
        )
    }
}

@Preview(showBackground = true, name = "Game Results - Try Again 4/15")
@Composable
fun ThinkAndChooseGameOverTryAgainPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ThinkAndChooseGameScreen(
            onBackClick = {},
            initialGameState = GameState.GAME_OVER,
            initialScore = 4
        )
    }
}
