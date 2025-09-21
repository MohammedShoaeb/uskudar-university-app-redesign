package com.example.uskudaruniversityapp.menuBar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.provider.CalendarContract
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.example.uskudaruniversityapp.R
import java.net.URLEncoder
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


sealed class UpdateItem {
    data class Announcement(
        val id: String,
        val title: String,
        val content: String,
        val date: LocalDate,
        val source: String
    ) : UpdateItem()

    data class News(
        val id: String,
        val title: String,
        val imageUrl: Int,
        val date: LocalDate
    ) : UpdateItem()

    data class Activity(
        val id: String,
        val title: String,
        val location: String,
        val date: LocalDate,
        val time: String
    ) : UpdateItem()
}

@SuppressLint("QueryPermissionsNeeded")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun UpdatesPage(navController: NavController) {
    val announcements = remember {
        listOf(
            UpdateItem.Announcement(
                id = "ann1",
                title = "About Suspension of the Educational Activities",
                content = "Educational activities at our university will be suspended on Thursday, April 24 and Friday, April 25. This announcement is for the attention of all our students.",
                date = LocalDate.of(2025, 4, 23),
                source = "Uskudar University Presidency"
            ),
            UpdateItem.Announcement(
                id = "ann2",
                title = "Important Exam Schedule Change",
                content = "Due to unforeseen circumstances, the final exam schedule has been updated. Please check the new schedule on the student portal.",
                date = LocalDate.of(2025, 5, 10),
                source = "Registrar's Office"
            )
        )
    }

    val newsList = remember {
        listOf(
            UpdateItem.News(
                id = "news1",
                title = "Prof. Nevzat Tarhan: “Young people follow footprints, not words”",
                imageUrl = R.drawable.a,
                date = LocalDate.of(2024, 4, 22)
            ),
            UpdateItem.News(
                id = "news2",
                title = "University Ranks Top in Innovation Index",
                imageUrl = R.drawable.b,
                date = LocalDate.of(2025, 3, 15)
            ),
            UpdateItem.News(
                id = "news3",
                title = "Awareness through art during World Disability Week…",
                imageUrl = R.drawable.c,
                date = LocalDate.of(2024, 4, 22)
            )
        )
    }

    val activities = remember {
        listOf(
            UpdateItem.Activity(
                id = "act1",
                title = "Istanbul Spine Master",
                location = "Location",
                date = LocalDate.of(2025, 4, 27),
                time = "8:00 - 17:00"
            ),
            UpdateItem.Activity(
                id = "act2",
                title = "Spring Festival Concert",
                location = "Main Campus Auditorium",
                date = LocalDate.of(2025, 5, 20),
                time = "19:00 - 22:00"
            )
        )
    }

    val tabs = listOf("Announcement", "News", "Activities")
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                TopAppBar(
                    title = { Text(text = "Updates", color = MaterialTheme.colorScheme.onSurface) },
                    navigationIcon = {
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                )
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) },
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            when (tabs[selectedTabIndex]) {
                "Announcement" -> {
                    items(announcements) { announcement ->
                        AnnouncementCard(
                            announcement = announcement,
                            onShareClick = { shareContent ->
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, shareContent)
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, null)
                                if (shareIntent.resolveActivity(context.packageManager) != null) {
                                    context.startActivity(shareIntent)
                                } else {
                                    println("No app found to handle sharing.")
                                }
                            })
                    }
                }

                "News" -> {
                    items(newsList) { news ->
                        NewsCard(
                            news = news,
                            navController = navController
                        )
                    }
                }

                "Activities" -> {
                    items(activities) { activity ->
                        ActivityCard(
                            activity = activity,
                            onViewMapClick = {
                                val gmmIntentUri = "geo:0,0?q=Üsküdar University".toUri()
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.setPackage("com.google.android.apps.maps")

                                if (mapIntent.resolveActivity(context.packageManager) != null) {
                                    context.startActivity(mapIntent)
                                } else {
                                    println("Google Maps app not found.")
                                }
                            }, onAddToCalendarClick = { activityToAddToCalendar ->
                            }
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnnouncementCard(
    announcement: UpdateItem.Announcement,
    onShareClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = announcement.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = announcement.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = announcement.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = announcement.source,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                TextButton(
                    onClick = {
                        val shareText =
                            "${announcement.title}\n\n${announcement.content}\n\nSource: ${announcement.source}"
                        onShareClick(shareText)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Share")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsCard(
    news: UpdateItem.News,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val encodedTitle = URLEncoder.encode(news.title, "UTF-8")
                val formattedDate = news.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                val encodedDate = URLEncoder.encode(formattedDate, "UTF-8")

                navController.navigate("news_detail/$encodedTitle/${news.imageUrl}/$encodedDate")
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = news.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = news.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            TextButton(onClick = {
                val encodedTitle = URLEncoder.encode(news.title, "UTF-8")
                val formattedDate = news.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                val encodedDate = URLEncoder.encode(formattedDate, "UTF-8")

                navController.navigate("news_detail/$encodedTitle/${news.imageUrl}/$encodedDate")
            }) {
                Text("View More", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivityCard(
    activity: UpdateItem.Activity,
    onViewMapClick: () -> Unit,
    onAddToCalendarClick: (UpdateItem.Activity) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = activity.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = activity.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = onViewMapClick) {
                    Text("View Map", color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Date",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = activity.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.Timelapse,
                    contentDescription = "Time",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = activity.time,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            AddtoCalendarButton(activity = activity) {
                onAddToCalendarClick(activity)
            }
        }
    }
}

@SuppressLint("QueryPermissionsNeeded")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddtoCalendarButton(activity: UpdateItem.Activity, onClick: (UpdateItem.Activity) -> Unit) {
    val context = LocalContext.current

    Button(
        onClick = {
            onClick(activity)

            val intent = Intent(Intent.ACTION_INSERT).apply {
                setData(CalendarContract.Events.CONTENT_URI)
                putExtra(CalendarContract.Events.TITLE, activity.title)
                putExtra(CalendarContract.Events.EVENT_LOCATION, activity.location)

                val timeParts = activity.time.split(" - ")
                if (timeParts.size == 2) {
                    val startTimeStr = timeParts[0]
                    val endTimeStr = timeParts[1]

                    val startHour = startTimeStr.substringBefore(":").toInt()
                    val startMinute = startTimeStr.substringAfter(":").toInt()
                    val endHour = endTimeStr.substringBefore(":").toInt()
                    val endMinute = endTimeStr.substringAfter(":").toInt()

                    val startMillis = activity.date.atTime(startHour, startMinute)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                    val endMillis = activity.date.atTime(endHour, endMinute)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()

                    putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                    putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
                } else {
                    putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                }

                putExtra(CalendarContract.Events.DESCRIPTION, "Details for ${activity.title}")
            }
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                println("No calendar app found to handle the intent.")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text("Add to Calendar", color = MaterialTheme.colorScheme.onPrimary)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewUpdatesPage() {
    MaterialTheme {
    }
}