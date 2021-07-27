package com.capsule.healthytimes

import android.app.Application
import com.capsule.healthytimes.core.CoreComponentProvider
import com.capsule.healthytimes.core.HealthyTimesCore
import com.capsule.healthytimes.core.di.HealthyTimesCoreComponent
import com.capsule.healthytimes.di.DaggerHealthyTimesAppComponent
import javax.inject.Inject

class HealthyTimesApp : Application(), CoreComponentProvider {

    @Inject
    lateinit var healthyTimesCore: HealthyTimesCore

    private val appComponent by lazy {
        DaggerHealthyTimesAppComponent
            .builder()
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)

    }

    override fun provideCoreComponent(): HealthyTimesCoreComponent {
        return healthyTimesCore.coreComponent
    }
}