import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:snacker/ui/pages/search_page.dart';

final appBarList = [
  AppBar(
    title: const Text("見つける"),
  ),
  AppBar(
    title: const Text("未読"),
  ),
  AppBar(
    title: const Text("ユーザー"),
  ),
];

final appBarIndexProvider = StateProvider((ref) => 0);

class HomePage extends ConsumerWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {

    int appBarIndex = ref.watch(appBarIndexProvider).state;

    return Scaffold(
      appBar: appBarList[appBarIndex],
      bottomNavigationBar: BottomAppBar(
        color: Theme.of(context).primaryColor,
        notchMargin: 8,
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 8),
          child: Row(
            children: [
              IconButton(
                icon: Icon(Icons.search),
                onPressed: () {
                  ref.watch(appBarIndexProvider).state = 0;
                },
              ),
              IconButton(
                icon: Icon(Icons.list),
                onPressed: () {
                  ref.watch(appBarIndexProvider).state = 1;
                },
              ),
              IconButton(
                icon: Icon(Icons.person),
                onPressed: () {
                  ref.watch(appBarIndexProvider).state = 2;
                },
              )
            ],
          ),
        ),
      ),
      body: buildBody(ref),
    );
  }

  Widget buildBody(WidgetRef ref) {
    final index = ref.watch(appBarIndexProvider).state;
    if (index == 0) {
      return SearchPage();
    } else {
      return Container();
    }
  }
}
