import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/ui/providers/fetch_snack_usecase_provider.dart';

final archivedSnackListProvider = StreamProvider<List<Snack>>((ref) {
  final usecase = ref.read(fetchSnackUsecaseProvider);
  usecase.executeArchivedSnackList();
  return usecase.archivedSnackList;
});