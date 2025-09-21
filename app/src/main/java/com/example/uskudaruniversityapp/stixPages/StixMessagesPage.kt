package com.example.uskudaruniversityapp.stixPages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.uskudaruniversityapp.dataClass.StixMessage
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun StixMessagesPage(navController: NavController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val allMessages = remember {
        mutableStateListOf(
            StixMessage("msg1", "SALIM JIBRIN DANBATTA", "Check announcement...", LocalDate.of(2025, 4, 20), 1, true),
            StixMessage("msg2", "TÜRKER TEKİN ERGÜZEL", "Project deadline approaching.", LocalDate.of(2025, 4, 19), 0, true),
            StixMessage("msg3", "BELAYNESH CHEKOL", "Inquiry about the course.", LocalDate.of(2025, 4, 18), 1, true),
            StixMessage("msg4", "You", "Question about assignment 3.", LocalDate.of(2025, 4, 17), 0, false),
            StixMessage("msg5", "SALIM JIBRIN DANBATTA", "Single Course Exam", LocalDate.of(2025, 4, 20), 1, true)
        )
    }

    val tabs = listOf("All", "Incoming", "Unread", "Read", "Outgoing")
    val pagerState = rememberPagerState(pageCount = {tabs.size})
    val coroutineScopePager = rememberCoroutineScope()

    var showSendMessageBottomSheet by remember { mutableStateOf(false) }

    StixAppShell(
        navController = navController,
        title = "Inbox",
        showBackButton = true,
        showBottomBar = true,
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
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScopePager.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(title) },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalPager(

                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val filteredMessages = when (tabs[page]) {
                    "All" -> allMessages
                    "Incoming" -> allMessages.filter { it.isIncoming }
                    "Unread" -> allMessages.filter { it.unreadCount > 0 }
                    "Read" -> allMessages.filter { it.unreadCount == 0 && it.isIncoming }
                    "Outgoing" -> allMessages.filter { !it.isIncoming }
                    else -> allMessages
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredMessages) { message ->
                        MessageListItem(message = message) {
                            navController.navigate("stix_chat/${message.id}")
                        }
                    }
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FloatingActionButton(
                onClick = { showSendMessageBottomSheet = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Create, "New message")
            }
        }
    }


    if (showSendMessageBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.safeDrawingPadding(),
            onDismissRequest = { showSendMessageBottomSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            StixSendMessageContent(
                onDismiss = { showSendMessageBottomSheet = false },
                onMessageSent = { contact, course, subject, message, fileUri ->
                    coroutineScope.launch {
                        allMessages.add(
                            0,
                            StixMessage(
                                id = "msg_${System.currentTimeMillis()}",
                                sender = "You",
                                subjectSnippet = subject,
                                date = LocalDate.now(),
                                unreadCount = 0,
                                isIncoming = false
                            )
                        )
                        snackbarHostState.showSnackbar("Message to '$contact' sent successfully!")
                        showSendMessageBottomSheet = false
                    }
                },
                snackbarHostState = snackbarHostState
            )
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageListItem(message: StixMessage, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Sender Avatar",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(16.dp))


            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = message.sender,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = message.subjectSnippet,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(16.dp))


            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = message.date.format(DateTimeFormatter.ofPattern("dd MMM")),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (message.unreadCount > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Badge(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Text(message.unreadCount.toString())
                    }
                }
            }
        }
    }
}