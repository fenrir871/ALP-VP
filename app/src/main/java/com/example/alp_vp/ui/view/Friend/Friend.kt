package com.example.alp_vp.ui.view.Friend

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Friend() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp),  // Add extra padding for bottom navbar
        contentAlignment = Alignment.Center
    ) {
        Text("Friend Screen")
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun FriendPreview() {
    Friend()
}