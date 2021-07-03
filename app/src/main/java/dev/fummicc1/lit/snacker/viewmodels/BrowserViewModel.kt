package dev.fummicc1.lit.snacker.viewmodels

import android.app.Application
import androidx.lifecycle.*
import dev.fummicc1.lit.snacker.Constants
import dev.fummicc1.lit.snacker.MyApplication
import dev.fummicc1.lit.snacker.interactor.impl.SnackInteractor
import dev.fummicc1.lit.snacker.interactor.impl.SnackTagKindInteractor
import dev.fummicc1.lit.snacker.interactor.interfaces.SnackTagKindUseCase
import dev.fummicc1.lit.snacker.interactor.interfaces.SnackUseCase
import dev.fummicc1.lit.snacker.models.AddSnackPreset
import dev.fummicc1.lit.snacker.models.AssociatedSnack
import dev.fummicc1.lit.snacker.models.RecentlyViewedSnackPresentable
import dev.fummicc1.lit.snacker.models.SnackPresentable
import dev.fummicc1.lit.snacker.services.HTMLClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

open class BrowserViewModel(application: Application) : AndroidViewModel(application) {
    protected val _currentBrowsingSnack: MutableLiveData<SnackPresentable> = MutableLiveData()
    protected val _addSnackPreset: MutableLiveData<AddSnackPreset> = MutableLiveData()

    private val snackUseCase: SnackUseCase = SnackInteractor(application as MyApplication)

    val associatedSnacks: LiveData<List<AssociatedSnack>> = Transformations.map(_currentBrowsingSnack) {
        it.associatedSnacks
    }
    val currentWebUrl: LiveData<String> = Transformations.map(_currentBrowsingSnack) {
        it.url
    }
    val addSnackPreset: AddSnackPreset?
        get() = _addSnackPreset.value

    init {
        initialConfiguration()
    }

    fun updateWebPage(url: String, title: String?) {
        if (url == Constants.initialWebUrl) return
        viewModelScope.launch(Dispatchers.IO) {

            val fixedTitle = title ?: HTMLClient().getTitle(url)

            val associatedSnacks = snackUseCase.searchAssociatedSnackFromTitle(fixedTitle, url)
            val snack = SnackPresentable(
                0,
                url,
                null,
                fixedTitle,
                3,
                associatedSnacks,
                listOf(),
                null
            )
            _currentBrowsingSnack.postValue(snack)

            val addSnackPreset = AddSnackPreset(
                url,
                fixedTitle,
                3
            )
            _addSnackPreset.postValue(addSnackPreset)

            val history = snackUseCase.fetchSameUrlSnackHistory(url)?.apply {
                viewedAt = Date()
            }
            if (history == null) {
                val newHistory = RecentlyViewedSnackPresentable(
                    0,
                    url,
                    null,
                    fixedTitle ,
                    Date()
                )
                snackUseCase.createRecentlyViewedSnack(newHistory)
            } else {
                snackUseCase.updateRecentlyViewedSnack(history)
            }
        }
    }

   suspend fun convertAssociatedToSnack(associatedSnack: AssociatedSnack): SnackPresentable {
        return snackUseCase.convertAssociatedToSnack(associatedSnack)
    }

    open fun initialConfiguration() {
        viewModelScope.launch(Dispatchers.IO) {
            snackUseCase.getRecentlySancks(1).lastOrNull()?.let {
                _currentBrowsingSnack.postValue(snackUseCase.convertHistoryToSnack(it))
            }
        }
    }
}