package com.example.mywechat.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.mywechat.R
import com.example.mywechat.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel: SignupViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.loginUserButton.setOnClickListener {
            goToLogin()
        }
        binding.registerButton.setOnClickListener {
            binding.apply {
                if (passwordRegister.text.toString() != passwordCheck.text.toString()) {
                    AlertDialog.Builder(requireContext()).setTitle(getString(R.string.register_wrong_password)).show()
                    binding.passwordRegister.setText("")
                    binding.passwordCheck.setText("")
                }
            }

            registerViewModel.register(binding.usernameRegister.text.toString(), binding.passwordRegister.text.toString())
        }
        registerViewModel.liveData.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    true -> goToLogin()
                    false -> AlertDialog.Builder(requireContext()).setTitle(getString(R.string.register_failure)).show()
                }
                registerViewModel.liveData.value = null
            }
        }
    }

    private fun goToLogin() {
        findNavController().popBackStack()
    }
}
