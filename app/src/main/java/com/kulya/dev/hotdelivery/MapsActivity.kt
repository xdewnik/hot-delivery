package com.kulya.dev.hotdelivery

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.activity_maps.*
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.maps.MapboxMap
import android.widget.Toast
import android.support.annotation.NonNull
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.kulya.dev.hotdelivery.R.id.mapView
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.jar.Manifest


class MapsActivity : AppCompatActivity() , PermissionsListener, OnMapReadyCallback, MapboxMap.OnMapClickListener{
//    private var mapView: MapView? = null


    private var mapboxMap: MapboxMap? = null

    private var permissionsManager: PermissionsManager? = null
    private var originLocation: Location? = null
    private var destinationMarker: Marker? = null

    private var originCoord: LatLng? = null
    private var destinationCoord: LatLng? = null

    private var originPosition: Point? = null
    private var destinationPosition: Point? = null
    private var currentRoute: DirectionsRoute? = null
    private var TAG = "DirectionsActivity"
    private var navigationMapRoute: NavigationMapRoute? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        setContentView(R.layout.activity_maps)

//        mapView = findViewById(R.id.mapView)
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync(this)
    }

    override fun onMapReady(mapboxMap: MapboxMap?) {
        this.mapboxMap = mapboxMap
//        enableLocationComponent()
        originLocation = getLastKnownLocation()
        originCoord = LatLng(originLocation!!.latitude, originLocation!!.longitude)


        mapboxMap!!.addMarker(MarkerOptions().setPosition(LatLng(originLocation!!.latitude, originLocation!!.longitude)))
        val position = CameraPosition.Builder()
            .target(LatLng(originLocation!!.latitude, originLocation!!.longitude)) // Sets the new camera position
            .zoom(17.0) // Sets the zoom
            .build() // Creates a CameraPosition from the builder

        mapboxMap.animateCamera(
            CameraUpdateFactory
                .newCameraPosition(position), 7000
        )
        mapboxMap!!.addOnMapClickListener(this)
    }

    override fun onMapClick(point: LatLng) {
        if (destinationMarker != null) {
            mapboxMap!!.removeMarker(destinationMarker!!)
        }
        destinationCoord = point
        destinationMarker = mapboxMap!!.addMarker(
            MarkerOptions()
                .title("Order #1541241").snippet("Dobr 14124 321")
                .position(destinationCoord)
        )


        destinationPosition = Point.fromLngLat(destinationCoord!!.longitude, destinationCoord!!.latitude)
        originPosition = Point.fromLngLat(originCoord!!.longitude, originCoord!!.latitude)
        getRoute(originPosition!!, destinationPosition!!)


    }


    private fun getRoute(origin: Point, destination: Point) {
        NavigationRoute.builder(this)
            .accessToken(Mapbox.getAccessToken()!!)
            .origin(origin)
            .destination(destination)
            .build()
            .getRoute(object : Callback<DirectionsResponse> {
                override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                    // You can get the generic HTTP info about the response
                    Log.d(TAG, "Response code: " + response.code())
                    if (response.body() == null) {
                        Log.e(
                            TAG,
                            "No routes found, make sure you set the right user and access token."
                        )
                        return
                    } else if (response.body()!!.routes().size < 1) {
                        Log.e(TAG, "No routes found")
                        return
                    }

                    currentRoute = response.body()!!.routes()[0]

                    // Draw the route on the map
                    if (navigationMapRoute != null) {
                        navigationMapRoute!!.removeRoute()
                    } else {
                        navigationMapRoute = NavigationMapRoute(null, mapView, mapboxMap!!, R.style.NavigationMapRoute)
                    }
                    navigationMapRoute!!.addRoute(currentRoute)
                }

                override fun onFailure(call: Call<DirectionsResponse>, throwable: Throwable) {
                    Log.e(TAG, "Error: " + throwable.message)
                }
            })
    }

    private fun getLastKnownLocation():Location?{
        var mLocationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var providers = mLocationManager.getProviders(true)
        var bestLocation : Location? = null
        providers.forEach {
            checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, 0, 0)
            var l: Location? = mLocationManager.getLastKnownLocation(it)
            if(l != null && (bestLocation == null || l.accuracy < bestLocation!!.accuracy)){
                bestLocation = l
            }
        }

        return bestLocation
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Activate the MapboxMap LocationComponent to show user location
            // Adding in LocationComponentOptions is also an optional parameter
            val locationComponent = mapboxMap!!.locationComponent
            locationComponent.activateLocationComponent(this)
            locationComponent.isLocationComponentEnabled = true
            // Set the component's camera mode
            locationComponent.cameraMode = CameraMode.TRACKING
            originLocation = locationComponent.lastKnownLocation
            Log.d("hui", "hui")

        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager!!.requestLocationPermissions(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionsManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent()
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    public override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    public override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    public override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    public override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }
}
