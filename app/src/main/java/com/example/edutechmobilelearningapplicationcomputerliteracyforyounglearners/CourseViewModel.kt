package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CourseViewModel(application: Application) : AndroidViewModel(application) {
    private val courseDao = AppDatabase.getDatabase(application).courseDao()
    
    val allProgress: Flow<List<CourseProgress>> = courseDao.getAllProgress()
    val allGameProgress: Flow<List<GameProgress>> = courseDao.getAllGameProgress()
    
    // Automated stats calculation based on database state
    val overallStats: StateFlow<OverallProgress> = allProgress.map { list ->
        val allCourses = ProgressTracker.ALL_COURSES
        val completedCount = allCourses.count { courseName ->
            list.any { it.courseName == courseName && it.isCompleted }
        }
        val percentage = if (allCourses.isNotEmpty()) {
            (completedCount.toFloat() / allCourses.size * 100).toInt()
        } else 0
        OverallProgress(id = 1, percentage = percentage, courseBadgesCount = completedCount)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OverallProgress(id = 1, percentage = 0, courseBadgesCount = 0))

    init {
        // Sync the calculated percentage and badge count to the overall_progress table
        viewModelScope.launch {
            overallStats.collect { stats ->
                courseDao.updateOverallProgress(stats)
            }
        }
    }

    fun markCourseCompleted(courseName: String) {
        viewModelScope.launch {
            val existing = courseDao.getCourseProgress(courseName)
            if (existing == null) {
                courseDao.insertOrUpdate(CourseProgress(courseName, isCompleted = true))
            } else if (!existing.isCompleted) {
                courseDao.updateCompletion(courseName, true)
            }
        }
    }

    fun updateAssessmentScore(courseName: String, score: Int, total: Int) {
        viewModelScope.launch {
            val existing = courseDao.getCourseProgress(courseName)
            if (existing == null) {
                courseDao.insertOrUpdate(
                    CourseProgress(
                        courseName = courseName,
                        isCompleted = true,
                        assessmentScore = score,
                        totalQuestions = total
                    )
                )
            } else {
                courseDao.updateScore(courseName, score, total)
                courseDao.updateCompletion(courseName, true)
            }
        }
    }

    fun updateGameScore(gameName: String, sessionScore: Int) {
        viewModelScope.launch {
            val existing = courseDao.getGameProgress(gameName)
            // Cumulative scoring logic: add session points to existing total
            val currentScore = existing?.score ?: 0
            val newTotalScore = currentScore + sessionScore
            
            // Required thresholds for unlocking badges
            val required = when (gameName) {
                "Word Scramble" -> 30
                "Think and Choose" -> 15
                "Match and Learn" -> 25
                else -> 10
            }
            
            val isUnlocked = newTotalScore >= required || (existing?.isUnlocked ?: false)
            
            if (existing == null) {
                courseDao.insertOrUpdateGame(GameProgress(gameName, newTotalScore, isUnlocked))
            } else {
                courseDao.updateGameScore(gameName, newTotalScore, isUnlocked)
            }
        }
    }
}
