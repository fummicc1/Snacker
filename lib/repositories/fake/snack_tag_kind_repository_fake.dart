import 'package:snacker/database.dart';
import 'package:snacker/entities/snack_tag_kind.dart';
import 'package:snacker/repositories/fake/fake_store.dart';
import 'package:snacker/repositories/snack_tag_kind_repository.dart';

class FakeSnackTagKindRepository with SnackTagKindRepository {

  final FakeStore fakeStore;

  FakeSnackTagKindRepository({required this.fakeStore});

  @override
  Future<int> createSnackTagKind({required SnackTagKind snackTagKind}) async {
    final id = fakeStore.snackTagKindList.length;
    snackTagKind.id = id;
    fakeStore.snackTagKindList.add(snackTagKind);
    return id;
  }

  @override
  Future deleteSnackTagKind({required int id}) async {
    fakeStore.snackTagKindList.removeWhere((element) => element.id == id);
  }

  @override
  Future<List<SnackTagKind>> getAllSnackTagKind() async {
    return fakeStore.snackTagKindList;
  }

  @override
  Future<SnackTagKind> getSnackTagKind({required int id}) async {
    return fakeStore.snackTagKindList.where((element) => element.id == id).first;
  }

  @override
  Future<List<SnackTagKind>> getSnackTagKindWithQuery(
      {required List<EqualQueryModel> queries}) {
    throw UnimplementedError();
  }

  @override
  Future updateSnackTagKind({required SnackTagKind newSnackTagKind}) async {
    final index =
        fakeStore.snackTagKindList.indexWhere((element) => element.id == newSnackTagKind.id);
    fakeStore.snackTagKindList[index] = newSnackTagKind;
  }
}
