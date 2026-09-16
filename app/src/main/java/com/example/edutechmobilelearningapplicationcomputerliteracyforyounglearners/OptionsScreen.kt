package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsScreen(
    bgmVolume: Float = 0.8f,
    onBgmVolumeChange: (Float) -> Unit = {},
    onBackClick: () -> Unit
) {
    var soundVolume by remember { mutableStateOf(0.5f) }
    var forceMode by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF4A90E2),
                        Color(0xFFA173FA)
                    )
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
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Options",
                    fontFamily = Kavoon,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Sound Bar
            Text(
                text = "Sound Effects",
                fontFamily = Kavoon,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Slider(
                value = soundVolume,
                onValueChange = { soundVolume = it },
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // BGM Bar - Now functional
            Text(
                text = "BGM",
                fontFamily = Kavoon,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Slider(
                value = bgmVolume,
                onValueChange = onBgmVolumeChange,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Force Mode Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

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
