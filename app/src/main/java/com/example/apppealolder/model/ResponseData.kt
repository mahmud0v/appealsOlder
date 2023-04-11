package com.example.apppealolder

import java.lang.Exception
data class ResponseData<T>(
    val isSuccess: Boolean,
    val data:T? = null,
    val exception: Exception? =null
)