import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/color.dart';
import 'package:snacker/ui/pages/home_page.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  runApp(const ProviderScope(child: MyApp()));
}

class MyApp extends HookConsumerWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return MaterialApp(
      title: 'Snacker',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
          primarySwatch: MaterialColor(primaryColor.value, const {
            50: Color(0xFFEDEAE8),
            100: Color(0xFFD3C9C6),
            200: Color(0xFFB6A6A0),
            300: Color(0xFF99827A),
            400: Color(0xFF83675E),
            500: Color(0xFF6D4C41),
            600: Color(0xFF65453B),
            700: Color(0xFF5A3C32),
            800: Color(0xFF50332A),
            900: Color(0xFF3E241C),
          }),
          backgroundColor: secondaryBackgroundColor),
      home: HomePage(),
    );
  }
}
