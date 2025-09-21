package com.example.uskudaruniversityapp.stixPages

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class DocumentItem(
    val id: String,
    val title: String,
    val fileName: String,
    val uploadTime: LocalDateTime,
    val lastUpdate: LocalDateTime,
    val isUpdated: Boolean = false,
)

data class AnnouncementItem(
    val id: String,
    val title: String,
    val content: String,
    val uploadTime: LocalDateTime,
)

enum class AssignmentStatus {
    NOT_SUBMITTED,
    SUBMITTED,
    GRADED
}

data class AssignmentItem(
    val id: String,
    val title: String,
    val content: String,
    val uploadTime: LocalDateTime,
    var status: AssignmentStatus,
)


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalPagerApi::class
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StixCourseDetailPage(navController: NavController) {

    val documents = remember {
        listOf(
            DocumentItem(
                "doc1",
                "Week-8 lecture notes",
                "Interface_Design_Methods_Lecture_Updated.pdf",
                LocalDateTime.of(2025, 4, 17, 11, 25),
                LocalDateTime.of(2025, 4, 17, 11, 26),
                isUpdated = true
            ),
            DocumentItem(
                "doc2",
                "Week-7 lecture notes",
                "Mobile_App_Development_Lecture.pdf",
                LocalDateTime.of(2025, 4, 10, 10, 0),
                LocalDateTime.of(2025, 4, 10, 10, 0)
            )
        )
    }

    val announcements = remember {
        listOf(
            AnnouncementItem(
                "ann1",
                "I am postponing Tomorrow's presentation",
                "Dear students, I hope everyone of you is doing well. I would like to let you know that we wont have a presentation and lecture tomorrow. Take care and stay safe.",
                LocalDateTime.of(2025, 4, 17, 11, 25)
            ),
            AnnouncementItem(
                "ann2",
                "Midterm Exam Schedule",
                "The midterm exam schedule for all courses has been published on the student portal. Please check the dates and times carefully.",
                LocalDateTime.of(2025, 4, 15, 9, 0)
            )
        )
    }

    val assignments = remember {
        mutableStateListOf(
            AssignmentItem(
                "ass1",
                "Group name, members and project title submission",
                "Dear all, Please submit your group name, member list and project title here through your group representative.",
                LocalDateTime.of(2025, 4, 17, 11, 26),
                AssignmentStatus.NOT_SUBMITTED
            ),
            AssignmentItem(
                "ass2",
                "Research Paper Submission",
                "Submit your research paper by the end of the day. Ensure all guidelines are followed.",
                LocalDateTime.of(2025, 4, 16, 10, 0),
                AssignmentStatus.NOT_SUBMITTED
            )
        )
    }

    val tabs = remember {
        listOf(
            "All",
            "Documents",
            "Announcement",
            "Assignments"
        )
    }
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    var showAssignmentBottomSheet by remember { mutableStateOf(false) }
    var selectedAssignmentForSubmission by remember { mutableStateOf<AssignmentItem?>(null) }



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Course Details",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {

            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }



            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    when (tabs[page]) {
                        "All" -> {

                            items(documents) { document ->
                                DocumentCard(
                                    document = document,
                                    snackbarHostState = snackbarHostState
                                )
                            }
                            items(announcements) { announcement ->
                                AnnouncementCard(announcement = announcement)
                            }
                            items(assignments) { assignment ->
                                AssignmentCard(
                                    assignment = assignment,
                                    onSubmitClick = {
                                        selectedAssignmentForSubmission = it
                                        showAssignmentBottomSheet = true
                                    }
                                )
                            }
                        }

                        "Documents" -> {
                            items(documents) { document ->
                                DocumentCard(
                                    document = document,
                                    snackbarHostState = snackbarHostState
                                )
                            }
                        }

                        "Announcement" -> {
                            items(announcements) { announcement ->
                                AnnouncementCard(announcement = announcement)
                            }
                        }

                        "Assignments" -> {
                            items(assignments) { assignment ->
                                AssignmentCard(
                                    assignment = assignment,
                                    onSubmitClick = {
                                        selectedAssignmentForSubmission = it
                                        showAssignmentBottomSheet = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showAssignmentBottomSheet && selectedAssignmentForSubmission != null) {
            AssignmentSubmissionBottomSheet(
                assignment = selectedAssignmentForSubmission!!,
                onDismiss = { showAssignmentBottomSheet = false },
                onSubmit = { message, fileUri ->
                    coroutineScope.launch {
                        val submittedAssignment =
                            assignments.find { it.id == selectedAssignmentForSubmission?.id }
                        submittedAssignment?.status = AssignmentStatus.SUBMITTED
                        snackbarHostState.showSnackbar("Assignment '${submittedAssignment?.title}' submitted successfully!")
                        showAssignmentBottomSheet = false
                        selectedAssignmentForSubmission = null
                    }
                },
                snackbarHostState = snackbarHostState )
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentCard(document: DocumentItem, snackbarHostState: SnackbarHostState) {
    val coroutineScope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {},
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Description,
                    contentDescription = "Document",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = document.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = document.fileName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (document.isUpdated) {
                    Spacer(Modifier.width(8.dp))
                    Card(
                        shape = RoundedCornerShape(50),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Text(
                            text = "Updated",
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Upload,
                        contentDescription = "Upload Time",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Uploaded: ${document.uploadTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.History,
                        contentDescription = "Last Update",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Last updated: ${
                            document.lastUpdate.format(
                                DateTimeFormatter.ofPattern(
                                    "yyyy-MM-dd HH:mm"
                                )
                            )
                        }",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Opening document: ${document.fileName}")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Visibility, contentDescription = "View")
                    Spacer(Modifier.width(4.dp))
                    Text("View")
                }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(
                    onClick = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Downloading document: ${document.fileName}")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Download, contentDescription = "Download")
                    Spacer(Modifier.width(4.dp))
                    Text("Download")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementCard(announcement: AnnouncementItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {},
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Campaign,
                    contentDescription = "Announcement",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = announcement.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = announcement.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Upload Time: ${announcement.uploadTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentCard(assignment: AssignmentItem, onSubmitClick: (AssignmentItem) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {},
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Assignment,
                    contentDescription = "Assignment",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = assignment.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = assignment.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Upload Time: ${assignment.uploadTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.End)
            )
            Spacer(Modifier.height(16.dp))

            when (assignment.status) {
                AssignmentStatus.NOT_SUBMITTED -> {
                    Button(
                        onClick = { onSubmitClick(assignment) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.UploadFile, contentDescription = "Submit Assignment")
                        Spacer(Modifier.width(8.dp))
                        Text("Submit Your Assignment")
                    }
                }

                AssignmentStatus.SUBMITTED -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Submitted",
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Assignment Submitted",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Waiting for grading...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                AssignmentStatus.GRADED -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Grade,
                            contentDescription = "Graded",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Assignment Graded",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentSubmissionBottomSheet(
    assignment: AssignmentItem,
    onDismiss: () -> Unit,
    onSubmit: (message: String, fileUri: Uri?) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    var messageText by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }

    var messageError by remember { mutableStateOf(false) }
    var fileError by remember { mutableStateOf(false) }

    ModalBottomSheet(
        modifier = Modifier.safeDrawingPadding(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
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
                    text = "Submit Assignment",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )


                Text(
                    text = "\"${assignment.title}\"",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )



                OutlinedTextField(
                    value = messageText,
                    onValueChange = {
                        messageText = it
                        messageError = it.isBlank()
                    },
                    label = { Text("Your Message") },
                    isError = messageError,
                    supportingText = {
                        if (messageError) Text("Message is required")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    maxLines = 8,
                    shape = RoundedCornerShape(12.dp)
                )


                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Upload Assignment File",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
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
                                        "content://dummy/path/to/my_assignment.pdf".toUri()
                                    selectedFileName = "my_assignment_document.pdf"
                                    fileError = false
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.AttachFile, contentDescription = "Choose File")
                            Spacer(Modifier.width(6.dp))
                            Text("Choose File")
                        }

                        if (selectedFileName != null) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = selectedFileName ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = {
                                        selectedFileUri = null
                                        selectedFileName = null
                                        fileError = true
                                    }
                                ) {
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
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }


                    if (fileError) {
                        Text(
                            text = "File is required",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Text(
                        text = "Accepted formats: PDF, DOCX, JPG (Max 25MB)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

            }

            SnackbarHost(hostState = snackbarHostState)


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Cancel")
                }

                Spacer(Modifier.width(16.dp))

                Button(
                    onClick = {
                        messageError = messageText.isBlank()
                        fileError = selectedFileUri == null

                        if (!messageError && !fileError) {
                            coroutineScope.launch {
                                sheetState.hide()
                                onSubmit(messageText, selectedFileUri)
                                onDismiss()
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Please fill in all required fields.")
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !messageError && !fileError
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Submit")
                    Spacer(Modifier.width(8.dp))
                    Text("Submit")
                }
            }

        }
    }
}
