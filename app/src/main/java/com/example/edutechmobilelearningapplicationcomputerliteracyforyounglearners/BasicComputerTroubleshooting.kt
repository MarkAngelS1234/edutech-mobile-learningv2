package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme

/**
 * BasicComputerTroubleshootingScreen - Detailed lesson content for the "Basic Computer Troubleshooting" topic.
 */
@Composable
fun BasicComputerTroubleshootingScreen(onBackClick: () -> Unit) {
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
                .padding(horizontal = 20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Lesson Details",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Content Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Basic Computer Troubleshooting",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A90E2)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "What is Troubleshooting?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Troubleshooting is a logical, systematic search for the source of a problem in order to solve it, and make the product or process operational again.",
                        fontSize = 15.sp,
                        color = Color.Gray,
                        lineHeight = 22.sp
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "Common Solutions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    TroubleshootingLessonItem("Check Connections", "Make sure all cables and power cords are plugged in correctly.")
                    TroubleshootingLessonItem("Restart the Computer", "Many problems can be solved by simply turning the computer off and back on.")
                    TroubleshootingLessonItem("Check for Updates", "Sometimes software needs to be updated to fix bugs.")
                    TroubleshootingLessonItem("Ask for Help", "If you can't fix it yourself, don't be afraid to ask an adult or a teacher.")
                }
            }
        }
    }
}

@Composable
fun TroubleshootingLessonItem(title: String, description: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "• $title",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color(0xFF4A90E2)
        )
        Text(
            text = description,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BasicComputerTroubleshootingPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        BasicComputerTroubleshootingScreen(onBackClick = {})
    }
}
