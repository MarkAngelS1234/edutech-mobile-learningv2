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
 * InternetBasicsScreen - Detailed lesson content for the "Internet Basics" topic.
 */
@Composable
fun InternetBasicsScreen(onBackClick: () -> Unit) {
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
                        text = "Internet Basics",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A90E2)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "What is the Internet?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "The Internet is a global network of computers connected together. It allows people to share information and communicate with each other from anywhere in the world.",
                        fontSize = 15.sp,
                        color = Color.Gray,
                        lineHeight = 22.sp
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "Key Internet Concepts",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    InternetLessonItem("World Wide Web (WWW)", "A collection of websites you can access through the internet.")
                    InternetLessonItem("Web Browser", "A program (like Chrome or Safari) used to view websites.")
                    InternetLessonItem("Website", "A collection of related web pages with images, videos, or other digital assets.")
                    InternetLessonItem("Search Engine", "A tool (like Google) that helps you find information on the internet.")
                }
            }
        }
    }
}

@Composable
private fun InternetLessonItem(title: String, description: String) {
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
fun InternetBasicsPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        InternetBasicsScreen(onBackClick = {})
    }
}
