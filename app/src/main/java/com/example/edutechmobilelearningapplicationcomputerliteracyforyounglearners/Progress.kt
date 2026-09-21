package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.Kavoon
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ProgressScreen(onBackClick: () -> Unit, viewModel: CourseViewModel? = null) {
    val isInspectionMode = LocalInspectionMode.current
    val actualViewModel = if (isInspectionMode) null else viewModel ?: viewModel()

    val scrollState = rememberScrollState()

    // Observe the automated stats and progress list from the database
    val progressList by if (isInspectionMode) {
        remember { mutableStateOf(emptyList<CourseProgress>()) }
    } else {
        actualViewModel!!.allProgress.collectAsState(initial = emptyList())
    }
    
    val overallStats by if (isInspectionMode) {
        remember { mutableStateOf(OverallProgress(id = 1, percentage = 0, courseBadgesCount = 0)) }
    } else {
        actualViewModel!!.overallStats.collectAsState()
    }
    
    val progressMap = progressList.associateBy { it.courseName }
    val allCourses = ProgressTracker.ALL_COURSES
    val progressPercentage = overallStats.percentage
    val allCompleted = if (isInspectionMode) true else (progressPercentage == 100)

    // Using rememberSaveable to preserve state during orientation changes
    var showGeneratorDialog by rememberSaveable { mutableStateOf(false) }
    var showCertificateDialog by rememberSaveable { mutableStateOf(false) }
    var userName by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf( Color(0xFF4A90E2), Color(0xFFA173FA)
                    )
                )
            )
    ) {
        // Minimal background decorations
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            drawCircle(
                color = Color.White.copy(alpha = 0.12f),
                radius = 35.dp.toPx(),
                center = Offset(width - 15.dp.toPx(), 60.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = 25.dp.toPx(),
                center = Offset(width - 45.dp.toPx(), 75.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = 80.dp.toPx(),
                center = Offset(0f, height - 10.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.08f),
                radius = 60.dp.toPx(),
                center = Offset(50.dp.toPx(), height)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 800.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                EduTechBackButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Text(
                    text = "Your Progress",
                    fontFamily = Kavoon,
                    fontSize = 29.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Overall Progress",
                        fontWeight = FontWeight.Bold,
                        fontFamily = Kavoon,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    CircularWaterProgressIndicator(
                        progress = progressPercentage / 100f,
                        size = 180.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "$progressPercentage% Completed",
                        fontFamily = Kavoon,
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Course Checklist",
                        fontWeight = FontWeight.Bold,
                        fontFamily = Kavoon,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        allCourses.forEach { courseName ->
                            val progress = progressMap[courseName]
                            val isDone = progress?.isCompleted == true
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (isDone) Color(0xFF4CAF50) else Color.LightGray,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = courseName,
                                        fontFamily = Kavoon,
                                        fontSize = 16.sp,
                                        color = if (isDone) Color.Black else Color.Gray,
                                        fontWeight = if (isDone) FontWeight.Medium else FontWeight.Normal
                                    )
                                    if (isDone && progress != null && progress.totalQuestions > 0) {
                                        Text(
                                            text = "Assessment Score: ${progress.assessmentScore} / ${progress.totalQuestions}",
                                            fontSize = 12.sp,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { showGeneratorDialog = true },
                            enabled = allCompleted,
                            modifier = Modifier.align(Alignment.End),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            ),
                            contentPadding = PaddingValues()
                        ) {
                            val gradientColors = if (allCompleted) {
                                listOf(Color(0xFF4A90E2), Color(0xFFA173FA))
                            } else {
                                listOf(Color.Gray.copy(alpha = 0.6f), Color.LightGray.copy(alpha = 0.6f))
                            }
                            Box(
                                modifier = Modifier
                                    .background(
                                        brush = Brush.horizontalGradient(gradientColors),
                                        shape = RoundedCornerShape(50)
                                    )
                                    .padding(horizontal = 24.dp, vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Claim Certificate",
                                    color = Color.White,
                                    fontFamily = Kavoon,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Dialog for entering learner's name
        if (showGeneratorDialog) {
            Dialog(
                onDismissRequest = { showGeneratorDialog = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                ECertificateGeneratorContent(
                    userName = userName,
                    onUserNameChange = { userName = it },
                    onGenerate = {
                        showGeneratorDialog = false
                        showCertificateDialog = true
                    },
                    onDismiss = { showGeneratorDialog = false }
                )
            }
        }

        // Dialog for certificate preview and saving
        if (showCertificateDialog) {
            Dialog(
                onDismissRequest = { showCertificateDialog = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                ECertificateContent(
                    userName = userName,
                    onClose = { showCertificateDialog = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ECertificateGeneratorContent(
    userName: String,
    onUserNameChange: (String) -> Unit,
    onGenerate: () -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .widthIn(max = 450.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Congratulations!",
                fontFamily = Kavoon,
                fontSize = 26.sp,
                color = Color(0xFF5E35B1),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You've successfully completed all courses! Enter your name to generate your E-Certificate.",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = userName,
                onValueChange = onUserNameChange,
                label = { Text("Your Full Name", fontSize = 14.sp) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4A90E2),
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.5.dp, Color.Gray),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text("Cancel", color = Color.Gray, fontFamily = Kavoon)
                }
                Button(
                    onClick = onGenerate,
                    enabled = userName.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(listOf(Color(0xFF4A90E2), Color(0xFFA173FA))),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Generate", color = Color.White, fontFamily = Kavoon)
                    }
                }
            }
        }
    }
}

@Composable
fun ECertificateContent(
    userName: String,
    onClose: () -> Unit
) {
    val context = LocalContext.current

    // Automatically switch to Landscape when this content is shown and restore on close
    DisposableEffect(Unit) {
        val activity = context.findActivity()
        val originalOrientation = activity?.requestedOrientation ?: ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        onDispose {
            activity?.requestedOrientation = originalOrientation
        }
    }

    val currentDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
    val namePurple = Color(0xFF5E35B1)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "E-Certificate Preview",
                    color = Color.White,
                    fontFamily = Kavoon,
                    fontSize = 22.sp
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.414f)
                    .shadow(20.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.ec_ert),
                        contentDescription = "Certificate Background",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )

                    // Position Name at exactly 42.5% Y height, horizontally centered
                    Text(
                        text = userName.ifBlank { "Learner's Name" },
                        fontFamily = Kavoon,
                        fontSize = 48.sp,
                        color = namePurple,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(BiasAlignment(0f, -0.12f))
                            .fillMaxWidth(0.8f)
                    )

                    // Position Date at exactly 88% Y height and 29.1% X width, perfectly aligning on top of "Date Completed" line
                    Text(
                        text = currentDate,
                        fontFamily = Kavoon,
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.align(BiasAlignment(-0.6f, 0.63f))
                    )
                }
            }
            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    saveCertificateAsPdf(context, userName, currentDate)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(60.dp)
                    .shadow(12.dp, RoundedCornerShape(50))
            ) {
                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text("Save Certificate as PDF", fontFamily = Kavoon, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

fun saveCertificateAsPdf(context: Context, name: String, date: String) {
    val pdfDocument = PdfDocument()
    val pageWidth = 842
    val pageHeight = 595
    val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas
    val paint = Paint()

    val kavoonTypeface = ResourcesCompat.getFont(context, R.font.kavoon_regular)
    val namePurpleInt = 0xFF5E35B1.toInt()

    // Load the background image
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ec_ert)
    if (bitmap != null) {
        val destRect = android.graphics.Rect(0, 0, pageWidth, pageHeight)
        canvas.drawBitmap(bitmap, null, destRect, paint)
    }

    // 1. Draw Name with fixed coordinate position, horizontally centered, and auto-scaled if too long
    val nameText = name.ifBlank { "Learner" }
    val fixedNameCenterX = pageWidth / 2f
    val fixedNameBaselineY = 290f  // Adjusted upward to match the visual design and prevent downward positioning
    val maxNameWidth = 550f

    paint.typeface = kavoonTypeface
    paint.color = namePurpleInt
    paint.textSize = 60f
    paint.isFakeBoldText = true
    paint.textAlign = Paint.Align.CENTER

    // Adjust text size proportionally if name exceeds max width
    var nameWidth = paint.measureText(nameText)
    if (nameWidth > maxNameWidth) {
        paint.textSize = 60f * (maxNameWidth / nameWidth)
    }

    canvas.drawText(nameText, fixedNameCenterX, fixedNameBaselineY, paint)

    // 2. Draw Date with fixed coordinate position, centered directly under 'Date Completed'
    val fixedDateCenterX = 220f
    val fixedDateBaselineY = pageHeight - 107f

    paint.typeface = kavoonTypeface
    paint.color = android.graphics.Color.DKGRAY
    paint.textSize = 20f
    paint.isFakeBoldText = false
    paint.textAlign = Paint.Align.CENTER

    // Measure date width (as required for centering validation)
    val dateWidth = paint.measureText(date)

    canvas.drawText(date, fixedDateCenterX, fixedDateBaselineY, paint)

    pdfDocument.finishPage(page)
    val fileName = "Certificate_${name.replace(" ", "_")}.pdf"
    
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                val outputStream: OutputStream? = context.contentResolver.openOutputStream(it)
                outputStream?.use { os -> pdfDocument.writeTo(os) }
                Toast.makeText(context, "PDF saved to Downloads", Toast.LENGTH_LONG).show()
            }
        } else {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = java.io.File(downloadsDir, fileName)
            pdfDocument.writeTo(java.io.FileOutputStream(file))
            Toast.makeText(context, "PDF saved to Downloads", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Failed to save PDF", Toast.LENGTH_SHORT).show()
    } finally {
        pdfDocument.close()
    }
}

private fun drawPdfStar(canvas: android.graphics.Canvas, paint: Paint, cx: Float, cy: Float, radius: Float) {
    val path = android.graphics.Path()
    val innerRadius = radius * 0.45f
    for (i in 0 until 10) {
        val angle = i * PI / 5 - PI / 2
        val r = if (i % 2 == 0) radius else innerRadius
        val x = (cx + r * cos(angle)).toFloat()
        val y = (cy + r * sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    canvas.drawPath(path, paint)
}

@Composable
fun CircularWaterProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    size: Dp = 180.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "water_wave")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_offset"
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.2f))
            .border(4.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    )
    {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val widthPx = size.toPx()
            val heightPx = size.toPx()
            val waveHeight = 10.dp.toPx()
            val fillLevel = heightPx * (1 - progress)

            val waterPath = Path().apply {
                moveTo(0f, heightPx)
                lineTo(0f, fillLevel)
                for (x in 0..widthPx.toInt()) {
                    val y = fillLevel + waveHeight * sin((x / widthPx) * 2 * PI + waveOffset).toFloat()
                    lineTo(x.toFloat(), y)
                }
                lineTo(widthPx, heightPx)
                close()
            }

            drawPath(
                path = waterPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF4A90E2), Color(0xFFA173FA))
                )
            )
        }
        Text(
            text = "${(progress * 100).toInt()}%",
            fontFamily = Kavoon,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = if (progress > 0.5f) Color.White else Color(0xFF4A90E2),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

// Helper function to find Activity from Context
fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

@Preview(showBackground = true)
@Composable
fun ProgressPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ProgressScreen(onBackClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ECertificateGeneratorPreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        ECertificateGeneratorContent(
            userName = "Alex",
            onUserNameChange = {},
            onGenerate = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 600)
@Composable
fun ECertificatePreview() {
    EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
        Box(modifier = Modifier.fillMaxSize().background(Color.Gray), contentAlignment = Alignment.Center) {
            ECertificateContent(
                userName = "Jane Doe",
                onClose = {}
            )
        }
    }
}
