import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/src/widgets/framework.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';

class UnReadSnackListPage extends HookConsumerWidget {
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return buildEmptyView(context, ref);
  }

  Widget buildEmptyView(BuildContext context, WidgetRef ref) {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Center(
        child: Column(
          children: [
            Spacer(),
            Text(
              "ここには保存した記事のうち、まだ読了状態ではない記事が一覧で表示されます。",
              style: Theme.of(context).textTheme.bodyText1
            ),
            SizedBox(height: 32,),
            Container(
              height: 48,
              width: 48,
              child: Image(image: AssetImage("assets/images/icon.png")),
            ),
            Spacer(),
          ],
        ),
      ),
    );
  }
}