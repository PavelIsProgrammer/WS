package com.petrs.smartlab.data.repository.impl

import android.util.Log
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.petrs.smartlab.data.DataResult
import com.petrs.smartlab.data.ErrorType
import com.petrs.smartlab.data.api.SmartLabApi
import com.petrs.smartlab.data.models.ApiError
import com.petrs.smartlab.data.models.CreateOrderDTO
import com.petrs.smartlab.data.models.MessageDTO
import com.petrs.smartlab.data.models.request.CreateOrderRequestBody
import com.petrs.smartlab.data.models.request.CreateProfileBody
import com.petrs.smartlab.data.models.request.UpdateProfileBody
import com.petrs.smartlab.data.repository.SmartLabRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.IOException

class SmartLabRepositoryImpl(private val api: SmartLabApi) : SmartLabRepository {

    private suspend inline fun <reified T> execRequest(
        request: () -> Response<T>
    ): DataResult<T> {
        return try {
            var response = request()

            if (response.isSuccessful)
                DataResult.Success(response.body() as T)
            else
                DataResult.Error(ErrorType.REQUEST_ERROR, getError(response))
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.i("info", it) }
            when (e) {
                is IOException -> DataResult.Error(ErrorType.NO_INTERNET)
                else -> DataResult.Error(ErrorType.REQUEST_ERROR)
            }
        }
    }

    private fun getError(response: Response<*>): String {
        val adapter: TypeAdapter<ApiError> = Gson().getAdapter(ApiError::class.java)
        return adapter.fromJson(response.errorBody()?.string()).errors
    }

    override suspend fun sendCode(email: String) = execRequest { api.sendCode(email) }

    override suspend fun signIn(email: String, code: String) = execRequest { api.signIn(email, code) }

    override suspend fun createProfile(token: String, profileBody: CreateProfileBody) = execRequest {
        api.createProfile(token, profileBody)
    }

    override suspend fun getCatalog() = execRequest { api.getCatalog() }

    override suspend fun getNews() = execRequest { api.getNews() }

    override suspend fun updateProfile(
        token: String,
        profileBody: UpdateProfileBody
    ) = execRequest { api.updateProfile(token, profileBody) }

    override suspend fun updateProfilePhoto(
        token: String,
        file: MultipartBody.Part
    ): DataResult<MessageDTO> = execRequest {
        api.updateProfilePhoto(token = token, file = file)
    }

    override suspend fun createOrder(
        token: String,
        orderRequestBody: CreateOrderRequestBody
    ): DataResult<CreateOrderDTO> = execRequest { api.createOrder(token, orderRequestBody) }

    override suspend fun addAudioMessageToOrder(
        token: String,
        orderId: MultipartBody.Part,
        audio: MultipartBody.Part
    ): DataResult<MessageDTO> = execRequest {
        api.addAudioComment(token, orderId, audio)
    }
}