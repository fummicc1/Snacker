import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/domains/fetch_snack_usecase.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/repositories/snack_repository.dart';

mixin AddSnackUseCase {
  Future<int> execute(
      {required String url,
      required String title,
      String? thumbnailUrl,
      required int priority});
}

class AddSnackUseCaseImpl with AddSnackUseCase {
  final SnackRepository _snackRepository;
  final FetchSnackUsecase _fetchSnackUsecase;

  AddSnackUseCaseImpl(this._snackRepository, this._fetchSnackUsecase);

  @override
  Future<int> execute(
      {required String url,
      required String title,
      String? thumbnailUrl,
      required int priority}) async {
    final Snack snack =
        Snack(title: title, url: url, priority: priority, isArchived: false);
    final id = await _snackRepository.createSnack(snack: snack);
    await _fetchSnackUsecase.executeList();
    await _fetchSnackUsecase.executeUnreadSnackList();
    await _fetchSnackUsecase.executeArchivedSnackList();
    return id;
  }
}
