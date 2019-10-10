package com.example.travelorder.utils

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar

class SnackBarUtil {
    fun makeErrorSnackBar(view: View, message: String){
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(Color.RED)
        snackbar.show()
    }

    fun makeSuccsessSnackBar(view: View, message: String){
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(Color.GREEN)
        snackbar.show()
    }
}