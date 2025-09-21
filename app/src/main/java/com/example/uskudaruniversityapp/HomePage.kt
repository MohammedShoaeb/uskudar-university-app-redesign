package com.example.uskudaruniversityapp

import android.content.Intent
import android.os.Build
import android.provider.CalendarContract
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.example.uskudaruniversityapp.dataClass.ServiceItem
import com.example.uskudaruniversityapp.dataClass.serviceItems
import com.example.uskudaruniversityapp.menuBar.ActivityCard
import com.example.uskudaruniversityapp.menuBar.AnnouncementCard
import com.example.uskudaruniversityapp.menuBar.NewsCard
import com.example.uskudaruniversityapp.menuBar.UpdateItem
import com.example.uskudaruniversityapp.notificationPages.NotificationItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    navController: NavController,
    favoritedServiceNames: List<String> = emptyList(),
    hasUnreadNotifications: Boolean,
    notifications: List<NotificationItem>,
    onUpdateFavorites: (List<String>) -> Unit,
) {
    val maxFavorites = 8
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var isEditing by remember { mutableStateOf(false) }
    var tempFavorites by remember { mutableStateOf(favoritedServiceNames.toMutableList()) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }
    val markedForDeletion = remember { mutableStateListOf<String>() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_img),
                            contentDescription = "University Logo",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Welcome,", style = MaterialTheme.typography.titleMedium)
                            Text(
                                "Muhammed Suayb.",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                    Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                        IconButton(onClick = { navController.navigate("notifications_list") }) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        if (hasUnreadNotifications) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-4).dp, y = 4.dp)
                            )
                        }
                    }
                }
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {
            BannerPagerWithIndicator()
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    "My Platforms",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    listOf(
                        PlatformItem("STIX", R.drawable.stix_icon),
                        PlatformItem("OBS", R.drawable.obs_icon),
                        PlatformItem("ALMS", R.drawable.alms_icon)
                    ).forEach { platform ->
                        PlatformCard(platform) {
                            val url = when (platform.name) {
                                "OBS" -> "https://obs.uskudar.edu.tr"
                                "ALMS" -> "https://lms.uskudar.edu.tr/almsp"
                                else -> null
                            }
                            if (url != null) {
                                navController.navigate(
                                    "webview_screen/${
                                        URLEncoder.encode(
                                            url,
                                            StandardCharsets.UTF_8.toString()
                                        )
                                    }"
                                )
                            } else if (platform.name == "STIX") {
                                navController.navigate("stix_graph")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                FavoriteServicesSection(
                    favoritedServiceNames = favoritedServiceNames,
                    isEditing = isEditing,
                    tempFavorites = tempFavorites,
                    markedForDeletion = markedForDeletion,
                    onEditClick = { isEditing = true },
                    onCancelClick = {
                        if (tempFavorites != favoritedServiceNames || markedForDeletion.isNotEmpty()) {
                            showCancelDialog = true
                        } else {
                            isEditing = false
                            tempFavorites = favoritedServiceNames.toMutableList()
                            markedForDeletion.clear()
                        }
                    },
                    onSaveClick = {
                        if (tempFavorites != favoritedServiceNames || markedForDeletion.isNotEmpty()) {
                            showSaveDialog = true
                        } else {
                            isEditing = false
                        }
                    },
                    onDeleteClick = { serviceName ->
                        markedForDeletion.add(serviceName)
                    },
                    onUndoDeleteClick = { serviceName ->
                        markedForDeletion.remove(serviceName)
                    },
                    onNavigateToQuickMenu = {
                        navController.navigate("quick_menu")
                    },
                    navController = navController,
                    onServiceCardClick = {
                        navController.navigate("service_item_detail/${it.text}")

                    }
                )


            }
            Spacer(modifier = Modifier.height(24.dp))
            LatestUpdatesSection(navController)
        }
    }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showSaveDialog = false
                    isEditing = false
                    tempFavorites.removeAll(markedForDeletion)
                    markedForDeletion.clear()
                    onUpdateFavorites(tempFavorites)
                    scope.launch {
                        snackbarHostState.showSnackbar("Favorites saved.")
                    }
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Dismiss")
                }
            },
            title = { Text("Save Changes") },
            text = { Text("Are you sure you want to save changes to your favorites?") }
        )
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    tempFavorites = favoritedServiceNames.toMutableList()
                    markedForDeletion.clear()
                    isEditing = false
                    showCancelDialog = false
                    scope.launch { snackbarHostState.showSnackbar("Changes discarded.") }
                }) {
                    Text("Discard")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("Keep Editing")
                }
            },
            title = { Text("Discard Changes") },
            text = { Text("Are you sure you want to discard your changes?") }
        )
    }
}


@Composable
fun FavoriteServicesSection(
    favoritedServiceNames: List<String>,
    isEditing: Boolean,
    tempFavorites: List<String>,
    markedForDeletion: List<String>,
    maxFavorites: Int = 8,
    onEditClick: () -> Unit,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: (String) -> Unit,
    onUndoDeleteClick: (String) -> Unit,
    onNavigateToQuickMenu: () -> Unit,
    navController: NavController,
    onServiceCardClick: (ServiceItem) -> Unit

) {
    val favoritedServices = serviceItems.filter { it.text in tempFavorites }.take(maxFavorites)

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "My Favorite Services (${tempFavorites.size}/8)",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )

            if (favoritedServices.isNotEmpty() || isEditing) {
                Row {
                    if (!isEditing) {
                        TextButton(onClick = onEditClick) { Text("Edit") }
                    } else {
                        TextButton(onClick = onCancelClick) { Text("Cancel") }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = onSaveClick) { Text("Save") }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        if (favoritedServices.isEmpty() && !isEditing) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No favorites yet.", style = MaterialTheme.typography.bodyLarge)
                        Text("Go to 'Menu' and favorite some services!", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (favoritedServices.size <= 4) 100.dp else 200.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                userScrollEnabled = false
            ) {
                items(favoritedServices) { service ->
                    FavoriteItemCard(
                        service = service,
                        isEditing = isEditing,
                        isMarkedForDeletion = markedForDeletion.contains(service.text),
                        onDeleteClick = { onDeleteClick(service.text) },
                        onUndoDeleteClick = { onUndoDeleteClick(service.text) },
                        onClick = onServiceCardClick
                    )
                }
                if (!isEditing && favoritedServices.size < maxFavorites) {
                    item {
                        AddMoreCard(onClick = onNavigateToQuickMenu)
                    }
                }
            }

        }
    }
}

@Composable
fun FavoriteItemCard(
    service: ServiceItem,
    isEditing: Boolean = false,
    isMarkedForDeletion: Boolean = false,
    onDeleteClick: () -> Unit,
    onUndoDeleteClick: () -> Unit ,
    onClick: (ServiceItem) -> Unit

) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .alpha(if (isMarkedForDeletion) 0.3f else 1f)
    ) {
        Card(
            modifier = Modifier
                .aspectRatio(1f)
                .clickable(enabled = !isEditing) { if (!isEditing) {
                    onClick(service)
                }},
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = service.icon,
                    contentDescription = service.text,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = service.text,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Visible,
                    softWrap = true
                )
            }
        }

        if (isEditing) {
            IconButton(
                onClick = if (isMarkedForDeletion) onUndoDeleteClick else onDeleteClick,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopEnd)
                    .background(
                        if (isMarkedForDeletion) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                        CircleShape
                    )
                    .padding(1.dp)
            ) {
                Icon(
                    imageVector = if (isMarkedForDeletion) Icons.Default.Add else Icons.Default.Close,
                    contentDescription = if (isMarkedForDeletion) "Undo Remove" else "Remove",
                    tint = if (isMarkedForDeletion) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
fun AddMoreCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { onClick() }
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = 0.3f
            )
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add More Favorites",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Add More",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerPagerWithIndicator() {
    val bannerImages = listOf(
        R.drawable.carsi,
        R.drawable.altunizade,
        R.drawable.a,
        R.drawable.b,
        R.drawable.c,

        )

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

    val actualPageCount = bannerImages.size
    val pagerState = rememberPagerState(initialPage = Int.MAX_VALUE / 2) {
        Int.MAX_VALUE
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val pageWidth = screenWidth * 0.9f
    val horizontalPadding = (screenWidth - pageWidth) / 2


    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000)
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fixed(pageWidth),
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            pageSpacing = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { index ->
            val page = index % actualPageCount
            Image(
                painter = painterResource(id = bannerImages[page]),
                contentDescription = "Banner $page",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp))
            )
        }


    }
}


data class PlatformItem(
    val name: String,
    val iconResId: Int,
)

@Composable
fun PlatformCard(
    platform: PlatformItem,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier

            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = platform.iconResId),
                contentDescription = platform.name,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = platform.name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LatestUpdatesSection(navController: NavController) {
    val context = LocalContext.current


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
            ),
            UpdateItem.Announcement(
                id = "ann3",
                title = "Library Extended Hours",
                content = "The university library will operate with extended hours during the exam period, from 8 AM to midnight.",
                date = LocalDate.of(2025, 5, 1),
                source = "University Library"
            )
        ).sortedByDescending { it.date }
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
            ),
            UpdateItem.News(
                id = "news4",
                title = "New Research Grant Awarded to Engineering Faculty",
                imageUrl = R.drawable.a,
                date = LocalDate.of(2025, 6, 10)
            )
        ).sortedByDescending { it.date }
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
            ),
            UpdateItem.Activity(
                id = "act3",
                title = "Career Fair 2025",
                location = "Sports Hall",
                date = LocalDate.of(2025, 6, 15),
                time = "10:00 - 16:00"
            )
        ).sortedByDescending { it.date }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Latest Announcements",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

            }
            Spacer(modifier = Modifier.height(12.dp))
            if (announcements.isEmpty()) {
                Text(
                    "No recent announcements.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                announcements.take(2).forEachIndexed { index, announcement ->
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
                                println("No app found to handle sharing for announcement.")
                            }
                        }
                    )
                    if (index < announcements.take(2).size - 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))


        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Latest News",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

            }
            Spacer(modifier = Modifier.height(12.dp))
            if (newsList.isEmpty()) {
                Text(
                    "No recent news.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                newsList.take(2).forEachIndexed { index, news ->
                    NewsCard(news = news, navController = navController)
                    if (index < newsList.take(2).size - 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))


        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Latest Activities",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

            }
            Spacer(modifier = Modifier.height(12.dp))
            if (activities.isEmpty()) {
                Text(
                    "No recent activities.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                activities.take(2).forEachIndexed { index, activity ->
                    ActivityCard(
                        activity = activity,
                        onViewMapClick = {
                            val gmmIntentUri =
                                "geo:0,0?q=${URLEncoder.encode(activity.location, "UTF-8")}".toUri()
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")

                            if (mapIntent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(mapIntent)
                            } else {
                                println("Google Maps app not found for activity.")
                            }
                        },
                        onAddToCalendarClick = { activityToAddToCalendar ->
                            val intent = Intent(Intent.ACTION_INSERT).apply {
                                setData(CalendarContract.Events.CONTENT_URI)
                                putExtra(
                                    CalendarContract.Events.TITLE,
                                    activityToAddToCalendar.title
                                )
                                putExtra(
                                    CalendarContract.Events.EVENT_LOCATION,
                                    activityToAddToCalendar.location
                                )

                                val timeParts = activityToAddToCalendar.time.split(" - ")
                                if (timeParts.size == 2) {
                                    val startTimeStr = timeParts[0]
                                    val endTimeStr = timeParts[1]

                                    val startHour = startTimeStr.substringBefore(":").toInt()
                                    val startMinute = startTimeStr.substringAfter(":").toInt()
                                    val endHour = endTimeStr.substringBefore(":").toInt()
                                    val endMinute = endTimeStr.substringAfter(":").toInt()

                                    val startMillis =
                                        activityToAddToCalendar.date.atTime(startHour, startMinute)
                                            .atZone(ZoneId.systemDefault())
                                            .toInstant()
                                            .toEpochMilli()
                                    val endMillis =
                                        activityToAddToCalendar.date.atTime(endHour, endMinute)
                                            .atZone(ZoneId.systemDefault())
                                            .toInstant()
                                            .toEpochMilli()

                                    putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                                    putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
                                } else {
                                    putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                                }
                                putExtra(
                                    CalendarContract.Events.DESCRIPTION,
                                    "Details for ${activityToAddToCalendar.title}"
                                )
                            }
                            if (intent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(intent)
                            } else {
                                println("No calendar app found to handle the intent for activity.")
                            }
                        }
                    )
                    if (index < activities.take(2).size - 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}