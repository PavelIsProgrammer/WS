package com.petrs.smartlab.data.sharedPrefs

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHandler(context: Context) {

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    fun changeOnboardingStatus(status: Boolean) =
        sharedPrefs.edit().putBoolean(SharedPreferencesKeys.OnboardingStatus.name, status).apply()

    fun getOnboardingStatus() =
        sharedPrefs.getBoolean(SharedPreferencesKeys.OnboardingStatus.name, false)
}