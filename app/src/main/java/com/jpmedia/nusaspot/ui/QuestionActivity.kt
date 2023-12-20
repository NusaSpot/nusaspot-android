package com.jpmedia.nusaspot.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jpmedia.nusaspot.api.ProfilResponse
import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi
import com.jpmedia.nusaspot.databinding.ActivityQuestionBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Simpan.setOnClickListener {
            postQuestion()
        }
    }

    private fun postQuestion() {

        val height = binding.editTBD.text.toString().trim()
        val weight = binding.editBB.text.toString().trim()

        if (height.isEmpty() || weight.isEmpty()) {
            Toast.makeText(applicationContext, "Isi semua field", Toast.LENGTH_SHORT).show()
            return
        }

        val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("token", null)


        retro.updateBody("Bearer $authToken", height, weight).enqueue(object : Callback<ProfilResponse> {
            override fun onResponse(call: Call<ProfilResponse>, response: Response<ProfilResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "Data berhasil diunggah!", Toast.LENGTH_SHORT).show()
                   val intent = Intent(this@QuestionActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Gagal mengunggah data. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfilResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Terjadi kesalahan. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
                t.message?.let { Log.e("error", it) }
            }
        })
    }
}
