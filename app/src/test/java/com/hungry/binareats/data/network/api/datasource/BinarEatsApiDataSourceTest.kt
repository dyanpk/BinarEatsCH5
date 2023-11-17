package com.hungry.binareats.data.network.api.datasource

import com.hungry.binareats.data.network.api.model.category.CategoriesResponse
import com.hungry.binareats.data.network.api.model.menu.MenusResponse
import com.hungry.binareats.data.network.api.model.order.OrderRequest
import com.hungry.binareats.data.network.api.model.order.OrderResponse
import com.hungry.binareats.data.network.api.service.BinarEatsApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BinarEatsApiDataSourceTest {

    @MockK
    private lateinit var service: BinarEatsApiService

    private lateinit var dataSource: BinarEatsDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = BinarEatsApiDataSource(service)
    }

    @Test
    fun getMenus() {
        runTest {
            val mockResponse = mockk<MenusResponse>(relaxed = true)
            coEvery { service.getMenus(any()) } returns mockResponse
            val response = dataSource.getMenus("Nasi goreng")
            coVerify { service.getMenus(any()) }
            assertEquals(response, mockResponse)
        }
    }

    @Test
    fun getCategories() {
        runTest {
            val mockResponse = mockk<CategoriesResponse>(relaxed = true)
            coEvery { service.getCategories() } returns mockResponse
            val response = dataSource.getCategories()
            coVerify { service.getCategories() }
            assertEquals(response, mockResponse)
        }
    }

    @Test
    fun createOrder() {
        runTest {
            val mockRequest = mockk<OrderRequest>(relaxed = true)
            val mockResponse = mockk<OrderResponse>(relaxed = true)
            coEvery { service.createOrder(any()) } returns mockResponse
            val response = dataSource.createOrder(mockRequest)
            coVerify { service.createOrder(any()) }
            assertEquals(response, mockResponse)
        }
    }
}
