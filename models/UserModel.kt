package com.telemed.models

import com.google.gson.annotations.SerializedName


class UserModel : BaseModel() {

    @SerializedName("user")
    var user: User? = null

    @SerializedName("token")
    var token: String? = null

    inner class User : BaseModel() {

        @field:SerializedName("isSuspended")
        val isSuspended: Boolean? = null

        @field:SerializedName("audioSessionRate")
        val audioSessionRate: Int? = null

        @field:SerializedName("created")
        val created: String? = null

        @field:SerializedName("fullName")
        val fullName: String? = null

        @field:SerializedName("isAccountComplete")
        val isAccountComplete: Boolean? = null

        @field:SerializedName("experience")
        val experience: Int? = null

        @field:SerializedName("isEmailVerified")
        val isEmailVerified: Boolean? = null

        @field:SerializedName("pushNotificationAllowed")
        val pushNotificationAllowed: Boolean? = null

        @field:SerializedName("isDeleted")
        val isDeleted: Boolean? = null

        @field:SerializedName("phone")
        val phone: String? = null

        @field:SerializedName("step")
        val step: Int? = null

        @field:SerializedName("_id")
        val id: String? = null

        @field:SerializedName("videoSessionRate")
        val videoSessionRate: Int? = null

        @field:SerializedName("updated")
        val updated: String? = null

        @field:SerializedName("email")
        val email: String? = null
    }

}