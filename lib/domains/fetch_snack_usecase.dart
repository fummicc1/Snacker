
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
  int _requestCountPerminute = 0;

  FetchSnackUsecaseImpl(this._snackRepository) {
    Timer.periodic(Duration(minutes: 1), (timer) {
      _requestCountPerminute = 0;
    });
  }



  @override
  Future<List<Snack>> executeList() async {

    final useCache = _requestCountPerminute > 5 ? true : false;

    final snackList = await _snackRepository.getAllSnack(useCache: useCache);

    _requestCountPerminute++;

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

final FetchSnackUsecase fetchSnackUsecase = FetchSnackUsecaseImpl(snackRepository);