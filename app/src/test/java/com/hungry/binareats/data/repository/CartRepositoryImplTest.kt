package com.hungry.binareats.data.repository

import app.cash.turbine.test
import com.hungry.binareats.data.local.database.datasource.CartDataSource
import com.hungry.binareats.data.local.database.entity.CartEntity
import com.hungry.binareats.data.network.api.datasource.BinarEatsDataSource
import com.hungry.binareats.data.network.api.model.order.OrderResponse
import com.hungry.binareats.model.Cart
import com.hungry.binareats.model.Menu
import com.hungry.binareats.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CartRepositoryImplTest {

    @MockK
    lateinit var cartDataSource: CartDataSource

    @MockK
    lateinit var binarEatsDataSource: BinarEatsDataSource

    private lateinit var repo: CartRepository

    private val fakeCartList = listOf(
        CartEntity(
            id = 1,
            menuId = 1,
            nameOfMenu = "nasi goreng",
            priceOfMenu = 15000.0,
            imgUrlMenu = "nasigoreng.png",
            itemQuantity = 3,
            itemNotes = "pedes"
        ),
        CartEntity(
            id = 2,
            menuId = 2,
            nameOfMenu = "nasi liwet",
            priceOfMenu = 10000.0,
            imgUrlMenu = "nasigoreng.png",
            itemQuantity = 1,
            itemNotes = "pedes"
        )
    )

    private val mockCart = Cart(
        id = 1,
        menuId = 1,
        nameOfMenu = "nasi goreng",
        priceOfMenu = 15000.0,
        imgUrlMenu = "nasigoreng.png",
        itemQuantity = 3,
        itemNotes = "pedes"
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repo = CartRepositoryImpl(cartDataSource, binarEatsDataSource)
    }

    @Test
    fun `get user cart data, result loading`() {
        every { cartDataSource.getAllCarts() } returns flow {
            emit(fakeCartList)
        }
        runTest {
            repo.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2101)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                verify { cartDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `get user cart data, result success`() {
        every { cartDataSource.getAllCarts() } returns flow {
            emit(fakeCartList)
        }
        runTest {
            repo.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.first?.size, 2)
                assertEquals(data.payload?.second, 55000.0)
                verify { cartDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `get user cart data, result empty`() {
        every { cartDataSource.getAllCarts() } returns flow {
            emit(listOf())
        }
        runTest {
            repo.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                verify { cartDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `create cart loading`() {
        runTest {
            val mockProduct = mockk<Menu>(relaxed = true)
            coEvery { cartDataSource.insertCart(any()) } returns 1
            repo.createCart(mockProduct, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(110)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Loading)
                    coVerify { cartDataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun `create cart success`() {
        runTest {
            val mockProduct = mockk<Menu>(relaxed = true)
            coEvery { cartDataSource.insertCart(any()) } returns 1
            repo.createCart(mockProduct, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Success)
                    assertEquals(result.payload, true)
                    coVerify { cartDataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun `create cart error`() {
        runTest {
            val mockProduct = mockk<Menu>(relaxed = true)
            coEvery { cartDataSource.insertCart(any()) } throws IllegalStateException("Mock Error")
            repo.createCart(mockProduct, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Error)
                    coVerify { cartDataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun `decrease cart when quantity less than or equal 0`() {
        coEvery { cartDataSource.deleteCart(any()) } returns 1
        coEvery { cartDataSource.updateCart(any()) } returns 1
        runTest {
            repo.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 0) { cartDataSource.deleteCart(any()) }
                coVerify(atLeast = 1) { cartDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `decrease cart when quantity more than 0`() {
        coEvery { cartDataSource.deleteCart(any()) } returns 1
        coEvery { cartDataSource.updateCart(any()) } returns 1
        runTest {
            repo.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 0) { cartDataSource.deleteCart(any()) }
                coVerify(atLeast = 1) { cartDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `increase cart`() {
        coEvery { cartDataSource.updateCart(any()) } returns 1
        runTest {
            repo.increaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { cartDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `set cart notes`() {
        coEvery { cartDataSource.updateCart(any()) } returns 1
        runTest {
            repo.setCartNotes(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { cartDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `delete cart`() {
        coEvery { cartDataSource.deleteCart(any()) } returns 1
        runTest {
            repo.deleteCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { cartDataSource.deleteCart(any()) }
            }
        }
    }

    @Test
    fun `test order`() {
        runTest {
            val mockCarts = listOf(
                Cart(
                    id = 1,
                    menuId = 1,
                    nameOfMenu = "nasi goreng",
                    priceOfMenu = 15000.0,
                    imgUrlMenu = "nasigoreng.png",
                    itemQuantity = 3,
                    itemNotes = "pedes"
                ),
                Cart(
                    id = 2,
                    menuId = 2,
                    nameOfMenu = "nasi liwet",
                    priceOfMenu = 10000.0,
                    imgUrlMenu = "nasiliwet.png",
                    itemQuantity = 1,
                    itemNotes = "pedes"
                )
            )
            coEvery { binarEatsDataSource.createOrder(any()) } returns OrderResponse(
                code = 200,
                message = "Success",
                status = true
            )
            repo.order(mockCarts).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertTrue(result is ResultWrapper.Success)
            }
        }
    }

    @Test
    fun deleteAll() {
        coEvery { cartDataSource.deleteAll() } returns Unit
        runTest {
            val result = repo.deleteAll()
            coVerify { cartDataSource.deleteAll() }
            assertEquals(result, Unit)
        }
    }
}
