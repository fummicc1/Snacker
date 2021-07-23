import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/domains/add_snack_usecase.dart';
import 'package:snacker/domains/get_web_page_title_usecase.dart';
import 'package:snacker/entities/snack.dart';

final addSnackProvider = ChangeNotifierProvider((ref) => AddSnackProvider(
    Snack(title: "", url: "", priority: 3, isArchived: false),
    addSnackUsecase,
    GetWebPageTitleUseCaseImpl()));

class AddSnackProvider extends ChangeNotifier {
  final Snack snack;
  final AddSnackUseCase _addSnackUseCase;
  final GetWebPageTitleUseCase _getWebPageTitleUseCase;

  AddSnackProvider(
      this.snack, this._addSnackUseCase, this._getWebPageTitleUseCase);

  updateTitle(String title) {
    snack.title = title;
    notifyListeners();
  }

  updateUrl(String url, {required bool shouldScraping}) async {
    snack.url = url;

    if (shouldScraping) {
      final title = await _getWebPageTitleUseCase.execute(url: url);
      snack.title = title;
      notifyListeners();
    } else {
      notifyListeners();
    }
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
