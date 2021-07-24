import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/domains/add_snack_usecase.dart';
import 'package:snacker/ui/providers/snack_repository_provider.dart';

final addSnackUseCaseProvider = Provider<AddSnackUseCase>((ref) {
  final snackRepository = ref.read(snackRepositoryProvider);
  return AddSnackUseCaseImpl(snackRepository);
});