package com.jpmedia.nutricare.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jpmedia.nutricare.R
import com.jpmedia.nutricare.api.OtpResponse
import com.jpmedia.nutricare.api.Retro
import com.jpmedia.nutricare.api.UserApi
import com.jpmedia.nutricare.api.UserRequest
import com.jpmedia.nutricare.api.UserResponse
import com.jpmedia.nutricare.ui.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpActivity : AppCompatActivity() {
    private lateinit var btnSubmit: Button
    private lateinit var inputCode1: EditText
    private lateinit var inputCode2: EditText
    private lateinit var inputCode3: EditText
    private lateinit var inputCode4: EditText
    private lateinit var inputCode5: EditText
    private lateinit var inputCode6: EditText
    private lateinit var resend: TextView // Inisialisasi TextView untuk resend

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        inputCode1 = findViewById(R.id.inputCode1)
        inputCode2 = findViewById(R.id.inputCode2)
        inputCode3 = findViewById(R.id.inputCode3)
        inputCode4 = findViewById(R.id.inputCode4)
        inputCode5 = findViewById(R.id.inputCode5)
        inputCode6 = findViewById(R.id.inputCode6)
        resend = findViewById(R.id.textResendOTP) // Inisialisasi TextView untuk resend

        val email = intent.getStringExtra("email")
        resend.setOnClickListener {
            showProgressBar(true)
            if (email != null) {
                requestOtp(email)
            }
        }

        btnSubmit = findViewById(R.id.btnSubmitOtp)
        btnSubmit.setOnClickListener {
            showProgressBar(true)
            val inputCode1Text = inputCode1.text.toString().trim()
            val inputCode2Text = inputCode2.text.toString().trim()
            val inputCode3Text = inputCode3.text.toString().trim()
            val inputCode4Text = inputCode4.text.toString().trim()
            val inputCode5Text = inputCode5.text.toString().trim()
            val inputCode6Text = inputCode6.text.toString().trim()

            val otp = "$inputCode1Text$inputCode2Text$inputCode3Text$inputCode4Text$inputCode5Text$inputCode6Text"
            verifyOtp(otp)
        }


        val editTexts = arrayOf(inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6)

        for (i in 0 until editTexts.size - 1) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {

                        editTexts[i + 1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }


        editTexts[5].addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
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
                        showProgressBar(false)
                        val toast = Toast.makeText(
                            this@OtpActivity,
                            "Permintaan OTP berhasil terkirim",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                } else {
                    showProgressBar(false)
                    val toast = Toast.makeText(

                        this@OtpActivity,
                        "Gagal meminta OTP. Silakan coba lagi.",
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

    private fun verifyOtp(otp: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("token", null)
        val email = intent.getStringExtra("email")
        val request = UserRequest()
        request.email = email
        request.otp = otp
        val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
        retro.postOtp(request).enqueue(object : Callback<OtpResponse> {
            override fun onResponse(call: Call<OtpResponse>, response: Response<OtpResponse>) {
                if (response.isSuccessful) {
                    val otpResponse = response.body()
                    val status = otpResponse?.status
                    if (status != "error") {
                        showProgressBar(false)
                        val toast = Toast.makeText(applicationContext, "Verifikasi berhasil, Silahkan lengkapi data anda !", Toast.LENGTH_SHORT)
                        toast.show()
                        if (authToken != null) {
                            redirectToHome(authToken)
                        }
                    } else {
                        showProgressBar(false)
                        Log.e("Error", "Gagal verifikasi OTP. Respons tidak berhasil. Kode status: " + response.code())
                        val toast = Toast.makeText(applicationContext, "Gagal verifikasi OTP. Silakan coba lagi.", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                } else {
                    showProgressBar(false)
                    Log.e("Error", "Gagal verifikasi OTP. Respons tidak berhasil. Kode status: " + response.code())
                    val toast = Toast.makeText(applicationContext, "Gagal verifikasi OTP. Silakan coba lagi.", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }

            override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                showProgressBar(false)
                t.message?.let { Log.e("error", it) }
            }
        })
    }

    private fun redirectToHome(token: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("token", token)
        startActivity(intent)
        finish()
    }
}
