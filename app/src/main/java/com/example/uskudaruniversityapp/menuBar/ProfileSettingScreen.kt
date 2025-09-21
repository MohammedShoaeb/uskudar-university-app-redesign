package com.example.uskudaruniversityapp.menuBar

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingScreen(navController: NavHostController) {
    val context = LocalContext.current

    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    var showLanguageDialog by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("English") }

    var showSignoutDialog by remember { mutableStateOf(false) }

    var showExternalLinkDialog by remember { mutableStateOf(false) }
    var externalLinkUrl by remember { mutableStateOf<String?>("https://uskudar.edu.tr") }
    var externalLinkPurpose by remember { mutableStateOf<String?>("yazilimekibi@uskudar.edu.tr") }


    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer),
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = MaterialTheme.colorScheme.onSurface) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to previous screen",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    text = "Preference",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column {
                        // Notification Toggle
                        ListItem(
                            headlineContent = { Text("Notifications") },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notification settings",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            trailingContent = {
                                Switch(
                                    checked = notificationsEnabled,
                                    onCheckedChange = { notificationsEnabled = it },
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .scale(0.8f),
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                            },
                            supportingContent = {
                                Text(
                                    "Manage app notifications",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            modifier = Modifier.clickable {
                                notificationsEnabled = !notificationsEnabled
                            }
                        )
                        // Dark Mode Toggle
                        ListItem(
                            headlineContent = { Text("Dark mode") },
                            leadingContent = {
                                Icon(
                                    Icons.Default.DarkMode,
                                    contentDescription = "Toggle dark mode"
                                )
                            },
                            trailingContent = {
                                Switch(
                                    checked = darkModeEnabled,
                                    onCheckedChange = { darkModeEnabled = it },
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .scale(0.8f),
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                            },
                            modifier = Modifier.clickable { darkModeEnabled = !darkModeEnabled }
                        )
                        ListItem(
                            headlineContent = { Text("Change Language") },
                            supportingContent = {
                                Text(
                                    selectedLanguage,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            leadingContent = {
                                Icon(
                                    Icons.Default.Translate,
                                    contentDescription = "Change language"
                                )
                            },
                            trailingContent = {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForwardIos,
                                    contentDescription = "Go to language settings"
                                )
                            },
                            modifier = Modifier.clickable { showLanguageDialog = true }
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Help & Support",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 0.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column {
                        // Visit Website
                        ListItem(
                            headlineContent = { Text("Visit Website") },
                            leadingContent = {
                                Icon(
                                    Icons.Default.Public,
                                    contentDescription = "Visit university website"
                                )
                            },
                            trailingContent = {
                                Icon(
                                    Icons.Default.OpenInNew,
                                    contentDescription = "Open external link"
                                )
                            },
                            modifier = Modifier.clickable {
                                externalLinkUrl =
                                    "https://www.uskudar.edu.tr/"
                                externalLinkPurpose = "the university website"
                                showExternalLinkDialog = true
                            }
                        )
                        // Support
                        ListItem(
                            headlineContent = { Text("Support") },
                            leadingContent = {
                                Icon(
                                    Icons.Default.SupportAgent,
                                    contentDescription = "Contact support"
                                )
                            },
                            trailingContent = {
                                Icon(
                                    Icons.Default.OpenInNew,
                                    contentDescription = "Open external link"
                                )
                            },
                            modifier = Modifier.clickable {
                                externalLinkUrl =
                                    "mailto:yazilimekibi@uskudar.edu.tr?subject=App%20Support%20Request"
                                externalLinkPurpose = "your email app to send a support message"
                                showExternalLinkDialog = true
                            }
                        )
                        Divider(color = MaterialTheme.colorScheme.outline)
                        // Sign Out
                        ListItem(
                            headlineContent = {
                                Text(
                                    "Sign Out",
                                    color = MaterialTheme.colorScheme.error
                                )
                            },
                            leadingContent = {
                                Icon(
                                    Icons.Default.Logout,
                                    contentDescription = "Sign out",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            },
                            modifier = Modifier
                                .clickable { showSignoutDialog = true }
                        )

                    }
                }
            }


            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "App Version: v4.0.48",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }


    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = {
                Text(
                    "Choose App Language",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            },
            text = {
                Column(modifier = Modifier.padding(0.dp)) {
                    listOf("English", "Türkçe").forEachIndexed { index, lang ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedLanguage = lang
                                    showLanguageDialog = false
                                }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedLanguage == lang,
                                onClick = null
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(lang, style = MaterialTheme.typography.bodyMedium)
                        }

                        if (index < 1) {
                            Divider(
                                color = MaterialTheme.colorScheme.outlineVariant,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showLanguageDialog = false },
                    modifier = Modifier.padding(top = 0.dp)
                ) {
                    Text("Cancel")
                }
            }
        )
    }



    if (showSignoutDialog) {
        AlertDialog(
            onDismissRequest = { showSignoutDialog = false },
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
            title = { Text("Sign Out", style = MaterialTheme.typography.titleMedium) },
            text = {
                Text(
                    "Are you sure you want to sign out? You’ll need to log in again to access your account.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSignoutDialog = false
                        println("User signed out!")
                        navController.navigate("login")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Sign Out", color = MaterialTheme.colorScheme.onError)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSignoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }


    if (showExternalLinkDialog && externalLinkUrl != null && externalLinkPurpose != null) {
        AlertDialog(
            onDismissRequest = {
                showExternalLinkDialog = false
                externalLinkUrl = null
                externalLinkPurpose = null
            },
            icon = { Icon(Icons.Default.OpenInNew, contentDescription = null) },
            title = { Text("Open in Browser?") },
            text = {
                Text(
                    text = "You're about to open ${externalLinkPurpose}. This action will take you outside the app.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        externalLinkUrl?.let { url ->
                            try {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                            } catch (e: Exception) {
                                println("Error opening URL: ${e.message}")
                            }
                        }
                        showExternalLinkDialog = false
                        externalLinkUrl = null
                        externalLinkPurpose = null
                    }
                ) {
                    Text("Open Link")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showExternalLinkDialog = false
                        externalLinkUrl = null
                        externalLinkPurpose = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

}

