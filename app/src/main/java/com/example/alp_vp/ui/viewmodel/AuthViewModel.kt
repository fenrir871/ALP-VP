package com.example.alp_vp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alp_vp.data.local.TokenManager
import com.example.alp_vp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val repository = AuthRepository(tokenManager)

    // ========== LOGIN FORM STATE ==========
    private val _loginEmail = MutableStateFlow("")
    val loginEmail: StateFlow<String> = _loginEmail.asStateFlow()

    private val _loginPassword = MutableStateFlow("")
    val loginPassword: StateFlow<String> = _loginPassword.asStateFlow()

    // ========== REGISTER FORM STATE ==========
    private val _registerName = MutableStateFlow("")
    val registerName: StateFlow<String> = _registerName.asStateFlow()

    private val _registerUsername = MutableStateFlow("")
    val registerUsername: StateFlow<String> = _registerUsername.asStateFlow()

    private val _registerPhone = MutableStateFlow("")
    val registerPhone: StateFlow<String> = _registerPhone.asStateFlow()

    private val _registerEmail = MutableStateFlow("")
    val registerEmail: StateFlow<String> = _registerEmail.asStateFlow()

    private val _registerPassword = MutableStateFlow("")
    val registerPassword: StateFlow<String> = _registerPassword.asStateFlow()

    // ========== UI STATE ==========
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess.asStateFlow()

    // ========== LOGIN FORM UPDATE FUNCTIONS ==========

    fun updateLoginEmail(email: String) {
        _loginEmail.value = email
    }

    fun updateLoginPassword(password: String) {
        _loginPassword.value = password
    }

    // ========== REGISTER FORM UPDATE FUNCTIONS ==========

    fun updateRegisterName(name: String) {
        _registerName.value = name
    }

    fun updateRegisterUsername(username: String) {
        _registerUsername.value = username
    }

    fun updateRegisterPhone(phone: String) {
        _registerPhone.value = phone
    }

    fun updateRegisterEmail(email: String) {
        _registerEmail.value = email
    }

    fun updateRegisterPassword(password: String) {
        _registerPassword.value = password
    }

    // ========== LOGIN FUNCTION ==========
    fun login() {
        // Validate that fields aren't empty
        if (_loginEmail.value.isBlank() || _loginPassword.value.isBlank()) {
            _errorMessage.value = "Please fill in all fields"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.login(_loginEmail.value, _loginPassword.value)

            result.onSuccess {
                _loginSuccess.value = true
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Login failed"
            }

            _isLoading.value = false
        }
    }

    fun register(confirmPassword: String) {
        if (_registerName.value.isBlank() ||
            _registerUsername.value.isBlank() ||
            _registerPhone.value.isBlank() ||
            _registerEmail.value.isBlank() ||
            _registerPassword.value.isBlank()
        ) {
            _errorMessage.value = "Please fill in all fields"
            return
        }

        if (_registerPassword.value != confirmPassword) {
            _errorMessage.value = "Passwords do not match"
            return
        }

        if (_registerPassword.value.length < 6) {
            _errorMessage.value = "Password must be at least 6 characters"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.register(
                _registerName.value,
                _registerUsername.value,
                _registerPhone.value,
                _registerEmail.value,
                _registerPassword.value
            )

            result.onSuccess {
                _registerSuccess.value = true
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Registration failed"
            }

            _isLoading.value = false
        }
    }

    // ========== UTILITY FUNCTIONS ==========
    fun clearMessage() {
        _errorMessage.value = null
    }

    fun resetLoginForm() {
        _loginEmail.value = ""
        _loginPassword.value = ""
        _loginSuccess.value = false
    }

    fun resetRegisterForm() {
        _registerName.value = ""
        _registerUsername.value = ""
        _registerPhone.value = ""
        _registerEmail.value = ""
        _registerPassword.value = ""
        _registerSuccess.value = false
    }
}