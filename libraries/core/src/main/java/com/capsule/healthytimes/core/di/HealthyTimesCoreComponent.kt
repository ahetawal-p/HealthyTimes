package com.capsule.healthytimes.core.di

import android.app.Application
import coil.ImageLoader
import com.capsule.healthytimes.core.HealthyTimesCore
import com.capsule.healthytimes.core.di.network.NetworkModule
import com.capsule.healthytimes.core.di.viewmodel.ViewModelFactoryModule
import com.capsule.healthytimes.core.domain.respository.ArticleService
import com.capsule.healthytimes.core.domain.respository.BookmarksRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [HealthyTimesCoreModule::class,
        ViewModelFactoryModule::class,
        NetworkModule::class]
)
@Singleton
interface HealthyTimesCoreComponent {

    fun inject(core: HealthyTimesCore)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): HealthyTimesCoreComponent
    }

    fun getArticleService(): ArticleService

    fun getBookmarkRepo(): BookmarksRepository

    fun getImageLoader(): ImageLoader
}