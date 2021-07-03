package dev.fummicc1.lit.snacker.viewmodels

import android.app.Application
import androidx.lifecycle.*
import dev.fummicc1.lit.snacker.MyApplication
import dev.fummicc1.lit.snacker.entities.Snack
import dev.fummicc1.lit.snacker.interactor.impl.SnackInteractor
import dev.fummicc1.lit.snacker.interactor.impl.SnackTagKindInteractor
import dev.fummicc1.lit.snacker.interactor.interfaces.SnackTagKindUseCase
import dev.fummicc1.lit.snacker.interactor.interfaces.SnackUseCase
import dev.fummicc1.lit.snacker.models.SnackPresentable
import dev.fummicc1.lit.snacker.models.SnackTagKindPresentable
import dev.fummicc1.lit.snacker.models.SnackTagPresentable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SavedSnackListViewModel(application: Application) : AndroidViewModel(application) {

    private val snackUseCase: SnackUseCase = SnackInteractor(application as MyApplication)
    private val snackTagKindUseCase: SnackTagKindUseCase = SnackTagKindInteractor(application as MyApplication)

    private val _snackList: MutableLiveData<List<SnackPresentable>> = MutableLiveData()
    private val _selectSnack: MutableLiveData<SnackPresentable> = MutableLiveData()
    private val _avaliableTags: MutableLiveData<List<SnackTagKindPresentable>> = MutableLiveData()
    private val _selectingTags: MutableLiveData<List<SnackTagKindPresentable>> =
        MutableLiveData(listOf())

    val snackList: LiveData<List<SnackPresentable>> =
        Transformations.distinctUntilChanged(_snackList)
    val availableTags: LiveData<List<SnackTagKindPresentable>> = _avaliableTags

    val selectSnack: LiveData<SnackPresentable> = _selectSnack

    val shouldShowEmptyState = _snackList.map {
        it.isEmpty()
    }

    init {
        // 別にCoroutineScopeを作成する必要がある
        viewModelScope.launch(Dispatchers.IO) {
            snackTagKindUseCase.observeFilterWithActivation(true, null).collect {
                _avaliableTags.postValue(it)
            }
        }

        fetchSnackList()
    }

    private fun fetchSnackList() {
        viewModelScope.launch(Dispatchers.IO) {
            val prefs = getApplication<MyApplication>().prefs
            val limit = prefs.getInt("snack_display_limit_count", 5)
            snackUseCase.observeFilteredWithStatus(Snack.BookmarkStatus.SAVED, limit).collect { snackList ->
                snackList.filter { snack ->
                    val selectingTags = _selectingTags.value ?: listOf()
                    if (selectingTags.isEmpty()) {
                        return@filter true
                    }
                    snack.tags.forEach {
                        if (selectingTags.map { it.name }.contains(it.name)) {
                            return@filter true
                        }
                    }
                    return@filter false
                }.apply {
                    _snackList.postValue(this@apply)
                }
            }
        }
    }

    private fun filterSnackListWithTagNames(tagNames: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val value = snackUseCase.filterhWithStatus(Snack.BookmarkStatus.SAVED)
            value.filter { snack ->
                if (tagNames.isEmpty()) {
                    return@filter true
                }
                snack.tags.forEach {
                    if (tagNames.contains(it.name)) {
                        return@filter true
                    }
                }
                return@filter false
            }.apply {
                _snackList.postValue(this)
            }
        }
    }

    fun selectSnack(snackPresentable: SnackPresentable) {
        _selectSnack.postValue(snackPresentable)
    }

    fun deleteSnack(snackPresentable: SnackPresentable) {
        viewModelScope.launch(Dispatchers.IO) {
            snackUseCase.deleteSnack(snackPresentable.id)
        }
    }

    fun archiveSnack(snackPresentable: SnackPresentable) {
        viewModelScope.launch(Dispatchers.IO) {
            snackPresentable.status = Snack.BookmarkStatus.ARCHIVED
            snackUseCase.updateSnack(snackPresentable.id, snackPresentable)
        }
    }

    fun refreshTags() {
        viewModelScope.launch(Dispatchers.IO) {
            snackTagKindUseCase.filterWithActivation(true).apply {
                _avaliableTags.postValue(this)
            }
        }
    }

    fun deselectTag(snackTagKindPresentable: SnackTagKindPresentable) {
        _selectingTags.value?.let { selectings ->
            return@let selectings.filter {
                it.id != snackTagKindPresentable.id
            }
        }?.let {
            _selectingTags.postValue(it)
            filterSnackListWithTagNames(it.map { it.name })
        }
    }

    fun selectSnackTagPresentableId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val snackTagPresentable = snackTagKindUseCase.get(id)
            _selectingTags.value?.let { selectings ->
                return@let if (selectings.contains(snackTagPresentable)) selectings else selectings + listOf(
                    snackTagPresentable
                )
            }?.let {
                _selectingTags.postValue(it)
                filterSnackListWithTagNames(it.map { it.name })
            }
        }
    }
}