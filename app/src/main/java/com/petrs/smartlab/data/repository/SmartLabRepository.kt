package com.petrs.smartlab.data.repository

import com.petrs.smartlab.data.DataResult
import com.petrs.smartlab.data.models.MessageDTO
import com.petrs.smartlab.data.models.ProfileInfoDTO
import com.petrs.smartlab.data.models.TokenDTO
import com.petrs.smartlab.data.models.request.CreateProfileBody

interface SmartLabRepository {

    suspend fun sendCode(email: String): DataResult<MessageDTO>

    suspend fun signIn(email: String, code: String): DataResult<TokenDTO>

    suspend fun createProfile(token: String, profileBody: CreateProfileBody): DataResult<ProfileInfoDTO>
}