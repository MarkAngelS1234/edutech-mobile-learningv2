package com.example.edutechmobilelearningapplicationcomputerliteracyforyounglearners

data class ComputerQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

data class SafetyQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

data class Badge(
    val name: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean,
    val currentPoints: Int = 0,
    val requiredPoints: Int = 0
)
