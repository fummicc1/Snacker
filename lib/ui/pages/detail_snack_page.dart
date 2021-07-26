import 'package:flutter/material.dart';
import 'package:flutter/src/widgets/framework.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/entities/snack.dart';

class DetailSnackPage extends HookConsumerWidget {

  DetailSnackPage({Key? key, required this.snack}) : super(key: key);

  final Snack snack;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Scaffold(
      body: buildBody(context, ref),
    );
  }

  Widget buildBody(BuildContext context, WidgetRef ref) {
    return Container();
  }
}