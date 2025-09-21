package com.example.uskudaruniversityapp.stixPages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.uskudaruniversityapp.dataClass.StixAppointment
import kotlinx.coroutines.launch
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StixAppointmentPage(navController: NavController) {
    val appointments = remember { mutableStateListOf<StixAppointment>() }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()


    val onAppointmentAdded: (StixAppointment) -> Unit = { newAppointment ->
        appointments.add(newAppointment)
        showBottomSheet = false
    }

    StixAppShell(
        navController = navController,
        title = "Appointment",
        showBackButton = true
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (appointments.isEmpty()) {
                    Spacer(modifier = Modifier.height(64.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "No appointments yet!",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                "Need to schedule one?",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { showBottomSheet = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Create a new appointment")
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Text(
                                "Your Scheduled Appointments",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        items(appointments) { appointment ->
                            AppointmentListItem(
                                appointment = appointment,
                                onDelete = { toDelete ->
                                    appointments.remove(toDelete)
                                }
                            )
                        }
                    }
                }
            }


            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState
                ) {
                    StixNewAppointmentSheet(
                        onSubmit = onAppointmentAdded,
                        onDismiss = { showBottomSheet = false })

                }
            }
        }
    }
}

@Composable
fun AppointmentListItem(
    appointment: StixAppointment,
    onDelete: (StixAppointment) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete Appointment?") },
            text = { Text("Are you sure you want to delete \"${appointment.subject}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(appointment)
                    showDialog = false
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* maybe open detail */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    appointment.subject,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text("Contact: ${appointment.contact}", style = MaterialTheme.typography.bodyMedium)
                Text("Date: ${appointment.date}", style = MaterialTheme.typography.bodySmall)
                Text("Time: ${appointment.time}", style = MaterialTheme.typography.bodySmall)
            }

            IconButton(onClick = { showDialog = true }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StixNewAppointmentSheet(
    onSubmit: (StixAppointment) -> Unit,
    onDismiss: () -> Unit,
) {
    var subject by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var contactExpanded by remember { mutableStateOf(false) }
    val contacts = listOf("Dr. SALIM", "Prof. BELAYNESH", "Dr. TÜRKAY")

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (contacts.isNotEmpty()) contact = contacts.first()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("New Appointment", style = MaterialTheme.typography.headlineSmall)


            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Subject (required)") },
                isError = subject.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = contactExpanded,
                onExpandedChange = { contactExpanded = !contactExpanded }
            ) {
                OutlinedTextField(
                    value = contact,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Select Contact") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(contactExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = contactExpanded,
                    onDismissRequest = { contactExpanded = false }
                ) {
                    contacts.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                contact = it
                                contactExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


        }
        SnackbarHost(hostState = snackbarHostState)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel")
            }

            Spacer(Modifier.width(16.dp))

            Button(
                onClick = {
                    if (subject.isBlank() || contact.isBlank()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Please fill in all required fields.")
                        }
                        return@Button
                    }

                    val appointment = StixAppointment(
                        id = "appt_${System.currentTimeMillis()}",
                        subject = subject,
                        contact = contact,
                        date = LocalDate.now(),
                        time = "10:00",
                        status = "Pending"
                    )

                    onSubmit(appointment)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Schedule")
            }
        }

    }
}
