import 'package:json_annotation/json_annotation.dart';

part 'snack_tag_model.g.dart';

@JsonSerializable()
class SnackTagModel {
  int? id;
  String name;

  SnackTagModel({this.id, required this.name});

  factory SnackTagModel.fromJson(Map<String, dynamic> json) =>
      _$SnackTagModelFromJson(json);

  Map<String, dynamic> get json => _$SnackTagModelToJson(this);
}
