package com.petrs.smartlab.data.repository

interface SharedPreferencesRepository {

    fun changeOnboardingStatus(status: Boolean)
    fun getOnboardingStatus(): Boolean
}