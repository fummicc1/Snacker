package dev.fummicc1.lit.snacker.activities

import android.animation.ObjectAnimator
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.databinding.ActivityMainBinding
import dev.fummicc1.lit.snacker.fragments.BrowserFragment
import dev.fummicc1.lit.snacker.fragments.ProfileFragment
import dev.fummicc1.lit.snacker.fragments.SearchSnackFragment
import dev.fummicc1.lit.snacker.fragments.SnackListFragment
import dev.fummicc1.lit.snacker.interfaces.IOnBackPressed
import dev.fummicc1.lit.snacker.interfaces.IWebLoadingProgressIncidactor
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_browser.*

class MainActivity : AppCompatActivity(), IWebLoadingProgressIncidactor {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            setSupportActionBar(mainToolbar)

            mainViewPager.adapter = ScreenSlidePageAdapter(this@MainActivity)

            bottomNavigationView.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.browse_snack_bottom_icon -> {
                        mainViewPager.setCurrentItem(0, false)
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.snack_list_button_icon -> {
                        mainViewPager.setCurrentItem(1, false)
                        showToolbar()
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.profile_button_icon -> {
                        mainViewPager.setCurrentItem(2, false)
                        showToolbar()
                        return@setOnNavigationItemSelectedListener true
                    }
                }
                false
            }
            mainViewPager.isUserInputEnabled = false
        }

        val prefs = getSharedPreferences("snaker_prefs", MODE_PRIVATE)
        val shouldShowWalkthrough = prefs.getBoolean("show_walkthrough", true)
        if (!shouldShowWalkthrough) return
        val intent = Intent(this, AppIntroActivity::class.java)
        startActivity(intent)
    }

    private fun configureBrowserFragmentHandlerIfNeeded() {
        binding.apply {
            val browserFragment = supportFragmentManager.fragments.get(0) as BrowserFragment
            if (browserFragment.handler == null) {

                browserFragment.handler = object : BrowserFragment.Handler {
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

    override fun onBackPressed() {
        val count = supportFragmentManager.fragments.size

        if (count > 0) {
            binding.apply {
                when (mainViewPager.currentItem) {
                    0 -> {
                        val fragment =
                            supportFragmentManager.fragments.get(mainViewPager.currentItem) as? IOnBackPressed
                        fragment?.onBackPressed()
                        return
                    }
                }
            }
        }
        super.onBackPressed()
    }

    private fun showToolbar() {
        supportActionBar?.let {
            if (it.isShowing) return
            it.show()
        }
    }

    private inner class ScreenSlidePageAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> BrowserFragment()
                1 -> SnackListFragment()
                else -> ProfileFragment()
            }
        }
    }

    override fun onComplete() {
        binding.apply {
            toolbarProgressbar.setProgress(1, true)
            ObjectAnimator.ofFloat(toolbarProgressbar, "alpha", 1f, 0f).apply {
                duration = 1000
                start()
            }
        }
    }

    override fun onProgress(progress: Int) {
        binding.apply {
            toolbarProgressbar.setProgress(progress, true)
            toolbarProgressbar.visibility = View.VISIBLE
            toolbarProgressbar.alpha = 1f
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        binding.apply {
            when (mainViewPager.currentItem) {
                0 -> {
                    menuInflater.inflate(R.menu.browse_snack_menu, menu)
                }
                1 -> {
                    menuInflater.inflate(R.menu.snack_list_menu, menu)
                }
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search_snack_icon_button -> {
                val intent = Intent(this, SearchSnackActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return false
    }
}