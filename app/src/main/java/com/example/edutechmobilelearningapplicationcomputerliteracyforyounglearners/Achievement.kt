package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon

@Composable
fun AchievementsScreen(onBackClick: () -> Unit, viewModel: CourseViewModel = viewModel()) {
    val progressList by viewModel.allProgress.collectAsState(initial = emptyList())
    val gameProgressList by viewModel.allGameProgress.collectAsState(initial = emptyList())
    
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
                description = "Score 15 pts in Think & Choose",
                icon = "💡",
                isUnlocked = gameProgressMap["Think & Choose"]?.isUnlocked ?: false,
                currentPoints = gameProgressMap["Think & Choose"]?.score ?: 0,
                requiredPoints = 15
            ),
            Badge(
                name = "Pair Pro",
                description = "Score 25 pts in Connect the Pairs",
                icon = "🔗",
                isUnlocked = gameProgressMap["Connect the Pairs"]?.isUnlocked ?: false,
                currentPoints = gameProgressMap["Connect the Pairs"]?.score ?: 0,
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
        // Clean, presentable cloud background decorations inspired by reference photo
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Top Left Cloud (Subtle, soft white)
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 35.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(10.dp.toPx(), 50.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 25.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(40.dp.toPx(), 55.dp.toPx())
            )

            // Top Right Cloud
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 40.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width - 15.dp.toPx(), 60.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 30.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width - 45.dp.toPx(), 70.dp.toPx())
            )

            // Bottom Left Cloud Puff
            drawCircle(
                color = Color.White.copy(alpha = 0.12f),
                radius = 85.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(0f, height - 10.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = 60.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(55.dp.toPx(), height)
            )

            // Bottom Right Cloud Puff
            drawCircle(
                color = Color.White.copy(alpha = 0.12f),
                radius = 95.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width, height - 15.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = 65.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width - 65.dp.toPx(), height)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "My Achievements",
                    fontSize = 24.sp,
                    fontFamily = Kavoon,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AchievementTab(
                    text = "Course",
                    isSelected = showCourseBadges,
                    onClick = { showCourseBadges = true },
                    modifier = Modifier.weight(1f)
                )
                AchievementTab(
                    text = "Games",
                    isSelected = !showCourseBadges,
                    onClick = { showCourseBadges = false },
                    modifier = Modifier.weight(1f)
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(currentBadges, key = { it.name }) { badge ->
                    BadgeItem(badge)
                }
            }
        }
    }
}

@Composable
fun AchievementTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = remember(isSelected) {
        if (isSelected) Color.White.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.1f)
    }
    
    Surface(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(12.dp),
        color = containerColor,
        border = if (isSelected) BorderStroke(2.dp, Color.White) else null
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                fontFamily = Kavoon,
                text = text,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun BadgeItem(badge: Badge) {
    val isCompleted = remember(badge) { badge.requiredPoints > 0 && badge.currentPoints >= badge.requiredPoints }
    val isUnlockedOrCompleted = remember(badge, isCompleted) { badge.isUnlocked || isCompleted }
    val displayedPoints = remember(badge, isCompleted) { if (isCompleted) badge.requiredPoints else badge.currentPoints }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(if (isUnlockedOrCompleted) Color.White.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.1f))
                    .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isUnlockedOrCompleted) badge.icon else "🔒",
                    fontSize = 32.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = badge.name,
                fontFamily = Kavoon,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = badge.description,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )

            if (badge.requiredPoints > 0) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (isCompleted) "Completed" else "$displayedPoints / ${badge.requiredPoints} pts",
                    fontSize = 11.sp,
                    color = if (isCompleted) Color.Yellow else Color.White,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { (displayedPoints.toFloat() / badge.requiredPoints).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth(0.8f).height(4.dp)
                )
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
