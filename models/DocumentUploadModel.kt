package com.telemed.models

import com.google.gson.annotations.SerializedName

class DocumentUploadModel : BaseModel() {

    @SerializedName("isSelect")
    var isSelect: Boolean? = null

    @SerializedName("url")
    var url: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    var name: String? = null

}

