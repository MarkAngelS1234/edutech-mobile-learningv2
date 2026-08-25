package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import kotlinx.coroutines.delay
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon

@Composable
fun LoadingComponent(progress: Float, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 32.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Loading",
            fontFamily = Kavoon,
            fontSize = 27.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(20.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(17.dp),
            color = Color.White,
            trackColor = Color.White.copy(alpha = 0.3f)
        )
    }
}

@Composable
fun LoadingScreen(
    durationMillis: Long = 4000L,
    onLoadingComplete: () -> Unit
) {
    var progress by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        val totalSteps = 100
        val delayPerStep = durationMillis / totalSteps
        for (i in 1..totalSteps) {
            delay(delayPerStep)
            progress = i / 100f
        }
        onLoadingComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4A90E2)), // Darker background to see white text in standalone screen
        contentAlignment = Alignment.Center
    ) {
        LoadingComponent(progress = progress)
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        LoadingScreen(onLoadingComplete = {})
    }
}
