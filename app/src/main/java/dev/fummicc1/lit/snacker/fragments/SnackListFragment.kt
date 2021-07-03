package dev.fummicc1.lit.snacker.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dev.fummicc1.lit.snacker.activities.ReadSnackActivity
import dev.fummicc1.lit.snacker.databinding.FragmentSnackListBinding
import dev.fummicc1.lit.snacker.models.SnackPresentable
import dev.fummicc1.lit.snacker.views.SnackListAdapter

class SnackListFragment : Fragment() {

    private lateinit var binding: FragmentSnackListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentSnackListBinding.inflate(layoutInflater, container, false)

        binding.apply {

            val adapter = ScreenSlidePageAdapter(requireActivity())
            snackListViewPager.adapter = adapter

            TabLayoutMediator(snackListTabLayout, snackListViewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "未読"
                    1 -> tab.text = "読了済"
                    2 -> tab.text = "最近"
                }
            }.attach()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.apply {
            when (snackListViewPager.currentItem) {
                0 -> {
                    requireActivity().title = "未読"
                    requireActivity().invalidateOptionsMenu()
                    (requireActivity() as? AppCompatActivity)?.apply {
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    }
                }
                1 -> {
                    requireActivity().title = "読了済"
                    requireActivity().invalidateOptionsMenu()
                    (requireActivity() as? AppCompatActivity)?.apply {
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    }
                }
                2 -> {
                    requireActivity().title = "最近"
                    requireActivity().invalidateOptionsMenu()
                    (requireActivity() as? AppCompatActivity)?.apply {
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    }
                }
            }
        }
    }

    private inner class ScreenSlidePageAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> SavedSnackListFragment()
                1 -> ArchivedSnackListFragment()
                else -> RecentlyViewedSnackListFragment()
            }
        }
    }
}