package com.example.beepgeneratorservice

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.*
import kotlinx.coroutines.*

private const val MSG_START_PROCESS = 1
private const val MSG_STOP_PROCESS = 2

class BeepService : Service() {

    private lateinit var coroutineScope: CoroutineScope

    private lateinit var messenger: Messenger

    override fun onDestroy() {
        coroutineScope.cancel()
    }

    override fun onBind(p0: Intent?): IBinder {
        messenger = Messenger(object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    MSG_START_PROCESS -> {
                        coroutineScope = CoroutineScope(Dispatchers.Main)
                        coroutineScope.launch {
                            val toneGenerator = ToneGenerator(AudioManager.STREAM_SYSTEM, 100)
                            while (true) {
                                toneGenerator.startTone(ToneGenerator.TONE_CDMA_PIP, 100)
                                delay(10000)
                            }
                        }
                    }
                    MSG_STOP_PROCESS -> {
                        coroutineScope.cancel()
                    }
                }
            }
        })
        return messenger.binder
    }
}