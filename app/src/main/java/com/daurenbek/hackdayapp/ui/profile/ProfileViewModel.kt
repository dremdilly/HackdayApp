package com.daurenbek.hackdayapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daurenbek.hackdayapp.network.NetworkApi
import com.daurenbek.hackdayapp.network.ProfileModel
import com.example.metacourse.ui.auth.SigninViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileViewModel : ViewModel() {
    private val profileEventsChannel = Channel<ProfileEvents>()
    val profileEvents = profileEventsChannel.receiveAsFlow()

    private val logTag = javaClass.simpleName

    fun getProfileData() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = NetworkApi.retrofitService.sendProfileRequest(
                "application/json",
                "application/json"
            )

            if (response.isSuccessful) {
                setProfile(response.body()!!)
            } else {
                Timber.tag(logTag).d(response.errorBody()?.string())
            }

        } catch (t: Throwable) {
            Timber.tag(logTag).d(t)
        }
    }

    private val _firstName = MutableLiveData<String?>()
    private val _secondName = MutableLiveData<String?>()
    private val _email = MutableLiveData<String?>()


    fun setProfile(profile: ProfileModel) {
        viewModelScope.launch {
            _firstName.value = profile.firstName
            _secondName.value = profile.secondName
            _email.value = profile.email
        }
    }


    val firstName: LiveData<String?> = _firstName
    val secondName: LiveData<String?> = _secondName
    val email: LiveData<String?> = _email

    sealed class ProfileEvents {

    }
}