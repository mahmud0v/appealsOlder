package com.example.apppealolder.db

import java.lang.Exception


interface AsyncTaskCallback<T> {

    fun onSuccess(data: T? = null)
    fun onError(error: Exception?)

}