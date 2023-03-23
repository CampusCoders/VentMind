package com.campuscoders.ventmind.view

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.campuscoders.ventmind.R
import com.campuscoders.ventmind.databinding.FragmentPopUpUserNameBinding
import com.campuscoders.ventmind.databinding.FragmentSettingsBinding
import com.campuscoders.ventmind.util.*
import com.campuscoders.ventmind.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopUpUserNameFragment : DialogFragment(){

    private var _binding: FragmentPopUpUserNameBinding? = null
    private val binding get() = _binding!!

    val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPopUpUserNameBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        binding.changeUserName.setOnClickListener{
            if (validation())
            {
                viewModel.updateUsernameFun(binding.editTextRegisterUsername.text.toString())
            }
            dismiss()
        }
    }

    private fun observer() {
        viewModel.updateUsername.observe(viewLifecycleOwner) {state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressPopUpUserName.show()
                }
                is UiState.Success -> {
                    binding.progressPopUpUserName.hide()
                    toast(state.data)
                }
                is UiState.Failure -> {
                    binding.progressPopUpUserName.hide()
                    toast(state.error!!)
                }
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true
        if (binding.editTextRegisterUsername.text.toString().isNullOrEmpty()) {
            isValid = false
            toast("Enter new username!")
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}