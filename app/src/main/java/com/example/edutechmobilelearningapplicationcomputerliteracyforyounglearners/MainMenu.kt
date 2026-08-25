package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

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

    when {
        showGames -> GamesScreen(onBackClick = { showGames = false })
        showOptions -> OptionsScreen(
            bgmVolume = bgmVolume,
            onBgmVolumeChange = onBgmVolumeChange,
            onBackClick = { showOptions = false }
        )
        showProgress -> ProgressScreen(onBackClick = { showProgress = false })
        showAbout -> AboutScreen(onBackClick = { showAbout = false })
        else -> MainMenuView(
            onCoursesClick = onCoursesClick,
            onGamesClick = { showGames = true },
            onOptionsClick = { showOptions = true },
            onProgressClick = { showProgress = true },
            onAboutClick = { showAbout = true }
        )
    }
}

@Composable
fun MainMenuView(
    onCoursesClick: () -> Unit,
    onGamesClick: () -> Unit,
    onOptionsClick: () -> Unit,
    onProgressClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF4A90E2), Color(0xFF50E3C2))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "What would you like to learn today?",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(40.dp))

            val menuItems = remember {
                listOf(
                    MenuEntry("Courses", R.drawable.pictur_e, onCoursesClick, width = 155.dp, height = 150.dp),
                    MenuEntry("Games", R.drawable.edgam_es, onGamesClick, width = 155.dp, height = 150.dp),
                    MenuEntry("Options", R.drawable.se_t, onOptionsClick, width = 155.dp, height = 150.dp),
                    MenuEntry("Progress", R.drawable.p_rog, onProgressClick, width = 155.dp, height = 150.dp),
                    MenuEntry("Shop", R.drawable.a_ch, {}, width = 155.dp, height = 150.dp),
                    MenuEntry("About", R.drawable.info_o, onAboutClick, width = 155.dp, height = 150.dp)
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
