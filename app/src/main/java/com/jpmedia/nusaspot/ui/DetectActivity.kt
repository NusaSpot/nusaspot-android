package com.jpmedia.nusaspot.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jpmedia.nusaspot.adapter.DetectAdapter
import com.jpmedia.nusaspot.api.DetectStartResponse
import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi
import com.jpmedia.nusaspot.databinding.ActivityDetectBinding
import com.jpmedia.nusaspot.model.DetectViewModel
import com.jpmedia.nusaspot.model.DetectViewModelFactory
import com.jpmedia.nusaspot.repository.DetectRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetectBinding
    private lateinit var detectViewModel: DetectViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var detectAdapter: DetectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apiService = Retro().getRetroClientInstance().create(UserApi::class.java)
        val detectRepository = DetectRepository(apiService)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        detectAdapter = DetectAdapter(emptyList(), this)
        recyclerView.adapter = detectAdapter
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("token", null)

        if (authToken != null) {
            detectViewModel = ViewModelProvider(this, DetectViewModelFactory(detectRepository))
                .get(DetectViewModel::class.java)
            detectViewModel.fetchDetect("Bearer $authToken")
            detectViewModel.detectData.observe(this) { detectList ->
                detectAdapter.updateData(detectList)
            }
            detectAdapter.setOnItemClickListener { position ->
                val clickedItem = detectAdapter.getItem(position)

                if (clickedItem?.id != null && clickedItem.id != 0) {
                    val intent = Intent(this@DetectActivity, DetailDetectActivity::class.java)
                    intent.putExtra("DETECT_ID", clickedItem.id.toString()) // Mengonversi Int ke String
                    intent.putExtra("status", 1)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@DetectActivity, "Invalid item", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
