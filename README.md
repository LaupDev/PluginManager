# PluginManager

This is an interesting project. The goal was to learn how to use Android services.
There are three branches:
* master - plugin manager that controls different Android services. It can send commans to services in order to start or stop them
* master_2 - Android service that shows Toast every 10 seconds if turned on
* master-3 - Android service that makes "beep" every 10 seconds sound if turned on

**The main feature is — if you want to add a new plugin, you don't need to change anything in Plugin Manager code or manually add new plugins(services). It searches for it automatically using intent filters**
