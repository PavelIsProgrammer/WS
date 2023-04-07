package com.petrs.smartlab.domain.useCases

import com.petrs.smartlab.data.ErrorType
import com.petrs.smartlab.data.repository.SharedPreferencesRepository
import com.petrs.smartlab.domain.DomainResult

class SaveAppPasswordUseCase(private val sharedPreferencesRepository: SharedPreferencesRepository) {

    operator fun invoke(password: String): DomainResult<Unit> = try {
        DomainResult.Success(sharedPreferencesRepository.saveAppPassword(password))
    } catch (e: Exception) {
        DomainResult.Error(ErrorType.REQUEST_ERROR, "Непредвиденная ошибка")
    }
}