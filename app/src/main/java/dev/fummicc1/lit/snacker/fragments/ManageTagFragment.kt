package dev.fummicc1.lit.snacker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dev.fummicc1.lit.snacker.databinding.FragmentManageTagBinding
import dev.fummicc1.lit.snacker.models.SnackTagKindPresentable
import dev.fummicc1.lit.snacker.viewmodels.ManageTagViewModel
import dev.fummicc1.lit.snacker.views.TagManageAdapter

class ManageTagFragment : Fragment() {

    private lateinit var binding: FragmentManageTagBinding
    private val viewModel: ManageTagViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentManageTagBinding.inflate(inflater, container, false)
        binding.apply {
            val adapter = TagManageAdapter(requireContext(), object : TagManageAdapter.Handler {
                override fun onSwitchActivateToggle(tagKind: SnackTagKindPresentable, isActive: Boolean) {
                    viewModel.updateTagActivation(tagKind, isActive)
                }
            })
            tagRecyclerView.adapter = adapter
            tagRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            viewModel.allTags.observe(viewLifecycleOwner, Observer {
                adapter.injectTags(it)
            })
        }

        return binding.root
    }
}