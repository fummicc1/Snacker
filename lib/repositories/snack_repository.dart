import 'dart:async';

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/firebase/firestore.dart';

mixin SnackRepository {
  Future<String> createSnack({required Snack snack});

  Future updateSnack({required Snack newSnack});

  Future<Snack> getSnack({required String id});

  Future<List<Snack>> getAllSnack();

  Future<List<Snack>> getSnackWithQuery(
      {required List<EqualQueryModel> queries});

  Future deleteSnack({required String id});
}

class SnackRepositoryImpl with SnackRepository {
  final FirestoreClient _firestoreClient;

  SnackRepositoryImpl(this._firestoreClient);

  @override
  Future<String> createSnack({required Snack snack}) async {
    final documentID = await _firestoreClient.createDoc(collectionName: Snack.collectionName, data: snack.toMap());
    return documentID;
  }

  @override
  Future deleteSnack({required String id}) async {
    await _firestoreClient.deleteDocWithPath(collection: Snack.collectionName, documentId: id);
  }

  @override
  Future<List<Snack>> getAllSnack() async {
    try {
      final response = await _firestoreClient.getCollection(Snack.collectionName);
      final snackList =
          response.map((map) => Snack.fromMap(map: map)).toList().cast<Snack>();
      return snackList;
    } catch (e) {
      return Future.error(e);
    }
  }

  @override
  Future<Snack> getSnack({required String id}) async {
    final filter = EqualQueryModel(field: "id", value: id);
    Query base = FirebaseFirestore.instance.collection(Snack.collectionName);
    base = filter.build(from: base);
    final response = await _firestoreClient.getWithQuery(base);
    if (response.isEmpty) {
      return Future.error("No snack found");
    }
    return Snack.fromMap(map: response.first);
  }

  @override
  Future updateSnack({required Snack newSnack}) async {
    final ref = FirebaseFirestore.instance.collection(Snack.collectionName).doc(newSnack.id);
    _firestoreClient.updateDocWithReference(reference: newSnack, data: data)
    return _databaseType.update(
        data: newSnack.toMap(), collectionName: Snack.collectionName);
  }

  @override
  Future<List<Snack>> getSnackWithQuery(
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
      return response.map((res) => Snack.fromMap(map: res)).toList();
    } catch (e) {
      return Future.error(e);
    }
  }
}
