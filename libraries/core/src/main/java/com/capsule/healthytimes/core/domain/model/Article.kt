package com.capsule.healthytimes.core.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "articles")
@Parcelize
data class Article(
    @PrimaryKey
    val id: String,
    val abstractInfo: String,
    val url: String,
    val headline: String,
    val thumbnail: String? = null,
    val isFavorite: Boolean = false
) : Parcelable


data class ArticlesFeedList(
    val feedList: List<Article>,
    val totalResults: Int
)