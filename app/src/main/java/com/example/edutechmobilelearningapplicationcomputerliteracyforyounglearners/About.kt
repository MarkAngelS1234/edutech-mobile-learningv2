package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon

/**
 * AboutScreen refactored to match the reference image layout.
 * Includes a vertical gradient background, decorative clouds, and a logo with a soft blob background.
 */
@Composable
fun AboutScreen(
    onBackClick: () -> Unit,
    appName: String = "EduTech Mobile Learning",
    version: String = "Version 1.0",
    footerText: String = "@2026 EduTech Mobile",
    logoSize: Dp = 180.dp
) {
    val backgroundBrush = remember {
        Brush.verticalGradient(
            listOf(Color(0xFF4A90E2), Color(0xFFA173FA))
        )
    }

    val versionTextColor = Color.White.copy(alpha = 0.9f)
    val footerTextColor = Color.White.copy(alpha = 0.8f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        // Decorative background cloud layout mimicking the reference image
        BackgroundDecorations()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header at the top
            AboutHeader(onBackClick)

            Spacer(modifier = Modifier.weight(1f))

            // Main Content Area: Icon and Info Text
            AboutMainContent(
                appName = appName,
                version = version,
                versionTextColor = versionTextColor,
                logoSize = logoSize
            )

            Spacer(modifier = Modifier.weight(1.2f))

            // Footer at the bottom
            AboutFooter(
                footerText = footerText,
                footerTextColor = footerTextColor
            )
        }
    }
}

/**
 * Sub-composable for the About screen header.
 */
@Composable
fun AboutHeader(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Text(
            fontFamily = Kavoon,
            text = "About",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Sub-composable for the main information area.
 */
@Composable
fun AboutMainContent(
    appName: String,
    version: String,
    versionTextColor: Color,
    logoSize: Dp = 200.dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        // App Logo Icon - Circular Surface removed
        Image(
            painter = painterResource(id = R.drawable.orbb_y),
            contentDescription = null,
            modifier = Modifier.size(logoSize),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Application Name Text
        Text(
            text = appName,
            fontFamily = Kavoon,
            fontSize = 35.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 35.sp
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Version Text
    Text(
        fontFamily = Kavoon,
        text = version,
        fontSize = 18.sp,
        color = versionTextColor,
        textAlign = TextAlign.Center,
        lineHeight = 50.sp
    )
}

/**
 * Sub-composable for the About screen footer.
 */
@Composable
fun AboutFooter(footerText: String, footerTextColor: Color) {
    Text(
        fontFamily = Kavoon,
        text = footerText,
        color = footerTextColor,
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
        lineHeight = 50.sp,
    )
    Spacer(modifier = Modifier.height(16.dp))
}

/**
 * Decorative background elements (clouds) to match the provided reference image layout.
 * Uses overlapping circles with low opacity to create a beautiful, playful sky effect.
 */
@Composable
private fun BackgroundDecorations() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Top-left subtle clouds
        Box(
            modifier = Modifier
                .offset(x = (-25).dp, y = 50.dp)
                .size(90.dp)
                .background(Color.White.copy(alpha = 0.22f), CircleShape)
        )
        Box(
            modifier = Modifier
                .offset(x = 30.dp, y = 70.dp)
                .size(60.dp)
                .background(Color.White.copy(alpha = 0.15f), CircleShape)
        )

        // Top-right subtle clouds
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 25.dp, y = 40.dp)
                .size(110.dp)
                .background(Color.White.copy(alpha = 0.2f), CircleShape)
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-45).dp, y = 60.dp)
                .size(65.dp)
                .background(Color.White.copy(alpha = 0.15f), CircleShape)
        )

        // Bottom-left larger clouds
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-40).dp, y = 40.dp)
                .size(210.dp)
                .background(Color.White.copy(alpha = 0.12f), CircleShape)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 60.dp, y = 100.dp)
                .size(150.dp)
                .background(Color.White.copy(alpha = 0.08f), CircleShape)
        )

        // Bottom-right larger clouds
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 50.dp, y = 60.dp)
                .size(250.dp)
                .background(Color.White.copy(alpha = 0.14f), CircleShape)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-60).dp, y = 120.dp)
                .size(180.dp)
                .background(Color.White.copy(alpha = 0.08f), CircleShape)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        AboutScreen(onBackClick = {})
    }
}
