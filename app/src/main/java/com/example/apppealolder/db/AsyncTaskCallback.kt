package com.example.apppealolder.db

import java.lang.Exception


interface AsyncTaskCallback<T> {

    fun onSuccess(listData: T? = null)
    fun onError(error:Exception?)

}