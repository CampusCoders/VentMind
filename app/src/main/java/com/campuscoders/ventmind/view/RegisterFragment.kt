package com.campuscoders.ventmind.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.campuscoders.ventmind.R
import com.campuscoders.ventmind.databinding.FragmentRegisterBinding
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.*
import com.campuscoders.ventmind.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment: Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        binding.buttonRegisterCreate.setOnClickListener {
            if(validation()) {
                viewModel.registerFun(
                    binding.editTextRegisterEmailAddress.text.toString(),
                    binding.editTextRegisterPassword.text.toString(),
                    createUser()
                )
            }
        }
    }

    private fun observer() {
        viewModel.register.observe(viewLifecycleOwner) {state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBarRegister.show()
                }
                is UiState.Success -> {
                    binding.progressBarRegister.hide()
                    toast(state.data)
                    findNavController().navigate(R.id.action_registerFragment_to_feedFragment)
                }
                is UiState.Failure -> {
                    binding.progressBarRegister.hide()
                    toast(state.error!!)
                }
            }
        }
    }

    private fun createUser(): User {
        return User(
            user_avatar = "",
            user_bio = "",
            user_email = binding.editTextRegisterEmailAddress.text.toString(),
            user_lock = false,
            user_nick = binding.editTextRegisterUsername.text.toString(),
            user_score = 0
        )
    }

    private fun validation(): Boolean {
        var isValid = true
        if(binding.editTextRegisterUsername.text.toString().isNullOrEmpty()) {
            isValid = false
            toast("Enter username.")
        }
        if (binding.editTextRegisterEmailAddress.text.toString().isNullOrEmpty()) {
            isValid = false
            toast("Enter email address.")
        } else {
            if (!binding.editTextRegisterEmailAddress.text.toString().isValidEmail()) {
                isValid = false
                toast("Invalid email address.")
            }
        }
        if (binding.editTextRegisterPassword.text.toString().isNullOrEmpty()) {
            isValid = false
            toast("Enter password.")
        } else {
            if (binding.editTextRegisterPassword.text.toString().length < 8) {
                isValid = false
                toast("Invalid password.")
            }
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}