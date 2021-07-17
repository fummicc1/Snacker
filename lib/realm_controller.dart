import 'package:realm/realm.dart';

import 'entities/snack.dart';

mixin Database {

}

class RealmController {

  late Realm _realm;

  RealmController() {
    _configureRealm();
  }

  _configureRealm() {
    final config = Configuration();
    config.schema.add(Snack);
    _realm = Realm(config);
  }

  List<Item> read<Item extends RealmObject>() {
    final items =  _realm.objects<Item>();
    return items.asList();
  }
}