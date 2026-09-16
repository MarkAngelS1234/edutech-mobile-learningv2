package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon

/**
 * AboutScreen refactored to incorporate cloud decorations inspired by the reference image.
 * The background vertical gradient hex colors are kept exactly as requested without alteration.
 *
 * @param onBackClick Callback for the back navigation button.
 * @param appName Customizable application name.
 * @param version Customizable version string.
 * @param footerText Customizable footer copyright text.
 */
@Composable
fun AboutScreen(
    onBackClick: () -> Unit,
    appName: String = "EduTech Mobile Learning",
    version: String = "Version 1.0",
    footerText: String = "@2026 EduTech Mobile"
) {
    val backgroundBrush = remember {
        Brush.verticalGradient(
            listOf(Color(0xFF4A90E2), Color(0xFFA173FA))
        )
    }

    val logoSurfaceColor = remember { Color.White.copy(alpha = 0.2f) }
    val versionTextColor = remember { Color.White.copy(alpha = 0.7f) }
    val footerTextColor = remember { Color.White.copy(alpha = 0.6f) }

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
            // Header at the very top
            AboutHeader(onBackClick)

            // Pushes the main content to the center, but slightly upward
            Spacer(modifier = Modifier.weight(0.8f))

            // Main Content Area: Icon, Circle, and Info Text (Grouped and Centered)
            AboutMainContent(
                appName = appName,
                version = version,
                logoSurfaceColor = logoSurfaceColor,
                versionTextColor = versionTextColor
            )

            // More weight here to push the content "upward a bit"
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
 * Sub-composable for the About screen header, centering the text "About" perfectly at the center of the header row.
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
    logoSurfaceColor: Color,
    versionTextColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        // App Logo Icon inside a Circular Surface
        Surface(
            modifier = Modifier.size(200.dp),
            shape = CircleShape,
            color = logoSurfaceColor
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxSize(),
                tint = Color.White
            )
        }

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
