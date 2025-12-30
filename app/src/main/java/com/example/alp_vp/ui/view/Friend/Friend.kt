package com.example.alp_vp.ui.view.Friend

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alp_vp.data.repository.FriendRepository
import com.example.alp_vp.data.repository.UserRepository
import com.example.alp_vp.ui.model.Friend

@Composable
fun Friend() {
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }
    val friendRepository = remember { FriendRepository(context) }
    val currentUser = remember { userRepository.getUser() }

    val blueStart = Color(0xFF2A7DE1)
    val blueEnd = Color(0xFF3BB0FF)

    var searchQuery by remember { mutableStateOf("") }

    // Get friends from repository
    val allFriends = remember {
        val friendsFromRepo = friendRepository.getAllFriends()
        mutableListOf<Friend>().apply {
            // Add friends from repository
            addAll(friendsFromRepo)

            // Add current user to the list if they exist
            currentUser?.let { user ->
                // For demo, using a random score. In real app, get from user's actual score
                val userScore = 76 // This should come from user's actual game data
                add(Friend(
                    rank = 0,
                    id = 999,
                    name = user.fullName,
                    username = "@${user.username}",
                    highestScore = userScore,
                    isCurrentUser = true
                ))
            }
        }
    }

    // Sort by score (descending) and assign ranks
    val friends = remember(allFriends) {
        allFriends
            .sortedByDescending { it.highestScore }
            .mapIndexed { index, friend ->
                friend.copy(rank = index + 1)
            }
    }

    // Calculate current user's rank
    val userRank = friends.find { it.isCurrentUser }?.rank

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        // Header Section with gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 45.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(blueStart, blueEnd)
                    )
                )
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, start = 24.dp, end = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side: Icon in rounded rectangle and text
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rounded rectangle background for icon (like Login page)
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x33FFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.People,
                            contentDescription = "Friends",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "All Friends",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Compete with your friends",
                            color = Color(0xEEFFFFFF),
                            fontSize = 13.sp
                        )
                    }
                }

                // Right side: Rank badge
                Surface(
                    modifier = Modifier
                        .size(70.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0x33FFFFFF)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Your Rank",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (userRank != null) "#$userRank" else "N/A",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Search bar (floating above the list)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-30).dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF9AA7B8)
                    )
                },
                placeholder = {
                    Text(
                        "Search friends...",
                        color = Color(0xFF9AA7B8),
                        fontSize = 14.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    cursorColor = blueStart
                ),
                shape = RoundedCornerShape(20.dp),
                singleLine = true
            )
        }

        // Friends List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-20).dp),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 8.dp,
                bottom = 100.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(friends.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.username.contains(searchQuery, ignoreCase = true)
            }) { friend ->
                FriendCard(friend = friend, blueColor = blueStart)
            }
        }
    }
}

@Composable
fun FriendCard(friend: Friend, blueColor: Color) {
    // Determine rank color based on position
    val rankBackgroundColor = when(friend.rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> Color(0xFFE0E0E0) // Gray
    }

    val rankTextColor = when(friend.rank) {
        1 -> Color(0xFFB8860B) // Dark gold
        2 -> Color(0xFF757575) // Dark silver
        3 -> Color(0xFF8B4513) // Dark bronze
        else -> Color(0xFF757575) // Dark gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (friend.isCurrentUser) {
                    Modifier.border(2.dp, blueColor, RoundedCornerShape(20.dp))
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (friend.isCurrentUser) 6.dp else 3.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank number with color coding
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(rankBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#${friend.rank}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = rankTextColor
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Profile Avatar
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(blueColor.copy(alpha = 0.1f))
                    .border(2.dp, blueColor.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = blueColor,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Friend Info - Name and Points
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = friend.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1D26)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${friend.highestScore} points",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = blueColor
                )
            }

            // Add Friend Button (only for other users, not current user)
            if (!friend.isCurrentUser) {
                IconButton(
                    onClick = { /* Handle add/remove friend */ },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(blueColor.copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "Add Friend",
                        tint = blueColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun FriendPreview() {
    Friend()
}