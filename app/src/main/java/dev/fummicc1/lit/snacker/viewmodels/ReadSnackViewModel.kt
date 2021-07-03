package dev.fummicc1.lit.snacker.viewmodels

import android.app.Application
import androidx.lifecycle.*
import dev.fummicc1.lit.snacker.MyApplication
import dev.fummicc1.lit.snacker.entities.Snack
import dev.fummicc1.lit.snacker.interactor.impl.SnackInteractor
import dev.fummicc1.lit.snacker.interactor.impl.StockHistoryInteractor
import dev.fummicc1.lit.snacker.interactor.interfaces.SnackUseCase
import dev.fummicc1.lit.snacker.interactor.interfaces.StockHistoryUseCase
import dev.fummicc1.lit.snacker.models.AddSnackPreset
import dev.fummicc1.lit.snacker.models.SnackPresentable
import dev.fummicc1.lit.snacker.models.StockHistoryPresentable
import dev.fummicc1.lit.snacker.services.HTMLClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

open class ReadSnackViewModel(application: Application, snack: SnackPresentable?) : BrowserViewModel(application) {

    protected val snackUseCase: SnackUseCase = SnackInteractor(application as MyApplication)
    private val stockHistoryUseCase: StockHistoryUseCase = StockHistoryInteractor(application as MyApplication)

    protected val _onReceiveMessage: MutableLiveData<String> = MutableLiveData()
    protected val _bookmarkStatus: MutableLiveData<Snack.BookmarkStatus?> = MutableLiveData(null)
    protected val htmlClient = HTMLClient()

    val onReceiveMessage: LiveData<String> = _onReceiveMessage

    val bookmarkStatus: LiveData<Snack.BookmarkStatus?> = _bookmarkStatus

    init {
        snack?.let {
            _currentBrowsingSnack.postValue(it)
            _bookmarkStatus.postValue(it.status)
        }
    }

    fun performArchiveAction() {
        viewModelScope.launch(Dispatchers.IO) {
            val addSnackPreset = this@ReadSnackViewModel.addSnackPreset
            if (addSnackPreset == null) return@launch
            val snack = snackUseCase.searchSnackWithUrl(addSnackPreset.url)

            if (snack == null) {
                archiveCurrentWebPage(snack, addSnackPreset)
            } else if (snack.status == Snack.BookmarkStatus.SAVED) {
                archiveCurrentWebPage(snack, addSnackPreset)
            } else if (snack.status == Snack.BookmarkStatus.ARCHIVED) {
                resignArchiveCurrentWebPage(snack)
            }
        }
    }

    private fun resignArchiveCurrentWebPage(snackPresentable: SnackPresentable) {
        viewModelScope.launch(Dispatchers.IO) {
            snackPresentable.status = Snack.BookmarkStatus.SAVED
            snackUseCase.updateSnack(snackPresentable.id, snackPresentable)
            _onReceiveMessage.postValue("記事の未読にしました")
            _bookmarkStatus.postValue(Snack.BookmarkStatus.SAVED)
        }
    }

    private fun archiveCurrentWebPage(snackPresentable: SnackPresentable?, addSnackPreset: AddSnackPreset) {
        viewModelScope.launch(Dispatchers.IO) {
            if (snackPresentable == null || snackPresentable.id == 0) {

                val thumbnailUrl = htmlClient.getOGPImageUrl(addSnackPreset.url)
                val title = addSnackPreset.title ?: "タイトルなし"

                val newSnack = SnackPresentable(
                    0,
                    addSnackPreset.url,
                    thumbnailUrl,
                    title,
                    addSnackPreset.priority,
                    listOf(),
                    listOf(),
                    Snack.BookmarkStatus.SAVED
                )
                snackUseCase.createSnackAndTags(newSnack, listOf())

                var currentHistory = stockHistoryUseCase.searchWithCurrentTime()?.apply {
                    count += 1
                }
                if (currentHistory == null) {
                    var current = Date()
                    val calendar = Calendar.getInstance()
                    calendar.time = current
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    current = calendar.time
                    currentHistory = StockHistoryPresentable(
                        0,
                        current,
                        1
                    )
                    stockHistoryUseCase.create(currentHistory)
                } else {
                    stockHistoryUseCase.update(currentHistory)
                }

                _onReceiveMessage.postValue("記事を保存し、未読ボックスに入れました")
                _bookmarkStatus.postValue(Snack.BookmarkStatus.SAVED)
            } else {
                snackPresentable.status = Snack.BookmarkStatus.ARCHIVED
                snackUseCase.updateSnack(snackPresentable.id, snackPresentable)
                _onReceiveMessage.postValue("記事を読了ボックスに入れました")
                _bookmarkStatus.postValue(Snack.BookmarkStatus.ARCHIVED)
            }
        }
    }

    override fun initialConfiguration() {
    }
}