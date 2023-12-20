package com.jpmedia.nutricare.ui.deteksi

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jpmedia.nutricare.R
import com.jpmedia.nutricare.api.DetectStartResponse
import com.jpmedia.nutricare.api.PostDetectResponse
import com.jpmedia.nutricare.api.Retro
import com.jpmedia.nutricare.api.UserApi
import com.jpmedia.nutricare.databinding.FragmentDeteksiBinding
import com.jpmedia.nutricare.ui.DetailDetectActivity
import com.jpmedia.nutricare.utils.getImageUri
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

class DeteksiFragment : Fragment() {

    private var _binding: FragmentDeteksiBinding? = null
    private var currentImageUri: Uri? = null

    private val binding get() = _binding!!
    private var detectId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDeteksiBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("token", null)
        startDetect()
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.detectButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener {
            lifecycleScope.launch {
                showLoading(true)
                withContext(Dispatchers.IO) {

                    uploadImage(authToken)
                }
                showLoading(false)
            }

        }



        return root
    }

    private fun startDetect() {
        val apiService = Retro().getRetroClientInstance().create(UserApi::class.java)
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("token", null)

        if (authToken != null) {
            apiService.getDetectStart("Bearer $authToken").enqueue(object :
                Callback<DetectStartResponse> {
                override fun onResponse(call: Call<DetectStartResponse>, response: Response<DetectStartResponse>) {
                    if (response.isSuccessful) {
                        val detectStartResponse = response.body()
                        detectStartResponse?.data?.let { data ->
                            // Lakukan sesuatu dengan data detectStartResponse, contohnya pindah ke activity lain
//                            val intent = Intent(requireActivity(), PostDetectActivity::class.java)
//                            intent.putExtra("DETECT_ID", data.id.toString())
//                            startActivity(intent)
//                            requireActivity().finish()
                            detectId = data.id.toString()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Detect start failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DetectStartResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Detect start request failed", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "Token is null. Handle this case appropriately.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun uploadImage(authToken: String?) {


        currentImageUri?.let { uri ->
            val image = uriToFile(uri, requireContext()).reduceFileImage()
            val requestImageFile = RequestBody.create("image/jpeg".toMediaType(), image)
            val detectPicture =
                MultipartBody.Part.createFormData("image", image.name, requestImageFile)

            val retro = Retro().getRetroClientInstance().create(UserApi::class.java)

            if (detectId != null) {
                retro.postDetect("Bearer $authToken", detectPicture, detectId!!).enqueue(object :
                    Callback<PostDetectResponse> {
                    override fun onResponse(
                        call: Call<PostDetectResponse>,
                        response: Response<PostDetectResponse>,
                    ) {
                        showLoading(false)
                        if (response.isSuccessful) {
                            showInitialImageState()
                            val intent = Intent(requireActivity(), DetailDetectActivity::class.java)
                            intent.putExtra("DETECT_ID", detectId)
                            intent.putExtra("status", 0)
                            startActivity(intent)
                            requireActivity()
                        } else {
                            val toast = Toast.makeText(
                                requireContext(),
                                "Gagal mengirim detect. Coba lagi.",
                                Toast.LENGTH_SHORT
                            )
                            toast.show()
                        }
                    }

                    override fun onFailure(call: Call<PostDetectResponse>, t: Throwable) {
                        showLoading(false)
                        Log.e("error", t.message ?: "Unknown error")
                        val toast = Toast.makeText(
                            requireContext(),
                            "Gagal mengirim detect. Coba lagi.",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                })
            } else {
                showLoading(false)
                Toast.makeText(requireContext(), "Detect ID tidak valid $detectId", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            showLoading(false)
            Toast.makeText(requireContext(), "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showLoading(isVisible: Boolean) {
        val progressBar = binding.progressBar
        val darkBackground = binding.darkBackground

        if (isVisible) {
            progressBar.visibility = View.VISIBLE
            darkBackground.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
            darkBackground.visibility = View.GONE
        }
    }
    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private fun showInitialImageState() {
      //  binding.previewImageView.setImageURI(null)
        binding.previewImageView.setImageResource(R.drawable.placeholder_image)
    }


    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
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
            binding.previewImageView.setImageURI(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
