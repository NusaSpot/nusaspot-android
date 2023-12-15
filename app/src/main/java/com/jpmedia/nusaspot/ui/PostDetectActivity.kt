package com.jpmedia.nusaspot.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.jpmedia.nusaspot.api.PostDetectResponse
import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi
import com.jpmedia.nusaspot.databinding.ActivityPostDetectBinding
import com.jpmedia.nusaspot.utils.getImageUri
import com.jpmedia.nusaspot.utils.reduceFileImage
import com.jpmedia.nusaspot.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetectBinding
    private var currentImageUri: Uri? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetectBinding.inflate(layoutInflater)
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.detectButton.setOnClickListener { startCamera() }
        val detectId = intent.getStringExtra("DETECT_ID")
        if (detectId !=null){
            Toast.makeText(this, "$detectId", Toast.LENGTH_SHORT).show()
        }
        supportActionBar?.hide()
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
            val image = uriToFile(uri, this).reduceFileImage()
            val requestImageFile = RequestBody.create("image/jpeg".toMediaType(), image)
            val detectPicture =
                MultipartBody.Part.createFormData("image", image.name, requestImageFile)
            val authToken = sharedPreferences.getString("token", null)
            val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
            val detectId = intent.getStringExtra("DETECT_ID")
            if (detectId != null) {
                retro.postDetect("Bearer $authToken", detectPicture, detectId).enqueue(object :
                    Callback<PostDetectResponse> {
                    override fun onResponse(
                        call: Call<PostDetectResponse>,
                        response: Response<PostDetectResponse>,
                    ) {
                        showLoading(false)
                        if (response.isSuccessful) {
                            val toast = Toast.makeText(
                                applicationContext,
                                "Detect berhasil diunggah!",
                                Toast.LENGTH_SHORT
                            )
                            toast.show()

                            val intent = Intent(this@PostDetectActivity, DetailDetectActivity::class.java)
                            intent.putExtra("DETECT_ID", detectId.toString())
                            intent.putExtra("status", 0)
                            startActivity(intent)
                            // Handle berhasil
                        } else {
                            val toast = Toast.makeText(
                                applicationContext,
                                "Gagal mengirim detect. Coba lagi.",
                                Toast.LENGTH_SHORT
                            )
                            toast.show()
                            // Handle gagal
                        }
                    }

                    override fun onFailure(call: Call<PostDetectResponse>, t: Throwable) {
                        showLoading(false)
                        Log.e("error", t.message ?: "Unknown error")
                        val toast = Toast.makeText(
                            applicationContext,
                            "Gagal mengirim detect. Coba lagi.",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                        // Handle kegagalan lainnya
                    }
                })
            } else {
                showLoading(false)
                Toast.makeText(this, "Detect ID tidak valid $detectId", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            showLoading(false)
            Toast.makeText(this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showLoading(isLoading: Boolean) {
       // binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}
