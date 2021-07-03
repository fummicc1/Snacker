package dev.fummicc1.lit.snacker.activities

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.databinding.ActivityReadSnackBinding
import dev.fummicc1.lit.snacker.fragments.ReadSnackFragment
import dev.fummicc1.lit.snacker.interfaces.IOnBackPressed
import dev.fummicc1.lit.snacker.interfaces.IWebLoadingProgressIncidactor

class ReadSnackActivity : AppCompatActivity(), IWebLoadingProgressIncidactor {

    private lateinit var binding: ActivityReadSnackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadSnackBinding.inflate(layoutInflater)
        binding.apply {
            setSupportActionBar(readSnackToolbar)
        }
        setContentView(binding.root)

        configureReadSnackFragmentHandlerIfNeeded()
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.fragments.size

        if (count > 0) {
            binding.apply {
                val fragment = supportFragmentManager.findFragmentById(R.id.readSnackFragmentContainer) as? IOnBackPressed
                fragment?.onBackPressed()
            }
        }
        super.onBackPressed()
    }

    override fun onComplete() {
        binding.apply {
            readSnackProgressBar.setProgress(1, true)
            ObjectAnimator.ofFloat(readSnackProgressBar, "alpha", 1f, 0f).apply {
                duration = 1000
                start()
            }
        }
    }

    override fun onProgress(progress: Int) {
        binding.apply {
            readSnackProgressBar.setProgress(progress, true)
            readSnackProgressBar.visibility = View.VISIBLE
            readSnackProgressBar.alpha = 1f
        }
    }

    private fun configureReadSnackFragmentHandlerIfNeeded() {
        binding.apply {
            val readSnackFragment = supportFragmentManager.fragments.get(0) as ReadSnackFragment
            if (readSnackFragment.handler == null) {
                readSnackFragment.handler = object : ReadSnackFragment.Handler {
                    override fun updateToolbarVisibility(shouldShow: Boolean) {
                        supportActionBar?.apply {
                            if (shouldShow) {
                                show()
                            } else {
                                hide()
                            }
                        }
                    }
                }
            }
        }
    }
}