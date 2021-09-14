import 'package:flutter/material.dart';

class ListEmptyStateView extends StatelessWidget {
  const ListEmptyStateView({Key? key, required this.message}) : super(key: key);

  final String message;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Center(
        child: Column(
          children: [
            const Spacer(),
            Text(message, style: Theme.of(context).textTheme.bodyText1),
            const SizedBox(
              height: 32,
            ),
            const SizedBox(
              height: 48,
              width: 48,
              child: Image(image: AssetImage("assets/images/icon.png")),
            ),
            const Spacer(),
          ],
        ),
      ),
    );
  }
}
