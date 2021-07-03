package dev.fummicc1.lit.snacker.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.XAxis
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.databinding.FragmentWorkGraphBinding
import dev.fummicc1.lit.snacker.entities.StockHistory
import dev.fummicc1.lit.snacker.viewmodels.WorkGraphViewModel
import java.util.*

class WorkGraphFragment : Fragment() {

    private lateinit var binding: FragmentWorkGraphBinding
    private val viewModel: WorkGraphViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWorkGraphBinding.inflate(layoutInflater, container, false)

        binding.apply {
            viewModel.barData.observe(viewLifecycleOwner, Observer {
                stockCountBarChart.data = it
                stockCountBarChart.invalidate()
                stockCountBarChart.animateY(1000)
            })
            stockCountBarChart.apply {
                legend.textSize = 16f
                xAxis.textSize = 12f
                legend.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
                axisLeft.textSize = 16f
                description.isEnabled = true
            }

            viewModel.showingDateList.observe(viewLifecycleOwner, Observer {
                stockCountBarChart.xAxis.valueFormatter = WorkGraphViewModel.DateValueFormatter(it)
            })
        }
        return binding.root
    }
}