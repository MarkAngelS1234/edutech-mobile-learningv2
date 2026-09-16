package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Query("SELECT * FROM course_progress")
    fun getAllProgress(): Flow<List<CourseProgress>>

    @Query("SELECT * FROM course_progress WHERE courseName = :name LIMIT 1")
    suspend fun getCourseProgress(name: String): CourseProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(progress: CourseProgress)

    @Query("UPDATE course_progress SET isCompleted = :completed WHERE courseName = :name")
    suspend fun updateCompletion(name: String, completed: Boolean)

    @Query("UPDATE course_progress SET assessmentScore = :score, totalQuestions = :total WHERE courseName = :name")
    suspend fun updateScore(name: String, score: Int, total: Int)

    @Query("SELECT * FROM overall_progress WHERE id = 1 LIMIT 1")
    fun getOverallProgress(): Flow<OverallProgress?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOverallProgress(progress: OverallProgress)

    // Game Progress methods
    @Query("SELECT * FROM game_progress")
    fun getAllGameProgress(): Flow<List<GameProgress>>

    @Query("SELECT * FROM game_progress WHERE gameName = :name LIMIT 1")
    suspend fun getGameProgress(name: String): GameProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateGame(progress: GameProgress)

    @Query("UPDATE game_progress SET score = :score, isUnlocked = :unlocked WHERE gameName = :name")
    suspend fun updateGameScore(name: String, score: Int, unlocked: Boolean)
}
