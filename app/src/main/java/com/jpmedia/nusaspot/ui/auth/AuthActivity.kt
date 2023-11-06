package com.jpmedia.nusaspot.ui.auth

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import android.os.Bundle
import android.view.WindowManager
import com.google.android.material.tabs.TabLayout
import com.jpmedia.nusaspot.R
import com.jpmedia.nusaspot.adapter.ViewPagerAdapter

class AuthActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        supportActionBar?.hide()
        tabLayout = findViewById(R.id.tab_layout)
        viewPager2 = findViewById(R.id.view_pager)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        tabLayout.addTab(tabLayout.newTab().setText("Login"))
        tabLayout.addTab(tabLayout.newTab().setText("Register"))

        val fragmentManager = supportFragmentManager
        adapter = ViewPagerAdapter(fragmentManager, lifecycle)
        viewPager2.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager2.setCurrentItem(tab.position, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Do nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Do nothing
            }
        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }
}



