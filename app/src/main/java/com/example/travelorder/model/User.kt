package com.example.travelorder.model

data class User (
    val id: Int?,
    val name: String,
    val surname: String,
    val email: String,
    val workplace: String,
    val token: String,
    val error: String,
    val address: String,
    val company_name: String,
    val company_address: String
)