package ru.auskov.gpstracker.main.track.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.auskov.gpstracker.db.MainDb
import ru.auskov.gpstracker.main.track.data.TrackData
import javax.inject.Inject

@HiltViewModel
class TrackViewModel @Inject constructor(
    private val mainDb: MainDb,
) : ViewModel() {
    val trackList = mainDb.trackDao.getAllTracks()

    var trackToDelete: TrackData? = null

    fun deleteTrack() = viewModelScope.launch(Dispatchers.IO) {
        trackToDelete?.let { mainDb.trackDao.deleteTrack(it) }
    }
}