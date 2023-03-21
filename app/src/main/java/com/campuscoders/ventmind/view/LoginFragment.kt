package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.campuscoders.ventmind.R
import com.campuscoders.ventmind.databinding.FragmentLoginBinding
import com.campuscoders.ventmind.util.*
import com.campuscoders.ventmind.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        // ACTIONS
        binding.buttonLoginCreateAccount.setOnClickListener {
            // register ekranına gider
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.textViewForgotPassword.setOnClickListener {
            // forgotPassword ekranına gider
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
        binding.buttonSignIn.setOnClickListener {
            // validation kontrolü ile login işlemi yapılır.
            if(validation()) {
                viewModel.loginFun(binding.editTextLoginEmailAddress.text.toString(),binding.editTextLoginPassword.text.toString())
            }
        }
    }

    private fun observer() {
        viewModel.login.observe(viewLifecycleOwner) {state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBarLogin.show()
                }
                is UiState.Failure -> {
                    binding.progressBarLogin.hide()
                    toast(state.error!!)
                }
                is UiState.Success -> {
                    binding.progressBarLogin.hide()
                    toast(state.data)
                    findNavController().navigate(R.id.action_loginFragment_to_feedFragment)
                }
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true
        if(binding.editTextLoginEmailAddress.text.toString().isNullOrEmpty()) {
            isValid = false
            toast("Enter email!")
        } else {
            if(!binding.editTextLoginEmailAddress.text.toString().isValidEmail()) {
                isValid = false
                toast("Invalid email")
            }
        }

        if(binding.editTextLoginPassword.text.toString().isNullOrEmpty()) {
            isValid = false
            toast("Enter password!")
        } else {
            if(binding.editTextLoginPassword.text.toString().length < 8) {
                isValid = false
                toast("Invalid password")
            }
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}