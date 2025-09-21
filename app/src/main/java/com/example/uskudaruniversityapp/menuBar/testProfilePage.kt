package com.example.uskudaruniversityapp.menuBar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.uskudaruniversityapp.dataClass.CourseProfilePage
import com.example.uskudaruniversityapp.dataClass.GpaHistory
import com.example.uskudaruniversityapp.dataClass.SemesterInfo
import com.example.uskudaruniversityapp.dataClass.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(navController: NavController) {
    Scaffold(  topBar = {
        TopAppBar(
            title = { Text("My Profile") },
            actions = {
                IconButton(onClick = {  navController.navigate("settings_route") }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }) {paddingValues ->

    LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { UserInfoSection(user = dummyUser) }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
            item { TodaysCoursesSection(courses = dummyCourses) }

            item { GpaChartSection(history = dummyGpaHistory) }
            item { SemesterHistorySection(semesters = dummySemesterHistory) }
        }
    }
}

@Composable
fun UserInfoSection(user: UserProfile) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = user.studentId,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Class: ${user.year}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(Modifier.height(12.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Current GPA",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "%.2f".format(user.gpa),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Current GPA",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun TodaysCoursesSection(courses: List<CourseProfilePage>) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Today's Courses",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (courses.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.EventNote,
                        contentDescription = "No Courses Scheduled",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "No courses scheduled for today!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Enjoy your day off or check your full schedule.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            courses.forEach { course ->
                CourseCard(
                    courseName = course.name,
                    time = course.time,
                    location = course.location,
                    breaks = course.breaks
                )
            }
        }
    }
}

@Composable
fun SemesterHistorySection(semesters: List<SemesterInfo>) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Semester Summary",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Semester Name",
                        Modifier.weight(2f),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Courses",
                        Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Credits",
                        Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "ECTS",
                        Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                semesters.forEachIndexed { index, semester ->
                    val rowBackgroundColor = if (index % 2 == 0) {
                        MaterialTheme.colorScheme.surfaceVariant
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                rowBackgroundColor,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(
                                vertical = 8.dp,
                                horizontal = 4.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            semester.name,
                            Modifier.weight(2f),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            semester.coursesTaken.toString(),
                            Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            semester.totalCredits.toString(),
                            Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            semester.totalEcts.toString(),
                            Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    if (index < semesters.size - 1) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun CourseCard(
    courseName: String,
    time: String,
    location: String,
    breaks: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = courseName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoRow(icon = Icons.Default.Videocam, text = "Class: $location")
                InfoRow(icon = Icons.Default.Schedule, text = time)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                InfoRow(
                    icon = Icons.Default.HourglassTop,
                    text = "$breaks Break${if (breaks > 1) "s" else ""}"
                )
            }
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f),
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
        )
    }
}


@Composable
fun GpaChartSection(history: List<GpaHistory>) {
    var selectedYear by remember { mutableStateOf<String?>(null) }

    val chartHeight = 140.dp
    val maxGpa = 4.0f
    val gpaSteps = 5
    val schema = MaterialTheme.colorScheme

    // Calculate average GPA for the line
    val averageGpa = remember(history) {
        if (history.isEmpty()) 0f else history.map { it.gpa }.average().toFloat()
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Total GPA History",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = schema.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                // Y-axis labels
                Column(
                    modifier = Modifier
                        .height(chartHeight)
                        .width(40.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    for (i in gpaSteps - 1 downTo 0) {
                        Text(
                            text = "%.1f".format(i * (maxGpa / (gpaSteps - 1))),
                            style = MaterialTheme.typography.bodySmall,
                            color = schema.onSurfaceVariant
                        )
                    }
                }

                // Chart bars and average GPA line
                Row(
                    modifier = Modifier
                        .height(chartHeight)
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                        .drawBehind {
                            val stepHeight = size.height / (gpaSteps - 1)
                            val lineColor = schema.outline.copy(alpha = 0.3f)

                            // Draw horizontal grid lines
                            for (i in 0 until gpaSteps) {
                                val y = i * stepHeight
                                drawLine(
                                    color = lineColor,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }


                            if (history.isNotEmpty()) {
                                val averageGpaY = size.height * (1f - (averageGpa / maxGpa))
                                drawLine(
                                    color = schema.primary,
                                    start = Offset(0f, averageGpaY),
                                    end = Offset(size.width, averageGpaY),
                                    strokeWidth = 2.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(
                                        floatArrayOf(10f, 10f),
                                        0f
                                    ) // Dashed line
                                )
                            }
                        },
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Bottom
                ) {
                    history.forEach { item ->
                        val isCurrentlySelected = selectedYear == item.year
                        val isAnySelected = selectedYear != null

                        val alpha: Float by animateFloatAsState(
                            targetValue = if (!isAnySelected || isCurrentlySelected) 1f else 0.4f,
                            label = "barAlpha"
                        )

                        val barScaleX: Float by animateFloatAsState(
                            targetValue = if (isCurrentlySelected) 1.1f else 1f,
                            label = "barScaleX"
                        )
                        val barScaleY: Float by animateFloatAsState(
                            targetValue = if (isCurrentlySelected) 1.05f else 1f,
                            label = "barScaleY"
                        )

                        val barHeightRatio = item.gpa / maxGpa
                        val barDpHeight = chartHeight * barHeightRatio
                        val minLabelHeight = 24.dp

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier
                                .width(50.dp)
                                .alpha(alpha)
                                .clickable {
                                    selectedYear =
                                        if (selectedYear == item.year) null else item.year
                                }
                        ) {
                            Text(
                                text = "%.2f".format(item.gpa),
                                style = MaterialTheme.typography.bodySmall,
                                color = schema.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )


                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(barDpHeight)
                                    .graphicsLayer {
                                        scaleX = barScaleX
                                        scaleY = barScaleY
                                        transformOrigin =
                                            TransformOrigin(
                                                0.5f,
                                                1f
                                            )
                                    }
                                    .background(
                                        schema.primary,
                                        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                    )
                                    .border(
                                        width = if (isCurrentlySelected) 2.dp else 0.dp,
                                        color = if (isCurrentlySelected) schema.secondary else Color.Unspecified,
                                        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                    ),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                if (barDpHeight >= minLabelHeight) {
                                    Text(
                                        text = item.year,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = schema.onPrimary,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                            }

                            if (barDpHeight < minLabelHeight) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.year,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = schema.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
            if (history.isNotEmpty()) {
                Text(
                    text = "Overall Average GPA: %.2f".format(averageGpa),
                    style = MaterialTheme.typography.bodySmall,
                    color = schema.onSurfaceVariant,
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                )
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewGpaChart() {
    val sampleHistory = listOf(
        GpaHistory("1st year", 2.75f),
        GpaHistory("2nd year", 3.25f),
        GpaHistory("3rd year", 1.50f),
        GpaHistory("4th year", 3.80f),
        GpaHistory("5th year", 3.90f)
    )
    MaterialTheme {
        GpaChartSection(history = sampleHistory)
    }
}


// --- Dummy Data ---
val dummyUser = UserProfile(
    name = "MUHAMMED SUAYB",
    studentId = "210209337",
    email = "muhammed.suayb@st.uskudar.edu.tr",
    year = "4th",
    gpa = 1.00
)

val dummyCourses = listOf(
    CourseProfilePage("Graduation Thesis", "09:40 - 11:30", "Online", 1),
    CourseProfilePage("Occupational Health and Safety", "10:40 - 13:30", "Online", 3),
    CourseProfilePage("Theoretical and Computational Neuroscience", "11:40 - 14:30", "MERKEZ-NT", 2)
)

val dummyEmptyCourses = emptyList<CourseProfilePage>()


val dummyGpaHistory = listOf(
    GpaHistory("1st year", 2.75f),
    GpaHistory("2nd year", 3.25f),
    GpaHistory("3rd year", 1.50f),
    GpaHistory("4th year", 3.80f)
)

val dummySemesterHistory = listOf(
    SemesterInfo("2024-2025 Güz", 7, 22, 40),
    SemesterInfo("2023-2024 Güz", 10, 27, 43),
    SemesterInfo("2022-2023 Güz", 10, 31, 45),
    SemesterInfo("2021-2022 Güz", 8, 25, 42),
    SemesterInfo("2021-2022 Bahar", 9, 28, 44)
)