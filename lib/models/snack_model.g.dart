// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'snack_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

SnackModel _$SnackModelFromJson(Map<String, dynamic> json) => $checkedCreate(
      'SnackModel',
      json,
      ($checkedConvert) {
        final val = SnackModel(
          id: $checkedConvert('id', (v) => v as int?),
          title: $checkedConvert('title', (v) => v as String),
          url: $checkedConvert('url', (v) => v as String),
          thumbnailUrl: $checkedConvert('thumbnail_url', (v) => v as String?),
          priority: $checkedConvert('priority', (v) => v as int),
          isArchived: $checkedConvert('is_archived', (v) => v as bool),
          tagList: $checkedConvert(
              'tag_list',
              (v) => (v as List<dynamic>)
                  .map((e) => SnackTagModel.fromJson(e as Map<String, dynamic>))
                  .toList()),
        );
        return val;
      },
      fieldKeyMap: const {
        'thumbnailUrl': 'thumbnail_url',
        'isArchived': 'is_archived',
        'tagList': 'tag_list'
      },
    );

Map<String, dynamic> _$SnackModelToJson(SnackModel instance) =>
    <String, dynamic>{
      'id': instance.id,
      'title': instance.title,
      'url': instance.url,
      'thumbnail_url': instance.thumbnailUrl,
      'priority': instance.priority,
      'is_archived': instance.isArchived,
      'tag_list': instance.tagList,
    };
