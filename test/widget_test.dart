// This is a basic Flutter widget test.
//
// To perform an interaction with a widget in your test, use the WidgetTester
// utility that Flutter provides. For example, you can send tap and scroll
// gestures. You can also use WidgetTester to find child widgets in the widget
// tree, read text, and verify that the values of widget properties are correct.

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/domains/fake/get_webpage_title_usecase_fake.dart';

import 'package:snacker/main.dart';
import 'package:snacker/repositories/fake/fake_store.dart';
import 'package:snacker/repositories/fake/snack_repository_fake.dart';
import 'package:snacker/ui/providers/get_webpage_title_usecase_provider.dart';
import 'package:snacker/ui/providers/snack_repository_provider.dart';

void main() {
  testWidgets('Select tab', (WidgetTester tester) async {
    TestWidgetsFlutterBinding.ensureInitialized();

    // Build our app and trigger a frame.
    await tester.pumpWidget(ProviderScope(overrides: [
      snackRepositoryProvider
          .overrideWithProvider(Provider((ref) => FakeSnackRepository(fakeStore: FakeStore()))),
      getWebPageTitleUseCaseProvider
          .overrideWithProvider(Provider((ref) => GetWebPageTitleUseCaseFake()))
    ], child: MyApp()));

    // Verify that our counter starts at 0.
    expect(find.text('検索'), findsOneWidget);

    // Tap the '+' icon and trigger a frame.
    await tester.tap(find.byIcon(Icons.list));
    await tester.runAsync(() async {
      await tester.pump();
    });

    // Verify that our counter has incremented.
    // expect(find.text('優先度'), findsOneWidget);
    expect(find.text('見つける'), findsNothing);
    expect(find.text('未読'), findsOneWidget);
    expect(find.text('読了済み'), findsOneWidget);
  });
}
