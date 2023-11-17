package com.hungry.binareats.presentation.feature.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hungry.binareats.data.local.preferences.UserPreferenceDataSource
import com.hungry.binareats.data.repository.MenuRepository
import com.hungry.binareats.tools.MainCoroutineRule
import com.hungry.binareats.tools.getOrAwaitValue
import com.hungry.binareats.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class HomeViewModelTest {

    @MockK
    private lateinit var repo: MenuRepository

    @MockK
    private lateinit var userPref: UserPreferenceDataSource

    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get : Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = spyk(
            HomeViewModel(repo, userPref)
        )
        coEvery { repo.getMenus(any()) } returns flow {
            emit(
                ResultWrapper.Success(
                    listOf(
                        mockk(relaxed = true),
                        mockk(relaxed = true)

                    )
                )
            )
        }
    }

    @Test
    fun getMenus() {
        viewModel.getMenus()
        val result = viewModel.menus.getOrAwaitValue()
        assertEquals(result.payload?.size, 2)
        coVerify { repo.getMenus() }
    }
}
