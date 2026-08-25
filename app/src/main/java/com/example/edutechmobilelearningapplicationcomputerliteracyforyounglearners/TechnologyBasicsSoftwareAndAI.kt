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
 * TechnologyBasicsSoftwareAndAIScreen - Detailed lesson content for "Technology Basics: Software and AI".
 */
@Composable
fun TechnologyBasicsSoftwareAndAIScreen(onBackClick: () -> Unit) {
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
                        text = "Technology Basics: Software and AI",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A90E2)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "What is Software?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Software is a set of instructions that tells a computer what to do. While hardware is something you can touch, software is digital.",
                        fontSize = 15.sp,
                        color = Color.Gray,
                        lineHeight = 22.sp
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "Types of Software",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    TechLessonItem("System Software", "Programs like Windows or macOS that help the computer run itself.")
                    TechLessonItem("Application Software", "Apps you use to do specific things, like games or drawing tools.")
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "What is AI (Artificial Intelligence)?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "AI is when computers are trained to 'think' or learn like humans. It helps computers recognize faces, play games, or suggest videos you might like.",
                        fontSize = 15.sp,
                        color = Color.Gray,
                        lineHeight = 22.sp
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    TechLessonItem("Learning", "AI learns from lots of information to get better at its job.")
                    TechLessonItem("Everyday AI", "AI is used in voice assistants like Alexa or Siri and in self-driving cars.")
                }
            }
        }
    }
}

@Composable
private fun TechLessonItem(title: String, description: String) {
    Column(modifier = Modifier.padding(vertical = 10.dp)) {
        Text(
            text = "• $title",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = Color(0xFF4A90E2)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            fontSize = 15.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp),
            lineHeight = 20.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TechnologyBasicsSoftwareAndAIPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        TechnologyBasicsSoftwareAndAIScreen(onBackClick = {})
    }
}
