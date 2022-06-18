package com.daurenbek.hackdayapp.network

data class SubjectModel(
    val id: Long,
    val name: String,
    val description: String,
    val specializationSet: List<SpecializationModel>
)
