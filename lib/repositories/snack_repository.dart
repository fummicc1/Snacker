import 'package:snacker/database.dart';
import 'package:snacker/entities/snack.dart';

mixin SnackRepository {
  Future<int> createSnack({required Snack snack});

  Future updateSnack({required Snack newSnack});

  Future<Snack> getSnack({required int id});

  Future<List<Snack>> getAllSnack({bool useCache = false});
  
  Future deleteSnack({required int id});
}

class SnackRepositoryImpl with SnackRepository {
  final DatabaseType _databaseType;

  List<Snack> _snackList = [];

  SnackRepositoryImpl(this._databaseType);

  @override
  Future<int> createSnack({required Snack snack}) {
    return _databaseType.create(data: snack.toMap(), tableName: Snack.tableName);
  }

  @override
  Future deleteSnack({required int id}) {
    return _databaseType.delete(id: id, tableName: Snack.tableName);
  }

  @override
  Future<List<Snack>> getAllSnack({bool useCache = false}) async {
    if (useCache) {
      return _snackList;
    }
    try {
      final response = await _databaseType.readAll(tableName: Snack.tableName);
      this._snackList = response.map((map) => Snack.fromMap(map: map)).toList().cast<Snack>();
      return _snackList;
    } catch (e) {
      return Future.error(e);
    }
  }

  @override
  Future<Snack> getSnack({required int id}) async {
    final response = await _databaseType.read(tableName: Snack.tableName, where: "id = ?", whereArgs: ["$id"]);
    return Snack.fromMap(map: response);
  }

  @override
  Future updateSnack({required Snack newSnack}) async {
    return _databaseType.update(data: newSnack.toMap(), tableName: Snack.tableName);
  }

  
}
