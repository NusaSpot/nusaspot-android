package com.jpmedia.nusaspot.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jpmedia.nusaspot.databinding.FragmentHomeBinding
import com.jpmedia.nusaspot.ui.DetectActivity


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inisialisasi ViewModel
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Observasi perubahan pada userData
        homeViewModel.userData.observe(viewLifecycleOwner) { userResponse ->
            // Update tampilan dengan data pengguna yang diperoleh dari API
            val user = userResponse?.data
            binding.TextView.text = "Welcome, ${user?.name}"
        }

        binding.btnDetect.setOnClickListener {
            val intent = Intent(requireContext(), DetectActivity::class.java)
            startActivity(intent)
        }

        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("token", null)

        if (authToken != null) {
            homeViewModel.loadUserData(authToken)
        }else{
            Toast.makeText(requireContext(), "token kosong", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
