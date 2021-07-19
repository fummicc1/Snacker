import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/domains/fetch_snack_usecase.dart';

final unReadSnackListProvider =
StateProvider((ref) => fetchSnackUsecase.executeUnreadSnackList());