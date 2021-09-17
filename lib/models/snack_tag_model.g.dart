// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'snack_tag_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

SnackTagModel _$SnackTagModelFromJson(Map<String, dynamic> json) =>
    $checkedCreate(
      'SnackTagModel',
      json,
      ($checkedConvert) {
        final val = SnackTagModel(
          id: $checkedConvert('id', (v) => v as int?),
          name: $checkedConvert('name', (v) => v as String),
        );
        return val;
      },
    );

Map<String, dynamic> _$SnackTagModelToJson(SnackTagModel instance) =>
    <String, dynamic>{
      'id': instance.id,
      'name': instance.name,
    };
