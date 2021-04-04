package com.galaxysoftware.istockenterpriseiorder.views.ui.outstand

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class OutstandViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is outstand Fragment"
    }
    val text: LiveData<String> = _text

}