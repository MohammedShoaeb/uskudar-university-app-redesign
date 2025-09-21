package com.example.uskudaruniversityapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uskudaruniversityapp.components.BottomNavigationBar
import com.example.uskudaruniversityapp.components.WebViewScreen
import com.example.uskudaruniversityapp.dataClass.bottomNavItems
import com.example.uskudaruniversityapp.menuBar.NewsDetailScreen
import com.example.uskudaruniversityapp.menuBar.ProfilePage
import com.example.uskudaruniversityapp.menuBar.ProfileSettingScreen
import com.example.uskudaruniversityapp.menuBar.QrPage
import com.example.uskudaruniversityapp.menuBar.QuickMenuPage
import com.example.uskudaruniversityapp.menuBar.UpdatesPage
import com.example.uskudaruniversityapp.menuBar.stixGraph
import com.example.uskudaruniversityapp.notificationPages.NotificationDetailScreen
import com.example.uskudaruniversityapp.notificationPages.NotificationItem
import com.example.uskudaruniversityapp.notificationPages.NotificationListScreen
import com.example.uskudaruniversityapp.quickMenuPGs.LibraryMenuPage
import com.example.uskudaruniversityapp.quickMenuPGs.MyBorrowedBooksPage
import com.example.uskudaruniversityapp.quickMenuPGs.NearbyLibrariesPage
import com.example.uskudaruniversityapp.quickMenuPGs.SearchBooksPage
import com.example.uskudaruniversityapp.quickMenuPGs.ServiceItemDetailScreen
import com.example.uskudaruniversityapp.util.FavoriteToggleResult
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val bottomRoutes = bottomNavItems.map { it.route }
    val shouldShowBottomBar = currentRoute in bottomRoutes

    val pagerState = rememberPagerState(
        pageCount = { bottomRoutes.size },
        initialPage = bottomRoutes.indexOf("home").coerceAtLeast(0)
    )
    val coroutineScope = rememberCoroutineScope()

    val notifications = remember {
        mutableStateListOf(
            NotificationItem(
                "notif1",
                "New Exam Schedule Released",
                "The updated final exam schedule is now available on the student portal. Please review it carefully.",
                LocalDateTime.of(2025, 6, 10, 14, 30)
            ),
            NotificationItem(
                "notif2",
                "Campus Closure Tomorrow",
                "Due to extreme weather conditions, the campus will be closed all day tomorrow, June 11th.",
                LocalDateTime.of(2025, 6, 9, 20, 0)
            ),
            NotificationItem(
                "notif3",
                "Sports Day Event",
                "Join us for the annual Sports Day on June 15th! See activities section for more details.",
                LocalDateTime.of(2025, 6, 8, 10, 0),
                isRead = true
            )
        )
    }


    val hasUnreadNotifications by remember {
        derivedStateOf {
            notifications.any { !it.isRead }
        }
    }


    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            val route = bottomRoutes[pagerState.currentPage]
            if (route != currentRoute) {
                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }


    LaunchedEffect(currentRoute) {
        val index = bottomRoutes.indexOf(currentRoute)
        if (index >= 0 && pagerState.currentPage != index) {

            coroutineScope.launch {
                pagerState.animateScrollToPage(page = index)
            }
        }
    }

    val favoritedServices = remember { mutableStateListOf<String>() }


    val onToggleFavorite: (String) -> FavoriteToggleResult = { name ->
        if (favoritedServices.contains(name)) {
            favoritedServices.remove(name)
            FavoriteToggleResult.Toggled(name, false)
        } else {
            if (favoritedServices.size < 8) {
                favoritedServices.add(name)
                FavoriteToggleResult.Toggled(name, true)
            } else {
                FavoriteToggleResult.LimitReached
            }
        }
    }


    val isServiceFavorite: (String) -> Boolean = { name ->
        favoritedServices.contains(name)
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onSelectedTab = { index, route ->
                        if (currentRoute != route) {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (bottomRoutes[page]) {
                    "home" -> HomePage(
                        navController,
                        favoritedServices.toList(),
                        hasUnreadNotifications = hasUnreadNotifications,
                        notifications = notifications,
                        onUpdateFavorites = { updatedList ->
                            favoritedServices.clear()
                            favoritedServices.addAll(updatedList)
                        }
                    )

                    "updates" -> UpdatesPage(navController)
                    "qr" -> QrPage()
                    "quick_menu" -> QuickMenuPage(
                        navController,
                        onToggleFavorite = onToggleFavorite,
                        isServiceFavorite = isServiceFavorite
                    )

                    "profile" -> ProfilePage(navController)
                    else -> {}
                }
            }

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                bottomRoutes.forEach { route ->
                    composable(route) {}
                }


                composable("service_item_detail/{serviceName}") { entry ->
                    val name = entry.arguments?.getString("serviceName")
                    ServiceItemDetailScreen(
                        navController = navController,
                        serviceName = name,
                        onToggleFavorite,
                        isServiceFavorite
                    )
                }
                composable("settings_route") {
                    ProfileSettingScreen(navController)
                }
                composable("login") {
                    LoginScreen() {
                        navController.navigate("home")
                    }
                }
                composable("library_menu") {
                    LibraryMenuPage(navController)
                }
                composable("nearbyLibraries") {
                    NearbyLibrariesPage(navController)
                }
                composable("borrowedBooks") {
                    MyBorrowedBooksPage(navController)
                }
                composable("searchBooks") {
                    SearchBooksPage(navController)
                }
                composable(
                    route = "news_detail/{newsTitle}/{newsImageUrl}/{newsDate}",
                    arguments = listOf(
                        navArgument("newsTitle") { type = NavType.StringType },
                        navArgument("newsImageUrl") {
                            type = NavType.IntType
                        },
                        navArgument("newsDate") { type = NavType.StringType }
                    )) { backStackEntry ->
                    val newsTitle = backStackEntry.arguments?.getString("newsTitle")
                    val newsImageUrl = backStackEntry.arguments?.getInt("newsImageUrl")
                    val newsDate = backStackEntry.arguments?.getString("newsDate")
                    NewsDetailScreen(
                        navController = navController,
                        newsTitle = newsTitle,
                        newsImageUrl = newsImageUrl,
                        newsDate = newsDate
                    )
                }


                composable("notifications_list") {
                    NotificationListScreen(
                        navController = navController,
                        notifications = notifications
                    )
                }
                composable(
                    route = "notification_detail/{notificationTitle}/{notificationContent}/{notificationTimestamp}",
                    arguments = listOf(
                        navArgument("notificationTitle") { type = NavType.StringType },
                        navArgument("notificationContent") { type = NavType.StringType },
                        navArgument("notificationTimestamp") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val title = backStackEntry.arguments?.getString("notificationTitle")
                    val content = backStackEntry.arguments?.getString("notificationContent")
                    val timestamp = backStackEntry.arguments?.getString("notificationTimestamp")
                    NotificationDetailScreen(
                        navController = navController,
                        notificationTitle = title,
                        notificationContent = content,
                        notificationTimestamp = timestamp
                    )
                }


                composable(
                    route = "webview_screen/{url}",
                    arguments = listOf(navArgument("url") { type = NavType.StringType })
                ) { backStackEntry ->
                    val encodedUrl = backStackEntry.arguments?.getString("url")
                    val decodedUrl = encodedUrl?.let { URLDecoder.decode(it, "UTF-8") }

                    WebViewScreen(navController = navController, url = decodedUrl)
                }




                stixGraph(navController = navController)


            }


        }
    }
}
