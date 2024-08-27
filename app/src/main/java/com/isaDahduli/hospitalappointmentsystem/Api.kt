package com.isaDahduli.hospitalappointmentsystem

import retrofit2.Call
import retrofit2.http.*

interface Api
{
    @FormUrlEncoded
    @POST("register_user.php")
    fun registerUser(
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("phone") phone: String
    ): Call<ResponseModel>


    @FormUrlEncoded
    @POST("add_appointment.php")
    fun addAppointment(
        @Field("user_id") userId: Int,
        @Field("department") department: String,
        @Field("doctor") doctor: String,
        @Field("date") date: String,
        @Field("time") time: String
    ): Call<ResponseModel>


    @FormUrlEncoded
    @POST("login.php")
    fun loginUser(
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Call<ResponseModel>

    @GET("get_appointment.php")
    fun getAppointmentsForUser(@Query("user_id") userId: Int): Call<AppointmentsResponse>

    @DELETE("delete_appointment.php")
    fun deleteAppointment(@Query("user_id") userId: Int, @Query("appointment_id") appointmentId: Int):Call<ResponseModel>
}