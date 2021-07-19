import 'package:flutter/material.dart';
import 'package:flutter_hooks/flutter_hooks.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/ui/pages/add_snack_page.dart';
import 'package:snacker/ui/pages/list_page.dart';
import 'package:snacker/ui/pages/search_page.dart';
import 'package:snacker/ui/providers/add_snack_provider.dart';
import 'package:snacker/ui/providers/app_bar_index_provider.dart';
import 'package:snacker/ui/providers/search_website_provider.dart';

const appBarShape = RoundedRectangleBorder(
    borderRadius: BorderRadius.vertical(bottom: Radius.circular(8)));

final PreferredSizeWidget Function(BuildContext, WidgetRef, TabController)
    tabBar = (context, ref, controller) {
  final appBarIndex = ref.watch(appBarIndexProvider).state;
  if (appBarIndex == 1) {
    return TabBar(
      tabs: [
        Tab(text: "未読"),
        Tab(text: "読了済み"),
        Tab(text: "最近"),
      ],
      labelColor: Theme.of(context).primaryColorLight,
      unselectedLabelColor: Theme.of(context).primaryColorLight,
      controller: controller,
    );
  } else {
    return TabBar(tabs: [], controller: controller);
  }
};

final List<AppBar> Function(BuildContext, WidgetRef, TabController) appBarList =
    (context, ref, tabController) => [
          AppBar(
            title: const Text("見つける"),
            shape: appBarShape,
            actions: [
              IconButton(
                  onPressed: () {
                    final currentWebsite = ref.read(searchingWebsiteProvider).state;
                    Navigator.of(context).push(MaterialPageRoute(
                         settings: const RouteSettings(name: "add_snack"),
                        builder: (context) {
                           final provider = ref.watch(addSnackProvider);
                           provider.updateUrl(currentWebsite);
                           return AddSnackPage();
                        }));
                  },
                  icon: Icon(Icons.add))
            ],
          ),
          AppBar(
            title: const Text("一覧"),
            shape: appBarShape,
            bottom: tabBar(context, ref, tabController),
          ),
          AppBar(
            title: const Text("ユーザー"),
            shape: appBarShape,
            actions: [],
          ),
        ];

class HomePage extends HookConsumerWidget {
  late TabController _tabController;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    int appBarIndex = ref.watch(appBarIndexProvider).state;
    _tabController = useTabController(initialLength: 3);

    return Scaffold(
      appBar: appBarList(context, ref, _tabController)[appBarIndex],
      bottomNavigationBar: BottomNavigationBar(
        selectedItemColor: Theme.of(context).primaryColor,
        items: const [
          BottomNavigationBarItem(icon: Icon(Icons.search), label: "検索"),
          BottomNavigationBarItem(icon: Icon(Icons.list), label: "一覧"),
          BottomNavigationBarItem(icon: Icon(Icons.person), label: "ユーザー"),
        ],
        currentIndex: appBarIndex,
        onTap: (index) {
          ref.watch(appBarIndexProvider).state = index;
        },
      ),
      body: DefaultTabController(
        child: buildBody(ref),
        length: 3,
      ),
    );
  }

  Widget buildBody(WidgetRef ref) {
    final index = ref.watch(appBarIndexProvider).state;
    if (index == 0) {
      return SearchPage();
    } else if (index == 1) {
      return ListPage(tabController: _tabController);
    } else {
      return Container();
    }
  }
}
