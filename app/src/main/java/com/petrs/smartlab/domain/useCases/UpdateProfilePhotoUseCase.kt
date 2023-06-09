package com.petrs.smartlab.domain.useCases

import com.petrs.smartlab.data.repository.SharedPreferencesRepository
import com.petrs.smartlab.data.repository.SmartLabRepository
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.domain.mappers.toDomain
import com.petrs.smartlab.domain.mappers.toDomainResult
import com.petrs.smartlab.domain.models.MessageDomain
import com.petrs.smartlab.utils.getMimeType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UpdateProfilePhotoUseCase(
    private val repository: SmartLabRepository,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {

    suspend operator fun invoke(file: File): DomainResult<MessageDomain> = repository.updateProfilePhoto(
        token = sharedPreferencesRepository.getToken(),
        file = MultipartBody.Part.createFormData("file", file.name, file.asRequestBody())
    ).toDomainResult { it.toDomain() }
}