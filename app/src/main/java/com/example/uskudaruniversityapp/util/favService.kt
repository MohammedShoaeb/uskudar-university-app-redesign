package com.example.uskudaruniversityapp.util

// Define possible outcomes for the favorite toggle action
sealed class FavoriteToggleResult {
    // Indicates the favorite status was successfully toggled
    data class Toggled(val serviceName: String, val isNowFavorite: Boolean) : FavoriteToggleResult()
    // Indicates the action failed because the favorite limit was reached
    object LimitReached : FavoriteToggleResult()
}