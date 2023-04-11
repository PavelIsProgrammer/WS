package com.petrs.smartlab.ui.fragments.main.order_register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petrs.smartlab.data.models.request.CreateOrderRequestBody
import com.petrs.smartlab.data.models.request.PatientRequestBody
import com.petrs.smartlab.data.models.request.CatalogItemRequestBody
import com.petrs.smartlab.data.models.request.CreateProfileBody
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.domain.LoadingState
import com.petrs.smartlab.domain.models.CatalogItemDomain
import com.petrs.smartlab.domain.models.CreateOrderDomain
import com.petrs.smartlab.domain.models.MessageDomain
import com.petrs.smartlab.domain.models.ProfileInfoDomain
import com.petrs.smartlab.domain.useCases.*
import com.petrs.smartlab.ui.fragments.main.order_register.models.CartItem
import com.petrs.smartlab.ui.fragments.main.order_register.models.PatientItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class OrderRegisterViewModel(
    private val getCartUseCase: GetCartUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val addAudioCommentToOrderUseCase: AddAudioCommentToOrderUseCase,
    private val createProfileUseCase: CreateProfileUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val saveCartUseCase: SaveCartUseCase
) : ViewModel() {

    private val _cart: MutableStateFlow<DomainResult<List<CatalogItemDomain>>> =
        MutableStateFlow(DomainResult.Loading(LoadingState.INITIAL))
    val cart = _cart.asStateFlow()

    private val _result: MutableLiveData<List<Pair<PatientItem, List<CartItem>>>> =
        MutableLiveData(emptyList())
    val result: LiveData<List<Pair<PatientItem, List<CartItem>>>> = _result

    private val _patient: MutableStateFlow<DomainResult<ProfileInfoDomain>> =
        MutableStateFlow(DomainResult.Loading(LoadingState.INITIAL))
    val patient = _patient.asStateFlow()

    private val _profileInfo: MutableStateFlow<DomainResult<ProfileInfoDomain>> =
        MutableStateFlow(DomainResult.Loading(LoadingState.INITIAL))
    val profileInfo = _profileInfo.asStateFlow()

    private val _saveProfile: MutableStateFlow<DomainResult<Unit>> =
        MutableStateFlow(DomainResult.Loading(LoadingState.INITIAL))
    val saveProfile = _saveProfile.asStateFlow()

    private val _createOrder: MutableStateFlow<DomainResult<CreateOrderDomain>> =
        MutableStateFlow(DomainResult.Loading(LoadingState.INITIAL))
    val createOrder = _createOrder.asStateFlow()

    private val _addAudioToOrder: MutableStateFlow<DomainResult<MessageDomain>> =
        MutableStateFlow(DomainResult.Loading(LoadingState.INITIAL))
    val addAudioToOrder = _addAudioToOrder.asStateFlow()



    private val _savedPatients: MutableStateFlow<List<ProfileInfoDomain>> = MutableStateFlow(emptyList())
    val savedPatients = _savedPatients.asStateFlow()

    var staticCart = listOf<CatalogItemDomain>()
    var staticPatients = arrayListOf<ProfileInfoDomain>()
    var selectedPatients = arrayListOf<PatientItem>()
    var staticResult = listOf<Pair<PatientItem, List<CartItem>>>()

    val orderRequestBody = CreateOrderRequestBody("", "", "", patients = emptyList())

    fun getCart() = viewModelScope.launch {
        _cart.value = getCartUseCase()
    }

    fun submitNewAnalyzes(list: List<Pair<PatientItem, List<CartItem>>>, onComplete: () -> Unit) =
        viewModelScope.launch {
            Log.d("TAG", list.toString())
            staticResult = list

            val newPatientsRequestList = arrayListOf<PatientRequestBody>()
            staticResult.forEach {
                if (it.first.selected) {
                    val newCatalogItemRequestBodyList = arrayListOf<CatalogItemRequestBody>()
                    it.second.forEach { cartItem ->
                        if (cartItem.selected) newCatalogItemRequestBodyList.add(
                            CatalogItemRequestBody(
                                id = cartItem.catalogItem.id,
                                price = cartItem.catalogItem.price
                            )
                        )
                    }
                    newPatientsRequestList.add(
                        PatientRequestBody(
                            name = it.first.profile.lastName + " " + it.first.profile.firstName,
                            items = newCatalogItemRequestBodyList
                        )
                    )
                }
            }

            orderRequestBody.patients = newPatientsRequestList

            _result.value = list
        }.invokeOnCompletion { onComplete() }

    fun getPatients() = viewModelScope.launch {
        var result = getProfileUseCase()
        if (result is DomainResult.Success) {
            Log.d("TAG", result.data.toString())
            staticPatients.clear()
            staticPatients.add(result.data)
            selectedPatients.add(PatientItem(result.data, true))

            _patient.value = result
            _savedPatients.value = staticPatients
        }
    }

    fun createProfile(
        name: String,
        lastName: String,
        midName: String,
        birthDate: String,
        sexOrientation: String
    ) = viewModelScope.launch {
        _profileInfo.value = DomainResult.Loading(LoadingState.REQUEST_LOADING)
        _profileInfo.value = createProfileUseCase(
            CreateProfileBody(
                firstName = name,
                lastName = lastName,
                midName = midName,
                birth = birthDate,
                sexOrientation = sexOrientation
            )
        )
    }

    fun saveProfile(profile: ProfileInfoDomain) = viewModelScope.launch {
        staticPatients.add(profile)
        selectedPatients.add(PatientItem(profile, false))
        _savedPatients.value = staticPatients
    }

    fun createOrder() = viewModelScope.launch {
        _createOrder.value = DomainResult.Loading(LoadingState.REQUEST_LOADING)
        _createOrder.value = createOrderUseCase(orderRequestBody)
        saveCartUseCase(emptyList())
    }

    fun addAudioMessage(orderId: Int, audioFile: File) = viewModelScope.launch {
        _addAudioToOrder.value = addAudioCommentToOrderUseCase(orderId, audioFile)
    }
}