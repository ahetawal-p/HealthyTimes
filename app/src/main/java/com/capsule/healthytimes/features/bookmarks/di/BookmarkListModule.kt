package com.capsule.healthytimes.features.bookmarks.di

import androidx.lifecycle.ViewModel
import com.capsule.healthytimes.core.di.viewmodel.ViewModelKey
import com.capsule.healthytimes.features.bookmarks.BookmarksListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [BookmarkListModule.BookmarkListAbstractModule::class])
class BookmarkListModule {

    @Module
    internal interface BookmarkListAbstractModule {
        @Binds
        @IntoMap
        @ViewModelKey(BookmarksListViewModel::class)
        fun bindViewModel(viewModel: BookmarksListViewModel): ViewModel
    }
}