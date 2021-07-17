import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/color.dart';
import 'package:snacker/ui/pages/home_page.dart';

void main() {
  runApp(ProviderScope(child: MyApp()));
}

class MyApp extends HookConsumerWidget {
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return MaterialApp(
      title: 'Snacker',
      theme: ThemeData(
          primaryColor: primaryColor,
          primaryColorLight: lightPrimaryColor,
      backgroundColor: secondaryBackgroundColor),
      home: HomePage(),
    );
  }
}
