package com.petrs.smartlab.domain.useCases

import com.petrs.smartlab.data.ErrorType
import com.petrs.smartlab.data.repository.SharedPreferencesRepository
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.domain.mappers.toDomain
import com.petrs.smartlab.domain.models.CatalogItemDomain

class GetCartUseCase(private val sharedPreferencesRepository: SharedPreferencesRepository) {

    operator fun invoke(): DomainResult<List<CatalogItemDomain>> = try {
        DomainResult.Success(sharedPreferencesRepository.getCart().map { it.toDomain() })
    } catch (e: Exception) {
        DomainResult.Error(ErrorType.REQUEST_ERROR, "Непредвиденная ошибка")
    }
}