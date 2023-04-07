package com.petrs.smartlab.domain.useCases

import com.petrs.smartlab.data.models.request.CreateProfileBody
import com.petrs.smartlab.data.repository.SharedPreferencesRepository
import com.petrs.smartlab.data.repository.SmartLabRepository
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.domain.mappers.toDomain
import com.petrs.smartlab.domain.mappers.toDomainResult
import com.petrs.smartlab.domain.models.ProfileInfoDomain

class CreateProfileUseCase(
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    private val repository: SmartLabRepository
) {

    suspend operator fun invoke(profileBody: CreateProfileBody): DomainResult<ProfileInfoDomain> =
        repository.createProfile(sharedPreferencesRepository.getToken(), profileBody)
            .toDomainResult { it.toDomain() }
}