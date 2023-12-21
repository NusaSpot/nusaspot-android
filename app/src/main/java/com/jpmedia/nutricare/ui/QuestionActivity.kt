package com.jpmedia.nutricare.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jpmedia.nutricare.R
import com.jpmedia.nutricare.api.ProfilResponse
import com.jpmedia.nutricare.api.Retro
import com.jpmedia.nutricare.api.UserApi
import com.jpmedia.nutricare.databinding.ActivityQuestionBinding
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
            showProgressBar(true)
            postQuestion()
        }
    }

    private fun postQuestion() {

        val height = binding.editTBD.text.toString().trim()
        val weight = binding.editBB.text.toString().trim()

        if (height.isEmpty() || weight.isEmpty()) {
            showProgressBar(false)
            Toast.makeText(applicationContext, "data tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("token", null)


        retro.updateBody("Bearer $authToken", height, weight).enqueue(object : Callback<ProfilResponse> {
            override fun onResponse(call: Call<ProfilResponse>, response: Response<ProfilResponse>) {
                if (response.isSuccessful) {


                    val bodyStatus = response.body()?.data?.body_status
                    showProgressBar(false)

                   val intent = Intent(this@QuestionActivity, CheckNextActivity::class.java)
                    intent.putExtra("bodyStatus", bodyStatus)
                    startActivity(intent)
                    finish()
                } else {
                    showProgressBar(false)
                    Toast.makeText(applicationContext, "Gagal mengunggah data. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfilResponse>, t: Throwable) {
                showProgressBar(false)
                Toast.makeText(applicationContext, "Terjadi kesalahan. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
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
}
