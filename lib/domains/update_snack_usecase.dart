import 'package:snacker/domains/fetch_snack_usecase.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/repositories/snack_repository.dart';

mixin UpdateSnackUseCase {
  Future execute({required Snack newSnack});
}

class UpdateSnackUseCaseImpl with UpdateSnackUseCase {
  UpdateSnackUseCaseImpl(this._snackRepository, this._fetchSnackUsecase);

  final SnackRepository _snackRepository;
  final FetchSnackUsecase _fetchSnackUsecase;

  @override
  Future execute({required Snack newSnack}) async {
    await _snackRepository.updateSnack(newSnack: newSnack);
    await _fetchSnackUsecase.executeList();
    await _fetchSnackUsecase.executeUnreadSnackList();
    await _fetchSnackUsecase.executeArchivedSnackList();
  }
}