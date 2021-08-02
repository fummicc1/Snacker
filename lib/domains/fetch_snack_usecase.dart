import 'dart:async';

import 'package:snacker/database.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/models/snack_model.dart';
import 'package:snacker/models/snack_tag_model.dart';
import 'package:snacker/repositories/snack_repository.dart';
import 'package:snacker/repositories/snack_tag_kind_repository.dart';
import 'package:snacker/repositories/snack_tag_repository.dart';
import 'package:tuple/tuple.dart';

mixin FetchSnackUsecase {
  Stream<List<SnackModel>> get snackList;

  Stream<List<SnackModel>> get unreadSnackList;

  Stream<List<SnackModel>> get archivedSnackList;

  Future<SnackModel> executeSingle({required int id});

  Future<List<SnackModel>> executeList();

  Future<List<SnackModel>> executeUnreadSnackList();

  Future<List<SnackModel>> executeArchivedSnackList();
}

class FetchSnackUsecaseImpl with FetchSnackUsecase {
  static const maxRequestCountPerMinute = 60;

  final SnackRepository snackRepository;
  final SnackTagRepository snackTagRepository;
  final SnackTagKindRepository snackTagKindRepository;

  Stream<List<SnackModel>> get snackList =>
      _allSnackListController.stream.asBroadcastStream();

  Stream<List<SnackModel>> get unreadSnackList =>
      _unreadSnackListController.stream.asBroadcastStream();

  Stream<List<SnackModel>> get archivedSnackList =>
      _archivedSnackListController.stream.asBroadcastStream();

  StreamController<List<SnackModel>> _allSnackListController =
      StreamController();
  StreamController<List<SnackModel>> _unreadSnackListController =
      StreamController();
  StreamController<List<SnackModel>> _archivedSnackListController =
      StreamController();

  FetchSnackUsecaseImpl(
      {required this.snackRepository,
      required this.snackTagRepository,
      required this.snackTagKindRepository});

  @override
  Future<List<SnackModel>> executeList() async {
    final entityList =
        await snackRepository.getAllSnack().catchError((_) => [].cast<Snack>());

    final modelList = entityList
        .map((snack) {
          if (snack.id == null) return null;
          return executeSingle(id: snack.id!);
        })
        .whereType<SnackModel>()
        .toList();

    _allSnackListController.add(modelList);

    return modelList;
  }

  @override
  Future<SnackModel> executeSingle({required int id}) async {
    final snack = await snackRepository.getSnack(id: id);
    final tagEntityList =
        await snackTagRepository.getSnackTagListOfSnack(snackId: snack.id);
    final tags = tagEntityList
        .map((data) => SnackTagModel(id: data.item1.id, name: data.item2))
        .toList();
    final snackModel = SnackModel(
        title: snack.title,
        url: snack.url,
        thumbnailUrl: snack.thumbnailUrl,
        priority: snack.priority,
        isArchived: snack.isArchived,
        tagList: tags);
    return snackModel;
  }

  @override
  Future<List<SnackModel>> executeArchivedSnackList() async {
    final isArchivedQuery = EqualQueryModel(field: "is_archived", value: "1");

    final List<Snack> snackList = await snackRepository.getSnackWithQuery(
        queries: [isArchivedQuery]).catchError((_) => [].cast<Snack>());

    final models = snackList
        .map((snack) {
          if (snack.id == null) return null;
          return executeSingle(id: snack.id!);
        })
        .whereType<SnackModel>()
        .toList();

    _archivedSnackListController.add(models);

    return models;
  }

  @override
  Future<List<SnackModel>> executeUnreadSnackList() async {
    final isArchivedQuery = EqualQueryModel(field: "is_archived", value: "0");

    final List<Snack> snackList = await snackRepository.getSnackWithQuery(
        queries: [isArchivedQuery]).catchError((_) => [].cast<Snack>());

    final models = snackList
        .map((snack) {
      if (snack.id == null) return null;
      return executeSingle(id: snack.id!);
    })
        .whereType<SnackModel>()
        .toList();

    _archivedSnackListController.add(models);

    return models;
  }
}
