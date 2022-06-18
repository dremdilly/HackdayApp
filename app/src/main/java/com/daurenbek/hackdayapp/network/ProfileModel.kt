package com.daurenbek.hackdayapp.network

data class ProfileModel(
    val id: Long,
    val firstName: String?,
    val secondName: String?,
    val email: String,
    val roles: List<RoleModel>
//    val dateOfBirth: String?,
//    val password: String,
)