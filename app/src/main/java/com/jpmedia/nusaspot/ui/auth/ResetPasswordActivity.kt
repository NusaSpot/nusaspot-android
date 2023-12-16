package com.jpmedia.nusaspot.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.jpmedia.nusaspot.R
import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi
import com.jpmedia.nusaspot.api.UserRequest
import com.jpmedia.nusaspot.api.UserResponse
import com.jpmedia.nusaspot.databinding.ActivityRegisterBinding
import com.jpmedia.nusaspot.databinding.ActivityResetPasswordBinding
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
                    val otpResponse = response.body()
                    if (otpResponse != null) {
                        redirectToOtp(email)
                    }
                } else {
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

    private fun redirectToOtp(email: String) {
        val intent = Intent(this, OtpResetActivity::class.java)
        intent.putExtra("email", email)
    //    intent.putExtra("reset","reset")
        startActivity(intent)
        finish()
    }
}