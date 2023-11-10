package com.jpmedia.nusaspot.ui.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.jpmedia.nusaspot.R
import com.jpmedia.nusaspot.api.OtpResponse
import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi
import com.jpmedia.nusaspot.api.UserRequest
import com.jpmedia.nusaspot.ui.MainActivity
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
    private lateinit var resend :EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        val inputMobile = findViewById<TextView>(R.id.inputMobile)

        inputCode1 = findViewById(R.id.inputCode1)
        inputCode2 = findViewById(R.id.inputCode2)
        inputCode3 = findViewById(R.id.inputCode3)
        inputCode4 = findViewById(R.id.inputCode4)
        inputCode5 = findViewById(R.id.inputCode5)
        inputCode6 = findViewById(R.id.inputCode6)

        btnSubmit = findViewById(R.id.btnSubmitOtp)


        btnSubmit.setOnClickListener {
            val inputCode1Text = inputCode1.text.toString().trim()
            val inputCode2Text = inputCode2.text.toString().trim()
            val inputCode3Text = inputCode3.text.toString().trim()
            val inputCode4Text = inputCode4.text.toString().trim()
            val inputCode5Text = inputCode5.text.toString().trim()
            val inputCode6Text = inputCode6.text.toString().trim()

            val otp = "$inputCode1Text$inputCode2Text$inputCode3Text$inputCode4Text$inputCode5Text$inputCode6Text"
            verifyOtp(otp)
        }

        // Membuat array dari EditText
        val editTexts = arrayOf(inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6)

        // Menambahkan teks changed listener ke setiap EditText
        for (i in 0 until editTexts.size - 1) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        // Jika panjang teks adalah 1 karakter, pindah fokus ke EditText berikutnya
                        editTexts[i + 1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }

        // Listener untuk EditText terakhir
        editTexts[5].addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Ini adalah EditText terakhir, di sini Anda dapat meng-handle aksi setelah mengisi kode OTP terakhir
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    fun resendOTP(view: View) {
        val toast = Toast.makeText(applicationContext, "Kirim ulang OTP, Fitur ini belum diaktifkan guys !", Toast.LENGTH_SHORT)
        toast.show()
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
                        val toast = Toast.makeText(applicationContext, "Verifikasi berhasil, Silahkan lengkapi data anda !", Toast.LENGTH_SHORT)
                        toast.show()
                        if (authToken != null) {
                            redirectToHome(authToken)
                        }
                    } else {
                        Log.e("Error", "Gagal verifikasi OTP. Respons tidak berhasil. Kode status: " + response.code())
                        val toast = Toast.makeText(applicationContext, "Gagal verifikasi OTP. Silakan coba lagi.", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                } else {
                    Log.e("Error", "Gagal verifikasi OTP. Respons tidak berhasil. Kode status: " + response.code())
                    val toast = Toast.makeText(applicationContext, "Gagal verifikasi OTP. Silakan coba lagi.", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }

            override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
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
