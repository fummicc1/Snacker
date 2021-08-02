import 'package:flutter/cupertino.dart';
import 'package:snacker/models/snack_model.dart';

class Snack {
  int? id;

  String title;

  String url;

  String? thumbnailUrl;

  int priority;

  bool isArchived;

  static const tableName = "snacks";

  Snack(
      {this.id,
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

  factory Snack.fromMap({required Map<String, dynamic> map}) {
    var id = map["id"] as int;
    var title = map["title"] as String;
    var url = map["url"] as String;
    var thumbnailUrl = map["thumbnail_url"] as String? ?? "";
    var priority = map["priority"] as int;
    var isArchived = map["is_archived"] == 1 ? true : false;
    return Snack(
        id: id,
        title: title,
        url: url,
        thumbnailUrl: thumbnailUrl,
        priority: priority,
        isArchived: isArchived);
  }

  factory Snack.fromModel({required SnackModel model}) {
    return Snack(
        id: model.id,
        title: model.title,
        url: model.url,
        thumbnailUrl: model.thumbnailUrl,
        priority: model.priority,
        isArchived: model.isArchived);
  }
}
