package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import kotlin.math.absoluteValue

/**
 * GameItemData - Data structure for carousel items to allow easy renaming.
 */
data class GameItemData(val title: String, val color: Color)

/**
 * GamesScreen - Features a smooth, presentable image carousel with a glassomorphic style.
 */
@Composable
fun GamesScreen(onBackClick: () -> Unit) {
    var activeGame by remember { mutableStateOf<String?>(null) }

    when (activeGame) {
        "Word Scramble" -> {
            WordScrambleGameScreen(onBackClick = { activeGame = null })
        }

        "Connect the Pairs" -> {
            ConnectThePairsGameScreen(onBackClick = { activeGame = null })
        }

        "Think & Choose" -> {
            ThinkAndChooseGameScreen(onBackClick = { activeGame = null })
        }

        "Fact or Not?" -> {
            FactOrNotGameScreen(onBackClick = { activeGame = null })
        }

        else -> {
            GamesCarouselContent(
                onBackClick = onBackClick,
                onGameSelect = { activeGame = it }
            )
        }
    }
}

/**
 * GamesCarouselContent - The glassomorphic carousel UI.
 */
@Composable
fun GamesCarouselContent(onBackClick: () -> Unit, onGameSelect: (String) -> Unit) {
    // RENAME YOUR GAMES HERE:
    val carouselItems = listOf(
        GameItemData("Word Scramble", Color(0xFFFFADAD)),
        GameItemData("Connect the Pairs", Color(0xFFFFD6A5)),
        GameItemData("Think & Choose", Color(0xFFFDFFB6)),
        GameItemData("Fact or Not?", Color(0xFFCAFFBF)),
        GameItemData("Challenge Mode", Color(0xFF9BF6FF))
    )

    val pagerState = rememberPagerState(pageCount = { carouselItems.size })

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
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Learning Games",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Smooth Carousel
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 48.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) { page ->
                val item = carouselItems[page]
                Card(
                    onClick = { onGameSelect(item.title) },
                    modifier = Modifier
                        .graphicsLayer {
                            val pageOffset = (
                                (pagerState.currentPage - page) + pagerState
                                    .currentPageOffsetFraction
                            ).absoluteValue

                            lerp(
                                start = 0.85f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).also { scale ->
                                scaleX = scale
                                scaleY = scale
                            }

                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        }
                        .fillMaxSize()
                        .padding(8.dp),
                    shape = RoundedCornerShape(28.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.15f)
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.White.copy(alpha = 0.4f),
                                Color.White.copy(alpha = 0.1f)
                            )
                        )
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        item.color.copy(alpha = 0.25f),
                                        item.color.copy(alpha = 0.05f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Page Indicators
            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val isSelected = pagerState.currentPage == iteration
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color.White else Color.White.copy(alpha = 0.3f))
                            .animateContentSize()
                            .size(width = if (isSelected) 24.dp else 10.dp, height = 10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Swipe to explore!",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GamesPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        GamesScreen(onBackClick = {})
    }
}
