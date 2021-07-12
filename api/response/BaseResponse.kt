package com.telemed.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


open class BaseResponse {
    @SerializedName("type")
    @Expose
    var type: Boolean? = null
    @SerializedName("limit")
    @Expose
    var limit: Int? = null
    @SerializedName("page")
    @Expose
    var page: String? = null
    @SerializedName("next")
    @Expose
    var next: Boolean? = null

    @field:SerializedName("message")
    var message: String? = null

    @field:SerializedName("success")
    val success: Boolean? = null

    @field:SerializedName("nextpage")
    val nextpage: String? = null

    @field:SerializedName("totalTickets")
    val totalTickets: Int? = null

    @field:SerializedName("isAvailable")
    val isAvailable: Boolean? = null

    @field:SerializedName("isPending")
    val isPending: Boolean? = null

    @field:SerializedName("isPendingPayment")
    val isPendingPayment: Boolean? = null

}