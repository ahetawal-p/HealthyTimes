package com.capsule.healthytimes.core.di

import android.app.Application
import coil.ImageLoader
import com.capsule.healthytimes.core.db.BookmarkDao
import com.capsule.healthytimes.core.db.BookmarksDatabase
import com.capsule.healthytimes.core.domain.respository.BookmarksRepository
import com.capsule.healthytimes.core.domain.respository.shared.BookmarksRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HealthyTimesCoreModule {

    @Provides
    @Singleton
    fun provideDb(context: Application): BookmarksDatabase {
        return BookmarksDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideDbDao(db: BookmarksDatabase): BookmarkDao {
        return db.bookMarkDao()
    }

    @Provides
    @Singleton
    fun provideBookmarkRepo(dao: BookmarkDao): BookmarksRepository {
        return BookmarksRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideImageLoader(context: Application): ImageLoader {
        return ImageLoader
            .Builder(context)
            .availableMemoryPercentage(0.25)
            .crossfade(true)
            .build()
    }

}