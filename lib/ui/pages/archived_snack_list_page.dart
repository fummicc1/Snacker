import 'package:flutter/cupertino.dart';
import 'package:flutter/src/widgets/framework.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/ui/components/list_empty_state_widget.dart';
import 'package:snacker/ui/components/snack_list_item.dart';
import 'package:snacker/ui/providers/archived_snack_list_provider.dart';

class ArchivedSnackListPage extends HookConsumerWidget {
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final archivedSnackList = ref.watch(archivedSnackListProvider);
    return archivedSnackList.when(
        data: (snackList) {
          if (snackList.isEmpty) {
            return buildEmptyView(context, ref);
          }
          return ListView.builder(
              itemCount: snackList.length,
              itemBuilder: (context, index) {
                final snack = snackList[index];
                return SnackListItem(snack: snack);
              });
        },
        loading: () => buildEmptyView(context, ref),
        error: (error, _) => ListEmptyStateView(message: "エラー: $error"));
  }

  Widget buildEmptyView(BuildContext context, WidgetRef ref) {
    return ListEmptyStateView(message: "ここには保存した記事のうち、まだ読了状態ではない記事が一覧で表示されます。");
  }
}
