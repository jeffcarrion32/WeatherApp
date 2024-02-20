package com.example.weatherapp.presentation.ui

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.data.networking.WeatherApiClient
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.domain.model.Main
import com.example.weatherapp.domain.model.NetworkStateResponse
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.model.WeatherResponse
import com.example.weatherapp.presentation.ui.theme.WeatherAppTheme
import com.example.weatherapp.presentation.viewmodel.WeatherViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherNavHost(
                        viewModel = viewModel,
                        startDestination = "searchview"
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String,
    viewModel: WeatherViewModel
) {
    val inputCity = remember { mutableStateOf("") }
    inputCity.value = LocalContext.current
        .getSharedPreferences("appSharedPres", MODE_PRIVATE)
        .getString("userLastSearch", "").orEmpty()

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(route = "searchview") {
            MainScreen(
                viewModel = viewModel,
                navController = navController,
                inputCity = inputCity
            )
        }
        composable(route = "detailview") {
            DetailView(
                response = viewModel.weatherResponse.value,
                navController = navController,
                temperature = viewModel.temperature
            )
        }
    }
}

@Composable
fun MainScreen(
    viewModel: WeatherViewModel,
    navController: NavHostController = rememberNavController(),
    inputCity: MutableState<String>
) {
    when (viewModel.networkResponse.value) {
        is NetworkStateResponse.Failure -> {
            Toast.makeText(
                LocalContext.current,
                "Error getting weather data",
                Toast.LENGTH_SHORT
            ).show()
        }

        is NetworkStateResponse.Loading -> {

        }

        is NetworkStateResponse.Success -> {
            LocalContext.current
                .getSharedPreferences("appSharedPres", MODE_PRIVATE).edit()
                .putString("userLastSearch", inputCity.value)
                .apply()
            navController.navigate("detailview")
            viewModel.networkResponse.value = null
        }

        else -> {}
    }
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = inputCity.value,
            onValueChange = { inputCity.value = it },
            maxLines = 1
        )
        Button(
            onClick = {
                viewModel.makeWeatherRequest(inputCity.value)
            }
        ) {
            Text(text = "Search")
        }
        if (viewModel.networkResponse.value == NetworkStateResponse.Loading) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun DetailView(response: WeatherResponse?, navController: NavHostController, temperature: Double) {
    Column {
        DetailCard(
            details = response?.main,
            weather = response?.weather,
            temperature = temperature
        )

        Button(
            onClick = {
                navController.navigate("searchview")
            }
        ) {
            Text(text = "Back")
        }
    }
}


@Composable
fun DetailCard(details: Main?, weather: List<Weather>?, temperature: Double) {
    Card(modifier = Modifier.fillMaxWidth(1f)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = "https://openweathermap.org/img/wn/${weather?.get(0)?.icon}@2x.png"
                ),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth(1f)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "$temperature °F")
                    Text(text = "Temperature")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = details?.humidity.toString())
                    Text(text = "Humidity")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    WeatherAppTheme {
        WeatherNavHost(
            startDestination = "searchview",
            viewModel = WeatherViewModel(WeatherRepository(WeatherApiClient()))
        )
    }
}