package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon

@Composable
fun GameBadgesScreen(onBackClick: () -> Unit, viewModel: CourseViewModel = viewModel()) {
    val gameProgressList by viewModel.allGameProgress.collectAsState(initial = emptyList())
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
                description = "Score 15 pts in Think and Choose",
                icon = "💡",
                isUnlocked = gameProgressMap["Think and Choose"]?.isUnlocked ?: false,
                currentPoints = gameProgressMap["Think and Choose"]?.score ?: 0,
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
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            
            // Top Left Cloud
            drawCircle(
                color = Color.White.copy(alpha = 0.2f),
                radius = 40.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(-10.dp.toPx(), 40.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 30.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(20.dp.toPx(), 25.dp.toPx())
            )
            
            // Top Right Cloud
            drawCircle(
                color = Color.White.copy(alpha = 0.2f),
                radius = 45.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width + 10.dp.toPx(), 50.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 35.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width - 25.dp.toPx(), 35.dp.toPx())
            )
            
            // Bottom Left Clouds
            drawCircle(
                color = Color.White.copy(alpha = 0.12f),
                radius = 90.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(0f, height)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = 70.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(70.dp.toPx(), height + 15.dp.toPx())
            )
            
            // Bottom Right Clouds
            drawCircle(
                color = Color.White.copy(alpha = 0.12f),
                radius = 100.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width, height)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = 75.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width - 75.dp.toPx(), height + 20.dp.toPx())
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
                    .padding(top = 40.dp)
                    .padding(horizontal = 8.dp),
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
                    text = "Game Badges",
                    fontFamily = Kavoon,
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
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
                    BadgeItem(badge)
                }
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
