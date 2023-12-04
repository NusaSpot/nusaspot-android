package com.jpmedia.nusaspot.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jpmedia.nusaspot.api.DetectDataItem
import com.jpmedia.nusaspot.repository.DetectRepository

class DetectViewModel(private val detectRepository: DetectRepository) : ViewModel() {
    private var _detectData = MutableLiveData<List<DetectDataItem>>()

    val detectData: LiveData<List<DetectDataItem>> get() = _detectData

    fun fetchDetect(authorization: String) {
        _detectData = detectRepository.getDetect(authorization) as MutableLiveData<List<DetectDataItem>>
    }
}

