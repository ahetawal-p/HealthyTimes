package com.capsule.healthytimes.features.details.di

import com.capsule.healthytimes.core.di.FragmentScope
import com.capsule.healthytimes.core.di.HealthyTimesCoreComponent
import com.capsule.healthytimes.features.details.ArticleDetailFragment
import dagger.Component

@Component(
    modules = [ArticleDetailModule::class],
    dependencies = [HealthyTimesCoreComponent::class]
)
@FragmentScope
interface ArticleDetailComponent {
    fun inject(fragment: ArticleDetailFragment)
}