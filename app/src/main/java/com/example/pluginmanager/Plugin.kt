package com.example.pluginmanager

import android.os.Messenger

data class Plugin(
    val name: String,
    val isBound: Boolean,
    val messenger: Messenger,
    var isEnabled: Boolean = false
)
