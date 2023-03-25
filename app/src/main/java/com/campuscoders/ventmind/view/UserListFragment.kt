package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.campuscoders.ventmind.R
import com.campuscoders.ventmind.adapter.UserListAdapter
import com.campuscoders.ventmind.databinding.FragmentUserListBinding
import com.campuscoders.ventmind.util.UiState
import com.campuscoders.ventmind.util.hide
import com.campuscoders.ventmind.util.show
import com.campuscoders.ventmind.util.toast
import com.campuscoders.ventmind.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment: Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    val viewModel: ListViewModel by viewModels()

    private val userListAdapter by lazy {
        UserListAdapter(
            avatarOnItemClickListener = {
                // userid
                // profil ekranına gidilecek
                findNavController().navigate(R.id.action_userListFragment_to_profileFragment, Bundle().apply {
                    putString("user_id",it)
                })
            },
            usernameOnItemClickListener = {
                // userid
                // profil ekranına gidilecek
                findNavController().navigate(R.id.action_userListFragment_to_profileFragment, Bundle().apply {
                    putString("user_id",it)
                })
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentUserListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recyclerView init
        binding.recyclerViewUserList.adapter = userListAdapter
        binding.recyclerViewUserList.layoutManager = LinearLayoutManager(requireContext())

        observer()

        viewModel.getUsers()
    }

    private fun observer(){
        viewModel.users.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Loading -> {
                    binding.progressBarUserList.show()
                }
                is UiState.Success -> {
                    binding.progressBarUserList.hide()
                    userListAdapter.updateList(state.data.toMutableList())
                }
                is UiState.Failure -> {
                    binding.progressBarUserList.hide()
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