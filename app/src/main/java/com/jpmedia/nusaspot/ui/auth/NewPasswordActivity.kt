package com.jpmedia.nusaspot.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.jpmedia.nusaspot.R
import com.jpmedia.nusaspot.api.ChangePasswordResponse
import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi
import com.jpmedia.nusaspot.api.UserRequest
import com.jpmedia.nusaspot.api.UserResponse
import com.jpmedia.nusaspot.databinding.ActivityNewPasswordBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSubmit.setOnClickListener {

            val email = intent.getStringExtra("email")
            if (email != null && binding.edtPwd.text.isNotBlank() && binding.edtPwdConf.text.isNotBlank()) {
                resetPassword(email)
            } else {
                Toast.makeText(
                    this@NewPasswordActivity,
                    "Email or password fields cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun resetPassword(email: String) {
        // Retrieve values from the form
        val otp = intent.getStringExtra("otp")
        val password = binding.edtPwd.text.toString()
        val password_confirmation = binding.edtPwdConf.text.toString()

        val requestOtpReg = UserRequest().apply {
            this.email = email
            this.otp = otp
            this.password = password
            this.password_confirmation = password_confirmation
        }

        val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
        retro.resetPassword(requestOtpReg).enqueue(object : Callback<ChangePasswordResponse> {
            override fun onResponse(call: Call<ChangePasswordResponse>, response: Response<ChangePasswordResponse>) {
                if (response.isSuccessful) {
                    val otpResponse = response.body()
                    if (otpResponse != null) {
                        redirectToLogin(email)
                    }
                } else {
                    val toast = Toast.makeText(
                        this@NewPasswordActivity,
                        "Failed to request OTP. Please try again.",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }

            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                t.message?.let { Log.e("error", it) }
            }
        })
    }

    private fun redirectToLogin(email: String) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
