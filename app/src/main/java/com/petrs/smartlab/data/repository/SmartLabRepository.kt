package com.petrs.smartlab.data.repository

import com.petrs.smartlab.data.DataResult
import com.petrs.smartlab.data.models.*
import com.petrs.smartlab.data.models.request.CreateProfileBody

interface SmartLabRepository {

    suspend fun sendCode(email: String): DataResult<MessageDTO>

    suspend fun signIn(email: String, code: String): DataResult<TokenDTO>

    suspend fun createProfile(token: String, profileBody: CreateProfileBody): DataResult<ProfileInfoDTO>

    suspend fun getCatalog(): DataResult<List<CatalogItemDTO>>

    suspend fun getNews(): DataResult<List<NewsItemDTO>>
}