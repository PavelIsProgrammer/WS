package com.petrs.smartlab.di

import com.petrs.smartlab.ui.activities.login.LoginViewModel
import com.petrs.smartlab.ui.fragments.start.client_card.CreateClientCardViewModel
import com.petrs.smartlab.ui.fragments.start.create_password.CreatePasswordViewModel
import com.petrs.smartlab.ui.fragments.start.email_code.EmailCodeViewModel
import com.petrs.smartlab.ui.fragments.start.onboarding.OnboardingViewModel
import com.petrs.smartlab.ui.fragments.start.sign_in.SignInViewModel
import com.petrs.smartlab.ui.fragments.start.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {

    viewModel {
        LoginViewModel()
    }

    viewModel {
        SplashViewModel(
            getOnboardingStatusUseCase = get(),
            getTokenUseCase = get(),
            getAppPasswordUseCase = get()
        )
    }

    viewModel {
        OnboardingViewModel(
            changeOnboardingStatusUseCase = get()
        )
    }

    viewModel {
        SignInViewModel(
            sendCode = get()
        )
    }

    viewModel {
        EmailCodeViewModel(
            sendCodeUseCase = get(),
            signInUseCase = get(),
            saveTokenUseCase = get()
        )
    }

    viewModel {
        CreatePasswordViewModel(
            getProfileUseCase = get(),
            savePasswordUseCase = get()
        )
    }

    viewModel {
        CreateClientCardViewModel(
            createProfileUseCase = get(),
            saveProfileUseCase = get()
        )
    }
}