package com.petrs.smartlab.di

import com.petrs.smartlab.domain.useCases.ChangeOnboardingStatusUseCase
import com.petrs.smartlab.domain.useCases.GetOnboardingStatusUseCase
import org.koin.dsl.module

val domainModule = module {

    factory {
        ChangeOnboardingStatusUseCase(
            sharedPreferencesRepository = get()
        )
    }

    factory {
        GetOnboardingStatusUseCase(
            sharedPreferencesRepository = get()
        )
    }
}