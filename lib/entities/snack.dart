import 'package:realm/realm.dart';

part 'snack.g.dart';

class _Snack {
  @RealmProperty(primaryKey: true)
  late int id;

  @RealmProperty()
  late String title;

  @RealmProperty()
  late String url;

  @RealmProperty()
  late String? thumbnailUrl;

  @RealmProperty()
  late int? priority;

  @RealmProperty()
  late DateTime? createdAt;
}