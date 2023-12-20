package com.jpmedia.nutricare.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jpmedia.nutricare.databinding.FragmentUserBinding
import com.jpmedia.nutricare.ui.DetectActivity
import com.jpmedia.nutricare.ui.DetailProfilActivity
import com.jpmedia.nutricare.ui.auth.LogoutListener

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
        binding.detailProfil.setOnClickListener {
            val intent = Intent(requireContext(), DetailProfilActivity::class.java)
            startActivity(intent)

        }

        binding.riwayatDeteksi.setOnClickListener{
            val intent = Intent(requireContext(), DetectActivity::class.java)
            startActivity(intent)

        }
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.profilData.observe(viewLifecycleOwner) { profilResponse ->

            val profil = profilResponse?.data
            val guest = profil?.is_guest

            if (guest != "0") {

                binding.detailProfil.visibility = View.GONE
                binding.riwayatDeteksi.visibility = View.GONE
            } else {

                binding.detailProfil.visibility = View.VISIBLE
                binding.riwayatDeteksi.visibility = View.VISIBLE
            }

            binding.profilName.text = profil?.name
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

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}