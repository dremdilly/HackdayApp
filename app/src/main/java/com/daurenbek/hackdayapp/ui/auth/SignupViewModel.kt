package com.daurenbek.hackdayapp.ui.auth

import androidx.lifecycle.ViewModel
import com.daurenbek.hackdayapp.PreferenceManager
import com.daurenbek.hackdayapp.network.NetworkApi
import com.daurenbek.hackdayapp.network.RegisterData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SignupViewModel : ViewModel() {
    private val signupEventsChannel = Channel<SignupEvents>()
    val signupEvents = signupEventsChannel.receiveAsFlow()

    private val logTag = javaClass.simpleName

    fun sendRegisterData(firstName: String, secondName: String, email: String, password: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            NetworkApi.authRetrofit("http://10.0.2.2:8080/")
            PreferenceManager.updateBaseUrl("http://10.0.2.2:8080/")
            PreferenceManager.updatePhoto("")

            val response = NetworkApi.authService.sendRegisterRequest(
                "application/json",
                "application/json",
                RegisterData(firstName, secondName, email, password)
            )

            if (response.isSuccessful) {
                signupEventsChannel.send(SignupEvents.NavigateToSignin)
//                NetworkApi.createRetrofit("https://cabinet.spdev.kz", response.body()!!.jwtAuthResponse.accessToken)
            } else {
                signupEventsChannel.send(
                    SignupEvents.ShowErrorMessage(
                        response.errorBody()?.string() ?: "Registration Error"
                    )
                )
                Timber.tag(logTag).d(response.errorBody()?.string())
            }

        } catch (t: Throwable) {
            Timber.tag(logTag).d(t)
            signupEventsChannel.send(SignupEvents.ShowErrorMessage("Connection Error"))
        }
    }

    sealed class SignupEvents {
        object NavigateToSignin : SignupEvents()
        data class ShowErrorMessage(val msg: String) : SignupEvents()
    }
}