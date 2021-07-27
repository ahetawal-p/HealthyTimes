package com.capsule.healthytimes.core

import android.app.Application
import com.capsule.healthytimes.core.di.DaggerHealthyTimesCoreComponent

class HealthyTimesCore(val context: Application) {

    companion object {
        const val TAG = "HealthyTimesApp"
    }

    val coreComponent = DaggerHealthyTimesCoreComponent
        .builder()
        .application(context)
        .build()

    init {
        coreComponent.inject(this)
    }
}