package com.capsule.healthytimes.di

import android.app.Application
import com.capsule.healthytimes.core.HealthyTimesCore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HealthyTimesAppModule {

    @Provides
    @Singleton
    fun provideCore(context: Application): HealthyTimesCore {
        return HealthyTimesCore(context)
    }
}