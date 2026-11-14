package com.ejemplo.grovespemusic.model

enum class UserRole {
    FAN, ARTIST
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole = UserRole.FAN,
    val profilePicture: String? = null
)

data class Band(
    val id: String,
    val name: String,
    val genre: String,
    val location: String = "CDMX",
    val description: String,
    val imageUrl: String,
    val monthlyListeners: Int,
    val totalEarnings: Double = 0.0,
    val ownerId: String? = null, // ID of the user who owns this band profile
    val songs: List<Song> = emptyList(),
    val events: List<Event> = emptyList()
)

data class Song(
    val id: String,
    val title: String,
    val duration: String,
    val streamCount: Int,
    val audioUrl: String = "" // Placeholder for future real audio
)

data class Event(
    val id: String,
    val venue: String,
    val date: String,
    val price: String
)
