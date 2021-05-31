package de.lasergame.wear_spielwiese

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.messaging.FirebaseMessaging
import de.lasergame.wear_spielwiese.databinding.ActivityMainBinding

class MainActivity : Activity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)

        Log.d(TAG, "app started")

        if (hasGps()) {
            Log.d(TAG,"gps available")
        } else {
            Log.d(TAG, "no gps")
        }

        if (resultCode == ConnectionResult.SUCCESS) {
            Log.d(TAG, "play available")
        } else {
            Log.d(TAG,"not successful, code: $resultCode")
        }
        
        Log.d(TAG, "trying to get firebase token")

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result
            Log.d(TAG, "received token: $token")
        }
    }

    private fun hasGps(): Boolean = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)
}