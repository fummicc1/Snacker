package dev.fummicc1.lit.snacker.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import dev.fummicc1.lit.snacker.databinding.ActivityManageTagBinding
import dev.fummicc1.lit.snacker.databinding.ActivityManageTagBindingImpl

class ManageTagActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageTagBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageTagBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setSupportActionBar(manageTagToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            title = "表示タグの管理"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}