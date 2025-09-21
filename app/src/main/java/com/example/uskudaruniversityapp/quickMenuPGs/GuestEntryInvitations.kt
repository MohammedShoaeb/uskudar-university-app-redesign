package com.example.uskudaruniversityapp.quickMenuPGs


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestInvitationPage(navController: NavController) {
    var showCreateInvitationForm by remember { mutableStateOf(false) }
    var invitations by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var invitationToDelete by remember { mutableStateOf<Pair<String, String>?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            AnimatedContent(
                targetState = showCreateInvitationForm,
                transitionSpec = {
                    (slideInVertically(animationSpec = tween(300)) { height -> height } + fadeIn(
                        animationSpec = tween(300)
                    ))
                        .togetherWith(slideOutVertically(animationSpec = tween(300)) { height -> -height } + fadeOut(
                            animationSpec = tween(300)
                        ))
                }, label = "Invitation Form Transition"
            ) { targetState ->
                if (targetState) {
                    CreateInvitationForm(
                        modifier = Modifier.fillMaxWidth(),
                        onCreateInvitation = { campus, visitorCode ->
                            invitations = invitations + Pair(campus, visitorCode)
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Invitation created successfully!",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Short
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    invitations = invitations.dropLast(1)
                                    snackbarHostState.showSnackbar("Invitation undone.")
                                }
                            }
                            showCreateInvitationForm = false
                        },
                        onCancel = { showCreateInvitationForm = false },
                        snackbarHostState = snackbarHostState,
                        scope = scope
                    )
                } else {
                    if (invitations.isEmpty()) {
                        EmptyInvitationsState(
                            modifier = Modifier.fillMaxWidth(),
                            onCreateNewInvitationClick = { showCreateInvitationForm = true }
                        )
                    } else {
                        InvitationList(
                            invitations = invitations,
                            onDeleteClicked = { invitationToDelete = it },
                            onAddNewClicked = { showCreateInvitationForm = true }
                        )
                    }
                }
            }
        }
    }

    if (invitationToDelete != null) {
        AlertDialog(
            onDismissRequest = { invitationToDelete = null },
            title = { Text("Delete Invitation") },
            text = { Text("Are you sure you want to delete this invitation?") },
            confirmButton = {
                TextButton(onClick = {
                    invitations = invitations.filterNot { it == invitationToDelete }
                    invitationToDelete = null
                    scope.launch {
                        snackbarHostState.showSnackbar("Invitation deleted.")
                    }
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { invitationToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun InvitationList(
    invitations: List<Pair<String, String>>,
    onDeleteClicked: (Pair<String, String>) -> Unit,
    onAddNewClicked: () -> Unit,
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Guest Invitations", style = MaterialTheme.typography.titleLarge)
            ElevatedButton(onClick = onAddNewClicked) {
                Icon(Icons.Default.PersonAdd, contentDescription = "Add Invitation")
                Spacer(modifier = Modifier.width(8.dp))
                Text("New Invitation")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(invitations) { invitation ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Campus: ${invitation.first}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                "Visitor Code: ${invitation.second}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            onClick = { onDeleteClicked(invitation) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Invitation")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyInvitationsState(modifier: Modifier = Modifier, onCreateNewInvitationClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedButton(
            onClick = onCreateNewInvitationClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.PersonAdd,
                    contentDescription = "Create New Invitation",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Create New Invitation",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Looks empty here! Create your first guest invitation.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CreateInvitationForm(
    modifier: Modifier = Modifier,
    onCreateInvitation: (String, String) -> Unit,
    onCancel: () -> Unit,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
) {
    var selectedCampusText by remember { mutableStateOf("") }
    var visitorCode by remember { mutableStateOf("") }
    var isCampusExpanded by remember { mutableStateOf(false) }
    var showVisitorCodeError by remember { mutableStateOf(false) }

    val campuses = listOf(
        "Altunizade Merkez Yerleske",
        "Uskudar Carsi Yerleske",
        "NP Saglik Yerleskesi",
        "Beylerbeyi Yerleskesi"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = isCampusExpanded,
            onExpandedChange = { isCampusExpanded = !isCampusExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedCampusText,
                onValueChange = {},
                readOnly = true,
                label = { Text("Campus Selection") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCampusExpanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = isCampusExpanded,
                onDismissRequest = { isCampusExpanded = false }
            ) {
                campuses.forEach { campus ->
                    DropdownMenuItem(
                        text = { Text(campus) },
                        onClick = {
                            selectedCampusText = campus
                            isCampusExpanded = false
                        }
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = visitorCode,
                onValueChange = {
                    visitorCode = it
                    showVisitorCodeError = false
                },
                label = { Text("Visitor Code") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                singleLine = true,
                isError = showVisitorCodeError,
                supportingText = {
                    if (showVisitorCodeError) {
                        Text(
                            text = "Visitor code cannot be empty.",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

        }



        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                ),
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    if (selectedCampusText.isNotBlank() && visitorCode.isNotBlank()) {
                        onCreateInvitation(selectedCampusText, visitorCode)
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Please select a campus and enter a visitor code.",
                                duration = SnackbarDuration.Short
                            )
                        }
                        showVisitorCodeError = visitorCode.isBlank()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = selectedCampusText.isNotBlank() && visitorCode.isNotBlank()
            ) {
                Text(
                    text = "Create Invitation",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            text = "Note: Your guest is assumed to have entered when the request is created. So please create the request in the campus.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}