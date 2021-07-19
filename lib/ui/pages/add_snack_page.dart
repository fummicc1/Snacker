import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/entities/snack.dart';

final addSnackProvider = StateProvider(
        (ref) => Snack(title: "", url: "", priority: 3, isArchived: false));

class AddSnackPage extends HookConsumerWidget {
  final String url;

  AddSnackPage({Key? key, required this.url}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final snack = ref.watch(addSnackProvider).state;

    return Scaffold(
      appBar: AppBar(
        title: const Text("記事の登録"),
        actions: [IconButton(onPressed: () {}, icon: Icon(Icons.add))],
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            SizedBox(
              height: 16,
            ),
            TextField(),
            SizedBox(
              height: 16,
            ),
            TextField(),
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
                            if (index + 1 < snack.priority) {
                              return IconButton(
                                  onPressed: () {
                                    ref.read(addSnackProvider).state.priority =
                                        index + 1;
                                  },
                                  icon: Icon(Icons.star));
                            } else {
                              return IconButton(
                                  onPressed: () {
                                    ref.read(addSnackProvider).state.priority =
                                        index + 1;
                                  },
                                  icon: Icon(Icons.star_border));
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
