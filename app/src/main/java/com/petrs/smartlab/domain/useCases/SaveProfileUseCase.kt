package com.petrs.smartlab.domain.useCases

import com.petrs.smartlab.data.ErrorType
import com.petrs.smartlab.data.repository.SharedPreferencesRepository
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.domain.mappers.toDTO
import com.petrs.smartlab.domain.models.ProfileInfoDomain

class SaveProfileUseCase(private val sharedPreferencesRepository: SharedPreferencesRepository) {

    operator fun invoke(profile: ProfileInfoDomain): DomainResult<Unit> = try {
        DomainResult.Success(sharedPreferencesRepository.saveProfile(profile.toDTO()))
    } catch (e: Exception) {
        DomainResult.Error(ErrorType.REQUEST_ERROR, "Непредвиденная ошибка")
    }
}