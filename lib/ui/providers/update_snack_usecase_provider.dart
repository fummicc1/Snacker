import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/domains/update_snack_usecase.dart';
import 'package:snacker/repositories/snack_tag_kind_repository.dart';
import 'package:snacker/repositories/snack_tag_repository.dart';
import 'package:snacker/ui/providers/fetch_snack_usecase_provider.dart';
import 'package:snacker/ui/providers/snack_repository_provider.dart';
import 'package:snacker/ui/providers/snack_tag_kind_reposiory_provider.dart';
import 'package:snacker/ui/providers/snack_tag_repository_provider.dart';

final updateSnackUseCaseProvider = Provider<UpdateSnackUseCase>((ref) {
  final repository = ref.read(snackRepositoryProvider);
  final SnackTagRepository snackTagRepository =
      ref.read(snackTagRepositoryProvider);
  final SnackTagKindRepository snackTagKindRepository =
      ref.read(snackTagKindRepositoryProvider);
  final fetchSnackUseCase = ref.read(fetchSnackUsecaseProvider);
  return UpdateSnackUseCaseImpl(
      snackRepository: repository,
      snackTagRepository: snackTagRepository,
      snackTagKindRepository: snackTagKindRepository,
      fetchSnackUsecase: fetchSnackUseCase);
});
