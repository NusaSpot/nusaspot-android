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
import androidx.recyclerview.widget.LinearLayoutManager
import com.jpmedia.nusaspot.adapter.HomeAdapter
import com.jpmedia.nusaspot.api.Recipe
import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi
import com.jpmedia.nusaspot.databinding.FragmentHomeBinding
import com.jpmedia.nusaspot.ui.repository.ResepRepository
import com.jpmedia.nusaspot.model.ResepViewModel
import com.jpmedia.nusaspot.model.ResepViewModelFactory
import com.jpmedia.nusaspot.ui.resep.DetailResepActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var resepViewModel: ResepViewModel

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
        resepViewModel = ViewModelProvider(this, resepViewModelFactory).get(ResepViewModel::class.java)
        homeAdapter = HomeAdapter(object : HomeAdapter.OnRecipeClickListener {
            override fun onRecipeClick(recipe: Recipe) {
                onItemClick(recipe)
            }
        })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = homeAdapter
        }
        resepViewModel.recipeList.observe(viewLifecycleOwner) { recipes ->
            recipes?.let {
                homeAdapter.setRecipes(it)
            }
        }
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("token", null)
        resepViewModel.fetchRecipes("Bearer $authToken".orEmpty())

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
