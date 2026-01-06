package com.example.alp_vp.ui.view.LoginRegister

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alp_vp.data.local.TokenManager
import com.example.alp_vp.ui.viewmodel.AuthViewModel


@Composable
fun Register(
    tokenManager: TokenManager,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val viewModel: AuthViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(tokenManager) as T
            }
        }
    )

    // Add these state observations
    val name by viewModel.registerName.collectAsState()
    val username by viewModel.registerUsername.collectAsState()
    val email by viewModel.registerEmail.collectAsState()
    val phone by viewModel.registerPhone.collectAsState()
    val password by viewModel.registerPassword.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val registerSuccess by viewModel.registerSuccess.collectAsState()

    // Navigate on success
    LaunchedEffect(registerSuccess) {
        if (registerSuccess) {
            onRegisterSuccess()
        }
    }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(Color(0xFF2196F3)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 32.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(64.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF64B5F6).copy(alpha = 0.5f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = "Heart Icon",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Create Account",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Start your health journey today",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            // White Card with form content
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-16).dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    CustomTextField(
                        label = "Full Name",
                        value = name,
                        onValueChange = { viewModel.updateRegisterName(it) },
                        placeholder = "Enter your full name",
                        leadingIcon = Icons.Default.Person,
                        enabled = !isLoading
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextField(
                        label = "Username",
                        value = username,
                        onValueChange = { viewModel.updateRegisterUsername(it) },
                        placeholder = "Enter your username",
                        leadingIcon = Icons.Default.AlternateEmail,
                        enabled = !isLoading
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextField(
                        label = "Email",
                        value = email,
                        onValueChange = { viewModel.updateRegisterEmail(it) },
                        placeholder = "Enter your email",
                        leadingIcon = Icons.Default.Email,
                        enabled = !isLoading
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextField(
                        label = "Phone",
                        value = phone,
                        onValueChange = { viewModel.updateRegisterPhone(it) },
                        placeholder = "Enter your phone number",
                        leadingIcon = Icons.Default.Phone,
                        enabled = !isLoading
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextField(
                        label = "Password",
                        value = password,
                        onValueChange = { viewModel.updateRegisterPassword(it) },
                        placeholder = "Create a password",
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onTogglePassword = { passwordVisible = !passwordVisible },
                        enabled = !isLoading
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextField(
                        label = "Confirm Password",
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = "Confirm your password",
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true,
                        passwordVisible = confirmPasswordVisible,
                        onTogglePassword = { confirmPasswordVisible = !confirmPasswordVisible }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            viewModel.register(confirmPassword)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3)
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Create Account",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    errorMessage?.let {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                        ) {
                            Text(
                                text = it,
                                modifier = Modifier.padding(12.dp),
                                color = Color(0xFFC62828),
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "or",
                        fontSize = 13.sp,
                        color = Color(0xFF8E98A3),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Already have an account? ",
                            fontSize = 13.sp,
                            color = Color(0xFF5A6978)
                        )
                        Text(
                            text = "Sign In",
                            fontSize = 13.sp,
                            color = Color(0xFF2196F3),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onNavigateToLogin() }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    Text(
        text = label,
        fontSize = 13.sp,
        color = Color(0xFF5A6978),
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 14.sp,
                color = Color(0xFFADB5BD)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = Color(0xFF8E98A3),
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = if (isPassword && onTogglePassword != null) {
            {
                IconButton(onClick = onTogglePassword) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle password visibility",
                        tint = Color(0xFF8E98A3),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF5F7FA),
            unfocusedContainerColor = Color(0xFFF5F7FA),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color(0xFF2D3748),
            unfocusedTextColor = Color(0xFF2D3748)
        ),
        singleLine = true
    )
}

//@Composable
//@Preview
//private fun RegisterView() {
//    Register()
//}
