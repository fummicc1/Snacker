import 'package:cloud_firestore/cloud_firestore.dart';

class EqualQueryModel {
  final String field;
  final dynamic value;

  EqualQueryModel({required this.field, required this.value});

  Query build({required Query from}) {
    return from.where(field, isEqualTo: value);
  }
}

class FirestoreClient {
  final FirebaseFirestore _firebaseFirestore;

  FirestoreClient(this._firebaseFirestore);

  Stream<List<Map<String, dynamic>>> listenCollection(String collectionName) =>
      _firebaseFirestore.collection(collectionName).snapshots().map(
          (snapshot) =>
              snapshot.docs.map((document) => document.data()).toList());

  Stream<Map<String, dynamic>> listenDoc(
          {required String collectionName, required String documentName}) =>
      _firebaseFirestore
          .collection(collectionName)
          .doc(documentName)
          .snapshots()
          .map((snapshot) => snapshot.data() ?? {});

  Future<List<Map<String, dynamic>>> getCollection(
      String collectionName) async {
    final Query ref = _firebaseFirestore.collection(collectionName);
    return getWithQuery(ref);
  }

  Future<Map<String, dynamic>> getDoc(
      {required String collectionName, required String documentName}) async {
    final snapshot = await _firebaseFirestore
        .collection(collectionName)
        .doc(documentName)
        .get();
    final data = snapshot.data();
    if (data == null) {
      return Future.error("No Snapshot data");
    }
    return data;
  }

  Future<List<Map<String, dynamic>>> getWithQuery(Query<Object?> query) async {
    final response = await query.get();
    final mapList = response.docs
        .map((document) => document.data())
        .toList()
        .cast<Map<String, dynamic>>();
    return mapList;
  }

  Future<String> createDoc(
      {required String collectionName,
      String? documentId,
      required Map<String, dynamic> data}) async {
    final DocumentReference ref;
    if (documentId != null) {
      ref = _firebaseFirestore.collection(collectionName).doc(documentId);
    } else {
      ref = _firebaseFirestore.collection(collectionName).doc();
    }
    ref.set(data);
    return ref.id;
  }

  Future updateDocWithReference(
      {required DocumentReference reference,
      required Map<String, dynamic> data}) async {
    reference.set(data, SetOptions(merge: true));
  }

  Future updateDocWithPath(
      {required String collection,
      required String documentId,
      required Map<String, dynamic> data}) async {
    final reference = _firebaseFirestore.collection(collection).doc(documentId);
    await updateDocWithReference(reference: reference, data: data);
  }

  Future deleteDocWithReference(DocumentReference reference) async {
    reference.delete();
  }

  Future deleteDocWithPath(
      {required String collection, required String documentId}) async {
    final reference = _firebaseFirestore.collection(collection).doc(documentId);
    await deleteDocWithReference(reference);
  }

  CollectionReference<Object?> buildCollectionReference(
      {required String collectionName}) {
    return _firebaseFirestore.collection(collectionName);
  }
}
