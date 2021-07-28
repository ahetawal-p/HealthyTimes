package com.capsule.healthytimes.features.details

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieAnimationView
import com.capsule.healthytimes.R
import com.capsule.healthytimes.common.ViewState
import com.capsule.healthytimes.core.di.viewmodel.ViewModelProviderFactory
import com.capsule.healthytimes.databinding.ArticleDetailFragmentBinding
import com.capsule.healthytimes.extensions.coreComponent
import com.capsule.healthytimes.extensions.share
import com.capsule.healthytimes.features.details.di.DaggerArticleDetailComponent
import javax.inject.Inject

class ArticleDetailFragment : Fragment() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private val viewModel by viewModels<ArticleDetailViewModel> { providerFactory }

    private var _binding: ArticleDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val args: ArticleDetailFragmentArgs by navArgs()

    private var favoriteMenu: MenuItem? = null

    private var shareMenu: MenuItem? = null

    private var isFavorite = false

    override fun onAttach(context: Context) {
        DaggerArticleDetailComponent
            .builder()
            .healthyTimesCoreComponent(coreComponent())
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.setupInitialState(args.article, args.source)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ArticleDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webView = binding.articleDetailWebview
        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                binding.progressBar.isGone = true
                buildMenu()
                webView.isGone = false
                super.onPageFinished(view, url)
            }
        }

        viewModel.loadState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ViewState.Success -> {
                    isFavorite = state.data.isFav
                    webView.loadUrl(state.data.url)
                }
                else -> {
                    //do nothing
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.article_detail_menu_options, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        shareMenu = menu.findItem(R.id.shareArticle).also {
            it.actionView.setOnClickListener {
                requireActivity().share(
                    getString(R.string.share_title),
                    args.article.url
                )
            }
            it.isVisible = false
        }
        favoriteMenu = menu.findItem(R.id.favoriteArticle).also {
            it.actionView.setOnClickListener { toggleFavorite() }
            it.isVisible = false
        }
    }

    private fun toggleFavorite() {
        favoriteMenu?.let { menu ->
            val imageView = menu.actionView.findViewById<ImageView>(R.id.favorite_icon)
            val animationView =
                menu.actionView.findViewById<LottieAnimationView>(R.id.favoriteAnimation)
            isFavorite = !isFavorite
            viewModel.toggleFavorite(args.article, isFavorite)

            if (isFavorite) {
                animationView.isGone = false
                favoriteMenu!!.actionView.isClickable = false
                imageView.isGone = true

                animationView.let {
                    it.isGone = false
                    it.addAnimatorListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) {}
                        override fun onAnimationCancel(animation: Animator?) {}
                        override fun onAnimationStart(animation: Animator?) {}
                        override fun onAnimationEnd(anim: Animator?) {
                            animationView.isGone = true
                            imageView.setImageResource(R.drawable.ic_favorited)
                            imageView.isGone = false
                            menu.actionView.isClickable = true
                        }
                    })
                    it.playAnimation()
                }
            } else {
                animationView.isGone = true
                imageView.let {
                    it.setImageResource(R.drawable.ic_favorite)
                    it.isGone = false
                }
            }
        }

    }

    private fun buildMenu() {
        favoriteMenu?.apply {
            isVisible = true
            val favIconRes =
                if (isFavorite) R.drawable.ic_favorited else R.drawable.ic_favorite
            val favIcon: ImageView =
                favoriteMenu!!.actionView.findViewById(R.id.favorite_icon)
            favIcon.setImageResource(favIconRes)
        }
        shareMenu?.apply {
            isVisible = true
        }
    }
}