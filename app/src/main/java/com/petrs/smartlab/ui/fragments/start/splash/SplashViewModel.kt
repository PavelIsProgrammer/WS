package com.petrs.smartlab.ui.fragments.start.splash

import androidx.lifecycle.ViewModel
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.domain.LoadingState
import com.petrs.smartlab.domain.useCases.GetAppPasswordUseCase
import com.petrs.smartlab.domain.useCases.GetOnboardingStatusUseCase
import com.petrs.smartlab.domain.useCases.GetTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SplashViewModel(
    private val getOnboardingStatusUseCase: GetOnboardingStatusUseCase,
    private val getAppPasswordUseCase: GetAppPasswordUseCase,
    private val getTokenUseCase: GetTokenUseCase
) : ViewModel() {

    private val _onboardingStatus: MutableStateFlow<DomainResult<Boolean>> =
        MutableStateFlow(DomainResult.Loading(LoadingState.INITIAL))
    val onboardingStatus = _onboardingStatus.asStateFlow()

    private val _token: MutableStateFlow<DomainResult<String>> = MutableStateFlow(DomainResult.Loading(LoadingState.INITIAL))
    val token = _token.asStateFlow()

    private val _password: MutableStateFlow<DomainResult<String>> = MutableStateFlow(DomainResult.Loading(LoadingState.INITIAL))
    val password = _password.asStateFlow()

    fun checkOnboardingStatus() {
        _onboardingStatus.value = getOnboardingStatusUseCase()
    }

    fun checkPassword() {
        _password.value = getAppPasswordUseCase()
    }

    fun checkToken() {
        _token.value = getTokenUseCase()
    }
}