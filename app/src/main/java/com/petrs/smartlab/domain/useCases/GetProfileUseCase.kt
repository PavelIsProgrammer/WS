package com.petrs.smartlab.domain.useCases

import com.petrs.smartlab.data.ErrorType
import com.petrs.smartlab.data.repository.SharedPreferencesRepository
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.domain.mappers.toDomain
import com.petrs.smartlab.domain.models.ProfileInfoDomain

class GetProfileUseCase(private val sharedPreferencesRepository: SharedPreferencesRepository) {

    operator fun invoke(): DomainResult<ProfileInfoDomain> = try {
        DomainResult.Success(sharedPreferencesRepository.getProfile().toDomain())
    } catch (e: Exception) {
        DomainResult.Error(ErrorType.REQUEST_ERROR, "Непредвиденная ошибка")
    }
}