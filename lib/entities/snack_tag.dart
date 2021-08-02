import 'package:snacker/entities/snack.dart';
import 'package:snacker/models/snack_tag_model.dart';

class SnackTag {
  int? id;
  int snackId;
  int tagId;

  static const tableName = "snack_tags";

  SnackTag({this.id, required this.snackId, required this.tagId});

  Map<String, dynamic> toMap() =>
      {"id": id, "snack_id": snackId, "tag_id": tagId};

  factory SnackTag.fromMap({required Map<String, dynamic> map}) {
    var id = map["id"] as int;
    var snackId = map["snack_id"] as int;
    var tagId = map["tag_id"] as int;
    return SnackTag(id: id, snackId: snackId, tagId: tagId);
  }
}
