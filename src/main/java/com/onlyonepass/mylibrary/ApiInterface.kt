package com.onlyonepass.mylibrary

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @POST("clients_api")
    fun GetUser():Call<ResponseData>
}