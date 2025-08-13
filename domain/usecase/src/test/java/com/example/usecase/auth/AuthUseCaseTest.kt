package com.example.usecase.auth

import com.example.code.common.utils.Resource
import com.example.commom_entity.User
import com.example.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var authUseCase: AuthUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        authUseCase = AuthUseCase(authRepository)
    }

    @After
    fun tearDown() {
        clearMocks(authRepository)
    }

    @Test
    fun `invoke should return FirebaseUser on success`() = runBlocking {
        val email = "test@example.com"
        val password = "password123"
        val mockFirebaseUser: FirebaseUser = mockk()
        coEvery { authRepository.signInWithEmailAndPassword(email, password) } returns Resource.Success(mockFirebaseUser)

        val result = authUseCase(email, password)

        assertTrue(result is Resource.Success)
        assertEquals(mockFirebaseUser, (result as Resource.Success).data)
        coVerify { authRepository.signInWithEmailAndPassword(email, password) }
    }

    @Test
    fun `resetPassword should return success message`() = runBlocking {
        val email = "test@example.com"
        val successMessage = "Password reset email sent."
        coEvery { authRepository.resetPassword(email) } returns Resource.Success(successMessage)

        val result = authUseCase.resetPassword(email)

        assertTrue(result is Resource.Success)
        assertEquals(successMessage, (result as Resource.Success).data)
        coVerify { authRepository.resetPassword(email) }
    }

    @Test
    fun `signOut should call repository signOut`() = runBlocking {
        coEvery { authRepository.signOut() } just Runs

        authUseCase.signOut()

        coVerify { authRepository.signOut() }
    }

    @Test
    fun `getCurrentUser should return FirebaseUser`() {
        val mockFirebaseUser: FirebaseUser = mockk()
        every { authRepository.getCurrentUser() } returns mockFirebaseUser

        val result = authUseCase.getCurrentUser()

        assertEquals(mockFirebaseUser, result)
        verify { authRepository.getCurrentUser() }
    }

    @Test
    fun `createAccountWithEmailAndPassword should return FirebaseUser on success`() = runBlocking {
        val user = User("testUser", "test@example.com")
        val password = "password123"
        val mockFirebaseUser: FirebaseUser = mockk()
        coEvery { authRepository.createAccountWithEmailAndPassword(user, password) } returns Resource.Success(mockFirebaseUser)

        val result = authUseCase.createAccountWithEmailAndPassword(user, password)

        assertTrue(result is Resource.Success)
        assertEquals(mockFirebaseUser, (result as Resource.Success).data)
        coVerify { authRepository.createAccountWithEmailAndPassword(user, password) }
    }
}