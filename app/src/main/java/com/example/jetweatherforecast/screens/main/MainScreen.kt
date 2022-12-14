package com.example.jetweatherforecast.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bawp.jetweatherforecast.screens.settings.SettingsViewModel
import com.example.jetweatherforecast.data.DataOrException
import com.example.jetweatherforecast.model.Weather
import com.example.jetweatherforecast.navigation.WeatherScreens
import com.example.jetweatherforecast.utils.formatDate
import com.example.jetweatherforecast.utils.formatDecimals
import com.example.jetweatherforecast.widgets.*

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    city: String?
){
    val unitFromDb = settingsViewModel.unitList.collectAsState().value
    var unit by remember {
        mutableStateOf("metric")
    }
    var isMetric by remember {
        mutableStateOf(false)
    }

    if (!unitFromDb.isNullOrEmpty()) {
        unit = unitFromDb[0].unit.split(" ")[0].lowercase()
        isMetric = unit == "metric"
        val weatherData: State<DataOrException<Weather, Boolean, Exception>> =
            produceState<DataOrException<Weather, Boolean, Exception>>(
                initialValue = DataOrException(loading = true)){
                value = mainViewModel.getWeatherData(city!!, unit)
            }

        if (weatherData.value.loading == true) {
            CircularProgressIndicator()
        } else if (weatherData.value.data != null) {
            MainScaffold(weather = weatherData.value.data!!, navController, isMetric)
        }
    } else {

    }


}

@Composable
fun MainScaffold(weather: Weather, navController: NavController, isMetric: Boolean) {

    Scaffold(topBar = {
        WeatherAppBar(
            title = weather.city.name + ", ${weather.city.country}",
            navController = navController,
            onAddActionClicked = {
                navController.navigate(WeatherScreens.SearchScreen.name)
            },
            elevation = 5.dp) {
        }
    }) {
        MainContent(data = weather, isMetric)
    }
    
}

@Composable
fun MainContent(data: Weather, isMetric: Boolean) {
    val imageUrl = "https://openweathermap.org/img/wn/${data.list[0].weather[0].icon}.png"
    Column(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formatDate(data.list.first().dt),
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSecondary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(6.dp)
        )
        Surface(
            modifier = Modifier
                .padding(4.dp)
                .size(200.dp),
            shape = CircleShape,
            color = Color(0xFFFFC400)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WeatherStateImage(imageUrl = imageUrl)
                Text(
                    text = formatDecimals(data.list.first().temp.day) + "??",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = data.list.first().weather.first().main,
                    fontStyle = FontStyle.Italic
                )
            }
        }
        HumidityWindPressureRow(data, isMetric)
        Divider()
        SunriseSunsetRow(data)
        WeekForecast(data)
    }
}

@Composable
fun WeekForecast(data: Weather) {
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "This Week",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.ExtraBold
        )
        Surface(
            color = Color(0xFFF0F0F0),
            shape = RoundedCornerShape(12.dp),
            elevation = 4.dp
        ) {
//            val otherDaysIndices = listOf(1,2,3,4,5,6)
//            LazyColumn(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                items(otherDaysIndices) { index ->
//                    DayOfWeekForecast(data.list[index])
//                }
//            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (i in 1..6) {
                    DayOfWeekForecast(data.list[i])
                }
            }
        }

    }
}


