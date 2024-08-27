package com.isaDahduli.hospitalappointmentsystem

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
class MainUserActivity : AppCompatActivity()
{
    private var AddAppointmentButton: Button? = null
    private var ViewAllAppointmentsButton: Button? = null


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_user)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        AddAppointmentButton = findViewById<View>(R.id.button_go_to_add_appointment) as Button
        ViewAllAppointmentsButton = findViewById<View>(R.id.button_view_all_appointments) as Button
    }

    fun goToAdd(view: View?)
    {
        AddAppointmentButton!!.setOnClickListener {
            val i = Intent(
                this@MainUserActivity,AddAppointmentActivity::class.java)
            startActivity(i)
        }
    }

    fun goToViewAll(view: View?)
    {
        ViewAllAppointmentsButton!!.setOnClickListener {
            val i = Intent(
                this@MainUserActivity,ViewAllAppointments::class.java)
            startActivity(i)
        }
    }

}