package com.telemed.models

import com.google.gson.annotations.SerializedName

class CalenderModel : BaseModel() {

    @SerializedName("isSelect")
    var isSelect: Boolean? = false

    @SerializedName("weekDay")
    var weekDay: String? = null

    @SerializedName("dateNo")
    var dateNo: String? = null

    @SerializedName("date")
    var date: String? = null

}

