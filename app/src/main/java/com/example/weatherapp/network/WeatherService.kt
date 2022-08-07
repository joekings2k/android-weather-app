package com.example.weatherapp.network

import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.models.CityResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("2.5/weather")
    fun getWeather (
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("units") units: String?,
        @Query("appid") appid:String?
    ): Call<WeatherResponse>

    @GET("1.0/direct")
    fun getLongitudeLatitude(
        @Query("city") city:String,
        @Query("appid") appid:String?
    ): Call<CityResponse>
}