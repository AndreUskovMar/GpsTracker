package ru.auskov.gpstracker.main.track.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.auskov.gpstracker.db.MainDb
import javax.inject.Inject

@HiltViewModel
class TrackViewModel @Inject constructor(
  mainDb: MainDb,
) : ViewModel() {
    val trackList = mainDb.trackDao.getAllTracks()
}