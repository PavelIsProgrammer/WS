package com.petrs.smartlab.domain.useCases

import com.petrs.smartlab.data.ErrorType
import com.petrs.smartlab.data.repository.SharedPreferencesRepository
import com.petrs.smartlab.domain.DomainResult

class SaveTokenUseCase(private val sharedPreferencesRepository: SharedPreferencesRepository) {

    operator fun invoke(token: String) = try {
        DomainResult.Success(sharedPreferencesRepository.saveToken(token))
    } catch (e: Exception) {
        DomainResult.Error(ErrorType.REQUEST_ERROR, "Непредвиденная ошибка")
    }
}