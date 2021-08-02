import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/models/snack_model.dart';

final StateProvider<String> detailPageWebsiteProvider =
    StateProvider((_) => "https://google.com");

final StateProvider<SnackModel?> detailSnackProvider = StateProvider((_) => null);
