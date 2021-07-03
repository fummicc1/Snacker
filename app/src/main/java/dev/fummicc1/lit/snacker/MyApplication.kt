package dev.fummicc1.lit.snacker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dev.fummicc1.lit.snacker.databases.SnackDatabase

class MyApplication: Application() {

    lateinit var snackDatabase: SnackDatabase
    lateinit var prefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        prefs = getSharedPreferences("snacker_shared_preferences", Context.MODE_PRIVATE)
        snackDatabase = SnackDatabase.getDatabase(this)
    }
}