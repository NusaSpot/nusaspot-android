package com.jpmedia.nusaspot.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.navigation.NavigationBarView
import com.jpmedia.nusaspot.R
import com.jpmedia.nusaspot.databinding.ActivityBottomNavBinding
import com.jpmedia.nusaspot.ui.auth.LoginActivity
import com.jpmedia.nusaspot.ui.auth.LogoutListener


class MainActivity : AppCompatActivity(), LogoutListener {

    private lateinit var binding: ActivityBottomNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!isUserLoggedIn()) {
            redirectWelcome()
        }

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_bottom_nav)
        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        navView.setupWithNavController(navController)
    }
    override fun performLogout() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("token")
        editor.apply()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null) != null
    }

    private fun redirectWelcome() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
