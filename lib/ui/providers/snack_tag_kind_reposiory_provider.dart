import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/database.dart';
import 'package:snacker/repositories/snack_tag_kind_repository.dart';

final snackTagKindRepositoryProvider = Provider<SnackTagKindRepository>(
    (_) => SnackTagKindRepositoryImpl(database));
