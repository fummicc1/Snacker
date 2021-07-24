import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/src/widgets/framework.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/ui/components/list_empty_state_widget.dart';
import 'package:snacker/ui/components/snack_list_item.dart';
import 'package:snacker/ui/providers/un_read_snack_list_provider.dart';

class UnReadSnackListPage extends HookConsumerWidget {
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final unReadSnackList = ref.watch(unReadSnackListProvider).state;

    return FutureBuilder(
      future: unReadSnackList,
      initialData: [],
      builder: (context, snapshot) {
        if (snapshot.hasData && snapshot.data is List<Snack>) {
          final snackList = snapshot.data as List<Snack>;
          if (snackList.isEmpty) {
            return buildEmptyView(context, ref);
          }
          return buildContentView(context, ref, snackList: snackList);
        } else {
          return buildEmptyView(context, ref);
        }
      },
    );
  }

  Widget buildEmptyView(BuildContext context, WidgetRef ref) {
    return ListEmptyStateView(message: "ここには保存した記事のうち、まだ読了状態ではない記事が一覧で表示されます。");
  }

  Widget buildContentView(BuildContext context, WidgetRef ref,
      {required List<Snack> snackList}) {
    return Stack(
      children: [
        ListView.builder(
            itemCount: snackList.length,
            itemBuilder: (context, index) {
              final snack = snackList[index];
              return SnackListItem(snack: snack);
            })
      ],
    );
  }
}
