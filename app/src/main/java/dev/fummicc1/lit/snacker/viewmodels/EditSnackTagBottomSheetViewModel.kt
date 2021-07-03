package dev.fummicc1.lit.snacker.viewmodels

import android.app.Application
import androidx.lifecycle.*
import dev.fummicc1.lit.snacker.MyApplication
import dev.fummicc1.lit.snacker.interactor.impl.SnackInteractor
import dev.fummicc1.lit.snacker.interactor.impl.SnackTagKindInteractor
import dev.fummicc1.lit.snacker.interactor.interfaces.SnackTagKindUseCase
import dev.fummicc1.lit.snacker.interactor.interfaces.SnackUseCase
import dev.fummicc1.lit.snacker.models.SnackPresentable
import dev.fummicc1.lit.snacker.models.SnackTagKindPresentable
import dev.fummicc1.lit.snacker.models.SnackTagPresentable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditSnackTagBottomSheetViewModel(
    private val snack: SnackPresentable,
    application: Application
) :
    AndroidViewModel(application) {

    private val _selectingTags: MutableLiveData<List<SnackTagKindPresentable>> =
        MutableLiveData(listOf())
    private val _allTagKinds = MutableLiveData<List<SnackTagKindPresentable>>()
    private val _closeBottomSheet = MutableLiveData<Unit>()

    private val snackUseCase: SnackUseCase = SnackInteractor(application as MyApplication)
    private val snackTagKindUseCase: SnackTagKindUseCase = SnackTagKindInteractor(application as MyApplication)

    val selectingTags = _selectingTags
    val closeBottomSheet = _closeBottomSheet
    val allTagKinds: LiveData<List<SnackTagKindPresentable>> = _allTagKinds

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _allTagKinds.postValue(snackTagKindUseCase.getAll())
        }

        viewModelScope.launch(Dispatchers.IO) {
            snack.tags
                .map { it.name }
                .map { name ->
                    snackTagKindUseCase.searchWithName(name)
                }
                .filterNotNull()
                .let {
                    val current = _selectingTags.value ?: listOf()
                    _selectingTags.postValue(current + it)
                }
        }
    }

    fun toggleTag(tag: SnackTagKindPresentable) {
        val tags = selectingTags.value?.toMutableList()
        if (tags == null) return
        val willChecked = tags.map { it.name }.contains(tag.name).not()

        if (willChecked) {
            tags.add(tag)
        } else {
            tags.remove(tag)
        }
        _selectingTags.postValue(tags)
    }

    fun decideTags() {
        val selecting = _selectingTags.value
        if (selecting == null) return
        viewModelScope.launch(Dispatchers.IO) {
            snackUseCase.updateSnackTagPresentables(snack, selecting)
            _closeBottomSheet.postValue(Unit)
        }
    }

    class Factory(private val snack: SnackPresentable, private val application: Application) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EditSnackTagBottomSheetViewModel(snack, application) as T
        }
    }
}