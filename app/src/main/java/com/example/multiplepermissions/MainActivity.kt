package com.example.multiplepermissions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

const val MA = "MainActivity"

class MainActivity : AppCompatActivity() {
    private var cameraPermission = Manifest.permission.CAMERA
    private var writePermission = Manifest.permission.WRITE_CONTACTS
    private var permissions: Array<String> = arrayOf(cameraPermission, writePermission)
    private lateinit var launcher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cameraGranted: Int = ContextCompat.checkSelfPermission(this, cameraPermission)
        val writeGranted: Int = ContextCompat.checkSelfPermission(this, writePermission)

        if (cameraGranted == PackageManager.PERMISSION_GRANTED && writeGranted == PackageManager.PERMISSION_GRANTED) {
            Log.d(MA, "permission already granted")
        } else {
            val contract = ActivityResultContracts.RequestMultiplePermissions()
            val callback: Results = Results()
            launcher = registerForActivityResult(contract, callback)
            launcher.launch(permissions)
        }
    }

    inner class Results : ActivityResultCallback <Map<String, Boolean>> {
        override fun onActivityResult(result: Map<String, Boolean>?) {
            if (result != null) {
                for (data in result) {
                    Log.d(MA, "data is: $data")
                }
                if (result.containsValue(false)) {
                    Log.d(MA, "sorry, not all permissions were granted")
                } else {
                    Log.d(MA, "great, all permissions granted by user")
                    // proceed with activity
                }
            }
        }
    }
}