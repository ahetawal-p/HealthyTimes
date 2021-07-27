package com.capsule.healthytimes.features.bookmarks.di

import com.capsule.healthytimes.core.di.FragmentScope
import com.capsule.healthytimes.core.di.HealthyTimesCoreComponent
import com.capsule.healthytimes.features.bookmarks.BookmarksListFragment
import dagger.Component

@Component(modules = [BookmarkListModule::class], dependencies = [HealthyTimesCoreComponent::class])
@FragmentScope
interface BookmarkListComponent {
    fun inject(fragment: BookmarksListFragment)
}