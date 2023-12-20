package com.jpmedia.nusaspot.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jpmedia.nusaspot.R
import com.jpmedia.nusaspot.adapter.HomeAdapter
import com.jpmedia.nusaspot.api.Recipe
import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi
import com.jpmedia.nusaspot.databinding.FragmentHomeBinding
import com.jpmedia.nusaspot.ui.repository.ResepRepository
import com.jpmedia.nusaspot.model.ResepViewModel
import com.jpmedia.nusaspot.model.ResepViewModelFactory
import com.jpmedia.nusaspot.ui.DetailDetectActivity
import com.jpmedia.nusaspot.ui.QuestionActivity
import com.jpmedia.nusaspot.ui.resep.DetailResepActivity
import com.jpmedia.nusaspot.ui.user.UserViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var resepViewModel: ResepViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val apiService = Retro().getRetroClientInstance().create(UserApi::class.java)
        val resepRepository = ResepRepository(apiService)
        val resepViewModelFactory = ResepViewModelFactory(resepRepository)
        resepViewModel =
            ViewModelProvider(this, resepViewModelFactory).get(ResepViewModel::class.java)
        homeAdapter = HomeAdapter(object : HomeAdapter.OnRecipeClickListener {
            override fun onRecipeClick(recipe: Recipe) {
                onItemClick(recipe)
            }
        })

        val sharedPreferences =
            requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("token", null)
        resepViewModel.fetchRecipes("Bearer $authToken".orEmpty())


        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.profilData.observe(viewLifecycleOwner) { profilResponse ->
            // Handle perubahan data profil di sini
            profilResponse?.data?.let { profilData ->


                if(profilData.gender == "male")
                {
                    binding.gender.text = "Laki-Laki"
                }else
                {
                    binding.gender.text = "Perempuan"
                }
                if (profilData.height != null) {
                    binding.detect.visibility = View.VISIBLE
                    binding.noDetect.visibility = View.GONE


                    binding.tinggi.text = "${profilData.height} cm"
                    binding.berat.text = "${profilData.weight} kg"
                    binding.quotes.text = profilData.quotes
                    binding.Nama.text = profilData.name
                    binding.Health.text = profilData.body_status
                    // Mendapatkan tanggal lahir dari profilData
                    val dateOfBirth = profilData.dateOfBirth


                    val birthDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateOfBirth)

                    val calBirth = Calendar.getInstance()
                    calBirth.time = birthDate
                    val calToday = Calendar.getInstance()
                    val age = calToday.get(Calendar.YEAR) - calBirth.get(Calendar.YEAR)
                    val isBeforeBirthday =
                        calToday.get(Calendar.DAY_OF_YEAR) < calBirth.get(Calendar.DAY_OF_YEAR)
                    val finalAge = if (isBeforeBirthday) age - 1 else age
                    binding.umur.text = "$finalAge tahun"

                } else {
                    binding.detect.visibility = View.GONE
                    binding.noDetect.visibility = View.VISIBLE

                }

            }
        }

        if (authToken != null) {
            userViewModel.loadProfilData(authToken)
        }


        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = homeAdapter
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
                    resepViewModel.fetchRecipes("Bearer $authToken".orEmpty())
                } else {
                    for (term in searchTerms) {
                        resepViewModel.searchRecipes("Bearer $authToken", term)
                    }
                    binding.emptyTextView.visibility = View.GONE
                }

                return true
            }
        })

        binding.question.setOnClickListener {
            val intent = Intent(requireActivity(), QuestionActivity::class.java)

            startActivity(intent)
        }

        binding.welcomeCard.setOnClickListener {
            val intent = Intent(requireActivity(), QuestionActivity::class.java)

            startActivity(intent)
        }

        resepViewModel.recipeList.observe(viewLifecycleOwner) { recipes ->
            recipes?.let {
                if (it.isEmpty()) {
                    // Tampilkan pesan jika data resep tidak ditemukan
                    binding.emptyTextView.visibility = View.VISIBLE
                } else {
                    binding.emptyTextView.visibility = View.GONE
                }
                homeAdapter.setRecipes(it)
            }
            if (recipes == null) {
                homeAdapter.setRecipes(emptyList())
                binding.emptyTextView.visibility = View.VISIBLE
            }
        }





        return root
    }




    private fun onItemClick(recipe: Recipe) {
        val intent = Intent(requireContext(), DetailResepActivity::class.java)
        intent.putExtra("recipeId", recipe.id)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
