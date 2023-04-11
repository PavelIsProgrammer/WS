package com.petrs.smartlab.domain.useCases

import com.petrs.smartlab.data.models.request.CreateOrderRequestBody
import com.petrs.smartlab.data.repository.SharedPreferencesRepository
import com.petrs.smartlab.data.repository.SmartLabRepository
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.domain.mappers.toDomain
import com.petrs.smartlab.domain.mappers.toDomainResult
import com.petrs.smartlab.domain.models.CreateOrderDomain

class CreateOrderUseCase(
    private val repository: SmartLabRepository,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {
    suspend operator fun invoke(orderRequestBody: CreateOrderRequestBody): DomainResult<CreateOrderDomain> =
        repository.createOrder(sharedPreferencesRepository.getToken(), orderRequestBody)
            .toDomainResult { it.toDomain() }
}
