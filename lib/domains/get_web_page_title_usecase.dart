import 'package:universal_html/controller.dart';

mixin GetWebPageTitleUseCase {
  Future<String> execute({required String url});
}

enum GetWebPageTitleUseCaseError { noTitleOnWebPage }

class GetWebPageTitleUseCaseImpl with GetWebPageTitleUseCase {
  @override
  Future<String> execute({required String url}) async {
    final controller = WindowController();
    await controller.openHttp(uri: Uri.parse(url));
    final title =
        controller.window?.document.querySelector('title')?.firstChild?.text;
    if (title == null) {
      return Future.error(GetWebPageTitleUseCaseError.noTitleOnWebPage);
    }
    return title;
  }
}
