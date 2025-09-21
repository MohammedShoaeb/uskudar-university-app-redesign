package com.example.uskudaruniversityapp.quickMenuPGs


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPagerIndicator


data class RouteInfo(
    val start: String,
    val destination: String,
    val stops: List<String>,
    val times: List<String>
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartDestinationCard(route: RouteInfo) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Current Route",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Start",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                    )
                    Spacer(Modifier.height(4.dp))
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Start",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = route.start,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Route Direction",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(24.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Destination",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                    )
                    Spacer(Modifier.height(4.dp))
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = "Destination",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = route.destination,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}








@OptIn(ExperimentalPagerApi::class)
@Composable
fun TwoStepsPager(stops: List<String>) {
    val totalPages = (stops.size + 1) / 2
    val pagerState = rememberPagerState(pageCount = { totalPages })

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Text(
            text = "Route Steps",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            val firstIndex = page * 2
            val secondIndex = firstIndex + 1
            val isLastPageOfPager = page == pagerState.pageCount - 1
            val schema = MaterialTheme.colorScheme

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val firstLabel = when (firstIndex) {
                        0 -> "Start"
                        stops.lastIndex -> "Finish"
                        else -> "Step ${firstIndex}"
                    }
                    val secondLabel = when {
                        secondIndex >= stops.size -> ""
                        secondIndex == stops.lastIndex -> "Finish"
                        secondIndex == 1 -> "Step 1"
                        else -> "Step ${secondIndex}"
                    }

                    Text(
                        text = firstLabel,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )

                    if (secondLabel.isNotEmpty()) {
                        Text(
                            text = secondLabel,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }


                Spacer(modifier = Modifier.height(4.dp))

                Canvas(modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)) {
                    val canvasWidth = size.width
                    val centerY = size.height / 2
                    val dotRadius = 8.dp.toPx()
                    val strokeWidth = 3.dp.toPx()

                    val lineColor = schema.primary.copy(alpha = 0.6f)
                    val borderColor = schema.onSurfaceVariant.copy(alpha = 0.6f)

                    val firstDotX = canvasWidth * 0.25f
                    val secondDotX = canvasWidth * 0.75f

                    if (page > 0) {
                        drawLine(
                            lineColor,
                            Offset(0f, centerY),
                            Offset(firstDotX - dotRadius, centerY),
                            strokeWidth = strokeWidth
                        )
                    }

                    drawCircle(lineColor, dotRadius, Offset(firstDotX, centerY))
                    drawCircle(borderColor, dotRadius, Offset(firstDotX, centerY), style = Stroke(width = 1.5.dp.toPx()))

                    if (secondIndex < stops.size) {
                        drawLine(
                            lineColor,
                            Offset(firstDotX + dotRadius, centerY),
                            Offset(secondDotX - dotRadius, centerY),
                            strokeWidth = strokeWidth
                        )

                        drawCircle(lineColor, dotRadius, Offset(secondDotX, centerY))
                        drawCircle(borderColor, dotRadius, Offset(secondDotX, centerY), style = Stroke(width = 1.5.dp.toPx()))

                        if (!isLastPageOfPager) {
                            drawLine(
                                lineColor,
                                Offset(secondDotX + dotRadius, centerY),
                                Offset(canvasWidth, centerY),
                                strokeWidth = strokeWidth
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val firstStopText = stops[firstIndex]
                    val secondStopText = if (secondIndex < stops.size) stops[secondIndex] else ""

                    Text(
                        text = firstStopText,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (secondStopText.isNotEmpty()) {
                        Text(
                            text = secondStopText,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Showing steps ${firstIndex + 1} - ${minOf(secondIndex + 1, stops.size)} of ${stops.size}",
                    style = MaterialTheme.typography.labelSmall,
                    color = schema.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WorkingHours(times: List<String>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Working Hours",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f)),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (times.isEmpty()) {
                    Text(
                        text = "No schedule available for this route.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                } else {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        times.forEach { time ->
                            AssistChip(
                                onClick = { /* Do nothing or show detail of this time */ },
                                label = { Text(time) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                    labelColor = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RingHoursPages(navController: NavController) {

    val routes = listOf(
        RouteInfo(
            start = "Market Campus",
            destination = "Market Campus",
            stops = listOf(
                "Market Campus",
                "Student Dormitory",
                "Central Campus",
                "South Campus",
                "Metrobus",
                "Central Campus",
                "Market Campus"
            ),
            times = listOf(
                "7:30", "7:45", "8:00", "8:15", "8:20", "8:30", "8:45", "9:00",
                "9:15", "9:30", "9:45", "10:00", "10:30", "11:00", "11:30",
                "12:00", "12:30", "13:00", "13:30", "14:30", "15:00", "15:30",
                "16:20", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45",
                "18:00", "18:15", "18:30", "19:00", "19:30", "20:00", "20:30",
                "21:00", "22:00", "23:00"
            )
        ),
        RouteInfo(
            start = "Central Campus",
            destination = "Market Campus",
            stops = listOf(
                "Central Campus",
                "Student Dormitory",
                "Market Campus"
            ),
            times = listOf(
                "7:30", "7:45", "8:00", "8:15", "8:30", "8:45", "9:00",
                "9:15", "9:30", "9:45", "10:00", "10:30", "11:00", "11:30",
                "12:00", "12:30", "13:00", "13:30", "14:00",
                "14:30", "15:00", "15:30", "16:00", "16:15", "16:30", "16:45",
                "17:00", "17:15", "17:30", "17:45", "18:00",
                "18:15", "18:30", "19:00", "19:30", "20:00",
                "21:30", "22:30"
            )
        ),
        RouteInfo(
            start = "Metrobus",
            destination = "Market Campus",
            stops = listOf(
                "Metrobus",
                "Central Campus",
                "South Campus",
                "Market Campus"
            ),
            times = listOf(
                "7:45", "8:00", "8:15", "08:30", "08:45", "9:00",
                "9:15", "9:30", "9:45", "10:00",
                "10:30", "11:00", "11:30", "12:00", "12:30", "13:00",
                "13:30", "14:00",
                "14:30", "15:00", "15:30", "16:00",
                "16:15", "16:30", "16:45", "17:00",
                "17:15", "17:45", "18:30", "19:00", "19:30", "20:00", "20:30",
                "21:15", "22:15", "22:30"
            )
        ),
        RouteInfo(
            start = "Market Campus",
            destination = "NP Health Campus",
            stops = listOf(
                "Market Campus",
                "Student Dormitory",
                "Central Campus",
                "South Campus",
                "NPIstanbul Hospital",
                "NP Health Campus"
            ),
            times = listOf(
                "08:00",
                "8:30", "9:00",
                "9:30", "10:30", "12:30", "14:30", "16:20"
            )
        ),
        RouteInfo(
            start = "NP Health Campus",
            destination = "Market Campus",
            stops = listOf(
                "NP Health Campus",
                "NPIstanbul Hospital",
                "Central Campus",
                "Student Dormitory",
                "Market Campus"
            ),
            times = listOf(
                "08:10", "8:30", "9:00",
                "9:30", "10:00",
                "11:10", "13:10", "15:10", "17:10", "17:40"
            )
        )
    )

    val pagerState = rememberPagerState(pageCount = { routes.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) { page ->
                StartDestinationCard(route = routes[page])
            }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                pageCount = routes.size,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp),
                activeColor = MaterialTheme.colorScheme.primary,
                inactiveColor = MaterialTheme.colorScheme.outline
            )

            val currentPageRoute = routes[pagerState.currentPage]

            Spacer(modifier = Modifier.height(24.dp))

            TwoStepsPager(stops = currentPageRoute.stops)

            Spacer(modifier = Modifier.height(24.dp))

            WorkingHours(times = currentPageRoute.times)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PreviewRingHoursPages() {
    MaterialTheme {
    }
}