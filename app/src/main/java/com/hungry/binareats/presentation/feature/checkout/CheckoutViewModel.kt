package com.hungry.binareats.presentation.feature.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hungry.binareats.data.repository.CartRepository
import com.hungry.binareats.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val repo: CartRepository
) : ViewModel() {

    val cartList = repo.getUserCartData().asLiveData(Dispatchers.IO)

    private val _checkoutResult = MutableLiveData<ResultWrapper<Boolean>>()
    val checkoutResult: LiveData<ResultWrapper<Boolean>>
        get() = _checkoutResult

    fun order() {
        viewModelScope.launch(Dispatchers.IO) {
            val carts = cartList.value?.payload?.first ?: return@launch
            repo.order(carts).collect {
                _checkoutResult.postValue(it)
            }
        }
    }

    fun clearCart(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAll()
        }
    }
}