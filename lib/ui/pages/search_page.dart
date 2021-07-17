import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:webview_flutter/webview_flutter.dart';

final searchingWebsite = StateProvider((_) => "https://google.com");

class SearchPage extends HookConsumerWidget {
  const SearchPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {

    final currentWebsite = ref.watch(searchingWebsite).state;

    return WebView(
      initialUrl: currentWebsite,
    );
  }
}
