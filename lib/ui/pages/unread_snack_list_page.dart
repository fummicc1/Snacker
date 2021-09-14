import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/models/snack_model.dart';
import 'package:snacker/ui/components/list_empty_state_widget.dart';
import 'package:snacker/ui/components/snack_list_item.dart';
import 'package:snacker/ui/providers/un_read_snack_list_provider.dart';

class UnReadSnackListPage extends HookConsumerWidget {
  const UnReadSnackListPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final unReadSnackList = ref.watch(unReadSnackListProvider);

    return unReadSnackList.when(
        data: (snackList) => snackList.isEmpty
            ? buildEmptyView(context, ref)
            : buildContentView(context, ref, snackList: snackList),
        loading: () => buildEmptyView(context, ref),
        error: (error, _) => buildEmptyView(context, ref));
  }

  Widget buildEmptyView(BuildContext context, WidgetRef ref) {
    return const ListEmptyStateView(
        message: "ここには保存した記事のうち、まだ読了状態ではない記事が一覧で表示されます。");
  }

  Widget buildContentView(BuildContext context, WidgetRef ref,
      {required List<SnackModel> snackList}) {
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
