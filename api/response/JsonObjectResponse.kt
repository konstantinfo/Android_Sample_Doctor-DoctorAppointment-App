package com.telemed.api.response

import com.google.gson.annotations.SerializedName


/*This class to get all Object type response*/
class JsonObjectResponse<T> : BaseResponse(){
    @SerializedName("data")
    var `object`: T? = null

    @SerializedName("result")
    var `result`: T? = null
}