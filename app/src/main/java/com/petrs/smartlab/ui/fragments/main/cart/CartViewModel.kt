package com.petrs.smartlab.ui.fragments.main.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.domain.LoadingState
import com.petrs.smartlab.domain.models.CatalogItemDomain
import com.petrs.smartlab.domain.useCases.GetCartUseCase
import com.petrs.smartlab.domain.useCases.SaveCartUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val getCartUseCase: GetCartUseCase,
    private val saveCartUseCase: SaveCartUseCase
) : ViewModel() {

    private val _cart: MutableStateFlow<DomainResult<List<CatalogItemDomain>>> =
        MutableStateFlow(DomainResult.Loading(LoadingState.INITIAL))
    val cart = _cart.asStateFlow()

    private val _cartSum: MutableStateFlow<Int> = MutableStateFlow(0)
    val carSum = _cartSum.asStateFlow()

    fun getCart() = viewModelScope.launch {
        _cart.value = getCartUseCase()
    }

    fun clearCart() {
        saveCartUseCase(emptyList())
        getCart()
        getCartSum()
    }

    fun addInCart(item: CatalogItemDomain) {
        val cart = getCartUseCase()
        val newCart = arrayListOf<CatalogItemDomain>()
        if (cart is DomainResult.Success) {
            if (item.inCart > 0) {
                if (cart.data.find { it.id == item.id } == null) {
                    newCart.addAll(cart.data)
                    newCart.add(item)
                } else {
                    newCart.addAll(cart.data)
                    newCart.map { if (it.id == item.id) it.inCart = item.inCart }
                }
            } else {
                newCart.addAll(cart.data.filter { it.id != item.id })
            }
        }
        saveCartUseCase(newCart)
        getCart()
        getCartSum()
    }

    fun getCartSum() = viewModelScope.launch {
        val cart = getCartUseCase()
        var sum = 0
        if (cart is DomainResult.Success) {
            cart.data.forEach {
                sum += it.price.toInt() * it.inCart
            }
        }
        _cartSum.value = sum
    }
}