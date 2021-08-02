import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/ui/providers/add_snack_provider.dart';
import 'package:snacker/ui/providers/fetch_snack_usecase_provider.dart';
import 'package:snacker/ui/providers/un_read_snack_list_provider.dart';

class AddSnackPage extends HookConsumerWidget {
  AddSnackPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final provider = ref.watch(addSnackProvider);
    final titleController = useTextEditingController(text: provider.title);
    final urlController = useTextEditingController(text: provider.url);

    return Scaffold(
      appBar: AppBar(
        title: const Text("記事の登録"),
        actions: [
          IconButton(
              onPressed: () async {
                await provider.register();
                Navigator.of(context).pop();
              },
              icon: Icon(Icons.add))
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            SizedBox(
              height: 16,
            ),
            TextField(
              controller: urlController,
              decoration: InputDecoration(labelText: "URL"),
              onChanged: (url) => provider.updateUrl(url, shouldScraping: false),
            ),
            SizedBox(
              height: 16,
            ),
            TextField(
              controller: titleController,
              decoration: InputDecoration(labelText: "記事のタイトル"),
              onChanged: (title) => provider.updateTitle(title),
            ),
            SizedBox(
              height: 16,
            ),
            Container(
              height: 64,
              child: Row(
                children: [
                  Text("優先度"),
                  Flexible(
                      child: ListView.builder(
                          scrollDirection: Axis.horizontal,
                          itemCount: 5,
                          itemBuilder: (context, index) {
                            if (index + 1 <= provider.priority) {
                              return IconButton(
                                  onPressed: () {
                                    provider.updatePriority(index + 1);
                                  },
                                  icon: Icon(Icons.star, size: 32));
                            } else {
                              return IconButton(
                                  onPressed: () {
                                    provider.updatePriority(index + 1);
                                  },
                                  icon: Icon(Icons.star_border, size: 32));
                            }
                          }))
                ],
              ),
            )
          ],
        ),
      ),
    );
  }
}
