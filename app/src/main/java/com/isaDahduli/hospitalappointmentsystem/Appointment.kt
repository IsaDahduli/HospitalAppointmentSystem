package com.isaDahduli.hospitalappointmentsystem

data class Appointment(
    val id: Int,
    val department: String,
    val doctor: String,
    val date: String,
    val time: String
)

data class AppointmentsResponse(val status: String,val appointments: List<Appointment>)
