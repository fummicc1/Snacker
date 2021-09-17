// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'snack.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Snack _$SnackFromJson(Map<String, dynamic> json) => $checkedCreate(
      'Snack',
      json,
      ($checkedConvert) {
        final val = Snack(
          id: $checkedConvert('id', (v) => v as int?),
          title: $checkedConvert('title', (v) => v as String),
          url: $checkedConvert('url', (v) => v as String),
          thumbnailUrl: $checkedConvert('thumbnail_url', (v) => v as String?),
          priority: $checkedConvert('priority', (v) => v as int),
          isArchived: $checkedConvert('is_archived', (v) => v as bool),
        );
        return val;
      },
      fieldKeyMap: const {
        'thumbnailUrl': 'thumbnail_url',
        'isArchived': 'is_archived'
      },
    );

Map<String, dynamic> _$SnackToJson(Snack instance) => <String, dynamic>{
      'id': instance.id,
      'title': instance.title,
      'url': instance.url,
      'thumbnail_url': instance.thumbnailUrl,
      'priority': instance.priority,
      'is_archived': instance.isArchived,
    };
