package com.example.alp_vp.ui.view.Friend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alp_vp.ui.model.Friend
import com.example.alp_vp.ui.viewmodel.FriendViewModel
import com.example.alp_vp.data.repository.UserRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Friend() {
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }

    // Create ViewModel with context
    val viewModel: FriendViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(FriendViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return FriendViewModel(context) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    )

    val leaderboard by viewModel.leaderboard.collectAsState()
    val allFriends by viewModel.allFriends.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Leaderboard", "All Friends")

    val blueStart = Color(0xFF2A7DE1)
    val blueEnd = Color(0xFF3BB0FF)

    // Get current user info
    val currentUser = remember { userRepository.getUser() }

    // Load data on first composition
    LaunchedEffect(Unit) {
        currentUser?.let {
            viewModel.loadLeaderboard(
                userName = it.username,
                username = it.username,
                userScore = 95 // You can get this from wherever you store scores
            )
        }
        viewModel.loadAllFriends()
    }

    // Show success/error messages
    LaunchedEffect(successMessage, errorMessage) {
        if (successMessage != null || errorMessage != null) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Friends", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = blueStart,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F7FA))
        ) {
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = blueStart
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontWeight = FontWeight.SemiBold) }
                    )
                }
            }

            // Messages
            errorMessage?.let {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Text(
                        text = it,
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFFC62828)
                    )
                }
            }

            successMessage?.let {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Text(
                        text = it,
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFF2E7D32)
                    )
                }
            }

            // Content
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = blueStart)
                }
            } else {
                when (selectedTab) {
                    0 -> LeaderboardTab(leaderboard)
                    1 -> AllFriendsTab(allFriends)
                }
            }
        }
    }
}

@Composable
fun LeaderboardTab(leaderboard: List<Friend>) {
    if (leaderboard.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No friends yet. Add some friends!",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(leaderboard.size) { index ->
                val friend = leaderboard[index]
                // Add rank (index + 1) to the friend
                LeaderboardCard(friend, index + 1)
            }
        }
    }
}

@Composable
fun LeaderboardCard(friend: Friend, rank: Int) {
    val blueStart = Color(0xFF2A7DE1)
    val rankColor = when (rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (friend.isCurrentUser) Color(0xFFE3F2FD) else Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank badge
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(rankColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#$rank",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = rankColor
                )
            }

            Spacer(Modifier.width(16.dp))

            // Friend info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = friend.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF2D3748)
                    )
                    if (friend.isCurrentUser) {
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "(You)",
                            fontSize = 12.sp,
                            color = blueStart,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Text(
                    text = friend.username,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            // Score
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = blueStart.copy(alpha = 0.1f)
            ) {
                Text(
                    text = "${friend.highestScore}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = blueStart
                )
            }
        }
    }
}

@Composable
fun AllFriendsTab(friends: List<Friend>) {
    if (friends.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No friends yet",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(friends) { friend ->
                FriendCard(friend)
            }
        }
    }
}

@Composable
fun FriendCard(friend: Friend) {
    val blueStart = Color(0xFF2A7DE1)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(blueStart.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = blueStart,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            // Friend info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = friend.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF2D3748)
                )
                Text(
                    text = friend.username,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            // Score badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = blueStart.copy(alpha = 0.1f)
            ) {
                Text(
                    text = "${friend.highestScore}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = blueStart
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun FriendPreview() {
    Friend()
}