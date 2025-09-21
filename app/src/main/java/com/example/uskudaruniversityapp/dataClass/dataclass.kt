package com.example.uskudaruniversityapp.dataClass

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDate


data class FilterItem(val name: String, val count: Int)

data class ServiceItem(
    val icon: ImageVector,
    val text: String,
    val category: String,
)

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String,
    val activeIcon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem("home", Icons.Outlined.Home, "Home", Icons.Filled.Home),
    BottomNavItem(
        "updates", Icons.AutoMirrored.Outlined.TrendingUp, "Updates",
        Icons.AutoMirrored.Filled.TrendingUp
    ),
    BottomNavItem("qr", Icons.Outlined.QrCodeScanner, "Attendance", Icons.Filled.QrCodeScanner),
    BottomNavItem("quick_menu", Icons.Outlined.GridView, "Menu", Icons.Filled.GridView),
    BottomNavItem("profile", Icons.Outlined.Person, "Profile", Icons.Filled.Person)
)

data class Course(
    val code: String,
    val name: String,
    val instructorName: String,
    val instructorEmail: String,
)


data class UserProfile(
    val name: String,
    val studentId: String,
    val email: String,
    val year: String,
    val gpa: Double,
)

data class CourseProfilePage(
    val name: String,
    val time: String,
    val location: String,
    val breaks: Int,
)

data class GpaHistory(
    val year: String,
    val gpa: Float,
)

data class SemesterInfo(
    val name: String,
    val coursesTaken: Int,
    val totalCredits: Int,
    val totalEcts: Int,
)


///////////////////////////////////////////////////////////////////////////////////////

// food page data class
data class FoodItem(
    val name: String,
    val calories: Int,
    val iconResId: Int,
    val type: FoodType,
)

enum class FoodType {
}

data class MealPeriod(
    val name: String,
    val items: List<FoodItem>,
)

data class DailyMenu(
    val date: LocalDate,
    val mealPeriods: List<MealPeriod>,
)


data class PlatformItem(
    val name: String,
    val iconResId: Int,
)


