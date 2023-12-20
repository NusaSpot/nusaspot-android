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
import com.jpmedia.nutricare.api.Retro
import com.jpmedia.nutricare.api.UserApi
import com.jpmedia.nutricare.api.UserRequest
import com.jpmedia.nutricare.api.UserResponse
import com.jpmedia.nutricare.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            showProgressBar(true)
            register()

        }

        binding.tvLogin.setOnClickListener {
            login()
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
    private fun register() {
        val request = UserRequest().apply {
            name = binding.name.text.toString().trim()
            email = binding.edtEmail.text.toString().trim()
            password = binding.edtPwd.text.toString().trim()
            password_confirmation = binding.edtPwdConf.text.toString().trim()
        }

        val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
        retro.register(request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    val token = userResponse?.data?.token
                    val email = userResponse?.data?.email

                    if (token != null) {
                        saveAuthToken(token)
                        if (email != null) {
                            requestOtp(email)
                        }
                        showProgressBar(false)
                        val toast = Toast.makeText(
                            applicationContext,
                            "Pendaftaran Berhasil Silahkan Masukkan OTP",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                } else {
                    showProgressBar(false)
                    val toast = Toast.makeText(
                        applicationContext,
                        "Gagal mendaftar. Silakan coba lagi.",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showProgressBar(false)
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

    private fun requestOtp(email: String) {
        val requestOtpReg = UserRequest().apply {
            this.email = email
        }

        val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
        retro.requestOtp(requestOtpReg).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val otpResponse = response.body()
                    if (otpResponse != null) {
                        redirectToOtp(email)
                    }
                } else {
                    val toast = Toast.makeText(
                        this@RegisterActivity,
                        "Gagal meminta OTP. Silakan coba lagi.",
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


    private fun redirectToOtp(email: String) {
        val intent = Intent(this, OtpActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
        finish()
    }

    private fun login() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }
}
