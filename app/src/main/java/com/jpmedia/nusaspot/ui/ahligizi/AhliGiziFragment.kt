package com.jpmedia.nusaspot.ui.ahligizi

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jpmedia.nusaspot.adapter.NutritionistAdapter
import com.jpmedia.nusaspot.api.NutritionistData
import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi
import com.jpmedia.nusaspot.databinding.FragmentAhliGiziBinding
import com.jpmedia.nusaspot.model.NutritionistViewModel
import com.jpmedia.nusaspot.model.NutritionistViewModelFactory
import com.jpmedia.nusaspot.ui.repository.NutritionistRepository
import com.jpmedia.nusaspot.ui.resep.DetailResepActivity

class AhliGiziFragment : Fragment() {
    private var _binding: FragmentAhliGiziBinding? = null
    private val binding get() = _binding!!
    private lateinit var nutritionistAdapter: NutritionistAdapter
    private lateinit var nutritionistViewModel: NutritionistViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAhliGiziBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val apiService = Retro().getRetroClientInstance().create(UserApi::class.java)
        val nutritionistRepository = NutritionistRepository(apiService)
        val nutritionistViewModelFactory = NutritionistViewModelFactory(nutritionistRepository)
        nutritionistViewModel =
            ViewModelProvider(this, nutritionistViewModelFactory).get(NutritionistViewModel::class.java)
        nutritionistAdapter = NutritionistAdapter(object : NutritionistAdapter.OnNutritionistClickListener {
            override fun onNutritionistClick(phone: String) {
                // Panggil fungsi atau lakukan aksi yang sesuai dengan klik item di sini
                openWhatsAppChat(phone)
            }
        })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = nutritionistAdapter
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val sharedPreferences =
                    requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val authToken = sharedPreferences.getString("token", null)
                val searchTerms = newText.orEmpty().split("\\s+".toRegex())
                if (searchTerms.isEmpty() || searchTerms.all { it.isBlank() }) {
                    binding.emptyTextView.visibility = View.GONE
                    nutritionistViewModel.getNutritionist("Bearer $authToken".orEmpty())
                } else {
                    for (term in searchTerms) {
                        nutritionistViewModel.searchNutritionists("Bearer $authToken", term)
                    }
                    binding.emptyTextView.visibility = View.GONE
                }

                return true
            }
        })

        nutritionistViewModel.recipeList.observe(viewLifecycleOwner) { nutritionists ->
            nutritionists?.let {
                if (it.isEmpty()) {
                    binding.emptyTextView.visibility = View.VISIBLE
                } else {
                    binding.emptyTextView.visibility = View.GONE
                }
                nutritionistAdapter.setNutritionists(it)
            }
            if (nutritionists == null) {
                nutritionistAdapter.setNutritionists(emptyList())
                binding.emptyTextView.visibility = View.VISIBLE
            }
        }
        val sharedPreferences =
            requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("token", null)
        nutritionistViewModel.getNutritionist("Bearer $authToken".orEmpty())

        return root
    }

    private fun openWhatsAppChat(phone: String) {
        // Contoh aksi: Buka obrolan WhatsApp dengan nomor telepon yang diberikan
        val url = "https://api.whatsapp.com/send?phone=$phone"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

