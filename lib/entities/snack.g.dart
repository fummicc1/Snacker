// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'snack.dart';

// **************************************************************************
// RealmObjectGenerator
// **************************************************************************

class Snack extends RealmObject {
  // ignore_for_file: unused_element, unused_local_variable
  Snack._constructor() : super.constructor();
  Snack();

  @RealmProperty(primaryKey: true)
  int get id => super['id'] as int;
  set id(int value) => super['id'] = value;

  @RealmProperty()
  String get title => super['title'] as String;
  set title(String value) => super['title'] = value;

  @RealmProperty()
  String get url => super['url'] as String;
  set url(String value) => super['url'] = value;

  @RealmProperty()
  String get thumbnailUrl => super['thumbnailUrl'] as String;
  set thumbnailUrl(String value) => super['thumbnailUrl'] = value;

  @RealmProperty()
  int get priority => super['priority'] as int;
  set priority(int value) => super['priority'] = value;

  @RealmProperty()
  DateTime get createdAt => super['createdAt'] as DateTime;
  set createdAt(DateTime value) => super['createdAt'] = value;

  static dynamic getSchema() {
    const dynamic type = _Snack;
    return RealmObject.getSchema('Snack', [
      SchemaProperty('id', type: 'int', primaryKey: true),
      SchemaProperty('title', type: 'string'),
      SchemaProperty('url', type: 'string'),
      SchemaProperty('thumbnailUrl', type: 'string'),
      SchemaProperty('priority', type: 'int'),
      SchemaProperty('createdAt', type: 'DateTime'),
    ]);
  }
}
