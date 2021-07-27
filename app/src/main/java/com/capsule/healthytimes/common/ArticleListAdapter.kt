package com.capsule.healthytimes.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import com.capsule.healthytimes.R
import com.capsule.healthytimes.core.domain.model.Article
import com.capsule.healthytimes.databinding.ArticleItemBinding

typealias OnClickArticle = (Article) -> Unit
typealias OnClickFav = (Article) -> Unit

class ArticleListAdapter(private val imageLoader: ImageLoader) :
    ListAdapter<Article, RecyclerView.ViewHolder>(ArticleDiff()) {
    var onClickArticle: OnClickArticle? = null
    var onClickFav: OnClickFav? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ArticleItemViewHolder(
            ArticleItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as ArticleItemViewHolder).bind(
            getItem(position),
            { onClickArticle?.invoke(it) },
            imageLoader
        )
}

class ArticleItemViewHolder(binding: ArticleItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val headlineView = binding.headline
    private val logoImageView = binding.logo
    private val subtitleView = binding.subTitle
    private val container = binding.container

    fun bind(
        item: Article,
        onArticleClickListener: (Article) -> Unit,
        imageLoader: ImageLoader
    ) {
        item.let { it ->
            headlineView.text = it.headline
            subtitleView.text = it.abstractInfo
            logoImageView.load(it.thumbnail, imageLoader) {
                error(R.drawable.ic_placeholder)
                fallback(R.drawable.ic_placeholder)
            }
            container.setOnClickListener { onArticleClickListener(item) }
        }
    }
}


private class ArticleDiff : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article) =
        oldItem.id == newItem.id && oldItem.isFavorite == newItem.isFavorite

    override fun areContentsTheSame(oldItem: Article, newItem: Article) =
        oldItem.id == newItem.id && oldItem.isFavorite == newItem.isFavorite

}