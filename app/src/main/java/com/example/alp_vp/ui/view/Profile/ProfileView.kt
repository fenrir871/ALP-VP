package com.example.alp_vp.ui.view.Profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alp_vp.data.repository.UserRepository
import com.example.alp_vp.data.repository.FriendRepository

@Composable
fun ProfileView(
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }
    val friendRepository = remember { FriendRepository(context) }
    var user by remember { mutableStateOf(userRepository.getCurrentUser()) }

    // Get friends count dynamically - will recompose when friends change
    var friendsCount by remember { mutableStateOf(friendRepository.getAllFriends().size) }

    // Get highest score dynamically - will recompose when score changes
    var highestScore by remember { mutableStateOf(userRepository.getHighestScore()) }

    // Refresh data on composition
    LaunchedEffect(Unit) {
        friendsCount = friendRepository.getAllFriends().size
        highestScore = userRepository.getHighestScore()
    }

    val blueStart = Color(0xFF2A7DE1)
    val blueEnd = Color(0xFF3BB0FF)
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .verticalScroll(scrollState)
    ) {
        // Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 45.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(blueStart, blueEnd)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Decorative circles
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .offset(x = 220.dp, y = (-40).dp)
                    .clip(CircleShape)
                    .background(Color(0x22FFFFFF))
            )
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .offset(x = (-60).dp, y = 60.dp)
                    .clip(CircleShape)
                    .background(Color(0x22FFFFFF))
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Profile Avatar - Circular with white border
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(4.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Picture",
                        tint = Color(0xFF2A7DE1),
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = user?.fullName ?: "Guest User",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "@${user?.username ?: "guest"}",
                    color = Color(0xEEFFFFFF),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Stats Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(value = highestScore.toString(), label = "Highest Score", color = blueStart)
                HorizontalDivider(
                    modifier = Modifier
                        .width(1.dp)
                        .height(50.dp),
                    color = Color(0xFFE8EDF2)
                )
                StatItem(value = friendsCount.toString(), label = "Friends", color = Color(0xFFFF6B6B))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Personal Information Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Personal Information",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1D26),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    InfoItem(
                        icon = Icons.Default.Person,
                        label = "Full Name",
                        value = user?.fullName ?: "Not set",
                        iconTint = Color(0xFF4A90E2),
                        onEdit = { newValue ->
                            user?.let {
                                val updatedUser = it.copy(fullName = newValue)
                                userRepository.updateUser(updatedUser)
                                user = updatedUser
                            }
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color(0xFFE8EDF2)
                    )

                    InfoItem(
                        icon = Icons.Default.AccountCircle,
                        label = "Username",
                        value = user?.username ?: "Not set",
                        iconTint = Color(0xFF9C27B0),
                        onEdit = { newValue ->
                            user?.let {
                                val updatedUser = it.copy(username = newValue)
                                userRepository.updateUser(updatedUser)
                                user = updatedUser
                            }
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color(0xFFE8EDF2)
                    )

                    InfoItem(
                        icon = Icons.Default.Phone,
                        label = "Phone",
                        value = user?.phone ?: "Not set",
                        iconTint = Color(0xFFFF9800),
                        onEdit = { newValue ->
                            user?.let {
                                val updatedUser = it.copy(phone = newValue)
                                userRepository.updateUser(updatedUser)
                                user = updatedUser
                            }
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color(0xFFE8EDF2)
                    )

                    InfoItem(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = user?.email ?: "Not set",
                        iconTint = Color(0xFF4CAF50),
                        onEdit = { newValue ->
                            user?.let {
                                val updatedUser = it.copy(email = newValue)
                                userRepository.updateUser(updatedUser)
                                user = updatedUser
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Logout Button
        Button(
            onClick = {
                userRepository.logout()
                onLogout()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 100.dp)  // Add extra padding for bottom navbar
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF3B30)
            )
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Log Out",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Log Out",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun StatItem(value: String, label: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF6C7A92)
        )
    }
}


@Composable
fun InfoItem(
    icon: ImageVector,
    label: String,
    value: String,
    iconTint: Color,
    onEdit: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var editedValue by remember { mutableStateOf(value) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFF9AA7B8)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1D26)
            )
        }
        IconButton(onClick = {
            editedValue = value
            errorMessage = null
            showDialog = true
        }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color(0xFF9AA7B8),
                modifier = Modifier.size(20.dp)
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                errorMessage = null
            },
            title = {
                Text(
                    text = "Edit $label",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = editedValue,
                        onValueChange = {
                            editedValue = it
                            errorMessage = null
                        },
                        label = { Text(label) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2A7DE1),
                            focusedLabelColor = Color(0xFF2A7DE1),
                            errorBorderColor = Color.Red
                        ),
                        isError = errorMessage != null
                    )
                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage!!,
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val validation = validateField(label, editedValue)
                        if (validation.isValid) {
                            onEdit(editedValue)
                            showDialog = false
                            errorMessage = null
                        } else {
                            errorMessage = validation.error
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2A7DE1)
                    )
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    errorMessage = null
                }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}

data class ValidationResult(val isValid: Boolean, val error: String? = null)

fun validateField(fieldName: String, value: String): ValidationResult {
    return when (fieldName) {
        "Full Name" -> {
            when {
                value.isBlank() -> ValidationResult(false, "Please enter your full name")
                else -> ValidationResult(true)
            }
        }
        "Username" -> {
            when {
                value.isBlank() -> ValidationResult(false, "Please enter a username")
                value.length < 3 -> ValidationResult(false, "Username must be at least 3 characters")
                value.length > 50 -> ValidationResult(false, "Username must not exceed 50 characters")
                else -> ValidationResult(true)
            }
        }
        "Email" -> {
            when {
                value.isBlank() -> ValidationResult(false, "Please enter your email")
                !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches() -> {
                    ValidationResult(false, "Please enter a valid email")
                }
                else -> ValidationResult(true)
            }
        }
        "Phone" -> {
            when {
                value.isBlank() -> ValidationResult(false, "Please enter your phone number")
                value.length < 10 -> ValidationResult(false, "Phone number must be at least 10 digits")
                value.length > 20 -> ValidationResult(false, "Phone number must not exceed 20 characters")
                else -> ValidationResult(true)
            }
        }
        else -> ValidationResult(true)
    }
}

@Composable
@Preview
fun ProfileViewPreview() {
    ProfileView()
}


