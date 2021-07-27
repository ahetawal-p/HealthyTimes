package com.capsule.healthytimes.core.di

import javax.inject.Scope

/**
 * Scope used for any fragment level dependencies
 */
@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentScope

/**
 * Scope used for any dependencies at Activity level.
 * Currently only for MainActivity
 */
@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope