import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/domains/get_web_page_title_usecase.dart';

final getWebPageTitleUseCaseProvider =
    Provider<GetWebPageTitleUseCase>((ref) => GetWebPageTitleUseCaseImpl());
