package com.example.weatherapp.view


import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var GET:SharedPreferences
    private lateinit var SET: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET= GET.edit()

        viewModel= ViewModelProvider(this).get(MainViewModel::class.java)

        var cName= GET.getString("cityName","jamshedpur")
        edt_city_name.setText(cName)

        viewModel.refreshData(cName!!)

        getLiveData()

        swipe_refresh_layout.setOnRefreshListener {
            ll_data.visibility= View.GONE
            tv_error.visibility= View.GONE
            progressBar.visibility= View.GONE

            var cityName= GET.getString("cityName", cName)
            edt_city_name.setText(cityName)
            viewModel.refreshData(cityName!!)
            swipe_refresh_layout.isRefreshing=false
        }

        img_search_city.setOnClickListener{
            val cityName= edt_city_name.text.toString()
            SET.apply()
            viewModel.refreshData(cityName)
            getLiveData()
        }

    }

    private fun getLiveData() {
       viewModel.weather_data.observe(this, Observer { data ->
           data?.let {
               ll_data.visibility= View.VISIBLE
               progressBar.visibility= View.GONE
               tv_degree.text= data.main.temp.toString() + " C"
               tv_city_code.text= data.sys.country.toString()
               tv_city_name.text=data.name.toString()
               tv_humidity.text=data.main.humidity.toString()
               tv_wind_speed.text=data.wind.speed.toString() +"%"
               tv_lat.text=data.coord.lat.toString()
               tv_lon.text=data.coord.lon.toString()

               Glide.with(this).load("http://openweathermap.org/img/wn/"+ data.weather[0].icon+"@2x.png")
                   .into(img_weather_pictures)

           }
       })

        viewModel.weather_load.observe(this, Observer { load ->
            load?.let {
                if(it){
                    progressBar.visibility= View.VISIBLE
                    tv_error.visibility=View.GONE
                    ll_data.visibility= View.GONE
                }else{
                    progressBar.visibility=View.GONE
                }
            }
        })

        viewModel.weather_error.observe(this, Observer { error ->
            error?.let {
                if (it){
                    tv_error.visibility=View.VISIBLE
                    ll_data.visibility=View.GONE
                    progressBar.visibility=View.GONE
                } else{
                    tv_error.visibility= View.GONE
                }
            }
        })
    }
}