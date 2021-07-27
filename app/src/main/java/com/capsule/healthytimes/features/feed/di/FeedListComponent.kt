package com.capsule.healthytimes.features.feed.di

import com.capsule.healthytimes.core.di.FragmentScope
import com.capsule.healthytimes.core.di.HealthyTimesCoreComponent
import com.capsule.healthytimes.features.feed.FeedListFragment
import dagger.Component

@Component(modules = [FeedListModule::class], dependencies = [HealthyTimesCoreComponent::class])
@FragmentScope
interface FeedListComponent {
    fun inject(fragment: FeedListFragment)
}