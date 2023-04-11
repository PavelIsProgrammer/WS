package com.petrs.smartlab.data.repository

import com.petrs.smartlab.data.DataResult
import com.petrs.smartlab.data.models.*
import com.petrs.smartlab.data.models.request.CreateOrderRequestBody
import com.petrs.smartlab.data.models.request.CreateProfileBody
import com.petrs.smartlab.data.models.request.UpdateProfileBody
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface SmartLabRepository {

    suspend fun sendCode(email: String): DataResult<MessageDTO>

    suspend fun signIn(email: String, code: String): DataResult<TokenDTO>

    suspend fun createProfile(token: String, profileBody: CreateProfileBody): DataResult<ProfileInfoDTO>

    suspend fun getCatalog(): DataResult<List<CatalogItemDTO>>

    suspend fun getNews(): DataResult<List<NewsItemDTO>>

    suspend fun updateProfile(token: String, profileBody: UpdateProfileBody): DataResult<ProfileInfoDTO>

    suspend fun updateProfilePhoto(token: String, file: MultipartBody.Part): DataResult<MessageDTO>

    suspend fun createOrder(token: String, orderRequestBody: CreateOrderRequestBody): DataResult<CreateOrderDTO>

    suspend fun addAudioMessageToOrder(token: String, orderId: MultipartBody.Part, audio: MultipartBody.Part): DataResult<MessageDTO>
}