import 'package:snacker/database.dart';
import 'package:snacker/entities/snack_tag.dart';
import 'package:snacker/repositories/fake/fake_store.dart';
import 'package:snacker/repositories/fake/snack_tag_kind_repository_fake.dart';
import 'package:snacker/repositories/snack_tag_kind_repository.dart';
import 'package:snacker/repositories/snack_tag_repository.dart';
import 'package:tuple/tuple.dart';

class FakeSnackTagRepository with SnackTagRepository {
  final FakeStore fakeStore;

  FakeSnackTagRepository({required this.fakeStore});

  @override
  Future<int> createSnackTag({required SnackTag snackTag}) async {
    final newId = fakeStore.snackTagList.length;
    snackTag.id = newId;
    fakeStore.snackTagList.add(snackTag);
    return newId;
  }

  @override
  Future deleteSnackTag({required int id}) async {
    fakeStore.snackTagList.removeWhere((element) => element.id == id);
  }

  @override
  Future<List<SnackTag>> getAllSnackTag() async {
    return fakeStore.snackTagList;
  }

  @override
  Future<SnackTag> getSnackTag({required int id}) async {
    return fakeStore.snackTagList.where((element) => element.id == id).first;
  }

  @override
  Future<List<Tuple2<SnackTag, SnackTagKindName>>> getSnackTagListOfSnack(
      {required int? snackId}) async {
    final tags = fakeStore.snackTagList
        .where((element) => element.snackId == snackId)
        .toList();
    final res = tags.map((tag) {
      final name = fakeStore.snackTagKindList
          .firstWhere((element) => element.id == tag.tagId)
          .name;
      return Tuple2(tag, name);
    }).toList();
    return res;
  }

  @override
  Future<List<SnackTag>> getSnackTagWithQuery(
      {required List<EqualQueryModel> queries}) {
    throw UnimplementedError();
  }

  @override
  Future updateSnackTag({required SnackTag newSnackTag}) async {
    final index = fakeStore.snackTagList
        .indexWhere((element) => element.id == newSnackTag.id);
    fakeStore.snackTagList[index] = newSnackTag;
  }
}
