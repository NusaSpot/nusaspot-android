package com.jpmedia.nusaspot.ui.resep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jpmedia.nusaspot.R
import com.jpmedia.nusaspot.adapter.ResepAdapter
import com.jpmedia.nusaspot.api.FinishResponse
import com.jpmedia.nusaspot.api.Recipe
import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResepActivity : AppCompatActivity(), ResepAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ResepAdapter
    private lateinit var apiService: UserApi
    private lateinit var detectId: String
    private lateinit var authToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resep)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ResepAdapter()
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(this)
        apiService = Retro().getRetroClientInstance().create(UserApi::class.java)
        detectId = intent.getStringExtra("detectId") ?: ""
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("token", "") ?: ""
        fetchRecipes()
    }

    private fun fetchRecipes() {
        if (detectId.isNotEmpty() && authToken.isNotEmpty()) {
            apiService.detectFinish("Bearer $authToken", detectId).enqueue(object : Callback<FinishResponse> {
                override fun onResponse(call: Call<FinishResponse>, response: Response<FinishResponse>) {
                    if (response.isSuccessful) {
                        val finishResponse = response.body()
                        finishResponse?.data?.let { data ->
                            val recipes = data as List<Recipe>
                            adapter.setData(recipes)
                        }
                    } else {
                        Toast.makeText(this@ResepActivity, "Detect finish failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<FinishResponse>, t: Throwable) {
                    Toast.makeText(this@ResepActivity, "Detect finish request failed", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this@ResepActivity, "detectId or authToken is empty. Handle this case appropriately.", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onItemClick(recipe: Recipe) {
        val intent = Intent(this, DetailResepActivity::class.java)
        intent.putExtra("recipeId", recipe.id)
        startActivity(intent)
    }
}
