package com.daurenbek.hackdayapp.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.daurenbek.hackdayapp.R
import com.daurenbek.hackdayapp.databinding.FragmentSignupBinding
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class SignupFragment : Fragment(R.layout.fragment_signup) {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var viewModel: SignupViewModel

    private val logTag = javaClass.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignupBinding.bind(view)

        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]

        binding.signupBtn.setOnClickListener {
            sendRegisterData()
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        this.lifecycleScope.launchWhenResumed {
            viewModel.signupEvents.collect {
                when(it) {
                    is SignupViewModel.SignupEvents.NavigateToSignin -> {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun sendRegisterData() {
        val nameData = binding.nameInputText.text.toString()
        val secondNameData = binding.secondNameInputText.text.toString()
        val emailData = binding.emailInputText.text.toString()
        val passwordData = binding.passwordInputText.text.toString()

        Timber.tag(logTag).d("$nameData $secondNameData $emailData $passwordData")
        viewModel.sendRegisterData(nameData, secondNameData, emailData, passwordData)
    }
}