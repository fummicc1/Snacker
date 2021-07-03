package dev.fummicc1.lit.snacker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.fummicc1.lit.snacker.MyApplication
import dev.fummicc1.lit.snacker.interactor.impl.SnackInteractor
import dev.fummicc1.lit.snacker.interactor.interfaces.SnackUseCase
import dev.fummicc1.lit.snacker.models.SnackPresentable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchSnackViewModel(application: Application): AndroidViewModel(application) {

    private val snackUseCase: SnackUseCase = SnackInteractor(application as MyApplication)

    private val _query: MutableLiveData<String> = MutableLiveData()

    private val _results: MutableLiveData<List<SnackPresentable>> = MutableLiveData()

    val result: LiveData<List<SnackPresentable>> = _results

    init {
        viewModelScope.launch(Dispatchers.IO) {
            snackUseCase.fetchAllSnack().let {
                _results.postValue(it)
            }
        }
    }

    fun updateQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _query.postValue(query)

            if (query.isEmpty()) {
                snackUseCase.fetchAllSnack().let {
                    _results.postValue(it)
                }
            } else {
                snackUseCase.filterWithTitle(query).let {
                    _results.postValue(it)
                }
            }
        }
    }
}