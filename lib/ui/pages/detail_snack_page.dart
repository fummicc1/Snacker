import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/models/snack_model.dart';
import 'package:snacker/ui/components/snack_webview.dart';
import 'package:snacker/ui/providers/detail_snack_provider.dart';
import 'package:snacker/ui/providers/should_show_fab_provider.dart';
import 'package:snacker/ui/providers/update_snack_usecase_provider.dart';
import 'package:url_launcher/url_launcher.dart';

class DetailSnackPage extends HookConsumerWidget {
  const DetailSnackPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final updateSnackUseCase = ref.watch(updateSnackUseCaseProvider);
    final shouldShowFAB = ref.watch(shouldShowFABProvider).state;

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
                ref.read(shouldShowFABProvider).state = !shouldShowFAB;
              },
              icon: Icon(
                Icons.remove_red_eye_rounded,
                color: Colors.white.withAlpha(shouldShowFAB ? 255 : 100),
              )),
          IconButton(
              onPressed: () {
                launch(currentWebsite);
              },
              icon: const Icon(Icons.open_in_browser_rounded)),
        ],
      ),
      floatingActionButton: shouldShowFAB
          ? FloatingActionButton.extended(
              label: buildFABLabel(snack: snack),
              icon: buildFABIcon(snack: snack),
              onPressed: () async {
                snack.isArchived = !snack.isArchived;
                try {
                  await updateSnackUseCase.execute(newSnack: snack);
                  ref.read(detailSnackProvider).state = snack;
                } catch (e) {
                  assert(false, e.toString());
                  ScaffoldMessenger.of(context)
                      .showSnackBar(const SnackBar(content: Text("??????????????????????????????")));
                }
              },
            )
          : null,
      floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
    );
  }

  Widget buildFABIcon({required SnackModel snack}) {
    final isArchived = snack.isArchived;
    if (isArchived) {
      return const Icon(Icons.check_box);
    } else {
      return const Icon(Icons.check_box_outline_blank);
    }
  }

  Widget buildFABLabel({required SnackModel snack}) {
    final isArchived = snack.isArchived;
    if (isArchived) {
      return const Text("?????????????????????");
    } else {
      return const Text("??????");
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
        ),
      ],
    );
  }
}
