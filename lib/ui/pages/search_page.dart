import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/ui/providers/search_website_provider.dart';
import 'package:webview_flutter/webview_flutter.dart';

class SearchPage extends HookConsumerWidget {
  const SearchPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {

    final currentWebsite = ref.watch(searchingWebsiteProvider).state;

    return WebView(
      initialUrl: currentWebsite,
      onPageFinished: (url) {
        ref.read(searchingWebsiteProvider).state = url;
      },
    );
  }
}
