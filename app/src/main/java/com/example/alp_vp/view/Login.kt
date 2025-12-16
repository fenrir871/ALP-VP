package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen() {
    val blueStart = Color(0xFF2A7DE1)
    val blueEnd = Color(0xFF3BB0FF)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF5F7FA)) {
        Box(Modifier.fillMaxSize()) {
            // Top banner with rounded bottom corners (shortened)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.40f) // was 0.55f
                    .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 45.dp))
                    .background(Brush.verticalGradient(listOf(blueStart, blueEnd)))
            ) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0x33FFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Welcome Back!",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Sign in to continue your health journey",
                        color = Color(0xEEFFFFFF),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = (-140).dp), // was -160.dp to match shorter banner
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 20.dp)) {
                    Text("Email", color = Color(0xFF6C7A92), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp),
                        leadingIcon = {
                            Icon(Icons.Outlined.Email, contentDescription = null, tint = Color(0xFF9AA7B8))
                        },
                        placeholder = { Text("Enter your email", color = Color(0xFF9AA7B8), fontSize = 14.sp) },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE8EDF2),
                            focusedBorderColor = blueStart,
                            unfocusedContainerColor = Color(0xFFF9FBFD),
                            focusedContainerColor = Color(0xFFF9FBFD),
                            cursorColor = blueStart
                        )
                    )

                    Spacer(Modifier.height(16.dp))

                    Text("Password", color = Color(0xFF6C7A92), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp),
                        leadingIcon = {
                            Icon(Icons.Outlined.Lock, contentDescription = null, tint = Color(0xFF9AA7B8))
                        },
                        trailingIcon = {
                            Icon(Icons.Outlined.Visibility, contentDescription = null, tint = Color(0xFF9AA7B8))
                        },
                        placeholder = { Text("Enter your password", color = Color(0xFF9AA7B8), fontSize = 14.sp) },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE8EDF2),
                            focusedBorderColor = blueStart,
                            unfocusedContainerColor = Color(0xFFF9FBFD),
                            focusedContainerColor = Color(0xFFF9FBFD),
                            cursorColor = blueStart
                        )
                    )

                    Spacer(Modifier.height(10.dp))
                    TextButton(onClick = {}, modifier = Modifier.align(Alignment.End)) {
                        Text("Forgot Password?", color = blueStart, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }

                    Spacer(Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.horizontalGradient(listOf(blueStart, blueEnd))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Sign In", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    }

                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(Modifier.weight(1f), color = Color(0xFFE8EDF2))
                        Text("  or  ", color = Color(0xFF9AA7B8), fontSize = 12.sp)
                        Divider(Modifier.weight(1f), color = Color(0xFFE8EDF2))
                    }

                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Don't have an account? ", color = Color(0xFF6C7A92), fontSize = 13.sp)
                        Text("Sign Up", color = blueStart, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    LoginScreen()
}