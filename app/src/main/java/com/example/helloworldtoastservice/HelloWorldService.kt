package com.example.helloworldtoastservice

import android.app.Service
import android.content.Intent
import android.os.*
import android.widget.Toast
import kotlinx.coroutines.*

private const val MSG_START_PROCESS = 1
private const val MSG_STOP_PROCESS = 2

class HelloWorldService : Service() {

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
                            var count = 1
                            while (true) {
                                Toast.makeText(
                                    this@HelloWorldService.applicationContext,
                                    "Hello World! #$count",
                                    Toast.LENGTH_SHORT
                                ).show()
                                count += 1
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