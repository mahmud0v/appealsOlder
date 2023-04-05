package com.example.apppealolder.db

import com.example.apppealolder.model.AppealInfo

interface AsyncTaskCallback {

    fun onSuccess(listData:ArrayList<AppealInfo>)
    fun onError(error:String)
    fun onLoading()

}