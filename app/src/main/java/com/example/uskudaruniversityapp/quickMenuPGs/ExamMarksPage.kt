package com.example.uskudaruniversityapp.quickMenuPGs


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlin.math.roundToInt


data class GradeComponent(
    val name: String,
    val score: Double,
    val weight: Double,
    val maxScore: Double = 100.0,
)

data class CourseWithDetails(
    val courseCode: String,
    val courseName: String,
    val finalNumericGrade: Int?,
    val finalLetterGrade: String?,
    val gradeComponents: List<GradeComponent>,
) {
    val currentAverage: Double
        get() {
            if (gradeComponents.isEmpty()) return 0.0

            var totalWeightedScore = 0.0
            var totalWeight = 0.0

            gradeComponents.forEach { component ->
                totalWeightedScore += (component.score / component.maxScore) * component.weight
                totalWeight += component.weight
            }
            return if (totalWeight > 0) (totalWeightedScore / totalWeight) * 100.0 else 0.0
        }

    val currentWeightCovered: Double
        get() = gradeComponents.sumOf { it.weight }
}

data class SemesterWithCourses(
    val academicYear: String,
    val semesterName: String,
    val courses: List<CourseWithDetails>,
)

val dummyStudentGrades = listOf(
    SemesterWithCourses(
        academicYear = "2023-2024",
        semesterName = "Bahar (Spring Semester)",
        courses = listOf(
            CourseWithDetails(
                courseCode = "COMP301",
                courseName = "Advanced Data Structures",
                finalNumericGrade = 92,
                finalLetterGrade = "AA",
                gradeComponents = listOf(
                    GradeComponent("Midterm 1", 90.0, 0.25),
                    GradeComponent("Midterm 2", 88.0, 0.25),
                    GradeComponent("Quizzes", 95.0, 0.20),
                    GradeComponent("Final Exam", 98.0, 0.30)
                )
            ),
            CourseWithDetails(
                courseCode = "MATH305",
                courseName = "Differential Equations",
                finalNumericGrade = 75,
                finalLetterGrade = "BB",
                gradeComponents = listOf(
                    GradeComponent("Midterm", 70.0, 0.40),
                    GradeComponent("Homework", 80.0, 0.20),
                    GradeComponent("Final Exam", 78.0, 0.40)
                )
            ),
            CourseWithDetails(
                courseCode = "PHYS203",
                courseName = "Electromagnetism",
                finalNumericGrade = null,
                finalLetterGrade = null,
                gradeComponents = listOf(
                    GradeComponent("Midterm 1", 65.0, 0.30),
                    GradeComponent("Lab Reports", 80.0, 0.20)

                )
            ),
            CourseWithDetails(
                courseCode = "HIST101",
                courseName = "History of Civilization",
                finalNumericGrade = 88,
                finalLetterGrade = "BA",
                gradeComponents = listOf(
                    GradeComponent("Midterm", 85.0, 0.35),
                    GradeComponent("Essay", 90.0, 0.30),
                    GradeComponent("Final Exam", 89.0, 0.35)
                )
            ),
            CourseWithDetails(
                courseCode = "ART100",
                courseName = "Introduction to Art History",
                finalNumericGrade = 95,
                finalLetterGrade = "AA",
                gradeComponents = listOf(
                    GradeComponent("Project 1", 92.0, 0.20),
                    GradeComponent("Midterm", 96.0, 0.40),
                    GradeComponent("Final Exam", 97.0, 0.40)
                )
            )
        )
    ),
    SemesterWithCourses(
        academicYear = "2022-2023",
        semesterName = "Güz (Fall Semester)",
        courses = listOf(
            CourseWithDetails(
                courseCode = "COMP201",
                courseName = "Object-Oriented Programming",
                finalNumericGrade = 80,
                finalLetterGrade = "BB",
                gradeComponents = listOf(
                    GradeComponent("Midterm", 75.0, 0.40),
                    GradeComponent("Assignments", 85.0, 0.20),
                    GradeComponent("Final Exam", 82.0, 0.40)
                )
            ),
            CourseWithDetails(
                courseCode = "MATH201",
                courseName = "Calculus II",
                finalNumericGrade = 68,
                finalLetterGrade = "CC",
                gradeComponents = listOf(
                    GradeComponent("Midterm 1", 60.0, 0.25),
                    GradeComponent("Midterm 2", 55.0, 0.25),
                    GradeComponent("Quizzes", 70.0, 0.10),
                    GradeComponent("Final Exam", 75.0, 0.40)
                )
            )
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamMarksPage(navController: NavController) {
    val context = LocalContext.current

    Scaffold(
    ) { paddingValues ->
        if (dummyStudentGrades.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No exam marks available yet.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(dummyStudentGrades) { semester ->
                    SemesterSection(semester = semester)
                }
            }
        }
    }
}

@Composable
fun SemesterSection(semester: SemesterWithCourses) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "${semester.academicYear} ${semester.semesterName}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            semester.courses.forEach { course ->
                CourseGradesCard(course = course)
            }
        }
    }
}

@Composable
fun CourseGradesCard(course: CourseWithDetails) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()

            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = course.courseCode,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = course.courseName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (course.finalLetterGrade != null) {
                        Text(
                            text = course.finalLetterGrade,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (course.finalLetterGrade == "FF" || course.finalLetterGrade == "FD") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    } else {
                        if (!expanded) {
                            Text(
                                text = "${course.currentAverage.roundToInt()}%",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }

                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(durationMillis = 300)),
                exit = shrinkVertically(animationSpec = tween(durationMillis = 300))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "Grade Breakdown:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                            .padding(vertical = 6.dp, horizontal = 4.dp)
                    ) {
                        Text(
                            text = "Component",
                            modifier = Modifier.weight(0.5f),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Score",
                            modifier = Modifier.weight(0.25f),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Weight",
                            modifier = Modifier.weight(0.25f),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))

                    if (course.gradeComponents.isEmpty()) {
                        Text(
                            text = "No grade components entered yet.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        course.gradeComponents.forEach { component ->
                            GradeComponentRow(component = component)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (course.finalLetterGrade == null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Current Calculated Average:",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${course.currentAverage.roundToInt()}%",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Text(
                            text = "(${(course.currentWeightCovered * 100).roundToInt()}% of total grade)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GradeComponentRow(component: GradeComponent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = component.name,
            modifier = Modifier.weight(0.5f),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "${component.score.roundToInt()}/${component.maxScore.roundToInt()}",
            modifier = Modifier.weight(0.25f),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "${(component.weight * 100).roundToInt()}%",
            modifier = Modifier.weight(0.25f),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PreviewExamMarksPageRedesigned() {
    MaterialTheme {
//        ExamMarksPage()
    }
}