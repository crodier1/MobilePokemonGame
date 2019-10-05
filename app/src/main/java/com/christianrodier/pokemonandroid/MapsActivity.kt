package com.christianrodier.pokemonandroid

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermission()
    }

    var ACCESSLOCATION  = 123

    fun checkPermission(){
        if(Build.VERSION.SDK_INT >= 23){
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), ACCESSLOCATION)
                return
            }
        }

        GetUserLocation()
        loadListOfPokemon()
    }

    fun GetUserLocation(){
        // TODO implement this
        Toast.makeText(this, "User Location Accessed", Toast.LENGTH_LONG).show()

        var myLocation = MyLocationListener()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //when you move or after time passes you will request user location
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocation)

        var mythread = mythread()
        mythread.start()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            ACCESSLOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    GetUserLocation()
                } else {

                    Toast.makeText(this, "App need your permission to access location", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions()
            .position(sydney)
            .title("Me")
            .snippet(" here is my location")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14f))
    }

    //get user location
    var location: Location? = null
    inner class MyLocationListener:LocationListener{


        constructor(){
            location = Location("Start")
            location!!.longitude = 0.0
            location!!.latitude = 0.0
        }

        override fun onLocationChanged(p0: Location?) {
            location = p0
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

        }

        override fun onProviderEnabled(p0: String?) {

        }

        override fun onProviderDisabled(p0: String?) {

        }

    }

    var oldLocation: Location? = null

    inner class mythread:Thread{
        constructor():super(){
            oldLocation = Location("Start")
            oldLocation!!.longitude = 0.0
            oldLocation!!.latitude = 0.0

        }

        override fun  run(){
            while (true){
                try {
                    if(oldLocation!!.distanceTo(location) == 0f){

                        continue
                    }

                    oldLocation = location

                    runOnUiThread {
                        mMap!!.clear()

                        //show me on map

                        val sydney = LatLng(location!!.latitude,  location!!.longitude)
                        mMap.addMarker(MarkerOptions()
                            .position(sydney)
                            .title("Me")
                            .snippet(" here is my location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14f))

                        //show pokemon on map

                        for (i in listOfPokemon){
                            var newPokemon = i

                            if (!i.wasCaught!!){

                                val pokemonLocation = LatLng(i.location!!.latitude,  i.location!!.longitude)
                                mMap.addMarker(MarkerOptions()
                                    .position(pokemonLocation)
                                    .title(i.name)
                                    .snippet("${i.description} Power: ${i.power}")
                                    .icon(BitmapDescriptorFactory.fromResource(i.image!!)))

                                if (location!!.distanceTo(i.location)< 2){
                                    i.wasCaught = true
                                    playerPower += i.power!!

                                    Toast.makeText(applicationContext, "${i.name} caught! Your power is now $playerPower", Toast.LENGTH_LONG).show()

                                }


                            }
                        }
                    }

                    Thread.sleep(1000)



                }catch (ex: Exception){

                }
            }
        }
    }

    var playerPower = 0.0
    var listOfPokemon = ArrayList<Pokemon>()

    fun loadListOfPokemon (){

        listOfPokemon.add(Pokemon(name = "Charmander", description = "Fire", power = 3.0,image = R.drawable.charmander, latitude = 37.77, longitude = -122.40 ))
        listOfPokemon.add(Pokemon(name = "Bulbasaur", description = "Plant", power = 1.0,image = R.drawable.bulbasaur, latitude = 37.79, longitude = -122.41))
        listOfPokemon.add(Pokemon(name = "Squirtle", description = "Water", power = 2.0,image = R.drawable.squirtle, latitude = 37.80, longitude = -122.42 ))


    }
}
