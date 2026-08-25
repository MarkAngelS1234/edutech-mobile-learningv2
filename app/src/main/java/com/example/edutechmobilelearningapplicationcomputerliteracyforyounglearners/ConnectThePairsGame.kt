package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun ConnectThePairsGameScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    var gameState by remember { mutableStateOf("intro") }
    var countdown by remember { mutableIntStateOf(3) }

    // Auto-rotate to landscape when entering the game
    DisposableEffect(Unit) {
        val activity = context as? Activity
        val originalOrientation = activity?.requestedOrientation ?: ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        onDispose {
            activity?.requestedOrientation = originalOrientation
        }
    }

    LaunchedEffect(gameState) {
        if (gameState == "countdown") {
            countdown = 3
            while (countdown > 0) {
                delay(1000)
                countdown--
            }
            gameState = "line_matching"
        }
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
            label = "StateTransition"
        ) { state ->
            when (state) {
                "intro" -> GameIntro(onStart = { gameState = "countdown" }, onBack = onBackClick)
                "countdown" -> GameCountdown(countdown)
                "line_matching" -> LineMatchingPhase(onComplete = { gameState = "drag_drop" })
                "drag_drop" -> DragDropPhase(onComplete = { gameState = "game_over" })
                "game_over" -> GameOverScreen(onBack = onBackClick)
            }
        }
    }
}

@Composable
fun GameIntro(onStart: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Welcome to Connect the Pairs!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Directives: First, connect the dots with lines. Then, drag items to their matching spots. Good luck!",
            fontSize = 18.sp,
            color = Color.White.copy(alpha = 0.9f),
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 600.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row {
            OutlinedButton(
                onClick = onBack,
                border = BorderStroke(2.dp, Color.White),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text("Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF4A90E2))
            ) {
                Text("Start Game", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun GameCountdown(count: Int) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = if (count > 0) count.toString() else "GO!",
            fontSize = 100.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
        )
    }
}

@Composable
fun LineMatchingPhase(onComplete: () -> Unit) {
    val items = listOf("CPU", "Monitor", "Keyboard", "Mouse", "Printer")
    val targets = listOf("Screen", "Brain", "Pointer", "Typing", "Paper")
    
    // Logic: CPU-Brain (0-1), Monitor-Screen (1-0), Keyboard-Typing (2-3), Mouse-Pointer (3-2), Printer-Paper (4-4)
    val correctPairs = mapOf(0 to 1, 1 to 0, 2 to 3, 3 to 2, 4 to 4)

    var selectedLeft by remember { mutableStateOf<Int?>(null) }
    var matches by remember { mutableStateOf(setOf<Pair<Int, Int>>()) }
    
    val leftPositions = remember { mutableStateMapOf<Int, Offset>() }
    val rightPositions = remember { mutableStateMapOf<Int, Offset>() }

    if (matches.size == items.size) {
        LaunchedEffect(Unit) {
            delay(1000)
            onComplete()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Phase 1: Line Connecting (5 Questions)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(24.dp))
        
        Box(modifier = Modifier.fillMaxSize()) {
            // Draw lines on canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                matches.forEach { (l, r) ->
                    val start = leftPositions[l]
                    val end = rightPositions[r]
                    if (start != null && end != null) {
                        drawLine(
                            color = Color.White,
                            start = start,
                            end = end,
                            strokeWidth = 4f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                    }
                }
            }

            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly) {
                // Left Column
                Column(verticalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxHeight()) {
                    items.forEachIndexed { index, name ->
                        Button(
                            onClick = { 
                                if (!matches.any { it.first == index }) {
                                    selectedLeft = index 
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when {
                                    matches.any { it.first == index } -> Color(0xFF66BB6A)
                                    selectedLeft == index -> Color.White
                                    else -> Color.White.copy(alpha = 0.2f)
                                }
                            ),
                            modifier = Modifier
                                .width(120.dp)
                                .onGloballyPositioned { coords ->
                                    val parentPos = coords.positionInParent()
                                    leftPositions[index] = Offset(parentPos.x + coords.size.width, parentPos.y + coords.size.height / 2)
                                }
                        ) {
                            Text(name, color = if (selectedLeft == index) Color(0xFF4A90E2) else Color.White)
                        }
                    }
                }

                // Right Column
                Column(verticalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxHeight()) {
                    targets.forEachIndexed { index, name ->
                        Button(
                            onClick = {
                                selectedLeft?.let { l ->
                                    if (correctPairs[l] == index) {
                                        matches = matches + (l to index)
                                    }
                                    selectedLeft = null
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (matches.any { it.second == index }) Color(0xFF66BB6A) else Color.White.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier
                                .width(120.dp)
                                .onGloballyPositioned { coords ->
                                    val parentPos = coords.positionInParent()
                                    rightPositions[index] = Offset(parentPos.x, parentPos.y + coords.size.height / 2)
                                }
                        ) {
                            Text(name, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DragDropPhase(onComplete: () -> Unit) {
    val items = listOf("Hardware", "Software", "Internet", "AI", "Safety")
    val correctTargets = listOf("Physical", "Apps", "Global Network", "Machine Learning", "Rules")
    
    val solvedIndices = remember { mutableStateListOf<Int>() }
    val targetPositions = remember { mutableStateMapOf<String, LayoutCoordinates>() }

    if (solvedIndices.size == 5) {
        LaunchedEffect(Unit) {
            delay(1000)
            onComplete()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Phase 2: Drag and Drop (5 Questions)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceAround) {
            // Source Items
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
                items.forEachIndexed { index, name ->
                    Box(modifier = Modifier.height(60.dp), contentAlignment = Alignment.Center) {
                        if (index !in solvedIndices) {
                            DraggablePairItem(name) { droppedOffset ->
                                // Check if dropped near the correct target
                                var foundMatch = false
                                targetPositions.forEach { (targetName, coords) ->
                                    val rect = coords.boundsInParent()
                                    if (rect.contains(droppedOffset)) {
                                        val isCorrect = when(name) {
                                            "Hardware" -> targetName == "Physical"
                                            "Software" -> targetName == "Apps"
                                            "Internet" -> targetName == "Global Network"
                                            "AI" -> targetName == "Machine Learning"
                                            "Safety" -> targetName == "Rules"
                                            else -> false
                                        }
                                        if (isCorrect) {
                                            solvedIndices.add(index)
                                            foundMatch = true
                                        }
                                    }
                                }
                                foundMatch
                            }
                        }
                    }
                }
            }

            // Target Slots
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
                correctTargets.forEach { target ->
                    Surface(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(width = 180.dp, height = 50.dp)
                            .onGloballyPositioned { targetPositions[target] = it },
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(target, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DraggablePairItem(name: String, onDrop: (Offset) -> Boolean) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var currentPosition by remember { mutableStateOf(Offset.Zero) }

    Surface(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .onGloballyPositioned { currentPosition = it.positionInParent() }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        val droppedAt = Offset(currentPosition.x + size.width / 2, currentPosition.y + size.height / 2)
                        if (!onDrop(droppedAt)) {
                            offsetX = 0f
                            offsetY = 0f
                        }
                    }
                ) { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
            .size(width = 120.dp, height = 50.dp),
        color = Color.White,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 4.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(name, color = Color(0xFF4A90E2), fontWeight = FontWeight.Bold)
        }
    }
}

// Extension to get parent bounds
fun LayoutCoordinates.boundsInParent(): androidx.compose.ui.geometry.Rect {
    val pos = positionInParent()
    return androidx.compose.ui.geometry.Rect(pos.x, pos.y, pos.x + size.width, pos.y + size.height)
}

@Composable
fun GameOverScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("CONGRATULATIONS!", fontSize = 48.sp, fontWeight = FontWeight.Black, color = Color.White)
        Text("You've mastered all 10 questions!", fontSize = 24.sp, color = Color.White)
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF4A90E2))
        ) {
            Text("Back to Games", fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
fun ConnectPairsPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ConnectThePairsGameScreen(onBackClick = {})
    }
}
