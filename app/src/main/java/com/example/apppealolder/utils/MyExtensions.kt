package com.example.apppealolder.utils

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.snackbar.Snackbar


fun View.showSnackbar(errorMessage: String) {
    Snackbar.make(this, errorMessage, Snackbar.LENGTH_SHORT).show()
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.unVisible() {
    this.visibility = View.GONE
}

fun Activity.switchOnNight() {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
}

fun Activity.switchOnLight() {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
}

