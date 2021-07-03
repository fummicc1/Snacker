package dev.fummicc1.lit.snacker.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.activities.ReadSnackActivity
import dev.fummicc1.lit.snacker.databinding.FragmentRecentlyViewedSnackListBinding
import dev.fummicc1.lit.snacker.models.RecentlyViewedSnackPresentable
import dev.fummicc1.lit.snacker.models.SnackPresentable
import dev.fummicc1.lit.snacker.viewmodels.RecentlyViewedSnackListViewModel
import dev.fummicc1.lit.snacker.views.RecentlyViewedSnackListAdapter
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class RecentlyViewedSnackListFragment : Fragment() {

    private lateinit var binding: FragmentRecentlyViewedSnackListBinding
    private val viewModel: RecentlyViewedSnackListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentlyViewedSnackListBinding.inflate(inflater, container, false)
        binding.apply {

            val adapter = RecentlyViewedSnackListAdapter(
                requireContext(),
                object : RecentlyViewedSnackListAdapter.Handler {
                    override fun onClick(item: RecentlyViewedSnackPresentable) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            val snack = viewModel.convertToSnackPresentable(item)
                            withContext(Dispatchers.Main) {
                                val intent = Intent(requireActivity(), ReadSnackActivity::class.java)
                                intent.putExtra(BrowserFragment.snackField, snack)
                                requireActivity().startActivity(intent)
                            }
                        }
                    }
                })

            recentlyViewedSnackRecyclerView.adapter =
                ScaleInAnimationAdapter(AlphaInAnimationAdapter(adapter))
            recentlyViewedSnackRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            viewModel.recentlyViewedSnackList.observe(viewLifecycleOwner, Observer {
                adapter.injectItems(it)
            })
            
            includeRecentEmptyState.messageTextView.text = "アプリ内で最近閲覧したWebサイトが一覧で表示されます。"

            viewModel.shouldShowEmptyState.observe(viewLifecycleOwner, Observer {
                includeRecentEmptyState.messageRootView.visibility = if (it) View.VISIBLE else View.GONE
            })
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().title = "最近"
    }
}