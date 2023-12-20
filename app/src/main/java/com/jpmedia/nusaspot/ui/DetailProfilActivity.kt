package com.jpmedia.nusaspot.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jpmedia.nusaspot.databinding.ActivityDetailProfilBinding
import com.jpmedia.nusaspot.ui.auth.ResetPasswordActivity
import com.jpmedia.nusaspot.ui.user.UserViewModel


class DetailProfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProfilBinding
    private var currentImageUri: Uri? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfilBinding.inflate(layoutInflater)

        binding.editIcon.setOnClickListener {
            val intent = Intent(this@DetailProfilActivity, EditProfilActivity::class.java)
            startActivity(intent)
        }

        binding.changePasswordButton.setOnClickListener {
            val intent = Intent(this@DetailProfilActivity, ResetPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        setContentView(binding.root)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.profilData.observe(this, { profilResponse ->
            // Handle perubahan data profil di sini
            profilResponse?.data?.let { profilData ->
                // Misalnya, tampilkan data pada TextView atau EditText
                binding.profileName.text = profilData.name
                binding.email.text = profilData.email
                if(profilData.gender == "male")
                {
                    binding.jenisKelamin.text = "Laki-Laki"
                }else
                {
                    binding.jenisKelamin.text = "Perempuan"
                }
                binding.noHp.text = profilData.phone
                binding.tinggi.text = "${profilData.height} cm"
                binding.berat.text = "${profilData.weight} kg"


                Glide.with(this)
                    .load(profilData.profilePicture)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profileImage)

            }
        })

        val authToken = sharedPreferences.getString("token", null)
        // Memuat data profil menggunakan UserViewModel
        if (authToken != null) {
            userViewModel.loadProfilData(authToken)
        }
    }





//    private fun uploadImage() {
//        showLoading(true)
//        currentImageUri?.let { uri ->
//            val imageFile = uriToFile(uri, this).reduceFileImage()
//            val requestImageFile = RequestBody.create("image/jpeg".toMediaType(), imageFile)
//            val profile_picture = MultipartBody.Part.createFormData("profile_picture", imageFile.name, requestImageFile)
//            val gender = RequestBody.create("text/plain".toMediaType(), binding.genderSpinner.selectedItem.toString())
//            val phone = RequestBody.create("text/plain".toMediaType(),binding.phoneEditText.text.toString())
//            val date_of_birth = RequestBody.create("text/plain".toMediaType(),binding.dateOfBirthEditText.text.toString())
//            val authToken = sharedPreferences.getString("token", null)
//
//            val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
//            retro.postProfil("Bearer $authToken", profile_picture, phone, gender,date_of_birth).enqueue(object :
//                Callback<ProfilResponse> {
//                override fun onResponse(
//                    call: Call<ProfilResponse>,
//                    response: Response<ProfilResponse>,
//                ) {
//                    if (response.isSuccessful) {
//                        showLoading(false)
//                        val toast = Toast.makeText(applicationContext,
//                            "Cerita berhasil diunggah!",
//                            Toast.LENGTH_SHORT)
//                        toast.show()
//                        val mainIntent = Intent(applicationContext, MainActivity::class.java)
//                        mainIntent.flags =
//                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        startActivity(mainIntent)
//                        finishAffinity()
//                    } else {
//                        showLoading(false)
//                        val toast = Toast.makeText(applicationContext,
//                            "Gagal mengirim cerita. Coba lagi.",
//                            Toast.LENGTH_SHORT)
//                        toast.show()
//                    }
//                }
//
//                override fun onFailure(call: Call<ProfilResponse>, t: Throwable) {
//                    showLoading(false)
//
//                    t.message?.let { Log.e("error", it) }
//                }
//            })
//        } ?: run {
//            showLoading(false)
//            Toast.makeText(this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun showLoading(isLoading: Boolean) {
//        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
//    }


}