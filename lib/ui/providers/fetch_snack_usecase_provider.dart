import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/domains/fetch_snack_usecase.dart';
import 'package:snacker/repositories/snack_repository.dart';
import 'package:snacker/repositories/snack_tag_kind_repository.dart';
import 'package:snacker/repositories/snack_tag_repository.dart';
import 'package:snacker/ui/providers/snack_repository_provider.dart';
import 'package:snacker/ui/providers/snack_tag_kind_reposiory_provider.dart';
import 'package:snacker/ui/providers/snack_tag_repository_provider.dart';

final Provider<FetchSnackUsecase> fetchSnackUsecaseProvider = Provider((ref) {
  final SnackRepository snackRepository = ref.read(snackRepositoryProvider);
  final SnackTagRepository snackTagRepository =
      ref.read(snackTagRepositoryProvider);
  final SnackTagKindRepository snackTagKindRepository =
      ref.read(snackTagKindRepositoryProvider);
  return FetchSnackUsecaseImpl(
      snackRepository: snackRepository,
      snackTagRepository: snackTagRepository,
      snackTagKindRepository: snackTagKindRepository);
});
