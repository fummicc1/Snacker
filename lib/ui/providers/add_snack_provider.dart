import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/domains/add_snack_usecase.dart';
import 'package:snacker/domains/get_web_page_title_usecase.dart';
import 'package:snacker/ui/providers/add_snack_usecase_provider.dart';
import 'package:snacker/ui/providers/get_webpage_title_usecase_provider.dart';

final addSnackProvider = ChangeNotifierProvider<AddSnackProvider>((ref) {
  final addSnackUseCase = ref.read(addSnackUseCaseProvider);
  final getWebPageTitleUseCase = ref.read(getWebPageTitleUseCaseProvider);
  return AddSnackProvider(
    title: "",
    url: "",
    priority: 3,
    tagNameList: [],
    addSnackUseCase: addSnackUseCase,
    getWebPageTitleUseCase: getWebPageTitleUseCase,
  );
});

class AddSnackProvider extends ChangeNotifier {
  String title;
  int priority;
  String? thumbnailUrl;
  String url;
  List<String> tagNameList;

  final AddSnackUseCase addSnackUseCase;
  final GetWebPageTitleUseCase getWebPageTitleUseCase;

  AddSnackProvider(
      {required this.title,
      required this.url,
      this.thumbnailUrl,
      required this.priority,
      required this.tagNameList,
      required this.addSnackUseCase,
      required this.getWebPageTitleUseCase});

  updateTitle(String title) {
    this.title = title;
    notifyListeners();
  }

  Future updateUrl(String url, {required bool shouldScraping}) async {
    this.url = url;

    try {
      if (shouldScraping) {
        final title = await getWebPageTitleUseCase.execute(url: url);
        this.title = title;
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
    this.priority = priority;
    notifyListeners();
  }

  Future register() async {
    try {
      final response = await addSnackUseCase.execute(
        url: url,
        title: title,
        priority: priority,
        thumbnailUrl: thumbnailUrl,
        tagNameList: tagNameList,
      );
      return response;
    } catch (e) {
      return Future.error(e);
    }
  }
}
