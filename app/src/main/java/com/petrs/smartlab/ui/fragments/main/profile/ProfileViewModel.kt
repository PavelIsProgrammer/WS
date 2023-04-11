package com.petrs.smartlab.ui.fragments.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petrs.smartlab.data.models.request.UpdateProfileBody
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.domain.LoadingState
import com.petrs.smartlab.domain.models.MessageDomain
import com.petrs.smartlab.domain.models.ProfileInfoDomain
import com.petrs.smartlab.domain.useCases.GetProfileUseCase
import com.petrs.smartlab.domain.useCases.SaveProfileUseCase
import com.petrs.smartlab.domain.useCases.UpdateProfilePhotoUseCase
import com.petrs.smartlab.domain.useCases.UpdateProfileUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    val saveProfileUseCase: SaveProfileUseCase,
    private val updateProfilePhotoUseCase: UpdateProfilePhotoUseCase
) : ViewModel() {

    private val _profile: MutableStateFlow<DomainResult<ProfileInfoDomain>> =
        MutableStateFlow(DomainResult.Loading(LoadingState.INITIAL))
    val profile = _profile.asStateFlow()

    private val _avatar: MutableStateFlow<DomainResult<MessageDomain>> = MutableStateFlow(DomainResult.Loading(LoadingState.INITIAL))
    val avatar = _avatar.asStateFlow()

    fun getProfile() {
        _profile.value = DomainResult.Loading(LoadingState.REQUEST_LOADING)
        _profile.value = getProfileUseCase()
    }

    fun updateProfile(
        name: String,
        lastName: String,
        midName: String,
        birthDate: String,
        sexOrientation: String
    ) = viewModelScope.launch {
        val state = profile.value
        if (state is DomainResult.Success) {
            _profile.value = updateProfileUseCase(
                UpdateProfileBody(
                    id = state.data.id,
                    firstName = name,
                    lastName = lastName,
                    midName = midName,
                    birth = birthDate,
                    sexOrientation = sexOrientation
                )
            )
        }
    }

    fun saveProfile(profile: ProfileInfoDomain) = viewModelScope.launch {
        saveProfileUseCase(profile)
        getProfile()
    }

    fun updateProfilePhoto(path: String) = viewModelScope.launch {
        _avatar.value = DomainResult.Loading(LoadingState.REQUEST_LOADING)
        _avatar.value = updateProfilePhotoUseCase(File(path))
    }
}