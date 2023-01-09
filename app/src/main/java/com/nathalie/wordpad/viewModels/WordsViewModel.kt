package com.nathalie.wordpad.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nathalie.wordpad.model.SortKey
import com.nathalie.wordpad.model.SortOrder
import com.nathalie.wordpad.model.Word
import com.nathalie.wordpad.repository.WordRepository
import com.nathalie.wordpad.utils.StorageService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class WordsViewModel(val repo: WordRepository, val storageService: StorageService) : ViewModel() {
    val words: MutableLiveData<List<Word>> = MutableLiveData()
    val sortBy: MutableLiveData<String> = MutableLiveData()
    val sortOrder: MutableLiveData<String> = MutableLiveData()
    val swipeRefreshLayoutFinished: MutableSharedFlow<Unit> = MutableSharedFlow()

    init {

        getWords("")
        sortBy.value = storageService.getString(SortKey.SORT_BY.name)
        sortOrder.value = storageService.getString(SortKey.SORT_ORDER.name)
        Log.d("debugging", "${sortOrder.value } ${sortBy.value}")
    }

    fun onChangeSortBy(value: String) {
        sortBy.value = value
        storageService.setString(SortKey.SORT_BY.name, value)
    }

    fun onChangeSortOrder(value: String) {
        sortOrder.value = value
        storageService.setString(SortKey.SORT_ORDER.name, value)
    }

    fun onRefresh() {
        viewModelScope.launch {
            delay(3000)
            getWords("")
            swipeRefreshLayoutFinished.emit(Unit)
        }
    }

    fun getWords(str: String) {
        val res = repo.getWords(str, false)
        words.value = res.filter { !it.status }
    }

    fun sortWords(order: String, type: String, str: String) {
        var res = repo.getWords(str, false)
        if (order == SortOrder.ASC.name && type == "TITLE") {
            res = res.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) {
                it.title
            })
        } else if (order == "dsc" && type == "title") {
            res = res.sortedWith(compareByDescending(String.CASE_INSENSITIVE_ORDER) {
                it.title
            })
        } else if (order == "dsc" && type == "date") {
            res = res.reversed()
        }

        words.value = res.filter { !it.status }
    }

    class Provider(val repo: WordRepository, val storageService: StorageService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WordsViewModel(repo, storageService) as T
        }
    }
}