import 'package:flutter/cupertino.dart';

class Snack {
  late int? id;

  late String title;

  late String url;

  late String? thumbnailUrl;

  late int priority;

  late bool isArchived;

  static const tableName = "snacks";

  Snack(
      {this.id = null,
      required this.title,
      required this.url,
      this.thumbnailUrl,
      required this.priority,
      required this.isArchived});

  Map<String, dynamic> toMap() => {
        "id": id,
        "title": title,
        "url": url,
        "thumbnail_url": thumbnailUrl,
        "priority": priority,
        "is_archived": isArchived ? 1 : 0
      };

  Snack.fromMap({required Map<String, dynamic> map}) {
    id = map["id"] as int;
    title = map["title"] as String;
    thumbnailUrl = map["thumbnail_url"] as String? ?? "";
    priority = map["priority"] as int;
    isArchived = map["is_archived"] == 1 ? true : false;
  }
}
