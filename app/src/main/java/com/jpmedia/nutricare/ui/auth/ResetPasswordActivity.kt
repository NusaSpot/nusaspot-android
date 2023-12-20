package com.jpmedia.nutricare.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.jpmedia.nutricare.R
import com.jpmedia.nutricare.api.Retro
import com.jpmedia.nutricare.api.UserApi
import com.jpmedia.nutricare.api.UserRequest
import com.jpmedia.nutricare.api.UserResponse
import com.jpmedia.nutricare.databinding.ActivityResetPasswordBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding:ActivityResetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRest.setOnClickListener {
            showProgressBar(true)
            val email = binding.edtEmail.text.toString()
            if (email.isNotEmpty()) {
                requestOtp(email)
            } else {
                Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun requestOtp(email: String) {
        val requestOtpReg = UserRequest().apply {
            this.email = email
        }

        val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
        retro.requestOtp(requestOtpReg).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    showProgressBar(false)
                    val otpResponse = response.body()
                    if (otpResponse != null) {
                        redirectToOtp(email)
                    }
                } else {
                    showProgressBar(false)
                    val toast = Toast.makeText(
                        this@ResetPasswordActivity,
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

    private fun redirectToOtp(email: String) {
        val intent = Intent(this, OtpResetActivity::class.java)
        intent.putExtra("email", email)
    //    intent.putExtra("reset","reset")
        startActivity(intent)
        finish()
    }
}