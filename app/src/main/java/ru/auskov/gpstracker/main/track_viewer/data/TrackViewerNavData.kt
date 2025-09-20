package ru.auskov.gpstracker.main.track_viewer.data

import kotlinx.serialization.Serializable

@Serializable
data class TrackViewerNavData (
    val name: String,
    val date: String,
    val distance: String,
    val time: String = "",
    val averageSpeed: String,
    val geoPoints: String
)