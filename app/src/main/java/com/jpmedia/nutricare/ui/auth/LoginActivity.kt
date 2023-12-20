package com.jpmedia.nutricare.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jpmedia.nutricare.R
import com.jpmedia.nutricare.api.GuestResponse
import com.jpmedia.nutricare.api.Retro
import com.jpmedia.nutricare.api.UserApi
import com.jpmedia.nutricare.api.UserRequest
import com.jpmedia.nutricare.api.UserResponse
import com.jpmedia.nutricare.databinding.ActivityLoginBinding
import com.jpmedia.nutricare.ui.MainActivity
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
            showProgressBar(true)
            login()
        }

        binding.tvRegister.setOnClickListener {
            register()
        }

        binding.resetPage.setOnClickListener{
            reset()
        }

        binding.guest.setOnClickListener {
            showProgressBar(true)
            loginGuest()
        }
    }

    private fun showProgressBar(isVisible: Boolean) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val darkBackground = findViewById<View>(R.id.darkBackground)

        if (isVisible) {
            progressBar.visibility = View.VISIBLE
            darkBackground.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
            darkBackground.visibility = View.GONE
        }
    }


    private fun loginGuest(){
        val retro = Retro().getRetroClientInstance().create(UserApi::class.java)

        retro.getGuest().enqueue(object : Callback<GuestResponse> {
            override fun onResponse(call: Call<GuestResponse>, response: Response<GuestResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    val token = userResponse?.data?.token
                    val username = userResponse?.data?.name
                    if (token != null && username != null) {
                        saveAuthToken(token)
                        redirectToHome(username)
                    }
                } else {
                    val toast = Toast.makeText(
                        applicationContext,
                        "Username atau password salah",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
                showProgressBar(false)
            }

            override fun onFailure(call: Call<GuestResponse>, t: Throwable) {
                t.message?.let { Log.e("error", it) }
                showProgressBar(false)
            }
        })
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
                    }
                } else {
                    val toast = Toast.makeText(
                        applicationContext,
                        "Username atau password salah",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
                showProgressBar(false)
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t.message?.let { Log.e("error", it) }
                showProgressBar(false)
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
