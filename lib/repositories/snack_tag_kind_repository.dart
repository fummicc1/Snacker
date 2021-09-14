import 'package:snacker/database.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/entities/snack_tag_kind.dart';

mixin SnackTagKindRepository {
  Future<int> createSnackTagKind({required SnackTagKind snackTagKind});

  Future updateSnackTagKind({required SnackTagKind newSnackTagKind});

  Future<SnackTagKind> getSnackTagKind({required int id});

  Future<List<SnackTagKind>> getAllSnackTagKind();

  Future<List<SnackTagKind>> getSnackTagKindWithQuery(
      {required List<EqualQueryModel> queries});

  Future deleteSnackTagKind({required int id});
}

class SnackTagKindRepositoryImpl with SnackTagKindRepository {
  final DatabaseType _databaseType;

  SnackTagKindRepositoryImpl(this._databaseType);

  @override
  Future<int> createSnackTagKind({required SnackTagKind snackTagKind}) {
    return _databaseType.create(
        data: snackTagKind.toMap(), tableName: Snack.tableName);
  }

  @override
  Future deleteSnackTagKind({required int id}) {
    return _databaseType.delete(id: id, tableName: Snack.tableName);
  }

  @override
  Future<List<SnackTagKind>> getAllSnackTagKind() async {
    try {
      final response = await _databaseType.readAll(tableName: Snack.tableName);
      final list = response
          .map((map) => SnackTagKind.fromMap(map: map))
          .toList()
          .cast<SnackTagKind>();
      return list;
    } catch (e) {
      return Future.error(e);
    }
  }

  @override
  Future<SnackTagKind> getSnackTagKind({required int id}) async {
    final response = await _databaseType
        .read(tableName: Snack.tableName, where: "id = ?", whereArgs: ["$id"]);
    if (response.isEmpty) {
      return Future.error("No snack found");
    }
    return SnackTagKind.fromMap(map: response.first);
  }

  @override
  Future updateSnackTagKind({required SnackTagKind newSnackTagKind}) async {
    return _databaseType.update(
        data: newSnackTagKind.toMap(), tableName: Snack.tableName);
  }

  @override
  Future<List<SnackTagKind>> getSnackTagKindWithQuery(
      {required List<EqualQueryModel> queries}) async {
    String where = "";
    List<String> args = [];

    for (int i = 0; i < queries.length; i++) {
      final query = queries[i];
      where = query.buildWhere(from: where);
      args = query.buildArgs(from: args);
    }

    try {
      final response = await _databaseType.read(
          tableName: Snack.tableName, where: where, whereArgs: args);
      return response.map((res) => SnackTagKind.fromMap(map: res)).toList();
    } catch (e) {
      return Future.error(e);
    }
  }
}
