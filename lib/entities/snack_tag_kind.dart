import 'package:json_annotation/json_annotation.dart';
part 'snack_tag_kind.g.dart';

@JsonSerializable()
class SnackTagKind {
  int? id;
  String name;
  bool isActive;

  static const tableName = "snack_tag_kinds";

  SnackTagKind({this.id, required this.name, required this.isActive});

  Map<String, dynamic> toMap() => _$SnackTagKindToJson(this);

  factory SnackTagKind.fromMap({required Map<String, dynamic> map}) => _$SnackTagKindFromJson(map);
}
