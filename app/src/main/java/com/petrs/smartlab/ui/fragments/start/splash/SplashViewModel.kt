package com.petrs.smartlab.ui.fragments.start.splash

import androidx.lifecycle.ViewModel
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.domain.LoadingState
import com.petrs.smartlab.domain.useCases.GetOnboardingStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SplashViewModel(
    private val getOnboardingStatusUseCase: GetOnboardingStatusUseCase
) : ViewModel() {

    private val _onboardingStatus: MutableStateFlow<DomainResult<Boolean>> =
        MutableStateFlow(DomainResult.Loading(LoadingState.INITIAL))
    val onboardingStatus = _onboardingStatus.asStateFlow()

    fun checkOnboardingStatus() {
        _onboardingStatus.value = getOnboardingStatusUseCase()
    }
}