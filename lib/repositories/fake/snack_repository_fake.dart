import 'package:snacker/database.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/repositories/snack_repository.dart';

class FakeSnackRepository with SnackRepository {

  List<Snack> _snackList = [];

  @override
  Future<int> createSnack({required Snack snack}) async {
    final newId = _snackList.length;
    snack.id = newId;
    _snackList.add(snack);
    return newId;
  }

  @override
  Future deleteSnack({required int id}) async {
    final targetIndex = _snackList.indexWhere((element) => element.id == id);
    _snackList.removeAt(targetIndex);
  }

  @override
  Future<List<Snack>> getAllSnack({bool useCache = false}) async {
    if (useCache) {
      return Future.error("UnSupported condition");
    }
    return _snackList;
  }

  @override
  Future<Snack> getSnack({required int id}) async {
    final target = _snackList.firstWhere((element) => element.id == id);
    return target;
  }

  @override
  Future<List<Snack>> getSnackWithQuery({required List<EqualQueryModel> queries}) async {
    return _snackList;
  }

  @override
  Future updateSnack({required Snack newSnack}) async {
    final index = _snackList.indexWhere((element) => element.id == newSnack.id);
    _snackList[index] = newSnack;
  }

}