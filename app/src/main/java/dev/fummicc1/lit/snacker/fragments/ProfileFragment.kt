package dev.fummicc1.lit.snacker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.fragment_work_graph.view.*

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().title = "ユーザー"
        (requireActivity() as? AppCompatActivity)?.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }
}