package com.example.alp_vp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF5BA3F5), Color(0xFF4A9AED))
                    ),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
        ) {

        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {
                // Profile Avatar
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                        .background(Color.White)
                ) {
                    // Replace with your avatar image
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Picture",
                        tint = Color(0xFFFF8A65),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "bbbb",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "@bbbb",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        }

        // Stats Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-30).dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(value = "95", label = "Highest Score", color = Color(0xFF4A90E2))
                Divider(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp),
                    color = Color.LightGray
                )
                StatItem(value = "15", label = "Friends", color = Color(0xFFFF6B6B))
            }
        }

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
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    InfoItem(
                        icon = Icons.Default.Person,
                        label = "Full Name",
                        value = "bbbb",
                        iconTint = Color(0xFF4A90E2)
                    )
                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    InfoItem(
                        icon = Icons.Default.AccountCircle,
                        label = "Username",
                        value = "bbbb",
                        iconTint = Color(0xFF9C27B0)
                    )
                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    InfoItem(
                        icon = Icons.Default.Phone,
                        label = "Phone",
                        value = "1234567899",
                        iconTint = Color(0xFFFF9800)
                    )
                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    InfoItem(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = "wahyudibertrand02@gmail.com",
                        iconTint = Color(0xFF4CAF50)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Logout Button
        Button(
            onClick = { /* Handle logout */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3B30))
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
            color = Color.Black
        )
        Text(
            text = "out of 100",
            fontSize = 11.sp,
            color = Color.Gray
        )
    }
}


@Composable
fun InfoItem(icon: ImageVector, label: String, value: String, iconTint: Color) {
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
                color = Color.Gray
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
        IconButton(onClick = { /* Handle edit */ }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
@Preview
fun ProfileViewPreview() {
    ProfileView()
}


