// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'snack_tag.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

SnackTag _$SnackTagFromJson(Map<String, dynamic> json) => $checkedCreate(
      'SnackTag',
      json,
      ($checkedConvert) {
        final val = SnackTag(
          id: $checkedConvert('id', (v) => v as int?),
          snackId: $checkedConvert('snack_id', (v) => v as int),
          tagId: $checkedConvert('tag_id', (v) => v as int),
        );
        return val;
      },
      fieldKeyMap: const {'snackId': 'snack_id', 'tagId': 'tag_id'},
    );

Map<String, dynamic> _$SnackTagToJson(SnackTag instance) => <String, dynamic>{
      'id': instance.id,
      'snack_id': instance.snackId,
      'tag_id': instance.tagId,
    };
