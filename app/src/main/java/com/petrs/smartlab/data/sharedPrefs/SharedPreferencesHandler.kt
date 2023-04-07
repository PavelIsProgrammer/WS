package com.petrs.smartlab.data.sharedPrefs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.petrs.smartlab.data.models.ProfileInfoDTO

class SharedPreferencesHandler(context: Context) {

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    fun changeOnboardingStatus(status: Boolean) =
        sharedPrefs.edit().putBoolean(SharedPreferencesKeys.OnboardingStatus.name, status).apply()

    fun getOnboardingStatus() =
        sharedPrefs.getBoolean(SharedPreferencesKeys.OnboardingStatus.name, false)

    fun saveToken(token: String) =
        sharedPrefs.edit().putString(SharedPreferencesKeys.AuthorizationToken.name, "Bearer $token").apply()

    fun getToken() =
        sharedPrefs.getString(SharedPreferencesKeys.AuthorizationToken.name, null) ?: ""

    fun saveAppPassword(password: String) =
        sharedPrefs.edit().putString(SharedPreferencesKeys.AppPassword.name, password).apply()

    fun getAppPassword() =
        sharedPrefs.getString(SharedPreferencesKeys.AppPassword.name, null) ?: ""

    fun saveProfile(profile: ProfileInfoDTO) =
        sharedPrefs.edit().putString(SharedPreferencesKeys.Profile.name, Gson().toJson(profile)).apply()

    fun getProfile(): ProfileInfoDTO {
        var res = sharedPrefs.getString(SharedPreferencesKeys.Profile.name, null)
        return if (res != null) Gson().fromJson(res, ProfileInfoDTO::class.java) else ProfileInfoDTO()
    }

}