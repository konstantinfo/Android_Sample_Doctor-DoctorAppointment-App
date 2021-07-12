package com.telemed.models

import com.google.gson.annotations.SerializedName

class SpecialityModel : BaseModel() {

    @SerializedName("isSuspended")
    var isSuspended: Boolean? = null

    @SerializedName("isDeleted")
    var isDeleted: Boolean? = null

    @SerializedName("_id")
    var _id: String? = null

    @SerializedName("specialityName")
    var specialityName: String? = null

    @SerializedName("created")
    var created: String? = null

    @SerializedName("updated")
    var updated: String? = null

    @SerializedName("DoctorCount")
    var DoctorCount: Int? = null

    @SerializedName("specialityIcon")
    var specialityIcon: String? = null
}

