
import 'dart:async';

import 'package:snacker/database.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/repositories/snack_repository.dart';

mixin FetchSnackUsecase {
  Future<Snack> executeSingle({required int id});
  Future<List<Snack>> executeList();
  Future<List<Snack>> executeUnreadSnackList();
}

class FetchSnackUsecaseImpl with FetchSnackUsecase {

  final SnackRepository _snackRepository;
  late Timer _timer;
  int _requestCountPerMinute = 0;

  FetchSnackUsecaseImpl(this._snackRepository) {
    _timer = Timer.periodic(Duration(minutes: 1), (timer) {
      _requestCountPerMinute = 0;
    });
  }

  @override
  Future<List<Snack>> executeList() async {

    final useCache = _requestCountPerMinute > 5 ? true : false;

    final snackList = await _snackRepository.getAllSnack(useCache: useCache);

    _requestCountPerMinute++;

    return snackList;
  }

  @override
  Future<Snack> executeSingle({required int id}) {
    return _snackRepository.getSnack(id: id);
  }

  @override
  Future<List<Snack>> executeUnreadSnackList() async {

    final isArchivedQuery = EqualQueryModel(field: "is_archived", value: "0");

    final List<Snack> snackList = await _snackRepository.getSnackWithQuery(queries: [isArchivedQuery]);
    return snackList;
  }
}