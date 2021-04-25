package com.api.tvmaze.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Network  {
    companion object{

        fun retrofitConfig(path: String) : Retrofit {

            return Retrofit.Builder()
                .baseUrl(path)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}