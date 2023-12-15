package com.jpmedia.nusaspot.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import com.jpmedia.nusaspot.api.ProfilResponse
import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi
import com.jpmedia.nusaspot.databinding.ActivityEditProfilBinding
import com.jpmedia.nusaspot.utils.reduceFileImage
import com.jpmedia.nusaspot.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditProfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfilBinding
    private var currentImageUri: Uri? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener { uploadImage() }
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        setContentView(binding.root)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }
    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        showLoading(true)
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val requestImageFile = RequestBody.create("image/jpeg".toMediaType(), imageFile)
            val profile_picture = MultipartBody.Part.createFormData("profile_picture", imageFile.name, requestImageFile)
            val gender = RequestBody.create("text/plain".toMediaType(), binding.genderSpinner.selectedItem.toString())
            val phone = RequestBody.create("text/plain".toMediaType(),binding.phoneEditText.text.toString())
            val date_of_birth = RequestBody.create("text/plain".toMediaType(),binding.dateOfBirthEditText.text.toString())
            val authToken = sharedPreferences.getString("token", null)

            val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
            retro.postProfil("Bearer $authToken", profile_picture, phone, gender,date_of_birth).enqueue(object :
                Callback<ProfilResponse> {
                override fun onResponse(
                    call: Call<ProfilResponse>,
                    response: Response<ProfilResponse>,
                ) {
                    if (response.isSuccessful) {
                        showLoading(false)
                        val toast = Toast.makeText(applicationContext,
                            "Cerita berhasil diunggah!",
                            Toast.LENGTH_SHORT)
                        toast.show()
                        val mainIntent = Intent(applicationContext, MainActivity::class.java)
                        mainIntent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(mainIntent)
                        finishAffinity()
                    } else {
                        showLoading(false)
                        val toast = Toast.makeText(applicationContext,
                            "Gagal mengirim cerita. Coba lagi.",
                            Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }

                override fun onFailure(call: Call<ProfilResponse>, t: Throwable) {
                    showLoading(false)

                    t.message?.let { Log.e("error", it) }
                }
            })
        } ?: run {
            showLoading(false)
            Toast.makeText(this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}