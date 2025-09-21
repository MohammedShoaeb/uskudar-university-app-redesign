package com.example.uskudaruniversityapp.quickMenuPGs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp



data class ClassItem(
    val day: String,
    val courseName: String,
    val startTime: String,
    val endTime: String,
    val classType: String,
    val breaks: Int
)

@Composable
fun ClassCard(classItem: ClassItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = classItem.courseName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Class Time",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${classItem.startTime} - ${classItem.endTime}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val icon = if (classItem.classType == "Online") Icons.Default.Computer else Icons.Default.LocationOn
                    Icon(
                        imageVector = icon,
                        contentDescription = "Class Type",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = classItem.classType,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (classItem.breaks > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${classItem.breaks} Breaks",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun WeeklySchedulePage() {
    val scheduleItems = remember {
        listOf(
            ClassItem("Monday", "Digital Media Translation", "14:40", "17:30", "Online", 2),
            ClassItem("Tuesday", "Graduation Thesis", "09:40", "11:30", "Online", 1),
            ClassItem("Tuesday", "Occupational Health and Safety", "10:40", "13:30", "Online", 3),
            ClassItem("Tuesday", "Theoretical and Computational Neuroscience", "11:40", "14:30", "MERKEZ-NT", 2),
            ClassItem("Wednesday", "Research Methods", "09:00", "11:00", "Online", 1),
            ClassItem("Wednesday", "Advanced Algorithms", "13:00", "16:00", "MERKEZ-NT", 2),
            ClassItem("Thursday", "Data Science and Analytics", "08:40", "11:30", "Online", 4),
            ClassItem("Thursday", "Analysis and Design of User Interface", "11:40", "14:30", "Online", 2),
            ClassItem("Friday", "Ethics in AI", "13:00", "15:00", "Online", 1)
        )
    }


    val groupedSchedule = scheduleItems.groupBy { it.day }


    val expandedState = remember { mutableStateMapOf<String, Boolean>().apply {
        groupedSchedule.keys.forEach { day -> put(day, true) }
    }}


    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        if (scheduleItems.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "No classes scheduled for this week.",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.surfaceContainer),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
            ) {
                groupedSchedule.forEach { (day, classes) ->
                    item {

                        DayHeader(
                            day = day,
                            isExpanded = expandedState[day] ?: true,
                            onClick = {
                                expandedState[day] = !(expandedState[day] ?: true)
                            }
                        )
                    }

                    item {
                        AnimatedVisibility(
                            visible = expandedState[day] ?: true,
                            enter = expandVertically(animationSpec = tween(durationMillis = 300)),
                            exit = shrinkVertically(animationSpec = tween(durationMillis = 300))
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                classes.forEach { classItem ->
                                    ClassCard(classItem = classItem) {
                                        println("Clicked on ${classItem.courseName} on ${classItem.day}")

                                    }
                                }
                            }
                        }
                    }
                    item {

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}






@Composable
fun DayHeader(day: String, isExpanded: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()

            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Icon(
            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = if (isExpanded) "Collapse" else "Expand",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(24.dp)
        )
    }
}






@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PreviewWeeklySchedulePage() {
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            WeeklySchedulePage()
        }
    }
}


