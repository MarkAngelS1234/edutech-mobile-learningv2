package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import kotlinx.coroutines.delay

@Composable
fun MainMenuScreen(
    bgmVolume: Float = 0.8f,
    onBgmVolumeChange: (Float) -> Unit = {},
    onCoursesClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var showGames by remember { mutableStateOf(false) }
    var showOptions by remember { mutableStateOf(false) }
    var showProgress by remember { mutableStateOf(false) }
    var showAbout by remember { mutableStateOf(false) }
    var showAchievements by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    val isTopLevel = !showGames && !showOptions && !showProgress && !showAbout && !showAchievements

    // Handle System Back Button
    BackHandler(enabled = isTopLevel) {
        showExitDialog = true
    }

    if (showExitDialog) {
        ExitConfirmationDialog(
            onConfirm = {
                showExitDialog = false
                (context as? Activity)?.finish()
            },
            onDismiss = { showExitDialog = false }
        )
    }

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
fun ExitConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(enabled = false) { }, // Prevent clicks from going through
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .fillMaxWidth(0.85f)
                    .padding(24.dp),
                shape = RoundedCornerShape(32.dp),
                color = Color.White,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Mascot/Icon
                    Image(
                        painter = painterResource(id = R.drawable.orbb_y),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Are You Sure You\nWant to Exit?",
                        fontFamily = Kavoon,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A90E2),
                        textAlign = TextAlign.Center,
                        lineHeight = 34.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Come back soon to learn more!",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Stay Button
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4A90E2)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "Stay",
                                fontFamily = Kavoon,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }

                        // Exit Button
                        OutlinedButton(
                            onClick = onConfirm,
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            border = BorderStroke(2.dp, Color(0xFFA173FA)),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFFA173FA)
                            )
                        ) {
                            Text(
                                text = "Exit",
                                fontFamily = Kavoon,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
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
                .widthIn(max = 700.dp) // Unified max width for visual balance
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "What would you like to learn today?",
                fontSize = 30.sp,
                fontFamily = Kavoon,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            val menuItems = remember {
                listOf(
                    MenuEntry(
                        "Courses",
                        R.drawable.pictur_e,
                        onCoursesClick
                    ),
                    MenuEntry(
                        "Games",
                        R.drawable.edgam_es,
                        onGamesClick
                    ),
                    MenuEntry(
                        "Options",
                        R.drawable.se_t,
                        onOptionsClick
                    ),
                    MenuEntry(
                        "Progress",
                        R.drawable.p_rog,
                        onProgressClick
                    ),
                    MenuEntry(
                        "Achievements",
                        R.drawable.a_ch,
                        onAchievementsClick
                    ),
                    MenuEntry(
                        "About",
                        R.drawable.info_o,
                        onAboutClick
                    )
                )
            }

            val spacing = 16.dp
            val density = LocalDensity.current
            val initialOffsetPx = with(density) { 30.dp.toPx() }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalArrangement = Arrangement.spacedBy(spacing),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(menuItems, key = { _, entry -> entry.title }) { index, entry ->
                    val visible = remember { mutableStateOf(false) }
                    val isClicked = remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        delay(index * 100L)
                        visible.value = true
                    }

                    // Click transition animations
                    val clickScale by animateFloatAsState(
                        targetValue = if (isClicked.value) 1.06f else 1.0f,
                        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
                        label = "clickScale"
                    )

                    val clickAlpha by animateFloatAsState(
                        targetValue = if (isClicked.value) 0.85f else 1.0f,
                        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
                        label = "clickAlpha"
                    )

                    // Navigation logic triggered after the short animation
                    LaunchedEffect(isClicked.value) {
                        if (isClicked.value) {
                            delay(250) // Wait for the transition to finish
                            entry.onAction()
                        }
                    }

                    val alpha by animateFloatAsState(
                        targetValue = if (visible.value) 1f else 0f,
                        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
                        label = "alpha"
                    )

                    val translationY by animateFloatAsState(
                        targetValue = if (visible.value) 0f else initialOffsetPx,
                        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
                        label = "translationY"
                    )

                    Image(
                        painter = painterResource(id = entry.imageRes),
                        contentDescription = entry.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(185f / 180f)
                            .graphicsLayer {
                                this.alpha = alpha * clickAlpha
                                this.translationY = translationY
                                this.scaleX = clickScale
                                this.scaleY = clickScale
                            }
                            .clip(RoundedCornerShape(20.dp))
                            .clickable(enabled = !isClicked.value) {
                                isClicked.value = true
                            },
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
    }
}

data class MenuEntry(
    val title: String,
    val imageRes: Int,
    val onAction: () -> Unit
)

@Preview(showBackground = true)
@Composable
fun MainMenuPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        MainMenuScreen()
    }
}
