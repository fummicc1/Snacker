import 'package:snacker/domains/fetch_snack_usecase.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/models/snack_model.dart';
import 'package:snacker/repositories/snack_repository.dart';
import 'package:snacker/repositories/snack_tag_kind_repository.dart';
import 'package:snacker/repositories/snack_tag_repository.dart';

mixin UpdateSnackUseCase {
  Future execute({required SnackModel newSnack});
}

class UpdateSnackUseCaseImpl with UpdateSnackUseCase {
  UpdateSnackUseCaseImpl(
      {required this.snackRepository,
      required this.snackTagRepository,
      required this.snackTagKindRepository,
      required this.fetchSnackUsecase});

  final SnackRepository snackRepository;
  final FetchSnackUsecase fetchSnackUsecase;
  final SnackTagRepository snackTagRepository;
  final SnackTagKindRepository snackTagKindRepository;

  @override
  Future execute({required SnackModel newSnack}) async {
    final snackEntity = Snack.fromMap(map: newSnack.json);

    await snackRepository.updateSnack(newSnack: snackEntity);

    final previousAllSnackTag = await snackTagRepository.getSnackTagListOfSnack(
        snackId: snackEntity.id);

    for (final element in previousAllSnackTag) {
      final id = element.item1.id;
      if (newSnack.tagList.map((e) => e.id).contains(id)) {
        await snackTagRepository.updateSnackTag(newSnackTag: element.item1);
      } else {
        await snackTagRepository.deleteSnackTag(id: id!);
      }
    }

    await fetchSnackUsecase
        .executeList()
        .catchError((_) => [].cast<SnackModel>());
    await fetchSnackUsecase
        .executeUnreadSnackList()
        .catchError((_) => [].cast<SnackModel>());
    await fetchSnackUsecase
        .executeArchivedSnackList()
        .catchError((_) => [].cast<SnackModel>());
  }
}
