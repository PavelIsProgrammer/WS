package com.petrs.smartlab.data.api

import com.petrs.smartlab.data.models.*
import com.petrs.smartlab.data.models.request.CreateOrderRequestBody
import com.petrs.smartlab.data.models.request.CreateProfileBody
import com.petrs.smartlab.data.models.request.UpdateProfileBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface SmartLabApi {
    @POST("/api/sendCode")
    suspend fun sendCode(
        @Header("email") email: String
    ): Response<MessageDTO>

    @POST("/api/signin")
    suspend fun signIn(
        @Header("email") email: String,
        @Header("code") code: String
    ): Response<TokenDTO>

    @POST("/api/createProfile")
    suspend fun createProfile(
        @Header("Authorization") token: String,
        @Body requestBody: CreateProfileBody
    ): Response<ProfileInfoDTO>

    @GET("/api/catalog")
    suspend fun getCatalog(): Response<List<CatalogItemDTO>>

    @GET("/api/news")
    suspend fun getNews(): Response<List<NewsItemDTO>>

    @PUT("/api/updateProfile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body requestBody: UpdateProfileBody
    ): Response<ProfileInfoDTO>

    @Multipart
    @POST("/api/avatar")
    suspend fun updateProfilePhoto(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Response<MessageDTO>

    @POST("/api/order")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Body requestBody: CreateOrderRequestBody
    ): Response<CreateOrderDTO>

    @Multipart
    @POST("/api/saveAudioComment")
    suspend fun addAudioComment(
        @Header("Authorization") token: String,
        @Part orderId: MultipartBody.Part,
        @Part audio: MultipartBody.Part
    ): Response<MessageDTO>
}