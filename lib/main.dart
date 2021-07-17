import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:realm/realm.dart';
import 'package:snacker/color.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/realm_controller.dart';
import 'package:snacker/ui/pages/home_page.dart';

void main() {
  runApp(ProviderScope(child: MyApp()));
}

class MyApp extends ConsumerWidget {
  late RealmController _realmController;

  MyApp() {
    _realmController = RealmController();
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return MaterialApp(
      title: 'Snacker',
      theme: ThemeData(
          primaryColor: primaryColor,
          primaryColorLight: lightPrimaryColor),
      home: HomePage(),
    );
  }
}
