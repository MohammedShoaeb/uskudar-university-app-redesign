package com.example.uskudaruniversityapp.stixPages

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.uskudaruniversityapp.dataClass.BottomNavItemSTIX

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StixAppShell(
    navController: NavController,
    title: String,
    showBackButton: Boolean = true,

    snackbarHost: @Composable () -> Unit = {},
    showBottomBar: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val bottomNavItems = listOf(
        BottomNavItemSTIX(
            "Courses",
            Icons.Outlined.ListAlt,
            "stix_course_list",
            Icons.Filled.ListAlt
        ),
        BottomNavItemSTIX(
            "Appointment",
            Icons.Outlined.DateRange,
            "stix_appointment",
            Icons.Filled.DateRange
        ),
        BottomNavItemSTIX(
            "Attendance",
            Icons.Outlined.QrCode,
            "stix_attendance",
            Icons.Filled.QrCode
        ),
        BottomNavItemSTIX(
            "Messages",
            Icons.Outlined.MailOutline,
            "stix_messages",
            Icons.Filled.MailOutline
        ),
        BottomNavItemSTIX(
            "Education fee",
            Icons.Outlined.AccountBalanceWallet,
            "stix_education_fee",
            Icons.Filled.AccountBalanceWallet
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    if (currentRoute != "stix_course_list" && showBackButton) {
        BackHandler {
            navController.navigate("stix_course_list") {
                popUpTo("stix_course_list") {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }


    Scaffold(
        topBar = {
           Column {

               TopAppBar(
                   title = { Text(text = title, color = MaterialTheme.colorScheme.onSurface) },
                   navigationIcon = {
                       if (showBackButton) {
                           IconButton(onClick = { navController.popBackStack() }) {
                               Icon(
                                   Icons.AutoMirrored.Filled.ArrowBack,
                                   contentDescription = "Back",
                                   tint = MaterialTheme.colorScheme.onSurface
                               )
                           }
                       }
                   },
                   actions = actions,
                   colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
               )
               HorizontalDivider(thickness = 0.5.dp, color=MaterialTheme.colorScheme.outlineVariant)
           }
        },

        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    modifier = Modifier.padding(0.dp),
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ) {
                    bottomNavItems.forEachIndexed { index, item ->
                        val selected = currentRoute == item.route

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.activeIcon else item.icon,
                                    contentDescription = item.name
                                )
                            },
                            label = {
                                Text(
                                    item.name,
                                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            selected = selected,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo("stix_course_list") {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                                indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        },

        snackbarHost = snackbarHost

    ) { paddingValues ->
        content(paddingValues)
    }
}