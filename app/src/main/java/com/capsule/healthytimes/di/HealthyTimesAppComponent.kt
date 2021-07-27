package com.capsule.healthytimes.di

import android.app.Application
import com.capsule.healthytimes.HealthyTimesApp
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [HealthyTimesAppModule::class])
interface HealthyTimesAppComponent {
    fun inject(app: HealthyTimesApp)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): HealthyTimesAppComponent
    }
}