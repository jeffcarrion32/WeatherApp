package com.example.weatherapp

import android.os.Bundle
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.weatherapiresponse.NetworkStateResponse
import com.example.weatherapp.weatherapiresponse.Weather
import com.example.weatherapp.weatherapiresponse.WeatherResponse
import com.example.weatherapp.weatherviewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

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
    startDestination : String,
    viewModel: WeatherViewModel
) {
    val inputCity = remember { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(route = "searchview") {
            MainScreen(viewModel = viewModel, navController = navController, inputCity = inputCity)
        }
        composable(route = "detailview") {
            DetailView(response = viewModel.weather, navController = navController)
        }
    }
}

@Composable
fun MainScreen(
    viewModel: WeatherViewModel,
    navController: NavHostController = rememberNavController(),
    inputCity: MutableState<String>
) {
    when(viewModel.response.value) {
        is NetworkStateResponse.Failure -> {

        }
        NetworkStateResponse.Loading -> {

        }
        is NetworkStateResponse.Success -> {
            navController.navigate("detailview")
            viewModel.response.value = null
        }
        else -> {}
    }
    Column {
        TextField(
            value = inputCity.value,
            onValueChange = { inputCity.value = it },
            maxLines = 1
        )
        Button(
            onClick = {
                viewModel.makeRequest(inputCity.value)
            }
        ) {
            Text(text = "Search")
        }
    }
}

@Composable
fun DetailView(response: WeatherResponse?, navController: NavHostController) {
    Column {
        response?.weather?.forEach {
            DetailCard(details = it)
        }

        Button(
            onClick = {
                navController.navigate("searchview") }
        ) {
            Text(text = "Back")
        }
    }
}


@Composable
fun DetailCard(details: Weather) {
    Card(modifier = Modifier.fillMaxWidth(1f)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = "https://openweathermap.org/img/wn/${details.icon}@2x.png"
                ),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth(1f)
            ) {
                Text(text = details.main)
                Text(text = details.description)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        WeatherNavHost(
            startDestination = "searchview",
            viewModel = WeatherViewModel()
        )
    }
}