package com.telemed.api.response

import com.google.gson.annotations.SerializedName
import com.telemed.models.BaseModel
import com.telemed.utils.Constants
import com.telemed.utils.DateTimeUtils

class AppointmentResponse : BaseModel() {

    @field:SerializedName("patient")
    val patient: Patient? = null

    @field:SerializedName("appointment")
    val appointment: Appointment? = null

    @field:SerializedName("_id")
    val id: String? = null

    @field:SerializedName("isCanceled")
    val isCanceled: Boolean? = false

    @field:SerializedName("bookingDetails")
    val bookingDetails: BookingDetails? = null
}

class Appointment : BaseModel() {

    @field:SerializedName("speciality")
    val speciality: Speciality? = null

    @field:SerializedName("fullName")
    val fullName: String? = ""

    @field:SerializedName("_id")
    val id: String? = null

    @field:SerializedName("avatar")
    val avatar: String? = null

    @field:SerializedName("asA")
    val asA: String? = null

    @field:SerializedName("whatsapp")
    val whatsapp: String? = null
}

class DocumentsItem : BaseModel() {

    @field:SerializedName("fileName")
    val fileName: String? = null

    @field:SerializedName("_id")
    val id: String? = null
}

class BookingDetails : BaseModel() {

    @field:SerializedName("date")
    val date: String? = null

    @field:SerializedName("mode")
    val mode: String? = null

    @field:SerializedName("slots")
    val slots: List<SlotsItem?>? = null

    @field:SerializedName("totalPayable")
    val totalPayable: String? = null

    @field:SerializedName("_id")
    val id: String? = null

    fun isVideoMode(): Boolean {
        return mode.equals("video", true)
    }

    fun showMode(): String {
        return if (isVideoMode()) {
            "Mode: Video Call"
        } else {
            "Mode: Audio Call"
        }
    }

    fun showDate(): String {
        var slotTime = ""
        slotTime = if (slots!!.size == 1) {
            slots.get(0)?.slotTime!!
        } else {
            slots.get(0)?.slotTime!!.split("-").get(0) + " - " + slots.get(
                slots.size - 1
            )?.slotTime!!.split("-").get(1)
        }
        return DateTimeUtils.getDateFromUTCString(
            date,
            Constants.DATE_FORMAT_MONGO_DB,
            Constants.DATE_FORMAT_dd_MMM_yy
        ) + ", " + slotTime
    }

    fun isAvailable(): Boolean {
        return DateTimeUtils.convertDateTimeToMilliSeconds(
            date!!.split("T")[0] + " " + slots!!.get(0)?.slotTime!!.split("-").get(0),
            Constants.DATE_TIME_FORMAT_MONGO_DB
        ) < System.currentTimeMillis()
    }
}

class Patient : BaseModel() {

    @field:SerializedName("gender")
    val gender: String? = null

    @field:SerializedName("documents")
    val documents: List<DocumentsItem?>? = null

    @field:SerializedName("dob")
    val dob: String? = null

    @field:SerializedName("fullName")
    val fullName: String? = null

    @field:SerializedName("_id")
    val id: String? = null

    @field:SerializedName("age")
    val age: Int? = null

    @field:SerializedName("whatsapp")
    val whatsapp: String? = null
}

class Speciality : BaseModel() {

    @field:SerializedName("specialityName")
    val specialityName: String? = null

    @field:SerializedName("specialityIcon")
    val specialityIcon: String? = null
}

class SlotsItem : BaseModel() {

    @field:SerializedName("slotTime")
    val slotTime: String? = null

    @field:SerializedName("utcTime")
    val utcTime: String? = null

    @field:SerializedName("isBooked")
    val isBooked: Boolean? = null

    @field:SerializedName("_id")
    val id: String? = null

    @field:SerializedName("bookingId")
    val bookingId: String? = null
}
