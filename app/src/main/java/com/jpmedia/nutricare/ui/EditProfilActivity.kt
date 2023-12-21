package com.jpmedia.nutricare.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jpmedia.nutricare.R
import com.jpmedia.nutricare.api.ProfilResponse
import com.jpmedia.nutricare.api.Retro
import com.jpmedia.nutricare.api.UserApi
import com.jpmedia.nutricare.databinding.ActivityEditProfilBinding
import com.jpmedia.nutricare.ui.user.UserViewModel
import com.jpmedia.nutricare.utils.reduceFileImage
import com.jpmedia.nutricare.utils.uriToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class EditProfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfilBinding
    private var currentImageUri: Uri? = null
    private lateinit var userViewModel: UserViewModel
    private var defaultProfilData: ProfilResponse.ProfilData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("token", null)
        userViewModel.profilData.observe(this, { profilResponse ->
            profilResponse?.data?.let { profilData ->

                defaultProfilData = profilData
                binding.edtName.setText(profilData.name)
                binding.edtPhone.setText(profilData.phone)
                binding.edtTinggi.setText(profilData.height)
                binding.edtBerat.setText(profilData.weight)
                binding.edtTanggalLahir.setText(profilData.dateOfBirth)


                Glide.with(this)
                    .load(profilData.profilePicture)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profileImage)

            }
        })

        if (authToken != null) {
            userViewModel.loadProfilData(authToken)
        }
        binding.editFoto.setOnClickListener { startGallery() }
        binding.simpan.setOnClickListener {
            lifecycleScope.launch {
                showLoading(true)
                withContext(Dispatchers.IO) {

                    uploadImage()
                }
                showLoading(false)
            }
        }


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
            binding.profileImage.setImageURI(it)
        }
    }

    fun showDatePickerDialog(view: View) {
        val editText = view as EditText
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                // Update the EditText with the selected date
                val dateString = String.format("%04d-%02d-%02d", year, month + 1, day)

                editText.setText(dateString)
            },
            year, month, day
        )

        // Show the date picker dialog
        datePickerDialog.show()
    }

    private fun showLoading(isVisible: Boolean) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val darkBackground = findViewById<View>(R.id.darkBackground)

        if (isVisible) {
            progressBar.visibility = View.VISIBLE
            darkBackground.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
            darkBackground.visibility = View.GONE
        }
    }
    private fun uploadImage() {

        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val requestImageFile = RequestBody.create("image/jpeg".toMediaType(), imageFile)
            val profile_picture = MultipartBody.Part.createFormData("profile_picture", imageFile.name, requestImageFile)
            val gender = RequestBody.create("text/plain".toMediaType(), binding.edtJK.selectedItem.toString())
            val phone = RequestBody.create("text/plain".toMediaType(), binding.edtPhone.text.toString())
            val height = RequestBody.create("text/plain".toMediaType(), binding.edtTinggi.text.toString())
            val weight = RequestBody.create("text/plain".toMediaType(), binding.edtBerat.text.toString())
            val name = RequestBody.create("text/plain".toMediaType(), binding.edtName.text.toString())
            val date_of_birth = RequestBody.create("text/plain".toMediaType(), binding.edtTanggalLahir.text.toString())
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val authToken = sharedPreferences.getString("token", null)
            val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
            retro.postProfil("Bearer $authToken", profile_picture, phone, gender, date_of_birth, height, weight, name)
                .enqueue(object :
                    Callback<ProfilResponse> {
                    override fun onResponse(
                        call: Call<ProfilResponse>,
                        response: Response<ProfilResponse>,
                    ) {
                        if (response.isSuccessful) {
                            showLoading(false)
                            val toast = Toast.makeText(applicationContext,
                                "Profil berhasil diunggah!",
                                Toast.LENGTH_SHORT)
                            toast.show()
                            val Intent = Intent(applicationContext, DetailProfilActivity::class.java)
                            startActivity(Intent)
                            finish()
                        } else {
                            showLoading(false)
                            val errorMessage = when (response.code()) {

                                else -> "Gagal mengirim cerita. Coba lagi."
                            }

                            val toast = Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT)
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

        }
    }


}
