import 'package:json_annotation/json_annotation.dart';
part 'snack_tag.g.dart';

@JsonSerializable()
class SnackTag {
  int? id;
  int snackId;
  int tagId;

  static const tableName = "snack_tags";

  SnackTag({this.id, required this.snackId, required this.tagId});

  Map<String, dynamic> toMap() => _$SnackTagToJson(this);

  factory SnackTag.fromMap({required Map<String, dynamic> map}) => _$SnackTagFromJson(map);
}
