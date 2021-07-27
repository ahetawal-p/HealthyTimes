package com.capsule.healthytimes.features.bookmarks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import com.capsule.healthytimes.common.ArticleListAdapter
import com.capsule.healthytimes.common.ViewState
import com.capsule.healthytimes.core.di.viewmodel.ViewModelProviderFactory
import com.capsule.healthytimes.databinding.BookmarksListFragmentBinding
import com.capsule.healthytimes.extensions.coreComponent
import com.capsule.healthytimes.features.bookmarks.di.DaggerBookmarkListComponent
import javax.inject.Inject

class BookmarksListFragment : Fragment() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    lateinit var imageLoader: ImageLoader

    private val viewModel by viewModels<BookmarksListViewModel> { providerFactory }

    private var _binding: BookmarksListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        DaggerBookmarkListComponent
            .builder()
            .healthyTimesCoreComponent(coreComponent())
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchFavs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BookmarksListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val articleListAdapter = ArticleListAdapter(imageLoader)
        articleListAdapter.onClickArticle = {
            val action =
                BookmarksListFragmentDirections.actionBookmarkToDetail(it)
            findNavController().navigate(action)
        }
        binding.bookmarkList.adapter = articleListAdapter

        viewModel.data.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ViewState.Success -> {
                    if (state.data.results.isEmpty()) {
                        binding.noBooksContainer.isGone = false
                    } else {
                        articleListAdapter.submitList(state.data.results)
                        binding.noBooksContainer.isGone = true
                        binding.bookmarkList.isGone = false
                    }
                    binding.progressBar.isGone = true
                }
                is ViewState.Error -> {

                }
                ViewState.Loading -> {
                    binding.progressBar.isGone = false
                    binding.bookmarkList.isGone = true
                }
                else -> {
                    // no nothing
                }
            }
        }
    }
}