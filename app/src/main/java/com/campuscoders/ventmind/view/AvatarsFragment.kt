package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.campuscoders.ventmind.adapter.AvatarAdapter
import com.campuscoders.ventmind.databinding.FragmentAvatarsBinding
import com.campuscoders.ventmind.util.UiState
import com.campuscoders.ventmind.util.hide
import com.campuscoders.ventmind.util.show
import com.campuscoders.ventmind.util.toast
import com.campuscoders.ventmind.viewmodel.AvatarsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AvatarsFragment: Fragment() {

    private var _binding: FragmentAvatarsBinding? = null
    private val binding get() = _binding!!

    val viewModel: AvatarsViewModel by viewModels()

    private val avatarAdapter by lazy{
        AvatarAdapter(
            onAvatarClickListener = {
                // avatar source

            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAvatarsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        binding.recyclerViewAvatars.adapter = avatarAdapter
        binding.recyclerViewAvatars.layoutManager = staggeredGridLayoutManager
        binding.recyclerViewAvatars.itemAnimator = null

        observer()

        viewModel.getAvatarsFun()
        viewModel.getUserScore()
    }

    private fun observer(){
        viewModel.avatar.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Loading ->{
                    binding.progressBarAvatar.show()
                }
                is UiState.Success ->{
                    binding.progressBarAvatar.hide()
                    avatarAdapter.updateList(state.data.toMutableList())
                }
                is UiState.Failure ->{
                    binding.progressBarAvatar.hide()
                    toast(state.error?:"")
                }
            }

        viewModel.userScore.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Loading -> {
                    binding.progressBarAvatar.show()
                }
                is UiState.Success -> {
                    binding.progressBarAvatar.hide()
                    binding.textViewAvatarsScore.text = state.data.toString()
                }
                is UiState.Failure -> {
                    binding.progressBarAvatar.hide()
                    toast(state.error?:"")
                }
            }
        }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}