package com.jpmedia.nusaspot.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jpmedia.nusaspot.adapter.DetailAdapter
import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi
import com.jpmedia.nusaspot.databinding.ActivityDetailDetectBinding
import com.jpmedia.nusaspot.model.DetailViewModelFactory
import com.jpmedia.nusaspot.model.DetectDetailViewModel
import com.jpmedia.nusaspot.ui.repository.DetailRepository


class DetailDetectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDetectBinding
    private lateinit var detectDetailViewModel: DetectDetailViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var detailAdapter: DetailAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDetectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apiService = Retro().getRetroClientInstance().create(UserApi::class.java)
        val detailRepository = DetailRepository(apiService)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        detailAdapter = DetailAdapter(this, emptyList())
        recyclerView.adapter = detailAdapter
        val spanCount = 2 // Jumlah kolom dalam grid
        recyclerView.layoutManager = GridLayoutManager(this, spanCount)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("token", null)
        val detectId = intent.getStringExtra("DETECT_ID")
        if (!detectId.isNullOrEmpty()) {
            // Inisialisasi ViewModel dengan menggunakan DetailRepository
            detectDetailViewModel = ViewModelProvider(this, DetailViewModelFactory(detailRepository))
                .get(DetectDetailViewModel::class.java)
            detectDetailViewModel.fetchDetail(detectId, "Bearer $authToken".orEmpty())
            detectDetailViewModel.detectDetailData.observe(this, { detailDataList ->
                // Perbarui adapter RecyclerView dengan data terbaru
                detailAdapter.updateData(detailDataList)
            })
        } else {
            Toast.makeText(this, "Invalid detectId", Toast.LENGTH_SHORT).show()
        }
    }
}
