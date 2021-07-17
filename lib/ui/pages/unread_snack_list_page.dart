import 'package:flutter/cupertino.dart';
import 'package:flutter/src/widgets/framework.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';

class UnReadSnackListPage extends HookConsumerWidget {
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return buildEmptyView(context, ref);
  }

  Widget buildEmptyView(BuildContext context, WidgetRef ref) {
    return Center(
      child: Column(
        children: [
          Text(
            "ここには保存した記事のうち、まだ読了状態ではない記事が一覧で表示されます。"
          )
        ],
      ),
    );
  }
}