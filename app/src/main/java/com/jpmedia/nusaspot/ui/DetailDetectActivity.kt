package com.jpmedia.nusaspot.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.jpmedia.nusaspot.R
import com.jpmedia.nusaspot.api.DetectDetailResponse
import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi
import com.jpmedia.nusaspot.databinding.ActivityDetailDetectBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailDetectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDetectBinding
    private lateinit var imageView : ImageView
    private lateinit var detectIdTextView : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDetectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageView = binding.image
        detectIdTextView = binding.detectId
       val retro = Retro().getRetroClientInstance().create(UserApi::class.java)

        val detectId = intent.getStringExtra("DETECT_ID")
        if (!detectId.isNullOrEmpty()) {
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val authToken = sharedPreferences.getString("token", null)
            val call = retro.getDetailDetect(detectId, "Bearer $authToken")
             call.enqueue(object : Callback<DetectDetailResponse> {
                override fun onResponse(call: Call<DetectDetailResponse>, response: Response<DetectDetailResponse>) {
                    if (response.isSuccessful) {
                        val detailDetectResponse = response.body()
                        if (!detailDetectResponse?.data.isNullOrEmpty()) {
                            val firstItem = detailDetectResponse?.data?.get(0)
                            Glide.with(this@DetailDetectActivity)
                                .load(firstItem?.image)
                                .into(imageView)
                            detectIdTextView.text = firstItem?.detectId
                        } else {
                            Toast.makeText(this@DetailDetectActivity, "No data available", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@DetailDetectActivity, "Failed to fetch detail", Toast.LENGTH_SHORT).show()

                    }
                }
                override fun onFailure(call: Call<DetectDetailResponse>, t: Throwable) {
                    Toast.makeText(this@DetailDetectActivity, "Network error", Toast.LENGTH_SHORT).show()
                    Log.e("DetailDetectActivity", "Network error", t)
                }
            })
        } else {
            Toast.makeText(this, "Invalid detectId", Toast.LENGTH_SHORT).show()
        }
    }
}