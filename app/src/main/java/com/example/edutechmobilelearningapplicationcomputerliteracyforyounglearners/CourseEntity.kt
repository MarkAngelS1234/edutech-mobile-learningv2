package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course_progress")
data class CourseProgress(
    @PrimaryKey val courseName: String,
    val isCompleted: Boolean = false,
    val assessmentScore: Int = 0,
    val totalQuestions: Int = 0
)

@Entity(tableName = "overall_progress")
data class OverallProgress(
    @PrimaryKey val id: Int = 1,
    val percentage: Int = 0,
    val courseBadgesCount: Int = 0
)

@Entity(tableName = "game_progress")
data class GameProgress(
    @PrimaryKey val gameName: String,
    val score: Int = 0,
    val isUnlocked: Boolean = false
)

@Entity(tableName = "game_badges")
data class GameBadgeEntity(
    @PrimaryKey val name: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean = false,
    val currentPoints: Int = 0,
    val requiredPoints: Int = 0
)
