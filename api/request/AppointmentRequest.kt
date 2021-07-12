package com.telemed.api.request

import com.google.gson.annotations.SerializedName
import com.telemed.models.BaseModel
import com.telemed.utils.Constants

class AppointmentRequest : BaseModel() {

    @field:SerializedName("consultantId")
    var consultantId: String? = null

    @field:SerializedName("patientDetails")
    var patientDetails: PatientDetails = PatientDetails()

    @field:SerializedName("slotId")
    var slotId: List<String>? = arrayListOf()

    @field:SerializedName("sessionMode")
    var sessionMode: String? = null

    @field:SerializedName("appointmentDate")
    var appointmentDate: String? = null
}

class PatientDetails : BaseModel() {
    @field:SerializedName("fullName")
    var fullName: String? = null

    @field:SerializedName("dob")
    var dob: String? = null

    @field:SerializedName("age")
    var age: Int? = null

    @field:SerializedName("gender")
    var gender: String? = null

    @field:SerializedName("reason")
    var reason: String? = null

    @field:SerializedName("documents")
    var documents: ArrayList<DocumentsDetails>? = null
}


class DocumentsDetails : BaseModel() {
    @field:SerializedName("fileName")
    var fileName: String? = null

    @field:SerializedName("documentName")
    var documentName: String? = null

    @field:SerializedName("date")
    var date: String? = null

    fun isImage(): Boolean {
        return !(documentName!!.endsWith(Constants.PDF_TYPE) || documentName!!.endsWith(Constants.WORD_TYPE) || documentName!!.endsWith(
            Constants.WRD_FILE_TYPE
        ))
    }
}