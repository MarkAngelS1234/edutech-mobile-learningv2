package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon

@Composable
fun GameBadgesScreen(onBackClick: () -> Unit, viewModel: CourseViewModel = viewModel()) {
    val isInspection = LocalInspectionMode.current
    val gameProgressList by if (isInspection) {
        remember { mutableStateOf(emptyList<GameProgress>()) }
    } else {
        viewModel.allGameProgress.collectAsState(initial = emptyList())
    }
    GameBadgesContent(onBackClick = onBackClick, gameProgressList = gameProgressList)
}

@Composable
fun GameBadgesContent(onBackClick: () -> Unit, gameProgressList: List<GameProgress>) {
    val gameProgressMap = remember(gameProgressList) { gameProgressList.associateBy { it.gameName } }

    val currentBadges = remember(gameProgressMap) {
        listOf(
            Badge(
                name = "Scramble Wizard",
                description = "Score 30 pts in Word Scramble",
                icon = "🧩",
                isUnlocked = gameProgressMap["Word Scramble"]?.isUnlocked ?: false,
                currentPoints = gameProgressMap["Word Scramble"]?.score ?: 0,
                requiredPoints = 30
            ),
            Badge(
                name = "Logic Master",
                description = "Score 15 pts in Find the Right One",
                icon = "💡",
                isUnlocked = gameProgressMap["Find the Right One"]?.isUnlocked ?: false,
                currentPoints = gameProgressMap["Find the Right One"]?.score ?: 0,
                requiredPoints = 15
            ),
            Badge(
                name = "Pair Pro",
                description = "Score 25 pts in Match and Learn",
                icon = "🔗",
                isUnlocked = gameProgressMap["Match and Learn"]?.isUnlocked ?: false,
                currentPoints = gameProgressMap["Match and Learn"]?.score ?: 0,
                requiredPoints = 25
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF4A90E2), Color(0xFFA173FA))
                )
            )
    ) {
        // Cloud Decorations based on reference photo
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            
            // Top Left Cloud
            drawCircle(
                color = Color.White.copy(alpha = 0.2f),
                radius = 40.dp.toPx(),
                center = Offset(-10.dp.toPx(), 40.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 30.dp.toPx(),
                center = Offset(20.dp.toPx(), 25.dp.toPx())
            )
            
            // Top Right Cloud
            drawCircle(
                color = Color.White.copy(alpha = 0.2f),
                radius = 45.dp.toPx(),
                center = Offset(width + 10.dp.toPx(), 50.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 35.dp.toPx(),
                center = Offset(width - 25.dp.toPx(), 35.dp.toPx())
            )
            
            // Bottom Left Clouds
            drawCircle(
                color = Color.White.copy(alpha = 0.12f),
                radius = 90.dp.toPx(),
                center = Offset(0f, height)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = 70.dp.toPx(),
                center = Offset(70.dp.toPx(), height + 15.dp.toPx())
            )
            
            // Bottom Right Clouds
            drawCircle(
                color = Color.White.copy(alpha = 0.12f),
                radius = 100.dp.toPx(),
                center = Offset(width, height)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = 75.dp.toPx(),
                center = Offset(width - 75.dp.toPx(), height + 20.dp.toPx())
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 950.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 16.dp, end = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                EduTechBackButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Text(
                    text = "Game Badges",
                    fontFamily = Kavoon,
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(currentBadges) { badge ->
                    GameBadgeItem(badge)
                }
            }
        }
    }
}

@Composable
fun GameBadgeItem(badge: Badge) {
    val isCompleted = remember(badge) { badge.requiredPoints > 0 && badge.currentPoints >= badge.requiredPoints }
    val isUnlockedOrCompleted = remember(badge, isCompleted) { badge.isUnlocked || isCompleted }
    val displayedPoints = remember(badge, isCompleted) { if (isCompleted) badge.requiredPoints else badge.currentPoints }

    val themeColors = remember(badge.name) {
        when (badge.name) {
            "Scramble Wizard" -> BadgeTheme(
                circleColor = Color(0xFF5C9DFF),
                pillColor = Color(0xFF9C71D2),
                sparkColor = Color(0xFFB392F7)
            )
            "Logic Master" -> BadgeTheme(
                circleColor = Color(0xFF00E5A3),
                pillColor = Color(0xFF55B3FF),
                sparkColor = Color(0xFF00E5A3)
            )
            "Pair Pro" -> BadgeTheme(
                circleColor = Color(0xFFFA5BBE),
                pillColor = Color(0xFFF373B4),
                sparkColor = Color(0xFFFA5BBE)
            )
            else -> BadgeTheme(
                circleColor = Color(0xFF5C9DFF),
                pillColor = Color(0xFF9C71D2),
                sparkColor = Color(0xFFB392F7)
            )
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(3.dp, Color(0xFF8C52FF))
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Cute cloud decoration at the bottom of the card
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .align(Alignment.BottomCenter)
            ) {
                val w = size.width
                val h = size.height
                drawCircle(
                    color = Color(0xFFA173FA).copy(alpha = 0.7f),
                    radius = 24.dp.toPx(),
                    center = Offset(20.dp.toPx(), h)
                )
                drawCircle(
                    color = Color(0xFFA173FA).copy(alpha = 0.7f),
                    radius = 32.dp.toPx(),
                    center = Offset(w - 25.dp.toPx(), h - 5.dp.toPx())
                )
                drawCircle(
                    color = Color(0xFFA173FA).copy(alpha = 0.7f),
                    radius = 20.dp.toPx(),
                    center = Offset(w / 2, h + 2.dp.toPx())
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 14.dp, start = 8.dp, end = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Circle Badge Icon with stylized diagonal spark lines on both sides
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    // Left sparks
                    Canvas(modifier = Modifier.size(16.dp)) {
                        val sparkC = themeColors.sparkColor
                        drawLine(
                            color = sparkC,
                            start = Offset(12.dp.toPx(), 4.dp.toPx()),
                            end = Offset(4.dp.toPx(), 0.dp.toPx()),
                            strokeWidth = 3.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = sparkC,
                            start = Offset(14.dp.toPx(), 12.dp.toPx()),
                            end = Offset(2.dp.toPx(), 14.dp.toPx()),
                            strokeWidth = 3.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    // Main circle badge icon with high gloss look
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(themeColors.circleColor)
                            .border(3.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isUnlockedOrCompleted) badge.icon else "🔒",
                            fontSize = 32.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    // Right sparks
                    Canvas(modifier = Modifier.size(16.dp)) {
                        val sparkC = themeColors.sparkColor
                        drawLine(
                            color = sparkC,
                            start = Offset(0.dp.toPx(), 4.dp.toPx()),
                            end = Offset(8.dp.toPx(), 0.dp.toPx()),
                            strokeWidth = 3.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = sparkC,
                            start = Offset(2.dp.toPx(), 12.dp.toPx()),
                            end = Offset(14.dp.toPx(), 14.dp.toPx()),
                            strokeWidth = 3.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }

                // Styled pill shape badge name title matching photo exactly
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(themeColors.pillColor, shape = RoundedCornerShape(50.dp))
                        .padding(vertical = 6.dp, horizontal = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badge.name,
                        fontFamily = Kavoon,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Subtitle description under the badge name pill
                Text(
                    text = badge.description,
                    fontSize = 12.sp,
                    fontFamily = Kavoon,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF283593),
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )

                // Refactored cartoonized game point progress indicator bar with water wobbling effect
                if (badge.requiredPoints > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isCompleted) "Completed" else "$displayedPoints / ${badge.requiredPoints} pts",
                        fontFamily = Kavoon,
                        fontSize = 11.sp,
                        color = if (isCompleted) Color(0xFF2E7D32) else Color(0xFF3F51B5),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    CartoonWavyProgressIndicator(
                        progress = (displayedPoints.toFloat() / badge.requiredPoints).coerceIn(0f, 1f),
                        color = themeColors.circleColor,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CartoonWavyProgressIndicator(
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "badgeWave")
    
    val phase1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase1"
    )

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "badgeProgress"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFFE0E0E0))
            .border(2.dp, Color(0xFF8C52FF), RoundedCornerShape(50.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val fillWidth = width * animatedProgress
            
            if (fillWidth > 0) {
                val wavePath = Path().apply {
                    val waveHeight = 3.dp.toPx()
                    val waveLength = 30.dp.toPx()
                    
                    moveTo(0f, height)
                    lineTo(0f, height * 0.3f)
                    
                    var x = 0f
                    while (x <= fillWidth) {
                        val y = height * 0.3f + Math.sin((x / waveLength * 2 * Math.PI) + phase1).toFloat() * waveHeight
                        lineTo(x, y)
                        x += 1f
                    }
                    
                    lineTo(fillWidth, height)
                    close()
                }
                drawPath(path = wavePath, color = color)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameBadgesPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        GameBadgesContent(onBackClick = {}, gameProgressList = emptyList())
    }
}
