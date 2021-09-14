import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/models/snack_model.dart';
import 'package:snacker/ui/providers/fetch_snack_usecase_provider.dart';

final unReadSnackListProvider = StreamProvider<List<SnackModel>>((ref) {
  final usecase = ref.read(fetchSnackUsecaseProvider);
  usecase.executeUnreadSnackList();
  return usecase.unreadSnackList;
});
