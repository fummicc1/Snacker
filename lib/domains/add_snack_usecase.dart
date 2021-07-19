import 'package:snacker/entities/snack.dart';
import 'package:snacker/repositories/snack_repository.dart';

mixin AddSnackUseCase {
  Future execute({required String url, required String title, String? thumbnailUrl, required int priority});
}

class AddSnackInteractor with AddSnackUseCase {

  final SnackRepository _snackRepository;

  AddSnackInteractor(this._snackRepository);

  @override
  Future execute({required String url, required String title, String? thumbnailUrl, required int priority}) {
    final Snack snack = Snack(title: title, url: url, priority: priority);
    return _snackRepository.createSnack(snack: snack);
  }
}