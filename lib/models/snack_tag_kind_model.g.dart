// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'snack_tag_kind_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

SnackTagKindModel _$SnackTagKindModelFromJson(Map<String, dynamic> json) =>
    $checkedCreate(
      'SnackTagKindModel',
      json,
      ($checkedConvert) {
        final val = SnackTagKindModel(
          id: $checkedConvert('id', (v) => v as int?),
          name: $checkedConvert('name', (v) => v as String),
          isActive: $checkedConvert('is_active', (v) => v as bool),
        );
        return val;
      },
      fieldKeyMap: const {'isActive': 'is_active'},
    );

Map<String, dynamic> _$SnackTagKindModelToJson(SnackTagKindModel instance) =>
    <String, dynamic>{
      'id': instance.id,
      'name': instance.name,
      'is_active': instance.isActive,
    };
