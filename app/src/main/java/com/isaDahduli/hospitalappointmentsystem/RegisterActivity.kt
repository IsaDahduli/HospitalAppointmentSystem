package com.isaDahduli.hospitalappointmentsystem

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity()
{
    private var firstNameEditText: EditText? = null
    private var lastNameEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var confirmPasswordEditText: EditText? = null
    private var emailEditText: EditText? = null
    private var phoneNumberEditText: EditText? = null

    private var addUserRegisterButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        firstNameEditText = findViewById<View>(R.id.edit_text_first_name) as EditText
        lastNameEditText = findViewById<View>(R.id.edit_text_last_name) as EditText
        passwordEditText = findViewById<View>(R.id.edit_text_password_register) as EditText
        confirmPasswordEditText = findViewById<View>(R.id.edit_text_password_confirm) as EditText
        emailEditText = findViewById<View>(R.id.edit_text_email) as EditText
        phoneNumberEditText = findViewById<View>(R.id.edit_text_phone_register) as EditText

        addUserRegisterButton = findViewById<View>(R.id.button_register_add_user) as Button

        addUserRegisterButton?.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser()
    {
        val firstName = firstNameEditText?.text.toString().trim()
        val lastName = lastNameEditText?.text.toString().trim()
        val password = passwordEditText?.text.toString().trim()
        val confirmPassword = confirmPasswordEditText?.text.toString().trim()
        val email = emailEditText?.text.toString().trim()
        val phoneNumber = phoneNumberEditText?.text.toString().trim()

        if (firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword)
        {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        // Call the register API using Retrofit
        val call = RetrofitClient.api.registerUser(firstName, lastName, password, email, phoneNumber)
        call.enqueue(object : Callback<ResponseModel>
        {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>)
            {
                if (response.isSuccessful && response.body() != null)
                {
                    val responseModel = response.body()
                    if (responseModel?.status == "success")
                    {
                        Toast.makeText(this@RegisterActivity, "Registration successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    }
                    else
                    {
                        Toast.makeText(this@RegisterActivity, responseModel?.message, Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable)
            {
                Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
