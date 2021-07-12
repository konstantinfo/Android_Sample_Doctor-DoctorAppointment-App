package com.telemed.api

import android.content.Context
import com.telemed.utils.Utils


class ApiSingleTonCore  // Build retrofit once when creating a single instance

private constructor(context: Context) {
    val apiService: ApiService

    init {
        this.apiService = Utils.getRetrofit(context).create(ApiService::class.java)
    }

    companion object {
        private var instance: ApiSingleTonCore? = null

        fun getInstance(context: Context): ApiSingleTonCore {
            if (instance == null) {
                instance = ApiSingleTonCore(context)
            }
            return instance as ApiSingleTonCore
        }
    }
}