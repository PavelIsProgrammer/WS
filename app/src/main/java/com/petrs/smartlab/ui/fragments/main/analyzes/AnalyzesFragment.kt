package com.petrs.smartlab.ui.fragments.main.analyzes

import com.google.android.material.appbar.AppBarLayout
import com.petrs.smartlab.R
import com.petrs.smartlab.data.ErrorType
import com.petrs.smartlab.databinding.FragmentAnalyzesBinding
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.domain.models.CatalogItemDomain
import com.petrs.smartlab.ui.base.BaseFragment
import com.petrs.smartlab.ui.base.error_dialog.ErrorDialog
import com.petrs.smartlab.ui.base.error_dialog.ErrorDialogParams
import com.petrs.smartlab.ui.fragments.main.analyzes.analyzes_adapter.AnalyzesAdapter
import com.petrs.smartlab.ui.fragments.main.analyzes.analyzes_adapter.CatalogEventListener
import com.petrs.smartlab.ui.fragments.main.analyzes.category_adapter.CategoryAdapter
import com.petrs.smartlab.ui.fragments.main.analyzes.category_adapter.CategoryEventListener
import com.petrs.smartlab.ui.fragments.main.analyzes.category_adapter.CategoryViewHolder
import com.petrs.smartlab.ui.fragments.main.analyzes.models.Category
import com.petrs.smartlab.ui.fragments.main.analyzes.news_adapter.NewsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class AnalyzesFragment : BaseFragment<FragmentAnalyzesBinding, AnalyzesViewModel>(
    FragmentAnalyzesBinding::inflate
) {
    override val viewModel: AnalyzesViewModel by viewModel()

    private lateinit var lastClickedCategory: Category

    private val newsAdapter by lazy { NewsAdapter() }
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var analyzesAdapter: AnalyzesAdapter

    private var appBarIsExpanded = true

    override fun initView() {
        binding.srlAnalyzes.isEnabled = appBarIsExpanded

        val listener = AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            appBarIsExpanded = verticalOffset == 0
            binding.srlAnalyzes.isEnabled = appBarIsExpanded
        }
        binding.appBarLayout.addOnOffsetChangedListener(listener)

        categoryAdapter = CategoryAdapter(
            object : CategoryEventListener {
                override fun onCategoryClick(category: Category) {
                    val position = viewModel.categoriesList.indexOf(category)
                    analyzesAdapter.submitList(viewModel.analyzesList[position])

                    val holder = binding.rvCategories.getChildViewHolder(
                        binding.rvCategories.getChildAt(
                            categoryAdapter.currentList.indexOf(
                                lastClickedCategory
                            )
                        )
                    ) as CategoryViewHolder
                    holder.deselectItem()
                    lastClickedCategory = category
                }
            }
        )

        analyzesAdapter = AnalyzesAdapter(
            object : CatalogEventListener {
                override fun onAddClicked(analysis: CatalogItemDomain) {}

                override fun onItemClicked(analysis: CatalogItemDomain) {}
            }
        )

        viewModel.getNews()
        viewModel.getCatalog()
        binding.apply {
            rvNews.adapter = newsAdapter
            rvCategories.adapter = categoryAdapter
            rvAnalyzes.adapter = analyzesAdapter

            srlAnalyzes.setOnRefreshListener {
                viewModel.getNews()
                viewModel.getCatalog()

                rvNews.adapter = newsAdapter
                rvCategories.adapter = categoryAdapter
                rvAnalyzes.adapter = analyzesAdapter

                srlAnalyzes.isRefreshing = false
            }
        }
    }

    override fun observeViewModel() {
        viewModel.apply {
            news.observe { state ->
                when (state) {
                    is DomainResult.Success -> {
                        newsAdapter.submitList(state.data)
                    }
                    is DomainResult.Error -> {
                        val message = if (state.type == ErrorType.NO_INTERNET)
                            getString(R.string.network_error)
                        else
                            state.errors
                        val errorDialog =
                            ErrorDialog.getInstance(ErrorDialogParams(message))
                        errorDialog.show(childFragmentManager, ErrorDialog.TAG)
                    }
                    is DomainResult.Loading -> {}
                }
            }
            catalog.observe { state ->
                when (state) {
                    is DomainResult.Success -> {
                        sortCategories()
                        categoryAdapter.submitList(categoriesList) {
                            lastClickedCategory = categoriesList[0]
                        }
                        analyzesAdapter.submitList(analyzesList[0])
                    }
                    is DomainResult.Error -> {
                        val message = if (state.type == ErrorType.NO_INTERNET)
                            getString(R.string.network_error)
                        else
                            state.errors
                        val errorDialog =
                            ErrorDialog.getInstance(ErrorDialogParams(message))
                        errorDialog.show(childFragmentManager, ErrorDialog.TAG)
                    }
                    is DomainResult.Loading -> {}
                }
            }
        }
    }
}