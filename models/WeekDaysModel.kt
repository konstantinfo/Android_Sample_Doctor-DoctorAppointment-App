package com.telemed.models

import com.google.gson.annotations.SerializedName

class WeekDaysModel : BaseModel() {

    @SerializedName("isSelect")
    var isSelect: Boolean? = null

    @SerializedName("code")
    var code: String? = null

    @SerializedName("value")
    var value: String? = null

}

