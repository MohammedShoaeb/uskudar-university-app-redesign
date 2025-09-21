package com.example.uskudaruniversityapp.quickMenuPGs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController



data class RouteStop(
    val name: String,
    val isMajorStop: Boolean = false
)

data class Route(
    val startPoint: String,
    val destinationPoint: String,
    val stops: List<RouteStop>
)

data class ScheduleEntry(
    val time: String
)

val dummyRoute = Route(
    startPoint = "Merkez Yerleske",
    destinationPoint = "Carsi Yerleske",
    stops = listOf(
        RouteStop("Merkez Yerleske", isMajorStop = true),
        RouteStop("Öğrenci Yurdu"),
        RouteStop("Carsi Yerleske", isMajorStop = true)
    )
)

val dummySchedule = listOf(
    ScheduleEntry("7:30"),
    ScheduleEntry("7:45"),
    ScheduleEntry("8:00"),
    ScheduleEntry("8:15"),
    ScheduleEntry("8:30"),
    ScheduleEntry("8:45"),
    ScheduleEntry("9:00"),
    ScheduleEntry("9:15"),
    ScheduleEntry("9:30"),
    ScheduleEntry("9:45"),
    ScheduleEntry("10:00"),
    ScheduleEntry("10:15"),
    ScheduleEntry("10:30")
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RingHoursPage(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shuttle Services") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                RouteOverview(dummyRoute)
            }

            item {
                Text(
                    text = "Route Map",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                RouteMapVisualization(dummyRoute.stops)
            }

            item {
                Text(
                    text = "Working Hours",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                WorkingHoursList(dummySchedule)
            }
        }
    }
}

@Composable
fun RouteOverview(route: Route) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Starting Point",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Starting Point",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = route.startPoint,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Route Arrow",
                modifier = Modifier
                    .size(48.dp)
                    .padding(horizontal = 8.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.Flag,
                    contentDescription = "Destination",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Destination",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = route.destinationPoint,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun RouteMapVisualization(stops: List<RouteStop>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            val lineColor = MaterialTheme.colorScheme.primary
            val dotColor = MaterialTheme.colorScheme.secondary
            val majorDotColor = MaterialTheme.colorScheme.primary

            val stopHeight = 60.dp
            val totalHeight = (stops.size * stopHeight.value).dp

            stops.forEachIndexed { index, stop ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(stopHeight),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(stopHeight)
                            .background(Color.Transparent)
                    ) {
                        if (index < stops.size - 1) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val startY = size.height / 2
                                val endY = size.height
                                drawLine(
                                    color = lineColor,
                                    start = Offset(x = center.x, y = 0f),
                                    end = Offset(x = center.x, y = size.height),
                                    strokeWidth = 4f,
                                    cap = StrokeCap.Round,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                )
                            }
                        }
                    }


                    Canvas(modifier = Modifier.size(24.dp)) {
                        drawCircle(
                            color = if (stop.isMajorStop) majorDotColor else dotColor,
                            radius = size.minDimension / 2 - 2.dp.toPx(),
                            center = center
                        )
                        if (stop.isMajorStop) {

                            drawCircle(
                                color = Color.White,
                                radius = size.minDimension / 2 - 6.dp.toPx(),
                                center = center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))


                    Text(
                        text = stop.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (stop.isMajorStop) FontWeight.Bold else FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}


@Composable
fun WorkingHoursList(schedule: List<ScheduleEntry>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            schedule.forEach { entry ->
                ListItem(
                    headlineContent = { Text(entry.time) },
                    leadingContent = {
                        Icon(
                            Icons.Default.AccessTime,
                            contentDescription = "Time",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                )
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PreviewRingHoursPage() {
    MaterialTheme {
//        RingHoursPage()
    }
}