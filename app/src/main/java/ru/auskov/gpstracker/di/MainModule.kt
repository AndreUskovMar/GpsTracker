package ru.auskov.gpstracker.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
}