package dev.fummicc1.lit.snacker.fragments

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.activities.ReadSnackActivity
import dev.fummicc1.lit.snacker.databinding.FragmentSearchSnackBinding
import dev.fummicc1.lit.snacker.models.SnackPresentable
import dev.fummicc1.lit.snacker.viewmodels.SearchSnackViewModel
import dev.fummicc1.lit.snacker.views.SnackListAdapter
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter

class SearchSnackFragment : Fragment() {

    private lateinit var binding: FragmentSearchSnackBinding
    private val viewModel: SearchSnackViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentSearchSnackBinding.inflate(inflater, container, false)
        binding.apply {
            val adapter = SnackListAdapter(requireContext())

            adapter.configureEventListner(object : SnackListAdapter.EventListener {
                override fun onSelectSnack(snack: SnackPresentable) {
                    Intent(requireActivity(), ReadSnackActivity::class.java).apply {
                        putExtra(BrowserFragment.snackField, snack)
                        startActivity(this)
                        requireActivity().finish()
                    }
                }

                override fun onShowOptionMenu(snack: SnackPresentable) {
                }
            })

            searchSnackRecyclerView.adapter = ScaleInAnimationAdapter(AlphaInAnimationAdapter(adapter))
            searchSnackRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            viewModel.result.observe(viewLifecycleOwner, Observer {
                adapter.injectSnackList(it)
            })
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_snack_menu, menu)

        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search_snack_icon_button).actionView as? SearchView)?.apply {
            isIconifiedByDefault = true
            queryHint = "記事のタイトルで入力"
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.updateQuery(query ?: "")
                    this@apply.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.updateQuery(newText ?: "")
                    return true
                }
            })
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
}