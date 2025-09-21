package com.example.uskudaruniversityapp.dataClass

import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDate

data class StixMessage(
    val id: String,
    val sender: String,
    val subjectSnippet: String,
    val date: LocalDate,
    val unreadCount: Int,
    val isIncoming: Boolean
)

data class StixChatMessage(
    val id: String,
    val sender: String?,
    val content: String,
    val timestamp: String,
    val isSentByMe: Boolean
)
data class BottomNavItemSTIX(
    val name: String,
    val icon: ImageVector,
    val route: String,
    val activeIcon: ImageVector

)

data class StixAppointment(
    val id: String,
    val subject: String,
    val contact: String,
    val date: LocalDate,
    val time: String,
    val status: String
)