package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen() {
    val blueStart = Color(0xFF2A7DE1)
    val blueEnd = Color(0xFF3BB0FF)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(blueStart, blueEnd),
                        startY = 0f,
                        endY = 800f
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x33FFFFFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "logo",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(Modifier.height(20.dp))
                Text(
                    text = "Welcome Back!",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Sign in to continue your health journey",
                    color = Color(0xEEFFFFFF),
                    fontSize = 14.sp
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.TopCenter)
                .offset(y = 200.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Email", color = Color(0xFF6C7A92), fontSize = 12.sp)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Outlined.Email, contentDescription = null, tint = Color(0xFF6C7A92))
                    },
                    placeholder = { Text("Enter your email") },
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(Modifier.height(16.dp))
                Text("Password", color = Color(0xFF6C7A92), fontSize = 12.sp)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Visibility,
                            contentDescription = null,
                            tint = Color(0xFF6C7A92)
                        )
                    },
                    placeholder = { Text("Enter your password") },
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(Modifier.height(10.dp))
                TextButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Forgot Password?", color = blueStart, fontSize = 12.sp)
                }

                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { /* TODO: Sign in */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = blueStart
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),

                        contentAlignment = Alignment.Center
                    ) {
                        Text("Sign In", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(Modifier.weight(1f))
                    Text("  or  ", color = Color(0xFF9AA7B8), fontSize = 12.sp)
                    Divider(Modifier.weight(1f))
                }

                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Don\'t have an account? ", color = Color(0xFF6C7A92))
                    TextButton(onClick = { /* TODO: Sign up */ }) {
                        Text("Sign Up", color = blueStart, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun LoginScreenPreview() {
    LoginScreen()
}