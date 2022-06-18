package com.daurenbek.hackdayapp.ui.universities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UniversitiesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is universities Fragment"
    }
    val text: LiveData<String> = _text
}