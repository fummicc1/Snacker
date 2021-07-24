import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/domains/fetch_snack_usecase.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/ui/providers/fetch_snack_usecase_provider.dart';

final unReadSnackListProvider = StateProvider<Future<List<Snack>>>((ref) {
  final usecase = ref.read(fetchSnackUsecaseProvider);
  return usecase.executeUnreadSnackList();
});
