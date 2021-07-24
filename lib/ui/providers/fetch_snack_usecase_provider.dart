import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/domains/fetch_snack_usecase.dart';
import 'package:snacker/repositories/snack_repository.dart';
import 'package:snacker/ui/providers/snack_repository_provider.dart';

final Provider<FetchSnackUsecase> fetchSnackUsecaseProvider = Provider((ref) {
  final SnackRepository snackRepository = ref.read(snackRepositoryProvider);
  return FetchSnackUsecaseImpl(snackRepository);
});