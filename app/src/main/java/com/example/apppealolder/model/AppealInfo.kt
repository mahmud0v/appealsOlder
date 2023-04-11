package com.example.apppealolder.model



data class AppealInfo(
    val id: Int,
    val phone_number: String,
    val district: String,
    val request_data: String,
    val description: String,
    var isAllow: Int
):java.io.Serializable
