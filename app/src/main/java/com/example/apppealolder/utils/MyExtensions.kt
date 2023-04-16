package com.example.apppealolder.utils

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.snackbar.Snackbar


fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
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

fun View.clickable() {
    this.isClickable = true
}

fun View.disable() {
    this.isClickable = false
}
