package dev.fummicc1.lit.snacker.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.fummicc1.lit.snacker.databases.snack.SnackDao
import dev.fummicc1.lit.snacker.databases.snack_history.SnackHistoryDao
import dev.fummicc1.lit.snacker.databases.snack_tag.SnackTagDao
import dev.fummicc1.lit.snacker.databases.snack_tag_kind.SnackTagKindDao
import dev.fummicc1.lit.snacker.databases.stock_history.StockHistoryDao
import dev.fummicc1.lit.snacker.entities.*

@Database(
    entities = arrayOf(
        Snack::class,
        SnackTag::class,
        SnackTagKind::class,
        StockHistory::class,
        SnackHistory::class
    ), version = 13
)
@TypeConverters(Converters::class)
abstract class SnackDatabase : RoomDatabase() {

    abstract fun snackDao(): SnackDao

    abstract fun snackTagDao(): SnackTagDao

    abstract fun snackTagKindDao(): SnackTagKindDao

    abstract fun stockHistoryDao(): StockHistoryDao

    abstract fun snackHistoryDao(): SnackHistoryDao

    companion object {
        @Volatile
        private var _instance: SnackDatabase? = null

        fun getDatabase(context: Context): SnackDatabase {
            return _instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SnackDatabase::class.java,
                    "snack_database"
                )
                    .addMigrations(MIGRATION_9_10)
                    .addMigrations(MIGRATION_10_11)
                    .addMigrations(MIGRATION_11_12)
                    .addMigrations(MIGRATION_12_13)
                    .build()
                _instance = instance
                instance
            }
        }

        val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `Snack` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `url` TEXT NOT NULL, `title` TEXT NOT NULL, `priority` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, `status` TEXT)")
                database.execSQL("ALTER TABLE `Snack` ADD thumbnail_url TEXT")
            }
        }

        val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `snack_tag_kind` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `is_active` INTEGER NOT NULL)")
                database.execSQL("ALTER TABLE `snack_tag_kind` ADD is_active INTEGER DEFAULT 1")
            }
        }

        val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `snack_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `url` TEXT NOT NULL, `thumbnail_url` TEXT, `title` TEXT NOT NULL, `viewed_at` INTEGER NOT NULL)")
            }
        }

        val MIGRATION_12_13 = object : Migration(12, 13) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `snack_tag_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `snack_id` INTEGER NOT NULL, `tag_id` INTEGER NOT NULL, FOREIGN KEY(`snack_id`) REFERENCES `Snack`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
                database.execSQL("INSERT INTO `snack_tag_new` (id, snack_id, tag_id) SELECT id, snack_id, tag_id FROM `snack_tag`")
                database.execSQL("DROP TABLE `snack_tag`")
                database.execSQL("ALTER TABLE `snack_tag_new` RENAME TO `snack_tag`")
            }
        }
    }
}