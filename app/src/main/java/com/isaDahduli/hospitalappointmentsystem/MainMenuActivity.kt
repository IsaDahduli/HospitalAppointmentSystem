package com.isaDahduli.hospitalappointmentsystem

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainMenuActivity : AppCompatActivity()
{
    private var RegisterActivityButton: Button? = null
    private var LoginActivityButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        RegisterActivityButton = findViewById<View>(R.id.button_register) as Button
        LoginActivityButton = findViewById<View>(R.id.button_login) as Button
    }

    fun goToRegister(view: View?)
    {
        RegisterActivityButton!!.setOnClickListener {
            val i = Intent(
                this@MainMenuActivity,RegisterActivity::class.java)
            startActivity(i)
        }
    }

    fun goToLogin(view: View?)
    {
        LoginActivityButton!!.setOnClickListener {
            val i = Intent(
                this@MainMenuActivity,LoginActivity::class.java)
            startActivity(i)
        }
    }
}