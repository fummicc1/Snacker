import 'package:json_annotation/json_annotation.dart';

part 'snack_tag_kind_model.g.dart';

@JsonSerializable()
class SnackTagKindModel {
  int? id;
  String name;
  bool isActive;

  SnackTagKindModel({this.id, required this.name, required this.isActive});

  factory SnackTagKindModel.fromJson(Map<String, dynamic> json) =>
      _$SnackTagKindModelFromJson(json);

  Map<String, dynamic> get json => _$SnackTagKindModelToJson(this);
}
