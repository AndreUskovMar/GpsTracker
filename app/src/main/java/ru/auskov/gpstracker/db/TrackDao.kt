package ru.auskov.gpstracker.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.auskov.gpstracker.main.track.data.TrackData

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackData)

    @Delete
    suspend fun deleteTrack(track: TrackData)

    @Query("SELECT * FROM tracks")
    fun getAllTracks(): Flow<List<TrackData>>
}