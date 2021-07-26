import 'package:hooks_riverpod/hooks_riverpod.dart';

final searchingWebsiteProvider = StateProvider((_) => "https://google.com");

final websiteLoadingProgressProvider = StateProvider<int>((_) => 0);