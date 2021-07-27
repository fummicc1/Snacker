import 'dart:async';

import 'package:flutter/material.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/ui/providers/search_website_provider.dart';
import 'package:webview_flutter/webview_flutter.dart';

class SnackWebView extends HookConsumerWidget {
  SnackWebView(
      {Key? key,
      Completer<WebViewController>? completer,
      required this.website,
      required this.onChangeWebsite})
      : _completer = completer ?? Completer(),
        super(key: key);

  final Completer<WebViewController> _completer;
  final Function(String) onChangeWebsite;
  String website;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return WebView(
      initialUrl: website,
      onWebViewCreated: onWebViewCreated,
      javascriptMode: JavascriptMode.unrestricted,
      onPageFinished: (url) {
        website = url;
        onChangeWebsite(url);
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
