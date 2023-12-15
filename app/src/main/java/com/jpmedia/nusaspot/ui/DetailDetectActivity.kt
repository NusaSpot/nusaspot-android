package com.jpmedia.nusaspot.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jpmedia.nusaspot.adapter.DetailAdapter
import com.jpmedia.nusaspot.api.DetectStartResponse
import com.jpmedia.nusaspot.api.FinishResponse
import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi
import com.jpmedia.nusaspot.databinding.ActivityDetailDetectBinding
import com.jpmedia.nusaspot.model.DetailViewModelFactory
import com.jpmedia.nusaspot.model.DetectDetailViewModel
import com.jpmedia.nusaspot.ui.repository.DetailRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

        val buttonAddImage = binding.buttonAddImage

        val status = intent.getIntExtra("status", 0)

        if (status == 1) {
            // Status 1, tombol "Tambah Gambar" ditampilkan
            buttonAddImage.visibility = View.GONE

        } else {
            // Status lainnya, tombol "Tambah Gambar" disembunyikan
            buttonAddImage.visibility = View.VISIBLE

        }

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("token", null)
        val detectId = intent.getStringExtra("DETECT_ID")

        binding.buttonSearchRecipe.setOnClickListener {
            if (authToken != null) {
                // Lakukan panggilan ke endpoint detectFinish
                if (detectId != null) {
                    apiService.detectFinish("Bearer $authToken", detectId).enqueue(object :
                        Callback<FinishResponse> {
                        override fun onResponse(call: Call<FinishResponse>, response: Response<FinishResponse>) {
                            if (response.isSuccessful) {
                                // Tanggapi hasil yang berhasil
                                val finishResponse = response.body()
                                // Tampilkan ID dari respons JSON ke dalam Toast
                                finishResponse?.data?.let { data ->
                                    // Optional: Hanya jika ingin menutup aktivitas saat pindah ke DetectActivity
                                }

                            } else {
                                // Tanggapi kesalahan dari respons HTTP
                                Toast.makeText(this@DetailDetectActivity, "Detect finish failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<FinishResponse>, t: Throwable) {
                            // Tanggapi kegagalan dalam melakukan panggilan
                            Toast.makeText(this@DetailDetectActivity, "Detect finish request failed", Toast.LENGTH_SHORT).show()
                        }
                    })

                    val intent = Intent(this@DetailDetectActivity, DetectActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this@DetailDetectActivity, "Token is null. Handle this case appropriately.", Toast.LENGTH_SHORT).show()
            }
        }

        buttonAddImage.setOnClickListener {
            val intent = Intent(this@DetailDetectActivity, PostDetectActivity::class.java)
            intent.putExtra("DETECT_ID", detectId.toString())
            startActivity(intent)
        }

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
