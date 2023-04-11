package com.example.apppealolder.utils

import android.view.View
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