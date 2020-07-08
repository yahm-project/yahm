package it.unibo.yahm.client.entities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import it.unibo.pslab.jaca_android.core.ServiceArtifact
import it.unibo.yahm.client.sensors.GpsLocation
import java.util.*


class GPSArtifact : ServiceArtifact() {

    private var gpsListener: LocationListener? = null
    private var networkListener: LocationListener? = null
    private val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun init() {
        val minDistance: Float = 0.0f
        val minTime: Long = 100
        Log.d("GPS", "init")
        val listenerCreator = {
            object : LocationListener {
                override fun onLocationChanged(location: Location?) {
                    if (location != null) {
                        Log.d("GPS", location.toString())
                        beginExternalSession()
                        updateObsProperty("gpsInfo", Optional.of(
                            GpsLocation(location.latitude,
                                location.longitude,
                                location.accuracy,
                                location.speed,
                                System.currentTimeMillis())
                        ))
                        endExternalSession(true)
                    }
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                    // Nope
                }

                override fun onProviderEnabled(provider: String?) {
                    // Nope
                }

                override fun onProviderDisabled(provider: String?) {
                    // Nope
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            throw IllegalAccessError()
        }
        gpsListener = listenerCreator()
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener!!, Looper.getMainLooper())
        networkListener = listenerCreator()
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, networkListener!!, Looper.getMainLooper())
        defineObsProperty("gpsInfo", Optional.empty<String>())
    }

    override fun dispose() {
        if (gpsListener != null) {
            locationManager.removeUpdates(gpsListener!!)
            gpsListener = null
        }
        if (networkListener != null) {
            locationManager.removeUpdates(networkListener!!)
            networkListener = null
        }
    }

}