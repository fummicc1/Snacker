// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'snack_tag_kind.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

SnackTagKind _$SnackTagKindFromJson(Map<String, dynamic> json) =>
    $checkedCreate(
      'SnackTagKind',
      json,
      ($checkedConvert) {
        final val = SnackTagKind(
          id: $checkedConvert('id', (v) => v as int?),
          name: $checkedConvert('name', (v) => v as String),
          isActive: $checkedConvert('is_active', (v) => v as bool),
        );
        return val;
      },
      fieldKeyMap: const {'isActive': 'is_active'},
    );

Map<String, dynamic> _$SnackTagKindToJson(SnackTagKind instance) =>
    <String, dynamic>{
      'id': instance.id,
      'name': instance.name,
      'is_active': instance.isActive,
    };
