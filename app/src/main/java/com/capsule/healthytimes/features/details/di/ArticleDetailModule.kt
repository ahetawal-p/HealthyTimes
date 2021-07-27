package com.capsule.healthytimes.features.details.di

import androidx.lifecycle.ViewModel
import com.capsule.healthytimes.core.di.viewmodel.ViewModelKey
import com.capsule.healthytimes.core.domain.respository.ArticleService
import com.capsule.healthytimes.core.domain.respository.ArticlesFeedRepository
import com.capsule.healthytimes.features.details.ArticleDetailViewModel
import com.capsule.healthytimes.features.feed.data.ArticlesFeedRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [ArticleDetailModule.ArticleDetailAbstractModule::class])
class ArticleDetailModule {
    @Provides
    fun repository(articleService: ArticleService): ArticlesFeedRepository =
        ArticlesFeedRepositoryImpl(articleService)

    @Module
    internal interface ArticleDetailAbstractModule {
        @Binds
        @IntoMap
        @ViewModelKey(ArticleDetailViewModel::class)
        fun bindViewModel(viewModel: ArticleDetailViewModel): ViewModel
    }
}