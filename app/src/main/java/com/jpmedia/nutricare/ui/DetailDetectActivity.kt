package com.jpmedia.nutricare.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jpmedia.nutricare.adapter.DetailAdapter
import com.jpmedia.nutricare.api.DeleteResponse
import com.jpmedia.nutricare.api.FinishResponse
import com.jpmedia.nutricare.api.Retro
import com.jpmedia.nutricare.api.UserApi
import com.jpmedia.nutricare.databinding.ActivityDetailDetectBinding
import com.jpmedia.nutricare.model.DetailViewModelFactory
import com.jpmedia.nutricare.model.DetectDetailViewModel
import com.jpmedia.nutricare.ui.repository.DetailRepository
import com.jpmedia.nutricare.ui.resep.ResepActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailDetectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDetectBinding
    private lateinit var detectDetailViewModel: DetectDetailViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var detailAdapter: DetailAdapter
    private var authToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDetectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apiService = Retro().getRetroClientInstance().create(UserApi::class.java)
        val detailRepository = DetailRepository(apiService)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        detailAdapter = DetailAdapter(this, mutableListOf())
        recyclerView.adapter = detailAdapter
        val spanCount = 2
        recyclerView.layoutManager = GridLayoutManager(this, spanCount)

        val buttonAddImage = binding.buttonAddImage

        val status = intent.getIntExtra("status", 0)

        if (status == 1) {
            buttonAddImage.visibility = View.GONE
        } else {
            buttonAddImage.visibility = View.VISIBLE
        }

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("token", null)
        val detectId = intent.getStringExtra("DETECT_ID")
        detailAdapter.setOnDeleteButtonClickListener { detectId, id ->
            if (authToken != null) {
                val apiService = Retro().getRetroClientInstance().create(UserApi::class.java)
                apiService.deleteDetect("Bearer $authToken", detectId, id)
                    .enqueue(object : Callback<DeleteResponse> {
                        override fun onResponse(
                            call: Call<DeleteResponse>,
                            response: Response<DeleteResponse>
                        ) {
                            if (response.isSuccessful) {
                                val deleteResponse = response.body()

                                val deletedItemPosition =
                                    detailAdapter.getDetailDataList().indexOfFirst { it.id == id }
                                if (deletedItemPosition != -1) {
                                    detailAdapter.removeAt(deletedItemPosition)
                                }
                            } else {
                                Toast.makeText(
                                    this@DetailDetectActivity,
                                    "Detect finish failed: ${response.code()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                            Toast.makeText(
                                this@DetailDetectActivity,
                                "Detect finish request failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }
        binding.buttonSearchRecipe.setOnClickListener {
            if (authToken != null && detectId != null) {

                apiService.detectFinish("Bearer $authToken", detectId)
                    .enqueue(object : Callback<FinishResponse> {
                        override fun onResponse(
                            call: Call<FinishResponse>,
                            response: Response<FinishResponse>
                        ) {
                            if (response.isSuccessful) {
                                val finishResponse = response.body()
                                finishResponse?.data?.let { data ->
                                    val intent =
                                        Intent(this@DetailDetectActivity, ResepActivity::class.java)
                                    intent.putExtra("detectId", detectId)
                                    startActivity(intent)
                                    finish()
                                }
                            } else {
                                Toast.makeText(
                                    this@DetailDetectActivity,
                                    "Detect finish failed: ${response.code()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<FinishResponse>, t: Throwable) {
                            Toast.makeText(
                                this@DetailDetectActivity,
                                "Detect finish request failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            } else {
                Toast.makeText(
                    this@DetailDetectActivity,
                    "Token or detectId is null. Handle this case appropriately.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        buttonAddImage.setOnClickListener {
            onBackPressed()
        }

        if (!detectId.isNullOrEmpty()) {
            detectDetailViewModel =
                ViewModelProvider(this, DetailViewModelFactory(detailRepository))
                    .get(DetectDetailViewModel::class.java)
            detectDetailViewModel.fetchDetail(detectId, "Bearer $authToken".orEmpty())
            detectDetailViewModel.detectDetailData.observe(this, { detailDataList ->
                detailAdapter.updateData(detailDataList, status)
            })
        } else {
            Toast.makeText(this, "Invalid detectId", Toast.LENGTH_SHORT).show()
        }
    }
}
