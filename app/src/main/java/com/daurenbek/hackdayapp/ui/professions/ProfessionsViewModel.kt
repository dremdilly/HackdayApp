package com.daurenbek.hackdayapp.ui.professions

import androidx.lifecycle.*
import com.daurenbek.hackdayapp.ResourceHelper
import com.daurenbek.hackdayapp.network.NetworkApi
import com.daurenbek.hackdayapp.network.SpecializationModel
import com.daurenbek.hackdayapp.network.SubjectModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber

class ProfessionsViewModel(
    private val resourceHelper: ResourceHelper
) : ViewModel() {
    private val professionEventsChannel = Channel<ProfessionEvents>()
    val professionEvents = professionEventsChannel.receiveAsFlow()

    fun getAllSpecializations() = viewModelScope.launch {
        try {
            var allSpecializationResponse: Response<List<SpecializationModel>>? = null

            allSpecializationResponse = withContext(Dispatchers.IO) {
                NetworkApi.retrofitService.getAllSpecializations()
            }

            if (allSpecializationResponse.isSuccessful) {
                val result = allSpecializationResponse.body()
                Timber.d("success response: ${result.toString()}")

                result?.let {
                    professionEventsChannel.send(ProfessionEvents.ShowAllResult(result))
                }
            } else {
                var errorMessage = allSpecializationResponse.errorBody()?.string()
                Timber.d("unsuccess response: $errorMessage")
                if (errorMessage.isNullOrBlank()) {
                    errorMessage = "Произошла ошибка!"
                    professionEventsChannel.send(ProfessionEvents.ShowNotFound("Не найдено"))
                }
                professionEventsChannel.send(ProfessionEvents.ShowErrorMessage(errorMessage))
                professionEventsChannel.send(ProfessionEvents.ShowNotFound("Не найдено"))
            }
        } catch (e: Exception) {
            Timber.d(e)
            professionEventsChannel.send(ProfessionEvents.ShowErrorMessage(e.message.toString()))
        }
    }

    fun getAllSubjects() = viewModelScope.launch {
        try {
            var allSubjectResponse: Response<List<SubjectModel>>? = null

            allSubjectResponse = withContext(Dispatchers.IO) {
                NetworkApi.retrofitService.getAllSubjects()
            }

            if (allSubjectResponse.isSuccessful) {
                val result = allSubjectResponse.body()
                Timber.d("success response: ${result.toString()}")

                result?.let {
                    professionEventsChannel.send(ProfessionEvents.ShowSubjectsResult(result))
                }
            } else {
                var errorMessage = allSubjectResponse.errorBody()?.string()
                Timber.d("unsuccess response: $errorMessage")
                if (errorMessage.isNullOrBlank()) {
                    errorMessage = "Произошла ошибка!"
                    professionEventsChannel.send(ProfessionEvents.ShowNotFound("Не найдено"))
                }
                professionEventsChannel.send(ProfessionEvents.ShowErrorMessage(errorMessage))
                professionEventsChannel.send(ProfessionEvents.ShowNotFound("Не найдено"))
            }
        } catch (e: Exception) {
            Timber.d(e)
            professionEventsChannel.send(ProfessionEvents.ShowErrorMessage(e.message.toString()))
        }
    }

    sealed class ProfessionEvents {
        data class ShowAllResult(val result: List<SpecializationModel>) : ProfessionEvents()
        data class ShowSubjectsResult(val result: List<SubjectModel>) : ProfessionEvents()
        data class ShowErrorMessage(val msg: String) : ProfessionEvents()
        data class ShowNotFound(val msg: String) : ProfessionEvents()

    }
}


class ProfessionsViewModelFactory(private val resourceHelper: ResourceHelper) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ProfessionsViewModel(resourceHelper) as T
}