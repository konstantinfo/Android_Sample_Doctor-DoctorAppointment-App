package com.telemed.api

import com.telemed.api.request.AppointmentRequest
import com.telemed.api.response.*
import com.telemed.models.UserModel
import com.telemed.utils.Constants
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @POST(Constants.MAIN_URL + "appointment/getConsultantSlots")
    @Headers(
        "Content-Type: application/json",
        "X-TeleMedicine-Version: " + Constants.VERSION,
        "X-TeleMedicine-Platform: " + Constants.OS,
        "Accept-Language: en"
    )
    fun consultantSlots(
        @Header("Authorization") token: String?,
        @Body requestBuilder: RequestBuilder
    ): Call<JsonArrayResponse<SlotsResponse>>


    @POST(Constants.MAIN_URL + "appointment/bookAppointment")
    @Headers(
        "Content-Type: application/json",
        "X-TeleMedicine-Version: " + Constants.VERSION,
        "X-TeleMedicine-Platform: " + Constants.OS,
        "Accept-Language: en"
    )
    fun bookAppointment(
        @Header("Authorization") token: String?,
        @Body request: AppointmentRequest
    ): Call<JsonArrayResponse<UserModel>>

}




