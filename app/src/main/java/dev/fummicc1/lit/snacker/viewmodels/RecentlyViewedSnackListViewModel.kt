package dev.fummicc1.lit.snacker.viewmodels

import android.app.Application
import androidx.lifecycle.*
import dev.fummicc1.lit.snacker.MyApplication
import dev.fummicc1.lit.snacker.interactor.impl.SnackInteractor
import dev.fummicc1.lit.snacker.interactor.interfaces.SnackUseCase
import dev.fummicc1.lit.snacker.models.RecentlyViewedSnackPresentable
import dev.fummicc1.lit.snacker.models.SnackPresentable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecentlyViewedSnackListViewModel(application: Application) : AndroidViewModel(application) {

    private val snackUseCase: SnackUseCase = SnackInteractor(application as MyApplication)

    val shouldShowEmptyState: LiveData<Boolean>

    val recentlyViewedSnackList: LiveData<List<RecentlyViewedSnackPresentable>>

    init {
        val stream = snackUseCase.observeRecentlySancks(10).asLiveData(viewModelScope.coroutineContext)
        recentlyViewedSnackList = stream
        shouldShowEmptyState = stream.map {
            it.isEmpty()
        }
    }

    suspend fun convertToSnackPresentable(recentlyViewedSnackPresentable: RecentlyViewedSnackPresentable): SnackPresentable {
        return snackUseCase.convertHistoryToSnack(recentlyViewedSnackPresentable)
    }
}