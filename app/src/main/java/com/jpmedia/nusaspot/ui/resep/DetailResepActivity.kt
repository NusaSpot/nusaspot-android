package com.jpmedia.nusaspot.ui.resep

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.jpmedia.nusaspot.api.DetailResepResponse
import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi
import com.jpmedia.nusaspot.databinding.ActivityDetailResepBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailResepActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailResepBinding
    private lateinit var apiService: UserApi
    private lateinit var authToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailResepBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiService = Retro().getRetroClientInstance().create(UserApi::class.java)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("token", null).toString()
        fetchDetailRecipe()
    }

    private fun fetchDetailRecipe() {
        val resepId = intent.getIntExtra("recipeId", 0)
        if(resepId !=null)
        {
            Toast.makeText(this, "${resepId}", Toast.LENGTH_SHORT).show()
        }
        apiService.getDetailRecipe("Bearer $authToken", resepId).enqueue(object : Callback<DetailResepResponse> {
            override fun onResponse(call: Call<DetailResepResponse>, response: Response<DetailResepResponse>) {
                if (response.isSuccessful) {
                    val detailResepResponse = response.body()
                    val imageResepUrl = detailResepResponse?.data?.image ?: ""
                    val title = detailResepResponse?.data?.title ?: ""
                    val ingredients = detailResepResponse?.data?.ingredients ?: ""
                    val tutorials = detailResepResponse?.data?.tutorials ?: ""
                    Glide.with(this@DetailResepActivity)
                        .load(imageResepUrl)
                        .into(binding.imageResep)

                    binding.listBahan.text = ingredients
                    binding.listCaraPenyajian.text = tutorials
                } else {
                    Toast.makeText(this@DetailResepActivity, "Failed to fetch recipe details: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DetailResepResponse>, t: Throwable) {
                Toast.makeText(this@DetailResepActivity, "Failed to fetch recipe details", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

