import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/ui/components/snack_webview.dart';
import 'package:snacker/ui/providers/detail_snack_provider.dart';
import 'package:snacker/ui/providers/search_website_provider.dart';
import 'package:snacker/ui/providers/update_snack_usecase_provider.dart';
import 'package:url_launcher/url_launcher.dart';

class DetailSnackPage extends HookConsumerWidget {
  const DetailSnackPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final updateSnackUseCase = ref.watch(updateSnackUseCaseProvider);

    final snack = ref.watch(detailSnackProvider).state;
    final currentWebsite = ref.watch(detailPageWebsiteProvider).state;

    if (snack == null) {
      return Container();
    }

    return Scaffold(
      body: buildBody(context, ref),
      appBar: AppBar(
        title: Text(Uri.parse(snack.url).host),
        actions: [
          IconButton(
              onPressed: () {
                launch(currentWebsite);
              },
              icon: Icon(Icons.open_in_browser_rounded)),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        label: buildFABLabel(snack: snack),
        icon: buildFABIcon(snack: snack),
        onPressed: () async {
          snack.isArchived = !snack.isArchived;
          try {
            await updateSnackUseCase.execute(newSnack: snack);
            ref.read(detailSnackProvider).state = snack;
          } catch (e) {
            assert(false, e.toString());
            ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("エラーが発生しました")));
          }
        },
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
    );
  }

  Widget buildFABIcon({ required Snack snack }) {
    final isArchived = snack.isArchived;
    if (isArchived) {
      return Icon(Icons.check_box);
    } else {
      return Icon(Icons.check_box_outline_blank);
    }
  }

  Widget buildFABLabel({ required Snack snack }) {
    final isArchived = snack.isArchived;
    if (isArchived) {
      return Text("アーカイブ済み");
    } else {
      return Text("未読");
    }
  }

  Widget buildBody(BuildContext context, WidgetRef ref) {

    final website = ref.watch(detailPageWebsiteProvider).state;

    return Stack(
      children: [
        SnackWebView(
          website: website,
          onChangeWebsite: (url) {
            ref.read(detailPageWebsiteProvider).state = url;
          },
        )
      ],
    );
  }
}
