package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.campuscoders.ventmind.adapter.TrendAdapter
import com.campuscoders.ventmind.databinding.FragmentTrendBinding
import com.campuscoders.ventmind.util.UiState
import com.campuscoders.ventmind.util.hide
import com.campuscoders.ventmind.util.show
import com.campuscoders.ventmind.util.toast
import com.campuscoders.ventmind.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrendFragment: Fragment() {

    private  var _binding: FragmentTrendBinding? = null
    private  val binding get() = _binding!!

    val viewModel: ListViewModel by viewModels()

    private val trendAdapter by lazy {
        TrendAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentTrendBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recyclerView
        binding.recyclerViewTrend.adapter = trendAdapter
        binding.recyclerViewTrend.layoutManager = LinearLayoutManager(requireContext())

        observer()

        viewModel.getTrends()
    }

    private fun observer(){
        viewModel.trend.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Loading -> {
                    binding.progressBarTrend.show()
                }
                is UiState.Success -> {
                    binding.progressBarTrend.hide()
                    trendAdapter.updateList(state.data.toMutableList())
                }
                is UiState.Failure -> {
                    binding.progressBarTrend.hide()
                    toast(state.error?:"")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}