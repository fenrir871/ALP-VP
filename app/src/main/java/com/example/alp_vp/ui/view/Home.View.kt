package com.example.alp_vp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import com.example.alp_vp.ui.viewmodel.DailyActivityViewModel

@Composable
fun HomeView(dailyActivityViewModel: DailyActivityViewModel) {
    val state = dailyActivityViewModel.uiState.collectAsState().value

    // Collect scores and messages
    val sleepScore by dailyActivityViewModel._sleepScore.collectAsState()
    val sleepMessage by dailyActivityViewModel._sleepMessage.collectAsState()
    val waterScore by dailyActivityViewModel._waterScore.collectAsState()
    val waterMessage by dailyActivityViewModel._waterMessage.collectAsState()
    val stepsScore by dailyActivityViewModel._stepsScore.collectAsState()
    val stepsMessage by dailyActivityViewModel._stepsMessage.collectAsState()
    val caloriesScore by dailyActivityViewModel._caloriesScore.collectAsState()
    val caloriesMessage by dailyActivityViewModel._caloriesMessage.collectAsState()

    Surface(color = Color(0xFFF3F6FB), modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 100.dp  // Add extra padding for bottom navbar
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                HeaderCard(
                    username = state.username,
                    dateLabel = state.dateLabel,
                    streakDays = state.streakDays,
                    avgScore = state.avgScore,
                    goalsDone = state.goalsDone,
                    goalsTotal = state.goalsTotal
                )
            }
            item {
                ProgressCard(
                    steps = state.steps.toIntOrNull() ?: 0,
                    calories = state.calories.toIntOrNull() ?: 0,
                    waterGlasses = state.waterGlasses.toIntOrNull() ?: 0,
                    sleepHours = state.sleepHours.toFloatOrNull() ?: 0f,
                    dailyActivityViewModel = dailyActivityViewModel
                )
            }
            item {
                InputDataCard (
                    sleepHours = state.sleepHours,
                    waterGlasses = state.waterGlasses,
                    steps = state.steps,
                    calories = state.calories,
                    onSleepChange = dailyActivityViewModel::onSleepChange,
                    onWaterChange = dailyActivityViewModel::onWaterChange,
                    onStepsChange = dailyActivityViewModel::onStepsChange,
                    onCaloriesChange = dailyActivityViewModel::onCaloriesChange,
                    // Pass scores and messages
                    sleepScore = sleepScore,
                    sleepMessage = sleepMessage,
                    waterScore = waterScore,
                    waterMessage = waterMessage,
                    stepsScore = stepsScore,
                    stepsMessage = stepsMessage,
                    caloriesScore = caloriesScore,
                    caloriesMessage = caloriesMessage
                )
            }
        }
    }
}

@Composable
private fun InputDataCard(
    sleepHours: String,
    waterGlasses: String,
    steps: String,
    calories: String,
    onSleepChange: (String) -> Unit,
    onWaterChange: (String) -> Unit,
    onStepsChange: (String) -> Unit,
    onCaloriesChange: (String) -> Unit,
    sleepScore: Float,
    sleepMessage: String,
    waterScore: Float,
    waterMessage: String,
    stepsScore: Float,
    stepsMessage: String,
    caloriesScore: Float,
    caloriesMessage: String
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "Input Your Data",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF1E2A3A)
            )
            Spacer(Modifier.height(12.dp))

            InputRowField(
                icon = Icons.Outlined.Hotel,
                tint = Color(0xFF9B6BFF),
                title = "Sleep (hours)",
                value = sleepHours,
                onValueChange = onSleepChange,
                score = sleepScore,
                message = sleepMessage
            )
            Divider(color = Color(0xFFEDEFF2), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
            InputRowField(
                icon = Icons.Outlined.WaterDrop,
                tint = Color(0xFF3DA8FF),
                title = "Water (glasses)",
                value = waterGlasses,
                onValueChange = onWaterChange,
                score = waterScore,
                message = waterMessage
            )
            Divider(color = Color(0xFFEDEFF2), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
            InputRowField(
                icon = Icons.Outlined.DirectionsRun,
                tint = Color(0xFFFF7A3D),
                title = "Steps",
                value = steps,
                onValueChange = onStepsChange,
                score = stepsScore,
                message = stepsMessage
            )
            Divider(color = Color(0xFFEDEFF2), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
            InputRowField(
                icon = Icons.Outlined.FavoriteBorder,
                tint = Color(0xFFFF5A7A),
                title = "Calories",
                value = calories,
                onValueChange = onCaloriesChange,
                score = caloriesScore,
                message = caloriesMessage
            )
        }
    }
}

@Composable
private fun InputRowField(
    icon: ImageVector,
    tint: Color,
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    score: Float,
    message: String
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(tint.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = tint)
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = Color(0xFF637083), fontSize = 13.sp)
            TextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            // Only show the message, not the score text
            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    color = if (score >= 15f) Color(0xFF43A047) else Color(0xFFD32F2F),
                    fontSize = 12.sp
                )
            }
        }
    }
}



@Composable
private fun HeaderCard(
    username: String,
    dateLabel: String,
    streakDays: Int,
    avgScore: Int,
    goalsDone: Int,
    goalsTotal: Int
) {
    val blueA = Color(0xFF2E64FE)
    val blueB = Color(0xFF6BA7FF)
    val headerBrush = Brush.linearGradient(listOf(blueA, blueB))

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(headerBrush, RoundedCornerShape(28.dp))
    ) {
        Column(
            modifier = Modifier
                .background(headerBrush)
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Welcome back,",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = username,
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = dateLabel,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f))
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatPill(icon = Icons.Outlined.Waves, label = "Streak", value = "$streakDays days")
                StatPill(icon = Icons.Outlined.Bolt, label = "Avg Score", value = "$avgScore")
                StatPill(icon = Icons.Outlined.FavoriteBorder, label = "Goals", value = "$goalsDone/$goalsTotal")
            }
        }
    }
}

@Composable
private fun StatPill(icon: ImageVector, label: String, value: String) {
    Box(
        modifier = Modifier
            .width(90.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.25f))
            .padding(vertical = 16.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 11.sp,
                maxLines = 1
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ProgressCard(
    steps: Int,
    calories: Int,
    waterGlasses: Int,
    sleepHours: Float,
    dailyActivityViewModel: DailyActivityViewModel
) {
    val stepsScore by dailyActivityViewModel._stepsScore.collectAsState()
    val caloriesScore by dailyActivityViewModel._caloriesScore.collectAsState()
    val waterScore by dailyActivityViewModel._waterScore.collectAsState()
    val sleepScore by dailyActivityViewModel._sleepScore.collectAsState()

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Today's Progress",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E2A3A)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Outlined.Waves,
                    contentDescription = null,
                    tint = Color(0xFF5A7BFF),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                MetricTile(
                    icon = Icons.Outlined.DirectionsRun,
                    iconTint = Color(0xFFFF7A3D),
                    bg = Color(0xFFFFF5F0),
                    title = "Steps",
                    value = steps.toString(),
                    suffix = "",
                    score = (stepsScore / 2).toInt(),
                    max = 10,
                    modifier = Modifier.weight(1f)
                )
                MetricTile(
                    icon = Icons.Outlined.FavoriteBorder,
                    iconTint = Color(0xFFFF5A7A),
                    bg = Color(0xFFFFF0F3),
                    title = "Calories",
                    value = calories.toString(),
                    suffix = "",
                    score = (caloriesScore / 2).toInt(),
                    max = 10,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                MetricTile(
                    icon = Icons.Outlined.WaterDrop,
                    iconTint = Color(0xFF3DA8FF),
                    bg = Color(0xFFF0F8FF),
                    title = "Water",
                    value = waterGlasses.toString(),
                    suffix = "glasses",
                    score = (waterScore / 2).toInt(),
                    max = 10,
                    modifier = Modifier.weight(1f)
                )
                MetricTile(
                    icon = Icons.Outlined.Hotel,
                    iconTint = Color(0xFF9B6BFF),
                    bg = Color(0xFFF8F5FF),
                    title = "Sleep",
                    value = sleepHours.toInt().toString(),
                    suffix = "hours",
                    score = (sleepScore / 2).toInt(),
                    max = 10,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MetricTile(
    icon: ImageVector,
    iconTint: Color,
    bg: Color,
    title: String,
    value: String,
    suffix: String,
    score: Int,
    max: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .padding(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = title,
                    color = Color(0xFF637083),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E2A3A)
                )
                if (suffix.isNotEmpty()) {
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = suffix,
                        color = Color(0xFF7A8899),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                ProgressBar(
                    modifier = Modifier.weight(1f),
                    current = score,
                    max = max,
                    barColor = iconTint,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "$score/$max",
                    color = Color(0xFF9AA6B2),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ProgressBar(current: Int, max: Int, barColor: Color, modifier: Modifier = Modifier) {
    val pct = remember(current, max) { if (max <= 0) 0f else current.toFloat() / max.toFloat() }
    val track = Color(0xFFE8EBF0)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(track)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(pct.coerceIn(0f, 1f))
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(barColor)
        )
    }
}

@Composable
@Preview
fun HomeViewPreview() {
    // Leave empty or provide a mock if needed
}