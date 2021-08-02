import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/domains/add_snack_usecase.dart';
import 'package:snacker/domains/fake/get_webpage_title_usecase_fake.dart';
import 'package:snacker/domains/fetch_snack_usecase.dart';
import 'package:snacker/domains/update_snack_usecase.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/models/snack_model.dart';
import 'package:snacker/repositories/fake/fake_store.dart';
import 'package:snacker/repositories/fake/snack_repository_fake.dart';
import 'package:snacker/repositories/fake/snack_tag_kind_repository_fake.dart';
import 'package:snacker/repositories/fake/snack_tag_repository_fake.dart';
import 'package:snacker/repositories/snack_repository.dart';
import 'package:snacker/repositories/snack_tag_kind_repository.dart';
import 'package:snacker/repositories/snack_tag_repository.dart';
import 'package:snacker/ui/pages/detail_snack_page.dart';
import 'package:snacker/ui/providers/add_snack_usecase_provider.dart';
import 'package:snacker/ui/providers/detail_snack_provider.dart';
import 'package:snacker/ui/providers/fetch_snack_usecase_provider.dart';
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

  testWidgets("Change isArchived State", (tester) async {
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

    UpdateSnackUseCase updateSnackUseCase = UpdateSnackUseCaseImpl(
        snackRepository: fakeSnackRepository,
        fetchSnackUsecase: fetchSnackUseCase,
        snackTagRepository: fakeSnackTagRepository,
        snackTagKindRepository: fakeSnackTagKindRepository);

    final snack = SnackModel(
        title: "fummicc1",
        url: "https://github.com/fummicc1",
        priority: 4,
        isArchived: false,
        tagList: []);

    final newId = await addSnackUseCase.execute(
        url: snack.url,
        title: snack.title,
        priority: snack.priority,
        tagNameList: []);

    snack.id = newId;

    // Build our app and trigger a frame.
    await tester.pumpWidget(ProviderScope(
        overrides: [
          snackRepositoryProvider
              .overrideWithProvider(Provider((ref) => fakeSnackRepository)),
          getWebPageTitleUseCaseProvider.overrideWithProvider(
              Provider((ref) => GetWebPageTitleUseCaseFake())),
          addSnackUseCaseProvider.overrideWithValue(addSnackUseCase),
          detailSnackProvider.overrideWithValue(StateController(snack)),
          detailPageWebsiteProvider
              .overrideWithValue(StateController(snack.url)),
          updateSnackUseCaseProvider.overrideWithValue(updateSnackUseCase),
          fetchSnackUsecaseProvider.overrideWithValue(fetchSnackUseCase)
        ],
        child: MaterialApp(
          home: DetailSnackPage(),
        )));

    // isArchived == false
    expect(find.text("未読"), findsOneWidget);
    expect(find.text("アーカイブ済み"), findsNothing);

    await tester.tap(find.text("未読"));
    await tester.pump();

    expect(find.text("未読"), findsNothing);
    expect(find.text("アーカイブ済み"), findsOneWidget);
  });
}
