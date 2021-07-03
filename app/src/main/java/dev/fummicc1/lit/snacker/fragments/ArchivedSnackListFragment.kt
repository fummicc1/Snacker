package dev.fummicc1.lit.snacker.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.activities.ReadSnackActivity
import dev.fummicc1.lit.snacker.databinding.FragmentArchivedSnackListBinding
import dev.fummicc1.lit.snacker.databinding.ViewSnackTagChipBinding
import dev.fummicc1.lit.snacker.models.SnackPresentable
import dev.fummicc1.lit.snacker.models.SnackTagKindPresentable
import dev.fummicc1.lit.snacker.viewmodels.ArchivedSnackListViewModel
import dev.fummicc1.lit.snacker.views.SnackListAdapter
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter

class ArchivedSnackListFragment : Fragment() {

    private lateinit var binding: FragmentArchivedSnackListBinding
    private val viewModel: ArchivedSnackListViewModel by viewModels()

    private var isVisibleFragment: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentArchivedSnackListBinding.inflate(layoutInflater, container, false)

        binding.apply {
            val adapter = SnackListAdapter(requireContext())

            arhivedSnackListRecyclerView.adapter = ScaleInAnimationAdapter(AlphaInAnimationAdapter(adapter))
            arhivedSnackListRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            adapter.configureEventListner(object : SnackListAdapter.EventListener {
                override fun onSelectSnack(snack: SnackPresentable) {
                    val intent = Intent(requireActivity(), ReadSnackActivity::class.java)
                    intent.putExtra(BrowserFragment.snackField, snack)
                    requireActivity().startActivity(intent)
                }

                override fun onShowOptionMenu(snack: SnackPresentable) {
                    viewModel.selectSnack(snack)
                }
            })

            viewModel.snackList.observe(viewLifecycleOwner, Observer {
                adapter.injectSnackList(it)
            })

            viewModel.availableTags.observe(viewLifecycleOwner, Observer { tags ->
                filterChipGroup.removeAllViews()
                tags.forEach {
                    addChipToGroup(it)
                }
            })

            includeArchivedEmptyState.messageTextView.text = "読了状態の記事が一覧で表示されます。"

            viewModel.shouldShowEmptyState.observe(viewLifecycleOwner, Observer {
                includeArchivedEmptyState.messageRootView.visibility = if (it) View.VISIBLE else View.GONE
            })
        }
        registerForContextMenu(binding.arhivedSnackListRecyclerView)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        registerForContextMenu(binding.arhivedSnackListRecyclerView)

        requireActivity().title = "読了済み"

        isVisibleFragment = true
    }

    override fun onPause() {
        super.onPause()
        isVisibleFragment = false
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu.add(Menu.NONE, 1, Menu.NONE, "削除").apply {
            val s = SpannableString("削除")
            s.setSpan(ForegroundColorSpan(Color.RED), 0, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setTitle(s)
        }
        menu.add(Menu.NONE, 2, Menu.NONE, "読了を解除する")
        menu.add(Menu.NONE, 3, Menu.NONE, "タグの編集")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter_snack_icon_button -> {
                binding.apply {
                    val isVisible = filterChipGroup.visibility == View.VISIBLE
                    filterChipGroup.visibility = if (isVisible) View.GONE else View.VISIBLE
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        if (isVisibleFragment.not()) return false

        val snack = viewModel.selectSnack.value
        if (snack == null) return false

        when (item.itemId) {
            1 -> {
                viewModel.deleteSnack(snack)
                return true
            }
            2 -> {
                viewModel.resignArchiveSnack(snack)
                return true
            }
            3 -> {
                val bottomSheetDialogFragment = EditSnackTagBottomSheetDialogFragment(snack)
                bottomSheetDialogFragment.show(childFragmentManager, bottomSheetDialogFragment.tag)
            }
        }
        return false
    }

    private fun addChipToGroup(snackTagKindPresentable: SnackTagKindPresentable) {
        ViewSnackTagChipBinding.inflate(layoutInflater, null, false).apply {
            this.tagName = snackTagKindPresentable.name
            (root as? Chip)?.let { chip ->
                chip.id = snackTagKindPresentable.id
                chip.isCheckable = true
                chip.isChecked = false
                chip.isCloseIconVisible = false
                chip.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        viewModel.selectSnackTagPresentableId(snackTagKindPresentable.id)
                    } else {
                        viewModel.deselectTag(snackTagKindPresentable)
                    }
                }
            }
        }.apply {
            binding.filterChipGroup.addView(this.root)
        }
    }
}