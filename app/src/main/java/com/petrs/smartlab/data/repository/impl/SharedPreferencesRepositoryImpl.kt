package com.petrs.smartlab.data.repository.impl

import com.petrs.smartlab.data.repository.SharedPreferencesRepository
import com.petrs.smartlab.data.sharedPrefs.SharedPreferencesHandler

class SharedPreferencesRepositoryImpl(private val handler: SharedPreferencesHandler) : SharedPreferencesRepository {

    override fun changeOnboardingStatus(status: Boolean) = handler.changeOnboardingStatus(status)

    override fun getOnboardingStatus(): Boolean = handler.getOnboardingStatus()
}