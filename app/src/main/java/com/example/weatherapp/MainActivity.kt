package com.example.weatherapp


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.models.CityResponse
import com.example.weatherapp.network.WeatherService
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {


    private lateinit var mFusedLocationClient: FusedLocationProviderClient

        lateinit var temp : String
        private var mProgressDialog:Dialog? = null

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         val btnselectCity :Button= findViewById(R.id.btnselectCity)
         btnselectCity.setOnClickListener {
             val tvtemp :TextView = findViewById(R.id.tv_temp)
             val usedtemp =temp

             Intent(this,MainActivity2::class.java).also {
                 it.putExtra("EXTRA_TEMP",usedtemp)
                 startActivity(it)
             }
         }


         val spCities: Spinner = findViewById(R.id.spCities)


         val customList = listOf("Ado-Ekiti","Lagos","PortHarcourt","Ikeja","Effon-Alaiye","Abuja","Benin ","Ibadan","Enugu","Toronto")
         val adapter  = ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,customList)
         spCities.adapter = adapter
         spCities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
             override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id : Long) {

                 Toast.makeText(this@MainActivity, "you selected "+customList[position], Toast.LENGTH_SHORT).show()
                 if (customList[position] == "Lagos"){
                     //val mLastLocation: Location = locationResult.lastLocation
                     val latitude = 6.5833
                     Log.i("Current Latitude", "$latitude")
                     val longitude = 3.75
                     Log.i("Current Longitude", "$longitude")
                     getLocationWeatherDetails(latitude, longitude)

                 }else if (customList[position] == "PortHarcourt"){
                     val latitude = 4.7676576
                     Log.i("Current Latitude", "$latitude")
                     val longitude = 7.0188527
                     Log.i("Current Longitude", "$longitude")
                     getLocationWeatherDetails(latitude, longitude)
                 }else if (customList[position] == "Ikeja"){
                     val latitude = 6.5960605
                     Log.i("Current Latitude", "$latitude")
                     val longitude = 3.340787
                     Log.i("Current Longitude", "$longitude")
                     getLocationWeatherDetails(latitude, longitude)
                 }else if (customList[position] == "Effon-Alaiye"){
                     val latitude =  7.6492989
                     Log.i("Current Latitude", "$latitude")
                     val longitude = 4.9196673
                     Log.i("Current Longitude", "$longitude")
                     getLocationWeatherDetails(latitude, longitude)
                 }else if (customList[position] == "Abuja"){
                     val latitude = 9.0643305
                     Log.i("Current Latitude", "$latitude")
                     val longitude = 7.4892974
                     Log.i("Current Longitude", "$longitude")
                     getLocationWeatherDetails(latitude, longitude)
                 }
                 else if (customList[position] == "Benin"){
                     val latitude = 6.3330586
                     Log.i("Current Latitude", "$latitude")
                     val longitude = 5.6221058
                     Log.i("Current Longitude", "$longitude")
                     getLocationWeatherDetails(latitude, longitude)
                 }else if (customList[position] == "Ibadan"){
                     val latitude = 7.3777462
                     Log.i("Current Latitude", "$latitude")
                     val longitude = 3.8972497
                     Log.i("Current Longitude", "$longitude")
                     getLocationWeatherDetails(latitude, longitude)
                 }
                 else if (customList[position] == "Enugu"){
                     val latitude =  6.4499833
                     Log.i("Current Latitude", "$latitude")
                     val longitude = 7.5000007
                     Log.i("Current Longitude", "$longitude")
                     getLocationWeatherDetails(latitude, longitude)
                 }else if (customList[position] == "Toronto"){
                     val latitude =  -71.2145
                     Log.i("Current Latitude", "$latitude")
                     val longitude = 46.8123
                     Log.i("Current Longitude", "$longitude")
                     getLocationWeatherDetails(latitude, longitude)
                 }
                 else{
                     //val mLastLocation: Location = locationResult.lastLocation
                     val latitude = 7.621
                     Log.i("Current Latitude", "$latitude")
                     val longitude = 5.2215
                     Log.i("Current Longitude", "$longitude")
                     getLocationWeatherDetails(latitude, longitude)
                     hideProgressDialog()
                 }
             }


             override fun onNothingSelected(p0: AdapterView<*>?) {
                 TODO("Not yet implemented")
             }
         }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        if (!isLocationEnabled()) {
            Toast.makeText(
                this,
                "Your location provider is turned off. Please turn it on.",
                Toast.LENGTH_SHORT
            ).show()


            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else {

            Dexter.withActivity(this)
                .withPermissions(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        if (report!!.areAllPermissionsGranted()) {
                            requestLocationData()
                        }

                        if (report.isAnyPermissionPermanentlyDenied) {
                            Toast.makeText(
                                this@MainActivity,
                                "You have denied location permission. Please allow it is mandatory.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        showRationalDialogForPermissions()
                    }
                }).onSameThread()
                .check()
            // END
        }
    }

    private fun isLocationEnabled(): Boolean {

        // This provides access to the system location services.
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog,
                                           _ ->
                dialog.dismiss()
            }.show()
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationData() {

        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        Looper.myLooper()?.let {
            mFusedLocationClient.requestLocationUpdates(
                mLocationRequest,mLocationCallback,
                it
            )
        }
    }
    private val mLocationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult) {



            val spCities: Spinner = findViewById(R.id.spCities)



            if (spCities.equals("lagos")){
                //val mLastLocation: Location = locationResult.lastLocation
                val latitude = 6.5833
                Log.i("Current Latitude", "$latitude")
                val longitude = 3.75
                Log.i("Current Longitude", "$longitude")
                getLocationWeatherDetails(latitude, longitude)

            }else{
                //val mLastLocation: Location = locationResult.lastLocation
                val latitude = 7.621
                Log.i("Current Latitude", "$latitude")
                val longitude = 5.2215
                Log.i("Current Longitude", "$longitude")
                getLocationWeatherDetails(latitude, longitude)
            }
        }
    }



   /* private fun getLocationWeatherDetails(city: String){
        if (Constants.isNetworkAvailable(this)){
            val retrofit:Retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service: WeatherService = retrofit.create<WeatherService>(WeatherService::class.java)
            val listCall: Call<CityResponse> = service.getLongitudeLatitude(
                city,Constants.APP_ID
            )

            showCustomProgressDialog()

            listCall.enqueue(object :Callback<CityResponse>{
                override fun onResponse(
                    call: Call<CityResponse>,
                    response: Response<CityResponse>
                ) {
                    if (response!!.isSuccessful){

                        hideProgressDialog()
                        val  cityList : CityResponse? = response.body()
                        Log.i("Response result ","$cityList")

                        // Call weather api using longitude and latitude
                        if (cityList != null) {
                            getLocationWeatherDetails(cityList.lat, cityList.lon)
                        }
                    }else{
                        val rc =response.code()
                        when (rc){
                            400 ->{
                                Log.e("error 400","Bad connection")
                            }
                            404 ->{
                                Log.e("Error 404 ","coonnection not found ")
                            }else ->{
                            Log.e("error", "Generic Error")
                        }
                        }
                    }
                }
                override fun onFailure(call: Call<CityResponse>, t: Throwable) {
                    Log.e("Errorrrr ",t!!.message.toString())
                    hideProgressDialog()
                }

            })

        }else{
            Toast.makeText(
                this@MainActivity,
                "No internet connection.Please turn it on ",
                Toast.LENGTH_LONG
            ).show()
        }

    }
*/

    private fun getLocationWeatherDetails(latitude:Double,longitude:Double){
        if (Constants.isNetworkAvailable(this@MainActivity)){
            val retrofit:Retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service: WeatherService = retrofit
                .create<WeatherService>(WeatherService::class.java)


            val listCall: Call<WeatherResponse> = service.getWeather(
                latitude,longitude, Constants.METRIC_UNIT,Constants.APP_ID
            )

            showCustomProgressDialog()
            listCall.enqueue(object : Callback<WeatherResponse>{
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if(response.isSuccessful){

                        hideProgressDialog()
                        val weatherList: WeatherResponse? = response.body()
                        if (weatherList != null) {
                            setupUI(weatherList)
                        }
                        Log.i("Response result","$weatherList")
                    }else{
                        val rc = response.code()
                        when(rc){
                            400 ->{
                                Log.e("Error 400","Bad connection")
                            }
                            404 ->{
                                Log.e("Error 404","Not Found ")
                            }else -> {
                                Log.e("Error","generic Error")
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.e("Errorrr",t!!.message.toString())

                }

            })



        }else{
            Toast.makeText(
                this@MainActivity,
                "No internet connection.Please turn it on ",
                Toast.LENGTH_LONG
            ).show()
        }
    }



    private fun showCustomProgressDialog(){
        mProgressDialog = Dialog(this)

        mProgressDialog!!.setContentView(R.layout.dialog_custom_progress)

        mProgressDialog!!.show()

    }

    private fun hideProgressDialog(){
        if (mProgressDialog!= null ){
            mProgressDialog!!.dismiss()
        }
    }
    private fun setupUI(weatherList: WeatherResponse){
        for(i in weatherList.weather.indices){
            Log.i("Weather Name ",weatherList.weather.toString())

            temp = weatherList.main.temp.toString()
            val tvMain :TextView = findViewById(R.id.tv_main)
            val tvMainDescription :TextView = findViewById(R.id.tv_main_description)
            val tvTemp :TextView = findViewById(R.id.tv_temp)
            val tvSunRiseTime :TextView = findViewById(R.id.tv_sunrise_time)
            val tvSunSetTime :TextView = findViewById(R.id.tv_sunset_time)
            val tvHumidity : TextView = findViewById(R.id.tv_humidity)
            val tvMin : TextView = findViewById(R.id.tv_min)
            val tvMax : TextView = findViewById(R.id.tv_max)
            val tvSpeed : TextView = findViewById(R.id.tv_speed)
            val tvName : TextView = findViewById(R.id.tv_name)
            val tvCountry : TextView = findViewById(R.id.tv_country)



            tvMain.text = weatherList.weather[i].main
            tvMainDescription.text = weatherList.weather[i].description
            tvTemp.text = weatherList.main.temp.toString() + getUnit(application.resources.configuration.toString())
            tvSunRiseTime.text = unixTime(weatherList.sys.sunrise)
            tvSunSetTime.text = unixTime(weatherList.sys.sunset)

            tvHumidity.text = weatherList.main.humidity.toString() +"per cent "
            tvMin.text = weatherList.main.temp_min.toString() + "min"
            tvMax.text = weatherList.main.temp_max.toString() + "max"
            tvSpeed.text = weatherList.wind.speed.toString()
            tvName.text = weatherList.name
            tvCountry.text = weatherList.sys.country

        }

    }


    private fun getUnit(value:String): String {
        var value = "°C"
        if ("US" == value || "LR" == value|| "MM" ==value){
            value = "℉"
        }
        return value
    }


    private fun unixTime(timex:Long):String?{
        val date = Date(timex *1000L)
        val sdf = SimpleDateFormat("HH:mm" ,Locale.UK)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

}
