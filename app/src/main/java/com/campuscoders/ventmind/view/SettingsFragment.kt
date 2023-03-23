package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.campuscoders.ventmind.R
import com.campuscoders.ventmind.databinding.FragmentSettingsBinding
import com.campuscoders.ventmind.util.UiState
import com.campuscoders.ventmind.util.hide
import com.campuscoders.ventmind.util.show
import com.campuscoders.ventmind.util.toast
import com.campuscoders.ventmind.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment: Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        binding.textViewResetPswd.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_forgotPasswordFragment)
        }

        binding.textViewChangeUserName.setOnClickListener {

        }

        binding.textViewLogout.setOnClickListener {
            viewModel.logOutFun()
        }

        binding.textViewDeleteAccount.setOnClickListener {
            viewModel.deleteAccountFun()
        }

        // logic
    }

    private fun observer(){
        viewModel.logOut.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Loading -> {
                    binding.progressBarSettings.show()
                }
                is UiState.Failure -> {
                    binding.progressBarSettings.hide()
                    toast(state.error!!)
                }
                is UiState.Success -> {
                    binding.progressBarSettings.hide()
                    toast(state.data)
                    findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
                }
            }
        }
        viewModel.deleteAccount.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Loading -> {
                    binding.progressBarSettings.show()
                }
                is UiState.Failure -> {
                    binding.progressBarSettings.hide()
                    toast(state.error!!)
                }
                is UiState.Success -> {
                    binding.progressBarSettings.hide()
                    toast(state.data)
                    findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}