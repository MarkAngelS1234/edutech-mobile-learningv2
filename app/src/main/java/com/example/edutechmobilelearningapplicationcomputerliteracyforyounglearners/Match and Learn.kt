// ============================================================================
// Match and Learn.kt
// A two-phase matching mini-game ("Line Matching" then "Drag & Drop") that
// teaches young learners the names and uses of common computer hardware and
// software. The whole screen is built with Jetpack Compose, runs in forced
// landscape orientation, and scales its layout with a single scaleFactor so
// it looks consistent across different tablet/phone sizes.
// ============================================================================

package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

// --- Android framework imports (Activity lookup, orientation, video playback) ---
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.net.Uri
import android.widget.VideoView

// --- Jetpack Compose animation imports (transitions, springs, tweens) ---
import androidx.compose.animation.*
import androidx.compose.animation.core.*

// --- Jetpack Compose foundation imports (layout building blocks, drawing, gestures) ---
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape

// --- Jetpack Compose Material 3 imports (buttons, cards, surfaces, icons) ---
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*

// --- Jetpack Compose runtime imports (state, remember, composables lifecycle) ---
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

// --- Jetpack Compose UI imports (modifiers, geometry, graphics, text styling) ---
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

// --- Lifecycle/ViewModel import (to read/write the score back to the course) ---
import androidx.lifecycle.viewmodel.compose.viewModel

// --- App-specific theme and font imports ---
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon

// --- Kotlin coroutines imports (timers, launching side-effect work) ---
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * A single matchable question in the game.
 *
 * @param id     Unique identifier for the question (used for stable diffing/shuffling).
 * @param item   The label shown on the "source" card (e.g. "Keyboard", "Word").
 * @param target The correct matching label shown on the "target" side (e.g. "TYPE", "WRITE").
 * @param phase  Which game phase this question belongs to: 1 = Line Matching, 2 = Drag & Drop.
 */
data class GameQuestion(
    val id: Int,
    val item: String,
    val target: String,
    val phase: Int
)

/**
 * Shared decorative background for every screen in the game: a blue-to-purple
 * gradient with soft translucent "cloud" and "blob" circles drawn in the
 * corners. [content] is drawn on top of this background, centered.
 */
@Composable
fun GameBackground(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF4A90E2), Color(0xFFA173FA)))),
        contentAlignment = Alignment.Center
    ) {
        // Canvas used purely for decorative shapes; it does not intercept touch input.
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Top decorative clouds
            val cloudWhite = Color.White.copy(alpha = 0.25f)
            // Left top cloud
            drawCircle(cloudWhite, radius = 35.dp.toPx(), center = Offset(0f, 40.dp.toPx()))

            // Right top cloud cluster (three overlapping circles for a puffy look)
            drawCircle(cloudWhite, radius = 35.dp.toPx(), center = Offset(size.width, 40.dp.toPx()))
            drawCircle(cloudWhite, radius = 50.dp.toPx(), center = Offset(size.width - 40.dp.toPx(), 20.dp.toPx()))
            drawCircle(cloudWhite, radius = 30.dp.toPx(), center = Offset(size.width - 90.dp.toPx(), 45.dp.toPx()))

            // Bottom decorative purple shapes
            val softPurple = Color(0xFFC084FC).copy(alpha = 0.35f)
            // Left bottom blob cluster
            drawCircle(softPurple, radius = 130.dp.toPx(), center = Offset(30.dp.toPx(), size.height))
            drawCircle(softPurple, radius = 100.dp.toPx(), center = Offset(150.dp.toPx(), size.height + 20.dp.toPx()))

            // Right bottom blob cluster (mirrors the left side)
            drawCircle(softPurple, radius = 130.dp.toPx(), center = Offset(size.width - 30.dp.toPx(), size.height))
            drawCircle(softPurple, radius = 100.dp.toPx(), center = Offset(size.width - 150.dp.toPx(), size.height + 20.dp.toPx()))
        }
        // Draw the caller-provided screen content on top of the background.
        content()
    }
}

/**
 * Root composable for the "Match and Learn" mini-game.
 *
 * Manages the overall game state machine (`intro` -> `line_matching` -> `drag_drop`
 * -> `game_over`), forces the screen into landscape orientation for the duration
 * of the game, and computes a single [scaleFactor] used to proportionally scale
 * every child composable's sizing so the layout adapts to different screen widths.
 *
 * @param onBackClick        Called when the player backs out of the game entirely.
 * @param viewModel          Optional course view model used to persist the final score.
 * @param initialState       Which game state to start in (mainly used by @Preview functions).
 * @param initialScore       Starting score (mainly used by @Preview functions).
 * @param initialScreenIndex Which sub-screen of a phase to start on (mainly used by @Preview functions).
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ConnectThePairsGameScreen(
    onBackClick: () -> Unit,
    viewModel: CourseViewModel? = null,
    initialState: String = "intro",
    initialScore: Int = 0,
    initialScreenIndex: Int = 0
) {
    val context = LocalContext.current
    // gameState drives which phase/screen is currently shown; survives process death via rememberSaveable.
    var gameState by rememberSaveable { mutableStateOf(initialState) }
    // totalScore accumulates points across both phases.
    var totalScore by rememberSaveable { mutableIntStateOf(initialScore) }

    // Android Studio's @Preview renderer can't provide a real ViewModel, so we skip it in inspection mode.
    val isInspectionMode = LocalInspectionMode.current
    val realViewModel: CourseViewModel? = if (isInspectionMode) null else {
        viewModel ?: viewModel()
    }

    // The full pool of Phase 1 (hardware) questions; shuffled once per composition via `remember`.
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

    // The full pool of Phase 2 (software/apps) questions.
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

    // Randomized (shuffled) working copies of each pool; re-shuffled every time "Start Game" is pressed.
    var randomizedP1 by remember { mutableStateOf(phase1FullPool.shuffled()) }
    var randomizedP2 by remember { mutableStateOf(phase2FullPool.shuffled()) }

    // Helper: walk up the Context wrapper chain to find the hosting Activity,
    // needed so we can temporarily force screen orientation.
    fun Context.findActivity(): Activity? {
        var currentContext = this
        while (currentContext is ContextWrapper) {
            if (currentContext is Activity) return currentContext
            currentContext = currentContext.baseContext
        }
        return null
    }

    // Force landscape orientation for the lifetime of this composable, and restore
    // whatever the original orientation was when it leaves composition.
    if (!isInspectionMode) {
        DisposableEffect(Unit) {
            val activity = context.findActivity()
            val originalOrientation = activity?.requestedOrientation ?: ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            onDispose { activity?.requestedOrientation = originalOrientation }
        }
    }

    // BoxWithConstraints lets us read the available width/height to compute scaleFactor.
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        // Optimized proportional scaling factor (using 800dp as base for standard tablet proportions).
        // Clamped between 1x (small screens) and 1.6x (large tablets) so nothing ever shrinks below the design baseline.
        val scaleFactor = (screenWidth / 800.dp).coerceIn(1f, 1.6f)

        GameBackground {
            // Cross-fades and scales between the four game states.
            AnimatedContent(
                targetState = gameState,
                modifier = Modifier.fillMaxSize(),
                transitionSpec = {
                    (fadeIn(animationSpec = tween(800)) + scaleIn(initialScale = 0.8f, animationSpec = tween(800)))
                        .togetherWith(fadeOut(animationSpec = tween(500)))
                },
                label = "StateTransition"
            ) { state ->
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    when (state) {
                        // --- Intro screen: title, "How to Play" panel, Back/Start buttons ---
                        "intro" -> GameIntro(
                            onStart = {
                                // Re-shuffle both pools and reset score every time a new game starts.
                                randomizedP1 = phase1FullPool.shuffled()
                                randomizedP2 = phase2FullPool.shuffled()
                                totalScore = 0
                                gameState = "line_matching"
                            },
                            onBack = onBackClick,
                            scaleFactor = scaleFactor
                        )
                        // --- Phase 1: draw a line from each picture to its matching purpose ---
                        "line_matching" -> LineMatchingPhase(
                            questions = randomizedP1,
                            onComplete = { score ->
                                totalScore += score
                                gameState = "drag_drop"
                            },
                            initialScreenIndex = initialScreenIndex,
                            scaleFactor = scaleFactor
                        )
                        // --- Phase 2: drag each app icon onto its matching usage box ---
                        "drag_drop" -> DragDropPhase(
                            questions = randomizedP2,
                            onComplete = { score ->
                                // Phase 2 completion grants a flat +1 bonus point on top of the raw score.
                                val phase2ScoreWithBonus = score + 1
                                totalScore += phase2ScoreWithBonus
                                // Persist the final score back to the hosting course/view model.
                                realViewModel?.updateGameScore("Match and Learn", totalScore)
                                gameState = "game_over"
                            },
                            initialScreenIndex = initialScreenIndex,
                            scaleFactor = scaleFactor
                        )
                        // --- Results screen: animated score reveal + Play Again / Back to Menu ---
                        "game_over" -> GameOverScreen(
                            score = totalScore,
                            onRestart = {
                                totalScore = 0
                                gameState = "intro"
                            },
                            onBack = onBackClick,
                            scaleFactor = scaleFactor
                        )
                    }
                }
            }
        }
    }
}

/**
 * The introductory screen: game title, the expandable "How to Play?" instructions
 * panel, and the Back / Start Game action buttons.
 */
@Composable
fun GameIntro(onStart: () -> Unit, onBack: () -> Unit, scaleFactor: Float = 1f) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .widthIn(max = (900 * scaleFactor).dp)
            .fillMaxWidth()
            .padding((32 * scaleFactor).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Game title, with a soft drop shadow for readability against the gradient background.
        Text(
            text = "Match and Learn!",
            fontFamily = Kavoon,
            fontSize = (74 * scaleFactor).sp, // Increased base size + scale
            color = Color.White,
            textAlign = TextAlign.Center,
            style = TextStyle(
                shadow = Shadow(color = Color.Black.copy(alpha = 0.3f), offset = Offset(4f * scaleFactor, 4f * scaleFactor), blurRadius = 8f * scaleFactor)
            )
        )

        Spacer(modifier = Modifier.height((32 * scaleFactor).dp))

        // Expandable panel with instructions + instruction video.
        InstructionExpandableButton(scaleFactor)

        Spacer(modifier = Modifier.height((48 * scaleFactor).dp))

        // Bottom action row: Back (return to previous screen) and Start Game (begin Phase 1).
        Row {
            OutlinedButton(
                onClick = onBack,
                border = BorderStroke((3 * scaleFactor).dp, Color.White),
                modifier = Modifier.height((60 * scaleFactor).dp).width((160 * scaleFactor).dp),
                shape = RoundedCornerShape((30 * scaleFactor).dp)
            ) {
                Text("Back", color = Color.White, fontFamily = Kavoon, fontSize = (22 * scaleFactor).sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width((32 * scaleFactor).dp))
            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier.height((60 * scaleFactor).dp).width((200 * scaleFactor).dp),
                shape = RoundedCornerShape((30 * scaleFactor).dp),
                elevation = ButtonDefaults.buttonElevation((8 * scaleFactor).dp)
            ) {
                Text("Start Game", color = Color(0xFF9575CD), fontWeight = FontWeight.Black, fontFamily = Kavoon, fontSize = (24 * scaleFactor).sp)
            }
        }
    }
}

/**
 * A tappable "How to Play?" card that expands in place to reveal the three
 * instruction rows, a divider, and the looping instruction video.
 *
 * The expanded content is height-capped and internally scrollable (so it can
 * never push the Back/Start Game buttons off-screen or get clipped by the
 * screen edge), and shows a small cartoon-style scrollbar plus a bouncing
 * "Scroll Down ↓" cue while the user is still near the top of the content.
 */
@Composable
fun InstructionExpandableButton(scaleFactor: Float = 1f) {
    // Whether the panel is currently expanded to show instructions + video.
    var expanded by remember { mutableStateOf(false) }

    // Cap the expanded area so it scrolls inside itself instead of pushing or clipping the screen.
    val configuration = LocalConfiguration.current
    val maxExpandedHeight = (configuration.screenHeightDp * 0.55f).dp
    // Tracks the scroll position of the expanded content (instructions + divider + video).
    val expandedScrollState = rememberScrollState()

    Surface(
        onClick = { expanded = !expanded },
        shape = RoundedCornerShape((24 * scaleFactor).dp),
        color = Color.White.copy(alpha = 0.95f),
        border = BorderStroke((4 * scaleFactor).dp, Color(0xFF9575CD)), // Golden cartoon border
        modifier = Modifier
            .fillMaxWidth(0.85f)
            // Smoothly animates the Surface's own height as it expands/collapses.
            .animateContentSize(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)),
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding((20 * scaleFactor).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // The always-visible header/toggle row ("How to Play? 💡" <-> "Got it! 👍").
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (expanded) "Got it! 👍" else "How to Play? 💡",
                    fontFamily = Kavoon,
                    fontSize = (24 * scaleFactor).sp,
                    color = Color(0xFF9575CD)
                )
            }

            if (expanded) {
                // Scrolling is isolated to this row: the header above, the Back / Start Game
                // buttons below and every other screen stay exactly where they are.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = maxExpandedHeight),
                    verticalAlignment = Alignment.Top
                ) {
                    // Box lets us overlay the "Scroll Down" cue on top of the scrollable column below.
                    Box(modifier = Modifier.weight(1f)) {
                        // The actual scrollable content: instruction rows -> divider -> video.
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(expandedScrollState),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height((16 * scaleFactor).dp))
                            // Three instruction rows explaining the two phases and scoring.
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy((12 * scaleFactor).dp)
                            ) {
                                InstructionRow("✏️", "1: Match Lines", "Draw lines from the object to its purpose!", scaleFactor)
                                InstructionRow("🎯", "2: Drag & Drop", "Drag the app icon to the correct usage box!", scaleFactor)
                                InstructionRow("✨", "Score Big!", "2 points for every correct match + 1 Bonus points!", scaleFactor)
                            }

                            // Divider directly below the instruction text
                            Spacer(modifier = Modifier.height((16 * scaleFactor).dp))
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = (2 * scaleFactor).dp,
                                color = Color(0xFF9575CD).copy(alpha = 0.3f)
                            )

                            // Instruction video directly below the divider, centered
                            Spacer(modifier = Modifier.height((16 * scaleFactor).dp))
                            InstructionVideoPlayer(scaleFactor = scaleFactor)
                            // Extra bottom breathing room so the bouncing cue never sits on top of the video
                            Spacer(modifier = Modifier.height((44 * scaleFactor).dp))
                        }

                        // Bouncing "Scroll Down" cue: only shown while the content is still
                        // at (or very near) the top. As soon as the user scrolls toward the
                        // video it fades out, and it fades back in if they scroll back up.
                        val nearTopThresholdPx = with(LocalDensity.current) { (24 * scaleFactor).dp.toPx() }
                        val isNearTop = expandedScrollState.maxValue > 0 &&
                                expandedScrollState.value <= nearTopThresholdPx
                        // Fully-qualified to avoid ambiguity with the RowScope.AnimatedVisibility
                        // overload that is also in scope because this Box sits inside a Row.
                        androidx.compose.animation.AnimatedVisibility(
                            visible = isNearTop,
                            enter = fadeIn(animationSpec = tween(300)),
                            exit = fadeOut(animationSpec = tween(300)),
                            modifier = Modifier.align(Alignment.BottomCenter)
                        ) {
                            ScrollDownCue(scaleFactor = scaleFactor)
                        }
                    }

                    Spacer(modifier = Modifier.width((10 * scaleFactor).dp))

                    // Thin cartoon-styled scrollbar showing scroll position within the panel.
                    CartoonScrollBar(
                        scrollState = expandedScrollState,
                        scaleFactor = scaleFactor,
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            }
        }
    }
}

/**
 * A small pill-shaped badge reading "Scroll Down ↓" that gently bounces up
 * and down forever, used to hint that more content is available below.
 * Visibility (fade in/out) is controlled entirely by the caller; this
 * composable only owns the bounce animation and its own visual styling.
 */
@Composable
fun ScrollDownCue(scaleFactor: Float = 1f) {
    // Infinite transition that drives a gentle vertical bounce.
    val infiniteTransition = rememberInfiniteTransition(label = "scrollCueBounce")
    val bounceOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f * scaleFactor,
        animationSpec = infiniteRepeatable(
            // 600ms up, 600ms back down = a gentle, unobtrusive bob rather than a fast jitter.
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounceOffset"
    )

    // Rounded purple pill background, matching the app's cartoon color palette.
    Surface(
        modifier = Modifier
            .padding(bottom = (6 * scaleFactor).dp)
            .graphicsLayer { translationY = bounceOffset },
        shape = RoundedCornerShape((50 * scaleFactor).dp),
        color = Color(0xFF9575CD),
        shadowElevation = 4.dp * scaleFactor
    ) {
        Text(
            text = "Scroll Down ↓",
            fontFamily = Kavoon,
            fontWeight = FontWeight.Bold,
            fontSize = (13 * scaleFactor).sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = (14 * scaleFactor).dp, vertical = (6 * scaleFactor).dp)
        )
    }
}

/**
 * A thin, hand-drawn-looking vertical scrollbar for the expanded instructions
 * panel. Draws nothing when there is no overflow to scroll, so the layout
 * never visibly shifts when the panel first expands.
 *
 * @param scrollState The [ScrollState] being visualized (shared with the scrollable Column).
 */
@Composable
fun CartoonScrollBar(
    scrollState: ScrollState,
    scaleFactor: Float = 1f,
    modifier: Modifier = Modifier
) {
    val maxScroll = scrollState.maxValue
    // Nothing to scroll: keep the slot but draw nothing, so the layout never shifts.
    val hasOverflow = maxScroll > 0 && maxScroll != Int.MAX_VALUE

    Canvas(
        modifier = modifier
            .width((12 * scaleFactor).dp)
            .padding(vertical = (16 * scaleFactor).dp)
    ) {
        // Bail out early (draw nothing) if there's nothing to scroll or we have no size yet.
        if (!hasOverflow || size.height <= 0f) return@Canvas

        val trackWidth = size.width
        // Fully-rounded ends on the track/thumb, giving the scrollbar a capsule shape.
        val radius = CornerRadius(trackWidth / 2f, trackWidth / 2f)

        // Track: soft purple capsule spanning the full height of the canvas.
        drawRoundRect(
            color = Color(0xFF9575CD).copy(alpha = 0.15f),
            size = size,
            cornerRadius = radius
        )

        // Thumb size is proportional to how much of the content is visible at once.
        val visibleFraction = size.height / (size.height + maxScroll)
        val thumbHeight = (size.height * visibleFraction).coerceAtLeast(trackWidth * 2.5f)
        // Thumb position is proportional to current scroll progress (0f = top, 1f = bottom).
        val progress = (scrollState.value.toFloat() / maxScroll).coerceIn(0f, 1f)
        val thumbTop = (size.height - thumbHeight) * progress

        // Cartoon thumb: white outline...
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(0f, thumbTop),
            size = Size(trackWidth, thumbHeight),
            cornerRadius = radius
        )
        // ...plus a solid purple core inset inside the white outline.
        drawRoundRect(
            color = Color(0xFF9575CD),
            topLeft = Offset(trackWidth * 0.2f, thumbTop + trackWidth * 0.2f),
            size = Size(trackWidth * 0.6f, (thumbHeight - trackWidth * 0.4f).coerceAtLeast(trackWidth * 0.6f)),
            cornerRadius = radius
        )
    }
}

/**
 * Plays the looping, autoplaying, controls-free `instruction.mp4` video inside
 * a rounded, bordered "frame" so it reads as a deliberate, child-friendly UI
 * element rather than a raw video surface.
 *
 * The frame:
 *  - Clips the video to soft rounded corners (both on the outer container and
 *    directly on the underlying [VideoView], since some devices render the
 *    video surface in a way that can otherwise ignore Compose's own clipping).
 *  - Keeps a fixed 16:9 aspect ratio, so the corner radius and framing always
 *    look correct no matter how wide the surrounding column is.
 *  - Is centered horizontally by the caller (the parent Column uses
 *    `Alignment.CenterHorizontally`), and centers its own content vertically
 *    via `contentAlignment = Alignment.Center`.
 *
 * In Android Studio's @Preview (inspection mode), [VideoView] cannot render,
 * so a lightweight placeholder box with the same rounded frame is shown instead.
 */
@Composable
fun InstructionVideoPlayer(scaleFactor: Float = 1f) {
    val isInspectionMode = LocalInspectionMode.current

    // A soft, friendly corner radius shared by both the placeholder and the real player,
    // so the two look identical in shape when switching between preview and runtime.
    val videoCornerRadius = RoundedCornerShape((16 * scaleFactor).dp)

    // VideoView cannot render inside Android Studio @Preview, so show a lightweight
    // placeholder in inspection mode while keeping the real player for runtime.
    if (isInspectionMode) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f) // Preserve the video's native 16:9 aspect ratio.
                .clip(videoCornerRadius)
                .background(Color(0xFF9575CD).copy(alpha = 0.1f))
                .border((3 * scaleFactor).dp, Color(0xFF9575CD).copy(alpha = 0.4f), videoCornerRadius),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Instruction Video",
                fontFamily = Kavoon,
                fontSize = (14 * scaleFactor).sp,
                color = Color(0xFF9575CD)
            )
        }
        return
    }

    // Outer framing Box: this is what actually gives the video its rounded, bordered
    // "card" look. Clipping here trims anything the inner AndroidView might draw
    // outside the rounded rectangle.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f) // Preserve the video's native 16:9 aspect ratio; never distorts the picture.
            .clip(videoCornerRadius),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            // Clipping is applied a second time directly on the AndroidView's own modifier
            // as a safety net: on some OEM builds a VideoView's internal SurfaceView can
            // otherwise bypass clipping applied only to an ancestor Box.
            modifier = Modifier
                .fillMaxSize()
                .clip(videoCornerRadius),
            factory = { ctx ->
                VideoView(ctx).apply {
                    val uri = Uri.parse("android.resource://${ctx.packageName}/${R.raw.instruction}")
                    setVideoURI(uri)
                    // No MediaController: playback controls stay hidden.
                    setMediaController(null)
                    isClickable = false
                    isFocusable = false
                    setOnPreparedListener { mp ->
                        mp.isLooping = true // Loop the instruction video forever while the panel is open.
                        start() // Autoplay as soon as the video is ready.
                    }
                }
            },
            // Defensive: if playback ever stops unexpectedly (e.g. audio focus changes), resume it.
            update = { videoView ->
                if (!videoView.isPlaying) videoView.start()
            },
            // Stop playback and release resources when the video leaves composition
            // (e.g. the instructions panel is collapsed).
            onRelease = { videoView ->
                videoView.stopPlayback()
            }
        )
    }
}

/**
 * One row of the "How to Play?" instructions: an emoji icon in a rounded
 * square, followed by a bold title and a description line.
 */
@Composable
fun InstructionRow(emoji: String, title: String, desc: String, scaleFactor: Float = 1f) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = (8 * scaleFactor).dp)
    ) {
        // Emoji "icon" chip.
        Box(
            modifier = Modifier
                .size((40 * scaleFactor).dp)
                .background(Color(0xFFF0F4FF), RoundedCornerShape((10 * scaleFactor).dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, fontSize = (24 * scaleFactor).sp)
        }
        Spacer(Modifier.width((16 * scaleFactor).dp))
        // Title + description text stack.
        Column {
            Text(title, fontWeight = FontWeight.ExtraBold, color = Color(0xFF333333), fontSize = (16 * scaleFactor).sp)
            Text(desc, color = Color(0xFF666666), fontSize = (14 * scaleFactor).sp, lineHeight = (18 * scaleFactor).sp)
        }
    }
}

/**
 * Wraps [LineMatchingView] and manages progression across Phase 1's two
 * sub-screens (3 questions each), accumulating the phase's total score before
 * calling [onComplete].
 */
@Composable
fun LineMatchingPhase(questions: List<GameQuestion>, onComplete: (Int) -> Unit, initialScreenIndex: Int = 0, scaleFactor: Float = 1f) {
    // Which of the two 3-question sub-screens is currently active (0 or 1).
    var screenIndex by remember { mutableIntStateOf(initialScreenIndex) }
    // Running total across both sub-screens of this phase.
    var totalScoreP1 by remember { mutableIntStateOf(0) }

    // Slice out the 3 questions belonging to the current sub-screen.
    val currentBatch = remember(screenIndex, questions) {
        val start = screenIndex * 3
        questions.subList(start, (start + 3).coerceAtMost(questions.size))
    }

    // `key(currentBatch)` forces LineMatchingView to fully reset its internal state
    // (active index, timer, matched pairs, etc.) whenever the batch of questions changes.
    key(currentBatch) {
        LineMatchingView(
            questions = currentBatch,
            screenTitle = "Phase 1: Line Matching (${screenIndex + 1}/2)",
            onScreenComplete = { score ->
                totalScoreP1 += score
                if ((screenIndex + 1) * 3 >= questions.size) {
                    // All sub-screens finished: report the phase's total score.
                    onComplete(totalScoreP1)
                } else {
                    // Advance to the next 3-question sub-screen.
                    screenIndex++
                }
            },
            scaleFactor = scaleFactor
        )
    }
}

/**
 * Phase 1 gameplay screen: the player drags a finger from a picture card to
 * its matching purpose button to "draw a line" connecting them. Each question
 * has a 10-second timer; a wrong match or a timeout both advance to the next
 * question after a short feedback flash.
 */
@Composable
fun LineMatchingView(
    questions: List<GameQuestion>,
    screenTitle: String,
    onScreenComplete: (Int) -> Unit,
    isTimerEnabled: Boolean = !LocalInspectionMode.current,
    scaleFactor: Float = 1f
) {
    // Index of the question currently being answered.
    var activeIdx by remember { mutableIntStateOf(0) }
    // Countdown timer (seconds) for the active question.
    var timeLeft by remember { mutableIntStateOf(10) }
    // Running score for this sub-screen.
    var score by remember { mutableIntStateOf(0) }
    // Transient feedback message + color (e.g. "Wrong!" in red), shown briefly then cleared.
    var feedback by remember { mutableStateOf<Pair<String, Color>?>(null) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    // Targets are shuffled independently of the questions so their on-screen order
    // doesn't give away the correct pairing.
    val targets = remember(questions) { questions.map { it.target }.shuffled() }
    // Screen-space positions of each picture card's connection point, keyed by question index.
    val itemPositions = remember { mutableStateMapOf<Int, Offset>() }
    // Screen-space positions of each target button's connection point, keyed by target index.
    val targetPositions = remember { mutableStateMapOf<Int, Offset>() }
    // Root position of the drag/drop container, used to convert root-space to local-space offsets.
    var containerPos by remember { mutableStateOf(Offset.Zero) }

    // Current in-progress drag line, from the picture card to the current finger position.
    var startPos by remember { mutableStateOf<Offset?>(null) }
    var currentDragPos by remember { mutableStateOf<Offset?>(null) }

    // Per-question correctness (true/false) once answered, used to color the picture cards.
    val results = remember { mutableStateMapOf<Int, Boolean>() }
    // Confirmed (question index, target index) pairs, used to draw the permanent matched lines.
    val matchedPairs = remember { mutableStateListOf<Pair<Int, Int>>() }

    // While dragging, find the nearest target within snapping distance to highlight it.
    val hoveredIdx = remember(currentDragPos) {
        if (currentDragPos == null) null
        else {
            var closestIdx: Int? = null
            var closestDist = Float.MAX_VALUE
            val threshold = with(density) { 95.dp.toPx() * scaleFactor }
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

    // Per-question countdown timer. Restarts whenever `activeIdx` (or the question set) changes.
    LaunchedEffect(activeIdx, questions) {
        if (!isTimerEnabled) return@LaunchedEffect
        timeLeft = 10
        while (timeLeft > 0) {
            delay(1000)
            if (feedback == null) timeLeft--
        }
        if (feedback == null) {
            // Ran out of time: mark this question wrong and move on after a short pause.
            feedback = "Time's up!" to Color.Gray
            results[activeIdx] = false
            delay(1000)
            feedback = null
            if (activeIdx < questions.size - 1) activeIdx++ else onScreenComplete(score)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .widthIn(max = (1100 * scaleFactor).dp)
            .fillMaxWidth()
            .padding((16 * scaleFactor).dp)
            .onGloballyPositioned { containerPos = it.positionInRoot() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header row: screen title, countdown timer, running score.
        Row(
            modifier = Modifier
                .widthIn(max = (850 * scaleFactor).dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(screenTitle, color = Color.White, fontFamily = Kavoon, fontWeight = FontWeight.Bold, fontSize = (22 * scaleFactor).sp)
            Text("Timer: ${timeLeft}s", color = if (timeLeft < 3) Color.Red else Color.White, fontFamily = Kavoon, fontSize = (22 * scaleFactor).sp, fontWeight = FontWeight.Black)
            Text("Score: $score", color = Color.White, fontFamily = Kavoon, fontSize = (22 * scaleFactor).sp, fontWeight = FontWeight.Bold)
        }

        // Main play area: handles the drag gesture for connecting a picture card to a target.
        Box(modifier = Modifier.weight(1f).fillMaxWidth().pointerInput(activeIdx) {
            detectDragGestures(
                onDragStart = { offset ->
                    // Only start a drag if the finger is near the currently active picture card.
                    if (feedback != null) return@detectDragGestures
                    val p = itemPositions[activeIdx]
                    if (p != null) {
                        val distance = (offset - p).getDistance()
                        val threshold = with(density) { 100.dp.toPx() * scaleFactor }
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
                    // On release, find the nearest target within range and check if it's correct.
                    if (startPos != null && currentDragPos != null) {
                        var foundTargetIdx: Int? = null
                        var closestDist = Float.MAX_VALUE
                        val threshold = with(density) { 95.dp.toPx() * scaleFactor }
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
                                // Correct match: award points, record the permanent line, advance.
                                score += 2
                                results[activeIdx] = true
                                matchedPairs.add(activeIdx to targetIdx)
                                if (activeIdx < questions.size - 1) {
                                    activeIdx++
                                } else {
                                    onScreenComplete(score)
                                }
                            } else {
                                // Wrong match: flash feedback, mark wrong, advance after a short delay.
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
                    // Reset the in-progress drag line regardless of outcome.
                    startPos = null
                    currentDragPos = null
                },
                onDragCancel = {
                    startPos = null
                    currentDragPos = null
                }
            )
        }, contentAlignment = Alignment.Center) {
            // Draws the permanent matched-pair lines and the temporary in-progress drag line.
            Canvas(modifier = Modifier.fillMaxSize()) {
                matchedPairs.forEach { (l, r) ->
                    val s = itemPositions[l]; val e = targetPositions[r]
                    if (s != null && e != null) {
                        drawLine(Color.White.copy(alpha = 0.8f), s, e, strokeWidth = 10f * scaleFactor, cap = StrokeCap.Round)
                    }
                }
                if (startPos != null && currentDragPos != null) {
                    // Dashed yellow line follows the finger while dragging.
                    drawLine(
                        color = Color.Yellow,
                        start = startPos!!,
                        end = currentDragPos!!,
                        strokeWidth = 8f * scaleFactor,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f * scaleFactor, 15f * scaleFactor), 0f),
                        cap = StrokeCap.Round
                    )
                }
            }

            // Two columns: picture cards (draggable sources) on the left, targets on the right.
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = (850 * scaleFactor).dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left column: one PictureCard per question, reporting its connection point via onPos.
                Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
                    questions.forEachIndexed { idx, q ->
                        PictureCard(
                            name = q.item,
                            isActive = idx == activeIdx,
                            statusColor = when (results[idx]) {
                                true -> Color(0xFF66BB6A)
                                false -> Color(0xFFEF5350)
                                else -> Color.White
                            },
                            scaleFactor = scaleFactor
                        ) { coords ->
                            val root = coords.positionInRoot()
                            // Connection point is the right-middle edge of the card.
                            itemPositions[idx] = Offset(root.x - containerPos.x + coords.size.width, root.y - containerPos.y + coords.size.height / 2)
                        }
                    }
                }
                // Right column: one TargetButton per (shuffled) target label.
                Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
                    targets.forEachIndexed { idx, t ->
                        val isMatched = matchedPairs.any { it.second == idx }
                        val isHovered = hoveredIdx == idx
                        TargetButton(t, isMatched, isHovered, scaleFactor = scaleFactor) { coords ->
                            val root = coords.positionInRoot()
                            // Connection point is the left-middle edge of the target.
                            targetPositions[idx] = Offset(root.x - containerPos.x, root.y - containerPos.y + coords.size.height / 2)
                        }
                    }
                }
            }

            // Full-screen "Wrong!"/"Time's up!" overlay, shown only for negative feedback.
            feedback?.let { (msg, color) -> if (msg == "Wrong!" || msg == "Time's up!") FeedbackDisplay(msg, color, scaleFactor) }
        }
    }
}

/**
 * Wraps [DragDropView] and manages progression across Phase 2's two
 * sub-screens (3 questions each), accumulating the phase's total score before
 * calling [onComplete].
 */
@Composable
fun DragDropPhase(questions: List<GameQuestion>, onComplete: (Int) -> Unit, initialScreenIndex: Int = 0, scaleFactor: Float = 1f) {
    // Which of the two 3-question sub-screens is currently active (0 or 1).
    var screenIndex by remember { mutableIntStateOf(initialScreenIndex) }
    // Running total across both sub-screens of this phase.
    var totalScoreP2 by remember { mutableIntStateOf(0) }

    // Slice out the 3 questions belonging to the current sub-screen.
    val currentBatch = remember(screenIndex, questions) {
        val start = screenIndex * 3
        questions.subList(start, (start + 3).coerceAtMost(questions.size))
    }

    // `key(currentBatch)` forces DragDropView to fully reset its internal state whenever
    // the batch of questions changes (new sub-screen).
    key(currentBatch) {
        DragDropView(
            questions = currentBatch,
            screenTitle = "Phase 2: Drag and Drop (${screenIndex + 1}/2)",
            onScreenComplete = { score ->
                totalScoreP2 += score
                if ((screenIndex + 1) * 3 >= questions.size) {
                    // All sub-screens finished: report the phase's total score.
                    onComplete(totalScoreP2)
                } else {
                    // Advance to the next 3-question sub-screen.
                    screenIndex++
                }
            },
            scaleFactor = scaleFactor
        )
    }
}

/**
 * Phase 2 gameplay screen: the player physically drags an app-icon card onto
 * its matching usage box. Each question has a 10-second timer, mirroring
 * [LineMatchingView]'s timing/feedback rules but with a real draggable card
 * instead of a drawn line.
 */
@Composable
fun DragDropView(
    questions: List<GameQuestion>,
    screenTitle: String,
    onScreenComplete: (Int) -> Unit,
    isTimerEnabled: Boolean = !LocalInspectionMode.current,
    scaleFactor: Float = 1f
) {
    // Index of the question currently being answered.
    var activeIdx by remember { mutableIntStateOf(0) }
    // Countdown timer (seconds) for the active question.
    var timeLeft by remember { mutableIntStateOf(10) }
    // Running score for this sub-screen.
    var score by remember { mutableIntStateOf(0) }
    // Transient feedback message + color (e.g. "Wrong!" in red).
    var feedback by remember { mutableStateOf<Pair<String, Color>?>(null) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    // Targets are shuffled independently of the questions so their order doesn't leak the answer.
    val targets = remember(questions) { questions.map { it.target }.shuffled() }
    // Screen-space centers of each target box, keyed by target index.
    val targetCenters = remember { mutableStateMapOf<Int, Offset>() }
    // Per-question correctness once answered, used to color the "answered" cards.
    val results = remember { mutableStateMapOf<Int, Boolean>() }
    // Indices of targets that have already received a correct drop.
    val matchedTargets = remember { mutableStateListOf<Int>() }
    // Root position of the drag/drop container, used to convert root-space to local-space offsets.
    var containerPos by remember { mutableStateOf(Offset.Zero) }

    // Live center of the card currently being dragged, used to compute hover highlighting.
    var currentDragCenter by remember { mutableStateOf<Offset?>(null) }

    // While dragging, find the nearest target within snapping distance to highlight it.
    val hoveredTargetIdx = remember(currentDragCenter) {
        if (currentDragCenter == null) null
        else {
            var closestIdx: Int? = null
            var closestDist = Float.MAX_VALUE
            val threshold = with(density) { 100.dp.toPx() * scaleFactor }
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

    // Per-question countdown timer. Restarts whenever `activeIdx` (or the question set) changes.
    LaunchedEffect(activeIdx, questions) {
        if (!isTimerEnabled) return@LaunchedEffect
        timeLeft = 10
        while (timeLeft > 0) {
            delay(1000)
            if (feedback == null) timeLeft--
        }
        if (feedback == null) {
            // Ran out of time: mark this question wrong and move on after a short pause.
            feedback = "Time's up!" to Color.Gray
            results[activeIdx] = false
            delay(1000)
            feedback = null
            if (activeIdx < questions.size - 1) activeIdx++ else onScreenComplete(score)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .widthIn(max = (1100 * scaleFactor).dp)
            .fillMaxWidth()
            .padding((16 * scaleFactor).dp)
            .onGloballyPositioned { containerPos = it.positionInRoot() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header row: screen title, countdown timer, running score.
        Row(
            modifier = Modifier
                .widthIn(max = (850 * scaleFactor).dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(screenTitle, color = Color.White, fontWeight = FontWeight.Bold, fontFamily = Kavoon, fontSize = (22 * scaleFactor).sp)
            Text("Timer: ${timeLeft}s", color = if (timeLeft < 3) Color.Red else Color.White, fontFamily = Kavoon,fontSize = (22 * scaleFactor).sp, fontWeight = FontWeight.Black)
            Text("Score: $score", color = Color.White, fontSize = (22 * scaleFactor).sp, fontFamily = Kavoon, fontWeight = FontWeight.Bold)
        }

        // Main play area: draggable app cards on the left, drop targets on the right.
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = (850 * scaleFactor).dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left column: for each question, show either the live draggable card (if active),
                // a colored "answered" card (if already played), or a dimmed "upcoming" placeholder.
                Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
                    questions.forEachIndexed { idx, q ->
                        Box(Modifier.size((180 * scaleFactor).dp, (80 * scaleFactor).dp), contentAlignment = Alignment.Center) {
                            when {
                                // Currently active question: render the real draggable card.
                                idx == activeIdx -> {
                                    DraggableCard(q.item, containerPos, feedback != null, scaleFactor = scaleFactor, onDragMove = { currentDragCenter = it }) { dropCenter ->
                                        currentDragCenter = null
                                        var closestIdx: Int? = null
                                        var closestDist = Float.MAX_VALUE
                                        val threshold = with(density) { 100.dp.toPx() * scaleFactor }

                                        // Find the nearest target within snapping distance of the drop point.
                                        targetCenters.forEach { (tIdx, center) ->
                                            val dist = (dropCenter - center).getDistance()
                                            if (dist < threshold && dist < closestDist) {
                                                closestDist = dist
                                                closestIdx = tIdx
                                            }
                                        }

                                        closestIdx?.let { tIdx ->
                                            if (targets[tIdx] == q.target) {
                                                // Correct drop: award points, mark the target matched, advance.
                                                score += 2
                                                results[idx] = true
                                                matchedTargets.add(tIdx)
                                                if (activeIdx < questions.size - 1) activeIdx++ else onScreenComplete(score)
                                            } else {
                                                // Wrong drop: flash feedback, mark wrong, advance after a short delay.
                                                feedback = "Wrong!" to Color(0xFFEF5350)
                                                results[idx] = false
                                                scope.launch {
                                                    delay(800)
                                                    feedback = null
                                                    if (activeIdx < questions.size - 1) activeIdx++ else onScreenComplete(score)
                                                }
                                            }
                                            true // Consumed the drop; DraggableCard won't spring back.
                                        } ?: false // No target found near the drop point; card springs back to origin.
                                    }
                                }
                                // Already-answered question: show a static green (correct) or red (wrong) card.
                                idx < activeIdx -> {
                                    val isCorrect = results[idx] == true
                                    Card(
                                        modifier = Modifier.fillMaxSize(),
                                        colors = CardDefaults.cardColors(containerColor = if (isCorrect) Color(0xFF66BB6A) else Color(0xFFEF5350)),
                                        shape = RoundedCornerShape((16 * scaleFactor).dp)
                                    ) {
                                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                            ButtonContentWithIcon(q.item, Color.White, scaleFactor)
                                        }
                                    }
                                }
                                // Not-yet-reached question: show a dimmed placeholder card.
                                else -> {
                                    Card(
                                        modifier = Modifier.fillMaxSize(),
                                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
                                        shape = RoundedCornerShape((16 * scaleFactor).dp)
                                    ) {
                                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                            ButtonContentWithIcon(q.item, Color.White.copy(alpha = 0.5f), scaleFactor)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Right column: one TargetButton per (shuffled) target label, reporting its center via onPos.
                Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
                    targets.forEachIndexed { idx, t ->
                        val isNear = hoveredTargetIdx == idx
                        TargetButton(
                            text = t,
                            isMatched = matchedTargets.contains(idx),
                            isHovered = isNear,
                            scaleFactor = scaleFactor
                        ) { coords ->
                            val root = coords.positionInRoot()
                            targetCenters[idx] = Offset(root.x - containerPos.x + coords.size.width / 2f, root.y - containerPos.y + coords.size.height / 2f)
                        }
                    }
                }
            }
        }
    }
    // Full-screen "Wrong!"/"Time's up!" overlay, shown only for negative feedback.
    feedback?.let { (msg, color) -> if (msg != "Correct!") FeedbackDisplay(msg, color, scaleFactor) }
}

/**
 * Renders an item's icon (if one is mapped for its name) alongside its text
 * label, centered. Shared by [PictureCard], [DraggableCard], and the static
 * answered/placeholder cards in [DragDropView].
 */
@Composable
fun ButtonContentWithIcon(name: String, color: Color, scaleFactor: Float) {
    // Map each known item name to its drawable resource; unrecognized names show text-only.
    val iconRes = when (name) {
        "Keyboard" -> R.drawable.keyboar_d
        "Monitor" -> R.drawable.monito_r
        "Mouse" -> R.drawable.mous_e
        "Speakers" -> R.drawable.speaker_s
        "Printer" -> R.drawable.print_er
        "CPU" -> R.drawable.cp_u
        "Word" -> R.drawable.ms_word
        "Chrome" -> R.drawable.chrom_e
        "Messenger" -> R.drawable.messenge_r
        "Duolingo" -> R.drawable.dou_lingo
        "Minecraft" -> R.drawable.mine_craft
        "Youtube" -> R.drawable.yout_ube
        else -> null
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(horizontal = (8 * scaleFactor).dp)
    ) {
        if (iconRes != null) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size((59 * scaleFactor).dp)
                    .padding(end = (8 * scaleFactor).dp)
            )
        }
        Text(
            text = name,
            color = Color(0xFF9575CD),
            fontFamily = Kavoon,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            fontSize = (18 * scaleFactor).sp
        )
    }
}

/**
 * A physically draggable card used in Phase 2. Tracks its own offset via
 * [Animatable]s, reports its live center to [onDragMove] while dragging, and
 * calls [onDrop] with the drop-point center on release. If [onDrop] returns
 * false (no target was close enough), the card animates back to its origin.
 */
@Composable
fun DraggableCard(
    name: String,
    containerPos: Offset,
    isFeedbackShowing: Boolean,
    scaleFactor: Float = 1f,
    onDragMove: (Offset) -> Unit,
    onDrop: (Offset) -> Boolean
) {
    // Animated horizontal/vertical drag offset from the card's original position.
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    var cardSize by remember { mutableStateOf(IntSize.Zero) }
    var startRootPos by remember { mutableStateOf(Offset.Zero) }
    val coroutineScope = rememberCoroutineScope()

    // Slightly enlarge the card while it's being dragged, for a satisfying "picked up" feel.
    val scale by animateFloatAsState(if (offsetX.value != 0f || offsetY.value != 0f) 1.15f else 1f, label = "scale")

    Card(
        modifier = Modifier.size((180 * scaleFactor).dp, (80 * scaleFactor).dp)
            .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
            .onGloballyPositioned {
                // Remember the card's original root position exactly once, on first layout.
                if (startRootPos == Offset.Zero) startRootPos = it.positionInRoot()
                cardSize = it.size
            }
            .scale(scale)
            .pointerInput(name) {
                // Ignore drag gestures while a feedback overlay ("Wrong!"/"Time's up!") is showing.
                if (isFeedbackShowing) return@pointerInput
                detectDragGestures(
                    onDragEnd = {
                        val centerInRoot = startRootPos + Offset(offsetX.value + cardSize.width / 2f, offsetY.value + cardSize.height / 2f)
                        val relCenter = Offset(centerInRoot.x - containerPos.x, centerInRoot.y - containerPos.y)
                        if (!onDrop(relCenter)) {
                            // No target was close enough: spring back to the original position.
                            coroutineScope.launch { offsetX.animateTo(0f, spring(stiffness = Spring.StiffnessLow)) }
                            coroutineScope.launch { offsetY.animateTo(0f, spring(stiffness = Spring.StiffnessLow)) }
                        }
                    },
                    onDrag = { change, drag ->
                        change.consume()
                        coroutineScope.launch { offsetX.snapTo(offsetX.value + drag.x) }
                        coroutineScope.launch { offsetY.snapTo(offsetY.value + drag.y) }
                        // Report the card's live center (in container-local coordinates) for hover detection.
                        val centerInRoot = startRootPos + Offset(offsetX.value + cardSize.width / 2f, offsetY.value + cardSize.height / 2f)
                        onDragMove(Offset(centerInRoot.x - containerPos.x, centerInRoot.y - containerPos.y))
                    },
                    onDragCancel = {
                        // Gesture was cancelled by the system: spring back to the original position.
                        coroutineScope.launch { offsetX.animateTo(0f, spring(stiffness = Spring.StiffnessLow)) }
                        coroutineScope.launch { offsetY.animateTo(0f, spring(stiffness = Spring.StiffnessLow)) }
                    }
                )
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(if (offsetX.value != 0f) 20.dp * scaleFactor else 4.dp * scaleFactor),
        shape = RoundedCornerShape((16 * scaleFactor).dp)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ButtonContentWithIcon(name, Color.Black, scaleFactor)
        }
    }
}

/**
 * A static (non-draggable) picture card used in Phase 1's left column. Gently
 * floats up and down while active or correctly matched, scales up slightly
 * while active, and reports its own position via [onPos] so the parent can
 * draw connecting lines to/from it.
 */
@Composable
fun PictureCard(name: String, isActive: Boolean, statusColor: Color, scaleFactor: Float = 1f, onPos: (LayoutCoordinates) -> Unit) {
    // Gentle up/down float animation, always running, used when the card is active or correctly matched.
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = -4f * scaleFactor,
        targetValue = 4f * scaleFactor,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "float"
    )

    // Slight scale-up while this card is the active question.
    val scale by animateFloatAsState(if (isActive) 1.08f else 1.0f, label = "scale")

    Card(
        modifier = Modifier.width((200 * scaleFactor).dp).height((80 * scaleFactor).dp)
            .onGloballyPositioned(onPos)
            // Float only while active or already correctly matched (green); static otherwise.
            .offset(y = if (isActive || statusColor == Color(0xFF66BB6A)) floatAnim.dp else 0.dp)
            .scale(scale)
            // Yellow highlight border while active and not yet answered (still white).
            .then(if (isActive && statusColor == Color.White) Modifier.border((4 * scaleFactor).dp, Color.Yellow, RoundedCornerShape((16 * scaleFactor).dp)) else Modifier),
        colors = CardDefaults.cardColors(containerColor = statusColor),
        elevation = CardDefaults.cardElevation(if (isActive) 12.dp * scaleFactor else 2.dp * scaleFactor),
        shape = RoundedCornerShape((16 * scaleFactor).dp)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ButtonContentWithIcon(name, if (statusColor == Color.White) Color.Black else Color.White, scaleFactor)
        }
    }
}

/**
 * A "purpose"/target chip used on the right side of both phases. Idly wobbles
 * gently forever, wobbles faster and scales up while a drag is hovering over
 * it, and turns green once matched. Reports its own position via [onPos].
 */
@Composable
fun TargetButton(text: String, isMatched: Boolean, isHovered: Boolean, scaleFactor: Float = 1f, onPos: (LayoutCoordinates) -> Unit) {
    // Slow idle float, used when nothing is hovering over this target.
    val infiniteTransition = rememberInfiniteTransition(label = "wobblePhysics")
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = -3f * scaleFactor,
        targetValue = 3f * scaleFactor,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "idleFloat"
    )
    // Fast excited wiggle, used when a drag is currently hovering over this target.
    val shakeWiggle by infiniteTransition.animateFloat(
        initialValue = -3f * scaleFactor,
        targetValue = 3f * scaleFactor,
        animationSpec = infiniteRepeatable(
            animation = tween(90, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "activeWobble"
    )

    // Bouncy scale-up while hovered.
    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.15f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    Surface(
        modifier = Modifier.width((200 * scaleFactor).dp).height((65 * scaleFactor).dp)
            .onGloballyPositioned(onPos)
            .scale(scale)
            .graphicsLayer {
                // Switch between the idle float and the excited wiggle depending on hover state.
                translationY = if (isHovered) shakeWiggle * 1.5f else floatAnim
                rotationZ = if (isHovered) shakeWiggle * 1.2f else 0f
            },
        shape = RoundedCornerShape((32 * scaleFactor).dp),
        color = if (isMatched) Color(0xFF66BB6A) else if (isHovered) Color.White.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.3f),
        border = BorderStroke(if (isHovered) (4 * scaleFactor).dp else (2 * scaleFactor).dp, if (isHovered) Color.Yellow else Color.White)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text, color = Color.White, fontFamily = Kavoon, fontWeight = FontWeight.Bold, fontSize = (18 * scaleFactor).sp, textAlign = TextAlign.Center)
        }
    }
}

/**
 * A brief full-screen dimmed overlay showing a warning icon and a short
 * message (e.g. "Wrong!" or "Time's up!"). Used by both game phases as
 * transient negative feedback.
 */
@Composable
fun FeedbackDisplay(msg: String, color: Color, scaleFactor: Float = 1f) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)), contentAlignment = Alignment.Center) {
        Card(colors = CardDefaults.cardColors(containerColor = color), shape = RoundedCornerShape((20 * scaleFactor).dp), elevation = CardDefaults.cardElevation(12.dp * scaleFactor)) {
            Row(modifier = Modifier.padding((32 * scaleFactor).dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size((64 * scaleFactor).dp)
                )
                Spacer(Modifier.width((20 * scaleFactor).dp))
                Text(msg, color = Color.White, fontSize = (40 * scaleFactor).sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * The celebratory "WELL DONE!" headline shown on the Game Over screen. Uses a
 * looping diagonal shine gradient sweeping across the text, plus a subtle
 * continuous breathing scale/rotation, and honors an external [entryScale]
 * for its initial pop-in animation.
 */
@Composable
fun WellDoneText(
    entryScale: Float = 1f,
    modifier: Modifier = Modifier,
    scaleFactor: Float = 1f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wellDoneEffects")

    // Horizontal position of the diagonal "shine" gradient, sweeping left-to-right forever.
    val shineOffset by infiniteTransition.animateFloat(
        initialValue = -1000f * scaleFactor,
        targetValue = 2000f * scaleFactor,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "shine"
    )

    // Subtle continuous "breathing" scale, paired with a matching slight rotation below.
    val morphScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "morphScale"
    )

    // Gold-white-gold diagonal gradient brush used to paint the shine effect onto the text.
    val shineBrush = Brush.linearGradient(
        colors = listOf(Color(0xFFFFE082), Color.White, Color(0xFFFFE082)),
        start = Offset(shineOffset, 0f),
        end = Offset(shineOffset + 300f * scaleFactor, 300f * scaleFactor)
    )

    Text(
        text = "WELL DONE!",
        fontSize = (80 * scaleFactor).sp,
        fontWeight = FontWeight.Black,
        fontFamily = Kavoon,
        textAlign = TextAlign.Center,
        style = TextStyle(
            brush = shineBrush,
            shadow = Shadow(color = Color(0xFFB8860B).copy(alpha = 0.5f), offset = Offset(2f * scaleFactor, 2f * scaleFactor), blurRadius = 4f * scaleFactor)
        ),
        modifier = modifier
            .scale(entryScale * morphScale)
            .graphicsLayer {
                // Tiny rotation tied to the breathing scale, for a lively "wobble" feel.
                rotationZ = (morphScale - 1f) * 30f
            }
    )
}

/** Design-time preview of [WellDoneText] on its own, against the game's blue background. */
@Preview(showBackground = true, widthDp = 600, heightDp = 300, backgroundColor = 0xFF4A90E2)
@Composable
fun WellDoneTextPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            WellDoneText()
        }
    }
}

/**
 * Displays "Final Score: X / Y", with externally controlled [alpha] and
 * [translationY] so the caller (GameOverScreen) can drive its fade/slide-in
 * entrance animation.
 */
@Composable
fun ScoreResult(
    score: Int,
    maxPoints: Int,
    alpha: Float = 1f,
    translationY: Float = 0f,
    modifier: Modifier = Modifier,
    scaleFactor: Float = 1f
) {
    Text(
        text = "Final Score: $score / $maxPoints",
        fontFamily = Kavoon,
        fontSize = (36 * scaleFactor).sp,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        modifier = modifier.graphicsLayer {
            this.alpha = alpha
            this.translationY = translationY
        }
    )
}

/** Design-time preview of [ScoreResult] on its own, against the game's blue background. */
@Preview(showBackground = true, backgroundColor = 0xFF4A90E2)
@Composable
fun ScoreResultPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            ScoreResult(score = 22, maxPoints = 25)
        }
    }
}

/**
 * The final results screen: an animated "WELL DONE!" pop-in, a counting-up
 * final score reveal, and (once the score animation is nearly finished) the
 * "Play Again" / "Back to Menu" buttons sliding in from below.
 */
@Composable
fun GameOverScreen(score: Int, onRestart: () -> Unit, onBack: () -> Unit, scaleFactor: Float = 1f) {
    val maxPoints = 25
    val isInspectionMode = LocalInspectionMode.current
    // In inspection mode (Preview), skip animations entirely so the static preview looks correct.
    var startAnimations by remember { mutableStateOf(isInspectionMode) }
    // The score value currently displayed, animated from 0 up to `score`.
    val animatedScore = remember { Animatable(if (isInspectionMode) score.toFloat() else 0f) }

    LaunchedEffect(Unit) {
        if (isInspectionMode) return@LaunchedEffect

        // Small delay before kicking off the entrance animations, for a bit of anticipation.
        startAnimations = true
        delay(600)
        animatedScore.animateTo(
            targetValue = score.toFloat(),
            animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
        )
    }

    // Bouncy pop-in scale for the "WELL DONE!" headline.
    val wellDoneScale by animateFloatAsState(
        targetValue = if (startAnimations) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "entryScale"
    )

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .widthIn(max = (900 * scaleFactor).dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WellDoneText(entryScale = wellDoneScale, scaleFactor = scaleFactor)

        Spacer(Modifier.height((16 * scaleFactor).dp))

        ScoreResult(
            score = animatedScore.value.roundToInt(),
            maxPoints = maxPoints,
            alpha = if (startAnimations) 1f else 0f,
            // Slides up slightly as the WELL DONE! text finishes popping in.
            translationY = (1f - wellDoneScale) * 50f * scaleFactor,
            scaleFactor = scaleFactor
        )

        Spacer(Modifier.height((40 * scaleFactor).dp))

        // Buttons appear only once the score count-up is nearly complete (>= 90% of the way there),
        // or immediately in inspection mode so the preview shows the full screen.
        AnimatedVisibility(
            visible = (startAnimations && animatedScore.value >= score.toFloat() * 0.9f) || isInspectionMode,
            enter = fadeIn(tween(1000)) + slideInVertically(initialOffsetY = { it / 2 })
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = onRestart,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.height((60 * scaleFactor).dp).width((220 * scaleFactor).dp),
                    shape = RoundedCornerShape((30 * scaleFactor).dp)
                ) {
                    Text("Play Again", color = Color(0xFFA173FA), fontFamily = Kavoon, fontSize = (22 * scaleFactor).sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height((20 * scaleFactor).dp))
                OutlinedButton(
                    onClick = onBack,
                    border = BorderStroke((2 * scaleFactor).dp, Color.White),
                    modifier = Modifier.height((60 * scaleFactor).dp).width((220 * scaleFactor).dp),
                    shape = RoundedCornerShape((30 * scaleFactor).dp)
                ) {
                    Text("Back to Menu", color = Color.White, fontFamily = Kavoon, fontSize = (20 * scaleFactor).sp)
                }
            }
        }
    }
}

/** Design-time preview of [GameOverScreen] on its own, against the full game background. */
@Preview(showBackground = true, widthDp = 800, heightDp = 400, name = "GameOver Screen Only")
@Composable
fun GameOverScreenPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        GameBackground {
            GameOverScreen(score = 22, onRestart = {}, onBack = {})
        }
    }
}

/** Design-time preview of the full game starting on the intro screen. */
@Preview(showBackground = true, widthDp = 800, heightDp = 400, name = "Game Intro")
@Composable
fun MatchandLearnIntroPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ConnectThePairsGameScreen(onBackClick = {}, initialState = "intro")
    }
}

/** Design-time preview of Phase 1, sub-screen 1 (questions 1-3). */
@Preview(showBackground = true, widthDp = 800, heightDp = 400, name = "Phase 1: Screen 1")
@Composable
fun MatchandLearnScreen1Preview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ConnectThePairsGameScreen(onBackClick = {}, initialState = "line_matching", initialScreenIndex = 0)
    }
}

/** Design-time preview of Phase 1, sub-screen 2 (questions 4-6). */
@Preview(showBackground = true, widthDp = 800, heightDp = 400, name = "Phase 1: Screen 2")
@Composable
fun MatchandLearnPhase1Screen2Preview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ConnectThePairsGameScreen(onBackClick = {}, initialState = "line_matching", initialScreenIndex = 1)
    }
}

/** Design-time preview of Phase 2, sub-screen 1 (questions 1-3). */
@Preview(showBackground = true, widthDp = 800, heightDp = 400, name = "Phase 2: Screen 1")
@Composable
fun MatchandLearnPhase2Screen1Preview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ConnectThePairsGameScreen(onBackClick = {}, initialState = "drag_drop", initialScreenIndex = 0)
    }
}

/** Design-time preview of Phase 2, sub-screen 2 (questions 4-6). */
@Preview(showBackground = true, widthDp = 800, heightDp = 400, name = "Phase 2: Screen 2")
@Composable
fun MatchandLearnPhase2Screen2Preview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ConnectThePairsGameScreen(onBackClick = {}, initialState = "drag_drop", initialScreenIndex = 1)
    }
}

/** Design-time preview of the Game Over screen with a near-perfect score. */
@Preview(showBackground = true, widthDp = 800, heightDp = 400, name = "Game Result (Over)")
@Composable
fun MatchandLearnGameOverPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ConnectThePairsGameScreen(onBackClick = {}, initialState = "game_over", initialScore = 25)
    }
}