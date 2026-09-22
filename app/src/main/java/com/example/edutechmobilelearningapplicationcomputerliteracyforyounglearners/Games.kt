    package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

    import androidx.compose.animation.animateContentSize
    import androidx.compose.foundation.BorderStroke
    import androidx.compose.foundation.Canvas
    import androidx.compose.foundation.background
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.pager.HorizontalPager
    import androidx.compose.foundation.pager.rememberPagerState
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.automirrored.filled.ArrowBack
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.runtime.saveable.rememberSaveable
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.geometry.Offset
    import androidx.compose.ui.graphics.Brush
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.graphics.graphicsLayer
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.compose.ui.util.lerp
    import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
    import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon
    import kotlin.math.absoluteValue

    /**
     * GameItemData - Data structure for carousel items to allow easy renaming.
     */
    data class GameItemData(val title: String, val color: Color, val backgroundImage: Int? = null)

    /**
     * GamesScreen - Features a smooth, presentable image carousel with a glassomorphic style.
     * Refactored to accept initialPage to support previewing different carousel items.
     */
    @Composable
    fun GamesScreen(onBackClick: () -> Unit, initialPage: Int = 0) {
        // Preserve active game state across orientation changes
        var activeGame by rememberSaveable { mutableStateOf<String?>(null) }

        when (activeGame) {
            "Word Scramble" -> {
                WordScrambleGameScreen(onBackClick = { activeGame = null })
            }

            "Match and Learn" -> {
                ConnectThePairsGameScreen(onBackClick = { activeGame = null })
            }

            "Find the Right One" -> {
                FindTheRightOneGameScreen(onBackClick = { activeGame = null })
            }

            else -> {
                GamesCarouselContent(
                    onBackClick = onBackClick,
                    onGameSelect = { activeGame = it },
                    initialPage = initialPage
                )
            }
        }
    }

    /**
     * GamesCarouselContent - Features a carousel with 3D offset block shadows.
     */
    @Composable
    fun GamesCarouselContent(
        onBackClick: () -> Unit,
        onGameSelect: (String) -> Unit,
        initialPage: Int = 0
    ) {
        val carouselItems = listOf(
            GameItemData(
                "Word Scramble",
                Color(0xFFFFADAD),
                backgroundImage = R.drawable.word_scramble
            ),
            GameItemData(
                "Match and Learn",
                Color(0xFFFFD6A5),
                backgroundImage = R.drawable.match_and_learn
            ),
            GameItemData(
                "Find the Right One",
                Color(0xFFFDFFB6),
                backgroundImage = R.drawable.think_and_choose
            )
        )

        val pagerState = rememberPagerState(
            initialPage = initialPage,
            pageCount = { carouselItems.size }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF4A90E2), Color(0xFFA173FA))
                    )
                )
        ) {
            // Decorative background elements inspired by the reference photo
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                // Top clouds
                drawCircle(
                    color = Color.White.copy(alpha = 0.2f),
                    radius = 50.dp.toPx(),
                    center = Offset(0f, 60.dp.toPx())
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.15f),
                    radius = 70.dp.toPx(),
                    center = Offset(20.dp.toPx(), 40.dp.toPx())
                )

                drawCircle(
                    color = Color.White.copy(alpha = 0.2f),
                    radius = 60.dp.toPx(),
                    center = Offset(width, 80.dp.toPx())
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.15f),
                    radius = 80.dp.toPx(),
                    center = Offset(width - 30.dp.toPx(), 60.dp.toPx())
                )

                // Bottom "humps" or bubbles
                drawCircle(
                    color = Color(0xFFA173FA).copy(alpha = 0.4f),
                    radius = 180.dp.toPx(),
                    center = Offset(40.dp.toPx(), height + 40.dp.toPx())
                )
                drawCircle(
                    color = Color(0xFFA173FA).copy(alpha = 0.3f),
                    radius = 120.dp.toPx(),
                    center = Offset(-20.dp.toPx(), height - 20.dp.toPx())
                )

                drawCircle(
                    color = Color(0xFFA173FA).copy(alpha = 0.4f),
                    radius = 200.dp.toPx(),
                    center = Offset(width - 60.dp.toPx(), height + 60.dp.toPx())
                )
                drawCircle(
                    color = Color(0xFFA173FA).copy(alpha = 0.3f),
                    radius = 150.dp.toPx(),
                    center = Offset(width + 20.dp.toPx(), height - 40.dp.toPx())
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = 1000.dp)
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with navigation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 20.dp, end = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    EduTechBackButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    Text(
                        text = "Learning Games",
                        color = Color.White,
                        fontFamily = Kavoon,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Smooth Carousel
                HorizontalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = 48.dp),
                    beyondViewportPageCount = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp) // Height increased for the shadow offset
                ) { page ->
                    val item = carouselItems[page]

                    Box(
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
                            .padding(16.dp)
                    ) {
                        // Thick Gradient Block Shadow (based on reference photo)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .offset(x = 12.dp, y = 12.dp)
                                .background(
                                    brush = Brush.verticalGradient(
                                        listOf(Color(0xFFFFFFFF), Color(0xFFA173FA))
                                    ),
                                    shape = RoundedCornerShape(32.dp)
                                )
                        )

                        // Main White Card
                        Card(
                            onClick = { onGameSelect(item.title) },
                            modifier = Modifier.fillMaxSize(),
                            shape = RoundedCornerShape(32.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (item.backgroundImage != null) Color.Black else Color.White
                            ),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(32.dp)) // Clip content to card corners
                            ) {
                                // Background Image if provided
                                if (item.backgroundImage != null) {
                                    Image(
                                        painter = painterResource(id = item.backgroundImage),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop // Fills the box perfectly
                                    )
                                    // Add a slight dark overlay to make text pop more
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Black.copy(alpha = 0.2f))
                                    )
                                } else {
                                    // Fallback to gradient background
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.linearGradient(
                                                    colors = listOf(
                                                        item.color.copy(alpha = 0.2f),
                                                        Color.White
                                                    )
                                                )
                                            )
                                    )
                                }

                                // Game Title
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = item.title,
                                        fontFamily = Kavoon,
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (item.backgroundImage != null) Color.White else Color(0xFF2D2D2D),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .then(
                                                if (item.backgroundImage != null) {
                                                    Modifier.background(
                                                        Color.Black.copy(alpha = 0.4f),
                                                        RoundedCornerShape(12.dp)
                                                    ).padding(horizontal = 16.dp, vertical = 8.dp)
                                                } else Modifier
                                            )
                                    )
                                }
                            }
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
                    fontFamily = Kavoon,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    @Preview(showBackground = true, name = "Carousel - Word Scramble", device = "id:pixel_5", showSystemUi = true)
    @Composable
    fun PreviewWordScramble() {
        EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
            GamesScreen(onBackClick = {}, initialPage = 0)
        }
    }

    @Preview(showBackground = true, name = "Carousel - Match and Learn", device = "id:pixel_5", showSystemUi = true)
    @Composable
    fun PreviewMatchAndLearn() {
        EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
            GamesScreen(onBackClick = {}, initialPage = 1)
        }
    }

    @Preview(showBackground = true, name = "Carousel - Find the Right One", device = "id:pixel_5", showSystemUi = true)
    @Composable
    fun PreviewThinkAndChoose() {
        EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
            GamesScreen(onBackClick = {}, initialPage = 2)
        }
    }
