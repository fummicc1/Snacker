class SnackTagKind {
  int? id;
  String name;
  bool isActive;

  static const tableName = "snack_tag_kinds";

  SnackTagKind({this.id, required this.name, required this.isActive});

  Map<String, dynamic> toMap() =>
      {"id": id, "name": name, "is_active": isActive ? 1 : 0};

  factory SnackTagKind.fromMap({required Map<String, dynamic> map}) {
    var id = map["id"] as int;
    var name = map["name"] as String;
    var isActiveNum = map["is_active"] as int;
    var isActive = isActiveNum == 1;
    return SnackTagKind(id: id, name: name, isActive: isActive);
  }
}
