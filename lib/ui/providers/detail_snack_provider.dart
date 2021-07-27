import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/entities/snack.dart';

final StateProvider<String> detailPageWebsiteProvider =
    StateProvider((_) => "https://google.com");

final StateProvider<Snack?> detailSnackProvider = StateProvider((_) => null);
