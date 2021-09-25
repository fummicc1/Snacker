
import 'dart:async';

import 'package:firebase_auth/firebase_auth.dart';

class AuthClient {
  final FirebaseAuth _firebaseAuth;

  final StreamController<User?> _userStreamController = StreamController();

  AuthClient(this._firebaseAuth) {
    final stream = _firebaseAuth.authStateChanges();
    _userStreamController.addStream(stream);
  }

  Stream<User?> get userStream => _userStreamController.stream;

  String? get currentUserId => _firebaseAuth.currentUser?.uid;

  Future<User?> logInWithEmail(
      {required String email, required String password}) async {
    final result = await _firebaseAuth.signInWithEmailAndPassword(
        email: email, password: password);
    return result.user;
  }

  Future<User?> signUpWithEmail(
      {required String email, required String password}) async {
    final result = await _firebaseAuth.createUserWithEmailAndPassword(
        email: email, password: password);
    return result.user;
  }

  Future signOut() => _firebaseAuth.signOut();

  Future delete() =>
      _firebaseAuth.currentUser?.delete() ?? Future.error("No Current User");
}