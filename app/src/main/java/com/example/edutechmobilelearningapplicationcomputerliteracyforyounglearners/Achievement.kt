package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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

data class BadgeTheme(
    val circleColor: Color,
    val pillColor: Color,
    val sparkColor: Color
)

@Composable
fun AchievementsScreen(onBackClick: () -> Unit, viewModel: CourseViewModel = viewModel()) {
    // Avoid initializing the real ViewModel during Compose Preview as it triggers database access
    val isInspection = LocalInspectionMode.current
    
    val progressList by if (isInspection) {
        remember { mutableStateOf(emptyList<CourseProgress>()) }
    } else {
        viewModel.allProgress.collectAsState(initial = emptyList())
    }
    
    val gameProgressList by if (isInspection) {
        remember { mutableStateOf(emptyList<GameProgress>()) }
    } else {
        viewModel.allGameProgress.collectAsState(initial = emptyList())
    }
    
    AchievementsScreenContent(
        onBackClick = onBackClick,
        progressList = progressList,
        gameProgressList = gameProgressList
    )
}

@Composable
fun AchievementsScreenContent(
    onBackClick: () -> Unit,
    progressList: List<CourseProgress>,
    gameProgressList: List<GameProgress>
) {
    val progressMap = remember(progressList) { progressList.associateBy { it.courseName } }
    val gameProgressMap = remember(gameProgressList) { gameProgressList.associateBy { it.gameName } }

    var showCourseBadges by remember { mutableStateOf(true) }
    
    val dynamicCourseBadges = remember(progressMap) {
        listOf(
            Badge(
                name = "Computer Explorer",
                description = "Introduction To Computer",
                icon = "💻",
                isUnlocked = progressMap[ProgressTracker.COURSE_INTRO]?.isCompleted ?: false
            ),
            Badge(
                name = "Hardware Hero",
                description = "Computer Hardware",
                icon = "⚙️",
                isUnlocked = progressMap[ProgressTracker.COURSE_HARDWARE]?.isCompleted ?: false
            ),
            Badge(
                name = "Software Specialist",
                description = "Computer Software",
                icon = "💾",
                isUnlocked = progressMap[ProgressTracker.COURSE_SOFTWARE]?.isCompleted ?: false
            ),
            Badge(
                name = "Internet Voyager",
                description = "Internet Basics",
                icon = "🌐",
                isUnlocked = progressMap[ProgressTracker.COURSE_INTERNET]?.isCompleted ?: false
            ),
            Badge(
                name = "Cyber Guard",
                description = "Internet Safety",
                icon = "🛡️",
                isUnlocked = progressMap[ProgressTracker.COURSE_SAFETY]?.isCompleted ?: false
            )
        )
    }

    val dynamicGameBadges = remember(gameProgressMap) {
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

    val currentBadges = remember(showCourseBadges, dynamicCourseBadges, dynamicGameBadges) {
        if (showCourseBadges) dynamicCourseBadges else dynamicGameBadges
    }

    val backgroundBrush = remember {
        Brush.verticalGradient(listOf(Color(0xFF4A90E2), Color(0xFFA173FA)))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        // Playful background clouds inspired by the reference image
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Top Left Cloud puff
            drawCircle(
                color = Color.White.copy(alpha = 0.2f),
                radius = 45.dp.toPx(),
                center = Offset(20.dp.toPx(), 60.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.2f),
                radius = 35.dp.toPx(),
                center = Offset(60.dp.toPx(), 75.dp.toPx())
            )

            // Top Right Cloud puff
            drawCircle(
                color = Color.White.copy(alpha = 0.2f),
                radius = 55.dp.toPx(),
                center = Offset(width - 20.dp.toPx(), 80.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.2f),
                radius = 40.dp.toPx(),
                center = Offset(width - 70.dp.toPx(), 95.dp.toPx())
            )

            // Large bottom cloud accumulation
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 110.dp.toPx(),
                center = Offset(40.dp.toPx(), height - 10.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 130.dp.toPx(),
                center = Offset(width - 50.dp.toPx(), height - 5.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 90.dp.toPx(),
                center = Offset(width / 2, height + 10.dp.toPx())
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 950.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Row with standardized back button and centered title
            // Header with standardized back button and centered title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                EduTechBackButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                
                Text(
                    text = "My Achievements",
                    fontSize = 24.sp,
                    fontFamily = Kavoon,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // High fidelity multi-tab capsule container selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .background(Color.White.copy(alpha = 0.25f), shape = RoundedCornerShape(50.dp))
                    .border(3.dp, Color.White, shape = RoundedCornerShape(50.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Course Tab Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            if (showCourseBadges) Color(0xFF8C52FF) else Color.Transparent,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .border(
                            width = if (showCourseBadges) 2.dp else 0.dp,
                            color = if (showCourseBadges) Color.White else Color.Transparent,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .clickable { showCourseBadges = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Course",
                        fontFamily = Kavoon,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Games Tab Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            if (!showCourseBadges) Color(0xFF8C52FF) else Color.Transparent,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .border(
                            width = if (!showCourseBadges) 2.dp else 0.dp,
                            color = if (!showCourseBadges) Color.White else Color.Transparent,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .clickable { showCourseBadges = false },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Games",
                        fontFamily = Kavoon,
                        color = if (!showCourseBadges) Color.White else Color(0xFFFFFFFF),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Scrollable Grid Layout matching photo proportions perfectly
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(currentBadges, key = { it.name }) { badge ->
                    BadgeItem(badge)
                }
            }
        }
    }
}

@Composable
fun BadgeItem(badge: Badge) {
    val isCompleted = remember(badge) { badge.requiredPoints > 0 && badge.currentPoints >= badge.requiredPoints }
    val isUnlockedOrCompleted = remember(badge, isCompleted) { badge.isUnlocked || isCompleted }
    val displayedPoints = remember(badge, isCompleted) { if (isCompleted) badge.requiredPoints else badge.currentPoints }

    // Accurate color map exactly mirroring the original design & color scheme of the photo reference
    val themeColors = remember(badge.name) {
        when (badge.name) {
            "Computer Explorer" -> BadgeTheme(
                circleColor = Color(0xFF5C9DFF),
                pillColor = Color(0xFF9C71D2),
                sparkColor = Color(0xFFB392F7)
            )
            "Hardware Hero" -> BadgeTheme(
                circleColor = Color(0xFF00E5A3),
                pillColor = Color(0xFF55B3FF),
                sparkColor = Color(0xFF00E5A3)
            )
            "Software Specialist" -> BadgeTheme(
                circleColor = Color(0xFFFA5BBE),
                pillColor = Color(0xFFF373B4),
                sparkColor = Color(0xFFFA5BBE)
            )
            "Internet Voyager" -> BadgeTheme(
                circleColor = Color(0xFFFFB72B),
                pillColor = Color(0xFFFF9E36),
                sparkColor = Color(0xFFFFB72B)
            )
            "Cyber Guard" -> BadgeTheme(
                circleColor = Color(0xFF8C71FF),
                pillColor = Color(0xFFB392F7),
                sparkColor = Color(0xFF8C71FF)
            )
            // Game badges dynamic theme
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
            // Soft white/blue cute cloud decoration at the bottom of the card matching the reference photo
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

                // Optional game point progress indicator bar
                if (badge.requiredPoints > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isCompleted) "Completed" else "$displayedPoints / ${badge.requiredPoints} pts",
                        fontSize = 11.sp,
                        color = if (isCompleted) Color(0xFF2E7D32) else Color(0xFF3F51B5),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { (displayedPoints.toFloat() / badge.requiredPoints).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(50.dp)),
                        color = themeColors.circleColor,
                        trackColor = Color(0xFFE0E0E0)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AchievementsPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        AchievementsScreenContent(onBackClick = {}, progressList = emptyList(), gameProgressList = emptyList())
    }
}
