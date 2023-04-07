package com.petrs.smartlab.di

import com.petrs.smartlab.ui.activities.login.LoginViewModel
import com.petrs.smartlab.ui.fragments.start.onboarding.OnboardingViewModel
import com.petrs.smartlab.ui.fragments.start.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {

    viewModel {
        LoginViewModel()
    }

    viewModel {
        SplashViewModel(
            getOnboardingStatusUseCase = get()
        )
    }

    viewModel {
        OnboardingViewModel(
            changeOnboardingStatusUseCase = get()
        )
    }
}