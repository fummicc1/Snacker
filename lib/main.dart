import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/color.dart';
import 'package:snacker/database.dart';
import 'package:snacker/ui/pages/home_page.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  database.open();
  runApp(ProviderScope(child: MyApp()));
}

class MyApp extends HookConsumerWidget {
  @override
  Widget build(BuildContext context, WidgetRef ref) {

    return MaterialApp(
      title: 'Snacker',
      theme: ThemeData(
          primarySwatch: MaterialColor(primaryColor.value, {
            50: const Color(0xFFEDEAE8),
            100: const Color(0xFFD3C9C6),
            200: const Color(0xFFB6A6A0),
            300: const Color(0xFF99827A),
            400: const Color(0xFF83675E),
            500: const Color(0xFF6D4C41),
            600: const Color(0xFF65453B),
            700: const Color(0xFF5A3C32),
            800: const Color(0xFF50332A),
            900: const Color(0xFF3E241C),
          }),
          backgroundColor: secondaryBackgroundColor),
      home: HomePage(),
    );
  }
}
