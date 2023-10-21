package com.hungry.binareats.data.network.api.datasource

import com.hungry.binareats.data.network.api.model.category.CategoriesResponse
import com.hungry.binareats.data.network.api.model.menu.MenusResponse
import com.hungry.binareats.data.network.api.model.order.OrderRequest
import com.hungry.binareats.data.network.api.model.order.OrderResponse
import com.hungry.binareats.data.network.api.service.BinarEatsApiService

interface BinarEatsDataSource {
    suspend fun getMenus(category: String? = null): MenusResponse
    suspend fun getCategories(): CategoriesResponse
    suspend fun createOrder(orderRequest: OrderRequest): OrderResponse
}

class BinarEatsApiDataSource(
    private val service: BinarEatsApiService
) : BinarEatsDataSource{
    override suspend fun getMenus(category: String?): MenusResponse {
        return service.getMenus(category)
    }

    override suspend fun getCategories(): CategoriesResponse {
        return service.getCategories()
    }

    override suspend fun createOrder(orderRequest: OrderRequest): OrderResponse {
        return service.createOrder(orderRequest)
    }

}