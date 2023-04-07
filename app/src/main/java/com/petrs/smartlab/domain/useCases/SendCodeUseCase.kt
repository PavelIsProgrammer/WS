package com.petrs.smartlab.domain.useCases

import com.petrs.smartlab.data.repository.SmartLabRepository
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.domain.mappers.toDomain
import com.petrs.smartlab.domain.mappers.toDomainResult
import com.petrs.smartlab.domain.models.MessageDomain

class SendCodeUseCase(private val repository: SmartLabRepository) {

    suspend operator fun invoke(email: String): DomainResult<MessageDomain> =
        repository.sendCode(email).toDomainResult { it.toDomain() }
}