import 'package:flutter/material.dart';
import 'package:flutter/src/widgets/framework.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/ui/pages/unread_snack_list_page.dart';

const numberOfTab = 3;

class ListPage extends HookConsumerWidget {
  ListPage({Key? key, required this.tabController}) : super(key: key);

  final TabController tabController;

  @override
  Widget build(BuildContext context, WidgetRef ref) {

    if (tabController.index == 0) {
      return UnReadSnackListPage();
    } else {
      return Container();
    }
  }
}
