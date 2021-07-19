import 'package:flutter/cupertino.dart';

class Snack {
  late int? id;

  late String title;

  late String url;

  late String? thumbnailUrl;

  late int priority;

  static const tableName = "snacks";

  Snack(
      {this.id = null,
      required this.title,
      required this.url,
      this.thumbnailUrl,
      required this.priority});

  Map<String, dynamic> toMap() => {
        "id": id,
        "title": title,
        "url": url,
        "thumbnail_url": thumbnailUrl,
        "priority": priority,
      };

  Snack.fromMap({required Map<String, dynamic> map}) {
    id = map["id"] as int;
    title = map["title"] as String;
    thumbnailUrl = map["thumbnail_url"] as String? ?? "";
    priority = map["priority"] as int;
  }
}
