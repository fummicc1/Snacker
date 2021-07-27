import 'dart:async';

import 'package:snacker/database.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/repositories/snack_repository.dart';

mixin FetchSnackUsecase {
  Stream<List<Snack>> get snackList;

  Stream<List<Snack>> get unreadSnackList;

  Stream<List<Snack>> get archivedSnackList;

  Future<Snack> executeSingle({required int id});

  Future<List<Snack>> executeList();

  Future<List<Snack>> executeUnreadSnackList();

  Future<List<Snack>> executeArchivedSnackList();
}

class FetchSnackUsecaseImpl with FetchSnackUsecase {
  static const maxRequestCountPerMinute = 60;

  final SnackRepository _snackRepository;
  int _requestCountPerMinute = 0;

  Stream<List<Snack>> get snackList =>
      _allSnackListController.stream.asBroadcastStream();

  Stream<List<Snack>> get unreadSnackList =>
      _unreadSnackListController.stream.asBroadcastStream();

  Stream<List<Snack>> get archivedSnackList =>
      _archivedSnackListController.stream.asBroadcastStream();

  StreamController<List<Snack>> _allSnackListController =
      StreamController.broadcast();
  StreamController<List<Snack>> _unreadSnackListController =
      StreamController.broadcast();
  StreamController<List<Snack>> _archivedSnackListController =
      StreamController.broadcast();

  FetchSnackUsecaseImpl(this._snackRepository) {
    Timer.periodic(Duration(minutes: 1), (timer) {
      _requestCountPerMinute = 0;
    });
  }

  @override
  Future<List<Snack>> executeList() async {
    final useCache =
        _requestCountPerMinute > maxRequestCountPerMinute ? true : false;

    if (useCache) {
      return snackList.last;
    }

    final list = await _snackRepository.getAllSnack();

    _allSnackListController.sink.add(list);

    _requestCountPerMinute++;

    return list;
  }

  @override
  Future<Snack> executeSingle({required int id}) {
    return _snackRepository.getSnack(id: id);
  }

  @override
  Future<List<Snack>> executeArchivedSnackList() async {
    final useCache =
        _requestCountPerMinute > maxRequestCountPerMinute ? true : false;

    if (useCache) {
      return archivedSnackList.last;
    }

    final isArchivedQuery = EqualQueryModel(field: "is_archived", value: "1");

    final List<Snack> snackList =
        await _snackRepository.getSnackWithQuery(queries: [isArchivedQuery]);

    _archivedSnackListController.sink.add(snackList);

    _requestCountPerMinute++;

    return snackList;
  }

  @override
  Future<List<Snack>> executeUnreadSnackList() async {
    final useCache =
        _requestCountPerMinute > maxRequestCountPerMinute ? true : false;

    if (useCache) {
      return unreadSnackList.last;
    }

    final isArchivedQuery = EqualQueryModel(field: "is_archived", value: "0");

    final List<Snack> snackList =
        await _snackRepository.getSnackWithQuery(queries: [isArchivedQuery]);

    _unreadSnackListController.sink.add(snackList);

    _requestCountPerMinute++;

    return snackList;
  }
}
