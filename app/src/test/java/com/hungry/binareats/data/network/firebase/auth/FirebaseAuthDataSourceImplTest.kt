package com.hungry.binareats.data.network.firebase.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import io.mockk.MockKAnnotations.init
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.lang.Exception

class FirebaseAuthDataSourceImplTest {

    @MockK(relaxed = true)
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var dataSource: FirebaseAuthDataSource

    @Before
    fun setUp() {
        init(this)
        dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
    }

    @Test
    fun doLogin() {
        coEvery { firebaseAuth.signInWithEmailAndPassword(any(), any()) } returns mockTaskAuthResult()
        runTest {
            val result = dataSource.doLogin("email", "password")
            Assert.assertTrue(result)
        }
    }

    @Test
    fun doRegister() {
        runTest {
            mockkConstructor(UserProfileChangeRequest.Builder::class)
            every { anyConstructed<UserProfileChangeRequest.Builder>().build() } returns mockk()
            val mockAuthResult = mockTaskAuthResult()
            every { firebaseAuth.createUserWithEmailAndPassword(any(), any()) } returns mockAuthResult
            val mockUser = mockk<FirebaseUser>(relaxed = true)
            every { mockAuthResult.result.user } returns mockUser
            every { mockUser.updateProfile(any()) } returns mockTaskVoid()
            val result = dataSource.doRegister("fullName", "email", "password")
            Assert.assertEquals(result, true)
        }
    }

    @Test
    fun updatePassword() {
        coEvery { firebaseAuth.currentUser?.updatePassword(any()) } returns mockTaskVoid()

        runTest {
            val result = dataSource.updatePassword("newPassword")
            Assert.assertTrue(result)
        }
    }

    @Test
    fun updateEmail() {
        coEvery { firebaseAuth.currentUser } returns mockk(relaxed = true)
        coEvery { firebaseAuth.currentUser?.updateEmail(any()) } returns mockTaskVoid()

        runTest {
            val result = dataSource.updateEmail("newEmail")
            Assert.assertTrue(result)
        }
    }

    @Test
    fun sendChangePasswordRequestByEmail() {
        every { firebaseAuth.currentUser?.email } returns "email"
        every { firebaseAuth.sendPasswordResetEmail("email") } returns mockTaskVoid()

        runTest {
            val result = dataSource.sendChangePasswordRequestByEmail()
            Assert.assertTrue(result)
        }
    }

    @Test
    fun getCurrentUser() {
        every { firebaseAuth.currentUser } returns mockk(relaxed = true)
        val result = dataSource.getCurrentUser()
        Assert.assertNotNull(result)
    }

    @Test
    fun isLoggedIn() {
        every { firebaseAuth.currentUser } returns mockk(relaxed = true)
        val result = dataSource.isLoggedIn()
        Assert.assertTrue(result)
    }

    @Test
    fun doLogout() {
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns firebaseAuth
        every { firebaseAuth.signOut() } returns Unit
        val result = dataSource.doLogout()
        Assert.assertEquals(result, true)
    }

    private fun mockTaskVoid(exception: Exception? = null): Task<Void> {
        val task: Task<Void> = mockk(relaxed = true)
        every { task.isComplete } returns true
        every { task.exception } returns exception
        every { task.isCanceled } returns false
        val relaxedVoid: Void = mockk(relaxed = true)
        every { task.result } returns relaxedVoid
        return task
    }

    private fun mockTaskAuthResult(exception: Exception? = null): Task<AuthResult> {
        val task: Task<AuthResult> = mockk(relaxed = true)
        every { task.isComplete } returns true
        every { task.exception } returns exception
        every { task.isCanceled } returns false
        val relaxedResult: AuthResult = mockk(relaxed = true)
        every { task.result } returns relaxedResult
        every { task.result.user } returns mockk(
            relaxed = true
        )
        return task
    }
}
