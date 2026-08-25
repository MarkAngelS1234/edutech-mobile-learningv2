package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProgressScreen(onBackClick: () -> Unit) {
    var studentName by remember { mutableStateOf("") }
    var showCertificate by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFAB47BC), Color(0xFF7B1FA2))
                )
            )
    ) {
        if (showCertificate) {
            CertificateView(name = studentName, onDismiss = { showCertificate = false })
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Your Progress",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Progress Info Card (Temporary Functional)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Overall Progress", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                        LinearProgressIndicator(
                            progress = { 0.75f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .height(8.dp),
                            color = Color(0xFFAB47BC),
                            trackColor = Color.LightGray
                        )
                        Text("75% Completed", fontSize = 14.sp, color = Color.DarkGray)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // E-Cert Generator Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Get Your E-Certificate",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF7B1FA2)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = studentName,
                            onValueChange = { studentName = it },
                            label = { Text("Enter your name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFAB47BC),
                                focusedLabelColor = Color(0xFFAB47BC)
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { if (studentName.isNotBlank()) showCertificate = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB47BC)),
                            shape = RoundedCornerShape(8.dp),
                            enabled = studentName.isNotBlank()
                        ) {
                            Text("Generate E-Certificate", color = Color.White)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    text = "Keep up the great work!",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun CertificateView(name: String, onDismiss: () -> Unit) {
    val currentDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.414f),
                shape = RoundedCornerShape(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF0)),
                border = BorderStroke(4.dp, Color(0xFFD4AF37))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Decorative inner border
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .border(1.dp, Color(0xFFD4AF37))
                    )

                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "CERTIFICATE",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF5D4037),
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            "OF COMPLETION",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5D4037),
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            "This is to certify that",
                            fontSize = 12.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            name.uppercase(),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "has successfully completed the",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            "Computer Literacy Course",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7B1FA2)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(currentDate, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                Box(modifier = Modifier.width(90.dp).height(1.dp).background(Color.Black))
                                Text("Date", fontSize = 9.sp, color = Color.Gray)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("EduTech Team", fontSize = 11.sp, fontWeight = FontWeight.Medium, fontFamily = FontFamily.Cursive)
                                Box(modifier = Modifier.width(90.dp).height(1.dp).background(Color.Black))
                                Text("Signature", fontSize = 9.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Close Certificate", color = Color(0xFF7B1FA2), fontWeight = FontWeight.Bold)
            }
        }
    }
}
