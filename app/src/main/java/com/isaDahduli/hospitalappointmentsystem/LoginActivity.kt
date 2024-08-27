package com.isaDahduli.hospitalappointmentsystem

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity()
{
    private lateinit var phoneNumberLoginEditText: EditText
    private lateinit var passwordLoginEditText: EditText
    private lateinit var loginUserButton: Button

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        phoneNumberLoginEditText = findViewById(R.id.edit_text_phone_login)
        passwordLoginEditText = findViewById(R.id.edit_text_password_login)
        loginUserButton = findViewById(R.id.button_login_enter_user)

        loginUserButton.setOnClickListener {
            val phone = phoneNumberLoginEditText.text.toString().trim()
            val password = passwordLoginEditText.text.toString().trim()

            if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password))
            {
                Toast.makeText(this, "Please enter both phone number and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val call = RetrofitClient.api.loginUser(phone, password)
            call.enqueue(object : Callback<ResponseModel>
            {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>)
                {
                    if (response.isSuccessful && response.body()?.status == "success")
                    {
                        val sharedPreferences = getSharedPreferences("HospitalAppPrefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putInt("user_id", response.body()?.user_id ?: -1) // Store user_id or -1 if null
                        editor.apply()

                        val intent = Intent(this@LoginActivity, MainUserActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else
                    {
                        Toast.makeText(this@LoginActivity, "Invalid phone number or password", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ResponseModel>, t: Throwable)
                {
                    Toast.makeText(this@LoginActivity, "Error: " + t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
