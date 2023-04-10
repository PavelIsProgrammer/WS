package com.petrs.smartlab.domain.useCases

import com.petrs.smartlab.data.models.request.UpdateProfileBody
import com.petrs.smartlab.data.repository.SharedPreferencesRepository
import com.petrs.smartlab.data.repository.SmartLabRepository
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.domain.mappers.toDomain
import com.petrs.smartlab.domain.mappers.toDomainResult
import com.petrs.smartlab.domain.models.ProfileInfoDomain

class UpdateProfileUseCase(
    private val repository: SmartLabRepository,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {

    suspend operator fun invoke(profileBody: UpdateProfileBody): DomainResult<ProfileInfoDomain> =
        repository.updateProfile(sharedPreferencesRepository.getToken(), profileBody)
            .toDomainResult { it.toDomain() }
}