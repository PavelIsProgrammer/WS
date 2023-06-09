package com.petrs.smartlab.domain.useCases

import com.petrs.smartlab.data.repository.SmartLabRepository
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.domain.mappers.toDomain
import com.petrs.smartlab.domain.mappers.toDomainResult
import com.petrs.smartlab.domain.models.CatalogItemDomain

class GetCatalogUseCase(private val repository: SmartLabRepository) {

    suspend operator fun invoke(): DomainResult<List<CatalogItemDomain>> =
        repository.getCatalog().toDomainResult { it.map { itemDTO -> itemDTO.toDomain() } }
}