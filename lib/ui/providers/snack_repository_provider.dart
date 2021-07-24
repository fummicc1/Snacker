import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/database.dart';
import 'package:snacker/repositories/snack_repository.dart';

final snackRepositoryProvider =
    Provider<SnackRepository>((ref) => SnackRepositoryImpl(database));
