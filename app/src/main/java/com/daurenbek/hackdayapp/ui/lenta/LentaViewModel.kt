package com.daurenbek.hackdayapp.ui.lenta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LentaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is lenta Fragment"
    }
    val text: LiveData<String> = _text
}