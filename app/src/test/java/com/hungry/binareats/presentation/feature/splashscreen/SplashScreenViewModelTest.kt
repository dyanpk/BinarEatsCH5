package com.hungry.binareats.presentation.feature.splashscreen

import com.hungry.binareats.data.repository.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SplashScreenViewModelTest {

    @MockK
    private lateinit var repo: UserRepository

    private lateinit var viewModel: SplashScreenViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = SplashScreenViewModel(repo)
    }

    @Test
    fun `user not logged in`() {
        every { repo.isLoggedIn() } returns false
        val result = viewModel.isUserLoggedIn()
        verify { repo.isLoggedIn() }
        assertFalse(result)
    }

    @Test
    fun `user logged in`() {
        every { repo.isLoggedIn() } returns true
        val result = viewModel.isUserLoggedIn()
        verify { repo.isLoggedIn() }
        assertTrue(result)
    }
}
