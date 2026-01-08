package com.example.alp_vp.ui.view.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alp_vp.ui.viewmodel.DailyActivityViewModel
import com.example.alp_vp.ui.viewmodel.HomeViewModel
import androidx.navigation.NavController
import com.example.alp_vp.ui.viewmodel.WeeklyViewModel
import kotlin.compareTo

import kotlin.toString

@Composable
fun HomeView(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory(LocalContext.current)),
    dailyActivityViewModel: DailyActivityViewModel = viewModel(factory = DailyActivityViewModel.Factory(LocalContext.current)),
    weeklyViewModel: WeeklyViewModel = viewModel(factory = WeeklyViewModel.Factory(LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsState()
    val weeklyStats by weeklyViewModel.weeklyStats.collectAsState()

    val todayScore by viewModel.todayScore.collectAsState()
    val hasTodayScore = todayScore > 0

    val sleepHours by dailyActivityViewModel._sleepHours.collectAsState()
    val waterGlasses by dailyActivityViewModel._waterGlasses.collectAsState()
    val steps by dailyActivityViewModel._steps.collectAsState()
    val calories by dailyActivityViewModel._calories.collectAsState()

    val sleepScore by dailyActivityViewModel._sleepScore.collectAsState()
    val waterScore by dailyActivityViewModel._waterScore.collectAsState()
    val stepsScore by dailyActivityViewModel._stepsScore.collectAsState()
    val caloriesScore by dailyActivityViewModel._caloriesScore.collectAsState()

    val sleepMessage by dailyActivityViewModel._sleepMessage.collectAsState()
    val waterMessage by dailyActivityViewModel._waterMessage.collectAsState()
    val stepsMessage by dailyActivityViewModel._stepsMessage.collectAsState()
    val caloriesMessage by dailyActivityViewModel._caloriesMessage.collectAsState()

    val calculatedScore by dailyActivityViewModel.calculatedScore.collectAsState()
    val showCalculatedScore by dailyActivityViewModel.showCalculatedScore.collectAsState()

    LaunchedEffect(sleepScore, waterScore, stepsScore, caloriesScore) {
        viewModel.updateStats(sleepScore, waterScore, stepsScore, caloriesScore)
    }

    LaunchedEffect(Unit) {
        weeklyViewModel.fetchWeeklySummary(userId = 1)
    }

    Surface(color = Color(0xFFF3F6FB), modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 120.dp
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .statusBarsPadding()
            ){

                item {
                    Text(
                        text = "Quick Access",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1D26),
                        modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 16.dp)
                    )
                }

                item {
                    InputDataCard(
                        sleepHours = sleepHours,
                        waterGlasses = waterGlasses,
                        steps = steps,
                        calories = calories,
                        sleepScore = sleepScore,
                        waterScore = waterScore,
                        stepsScore = stepsScore,
                        caloriesScore = caloriesScore,
                        sleepMessage = sleepMessage,
                        waterMessage = waterMessage,
                        stepsMessage = stepsMessage,
                        caloriesMessage = caloriesMessage,
                        calculatedScore = calculatedScore,
                        showCalculatedScore = showCalculatedScore,
                        onSleepChange = { dailyActivityViewModel.onSleepChange(it) },
                        onWaterChange = { dailyActivityViewModel.onWaterChange(it) },
                        onStepsChange = { dailyActivityViewModel.onStepsChange(it) },
                        onCaloriesChange = { dailyActivityViewModel.onCaloriesChange(it) },
                        onCalculateScore = { dailyActivityViewModel.onCalculateScore() },
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text(
                        text = "Quick Access",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1D26),
                        modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 16.dp)
                    )
                }



                item {
                    DailyActivityCard(
                        todayScore = todayScore,
                        hasTodayScore = hasTodayScore,
                        onNavigateToDailyActivity = { navController.navigate("dailyActivity") },
                    )
                }
                item {
                    HeaderCard(
                        username = uiState.username,
                        dateLabel = uiState.currentDate,  // Change this line
                        streakDays = uiState.streakDays,
                        avgScore = uiState.avgScore,
                        goalsDone = uiState.goalsCompleted,
                        goalsTotal = uiState.goalsTotal
                    )
                }
                item {
                    ProgressCard(
                        steps = steps,
                        calories = calories,
                        waterGlasses = waterGlasses,
                        sleepHours = sleepHours,
                        dailyActivityViewModel = dailyActivityViewModel
                    )
                }
                item {
                    InputDataCard(
                        sleepHours = sleepHours,
                        waterGlasses = waterGlasses,
                        steps = steps,
                        calories = calories,
                        sleepScore = sleepScore,
                        waterScore = waterScore,
                        stepsScore = stepsScore,
                        caloriesScore = caloriesScore,
                        sleepMessage = sleepMessage,
                        waterMessage = waterMessage,
                        stepsMessage = stepsMessage,
                        caloriesMessage = caloriesMessage,
                        calculatedScore = calculatedScore,
                        showCalculatedScore = showCalculatedScore,
                        onSleepChange = { dailyActivityViewModel.onSleepChange(it) },
                        onWaterChange = { dailyActivityViewModel.onWaterChange(it) },
                        onStepsChange = { dailyActivityViewModel.onStepsChange(it) },
                        onCaloriesChange = { dailyActivityViewModel.onCaloriesChange(it) },
                        onCalculateScore = { dailyActivityViewModel.onCalculateScore() },
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item {
                    WeeklySummarySection(
                        sleepAvg = weeklyStats.sleepAvg,
                        sleepScore = weeklyStats.sleepScore,
                        waterAvg = weeklyStats.waterAvg,
                        waterScore = weeklyStats.waterScore,
                        stepsAvg = weeklyStats.stepsAvg,
                        stepsScore = weeklyStats.stepsScore,
                        caloriesAvg = weeklyStats.caloriesAvg,
                        caloriesScore = weeklyStats.caloriesScore
                    )
                }
            }
        }
    }
}
@Composable
fun InputDataCard(
    sleepHours: Float,
    waterGlasses: Int,
    steps: Int,
    calories: Int,
    sleepScore: Float,
    waterScore: Float,
    stepsScore: Float,
    caloriesScore: Float,
    sleepMessage: String,
    waterMessage: String,
    stepsMessage: String,
    caloriesMessage: String,
    calculatedScore: Float,
    showCalculatedScore: Boolean,
    onSleepChange: (String) -> Unit,
    onWaterChange: (String) -> Unit,
    onStepsChange: (String) -> Unit,
    onCaloriesChange: (String) -> Unit,
    onCalculateScore: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Input Your Data",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1D26)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sleep Input
            InputRow(
                icon = Icons.Outlined.Bedtime,
                label = "Sleep (hours)",
                value = if (sleepHours == 0f) "" else sleepHours.toString(),
                onValueChange = onSleepChange,
                score = sleepScore,
                message = sleepMessage,
                iconColor = Color(0xFF9C27B0)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Water Input
            InputRow(
                icon = Icons.Outlined.WaterDrop,
                label = "Water (glasses)",
                value = if (waterGlasses == 0) "" else waterGlasses.toString(),
                onValueChange = onWaterChange,
                score = waterScore,
                message = waterMessage,
                iconColor = Color(0xFF2196F3)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Steps Input
            InputRow(
                icon = Icons.Outlined.DirectionsWalk,
                label = "Steps",
                value = if (steps == 0) "" else steps.toString(),
                onValueChange = onStepsChange,
                score = stepsScore,
                message = stepsMessage,
                iconColor = Color(0xFFFF5722)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Calories Input
            InputRow(
                icon = Icons.Outlined.LocalFireDepartment,
                label = "Calories",
                value = if (calories == 0) "" else calories.toString(),
                onValueChange = onCaloriesChange,
                score = caloriesScore,
                message = caloriesMessage,
                iconColor = Color(0xFFFF9800)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Calculate Button
            Button(
                onClick = onCalculateScore,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2A7DE1)
                )
            ) {
                Text(
                    text = "Calculate Average Score",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Show calculated score
            if (showCalculatedScore) {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Your Average Score",
                            fontSize = 14.sp,
                            color = Color(0xFF1B5E20)
                        )

                        Text(
                            text = String.format("%.1f", calculatedScore),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )

                        Text(
                            text = "out of 100",
                            fontSize = 12.sp,
                            color = Color(0xFF1B5E20)
                        )
                    }
                }
            }
        }
    }
}



@Composable
private fun InputRow(
    icon: ImageVector,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    score: Float,
    message: String,
    iconColor: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = Color(0xFF757575)
                )

                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = iconColor,
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        if (message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = message,
                fontSize = 12.sp,
                color = when {
                    message.contains("Invalid") -> Color(0xFFE53935)
                    score >= 15f -> Color(0xFF4CAF50)
                    else -> Color(0xFFFF9800)
                },
                modifier = Modifier.padding(start = 36.dp)
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
                value = if (value == "0" || value == "0.0") "" else value,
                onValueChange = { newValue ->
                    // Only allow digits and decimal point
                    if (newValue.isEmpty()) {
                        onValueChange("0")
                    } else if (newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                        onValueChange(newValue)
                    }
                },
                placeholder = {
                    Text(
                        text = "0",
                        color = Color(0xFF9AA6B2),
                        fontSize = 16.sp
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
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


// In Home.kt, update the DailyActivityCard
@Composable
fun DailyActivityCard(
    todayScore: Int,
    hasTodayScore: Boolean,
    onNavigateToDailyActivity: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToDailyActivity() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Assessment,
                    contentDescription = "Daily Activity",
                    tint = Color(0xFF2A7DE1),
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Daily Activity",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1D26)
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (hasTodayScore) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Today's Score: ",
                            fontSize = 13.sp,
                            color = Color(0xFF757575)
                        )
                        Text(
                            text = "$todayScore",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                        Text(
                            text = "/100",
                            fontSize = 13.sp,
                            color = Color(0xFF757575)
                        )
                    }
                } else {
                    Text(
                        text = "Track your health metrics",
                        fontSize = 13.sp,
                        color = Color(0xFF757575)
                    )
                }
            }

            // Arrow icon
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Go to Daily Activity",
                tint = Color(0xFF2A7DE1),
                modifier = Modifier.size(24.dp)
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
fun WeeklySummarySection(
    sleepAvg: Float? = null,
    sleepScore: Int? = null,
    waterAvg: Int? = null,
    waterScore: Int? = null,
    stepsAvg: Int? = null,
    stepsScore: Int? = null,
    caloriesAvg: Int? = null,
    caloriesScore: Int? = null
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Weekly Average Progress",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E2A3A)
            )
            Spacer(Modifier.height(16.dp))
            WeeklySummaryCard(
                icon = Icons.Outlined.Hotel,
                title = "Sleep",
                avgLabel = "hours",
                avg = sleepAvg ?: 0f,
                score = sleepScore ?: 0,
                bg = Color(0xFFF8F5FF)
            )
            Spacer(Modifier.height(12.dp))
            WeeklySummaryCard(
                icon = Icons.Outlined.WaterDrop,
                title = "Water",
                avgLabel = "glasses",
                avg = waterAvg?.toFloat() ?: 0f,
                score = waterScore ?: 0,
                bg = Color(0xFFF0F8FF)
            )
            Spacer(Modifier.height(12.dp))
            WeeklySummaryCard(
                icon = Icons.Outlined.DirectionsRun,
                title = "Steps",
                avgLabel = "steps",
                avg = stepsAvg?.toFloat() ?: 0f,
                score = stepsScore ?: 0,
                bg = Color(0xFFFFF5F0)
            )
            Spacer(Modifier.height(12.dp))
            WeeklySummaryCard(
                icon = Icons.Outlined.FavoriteBorder,
                title = "Calories",
                avgLabel = "kcal",
                avg = caloriesAvg?.toFloat() ?: 0f,
                score = caloriesScore ?: 0,
                bg = Color(0xFFFFF0F3)
            )
        }
    }
}
@Composable
private fun WeeklySummaryCard(
    icon: ImageVector,
    title: String,
    avgLabel: String,
    avg: Float,
    score: Int,
    bg: Color
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = bg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Color(0xFF9B6BFF), modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text(text = title, fontSize = 16.sp, color = Color(0xFF1E2A3A))
                Spacer(Modifier.weight(1f))
                Text(text = "Last 7 days", fontSize = 12.sp, color = Color(0xFF7A8899))
            }
            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Text(text = "Weekly Average", fontSize = 13.sp, color = Color(0xFF637083))
                    Text(
                        text = "${avg.toInt()} $avgLabel",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E2A3A)
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Text(text = "Score", fontSize = 13.sp, color = Color(0xFF637083))
                    Text(
                        text = "${score}/10",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E2A3A)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(Color(0xFFE8EBF0), RoundedCornerShape(3.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth((score / 10f).coerceIn(0f, 1f))
                        .height(6.dp)
                        .background(Color(0xFF9B6BFF), RoundedCornerShape(3.dp))
                )
            }
        }
    }
}





@Composable
@Preview
fun HomeViewPreview() {
    // Leave empty or provide a mock if needed
}