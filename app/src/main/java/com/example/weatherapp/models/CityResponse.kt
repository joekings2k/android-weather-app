package com.example.weatherapp.models

import java.io.Serializable

data class CityResponse (
    val name:String ,
    val localNames:LocalNames,
    val lat:Double,
    val lon:Double,
    val country:String ,
    val state:String
    ):Serializable
