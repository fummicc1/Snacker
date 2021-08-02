import 'package:snacker/entities/snack.dart';
import 'package:snacker/entities/snack_tag.dart';
import 'package:snacker/entities/snack_tag_kind.dart';

class FakeStore {
  List<Snack> snackList = [];
  List<SnackTag> snackTagList = [];
  List<SnackTagKind> snackTagKindList = [];
}