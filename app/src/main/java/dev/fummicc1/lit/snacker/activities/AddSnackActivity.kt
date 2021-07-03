package dev.fummicc1.lit.snacker.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.databinding.ActivityAddSnackBinding
import dev.fummicc1.lit.snacker.fragments.AddSnackFragment
import dev.fummicc1.lit.snacker.models.AddSnackPreset

class AddSnackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSnackBinding

    private var isLaunchedFromOtherApp = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSnackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setSupportActionBar(addSnackToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        title = "記事の登録"

        // Memorize where this@AddSnackActivity came.
        isLaunchedFromOtherApp = intent.action == Intent.ACTION_SEND
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_snack_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home && isLaunchedFromOtherApp) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            return true
        }
        return false
    }

    companion object {
        val addSnackPresetField: String = "add_snack_preset_field"
    }
}