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

  Stream<List<Snack>> get snackList =>
      _allSnackListController.stream.asBroadcastStream();

  Stream<List<Snack>> get unreadSnackList =>
      _unreadSnackListController.stream.asBroadcastStream();

  Stream<List<Snack>> get archivedSnackList =>
      _archivedSnackListController.stream.asBroadcastStream();

  StreamController<List<Snack>> _allSnackListController = StreamController();
  StreamController<List<Snack>> _unreadSnackListController = StreamController();
  StreamController<List<Snack>> _archivedSnackListController = StreamController();

  FetchSnackUsecaseImpl(this._snackRepository);

  @override
  Future<List<Snack>> executeList() async {

    final list = await _snackRepository
        .getAllSnack()
        .catchError((_) => [].cast<Snack>());

    _allSnackListController.add(list);

    return list;
  }

  @override
  Future<Snack> executeSingle({required int id}) {
    return _snackRepository.getSnack(id: id);
  }

  @override
  Future<List<Snack>> executeArchivedSnackList() async {

    final isArchivedQuery = EqualQueryModel(field: "is_archived", value: "1");

    final List<Snack> snackList = await _snackRepository.getSnackWithQuery(
        queries: [isArchivedQuery]).catchError((_) => [].cast<Snack>());

    _archivedSnackListController.add(snackList);

    return snackList;
  }

  @override
  Future<List<Snack>> executeUnreadSnackList() async {

    final isArchivedQuery = EqualQueryModel(field: "is_archived", value: "0");

    final List<Snack> snackList = await _snackRepository.getSnackWithQuery(
        queries: [isArchivedQuery]).catchError((_) => [].cast<Snack>());

    _unreadSnackListController.add(snackList);

    return snackList;
  }
}
