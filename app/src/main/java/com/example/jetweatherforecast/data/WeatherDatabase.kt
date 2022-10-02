package com.example.jetweatherforecast.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jetweatherforecast.model.Favorite

@Database(
    entities = [Favorite::class, com.example.jetweatherforecast.model.Unit::class],
    version = 2,
    exportSchema = false
)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}