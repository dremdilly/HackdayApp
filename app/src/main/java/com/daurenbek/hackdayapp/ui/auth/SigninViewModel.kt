package com.example.metacourse.ui.auth

import androidx.lifecycle.ViewModel
import com.daurenbek.hackdayapp.PreferenceManager
import com.daurenbek.hackdayapp.network.AuthorizeData
import com.daurenbek.hackdayapp.network.NetworkApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SigninViewModel : ViewModel() {
    private val signinEventsChannel = Channel<SigninEvents>()
    val signinEvents = signinEventsChannel.receiveAsFlow()

    private val logTag = javaClass.simpleName

    fun sendAuthData(email: String, password: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            NetworkApi.authRetrofit("http://localhost:95.141.136.227:8181/")
            PreferenceManager.updateBaseUrl("http://localhost:95.141.136.227:8181/")

            val response = NetworkApi.authService.sendAuthorizeRequest(
                "application/json",
                "application/json",
                AuthorizeData(email, password)
            )

            if (response.isSuccessful) {
                signinEventsChannel.send(SigninEvents.NavigateToMain)
//                NetworkApi.createRetrofit("http://10.0.2.2:8181/", response.body()!!.jwtAuthResponse.accessToken)
                NetworkApi.createRetrofit("http://10.0.2.2:8181/", response.body()!!.accessToken)
//                if(response.body()?.person?.firstName != "null" && response.body()?.person?.secondName != "null") {
//                    PreferenceManager.updateName(response.body()!!.person.firstName!!,
//                        response.body()?.person?.secondName!!)
//                }
//                PreferenceManager.updateEmail(response.body()!!.person.email)
                PreferenceManager.preferencesFlow.collect {
                    if(it[PreferenceManager.PreferencesKeys.PHOTO] == null) {
                        PreferenceManager.updatePhoto("")
                    }
                }
//                if(response.body()!!.person.dateOfBirth != null) {
//                    PreferenceManager.updateBirthdate(response.body()!!.person.dateOfBirth!!)
//                }
            } else {
                signinEventsChannel.send(
                    SigninEvents.ShowErrorMessage(
                        response.errorBody()?.string() ?: "Login Error"
                    )
                )
                Timber.tag(logTag).d(response.errorBody()?.string())
            }

        } catch (t: Throwable) {
            Timber.tag(logTag).d(t)
            signinEventsChannel.send(SigninEvents.ShowErrorMessage("Connection Error"))
        }
    }

    sealed class SigninEvents {
        object NavigateToMain : SigninEvents()
        data class ShowErrorMessage(val msg: String) : SigninEvents()
    }
}