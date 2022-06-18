package com.daurenbek.hackdayapp.network

data class SpecializationModel(
    val id: String,
    val name: String,
    val description: String,
    val lessonA: String,
    val lessonB: String,
    val minMark: Int
)