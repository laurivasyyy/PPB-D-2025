package com.example.sayurbox.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurbox.data.models.User
import com.example.sayurbox.repository.SayurboxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: SayurboxRepository
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun login(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Validate input
                if (email.isBlank() || password.isBlank()) {
                    onResult(false, "Email and password cannot be empty")
                    return@launch
                }

                if (!isValidEmail(email)) {
                    onResult(false, "Please enter a valid email address")
                    return@launch
                }

                val user = repository.login(email.trim(), password)
                if (user != null) {
                    _currentUser.value = user
                    _isLoggedIn.value = true
                    onResult(true, "Login successful")
                } else {
                    onResult(false, "Invalid email or password")
                }
            } catch (e: Exception) {
                onResult(false, "Login failed: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Validate input
                val validationResult = validateRegistrationInput(
                    firstName, lastName, email, phoneNumber, password
                )
                if (!validationResult.first) {
                    onResult(false, validationResult.second)
                    return@launch
                }

                // Check if email already exists
                if (repository.isEmailExists(email.trim())) {
                    onResult(false, "Email already exists")
                    return@launch
                }

                val user = User(
                    email = email.trim(),
                    firstName = firstName.trim(),
                    lastName = lastName.trim(),
                    phoneNumber = phoneNumber.trim(),
                    password = password // In production, hash this password
                )

                repository.register(user)
                onResult(true, "Registration successful")

            } catch (e: Exception) {
                onResult(false, "Registration failed: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        _isLoggedIn.value = false
        _currentUser.value = null
        _errorMessage.value = null
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    private fun validateRegistrationInput(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        password: String
    ): Pair<Boolean, String> {
        return when {
            firstName.isBlank() -> false to "First name is required"
            lastName.isBlank() -> false to "Last name is required"
            email.isBlank() -> false to "Email is required"
            phoneNumber.isBlank() -> false to "Phone number is required"
            password.isBlank() -> false to "Password is required"
            !isValidEmail(email) -> false to "Please enter a valid email address"
            password.length < 8 -> false to "Password must be at least 8 characters"
            !isValidPhoneNumber(phoneNumber) -> false to "Please enter a valid phone number"
            else -> true to ""
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        return phone.length >= 10 && phone.all { it.isDigit() || it in "+()-. " }
    }
}
