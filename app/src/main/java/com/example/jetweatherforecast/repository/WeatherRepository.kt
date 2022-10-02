package com.example.jetweatherforecast.repository

import com.example.jetweatherforecast.data.DataOrException
import com.example.jetweatherforecast.model.Weather
import com.example.jetweatherforecast.network.WeatherAPI
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherAPI) {
    suspend fun getWeather(cityQuery: String, unit: String):
            DataOrException<Weather, Boolean, Exception> {
        val response = try {
            api.getWeather(query = cityQuery, units = unit)
        }catch (e: Exception) {
            return DataOrException(e = e)
        }
        return DataOrException(data = response)
    }
}