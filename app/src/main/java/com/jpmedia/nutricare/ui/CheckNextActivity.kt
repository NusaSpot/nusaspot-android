package com.jpmedia.nutricare.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jpmedia.nutricare.R
import com.jpmedia.nutricare.databinding.ActivityCheckNextBinding
import com.jpmedia.nutricare.databinding.ActivityDetailDetectBinding
import com.jpmedia.nutricare.ui.ahligizi.AhliGiziFragment

class CheckNextActivity : AppCompatActivity() {

    private lateinit var binding:ActivityCheckNextBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckNextBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val status = intent.getStringExtra("bodyStatus")
        binding.status.text = status

        binding.cirLoginButton.setOnClickListener {

            if (binding.checkBoxAgree.isChecked) {
                binding.textHealthCondition.visibility = View.GONE
                binding.loadingimages.visibility = View.GONE
                binding.status.visibility = View.GONE
                binding.checkBoxAgree.visibility = View.GONE
                binding.cirLoginButton.visibility = View.GONE
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val fragmentAhliGizi = AhliGiziFragment()

                fragmentTransaction.replace(R.id.fragmentContainer, fragmentAhliGizi)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()

            } else {
                val intent = Intent(this@CheckNextActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@CheckNextActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}