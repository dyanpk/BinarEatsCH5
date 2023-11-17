package com.hungry.binareats.data.repository

import android.net.Uri
import app.cash.turbine.test
import com.google.firebase.auth.FirebaseUser
import com.hungry.binareats.data.network.api.model.user.User
import com.hungry.binareats.data.network.firebase.auth.FirebaseAuthDataSource
import com.hungry.binareats.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    @MockK
    lateinit var firebaseAuthDataSource: FirebaseAuthDataSource

    private lateinit var repo: UserRepository

    private val fullName = "fullName"
    private val photoUri = mockk<Uri>()
    private val newPassword = "newPassword"
    private val newEmail = "newEmail"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repo = UserRepositoryImpl(firebaseAuthDataSource)
    }

    @Test
    fun `doLogin loading`() {
        val email = "email"
        val password = "password"
        coEvery { firebaseAuthDataSource.doLogin(any(), any()) } returns true
        runTest {
            repo.doLogin(email, password)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(110)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Loading)
                    coVerify { firebaseAuthDataSource.doLogin(any(), any()) }
                }
        }
    }

    @Test
    fun `doLogin success`() {
        val email = "email"
        val password = "password"
        coEvery { firebaseAuthDataSource.doLogin(any(), any()) } returns true
        runTest {
            repo.doLogin(email, password)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Success)
                    coVerify { firebaseAuthDataSource.doLogin(any(), any()) }
                }
        }
    }

    @Test
    fun `doLogin error`() {
        val email = "email"
        val password = "password"
        coEvery { firebaseAuthDataSource.doLogin(any(), any()) } throws IllegalStateException("Mock error")
        runTest {
            repo.doLogin(email, password)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Error)
                    coVerify { firebaseAuthDataSource.doLogin(any(), any()) }
                }
        }
    }

    @Test
    fun `doRegister loading`() {
        val name = "name"
        val email = "email"
        val password = "password"
        coEvery { firebaseAuthDataSource.doRegister(any(), any(), any()) } returns true
        runTest {
            repo.doRegister(name, email, password)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(110)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Loading)
                    coVerify { firebaseAuthDataSource.doRegister(any(), any(), any()) }
                }
        }
    }

    @Test
    fun `doRegister success`() {
        val name = "name"
        val email = "email"
        val password = "password"
        coEvery { firebaseAuthDataSource.doRegister(any(), any(), any()) } returns true
        runTest {
            repo.doRegister(name, email, password)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Success)
                    coVerify { firebaseAuthDataSource.doRegister(any(), any(), any()) }
                }
        }
    }

    @Test
    fun `doRegister error`() {
        val name = "name"
        val email = "email"
        val password = "password"
        coEvery { firebaseAuthDataSource.doRegister(any(), any(), any()) } throws IllegalStateException("Mock error")
        runTest {
            repo.doRegister(name, email, password)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Error)
                    coVerify { firebaseAuthDataSource.doRegister(any(), any(), any()) }
                }
        }
    }

    @Test
    fun doLogout() {
        every { firebaseAuthDataSource.doLogout() } returns true
        val result = repo.doLogout()
        assertTrue(result)
        verify { firebaseAuthDataSource.doLogout() }
    }

    @Test
    fun isLoggedIn() {
        every { firebaseAuthDataSource.isLoggedIn() } returns true
        val result = repo.isLoggedIn()
        assertTrue(result)
        verify { firebaseAuthDataSource.isLoggedIn() }
    }

    @Test
    fun getCurrentUser() {
        val mockUser = User(
            "fullname",
            "photourl",
            "email"
        )
        val user: FirebaseUser = mockk()
        every { firebaseAuthDataSource.getCurrentUser() } returns user

        every { user.displayName } returns mockUser.fullName
        every { user.email } returns mockUser.email
        every { user.photoUrl.toString() } returns mockUser.photoUrl

        val result = repo.getCurrentUser()
        verify { firebaseAuthDataSource.getCurrentUser() }
        assertEquals(mockUser, result)
    }

    @Test
    fun `updateProfile loading`() {
        coEvery { firebaseAuthDataSource.updateProfile(any(), any()) } returns true
        runTest {
            repo.updateProfile(fullName, photoUri)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(110)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Loading)
                    coVerify { firebaseAuthDataSource.updateProfile(any(), any()) }
                }
        }
    }

    @Test
    fun `updateProfile success`() {
        coEvery { firebaseAuthDataSource.updateProfile(any(), any()) } returns true
        runTest {
            repo.updateProfile(fullName, photoUri)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Success)
                    coVerify { firebaseAuthDataSource.updateProfile(any(), any()) }
                }
        }
    }

    @Test
    fun `updateProfile error`() {
        coEvery { firebaseAuthDataSource.updateProfile(any(), any()) } throws IllegalStateException("Mock error")
        runTest {
            repo.updateProfile(fullName, photoUri)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Error)
                    coVerify { firebaseAuthDataSource.updateProfile(any(), any()) }
                }
        }
    }

    @Test
    fun `updatePassword loading`() {
        coEvery { firebaseAuthDataSource.updatePassword(any()) } returns true
        runTest {
            repo.updatePassword(newPassword)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(110)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Loading)
                    coVerify { firebaseAuthDataSource.updatePassword(any()) }
                }
        }
    }

    @Test
    fun `updatePassword success`() {
        coEvery { firebaseAuthDataSource.updatePassword(any()) } returns true
        runTest {
            repo.updatePassword(newPassword)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Success)
                    coVerify { firebaseAuthDataSource.updatePassword(any()) }
                }
        }
    }

    @Test
    fun `updatePassword error`() {
        coEvery { firebaseAuthDataSource.updatePassword(any()) } throws IllegalStateException("Mock error")
        runTest {
            repo.updatePassword(newPassword)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Error)
                    coVerify { firebaseAuthDataSource.updatePassword(any()) }
                }
        }
    }

    @Test
    fun `updateEmail loading`() {
        coEvery { firebaseAuthDataSource.updateEmail(any()) } returns true
        runTest {
            repo.updateEmail(newEmail)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(110)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Loading)
                    coVerify { firebaseAuthDataSource.updateEmail(any()) }
                }
        }
    }

    @Test
    fun `updateEmail success`() {
        coEvery { firebaseAuthDataSource.updateEmail(any()) } returns true
        runTest {
            repo.updateEmail(newEmail)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Success)
                    coVerify { firebaseAuthDataSource.updateEmail(any()) }
                }
        }
    }

    @Test
    fun `updateEmail error`() {
        coEvery { firebaseAuthDataSource.updateEmail(any()) } throws IllegalStateException("Mock error")
        runTest {
            repo.updateEmail(newEmail)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Error)
                    coVerify { firebaseAuthDataSource.updateEmail(any()) }
                }
        }
    }

    @Test
    fun sendChangePasswordRequestByEmail() {
        coEvery { firebaseAuthDataSource.sendChangePasswordRequestByEmail() } returns true
        val result = repo.sendChangePasswordRequestByEmail()
        assertTrue(result)
        coVerify { firebaseAuthDataSource.sendChangePasswordRequestByEmail() }
    }
}
