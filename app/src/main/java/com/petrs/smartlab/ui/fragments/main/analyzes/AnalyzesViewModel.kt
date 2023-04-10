package com.petrs.smartlab.ui.fragments.main.analyzes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.domain.LoadingState
import com.petrs.smartlab.domain.models.CatalogItemDomain
import com.petrs.smartlab.domain.models.NewsItemDomain
import com.petrs.smartlab.domain.useCases.GetCatalogUseCase
import com.petrs.smartlab.domain.useCases.GetNewsUseCase
import com.petrs.smartlab.ui.fragments.main.analyzes.models.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnalyzesViewModel(
    private val getCatalogUseCase: GetCatalogUseCase,
    private val getNewsUseCase: GetNewsUseCase
) : ViewModel() {

    private val _catalog: MutableStateFlow<DomainResult<List<CatalogItemDomain>>> =
        MutableStateFlow(DomainResult.Loading(LoadingState.INITIAL))
    val catalog = _catalog.asStateFlow()

    private val _news: MutableStateFlow<DomainResult<List<NewsItemDomain>>> =
        MutableStateFlow(DomainResult.Loading(LoadingState.INITIAL))
    val news = _news.asStateFlow()

    val categoriesList: ArrayList<Category> = arrayListOf()
    val analyzesList: ArrayList<List<CatalogItemDomain>> = arrayListOf()

    fun getNews() = viewModelScope.launch {
        _news.value = DomainResult.Loading(LoadingState.REQUEST_LOADING)
        _news.value = getNewsUseCase()
    }

    fun getCatalog() = viewModelScope.launch {
        _catalog.value = DomainResult.Loading(LoadingState.REQUEST_LOADING)
        _catalog.value = getCatalogUseCase()
    }

    fun sortCategories() {
        val state = catalog.value
        categoriesList.clear()
        analyzesList.clear()
        if (state is DomainResult.Success) {
            var latestList: List<CatalogItemDomain> = state.data
            while (latestList.isNotEmpty()) {
                val category = latestList[0].category
                categoriesList.add(Category(category))
                val sortedList = latestList.filter { it.category == category }
                analyzesList.add(sortedList)
                latestList = latestList.filter { it.category != category }
            }
        }
    }
}