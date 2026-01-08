package com.example.alp_vp.ui.view.Friend

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alp_vp.data.repository.FriendRepository
import com.example.alp_vp.data.repository.UserRepository
import com.example.alp_vp.ui.model.Friend
import com.example.alp_vp.ui.model.UserSearchModel
import kotlinx.coroutines.launch

@Composable
fun Friend() {
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }
    val friendRepository = remember { FriendRepository(context) }
    val currentUser = remember { userRepository.getCurrentUser() }
    val scope = rememberCoroutineScope()

    val blueStart = Color(0xFF2A7DE1)
    val blueEnd = Color(0xFF3BB0FF)

    var searchQuery by remember { mutableStateOf("") }
    var friends by remember { mutableStateOf<List<Friend>>(emptyList()) }
    var searchResults by remember { mutableStateOf<List<UserSearchModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isSearching by remember { mutableStateOf(false) }
    var userRank by remember { mutableStateOf<Int?>(null) }
    var showSearchMode by remember { mutableStateOf(false) }

    // Load leaderboard on first composition
    LaunchedEffect(Unit) {
        scope.launch {
            isLoading = true
            val result = friendRepository.getFriendLeaderboard()
            result.onSuccess { leaderboard ->
                val allFriends = leaderboard.toMutableList()

                currentUser?.let { user ->
                    // Only add current user if they have valid data
                    if (user.id != null && user.highestScore != null) {
                        allFriends.add(Friend(
                            rank = 0,
                            id = user.id,
                            name = user.fullName,
                            username = "@${user.username}",
                            highestScore = user.highestScore,
                            isCurrentUser = true
                        ))
                    }
                }

                friends = allFriends
                    .sortedByDescending { it.highestScore }
                    .mapIndexed { index, friend ->
                        friend.copy(rank = index + 1)
                    }

                userRank = friends.find { it.isCurrentUser }?.rank
                isLoading = false
            }.onFailure { error ->
                Toast.makeText(
                    context,
                    "Failed to load friends: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
                isLoading = false
            }
        }
    }

    // Search users when query changes
    LaunchedEffect(searchQuery) {
        if (searchQuery.length >= 2) {
            isSearching = true
            showSearchMode = true
            scope.launch {
                val result = friendRepository.searchUsers(searchQuery)
                result.onSuccess { results ->
                    searchResults = results
                    isSearching = false
                }.onFailure { error ->
                    Toast.makeText(
                        context,
                        "Search failed: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    isSearching = false
                }
            }
        } else {
            showSearchMode = false
            searchResults = emptyList()
        }
    }

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
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                            text = if (showSearchMode) "Search Users" else "All Friends",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (showSearchMode) "Find new friends" else "Compete with your friends",
                            color = Color(0xEEFFFFFF),
                            fontSize = 13.sp
                        )
                    }
                }

                // Rank badge (only show in friends mode)
                if (!showSearchMode) {
                    Surface(
                        modifier = Modifier.size(70.dp),
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
        }

        // Search bar
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
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = Color(0xFF9AA7B8)
                            )
                        }
                    }
                },
                placeholder = {
                    Text(
                        "Search users to add...",
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

        // Content
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = blueStart)
            }
        } else if (showSearchMode) {
            // Search Results
            if (isSearching) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = blueStart)
                }
            } else if (searchResults.isEmpty() && searchQuery.length >= 2) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Outlined.PersonSearch,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFF9AA7B8)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "No users found",
                            color = Color(0xFF637083),
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
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
                    items(searchResults) { user ->
                        UserSearchCard(
                            user = user,
                            blueColor = blueStart,
                            onAddFriend = {
                                scope.launch {
                                    val result = friendRepository.addFriend(user.id)
                                    result.onSuccess { message ->
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                        // Refresh search results
                                        val refreshResult = friendRepository.searchUsers(searchQuery)
                                        refreshResult.onSuccess { searchResults = it }
                                    }.onFailure { error ->
                                        Toast.makeText(
                                            context,
                                            error.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                    }
                }
            }
        } else {
            // Friends Leaderboard
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
                items(friends) { friend ->
                    FriendCard(
                        friend = friend,
                        blueColor = blueStart
                    )
                }
            }
        }
    }
}

@Composable
fun UserSearchCard(
    user: UserSearchModel,
    blueColor: Color,
    onAddFriend: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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

            // User Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1D26)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = user.username,
                    fontSize = 13.sp,
                    color = Color(0xFF637083)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${user.highestScore} points",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = blueColor
                )
            }

            // Action Button
            when (user.friendshipStatus) {
                "accepted" -> {
                    Surface(
                        modifier = Modifier
                            .height(40.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        color = Color(0xFFE8F5E9)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "Friends",
                                color = Color(0xFF4CAF50),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                "pending" -> {
                    Surface(
                        modifier = Modifier
                            .height(40.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        color = Color(0xFFFFF3E0)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.HourglassEmpty,
                                contentDescription = null,
                                tint = Color(0xFFFF9800),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "Pending",
                                color = Color(0xFFFF9800),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                else -> {
                    IconButton(
                        onClick = onAddFriend,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(blueColor)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Add Friend",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FriendCard(friend: Friend, blueColor: Color) {
    val rankBackgroundColor = when(friend.rank) {
        1 -> Color(0xFFFFD700)
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> Color(0xFFE0E0E0)
    }

    val rankTextColor = when(friend.rank) {
        1 -> Color(0xFFB8860B)
        2 -> Color(0xFF757575)
        3 -> Color(0xFF8B4513)
        else -> Color(0xFF757575)
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
            // Rank number
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

            // Friend Info
            Column(modifier = Modifier.weight(1f)) {
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

            // Current user badge
            if (friend.isCurrentUser) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = blueColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        "You",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = blueColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}