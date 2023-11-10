package com.jpmedia.nusaspot.ui.user

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jpmedia.nusaspot.databinding.FragmentUserBinding
import com.jpmedia.nusaspot.ui.auth.LogoutListener

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var logoutListener: LogoutListener
    private lateinit var userViewModel: UserViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LogoutListener) {
            logoutListener = context
        } else {
            throw ClassCastException("$context must implement LogoutListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.logoutButton.setOnClickListener {
            logoutListener.performLogout()
        }
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.profilData.observe(viewLifecycleOwner) { profilResponse ->

            val profil = profilResponse?.data
            binding.gender.text = profil?.gender
            binding.dateOfBirth.text = profil?.dateOfBirth
            binding.phone.text = profil?.phone
            profil?.profilePicture?.let { url ->
                Glide.with(this)
                    .load(url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.imageView)
            }

        }



        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("token", null)

        if (authToken != null) {
            userViewModel.loadProfilData(authToken)
        }else{
          //  Toast.makeText(requireContext(), "token kosong", Toast.LENGTH_SHORT).show()
        }

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}