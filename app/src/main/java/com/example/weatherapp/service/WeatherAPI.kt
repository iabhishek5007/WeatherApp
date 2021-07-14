package com.example.weatherapp.service

import com.example.weatherapp.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/weather?q=bing%C3%B6l&APPID=903d71205849499fb10207505080b002

interface WeatherAPI {

    @GET("data/2.5/weather?&units=metric&APPID=903d71205849499fb10207505080b002")
    fun getData(
        @Query("q") cityName: String
    ): Single<WeatherModel>
}