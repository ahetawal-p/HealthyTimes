package com.capsule.healthytimes.core

import com.capsule.healthytimes.core.di.HealthyTimesCoreComponent

/**
 * The core provider interface which the Application(context) should implement.
 * This enables us to share all the core dependencies across different de-coupled modules.
 * With this all the core dependencies can be provided using applicationContext.
 */
interface CoreComponentProvider {
    fun provideCoreComponent(): HealthyTimesCoreComponent
}