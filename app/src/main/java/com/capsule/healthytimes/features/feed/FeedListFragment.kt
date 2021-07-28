package com.capsule.healthytimes.features.feed

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import coil.ImageLoader
import com.capsule.healthytimes.common.ArticleListAdapter
import com.capsule.healthytimes.common.ViewState
import com.capsule.healthytimes.core.di.viewmodel.ViewModelProviderFactory
import com.capsule.healthytimes.databinding.FeedListFragmentBinding
import com.capsule.healthytimes.databinding.ListItemLoadingBinding
import com.capsule.healthytimes.extensions.coreComponent
import com.capsule.healthytimes.features.feed.di.DaggerFeedListComponent
import com.capsule.healthytimes.util.InfiniteListScrollListener
import com.capsule.healthytimes.util.SingleItemAdapter
import javax.inject.Inject

class FeedListFragment : Fragment() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    lateinit var imageLoader: ImageLoader

    private val viewModel by viewModels<FeedListViewModel> { providerFactory }

    private var _binding: FeedListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        DaggerFeedListComponent
            .builder()
            .healthyTimesCoreComponent(coreComponent())
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FeedListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val articleListAdapter = ArticleListAdapter(imageLoader)
        val loadingAdapter =
            SingleItemAdapter((ListItemLoadingBinding::inflate)) { binding, hasMore: Boolean ->
                binding.root.isGone = !hasMore
            }
        articleListAdapter.onClickArticle = {
            val action =
                FeedListFragmentDirections.actionFeedToArticleDetail(it)
            findNavController().navigate(action)
        }

        binding.feedList.adapter = ConcatAdapter(articleListAdapter, loadingAdapter)
        binding.feedList.addOnScrollListener(InfiniteListScrollListener {
            viewModel.fetchNextPage()
        })
        binding.refreshFeed.setOnRefreshListener {
            viewModel.refreshFeed()
        }

        viewModel.data.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ViewState.Success -> {
                    articleListAdapter.submitList(state.data.results)
                    loadingAdapter.value = state.data.hasMore
                    binding.progressBar.isGone = true
                    binding.feedList.isGone = false
                    binding.refreshFeed.isRefreshing = false
                    binding.noResults.isGone = true
                }
                is ViewState.Error -> {
                    binding.progressBar.isGone = true
                    binding.feedList.isGone = true
                    binding.noResults.isGone = false
                }
                ViewState.Loading -> {
                    binding.progressBar.isGone = false
                    binding.feedList.isGone = true
                    binding.noResults.isGone = true
                }
                else -> {
                    // no nothing
                }
            }
        }

    }


}