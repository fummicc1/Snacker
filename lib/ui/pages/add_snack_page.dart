import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/src/widgets/framework.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';

class AddSnackPage extends HookConsumerWidget {
  final String url;

  AddSnackPage({Key? key, required this.url}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("記事の登録"),
        actions: [IconButton(onPressed: () {}, icon: Icon(Icons.add))],
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            SizedBox(height: 16,),
            TextField(),
            SizedBox(height: 16,),
            TextField(),
            SizedBox(height: 16,),
            Row(
              children: [
                Text("優先度"),
                Wrap(
                  children: [
                    IconButton(onPressed: null, icon: Icon(Icons.star)),
                    IconButton(onPressed: null, icon: Icon(Icons.star)),
                    IconButton(onPressed: null, icon: Icon(Icons.star)),
                    IconButton(onPressed: null, icon: Icon(Icons.star)),
                    IconButton(onPressed: null, icon: Icon(Icons.star)),
                  ],
                )
              ],
            )
          ],
        ),
      ),
    );
  }
}
