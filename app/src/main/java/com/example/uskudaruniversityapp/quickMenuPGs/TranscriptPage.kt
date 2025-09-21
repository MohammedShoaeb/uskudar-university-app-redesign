package com.example.uskudaruniversityapp.quickMenuPGs

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class CourseEntry(
    val name: String,
    val grade: Int,
    val letter: String
)

data class SemesterTranscript(
    val year: String,
    val semesterName: String,
    val semesterNumber: String,
    val courses: List<CourseEntry>
)


val dummyTranscriptData = listOf(
    SemesterTranscript(
        year = "2021-2022",
        semesterName = "Bahar",
        semesterNumber = "2nd Semester",
        courses = listOf(
            CourseEntry("Introduction to algorithm and programming", 91, "AA"),
            CourseEntry("Discrete Mathamtics", 95, "DD"),
            CourseEntry("English II", 40, "DC"),
            CourseEntry("Calculus II", 10, "FF"),
            CourseEntry("Basic linear Algebra", 91, "BB")
        )
    ),
    SemesterTranscript(
        year = "2022-2023",
        semesterName = "Bahar",
        semesterNumber = "1st Semester",
        courses = listOf(
            CourseEntry("Introduction to algorithm and programming", 91, "AA"),
            CourseEntry("Discrete Mathamtics", 95, "DD"),
            CourseEntry("English II", 40, "DC"),
            CourseEntry("Calculus II", 10, "FF"),
            CourseEntry("Basic linear Algebra", 91, "BB")
        )
    ),
    SemesterTranscript(
        year = "2022-2023",
        semesterName = "Güz",
        semesterNumber = "2nd Semester",
        courses = listOf(
            CourseEntry("Data Structures", 88, "BA"),
            CourseEntry("Database Systems", 75, "CC"),
            CourseEntry("Operating Systems", 60, "DD"),
            CourseEntry("Software Engineering", 92, "AA")
        )
    )
)

val dummyEmptyTranscriptData = emptyList<SemesterTranscript>()


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranscriptPage() {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var isDownloading by remember { mutableStateOf(false) }


    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    if (!isDownloading) {
                        isDownloading = true
                        coroutineScope.launch {
                            delay(2000L)
                            isDownloading = false
                            snackbarHostState.showSnackbar(
                                message = "Transcript downloaded successfully!",
                                withDismissAction = true,
                                duration = SnackbarDuration.Short
                            )
                            Toast.makeText(context, "Transcript saved!", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth().padding(16.dp)
                    .height(56.dp),
                enabled = !isDownloading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (isDownloading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(16.dp))
                    Text("Downloading...")
                } else {
                    Icon(
                        Icons.Default.CloudDownload,
                        contentDescription = "Download icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Download Full Transcript", style = MaterialTheme.typography.titleMedium)
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                if (dummyTranscriptData.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.Description,
                                contentDescription = "No Transcript Available",
                                modifier = Modifier.size(72.dp),
                                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "No transcript data available.",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Please check back later or contact your academic advisor.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    itemsIndexed(dummyTranscriptData) { index, semester ->

                        val semesterKey =
                            "${semester.year} ${semester.semesterName} ${semester.semesterNumber}"
                        val isExpanded =
                            expandedStates.getOrElse(semesterKey) { false }


                        val rotationAngle by animateFloatAsState(
                            targetValue = if (isExpanded) 180f else 0f,
                            label = "arrowRotation"
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable {

                                    expandedStates[semesterKey] = !isExpanded
                                }
                                .padding(
                                    vertical = 12.dp,
                                    horizontal = 8.dp
                                ),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${semester.year} ${semester.semesterName}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "(${semester.semesterNumber})",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .rotate(rotationAngle),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }


                            AnimatedVisibility(
                                visible = isExpanded,
                                enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                                exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(MaterialTheme.colorScheme.surfaceContainer)
                                                .padding(vertical = 8.dp, horizontal = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                "Course Name",
                                                modifier = Modifier.weight(3f),
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                "Grade",
                                                modifier = Modifier.weight(1f),
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                "Letter",
                                                modifier = Modifier.weight(1f),
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                        HorizontalDivider(color = MaterialTheme.colorScheme.outline)


                                        semester.courses.forEachIndexed { courseIndex, course ->
                                            val rowBackgroundColor = if (courseIndex % 2 == 0) {
                                                MaterialTheme.colorScheme.surfaceVariant
                                            } else {
                                                MaterialTheme.colorScheme.surfaceContainer
                                            }

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(rowBackgroundColor)
                                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    course.name,
                                                    modifier = Modifier.weight(3f),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Text(
                                                    course.grade.toString(),
                                                    modifier = Modifier.weight(1f),
                                                    textAlign = TextAlign.Center,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                val letterColor = when (course.letter) {
                                                    "FF", "DC" -> MaterialTheme.colorScheme.error
                                                    "DD" -> MaterialTheme.colorScheme.tertiary
                                                    else -> MaterialTheme.colorScheme.primary
                                                }
                                                Text(
                                                    course.letter,
                                                    modifier = Modifier.weight(1f),
                                                    textAlign = TextAlign.Center,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = letterColor
                                                )
                                            }
                                            if (courseIndex < semester.courses.size - 1) {
                                                HorizontalDivider(
                                                    color = MaterialTheme.colorScheme.outlineVariant.copy(
                                                        alpha = 0.5f
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    )
}


