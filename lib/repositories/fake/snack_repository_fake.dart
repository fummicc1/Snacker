import 'package:snacker/database.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/repositories/fake/fake_store.dart';
import 'package:snacker/repositories/snack_repository.dart';

class FakeSnackRepository with SnackRepository {

  final FakeStore fakeStore;

  FakeSnackRepository({required this.fakeStore});

  @override
  Future<int> createSnack({required Snack snack}) async {
    final newId = fakeStore.snackList.length;
    snack.id = newId;
    fakeStore.snackList.add(snack);
    return newId;
  }

  @override
  Future deleteSnack({required int id}) async {
    final targetIndex = fakeStore.snackList.indexWhere((element) => element.id == id);
    fakeStore.snackList.removeAt(targetIndex);
  }

  @override
  Future<List<Snack>> getAllSnack({bool useCache = false}) async {
    if (useCache) {
      return Future.error("UnSupported condition");
    }
    return fakeStore.snackList;
  }

  @override
  Future<Snack> getSnack({required int id}) async {
    final target = fakeStore.snackList.firstWhere((element) => element.id == id);
    return target;
  }

  @override
  Future<List<Snack>> getSnackWithQuery({required List<EqualQueryModel> queries}) async {
    return fakeStore.snackList;
  }

  @override
  Future updateSnack({required Snack newSnack}) async {
    final index = fakeStore.snackList.indexWhere((element) => element.id == newSnack.id);
    fakeStore.snackList[index] = newSnack;
  }

}