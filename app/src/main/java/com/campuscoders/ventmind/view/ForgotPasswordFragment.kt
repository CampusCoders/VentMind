package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.campuscoders.ventmind.R
import com.campuscoders.ventmind.databinding.FragmentForgotPasswordBinding
import com.campuscoders.ventmind.util.*
import com.campuscoders.ventmind.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment: Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentForgotPasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        binding.forgotPassBtn.setOnClickListener{
            if(validation()){
                viewModel.forgotPasswordFun(binding.emailEt.text.toString())
            }
        }
    }

    private fun observer(){
        viewModel.forgotPassword.observe(viewLifecycleOwner) { state->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBarRegister.show()
                }
                is UiState.Failure -> {
                    binding.progressBarRegister.hide()
                    toast(state.error!!)
                }
                is UiState.Success -> {
                    binding.progressBarRegister.hide()
                    toast(state.data)
                    findNavController().navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
                }
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true
        if(binding.emailEt.text.toString().isNullOrEmpty()) {
            isValid = false
            toast("Enter email!")
        } else {
            if(!binding.emailEt.text.toString().isValidEmail()) {
                isValid = false
                toast("Invalid email")
            }
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}