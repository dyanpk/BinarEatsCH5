package com.hungry.binareats.presentation.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hungry.binareats.data.local.preferences.UserPreferenceDataSource
import com.hungry.binareats.data.repository.MenuRepository
import com.hungry.binareats.model.Category
import com.hungry.binareats.model.Menu
import com.hungry.binareats.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: MenuRepository,
    private val userPref: UserPreferenceDataSource
) : ViewModel() {

    private val _categories = MutableLiveData<ResultWrapper<List<Category>>>()
    val categories : LiveData<ResultWrapper<List<Category>>>
        get() = _categories

    private val _menus = MutableLiveData<ResultWrapper<List<Menu>>>()
    val menus : LiveData<ResultWrapper<List<Menu>>>
        get() = _menus


    fun getCategories(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCategories().collect{
                _categories.postValue(it)
            }
        }
    }

    fun getMenus(category: String? = null){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getMenus(if(category == "all") null else category).collect{
                _menus.postValue(it)
            }
        }
    }

    fun userLayoutModeLiveData() = userPref.getUserLayoutPrefFlow().asLiveData(Dispatchers.IO)

    fun setUserListViewMode(isLinearMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userPref.setUserLayoutPref(isLinearMode)
        }
    }

}
