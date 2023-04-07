package com.petrs.smartlab.data.repository.impl

import com.petrs.smartlab.data.models.ProfileInfoDTO
import com.petrs.smartlab.data.repository.SharedPreferencesRepository
import com.petrs.smartlab.data.sharedPrefs.SharedPreferencesHandler

class SharedPreferencesRepositoryImpl(private val handler: SharedPreferencesHandler) : SharedPreferencesRepository {

    override fun changeOnboardingStatus(status: Boolean) = handler.changeOnboardingStatus(status)

    override fun getOnboardingStatus(): Boolean = handler.getOnboardingStatus()

    override fun saveToken(token: String) = handler.saveToken(token)

    override fun getToken(): String = handler.getToken()

    override fun saveAppPassword(password: String) = handler.saveAppPassword(password)

    override fun getAppPassword(): String = handler.getAppPassword()

    override fun saveProfile(profile: ProfileInfoDTO) = handler.saveProfile(profile)

    override fun getProfile(): ProfileInfoDTO = handler.getProfile()
}