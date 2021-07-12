package com.telemed.api.response



// A generic class that contains response and code about loading this response.
data class Resource<out T>(val code: Int, val response: T, var message: String) {

}
