package com.capsule.healthytimes.core.domain.model

import com.capsule.healthytimes.core.di.network.NetworkModule
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Articles(
    @Json(name = "response")
    val response: ArticlesResponse

)

fun Articles.toDomain(): ArticlesFeedList {
    val articleList = mutableListOf<Article>()
    this.response.docs.forEach { doc ->
        val headline = doc.headline.printHeadline ?: doc.headline.main
        val thumbNail = doc.multiMedia.find { item ->
            item.type == "image" && item.subtype == "thumbnail"
        }
        articleList.add(
            Article(
                id = doc.id,
                abstractInfo = doc.abstract,
                url = doc.webUrl,
                headline = headline,
                thumbnail = if (thumbNail != null) "${NetworkModule.API_IMAGE_BASE_URL}${thumbNail.url}" else null
            )
        )
    }
    return ArticlesFeedList(articleList, this.response.meta.totalResults)
}

@JsonClass(generateAdapter = true)
data class Meta(
    @Json(name = "hits")
    val totalResults: Int,
    val offset: Int
)

@JsonClass(generateAdapter = true)
data class ArticlesResponse(
    val docs: Array<Docs>,
    @Json(name = "meta")
    val meta: Meta
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArticlesResponse

        if (!docs.contentEquals(other.docs)) return false

        return true
    }

    override fun hashCode(): Int {
        return docs.contentHashCode()
    }
}

@JsonClass(generateAdapter = true)
data class Docs(
    @Json(name = "_id")
    val id: String,
    val abstract: String,
    @Json(name = "web_url")
    val webUrl: String,
    val headline: Headline,
    @Json(name = "multimedia")
    val multiMedia: Array<Multimedia>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Docs

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

@JsonClass(generateAdapter = true)
data class Headline(
    val main: String,
    @Json(name = "print_headline")
    val printHeadline: String?
)

@JsonClass(generateAdapter = true)
data class Multimedia(
    val type: String,
    val subtype: String,
    val url: String
)