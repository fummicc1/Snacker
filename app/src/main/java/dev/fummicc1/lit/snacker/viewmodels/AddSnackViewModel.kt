package dev.fummicc1.lit.snacker.viewmodels

import android.app.Application
import android.webkit.URLUtil
import androidx.lifecycle.*
import dev.fummicc1.lit.snacker.MyApplication
import dev.fummicc1.lit.snacker.entities.Snack
import dev.fummicc1.lit.snacker.interactor.impl.SnackInteractor
import dev.fummicc1.lit.snacker.interactor.impl.SnackTagKindInteractor
import dev.fummicc1.lit.snacker.interactor.impl.StockHistoryInteractor
import dev.fummicc1.lit.snacker.interactor.interfaces.SnackTagKindUseCase
import dev.fummicc1.lit.snacker.interactor.interfaces.SnackUseCase
import dev.fummicc1.lit.snacker.interactor.interfaces.StockHistoryUseCase
import dev.fummicc1.lit.snacker.models.AddSnackPreset
import dev.fummicc1.lit.snacker.models.SnackPresentable
import dev.fummicc1.lit.snacker.models.SnackTagKindPresentable
import dev.fummicc1.lit.snacker.models.StockHistoryPresentable
import dev.fummicc1.lit.snacker.services.HTMLClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.*

class AddSnackViewModel(application: Application) : AndroidViewModel(application) {

    class InputError(val message: String, val type: InputType) {

        enum class InputType {
            URL,
            TITLE,
            INTERESTLEVEL
        }
    }

    private val snackUseCase: SnackUseCase = SnackInteractor(application as MyApplication)
    private val snackTagKindUseCase: SnackTagKindUseCase =
        SnackTagKindInteractor(application as MyApplication)
    private val stockHistoryUseCase: StockHistoryUseCase =
        StockHistoryInteractor(application as MyApplication)
    private val _selectingTags: MutableLiveData<List<SnackTagKindPresentable>> =
        MutableLiveData(listOf())
    private val _title: MutableLiveData<String> = MutableLiveData()
    private val _url: MutableLiveData<String> = MutableLiveData()
    private val _priorityLevel: MutableLiveData<Int> = MutableLiveData()
    private val _inputError: MutableLiveData<InputError> = MutableLiveData()
    private val _completeAddition: MutableLiveData<Unit> = MutableLiveData()
    private val htmlClient = HTMLClient()

    val title: LiveData<String>
        get() = _title
    val url: LiveData<String>
        get() = _url
    val priorityLevel: LiveData<Int>
        get() = _priorityLevel
    val inputError: LiveData<InputError>
        get() = _inputError
    val completeAddition: LiveData<Unit>
        get() = _completeAddition
    val availableTags: LiveData<List<SnackTagKindPresentable>> =
        Transformations.distinctUntilChanged(
            snackTagKindUseCase.observeFilterWithActivation(true, null)
                .asLiveData(viewModelScope.coroutineContext)
        )

    fun createSnack() {
        val title = _title.value ?: ""
        val url = _url.value ?: ""
        val interestLevel = _priorityLevel.value

        if (url.isEmpty()) {
            val error = InputError("URLが入力されていません", InputError.InputType.URL)
            _inputError.postValue(error)
            return
        }

        val isValidUrl = URLUtil.isValidUrl(url)
        if (!isValidUrl) {
            val error = InputError("無効なURLです", InputError.InputType.URL)
            _inputError.postValue(error)
            return
        }

        if (title.isEmpty()) {
            val error = InputError("タイトルが入力されていません", InputError.InputType.TITLE)
            _inputError.postValue(error)
            return
        }

        if (interestLevel == null) {
            val error = InputError("レベルが入力されていません", InputError.InputType.INTERESTLEVEL)
            _inputError.postValue(error)
            return
        }

        val snackTagPresentableIdList = _selectingTags.value.let {
            val list = it ?: listOf()
            list.map {
                it.id
            }
        }

        viewModelScope.launch(Dispatchers.IO) {

            val thumbnailUrl = htmlClient.getOGPImageUrl(url)

            val snack = SnackPresentable(
                0,
                url,
                thumbnailUrl,
                title,
                interestLevel,
                listOf(),
                listOf(),
                Snack.BookmarkStatus.SAVED
            )

            var current = Date()
            val calendar = Calendar.getInstance()
            calendar.time = current
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            current = calendar.time

            var isUpdate: Boolean = false
            var stockHistory: StockHistoryPresentable = StockHistoryPresentable(0, current, 0)
            val previousStockHistory = stockHistoryUseCase.searchWithCurrentTime()

            if (previousStockHistory != null) {
                stockHistory = previousStockHistory
                isUpdate = true
            }

            stockHistory.count += 1

            snackUseCase.createSnackAndTags(snack, snackTagPresentableIdList)
            if (isUpdate) {
                stockHistoryUseCase.update(stockHistory)
            } else {
                stockHistoryUseCase.create(stockHistory)
            }

            resetInput()
            withContext(Dispatchers.Main) {
                _completeAddition.postValue(Unit)
            }
        }
    }

    private fun resetInput() {
        _url.postValue(null)
        _title.postValue(null)
        _priorityLevel.postValue(null)
        _selectingTags.postValue(listOf())
    }

    fun updateTitle(title: String) {
        _title.postValue(title)
    }

    fun updateURL(url: String) {
        _url.postValue(url)
    }

    fun updateLevel(level: Int) {
        _priorityLevel.postValue(level)
    }

    fun deselectTag(snackTagKindPresentable: SnackTagKindPresentable) {
        _selectingTags.value?.let { selectings ->
            return@let selectings.filter {
                it.id != snackTagKindPresentable.id
            }
        }?.let {
            _selectingTags.postValue(it)
        }
    }

    fun selectSnackTagPresentableId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val snackTagPresentable = snackTagKindUseCase.get(id).let {
                SnackTagKindPresentable(
                    it.id,
                    it.name,
                    it.isActive
                )
            }
            withContext(Dispatchers.Main) {
                _selectingTags.value?.let { selectings ->
                    return@let if (selectings.contains(snackTagPresentable)) selectings else selectings + listOf(
                        snackTagPresentable
                    )
                }?.let {
                    _selectingTags.postValue(it)
                }
            }
        }
    }

    fun addSnackTag(name: String) {
        // 既に同じ名前のタグが存在しているかチェック
        if (availableTags.value?.map { it.name }?.contains(name) ?: false) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            snackTagKindUseCase.create(name)
        }
    }

    fun getAddSnackPresetFromUrl(url: String): AddSnackPreset {
        val title = runBlocking(Dispatchers.IO) {
            val client = HTMLClient()
            val title = client.getTitle(url)
            return@runBlocking title
        }
        return AddSnackPreset(
            url,
            title,
            3
        )
    }
}