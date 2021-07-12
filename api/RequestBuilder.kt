package com.telemed.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class RequestBuilder : Serializable {

    @SerializedName("_id")
    var id: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("countryCode")
    var countryCode: String? = null

    @SerializedName("phone")
    var mobile: String? = null

    @SerializedName("device_token")
    var deviceToken: String? = null

    @SerializedName("role")
    var role: Int? = null

    @SerializedName("otp")
    var otp: Int? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("newPassword")
    var newPassword: String? = null

    @SerializedName("currentPassword")
    var currentPassword: String? = null

    @SerializedName("conf_password")
    var confPassword: String? = null

    @SerializedName("token")
    var token: String? = null

    @SerializedName("username")
    var username: String? = null

    @SerializedName("firstName")
    var firstName: String? = null

    @SerializedName("lastName")
    var lastName: String? = null

    @SerializedName("tempToken")
    var tempToken: String? = null

    @SerializedName("device_model")
    var deviceModel: String? = null

    @SerializedName("file")
    var file: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("key")
    var key: String? = null
    @SerializedName("docId")
    var docId: String? = null

    @SerializedName("fullName")
    var fullName: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("hashtag")
    var hashTag: String? = null

    @SerializedName("timeCreated")
    var timeCreated: String? = null

    @SerializedName("search")
    var search: String? = null

    @SerializedName("lat")
    var lat: Double? = null

    @SerializedName("lng")
    var lng: Double? = null

    @SerializedName("address")
    var address: String? = null

    @SerializedName("memory")
    var memory: String? = null

    @SerializedName("collectionId")
    var collectionId: String? = null

    @SerializedName("category")
    var category: String? = null

    @SerializedName("itemId")
    var itemId: String? = null

    @SerializedName("countryCodeName")
    var countryCodeName: String? = null

    @SerializedName("id")
    var _id: String? = null

    @SerializedName("comment")
    var comment: String? = null

    @SerializedName("pagetoken")
    var pageToken: String? = null

    @SerializedName("flag")
    var flag: String? = null

    @SerializedName("date")
    var date: String? = null

    @SerializedName("consultantId")
    var consultantId: String? = null

    @SerializedName("offset")
    var offset: String? = null
}

