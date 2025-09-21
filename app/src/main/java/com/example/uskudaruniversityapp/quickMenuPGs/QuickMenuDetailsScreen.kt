package com.example.uskudaruniversityapp.quickMenuPGs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uskudaruniversityapp.components.WebPageViewer
import com.example.uskudaruniversityapp.util.FavoriteToggleResult

enum class SnackbarType { INFO, ERROR }
data class SnackbarMessage(
    val message: String,
    val type: SnackbarType? = SnackbarType.INFO,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null,
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceItemDetailScreen(
    navController: NavController,
    serviceName: String?,
    onToggleFavorite: (String) -> FavoriteToggleResult,
    isServiceFavorite: (String) -> Boolean,
) {

    val isCurrentServiceFavorite = serviceName?.let { isServiceFavorite(it) } == true
    var snackbarMessage by remember { mutableStateOf<SnackbarMessage?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

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
                    title = {
                        Text(
                            text = serviceName ?: "Service Detail",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            serviceName?.let { name ->
                                val result = onToggleFavorite(name)

                                when (result) {
                                    is FavoriteToggleResult.Toggled -> {
                                        snackbarMessage = SnackbarMessage(
                                            message = if (result.isNowFavorite) {
                                                "$name added to your quick menu."
                                            } else {
                                                "$name removed from your quick menu."
                                            },
                                            type = SnackbarType.INFO,
                                            actionLabel = "Undo",
                                            onAction = {
                                                onToggleFavorite(name)
                                                snackbarMessage = SnackbarMessage(
                                                    message = if (result.isNowFavorite) "$name removal undone." else "$name addition undone.",
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
                        })
                        {
                            Icon(
                                imageVector = if (isCurrentServiceFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isCurrentServiceFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                )
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        },
        snackbarHost = {
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
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            val serviceContentMap: Map<String, @Composable () -> Unit> = mapOf(
                "Semester Courses" to { SemesterCoursesPage() },
                "Contact Center" to { ContactCenterPage(navController) },
                "Solution Center" to { WebPageViewer("https://cozum.uskudar.edu.tr/") },
                "Digital Check-in" to { DigitalCheckinPage() },
                "Guest Entry Invitation" to { GuestInvitationPage(navController) },
                "My Petitions" to { PetitionPage(navController) },
                "Academic Staff" to { WebPageViewer("https://uskudar.edu.tr/tr/akademik-kadro") },
                "Academic Calendar" to { WebPageViewer("https://uskudar.edu.tr/tr/akademik-takvim") },
                "Transcript" to { TranscriptPage() },
                "Weekly Schedule" to { WeeklySchedulePage() },
                "Exam Marks" to { ExamMarksPage(navController) },
                "Laboratories" to { LaboratoriesPage(navController) },
                "Food List" to { FoodListPage() },
                "Ring Hours" to { RingHoursPages(navController) },
                "Bus Tracking" to { RingTracking() },
                "Campuses" to { CampusesPage(navController) },
                "Library" to { LibraryMenuPage(navController) },
                "UU Radio" to { WebPageViewer("https://radyo.uskudar.edu.tr/") },
                "UU TV" to { WebPageViewer("https://tv.uskudar.edu.tr/") },
                "Virtual Tours" to { WebPageViewer("https://uskudar.edu.tr/sanaltur/") },
                "Wage Calculation Tool" to { WageCalculationPage(navController) },
                "STIX QR Code Login" to { StixQrCodeLoginPage() }
            )
            val content: @Composable () -> Unit = serviceContentMap[serviceName ?: ""] ?: {
                GenericServiceDetailContent(serviceName)
            }

            content()
        }
    }
}


@Composable
fun GenericServiceDetailContent(serviceName: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Details for:",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = serviceName ?: "Unknown Service",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

    }
}

