package com.petrs.smartlab.domain.useCases

import com.petrs.smartlab.data.repository.SharedPreferencesRepository
import com.petrs.smartlab.data.repository.SmartLabRepository
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.domain.mappers.toDomain
import com.petrs.smartlab.domain.mappers.toDomainResult
import com.petrs.smartlab.domain.models.MessageDomain
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddAudioCommentToOrderUseCase(
    private val repository: SmartLabRepository,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {

    suspend operator fun invoke(orderId: Int, audio: File): DomainResult<MessageDomain> =
        repository.addAudioMessageToOrder(
            token = sharedPreferencesRepository.getToken(),
            orderId = MultipartBody.Part.createFormData("order_id", orderId.toString()),
            audio = MultipartBody.Part.createFormData("file", audio.name, audio.asRequestBody())
        ).toDomainResult { it.toDomain() }
}