import 'package:snacker/domains/fetch_snack_usecase.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/entities/snack_tag.dart';
import 'package:snacker/entities/snack_tag_kind.dart';
import 'package:snacker/repositories/snack_repository.dart';
import 'package:snacker/repositories/snack_tag_kind_repository.dart';
import 'package:snacker/repositories/snack_tag_repository.dart';

mixin AddSnackUseCase {
  Future<int> execute(
      {required String url,
      required String title,
      String? thumbnailUrl,
      required int priority,
      required List<String> tagNameList});
}

class AddSnackUseCaseImpl with AddSnackUseCase {
  final SnackRepository snackRepository;
  final SnackTagRepository snackTagRepository;
  final SnackTagKindRepository snackTagKindRepository;
  final FetchSnackUsecase fetchSnackUsecase;

  AddSnackUseCaseImpl(
      {required this.snackRepository,
      required this.fetchSnackUsecase,
      required this.snackTagRepository,
      required this.snackTagKindRepository});

  @override
  Future<int> execute(
      {required String url,
      required String title,
      String? thumbnailUrl,
      required int priority,
      required List<String> tagNameList}) async {
    final Snack snack =
        Snack(title: title, url: url, priority: priority, isArchived: false);
    final id = await snackRepository.createSnack(snack: snack);

    for (String tagName in tagNameList) {
      final SnackTagKind snackTagKind =
          SnackTagKind(name: tagName, isActive: true);
      final tagKindId = await snackTagKindRepository.createSnackTagKind(
          snackTagKind: snackTagKind);

      final snackTag = SnackTag(snackId: id, tagId: tagKindId);

      await snackTagRepository.createSnackTag(snackTag: snackTag);
    }

    await fetchSnackUsecase.executeList();
    await fetchSnackUsecase.executeUnreadSnackList();
    await fetchSnackUsecase.executeArchivedSnackList();
    return id;
  }
}
