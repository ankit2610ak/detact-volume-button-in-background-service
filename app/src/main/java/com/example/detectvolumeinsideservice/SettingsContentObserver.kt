package com.example.detectvolumeinsideservice

import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.os.Handler
import java.util.*


class SettingsContentObserver internal constructor(c: Context, handler: Handler?) :
    ContentObserver(handler) {
    var previousVolume: Int
    var context: Context = c

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        val audio = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val currentVolume: Int =
            Objects.requireNonNull(audio).getStreamVolume(AudioManager.STREAM_MUSIC)
        val delta = previousVolume - currentVolume
        if (delta > 0) {
            log("Volume Decreased")
            previousVolume = currentVolume
        } else if (delta < 0) {
            log("Volume Increased")
            previousVolume = currentVolume
        }
    }

    init {
        val audio = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        previousVolume = Objects.requireNonNull(audio).getStreamVolume(AudioManager.STREAM_MUSIC)
    }
}