package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class GameQuestion(
    val id: Int,
    val item: String,
    val target: String,
    val phase: Int
)

@Composable
fun GameBackground(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF4A90E2), Color(0xFFA173FA))))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Top decorative clouds
            val cloudWhite = Color.White.copy(alpha = 0.25f)
            // Left top
            drawCircle(cloudWhite, radius = 35.dp.toPx(), center = Offset(0f, 40.dp.toPx()))
            
            // Right top
            drawCircle(cloudWhite, radius = 35.dp.toPx(), center = Offset(size.width, 40.dp.toPx()))
            drawCircle(cloudWhite, radius = 50.dp.toPx(), center = Offset(size.width - 40.dp.toPx(), 20.dp.toPx()))
            drawCircle(cloudWhite, radius = 30.dp.toPx(), center = Offset(size.width - 90.dp.toPx(), 45.dp.toPx()))

            // Bottom decorative purple shapes
            val softPurple = Color(0xFFC084FC).copy(alpha = 0.35f)
            // Left bottom
            drawCircle(softPurple, radius = 130.dp.toPx(), center = Offset(30.dp.toPx(), size.height))
            drawCircle(softPurple, radius = 100.dp.toPx(), center = Offset(150.dp.toPx(), size.height + 20.dp.toPx()))
            
            // Right bottom
            drawCircle(softPurple, radius = 130.dp.toPx(), center = Offset(size.width - 30.dp.toPx(), size.height))
            drawCircle(softPurple, radius = 100.dp.toPx(), center = Offset(size.width - 150.dp.toPx(), size.height + 20.dp.toPx()))
        }
        content()
    }
}

@Composable
fun ConnectThePairsGameScreen(
    onBackClick: () -> Unit,
    viewModel: CourseViewModel? = null,
    initialState: String = "intro",
    initialScore: Int = 0,
    initialScreenIndex: Int = 0
) {
    val context = LocalContext.current
    var gameState by rememberSaveable { mutableStateOf(initialState) }
    var totalScore by rememberSaveable { mutableIntStateOf(initialScore) }

    val isInspectionMode = LocalInspectionMode.current
    val realViewModel: CourseViewModel? = if (isInspectionMode) null else {
        viewModel ?: viewModel()
    }

    val phase1FullPool = remember {
        listOf(
            GameQuestion(1, "Keyboard", "TYPE", 1),
            GameQuestion(2, "Monitor", "SEE", 1),
            GameQuestion(3, "Mouse", "POINT & CLICK", 1),
            GameQuestion(4, "Speakers", "HEAR", 1),
            GameQuestion(5, "Printer", "PRINT", 1),
            GameQuestion(6, "CPU", "PROCESS", 1),
        )
    }

    val phase2FullPool = remember {
        listOf(
            GameQuestion(7, "Word", "WRITE", 2),
            GameQuestion(8, "Chrome", "SEARCH", 2),
            GameQuestion(9, "Messenger", "TALK", 2),
            GameQuestion(10, "Duolingo", "LEARN", 2),
            GameQuestion(11, "Minecraft", "BUILD", 2),
            GameQuestion(12, "Youtube", "VIDEO", 2),
        )
    }

    var randomizedP1 by remember { mutableStateOf(phase1FullPool.shuffled()) }
    var randomizedP2 by remember { mutableStateOf(phase2FullPool.shuffled()) }

    fun Context.findActivity(): Activity? {
        var currentContext = this
        while (currentContext is ContextWrapper) {
            if (currentContext is Activity) return currentContext
            currentContext = currentContext.baseContext
        }
        return null
    }

    if (!isInspectionMode) {
        DisposableEffect(Unit) {
            val activity = context.findActivity()
            val originalOrientation = activity?.requestedOrientation ?: ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            onDispose { activity?.requestedOrientation = originalOrientation }
        }
    }

    GameBackground {
        AnimatedContent(
            targetState = gameState,
            transitionSpec = {
                (fadeIn(animationSpec = tween(800)) + scaleIn(initialScale = 0.8f, animationSpec = tween(800)))
                    .togetherWith(fadeOut(animationSpec = tween(500)))
            },
            label = "StateTransition"
        ) { state ->
            when (state) {
                "intro" -> GameIntro(
                    onStart = {
                        randomizedP1 = phase1FullPool.shuffled()
                        randomizedP2 = phase2FullPool.shuffled()
                        totalScore = 0
                        gameState = "line_matching"
                    },
                    onBack = onBackClick
                )
                "line_matching" -> LineMatchingPhase(
                    questions = randomizedP1,
                    onComplete = { score ->
                        totalScore += score
                        gameState = "drag_drop"
                    },
                    initialScreenIndex = initialScreenIndex
                )
                "drag_drop" -> DragDropPhase(
                    questions = randomizedP2,
                    onComplete = { score ->
                        val phase2ScoreWithBonus = score + 1
                        totalScore += phase2ScoreWithBonus
                        realViewModel?.updateGameScore("Match and Learn", totalScore)
                        gameState = "game_over"
                    },
                    initialScreenIndex = initialScreenIndex
                )
                "game_over" -> GameOverScreen(
                    score = totalScore,
                    onRestart = { 
                        totalScore = 0
                        gameState = "intro" 
                    },
                    onBack = onBackClick
                )
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
            text = "Match and Learn!", 
            fontFamily = Kavoon, 
            fontSize = 64.sp, 
            color = Color.White,
            textAlign = TextAlign.Center,
            style = TextStyle(
                shadow = Shadow(color = Color.Black.copy(alpha = 0.3f), offset = Offset(4f, 4f), blurRadius = 8f)
            )
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        InstructionExpandableButton()
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Row {
            OutlinedButton(
                onClick = onBack, 
                border = BorderStroke(3.dp, Color.White), 
                modifier = Modifier.height(60.dp).width(160.dp),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text("Back", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(32.dp))
            Button(
                onClick = onStart, 
                colors = ButtonDefaults.buttonColors(containerColor = Color.White), 
                modifier = Modifier.height(60.dp).width(200.dp),
                shape = RoundedCornerShape(30.dp),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text("Start Game", color = Color(0xFF9575CD), fontWeight = FontWeight.Black, fontFamily = Kavoon, fontSize = 22.sp)
            }
        }
    }
}

@Composable
fun InstructionExpandableButton() {
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 0.dp else 12.dp,
        label = "elevation"
    )

    Surface(
        onClick = { expanded = !expanded },
        interactionSource = interactionSource,
        shape = RoundedCornerShape(24.dp),
        color = Color.White.copy(alpha = 0.95f),
        border = BorderStroke(4.dp, Color(0xFFFFD700)), // Golden cartoon border
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .animateContentSize(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)),
        shadowElevation = elevation
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (expanded) "Got it! 👍" else "How to Play? 💡",
                    fontFamily = Kavoon,
                    fontSize = 24.sp,
                    color = Color(0xFF9575CD)
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InstructionRow("✏️", "Phase 1: Match Lines", "Draw lines from the object to its purpose!")
                    InstructionRow("🎯", "Phase 2: Drag & Drop", "Drag the app icon to the correct usage box!")
                    InstructionRow("✨", "Score Big!", "2 points for every correct match + Bonus points!")
                }
            }
        }
    }
}

@Composable
fun InstructionRow(emoji: String, title: String, desc: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF0F4FF), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, fontSize = 24.sp)
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.ExtraBold, color = Color(0xFF333333), fontSize = 16.sp)
            Text(desc, color = Color(0xFF666666), fontSize = 14.sp, lineHeight = 18.sp)
        }
    }
}

@Composable
fun LineMatchingPhase(questions: List<GameQuestion>, onComplete: (Int) -> Unit, initialScreenIndex: Int = 0) {
    var screenIndex by remember { mutableIntStateOf(initialScreenIndex) }
    var totalScoreP1 by remember { mutableIntStateOf(0) }

    val currentBatch = remember(screenIndex, questions) {
        val start = screenIndex * 3
        questions.subList(start, (start + 3).coerceAtMost(questions.size))
    }

    key(currentBatch) {
        LineMatchingView(
            questions = currentBatch,
            screenTitle = "Phase 1: Line Matching (${screenIndex + 1}/2)",
            onScreenComplete = { score ->
                totalScoreP1 += score
                if ((screenIndex + 1) * 3 >= questions.size) {
                    onComplete(totalScoreP1)
                } else {
                    screenIndex++
                }
            }
        )
    }
}

@Composable
fun LineMatchingView(
    questions: List<GameQuestion>,
    screenTitle: String,
    onScreenComplete: (Int) -> Unit,
    isTimerEnabled: Boolean = !LocalInspectionMode.current
) {
    var activeIdx by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(10) }
    var score by remember { mutableIntStateOf(0) }
    var feedback by remember { mutableStateOf<Pair<String, Color>?>(null) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val targets = remember(questions) { questions.map { it.target }.shuffled() }
    val itemPositions = remember { mutableStateMapOf<Int, Offset>() }
    val targetPositions = remember { mutableStateMapOf<Int, Offset>() }
    var containerPos by remember { mutableStateOf(Offset.Zero) }

    var startPos by remember { mutableStateOf<Offset?>(null) }
    var currentDragPos by remember { mutableStateOf<Offset?>(null) }

    val results = remember { mutableStateMapOf<Int, Boolean>() }
    val matchedPairs = remember { mutableStateListOf<Pair<Int, Int>>() }

    val hoveredIdx = remember(currentDragPos) {
        if (currentDragPos == null) null
        else {
            var closestIdx: Int? = null
            var closestDist = Float.MAX_VALUE
            val threshold = with(density) { 95.dp.toPx() }
            targetPositions.forEach { (idx, pos) ->
                val dist = (currentDragPos!! - pos).getDistance()
                if (dist < threshold && dist < closestDist) {
                    closestDist = dist
                    closestIdx = idx
                }
            }
            closestIdx
        }
    }

    LaunchedEffect(activeIdx, questions) {
        if (!isTimerEnabled) return@LaunchedEffect
        timeLeft = 10
        while (timeLeft > 0) {
            delay(1000)
            if (feedback == null) timeLeft--
        }
        if (feedback == null) {
            feedback = "Time's up!" to Color.Gray
            results[activeIdx] = false
            delay(1000)
            feedback = null
            if (activeIdx < questions.size - 1) activeIdx++ else onScreenComplete(score)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).onGloballyPositioned { containerPos = it.positionInRoot() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(screenTitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text("Timer: ${timeLeft}s", color = if (timeLeft < 3) Color.Red else Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
            Text("Score: $score", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth().pointerInput(activeIdx) {
            detectDragGestures(
                onDragStart = { offset ->
                    if (feedback != null) return@detectDragGestures
                    val p = itemPositions[activeIdx]
                    if (p != null) {
                        val distance = (offset - p).getDistance()
                        val threshold = with(density) { 100.dp.toPx() }
                        if (distance < threshold) {
                            startPos = p
                            currentDragPos = offset
                        }
                    }
                },
                onDrag = { change, dragAmount ->
                    if (startPos != null) {
                        change.consume()
                        currentDragPos = (currentDragPos ?: startPos!!) + dragAmount
                    }
                },
                onDragEnd = {
                    if (startPos != null && currentDragPos != null) {
                        var foundTargetIdx: Int? = null
                        var closestDist = Float.MAX_VALUE
                        val threshold = with(density) { 95.dp.toPx() }
                        targetPositions.forEach { (idx, pos) ->
                            val dist = (currentDragPos!! - pos).getDistance()
                            if (dist < threshold && dist < closestDist) {
                                closestDist = dist
                                foundTargetIdx = idx
                            }
                        }

                        if (foundTargetIdx != null) {
                            val targetIdx = foundTargetIdx!!
                            if (targets[targetIdx] == questions[activeIdx].target) {
                                score += 2
                                results[activeIdx] = true
                                matchedPairs.add(activeIdx to targetIdx)
                                if (activeIdx < questions.size - 1) {
                                    activeIdx++
                                } else {
                                    onScreenComplete(score)
                                }
                            } else {
                                feedback = "Wrong!" to Color(0xFFEF5350)
                                results[activeIdx] = false
                                scope.launch {
                                    delay(800)
                                    feedback = null
                                    if (activeIdx < questions.size - 1) activeIdx++ else onScreenComplete(score)
                                }
                            }
                        }
                    }
                    startPos = null
                    currentDragPos = null
                },
                onDragCancel = {
                    startPos = null
                    currentDragPos = null
                }
            )
        }) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                matchedPairs.forEach { (l, r) ->
                    val s = itemPositions[l]; val e = targetPositions[r]
                    if (s != null && e != null) {
                        drawLine(Color.White.copy(alpha = 0.8f), s, e, strokeWidth = 10f, cap = StrokeCap.Round)
                    }
                }
                if (startPos != null && currentDragPos != null) {
                    drawLine(
                        color = Color.Yellow,
                        start = startPos!!,
                        end = currentDragPos!!,
                        strokeWidth = 8f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 15f), 0f),
                        cap = StrokeCap.Round
                    )
                }
            }

            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
                    questions.forEachIndexed { idx, q ->
                        PictureCard(
                            name = q.item,
                            isActive = idx == activeIdx,
                            statusColor = when (results[idx]) {
                                true -> Color(0xFF66BB6A)
                                false -> Color(0xFFEF5350)
                                else -> Color.White
                            }
                        ) { coords ->
                            val root = coords.positionInRoot()
                            itemPositions[idx] = Offset(root.x - containerPos.x + coords.size.width, root.y - containerPos.y + coords.size.height / 2)
                        }
                    }
                }
                Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
                    targets.forEachIndexed { idx, t ->
                        val isMatched = matchedPairs.any { it.second == idx }
                        val isHovered = hoveredIdx == idx
                        TargetButton(t, isMatched, isHovered) { coords ->
                            val root = coords.positionInRoot()
                            targetPositions[idx] = Offset(root.x - containerPos.x, root.y - containerPos.y + coords.size.height / 2)
                        }
                    }
                }
            }

            feedback?.let { (msg, color) -> if (msg == "Wrong!" || msg == "Time's up!") FeedbackDisplay(msg, color) }
        }
    }
}

@Composable
fun DragDropPhase(questions: List<GameQuestion>, onComplete: (Int) -> Unit, initialScreenIndex: Int = 0) {
    var screenIndex by remember { mutableIntStateOf(initialScreenIndex) }
    var totalScoreP2 by remember { mutableIntStateOf(0) }

    val currentBatch = remember(screenIndex, questions) {
        val start = screenIndex * 3
        questions.subList(start, (start + 3).coerceAtMost(questions.size))
    }

    key(currentBatch) {
        DragDropView(
            questions = currentBatch,
            screenTitle = "Phase 2: Drag and Drop (${screenIndex + 1}/2)",
            onScreenComplete = { score ->
                totalScoreP2 += score
                if ((screenIndex + 1) * 3 >= questions.size) {
                    onComplete(totalScoreP2)
                } else {
                    screenIndex++
                }
            }
        )
    }
}

@Composable
fun DragDropView(
    questions: List<GameQuestion>, 
    screenTitle: String, 
    onScreenComplete: (Int) -> Unit,
    isTimerEnabled: Boolean = !LocalInspectionMode.current
) {
    var activeIdx by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(10) }
    var score by remember { mutableIntStateOf(0) }
    var feedback by remember { mutableStateOf<Pair<String, Color>?>(null) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val targets = remember(questions) { questions.map { it.target }.shuffled() }
    val targetCenters = remember { mutableStateMapOf<Int, Offset>() }
    val results = remember { mutableStateMapOf<Int, Boolean>() }
    val matchedTargets = remember { mutableStateListOf<Int>() }
    var containerPos by remember { mutableStateOf(Offset.Zero) }

    var currentDragCenter by remember { mutableStateOf<Offset?>(null) }

    val hoveredTargetIdx = remember(currentDragCenter) {
        if (currentDragCenter == null) null
        else {
            var closestIdx: Int? = null
            var closestDist = Float.MAX_VALUE
            val threshold = with(density) { 100.dp.toPx() }
            targetCenters.forEach { (idx, center) ->
                val dist = (currentDragCenter!! - center).getDistance()
                if (dist < threshold && dist < closestDist) {
                    closestDist = dist
                    closestIdx = idx
                }
            }
            closestIdx
        }
    }

    LaunchedEffect(activeIdx, questions) {
        if (!isTimerEnabled) return@LaunchedEffect
        timeLeft = 10
        while (timeLeft > 0) {
            delay(1000)
            if (feedback == null) timeLeft--
        }
        if (feedback == null) {
            feedback = "Time's up!" to Color.Gray
            results[activeIdx] = false
            delay(1000)
            feedback = null
            if (activeIdx < questions.size - 1) activeIdx++ else onScreenComplete(score)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).onGloballyPositioned { containerPos = it.positionInRoot() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(screenTitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text("Timer: ${timeLeft}s", color = if (timeLeft < 3) Color.Red else Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
            Text("Score: $score", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Row(modifier = Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
                questions.forEachIndexed { idx, q ->
                    Box(Modifier.size(180.dp, 80.dp), contentAlignment = Alignment.Center) {
                        when {
                            idx == activeIdx -> {
                                DraggableCard(q.item, containerPos, feedback != null, onDragMove = { currentDragCenter = it }) { dropCenter ->
                                    currentDragCenter = null
                                    var closestIdx: Int? = null
                                    var closestDist = Float.MAX_VALUE
                                    val threshold = with(density) { 100.dp.toPx() }

                                    targetCenters.forEach { (tIdx, center) ->
                                        val dist = (dropCenter - center).getDistance()
                                        if (dist < threshold && dist < closestDist) {
                                            closestDist = dist
                                            closestIdx = tIdx
                                        }
                                    }

                                    closestIdx?.let { tIdx ->
                                        if (targets[tIdx] == q.target) {
                                            score += 2
                                            results[idx] = true
                                            matchedTargets.add(tIdx)
                                            if (activeIdx < questions.size - 1) activeIdx++ else onScreenComplete(score)
                                        } else {
                                            feedback = "Wrong!" to Color(0xFFEF5350)
                                            results[idx] = false
                                            scope.launch {
                                                delay(800)
                                                feedback = null
                                                if (activeIdx < questions.size - 1) activeIdx++ else onScreenComplete(score)
                                            }
                                        }
                                        true
                                    } ?: false
                                }
                            }
                            idx < activeIdx -> {
                                val isCorrect = results[idx] == true
                                Card(
                                    modifier = Modifier.fillMaxSize(),
                                    colors = CardDefaults.cardColors(containerColor = if (isCorrect) Color(0xFF66BB6A) else Color(0xFFEF5350)),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Text(q.item, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                                    }
                                }
                            }
                            else -> {
                                Card(
                                    modifier = Modifier.fillMaxSize(),
                                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Text(q.item, color = Color.White.copy(alpha = 0.5f), fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
                targets.forEachIndexed { idx, t ->
                    val isNear = hoveredTargetIdx == idx
                    TargetButton(
                        text = t,
                        isMatched = matchedTargets.contains(idx),
                        isHovered = isNear
                    ) { coords ->
                        val root = coords.positionInRoot()
                        targetCenters[idx] = Offset(root.x - containerPos.x + coords.size.width / 2f, root.y - containerPos.y + coords.size.height / 2f)
                    }
                }
            }
        }
    }
    feedback?.let { (msg, color) -> if (msg != "Correct!") FeedbackDisplay(msg, color) }
}

@Composable
fun DraggableCard(
    name: String,
    containerPos: Offset,
    isFeedbackShowing: Boolean,
    onDragMove: (Offset) -> Unit,
    onDrop: (Offset) -> Boolean
) {
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    var cardSize by remember { mutableStateOf(androidx.compose.ui.unit.IntSize.Zero) }
    var startRootPos by remember { mutableStateOf(Offset.Zero) }
    val coroutineScope = rememberCoroutineScope()

    val scale by animateFloatAsState(if (offsetX.value != 0f || offsetY.value != 0f) 1.15f else 1f, label = "scale")

    Card(
        modifier = Modifier.size(180.dp, 80.dp)
            .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
            .onGloballyPositioned {
                if (startRootPos == Offset.Zero) startRootPos = it.positionInRoot()
                cardSize = it.size
            }
            .scale(scale)
            .pointerInput(name) {
                if (isFeedbackShowing) return@pointerInput
                detectDragGestures(
                    onDragEnd = {
                        val centerInRoot = startRootPos + Offset(offsetX.value + cardSize.width / 2f, offsetY.value + cardSize.height / 2f)
                        val relCenter = Offset(centerInRoot.x - containerPos.x, centerInRoot.y - containerPos.y)
                        if (!onDrop(relCenter)) {
                            coroutineScope.launch { offsetX.animateTo(0f, spring(stiffness = Spring.StiffnessLow)) }
                            coroutineScope.launch { offsetY.animateTo(0f, spring(stiffness = Spring.StiffnessLow)) }
                        }
                    },
                    onDrag = { change, drag ->
                        change.consume()
                        coroutineScope.launch { offsetX.snapTo(offsetX.value + drag.x) }
                        coroutineScope.launch { offsetY.snapTo(offsetY.value + drag.y) }
                        val centerInRoot = startRootPos + Offset(offsetX.value + cardSize.width / 2f, offsetY.value + cardSize.height / 2f)
                        onDragMove(Offset(centerInRoot.x - containerPos.x, centerInRoot.y - containerPos.y))
                    },
                    onDragCancel = {
                        coroutineScope.launch { offsetX.animateTo(0f, spring(stiffness = Spring.StiffnessLow)) }
                        coroutineScope.launch { offsetY.animateTo(0f, spring(stiffness = Spring.StiffnessLow)) }
                    }
                )
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(if (offsetX.value != 0f) 20.dp else 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(name, color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun PictureCard(name: String, isActive: Boolean, statusColor: Color, onPos: (LayoutCoordinates) -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "float"
    )

    val scale by animateFloatAsState(if (isActive) 1.08f else 1.0f, label = "scale")

    Card(
        modifier = Modifier.width(200.dp).height(80.dp)
            .onGloballyPositioned(onPos)
            .offset(y = if (isActive || statusColor == Color(0xFF66BB6A)) floatAnim.dp else 0.dp)
            .scale(scale)
            .then(if (isActive && statusColor == Color.White) Modifier.border(4.dp, Color.Yellow, RoundedCornerShape(16.dp)) else Modifier),
        colors = CardDefaults.cardColors(containerColor = statusColor),
        elevation = CardDefaults.cardElevation(if (isActive) 12.dp else 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(name, color = if (statusColor == Color.White) Color.Black else Color.White, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center, fontSize = 20.sp)
        }
    }
}

@Composable
fun TargetButton(text: String, isMatched: Boolean, isHovered: Boolean, onPos: (LayoutCoordinates) -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "wobblePhysics")
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "idleFloat"
    )
    val shakeWiggle by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(90, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "activeWobble"
    )

    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.15f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    Surface(
        modifier = Modifier.width(200.dp).height(65.dp)
            .onGloballyPositioned(onPos)
            .scale(scale)
            .graphicsLayer {
                translationY = if (isHovered) shakeWiggle * 1.5f else floatAnim
                rotationZ = if (isHovered) shakeWiggle * 1.2f else 0f
            },
        shape = RoundedCornerShape(32.dp),
        color = if (isMatched) Color(0xFF66BB6A) else if (isHovered) Color.White.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.3f),
        border = BorderStroke(if (isHovered) 4.dp else 2.dp, if (isHovered) Color.Yellow else Color.White)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun FeedbackDisplay(msg: String, color: Color) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)), contentAlignment = Alignment.Center) {
        Card(colors = CardDefaults.cardColors(containerColor = color), shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(12.dp)) {
            Row(modifier = Modifier.padding(32.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(Modifier.width(20.dp))
                Text(msg, color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun WellDoneText(
    entryScale: Float = 1f,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wellDoneEffects")

    val shineOffset by infiniteTransition.animateFloat(
        initialValue = -1000f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "shine"
    )

    val morphScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "morphScale"
    )

    val shineBrush = Brush.linearGradient(
        colors = listOf(Color(0xFFFFE082), Color.White, Color(0xFFFFE082)),
        start = Offset(shineOffset, 0f),
        end = Offset(shineOffset + 300f, 300f)
    )

    Text(
        text = "WELL DONE!",
        fontSize = 80.sp,
        fontWeight = FontWeight.Black,
        fontFamily = Kavoon,
        textAlign = TextAlign.Center,
        style = TextStyle(
            brush = shineBrush,
            shadow = Shadow(color = Color(0xFFB8860B).copy(alpha = 0.5f), offset = Offset(2f, 2f), blurRadius = 4f)
        ),
        modifier = modifier
            .scale(entryScale * morphScale)
            .graphicsLayer {
                rotationZ = (morphScale - 1f) * 30f
            }
    )
}

@Preview(showBackground = true, widthDp = 600, heightDp = 300, backgroundColor = 0xFF4A90E2)
@Composable
fun WellDoneTextPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            WellDoneText()
        }
    }
}

@Composable
fun ScoreResult(
    score: Int,
    maxPoints: Int,
    alpha: Float = 1f,
    translationY: Float = 0f,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Final Score: $score / $maxPoints",
        fontFamily = Kavoon,
        fontSize = 36.sp,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        modifier = modifier.graphicsLayer {
            this.alpha = alpha
            this.translationY = translationY
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF4A90E2)
@Composable
fun ScoreResultPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            ScoreResult(score = 22, maxPoints = 25)
        }
    }
}

@Composable
fun GameOverScreen(score: Int, onRestart: () -> Unit, onBack: () -> Unit) {
    val maxPoints = 25
    val isInspectionMode = LocalInspectionMode.current
    var startAnimations by remember { mutableStateOf(isInspectionMode) }
    val animatedScore = remember { Animatable(if (isInspectionMode) score.toFloat() else 0f) }

    LaunchedEffect(Unit) {
        if (isInspectionMode) return@LaunchedEffect
        
        startAnimations = true
        delay(600)
        animatedScore.animateTo(
            targetValue = score.toFloat(),
            animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
        )
    }
    
    val wellDoneScale by animateFloatAsState(
        targetValue = if (startAnimations) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "entryScale"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WellDoneText(entryScale = wellDoneScale)
        
        Spacer(Modifier.height(16.dp))
        
        ScoreResult(
            score = animatedScore.value.roundToInt(),
            maxPoints = maxPoints,
            alpha = if (startAnimations) 1f else 0f,
            translationY = (1f - wellDoneScale) * 50f
        )
        
        Spacer(Modifier.height(40.dp))
        
        AnimatedVisibility(
            visible = (startAnimations && animatedScore.value >= score.toFloat() * 0.9f) || isInspectionMode,
            enter = fadeIn(tween(1000)) + slideInVertically(initialOffsetY = { it / 2 })
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = onRestart, 
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White), 
                    modifier = Modifier.height(60.dp).width(220.dp), 
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text("Play Again", color = Color(0xFFA173FA), fontFamily = Kavoon, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(20.dp))
                OutlinedButton(
                    onClick = onBack, 
                    border = BorderStroke(2.dp, Color.White), 
                    modifier = Modifier.height(60.dp).width(220.dp), 
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text("Back to Menu", color = Color.White, fontFamily = Kavoon, fontSize = 20.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400, name = "GameOver Screen Only")
@Composable
fun GameOverScreenPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        GameBackground {
            GameOverScreen(score = 22, onRestart = {}, onBack = {})
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400, name = "Game Intro")
@Composable
fun ConnectPairsIntroPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ConnectThePairsGameScreen(onBackClick = {}, initialState = "intro")
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400, name = "Phase 1: Screen 1")
@Composable
fun ConnectPairsPhase1Screen1Preview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ConnectThePairsGameScreen(onBackClick = {}, initialState = "line_matching", initialScreenIndex = 0)
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400, name = "Phase 1: Screen 2")
@Composable
fun ConnectPairsPhase1Screen2Preview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ConnectThePairsGameScreen(onBackClick = {}, initialState = "line_matching", initialScreenIndex = 1)
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400, name = "Phase 2: Screen 1")
@Composable
fun ConnectPairsPhase2Screen1Preview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ConnectThePairsGameScreen(onBackClick = {}, initialState = "drag_drop", initialScreenIndex = 0)
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400, name = "Phase 2: Screen 2")
@Composable
fun ConnectPairsPhase2Screen2Preview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ConnectThePairsGameScreen(onBackClick = {}, initialState = "drag_drop", initialScreenIndex = 1)
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400, name = "Game Result (Over)")
@Composable
fun ConnectPairsGameOverPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ConnectThePairsGameScreen(onBackClick = {}, initialState = "game_over", initialScore = 25)
    }
}
