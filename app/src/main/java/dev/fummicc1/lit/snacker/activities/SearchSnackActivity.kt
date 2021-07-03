package dev.fummicc1.lit.snacker.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.databinding.ActivitySearchSnackBinding


class SearchSnackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchSnackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            setSupportActionBar(searchSnackToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        title = "全ての記事から探す"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}