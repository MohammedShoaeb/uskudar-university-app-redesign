package com.example.uskudaruniversityapp.menuBar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uskudaruniversityapp.components.FilterChipRow
import com.example.uskudaruniversityapp.dataClass.FilterItem
import com.example.uskudaruniversityapp.dataClass.ServiceItem
import com.example.uskudaruniversityapp.dataClass.serviceItems
import com.example.uskudaruniversityapp.quickMenuPGs.SnackbarMessage
import com.example.uskudaruniversityapp.quickMenuPGs.SnackbarType
import com.example.uskudaruniversityapp.util.FavoriteToggleResult
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickMenuPage(
    navController: NavController,
    onToggleFavorite: (String) -> FavoriteToggleResult,
    isServiceFavorite: (String) -> Boolean
) {
    val allServiceItems = remember { serviceItems }

    val allFilters = remember(allServiceItems) {
        listOf(
            FilterItem("All", allServiceItems.size),
            FilterItem("University Services", allServiceItems.count { it.category == "University Services" }),
            FilterItem("Academic Information", allServiceItems.count { it.category == "Academic Information" }),
            FilterItem("Campus Resources", allServiceItems.count { it.category == "Campus Resources" }),
            FilterItem("University Media", allServiceItems.count { it.category == "University Media" }),
            FilterItem("General Information", allServiceItems.count { it.category == "General Information" })
        )
    }

    var selectedFilter by remember { mutableStateOf(allFilters.first()) }

    val filteredServices = remember(selectedFilter, allServiceItems) {
        if (selectedFilter.name == "All") allServiceItems
        else allServiceItems.filter { it.category == selectedFilter.name }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf<SnackbarMessage?>(null) }
    var isGridView by remember { mutableStateOf(false) }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            val snackbarResult = snackbarHostState.showSnackbar(
                message = message.message,
                actionLabel = message.actionLabel,
                withDismissAction = true,
                duration = SnackbarDuration.Short
            )

            when (snackbarResult) {
                SnackbarResult.ActionPerformed -> {
                    message.onAction?.invoke()
                }
                SnackbarResult.Dismissed -> {
                }
            }
            snackbarMessage = null
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Quick Menu", color = MaterialTheme.colorScheme.onSurface) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                    actions = {
                        IconButton(onClick = { isGridView = !isGridView }) {
                            Icon(
                                imageVector = if (isGridView) Icons.Default.Apps else Icons.Default.ViewList,
                                contentDescription = if (isGridView) "Switch to list view" else "Switch to grid view",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
            }
        },        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                val containerColor = when (snackbarMessage?.type) {
                    SnackbarType.ERROR -> MaterialTheme.colorScheme.errorContainer
                    else -> MaterialTheme.colorScheme.inverseSurface
                }
                val contentColor = when (snackbarMessage?.type) {
                    SnackbarType.ERROR -> MaterialTheme.colorScheme.onErrorContainer
                    else -> MaterialTheme.colorScheme.inverseOnSurface
                }

                Snackbar(
                    snackbarData = data,
                    containerColor = containerColor,
                    contentColor = contentColor
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .background(MaterialTheme.colorScheme.background)
        ) {
            FilterChipRow(
                filters = allFilters,
                selectedFilter = selectedFilter,
                onFilterSelected = { filter -> selectedFilter = filter }
            )
            if (filteredServices.isEmpty()) {
                Text(
                    text = "No services found for '${selectedFilter.name}'.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = if (selectedFilter.name == "All") "All Services" else selectedFilter.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                )

                val sortedServices = filteredServices.sortedBy { it.text }

                if (isGridView) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 100.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp,vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items (sortedServices) { item ->
                            ServiceGridItemCard(
                                serviceItem = item,
                                onClick = { serviceName ->
                                    navController.navigate("service_item_detail/$serviceName")
                                },
                                isFavorited = isServiceFavorite(item.text),
                                onFavoriteClick = {
                                    val name = item.text
                                    val result = onToggleFavorite(name)
                                    when (result) {
                                        is FavoriteToggleResult.Toggled -> {
                                            snackbarMessage = SnackbarMessage(
                                                message = if (result.isNowFavorite)
                                                    "${name} added to your quick menu."
                                                else
                                                    "${name} removed from your quick menu.",
                                                type = SnackbarType.INFO,
                                                actionLabel = "Undo",
                                                onAction = {
                                                    onToggleFavorite(name)
                                                    snackbarMessage = SnackbarMessage(
                                                        message = if (result.isNowFavorite) "${name} removal undone." else "${name} addition undone.",
                                                        type = SnackbarType.INFO
                                                    )
                                                }
                                            )
                                        }
                                        FavoriteToggleResult.LimitReached -> {
                                            snackbarMessage = SnackbarMessage(
                                                message = "You can only favorite a maximum of 8 items.",
                                                type = SnackbarType.ERROR
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 4.dp)
                            .background(MaterialTheme.colorScheme.background),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
                    ) {
                        items(sortedServices) { item ->
                            ServiceCard(
                                serviceItem = item,
                                onClick = { serviceName ->
                                    navController.navigate("service_item_detail/$serviceName")
                                },
                                isFavorited = isServiceFavorite(item.text),
                                onFavoriteClick = {
                                    val name = item.text
                                    val result = onToggleFavorite(name)
                                    when (result) {
                                        is FavoriteToggleResult.Toggled -> {
                                            snackbarMessage = SnackbarMessage(
                                                message = if (result.isNowFavorite)
                                                    "${name} added to your quick menu."
                                                else
                                                    "${name} removed from your quick menu.",
                                                type = SnackbarType.INFO,
                                                actionLabel = "Undo",
                                                onAction = {
                                                    onToggleFavorite(name)
                                                    snackbarMessage = SnackbarMessage(
                                                        message = if (result.isNowFavorite) "${name} removal undone." else "${name} addition undone.",
                                                        type = SnackbarType.INFO
                                                    )
                                                }
                                            )
                                        }
                                        FavoriteToggleResult.LimitReached -> {
                                            snackbarMessage = SnackbarMessage(
                                                message = "You can only favorite a maximum of 8 items.",
                                                type = SnackbarType.ERROR
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/////////////////////////////////////////////////////////////////////////



@Composable
fun ServiceCard(
    serviceItem: ServiceItem,
    isFavorited: Boolean,
    onClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = { onClick(serviceItem.text) }),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = serviceItem.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = serviceItem.text,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorited) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorited) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onFavoriteClick(serviceItem.text) }
                )

                Spacer(modifier = Modifier.width(12.dp))

                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = "Go to ${serviceItem.text}",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

/////////////////////////////////////////////////////////////////////////


@Composable
fun ServiceGridItemCard(
    serviceItem: ServiceItem,
    isFavorited: Boolean,
    onClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = { onClick(serviceItem.text) })
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = serviceItem.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = serviceItem.text,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

            }
        }

        IconButton(
            onClick = { onFavoriteClick(serviceItem.text) },
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .background(
                    color = Color.Transparent,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = if (isFavorited) "Remove from favorites" else "Add to favorites",
                tint = if (isFavorited) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}