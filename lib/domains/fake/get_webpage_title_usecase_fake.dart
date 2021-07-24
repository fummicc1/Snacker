import 'package:snacker/domains/get_web_page_title_usecase.dart';

class GetWebPageTitleUseCaseFake with GetWebPageTitleUseCase {
  @override
  Future<String> execute({required String url}) async {
    return url;
  }

}