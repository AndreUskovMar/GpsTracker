package ru.auskov.gpstracker.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.auskov.gpstracker.db.MainDb
import ru.auskov.gpstracker.location.LocationDataSharer
import ru.auskov.gpstracker.utils.SettingsPreferencesManager
import ru.auskov.gpstracker.utils.TimerManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideTimeManager(): TimerManager {
        return TimerManager()
    }

    @Provides
    @Singleton
    fun provideLocationFlow(): LocationDataSharer {
        return LocationDataSharer()
    }

    @Provides
    @Singleton
    fun provideSettingsPreferencesManager(application: Application): SettingsPreferencesManager {
        return SettingsPreferencesManager(application)
    }

    @Provides
    @Singleton
    fun provideMainDb(application: Application): MainDb {
        return Room.databaseBuilder(
            application,
            MainDb::class.java,
            "gps_tracker"
        ).build()
    }
}