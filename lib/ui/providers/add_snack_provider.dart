import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/domains/add_snack_usecase.dart';
import 'package:snacker/domains/get_web_page_title_usecase.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/ui/providers/add_snack_usecase_provider.dart';
import 'package:snacker/ui/providers/get_webpage_title_usecase_provider.dart';

final addSnackProvider = ChangeNotifierProvider<AddSnackProvider>((ref) {
  final addSnackUseCase = ref.read(addSnackUseCaseProvider);
  final getWebPageTitleUseCase = ref.read(getWebPageTitleUseCaseProvider);
  return AddSnackProvider(
    Snack(title: "", url: "", priority: 3, isArchived: false),
    addSnackUseCase,
    getWebPageTitleUseCase,
  );
});

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

  Future updateUrl(String url, {required bool shouldScraping}) async {
    snack.url = url;

    try {
      if (shouldScraping) {
        final title = await _getWebPageTitleUseCase.execute(url: url);
        snack.title = title;
        notifyListeners();
      } else {
        notifyListeners();
      }
      return true;
    } catch (e) {
      return Future.error(e);
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
