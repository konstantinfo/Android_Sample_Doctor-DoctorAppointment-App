package com.telemed.api.response

import com.google.gson.annotations.SerializedName
import com.telemed.models.BaseModel
import com.telemed.utils.Constants
import com.telemed.utils.DateTimeUtils

class AppointmentDetailsResponse : BaseModel() {

    @field:SerializedName("patient")
    val patient: PatientDetails? = null

    @field:SerializedName("appointment")
    val appointment: AppointmentDetails? = null

    @field:SerializedName("_id")
    val id: String? = null

    @field:SerializedName("bookingDetails")
    val bookingDetails: BookingDetail? = null
}

class AppointmentDetails : BaseModel() {

    @field:SerializedName("speciality")
    val speciality: SpecialityDetails? = null

    @field:SerializedName("fullName")
    val fullName: String? = null

    @field:SerializedName("_id")
    val id: String? = null

    @field:SerializedName("avatar")
    val avatar: String? = null

    @field:SerializedName("city")
    val city: String? = null

    @field:SerializedName("asA")
    val asA: String? = null


    @field:SerializedName("whatsapp")
    val whatsapp: String? = null

    fun isConsultant(): Boolean {
        return asA.equals("consultant", true)
    }
}

class DocumentsItemDetails : BaseModel() {

    @field:SerializedName("fileName")
    var fileName: String? = null

    @field:SerializedName("_id")
    val _id: String? = null

    @field:SerializedName("documentName")
    var documentName: String = ""

    @field:SerializedName("id")
    var id: String = ""

    @field:SerializedName("type")
    var type: String = ""

    @field:SerializedName("date")
    var date: String? = null

    fun isConsultant(): Boolean {
        return type.equals("consultant", true)
    }

    fun isImage(): Boolean {
        return !(documentName.endsWith(Constants.PDF_TYPE) || documentName.endsWith(Constants.WORD_TYPE) || documentName.endsWith(
            Constants.WRD_FILE_TYPE
        ))
    }
}

class BookingDetail : BaseModel() {

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

class PatientDetails : BaseModel() {

    @field:SerializedName("gender")
    val gender: String? = null

    @field:SerializedName("documents")
    val documents: List<DocumentsItemDetails> = arrayListOf()

    @field:SerializedName("prescriptions")
    val prescriptions: List<DocumentsItemDetails> = arrayListOf()

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

    @field:SerializedName("reason")
    var reason: String? = null
}

class SpecialityDetails : BaseModel() {

    @field:SerializedName("specialityName")
    val specialityName: String? = null

    @field:SerializedName("specialityIcon")
    val specialityIcon: String? = null
}

