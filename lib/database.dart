import 'package:path/path.dart';
import 'package:snacker/entities/snack.dart';
import 'package:sqflite/sqflite.dart';

class EqualQueryModel {
  final String field;
  final dynamic value;

  EqualQueryModel({required this.field, required this.value});

  String buildWhere({required String from}) {
    return join(from, " $field = ? ");
  }

  List<String> buildArgs({required List<String> from}) {
    from.add(value);
    return from;
  }
}

mixin DatabaseType {

  Future open();

  Future<int> create(
      {required Map<String, dynamic> data, required String tableName});

  Future<int> update(
      {required Map<String, dynamic> data, required String tableName});

  Future<List<Map<String, dynamic>>> readAll({required String tableName});

  Future<List<Map<String, dynamic>>> read(
      {required String tableName,
      required String where,
      required List<String> whereArgs});

  Future<int> delete({required int id, required String tableName});
}

class DatabaseManager with DatabaseType {
  late Database _database;

  @override
  Future<Database> open() async {
    try {
      final database = await openDatabase(join(await getDatabasesPath(), "snacker.db"),
          onCreate: (database, version) {
            return database.execute(
                "CREATE TABLE IF NOT EXISTS snacks(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title TEXT NOT NULL, url TEXT NOT NULL, thumbnail_url TEXT, priority INTEGER NOT NULL, is_archived INTEGER NOT NULL)");
          }, version: 1);
      _database = database;
      return database;
    } catch (e) {
      return Future.error(e);
    }
  }

  @override
  Future<int> create(
      {required Map<String, dynamic> data, required String tableName}) async {
    final response = await _database.insert(tableName, data);
    return Future.value(response);
  }

  @override
  Future<int> update(
      {required Map<String, dynamic> data, required String tableName}) async {
    final response = await _database.update(tableName, data);
    return Future.value(response);
  }

  @override
  Future<List<Map<String, dynamic>>> readAll(
      {required String tableName}) async {
    final response = await _database.query(tableName);
    return Future.value(response);
  }

  @override
  Future<List<Map<String, dynamic>>> read(
      {required String tableName,
      required String where,
      required List<String> whereArgs}) async {
    final response =
        await _database.query(tableName, where: where, whereArgs: whereArgs);
    if (response.isEmpty) {
      return Future.error("Empty Query Result");
    }
    return Future.value(response);
  }

  @override
  Future<int> delete({required int id, required String tableName}) async {
    return _database.delete(tableName, where: "id = ?", whereArgs: [id]);
  }
}

final DatabaseType database = DatabaseManager();