package com.example.detectvolumeinsideservice

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var settingsContentObserver: SettingsContentObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Endless Service"
        settingsContentObserver = SettingsContentObserver(this, Handler())

        applicationContext.contentResolver.registerContentObserver(
            Settings.System.CONTENT_URI,
            true,
            settingsContentObserver
        )

        findViewById<Button>(R.id.btnStartService).let {
            it.setOnClickListener {
                log("START THE FOREGROUND SERVICE ON DEMAND")
                actionOnService(Actions.START)
            }
        }

        findViewById<Button>(R.id.btnStopService).let {
            it.setOnClickListener {
                log("STOP THE FOREGROUND SERVICE ON DEMAND")
                actionOnService(Actions.STOP)
            }
        }
    }

    private fun actionOnService(action: Actions) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return
        Intent(this, EndlessService::class.java).also {
            it.action = action.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                log("Starting the service in >=26 Mode")
                startForegroundService(it)
                return
            }
            log("Starting the service in < 26 Mode")
            startService(it)
        }
    }
}
