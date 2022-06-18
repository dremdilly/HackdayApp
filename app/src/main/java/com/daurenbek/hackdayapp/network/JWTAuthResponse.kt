package com.daurenbek.hackdayapp.network

data class JWTAuthResponse (
    val accessToken: String,
    val tokenType: String
)