package dev.fummicc1.lit.snacker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dev.fummicc1.lit.snacker.MyApplication
import dev.fummicc1.lit.snacker.interactor.impl.SnackTagKindInteractor
import dev.fummicc1.lit.snacker.interactor.interfaces.SnackTagKindUseCase
import dev.fummicc1.lit.snacker.models.SnackTagKindPresentable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManageTagViewModel(application: Application) : AndroidViewModel(application) {

    private val snackTagKindUseCase: SnackTagKindUseCase =
        SnackTagKindInteractor(application as MyApplication)

    val allTags: LiveData<List<SnackTagKindPresentable>> =
        snackTagKindUseCase.observeAll()
            .asLiveData(viewModelScope.coroutineContext)

    fun updateTagActivation(tagKind: SnackTagKindPresentable, isActive: Boolean) {
        tagKind.isActive = isActive
        viewModelScope.launch(Dispatchers.IO) {
            snackTagKindUseCase.update(tagKind)
        }
    }
}