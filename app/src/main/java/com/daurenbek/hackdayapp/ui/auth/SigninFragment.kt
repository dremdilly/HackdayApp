package com.daurenbek.hackdayapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.daurenbek.hackdayapp.MainActivity
import com.daurenbek.hackdayapp.R
import com.daurenbek.hackdayapp.databinding.FragmentSigninBinding
import com.example.metacourse.ui.auth.SigninViewModel
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class SigninFragment : Fragment(R.layout.fragment_signin) {
    private lateinit var binding: FragmentSigninBinding
    private lateinit var viewModel: SigninViewModel

    private val logTag = javaClass.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSigninBinding.bind(view)

        viewModel = ViewModelProvider(this)[SigninViewModel::class.java]

        binding.signinBtn.setOnClickListener {
            sendAuthData()
        }


        binding.toSignupBtn.setOnClickListener {
            val action = SigninFragmentDirections.actionSigninFragmentToSignupFragment()
            if(findNavController().currentDestination?.id == R.id.signinFragment) {
                findNavController().navigate(action)
            }
        }

        this.lifecycleScope.launchWhenResumed {
            viewModel.signinEvents.collect {
                when(it) {
                    is SigninViewModel.SigninEvents.NavigateToMain -> {
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun sendAuthData() {
        val emailData = binding.emailInputText.text.toString()
        val passwordData = binding.passwordInputText.text.toString()

        Timber.tag(logTag).d("$emailData $passwordData")
        viewModel.sendAuthData(emailData, passwordData)
    }
}