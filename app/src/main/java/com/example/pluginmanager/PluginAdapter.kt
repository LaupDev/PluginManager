package com.example.pluginmanager

import android.os.Message
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pluginmanager.databinding.ItemPluginBinding

class PluginAdapter(private val plugins: List<Plugin>) :
    RecyclerView.Adapter<PluginAdapter.PluginViewHolder>() {

    class PluginViewHolder(private val binding: ItemPluginBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(plugin: Plugin) {
            binding.pluginNameText.text = plugin.name
            binding.enableSwitch.isChecked = plugin.isEnabled
            binding.enableSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (plugin.isBound) {
                    plugin.isEnabled = isChecked
                    val message = if (isChecked) {
                        Message.obtain(null, MSG_START_PROCESS, 0, 0)
                    } else {
                        Message.obtain(null, MSG_STOP_PROCESS, 0, 0)
                    }
                    plugin.messenger.send(message)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PluginViewHolder {
        val binding = ItemPluginBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PluginViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PluginViewHolder, position: Int) {
        holder.bind(plugins[position])
    }

    override fun getItemCount() = plugins.size
}