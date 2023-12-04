package com.jpmedia.nusaspot.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jpmedia.nusaspot.api.DetailData
import com.jpmedia.nusaspot.ui.repository.DetailRepository

class DetectDetailViewModel(private val detailRepository: DetailRepository) : ViewModel() {
    private val _detectDetailData = MutableLiveData<List<DetailData>>()

    val detectDetailData: LiveData<List<DetailData>> get() = _detectDetailData

    fun fetchDetail(detectId: String, authorization: String) {
        detailRepository.getDetail(detectId, authorization).observeForever { detailDataList ->
            _detectDetailData.value = detailDataList
        }
    }
}
