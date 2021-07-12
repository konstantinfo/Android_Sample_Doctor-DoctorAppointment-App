package com.telemed.api.response


import com.google.gson.annotations.SerializedName

/**
 * Created by Rajpal on 10/12/2019
 */

/*This class to get all List type response*/
class JsonArrayResponse<T> : BaseResponse(){
    @SerializedName("data")
    var list: List<T>? = null

    @SerializedName("predictions")
    var predictions: List<T>? = null
}