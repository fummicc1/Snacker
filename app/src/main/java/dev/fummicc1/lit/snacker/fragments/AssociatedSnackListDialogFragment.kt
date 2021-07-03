package dev.fummicc1.lit.snacker.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dev.fummicc1.lit.snacker.activities.ReadSnackActivity
import dev.fummicc1.lit.snacker.databinding.FragmentAssociatedSnackListDialogBinding
import dev.fummicc1.lit.snacker.models.AssociatedSnack
import dev.fummicc1.lit.snacker.viewmodels.AssociatedSnackListDialogViewModel
import dev.fummicc1.lit.snacker.views.AssociatedSnackListAdapter
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AssociatedSnackListDialogFragment(private val associatedSnackLists: List<AssociatedSnack>): DialogFragment() {

    private val viewModel: AssociatedSnackListDialogViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val binding = FragmentAssociatedSnackListDialogBinding.inflate(it.layoutInflater, null, false)

            binding.apply {

                val adapter = AssociatedSnackListAdapter(requireContext())

                associatedSnackListDialogRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
                associatedSnackListDialogRecyclerView.adapter = ScaleInAnimationAdapter(AlphaInAnimationAdapter(adapter))

                adapter.injectSnackList(associatedSnackLists)
                adapter.configureEventListner(object: AssociatedSnackListAdapter.EventListener {
                    override fun onSelectSnack(snack: AssociatedSnack) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            val snackPresentable = viewModel.convertAssociatedToSnack(snack)
                            withContext(Dispatchers.Main) {
                                Intent(requireContext(), ReadSnackActivity::class.java).apply {
                                    putExtra(BrowserFragment.snackField, snackPresentable)
                                    startActivity(this)
                                    dismiss()
                                }
                            }
                        }
                    }
                })
            }

            builder.setView(binding.root)
                .setTitle("関連記事")
                .setNegativeButton("閉じる", { _, _ -> })

            return builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}