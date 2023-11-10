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
import com.jpmedia.nusaspot.ui.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {
    private lateinit var edtEmail: EditText
    private lateinit var edtPwd: EditText
    private lateinit var btnLogin: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        edtEmail = view.findViewById(R.id.edtEmail)
        edtPwd = view.findViewById(R.id.edtPwd)
        btnLogin = view.findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            login()
        }

        return view
    }

    private fun login() {
        val request = UserRequest()
        request.email = edtEmail.text.toString().trim()
        request.password = edtPwd.text.toString().trim()

        val retro = Retro().getRetroClientInstance().create(UserApi::class.java)
        retro.login(request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    val token = userResponse?.data?.token
                    val username = userResponse?.data?.name
                    if (token != null && username != null) {
                        saveAuthToken(token)
                        redirectToHome(username)
                        val toast = Toast.makeText(requireContext(), "Login berhasil", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                } else {
                    val toast = Toast.makeText(requireContext(), "Username atau password salah", Toast.LENGTH_SHORT)
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


    private fun redirectToHome(username: String) {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
        activity?.finish()
    }
}
