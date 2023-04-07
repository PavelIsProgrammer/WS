package com.petrs.smartlab.di

import com.petrs.smartlab.domain.useCases.*
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

    factory {
        SendCodeUseCase(
            repository = get()
        )
    }

    factory {
        SignInUseCase(
            repository = get()
        )
    }

    factory {
        CreateProfileUseCase(
            sharedPreferencesRepository = get(),
            repository = get()
        )
    }

    factory {
        GetTokenUseCase(
            sharedPreferencesRepository = get()
        )
    }

    factory {
        SaveTokenUseCase(
            sharedPreferencesRepository = get()
        )
    }

    factory {
        SaveAppPasswordUseCase(
            sharedPreferencesRepository = get()
        )
    }

    factory {
        GetAppPasswordUseCase(
            sharedPreferencesRepository = get()
        )
    }

    factory {
        SaveProfileUseCase(
            sharedPreferencesRepository = get()
        )
    }

    factory {
        GetProfileUseCase(
            sharedPreferencesRepository = get()
        )
    }
}