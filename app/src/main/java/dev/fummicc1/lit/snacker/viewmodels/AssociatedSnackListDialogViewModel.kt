package dev.fummicc1.lit.snacker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dev.fummicc1.lit.snacker.MyApplication
import dev.fummicc1.lit.snacker.interactor.impl.SnackInteractor
import dev.fummicc1.lit.snacker.interactor.interfaces.SnackUseCase
import dev.fummicc1.lit.snacker.models.AssociatedSnack
import dev.fummicc1.lit.snacker.models.SnackPresentable

class AssociatedSnackListDialogViewModel(application: Application): AndroidViewModel(application) {

    private val snackUseCase: SnackUseCase = SnackInteractor(application as MyApplication)

    suspend fun convertAssociatedToSnack(associatedSnack: AssociatedSnack): SnackPresentable {
        return snackUseCase.convertAssociatedToSnack(associatedSnack)
    }

}