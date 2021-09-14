import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/database.dart';
import 'package:snacker/repositories/snack_tag_repository.dart';

final snackTagRepositoryProvider =
    Provider<SnackTagRepository>((_) => SnackTagRepositoryImpl(database));
