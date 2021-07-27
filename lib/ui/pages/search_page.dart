import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import 'package:snacker/ui/components/snack_webview.dart';
import 'package:snacker/ui/providers/search_website_provider.dart';
import 'package:webview_flutter/webview_flutter.dart';

class SearchPage extends HookConsumerWidget {
  SearchPage({Key? key}) : super(key: key);

  final Completer<WebViewController> _completer = Completer();

  @override
  Widget build(BuildContext context, WidgetRef ref) {

    return Stack(
      children: [
        SnackWebView(completer: _completer, websiteProvider: searchingWebsiteProvider,),
        Positioned(
          top: 0,
          left: 0,
          right: 0,
          height: 4,
          child: buildLoadingWidget(context, ref),
        ),
        Positioned(
          bottom: 32,
          left: 24,
          width: 56,
          height: 56,
          child: buildBackButton(context, ref),
        )
      ],
    );
  }

  Widget buildLoadingWidget(BuildContext context, WidgetRef ref) {
    final currentLoadingProgress =
        ref.watch(websiteLoadingProgressProvider).state;

    if (currentLoadingProgress == 0 || currentLoadingProgress == 100) {
      return Container();
    }
    return LinearProgressIndicator(
      value: currentLoadingProgress.toDouble() / 100,
    );
  }

  Widget buildBackButton(BuildContext context, WidgetRef ref) {
    return FutureBuilder<WebViewController>(
        future: _completer.future,
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            return FutureBuilder<bool>(
                future: snapshot.requireData.canGoBack(),
                builder: (context, canGoBackSnapshot) {

                  if (canGoBackSnapshot.hasData && canGoBackSnapshot.requireData) {
                    return Container(
                      decoration: BoxDecoration(
                          color: Theme.of(context).scaffoldBackgroundColor,
                          shape: BoxShape.circle),
                      child: IconButton(
                        icon: Icon(Icons.arrow_back_ios_new),
                        color: Theme.of(context).primaryColor,
                        onPressed: () async {
                          final canBack = canGoBackSnapshot.data ?? false;
                          if (canBack) {
                            await snapshot.data?.goBack();
                          }
                        },
                      ),
                    );
                  }
                  return Container();
                });
          }
          return Container();
        });
  }
}
