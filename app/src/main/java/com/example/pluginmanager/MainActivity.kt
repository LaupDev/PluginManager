package com.example.pluginmanager

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Messenger
import android.util.Log
import com.example.pluginmanager.databinding.ActivityMainBinding

const val MSG_START_PROCESS = 1
const val MSG_STOP_PROCESS = 2

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: PluginAdapter

    private val plugins = mutableListOf<Plugin>()

    private val connection = object : ServiceConnection {
        @SuppressLint("NotifyDataSetChanged")
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            Log.d("TEST", "SUCCESS: $className")
            plugins.add(
                Plugin(
                    name = className.shortClassName.drop(1),
                    isBound = true,
                    messenger = Messenger(service)
                )
            )
            adapter.notifyDataSetChanged()
        }
        override fun onServiceDisconnected(className: ComponentName) {
            plugins.removeIf {
                it.name == className.shortClassName
            }
            val intent = Intent().setComponent(ComponentName(className.packageName, className.className))
            bindService(intent, this, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = PluginAdapter(plugins)
        binding.pluginListRecyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        findAndBindPlugins()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }

    private fun findAndBindPlugins() {
        val oldPlugins = plugins.toList()
        plugins.clear()
        val result = packageManager.queryIntentServices(Intent("intent.custom.PLUGIN"), PackageManager.MATCH_ALL)
        Log.d("TEST", "RESULT: $result")
        result.forEach {
            val samePlugin = oldPlugins.find { plugin ->
                plugin.name == it.serviceInfo.name.split(".").last()
            }
            if (samePlugin != null) {
                plugins.add(samePlugin)
            } else {
                val intent = Intent().setComponent(ComponentName(it.serviceInfo.packageName, it.serviceInfo.name))
                bindService(intent, connection, Context.BIND_AUTO_CREATE)
            }
        }
        adapter.notifyDataSetChanged()
    }
}