package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsScreen(
    bgmVolume: Float = 0.8f,
    onBgmVolumeChange: (Float) -> Unit = {},
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF4A90E2), // Lighter, cartoonish blue
                        Color(0xFFA173FA)
                    )
                )
            )
    ) {
        // Cloud decorations
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            drawCircle(
                color = Color.White.copy(alpha = 0.2f),
                radius = 40.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(20.dp.toPx(), 60.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.2f),
                radius = 30.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(50.dp.toPx(), 65.dp.toPx())
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.2f),
                radius = 45.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width - 20.dp.toPx(), 80.dp.toPx())
            )
            
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 100.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(0f, height)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 110.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width, height)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 800.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(24.dp)
        ) {
            // Standardized Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                EduTechBackButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                Text(
                    text = "Options",
                    style = TextStyle(
                        fontFamily = Kavoon,
                        fontSize = 32.sp, // Slightly adjusted to fit better in header row
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        shadow = Shadow(
                            color = Color(0xFF4A90E2),
                            offset = androidx.compose.ui.geometry.Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(100.dp))

            // BGM Section - Cartoonized
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = "BGM",
                    style = TextStyle(
                        fontFamily = Kavoon,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        shadow = Shadow(
                            color = Color(0xFF5C79FF),
                            offset = androidx.compose.ui.geometry.Offset(3f, 3f),
                            blurRadius = 4f
                        )
                    )
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                Slider(
                    value = bgmVolume,
                    onValueChange = onBgmVolumeChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    thumb = {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .shadow(6.dp, CircleShape)
                                .background(Color.White, CircleShape)
                                .border(2.dp, Color(0xFF89B9FF).copy(alpha = 0.5f), CircleShape)
                        )
                    },
                    track = { sliderState ->
                        val fraction = (sliderState.value - sliderState.valueRange.start) / (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.3f))
                                .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction)
                                    .fillMaxHeight()
                                    .background(Color.White)
                            )
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OptionsScreenPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        OptionsScreen(onBackClick = {})
    }
}
