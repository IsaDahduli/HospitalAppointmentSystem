package com.isaDahduli.hospitalappointmentsystem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AppointmentAdapter(context: Context, private val appointments: List<Appointment>, private val userId: Int) : ArrayAdapter<Appointment>(context, 0, appointments) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.appointment_list_item, parent, false)

        val appointment = appointments[position]

        val idTextView = view.findViewById<TextView>(R.id.text_view_appointment_id)
        val departmentTextView = view.findViewById<TextView>(R.id.text_view_appointment_department)
        val doctorTextView = view.findViewById<TextView>(R.id.text_view_appointment_doctor)
        val dateTextView = view.findViewById<TextView>(R.id.text_view_appointment_date)
        val timeTextView = view.findViewById<TextView>(R.id.text_view_appointment_time)
        val deleteButton = view.findViewById<Button>(R.id.remove_appointment)

        idTextView.text = "ID: ${appointment.id}"
        departmentTextView.text = appointment.department
        doctorTextView.text = appointment.doctor
        dateTextView.text = appointment.date
        timeTextView.text = appointment.time

        deleteButton.setOnClickListener {
            deleteAppointment(userId, appointment.id, position)
        }

        return view
    }

    private fun deleteAppointment(userId: Int, appointmentId: Int, position: Int)
    {
        val call = RetrofitClient.api.deleteAppointment(userId, appointmentId)
        call.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>)
            {
                if (response.isSuccessful && response.body()?.status == "success")
                {
                    (appointments as MutableList).removeAt(position)
                    notifyDataSetChanged()
                    Toast.makeText(context, "Appointment removed", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(context, "Failed to remove appointment", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable)
            {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

