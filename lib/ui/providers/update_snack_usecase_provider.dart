import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/domains/update_snack_usecase.dart';
import 'package:snacker/ui/providers/fetch_snack_usecase_provider.dart';
import 'package:snacker/ui/providers/snack_repository_provider.dart';

final updateSnackUseCaseProvider = Provider<UpdateSnackUseCase>((ref) {
  final repository = ref.read(snackRepositoryProvider);
  final fetchSnackUseCase = ref.read(fetchSnackUsecaseProvider);
  return UpdateSnackUseCaseImpl(repository, fetchSnackUseCase);
});