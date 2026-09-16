package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners.ui.theme.EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize and "open" the database so it shows in the Database Inspector
        val db = AppDatabase.getDatabase(applicationContext)
        lifecycleScope.launch {
            // Trigger a simple query to ensure the database file is opened
            db.courseDao().getCourseProgress("Startup")
        }

        setContent {
            EduTechMobileLearningApplicationComputerLiteracyForYoungLearnersTheme {
                DashboardAppNavigator()
            }
        }
    }
}
