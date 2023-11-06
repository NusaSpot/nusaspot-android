package com.jpmedia.nusaspot.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.jpmedia.nusaspot.R
import com.jpmedia.nusaspot.api.Retro
import com.jpmedia.nusaspot.api.UserApi
import com.jpmedia.nusaspot.api.UserRequest
import com.jpmedia.nusaspot.api.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {
    private lateinit var edtEmail: EditText
    private lateinit var edtPwd: EditText
    private lateinit var edtName: EditText
    private lateinit var edtPwdconf: EditText
    private lateinit var btnRegister: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        edtEmail = view.findViewById(R.id.edtEmail)
        edtPwd = view.findViewById(R.id.edtPwd)
        edtName = view.findViewById(R.id.name)
        edtPwdconf = view.findViewById(R.id.edtPwdConf)
        btnRegister = view.findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            register()
        }

        return view
    }

    private fun register() {
        val request = UserRequest()
        request.name = edtName.text.toString().trim()
        request.email = edtEmail.text.toString().trim()
        request.password = edtPwd.text.toString().trim()
        request.password_confirmation = edtPwdconf.text.toString().trim()

        val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
        retro.register(request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    val token = userResponse?.data?.token
                    val email = userResponse?.data?.email

                    if (token != null) {
                        saveAuthToken(token)
                        if (email != null) {
                            requestOtp(email)
                        }
                        val toast = Toast.makeText(requireContext(), "Pendaftaran Berhasil Silahkan Masukkan OTP", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                } else {
                    val toast = Toast.makeText(requireContext(), "Gagal mendaftar. Silakan coba lagi.", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t.message?.let { Log.e("error", it) }
            }
        })
    }

    private fun saveAuthToken(token: String) {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    private fun requestOtp(email: String) {
        val requestOtpReg = UserRequest()
        requestOtpReg.email = email // Menggunakan email yang diterima sebagai parameter
        val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
        retro.requestOtp(requestOtpReg).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val otpResponse = response.body()
                    if (otpResponse != null) {
                        redirectToOtp(email)
                    }
                } else {
                    val toast = Toast.makeText(requireContext(), "Failed to request OTP. Please try again.", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t.message?.let { Log.e("error", it) }
            }
        })
    }

    private fun redirectToOtp(email: String) {
        val intent = Intent(requireContext(), OtpActivity::class.java)
        intent.putExtra("email", email) // Menambahkan alamat email ke Intent
        startActivity(intent)
        activity?.finish()
    }
}
