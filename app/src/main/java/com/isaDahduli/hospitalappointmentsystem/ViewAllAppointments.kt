package com.isaDahduli.hospitalappointmentsystem

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAllAppointments : AppCompatActivity()
{
    private lateinit var listView: ListView
    private lateinit var adapter: AppointmentAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_appointments)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        listView = findViewById(R.id.appointmentsListView)
        val userId = getUserId()


        adapter = AppointmentAdapter(this, mutableListOf(), userId)
        listView.adapter = adapter

        fetchAppointments()
    }

    private fun getUserId(): Int
    {
        val sharedPreferences = getSharedPreferences("HospitalAppPrefs", MODE_PRIVATE)
        return sharedPreferences.getInt("user_id", -1) // Returns -1 if user_id is not found
    }

    private fun fetchAppointments()
    {
        val userId = getUserId()

        if (userId == -1)
        {
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitClient.api.getAppointmentsForUser(userId).enqueue(object : Callback<AppointmentsResponse>
        {
            override fun onResponse(call: Call<AppointmentsResponse>, response: Response<AppointmentsResponse>)
            {
                if (response.isSuccessful)
                {
                    val appointmentsResponse = response.body()
                    if (appointmentsResponse != null && appointmentsResponse.status == "success")
                    {
                        val appointments = appointmentsResponse.appointments
                        if (appointments != null && appointments.isNotEmpty())
                        {
                            Log.d("ViewAllAppointments", "Appointments: $appointments")
                            adapter.clear()
                            adapter.addAll(appointments)
                        }

                        else
                        {
                            Toast.makeText(this@ViewAllAppointments, "No appointments found.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else
                    {
                        Toast.makeText(this@ViewAllAppointments, "Failed to load appointments", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    Toast.makeText(this@ViewAllAppointments, "Failed to load appointments", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AppointmentsResponse>, t: Throwable)
            {
                Toast.makeText(this@ViewAllAppointments, "Error: " + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
