package com.hungry.binareats.data.repository

import app.cash.turbine.test
import com.hungry.binareats.data.network.api.datasource.BinarEatsDataSource
import com.hungry.binareats.data.network.api.model.category.CategoriesResponse
import com.hungry.binareats.data.network.api.model.category.CategoryResponse
import com.hungry.binareats.data.network.api.model.menu.MenuItemResponse
import com.hungry.binareats.data.network.api.model.menu.MenusResponse
import com.hungry.binareats.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MenuRepositoryImplTest {

    @MockK
    lateinit var dataSource: BinarEatsDataSource

    private lateinit var repo: MenuRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repo = MenuRepositoryImpl(dataSource)
    }

    @Test
    fun `get categories, with result loading`() {
        val mockCategoryResponse = mockk<CategoriesResponse>()
        runTest {
            coEvery { dataSource.getCategories() } returns mockCategoryResponse
            repo.getCategories().map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get categories, with result success`() {
        val fakeCategoryResponse = CategoryResponse(
            id = 1,
            imgUrlCategory = "nasi.png",
            nameOfCategory = "Nasi",
            slug = "nasi"
        )
        val fakeCategoriesResponse = CategoriesResponse(
            code = 200,
            status = true,
            message = "Success",
            data = listOf(fakeCategoryResponse)
        )
        runTest {
            coEvery { dataSource.getCategories() } returns fakeCategoriesResponse
            repo.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                assertEquals(data.payload?.get(0)?.id, 1)
                coVerify { dataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get categories, with result empty`() {
        val fakeCategoriesResponse = CategoriesResponse(
            code = 200,
            status = true,
            message = "Success",
            data = emptyList()
        )
        runTest {
            coEvery { dataSource.getCategories() } returns fakeCategoriesResponse
            repo.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { dataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get categories, with result error`() {
        runTest {
            coEvery { dataSource.getCategories() } throws IllegalStateException("Mock error")
            repo.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get menus, with result loading`() {
        val mockMenuResponse = mockk<MenusResponse>()
        runTest {
            coEvery { dataSource.getMenus(any()) } returns mockMenuResponse
            repo.getMenus("nasi goreng").map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.getMenus(any()) }
            }
        }
    }

    @Test
    fun `get menus, with result success`() {
        val fakeMenuItemResponse = MenuItemResponse(
            id = 1,
            name = "nasi goreng",
            productImgUrl = "nasigoreng.png",
            price = 15000.0,
            locationName = "Jl. BSD",
            descOfMenu = "nasi goreng enak",
            locationUrl = "jlbsd.com"
        )
        val fakeMenusResponse = MenusResponse(
            code = 200,
            status = true,
            message = "Success",
            data = listOf(fakeMenuItemResponse)
        )
        runTest {
            coEvery { dataSource.getMenus(any()) } returns fakeMenusResponse
            repo.getMenus("nasi goreng").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                assertEquals(data.payload?.get(0)?.id, 1)
                coVerify { dataSource.getMenus(any()) }
            }
        }
    }

    @Test
    fun `get menus, with result empty`() {
        val fakeMenusResponse = MenusResponse(
            code = 200,
            status = true,
            message = "Success",
            data = emptyList()
        )
        runTest {
            coEvery { dataSource.getMenus(any()) } returns fakeMenusResponse
            repo.getMenus().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { dataSource.getMenus(any()) }
            }
        }
    }

    @Test
    fun `get menus, with result error`() {
        runTest {
            coEvery { dataSource.getMenus(any()) } throws IllegalStateException("Mock error")
            repo.getMenus("nasi goreng").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.getMenus(any()) }
            }
        }
    }
}
