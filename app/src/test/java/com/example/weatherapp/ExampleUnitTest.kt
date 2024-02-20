package com.example.weatherapp

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.ViewModelProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.domain.model.Clouds
import com.example.weatherapp.domain.model.Coord
import com.example.weatherapp.domain.model.Main
import com.example.weatherapp.domain.model.Sys
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.model.WeatherResponse
import com.example.weatherapp.domain.model.Wind
import com.example.weatherapp.presentation.ui.MainActivity
import com.example.weatherapp.presentation.viewmodel.WeatherViewModel
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@ExperimentalAnimationApi
@RunWith(AndroidJUnit4::class)
class ExampleUnitTest {
    private lateinit var activity: MainActivity
    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setUp() {
        // Create an instance of MainActivity
        activity = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .get()

        // Get ViewModelProvider and instantiate MyViewModel
        viewModel = ViewModelProvider(activity)[WeatherViewModel::class.java]
    }


    @Test
    fun addition_isCorrect() {
        val weatherResponse = weatherResponseMadrid()
        viewModel.weatherResponse.value = weatherResponse

    }
    private fun weatherResponseMadrid(): WeatherResponse {
        return WeatherResponse(
            base = "stations",
            clouds = Clouds(all = 40),
            cod = 200,
            coord = Coord(lat=40.4165, lon=-3.7026),
            dt=1708096552,
            id=3117735,
            main= Main(humidity=53, pressure=1023, temp=288.05, temp_max=288.83, temp_min=286.49),
            name= "Madrid",
            sys= Sys(country = "ES", id=2007545, message=0.0, sunrise=1708067287, sunset=1708105799, type=2),
            visibility=10000,
            weather = listOf(
                Weather(
                    description="scattered clouds",
                    icon="03d",
                    id=802,
                    main="Clouds"
                )
            ),
            wind = Wind(deg=290, speed=3.09)
        )
    }
}
