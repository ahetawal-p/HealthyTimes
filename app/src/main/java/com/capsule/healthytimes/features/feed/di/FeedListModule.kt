package com.capsule.healthytimes.features.feed.di

import androidx.lifecycle.ViewModel
import com.capsule.healthytimes.core.di.viewmodel.ViewModelKey
import com.capsule.healthytimes.core.domain.respository.ArticleService
import com.capsule.healthytimes.core.domain.respository.ArticlesFeedRepository
import com.capsule.healthytimes.features.feed.FeedListViewModel
import com.capsule.healthytimes.features.feed.data.ArticlesFeedRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [FeedListModule.FeedListModuleAbstractModule::class])
class FeedListModule {

    @Provides
    fun repository(articleService: ArticleService): ArticlesFeedRepository =
        ArticlesFeedRepositoryImpl(articleService)

    @Module
    internal interface FeedListModuleAbstractModule {
        @Binds
        @IntoMap
        @ViewModelKey(FeedListViewModel::class)
        fun bindViewModel(viewModel: FeedListViewModel): ViewModel
    }
}