import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/domains/add_snack_usecase.dart';
import 'package:snacker/entities/snack.dart';

final addSnackProvider = ChangeNotifierProvider((ref) => AddSnackProvider(
    Snack(title: "", url: "", priority: 3, isArchived: false),
    addSnackUsecase));

class AddSnackProvider extends ChangeNotifier {
  final Snack snack;
  final AddSnackUseCase _addSnackUseCase;

  AddSnackProvider(this.snack, this._addSnackUseCase);

  updateTitle(String title) {
    snack.title = title;
    notifyListeners();
  }

  updateUrl(String url) {
    snack.url = url;
    notifyListeners();
  }

  updatePriority(int priority) {
    snack.priority = priority;
    notifyListeners();
  }

  Future register() async {
    try {
      final response = await _addSnackUseCase.execute(
          url: snack.url, title: snack.title, priority: snack.priority);
      return response;
    } catch (e) {
      return Future.error(e);
    }
  }
}
