import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/domains/add_snack_usecase.dart';
import 'package:snacker/domains/fake/get_webpage_title_usecase_fake.dart';
import 'package:snacker/domains/fetch_snack_usecase.dart';
import 'package:snacker/domains/update_snack_usecase.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/main.dart';
import 'package:snacker/repositories/fake/fake_store.dart';
import 'package:snacker/repositories/fake/snack_repository_fake.dart';
import 'package:snacker/repositories/fake/snack_tag_kind_repository_fake.dart';
import 'package:snacker/repositories/fake/snack_tag_repository_fake.dart';
import 'package:snacker/repositories/snack_repository.dart';
import 'package:snacker/repositories/snack_tag_kind_repository.dart';
import 'package:snacker/repositories/snack_tag_repository.dart';
import 'package:snacker/ui/components/snack_list_item.dart';
import 'package:snacker/ui/pages/detail_snack_page.dart';
import 'package:snacker/ui/pages/unread_snack_list_page.dart';
import 'package:snacker/ui/providers/add_snack_usecase_provider.dart';
import 'package:snacker/ui/providers/detail_snack_provider.dart';
import 'package:snacker/ui/providers/get_webpage_title_usecase_provider.dart';
import 'package:snacker/ui/providers/snack_repository_provider.dart';
import 'package:snacker/ui/providers/update_snack_usecase_provider.dart';

main() {

  FakeStore fakeStore = FakeStore();
  SnackRepository fakeSnackRepository = FakeSnackRepository(fakeStore: fakeStore);
  SnackTagRepository fakeSnackTagRepository = FakeSnackTagRepository(fakeStore: fakeStore);
  SnackTagKindRepository fakeSnackTagKindRepository =
  FakeSnackTagKindRepository(fakeStore: fakeStore);

  tearDown(() {
    fakeSnackRepository = FakeSnackRepository(fakeStore: fakeStore);
    fakeSnackTagRepository = FakeSnackTagRepository(fakeStore: fakeStore);
    fakeSnackTagKindRepository = FakeSnackTagKindRepository(fakeStore: fakeStore);
  });

  testWidgets("Unread Snack List Addition", (WidgetTester tester) async {
    TestWidgetsFlutterBinding.ensureInitialized();

    FetchSnackUsecase fetchSnackUseCase = FetchSnackUsecaseImpl(
        snackRepository: fakeSnackRepository,
        snackTagRepository: fakeSnackTagRepository,
        snackTagKindRepository: fakeSnackTagKindRepository);

    AddSnackUseCase addSnackUseCase = AddSnackUseCaseImpl(
        snackRepository: fakeSnackRepository,
        fetchSnackUsecase: fetchSnackUseCase,
        snackTagRepository: fakeSnackTagRepository,
        snackTagKindRepository: fakeSnackTagKindRepository);

    // Build our app and trigger a frame.
    await tester.pumpWidget(ProviderScope(overrides: [
      snackRepositoryProvider
          .overrideWithProvider(Provider((ref) => fakeSnackRepository)),
      getWebPageTitleUseCaseProvider.overrideWithProvider(
          Provider((ref) => GetWebPageTitleUseCaseFake())),
      addSnackUseCaseProvider.overrideWithValue(addSnackUseCase)
    ], child: MyApp()));

    // Move to list page
    final listBottomTab = find.byType(BottomNavigationBar);
    await tester.tap(listBottomTab);

    await tester.runAsync(() async {
      await tester.pump();
    });

    expect(find.byType(SnackListItem), findsNothing);

    await addSnackUseCase.execute(
        url: "https://github.com/fummicc1",
        title: "fummicc1",
        priority: 4,
        tagNameList: ["github"]);

    await tester.runAsync(() async {
      await tester.pump();
    });

    final item = find.byType(SnackListItem);
    expect(item, findsOneWidget);
    expect(find.text("fummicc1"), findsOneWidget);
  });
}
