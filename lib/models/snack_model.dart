import 'package:snacker/models/snack_tag_model.dart';

class SnackModel {
  int? id;

  String title;

  String url;

  String? thumbnailUrl;

  int priority;

  bool isArchived;

  List<SnackTagModel> tagList;

  SnackModel(
      {this.id,
      required this.title,
      required this.url,
      this.thumbnailUrl,
      required this.priority,
      required this.isArchived,
      required this.tagList});
}
