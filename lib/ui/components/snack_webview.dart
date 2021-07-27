import 'dart:async';

import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/entities/snack.dart';
import 'package:snacker/ui/providers/search_website_provider.dart';
import 'package:webview_flutter/webview_flutter.dart';

class SnackWebView extends HookConsumerWidget {
  SnackWebView(
      {Key? key,
      Completer<WebViewController>? completer,
      required this.websiteProvider})
      : _completer = completer ?? Completer(),
        super(key: key);

  final Completer<WebViewController> _completer;
  final StateProvider<String> websiteProvider;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final currentWebsite = ref.watch(websiteProvider).state;

    return WebView(
      initialUrl: currentWebsite,
      onWebViewCreated: onWebViewCreated,
      javascriptMode: JavascriptMode.unrestricted,
      onPageFinished: (url) {
        ref.read(websiteProvider).state = url;
      },
      onProgress: (progress) {
        ref.read(websiteLoadingProgressProvider).state = progress;
      },
      onWebResourceError: (error) {
        print(error.description);
      },
    );
  }

  onWebViewCreated(WebViewController webViewController) {
    _completer.complete(webViewController);
  }
}
