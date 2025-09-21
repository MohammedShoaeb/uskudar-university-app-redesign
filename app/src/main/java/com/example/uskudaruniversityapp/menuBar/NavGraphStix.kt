package com.example.uskudaruniversityapp.menuBar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.uskudaruniversityapp.stixPages.QrAttendanceScreen
import com.example.uskudaruniversityapp.stixPages.StixAppShell
import com.example.uskudaruniversityapp.stixPages.StixAppointmentPage
import com.example.uskudaruniversityapp.stixPages.StixChatPage
import com.example.uskudaruniversityapp.stixPages.StixCourseDetailPage
import com.example.uskudaruniversityapp.stixPages.StixEducationFeePage
import com.example.uskudaruniversityapp.stixPages.StixMessagesPage
import com.example.uskudaruniversityapp.stixPages.StixPage

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.stixGraph(navController: NavController) {
    navigation(
        startDestination = "stix_course_list",
        route = "stix_graph"
    ) {
        composable("stix_course_list") {
            StixAppShell(navController, title = "Courses") {
                StixPage(navController)
            }
        }

        composable("stix_appointment") {
            StixAppShell(navController, title = "Appointment") {
                StixAppointmentPage(navController)
            }
        }

        composable("stix_attendance") {
            StixAppShell(navController, title = "Attendance") {
                QrAttendanceScreen(navController)
            }
        }

        composable("stix_messages") {
            StixAppShell(navController, title = "Messages") {
                StixMessagesPage(navController)
            }
        }

        composable("stix_education_fee") {
            StixAppShell(navController, title = "Education Fee") {
                StixEducationFeePage(navController)
            }
        }

        composable(
            route = "stix_chat/{messageId}",
            arguments = listOf(navArgument("messageId") { type = NavType.StringType })
        ) { backStackEntry ->
            val messageId = backStackEntry.arguments?.getString("messageId")
            StixChatPage(navController = navController, messageId = messageId)
        }

        composable("stix_course_detail") { backStackEntry ->
            StixCourseDetailPage(navController = navController)
        }
    }
}