import 'package:flutter/material.dart';
import 'package:flutter/src/widgets/framework.dart';
import 'package:snacker/entities/snack.dart';

class SnackListItem extends StatelessWidget {
  const SnackListItem({Key? key, required this.snack}) : super(key: key);

  final Snack snack;

  @override
  Widget build(BuildContext context) {
    return Card(
      child: ListTile(
        title: Text(snack.title),
      )
    );
  }
}
