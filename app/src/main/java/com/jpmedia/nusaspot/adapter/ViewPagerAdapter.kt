package com.jpmedia.nusaspot.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jpmedia.nusaspot.ui.auth.LoginFragment
import com.jpmedia.nusaspot.ui.auth.RegisterFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return if (position == 1) {
            RegisterFragment()
        } else {
            LoginFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}
