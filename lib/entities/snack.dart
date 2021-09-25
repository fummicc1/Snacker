import 'package:json_annotation/json_annotation.dart';
part 'snack.g.dart';

@JsonSerializable()
class Snack {
  String? id;
  String title;
  String url;
  String? thumbnailUrl;
  int priority;
  bool isArchived;

  static const collectionName = "snacks";

  Snack(
      {this.id,
      required this.title,
      required this.url,
      this.thumbnailUrl,
      required this.priority,
      required this.isArchived});

  Map<String, dynamic> toMap() => _$SnackToJson(this);

  factory Snack.fromMap({required Map<String, dynamic> map}) => _$SnackFromJson(map);
}
