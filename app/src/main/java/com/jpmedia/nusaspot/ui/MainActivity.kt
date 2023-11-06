package com.jpmedia.nusaspot.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.jpmedia.nusaspot.R
import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi
import com.jpmedia.nusaspot.api.UserResponse
import com.jpmedia.nusaspot.ui.auth.AuthActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var btnPageMaps: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("token", null)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        if (authToken != null) {
            loadUsers(authToken)
        }
        if (!isUserLoggedIn()) {
            redirectWelcome()
        }
        btnLogout.setOnClickListener {
            performLogout()
        }
        btnPageMaps = findViewById(R.id.btnMapsPage)
        btnPageMaps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loadUsers(token: String) {
        val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
        val bearerToken = "Bearer $token"
        retro.getUsers(bearerToken).enqueue(object :
            Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val usersResponse = response.body()
                    if (usersResponse != null) {
                        val users = usersResponse.data
                        val usersName = findViewById<TextView>(R.id.TextView)
                        usersName.text = "Welcome, ${users?.name}"
                    }
                } else {
                    val toast = Toast.makeText(applicationContext, "Data tidak ditemukan", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {

            }
        })
    }

    private fun performLogout() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("token")
        editor.apply()
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null) != null
    }

    private fun redirectWelcome() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}