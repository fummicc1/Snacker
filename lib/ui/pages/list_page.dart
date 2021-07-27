import 'package:flutter/material.dart';
import 'package:flutter/src/widgets/framework.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/ui/pages/archived_snack_list_page.dart';
import 'package:snacker/ui/pages/unread_snack_list_page.dart';
import 'package:snacker/ui/providers/app_bar_index_provider.dart';
import 'package:snacker/ui/providers/tab_controller_index_provider.dart';

const numberOfTab = 3;

class ListPage extends HookConsumerWidget {
  const ListPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {

    final index = ref.watch(tabControllerIndexProvider).state;
    
    if (index == 0) {
      return UnReadSnackListPage();
    } else if (index == 1) {
      return ArchivedSnackListPage();
    } else {
      return Container();
    }
  }
}
