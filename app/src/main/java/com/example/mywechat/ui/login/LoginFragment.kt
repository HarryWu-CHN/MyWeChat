package com.example.mywechat.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mywechat.MainActivity
import com.example.mywechat.R
import com.example.mywechat.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout


@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel
        binding.loginButtonClick.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                withTimeout(10 * 1000) {
                    loginViewModel.login(binding.loginUsernameTextEdit.text.toString(), binding.loginPasswordTextEdit.text.toString()) {
                        when (it) {
                            true ->{
                                (requireActivity() as MainActivity).jumpToUser()
                            }
                            false -> {
                                AlertDialog.Builder(requireContext()).setTitle("用户名或密码错误！").show()
                            }
                        }
                    }
                    Log.d("LoginFragment", "login succeeded.")
                }
                Log.d("LoginFragment", "finished")
            }
        }
        binding.registerUserButtonClick.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }
}
