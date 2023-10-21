package com.hungry.binareats.data.repository


import com.hungry.binareats.data.network.api.datasource.BinarEatsDataSource
import com.hungry.binareats.data.network.api.model.category.toCategoryList
import com.hungry.binareats.data.network.api.model.menu.toMenuList
import com.hungry.binareats.model.Category
import com.hungry.binareats.model.Menu
import com.hungry.binareats.utils.ResultWrapper
import com.hungry.binareats.utils.proceedFlow
import kotlinx.coroutines.flow.Flow


interface MenuRepository {
    suspend fun getCategories(): Flow<ResultWrapper<List<Category>>>
    suspend fun getMenus(category: String? = null): Flow<ResultWrapper<List<Menu>>>
}

class MenuRepositoryImpl(
    private val apiDataSource: BinarEatsDataSource,
) :MenuRepository {

    override suspend fun getCategories(): Flow<ResultWrapper<List<Category>>> {
        return proceedFlow {
            apiDataSource.getCategories().data?.toCategoryList() ?: emptyList()
        }
    }

    override suspend fun getMenus(category: String?): Flow<ResultWrapper<List<Menu>>> {
        return proceedFlow {
            apiDataSource.getMenus(category).data?.toMenuList() ?: emptyList()
        }
    }


}