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
import 'package:snacker/ui/providers/tab_controller_index_provider.dart';
import 'package:url_launcher/url_launcher.dart';

const appBarShape = RoundedRectangleBorder(
    borderRadius: BorderRadius.vertical(bottom: Radius.circular(8)));

tabBar(context, ref, controller) {
  final appBarIndex = ref.watch(appBarIndexProvider).state;
  if (appBarIndex == 1) {
    return TabBar(
      tabs: const [
        Tab(text: "未読"),
        Tab(text: "読了済み"),
      ],
      labelColor: Colors.white,
      unselectedLabelColor: Theme.of(context).primaryColorLight,
      controller: controller,
    );
  } else {
    return TabBar(tabs: const [], controller: controller);
  }
}

appBarList(context, ref, tabController) => [
      AppBar(
        title: const Text("見つける"),
        shape: appBarShape,
        actions: [
          IconButton(
              onPressed: () async {
                final currentWebsite = ref.read(searchingWebsiteProvider).state;
                final provider = ref.watch(addSnackProvider);
                await provider
                    .updateUrl(currentWebsite, shouldScraping: true)
                    .catchError((_) => "");
                Navigator.of(context).push(MaterialPageRoute(
                    settings: const RouteSettings(name: "add_snack"),
                    builder: (context) {
                      return const AddSnackPage();
                    }));
              },
              icon: const Icon(Icons.add)),
          IconButton(
            onPressed: () {
              final currentWebsite = ref.read(searchingWebsiteProvider).state;
              launch(currentWebsite);
            },
            icon: const Icon(Icons.open_in_browser_rounded),
          ),
        ],
      ),
      AppBar(
        title: const Text("一覧"),
        shape: appBarShape,
        bottom: tabBar(context, ref, tabController),
      ),
    ];

class HomePage extends HookConsumerWidget {
  late TabController _tabController;

  HomePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    int appBarIndex = ref.watch(appBarIndexProvider).state;

    _tabController = useTabController(initialLength: 2);

    if (!_tabController.hasListeners) {
      _tabController.addListener(() {
        ref.read(tabControllerIndexProvider).state = _tabController.index;
      });
    }

    return Scaffold(
      appBar: appBarList(context, ref, _tabController)[appBarIndex],
      bottomNavigationBar: BottomNavigationBar(
        selectedItemColor: Theme.of(context).primaryColor,
        items: const [
          BottomNavigationBarItem(icon: Icon(Icons.search), label: "検索"),
          BottomNavigationBarItem(icon: Icon(Icons.list), label: "一覧"),
        ],
        currentIndex: appBarIndex,
        onTap: (index) {
          ref.read(appBarIndexProvider).state = index;
        },
      ),
      body: DefaultTabController(
        child: buildBody(ref),
        length: 2,
      ),
    );
  }

  Widget buildBody(WidgetRef ref) {
    final index = ref.watch(appBarIndexProvider).state;
    if (index == 0) {
      return SearchPage();
    } else if (index == 1) {
      return const ListPage();
    } else {
      return Container();
    }
  }
}
