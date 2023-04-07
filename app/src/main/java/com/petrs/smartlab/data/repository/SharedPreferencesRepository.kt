package com.petrs.smartlab.data.repository

import com.petrs.smartlab.data.models.ProfileInfoDTO

interface SharedPreferencesRepository {

    fun changeOnboardingStatus(status: Boolean)
    fun getOnboardingStatus(): Boolean

    fun saveToken(token: String)
    fun getToken(): String

    fun saveAppPassword(password: String)
    fun getAppPassword(): String

    fun saveProfile(profile: ProfileInfoDTO)
    fun getProfile(): ProfileInfoDTO
}