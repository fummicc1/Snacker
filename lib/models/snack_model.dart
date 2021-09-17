import 'package:snacker/models/snack_tag_model.dart';
import 'package:json_annotation/json_annotation.dart';

part 'snack_model.g.dart';

@JsonSerializable()
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

  factory SnackModel.fromJson(Map<String, dynamic> json) =>
      _$SnackModelFromJson(json);

  Map<String, dynamic> get json => _$SnackModelToJson(this);
}
