package com.example.uskudaruniversityapp.stixPages


import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StixSendMessageContent(
    onDismiss: () -> Unit,
    onMessageSent: (contact: String, course: String, subject: String, message: String, fileUri: Uri?) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val coroutineScope = rememberCoroutineScope()

    var contactText by remember { mutableStateOf("") }
    var courseNameText by remember { mutableStateOf("") }
    var subjectText by remember { mutableStateOf("") }
    var messageText by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }

    var contactError by remember { mutableStateOf(false) }
    var courseError by remember { mutableStateOf(false) }
    var subjectError by remember { mutableStateOf(false) }
    var messageError by remember { mutableStateOf(false) }

    val contacts = listOf("Dr. Öğr. Üyesi SALIM JIBRIN DANBATTA", "Prof. BELAYNESH CHEKOL")
    val courseNames =
        listOf("Data Science and Analytics", "Theoretical and Computational Neuroscience")

    var contactExpanded by remember { mutableStateOf(false) }
    var courseExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {

            Text(
                text = "Compose New Message",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(8.dp))


            ExposedDropdownMenuBox(
                expanded = contactExpanded,
                onExpandedChange = { contactExpanded = !contactExpanded }
            ) {
                OutlinedTextField(
                    value = contactText,
                    onValueChange = { contactText = it },
                    readOnly = true,
                    label = { Text("Select Contact *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(contactExpanded) },
                    isError = contactError,
                    supportingText = { if (contactError) Text("Contact is required") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = contactExpanded,
                    onDismissRequest = { contactExpanded = false }
                ) {
                    contacts.forEach { contact ->
                        DropdownMenuItem(
                            text = { Text(contact) },
                            onClick = {
                                contactText = contact
                                contactExpanded = false
                                contactError = false
                            }
                        )
                    }
                }
            }


            ExposedDropdownMenuBox(
                expanded = courseExpanded,
                onExpandedChange = { courseExpanded = !courseExpanded }
            ) {
                OutlinedTextField(
                    value = courseNameText,
                    onValueChange = { courseNameText = it },
                    readOnly = true,
                    label = { Text("Select Course *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(courseExpanded) },
                    isError = courseError,
                    supportingText = { if (courseError) Text("Course is required") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = courseExpanded,
                    onDismissRequest = { courseExpanded = false }
                ) {
                    courseNames.forEach { course ->
                        DropdownMenuItem(
                            text = { Text(course) },
                            onClick = {
                                courseNameText = course
                                courseExpanded = false
                                courseError = false
                            }
                        )
                    }
                }
            }


            OutlinedTextField(
                value = subjectText,
                onValueChange = {
                    subjectText = it
                    subjectError = it.isBlank()
                },
                label = { Text("Subject *") },
                isError = subjectError,
                supportingText = { if (subjectError) Text("Subject is required") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )


            OutlinedTextField(
                value = messageText,
                onValueChange = {
                    messageText = it
                    messageError = it.isBlank()
                },
                label = { Text("Your Message *") },
                isError = messageError,
                supportingText = { if (messageError) Text("Message is required") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                minLines = 4,
                maxLines = 8,
                shape = RoundedCornerShape(12.dp)
            )


            Text(
                text = "Attach File (optional)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        coroutineScope.launch {
                            selectedFileUri =
                                "content://dummy/path/to/my_message_attachment.pdf".toUri()
                            selectedFileName = "my_message_attachment.pdf"
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(0.5f)
                ) {
                    Icon(Icons.Default.AttachFile, contentDescription = "Choose File")
                    Spacer(Modifier.width(6.dp))
                    Text("Choose File")
                }
                if (selectedFileName != null) {
                    Row(
                        modifier = Modifier.weight(0.5f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = selectedFileName!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            selectedFileUri = null
                            selectedFileName = null
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove file",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                } else {
                    Text(
                        text = "No file selected",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(0.5f)
                    )
                }
            }
            Text(
                text = "Accepted formats: PDF, DOCX, JPG (Max 25MB). File is optional.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,

            )
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
                    contactError = contactText.isBlank()
                    courseError = courseNameText.isBlank()
                    subjectError = subjectText.isBlank()
                    messageError = messageText.isBlank()

                    if (!contactError && !courseError && !subjectError && !messageError) {
                        coroutineScope.launch {
                            onMessageSent(
                                contactText,
                                courseNameText,
                                subjectText,
                                messageText,
                                selectedFileUri
                            )
                            onDismiss()
                        }
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Please fill in all required fields.")
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send")
                Spacer(Modifier.width(4.dp))
                Text("Send")
            }
        }
    }
}
