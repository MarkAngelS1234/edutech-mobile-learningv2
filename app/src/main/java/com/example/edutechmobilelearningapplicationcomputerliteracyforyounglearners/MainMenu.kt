package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme

@Composable
fun MainMenuScreen(
    bgmVolume: Float = 0.8f,
    onBgmVolumeChange: (Float) -> Unit = {},
    onCoursesClick: () -> Unit = {}
) {
    var showGames by remember { mutableStateOf(false) }
    var showOptions by remember { mutableStateOf(false) }
    var showProgress by remember { mutableStateOf(false) }
    var showAbout by remember { mutableStateOf(false) }
    var showAchievements by remember { mutableStateOf(false) }

    when {
        showGames -> GamesScreen(onBackClick = { showGames = false })
        showOptions -> OptionsScreen(
            bgmVolume = bgmVolume,
            onBgmVolumeChange = onBgmVolumeChange,
            onBackClick = { showOptions = false }
        )
        showProgress -> ProgressScreen(onBackClick = { showProgress = false })
        showAbout -> AboutScreen(onBackClick = { showAbout = false })
        showAchievements -> AchievementsScreen(onBackClick = { showAchievements = false })
        else -> MainMenuView(
            onCoursesClick = onCoursesClick,
            onGamesClick = { showGames = true },
            onOptionsClick = { showOptions = true },
            onProgressClick = { showProgress = true },
            onAboutClick = { showAbout = true },
            onAchievementsClick = { showAchievements = true }
        )
    }
}

@Composable
fun MainMenuView(
    onCoursesClick: () -> Unit,
    onGamesClick: () -> Unit,
    onOptionsClick: () -> Unit,
    onProgressClick: () -> Unit,
    onAboutClick: () -> Unit,
    onAchievementsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF4A90E2), Color(0xFFA173FA))
                )
            )
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
                .fillMaxHeight()
                .widthIn(max = 800.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "What would you like to learn today?",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(40.dp))

            val menuItems = remember {
                listOf(
                    MenuEntry(
                        "Courses",
                        R.drawable.pictur_e,
                        onCoursesClick,
                        width = 185.dp,
                        height = 180.dp
                    ),
                    MenuEntry(
                        "Games",
                        R.drawable.edgam_es,
                        onGamesClick,
                        width = 185.dp,
                        height = 180.dp
                    ),
                    MenuEntry(
                        "Options",
                        R.drawable.se_t,
                        onOptionsClick,
                        width = 185.dp,
                        height = 180.dp
                    ),
                    MenuEntry(
                        "Progress",
                        R.drawable.p_rog,
                        onProgressClick,
                        width = 185.dp,
                        height = 180.dp
                    ),
                    MenuEntry(
                        "Achievements",
                        R.drawable.a_ch,
                        onAchievementsClick,
                        width = 185.dp,
                        height = 180.dp
                    ),
                    MenuEntry(
                        "About",
                        R.drawable.info_o,
                        onAboutClick,
                        width = 185.dp,
                        height = 180.dp
                    )
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(menuItems, key = { it.title }) { entry ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = entry.imageRes),
                            contentDescription = entry.title,
                            modifier = Modifier
                                .width(entry.width)
                                .height(entry.height)
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { entry.onAction() },
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
            }
        }
    }
}

data class MenuEntry(
    val title: String,
    val imageRes: Int,
    val onAction: () -> Unit,
    val width: Dp,
    val height: Dp
)

@Preview(showBackground = true)
@Composable
fun MainMenuPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        MainMenuScreen()
    }
}
