package com.example.travelorder.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class PreferencesUtil(private val context: Context) {

    private fun getEditableSharedPreferences(): SharedPreferences {
       return context.getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE)
    }

    fun saveString(keyValue: String, text: String){
        getEditableSharedPreferences().edit().putString(keyValue, text).apply()
    }

    fun saveInt(keyValue: String, id: Int?){
        id?.let {
            getEditableSharedPreferences().edit().putInt(keyValue, id).apply()
        }
    }

    fun getString(keyValue: String): String?{
        return getEditableSharedPreferences().getString(keyValue, "")
    }

    fun getInt(keyValue: String): Int{
        return getEditableSharedPreferences().getInt(keyValue, -1)
    }
}