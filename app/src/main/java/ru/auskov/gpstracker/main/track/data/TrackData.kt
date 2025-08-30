package ru.auskov.gpstracker.main.track.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackData(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val date: String,
    val distance: String,
    val time: String,
    val averageSpeed: String,
    val geoPoints: String
)
