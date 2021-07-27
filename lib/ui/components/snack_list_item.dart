import 'package:flutter/material.dart';
import 'package:flutter/src/widgets/framework.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/ui/pages/detail_snack_page.dart';
import 'package:snacker/ui/providers/detail_snack_provider.dart';

class SnackListItem extends HookConsumerWidget {
  const SnackListItem({Key? key, required this.snack}) : super(key: key);

  final Snack snack;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Card(
        child: ListTile(
      title: Text(snack.title),
      onTap: () {
        ref.read(detailPageWebsiteProvider).state = snack.url;
        ref.read(detailSnackProvider).state = snack;
        final page = DetailSnackPage();
        final route = MaterialPageRoute(
            builder: (context) => page,
            settings: const RouteSettings(name: "/detail_snack"));
        Navigator.of(context).push(route);
      },
    ));
  }
}
