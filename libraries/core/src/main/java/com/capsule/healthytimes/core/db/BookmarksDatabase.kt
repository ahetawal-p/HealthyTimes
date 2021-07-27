package com.capsule.healthytimes.core.db

import android.content.Context
import androidx.room.*
import com.capsule.healthytimes.core.domain.model.Article
import kotlinx.coroutines.flow.Flow

/**
 * Dao to access track events from DB
 */
@Dao
interface BookmarkDao {
    @Query("SELECT * from articles")
    fun getAllFavs(): Flow<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article): Long

    @Query("SELECT * FROM articles WHERE id = :id")
    fun findArticle(id: String): Article?

    @Delete
    suspend fun deleteFav(article: Article)
}

/**
 * UserActivity database
 */
@Database(entities = [Article::class], version = 1, exportSchema = false)
abstract class BookmarksDatabase : RoomDatabase() {

    abstract fun bookMarkDao(): BookmarkDao

    companion object {
        @Volatile
        private var INSTANCE: BookmarksDatabase? = null

        fun getDatabase(context: Context): BookmarksDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    BookmarksDatabase::class.java,
                    "bookmarks_db"
                ).fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}