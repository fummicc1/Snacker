import 'package:snacker/database.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/entities/snack_tag.dart';
import 'package:snacker/entities/snack_tag_kind.dart';
import 'package:snacker/firebase/firestore.dart';
import 'package:tuple/tuple.dart';

typedef SnackTagKindName = String;

mixin SnackTagRepository {
  Future<int> createSnackTag({required SnackTag snackTag});

  Future updateSnackTag({required SnackTag newSnackTag});

  Future<SnackTag> getSnackTag({required int id});

  Future<List<SnackTag>> getAllSnackTag();

  Future<List<Tuple2<SnackTag, SnackTagKindName>>> getSnackTagListOfSnack(
      {required int? snackId});

  Future deleteSnackTag({required int id});
}

class SnackTagRepositoryImpl with SnackTagRepository {
  final FirestoreClient _firestoreClient;

  SnackTagRepositoryImpl(this._firestoreClient);

  @override
  Future<int> createSnackTag({required SnackTag snackTag}) {

    return _databaseType.create(
        data: snackTag.toMap(), collectionName: Snack.collectionName);
  }

  @override
  Future deleteSnackTag({required int id}) {
    return _databaseType.delete(id: id, collectionName: Snack.collectionName);
  }

  @override
  Future<List<SnackTag>> getAllSnackTag() async {
    try {
      final response = await _databaseType.readAll(collectionName: Snack.collectionName);
      final list = response
          .map((map) => SnackTagKind.fromMap(map: map))
          .toList()
          .cast<SnackTag>();
      return list;
    } catch (e) {
      return Future.error(e);
    }
  }

  @override
  Future<SnackTag> getSnackTag({required int id}) async {
    final response = await _databaseType
        .read(collectionName: Snack.collectionName, where: "id = ?", whereArgs: ["$id"]);
    if (response.isEmpty) {
      return Future.error("No snack found");
    }
    return SnackTag.fromMap(map: response.first);
  }

  @override
  Future updateSnackTag({required SnackTag newSnackTag}) async {
    return _databaseType.update(
        data: newSnackTag.toMap(), collectionName: Snack.collectionName);
  }

  @override
  Future<List<SnackTag>> getSnackTagWithQuery(
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
          collectionName: Snack.collectionName, where: where, whereArgs: args);
      return response.map((res) => SnackTag.fromMap(map: res)).toList();
    } catch (e) {
      return Future.error(e);
    }
  }

  @override
  Future<List<Tuple2<SnackTag, SnackTagKindName>>> getSnackTagListOfSnack(
      {required int? snackId}) async {
    const String sql =
        "SElECT snack_tags.id, snack_tags.tag_id, snack_tags.snack_id, snack_tag_kinds.name, snack_tag_kinds.is_active FROM snack_tags INNER JOIN snack_tag_kinds on snack_tags.snack_id = ?";
    final response =
        await _databaseType.rawQuery(rawQuery: sql, args: [snackId.toString()]);
    return response
        .map((map) {
          final snackTag = SnackTag.fromMap(map: map);
          final name = map["name"] as SnackTagKindName;
          return Tuple2(snackTag, name);
        })
        .toList()
        .cast<Tuple2<SnackTag, SnackTagKindName>>();
  }
}
