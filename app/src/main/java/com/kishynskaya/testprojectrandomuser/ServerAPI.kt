package com.kishynskaya.testprojectrandomuser

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object ServerAPI {

    private const val BASE_URL = "https://randomuser.me/api/"

    private var builder: Retrofit.Builder =
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)

    private val retrofit = builder.build()

    var netService = retrofit.create<NetService>(NetService::class.java)

    interface NetService {
        @GET(".")
        fun getUsers(
            @Query("user_id") pageNumber: Int = 1,
            @Query("results") results: Int = 20
        ): Single<DataResult>
    }
}