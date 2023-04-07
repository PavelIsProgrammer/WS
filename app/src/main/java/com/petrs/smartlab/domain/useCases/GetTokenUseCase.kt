package com.petrs.smartlab.domain.useCases

import com.petrs.smartlab.data.ErrorType
import com.petrs.smartlab.data.repository.SharedPreferencesRepository
import com.petrs.smartlab.domain.DomainResult

class GetTokenUseCase(private val sharedPreferencesRepository: SharedPreferencesRepository) {

    operator fun invoke(): DomainResult<String> = try {
        DomainResult.Success(sharedPreferencesRepository.getToken())
    } catch (e: Exception) {
        DomainResult.Error(ErrorType.REQUEST_ERROR, "Непредвиденная ошибка")
    }
}