package com.jpmedia.nusaspot.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi
import com.jpmedia.nusaspot.api.UserRequest
import com.jpmedia.nusaspot.api.UserResponse
import com.jpmedia.nusaspot.databinding.ActivityLoginBinding
import com.jpmedia.nusaspot.ui.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cirLoginButton.setOnClickListener {
            login()
        }

        binding.tvRegister.setOnClickListener {
            // Handle click on tv_register TextView
            register()
        }

        binding.resetPage.setOnClickListener{
            reset()
        }
    }

    private fun login() {
        val request = UserRequest().apply {
            email = binding.edtEmail.text.toString().trim()
            password = binding.edtPwd.text.toString().trim()
        }

        val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
        retro.login(request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    val token = userResponse?.data?.token
                    val username = userResponse?.data?.name
                    if (token != null && username != null) {
                        saveAuthToken(token)
                        redirectToHome(username)
                        val toast = Toast.makeText(
                            applicationContext,
                            "Login berhasil",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                } else {
                    val toast = Toast.makeText(
                        applicationContext,
                        "Username atau password salah",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t.message?.let { Log.e("error", it) }
            }
        })
    }

    private fun saveAuthToken(token: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    private fun redirectToHome(username: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
        finish()
    }

    private fun register() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)

    }

    private fun reset()
    {
        val intent = Intent(this, ResetPasswordActivity::class.java)
        startActivity(intent)
    }
}
