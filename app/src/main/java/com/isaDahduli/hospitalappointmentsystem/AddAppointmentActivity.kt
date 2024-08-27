package com.isaDahduli.hospitalappointmentsystem

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AddAppointmentActivity : AppCompatActivity()
{
    private lateinit var calendarImage: ImageView
    private lateinit var timeImage: ImageView
    private lateinit var appointmentDate: EditText
    private lateinit var appointmentTime: EditText
    private lateinit var addUpdateButton: Button
    private val appointmentCalendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_appointment)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        calendarImage = findViewById(R.id.image_view_date)
        timeImage = findViewById(R.id.image_view_time)
        addUpdateButton = findViewById(R.id.button_add_appointment)
        appointmentDate = findViewById(R.id.edit_text_appointment_date)
        appointmentTime = findViewById(R.id.edit_text_appointment_time)

        val departmentsWithDoctors = mapOf(
            "Cardiology" to arrayOf("Dr. John Doe", "Dr. Mutlu Soylu"),
            "Neurology" to arrayOf("Dr. Jake Smith", "Dr. Feyza Cilek"),
            "Orthopedics" to arrayOf("Dr. Linda Snow", "Dr. Can Karakurt"),
            "Pediatrics" to arrayOf("Dr. Emily Johnson", "Dr. Mark Brown"),
            "General Surgery" to arrayOf("Dr. Mehmet Erkan", "Dr. Emma Tailor"))

        val spinnerDepartments = findViewById<Spinner>(R.id.spinner_departments)
        val spinnerDoctors = findViewById<Spinner>(R.id.spinner_doctors)

        // Adapter for Departments
        val departmentsAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            departmentsWithDoctors.keys.toTypedArray()
        )
        departmentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDepartments.adapter = departmentsAdapter

        spinnerDepartments.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long)
            {
                val selectedDepartment = parent.getItemAtPosition(position) as String
                val doctors = departmentsWithDoctors[selectedDepartment]

                val doctorsAdapter = ArrayAdapter(
                    this@AddAppointmentActivity,
                    android.R.layout.simple_spinner_item,
                    doctors ?: emptyArray()
                )
                doctorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerDoctors.adapter = doctorsAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>)
            {

            }
        }

        calendarImage.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    appointmentCalendar.set(year, month, day)
                    updateDateInView()
                },
                appointmentCalendar.get(Calendar.YEAR),
                appointmentCalendar.get(Calendar.MONTH),
                appointmentCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        timeImage.setOnClickListener {
            val currentHour = appointmentCalendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = appointmentCalendar.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, hourOfDay, minute ->
                if (hourOfDay in 9..17)
                {
                    appointmentCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    appointmentCalendar.set(Calendar.MINUTE, minute)
                    updateTimeInView()
                }
                else
                {
                    Toast.makeText(this, "Please select a time between 9 AM and 5 PM", Toast.LENGTH_LONG).show()
                }
            }, currentHour, currentMinute, true).show()
        }

        addUpdateButton.setOnClickListener {
            if (validateTime())
            {
                addUpdateButton.isEnabled = false
                addAppointment()
            }
        }
    }

    private fun updateDateInView()
    {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        appointmentDate.setText(sdf.format(appointmentCalendar.time))
    }

    private fun updateTimeInView()
    {
        val myFormat = "HH:mm"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        appointmentTime.setText(sdf.format(appointmentCalendar.time))
    }

    private fun validateTime(): Boolean
    {
        val currentCalendar = Calendar.getInstance()
        if (appointmentCalendar.before(currentCalendar) && appointmentCalendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR))
        {
            Toast.makeText(this, "Please select a future time", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun addAppointment()
    {
        val sharedPreferences = getSharedPreferences("HospitalAppPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)

        if (userId == -1)
        {
            Toast.makeText(this, "User not logged in. Please log in again.", Toast.LENGTH_LONG).show()
            addUpdateButton.isEnabled = true
            return
        }

        val department = findViewById<Spinner>(R.id.spinner_departments).selectedItem.toString()
        val doctor = findViewById<Spinner>(R.id.spinner_doctors).selectedItem.toString()
        val date = appointmentDate.text.toString()
        val time = appointmentTime.text.toString()

        val call = RetrofitClient.api.addAppointment(userId, department, doctor, date, time)
        call.enqueue(object : Callback<ResponseModel>
        {
            override fun onResponse(call: retrofit2.Call<ResponseModel>, response: Response<ResponseModel>)
            {
                addUpdateButton.isEnabled = true
                if (response.isSuccessful)
                {
                    val responseModel = response.body()
                    if (responseModel?.status == "success")
                    {
                        Toast.makeText(this@AddAppointmentActivity, "Appointment added successfully", Toast.LENGTH_LONG).show()
                        scheduleNotification(userId, department, doctor, date, time)
                        val intent = Intent(this@AddAppointmentActivity, MainUserActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else
                    {
                        Toast.makeText(this@AddAppointmentActivity, responseModel?.message, Toast.LENGTH_LONG).show()
                    }
                }
                else
                {
                    Toast.makeText(this@AddAppointmentActivity, "Server error: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseModel>, t: Throwable)
            {
                addUpdateButton.isEnabled = true
                Toast.makeText(this@AddAppointmentActivity, "Failed to add appointment: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun scheduleNotification(userId: Int, department: String, doctor: String, date: String, time: String) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, NotificationReceiver::class.java)
        intent.putExtra("userId", userId)
        intent.putExtra("department", department)
        intent.putExtra("doctor", doctor)
        intent.putExtra("date", date)
        intent.putExtra("time", time)

        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        val appointmentTime = sdf.parse("$date $time")
        val triggerTime = appointmentTime!!.time - (2 * 60 * 60 * 1000)

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }
}
