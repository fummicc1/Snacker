package dev.fummicc1.lit.snacker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.databinding.FragmentEditSnackTagBottomSheetDialogBinding
import dev.fummicc1.lit.snacker.models.SnackPresentable
import dev.fummicc1.lit.snacker.models.SnackTagKindPresentable
import dev.fummicc1.lit.snacker.viewmodels.EditSnackTagBottomSheetViewModel
import dev.fummicc1.lit.snacker.views.EditSnackTagAdapter

class EditSnackTagBottomSheetDialogFragment(val snack: SnackPresentable) :
    BottomSheetDialogFragment() {

    private lateinit var viewModel: EditSnackTagBottomSheetViewModel

    private lateinit var binding: FragmentEditSnackTagBottomSheetDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(
            this,
            EditSnackTagBottomSheetViewModel.Factory(snack, requireActivity().application)
        ).get(EditSnackTagBottomSheetViewModel::class.java)

        binding =
            FragmentEditSnackTagBottomSheetDialogBinding.inflate(layoutInflater, container, false)

        binding.apply {

            val adapter = EditSnackTagAdapter(requireContext())

            viewModel.allTagKinds.observe(viewLifecycleOwner, Observer {
                adapter.updateAllTagKindItems(it)
            })

            viewModel.closeBottomSheet.observe(viewLifecycleOwner, Observer {
                dismiss()
            })

            viewModel.selectingTags.observe(viewLifecycleOwner, Observer {
                adapter.updateActiveItems(it)
            })

            editSnackTagKindRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            editSnackTagKindRecyclerView.adapter = adapter

            snackTitleTextView.text = snack.title

            decideSnackTagsFloatingActionButton.setOnClickListener {
                viewModel.decideTags()
            }

            val recyclerViewHandler = object : EditSnackTagAdapter.Handler {
                override fun onTap(snackTagKindPresentable: SnackTagKindPresentable) {
                    viewModel.toggleTag(snackTagKindPresentable)
                }
            }
            adapter.updateHandler(recyclerViewHandler)
        }

        return binding.root
    }
}