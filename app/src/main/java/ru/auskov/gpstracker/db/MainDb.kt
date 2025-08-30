package ru.auskov.gpstracker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.auskov.gpstracker.main.track.data.TrackData

@Database(entities = [TrackData::class], version = 1)
abstract class MainDb: RoomDatabase() {
    abstract val trackDao: TrackDao
}